package com.mlynarz.ardena.controller;

import com.mlynarz.ardena.exception.ResourceNotFoundException;
import com.mlynarz.ardena.model.Level;
import com.mlynarz.ardena.model.RoleName;
import com.mlynarz.ardena.model.User;
import com.mlynarz.ardena.payload.*;
import com.mlynarz.ardena.payload.Request.RoleRequest;
import com.mlynarz.ardena.payload.Request.UserRequest;
import com.mlynarz.ardena.payload.Response.ApiResponse;
import com.mlynarz.ardena.payload.Response.EventResponse;
import com.mlynarz.ardena.payload.Response.PagedResponse;
import com.mlynarz.ardena.repository.EventRepository;
import com.mlynarz.ardena.repository.UserRepository;
import com.mlynarz.ardena.repository.VoteRepository;
import com.mlynarz.ardena.security.jwt.CurrentUser;
import com.mlynarz.ardena.security.jwt.UserPrincipal;
import com.mlynarz.ardena.service.EventService;
import com.mlynarz.ardena.service.UserService;
import com.mlynarz.ardena.util.AppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private EventService eventService;

    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/users/instructors")
    public List<UserSummary> getInstructors() {
        return userService.getUsersByRole(RoleName.ROLE_INSTRUCTOR);
    }

    @GetMapping("/users/admins")
    public List<UserSummary> getAdmins() {
        return userService.getUsersByRole(RoleName.ROLE_ADMIN);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("users/role/add/{username}")
    public ResponseEntity<?> addUserRole(@PathVariable String username, @Valid @RequestBody RoleRequest roleRequest) {

        userService.addUserRole(username, roleRequest);

        return ResponseEntity.ok(new ApiResponse(true, "Role added"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("users/role/remove/{userId}")
    public ResponseEntity<?> removeUserRole(@PathVariable Long userId, @Valid @RequestBody RoleRequest roleRequest) {

        userService.deleteUserRole(userId, roleRequest);

        return ResponseEntity.ok(new ApiResponse(true, "Role removed"));
    }

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER')")
    public UserSummary getCurrentUser(@CurrentUser UserPrincipal currentUser) {
        UserSummary userSummary = new UserSummary(currentUser.getId(), currentUser.getUsername(), currentUser.getName(), Level.Basic);
        return userSummary;
    }

    @GetMapping("/user/checkUsernameAvailability")
    public UserIdentityAvailability checkUsernameAvailability(@RequestParam(value = "username") String username) {
        Boolean isAvailable = !userRepository.existsByUsername(username);
        return new UserIdentityAvailability(isAvailable);
    }

    @GetMapping("/user/checkEmailAvailability")
    public UserIdentityAvailability checkEmailAvailability(@RequestParam(value = "email") String email) {
        Boolean isAvailable = !userRepository.existsByEmail(email);
        return new UserIdentityAvailability(isAvailable);
    }

    @GetMapping("/users/{username}")
    public UserProfile getUserProfile(@PathVariable(value = "username") String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        long eventCount = eventRepository.countByCreatedBy(user.getId());
        long voteCount = voteRepository.countByUserId(user.getId());

        UserProfile userProfile = new UserProfile(user.getId(), user.getUsername(), user.getName(), user.getCreatedAt(), eventCount, voteCount, user.getRiderLevel());

        return userProfile;
    }

    @GetMapping("/users/{username}/events")
    public PagedResponse<EventResponse> getEventsCreatedBy(@PathVariable(value = "username") String username,
                                                          @CurrentUser UserPrincipal currentUser,
                                                          @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                          @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        return eventService.getEventsCreatedBy(username, currentUser, page, size);
    }


    @GetMapping("/users/{username}/votes")
    public PagedResponse<EventResponse> getEventsVotedBy(@PathVariable(value = "username") String username,
                                                        @CurrentUser UserPrincipal currentUser,
                                                        @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                        @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        return eventService.getEventsVotedBy(username, currentUser, page, size);
    }

    @GetMapping("/users")
    public List<UserSummary> getUsers() {
        return userService.getAllUsers();
    }


    @PreAuthorize("hasRole('INSTRUCTOR')")
    @PatchMapping("users/{userId}")
    public ResponseEntity<?> changeUserLevel(@PathVariable Long userId, @Valid @RequestBody UserRequest userRequest) {

        userService.setUserLevel(userId, userRequest);

        return ResponseEntity.ok(new ApiResponse(true, "Level changed"));
    }

}
