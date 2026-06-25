package com.jp.SecutiryApp.SecutiryApplication.service;

import com.jp.SecutiryApp.SecutiryApplication.dto.LoginDTO;
import com.jp.SecutiryApp.SecutiryApplication.dto.LoginResponseDTO;
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
    private final UserService userService;
    private final JwtService  jwtService;
    private final SessionService  sessionService;

//    public String login(LoginDTO loginDTO){
    public LoginResponseDTO login(LoginDTO loginDTO){
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
        String refreshToken = jwtService.generateRefreshToken(userEntity);
        // generate the record for the session
        sessionService.generateNewSession(userEntity,refreshToken);
//        return token;

        return new LoginResponseDTO(userEntity.getId(),token,refreshToken);
    }


    public LoginResponseDTO refreshToken(String refreshToken){
        Long userId = jwtService.getUserIdFromToken(refreshToken);
        // we must validate if the token is form the session or not
        sessionService.validateSession(refreshToken);
        UserEntity user = userService.getUserById(userId);

        String accessToken = jwtService.generateToken(user);
        //we are not generating a refreshToken , if the refreshToken expired then the user must login
        return new LoginResponseDTO(user.getId(),accessToken,refreshToken);
    }
}
