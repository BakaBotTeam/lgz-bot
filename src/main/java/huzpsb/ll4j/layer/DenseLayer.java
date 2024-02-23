package huzpsb.ll4j.layer;

public class DenseLayer extends AbstractLayer {
    public double[][] weights;

    public DenseLayer(int inputSize, int outputSize) {
        super(inputSize, outputSize);
        weights = new double[inputSize][outputSize];
        initialize();
    }

    @Override
    public void forward() {
        for (int i = 0; i < output_size; i++) {
            output[i] = 0;
            for (int j = 0; j < input_size; j++) {
                output[i] += input[j] * weights[j][i];
            }
        }
    }

    @Override
    public void backward() {
        makeInputError();
        for (int i = 0; i < input_size; i++) {
            input_error[i] = 0;
            for (int j = 0; j < output_size; j++) {
                input_error[i] += output_error[j] * weights[i][j];
            }
        }
    }

    @Override
    public void update(double learningRate) {
        for (int i = 0; i < input_size; i++) {
            for (int j = 0; j < output_size; j++) {
                double delta = learningRate * output_error[j] * input[i];
                weights[i][j] -= delta;
            }
        }
    }

    @Override
    public void randomize(double rv) {
        for (int i = 0; i < input_size; i++) {
            for (int j = 0; j < output_size; j++) {
                weights[i][j] *= random.nextDouble(1 - rv, 1 + rv);
            }
        }
    }

    @Override
    public void initialize() {
        for (int i = 0; i < input_size; i++) {
            for (int j = 0; j < output_size; j++) {
                weights[i][j] = random.nextGaussian(0, 1.0 / Math.sqrt(input_size));
            }
        }
    }

    @Override
    public void serialize(StringBuilder sb) {
        sb.append("D ").append(input_size).append(" ").append(output_size).append(" ");
        for (int i = 0; i < input_size; i++) {
            for (int j = 0; j < output_size; j++) {
                sb.append(weights[i][j]).append(" ");
            }
        }
        sb.append("\n");
    }
}
