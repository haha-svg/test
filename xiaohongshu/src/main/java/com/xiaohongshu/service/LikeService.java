package com.xiaohongshu.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xiaohongshu.entity.Like;
import com.xiaohongshu.entity.Note;
import com.xiaohongshu.mapper.LikeMapper;
import com.xiaohongshu.mapper.NoteMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeMapper likeMapper;
    private final NoteMapper noteMapper;

    @Transactional
    public boolean toggleLike(Long userId, Long noteId) {
        LambdaQueryWrapper<Like> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Like::getUserId, userId).eq(Like::getNoteId, noteId);
        Like existingLike = likeMapper.selectOne(wrapper);

        Note note = noteMapper.selectById(noteId);
        if (note == null) {
            throw new RuntimeException("笔记不存在");
        }

        if (existingLike == null) {
            Like like = new Like();
            like.setUserId(userId);
            like.setNoteId(noteId);
            like.setCreatedAt(LocalDateTime.now());
            likeMapper.insert(like);

            note.setLikeCount(note.getLikeCount() + 1);
            noteMapper.updateById(note);

            return true;
        } else {
            likeMapper.deleteById(existingLike.getId());

            note.setLikeCount(note.getLikeCount() - 1);
            noteMapper.updateById(note);

            return false;
        }
    }

    public boolean isLiked(Long userId, Long noteId) {
        LambdaQueryWrapper<Like> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Like::getUserId, userId).eq(Like::getNoteId, noteId);
        return likeMapper.selectCount(wrapper) > 0;
    }
}