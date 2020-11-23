package com.file.compressor.FileCompressor.processor;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class BinaryProcessorTest {

    @InjectMocks
    private BinaryProcessor binaryProcessor;

    @Test
    public void getBinaryToIntEncodedStringTest() {
        String binaryString = "0000000100000010";
        String intString = "1,2";

        String binaryToIntEncodedString = binaryProcessor.getBinaryToIntEncodedString(binaryString);

        assertNotNull(binaryToIntEncodedString);
        assertEquals(intString, binaryToIntEncodedString);
    }

    @Test
    public void getIntToBinaryDecodedStringTest() {
        String binaryString = "0000000100000010";

        String binaryToIntEncodedString = binaryProcessor.getIntToBinaryDecodedString(new int[]{1,2});

        assertNotNull(binaryToIntEncodedString);
        assertEquals(binaryString, binaryToIntEncodedString);
    }

}