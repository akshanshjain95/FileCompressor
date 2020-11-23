package com.file.compressor.FileCompressor.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class HuffmanDecoder {

    @Autowired
    private BinaryProcessor binaryProcessor;

    public String getDecodedContent(String encodedContent, Map<Character, String> key) {
        String[] encodedIntsStrArr = encodedContent.split(",");

        //last number represents the length of the whole binary string, so not including that number when converting
        //ints to binary.
        int[] encodedInts = new int[encodedIntsStrArr.length - 1];

        for (int i=0 ; i<encodedIntsStrArr.length - 1 ; i++) {
            encodedInts[i] = Integer.parseInt(encodedIntsStrArr[i]);
        }

        String binaryString = binaryProcessor.getIntToBinaryDecodedString(encodedInts);

        //Removing extra padded characters in binary string according to the length(last number) given in the content file.
        binaryString = binaryString.substring(0,
                Integer.parseInt(encodedIntsStrArr[encodedIntsStrArr.length - 1]));

        Map<String, Character> reverseKeyMap = new HashMap<>();

        key.forEach((character, code) -> reverseKeyMap.put(code, character));

        return getDecodedString(reverseKeyMap, binaryString);
    }

    private String getDecodedString(Map<String, Character> reverseKey, String binaryString) {
        StringBuilder currentEncodedStr = new StringBuilder();
        StringBuilder decodedString = new StringBuilder();
        for (int i=0 ; i<binaryString.length() ; i++) {
            currentEncodedStr.append(binaryString.charAt(i));

            Character character = reverseKey.get(currentEncodedStr.toString());

            if (character != null) {
                decodedString.append(character);
                currentEncodedStr = new StringBuilder();
            }
        }

        return decodedString.toString();
    }
}
