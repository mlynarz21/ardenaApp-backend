package com.capgemini.parnextgen.controller;

import com.capgemini.parnextgen.model.Horse;
import com.capgemini.parnextgen.payload.ApiResponse;
import com.capgemini.parnextgen.payload.Request.HorseRequest;
import com.capgemini.parnextgen.payload.Response.HorseResponse;
import com.capgemini.parnextgen.service.HorseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/horses")
public class HorseController {

    @Autowired
    HorseService horseService;

    @GetMapping
    public List<HorseResponse> getHorses() {
        return horseService.getAllHorses();
    }

    @PostMapping
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<?> addHorse(@Valid @RequestBody HorseRequest horseRequest) {
        Horse horse = horseService.addHorse(horseRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{horseId}")
                .buildAndExpand(horse.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "Horse added Successfully"));
    }

    @DeleteMapping("/{horseId}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<?> deleteHorse(@PathVariable Long horseId) {

        horseService.deleteHorse(horseId);

        return ResponseEntity.ok(new ApiResponse(true, "Horse deleted"));
    }
}
