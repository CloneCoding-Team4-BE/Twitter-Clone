package com.example.spring_team4_be.entity;

import com.example.spring_team4_be.dto.request.MemberReqDto;
import com.example.spring_team4_be.dto.request.ProfileReqDto;
import com.example.spring_team4_be.dto.response.ImageResponseDto;
import com.example.spring_team4_be.util.Timestamped;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Member extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String nickname;

    @Column
    private String imageUrl;

    @Column
    private String bio;
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "member", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Heart> hearts;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "member", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<ReTwit> reTwits;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "member", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Twit> twits;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "follower")
    @JsonIgnore
    private List<Follow> followers;


    @OneToMany(fetch = FetchType.LAZY,mappedBy = "following", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Follow> followings;

    public Member(MemberReqDto memberReqDto){
        this.userId = memberReqDto.getUserId();
        this.password = memberReqDto.getPassword();
    }


    public void update(ProfileReqDto profileReqDto, ImageResponseDto imageResponseDto) {
        if (profileReqDto.getNickname() == null) {
            this.nickname = nickname;
        } else {
            this.nickname = profileReqDto.getNickname();
        }
        if (profileReqDto.getBio() == null) {
            this.bio = bio;
        } else {
            this.bio = profileReqDto.getBio();
        }

        if (imageResponseDto.getImageUrl() == null) {
            this.imageUrl = imageUrl;
        } else {
            this.imageUrl = imageResponseDto.getImageUrl();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        Member member = (Member) o;
        return id != null && Objects.equals(id, member.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public boolean validatePassword(PasswordEncoder passwordEncoder, String password) {
        return passwordEncoder.matches(password, this.password);
    }
}
