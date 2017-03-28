package com.example.valid;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class ValidController {

    @GetMapping("valid")
    public Object test(@Valid User user){
        return "0";
    }
}
