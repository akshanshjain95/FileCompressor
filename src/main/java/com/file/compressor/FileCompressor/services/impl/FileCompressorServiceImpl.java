package com.file.compressor.FileCompressor.services.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.file.compressor.FileCompressor.models.FileCompressResponse;
import com.file.compressor.FileCompressor.processor.HuffmanDecoder;
import com.file.compressor.FileCompressor.processor.HuffmanEncoder;
import com.file.compressor.FileCompressor.services.api.FileCompressorService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class FileCompressorServiceImpl implements FileCompressorService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private HuffmanEncoder huffmanEncoder;

    @Autowired
    private HuffmanDecoder huffmanDecoder;

    @Override
    public FileCompressResponse compressFile(MultipartFile file) {
        try {
            String fileContent = IOUtils.toString(file.getInputStream(), StandardCharsets.UTF_8);

            //removing last character from content since it's EOF.
//            fileContent = fileContent.substring(0, fileContent.length() - 1);

            Map<Character, String> huffmanCodes = huffmanEncoder.getKey(fileContent);

            String finalEncodeStr = huffmanEncoder.encodeFileContent(huffmanCodes, fileContent);

            byte[] encodedFileContent = finalEncodeStr.getBytes(StandardCharsets.UTF_8);

            String key = objectMapper.writeValueAsString(huffmanCodes);
            return FileCompressResponse.builder().fileContent(encodedFileContent).key(key).build();

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] decompressFile(MultipartFile file, MultipartFile key) {
        try {
            String keyContent = IOUtils.toString(key.getInputStream(), StandardCharsets.UTF_8);
            Map<Character, String> keyMap = objectMapper.readValue(keyContent, new TypeReference<Map<Character, String>>() {
            });

            String fileContent = IOUtils.toString(file.getInputStream(), StandardCharsets.UTF_8);

            return huffmanDecoder.getDecodedContent(fileContent, keyMap).getBytes();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
