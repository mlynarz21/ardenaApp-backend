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
        User user = new User("name", "username", "email@hotmail.com", "ppppp");
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

//    @Test
//    public void whenFindByNameOtherRole_thenReturnEmpty() {
//        // given
//        String horseName = "h1";
//        String horseName2 = "h2";
//        String horseName3 = "h3";
//        Horse horse = new Horse(horseName);
//        Horse horse2 = new Horse(horseName2);
//        entityManager.persist(horse);
//        entityManager.persist(horse2);
//        entityManager.flush();
//
//        // when
//        Optional<Horse> found = horseRepository.findByHorseName(horseName3);
//
//        // then
//        assertFalse(found.isPresent());
//    }
//
//    @Test
//    public void whenExistsByName_thenReturnTrue() {
//        // given
//        String horseName = "h1";
//        String horseName2 = "h2";
//        Horse horse = new Horse(horseName);
//        Horse horse2 = new Horse(horseName2);
//        entityManager.persist(horse);
//        entityManager.persist(horse2);
//        entityManager.flush();
//
//        // when
//        boolean exists = horseRepository.existsByHorseName(horseName);
//
//        // then
//        assertTrue(exists);
//    }
//
//    @Test
//    public void whenNotExistsByName_thenReturnFalse() {
//        // given
//        String horseName = "h1";
//        String horseName2 = "h2";
//        String horseName3 = "h3";
//        Horse horse = new Horse(horseName);
//        Horse horse2 = new Horse(horseName2);
//        entityManager.persist(horse);
//        entityManager.persist(horse2);
//        entityManager.flush();
//
//        // when
//        boolean exists = horseRepository.existsByHorseName(horseName3);
//
//        // then
//        assertFalse(exists);
//    }

}


