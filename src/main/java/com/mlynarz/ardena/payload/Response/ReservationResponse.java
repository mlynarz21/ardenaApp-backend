package com.mlynarz.ardena.payload.Response;

import com.mlynarz.ardena.model.Status;
import com.mlynarz.ardena.payload.UserSummary;

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
