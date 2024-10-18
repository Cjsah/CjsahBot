package net.cjsah.bot.event;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.plugin.PluginContext;
import net.cjsah.bot.plugin.PluginInfo;
import net.cjsah.bot.plugin.PluginThreadPools;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public final class EventManager {
    private static final Logger log = LoggerFactory.getLogger("EventManager");

    private static final List<EventNode<?>> events = new ArrayList<>();

    /**
     * 订阅给定类型的事件。
     * <p>
     * 该方法允许插件上下文中的当前插件订阅特定类型的事件，使用提供的事件处理程序。
     *
     * @param <T> 继承自Event的事件类型，表示要订阅的事件类型。
     * @param clazz 要订阅的事件的具体类。
     * @param handler 用于处理事件的消费者，定义了如何处理事件。
     */
    public static <T extends Event> void subscribe(Class<T> clazz, Consumer<T> handler) {
        // 获取当前插件上下文中的插件
        PluginInfo info = PluginContext.getCurrentPluginInfo();
        // 使用获取到的插件和事件处理程序进行订阅
        subscribe(info.getId(), clazz, handler);
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
    public static <T extends Event> void register(@NotNull Object object) {
        // 遍历对象的所有方法
        for (Method method : object.getClass().getMethods()) {
            // 忽略参数数量不为1的方法
            if (method.getParameterCount() != 1) continue;
            // 获取方法上的SubscribeEvent注解
            SubscribeEvent annotation = method.getAnnotation(SubscribeEvent.class);
            // 忽略没有SubscribeEvent注解的方法
            if (null == annotation) continue;
            // 获取方法的参数类型
            Class<?> parameterTypes = method.getParameterTypes()[0];
            // 忽略参数类型不是Event或其子类的方法
            if (!Event.class.isAssignableFrom(parameterTypes)) continue;
            // 创建一个事件触发器，用于在事件发生时调用相应的方法
            Consumer<?> trigger = (obj) -> {
                try {
                    // 调用对象的方法，传递事件对象作为参数
                    method.invoke(object, obj);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    // 在调用方法失败时记录错误日志
                    log.error(e.getMessage(), e);
                }
            };
            // 使用反射获取方法参数的类型，并将其和触发器一起注册到事件管理器中
            EventManager.subscribe((Class<T>) parameterTypes, (Consumer<T>) trigger);
        }
    }

    /**
     * 订阅事件
     * <p>
     * 该方法允许插件订阅特定类型的事件，当事件发生时，指定的事件处理程序会被调用
     *
     * @param pluginId  订阅事件的插件ID
     * @param clazz     被订阅事件的类类型
     * @param handler   事件发生时调用的事件处理程序，接受事件类型的实例作为参数
     */
    public static <T extends Event> void subscribe(String pluginId, Class<T> clazz, Consumer<T> handler) {
        events.add(new EventNode<>(pluginId, clazz, handler));
    }


    /**
     * 取消插件订阅
     * <p>
     * 当一个插件不再需要接收事件通知时，可以通过此方法取消其订阅，从而提高系统的性能和资源利用率
     *
     * @param pluginId 要取消订阅的插件ID
     */
    public static void unsubscribe(String pluginId) {
        // 移除所有属于指定插件的事件监听器
        events.removeIf(it -> Objects.equals(it.pluginId, pluginId));
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
    public static <T extends Event> void unsubscribe(Class<T> event) {
        PluginInfo info = PluginContext.getCurrentPluginInfo();
        events.removeIf(it -> Objects.equals(it.pluginId, info.getId()) && it.event == event);
    }


    /**
     * 广播事件
     *
     * @param <T> 继承自Event的事件类型
     * @param event 要广播的事件对象，如果为null，则不执行任何操作
     */
    @SuppressWarnings("unchecked")
    public static <T extends Event> void broadcast(@Nullable T event) {
        // 检查事件对象是否为null
        if (event == null) return;

        // 使用并行流过滤并执行匹配的事件处理函数
        events.stream().parallel().filter(it -> it.event.isAssignableFrom(event.getClass())).forEach(it -> {
            // 使用插件的线程池执行事件处理函数
            PluginThreadPools.execute(it.pluginId, () -> {
                try {
                    // 动态类型转换并调用事件处理函数
                    ((Consumer<T>) it.handler).accept(event);
                } catch (Exception e) {
                    // 记录异常信息
                    log.error("Error while handling event", e);
                }
            });
        });
    }

    public static void parseEvent(JSONObject raw) {
        int type = raw.getIntValue("type");
        EventType eventType = EventType.getByType(type);
        if (eventType == null) {
            log.warn("Unknown event type: {}, {}", type, raw);
            return;
        }
        JSONObject data = raw.getJSONObject("data");
        Event event = eventType.getFactory().apply(data);
        EventManager.broadcast(event);
    }


    record EventNode<T extends Event>(String pluginId, Class<T> event, Consumer<T> handler) {
    }

}
