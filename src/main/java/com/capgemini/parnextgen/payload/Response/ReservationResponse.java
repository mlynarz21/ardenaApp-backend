package com.capgemini.parnextgen.payload.Response;

import com.capgemini.parnextgen.model.Status;
import com.capgemini.parnextgen.payload.UserSummary;

public class ReservationResponse {

    private Long id;

    private Status status;

    private LessonResponse lesson;

    private HorseResponse horse;

    private UserSummary rider;

    public ReservationResponse(){

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

    public LessonResponse getLesson() {
        return lesson;
    }

    public void setLesson(LessonResponse lesson) {
        this.lesson = lesson;
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
