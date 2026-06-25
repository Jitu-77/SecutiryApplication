package com.jp.SecutiryApp.SecutiryApplication.controller;

import com.jp.SecutiryApp.SecutiryApplication.dto.LoginDTO;
import com.jp.SecutiryApp.SecutiryApplication.dto.LoginResponseDTO;
import com.jp.SecutiryApp.SecutiryApplication.dto.SignUpDTO;
import com.jp.SecutiryApp.SecutiryApplication.dto.UserDTO;
import com.jp.SecutiryApp.SecutiryApplication.service.AuthService;
import com.jp.SecutiryApp.SecutiryApplication.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    public final UserService userService;
    public  final AuthService authService;

    @Value("${deploy.env}")
    private String deployEnv;


    @PostMapping("/signup")
    public ResponseEntity<UserDTO> signUp(@RequestBody SignUpDTO signUpDTO){
        UserDTO userDTO = userService.signUp(signUpDTO);
        return ResponseEntity.ok(userDTO);
    }

    //we cannot create login in user service as it will
    //create circular dependency between the user service and the auth Manager
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginDTO loginDTO ,
                                        HttpServletResponse response // to add the http details
    ){
        // // normal token setUp
//        public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO ,
//                HttpServletResponse response // to add the http details
//    ){
        // normal token setUp
//        String token = authService.login(loginDTO);
//
//        Cookie cookie =new Cookie("token",token);
//        cookie.setHttpOnly(true); // to ensure req is of HTTP method only
//        //cookie.setSecure(); // for https
//        response.addCookie(cookie);
//        return ResponseEntity.ok(token);

        LoginResponseDTO loginResponseDTO = authService.login(loginDTO);
        Cookie cookie = new Cookie("refreshToken",loginResponseDTO.getRefreshToken());
        cookie.setHttpOnly(true);
        cookie.setSecure("production".equals(deployEnv));
        response.addCookie(cookie);
        return  ResponseEntity.ok(loginResponseDTO);
    }


    @PostMapping("/refreshToken")
    public ResponseEntity<LoginResponseDTO> refreshToken(HttpServletRequest request){
        String refreshToken = Arrays.stream(request.getCookies())
                .filter(cookie-> "refreshToken".equals(cookie.getName()))
                .findFirst()
                .map(cookie -> cookie.getValue())
                .orElseThrow(()-> new AuthenticationServiceException("Refresh token not found inside the Cookies"));


        LoginResponseDTO loginResponseDTO = authService.refreshToken(refreshToken);

        return ResponseEntity.ok(loginResponseDTO);
    }


}