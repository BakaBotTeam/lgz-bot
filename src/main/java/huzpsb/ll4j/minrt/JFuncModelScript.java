package huzpsb.ll4j.minrt;

import java.util.function.Function;

public class JFuncModelScript {
    public static Function<double[], double[]> compile(String[] script) {
        Function<double[], double[]> current = (input) -> {
            double[] copied = new double[input.length];
            System.arraycopy(input, 0, copied, 0, input.length);
            return copied;
        };

        for (String str : script) {
            if (str.length() < 2) {
                continue;
            }
            String[] tokens = str.split(" ");
            switch (tokens[0]) {
                case "D":
                    int ic = Integer.parseInt(tokens[1]);
                    int oc = Integer.parseInt(tokens[2]);
                    double[] weights = new double[ic * oc];
                    for (int i = 0; i < oc; i++) {
                        for (int j = 0; j < ic; j++) {
                            weights[i + j * oc] = Double.parseDouble(tokens[3 + i + j * oc]);
                        }
                    }
                    current = current.andThen((input) -> {
                        if (input.length != ic) {
                            throw new RuntimeException("Wrong input size for Dense layer (expected " + ic + ", got " + input.length + ")");
                        }
                        double[] tmp = new double[oc];
                        for (int i = 0; i < oc; i++) {
                            double sum = 0;
                            for (int j = 0; j < ic; j++) {
                                sum += input[j] * weights[i + j * oc];
                            }
                            tmp[i] = sum;
                        }
                        return tmp;
                    });
                    break;
                case "L":
                    int n = Integer.parseInt(tokens[1]);
                    current = current.andThen((input) -> {
                        if (input.length != n) {
                            throw new RuntimeException("Wrong input size for LeakyRelu layer (expected " + n + ", got " + input.length + ")");
                        }
                        double[] tmp = new double[n];
                        for (int i = 0; i < n; i++) {
                            tmp[i] = input[i] > 0 ? input[i] : input[i] * 0.01;
                        }
                        return tmp;
                    });
                    break;
            }
        }
        return current;
    }
}
