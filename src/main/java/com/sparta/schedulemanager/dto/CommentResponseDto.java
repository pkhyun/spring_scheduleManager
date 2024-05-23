package com.sparta.schedulemanager.dto;

import com.sparta.schedulemanager.entity.Comment;
import com.sparta.schedulemanager.entity.Schedule;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {
    private int commentId;
    private String contents;
//    private String userId;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public CommentResponseDto(Comment comment) {
        this.commentId = comment.getCommentId();
        this.contents = comment.getContents();
//        this.userId = comment.getUserId();
        this.createdAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
    }

}
