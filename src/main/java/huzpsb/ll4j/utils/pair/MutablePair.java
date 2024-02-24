package huzpsb.ll4j.utils.pair;

public class MutablePair<K, V> extends PairBase<K, V> {
    private K first;
    private V second;

    public MutablePair(K first, V second) {
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

    public void setFirst(K first) {
        this.first = first;
    }

    public void setSecond(V second) {
        this.second = second;
    }
}
