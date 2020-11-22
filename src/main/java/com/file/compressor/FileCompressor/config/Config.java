package com.file.compressor.FileCompressor.config;

import com.file.compressor.FileCompressor.services.api.FileCompressorService;
import com.file.compressor.FileCompressor.services.impl.FileCompressorServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Bean
    public FileCompressorService fileCompressorService() {
        return new FileCompressorServiceImpl();
    }
}
