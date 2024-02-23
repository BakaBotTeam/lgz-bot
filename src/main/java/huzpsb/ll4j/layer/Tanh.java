package huzpsb.ll4j.layer;

public class Tanh extends AbstractLayer {
    public Tanh(int inputSize) {
        super(inputSize, inputSize);
    }

    @Override
    public void forward() {
        for (int i = 0; i < output_size; i++) {
//            double x = input[i];
            output[i] = Math.tanh(input[i]);
//            output[i] = (exp(x) - exp(-x)) / (exp(x) + exp(-x));
        }
    }

    @Override
    public void backward() {
        makeInputError();
        for (int i = 0; i < input_size; i++) {
            double x = input_error[i];
            double derivativeTanh = 1.0 - output[i] * output[i]; // tanh导数公式
            input_error[i] = derivativeTanh * x;
        }
    }

    @Override
    public void update(double learningRate) {

    }

    @Override
    public void randomize(double rv) {

    }

    @Override
    public void initialize() {

    }

    @Override
    public void serialize(StringBuilder sb) {
        sb.append("Th ").append(input_size).append("\n");
    }
}
