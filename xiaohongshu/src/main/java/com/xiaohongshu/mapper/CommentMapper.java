package com.xiaohongshu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaohongshu.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
}