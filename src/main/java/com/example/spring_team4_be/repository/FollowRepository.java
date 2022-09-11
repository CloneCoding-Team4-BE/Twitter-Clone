package com.example.spring_team4_be.repository;

import com.example.spring_team4_be.entity.Follow;
import com.example.spring_team4_be.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow,Long> {
    List<Follow> findAllByFollowerAndFollowing(Member follower, Member following);
}
