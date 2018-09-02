package com.capgemini.parnextgen.repository;

import com.capgemini.parnextgen.model.Reservation;
import com.capgemini.parnextgen.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    boolean existsByLesson_IdAndRider_Id(long lessonId, long riderId);
    List<Reservation> findByRider_IdAndLessonDateGreaterThanEqualOrderByLessonDate(long riderId, Instant now);
    List<Reservation> findByLesson_Instructor_IdAndStatusAndLessonDateGreaterThanEqualOrderByLessonDate(long riderId, Status status, Instant now);
}

