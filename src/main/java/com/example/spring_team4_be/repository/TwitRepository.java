package com.example.spring_team4_be.repository;

import com.example.spring_team4_be.dto.reponse.TwitResponseDto;
import com.example.spring_team4_be.entity.ReTwit;
import com.example.spring_team4_be.entity.Twit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TwitRepository extends JpaRepository<Twit,Long> {
    @Query("select new com.example.spring_team4_be.dto.reponse." +
            "TwitResponseDto(t.member.id, t.member.nickname,t.member.imageUrl, t.id ,rt.createdAt , t.content, t.url)" +
            " FROM Twit t, ReTwit rt WHERE NOT(t.member.id = :id) AND rt.member.id = :id AND rt.twit.id=t.id" )
    List<TwitResponseDto> findMyReTwit(@Param("id")Long member_id);

    @Query("select new com.example.spring_team4_be.dto.reponse." +
            "TwitResponseDto(t.member.id, t.member.nickname,t.member.imageUrl, t.id ,t.createdAt , t.content, t.url)" +  "FROM Twit t WHERE t.member.id = :id")
    List<TwitResponseDto> findMyTwit(@Param("id")Long member_id);


}
