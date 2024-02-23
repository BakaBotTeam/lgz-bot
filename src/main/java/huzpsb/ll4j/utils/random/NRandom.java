package huzpsb.ll4j.utils.random;

public class NRandom {
    private long seed;

    public NRandom(long seed) {
        this.seed = seed;
    }

    public double nextDouble() {
        return (((long) (next(26)) << 27) + next(27)) * 0x1.0p-53;
    }

    public double nextDouble(double min, double max) {
        return nextDouble() * (max - min) + min;
    }

    public double nextGaussian() {
        double u = nextDouble();
        double v = nextDouble();
        return Math.sqrt(-2 * Math.log(u)) * Math.cos(2 * Math.PI * v);
    }

    public double nextGaussian(double mean, double std) {
        return nextGaussian() * std + mean;
    }

    private int next(int bits) {
        seed = (seed * 0x5DEECE66DL + 0xBL) & (1L << 48) - 1;
        return (int) (seed >>> (48 - bits));
    }
}
