package huzpsb.ll4j.filter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class Tokenizer {
    private final String[] vocab;

    public Tokenizer(String[] vocab) {
        this.vocab = vocab;
    }

    public static Tokenizer loadFromFile(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        int size = Integer.parseInt(scanner.nextLine());
        String[] vocab = new String[size];
        for (int i = 0; i < size; i++) {
            vocab[i] = scanner.nextLine();
        }
        return new Tokenizer(vocab);
    }

    public static Tokenizer loadFromFile(InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream);
        int size = Integer.parseInt(scanner.nextLine());
        String[] vocab = new String[size];
        for (int i = 0; i < size; i++) {
            vocab[i] = scanner.nextLine();
        }
        return new Tokenizer(vocab);
    }

    public double[] tokenize(String text) {
        double[] values = new double[vocab.length];
        for (int i = 0; i < values.length; i++) {
            if (text.contains(vocab[i])) {
                values[i] = 1;
            }
        }
        return values;
    }
}
