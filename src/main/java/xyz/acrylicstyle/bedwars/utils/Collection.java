package xyz.acrylicstyle.bedwars.utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class Collection<K, V> extends HashMap<K, V> {
    @SuppressWarnings("unchecked")
    public V first() {
        return (V) this.values().toArray()[0];
    }

    @SuppressWarnings("unchecked")
    public K[] keys() {
        return (K[]) this.keySet().toArray();
    }

    /**
     * @param action it passes value, index.
     */
    public void foreach(BiConsumer<V, Integer> action) {
        final int[] index = {0};
        this.values().forEach(v -> {
            index[0]++;
            action.accept(v, index[0]);
        });
    }

    public V add(K key, V value) {
        return super.put(key, value);
    }

    public void addAll(Map<? extends K, ? extends V> map) {
        super.putAll(map);
    }

    /**
     * Filters values. If returned true, that value will be kept. Returns new Collection of filtered values.
     * @param filter filter function.
     */
    public Collection<K, V> filter(Function<V, Boolean> filter) {
        Collection<K, V> newList = new Collection<>();
        K[] keys = this.keys();
        this.foreach((v, i) -> {
            if (filter.apply(v)) newList.put(keys[i], v);
        });
        return newList;
    }
}
