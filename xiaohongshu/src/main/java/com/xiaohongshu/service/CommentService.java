package com.xiaohongshu.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaohongshu.dto.CommentDTO;
import com.xiaohongshu.entity.Comment;
import com.xiaohongshu.entity.Note;
import com.xiaohongshu.mapper.CommentMapper;
import com.xiaohongshu.mapper.NoteMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentMapper commentMapper;
    private final NoteMapper noteMapper;

    @Transactional
    public Long addComment(Long userId, CommentDTO dto) {
        Note note = noteMapper.selectById(dto.getNoteId());
        if (note == null) {
            throw new RuntimeException("笔记不存在");
        }

        Comment comment = new Comment();
        comment.setUserId(userId);
        comment.setNoteId(dto.getNoteId());
        comment.setParentId(dto.getParentId());
        comment.setContent(dto.getContent());
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUpdatedAt(LocalDateTime.now());
        commentMapper.insert(comment);

        note.setCommentCount(note.getCommentCount() + 1);
        noteMapper.updateById(note);

        return comment.getId();
    }

    public Page<Comment> getCommentList(Long noteId, Integer page, Integer size) {
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Comment::getNoteId, noteId);
        wrapper.orderByDesc(Comment::getCreatedAt);
        return commentMapper.selectPage(new Page<>(page, size), wrapper);
    }

    @Transactional
    public void deleteComment(Long userId, Long commentId) {
        Comment comment = commentMapper.selectById(commentId);
        if (comment == null) {
            throw new RuntimeException("评论不存在");
        }
        if (!comment.getUserId().equals(userId)) {
            throw new RuntimeException("无权限删除");
        }

        Note note = noteMapper.selectById(comment.getNoteId());
        note.setCommentCount(note.getCommentCount() - 1);
        noteMapper.updateById(note);

        commentMapper.deleteById(commentId);
    }
}