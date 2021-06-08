package com.example.register.model;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MediaRepository extends JpaRepository<Media, Integer> {
    Boolean existsByPostInfo(PostInfo post);
    Media findByPostInfo(PostInfo post);
}
