package net.cjsah.bot.event;

import com.google.gson.JsonElement;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import lombok.AccessLevel;
import lombok.extern.slf4j.Slf4j;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import net.cjsah.bot.data.IEventBuilder;
import net.cjsah.bot.event.events.CancelableEvent;
import net.cjsah.bot.event.events.ReceivedEvent;
import net.cjsah.bot.event.events.Event;
import net.cjsah.bot.data.OB11BaseInfo;
import net.cjsah.bot.exception.EventException;
import net.cjsah.bot.packet.PacketHandler;
import net.cjsah.bot.packet.response.ResponseBuilder;
import net.cjsah.bot.plugin.PluginManager;
import net.cjsah.bot.util.CodecUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.function.Function;

@Slf4j(topic = "EventManager", access = AccessLevel.PUBLIC)
public final class EventManager {
    private static final EventManager INSTANCE = new EventManager();
    private static final AtomicLong ID_CACHE = new AtomicLong(0);
    private final Map<Long, EventNode<?>> events = new Long2ObjectOpenHashMap<>();

    public static EventManager getInstance() {
        return INSTANCE;
    }

    /**
     * 注册事件监听器
     * <p>
     * 本方法通过反射机制，自动检索并注册对象中所有标记为事件处理方法的方法
     * 被注册的方法应当接受一个Event的实例作为参数
     *
     * @param object 要注册事件监听器的对象，该对象的方法将被检索和注册
     * @param <T>    泛型参数，表示事件的类型，必须是Event的子类
     */
    @SuppressWarnings("unchecked")
    public <T extends Event> List<Long> register(String pluginId, @NotNull Object object) {
        Method[] methods = object.getClass().getMethods();
        List<Long> ids = new ArrayList<>(methods.length);
        // 遍历对象的所有方法
        for (Method method : methods) {
            // 忽略参数数量不为1的方法
            if (method.getParameterCount() != 1) continue;
            // 获取方法上的SubscribeEvent注解
            SubscribeEvent annotation = method.getAnnotation(SubscribeEvent.class);
            // 忽略没有SubscribeEvent注解的方法
            if (null == annotation) continue;
            // 获取方法的参数类型
            Class<?> eventType = method.getParameterTypes()[0];
            // 忽略参数类型不是Event或其子类的方法
            if (!Event.class.isAssignableFrom(eventType)) continue;
            // 创建一个事件触发器，用于在事件发生时调用相应的方法
            Consumer<T> trigger = (obj) -> {
                try {
                    // 调用对象的方法，传递事件对象作为参数
                    method.invoke(object, obj);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    // 在调用方法失败时记录错误日志
                    log.error(e.getMessage(), e);
                }
            };
            // 使用反射获取方法参数的类型，并将其和触发器一起注册到事件管理器中
            ids.add(this.subscribe(pluginId, (Class<T>) eventType, trigger));
        }
        return List.copyOf(ids);
    }

    /**
     * 订阅事件
     * <p>
     * 该方法允许插件订阅特定类型的事件，当事件发生时，指定的事件处理程序会被调用
     *
     * @param pluginId 订阅事件的插件ID
     * @param clazz    被订阅事件的类类型
     * @param handler  事件发生时调用的事件处理程序，接受事件类型的实例作为参数
     */
    public <T extends Event> long subscribe(String pluginId, Class<T> clazz, Consumer<T> handler) {
        long id = ID_CACHE.incrementAndGet();
        this.events.put(id, new EventNode<>(id, pluginId, clazz, handler));
        return id;
    }

    public boolean unsubscribe(long eventId) {
        return this.events.remove(eventId) != null;
    }

    /**
     * 取消订阅指定类型的事件
     * <p>
     * 此方法用于取消当前插件对特定类型事件的订阅。它通过获取当前插件信息和事件类型，
     * 从事件订阅列表中移除所有匹配的事件订阅项。这是为了确保当插件不再需要接收特定事件的通知时，
     * 能够及时解除订阅，避免内存泄漏和无效的事件通知。
     *
     * @param event 事件类型，必须是Event的子类
     */
    public <T extends Event> boolean unsubscribe(String pluginId, Class<T> event) {
        return this.events.values().removeIf(it ->
            Objects.equals(it.pluginId, pluginId) && it.event == event
        );
    }

    /**
     * 取消插件订阅
     * <p>
     * 当一个插件不再需要接收事件通知时，可以通过此方法取消其订阅，从而提高系统的性能和资源利用率
     *
     * @param pluginId 要取消订阅的插件ID
     */
    public boolean unsubscribe(String pluginId) {
        return this.events.values().removeIf(it -> Objects.equals(it.pluginId, pluginId));
    }

    /**
     * 广播事件
     *
     * @param <T>   继承自Event的事件类型
     * @param event 要广播的事件对象，如果为null，则不执行任何操作
     */
    public <T extends Event> T broadcast(@Nullable T event) {
        this.broadcast(event, false);
        return event;
    }

    @SuppressWarnings("unchecked")
    public <T extends Event> T broadcast(@Nullable T event, boolean await) {
        // 检查事件对象是否为null
        if (event == null) return null;
        log.debug("触发事件: {}", event);

        List<EventNode<?>> events = getInstance().events.values()
            .stream()
            .parallel()
            .filter(it -> it.event.isAssignableFrom(event.getClass()))
            .toList();

        CountDownLatch latch = new CountDownLatch(events.size());

        events.stream().parallel().forEach(it -> {
            boolean appended = PluginManager.execute(it.pluginId, () -> {
                try {
                    // 动态类型转换并调用事件处理函数
                    ((Consumer<T>) it.handler).accept(event);
                } catch (Exception e) {
                    // 记录异常信息
                    log.error("Error while handling event", e);
                } finally {
                    latch.countDown();
                }
            });
            if (!appended) {
                latch.countDown();
            }
        });

        if (await || event instanceof CancelableEvent) {
            try {
                latch.await();
            } catch (InterruptedException ignored) {
            }
        }
        return event;
    }

    private static final Codec<Either<OB11BaseInfo, ResponseBuilder>> WS_CODEC = Codec.either(OB11BaseInfo.CODEC, ResponseBuilder.CODEC);

    public void parseWebSocketEvent(long id, JsonElement raw) {
        Function<String, EventException> exception = EventException::new;

        Either<OB11BaseInfo, ResponseBuilder> result = CodecUtil.decode(WS_CODEC, raw, exception).orThrow();

        result.consume(
            event -> this.handleEvent(id, event, raw, exception),
            builder -> PacketHandler.getInstance().receive(builder)
        );
    }

    private void handleEvent(long id, OB11BaseInfo base, JsonElement raw, Function<String, EventException> exception) {
        IEventBuilder eventBuilder = base.getPostType();
        while (true) {
            Object obj = CodecUtil.decode(eventBuilder.codec(), raw, exception).orThrow();
            if (obj instanceof IEventBuilder builder) {
                eventBuilder = builder;
            } else if (obj instanceof ReceivedEvent event) {
                event.init(id, base);
                this.broadcast(event);
                return;
            } else {
                log.warn("Unsupported decoding data: {}", obj);
                return;
            }
        }
    }

    record EventNode<T extends Event>(long id, String pluginId, Class<T> event, Consumer<T> handler) {
    }

}
