package com.example.register.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikedRepository extends JpaRepository<LikedInfo,Integer> {
    public int countLikedInfosByPostId(int postId);
    public boolean existsByUserIdAndAndPostId(int userid, int postId);
    public int countByUserId(int userid);
    public List<LikedInfo> findAllByPostId(int PostId);

    List<LikedInfo> findAllByUserId(int userId);
}
