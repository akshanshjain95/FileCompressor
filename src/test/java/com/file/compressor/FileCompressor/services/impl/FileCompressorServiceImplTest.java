package com.file.compressor.FileCompressor.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.file.compressor.FileCompressor.models.FileCompressResponse;
import com.file.compressor.FileCompressor.processor.HuffmanDecoder;
import com.file.compressor.FileCompressor.processor.HuffmanEncoder;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.event.annotation.BeforeTestClass;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FileCompressorServiceImplTest {

    @InjectMocks
    private FileCompressorServiceImpl fileCompressorService;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private HuffmanDecoder huffmanDecoder;

    @Mock
    private HuffmanEncoder huffmanEncoder;

    @Test
    public void compressFileTest() throws JsonProcessingException {
        String fileContent = "This is a sample file for integration testing of the FileCompressor application. Adding some random characters aasdfsf\n" +
                "sdfskdvjdsdnksdflksdfslflnfksdbhj skjdfs lnsdnfk jsfsd kfsdfhksdf.";

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                fileContent.getBytes()
        );

        when(huffmanEncoder.getKey(any())).thenReturn(Collections.emptyMap());
        String encodedStr = "21,22,50";
        when(huffmanEncoder.encodeFileContent(any(), any())).thenReturn(encodedStr);
        when(objectMapper.writeValueAsString(any())).thenReturn("{}");

        FileCompressResponse fileCompressResponse = fileCompressorService.compressFile(file);
        assertArrayEquals(encodedStr.getBytes(), fileCompressResponse.getFileContent());
        assertEquals("{}", fileCompressResponse.getKey());
    }

    @Test
    public void decompressFileTest() throws IOException {
        String fileContent = "This is a sample file for integration testing of the FileCompressor application. Adding some random characters aasdfsf\n" +
                "sdfskdvjdsdnksdflksdfslflnfksdbhj skjdfs lnsdnfk jsfsd kfsdfhksdf.";

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                fileContent.getBytes()
        );

        MockMultipartFile key = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                fileContent.getBytes()
        );

        when(objectMapper.readValue(eq(IOUtils.toString(key.getInputStream(), StandardCharsets.UTF_8)), any(TypeReference.class))).thenReturn(Collections.emptyMap());
        String decodedString = "decoded string";
        when(huffmanDecoder.getDecodedContent(any(), any())).thenReturn(decodedString);

        byte[] decodedBytes = fileCompressorService.decompressFile(file, key);
        assertArrayEquals(decodedString.getBytes(), decodedBytes);
    }

}