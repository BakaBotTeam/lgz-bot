package huzpsb.ll4j.model;

import huzpsb.ll4j.data.DataEntry;
import huzpsb.ll4j.data.DataSet;
import huzpsb.ll4j.layer.*;
import huzpsb.ll4j.utils.pair.Pair;
import huzpsb.ll4j.utils.random.NRandom;
import huzpsb.ll4j.utils.random.RandomSeedGenerator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

@SuppressWarnings({"unused", "PatternVariableCanBeUsed"})
public class Model {
    public AbstractLayer[] layers;

    public Model(AbstractLayer... layers) {
        this.layers = layers;
    }

    public Pair<Integer, Integer> trainOn(DataSet dataSet) {
        return trainOn(dataSet, 8e-7);
    }

    public Pair<Integer, Integer> trainOn(DataSet dataSet, double learningRate) {
        int t = 0, f = 0;
        if (!(layers[layers.length - 1] instanceof JudgeLayer)) {
            throw new RuntimeException("Last layer is not output layer");
        }
        JudgeLayer judgeLayer = (JudgeLayer) layers[layers.length - 1];
        for (DataEntry dataEntry : dataSet.split) {
            layers[0].input = dataEntry.values;
            for (int i = 0; i < layers.length; i++) {
                layers[i].training = true;
                layers[i].forward();
                if (i < layers.length - 1) {
                    layers[i + 1].input = layers[i].output;
                }
            }
            int predict = judgeLayer.result;
            int actual = dataEntry.type;
            if (predict == actual) {
                t++;
            } else {
                f++;
            }
            judgeLayer.result = actual;
            for (int i = layers.length - 1; i >= 0; i--) {
                layers[i].backward();
                layers[i].update(learningRate);
                if (i > 0) {
                    layers[i - 1].output_error = layers[i].input_error;
                }
            }
        }
        return Pair.create(t, f);
    }

    // train parallel

    public List<DataEntry> trainAndGetWA(DataSet dataSet, double learningRate) {
        LinkedList<DataEntry> entries = new LinkedList<>();
        if (!(layers[layers.length - 1] instanceof JudgeLayer)) {
            throw new RuntimeException("Last layer is not output layer");
        }
        JudgeLayer judgeLayer = (JudgeLayer) layers[layers.length - 1];
        for (DataEntry dataEntry : dataSet.split) {
            layers[0].input = dataEntry.values;
            for (int i = 0; i < layers.length; i++) {
                layers[i].training = true;
                layers[i].forward();
                if (i < layers.length - 1) {
                    layers[i + 1].input = layers[i].output;
                }
            }
            int predict = judgeLayer.result;
            int actual = dataEntry.type;
            if (predict != actual) {
                entries.add(dataEntry);
            }
            judgeLayer.result = actual;
            for (int i = layers.length - 1; i >= 0; i--) {
                layers[i].backward();
                layers[i].update(learningRate);
                if (i > 0) {
                    layers[i - 1].output_error = layers[i].input_error;
                }
            }
        }
        return entries;
    }

    public Pair<Integer, Integer> testOn(DataSet dataSet) {
        int t = 0, f = 0;
        for (DataEntry dataEntry : dataSet.split) {
            layers[0].input = dataEntry.values;
            for (int i = 0; i < layers.length; i++) {
                layers[i].training = false;
                layers[i].forward();
                if (i < layers.length - 1) {
                    layers[i + 1].input = layers[i].output;
                }
            }
            JudgeLayer judgeLayer = (JudgeLayer) layers[layers.length - 1];
            int predict = judgeLayer.result;
            int actual = dataEntry.type;
            if (predict == actual) {
                t++;
            } else {
                f++;
            }
        }
        return Pair.create(t, f);
    }

    public List<DataEntry> testAndGetWA(DataSet dataSet) {
        LinkedList<DataEntry> entries = new LinkedList<>();
        for (DataEntry dataEntry : dataSet.split) {
            layers[0].input = dataEntry.values;
            for (int i = 0; i < layers.length; i++) {
                layers[i].training = false;
                layers[i].forward();
                if (i < layers.length - 1) {
                    layers[i + 1].input = layers[i].output;
                }
            }
            JudgeLayer judgeLayer = (JudgeLayer) layers[layers.length - 1];
            int predict = judgeLayer.result;
            int actual = dataEntry.type;
            if (predict == actual) continue;
            entries.add(dataEntry);
        }
        return entries;
    }

    public int predict(double[] input) {
        AbstractLayer[] layers = this.layers;
        if (!(layers[layers.length - 1] instanceof JudgeLayer)) {
            throw new RuntimeException("Last layer is not output layer");
        }
        for (AbstractLayer layer : layers) {
            layer.training = false;
            layer.input = input;
            layer.forward();
            input = layer.output;
        }
        return ((JudgeLayer) layers[layers.length - 1]).result;
    }

    public int predict(DataEntry de) {
        double[] input = de.values;
        for (AbstractLayer layer : layers) {
            layer.training = false;
            layer.input = input;
            layer.forward();
            input = layer.output;
        }
        return ((JudgeLayer) layers[layers.length - 1]).result;
    }

    public void save(String path) {
        try (PrintWriter pw = new PrintWriter(path, StandardCharsets.UTF_8)) {
            pw.print(toScript());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static Model read(BufferedReader reader) {
        return new Model(reader.lines().map(Model::parseLine).toArray(AbstractLayer[]::new));
    }

    public static Model read(Scanner scanner) {
        Builder builder = new Builder();
        while (scanner.hasNextLine()) {
            builder.layer(parseLine(scanner.nextLine()));
        }
        return builder.build();
    }

    public static Model read(String script) {
        String[] spLine = script.split("\n");
        Builder builder = new Builder();
        for (String line : spLine) {
            builder.layer(parseLine(line));
        }
        return builder.build();
    }

    public static Model readFrom(String path) {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            return read(reader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static AbstractLayer parseLine(String line) {
        String[] tokens = line.split(" ");
        switch (tokens[0]) {
            case "D":
                int inputSize = Integer.parseInt(tokens[1]);
                int outputSize = Integer.parseInt(tokens[2]);
                DenseLayer denseLayer = new DenseLayer(inputSize, outputSize);
                double[][] weights = denseLayer.weights;
                for (int i = 0; i < inputSize; i++) {
                    for (int j = 0; j < outputSize; j++) {
                        weights[i][j] = Double.parseDouble(tokens[3 + i * outputSize + j]);
                    }
                }
                return denseLayer;
            case "L":
                return new LeakyRelu(Integer.parseInt(tokens[1]));
            case "S":
                return new Sigmoid(Integer.parseInt(tokens[1]));
            case "Dropout":
                return new DropoutLayer(Integer.parseInt(tokens[1])).dropout(Double.parseDouble(tokens[2]));
            case "Softmax":
                return new Softmax(Integer.parseInt(tokens[1]));
            case "Th":
                return new Tanh(Integer.parseInt(tokens[1]));
            case "J":
                return new JudgeLayer(Integer.parseInt(tokens[1]));
            default:
                throw new AssertionError("Unknown layer type: " + tokens[0]);
        }
    }

    public String toScript() {
        StringBuilder sb = new StringBuilder();
        for (AbstractLayer layer : layers) {
            layer.serialize(sb);
        }
        return sb.toString();
    }

    public Model clean() {
        Builder builder = new Builder();
        for (AbstractLayer layer : layers) {
            if (layer instanceof AbstractLayer.TrainOnlyLayer) continue;
            builder.layer(layer);
        }
        return builder.build();
    }

    @SuppressWarnings({"UnusedReturnValue", "unused"})
    public static class Builder {
        private List<AbstractLayer> layers = new LinkedList<>();
        private RandomSeedGenerator randomGenerator = RandomSeedGenerator.Builtin.CONSTANT;

        public Builder() {
        }

        public Builder layer(AbstractLayer layer) {
            if (layer != null) {
                layer.random = new NRandom(randomGenerator.generateSeed());
                layers.add(layer);
            }
            return this;
        }

        public Builder setRandomGenerator(RandomSeedGenerator randomGenerator) {
            this.randomGenerator = randomGenerator;
            return this;
        }

        public RandomSeedGenerator getRandomGenerator() {
            return randomGenerator;
        }

        public List<AbstractLayer> getLayers() {
            return layers;
        }

        public Builder setLayers(List<AbstractLayer> layers) {
            this.layers = layers;
            return this;
        }

        public Model build() {
            return new Model(layers.toArray(AbstractLayer[]::new));
        }
    }


    @SuppressWarnings({"UnusedReturnValue", "unused"})
    public static class ContextBuilder extends Builder {
        private int size;
        private boolean hasOutput = false;

        public ContextBuilder(int inputSize) {
            this.size = inputSize;
        }

        @Override
        public ContextBuilder layer(AbstractLayer layer) {
            super.layer(layer);
            return this;
        }

        public ContextBuilder DenseLayer(int outputSize) {
            layer(new DenseLayer(size, outputSize));
            size = outputSize;
            return this;
        }

        public ContextBuilder LeakyReluLayer() {
            return layer(new LeakyRelu(size));
        }

        public ContextBuilder SigmoidLayer() {
            return layer(new Sigmoid(size));
        }

        public ContextBuilder SoftmaxLayer() {
            return layer(new Softmax(size));
        }

        public ContextBuilder TanhLayer() {
            return layer(new Tanh(size));
        }

        public ContextBuilder DropoutLayer(double dropout) {
            return layer(new DropoutLayer(size).dropout(dropout));
        }

        public ContextBuilder JudgeLayer() {
            hasOutput = true;
            return layer(new JudgeLayer(size));
        }

        @Override
        public ContextBuilder setRandomGenerator(RandomSeedGenerator randomGenerator) {
            super.setRandomGenerator(randomGenerator);
            return this;
        }

        @Override
        public Model build() {
            if (!hasOutput) JudgeLayer();
            return super.build();
        }
    }
}
