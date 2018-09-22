package com.mlynarz.ardena.Repository;

import com.mlynarz.ardena.model.Horse;
import com.mlynarz.ardena.repository.HorseRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
public class RoleRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private HorseRepository horseRepository;

    @Test
    public void whenFindByName_thenReturnHorse() {
        // given
        String horseName = "h1";
        String horseName2 = "h2";
        Horse horse = new Horse(horseName);
        Horse horse2 = new Horse(horseName2);
        entityManager.persist(horse);
        entityManager.persist(horse2);
        entityManager.flush();

        // when
        Horse found = horseRepository.findByHorseName(horseName).get();

        // then
        assertEquals(horse, found);
    }

    @Test
    public void whenFindByNameOtherRole_thenReturnEmpty() {
        // given
        String horseName = "h1";
        String horseName2 = "h2";
        String horseName3 = "h3";
        Horse horse = new Horse(horseName);
        Horse horse2 = new Horse(horseName2);
        entityManager.persist(horse);
        entityManager.persist(horse2);
        entityManager.flush();

        // when
        Optional<Horse> found = horseRepository.findByHorseName(horseName3);

        // then
        assertFalse(found.isPresent());
    }

    @Test
    public void whenExistsByName_thenReturnTrue() {
        // given
        String horseName = "h1";
        String horseName2 = "h2";
        Horse horse = new Horse(horseName);
        Horse horse2 = new Horse(horseName2);
        entityManager.persist(horse);
        entityManager.persist(horse2);
        entityManager.flush();

        // when
        boolean exists = horseRepository.existsByHorseName(horseName);

        // then
        assertTrue(exists);
    }

    @Test
    public void whenNotExistsByName_thenReturnFalse() {
        // given
        String horseName = "h1";
        String horseName2 = "h2";
        String horseName3 = "h3";
        Horse horse = new Horse(horseName);
        Horse horse2 = new Horse(horseName2);
        entityManager.persist(horse);
        entityManager.persist(horse2);
        entityManager.flush();

        // when
        boolean exists = horseRepository.existsByHorseName(horseName3);

        // then
        assertFalse(exists);
    }

}


