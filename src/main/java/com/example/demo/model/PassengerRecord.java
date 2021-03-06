package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import javax.persistence.Id;
import java.util.List;

import static org.springframework.data.elasticsearch.annotations.FieldType.Long;
import static org.springframework.data.elasticsearch.annotations.FieldType.Nested;
import static org.springframework.data.elasticsearch.annotations.FieldType.Text;

@Document(indexName = "aviation")
@Data
public class PassengerRecord {
    @Id
    @JsonProperty("_id")
    private String id;

    @Field(type = Text)
    private String name;

    @Field(type = Long)
    private Long trips;

    @Field(type = Nested, includeInParent = true)
    List<Airline> airline;
}
