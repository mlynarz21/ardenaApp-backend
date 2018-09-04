package com.mlynarz.ardena.controller;

import com.mlynarz.ardena.model.Reservation;
import com.mlynarz.ardena.payload.ApiResponse;
import com.mlynarz.ardena.payload.Response.ReservationResponse;
import com.mlynarz.ardena.security.jwt.CurrentUser;
import com.mlynarz.ardena.security.jwt.UserPrincipal;
import com.mlynarz.ardena.service.ReservationService;
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

    @GetMapping("/user/history")
    public List<ReservationResponse> getReservationHistoryByUser(@CurrentUser UserPrincipal currentUser) {
        return reservationService.getReservationHistoryByUser(currentUser.getId());
    }

    @GetMapping("/instructor")
    public List<ReservationResponse> getPendingReservationsByInstructor(@CurrentUser UserPrincipal currentUser) {
        return reservationService.getPendingReservationsByInstructor(currentUser.getId());
    }

    @GetMapping("/instructor/unpaid")
    public List<ReservationResponse> getUnpaidReservationsByInstructor(@CurrentUser UserPrincipal currentUser) {
        return reservationService.getUnpaidReservationsByInstructor(currentUser.getId());
    }

    @GetMapping("/user/unpaid")
    public List<ReservationResponse> getUnpaidReservationsByUser(@CurrentUser UserPrincipal currentUser) {
        return reservationService.getUnpaidReservationsByUser(currentUser.getId());
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
