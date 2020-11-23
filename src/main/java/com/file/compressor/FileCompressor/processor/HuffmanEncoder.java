package com.file.compressor.FileCompressor.processor;

import com.file.compressor.FileCompressor.models.TreeNode;
import com.file.compressor.FileCompressor.utilities.WordCountUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

@Component
public class HuffmanEncoder {

    @Autowired
    private BinaryProcessor binaryProcessor;

    public Map<Character, String> getKey(String content) {
        Map<Character, Integer> frequencies = WordCountUtil.getInstance().getFrequencies(content);

        return getHuffmanCodes(frequencies);
    }

    public String encodeFileContent(Map<Character, String> key, String content) {
        StringBuilder encoded = new StringBuilder();

        for (int i = 0; i < content.length(); i++) {
            encoded.append(key.get(content.charAt(i)));
        }

        int encodedLength = encoded.length();

        int rem = encodedLength % 8;
        if (rem != 0) {
            for (int i=0 ; i<(8-rem) ; i++) {
                encoded.append("0");
            }
        }

        String finalEncodedString = binaryProcessor.getBinaryToIntEncodedString(encoded.toString());

        return finalEncodedString + "," + encodedLength;
    }

    private Map<Character, String> getHuffmanCodes(Map<Character, Integer> freqMap) {
        PriorityQueue<TreeNode> pq = new PriorityQueue<>();

        freqMap.forEach((character, freq) -> {
            TreeNode treeNode = new TreeNode(freq, character);
            pq.add(treeNode);
        });

        while (pq.size() > 1) {
            TreeNode node1 = pq.remove();
            TreeNode node2 = pq.remove();

            TreeNode node3 = new TreeNode(node1.getFreq() + node2.getFreq());
            node3.setLeft(node1);
            node3.setRight(node2);
            pq.add(node3);

        }

        Map<Character, String> key = new HashMap<>();
        generateHuffmanCodes(pq.remove(), "", key);

        return key;
    }

    private void generateHuffmanCodes(TreeNode root, String code, Map<Character, String> key) {
        if (root == null) {
            return;
        }

        if (root.getLeft() == null && root.getRight() == null) {
            key.put(root.getCh(), code);
        } else {
            generateHuffmanCodes(root.getLeft(), code + "0", key);
            generateHuffmanCodes(root.getRight(), code + "1", key);
        }
    }
}
