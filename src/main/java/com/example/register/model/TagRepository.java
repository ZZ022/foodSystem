package com.example.register.model;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Foodtag, Integer> {
    public Foodtag findByName(String name);
}
