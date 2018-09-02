package com.mlynarz.ardena.repository;

import com.mlynarz.ardena.model.Horse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HorseRepository extends JpaRepository<Horse, Long> {
    boolean existsByHorseName(String horseName);
}
