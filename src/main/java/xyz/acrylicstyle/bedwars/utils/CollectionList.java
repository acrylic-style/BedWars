package xyz.acrylicstyle.bedwars.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class CollectionList<V> extends ArrayList<V> {
    public CollectionList() {
        super();
    }

    public CollectionList(ArrayList<? extends V> list) {
        super();
        this.addAll(list);
    }

    @SuppressWarnings("unchecked")
    public V first() {
        return (V) this.toArray()[0];
    }

    @SuppressWarnings("unchecked")
    public V[] valuesArray() {
        return (V[]) this.toArray();
    }

    /**
     * @param action it passes value, index.
     */
    public void foreach(BiConsumer<V, Integer> action) {
        final int[] index = {0};
        this.forEach(v -> {
            action.accept(v, index[0]);
            index[0]++;
        });
    }

    public V put(V value) {
        super.add(value);
        return value;
    }

    public <ListLike extends List<? extends V>> void putAll(ListLike list) {
        super.addAll(list);
    }

    /**
     * Filters values. If returned true, that value will be kept. Returns new Collection of filtered values.
     * @param filter filter function.
     */
    public CollectionList<V> filter(Function<V, Boolean> filter) {
        CollectionList<V> newList = new CollectionList<>();
        this.foreach((v, i) -> {
            if (filter.apply(v)) newList.add(v);
        });
        return newList;
    }

    public CollectionList<V> clone() {
        CollectionList<V> newList = new CollectionList<>();
        newList.addAll(this);
        return newList;
    }

    public CollectionList<V> removeReturnCollection(V v) {
        this.remove(v);
        return this;
    }
}