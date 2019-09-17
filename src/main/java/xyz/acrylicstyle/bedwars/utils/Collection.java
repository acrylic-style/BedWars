package xyz.acrylicstyle.bedwars.utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class Collection<K, V> extends HashMap<K, V> {
    public V first() {
        return this.valuesArray()[0];
    }

    @SuppressWarnings("unchecked")
    public K[] keys() {
        return (K[]) this.keySet().toArray();
    }

    @SuppressWarnings("unchecked")
    public V[] valuesArray() {
        return (V[]) this.values().toArray();
    }

    /**
     * @param action it passes value, index.
     */
    public void foreach(BiConsumer<V, Integer> action) {
        final int[] index = {0};
        this.values().forEach(v -> {
            action.accept(v, index[0]);
            index[0]++;
        });
    }

    /**
     * @param action it passes key, index.
     */
    public void foreachKeys(BiConsumer<K, Integer> action) {
        final int[] index = {0};
        this.keySet().forEach(k -> {
            action.accept(k, index[0]);
            index[0]++;
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

    public Collection<K, V> filterKeys(Function<K, Boolean> filter) {
        Collection<K, V> newList = new Collection<>();
        V[] values = this.valuesArray();
        this.foreachKeys((k, i) -> {
            if (filter.apply(k)) newList.put(k, values[i]);
        });
        return newList;
    }

    public Collection<K, V> removeReturnCollection(K k) {
        this.remove(k);
        return this;
    }

    public Collection<K, V> values(V v) {
        return this.filter(f -> f.equals(v));
    }
}
