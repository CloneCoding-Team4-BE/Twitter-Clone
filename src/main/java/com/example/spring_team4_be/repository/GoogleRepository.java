package com.example.spring_team4_be.repository;

import com.example.spring_team4_be.entity.Google;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GoogleRepository extends JpaRepository<Google,Long> {
    List<Google> findByEmail(String Email);

}
