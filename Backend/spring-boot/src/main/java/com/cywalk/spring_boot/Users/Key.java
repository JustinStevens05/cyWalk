package com.cywalk.spring_boot.Users;

import java.util.Optional;

public class Key {
    private long key;

    public Key(long key) {
        this.key = key;
    }

    public Key() {

    }

    public Long getKey() {
        return key;
    }

    public void setKey(Long key) {
        this.key = key;
    }
}
