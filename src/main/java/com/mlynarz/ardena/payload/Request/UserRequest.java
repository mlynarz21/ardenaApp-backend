package com.mlynarz.ardena.payload.Request;
import com.mlynarz.ardena.model.Level;

public class UserRequest {

    private String name;
    private String username;
    private Level level;

    public UserRequest() {

    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
