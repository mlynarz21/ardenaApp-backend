package com.mlynarz.ardena.repository;

import com.mlynarz.ardena.model.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    Optional<Event> findById(Long eventId);

    Page<Event> findByCreatedBy(Long userId, Pageable pageable);

    long countByCreatedBy(Long userId);

    List<Event> findByIdIn(List<Long> eventIds);

    List<Event> findByIdIn(List<Long> eventIds, Sort sort);
}
