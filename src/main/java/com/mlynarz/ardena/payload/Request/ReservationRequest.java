package com.mlynarz.ardena.payload.Request;

import com.mlynarz.ardena.model.Status;
import com.mlynarz.ardena.model.User;

import javax.validation.constraints.NotNull;

public class ReservationRequest{

    @NotNull
    private Status status;

    private User rider;

    private String horseName;

    public ReservationRequest(){

    }

    public ReservationRequest(@NotNull Status status, User rider, String horseName) {
        this.status = status;
        this.rider = rider;
        this.horseName = horseName;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public User getRider() {
        return rider;
    }

    public void setRider(User rider) {
        this.rider = rider;
    }

    public String getHorseName() {
        return horseName;
    }

    public void setHorseName(String horseName) {
        this.horseName = horseName;
    }
}
