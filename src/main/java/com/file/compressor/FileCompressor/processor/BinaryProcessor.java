package com.file.compressor.FileCompressor.processor;

import org.springframework.stereotype.Component;

@Component
public class BinaryProcessor {

    public String getBinaryToIntEncodedString(String content) {
        StringBuilder finalEncode = new StringBuilder();

        for (int i = 0; i < content.length(); i += 8) {
            String chunk;
            if (i + 8 < content.length())
                chunk = content.substring(i, i + 8);
            else
                chunk = content.substring(i);

            finalEncode.append(Integer.parseInt(chunk, 2)).append(",");
        }

        //removing last comma.
        return finalEncode.substring(0, finalEncode.length() - 1);
    }

    public String getIntToBinaryDecodedString(int[] content) {
        StringBuilder encodedBinary = new StringBuilder();

        for (int encodedInt : content) {
            encodedBinary.append(getEigthLengthedBinaryString(encodedInt));
        }

        return encodedBinary.toString();
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
