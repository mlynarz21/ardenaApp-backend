package com.mlynarz.ardena.payload;

import javax.validation.constraints.NotBlank;

public class NameRequest {
    @NotBlank
    private String newName;

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }
}
