package com.example;

import com.example.mongo.MongoApp;
import com.example.mongo.Person;
import com.example.mongo.PersonRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MongoApp.class)
public class MongoTests {


    @Autowired
    private PersonRepository personRepository;

    @Test
    public void test(){
        Person person = new Person("iselin", 28);
        personRepository.save(person);
    }
}
