package com.capgemini.parnextgen.controller;

import com.capgemini.parnextgen.model.Lesson;
import com.capgemini.parnextgen.payload.ApiResponse;
import com.capgemini.parnextgen.payload.Request.DateRequest;
import com.capgemini.parnextgen.payload.Request.LessonRequest;
import com.capgemini.parnextgen.payload.Response.LessonResponse;
import com.capgemini.parnextgen.security.jwt.CurrentUser;
import com.capgemini.parnextgen.security.jwt.UserPrincipal;
import com.capgemini.parnextgen.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/lessons")
public class LessonController {

    @Autowired
    LessonService lessonService;

    @GetMapping
    public List<LessonResponse> getLessons() {
        return lessonService.getAllLessons();
    }

    @GetMapping("/{lessonId}")
    public LessonResponse getLessonById(@PathVariable Long lessonId) {
        return lessonService.getLessonById(lessonId);
    }

    @PostMapping("/date")
    public List<LessonResponse> getLessonsByDate(@RequestBody DateRequest dateRequest) {
        return lessonService.getLessonsByDate(dateRequest);
    }

    @PostMapping("/userDate")
    public List<LessonResponse> getLessonsByDateAndUser(@RequestBody DateRequest dateRequest, @CurrentUser UserPrincipal currentUser) {
        return lessonService.getLessonsByDateAndUser(dateRequest, currentUser.getId());
    }

    @GetMapping("/instructor/{instructorId}")
    public List<LessonResponse> getLessonsByInstructor(@PathVariable Long instructorId) {
        return lessonService.getLessonsByInstructor(instructorId);
    }

    @GetMapping("/instructor")
    public List<LessonResponse> getLessonsByInstructor(@CurrentUser UserPrincipal currentUser) {
        return lessonService.getLessonsByInstructor(currentUser.getId());
    }

    @PostMapping("/instructorDate")
    public List<LessonResponse> getLessonsByDateAndInstructor(@RequestBody DateRequest dateRequest, @CurrentUser UserPrincipal currentUser) {
        return lessonService.getLessonsByDateAndInstructor(dateRequest, currentUser.getId());
    }

    @PostMapping
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<?> addLesson(@Valid @RequestBody LessonRequest lessonRequest, @CurrentUser UserPrincipal currentUser) {
        Lesson lesson = lessonService.addLesson(lessonRequest, currentUser.getId());

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{lessonId}")
                .buildAndExpand(lesson.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "Lesson added Successfully"));
    }

    @DeleteMapping("/{lessonId}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<?> deleteLesson(@PathVariable Long lessonId, @CurrentUser UserPrincipal currentUser) {

        lessonService.deleteLesson(lessonId, currentUser.getId());

        return ResponseEntity.ok(new ApiResponse(true, "Lesson deleted"));
    }

}
