package com.artmarket.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/img")
@RequiredArgsConstructor
public class ImageController {

    @GetMapping()
    public String test() {
        return "test";
    }

    @GetMapping("/display")
    public ResponseEntity<Resource> getFile(@Param("fileSource") String fileSource){

        Resource resource = new FileSystemResource(fileSource);
        if(!resource.exists()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        HttpHeaders header = new HttpHeaders();
        Path filePath = null;

        try{
            filePath = Paths.get(fileSource);
            header.add("Content-Type", Files.probeContentType(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(resource,header, HttpStatus.OK);

    }
}
