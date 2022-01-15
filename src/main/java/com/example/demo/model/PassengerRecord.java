package com.example.demo.model;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import javax.persistence.Id;
import java.util.List;

import static org.springframework.data.elasticsearch.annotations.FieldType.*;

@Document(indexName = "aviation")
@Data
public class PassengerRecord {
    @Id
    private String id;

    @Field(type = Text)
    private String name;

    @Field(type = Integer)
    private int trips;

    @Field(type = Nested, includeInParent = true)
    List<Airline> airline;
}
