package com.jp.SecutiryApp.SecutiryApplication.filter;

import com.jp.SecutiryApp.SecutiryApplication.entity.UserEntity;
import com.jp.SecutiryApp.SecutiryApplication.service.JwtService;
import com.jp.SecutiryApp.SecutiryApplication.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
@Component
@RequiredArgsConstructor
public class JwtAuthFilters  extends OncePerRequestFilter {

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver handlerExceptionResolver;

    private final JwtService jwtService;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            final String requestTokenHeader = request.getHeader("Authorization");
            if (requestTokenHeader == null || !requestTokenHeader.startsWith("Bearer")) {
                filterChain.doFilter(request, response); //pass on to the next filter
                return;
            }
            String token = requestTokenHeader.split("Bearer ")[1];
            Long userId = jwtService.getUserIdFromToken(token);
            if (userId != null &&
                    SecurityContextHolder.getContext().getAuthentication() == null // only when the context holder is null
            ) {
                UserEntity user = userService.getUserById(userId);
                //now got the user and userID
                // now make the authentication
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        //without roles
//                        new UsernamePasswordAuthenticationToken(user, null, null);
                        new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                //capturing the details of the user request for eg , ip address
                usernamePasswordAuthenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                //setting up the context holder
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                //passing the authentication filter
                filterChain.doFilter(request, response);
            }
        } catch (Exception e) {
            handlerExceptionResolver.resolveException(request,response,null,e);
        }

    }
}
