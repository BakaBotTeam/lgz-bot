package huzpsb.ll4j.layer;

public class Softmax extends AbstractLayer {
    public Softmax(int inputSize) {
        super(inputSize, inputSize);
    }

    @Override
    public void forward() {
        double[] input = this.input;
        double[] output = this.output;
        double maxInput = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < input_size; i++) {
            maxInput = Math.max(maxInput, input[i]);
        }

        double sum = 0.0;
        for (int i = 0; i < input_size; i++) {
            output[i] = Math.exp(input[i] - maxInput);
            sum += output[i];
        }

        // 归一化输出
        for (int i = 0; i < input_size; i++) {
            output[i] /= sum;
        }
    }

    @Override
    public void backward() {
        makeInputError();
        // 计算每个元素的输入误差
        for (int i = 0; i < input_size; i++) {
            // softmax层的导数是当前输出值乘以(1 - 当前输出值)，再乘以外部传入的该位置的输出误差
            double derivative = output[i] * (1.0 - output[i]) * output_error[i];

            // 更新输入误差
            input_error[i] = derivative;
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
        sb.append("Softmax ").append(input_size).append("\n");
    }
}
