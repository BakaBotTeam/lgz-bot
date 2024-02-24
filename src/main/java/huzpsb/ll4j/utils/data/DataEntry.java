package huzpsb.ll4j.utils.data;

public class DataEntry {
    public final int type;
    public final double[] values;

    public DataEntry(int type, double[] values) {
        this.type = type;
        this.values = values;
    }
}
