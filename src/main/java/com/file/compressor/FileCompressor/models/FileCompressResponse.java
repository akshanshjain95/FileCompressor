package com.file.compressor.FileCompressor.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@Builder
public class FileCompressResponse {

    private byte[] fileContent;
    private String key;
}
