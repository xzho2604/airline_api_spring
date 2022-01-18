package com.example.demo.controllers;

import com.example.demo.model.PassengerRecord;
import com.example.demo.repository.AviationRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class PassengerAirlineController {

    private final ElasticsearchOperations elasticsearchTemplate;
    private final AviationRepository aviationRepository;

    public PassengerAirlineController(ElasticsearchOperations elasticsearchTemplate,
                                      AviationRepository aviationRepository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.aviationRepository = aviationRepository;
    }

    @GetMapping("passenger_id/{id}")
    public List<PassengerRecord> getPassengerById(@PathVariable String id) {
        var resultPage = aviationRepository.findById(id, Pageable.ofSize(10));

        return resultPage.get().collect(Collectors.toList());
    }

    @GetMapping("trips/greater/{number}/{size}")
    public List<PassengerRecord> getTripGreaterThan(
            @PathVariable @Min(value = 0, message = "The value must be positive") Integer number,
            @PathVariable @Min(value = 1, message = "The value must be at least one") Integer size) {
        var resultPage = aviationRepository.findByTripsGreaterThan(number, Pageable.ofSize(size));

        return resultPage.get().collect(Collectors.toList());
    }

    @GetMapping("filter")
    public List<PassengerRecord> getPassengerByQuery(
            @RequestParam @Min(value = 1, message = "The value must be at least one") Optional<Integer> size,
            @RequestParam Optional<String> passengerName,
            @RequestParam @Min(value = 0, message = "The value must be positive") Optional<Long> trips,
            @RequestParam Optional<Long> airlineId,
            @RequestParam Optional<String> airlineName,
            @RequestParam Optional<String> airlineLogo
    ) {

        Criteria criteria = new Criteria();

        if (passengerName.isPresent()) criteria = criteria.and("name").is(passengerName.get());
        if (trips.isPresent()) criteria = criteria.and("trips").is(trips.get());
        if (airlineId.isPresent()) criteria = criteria.and("airline.id").is(airlineId.get());
        if (airlineName.isPresent()) criteria = criteria.and("airline.name").is(airlineName.get());
        if (airlineLogo.isPresent()) criteria = criteria.and("airline.logo").is(airlineLogo.get());

        Query query = new CriteriaQuery(criteria);

        size.ifPresent(integer -> query.setPageable(Pageable.ofSize(integer)));

        SearchHits<PassengerRecord> searchHits = elasticsearchTemplate.search(query, PassengerRecord.class);

        return searchHits.get()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }


}
