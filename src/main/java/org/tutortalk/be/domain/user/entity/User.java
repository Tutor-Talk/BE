package org.tutortalk.be.domain.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.Period;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false)
    private String name; // 이름

    private String email; // 이메일(로그인 ID), 구글/일반 공용, 구글 로그인 시 추가 정보를 받기 위해 일시적으로 허용 추후에 수정해야 함

    private String password; // 일반 회원가입 시 이용, 구글 가입 시 null

    @Column(nullable = false)
    private LocalDate birthDate; // 생년월일

    @Column(nullable = false)
    private String phone; // 전화번호

    @Column(nullable = false)
    private String region; //지역

    private String googleId; // 구글 식별자 코드

    private String profileImage; // 구글 프로필 사진

    private boolean isProfileComplete = false;

    /** 현재 만 나이 계산 */
    @Transient
    public int getAge() {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    // 구글 로그인 사용자인지 확인
    public boolean isGoogleUser(){
        return googleId != null;
    }

    public void setIsProfileComplete(boolean isProfileComplete) {
        this.isProfileComplete = isProfileComplete;
    }

    public void updateProfile(String name, String phone, String region, LocalDate birthDate) {
        if (name != null) this.name = name;
        if (phone != null) this.phone = phone;
        if (region != null) this.region = region;
        if (birthDate != null) this.birthDate = birthDate;
        this.isProfileComplete = true;
    }

    @Builder
    public User(String name, String email, String password, LocalDate birthDate, String phone, String region, String googleId, String profileImage) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.birthDate = birthDate;
        this.phone = phone;
        this.region = region;
        this.googleId = googleId;
        this.profileImage = profileImage;
    }
}
