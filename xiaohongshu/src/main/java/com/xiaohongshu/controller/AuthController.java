package com.xiaohongshu.controller;

import com.xiaohongshu.common.Result;
import com.xiaohongshu.dto.LoginDTO;
import com.xiaohongshu.dto.RegisterDTO;
import com.xiaohongshu.service.AuthService;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Resource
    private AuthService authService;

    @PostMapping("/sendCode")
    public Result<String> sendCode(@RequestParam String account) {
        authService.sendCode(account);
        return Result.success("验证码发送成功");
    }

    @PostMapping("/register")
    public Result<String> register(@Valid @RequestBody RegisterDTO registerDTO) {
        authService.register(registerDTO);
        return Result.success("注册成功");
    }

    @PostMapping("/login")
    public Result<String> login(@RequestBody LoginDTO loginDTO) {
        String token = authService.login(loginDTO);
        return Result.success(token);
    }
}
