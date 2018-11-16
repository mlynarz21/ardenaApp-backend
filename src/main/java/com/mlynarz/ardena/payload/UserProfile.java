package com.mlynarz.ardena.payload;

import com.mlynarz.ardena.model.Level;

import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

public class UserProfile {
    private Long id;
    private String username;
    private String name;
    private Instant joinedAt;
    private Long eventCount;
    private Long voteCount;
    private Level level;
    private String phoneNumber;
    private long age;

    public UserProfile(Long id, String username, String name, Instant joinedAt, Long eventCount, Long voteCount, Level level, String phoneNumber, Instant birthDate) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.joinedAt = joinedAt;
        this.eventCount = eventCount;
        this.voteCount = voteCount;
        this.level = level;
        this.phoneNumber = phoneNumber;
        this.age = ChronoUnit.YEARS.between(birthDate.atZone(ZoneId.systemDefault()),Instant.now().atZone(ZoneId.systemDefault()));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(Instant joinedAt) {
        this.joinedAt = joinedAt;
    }

    public Long getEventCount() {
        return eventCount;
    }

    public void setEventCount(Long eventCount) {
        this.eventCount = eventCount;
    }

    public Long getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Long voteCount) {
        this.voteCount = voteCount;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public long getAge() {
        return age;
    }

    public void setAge(long age) {
        this.age = age;
    }
}
