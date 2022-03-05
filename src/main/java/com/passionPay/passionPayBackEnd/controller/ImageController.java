package com.passionPay.passionPayBackEnd.controller;

import com.passionPay.passionPayBackEnd.service.S3UploaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class ImageController {

    private final S3UploaderService s3UploaderService;

    @Autowired
    public ImageController(S3UploaderService s3UploaderService) {
        this.s3UploaderService = s3UploaderService;
    }

    @PostMapping("/images")
    public String upload(@RequestParam("images") MultipartFile multipartFile) throws IOException {
        return s3UploaderService.upload(multipartFile, "static");
    }

    @DeleteMapping("/images/{filename}")
    public ResponseEntity<String> deleteFile(@PathVariable("filename") String filename) {
        return new ResponseEntity<>(s3UploaderService.deleteFile(filename), HttpStatus.OK);
    }

    @GetMapping("/test")
    public String test() {
        return "Hello, World!";
    }
}
