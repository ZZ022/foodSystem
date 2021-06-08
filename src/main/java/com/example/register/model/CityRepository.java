package com.example.register.model;

import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.criteria.CriteriaBuilder;

public interface CityRepository extends JpaRepository<City, Integer> {
    boolean existsByName(String name);
    City getByName(String name);
}
