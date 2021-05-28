package com.example.register.model;

<<<<<<< HEAD
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostInfoRepository extends JpaRepository<PostInfo, Integer> {
    List<PostInfo> findAllByOrderByDateDesc();
=======
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostInfoRepository extends JpaRepository<PostInfo, Integer> {
>>>>>>> 6f5c4cc... first commit
}
