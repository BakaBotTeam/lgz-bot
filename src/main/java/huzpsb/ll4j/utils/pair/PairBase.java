package huzpsb.ll4j.utils.pair;

public abstract class PairBase<K, V> implements Pair<K, V> {
    @Override
    public String toString() {
        return "Pair(first=" + first() + ",second=" + second() + ")";
    }
}
