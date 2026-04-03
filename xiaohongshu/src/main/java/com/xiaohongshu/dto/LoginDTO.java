package com.xiaohongshu.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginDTO {
    private String phone;
    private String email;

    @NotBlank(message = "密码不能为空")
    private String password;
}