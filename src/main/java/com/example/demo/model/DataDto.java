package com.example.demo.model;

import lombok.Data;

import java.util.List;

@Data
public class DataDto {
    private Integer totalPassengers;
    private Integer totalPages;
    private List<PassengerRecord> data;
}
