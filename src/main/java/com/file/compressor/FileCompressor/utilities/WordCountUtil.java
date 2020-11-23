package com.file.compressor.FileCompressor.utilities;

import java.util.HashMap;
import java.util.Map;

public class WordCountUtil {

    private static WordCountUtil instance;

    private WordCountUtil() {
    }

    public static WordCountUtil getInstance() {
        if (instance == null) {
            instance = new WordCountUtil();
        }

        return instance;
    }

    public Map<Character, Integer> getFrequencies(String content) {
        Map<Character, Integer> freqMap = new HashMap<>();

        for (int i = 0; i < content.length(); i++) {
            char currentCh = content.charAt(i);

            freqMap.merge(currentCh, 1, Integer::sum);
        }

        return freqMap;
    }
}
