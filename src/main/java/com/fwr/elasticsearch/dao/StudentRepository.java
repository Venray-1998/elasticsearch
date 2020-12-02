package com.fwr.elasticsearch.dao;

import com.fwr.elasticsearch.entity.Student;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author fwr
 * @date 2020-12-02
 */
public interface StudentRepository extends ElasticsearchRepository<Student, String> {

}
