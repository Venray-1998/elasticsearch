package com.fwr.elasticsearch.controller;

import com.fwr.elasticsearch.dao.StudentRepository;
import com.fwr.elasticsearch.entity.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.UUID;

/**
 * @author fwr
 * @date 2020-12-02
 */
@RestController
@RequestMapping("/test2")
@Slf4j
public class ERController {

    @Autowired
    private StudentRepository studentRepository;

    @GetMapping("/insert")
    public void insert() {
        Student student = new Student();
        student.setId(UUID.randomUUID().toString());
        student.setName("张三");
        student.setComment("马保国你耗子尾汁");
        student.setBirthday(new Date());
        studentRepository.save(student);
    }

    @GetMapping("/update")
    public void update() {
        Student student = new Student();
        student.setId("d4b75ace-18b0-4223-bf2c-3f6a163e1b78");
        student.setName("张三更新");
        student.setComment("马保国你耗子尾汁");
        student.setBirthday(new Date());
        studentRepository.save(student);
    }

}
