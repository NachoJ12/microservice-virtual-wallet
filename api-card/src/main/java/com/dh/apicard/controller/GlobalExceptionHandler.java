package com.dh.apicard.controller;


import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@RestController
@Log4j2
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleError(HttpServletRequest req, Exception ex) {
        Map<String, Object> result = new HashMap<>();
        result.put("TIMESTAMP", LocalDateTime.now());
        result.put("STATUS", HttpStatus.NOT_FOUND.value());
        result.put("ERROR", ex.getMessage());
        result.put("PATH", new UrlPathHelper().getPathWithinApplication(req));
        return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
    }
}
