package com.beginner.rest.webservices.restfulwebservices.hibernate.repository;

import com.beginner.rest.webservices.restfulwebservices.hibernate.dao.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IDriverRepository extends JpaRepository<Driver, Integer> {

}
