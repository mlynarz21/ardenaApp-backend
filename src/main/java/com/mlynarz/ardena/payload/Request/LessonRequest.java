package com.mlynarz.ardena.payload.Request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mlynarz.ardena.model.Level;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Date;

public class LessonRequest {

    private Level lessonLevel;

    private String instructor;

//    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm")
    @NotNull
    private Instant date;

    public LessonRequest(){

    }

    public LessonRequest(Level lessonLevel, String instructor, @NotNull Instant date) {
        this.lessonLevel = lessonLevel;
        this.instructor = instructor;
        this.date = date;
    }

    public Level getLessonLevel() {
        return lessonLevel;
    }

    public void setLessonLevel(Level lessonLevel) {
        this.lessonLevel = lessonLevel;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }
}
