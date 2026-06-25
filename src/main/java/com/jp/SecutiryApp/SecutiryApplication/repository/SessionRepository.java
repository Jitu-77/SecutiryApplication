package com.jp.SecutiryApp.SecutiryApplication.repository;

import com.jp.SecutiryApp.SecutiryApplication.entity.SessionEntity;
import com.jp.SecutiryApp.SecutiryApplication.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SessionRepository extends JpaRepository<SessionEntity,Long> {
    List<SessionEntity> findByUser(UserEntity userEntity);
    Optional<SessionEntity> findByRefreshToken(String refreshToken);
}
