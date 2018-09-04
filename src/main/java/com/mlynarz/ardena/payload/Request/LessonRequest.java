package com.mlynarz.ardena.payload.Request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mlynarz.ardena.model.Level;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class LessonRequest {

    private Level lessonLevel;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm")
    @NotNull
    private Date date;

    public LessonRequest(){

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

}
