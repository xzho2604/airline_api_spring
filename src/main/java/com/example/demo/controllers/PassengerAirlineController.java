package com.example.demo.controllers;

import com.example.demo.model.PassengerRecord;
import com.example.demo.repository.AviationRepository;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class PassengerAirlineController {

    private final RestHighLevelClient client;
    private final ElasticsearchOperations elasticsearchTemplate;
    private final AviationRepository aviationRepository;

    public PassengerAirlineController(RestHighLevelClient client, ElasticsearchOperations elasticsearchTemplate, AviationRepository aviationRepository) {
        this.client = client;
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.aviationRepository = aviationRepository;
    }

    @GetMapping("/{id}")
    public List<PassengerRecord> getPassengerById(@PathVariable int id) {
        var resultPage = aviationRepository.findById(id, Pageable.ofSize(10));

        return resultPage.get().collect(Collectors.toList());
    }

}
