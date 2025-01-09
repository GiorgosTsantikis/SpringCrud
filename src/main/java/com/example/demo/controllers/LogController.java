package com.example.demo.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/logs")
public class LogController {

    private static final Logger frontendLogger = LoggerFactory.getLogger("com.example.logs.frontend");


    @PostMapping("/sendLogs")
    public ResponseEntity<String> receiveLogs(@RequestBody Object logs){
            frontendLogger.debug("received {}",logs);
            return ResponseEntity.ok("received logs");
    }
}
