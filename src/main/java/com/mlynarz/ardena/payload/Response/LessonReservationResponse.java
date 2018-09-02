package com.mlynarz.ardena.payload.Response;

import com.mlynarz.ardena.model.Status;
import com.mlynarz.ardena.payload.UserSummary;

public class LessonReservationResponse {

    private Long id;

    private Status status;

    private HorseResponse horse;

    private UserSummary rider;

    public LessonReservationResponse(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public HorseResponse getHorse() {
        return horse;
    }

    public void setHorse(HorseResponse horse) {
        this.horse = horse;
    }

    public UserSummary getRider() {
        return rider;
    }

    public void setRider(UserSummary rider) {
        this.rider = rider;
    }
}
