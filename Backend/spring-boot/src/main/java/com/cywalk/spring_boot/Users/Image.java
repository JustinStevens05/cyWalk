package com.cywalk.spring_boot.Users;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Image {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private int id;
    private String filepath;

    public Image() {

    }

    public Image(int id, String filepath) {
        this.id = id;
        this.filepath = filepath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }
}
