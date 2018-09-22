package com.mlynarz.ardena.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "lessons")
public class Lesson implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Level lessonLevel;

    @NotNull
    private Instant date;

    @JsonIgnore
    @OneToMany(
            mappedBy = "lesson",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Reservation> reservations = new ArrayList<>();

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User instructor;

    public Lesson(){

    }

    public Lesson(Level lessonLevel, @NotNull Instant date, User instructor) {
        this.lessonLevel = lessonLevel;
        this.date = date;
        this.instructor = instructor;
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

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public User getInstructor() {
        return instructor;
    }

    public void setInstructor(User instructor) {
        this.instructor = instructor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lesson lesson = (Lesson) o;
        return lessonLevel == lesson.lessonLevel &&
                Objects.equals(date, lesson.date) &&
                Objects.equals(reservations, lesson.reservations) &&
                Objects.equals(instructor, lesson.instructor);
    }

    @Override
    public int hashCode() {

        return Objects.hash(lessonLevel, date, reservations, instructor);
    }
}
