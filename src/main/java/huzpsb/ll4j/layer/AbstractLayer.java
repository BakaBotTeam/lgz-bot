package huzpsb.ll4j.layer;

import huzpsb.ll4j.model.Model;
import huzpsb.ll4j.utils.random.NRandom;

public abstract class AbstractLayer {
    public int input_size;
    public int output_size;
    public NRandom random;
    public double[] input;
    public double[] output;
    public double[] input_error = null;
    public double[] output_error = null;
    public boolean training = false;

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

    public AbstractLayer mergeWith(AbstractLayer layer) {
        if (layer.getClass() != this.getClass()) {
            throw new RuntimeException("Can't merge different layer type");
        }
        if (layer.input_size != input_size || layer.output_size != output_size) {
            throw new RuntimeException("Can't merge different layer size");
        }
        // need to implement
        StringBuilder builder = new StringBuilder();
        this.serialize(builder);
        return Model.parseLine(builder.toString().replaceAll("\n", ""));
    }

    @Override
    public AbstractLayer clone() {
        try {
            return (AbstractLayer) super.clone();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public abstract void forward();

    public abstract void backward();

    public abstract void update(double learningRate);

    public abstract void randomize(double rv);

    public abstract void initialize();

    public abstract void serialize(StringBuilder sb);

    public interface TrainOnlyLayer {
    }
}
