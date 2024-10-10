package com.cywalk.spring_boot.Users;

import java.util.Optional;

public class Key {
    private Optional<Long> key;

    public Key(Optional<Long> key) {
        this.key = key;
    }

    public Key() {

    }

    public Optional<Long> getKey() {
        return key;
    }

    public void setKey(Long key) {
        this.key = Optional.of(key);
    }
}
