package com.psjw.dmaker.controller;


import com.psjw.dmaker.service.DMakerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class DMakerController {
    private final DMakerService dMakerService;


    @GetMapping("/developers")

    public List<String> getAllDevelopers() {
        // GET /developers HTTP/1.1
        log.info("GET /developers HTTP/1.1");

        return Arrays.asList("snow", "elsa", "Olaf");
    }

    @GetMapping("/create-developer")
    public List<String> createDeveloper() {
        // GET /developers HTTP/1.1
        log.info("GET /create-developer HTTP/1.1");
        dMakerService.createDeveloper();
        return Collections.singletonList("Olaf");
    }
}
