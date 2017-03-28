package com.example.rest;


import org.springframework.web.bind.annotation.PostMapping;

@org.springframework.web.bind.annotation.RestController
public class RestController {

    @PostMapping("/rest")
    public Object post(String name, String age){
        return name + "," + age;
    }
}
