package com.mlynarz.ardena.repository;

import com.mlynarz.ardena.model.Horse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HorseRepository extends JpaRepository<Horse, Long> {
    boolean existsByHorseName(String horseName);
    Optional<Horse> findByHorseName(String horseName);
}
