package com.aditi.springkafka.models;

import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class User2 {
    private Date date;

    public User2(Date date) {
        this.date = date;
    }

    public User2() {
    }

    public Date getDate() {
        return date;
    }
}
