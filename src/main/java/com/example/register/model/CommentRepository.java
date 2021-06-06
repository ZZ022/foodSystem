package com.example.register.model;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<CommentInfo,Integer> {

}
