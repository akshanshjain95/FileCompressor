package com.file.compressor.FileCompressor.controllers;

import com.file.compressor.FileCompressor.models.FileCompressResponse;
import com.file.compressor.FileCompressor.services.api.FileCompressorService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/process")
public class FileCompressorController {

    @Autowired
    FileCompressorService fileCompressorService;

    @PostMapping(value = "/compress", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> compressFile(@RequestParam("file") MultipartFile file) {
        FileCompressResponse response = fileCompressorService.compressFile(file);
        String fileName = FilenameUtils.getBaseName(file.getOriginalFilename());

        return ResponseEntity.status(HttpStatus.CREATED).header("key", response.getKey())
                .header("Content-Disposition", "attachment; filename=\"" + fileName + "-compressed.txt\"")
                .body(response.getFileContent());
    }

    @PostMapping(value = "/decompress", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> decompressFile(@RequestParam("file") MultipartFile file,
                                                 @RequestParam("key") MultipartFile key) {
        byte[] decodedFileBytes = fileCompressorService.decompressFile(file, key);
        String fileName = FilenameUtils.getBaseName(file.getOriginalFilename());

        return ResponseEntity.status(HttpStatus.CREATED)
                .header("Content-Disposition", "attachment; filename=\"" + fileName + "-decompressed.txt\"")
                .body(decodedFileBytes);
    }
}
