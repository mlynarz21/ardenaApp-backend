package com.mlynarz.ardena.service;

import com.mlynarz.ardena.exception.AppException;
import com.mlynarz.ardena.exception.ResourceNotFoundException;
import com.mlynarz.ardena.model.Role;
import com.mlynarz.ardena.model.RoleName;
import com.mlynarz.ardena.model.User;
import com.mlynarz.ardena.payload.Request.RoleRequest;
import com.mlynarz.ardena.payload.Request.UserRequest;
import com.mlynarz.ardena.payload.UserSummary;
import com.mlynarz.ardena.repository.RoleRepository;
import com.mlynarz.ardena.repository.UserRepository;
import com.mlynarz.ardena.util.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    public List<UserSummary> getUsersByRole(RoleName roleName){
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new AppException(roleName + " Role not set."));
        List<User> users= userRepository.findByRoles(role);
        List<UserSummary> userSummaries = new ArrayList<>();
        for (User u:users) {
            userSummaries.add(ModelMapper.mapUserToUserSummary(u));
        }
        return userSummaries;
    }

    public void addUserRole(String username, RoleRequest roleRequest) {
        Role role = roleRepository.findByName(roleRequest.getName())
                .orElseThrow(() -> new AppException(roleRequest.getName() + " Role not set."));
       User user = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User", "username",username));
       Set<Role> roles = user.getRoles();
       roles.add(role);
       user.setRoles(roles);
       userRepository.save(user);
    }

    public void deleteUserRole(Long userId, RoleRequest roleRequest) {
        Role role = roleRepository.findByName(roleRequest.getName())
                .orElseThrow(() -> new AppException(roleRequest.getName() + " Role not set."));
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id",userId));
        Set<Role> roles = user.getRoles();
        roles.remove(role);
        user.setRoles(roles);
        userRepository.save(user);
    }

    public List<UserSummary> getAllUsers(){
        List<User> users = userRepository.findAll();
        List<UserSummary> userSummaries = new ArrayList<>();
        for (User u:users) {
            userSummaries.add(ModelMapper.mapUserToUserSummary(u));
        }
        return userSummaries;
    }


    public void setUserLevel(Long userId, UserRequest userRequest) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id",userId));
        user.setRiderLevel(userRequest.getLevel());
        userRepository.save(user);
    }
}
