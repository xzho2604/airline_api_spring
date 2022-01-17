package com.example.demo.repository;

import com.example.demo.model.PassengerRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AviationRepository extends ElasticsearchRepository<PassengerRecord, String> {

    Page<PassengerRecord> findById(String id, Pageable pageable);

    Page<PassengerRecord> findByTripsGreaterThan(Integer number, Pageable pageable);
}
