package com.example.demo.config;

import com.example.demo.Application;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { Application.class })
class PassengerClientTest {
    @Autowired
    public PassengerClient passengerClient;

    @Test
    @DisplayName("Smoke Testing of the Feign client is working with the external API")
    public void getPassengerData(){
        var dataDto = passengerClient.getPassengerData(0,2);

        assertThat(dataDto.getTotalPassengers()).isEqualTo(28086);
        assertThat(dataDto.getTotalPages()).isEqualTo(14043);

        var passengers = dataDto.getData();
        assertThat(passengers).hasSize(2);

    }


}