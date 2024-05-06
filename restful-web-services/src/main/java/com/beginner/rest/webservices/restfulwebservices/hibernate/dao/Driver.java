package com.beginner.rest.webservices.restfulwebservices.hibernate.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Driver {
    @Id
    @GeneratedValue
    private Integer id;

    private String name;

    private LocalDateTime birthDate;

    /**
     * LAZY vs EAGER:
     * lazy: load it on demand; if getSponsorList() is called. This is useful if sponsorList is very large, and
     * it may not be efficient to load it if it is not being used actively
     * <p>
     * eager: fetch it whenever parent is fetched
     * <p>
     * mappedBy: this should be specified in the entity who owns the relationship
     */

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "driver")
    @JsonIgnore
    private List<Sponsor> sponsorList;

    public Driver() {

    }

    public Driver(Integer id, String name, LocalDateTime birthDate, List<Sponsor> sponsorList) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.sponsorList = sponsorList;
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

    public LocalDateTime getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDateTime birthDate) {
        this.birthDate = birthDate;
    }

    public List<Sponsor> getSponsorList() {
        return sponsorList;
    }

    public void setSponsorList(List<Sponsor> sponsorList) {
        this.sponsorList = sponsorList;
    }
}
