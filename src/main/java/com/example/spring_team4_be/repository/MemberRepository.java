package com.example.spring_team4_be.repository;

import com.example.spring_team4_be.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUserId(String userId);
    List<Member> findById(String id);
}
