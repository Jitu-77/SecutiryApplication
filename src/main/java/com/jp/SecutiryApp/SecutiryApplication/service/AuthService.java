package com.jp.SecutiryApp.SecutiryApplication.service;

import com.jp.SecutiryApp.SecutiryApplication.dto.LoginDTO;
import com.jp.SecutiryApp.SecutiryApplication.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService  jwtService;

    public String login(LoginDTO loginDTO){
        //authenticating via authentication manager
        // it implements the authenticate method
        //which furthur implements UsernamePasswordAuthenticationToken
        // we just need to pass down the data
        Authentication authentication =authenticationManager
                .authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginDTO.getEmail(),
                                loginDTO.getPassword())
                );
        // predefined method using entity class to get the user i.e principal
        UserEntity userEntity = (UserEntity) authentication.getPrincipal();
        // generate token
        String token = jwtService.generateToken(userEntity);
        return token;
    }
}
