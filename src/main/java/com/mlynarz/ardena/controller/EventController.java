package com.mlynarz.ardena.controller;

import com.mlynarz.ardena.model.Event;
import com.mlynarz.ardena.payload.Request.EventRequest;
import com.mlynarz.ardena.payload.Request.VoteRequest;
import com.mlynarz.ardena.payload.Response.ApiResponse;
import com.mlynarz.ardena.payload.Response.EventResponse;
import com.mlynarz.ardena.payload.Response.PagedResponse;
import com.mlynarz.ardena.security.jwt.CurrentUser;
import com.mlynarz.ardena.security.jwt.UserPrincipal;
import com.mlynarz.ardena.service.EventService;
import com.mlynarz.ardena.util.AppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventService eventService;

    private static final Logger logger = LoggerFactory.getLogger(EventController.class);

    @GetMapping
    public PagedResponse<EventResponse> getEvents(@CurrentUser UserPrincipal currentUser,
                                                  @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                  @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        return eventService.getAllEvents(currentUser, page, size);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createEvent(@Valid @RequestBody EventRequest eventRequest) {
        Event event = eventService.createEvent(eventRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{eventId}")
                .buildAndExpand(event.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "Event Created Successfully"));
    }


    @GetMapping("/{eventId}")
    public EventResponse getEventById(@CurrentUser UserPrincipal currentUser, @PathVariable Long eventId) {
        return eventService.getEventById(eventId, currentUser);
    }

    @PostMapping("/{eventId}/votes")
    @PreAuthorize("hasRole('USER')")
    public EventResponse castVote(@CurrentUser UserPrincipal currentUser, @PathVariable Long eventId, @Valid @RequestBody VoteRequest voteRequest) {
        return eventService.castVoteAndGetUpdatedEvent(eventId, voteRequest, currentUser);
    }

}
