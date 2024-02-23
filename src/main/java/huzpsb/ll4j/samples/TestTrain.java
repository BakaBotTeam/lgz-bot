package huzpsb.ll4j.samples;

import huzpsb.ll4j.data.CsvLoader;
import huzpsb.ll4j.data.DataSet;
import huzpsb.ll4j.layer.DenseLayer;
import huzpsb.ll4j.layer.JudgeLayer;
import huzpsb.ll4j.layer.LeakyRelu;
import huzpsb.ll4j.model.Model;

public class TestTrain {
    public static void main(String[] args) {
        DataSet trainingSet = CsvLoader.load("fashion-mnist_train.csv", 0);
        Model model = new Model(
            new DenseLayer(784, 100)
            , new LeakyRelu(100)
            , new DenseLayer(100, 100)
            , new LeakyRelu(100)
            , new DenseLayer(100, 10)
            , new JudgeLayer(10) // MSELoss
        );
        for (int i = 0; i < 128; i++) {
            model.trainOn(trainingSet);
        }
        model.save("test.model");
    }
}
