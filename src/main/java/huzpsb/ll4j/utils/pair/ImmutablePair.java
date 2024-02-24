package huzpsb.ll4j.utils.pair;
public class ImmutablePair<K, V> extends PairBase<K, V> {
    private final K first;
    private final V second;

    public ImmutablePair(K first, V second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public K first() {
        return first;
    }

    @Override
    public V second() {
        return second;
    }
}