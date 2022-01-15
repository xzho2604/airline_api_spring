package com.example.demo.model;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;

import static org.springframework.data.elasticsearch.annotations.FieldType.Text;
import static org.springframework.data.elasticsearch.annotations.FieldType.Integer;

@Data
public class Airline {
    @Field(type = Integer)
    private int id;

    @Field(type = Text)
    private String name;

    @Field(type = Text)
    private String country;

    @Field(type = Text)
    private String logo;

    @Field(type = Text)
    private String slogan;

    @Field(type = Text)
    private String headQuarters;

    @Field(type = Text)
    private String webSite;

    @Field(type = Text)
    private String established;
}
