package com.mlynarz.ardena.Repository;

import com.mlynarz.ardena.model.*;
import com.mlynarz.ardena.repository.ReservationRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ReservationRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ReservationRepository reservationRepository;

    @Test
    public void whenExistsByStatusIsNotAndLesson_IdAndRider_Id_thenReturnTrue() {
        // given
        User user = new User("name", "username", "email@hotmail.com", "ppppp", "111111111", Instant.now());
        entityManager.persist(user);

        Lesson lesson = new Lesson(Level.Basic, Instant.now().plus(Duration.ofDays(1)), user);
        entityManager.persist(lesson);

        Reservation reservation = new Reservation(Status.Confirmed, user, lesson);

        entityManager.persist(reservation);
        entityManager.flush();

        // when
        Boolean found = reservationRepository.existsByStatusIsNotAndLesson_IdAndRider_Id(Status.Cancelled, lesson.getId(), user.getId());

        // then
        assertTrue(found);
    }

    @Test
    public void whenExistsByStatusIsNotAndLesson_IdAndRider_Id_otherRider_thenReturnFalse() {
        // given
        User user = new User("name", "username", "email@hotmail.com", "ppppp", "111111111", Instant.now());
        User user2 = new User("name", "username2", "email2@hotmail.com", "ppppp", "111111111", Instant.now());
        entityManager.persist(user);
        entityManager.persist(user2);

        Lesson lesson = new Lesson(Level.Basic, Instant.now().plus(Duration.ofDays(1)), user);
        entityManager.persist(lesson);

        Reservation reservation = new Reservation(Status.Confirmed, user, lesson);

        entityManager.persist(reservation);
        entityManager.flush();

        // when
        Boolean found = reservationRepository.existsByStatusIsNotAndLesson_IdAndRider_Id(Status.Cancelled, lesson.getId(), user2.getId());

        // then
        assertFalse(found);
    }

    @Test
    public void whenExistsByStatusIsNotAndLesson_IdAndRider_Id_otherLesson_thenReturnTrue() {
        // given
        User user = new User("name", "username", "email@hotmail.com", "ppppp", "111111111", Instant.now());
        entityManager.persist(user);

        Lesson lesson = new Lesson(Level.Basic, Instant.now().plus(Duration.ofDays(1)), user);
        Lesson lesson2 = new Lesson(Level.Basic, Instant.now().plus(Duration.ofDays(1)), user);
        entityManager.persist(lesson);
        entityManager.persist(lesson2);

        Reservation reservation = new Reservation(Status.Confirmed, user, lesson);

        entityManager.persist(reservation);
        entityManager.flush();

        // when
        Boolean found = reservationRepository.existsByStatusIsNotAndLesson_IdAndRider_Id(Status.Cancelled, lesson2.getId(), user.getId());

        // then
        assertFalse(found);
    }

    @Test
    public void whenFindByRider_IdAndLessonDateGreaterThanEqualOrderByLessonDate_thenReturnList() {
        // given
        User user = new User("name", "username", "email@hotmail.com", "ppppp", "111111111", Instant.now());
        entityManager.persist(user);

        Lesson lesson = new Lesson(Level.Basic, Instant.now().plus(Duration.ofDays(1)), user);
        entityManager.persist(lesson);

        Reservation reservation = new Reservation(Status.Confirmed, user, lesson);

        entityManager.persist(reservation);
        entityManager.flush();

        List<Reservation> reservationList = new ArrayList<>();
        reservationList.add(reservation);

        // when
        List<Reservation> found = reservationRepository.findByRider_IdAndLessonDateGreaterThanEqualOrderByLessonDate(user.getId(), Instant.now());

        // then
        assertEquals(found, reservationList);
    }

    @Test
    public void whenFindByRider_IdAndLessonDateGreaterThanEqualOrderByLessonDate_OtherRider_thenReturnEmpty() {
        // given
        User user = new User("name", "username", "email@hotmail.com", "ppppp", "111111111", Instant.now());
        User user2 = new User("name", "username2", "email2@hotmail.com", "ppppp", "111111111", Instant.now());
        entityManager.persist(user);
        entityManager.persist(user2);

        Lesson lesson = new Lesson(Level.Basic, Instant.now().plus(Duration.ofDays(1)), user);
        entityManager.persist(lesson);

        Reservation reservation = new Reservation(Status.Confirmed, user, lesson);

        entityManager.persist(reservation);
        entityManager.flush();

        List<Reservation> reservationList = new ArrayList<>();

        // when
        List<Reservation> found = reservationRepository.findByRider_IdAndLessonDateGreaterThanEqualOrderByLessonDate(user2.getId(), Instant.now());

        // then
        assertEquals(reservationList, found);
    }

    @Test
    public void whenFindByRider_IdAndLessonDateGreaterThanEqualOrderByLessonDate_lesserDate_thenReturnEmpty() {
        // given
        User user = new User("name", "username", "email@hotmail.com", "ppppp", "111111111", Instant.now());
        entityManager.persist(user);

        Lesson lesson = new Lesson(Level.Basic, Instant.now().minus(Duration.ofDays(1)), user);
        entityManager.persist(lesson);

        Reservation reservation = new Reservation(Status.Confirmed, user, lesson);

        entityManager.persist(reservation);
        entityManager.flush();

        List<Reservation> reservationList = new ArrayList<>();

        // when
        List<Reservation> found = reservationRepository.findByRider_IdAndLessonDateGreaterThanEqualOrderByLessonDate(user.getId(), Instant.now());

        // then
        assertEquals(found, reservationList);
    }

    @Test
    public void whenFindByLesson_Instructor_IdAndStatusAndLessonDateGreaterThanEqualOrderByLessonDate_thenReturnList() {
        // given
        User user = new User("name", "username", "email@hotmail.com", "ppppp", "111111111", Instant.now());
        entityManager.persist(user);

        Lesson lesson = new Lesson(Level.Basic, Instant.now().plus(Duration.ofDays(1)), user);
        entityManager.persist(lesson);

        Reservation reservation = new Reservation(Status.Pending, user, lesson);

        entityManager.persist(reservation);
        entityManager.flush();

        List<Reservation> reservationList = new ArrayList<>();
        reservationList.add(reservation);

        // when
        List<Reservation> found = reservationRepository.findByLesson_Instructor_IdAndStatusAndLessonDateGreaterThanEqualOrderByLessonDate(user.getId(), Status.Pending, Instant.now());

        // then
        assertEquals(reservationList, found);
    }

    @Test
    public void whenFindByLesson_Instructor_IdAndStatusAndLessonDateGreaterThanEqualOrderByLessonDate_otherInstructor_thenReturnEmpty() {
        // given
        User user = new User("name", "username", "email@hotmail.com", "ppppp", "111111111", Instant.now());
        User user2 = new User("name", "username2", "email2@hotmail.com", "ppppp", "111111111", Instant.now());
        entityManager.persist(user);
        entityManager.persist(user2);

        Lesson lesson = new Lesson(Level.Basic, Instant.now().plus(Duration.ofDays(1)), user);
        entityManager.persist(lesson);

        Reservation reservation = new Reservation(Status.Pending, user, lesson);

        entityManager.persist(reservation);
        entityManager.flush();

        List<Reservation> reservationList = new ArrayList<>();

        // when
        List<Reservation> found = reservationRepository.findByLesson_Instructor_IdAndStatusAndLessonDateGreaterThanEqualOrderByLessonDate(user2.getId(), Status.Pending, Instant.now());

        // then
        assertEquals(reservationList, found);
    }

    @Test
    public void whenFindByLesson_Instructor_IdAndStatusAndLessonDateGreaterThanEqualOrderByLessonDate_otherStatus_thenReturnEmpty() {
        // given
        User user = new User("name", "username", "email@hotmail.com", "ppppp", "111111111", Instant.now());
        entityManager.persist(user);

        Lesson lesson = new Lesson(Level.Basic, Instant.now().plus(Duration.ofDays(1)), user);
        entityManager.persist(lesson);

        Reservation reservation = new Reservation(Status.Confirmed, user, lesson);

        entityManager.persist(reservation);
        entityManager.flush();

        List<Reservation> reservationList = new ArrayList<>();

        // when
        List<Reservation> found = reservationRepository.findByLesson_Instructor_IdAndStatusAndLessonDateGreaterThanEqualOrderByLessonDate(user.getId(), Status.Pending, Instant.now());

        // then
        assertEquals(reservationList, found);
    }

    @Test
    public void whenFindByLesson_Instructor_IdAndStatusAndLessonDateGreaterThanEqualOrderByLessonDate_lesserDate_thenReturnEmpty() {
        // given
        User user = new User("name", "username", "email@hotmail.com", "ppppp", "111111111", Instant.now());
        entityManager.persist(user);

        Lesson lesson = new Lesson(Level.Basic, Instant.now().minus(Duration.ofDays(1)), user);
        entityManager.persist(lesson);

        Reservation reservation = new Reservation(Status.Pending, user, lesson);

        entityManager.persist(reservation);
        entityManager.flush();

        List<Reservation> reservationList = new ArrayList<>();

        // when
        List<Reservation> found = reservationRepository.findByLesson_Instructor_IdAndStatusAndLessonDateGreaterThanEqualOrderByLessonDate(user.getId(), Status.Pending, Instant.now());

        // then
        assertEquals(reservationList, found);
    }

    @Test
    public void whenFindByLesson_Instructor_IdAndStatusAndLessonDateIsLessThanOrderByLessonDate_thenReturnList() {
        // given
        User user = new User("name", "username", "email@hotmail.com", "ppppp", "111111111", Instant.now());
        entityManager.persist(user);

        Lesson lesson = new Lesson(Level.Basic, Instant.now().minus(Duration.ofDays(1)), user);
        entityManager.persist(lesson);

        Reservation reservation = new Reservation(Status.Pending, user, lesson);

        entityManager.persist(reservation);
        entityManager.flush();

        List<Reservation> reservationList = new ArrayList<>();
        reservationList.add(reservation);

        // when
        List<Reservation> found = reservationRepository.findByLesson_Instructor_IdAndStatusAndLessonDateIsLessThanOrderByLessonDate(user.getId(), Status.Pending, Instant.now());

        // then
        assertEquals(reservationList, found);
    }

    @Test
    public void whenFindByLesson_Instructor_IdAndStatusAndLessonDateIsLessThanOrderByLessonDate_otherInstructor_thenReturnEmpty() {
        // given
        User user = new User("name", "username", "email@hotmail.com", "ppppp", "111111111", Instant.now());
        User user2 = new User("name", "username2", "email2@hotmail.com", "ppppp", "111111111", Instant.now());
        entityManager.persist(user);
        entityManager.persist(user2);

        Lesson lesson = new Lesson(Level.Basic, Instant.now().minus(Duration.ofDays(1)), user);
        entityManager.persist(lesson);

        Reservation reservation = new Reservation(Status.Pending, user, lesson);

        entityManager.persist(reservation);
        entityManager.flush();

        List<Reservation> reservationList = new ArrayList<>();

        // when
        List<Reservation> found = reservationRepository.findByLesson_Instructor_IdAndStatusAndLessonDateIsLessThanOrderByLessonDate(user2.getId(), Status.Pending, Instant.now());

        // then
        assertEquals(reservationList, found);
    }

    @Test
    public void whenFindByLesson_Instructor_IdAndStatusAndLessonDateIsLessThanOrderByLessonDate_otherStatus_thenReturnEmpty() {
        // given
        User user = new User("name", "username", "email@hotmail.com", "ppppp", "111111111", Instant.now());
        entityManager.persist(user);

        Lesson lesson = new Lesson(Level.Basic, Instant.now().minus(Duration.ofDays(1)), user);
        entityManager.persist(lesson);

        Reservation reservation = new Reservation(Status.Confirmed, user, lesson);

        entityManager.persist(reservation);
        entityManager.flush();

        List<Reservation> reservationList = new ArrayList<>();

        // when
        List<Reservation> found = reservationRepository.findByLesson_Instructor_IdAndStatusAndLessonDateIsLessThanOrderByLessonDate(user.getId(), Status.Pending, Instant.now());

        // then
        assertEquals(reservationList, found);
    }

    @Test
    public void whenFindByLesson_Instructor_IdAndStatusAndLessonDateIsLessThanOrderByLessonDate_lesserDate_thenReturnEmpty() {
        // given
        User user = new User("name", "username", "email@hotmail.com", "ppppp", "111111111", Instant.now());
        entityManager.persist(user);

        Lesson lesson = new Lesson(Level.Basic, Instant.now().plus(Duration.ofDays(1)), user);
        entityManager.persist(lesson);

        Reservation reservation = new Reservation(Status.Pending, user, lesson);

        entityManager.persist(reservation);
        entityManager.flush();

        List<Reservation> reservationList = new ArrayList<>();

        // when
        List<Reservation> found = reservationRepository.findByLesson_Instructor_IdAndStatusAndLessonDateIsLessThanOrderByLessonDate(user.getId(), Status.Pending, Instant.now());

        // then
        assertEquals(reservationList, found);
    }

    @Test
    public void whenFindByRider_IdAndLessonDateIsLessThanOrderByLessonDateDesc_thenReturnList() {
        // given
        User user = new User("name", "username", "email@hotmail.com", "ppppp", "111111111", Instant.now());
        entityManager.persist(user);

        Lesson lesson = new Lesson(Level.Basic, Instant.now().minus(Duration.ofDays(1)), user);
        entityManager.persist(lesson);

        Reservation reservation = new Reservation(Status.Confirmed, user, lesson);

        entityManager.persist(reservation);
        entityManager.flush();

        List<Reservation> reservationList = new ArrayList<>();
        reservationList.add(reservation);

        // when
        List<Reservation> found = reservationRepository.findByRider_IdAndLessonDateIsLessThanOrderByLessonDateDesc(user.getId(), Instant.now());

        // then
        assertEquals(found, reservationList);
    }

    @Test
    public void whenFindByRider_IdAndLessonDateIsLessThanOrderByLessonDateDesc_OtherRider_thenReturnEmpty() {
        // given
        User user = new User("name", "username", "email@hotmail.com", "ppppp", "111111111", Instant.now());
        User user2 = new User("name", "username2", "email2@hotmail.com", "ppppp", "111111111", Instant.now());
        entityManager.persist(user);
        entityManager.persist(user2);

        Lesson lesson = new Lesson(Level.Basic, Instant.now().minus(Duration.ofDays(1)), user);
        entityManager.persist(lesson);

        Reservation reservation = new Reservation(Status.Confirmed, user, lesson);

        entityManager.persist(reservation);
        entityManager.flush();

        List<Reservation> reservationList = new ArrayList<>();

        // when
        List<Reservation> found = reservationRepository.findByRider_IdAndLessonDateIsLessThanOrderByLessonDateDesc(user2.getId(), Instant.now());

        // then
        assertEquals(reservationList, found);
    }

    @Test
    public void whenFindByRider_IdAndLessonDateIsLessThanOrderByLessonDateDesc_lesserDate_thenReturnEmpty() {
        // given
        User user = new User("name", "username", "email@hotmail.com", "ppppp", "111111111", Instant.now());
        entityManager.persist(user);

        Lesson lesson = new Lesson(Level.Basic, Instant.now().plus(Duration.ofDays(1)), user);
        entityManager.persist(lesson);

        Reservation reservation = new Reservation(Status.Confirmed, user, lesson);

        entityManager.persist(reservation);
        entityManager.flush();

        List<Reservation> reservationList = new ArrayList<>();

        // when
        List<Reservation> found = reservationRepository.findByRider_IdAndLessonDateIsLessThanOrderByLessonDateDesc(user.getId(), Instant.now());

        // then
        assertEquals(found, reservationList);
    }

    @Test
    public void whenFindByRider_IdAndStatusAndLessonDateIsLessThanOrderByLessonDate_thenReturnList() {
        // given
        User user = new User("name", "username", "email@hotmail.com", "ppppp", "111111111", Instant.now());
        entityManager.persist(user);

        Lesson lesson = new Lesson(Level.Basic, Instant.now().minus(Duration.ofDays(1)), user);
        entityManager.persist(lesson);

        Reservation reservation = new Reservation(Status.Confirmed, user, lesson);

        entityManager.persist(reservation);
        entityManager.flush();

        List<Reservation> reservationList = new ArrayList<>();
        reservationList.add(reservation);

        // when
        List<Reservation> found = reservationRepository.findByRider_IdAndStatusAndLessonDateIsLessThanOrderByLessonDate(user.getId(), Status.Confirmed,Instant.now());

        // then
        assertEquals(found, reservationList);
    }

    @Test
    public void whenFindByRider_IdAndStatusAndLessonDateIsLessThanOrderByLessonDate_OtherRider_thenReturnEmpty() {
        // given
        User user = new User("name", "username", "email@hotmail.com", "ppppp", "111111111", Instant.now());
        User user2 = new User("name", "username2", "email2@hotmail.com", "ppppp", "111111111", Instant.now());
        entityManager.persist(user);
        entityManager.persist(user2);

        Lesson lesson = new Lesson(Level.Basic, Instant.now().minus(Duration.ofDays(1)), user);
        entityManager.persist(lesson);

        Reservation reservation = new Reservation(Status.Confirmed, user, lesson);

        entityManager.persist(reservation);
        entityManager.flush();

        List<Reservation> reservationList = new ArrayList<>();

        // when
        List<Reservation> found = reservationRepository.findByRider_IdAndStatusAndLessonDateIsLessThanOrderByLessonDate(user2.getId(), Status.Confirmed, Instant.now());

        // then
        assertEquals(reservationList, found);
    }

    @Test
    public void whenFindByRider_IdAndStatusAndLessonDateIsLessThanOrderByLessonDate_lesserDate_thenReturnEmpty() {
        // given
        User user = new User("name", "username", "email@hotmail.com", "ppppp", "111111111", Instant.now());
        entityManager.persist(user);

        Lesson lesson = new Lesson(Level.Basic, Instant.now().plus(Duration.ofDays(1)), user);
        entityManager.persist(lesson);

        Reservation reservation = new Reservation(Status.Confirmed, user, lesson);

        entityManager.persist(reservation);
        entityManager.flush();

        List<Reservation> reservationList = new ArrayList<>();

        // when
        List<Reservation> found = reservationRepository.findByRider_IdAndStatusAndLessonDateIsLessThanOrderByLessonDate(user.getId(), Status.Confirmed, Instant.now());

        // then
        assertEquals(found, reservationList);
    }

    @Test
    public void whenFindByRider_IdAndStatusAndLessonDateIsLessThanOrderByLessonDate_otherStatus_thenReturnEmpty() {
        //        // given
        User user = new User("name", "username", "email@hotmail.com", "ppppp", "111111111", Instant.now());
        entityManager.persist(user);

        Lesson lesson = new Lesson(Level.Basic, Instant.now().plus(Duration.ofDays(1)), user);
        entityManager.persist(lesson);

        Reservation reservation = new Reservation(Status.Cancelled, user, lesson);

        entityManager.persist(reservation);
        entityManager.flush();

        List<Reservation> reservationList = new ArrayList<>();

        // when
        List<Reservation> found = reservationRepository.findByRider_IdAndStatusAndLessonDateIsLessThanOrderByLessonDate(user.getId(), Status.Confirmed,Instant.now());

        // then
        assertEquals(found, reservationList);
    }


}


