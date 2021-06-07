package com.example.register.model;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LikedRepository extends JpaRepository<LikedInfo,Integer> {
    public int countLikedInfosByPostId(int postId);
    public boolean existsByUserIdAndAndPostId(int userid, int postId);
    public int countByUserId(int userid);
}
