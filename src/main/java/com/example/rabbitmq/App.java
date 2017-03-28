package com.example.rabbitmq;


import com.example.rabbitmq.service.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class App {


    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Autowired
    private ServiceImpl service;

    @GetMapping("/save")
    public void save(){
        service.save();
    }

    @GetMapping("/get")
    public void get(){
        service.get();
    }


}


