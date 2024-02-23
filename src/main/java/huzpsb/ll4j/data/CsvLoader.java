package huzpsb.ll4j.data;

import java.io.File;
import java.util.Scanner;

public class CsvLoader {
    public static DataSet load(String path, int labelIndex) {
        try {
            DataSet data = new DataSet();
            Scanner sc = new Scanner(new File(path), "UTF-8");
            String[] header = sc.nextLine().split(",");
            int n = header.length;
            while (sc.hasNextLine()) {
                String[] line = sc.nextLine().split(",");
                double[] x = new double[n - 1];
                int label = Integer.parseInt(line[labelIndex]);
                int idx = 0;
                for (int i = 0; i < n; i++) {
                    if (i != labelIndex) {
                        x[idx++] = Double.parseDouble(line[i]);
                    }
                }
                DataEntry entry = new DataEntry(label, x);
                data.split.add(entry);
            }
            return data;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
