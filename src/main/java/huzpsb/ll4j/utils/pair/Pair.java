package huzpsb.ll4j.utils.pair;

public interface Pair<K, V> {
    K first();

    V second();

    static <K, V> Pair<K, V> create(K key, V value) {
        return new ImmutablePair<>(key, value);
    }

    default Pair<K, V> mutable() {
        return new MutablePair<>(first(), second());
    }

    default Pair<K, V> immutable() {
        return new ImmutablePair<>(first(), second());
    }
}
