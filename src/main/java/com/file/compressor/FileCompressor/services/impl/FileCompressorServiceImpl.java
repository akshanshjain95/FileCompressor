package com.file.compressor.FileCompressor.services.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.file.compressor.FileCompressor.models.FileCompressResponse;
import com.file.compressor.FileCompressor.models.TreeNode;
import com.file.compressor.FileCompressor.services.api.FileCompressorService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class FileCompressorServiceImpl implements FileCompressorService {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public FileCompressResponse compressFile(MultipartFile file) {
        try {
            String fileContent = IOUtils.toString(file.getInputStream(), StandardCharsets.UTF_8);
            Map<Character, String> huffmanCodes = getHuffmanCodes(getFrequencies(fileContent));

            StringBuilder encoded = new StringBuilder();

            for (int i = 0; i < fileContent.length() - 1; i++) {
                encoded.append(huffmanCodes.get(fileContent.charAt(i)));
            }

            int encodedLength = encoded.length();

            int rem = encodedLength % 8;
            if (rem != 0) {
                for (int i=0 ; i<(8-rem) ; i++) {
                    encoded.append("0");
                }
            }

            StringBuilder finalEncode = new StringBuilder();

            for (int i = 0; i < encoded.length(); i += 8) {
                String chunk;
                if (i + 8 < encoded.length())
                    chunk = encoded.substring(i, i + 8);
                else
                    chunk = encoded.substring(i);

                finalEncode.append(Integer.parseInt(chunk, 2)).append(",");
            }

            String finalEncodeStr = finalEncode.append(encodedLength).toString();

            byte[] encodedFileContent = finalEncodeStr.getBytes(StandardCharsets.UTF_8);

            String key = objectMapper.writeValueAsString(huffmanCodes);
            return FileCompressResponse.builder().fileContent(encodedFileContent).key(key).build();

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private Map<Character, Integer> getFrequencies(String content) {
        Map<Character, Integer> freqMap = new HashMap<>();

        for (int i = 0; i < content.length() - 1; i++) {
            char currentCh = content.charAt(i);

            freqMap.merge(currentCh, 1, Integer::sum);
        }

        return freqMap;
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

    @Override
    public byte[] decompressFile(MultipartFile file, MultipartFile key) {
        try {
            String keyContent = IOUtils.toString(key.getInputStream(), StandardCharsets.UTF_8);
            Map<Character, String> keyMap = objectMapper.readValue(keyContent, new TypeReference<Map<Character, String>>() {
            });

            String fileContent = IOUtils.toString(file.getInputStream(), StandardCharsets.UTF_8);

            String[] encodedInts = fileContent.split(",");

            StringBuilder encodedBinary = new StringBuilder();

            for (int i = 0; i < encodedInts.length - 1; i++) {
                String encodedInt = encodedInts[i];
                encodedBinary.append(getEigthLengthedBinaryString(Integer.parseInt(encodedInt)));
            }

            String finalEncodedBinary = encodedBinary.substring(0, Integer.parseInt(encodedInts[encodedInts.length - 1]));

            Map<String, Character> reverseKeyMap = new HashMap<>();

            keyMap.forEach((character, code) -> {
                reverseKeyMap.put(code, character);
            });

            StringBuilder currentEncodedStr = new StringBuilder();
            StringBuilder decodedString = new StringBuilder();
            for (int i=0 ; i<finalEncodedBinary.length() ; i++) {
                currentEncodedStr.append(finalEncodedBinary.charAt(i));

                Character character = reverseKeyMap.get(currentEncodedStr.toString());

                if (character != null) {
                    decodedString.append(character);
                    currentEncodedStr = new StringBuilder();
                }
            }

            return decodedString.toString().getBytes(StandardCharsets.UTF_8);

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private String getEigthLengthedBinaryString(int i) {
        String binaryStr = Integer.toBinaryString(i);

        if (binaryStr.length() != 8) {
            int placesToPad = 8 - binaryStr.length();

            StringBuilder binaryStrBuilder = new StringBuilder();

            for (int j=0 ; j<placesToPad ; j++) {
                binaryStrBuilder.append("0");
            }

            binaryStrBuilder.append(binaryStr);

            return binaryStrBuilder.toString();
        }

        return binaryStr;
    }
}
