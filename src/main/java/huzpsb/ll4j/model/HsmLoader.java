package huzpsb.ll4j.model;

import huzpsb.ll4j.layer.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class HsmLoader {
    public static Model load(InputStream stream) {
        try {
            Scanner sc = new Scanner(stream, "UTF-8");
            ArrayList<AbstractLayer> layers = new ArrayList<>();
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line.length() < 2) {
                    continue;
                }
                String[] tokens = line.split(" ");
                switch (tokens[0]) {
                    case "D":
                        int w = Integer.parseInt(tokens[1]);
                        int h = Integer.parseInt(tokens[2]);
                        DenseLayer denseLayer = new DenseLayer(w, h);
                        for (int i = 0; i < w; i++) {
                            for (int j = 0; j < h; j++) {
                                denseLayer.weights[i][j] = Double.parseDouble(tokens[3 + i * h + j]);
                            }
                        }
                        layers.add(denseLayer);
                        break;
                    case "L":
                        int n = Integer.parseInt(tokens[1]);
                        layers.add(new LeakyRelu(n));
                        break;
                    case "J":
                        int m = Integer.parseInt(tokens[1]);
                        layers.add(new JudgeLayer(m));
                        break;
                    case "Th":
                        int o = Integer.parseInt(tokens[1]);
                        layers.add(new Tanh(o));
                        break;
                    case "Softmax":
                        int p = Integer.parseInt(tokens[1]);
                        layers.add(new Softmax(p));
                        break;
                    case "Dropout":
                        int q = Integer.parseInt(tokens[1]);
                        layers.add(new DropoutLayer(q));
                        break;
                    default:
                        throw new IOException("Unknown huzpsb.ll4j.layer type: " + tokens[0]);
                }
            }
            sc.close();
            AbstractLayer[] layersArray = new AbstractLayer[layers.size()];
            layers.toArray(layersArray);
            return new Model(layersArray);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static Model load(File file) {
        try {
            Scanner sc = new Scanner(file, "UTF-8");
            ArrayList<AbstractLayer> layers = new ArrayList<>();
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line.length() < 2) {
                    continue;
                }
                String[] tokens = line.split(" ");
                switch (tokens[0]) {
                    case "D":
                        int w = Integer.parseInt(tokens[1]);
                        int h = Integer.parseInt(tokens[2]);
                        DenseLayer denseLayer = new DenseLayer(w, h);
                        for (int i = 0; i < w; i++) {
                            for (int j = 0; j < h; j++) {
                                denseLayer.weights[i][j] = Double.parseDouble(tokens[3 + i * h + j]);
                            }
                        }
                        layers.add(denseLayer);
                        break;
                    case "L":
                        int n = Integer.parseInt(tokens[1]);
                        layers.add(new LeakyRelu(n));
                        break;
                    case "J":
                        int m = Integer.parseInt(tokens[1]);
                        layers.add(new JudgeLayer(m));
                        break;
                    case "Th":
                        int o = Integer.parseInt(tokens[1]);
                        layers.add(new Tanh(o));
                        break;
                    case "Softmax":
                        int p = Integer.parseInt(tokens[1]);
                        layers.add(new Softmax(p));
                        break;
                    case "Dropout":
                        int q = Integer.parseInt(tokens[1]);
                        layers.add(new DropoutLayer(q));
                        break;
                    default:
                        throw new IOException("Unknown huzpsb.ll4j.layer type: " + tokens[0]);
                }
            }
            sc.close();
            AbstractLayer[] layersArray = new AbstractLayer[layers.size()];
            layers.toArray(layersArray);
            return new Model(layersArray);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
