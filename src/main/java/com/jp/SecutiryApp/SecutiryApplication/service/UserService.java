package com.jp.SecutiryApp.SecutiryApplication.service;

import com.jp.SecutiryApp.SecutiryApplication.dto.SignUpDTO;
import com.jp.SecutiryApp.SecutiryApplication.dto.UserDTO;
import com.jp.SecutiryApp.SecutiryApplication.entity.UserEntity;
import com.jp.SecutiryApp.SecutiryApplication.exception.ResourceNotFoundException;
import com.jp.SecutiryApp.SecutiryApplication.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor

public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(()-> new ResourceNotFoundException("username not found with "+username));
    }

    public UserDTO signUp(SignUpDTO signUpDTO){
        Optional<UserEntity> userEntity = userRepository.findByEmail(signUpDTO.getEmail());
        if(userEntity.isPresent()){
            throw new BadCredentialsException("user with email "+signUpDTO.getEmail()+" exists in the DB");
        }
        UserEntity toBeCreated = modelMapper.map(signUpDTO , UserEntity.class );
        toBeCreated.setPassword(passwordEncoder.encode(toBeCreated.getPassword()));
        UserEntity savedUser =userRepository.save(toBeCreated);
        return modelMapper.map(savedUser,UserDTO.class);
    }

    public UserEntity getUserById(Long userId) {
        return userRepository.findById(userId).
                orElseThrow(() -> new ResourceNotFoundException("User with id "+ userId +
                " not found"));
    }
}
