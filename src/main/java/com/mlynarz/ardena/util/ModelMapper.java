package com.mlynarz.ardena.util;

import com.mlynarz.ardena.model.*;
import com.mlynarz.ardena.payload.ChoiceResponse;
import com.mlynarz.ardena.payload.PollResponse;
import com.mlynarz.ardena.payload.Response.*;
import com.mlynarz.ardena.payload.UserSummary;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class ModelMapper {

    public static PollResponse mapPollToPollResponse(Poll poll, Map<Long, Long> choiceVotesMap, User creator, Long userVote) {
        PollResponse pollResponse = new PollResponse();
        pollResponse.setId(poll.getId());
        pollResponse.setQuestion(poll.getQuestion());
        pollResponse.setCreationDateTime(poll.getCreatedAt());
        pollResponse.setExpirationDateTime(poll.getExpirationDateTime());
        Instant now = Instant.now();
        pollResponse.setExpired(poll.getExpirationDateTime().isBefore(now));

        List<ChoiceResponse> choiceResponses = poll.getChoices().stream().map(choice -> {
            ChoiceResponse choiceResponse = new ChoiceResponse();
            choiceResponse.setId(choice.getId());
            choiceResponse.setText(choice.getText());

            if (choiceVotesMap.containsKey(choice.getId())) {
                choiceResponse.setVoteCount(choiceVotesMap.get(choice.getId()));
            } else {
                choiceResponse.setVoteCount(0);
            }
            return choiceResponse;
        }).collect(Collectors.toList());

        pollResponse.setChoices(choiceResponses);
        pollResponse.setCreatedBy(mapUserToUserSummary(creator));

        if (userVote != null) {
            pollResponse.setSelectedChoice(userVote);
        }

        long totalVotes = pollResponse.getChoices().stream().mapToLong(ChoiceResponse::getVoteCount).sum();
        pollResponse.setTotalVotes(totalVotes);

        return pollResponse;
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
