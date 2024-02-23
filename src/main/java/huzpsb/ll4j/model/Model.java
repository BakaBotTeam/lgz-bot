package huzpsb.ll4j.model;

import huzpsb.ll4j.data.DataEntry;
import huzpsb.ll4j.data.DataSet;
import huzpsb.ll4j.layer.AbstractLayer;
import huzpsb.ll4j.layer.JudgeLayer;

import java.io.PrintWriter;

public class Model {
    public AbstractLayer[] layers;

    public Model(AbstractLayer... layers) {
        this.layers = layers;
    }

    public void trainOn(DataSet dataSet) {
        int t = 0, f = 0;
        for (DataEntry dataEntry : dataSet.split) {
            layers[0].input = dataEntry.values;
            for (int i = 0; i < layers.length; i++) {
                layers[i].forward();
                if (i < layers.length - 1) {
                    layers[i + 1].input = layers[i].output;
                }
            }
            if (!(layers[layers.length - 1] instanceof JudgeLayer)) {
                throw new RuntimeException("Last layer is not output layer");
            }
            JudgeLayer judgeLayer = (JudgeLayer) layers[layers.length - 1];
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
                layers[i].update(8e-7);
                if (i > 0) {
                    layers[i - 1].output_error = layers[i].input_error;
                }
            }
        }
        System.out.println("t: " + t + ", f: " + f);
    }

    public void testOn(DataSet dataSet) {
        int t = 0, f = 0;
        for (DataEntry dataEntry : dataSet.split) {
            layers[0].input = dataEntry.values;
            for (int i = 0; i < layers.length; i++) {
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
        System.out.println("t: " + t + ", f: " + f);
    }

    public int predict(DataEntry dataEntry) {
        layers[0].input = dataEntry.values;
        for (int i = 0; i < layers.length; i++) {
            layers[i].forward();
            if (i < layers.length - 1) {
                layers[i + 1].input = layers[i].output;
            }
        }
        JudgeLayer judgeLayer = (JudgeLayer) layers[layers.length - 1];
        int predict1 = judgeLayer.result;
        return predict1;
    }

    public void save(String path) {
        try {
            StringBuilder sb = new StringBuilder();
            for (AbstractLayer layer : layers) {
                layer.serialize(sb);
            }
            PrintWriter pw = new PrintWriter(path, "UTF-8");
            pw.print(sb);
            pw.close();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
