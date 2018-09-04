package com.mlynarz.ardena.repository;

import com.mlynarz.ardena.model.Reservation;
import com.mlynarz.ardena.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    boolean existsByStatusIsNotAndAndLesson_IdAndRider_Id(Status status, long lessonId, long riderId);
    List<Reservation> findByRider_IdAndLessonDateGreaterThanEqualOrderByLessonDate(long riderId, Instant now);
    List<Reservation> findByLesson_Instructor_IdAndStatusAndLessonDateGreaterThanEqualOrderByLessonDate(long instructorId, Status status, Instant now);
    List<Reservation> findByRider_IdAndLessonDateIsLessThanOrderByLessonDateDesc(long riderId, Instant now);
    List<Reservation> findByLesson_Instructor_IdAndStatusAndLessonDateIsLessThanOrderByLessonDate(long instructorId, Status status, Instant now);
    List<Reservation> findByRider_IdAndStatusAndLessonDateIsLessThanOrderByLessonDate(long instructorId, Status status, Instant now);

}
