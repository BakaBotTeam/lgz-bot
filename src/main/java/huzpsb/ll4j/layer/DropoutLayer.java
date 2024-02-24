package huzpsb.ll4j.layer;

public class DropoutLayer extends AbstractLayer implements AbstractLayer.TrainOnlyLayer {
    protected double dropoutRate;
    protected boolean dropoutState = false;
    protected boolean[] mask;

    public DropoutLayer(int input) {
        super(input, input);
    }

    @Override
    public void forward() {
        updateMask();
        output = input;
        if (!dropoutState || !training || dropoutRate >= 1.0) return;
        double div = 1 - dropoutRate;
        for (int i = 0; i < input.length; i++) {
            output[i] = (mask[i] ? 1 : 0) * input[i] / div;
        }
    }

    @Override
    public void backward() {
        input_error = output_error;
        if (!dropoutState || !training) return;
        makeInputError();
        for (int i = 0; i < input_error.length; i++) {
            input_error[i] *= mask[i] ? 1 : 0;
        }
    }

    @Override
    public void update(double learningRate) {
        // nothing to update
    }

    @Override
    public void randomize(double rv) {}

    @Override
    public void initialize() {
        updateMask();
    }

    public void updateMask() {
        mask = new boolean[input_size];
        if (dropoutRate >= 1.0 || !training || !dropoutState) return;
        for (int i = 0; i < input_size; i++) {
            mask[i] = random.nextGaussian() <= dropoutRate;
        }
    }

    public DropoutLayer enable() {
        dropoutState = true;
        return this;
    }

    public DropoutLayer disable() {
        dropoutState = false;
        return this;
    }

    public DropoutLayer dropout(double rate) {
        this.dropoutRate = rate;
        updateMask();
        enable();
        return this;
    }

    public double getDropoutRate() {
        return dropoutRate;
    }

    @Override
    public void serialize(StringBuilder sb) {
        sb.append("Dropout ").append(input_size).append(" ").append(dropoutRate).append("\n");
    }
}
