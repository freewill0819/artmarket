package com.artmarket.repository;

import com.artmarket.domain.users.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByUsername(String username);

    @Query(value = "SELECT * FROM user WHERE username = ?1 AND password = ?2",nativeQuery = true)
    Users login(String username, String password);
}
