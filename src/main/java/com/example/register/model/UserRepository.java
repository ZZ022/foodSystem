package com.example.register.model;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByName(String userName);
    boolean existsByName(String userName);
    boolean existsByNameAndPassword(String userName, String password);
    User findByNameAndPassword(String userName, String password);
    User findById(int userId);
}
