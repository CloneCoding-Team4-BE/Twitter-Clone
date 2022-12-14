package com.example.spring_team4_be.repository;

import com.example.spring_team4_be.dto.response.TwitSimpleResponseDto;
import com.example.spring_team4_be.entity.Twit;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface TwitRepository extends JpaRepository<Twit,Long> {
    @Query("select new com.example.spring_team4_be.dto.response." +
            "TwitSimpleResponseDto(t,rt.createdAt)" +
            " FROM Twit t, ReTwit rt WHERE NOT(t.member.id = :id) AND rt.member.id = :id AND rt.twit.id=t.id" )
    List<TwitSimpleResponseDto> findAllTwitWithReTwit(@Param("id")Long member_id);

    @Query("select new com.example.spring_team4_be.dto.response." +
            "TwitSimpleResponseDto(t ,t.createdAt)" +  "FROM Twit t WHERE t.member.id = :id")
    List<TwitSimpleResponseDto> findAllTwit(@Param("id")Long member_id);

    List<Twit> findAllByReTwit(Long twit_id);


    List<Twit> findAllByOrderByCreatedAtDesc(Pageable pageable);

    int countAllByReTwit(Long Twit_id);
}
