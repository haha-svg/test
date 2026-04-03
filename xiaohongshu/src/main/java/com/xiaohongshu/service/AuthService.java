package com.xiaohongshu.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xiaohongshu.dto.LoginDTO;
import com.xiaohongshu.dto.RegisterDTO;
import com.xiaohongshu.entity.User;
import com.xiaohongshu.mapper.UserMapper;
import com.xiaohongshu.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserMapper userMapper;
    private final JWTUtil jwtUtil;
    private final StringRedisTemplate redisTemplate;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public void register(RegisterDTO dto) {
        String key = dto.getPhone() != null ? dto.getPhone() : dto.getEmail();
        if (!verifyCode(key, dto.getCode())) {
            throw new RuntimeException("验证码错误或已过期");
        }

        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        if (dto.getPhone() != null) {
            wrapper.eq(User::getPhone, dto.getPhone());
        } else {
            wrapper.eq(User::getEmail, dto.getEmail());
        }
        if (userMapper.selectCount(wrapper) > 0) {
            throw new RuntimeException("该账号已注册");
        }

        User user = new User();
        user.setPhone(dto.getPhone());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setNickname(dto.getNickname());
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        userMapper.insert(user);

        redisTemplate.delete(key);
    }

    public String login(LoginDTO dto) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        if (dto.getPhone() != null) {
            wrapper.eq(User::getPhone, dto.getPhone());
        } else {
            wrapper.eq(User::getEmail, dto.getEmail());
        }
        User user = userMapper.selectOne(wrapper);
        if (user == null) {
            throw new RuntimeException("账号不存在");
        }

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        return jwtUtil.generateToken(user.getId());
    }

    public void sendCode(String phoneOrEmail) {
        String code = String.valueOf((int) ((Math.random() * 9 + 1) * 100000));
        redisTemplate.opsForValue().set("code:" + phoneOrEmail, code, 5, TimeUnit.MINUTES);
    }

    private boolean verifyCode(String phoneOrEmail, String code) {
        String savedCode = redisTemplate.opsForValue().get("code:" + phoneOrEmail);
        return code.equals(savedCode);
    }
}