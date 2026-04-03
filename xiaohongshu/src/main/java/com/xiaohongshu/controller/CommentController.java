package com.xiaohongshu.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaohongshu.common.Result;
import com.xiaohongshu.dto.CommentDTO;
import com.xiaohongshu.entity.Comment;
import com.xiaohongshu.service.CommentService;
import com.xiaohongshu.util.UserContext;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

    @Resource
    private CommentService commentService;

    @PostMapping
    public Result<Long> addComment(@Valid @RequestBody CommentDTO commentDTO) {
        Long userId = UserContext.getUserId();
        Long commentId = commentService.addComment(userId, commentDTO);
        return Result.success(commentId);
    }

    @GetMapping("/note/{noteId}")
    public Result<Page<Comment>> getCommentList(@PathVariable Long noteId,
                                          @RequestParam(defaultValue = "1") Integer page,
                                          @RequestParam(defaultValue = "10") Integer size) {
        Page<Comment> commentPage = commentService.getCommentList(noteId, page, size);
        return Result.success(commentPage);
    }

    @DeleteMapping("/{commentId}")
    public Result<String> deleteComment(@PathVariable Long commentId) {
        Long userId = UserContext.getUserId();
        commentService.deleteComment(userId, commentId);
        return Result.success("删除成功");
    }
}
