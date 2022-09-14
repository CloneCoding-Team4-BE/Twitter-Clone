package com.example.spring_team4_be.repository;

import com.example.spring_team4_be.entity.Heart;
import com.example.spring_team4_be.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HeartRepository extends JpaRepository<Heart,Long> {
    List<Heart> findByMemberIdAndTwitId(Long member_id, Long twit_id);
    List<Heart> findByMemberId(Long member_id);
    int countAllByTwitId(Long twit_id);
    int countByMemberAndTwitId(Member member, Long twit_id);
}
