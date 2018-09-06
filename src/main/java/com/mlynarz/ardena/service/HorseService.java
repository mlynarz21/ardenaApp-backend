package com.mlynarz.ardena.service;

import com.mlynarz.ardena.exception.ConflictException;
import com.mlynarz.ardena.exception.ResourceNotFoundException;
import com.mlynarz.ardena.model.Horse;
import com.mlynarz.ardena.payload.Request.HorseRequest;
import com.mlynarz.ardena.payload.Response.HorseResponse;
import com.mlynarz.ardena.repository.HorseRepository;
import com.mlynarz.ardena.util.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Service
public class HorseService {
    @Autowired
    HorseRepository horseRepository;

    public List<HorseResponse> getAllHorses(){

        List<HorseResponse> horseResponses = new ArrayList<>();
        for(Horse horse: horseRepository.findAll())
            horseResponses.add(ModelMapper.mapHorseToHorseResponse(horse));

        return horseResponses;
    }

    public Horse addHorse(HorseRequest horseRequest){
        if(horseRepository.existsByHorseName(horseRequest.getHorseName())) {
            throw new ConflictException("Horse with that name already exists!");
        }
        Horse newHorse = new Horse();
        newHorse.setHorseName(horseRequest.getHorseName());

        return horseRepository.save(newHorse);
    }

    private boolean canAddHorseWithName(String name){
        return horseRepository.existsByHorseName(name);
    }

    public void deleteHorse(long id){
        Horse horse = horseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Horse", "id", id));

        horseRepository.delete(horse);
    }

    public void updateHorse(long id, HorseRequest horseRequest) {
        if(horseRepository.existsByHorseName(horseRequest.getHorseName())) {
            throw new ConflictException("Horse with that name already exists!");
        }
        Horse horse = horseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Horse", "id", id));
        horse.setHorseName(horseRequest.getHorseName());
        horseRepository.save(horse);
        }
}
