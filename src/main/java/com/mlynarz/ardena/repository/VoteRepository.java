package com.mlynarz.ardena.repository;

import com.mlynarz.ardena.model.OptionVoteCount;
import com.mlynarz.ardena.model.Vote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    @Query("SELECT NEW com.mlynarz.ardena.model.OptionVoteCount(v.option.id, count(v.id)) FROM Vote v WHERE v.event.id in :eventIds GROUP BY v.option.id")
    List<OptionVoteCount> countByEventIdInGroupByOptionId(@Param("eventIds") List<Long> eventIds);

    @Query("SELECT NEW com.mlynarz.ardena.model.OptionVoteCount(v.option.id, count(v.id)) FROM Vote v WHERE v.event.id = :eventIds GROUP BY v.option.id")
    List<OptionVoteCount> countByEventIdGroupByOptionId(@Param("eventIds") Long eventIds);

    @Query("SELECT v FROM Vote v where v.user.id = :userId and v.event.id in :eventIds")
    List<Vote> findByUserIdAndEventIdIn(@Param("userId") Long userId, @Param("eventIds") List<Long> eventIds);

    @Query("SELECT v FROM Vote v where v.user.id = :userId and v.event.id = :eventIds")
    Vote findByUserIdAndEventId(@Param("userId") Long userId, @Param("eventIds") Long eventIds);

    @Query("SELECT COUNT(v.id) from Vote v where v.user.id = :userId")
    long countByUserId(@Param("userId") Long userId);

    @Query("SELECT v.event.id FROM Vote v WHERE v.user.id = :userId")
    Page<Long> findVotedEventIdsByUserId(@Param("userId") Long userId, Pageable pageable);
}

