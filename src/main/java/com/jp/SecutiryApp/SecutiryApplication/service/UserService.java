package com.jp.SecutiryApp.SecutiryApplication.service;

import com.jp.SecutiryApp.SecutiryApplication.exception.ResourceNotFoundException;
import com.jp.SecutiryApp.SecutiryApplication.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(()-> new ResourceNotFoundException("username not found with "+username));
    }
}
