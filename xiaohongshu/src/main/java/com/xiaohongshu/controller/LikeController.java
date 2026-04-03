package com.xiaohongshu.controller;

import com.xiaohongshu.common.Result;
import com.xiaohongshu.service.LikeService;
import com.xiaohongshu.util.UserContext;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;

@RestController
@RequestMapping("/api/like")
public class LikeController {

    @Resource
    private LikeService likeService;

    @PostMapping("/{noteId}")
    public Result<Boolean> toggleLike(@PathVariable Long noteId) {
        Long userId = UserContext.getUserId();
        boolean isLiked = likeService.toggleLike(userId, noteId);
        return Result.success(isLiked);
    }

    @GetMapping("/status/{noteId}")
    public Result<Boolean> getLikeStatus(@PathVariable Long noteId) {
        Long userId = UserContext.getUserId();
        boolean isLiked = likeService.isLiked(userId, noteId);
        return Result.success(isLiked);
    }
}
