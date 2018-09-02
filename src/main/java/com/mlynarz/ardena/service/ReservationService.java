package com.mlynarz.ardena.service;

import com.mlynarz.ardena.exception.BadRequestException;
import com.mlynarz.ardena.exception.ConflictException;
import com.mlynarz.ardena.exception.ResourceNotFoundException;
import com.mlynarz.ardena.model.*;
import com.mlynarz.ardena.payload.Response.ReservationResponse;
import com.mlynarz.ardena.repository.LessonRepository;
import com.mlynarz.ardena.repository.ReservationRepository;
import com.mlynarz.ardena.repository.UserRepository;
import com.mlynarz.ardena.util.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReservationService {
    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    LessonRepository lessonRepository;

    @Autowired
    UserRepository userRepository;

    public Reservation addReservation(Long lessonId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id",userId));
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> new ResourceNotFoundException("Lesson", "id",lessonId));
        if(reservationRepository.existsByLesson_IdAndRider_Id(lessonId,userId))
            throw new BadRequestException("This rider already reserved this lesson!");
        if(compareLevels(user.getRiderLevel(),lesson.getLessonLevel())<0)
            throw new BadRequestException("This level is too high for this rider");

        Reservation newReservation = new Reservation();
        newReservation.setRider(user);
        newReservation.setLesson(lesson);
        newReservation.setStatus(Status.Pending);

        return reservationRepository.save(newReservation);
    }

    private int compareLevels(Level l1, Level other) {
        if(other.ordinal() < l1.ordinal())
            return 1;
        else if(other.ordinal() > l1.ordinal())
            return -1;
        else
            return 1;
    }

    public List<ReservationResponse> getReservationsByUser(long userId) {
        List<ReservationResponse> reservationResponses = new ArrayList<>();
        for(Reservation reservation: reservationRepository.findByRider_IdAndLessonDateGreaterThanEqualOrderByLessonDate(userId, Instant.now()))
            reservationResponses.add(ModelMapper.mapReservationToReservationResponse(reservation));

        return reservationResponses;
    }

    public List<ReservationResponse> getPendingReservationsByInstructor(Long instructorId) {
        List<ReservationResponse> reservationResponses = new ArrayList<>();
        for(Reservation reservation: reservationRepository.findByLesson_Instructor_IdAndStatusAndLessonDateGreaterThanEqualOrderByLessonDate(instructorId,Status.Pending, Instant.now()))
            reservationResponses.add(ModelMapper.mapReservationToReservationResponse(reservation));

        return reservationResponses;
    }

    public void cancelReservation(Long reservationId, Long id) {
        Reservation reservationToCancel = reservationRepository.findById(reservationId).orElseThrow(() -> new ResourceNotFoundException("Reservation", "id",reservationId));
        if(!reservationToCancel.getRider().getId().equals(id) || !reservationToCancel.getLesson().getInstructor().getId().equals(id))
            throw new ConflictException("You are not the owner of that lesson");
        reservationToCancel.setStatus(Status.Cancelled);
        reservationRepository.save(reservationToCancel);
    }

    public void acceptReservation(Long reservationId, Long instructorId) {
        Reservation reservationToAccept = reservationRepository.findById(reservationId).orElseThrow(() -> new ResourceNotFoundException("Reservation", "id",reservationId));
        if(!reservationToAccept.getLesson().getInstructor().getId().equals(instructorId))
            throw new ConflictException("You are not the owner of that lesson");
        reservationToAccept.setStatus(Status.Confirmed);
        reservationRepository.save(reservationToAccept);
    }
}
