package com.mlynarz.ardena.service;

import com.mlynarz.ardena.exception.BadRequestException;
import com.mlynarz.ardena.exception.ResourceNotFoundException;
import com.mlynarz.ardena.model.Pass;
import com.mlynarz.ardena.model.User;
import com.mlynarz.ardena.payload.Request.PassRequest;
import com.mlynarz.ardena.repository.PassRepository;
import com.mlynarz.ardena.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.Instant;
import java.util.Calendar;

@Service
public class PassService {
    @Autowired
    PassRepository passRepository;

    @Autowired
    UserRepository userRepository;

    public Pass getValidPass(long userId){
        return passRepository.findByExpirationDateBeforeAndOwner_IdAndUsedRidesIsLessThanNoOfRidesPermitted(Instant.now(), userId)
                .orElseThrow(() -> new BadRequestException("This user has no valid passes"));
    }

    public void updatePass(Pass pass){
        passRepository.save(pass);
    }

    public Pass addPass(PassRequest passRequest, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id",userId));

        if(passRepository.findByExpirationDateBeforeAndOwner_IdAndUsedRidesIsLessThanNoOfRidesPermitted(Instant.now(), userId).isPresent())
            throw new BadRequestException("User has passes that are still valid");

        Instant expirationDate = Instant.now();
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(Date.from(Instant.now()));
        calendar1.set(calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), calendar1.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        calendar1.add(Calendar.MONTH,2);
        expirationDate = calendar1.toInstant();

        Pass pass = new Pass();
        pass.setExpirationDate(expirationDate);
        pass.setNoOfRidesPermitted(passRequest.getNoOfRidesPermitted());
        pass.setUsedRides(0);
        pass.setOwner(user);
        return passRepository.save(pass);
    }
}
