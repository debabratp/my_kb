package com.my.kb.vo;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class UserVO {

    private String email;
    @Size(min = 8, message = "Password length should be more then 8")
    private String password;
    private String projectName;
}
