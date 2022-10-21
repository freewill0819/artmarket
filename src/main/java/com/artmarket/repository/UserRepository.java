package com.artmarket.repository;

import com.artmarket.domain.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    @Query(value = "SELECT * FROM user WHERE username = ?1 AND password = ?2",nativeQuery = true)
    User login(String username, String password);
}
