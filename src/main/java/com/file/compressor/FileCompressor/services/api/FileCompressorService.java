package com.file.compressor.FileCompressor.services.api;

import com.file.compressor.FileCompressor.models.FileCompressResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
public interface FileCompressorService {

    FileCompressResponse compressFile(MultipartFile file);

    byte[] decompressFile(MultipartFile file, MultipartFile key);
}
