package com.xiaohongshu.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class CommentDTO {
    @NotNull(message = "笔记ID不能为空")
    private Long noteId;

    private Long parentId;

    @NotBlank(message = "评论内容不能为空")
    @Length(max = 500, message = "评论内容不能超过500字")
    private String content;
}