package com.example.register.model;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PostInfoRepository extends JpaRepository<PostInfo, Integer> {
    List<PostInfo> findAllByOrderByDateDesc();
}
