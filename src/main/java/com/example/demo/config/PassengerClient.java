package com.example.demo.config;

import com.example.demo.model.DataDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "passengerClient", url = "https://api.instantwebtools.net/v1")
public interface PassengerClient {

    @GetMapping("/passenger")
    DataDto getPassengerData(@RequestParam Integer page, @RequestParam Integer size);
}
