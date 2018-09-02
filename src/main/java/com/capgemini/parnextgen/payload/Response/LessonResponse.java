package com.capgemini.parnextgen.payload.Response;

import com.capgemini.parnextgen.model.Level;
import com.capgemini.parnextgen.payload.UserSummary;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

public class LessonResponse{

    private Long id;

    private Level lessonLevel;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm")
    @NotNull
    private Date date;

    private UserSummary instructor;

    private List<LessonReservationResponse> reservations;

    public LessonResponse(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Level getLessonLevel() {
        return lessonLevel;
    }

    public void setLessonLevel(Level lessonLevel) {
        this.lessonLevel = lessonLevel;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public UserSummary getInstructor() {
        return instructor;
    }

    public void setInstructor(UserSummary instructor) {
        this.instructor = instructor;
    }

    public List<LessonReservationResponse> getReservations() {
        return reservations;
    }

    public void setReservations(List<LessonReservationResponse> reservations) {
        this.reservations = reservations;
    }
}
