package com.mlynarz.ardena.util;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class Timer {
    public static Instant getNow(){
        return ZonedDateTime.now(ZoneId.of("Europe/Warsaw")).toInstant();
    }
}
