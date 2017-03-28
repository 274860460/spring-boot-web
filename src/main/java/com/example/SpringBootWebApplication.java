package com.example;

import com.example.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Module(name = "boot", parent = "setting")
@SpringBootApplication
public class SpringBootWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootWebApplication.class, args);
    }

    @Autowired
    ResourcesParser resourcesParser;

    @PostConstruct
    public void PostConstruct() {
        try {
            resourcesParser.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Link("/list")
    public void list() {

    }

    @Save(value = "/save", hasButton = false)
    public void save() {

    }

    @Update(value = "/update", button = "This is a update button")
    public void update() {

    }

    @Delete(method = RequestMethod.GET, resourceName = "xxx")
    public void delete() {

    }
}
