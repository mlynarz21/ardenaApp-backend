package com.mlynarz.ardena.Repository;

import com.mlynarz.ardena.model.Pass;
import com.mlynarz.ardena.model.User;
import com.mlynarz.ardena.repository.PassRepository;
import com.mlynarz.ardena.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(SpringRunner.class)
@DataJpaTest
public class PassRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PassRepository passRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void whenFindByExpirationDateBeforeAndOwnerIdAndUsedRidesIsLessThanNoOfRidesPermitted_thenReturnPass() {
        // given
        User user = new User("name", "username", "email@hotmail.com", "ppppp", "111111111", Instant.now());
        entityManager.persist(user);

        Pass pass = new Pass();
        pass.setExpirationDate(Instant.now().plus(Duration.ofDays(1)));
        pass.setOwner(user);
        pass.setNoOfRidesPermitted(10);
        pass.setUsedRides(9);
        entityManager.persist(pass);

        entityManager.flush();

        // when
        Pass found = passRepository.findByExpirationDateBeforeAndOwner_IdAndUsedRidesIsLessThanNoOfRidesPermitted(Instant.now(),user.getId()).get();

        // then
        assertEquals(pass, found);
    }

    @Test
    public void whenFindByExpirationDateBeforeAndOwnerIdAndUsedRidesIsLessThanNoOfRidesPermitted_Expired_thenReturnEmpty() {
        // given
        User user = new User("name", "username", "email@hotmail.com", "ppppp", "111111111", Instant.now());
        entityManager.persist(user);

        Pass pass = new Pass();
        pass.setExpirationDate(Instant.now().minus(Duration.ofDays(1)));
        pass.setOwner(user);
        pass.setNoOfRidesPermitted(10);
        pass.setUsedRides(0);
        entityManager.persist(pass);

        entityManager.flush();

        // when
        Optional<Pass> found = passRepository.findByExpirationDateBeforeAndOwner_IdAndUsedRidesIsLessThanNoOfRidesPermitted(Instant.now(),user.getId());

        // then
        assertFalse(found.isPresent());
    }

    @Test
    public void whenFindByExpirationDateBeforeAndOwnerIdAndUsedRidesIsLessThanNoOfRidesPermitted_UsedRidesEqualToPermitted_thenReturnEmpty() {
        // given
        User user = new User("name", "username", "email@hotmail.com", "ppppp", "111111111", Instant.now());
        entityManager.persist(user);

        Pass pass = new Pass();
        pass.setExpirationDate(Instant.now().plus(Duration.ofDays(1)));
        pass.setOwner(user);
        pass.setNoOfRidesPermitted(10);
        pass.setUsedRides(10);
        entityManager.persist(pass);

        entityManager.flush();

        // when
        Optional<Pass> found = passRepository.findByExpirationDateBeforeAndOwner_IdAndUsedRidesIsLessThanNoOfRidesPermitted(Instant.now(),user.getId());

        // then
        assertFalse(found.isPresent());
    }

    @Test
    public void whenFindByExpirationDateBeforeAndOwnerIdAndUsedRidesIsLessThanNoOfRidesPermitted_UsedRidesEqualToPermittedAndExpired_thenReturnEmpty() {
        // given
        User user = new User("name", "username", "email@hotmail.com", "ppppp", "111111111", Instant.now());
        entityManager.persist(user);

        Pass pass = new Pass();
        pass.setExpirationDate(Instant.now().minus(Duration.ofDays(1)));
        pass.setOwner(user);
        pass.setNoOfRidesPermitted(10);
        pass.setUsedRides(10);
        entityManager.persist(pass);

        entityManager.flush();

        // when
        Optional<Pass> found = passRepository.findByExpirationDateBeforeAndOwner_IdAndUsedRidesIsLessThanNoOfRidesPermitted(Instant.now(),user.getId());

        // then
        assertFalse(found.isPresent());
    }

    @Test
    public void whenFindByExpirationDateBeforeAndOwnerIdAndUsedRidesIsLessThanNoOfRidesPermitted_NoUserPasses_thenReturnEmpty() {
        // given
        User user = new User("name", "username", "email@hotmail.com", "ppppp", "111111111", Instant.now());
        User user2 = new User("name2", "username2", "email2@hotmail.com", "ppppp", "111111111", Instant.now());
        entityManager.persist(user);
        entityManager.persist(user2);

        Pass pass = new Pass();
        pass.setExpirationDate(Instant.now().plus(Duration.ofDays(1)));
        pass.setOwner(user);
        pass.setNoOfRidesPermitted(10);
        pass.setUsedRides(0);
        entityManager.persist(pass);

        entityManager.flush();

        // when
        Optional<Pass> found = passRepository.findByExpirationDateBeforeAndOwner_IdAndUsedRidesIsLessThanNoOfRidesPermitted(Instant.now(),user2.getId());

        // then
        assertFalse(found.isPresent());
    }

}


