package com.mlynarz.ardena.repository;

import com.mlynarz.ardena.model.Pass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface PassRepository extends JpaRepository<Pass, Long> {
    @Query("SELECT p FROM Pass p where p.owner.id = :userId and p.noOfRidesPermitted > p.usedRides and p.expirationDate > :now")
    Optional<Pass> findByExpirationDateBeforeAndOwner_IdAndUsedRidesIsLessThanNoOfRidesPermitted(@Param("now")Instant now, @Param("userId")Long userId);
}
