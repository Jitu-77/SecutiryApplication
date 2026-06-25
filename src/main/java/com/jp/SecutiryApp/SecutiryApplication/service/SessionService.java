package com.jp.SecutiryApp.SecutiryApplication.service;

import com.jp.SecutiryApp.SecutiryApplication.entity.SessionEntity;
import com.jp.SecutiryApp.SecutiryApplication.entity.UserEntity;
import com.jp.SecutiryApp.SecutiryApplication.repository.SessionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SessionService {
    private final SessionRepository sessionRepository;
    private final int SESSION_LIMIT = 2;
    @Transactional
    public void generateNewSession(UserEntity userEntity,String refreshToken){
        List<SessionEntity> userSessions = sessionRepository.findByUser(userEntity);
        if(userSessions.size() == SESSION_LIMIT){
            //find the last session
            // delete the last session details
            userSessions.sort((Comparator.comparing(userSession -> userSession.getLastUsedAt())));
            SessionEntity lastSessionDetails = userSessions.getFirst();
            sessionRepository.delete(lastSessionDetails);
        }
        //store new session details
        SessionEntity newSession = SessionEntity
                                    .builder()
                                    .user(userEntity)
                                    .refreshToken(refreshToken)
                                    .build();
        sessionRepository.save(newSession);
    }
    @Transactional
    public void validateSession(String refreshToken){
        SessionEntity session = sessionRepository.findByRefreshToken(refreshToken)
                .orElseThrow(()->new SessionAuthenticationException("Session not found for refreshToken: "+refreshToken));
        session.setLastUsedAt(LocalDateTime.now());
        sessionRepository.save(session);
    }
}
