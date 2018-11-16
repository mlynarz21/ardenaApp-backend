package com.mlynarz.ardena.service;

import com.mlynarz.ardena.exception.BadRequestException;
import com.mlynarz.ardena.exception.ResourceNotFoundException;
import com.mlynarz.ardena.model.*;
import com.mlynarz.ardena.payload.Request.DateRequest;
import com.mlynarz.ardena.payload.Request.LessonRequest;
import com.mlynarz.ardena.payload.Response.LessonResponse;
import com.mlynarz.ardena.repository.LessonRepository;
import com.mlynarz.ardena.repository.UserRepository;
import com.mlynarz.ardena.security.jwt.UserPrincipal;
import com.mlynarz.ardena.util.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class LessonService {
    @Autowired
    LessonRepository lessonRepository;

    @Autowired
    UserRepository userRepository;

    private static final int MAX_USERS_ON_LESSON = 2;

    public List<LessonResponse> getLessonsByDate(DateRequest dateRequest){

        Instant dayStart = Instant.now();
        Instant dayEnd = Instant.now();
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(Date.from(dateRequest.getDate()));
        calendar1.set(calendar1.get(Calendar.YEAR),calendar1.get(Calendar.MONTH),calendar1.get(Calendar.DAY_OF_MONTH),0,0,0);
        dayStart = calendar1.toInstant();
        dayEnd = dayStart.plus(Duration.ofHours(23));
        dayEnd = dayEnd.plus(Duration.ofMinutes(59));

        List<LessonResponse> lessonResponses = new ArrayList<>();
        for(Lesson lesson: lessonRepository.findByDateGreaterThanEqualAndDateBetweenOrderByDate(Instant.now(),dayStart, dayEnd)) {
            lessonResponses.add(ModelMapper.mapLessonToLessonResponse(lesson));
        }

        return lessonResponses;
    }

    public List<LessonResponse> getLessonsByDateAndUser(DateRequest dateRequest, long userId){

        Instant dayStart = Instant.now();
        Instant dayEnd = Instant.now();
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(Date.from(dateRequest.getDate()));
        calendar1.set(calendar1.get(Calendar.YEAR),calendar1.get(Calendar.MONTH),calendar1.get(Calendar.DAY_OF_MONTH),0,0,0);
        dayStart = calendar1.toInstant();
        dayEnd = dayStart.plus(Duration.ofHours(23));
        dayEnd = dayEnd.plus(Duration.ofMinutes(59));

        List<LessonResponse> lessonResponses = new ArrayList<>();
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id",userId));
        for(Lesson lesson: lessonRepository.findByDateGreaterThanEqualAndDateBetweenOrderByDate(Instant.now(),dayStart, dayEnd)) {
            if (!containsUserUnCancelledReservations(lesson.getReservations(), userId)
                    && getActiveReservationCountForLesson(lesson) < MAX_USERS_ON_LESSON
                    && compareLevels(user.getRiderLevel(),lesson.getLessonLevel())>0)
                lessonResponses.add(ModelMapper.mapLessonToLessonResponse(lesson));
        }

        return lessonResponses;
    }

    public List<LessonResponse> getLessonsByDateAndInstructor(DateRequest dateRequest, long instructorId) {

        Instant dayStart = Instant.now();
        Instant dayEnd = Instant.now();
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(Date.from(dateRequest.getDate()));
        calendar1.set(calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), calendar1.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        dayStart = calendar1.toInstant();
        dayEnd = dayStart.plus(Duration.ofHours(23));
        dayEnd = dayEnd.plus(Duration.ofMinutes(59));

        List<LessonResponse> lessonResponses = new ArrayList<>();
        for (Lesson lesson : lessonRepository.findByInstructor_IdAndDateGreaterThanEqualAndDateBetweenOrderByDate(instructorId, Instant.now(), dayStart, dayEnd)) {
            lessonResponses.add(ModelMapper.mapLessonToLessonResponse(lesson));
        }

        return lessonResponses;
    }

    private boolean containsUserUnCancelledReservations(List<Reservation> reservations, long userId){
        for (Reservation reservation: reservations) {
            if(reservation.getRider().getId()==userId && !reservation.getStatus().equals(Status.Cancelled))
                return true;
        }
        return false;
    }

    public List<LessonResponse> getAllComingLessons(UserPrincipal currentUser){
        User user = userRepository.findById(currentUser.getId()).orElseThrow(() -> new ResourceNotFoundException("User", "id",currentUser.getId()));

        List<LessonResponse> lessonResponses = new ArrayList<>();
        for(Lesson lesson: lessonRepository.findByDateGreaterThanEqual(Instant.now()))
            if (!containsUserUnCancelledReservations(lesson.getReservations(), currentUser.getId())
                    && getActiveReservationCountForLesson(lesson) < MAX_USERS_ON_LESSON
                    && compareLevels(user.getRiderLevel(),lesson.getLessonLevel())>0) {
                lessonResponses.add(ModelMapper.mapLessonToLessonResponse(lesson));
            }

        return lessonResponses;
    }

    public List<LessonResponse> getAllLessons(){

        List<LessonResponse> lessonResponses = new ArrayList<>();
        for(Lesson lesson: lessonRepository.findAll())
            lessonResponses.add(ModelMapper.mapLessonToLessonResponse(lesson));

        return lessonResponses;
    }

    public List<LessonResponse> getLessonsByInstructor(Long instructorId) {
        List<LessonResponse> lessonResponses = new ArrayList<>();
        for(Lesson lesson: lessonRepository.findByInstructor_IdAndDateGreaterThanEqualOrderByDate(instructorId, Instant.now()))
            lessonResponses.add(ModelMapper.mapLessonToLessonResponse(lesson));

        return lessonResponses;
    }

    public Lesson addLesson(LessonRequest lessonRequest, long userId){
        Lesson newLesson = new Lesson();

        if(Instant.now().isAfter(lessonRequest.getDate()))
            throw new BadRequestException("Cannot add lesson before now!");

        User instructor = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id",userId));

        if(isInstructorFree(lessonRequest.getDate(), instructor)) {
            newLesson.setInstructor(instructor);
            newLesson.setDate(lessonRequest.getDate());
            newLesson.setLessonLevel(lessonRequest.getLessonLevel());
        }
        else throw new BadRequestException("You already have lesson on that date!");

        return lessonRepository.save(newLesson);
    }

    public void deleteLesson(Long lessonId, Long id) {
        Lesson lessonToDelete = lessonRepository.findById(lessonId).orElseThrow(() -> new ResourceNotFoundException("Lesson", "id",lessonId));
        if(!lessonToDelete.getInstructor().getId().equals(id))
            throw new BadRequestException("You are not the owner of that lesson");

        lessonRepository.delete(lessonToDelete);
    }

    public LessonResponse getLessonById(Long lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> new ResourceNotFoundException("Lesson", "id",lessonId));
        return ModelMapper.mapLessonToLessonResponse(lesson);
    }

    public void updateLesson(Long lessonId, LessonRequest lessonRequest, UserPrincipal currentUser) {
        Lesson lessonToUpdate = lessonRepository.findById(lessonId).orElseThrow(() -> new ResourceNotFoundException("Lesson", "id",lessonId));
        if(!lessonToUpdate.getInstructor().getId().equals(currentUser.getId()))
            throw new BadRequestException("You are not the owner of that lesson");
        lessonToUpdate.setInstructor(userRepository.findByUsername(lessonRequest.getInstructor()).orElseThrow(() -> new ResourceNotFoundException("User", "username",lessonRequest.getInstructor())));
        lessonToUpdate.setDate(lessonRequest.getDate());
        lessonToUpdate.setLessonLevel(lessonRequest.getLessonLevel());
        lessonRepository.save(lessonToUpdate);
    }

    private boolean isInstructorFree(Instant date, User instructor){
        return lessonRepository.findByInstructorAndDateBetween(instructor, date.minus(1, ChronoUnit.MINUTES), date.plus(1, ChronoUnit.MINUTES))==null;
    }

    private int getActiveReservationCountForLesson(Lesson lesson) {
        int counter=0;
        for (Reservation r : lesson.getReservations()) {
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
