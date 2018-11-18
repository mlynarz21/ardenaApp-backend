package com.mlynarz.ardena.Service;

import com.mlynarz.ardena.exception.BadRequestException;
import com.mlynarz.ardena.model.Pass;
import com.mlynarz.ardena.model.User;
import com.mlynarz.ardena.payload.Request.PassRequest;
import com.mlynarz.ardena.repository.PassRepository;
import com.mlynarz.ardena.repository.UserRepository;
import com.mlynarz.ardena.service.PassService;
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
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@RunWith(SpringRunner.class)
public class PassServiceIntegrationTest {

    @TestConfiguration
    public static class PassServiceIntegrationTestContextConfiguration {

        @Bean
        public PassService passService() {
            return new PassService();
        }
    }

    @Autowired
    private PassService passService;

    @MockBean
    private PassRepository passRepository;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void whenGetValidPass_thenPassShouldBeReturned() {
        String name = "u1";
        String username = "username";
        String email = "email@o2.pl";
        String password = "pass";
        String phoneNumber = "111111111";
        User user = new User(name, username, email, password, phoneNumber, Instant.now());
        user.setId(1L);

        Pass pass = new Pass();
        pass.setUsedRides(0);
        pass.setNoOfRidesPermitted(10);
        pass.setExpirationDate(Instant.now().plus(Duration.ofDays(30)));
        pass.setOwner(user);

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(passRepository.findByExpirationDateBeforeAndOwner_IdAndUsedRidesIsLessThanNoOfRidesPermitted(Mockito.any(), Mockito.anyLong())).thenReturn(Optional.of(pass));

        Pass found = passService.getValidPass(1L);

        assertThat(found.getOwner()).isEqualTo(user);
    }

    @Test(expected = BadRequestException.class)
    public void whenGetValidPass_noValidPasses_thenException() {
        String name = "u1";
        String username = "username";
        String email = "email@o2.pl";
        String password = "pass";
        String phoneNumber = "111111111";
        User user = new User(name, username, email, password, phoneNumber, Instant.now());
        user.setId(1L);

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(passRepository.findByExpirationDateBeforeAndOwner_IdAndUsedRidesIsLessThanNoOfRidesPermitted(Mockito.any(), Mockito.anyLong())).thenReturn(Optional.empty());

        Pass found = passService.getValidPass(1L);
    }

    @Test
    public void whenGetValidPassByUsername_thenPassShouldBeReturned() {
        String name = "u1";
        String username = "username";
        String email = "email@o2.pl";
        String password = "pass";
        String phoneNumber = "111111111";
        User user = new User(name, username, email, password, phoneNumber, Instant.now());
        user.setId(1L);

        Pass pass = new Pass();
        pass.setUsedRides(0);
        pass.setNoOfRidesPermitted(10);
        pass.setExpirationDate(Instant.now().plus(Duration.ofDays(30)));
        pass.setOwner(user);

        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        Mockito.when(passRepository.findByExpirationDateBeforeAndOwner_IdAndUsedRidesIsLessThanNoOfRidesPermitted(Mockito.any(), Mockito.anyLong())).thenReturn(Optional.of(pass));

        Pass found = passService.getValidPassByUsername(username);

        assertThat(found.getOwner()).isEqualTo(user);
    }

    @Test(expected = BadRequestException.class)
    public void whenGetValidPassByUsername_noValidPasses_thenException() {
        String name = "u1";
        String username = "username";
        String email = "email@o2.pl";
        String password = "pass";
        String phoneNumber = "111111111";
        User user = new User(name, username, email, password, phoneNumber, Instant.now());
        user.setId(1L);

        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        Mockito.when(passRepository.findByExpirationDateBeforeAndOwner_IdAndUsedRidesIsLessThanNoOfRidesPermitted(Mockito.any(), Mockito.anyLong())).thenReturn(Optional.empty());

        Pass found = passService.getValidPassByUsername(username);
    }

    @Test(expected = BadRequestException.class)
    public void whenAddPass_passExists_thenReturnPass() {
        String name = "u1";
        String username = "username";
        String email = "email@o2.pl";
        String password = "pass";
        String phoneNumber = "111111111";
        User user = new User(name, username, email, password, phoneNumber, Instant.now());
        user.setId(1L);

        Pass pass = new Pass();
        pass.setUsedRides(0);
        pass.setNoOfRidesPermitted(10);
        pass.setExpirationDate(Instant.now().plus(Duration.ofDays(30)));
        pass.setOwner(user);

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(passRepository.findByExpirationDateBeforeAndOwner_IdAndUsedRidesIsLessThanNoOfRidesPermitted(Mockito.any(), Mockito.anyLong())).thenReturn(Optional.of(pass));

        Pass found = passService.addPass(new PassRequest(), 1L);
    }

    @Test
    public void whenAddPass_thenReturnPass() {
        String name = "u1";
        String username = "username";
        String email = "email@o2.pl";
        String password = "pass";
        String phoneNumber = "111111111";
        User user = new User(name, username, email, password, phoneNumber, Instant.now());
        user.setId(1L);

        Pass pass = new Pass();
        pass.setUsedRides(0);
        pass.setNoOfRidesPermitted(10);
        pass.setExpirationDate(Instant.now().plus(Duration.ofDays(30)));
        pass.setOwner(user);

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(passRepository.findByExpirationDateBeforeAndOwner_IdAndUsedRidesIsLessThanNoOfRidesPermitted(Mockito.any(), Mockito.anyLong())).thenReturn(Optional.empty());
        Mockito.when(passRepository.save(Mockito.any())).thenReturn(pass);

        Pass found = passService.addPass(new PassRequest(10), 1L);

        assertThat(found.getOwner()).isEqualTo(user);
    }
}
