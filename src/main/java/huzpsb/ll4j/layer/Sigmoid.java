package huzpsb.ll4j.layer;

public class Sigmoid extends AbstractLayer {
    public Sigmoid(int size) {
        super(size, size);
    }

    @Override
    public void forward() {
        for (int i = 0; i < output_size; i++) {
            output[i] = 1 / (1 + Math.exp(-input[i]));
        }
    }

    @Override
    public void backward() {
        makeInputError();
        for (int i = 0; i < output_size; i++) {
            input_error[i] = output_error[i] * output[i] * (1 - output[i]);
        }
    }

    @Override
    public void update(double learningRate) {
        // Sigmoid has no parameters to update
    }

    @Override
    public void randomize(double rv) {
        // Sigmoid has no parameters to randomize
    }

    @Override
    public void initialize() {
        // Sigmoid has no parameters to initialize
    }

    @Override
    public void serialize(StringBuilder sb) {
        sb.append("S ").append(input_size).append("\n");
    }
}
