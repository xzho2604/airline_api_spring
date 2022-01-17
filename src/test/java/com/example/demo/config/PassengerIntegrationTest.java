package com.example.demo.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class PassengerIntegrationTest {
    @Autowired
    public PassengerClient passengerClient;
    @Autowired
    private MockMvc mockMvc;

    @ParameterizedTest
    @MethodSource("pageArg")
    @DisplayName("Smoke Testing of the Feign client is working with the external API")
    public void getPassengerData(int page, int size, int expectedSize) {
        var dataDto = passengerClient.getPassengerData(page, size);

        var passengers = dataDto.getData();
        assertThat(passengers).hasSize(expectedSize);
    }

    @Test
    @DisplayName("get passenger info through /passenger endpoint with id should return the right passenger info")
    public void testPassengerIdEndpoint() throws Exception {
        mockMvc.perform(get("/passenger_id/5ff47cd6768bcfccba7c508b"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andExpect(content().string(containsString("5ff47cd6768bcfccba7c508b")));
    }

    @Test
    @DisplayName("get passenger info through /trips/greater endpoint with number of trips should return the right passenger info")
    public void testTripGreaterThanEndpoint() throws Exception {
        mockMvc.perform(get("/trips/greater/1973121/100"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(46)));
    }

    @ParameterizedTest
    @MethodSource("filterArgs")
    @DisplayName("filter passenger info using query parameters should return the right passengers info")
    public void testFilterEndpoint(MockHttpServletRequestBuilder getRequest, int expectedSize) throws Exception {

        mockMvc.perform(getRequest)
                .andDo(print())
                .andExpect(jsonPath("$.*", hasSize(expectedSize)))
                .andExpect(status().isOk());
    }

    private static Stream<Arguments> pageArg() {
        return Stream.of(
                Arguments.of(0, 2, 2),
                Arguments.of(1, 11, 11),
                Arguments.of(100, 20, 20)
        );
    }

    private static Stream<Arguments> filterArgs() {
        return Stream.of(
                Arguments.of(
                        get("/filter")
                                .param("passengerName", "Percy Townsend"),
                        57
                ),
                Arguments.of(
                        get("/filter")
                                .param("passengerName", "Glenn Moreira Filho")
                                .param("airlineName", "Eva Air"),
                        1
                ),
                Arguments.of(
                        get("/filter")
                                .param("trips", "250")
                                .param("airlineName", "Eva Air")
                                .param("passengerName", "Percy Townsend"),
                        56
                )
        );
    }


}