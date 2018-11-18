package com.mlynarz.ardena.Service;

import com.mlynarz.ardena.exception.ConflictException;
import com.mlynarz.ardena.exception.ResourceNotFoundException;
import com.mlynarz.ardena.model.Horse;
import com.mlynarz.ardena.model.Level;
import com.mlynarz.ardena.payload.Request.HorseRequest;
import com.mlynarz.ardena.payload.Response.HorseResponse;
import com.mlynarz.ardena.repository.HorseRepository;
import com.mlynarz.ardena.repository.UserRepository;
import com.mlynarz.ardena.service.HorseService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@RunWith(SpringRunner.class)
public class HorseServiceIntegrationTest {

    @TestConfiguration
    static class HorseServiceIntegrationTestContextConfiguration {

        @Bean
        public HorseService horseService() {
            return new HorseService();
        }
    }

    @Autowired
    private HorseService horseService;

    @MockBean
    private HorseRepository horseRepository;

    @Test
    public void whenGetAllHorses_thenHorseListShouldBeReturned() {
        List<Horse> resultList = new ArrayList<>();
        String name = "h1";
        String name2 = "h2";
        Horse horse = new Horse(name, Level.Basic);
        Horse horse2 = new Horse(name2, Level.Basic);
        horse.setId(1L);
        horse2.setId(2L);
        resultList.add(horse);
        resultList.add(horse2);

        Mockito.when(horseRepository.findAll()).thenReturn(resultList);

        List<HorseResponse> found = horseService.getAllHorses();

        assertThat(found.get(0).getHorseName()).isEqualTo(name);
        assertThat(found.get(1).getHorseName()).isEqualTo(name2);
    }

    @Test
    public void whenAddHorse_thenReturnHorse() {
        String name = "h1";
        Horse horse = new Horse(name, Level.Basic);
        horse.setId(1L);

        Mockito.when(horseRepository.existsByHorseName(name)).thenReturn(false);
        Mockito.when(horseRepository.save(horse)).thenReturn(horse);

        Horse found = horseService.addHorse(new HorseRequest(name, Level.Basic));

        assertThat(found.getHorseName()).isEqualTo(name);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void whenDeleteHorse_noHorse_thenThrowException() {
        String name = "h1";
        Horse horse = new Horse(name, Level.Basic);
        horse.setId(1L);

        Mockito.when(horseRepository.findById(1L)).thenReturn(Optional.empty());

        horseService.deleteHorse(1L);
    }

    @Test(expected = ConflictException.class)
    public void whenUpdateHorse_horseExists_thenThrowException() {
        String name = "h1";
        String name2 = "h2";
        Horse horse = new Horse(name, Level.Basic);
        horse.setId(1L);

        Mockito.when(horseRepository.findById(1L)).thenReturn(Optional.of(horse));
        Mockito.when(horseRepository.existsByHorseName(name2)).thenReturn(true);

        horseService.updateHorse(1L, new HorseRequest(name2, Level.Basic));
    }

    @Test
    public void whenUpdateHorse_thenThrowException() {
        String name = "h1";
        String name2 = "h2";
        Horse horse = new Horse(name, Level.Basic);
        horse.setId(1L);

        Mockito.when(horseRepository.findById(1L)).thenReturn(Optional.of(horse));
        Mockito.when(horseRepository.existsByHorseName(name2)).thenReturn(false);

        horseService.updateHorse(1L, new HorseRequest(name2, Level.Basic));
    }
}
