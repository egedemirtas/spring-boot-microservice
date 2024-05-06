package com.beginner.rest.webservices.restfulwebservices.filtering;

import com.fasterxml.jackson.annotation.JsonFilter;

import java.util.List;

@JsonFilter("teacherFilter")
public class Teacher {

    private Integer id;
    private String name;
    private List<String> courses;
    private String password;

    public Teacher(Integer id, String name, List<String> courses, String password) {
        this.id = id;
        this.name = name;
        this.courses = courses;
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getCourses() {
        return courses;
    }

    public void setCourses(List<String> courses) {
        this.courses = courses;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
