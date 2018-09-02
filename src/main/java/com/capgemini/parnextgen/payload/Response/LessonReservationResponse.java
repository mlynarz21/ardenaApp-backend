package com.capgemini.parnextgen.payload.Response;

import com.capgemini.parnextgen.model.Status;
import com.capgemini.parnextgen.payload.UserSummary;

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
