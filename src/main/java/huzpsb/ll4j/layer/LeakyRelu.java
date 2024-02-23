package huzpsb.ll4j.layer;

public class LeakyRelu extends AbstractLayer {
    public LeakyRelu(int size) {
        super(size, size);
    }

    @Override
    public void forward() {
        for (int i = 0; i < output_size; i++) {
            if (input[i] > 0) {
                output[i] = input[i];
            } else {
                output[i] = input[i] * 0.01;
            }
        }
    }

    @Override
    public void backward() {
        makeInputError();
        for (int i = 0; i < output_size; i++) {
            if (input[i] > 0) {
                input_error[i] = output_error[i];
            } else {
                input_error[i] = output_error[i] * 0.01;
            }
        }
    }

    @Override
    public void update(double learningRate) {
        // ReLu has no parameters to update
    }

    @Override
    public void randomize(double rv) {
        // ReLu has no parameters to randomize
    }

    @Override
    public void initialize() {
        // ReLu has no parameters to initialize
    }

    @Override
    public void serialize(StringBuilder sb) {
        sb.append("L ").append(input_size).append("\n");
    }
}
