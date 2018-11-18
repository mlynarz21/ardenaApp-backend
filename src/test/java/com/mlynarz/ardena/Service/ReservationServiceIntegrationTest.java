package com.mlynarz.ardena.Service;

import com.mlynarz.ardena.exception.BadRequestException;
import com.mlynarz.ardena.exception.ResourceNotFoundException;
import com.mlynarz.ardena.model.*;
import com.mlynarz.ardena.payload.Request.DateRequest;
import com.mlynarz.ardena.payload.Request.LessonRequest;
import com.mlynarz.ardena.payload.Request.ReservationRequest;
import com.mlynarz.ardena.payload.Response.LessonResponse;
import com.mlynarz.ardena.repository.HorseRepository;
import com.mlynarz.ardena.repository.LessonRepository;
import com.mlynarz.ardena.repository.ReservationRepository;
import com.mlynarz.ardena.repository.UserRepository;
import com.mlynarz.ardena.security.jwt.UserPrincipal;
import com.mlynarz.ardena.service.EmailService;
import com.mlynarz.ardena.service.LessonService;
import com.mlynarz.ardena.service.PassService;
import com.mlynarz.ardena.service.ReservationService;
import com.mlynarz.ardena.util.ModelMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@RunWith(SpringRunner.class)
public class ReservationServiceIntegrationTest {

    @TestConfiguration
    static class ReservationServiceIntegrationTestContextConfiguration {

        @Bean
        public ReservationService reservationService() {
            return new ReservationService();
        }
    }

    @Autowired
    private ReservationService reservationService;

    @MockBean
    private LessonRepository lessonRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private HorseRepository horseRepository;

    @MockBean
    private ReservationRepository reservationRepository;

    @MockBean
    private PassService passService;

    @Test(expected = ResourceNotFoundException.class)
    public void whenAddReservation_noLesson_thenThrowException() {
        String name = "u1";
        String username = "username";
        String email = "email@o2.pl";
        String password = "pass";
        String phoneNumber = "111111111";
        User user = new User(name, username, email, password, phoneNumber, Instant.now());
        user.setId(1L);

        Mockito.when(lessonRepository.findById(1L)).thenReturn(Optional.empty());
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        reservationService.addReservation(1L, 1L);
    }

    @Test(expected = BadRequestException.class)
    public void whenAddReservation_hasReservation_thenThrowException() {
        String name = "u1";
        String username = "username";
        String email = "email@o2.pl";
        String password = "pass";
        String phoneNumber = "111111111";
        User user = new User(name, username, email, password, phoneNumber, Instant.now());
        user.setId(1L);
        Lesson lesson = new Lesson(Level.Basic, Instant.now(), user);
        lesson.setId(1L);

        Mockito.when(lessonRepository.findById(1L)).thenReturn(Optional.of(lesson));
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(reservationRepository.existsByStatusIsNotAndLesson_IdAndRider_Id(Status.Cancelled, 1L, 1L)).thenReturn(true);

        reservationService.addReservation(1L, 1L);
    }

    @Test(expected = BadRequestException.class)
    public void whenAddReservation_noMorePlaces_thenThrowException() {
        String name = "u1";
        String username = "username";
        String email = "email@o2.pl";
        String password = "pass";
        String phoneNumber = "111111111";
        User user = new User(name, username, email, password, phoneNumber, Instant.now());
        user.setId(1L);
        Lesson lesson = new Lesson(Level.Basic, Instant.now(), user);
        lesson.setId(1L);
        lesson.getReservations().add(new Reservation(Status.Confirmed, user, lesson));
        lesson.getReservations().add(new Reservation(Status.Confirmed, user, lesson));
        lesson.getReservations().add(new Reservation(Status.Confirmed, user, lesson));
        lesson.getReservations().add(new Reservation(Status.Confirmed, user, lesson));
        lesson.getReservations().add(new Reservation(Status.Confirmed, user, lesson));

        Mockito.when(lessonRepository.findById(1L)).thenReturn(Optional.of(lesson));
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(reservationRepository.existsByStatusIsNotAndLesson_IdAndRider_Id(Status.Cancelled, 1L, 1L)).thenReturn(false);

        reservationService.addReservation(1L, 1L);
    }

    @Test(expected = BadRequestException.class)
    public void whenAddReservation_higherLevel_thenThrowException() {
        String name = "u1";
        String username = "username";
        String email = "email@o2.pl";
        String password = "pass";
        String phoneNumber = "111111111";
        User user = new User(name, username, email, password, phoneNumber, Instant.now());
        user.setId(1L);
        Lesson lesson = new Lesson(Level.Advanced, Instant.now(), user);
        lesson.setId(1L);

        Mockito.when(lessonRepository.findById(1L)).thenReturn(Optional.of(lesson));
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(reservationRepository.existsByStatusIsNotAndLesson_IdAndRider_Id(Status.Cancelled, 1L, 1L)).thenReturn(false);

        reservationService.addReservation(1L, 1L);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void whenCancelReservation_noReservation_thenThrowException() {
        String name = "u1";
        String username = "username";
        String email = "email@o2.pl";
        String password = "pass";
        String phoneNumber = "111111111";
        User user = new User(name, username, email, password, phoneNumber, Instant.now());
        user.setId(1L);
        Lesson lesson = new Lesson(Level.Advanced, Instant.now(), user);
        lesson.setId(1L);

        Reservation reservation = new Reservation(Status.Confirmed, user, lesson);
        reservation.setId(1L);

        Mockito.when(reservationRepository.findById(1L)).thenReturn(Optional.empty());

        reservationService.cancelReservation(1L, 1L);
    }

    @Test(expected = BadRequestException.class)
    public void whenCancelReservation_notOwner_thenThrowException() {
        String name = "u1";
        String username = "username";
        String email = "email@o2.pl";
        String password = "pass";
        String phoneNumber = "111111111";
        User user = new User(name, username, email, password, phoneNumber, Instant.now());
        user.setId(1L);
        Lesson lesson = new Lesson(Level.Advanced, Instant.now(), user);
        lesson.setId(1L);

        Reservation reservation = new Reservation(Status.Confirmed, user, lesson);
        reservation.setId(1L);

        Mockito.when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));

        reservationService.cancelReservation(1L, 2L);
    }

    @Test(expected = BadRequestException.class)
    public void whenCancelReservation_alreadyCancelled_thenThrowException() {
        String name = "u1";
        String username = "username";
        String email = "email@o2.pl";
        String password = "pass";
        String phoneNumber = "111111111";
        User user = new User(name, username, email, password, phoneNumber, Instant.now());
        user.setId(1L);
        Lesson lesson = new Lesson(Level.Advanced, Instant.now(), user);
        lesson.setId(1L);

        Reservation reservation = new Reservation(Status.Cancelled, user, lesson);
        reservation.setId(1L);

        Mockito.when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));

        reservationService.cancelReservation(1L, 1L);
    }

    @Test(expected = BadRequestException.class)
    public void whenCancelReservation_alreadyPaidCash_thenThrowException() {
        String name = "u1";
        String username = "username";
        String email = "email@o2.pl";
        String password = "pass";
        String phoneNumber = "111111111";
        User user = new User(name, username, email, password, phoneNumber, Instant.now());
        user.setId(1L);
        Lesson lesson = new Lesson(Level.Advanced, Instant.now(), user);
        lesson.setId(1L);

        Reservation reservation = new Reservation(Status.Paid_cash, user, lesson);
        reservation.setId(1L);

        Mockito.when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));

        reservationService.cancelReservation(1L, 1L);
    }

    @Test(expected = BadRequestException.class)
    public void whenCancelReservation_alreadyPaidPass_thenThrowException() {
        String name = "u1";
        String username = "username";
        String email = "email@o2.pl";
        String password = "pass";
        String phoneNumber = "111111111";
        User user = new User(name, username, email, password, phoneNumber, Instant.now());
        user.setId(1L);
        Lesson lesson = new Lesson(Level.Advanced, Instant.now(), user);
        lesson.setId(1L);

        Reservation reservation = new Reservation(Status.Paid_pass, user, lesson);
        reservation.setId(1L);

        Mockito.when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));

        reservationService.cancelReservation(1L, 1L);
    }

    @Test(expected = BadRequestException.class)
    public void whenCancelReservation_timeOverdue_thenThrowException() {
        String name = "u1";
        String username = "username";
        String email = "email@o2.pl";
        String password = "pass";
        String phoneNumber = "111111111";
        User user = new User(name, username, email, password, phoneNumber, Instant.now());
        user.setId(1L);
        Lesson lesson = new Lesson(Level.Advanced, Instant.now(), user);
        lesson.setId(1L);

        Reservation reservation = new Reservation(Status.Confirmed, user, lesson);
        reservation.setId(1L);

        Mockito.when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));

        reservationService.cancelReservation(1L, 1L);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void whenAcceptReservation_noReservation_thenThrowException() {
        String name = "u1";
        String username = "username";
        String email = "email@o2.pl";
        String password = "pass";
        String phoneNumber = "111111111";
        User user = new User(name, username, email, password, phoneNumber, Instant.now());
        user.setId(1L);
        Lesson lesson = new Lesson(Level.Advanced, Instant.now(), user);
        lesson.setId(1L);

        Reservation reservation = new Reservation(Status.Confirmed, user, lesson);
        reservation.setId(1L);

        Mockito.when(reservationRepository.findById(1L)).thenReturn(Optional.empty());

        reservationService.acceptReservation(1L, 1L);
    }

    @Test(expected = BadRequestException.class)
    public void whenAcceptReservation_notOwner_thenThrowException() {
        String name = "u1";
        String username = "username";
        String email = "email@o2.pl";
        String password = "pass";
        String phoneNumber = "111111111";
        User user = new User(name, username, email, password, phoneNumber, Instant.now());
        user.setId(1L);
        Lesson lesson = new Lesson(Level.Advanced, Instant.now(), user);
        lesson.setId(1L);

        Reservation reservation = new Reservation(Status.Confirmed, user, lesson);
        reservation.setId(1L);

        Mockito.when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));

        reservationService.acceptReservation(1L, 2L);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void whenPayForReservationWithCash_noReservation_thenThrowException() {
        String name = "u1";
        String username = "username";
        String email = "email@o2.pl";
        String password = "pass";
        String phoneNumber = "111111111";
        User user = new User(name, username, email, password, phoneNumber, Instant.now());
        user.setId(1L);
        Lesson lesson = new Lesson(Level.Advanced, Instant.now(), user);
        lesson.setId(1L);

        Reservation reservation = new Reservation(Status.Confirmed, user, lesson);
        reservation.setId(1L);

        Mockito.when(reservationRepository.findById(1L)).thenReturn(Optional.empty());
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        reservationService.payForReservationWithCash(1L, 1L);
    }

    @Test(expected = BadRequestException.class)
    public void whenPayForReservationWithCash_notOwner_thenThrowException() {
        String name = "u1";
        String username = "username";
        String email = "email@o2.pl";
        String password = "pass";
        String phoneNumber = "111111111";
        User user = new User(name, username, email, password, phoneNumber, Instant.now());
        user.setId(1L);
        String username2 = "username2";
        String email2 = "email2@o2.pl";
        User user2 = new User(name, username2, email2, password, phoneNumber, Instant.now());
        user2.setId(2L);
        Lesson lesson = new Lesson(Level.Advanced, Instant.now(), user);
        lesson.setId(1L);

        Reservation reservation = new Reservation(Status.Confirmed, user, lesson);
        reservation.setId(1L);

        Mockito.when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
        Mockito.when(userRepository.findById(2L)).thenReturn(Optional.of(user2));

        reservationService.payForReservationWithCash(1L, 2L);
    }

    @Test(expected = BadRequestException.class)
    public void whenPayForReservationWithCash_notConfirmed_thenThrowException() {
        String name = "u1";
        String username = "username";
        String email = "email@o2.pl";
        String password = "pass";
        String phoneNumber = "111111111";
        User user = new User(name, username, email, password, phoneNumber, Instant.now());
        user.setId(1L);
        Lesson lesson = new Lesson(Level.Advanced, Instant.now(), user);
        lesson.setId(1L);

        Reservation reservation = new Reservation(Status.Cancelled, user, lesson);
        reservation.setId(1L);

        Mockito.when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        reservationService.payForReservationWithCash(1L, 1L);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void whenPayForReservationWithPass_noReservation_thenThrowException() {
        String name = "u1";
        String username = "username";
        String email = "email@o2.pl";
        String password = "pass";
        String phoneNumber = "111111111";
        User user = new User(name, username, email, password, phoneNumber, Instant.now());
        user.setId(1L);
        Lesson lesson = new Lesson(Level.Advanced, Instant.now(), user);
        lesson.setId(1L);

        Reservation reservation = new Reservation(Status.Confirmed, user, lesson);
        reservation.setId(1L);

        Mockito.when(reservationRepository.findById(1L)).thenReturn(Optional.empty());
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        reservationService.payForReservationWithPass(1L, 1L);
    }

    @Test(expected = BadRequestException.class)
    public void whenPayForReservationWithPass_notOwner_thenThrowException() {
        String name = "u1";
        String username = "username";
        String email = "email@o2.pl";
        String password = "pass";
        String phoneNumber = "111111111";
        User user = new User(name, username, email, password, phoneNumber, Instant.now());
        user.setId(1L);
        String username2 = "username2";
        String email2 = "email2@o2.pl";
        User user2 = new User(name, username2, email2, password, phoneNumber, Instant.now());
        user2.setId(2L);
        Lesson lesson = new Lesson(Level.Advanced, Instant.now(), user);
        lesson.setId(1L);

        Reservation reservation = new Reservation(Status.Confirmed, user, lesson);
        reservation.setId(1L);

        Mockito.when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
        Mockito.when(userRepository.findById(2L)).thenReturn(Optional.of(user2));

        reservationService.payForReservationWithPass(1L, 2L);
    }

    @Test(expected = BadRequestException.class)
    public void whenPayForReservationWithPass_notConfirmed_thenThrowException() {
        String name = "u1";
        String username = "username";
        String email = "email@o2.pl";
        String password = "pass";
        String phoneNumber = "111111111";
        User user = new User(name, username, email, password, phoneNumber, Instant.now());
        user.setId(1L);
        Lesson lesson = new Lesson(Level.Advanced, Instant.now(), user);
        lesson.setId(1L);

        Reservation reservation = new Reservation(Status.Cancelled, user, lesson);
        reservation.setId(1L);

        Mockito.when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        reservationService.payForReservationWithPass(1L, 1L);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void whenUpdateReservation_noReservation_thenThrowException() {
        String name = "u1";
        String username = "username";
        String email = "email@o2.pl";
        String password = "pass";
        String phoneNumber = "111111111";
        User user = new User(name, username, email, password, phoneNumber, Instant.now());
        user.setId(1L);
        Lesson lesson = new Lesson(Level.Advanced, Instant.now(), user);
        lesson.setId(1L);

        UserPrincipal userPrincipal = UserPrincipal.create(user);

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        reservationService.updateReservation(1L, new ReservationRequest(), userPrincipal);
    }

    @Test(expected = BadRequestException.class)
    public void whenUpdateReservation_notOwner_thenThrowException() {
        String name = "u1";
        String username = "username";
        String email = "email@o2.pl";
        String password = "pass";
        String phoneNumber = "111111111";
        User user = new User(name, username, email, password, phoneNumber, Instant.now());
        user.setId(1L);
        String username2 = "username2";
        String email2 = "email2@o2.pl";
        User user2 = new User(name, username2, email2, password, phoneNumber, Instant.now());
        user2.setId(2L);
        Lesson lesson = new Lesson(Level.Advanced, Instant.now(), user);
        lesson.setId(1L);

        Reservation reservation = new Reservation(Status.Confirmed, user, lesson);
        reservation.setId(1L);

        UserPrincipal userPrincipal2 = UserPrincipal.create(user2);

        Mockito.when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
        Mockito.when(userRepository.findById(2L)).thenReturn(Optional.of(user2));

        reservationService.updateReservation(1L, new ReservationRequest(), userPrincipal2);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void whenUpdateReservation_noHorse_thenThrowException() {
        String name = "u1";
        String username = "username";
        String email = "email@o2.pl";
        String password = "pass";
        String phoneNumber = "111111111";
        User user = new User(name, username, email, password, phoneNumber, Instant.now());
        user.setId(1L);

        Lesson lesson = new Lesson(Level.Advanced, Instant.now(), user);
        lesson.setId(1L);

        Reservation reservation = new Reservation(Status.Confirmed, user, lesson);
        reservation.setId(1L);

        Horse horse = new Horse("lala", Level.Basic);

        UserPrincipal userPrincipal = UserPrincipal.create(user);

        Mockito.when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(horseRepository.findByHorseName(Mockito.anyString())).thenReturn(Optional.empty());

        reservationService.updateReservation(1L, new ReservationRequest(reservation.getStatus(), reservation.getRider(), "L"), userPrincipal);
    }

    @Test(expected = BadRequestException.class)
    public void whenUpdateReservation_cancelled_thenThrowException() {
        String name = "u1";
        String username = "username";
        String email = "email@o2.pl";
        String password = "pass";
        String phoneNumber = "111111111";
        User user = new User(name, username, email, password, phoneNumber, Instant.now());
        user.setId(1L);

        Lesson lesson = new Lesson(Level.Basic, Instant.now(), user);
        lesson.setId(1L);

        Reservation reservation = new Reservation(Status.Cancelled, user, lesson);
        reservation.setId(1L);

        Horse horse = new Horse("lala", Level.Advanced);

        UserPrincipal userPrincipal = UserPrincipal.create(user);

        Mockito.when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(horseRepository.findByHorseName(Mockito.anyString())).thenReturn(Optional.of(horse));

        reservationService.updateReservation(1L, new ReservationRequest(reservation.getStatus(), reservation.getRider(), "L"), userPrincipal);
    }

    @Test(expected = BadRequestException.class)
    public void whenUpdateReservation_higherHorseLevel_thenThrowException() {
        String name = "u1";
        String username = "username";
        String email = "email@o2.pl";
        String password = "pass";
        String phoneNumber = "111111111";
        User user = new User(name, username, email, password, phoneNumber, Instant.now());
        user.setId(1L);

        Lesson lesson = new Lesson(Level.Basic, Instant.now(), user);
        lesson.setId(1L);

        Reservation reservation = new Reservation(Status.Confirmed, user, lesson);
        reservation.setId(1L);

        Horse horse = new Horse("lala", Level.Advanced);

        UserPrincipal userPrincipal = UserPrincipal.create(user);

        Mockito.when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(horseRepository.findByHorseName(Mockito.anyString())).thenReturn(Optional.of(horse));

        reservationService.updateReservation(1L, new ReservationRequest(reservation.getStatus(), reservation.getRider(), "L"), userPrincipal);
    }


}
