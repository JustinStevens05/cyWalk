package com.cywalk.spring_boot.Users;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "A model for the secret key")
public class Key {
    @Schema(description = "A key")
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
