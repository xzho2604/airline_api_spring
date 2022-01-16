package com.example.demo.config;

import com.example.demo.model.DataDto;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "passengerClient", url = "https://api.instantwebtools.net/v1")
public interface PassengerClient {

    @GetMapping("/passenger")
    DataDto getPassengerData(@RequestParam int page, @RequestParam int size);
}
