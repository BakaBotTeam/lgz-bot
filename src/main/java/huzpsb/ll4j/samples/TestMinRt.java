package huzpsb.ll4j.samples;

import huzpsb.ll4j.minrt.MinRt;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@SuppressWarnings("DataFlowIssue")
public class TestMinRt {
    public static void main(String[] args) throws Exception {
        List<String> strList = new ArrayList<>();

        Scanner sc = new Scanner(TestMinRt.class.getResourceAsStream("/test.model"));
        while (sc.hasNextLine()) {
            strList.add(sc.nextLine());
        }
        String[] script = strList.toArray(new String[0]);
        sc.close();

        sc = new Scanner(new File("fashion-mnist_test.csv"));
        int correct = 0, wrong = 0;
        sc.nextLine(); // Skip header
        for (int c = 0; c < 100; c++) {
            String[] line = sc.nextLine().split(",");
            double[] input = new double[line.length - 1];
            for (int i = 0; i < line.length - 1; i++) {
                input[i] = Double.parseDouble(line[i + 1]);
            }

            if (correct == 0 && wrong == 0) {
                BufferedImage img = new BufferedImage(28, 28, BufferedImage.TYPE_BYTE_GRAY);
                for (int i = 0; i < 28 * 28; i++) {
                    int x = i % 28;
                    int y = i / 28;
                    int rgb = (int) (input[i] * 255);
                    img.setRGB(x, y, rgb << 16 | rgb << 8 | rgb);
                }
                ImageIO.write(img, "png", new File("test.png"));
            }

            int actualLabel = Integer.parseInt(line[0]);
            int predictedLabel = MinRt.doAi(input, script);
            if (actualLabel == predictedLabel) {
                correct++;
            } else {
                wrong++;
            }
        }
        System.out.println("Correct: " + correct + " Wrong: " + wrong);
    }
}
