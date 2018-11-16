package com.mlynarz.ardena.payload.Response;

import com.mlynarz.ardena.model.Level;

public class HorseResponse {
    private long id;
    private String horseName;
    private Level horseLevel;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getHorseName() {
        return horseName;
    }

    public void setHorseName(String horseName) {
        this.horseName = horseName;
    }

    public Level getHorseLevel() {
        return horseLevel;
    }

    public void setHorseLevel(Level horseLevel) {
        this.horseLevel = horseLevel;
    }
}
