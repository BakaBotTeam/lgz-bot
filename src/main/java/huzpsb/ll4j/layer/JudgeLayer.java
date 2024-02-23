package huzpsb.ll4j.layer;

public class JudgeLayer extends AbstractLayer {
    public int result = 0;

    public JudgeLayer(int size) {
        super(size, size);
    }

    @Override
    public void forward() {
        for (int i = 0; i < input_size; i++) {
            if (Double.isNaN(input[i])) {
                throw new RuntimeException("input[" + i + "] is NaN! Plz reduce learning rate!");
            }
            if (input[i] > input[result]) {
                result = i;
            }
        }
    }

    @Override
    public void backward() {
        makeInputError();
        for (int i = 0; i < input_size; i++) {
            if (i == result) {
                input_error[i] = input[i] - 1;
            } else {
                input_error[i] = input[i];
            }
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
        sb.append("J ").append(input_size).append("\n");
    }

    public static JudgeLayer judgeLayer(int size) {
        return new JudgeLayer(size);
    }
}
