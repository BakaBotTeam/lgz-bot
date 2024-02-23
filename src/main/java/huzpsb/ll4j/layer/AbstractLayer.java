package huzpsb.ll4j.layer;

import huzpsb.ll4j.utils.NRandom;

public abstract class AbstractLayer {
    public final int input_size;
    public final int output_size;
    public final NRandom random;
    public double[] input;
    public double[] output;
    public double[] input_error = null;
    public double[] output_error = null;

    public AbstractLayer(int inputSize, int outputSize) {
        this(inputSize, outputSize, 1145151919810L);
    }

    public AbstractLayer(int inputSize, int outputSize, long seed) {
        input_size = inputSize;
        output_size = outputSize;
        input = new double[input_size];
        output = new double[output_size];
        random = new NRandom(seed);
    }

    public void makeInputError() {
        if (input_error == null) {
            input_error = new double[input_size];
        }
    }

    public void makeOutputError() {
        if (output_error == null) {
            output_error = new double[output_size];
        }
    }

    public abstract void forward();

    public abstract void backward();

    public abstract void update(double learningRate);

    public abstract void randomize(double rv);

    public abstract void initialize();

    public abstract void serialize(StringBuilder sb);
}
