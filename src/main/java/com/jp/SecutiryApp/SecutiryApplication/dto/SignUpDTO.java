package com.jp.SecutiryApp.SecutiryApplication.dto;

import com.jp.SecutiryApp.SecutiryApplication.enums.Role;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.Set;

@Data
public class SignUpDTO {
    private String email;
    private String password;
    private String name;
    private Set<Role> roles;
}
