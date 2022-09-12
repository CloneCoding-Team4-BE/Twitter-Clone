package com.example.spring_team4_be.repository;

import com.example.spring_team4_be.entity.ReTwit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReTwitRepository extends JpaRepository<ReTwit,Long> {
    List<ReTwit> findAllByMemberIdAndTwitId(Long member_id, Long twit_id);


}
