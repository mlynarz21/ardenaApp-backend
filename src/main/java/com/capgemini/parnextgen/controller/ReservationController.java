package com.capgemini.parnextgen.controller;

import com.capgemini.parnextgen.model.Reservation;
import com.capgemini.parnextgen.payload.ApiResponse;
import com.capgemini.parnextgen.payload.Response.ReservationResponse;
import com.capgemini.parnextgen.security.jwt.CurrentUser;
import com.capgemini.parnextgen.security.jwt.UserPrincipal;
import com.capgemini.parnextgen.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    ReservationService reservationService;

    @PostMapping("lesson/{lessonId}")
    public ResponseEntity<?> addReservation(@PathVariable Long lessonId, @CurrentUser UserPrincipal currentUser) {
        Reservation lesson = reservationService.addReservation(lessonId, currentUser.getId());

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{reservationId}")
                .buildAndExpand(lesson.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "Reservation added Successfully"));
    }

    @GetMapping("/user")
    public List<ReservationResponse> getReservationsByUser(@CurrentUser UserPrincipal currentUser) {
        return reservationService.getReservationsByUser(currentUser.getId());
    }

    @GetMapping("/instructor")
    public List<ReservationResponse> getPendingReservationsByInstructor(@CurrentUser UserPrincipal currentUser) {
        return reservationService.getPendingReservationsByInstructor(currentUser.getId());
    }

    @PatchMapping("cancel/{reservationId}")
    public ResponseEntity<?> cancelReservation(@PathVariable Long reservationId, @CurrentUser UserPrincipal currentUser) {

        reservationService.cancelReservation(reservationId, currentUser.getId());

        return ResponseEntity.ok(new ApiResponse(true, "Reservation cancelled"));
    }

    @PatchMapping("accept/{reservationId}")
    public ResponseEntity<?> acceptReservation(@PathVariable Long reservationId, @CurrentUser UserPrincipal currentUser) {

        reservationService.acceptReservation(reservationId, currentUser.getId());

        return ResponseEntity.ok(new ApiResponse(true, "Reservation accepted"));
    }

}
