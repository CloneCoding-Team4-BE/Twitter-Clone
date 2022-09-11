package com.example.spring_team4_be.entity;

import com.example.spring_team4_be.util.Timestamped;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Twit extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column()
    private String content;
    @Column()
    private Long reTwit; // 답글 ID

    @Column()
    private String url;


    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Twit> parentTwit;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "twit", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Heart> hearts;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "twit", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<ReTwit> reTwits; // 리트윗 테이블

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Member member;
}
