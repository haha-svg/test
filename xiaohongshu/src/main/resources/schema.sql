CREATE DATABASE IF NOT EXISTS xiaohongshu DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE xiaohongshu;

CREATE TABLE IF NOT EXISTS `user` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
    `phone` VARCHAR(20) COMMENT '手机号',
    `email` VARCHAR(100) COMMENT '邮箱',
    `password` VARCHAR(255) NOT NULL COMMENT '密码',
    `nickname` VARCHAR(50) NOT NULL COMMENT '昵称',
    `avatar` VARCHAR(255) COMMENT '头像',
    `created_at` DATETIME NOT NULL COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL COMMENT '更新时间',
    UNIQUE KEY `uk_phone` (`phone`),
    UNIQUE KEY `uk_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

CREATE TABLE IF NOT EXISTS `note` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '笔记ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `title` VARCHAR(200) NOT NULL COMMENT '标题',
    `content` TEXT COMMENT '内容',
    `images` TEXT COMMENT '图片URL，多个逗号分隔',
    `like_count` INT DEFAULT 0 COMMENT '点赞数',
    `comment_count` INT DEFAULT 0 COMMENT '评论数',
    `created_at` DATETIME NOT NULL COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL COMMENT '更新时间',
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='笔记表';

CREATE TABLE IF NOT EXISTS `like` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '点赞ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `note_id` BIGINT NOT NULL COMMENT '笔记ID',
    `created_at` DATETIME NOT NULL COMMENT '创建时间',
    UNIQUE KEY `uk_user_note` (`user_id`, `note_id`),
    KEY `idx_note_id` (`note_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='点赞表';

CREATE TABLE IF NOT EXISTS `comment` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '评论ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `note_id` BIGINT NOT NULL COMMENT '笔记ID',
    `parent_id` BIGINT COMMENT '父评论ID，null表示一级评论',
    `content` VARCHAR(500) NOT NULL COMMENT '评论内容',
    `created_at` DATETIME NOT NULL COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL COMMENT '更新时间',
    KEY `idx_note_id` (`note_id`),
    KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评论表';
