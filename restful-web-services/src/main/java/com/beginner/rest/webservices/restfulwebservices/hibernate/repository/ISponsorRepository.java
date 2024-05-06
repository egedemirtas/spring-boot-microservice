package com.beginner.rest.webservices.restfulwebservices.hibernate.repository;

import com.beginner.rest.webservices.restfulwebservices.hibernate.dao.Sponsor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ISponsorRepository extends JpaRepository<Sponsor, Integer> {
}
