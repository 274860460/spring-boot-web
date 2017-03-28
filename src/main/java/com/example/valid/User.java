package com.example.valid;

import org.hibernate.validator.constraints.Length;

/**
 * Created by Quiet on 2017/1/12.
 */
public class User {


    @Length(min = 1, max = 10)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
