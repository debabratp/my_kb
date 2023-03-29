package com.my.kb.vo;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class LoginVO {

    private String email;
    @Size(min = 8)
    private String password;
}
