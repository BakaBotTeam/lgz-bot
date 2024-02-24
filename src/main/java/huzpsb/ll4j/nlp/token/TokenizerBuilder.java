package huzpsb.ll4j.nlp.token;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;

public class TokenizerBuilder {
    private final int outputSize;
    private final Map<String, Integer> occurrences = new HashMap<>();
    public int minLength = 1, maxLength = 5;
    public boolean smartEnglishContext = true, checkRepeat = false;


    public TokenizerBuilder(int outputSize) {
        this.outputSize = outputSize;
    }

    public void update(String text) {
        text = CharUtils.regularize(text);
        LinkedList<String> engStr = new LinkedList<>();
        if (smartEnglishContext) {
            int[] charArray = text.codePoints().toArray();
            for (int i = 0; i < charArray.length; i++) {
                char c = (char) charArray[i];
                if (CharUtils.isEnglishLetter(c)) {
                    int until = i + 1;
                    StringBuilder builder = new StringBuilder();
                    builder.append(c);
                    for (; until < charArray.length; until++) {
                        char ch = (char) charArray[until];
                        if (CharUtils.isEnglishLetter(ch)) {
                            builder.append(ch);
                            i = until;
                        }
                        else break;
                    }
                    String s = builder.toString();
                    updateVocab(s);
                    engStr.add(s);
                }
            }
        }

        int length = text.length();
        for (int i = 0; i < length; i++) {
            for (int j = i + minLength; j <= length; j++) {
                if(j - i > maxLength) break;
                String substring = text.substring(i, j);
                if (checkRepeat && substring.length() > 1) {
                    String str2 = substring.concat(substring);
                    if (str2.substring(1, str2.length() - 2).contains(substring)) continue;
                }

                if (smartEnglishContext && CharUtils.isEnglishContext(substring)) {
                    i = j;
                    for (String s : engStr) {
                        if (s.contains(substring) || substring.contains(s)) {
                            updateVocab(s);
                            break;
                        }
                    }
                    continue;
                }

                updateVocab(substring);
            }
        }
    }

    private void updateVocab(String text) {
        if (occurrences.containsKey(text)) {
            occurrences.put(text, occurrences.get(text) + 1);
        } else {
            occurrences.put(text, 1);
        }
    }

    public Tokenizer build() {
        String[] vocab = new String[outputSize];
        PriorityQueue<Entry> queue = new PriorityQueue<>();
        for (Map.Entry<String, Integer> entry : occurrences.entrySet()) {
            String key = entry.getKey();
            int value = entry.getValue();
            double weight = value * Math.log(key.length() + 10);
            queue.add(new Entry(key, weight));
        }
        for (int i = 0; i < outputSize; i++) {
            if (queue.isEmpty()) {
                break;
            }
            vocab[i] = queue.poll().key;
        }
        return new Tokenizer(vocab);
    }

    public void loadDefault() {
        for (char i = 'a'; i < 'z'; i++) {
            occurrences.put(String.valueOf(i), 0);
        }
        for (char i = 'A'; i < 'Z'; i++) {
            occurrences.put(String.valueOf(i), 0);
        }
        for (char i = '0'; i < '9'; i++) {
            occurrences.put(String.valueOf(i), 0);
        }
        occurrences.put(" ", 0);
    }

    static class Entry implements Comparable<Entry> {
        String key;
        double value;

        public Entry(String key, double value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public int compareTo(Entry o) {
            return Double.compare(o.value, value);
        }
    }
}

