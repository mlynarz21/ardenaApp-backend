package com.mlynarz.ardena.controller;

import com.mlynarz.ardena.model.Pass;
import com.mlynarz.ardena.payload.Response.ApiResponse;
import com.mlynarz.ardena.payload.Request.PassRequest;
import com.mlynarz.ardena.payload.Response.PassResponse;
import com.mlynarz.ardena.service.PassService;
import com.mlynarz.ardena.util.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/passes")
public class PassController {

    @Autowired
    PassService passService;

    @GetMapping("{username}")
    public PassResponse getUserPass(@PathVariable String username) {
        return ModelMapper.mapPassToPassResponse(passService.getValidPassByUsername(username));
    }

    @PostMapping("{userId}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<?> addPass(@Valid @RequestBody PassRequest passRequest, @PathVariable Long userId) {
        Pass pass = passService.addPass(passRequest, userId);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{passId}")
                .buildAndExpand(pass.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "Pass added Successfully"));
    }

}
