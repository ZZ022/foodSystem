package com.example.register.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.example.register.model.PostInfo.PostInfoProjection;

import java.util.List;
import java.util.Optional;

public interface PostInfoRepository extends JpaRepository<PostInfo, Integer> {
//    PostInfo getByNId(Integer nid);
    Optional<PostInfo> findById(int id);
    List<PostInfo> findAllByOrderByLikedInfos();
    List<PostInfo> findAllByOrderByDateDesc();
    List<PostInfo> findAllByUser(User user);
    List<PostInfo> findAllByUserOrderByDateDesc(User user);
    List<PostInfo> findAllByFoodtagOrderByDateDesc(Foodtag foodtag);
    List<PostInfo> findAllByFoodtag(Foodtag foodtag);

//    @Query(value = "SELECT nId FROM T_postInfo", nativeQuery = true)
//    PostInfoProjection findAllProjectedById();
}
