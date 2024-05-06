package com.beginner.rest.webservices.restfulwebservices.filtering;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("filtering")
public class FilteringController {

    @GetMapping("/student")
    public Student getStudent() {
        return new Student(1, "Ege", new ArrayList<>(), "324retrhyjuyhrter");
    }

    @GetMapping("/student-dyna-filter")
    public MappingJacksonValue getStudentDynaFilter() {
        Teacher teacher = new Teacher(1, "Ege", new ArrayList<>(), "324retrhyjuyhrter");

        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(teacher);
        PropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("name");
        FilterProvider filters = new SimpleFilterProvider().addFilter("teacherFilter", filter);
        mappingJacksonValue.setFilters(filters);

        return mappingJacksonValue;
    }
}
