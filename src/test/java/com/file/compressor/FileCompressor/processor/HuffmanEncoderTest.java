package com.file.compressor.FileCompressor.processor;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HuffmanEncoderTest {

    @InjectMocks
    private HuffmanEncoder huffmanEncoder;

    @Mock
    private BinaryProcessor binaryProcessor;

    @Test
    public void getKeyTest() {
        Map<Character, String> key = huffmanEncoder.getKey("This is some string.");
        assertNotNull(key);
        assertFalse(key.isEmpty());
    }

    @Test
    public void encodeFileContent() {
        Map<Character, String> key = new HashMap<>();
        key.put('a', "0");
        key.put('b', "1");

        when(binaryProcessor.getBinaryToIntEncodedString(anyString())).thenReturn("1");

        String str = "ab";
        String encodedStr = huffmanEncoder.encodeFileContent(key, str);
        assertEquals("1,2", encodedStr);
    }

}