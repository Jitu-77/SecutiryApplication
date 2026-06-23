package com.jp.SecutiryApp.SecutiryApplication.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Data
public class SignUpDTO {
    private String email;
    private String password;
    private String name;
}
