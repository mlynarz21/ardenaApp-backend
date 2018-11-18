package com.mlynarz.ardena.Service;

import com.mlynarz.ardena.exception.BadRequestException;
import com.mlynarz.ardena.exception.ConflictException;
import com.mlynarz.ardena.exception.ResourceNotFoundException;
import com.mlynarz.ardena.model.Horse;
import com.mlynarz.ardena.model.Lesson;
import com.mlynarz.ardena.model.Level;
import com.mlynarz.ardena.model.User;
import com.mlynarz.ardena.payload.Request.DateRequest;
import com.mlynarz.ardena.payload.Request.HorseRequest;
import com.mlynarz.ardena.payload.Request.LessonRequest;
import com.mlynarz.ardena.payload.Response.HorseResponse;
import com.mlynarz.ardena.payload.Response.LessonResponse;
import com.mlynarz.ardena.repository.HorseRepository;
import com.mlynarz.ardena.repository.LessonRepository;
import com.mlynarz.ardena.repository.UserRepository;
import com.mlynarz.ardena.security.jwt.UserPrincipal;
import com.mlynarz.ardena.service.EmailService;
import com.mlynarz.ardena.service.HorseService;
import com.mlynarz.ardena.service.LessonService;
import com.mlynarz.ardena.util.ModelMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
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
public class LessonServiceIntegrationTest {

    @TestConfiguration
    static class LessonServiceIntegrationTestContextConfiguration {

        @Bean
        public LessonService lessonService() {
            return new LessonService();
        }
    }

    @Autowired
    private LessonService lessonService;

    @MockBean
    private LessonRepository lessonRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private EmailService emailService;


    @Test
    public void whenGetLessonsByDate_thenLessonListShouldBeReturned() {
        List<LessonResponse> resultList = new ArrayList<>();
        List<Lesson> returnList = new ArrayList<>();
        String name = "u1";
        String username = "username";
        String email = "email@o2.pl";
        String password = "pass";
        String phoneNumber = "111111111";
        User user = new User(name, username, email, password, phoneNumber, Instant.now());
        user.setId(1L);
        Lesson lesson = new Lesson(Level.Basic, Instant.now(), user);
        Lesson lesson2 = new Lesson(Level.Advanced, Instant.now(), user);
        resultList.add(ModelMapper.mapLessonToLessonResponse(lesson));
        resultList.add(ModelMapper.mapLessonToLessonResponse(lesson2));
        returnList.add(lesson);
        returnList.add(lesson2);

        Mockito.when(lessonRepository.findByDateGreaterThanEqualAndDateBetweenOrderByDate(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(returnList);

        List<LessonResponse> found = lessonService.getLessonsByDate(new DateRequest(Instant.now()));

        assertThat(found.get(0).getLessonLevel()).isEqualTo(Level.Basic);
        assertThat(found.get(1).getLessonLevel()).isEqualTo(Level.Advanced);
    }

    @Test
    public void whenGetLessonsByDateAndInstructor_thenLessonListShouldBeReturned() {
        List<LessonResponse> resultList = new ArrayList<>();
        List<Lesson> returnList = new ArrayList<>();
        String name = "u1";
        String username = "username";
        String email = "email@o2.pl";
        String password = "pass";
        String phoneNumber = "111111111";
        User user = new User(name, username, email, password, phoneNumber, Instant.now());
        user.setId(1L);
        Lesson lesson = new Lesson(Level.Basic, Instant.now(), user);
        Lesson lesson2 = new Lesson(Level.Advanced, Instant.now(), user);
        resultList.add(ModelMapper.mapLessonToLessonResponse(lesson));
        resultList.add(ModelMapper.mapLessonToLessonResponse(lesson2));
        returnList.add(lesson);
        returnList.add(lesson2);

        Mockito.when(lessonRepository.findByInstructor_IdAndDateGreaterThanEqualAndDateBetweenOrderByDate(Mockito.anyLong(), Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(returnList);

        List<LessonResponse> found = lessonService.getLessonsByDateAndInstructor(new DateRequest(Instant.now()), 1L);

        assertThat(found.get(0).getLessonLevel()).isEqualTo(Level.Basic);
        assertThat(found.get(0).getInstructor().getId()).isEqualTo(1L);
        assertThat(found.get(1).getLessonLevel()).isEqualTo(Level.Advanced);
        assertThat(found.get(1).getInstructor().getId()).isEqualTo(1L);
    }

    @Test
    public void whenGetAllLessons_thenLessonListShouldBeReturned() {
        List<LessonResponse> resultList = new ArrayList<>();
        List<Lesson> returnList = new ArrayList<>();
        String name = "u1";
        String username = "username";
        String email = "email@o2.pl";
        String password = "pass";
        String phoneNumber = "111111111";
        User user = new User(name, username, email, password, phoneNumber, Instant.now());
        user.setId(1L);
        Lesson lesson = new Lesson(Level.Basic, Instant.now(), user);
        Lesson lesson2 = new Lesson(Level.Advanced, Instant.now(), user);
        resultList.add(ModelMapper.mapLessonToLessonResponse(lesson));
        resultList.add(ModelMapper.mapLessonToLessonResponse(lesson2));
        returnList.add(lesson);
        returnList.add(lesson2);

        Mockito.when(lessonRepository.findAll())
                .thenReturn(returnList);

        List<LessonResponse> found = lessonService.getAllLessons();

        assertThat(found.get(0).getLessonLevel()).isEqualTo(Level.Basic);
        assertThat(found.get(0).getInstructor().getId()).isEqualTo(1L);
        assertThat(found.get(1).getLessonLevel()).isEqualTo(Level.Advanced);
        assertThat(found.get(1).getInstructor().getId()).isEqualTo(1L);
    }

    @Test
    public void whenGetLessonsByInstructor_thenLessonListShouldBeReturned() {
        List<LessonResponse> resultList = new ArrayList<>();
        List<Lesson> returnList = new ArrayList<>();
        String name = "u1";
        String username = "username";
        String email = "email@o2.pl";
        String password = "pass";
        String phoneNumber = "111111111";
        User user = new User(name, username, email, password, phoneNumber, Instant.now());
        user.setId(1L);
        Lesson lesson = new Lesson(Level.Basic, Instant.now(), user);
        Lesson lesson2 = new Lesson(Level.Advanced, Instant.now(), user);
        resultList.add(ModelMapper.mapLessonToLessonResponse(lesson));
        resultList.add(ModelMapper.mapLessonToLessonResponse(lesson2));
        returnList.add(lesson);
        returnList.add(lesson2);

        Mockito.when(lessonRepository.findByInstructor_IdAndDateGreaterThanEqualOrderByDate(Mockito.anyLong(), Mockito.any()))
                .thenReturn(returnList);

        List<LessonResponse> found = lessonService.getLessonsByInstructor(1L);

        assertThat(found.get(0).getLessonLevel()).isEqualTo(Level.Basic);
        assertThat(found.get(0).getInstructor().getId()).isEqualTo(1L);
        assertThat(found.get(1).getLessonLevel()).isEqualTo(Level.Advanced);
        assertThat(found.get(1).getInstructor().getId()).isEqualTo(1L);
    }

    @Test
    public void whenGetLessonById_thenLessonShouldBeReturned() {
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

        LessonResponse found = lessonService.getLessonById(1L);

        assertThat(found.getLessonLevel()).isEqualTo(Level.Basic);
        assertThat(found.getInstructor().getId()).isEqualTo(1L);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void whenGetLessonById_noLesson_thenLessonShouldBeReturned() {
        String name = "u1";
        String username = "username";
        String email = "email@o2.pl";
        String password = "pass";
        String phoneNumber = "111111111";
        User user = new User(name, username, email, password, phoneNumber, Instant.now());
        user.setId(1L);
        Lesson lesson = new Lesson(Level.Basic, Instant.now(), user);
        lesson.setId(1L);

        Mockito.when(lessonRepository.findById(1L)).thenReturn(Optional.empty());

        LessonResponse found = lessonService.getLessonById(1L);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void whenDeleteLesson_noLesson_thenThrowException() {
        String name = "u1";
        String username = "username";
        String email = "email@o2.pl";
        String password = "pass";
        String phoneNumber = "111111111";
        User user = new User(name, username, email, password, phoneNumber, Instant.now());
        user.setId(1L);
        Lesson lesson = new Lesson(Level.Basic, Instant.now(), user);
        lesson.setId(1L);

        Mockito.when(lessonRepository.findById(1L)).thenReturn(Optional.empty());

        lessonService.deleteLesson(1L, 1L);
    }

    @Test(expected = BadRequestException.class)
    public void whenDeleteLesson_notOwner_thenThrowException() {
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

        lessonService.deleteLesson(1L, 2L);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void whenUpdateLesson_noLesson_thenThrowException() {
        String name = "u1";
        String username = "username";
        String email = "email@o2.pl";
        String password = "pass";
        String phoneNumber = "111111111";
        User user = new User(name, username, email, password, phoneNumber, Instant.now());
        user.setId(1L);
        Lesson lesson = new Lesson(Level.Basic, Instant.now(), user);
        lesson.setId(1L);

        Mockito.when(lessonRepository.findById(1L)).thenReturn(Optional.empty());

        UserPrincipal userPrincipal = UserPrincipal.create(user);

        lessonService.updateLesson(1L, new LessonRequest(), userPrincipal);
    }

    @Test(expected = BadRequestException.class)
    public void whenUpdateLesson_notOwner_thenThrowException() {
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
        Lesson lesson = new Lesson(Level.Basic, Instant.now(), user2);
        lesson.setId(1L);

        Mockito.when(lessonRepository.findById(1L)).thenReturn(Optional.of(lesson));

        UserPrincipal userPrincipal = UserPrincipal.create(user);

        lessonService.updateLesson(1L, new LessonRequest(), userPrincipal);
    }

    @Test(expected = BadRequestException.class)
    public void whenAddLesson_dateBefore_thenThrowException() {
        String name = "u1";
        String username = "username";
        String email = "email@o2.pl";
        String password = "pass";
        String phoneNumber = "111111111";
        User user = new User(name, username, email, password, phoneNumber, Instant.now());
        user.setId(1L);

        lessonService.addLesson(new LessonRequest(Level.Advanced, username, Instant.now().minus(Duration.ofDays(1))), 1L);
    }

    @Test(expected = BadRequestException.class)
    public void whenAddLesson_notFree_thenThrowException() {
        String name = "u1";
        String username = "username";
        String email = "email@o2.pl";
        String password = "pass";
        String phoneNumber = "111111111";
        User user = new User(name, username, email, password, phoneNumber, Instant.now());
        user.setId(1L);
        Lesson lesson = new Lesson(Level.Basic, Instant.now().plus(Duration.ofDays(1)), user);
        lesson.setId(1L);

        Mockito.when(lessonRepository.findByInstructorAndDateBetween(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(lesson);
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));


        lessonService.addLesson(new LessonRequest(Level.Advanced, username, Instant.now().plus(Duration.ofDays(1))), 1L);
    }

    @Test
    public void whenAddLesson_thenReturnLesson() {
        String name = "u1";
        String username = "username";
        String email = "email@o2.pl";
        String password = "pass";
        String phoneNumber = "111111111";
        User user = new User(name, username, email, password, phoneNumber, Instant.now());
        user.setId(1L);
        Lesson lesson = new Lesson(Level.Basic, Instant.now().plus(Duration.ofDays(1)), user);
        lesson.setId(1L);

        Mockito.when(lessonRepository.findByInstructorAndDateBetween(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(null);
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(lessonRepository.save(Mockito.any())).thenReturn(lesson);

        Lesson found = lessonService.addLesson(new LessonRequest(Level.Advanced, username, Instant.now().plus(Duration.ofDays(1))), 1L);
        assertThat(found.getLessonLevel()).isEqualTo(Level.Basic);
        assertThat(found.getInstructor().getId()).isEqualTo(1L);
    }
}
