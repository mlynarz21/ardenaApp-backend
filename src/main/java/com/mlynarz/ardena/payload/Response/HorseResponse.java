package com.mlynarz.ardena.payload.Response;

public class HorseResponse {
    private long id;
    private String horseName;

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
}
