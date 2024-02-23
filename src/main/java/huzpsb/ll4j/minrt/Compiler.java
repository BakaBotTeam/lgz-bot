package huzpsb.ll4j.minrt;

import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Compiler {
    public static void compileC(String from, String to) {
        try {
            Scanner sc = new Scanner(new File(from), StandardCharsets.UTF_8);
            StringBuilder sb = new StringBuilder("int judge(const double *l0) {\n    int ans = 0;\n");
            int layer = 0;
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line.length() < 2) {
                    continue;
                }
                layer = getLayer(line, sb, layer);
            }
            sc.close();
            PrintWriter pw = new PrintWriter(to, StandardCharsets.UTF_8);
            pw.print(sb);
            pw.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void compileSource(String script, String to) {
        try {
            StringBuilder sb = new StringBuilder("int judge(const double *l0) {\n    int ans = 0;\n");
            int layer = 0;
            for (String line : script.split("\n")) {
                layer = getLayer(line, sb, layer);
            }
            sb.append("    return ans;\n}\n");
            PrintWriter pw = new PrintWriter(to, StandardCharsets.UTF_8);
            pw.print(sb);
            pw.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static int getLayer(String line, StringBuilder sb, int layer) {
        layer++;
        String[] tokens = line.split(" ");
        switch (tokens[0]) {
            case "D":
                int ic = Integer.parseInt(tokens[1]);
                int oc = Integer.parseInt(tokens[2]);
                sb.append("    double *l").append(layer).append(" = (double *) malloc(sizeof(double) * ").append(oc).append(");\n");
                for (int i = 0; i < oc; i++) {
                    sb.append("    l").append(layer).append("[").append(i).append("] = ");
                    for (int j = 0; j < ic; j++) {
                        sb.append("l").append(layer - 1).append("[").append(j).append("] * ").append(tokens[3 + i + j * oc]);
                        if (j != ic - 1) {
                            sb.append(" + ");
                        }
                    }
                    sb.append(";\n");
                }
                break;
            case "L":
                int n = Integer.parseInt(tokens[1]);
                layer--;
                for (int i = 0; i < n; i++) {
                    sb.append("    l").append(layer).append("[").append(i).append("] = l").append(layer).append("[").append(i).append("] > 0 ? l").append(layer).append("[").append(i).append("] : l").append(layer).append("[").append(i).append("] * 0.01;\n");
                }
                break;
            case "S":
                int k = Integer.parseInt(tokens[1]);
                sb.append("    double *l").append(layer).append(" = (double *) malloc(sizeof(double) * ").append(k).append(");\n");
                for (int i = 0; i < k; i++) {
                    sb.append("    l").append(layer).append("[").append(i).append("] = ").append(tokens[2 + i]).append(";\n");
                }
                break;
            case "J":
                int m = Integer.parseInt(tokens[1]);
                for (int i = 1; i < m; i++) {
                    sb.append("    if (l").append(layer - 1).append("[").append(i).append("] > l").append(layer - 1).append("[ans]) ans = ").append(i).append(";\n");
                }
                for (int i = 1; i < layer; i++) {
                    sb.append("    free(l").append(i).append(");\n");
                }
                sb.append("    return ans;\n}\n");
                break;
            default:
                throw new RuntimeException("Unknown or unsupported layer type " + tokens[0]);
        }
        return layer;
    }

    public static void main(String[] args) {
        compileC("gen.model", "anti_ad.c");
    }
}
