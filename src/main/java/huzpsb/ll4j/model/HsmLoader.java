package huzpsb.ll4j.model;

import huzpsb.ll4j.layer.AbstractLayer;
import huzpsb.ll4j.layer.DenseLayer;
import huzpsb.ll4j.layer.JudgeLayer;
import huzpsb.ll4j.layer.LeakyRelu;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class HsmLoader {
    public static Model load(String path) {
        try {
            Scanner sc = new Scanner(new File(path), "UTF-8");
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
