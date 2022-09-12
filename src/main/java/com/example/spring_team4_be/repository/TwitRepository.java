package com.example.spring_team4_be.repository;

import com.example.spring_team4_be.dto.reponse.TwitSimpleResponseDto;
import com.example.spring_team4_be.entity.Twit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface TwitRepository extends JpaRepository<Twit,Long> {
    @Query("select new com.example.spring_team4_be.dto.reponse." +
            "TwitSimpleResponseDto(t,rt.createdAt)" +
            " FROM Twit t, ReTwit rt WHERE NOT(t.member.id = :id) AND rt.member.id = :id AND rt.twit.id=t.id" )
    List<TwitSimpleResponseDto> findAllTwitWithReTwit(@Param("id")Long member_id);

    @Query("select new com.example.spring_team4_be.dto.reponse." +
            "TwitSimpleResponseDto(t ,t.createdAt)" +  "FROM Twit t WHERE t.member.id = :id")
    List<TwitSimpleResponseDto> findAllTwit(@Param("id")Long member_id);

    List<Twit> findAllByReTwit(@Param("id")Long twit_id);
    List<Twit> findAllByOrderByCreatedAtDesc();

    int countAllByReTwit(Long Twit_id);
}
