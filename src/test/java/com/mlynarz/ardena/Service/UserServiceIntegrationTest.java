package com.mlynarz.ardena.Service;

import com.mlynarz.ardena.exception.AppException;
import com.mlynarz.ardena.exception.ResourceNotFoundException;
import com.mlynarz.ardena.model.Level;
import com.mlynarz.ardena.model.Role;
import com.mlynarz.ardena.model.RoleName;
import com.mlynarz.ardena.model.User;
import com.mlynarz.ardena.payload.UserSummary;
import com.mlynarz.ardena.repository.RoleRepository;
import com.mlynarz.ardena.repository.UserRepository;
import com.mlynarz.ardena.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@RunWith(SpringRunner.class)
public class UserServiceIntegrationTest {

    @TestConfiguration
    static class UserServiceIntegrationTestContextConfiguration {

        @Bean
        public UserService userService() {
            return new UserService();
        }
    }

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoleRepository roleRepository;

    @Test
    public void whenGetUsersByRole_thenUserListShouldBeReturned() {
        String name = "u1";
        String username = "username";
        String username2 = "username2";
        String email = "email@o2.pl";
        String email2 = "email2@o2.pl";
        String password = "pass";
        String phoneNumber = "111111111";
        User user = new User(name, username, email, password, phoneNumber, Instant.now());
        User user2 = new User( name, username2, email2, password, phoneNumber, Instant.now());
        UserSummary userSummary = new UserSummary(1L, name, username, Level.Basic);
        UserSummary userSummary2 = new UserSummary(2L, name, username2, Level.Basic);
        user.setId(1L);
        user2.setId(2L);

        Role role = new Role(RoleName.ROLE_USER);

        List<User> users = new ArrayList<>();
        users.add(user);
        users.add(user2);

        List<UserSummary> userSummaries = new ArrayList<>();
        userSummaries.add(userSummary);
        userSummaries.add(userSummary2);

        Mockito.when(userRepository.findByRoles(role)).thenReturn(users);
        Mockito.when(roleRepository.findByName(RoleName.ROLE_USER)).thenReturn(Optional.of(role));

        List<UserSummary> found = userService.getUsersByRole(RoleName.ROLE_USER);

        assertThat(found.get(0).getUsername()).isEqualTo(username);
        assertThat(found.get(1).getUsername()).isEqualTo(username2);
    }

    @Test(expected = AppException.class)
    public void whenGetUsersByRole_NoRole_thenException() {
        String name = "u1";
        String username = "username";
        String username2 = "username2";
        String email = "email@o2.pl";
        String email2 = "email2@o2.pl";
        String password = "pass";
        String phoneNumber = "111111111";
        User user = new User(name, username, email, password, phoneNumber, Instant.now());
        User user2 = new User( name, username2, email2, password, phoneNumber, Instant.now());
        user.setId(1L);
        user2.setId(2L);

        Role role = new Role(RoleName.ROLE_USER);

        List<User> users = new ArrayList<>();
        users.add(user);
        users.add(user2);

        Mockito.when(userRepository.findByRoles(role)).thenReturn(users);
        Mockito.when(roleRepository.findByName(RoleName.ROLE_USER)).thenReturn(Optional.empty());

        List<UserSummary> found = userService.getUsersByRole(RoleName.ROLE_USER);
    }

    @Test
    public void whenGetAllUsers_thenUserListShouldBeReturned() {
        String name = "u1";
        String username = "username";
        String username2 = "username2";
        String email = "email@o2.pl";
        String email2 = "email2@o2.pl";
        String password = "pass";
        String phoneNumber = "111111111";
        User user = new User(name, username, email, password, phoneNumber, Instant.now());
        User user2 = new User( name, username2, email2, password, phoneNumber, Instant.now());
        UserSummary userSummary = new UserSummary(1L, name, username, Level.Basic);
        UserSummary userSummary2 = new UserSummary(2L, name, username2, Level.Basic);
        user.setId(1L);
        user2.setId(2L);

        Role role = new Role(RoleName.ROLE_USER);

        List<User> users = new ArrayList<>();
        users.add(user);
        users.add(user2);

        List<UserSummary> userSummaries = new ArrayList<>();
        userSummaries.add(userSummary);
        userSummaries.add(userSummary2);

        Mockito.when(userRepository.findAll()).thenReturn(users);

        List<UserSummary> found = userService.getAllUsers();

        assertThat(found.get(0).getUsername()).isEqualTo(username);
        assertThat(found.get(1).getUsername()).isEqualTo(username2);
    }

}
