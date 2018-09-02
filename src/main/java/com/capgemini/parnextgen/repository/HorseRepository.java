package com.capgemini.parnextgen.repository;

import com.capgemini.parnextgen.model.Horse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HorseRepository extends JpaRepository<Horse, Long> {
    boolean existsByHorseName(String horseName);
}
