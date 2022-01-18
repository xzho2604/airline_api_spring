package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;

import static org.springframework.data.elasticsearch.annotations.FieldType.Long;
import static org.springframework.data.elasticsearch.annotations.FieldType.Text;

@Data
public class Airline {
    @Field(type = Long)
    private Long id;

    @Field(type = Text)
    private String name;

    @Field(type = Text)
    private String country;

    @Field(type = Text)
    private String logo;

    @Field(type = Text)
    private String slogan;

    @Field(type = Text)
    @JsonProperty("head_quaters")
    private String headQuarters;

    @Field(type = Text)
    private String website;

    @Field(type = Text)
    private String established;
}
