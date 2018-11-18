package com.mlynarz.ardena.service;

import com.mlynarz.ardena.exception.BadRequestException;
import com.mlynarz.ardena.exception.ResourceNotFoundException;
import com.mlynarz.ardena.model.*;
import com.mlynarz.ardena.payload.Request.ReservationRequest;
import com.mlynarz.ardena.payload.Response.ReservationResponse;
import com.mlynarz.ardena.repository.HorseRepository;
import com.mlynarz.ardena.repository.LessonRepository;
import com.mlynarz.ardena.repository.ReservationRepository;
import com.mlynarz.ardena.repository.UserRepository;
import com.mlynarz.ardena.security.jwt.UserPrincipal;
import com.mlynarz.ardena.util.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.mlynarz.ardena.model.Status.*;

@Service
public class ReservationService {
    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    LessonRepository lessonRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    HorseRepository horseRepository;

    @Autowired
    PassService passService;

    private static final int CANCELLATION_TIME = 24;
    private static final int HORSE_MAX_OCCURRENCE = 3;
    public static final int MAX_USERS_ON_LESSON = 5;

    public Reservation addReservation(Long lessonId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> new ResourceNotFoundException("Lesson", "id", lessonId));
        if (reservationRepository.existsByStatusIsNotAndLesson_IdAndRider_Id(Status.Cancelled, lessonId, userId))
            throw new BadRequestException("This rider already reserved this lesson!");
        if (compareLevels(user.getRiderLevel(), lesson.getLessonLevel()) < 0)
            throw new BadRequestException("This level is too high for this rider");
        if (getActiveReservationCount(lesson.getReservations()) >= MAX_USERS_ON_LESSON)
            throw new BadRequestException("There are too many riders on this lesson already!");

        Reservation newReservation = new Reservation();
        newReservation.setRider(user);
        newReservation.setLesson(lesson);
        newReservation.setStatus(Pending);

        return reservationRepository.save(newReservation);
    }

    public List<ReservationResponse> getReservationsByUser(long userId) {
        List<ReservationResponse> reservationResponses = new ArrayList<>();
        for(Reservation reservation: reservationRepository.findByRider_IdAndLessonDateGreaterThanEqualOrderByLessonDate(userId, Instant.now()))
            reservationResponses.add(ModelMapper.mapReservationToReservationResponse(reservation));

        return reservationResponses;
    }

    public List<ReservationResponse> getReservationHistoryByUser(Long userId) {
        List<ReservationResponse> reservationResponses = new ArrayList<>();
        for(Reservation reservation: reservationRepository.findByRider_IdAndLessonDateIsLessThanOrderByLessonDateDesc(userId, Instant.now()))
            reservationResponses.add(ModelMapper.mapReservationToReservationResponse(reservation));

        return reservationResponses;
    }

    public List<ReservationResponse> getPendingReservationsByInstructor(Long instructorId) {
        List<ReservationResponse> reservationResponses = new ArrayList<>();
        for(Reservation reservation: reservationRepository.findByLesson_Instructor_IdAndStatusAndLessonDateGreaterThanEqualOrderByLessonDate(instructorId, Pending, Instant.now()))
            reservationResponses.add(ModelMapper.mapReservationToReservationResponse(reservation));

        return reservationResponses;
    }

    public List<ReservationResponse> getUnpaidReservationsByUser(Long riderId) {
        List<ReservationResponse> reservationResponses = new ArrayList<>();
        for (Reservation reservation : reservationRepository.findByRider_IdAndStatusAndLessonDateIsLessThanOrderByLessonDate(riderId, Status.Confirmed, Instant.now()))
            reservationResponses.add(ModelMapper.mapReservationToReservationResponse(reservation));

        return reservationResponses;
    }

    public List<ReservationResponse> getUnpaidReservationsByInstructor(Long instructorId) {
        List<ReservationResponse> reservationResponses = new ArrayList<>();
        for (Reservation reservation : reservationRepository.findByLesson_Instructor_IdAndStatusAndLessonDateIsLessThanOrderByLessonDate(instructorId, Status.Confirmed, Instant.now()))
            reservationResponses.add(ModelMapper.mapReservationToReservationResponse(reservation));

        return reservationResponses;
    }

    public void cancelReservation(Long reservationId, Long id) {
        Reservation reservationToCancel = reservationRepository.findById(reservationId).orElseThrow(() -> new ResourceNotFoundException("Reservation", "id",reservationId));
        if(!reservationToCancel.getRider().getId().equals(id) && !reservationToCancel.getLesson().getInstructor().getId().equals(id))
            throw new BadRequestException("You are not the owner of that lesson");
        switch (reservationToCancel.getStatus()){
            case Cancelled:   throw new BadRequestException("This lesson was already cancelled");
            case Paid_cash:   throw new BadRequestException("This lesson was already paid (cash)");
            case Paid_pass:   throw new BadRequestException("This lesson was already paid (pass)");
        }
        if(reservationToCancel.getRider().getId().equals(id) && isLessonAfterCancellationTime(reservationToCancel.getLesson().getDate()))
            throw new BadRequestException("You cannot cancel reservation later than " + CANCELLATION_TIME + " hours before");

        reservationToCancel.setStatus(Cancelled);
        reservationToCancel.setHorse(null);
        reservationRepository.save(reservationToCancel);
    }

    public void acceptReservation(Long reservationId, Long instructorId) {
        Reservation reservationToAccept = reservationRepository.findById(reservationId).orElseThrow(() -> new ResourceNotFoundException("Reservation", "id",reservationId));
        if(!reservationToAccept.getLesson().getInstructor().getId().equals(instructorId))
            throw new BadRequestException("You are not the owner of that lesson");
        reservationToAccept.setStatus(Confirmed);
        reservationRepository.save(reservationToAccept);
    }

    public void payForReservationWithCash(long reservationId, long userId){
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id",userId));
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() -> new ResourceNotFoundException("Reservation", "id",reservationId));
        if(reservation.getStatus()!=Status.Confirmed){
            throw new BadRequestException("This reservation was already paid or is not obliged to pay");
        } else if(user.getId()==reservation.getLesson().getInstructor().getId()) {
            reservation.setStatus(Status.Paid_cash);
            reservationRepository.save(reservation);
        }
        else throw new BadRequestException("You are not the instructor of that lesson");
    }

    public void payForReservationWithPass(long reservationId, long userId){
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() -> new ResourceNotFoundException("Reservation", "id",reservationId));
        if(reservation.getLesson().getInstructor().getId()!=userId && reservation.getRider().getId()!=userId)
            throw new BadRequestException("You are not the owner of that lesson");
        Pass userPass = passService.getValidPass(reservation.getRider().getId());
        if(reservation.getStatus()!=Status.Confirmed){
            throw new BadRequestException("This reservation was already paid or is not obliged to pay");
        } else {
            reservation.setStatus(Status.Paid_pass);
            reservationRepository.save(reservation);
            userPass.setUsedRides(userPass.getUsedRides()+1);
            passService.updatePass(userPass);
        }

    }

    public void updateReservation(Long reservationId, @Valid ReservationRequest reservationRequest, UserPrincipal currentUser) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() -> new ResourceNotFoundException("Reservation", "id",reservationId));
        if(reservation.getLesson().getInstructor().getId()!=currentUser.getId()){
            throw new BadRequestException("You are not the owner of that lesson");
        }
        else {
            Horse horse = horseRepository.findByHorseName(reservationRequest.getHorseName()).orElseThrow(() -> new ResourceNotFoundException("Horse", "name",reservationRequest.getHorseName()));
            User user = userRepository.findById(reservationRequest.getRider().getId()).orElseThrow(() -> new ResourceNotFoundException("User", "id",reservationRequest.getRider().getId()));

            if(reservation.getStatus().equals(Status.Cancelled))
                throw new BadRequestException("This reservation was cancelled!");
            if(compareLevels(user.getRiderLevel(),horse.getHorseLevel())<0)
                throw new BadRequestException("This horse has higher level than rider!");

            if(canHorseBeAssigned(reservation, horse)) {
                reservation.setStatus(reservationRequest.getStatus());
                reservation.setRider(user);
                reservation.setHorse(horse);
                reservationRepository.save(reservation);
            }
            else throw new BadRequestException("This horse cannot be asigned!");
        }
    }

    private boolean isLessonAfterCancellationTime(Instant instant){
        return Instant.now().isAfter(instant.minus(Duration.ofHours(CANCELLATION_TIME)));
    }

    private boolean canHorseBeAssigned(Reservation reservation, Horse horse){
        int horseOccurrence=0;

        for (Reservation r: reservation.getLesson().getReservations()) {
            if(horse.equals(r.getHorse()))
                return false;
        }

        for (Lesson l: getAllLessonsForDay(reservation)) {
            for (Reservation r :l.getReservations()) {
                if(horse.equals(r.getHorse())){
                    horseOccurrence++;
                }
            }
        }

        return horseOccurrence < HORSE_MAX_OCCURRENCE;
    }

    private List<Lesson> getAllLessonsForDay(Reservation reservation){
        Instant dayStart = Instant.now();
        Instant dayEnd = Instant.now();
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(Date.from(reservation.getLesson().getDate()));
        calendar1.set(calendar1.get(Calendar.YEAR),calendar1.get(Calendar.MONTH),calendar1.get(Calendar.DAY_OF_MONTH),0,0,0);
        dayStart = calendar1.toInstant();
        dayEnd = dayStart.plus(Duration.ofHours(23));
        dayEnd = dayEnd.plus(Duration.ofMinutes(59));

        return lessonRepository.findByDateBetween(dayStart,dayEnd);
    }

    private int getActiveReservationCount(List<Reservation> reservations) {
        int counter=0;
        for (Reservation r : reservations) {
            if(!r.getStatus().equals(Status.Cancelled))
                counter++;
        }
        return counter;
    }

    private int compareLevels(Level l1, Level other) {
        if(other.ordinal() < l1.ordinal())
            return 1;
        else if(other.ordinal() > l1.ordinal())
            return -1;
        else
            return 1;
    }
}
