package com.example.spring_team4_be.repository;

import com.example.spring_team4_be.entity.Heart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HeartRepository extends JpaRepository<Heart,Long> {
}
