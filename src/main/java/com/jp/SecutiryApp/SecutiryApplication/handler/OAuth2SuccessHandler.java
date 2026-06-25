package com.jp.SecutiryApp.SecutiryApplication.handler;

import com.jp.SecutiryApp.SecutiryApplication.entity.UserEntity;
import com.jp.SecutiryApp.SecutiryApplication.service.JwtService;
import com.jp.SecutiryApp.SecutiryApplication.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.rmi.ServerException;
@RequiredArgsConstructor
@Component
@Slf4j
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserService userService;
    private final JwtService jwtService;
    @Value("${deploy.env}")
    private String deployEnv;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request ,
                                           HttpServletResponse response ,
                                           Authentication authentication) throws IOException, ServerException
    {
        OAuth2AuthenticationToken authenticationToken = (OAuth2AuthenticationToken) authentication;
        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authenticationToken.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        log.info("USER"+oAuth2User.toString());
        log.info("USER"+oAuth2User.getAttribute("email"));

        UserEntity user = userService.loadUserByEmail(email);

        if(user == null){
           UserEntity newUser =UserEntity.builder()
                   .name(oAuth2User.getAttribute("name"))
                   .email(email)
                   .build();
                user = userService.save(newUser);
        }

        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure("production".equals(deployEnv));
        response.addCookie(cookie);

        //our custom front end where the flow is to be redirected
        String frontEndUrl = "http://localhost:8080/home.html?token="+accessToken;
        // to redirect from BE
        response.sendRedirect(frontEndUrl);
        //or
//        getRedirectStrategy().sendRedirect(request, response, frontEndUrl);

    }
}
