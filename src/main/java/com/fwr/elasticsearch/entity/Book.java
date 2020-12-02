package com.fwr.elasticsearch.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author fwr
 * @date 2020-12-02
 */
@Data
public class Book {

    private Integer id;

    private String name;

    private String text;

    @JSONField(format = "yyyy-MM-dd")
    private Date date;
}
