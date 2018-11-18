package com.mlynarz.ardena.Controller;

import com.mlynarz.ardena.controller.HorseController;
import com.mlynarz.ardena.model.Horse;
import com.mlynarz.ardena.model.Level;
import com.mlynarz.ardena.payload.Request.HorseRequest;
import com.mlynarz.ardena.payload.Response.HorseResponse;
import com.mlynarz.ardena.service.HorseService;
import com.mlynarz.ardena.util.ModelMapper;
import jdk.nashorn.internal.objects.NativeJSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


@RunWith(SpringRunner.class)
@WebMvcTest(HorseController.class)
public class HorseControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private HorseService horseService;

    @Test
    @WithMockUser(roles = "USER")
    public void givenHorses_whenGetHorses_thenReturnJsonArray() throws Exception {

        HorseResponse horse = new HorseResponse("alex", Level.Basic);
        HorseResponse horse2 = new HorseResponse("alex2", Level.Basic);

        List<HorseResponse> allHorses = Arrays.asList(horse, horse2);

        given(horseService.getAllHorses()).willReturn(allHorses);

        mvc.perform(get("/api/horses")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].horseName", is(horse.getHorseName())));
    }

//    @Test
//    @WithMockUser(roles = "USER")
//    public void givenHorse_whenAddHorse_then_ReturnOk() throws Exception {
//
//        HorseRequest horseRequest = new HorseRequest("alex", Level.Basic);
//        Horse horse = new Horse("alex", Level.Basic);
//
//        given(horseService.addHorse(horseRequest)).willReturn(horse);
//
//        mvc.perform(post("/api/horses")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(Json.stringify(horseRequest)))
//                .andExpect(status().isOk());
//    }
}