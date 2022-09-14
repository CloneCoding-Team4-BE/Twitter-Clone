package com.example.spring_team4_be.repository;

import com.example.spring_team4_be.entity.Google;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GoogleRepository extends JpaRepository<Google,Long> {
    Optional<Google> findByEmail(String Email);

}
