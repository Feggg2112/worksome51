package com.easylive.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController/**
 * @author Feggg
 * @date 2025/4/1 11.
 */
public class TestController {
    @RequestMapping("/test")
    public String test(){
        return "Hello World";
    }
}
