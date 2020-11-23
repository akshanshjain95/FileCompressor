package com.file.compressor.FileCompressor.processor;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.event.annotation.BeforeTestClass;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HuffmanDecoderTest {

    @InjectMocks
    private HuffmanDecoder huffmanDecoder;

    @Mock
    private BinaryProcessor binaryProcessor;

    @Test
    public void getDecodedContentTest() {
        String encodedStr = "1,2";

        Map<Character, String> key = new HashMap<>();
        key.put('a', "0");
        key.put('b', "1");

        when(binaryProcessor.getIntToBinaryDecodedString(any())).thenReturn("01000000");
        String decodedContent = huffmanDecoder.getDecodedContent(encodedStr, key);
        assertEquals("ab", decodedContent);
    }

}