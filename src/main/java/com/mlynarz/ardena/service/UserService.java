package com.mlynarz.ardena.service;

import com.mlynarz.ardena.exception.AppException;
import com.mlynarz.ardena.model.Role;
import com.mlynarz.ardena.model.RoleName;
import com.mlynarz.ardena.model.User;
import com.mlynarz.ardena.payload.UserSummary;
import com.mlynarz.ardena.repository.RoleRepository;
import com.mlynarz.ardena.repository.UserRepository;
import com.mlynarz.ardena.util.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    public List<UserSummary> getInstructors(){
        Role instructorRole = roleRepository.findByName(RoleName.ROLE_INSTRUCTOR)
                .orElseThrow(() -> new AppException("User Role not set."));
        List<User> users= userRepository.findByRoles(instructorRole);
        List<UserSummary> userSummaries = new ArrayList<>();
        for (User u:users) {
            userSummaries.add(ModelMapper.mapUserToUserSummary(u));
        }
        return userSummaries;
    }
}
