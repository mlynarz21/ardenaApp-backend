package com.capgemini.parnextgen.service;

import com.capgemini.parnextgen.exception.ConflictException;
import com.capgemini.parnextgen.exception.ResourceNotFoundException;
import com.capgemini.parnextgen.model.Lesson;
import com.capgemini.parnextgen.model.Reservation;
import com.capgemini.parnextgen.payload.Request.DateRequest;
import com.capgemini.parnextgen.payload.Request.LessonRequest;
import com.capgemini.parnextgen.payload.Response.LessonResponse;
import com.capgemini.parnextgen.repository.LessonRepository;
import com.capgemini.parnextgen.repository.UserRepository;
import com.capgemini.parnextgen.util.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
public class LessonService {
    @Autowired
    LessonRepository lessonRepository;

    @Autowired
    UserRepository userRepository;

    public List<LessonResponse> getLessonsByDate(DateRequest dateRequest){

        Instant dayStart = Instant.now();
        Instant dayEnd = Instant.now();
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(dateRequest.getDate());
        calendar1.set(calendar1.get(Calendar.YEAR),calendar1.get(Calendar.MONTH),calendar1.get(Calendar.DAY_OF_MONTH),0,0,0);
        dayStart = calendar1.toInstant();
        dayEnd = dayStart.plus(Duration.ofHours(23));
        dayEnd = dayEnd.plus(Duration.ofMinutes(59));

        List<LessonResponse> lessonResponses = new ArrayList<>();
        for(Lesson lesson: lessonRepository.findByDateGreaterThanEqualAndDateBetweenOrderByDate(Instant.now(),dayStart, dayEnd))
            lessonResponses.add(ModelMapper.mapLessonToLessonResponse(lesson));

        return lessonResponses;
    }

    public List<LessonResponse> getLessonsByDateAndUser(DateRequest dateRequest, long userId){

        Instant dayStart = Instant.now();
        Instant dayEnd = Instant.now();
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(dateRequest.getDate());
        calendar1.set(calendar1.get(Calendar.YEAR),calendar1.get(Calendar.MONTH),calendar1.get(Calendar.DAY_OF_MONTH),0,0,0);
        dayStart = calendar1.toInstant();
        dayEnd = dayStart.plus(Duration.ofHours(23));
        dayEnd = dayEnd.plus(Duration.ofMinutes(59));

        List<LessonResponse> lessonResponses = new ArrayList<>();
        for(Lesson lesson: lessonRepository.findByDateGreaterThanEqualAndDateBetweenOrderByDate(Instant.now(),dayStart, dayEnd)) {
            if (!containsUser(lesson.getReservations(), userId))
                lessonResponses.add(ModelMapper.mapLessonToLessonResponse(lesson));
        }

        return lessonResponses;
    }

    public List<LessonResponse> getLessonsByDateAndInstructor(DateRequest dateRequest, long instructorId) {

        Instant dayStart = Instant.now();
        Instant dayEnd = Instant.now();
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(dateRequest.getDate());
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

    private boolean containsUser(List<Reservation> reservations, long userId){
        for (Reservation reservation: reservations) {
            if(reservation.getRider().getId()==userId)
                return true;
        }
        return false;
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
        newLesson.setInstructor(userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id",userId)));
        newLesson.setDate(lessonRequest.getDate().toInstant());
        newLesson.setLessonLevel(lessonRequest.getLessonLevel());

        return lessonRepository.save(newLesson);
    }

    public void deleteLesson(Long lessonId, Long id) {
        Lesson lessonToDelete = lessonRepository.findById(lessonId).orElseThrow(() -> new ResourceNotFoundException("Lesson", "id",lessonId));
        if(!lessonToDelete.getInstructor().getId().equals(id))
            throw new ConflictException("You are not the owner of that lesson");

        lessonRepository.delete(lessonToDelete);
    }

    public LessonResponse getLessonById(Long lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> new ResourceNotFoundException("Lesson", "id",lessonId));
        return ModelMapper.mapLessonToLessonResponse(lesson);
    }
}
