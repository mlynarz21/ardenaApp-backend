package com.capgemini.parnextgen.util;

import com.capgemini.parnextgen.model.*;
import com.capgemini.parnextgen.payload.ChoiceResponse;
import com.capgemini.parnextgen.payload.PollResponse;
import com.capgemini.parnextgen.payload.Response.*;
import com.capgemini.parnextgen.payload.UserSummary;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
        lessonResponse.setDate(Date.from(lesson.getDate()));
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
        return new UserSummary(user.getId(), user.getUsername(), user.getName());
    }

    public static RoleResponse mapRoleToRoleResponse(Role role){
        return new RoleResponse(role.getName());
    }
}
