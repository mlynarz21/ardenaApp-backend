package com.mlynarz.ardena.util;

import com.mlynarz.ardena.model.*;
import com.mlynarz.ardena.payload.Response.OptionResponse;
import com.mlynarz.ardena.payload.Response.EventResponse;
import com.mlynarz.ardena.payload.Response.*;
import com.mlynarz.ardena.payload.UserSummary;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class ModelMapper {

    public static EventResponse mapEventToEventResponse(Event event, Map<Long, Long> optionVotesMap, User creator, Long userVote) {
        EventResponse eventResponse = new EventResponse();
        eventResponse.setId(event.getId());
        eventResponse.setDescription(event.getDescription());
        eventResponse.setCreationDateTime(event.getCreatedAt());
        eventResponse.setEventDate(event.getEventDate());
        Instant now = Instant.now();
        eventResponse.setExpired(event.getEventDate().isBefore(now));

        List<OptionResponse> optionRespons = event.getOptions().stream().map(option -> {
            OptionResponse optionResponse = new OptionResponse();
            optionResponse.setId(option.getId());
            optionResponse.setText(option.getText());

            if (optionVotesMap.containsKey(option.getId())) {
                optionResponse.setVoteCount(optionVotesMap.get(option.getId()));
            } else {
                optionResponse.setVoteCount(0);
            }
            return optionResponse;
        }).collect(Collectors.toList());

        eventResponse.setOptions(optionRespons);
        eventResponse.setCreatedBy(mapUserToUserSummary(creator));

        if (userVote != null) {
            eventResponse.setSelectedOption(userVote);
        }

        long totalVotes = eventResponse.getOptions().stream().mapToLong(OptionResponse::getVoteCount).sum();
        eventResponse.setTotalVotes(totalVotes);

        return eventResponse;
    }

    public static HorseResponse mapHorseToHorseResponse(Horse horse) {
        HorseResponse horseResponse = new HorseResponse();
        horseResponse.setHorseName(horse.getHorseName());
        horseResponse.setId(horse.getId());

        return horseResponse;
    }

    public static LessonResponse mapLessonToLessonResponse(Lesson lesson) {
        LessonResponse lessonResponse = new LessonResponse();
        lessonResponse.setId(lesson.getId());
        lessonResponse.setDate(lesson.getDate());
        lessonResponse.setLessonLevel(lesson.getLessonLevel());
        lessonResponse.setInstructor(mapUserToUserSummary(lesson.getInstructor()));
        List<LessonReservationResponse> lessonReservationResponses = new ArrayList<>();
        for (Reservation r:lesson.getReservations()) {
            lessonReservationResponses.add(mapReservationToLessonReservationResponse(r));
        }
        lessonResponse.setReservations(lessonReservationResponses);
        return lessonResponse;
    }

    public static ReservationResponse mapReservationToReservationResponse(Reservation reservation) {
        ReservationResponse reservationResponse = new ReservationResponse();
        reservationResponse.setId(reservation.getId());
        reservationResponse.setLesson(ModelMapper.mapLessonToLessonResponse(reservation.getLesson()));
        reservationResponse.setStatus(reservation.getStatus());
        if(reservation.getHorse()!=null)
            reservationResponse.setHorse(mapHorseToHorseResponse(reservation.getHorse()));
        reservationResponse.setRider(mapUserToUserSummary(reservation.getRider()));
        return reservationResponse;
    }

    public static LessonReservationResponse mapReservationToLessonReservationResponse(Reservation reservation) {
        LessonReservationResponse lessonReservationResponse = new LessonReservationResponse();
        lessonReservationResponse.setId(reservation.getId());
        lessonReservationResponse.setStatus(reservation.getStatus());
        if(reservation.getHorse()!=null)
            lessonReservationResponse.setHorse(mapHorseToHorseResponse(reservation.getHorse()));
        lessonReservationResponse.setRider(mapUserToUserSummary(reservation.getRider()));
        return lessonReservationResponse;
    }

    public static UserSummary mapUserToUserSummary(User user){
        return new UserSummary(user.getId(), user.getUsername(), user.getName(), user.getRiderLevel());
    }

    public static RoleResponse mapRoleToRoleResponse(Role role){
        return new RoleResponse(role.getName());
    }

    public static PassResponse mapPassToPassResponse(Pass pass){
            PassResponse passResponse = new PassResponse();
            passResponse.setExpirationDate(pass.getExpirationDate());
            passResponse.setNoOfRidesPermitted(pass.getNoOfRidesPermitted());
            passResponse.setUsedRides(pass.getUsedRides());
            passResponse.setId(pass.getId());

            return passResponse;
    }
}
