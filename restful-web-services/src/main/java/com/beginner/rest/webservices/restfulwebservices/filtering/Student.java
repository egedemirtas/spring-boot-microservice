package com.beginner.rest.webservices.restfulwebservices.filtering;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

// For static filtering of fields you can also use: @JsonIgnoreProperties
// in practice, this approach is not recommended since if you want to change
// the name of "password" to "passcode", you have to make changes in the annotation too
@JsonIgnoreProperties("password")
public class Student {
    @JsonIgnore
    private Integer id;
    private String name;
    private List<String> courses;
    private String password;

    public Student(Integer id, String name, List<String> courses, String password) {
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
