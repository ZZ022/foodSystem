package com.example.register.model;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PostInfoRepository extends JpaRepository<PostInfo, Integer> {
    List<PostInfo> findAllByOrderByDateDesc();
    List<PostInfo> findAllByUser(User user);
    List<PostInfo> findAllByUserOrderByDateDesc(User user);
    List<PostInfo> findAllByFoodtagOrderByDateDesc(Foodtag foodtag);
    List<PostInfo> findAllByFoodtag(Foodtag foodtag);
}
