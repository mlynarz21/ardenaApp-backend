package com.mlynarz.ardena.payload.Request;

import com.mlynarz.ardena.model.Level;

public class HorseRequest {

    private String horseName;
    private Level horseLevel;

    public HorseRequest() {
    }

    public HorseRequest(String horseName, Level horseLevel) {
        this.horseName = horseName;
        this.horseLevel = horseLevel;
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
