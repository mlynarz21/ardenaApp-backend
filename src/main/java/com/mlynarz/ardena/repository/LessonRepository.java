package com.mlynarz.ardena.repository;

import com.mlynarz.ardena.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findByInstructor_IdAndDateGreaterThanEqualOrderByDate(long id, Instant now);
    List<Lesson> findByDateGreaterThanEqual(Instant now);
    List<Lesson> findByDateGreaterThanEqualAndDateBetweenOrderByDate(Instant now, Instant dayStart, Instant dayEnd);
    List<Lesson> findByInstructor_IdAndDateGreaterThanEqualAndDateBetweenOrderByDate(long instructorId, Instant now, Instant dayStart, Instant dayEnd);
}
