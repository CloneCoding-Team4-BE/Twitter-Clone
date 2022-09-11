package com.example.spring_team4_be.repository;

import com.example.spring_team4_be.entity.Twit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TwitRepository extends JpaRepository<Twit,Long> {
    List<Twit> findAllByOrderByCreatedAtDesc();

    int countAllByReTwit(Long Twit_id);
}
