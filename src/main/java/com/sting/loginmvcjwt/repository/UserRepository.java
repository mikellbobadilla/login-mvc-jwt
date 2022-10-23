package com.sting.loginmvcjwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sting.loginmvcjwt.models.AppUser;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Long>{
  Optional<AppUser> findByUsername(String username);
  boolean existsByUsername(String username);
}