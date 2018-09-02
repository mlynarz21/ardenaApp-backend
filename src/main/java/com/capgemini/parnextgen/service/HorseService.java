package com.capgemini.parnextgen.service;

import com.capgemini.parnextgen.exception.ConflictException;
import com.capgemini.parnextgen.exception.ResourceNotFoundException;
import com.capgemini.parnextgen.model.Horse;
import com.capgemini.parnextgen.payload.Request.HorseRequest;
import com.capgemini.parnextgen.payload.Response.HorseResponse;
import com.capgemini.parnextgen.repository.HorseRepository;
import com.capgemini.parnextgen.util.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
