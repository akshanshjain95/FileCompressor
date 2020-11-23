package com.file.compressor.FileCompressor.controllers;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;

public class FileCompressorControllerTest extends AbstractTest {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Test
    public void fileCompressorTest() throws Exception {
        String uri = "/process/compress";

        String fileContent = "This is a sample file for integration testing of the FileCompressor application. Adding some random characters aasdfsf\n" +
                "sdfskdvjdsdnksdflksdfslflnfksdbhj skjdfs lnsdnfk jsfsd kfsdfhksdf.";

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                fileContent.getBytes()
        );

        MvcResult mvcResult = mvc.perform(multipart(uri).file(file)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(201, status);

        String content = mvcResult.getResponse().getContentAsString();
        assertNotNull(content);

        String key = mvcResult.getResponse().getHeader("key");
        assertNotNull(key);

        MockMultipartFile encodedFile = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                content.getBytes()
        );

        MockMultipartFile keyFile = new MockMultipartFile(
                "key",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                key.getBytes()
        );

        String decompressUri = "/process/decompress";
        MvcResult decodedResult = mvc.perform(multipart(decompressUri).file(encodedFile).file(keyFile)).andReturn();

        String decodedContent = decodedResult.getResponse().getContentAsString();
        assertNotNull(decodedContent);

        assertEquals(fileContent, decodedContent);
    }

    @Test
    public void compressFileTestWhenNoFileIsGiven() throws Exception {
        String uri = "/process/compress";
        MvcResult mvcResult = mvc.perform(multipart(uri)).andReturn();

        assertEquals(400, mvcResult.getResponse().getStatus());
    }

    @Test
    public void decompressFileTestWhenNoFileIsGiven() throws Exception {
        String uri = "/process/decompress";
        MvcResult mvcResult = mvc.perform(multipart(uri)).andReturn();

        assertEquals(400, mvcResult.getResponse().getStatus());
    }

    @Test
    public void decompressFileTestWhenNoKeyIsGiven() throws Exception {
        String uri = "/process/decompress";

        String fileContent = "This is a sample file for integration testing of the FileCompressor application. Adding some random characters aasdfsf\n" +
                "sdfskdvjdsdnksdflksdfslflnfksdbhj skjdfs lnsdnfk jsfsd kfsdfhksdf.";

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                fileContent.getBytes()
        );

        MvcResult mvcResult = mvc.perform(multipart(uri).file(file)).andReturn();

        assertEquals(400, mvcResult.getResponse().getStatus());
    }

}