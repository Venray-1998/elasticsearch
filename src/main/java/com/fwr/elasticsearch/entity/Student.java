package com.fwr.elasticsearch.entity;

import io.github.classgraph.json.Id;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author fwr
 * @date 2020-12-02
 */
@Data
@Document(indexName = "student")
public class Student {

    @Id
    private String id;

    @Field(type = FieldType.Keyword)
    private String name;

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String comment;

    @Field(type = FieldType.Date, format = DateFormat.date)
    private Date birthday;
}
