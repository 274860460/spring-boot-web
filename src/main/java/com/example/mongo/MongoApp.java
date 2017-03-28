package com.example.mongo;

import com.mongodb.gridfs.GridFSDBFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@SpringBootApplication
public class MongoApp {


    public static void main(String[] args) {
        SpringApplication.run(MongoApp.class, args);
    }

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private GridFsOperations template;
    @Autowired
    private MongoTemplate mongoTemplate;

    @PostConstruct
    public void test() throws IOException {
        FileInputStream in = new FileInputStream(new File("C:\\Users\\Quiet\\Desktop\\dlp.sql"));
        template.store(in, "dlp.sql");
        template.store(in, "dlp.sql");
//        GridFsResource resource = template.getResource("dlp.sql");
//        template.delete(new Query(Criteria.where("filename").is("dlp.sql")));
        GridFSDBFile gridFSDBFile = template.findOne(new Query(Criteria.where("filename").is("dlp.sql")));

        mongoTemplate.save(new Person("iselin", 28));
    }

}