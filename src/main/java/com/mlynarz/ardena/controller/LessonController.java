package com.mlynarz.ardena.controller;

import com.mlynarz.ardena.model.Lesson;
import com.mlynarz.ardena.payload.Response.ApiResponse;
import com.mlynarz.ardena.payload.Request.DateRequest;
import com.mlynarz.ardena.payload.Request.LessonRequest;
import com.mlynarz.ardena.payload.Response.LessonResponse;
import com.mlynarz.ardena.security.jwt.CurrentUser;
import com.mlynarz.ardena.security.jwt.UserPrincipal;
import com.mlynarz.ardena.service.LessonService;
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

    @GetMapping("/coming")
    public List<LessonResponse> getComingLessons(@CurrentUser UserPrincipal currentUser) {
        return lessonService.getAllComingLessons(currentUser);
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

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @PatchMapping("{lessonId}")
    public ResponseEntity<?> updateLesson(@PathVariable Long lessonId, @Valid @RequestBody LessonRequest lessonRequest, @CurrentUser UserPrincipal currentUser) {
        lessonService.updateLesson(lessonId, lessonRequest, currentUser);
        return ResponseEntity.ok(new ApiResponse(true, "Lesson updated"));
    }

}
