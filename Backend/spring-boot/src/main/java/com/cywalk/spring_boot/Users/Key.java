package com.cywalk.spring_boot.Users;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "A model for the secret key")
public class Key {
    @Schema(description = "A key")
    private long id;

    public Key(long key) {
        this.id = key;
    }

    public Key() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
