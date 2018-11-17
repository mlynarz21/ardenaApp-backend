package com.mlynarz.ardena.Repository;

import com.mlynarz.ardena.model.Lesson;
import com.mlynarz.ardena.model.Level;
import com.mlynarz.ardena.model.User;
import com.mlynarz.ardena.repository.LessonRepository;
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

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
public class LessonRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private LessonRepository lessonRepository;

    @Test
    public void whenFindByDateGreaterThanEqual_thenReturnLessonList() {
        // given
        User user = new User("name", "username", "email@hotmail.com", "ppppp", "111111111", Instant.now());
        entityManager.persist(user);

        Lesson lesson = new Lesson(Level.Basic, Instant.now().plus(Duration.ofDays(1)), user);
        Lesson lesson2 = new Lesson(Level.Basic, Instant.now().plus(Duration.ofDays(2)), user);
        entityManager.persist(lesson);
        entityManager.persist(lesson2);
        entityManager.flush();

        List<Lesson> lessonList = new ArrayList<>();
        lessonList.add(lesson);
        lessonList.add(lesson2);

        // when
        List<Lesson> found = lessonRepository.findByDateGreaterThanEqual(Instant.now());

        // then
        assertEquals(lessonList, found);
    }

    @Test
    public void whenFindByDateGreaterThanEqual_dateLess_thenReturnEmpty() {
        // given
        User user = new User("name", "username", "email@hotmail.com", "ppppp", "111111111", Instant.now());
        entityManager.persist(user);

        Lesson lesson = new Lesson(Level.Basic, Instant.now().minus(Duration.ofDays(1)), user);
        Lesson lesson2 = new Lesson(Level.Basic, Instant.now().minus(Duration.ofDays(2)), user);
        entityManager.persist(lesson);
        entityManager.persist(lesson2);
        entityManager.flush();

        List<Lesson> lessonList = new ArrayList<>();

        // when
        List<Lesson> found = lessonRepository.findByDateGreaterThanEqual(Instant.now());

        // then
        assertEquals(lessonList, found);
    }

    @Test
    public void whenFindByInstructorAndDateBetween_thenReturnLesson() {
        // given
        User user = new User("name", "username", "email@hotmail.com", "ppppp", "111111111", Instant.now());
        entityManager.persist(user);

        Lesson lesson = new Lesson(Level.Basic, Instant.now().plus(Duration.ofDays(1)), user);
        entityManager.persist(lesson);
        entityManager.flush();

        // when
        Lesson found = lessonRepository.findByInstructorAndDateBetween(user, Instant.now(), Instant.now().plus(Duration.ofDays(1)));

        // then
        assertEquals(lesson, found);
    }

    @Test
    public void whenFindByInstructorAndDateBetween_otherLesson_thenReturnEmpty() {
        // given
        User user = new User("name", "username", "email@hotmail.com", "ppppp", "111111111", Instant.now());
        entityManager.persist(user);

        Lesson lesson = new Lesson(Level.Basic, Instant.now().plus(Duration.ofDays(2)), user);
        Lesson lesson2 = new Lesson(Level.Basic, Instant.now().minus(Duration.ofDays(2)), user);
        entityManager.persist(lesson);
        entityManager.persist(lesson2);
        entityManager.flush();

        // when
        Lesson found = lessonRepository.findByInstructorAndDateBetween(user, Instant.now(), Instant.now().plus(Duration.ofDays(1)));

        // then
        assertEquals(null, found);
    }

    @Test
    public void whenFindByInstructorAndDateBetween_otherInstructor_thenReturnLesson() {
        // given
        User user = new User("name", "username", "email@hotmail.com", "ppppp", "111111111", Instant.now());
        User user2 = new User("name", "username2", "email2@hotmail.com", "ppppp", "111111111", Instant.now());
        entityManager.persist(user);
        entityManager.persist(user2);

        Lesson lesson = new Lesson(Level.Basic, Instant.now().plus(Duration.ofDays(1)), user);
        entityManager.persist(lesson);
        entityManager.flush();

        // when
        Lesson found = lessonRepository.findByInstructorAndDateBetween(user2, Instant.now(), Instant.now().plus(Duration.ofDays(1)));

        // then
        assertEquals(null, found);
    }

    @Test
    public void whenFindByDateBetween_thenReturnLesson() {
        // given
        User user = new User("name", "username", "email@hotmail.com", "ppppp", "111111111", Instant.now());
        entityManager.persist(user);

        Lesson lesson = new Lesson(Level.Basic, Instant.now().plus(Duration.ofDays(1)), user);
        entityManager.persist(lesson);
        entityManager.flush();

        List<Lesson> lessonList = new ArrayList<>();
        lessonList.add(lesson);

        // when
        List<Lesson> found = lessonRepository.findByDateBetween(Instant.now(), Instant.now().plus(Duration.ofDays(1)));

        // then
        assertEquals(lessonList, found);
    }

    @Test
    public void whenFindByDateBetween_otherLesson_thenReturnEmpty() {
        // given
        User user = new User("name", "username", "email@hotmail.com", "ppppp", "111111111", Instant.now());
        entityManager.persist(user);

        Lesson lesson = new Lesson(Level.Basic, Instant.now().plus(Duration.ofDays(2)), user);
        Lesson lesson2 = new Lesson(Level.Basic, Instant.now().minus(Duration.ofDays(2)), user);
        entityManager.persist(lesson);
        entityManager.persist(lesson2);
        entityManager.flush();

        List<Lesson> lessonList = new ArrayList<>();

        // when
        List<Lesson> found = lessonRepository.findByDateBetween(Instant.now(), Instant.now().plus(Duration.ofDays(1)));

        // then
        assertEquals(lessonList, found);
    }

    @Test
    public void whenFindByDateGreaterThanEqualAndDateBetweenOrderByDate_thenReturnLessonList() {
        // given
        User user = new User("name", "username", "email@hotmail.com", "ppppp", "111111111", Instant.now());
        entityManager.persist(user);

        Lesson lesson = new Lesson(Level.Basic, Instant.now().plus(Duration.ofDays(1)), user);
        Lesson lesson2 = new Lesson(Level.Basic, Instant.now().plus(Duration.ofHours(49)), user);
        entityManager.persist(lesson);
        entityManager.persist(lesson2);
        entityManager.flush();

        List<Lesson> lessonList = new ArrayList<>();
        lessonList.add(lesson2);

        // when
        List<Lesson> found = lessonRepository.findByDateGreaterThanEqualAndDateBetweenOrderByDate(Instant.now(), Instant.now().plus(Duration.ofDays(2)), Instant.now().plus(Duration.ofDays(3)));

        // then
        assertEquals(lessonList, found);
    }

    @Test
    public void whenFindByDateGreaterThanEqualAndDateBetweenOrderByDate_dateLess_thenReturnEmpty() {
        // given
        User user = new User("name", "username", "email@hotmail.com", "ppppp", "111111111", Instant.now());
        entityManager.persist(user);

        Lesson lesson = new Lesson(Level.Basic, Instant.now().plus(Duration.ofDays(1)), user);
        Lesson lesson2 = new Lesson(Level.Basic, Instant.now().plus(Duration.ofDays(4)), user);
        entityManager.persist(lesson);
        entityManager.persist(lesson2);
        entityManager.flush();

        List<Lesson> lessonList = new ArrayList<>();

        // when
        List<Lesson> found = lessonRepository.findByDateGreaterThanEqualAndDateBetweenOrderByDate(Instant.now(), Instant.now().plus(Duration.ofDays(2)), Instant.now().plus(Duration.ofDays(3)));

        // then
        assertEquals(lessonList, found);
    }

    @Test
    public void whenFindByInstructor_IdAndDateGreaterThanEqualOrderByDate_thenReturnLessonList() {
        // given
        User user = new User("name", "username", "email@hotmail.com", "ppppp", "111111111", Instant.now());
        entityManager.persist(user);

        Lesson lesson = new Lesson(Level.Basic, Instant.now().plus(Duration.ofDays(1)), user);
        Lesson lesson2 = new Lesson(Level.Basic, Instant.now().plus(Duration.ofDays(2)), user);
        entityManager.persist(lesson);
        entityManager.persist(lesson2);
        entityManager.flush();

        List<Lesson> lessonList = new ArrayList<>();
        lessonList.add(lesson);
        lessonList.add(lesson2);

        // when
        List<Lesson> found = lessonRepository.findByInstructor_IdAndDateGreaterThanEqualOrderByDate(user.getId(), Instant.now());

        // then
        assertEquals(lessonList, found);
    }

    @Test
    public void whenFindByInstructor_IdAndDateGreaterThanEqualOrderByDate_dateLess_thenReturnEmpty() {
        // given
        User user = new User("name", "username", "email@hotmail.com", "ppppp", "111111111", Instant.now());
        entityManager.persist(user);

        Lesson lesson = new Lesson(Level.Basic, Instant.now().minus(Duration.ofDays(1)), user);
        Lesson lesson2 = new Lesson(Level.Basic, Instant.now().minus(Duration.ofDays(2)), user);
        entityManager.persist(lesson);
        entityManager.persist(lesson2);
        entityManager.flush();

        List<Lesson> lessonList = new ArrayList<>();

        // when
        List<Lesson> found = lessonRepository.findByInstructor_IdAndDateGreaterThanEqualOrderByDate(user.getId(), Instant.now());

        // then
        assertEquals(lessonList, found);
    }

    @Test
    public void whenFindByInstructor_IdAndDateGreaterThanEqualOrderByDate_otherInstructor_thenReturnEmpty() {
        // given
        User user = new User("name", "username", "email@hotmail.com", "ppppp", "111111111", Instant.now());
        User user2 = new User("name", "username2", "email2@hotmail.com", "ppppp", "111111111", Instant.now());
        entityManager.persist(user);
        entityManager.persist(user2);

        Lesson lesson = new Lesson(Level.Basic, Instant.now().plus(Duration.ofDays(1)), user);
        Lesson lesson2 = new Lesson(Level.Basic, Instant.now().plus(Duration.ofDays(2)), user);
        entityManager.persist(lesson);
        entityManager.persist(lesson2);
        entityManager.flush();

        List<Lesson> lessonList = new ArrayList<>();

        // when
        List<Lesson> found = lessonRepository.findByInstructor_IdAndDateGreaterThanEqualOrderByDate(user2.getId(), Instant.now());

        // then
        assertEquals(lessonList, found);
    }

    @Test
    public void whenFindByInstructor_IdAndDateGreaterThanEqualAndDateBetweenOrderByDate_thenReturnLessonList() {
        // given
        User user = new User("name", "username", "email@hotmail.com", "ppppp", "111111111", Instant.now());
        entityManager.persist(user);

        Lesson lesson = new Lesson(Level.Basic, Instant.now().plus(Duration.ofDays(1)), user);
        Lesson lesson2 = new Lesson(Level.Basic, Instant.now().plus(Duration.ofHours(49)), user);
        entityManager.persist(lesson);
        entityManager.persist(lesson2);
        entityManager.flush();

        List<Lesson> lessonList = new ArrayList<>();
        lessonList.add(lesson2);

        // when
        List<Lesson> found = lessonRepository.findByInstructor_IdAndDateGreaterThanEqualAndDateBetweenOrderByDate(user.getId(), Instant.now(), Instant.now().plus(Duration.ofDays(2)), Instant.now().plus(Duration.ofDays(3)));

        // then
        assertEquals(lessonList, found);
    }

    @Test
    public void whenFindByInstructor_IdAndDateGreaterThanEqualAndDateBetweenOrderByDate_dateLess_thenReturnEmpty() {
        // given
        User user = new User("name", "username", "email@hotmail.com", "ppppp", "111111111", Instant.now());
        entityManager.persist(user);

        Lesson lesson = new Lesson(Level.Basic, Instant.now().minus(Duration.ofDays(1)), user);
        Lesson lesson2 = new Lesson(Level.Basic, Instant.now().plus(Duration.ofDays(4)), user);
        entityManager.persist(lesson);
        entityManager.persist(lesson2);
        entityManager.flush();

        List<Lesson> lessonList = new ArrayList<>();

        // when
        List<Lesson> found = lessonRepository.findByInstructor_IdAndDateGreaterThanEqualAndDateBetweenOrderByDate(user.getId(), Instant.now(), Instant.now().plus(Duration.ofDays(2)), Instant.now().plus(Duration.ofDays(3)));

        // then
        assertEquals(lessonList, found);
    }

    @Test
    public void whenFindByInstructor_IdAndDateGreaterThanEqualAndDateBetweenOrderByDate_otherInstructor_thenReturnEmpty() {
        // given
        User user = new User("name", "username", "email@hotmail.com", "ppppp", "111111111", Instant.now());
        User user2 = new User("name", "username2", "email2@hotmail.com", "ppppp", "111111111", Instant.now());
        entityManager.persist(user);
        entityManager.persist(user2);

        Lesson lesson = new Lesson(Level.Basic, Instant.now().plus(Duration.ofDays(1)), user);
        Lesson lesson2 = new Lesson(Level.Basic, Instant.now().plus(Duration.ofHours(49)), user);
        entityManager.persist(lesson);
        entityManager.persist(lesson2);
        entityManager.flush();

        List<Lesson> lessonList = new ArrayList<>();

        // when
        List<Lesson> found = lessonRepository.findByInstructor_IdAndDateGreaterThanEqualAndDateBetweenOrderByDate(user2.getId(), Instant.now(), Instant.now().plus(Duration.ofDays(2)), Instant.now().plus(Duration.ofDays(3)));

        // then
        assertEquals(lessonList, found);
    }
}


