package com.mlynarz.ardena.payload;

import com.mlynarz.ardena.model.Level;

public class UserSummary {
    private Long id;
    private String username;
    private String name;

    private Level level;

    public UserSummary(Long id, String username, String name, Level level) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.level=level;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }
}
