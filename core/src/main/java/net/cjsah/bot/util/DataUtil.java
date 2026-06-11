package net.cjsah.bot.util;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
import net.cjsah.bot.data.IRowMapper;
import net.cjsah.bot.data.ITableMapper;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class DataUtil {
    public static <T extends IRowMapper> Map<Long, T> makeMap(Collection<T> nodes) {
        Map<Long, T> map = new Long2ObjectLinkedOpenHashMap<>();

        for (T node : nodes) {
            map.put(node.getRowKey(), node);
        }

        return map;
    }

    public static <T extends ITableMapper> Table<Long, Long, T> makeTable(Collection<T> nodes) {
        Table<Long, Long, T> table = HashBasedTable.create();

        for (T node : nodes) {
            table.put(node.getRowKey(), node.getColumnKey(), node);
        }

        return table;
    }

    public static <T> List<T> unpack(Map<?, T> map) {
        return unpack(map.values());
    }

    public static <T> List<T> unpack(Table<?, ?, T> table) {
        return unpack(table.values());
    }

    public static <T> List<T> unpack(Collection<T> collection) {
        return collection.stream().toList();
    }
}
