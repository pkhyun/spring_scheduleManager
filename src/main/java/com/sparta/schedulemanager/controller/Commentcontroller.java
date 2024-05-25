package com.sparta.schedulemanager.controller;

import com.sparta.schedulemanager.dto.CommentRequestDto;
import com.sparta.schedulemanager.dto.CommentResponseDto;
import com.sparta.schedulemanager.entity.Comment;
import com.sparta.schedulemanager.security.UserDetailsImpl;
import com.sparta.schedulemanager.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class Commentcontroller {

    private final CommentService commentService;

    public Commentcontroller(CommentService commentService) {
        this.commentService = commentService;
    }

    // 댓글 생성
    @PostMapping("/comment/{scheduleId}")
    public CommentResponseDto createComment(@PathVariable int scheduleId, @Valid @RequestBody CommentRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.createComment(scheduleId, requestDto, userDetails.getUser());
    }

    // 선택 댓글 수정
    @PutMapping("/comment/{id}")
    public CommentResponseDto updateComment(@PathVariable int id, @Valid @RequestBody CommentRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.updateComment(id, requestDto, userDetails.getUser());
    }

    // 선택 댓글 삭제
    @DeleteMapping("/comment/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable int id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.deleteComment(id, userDetails.getUser());
    }

    // 선택한 일정의 댓글들 조회
    @GetMapping("/comment/{scheduleId}")
    public List<Comment> getComments(@PathVariable int scheduleId) {
        return commentService.getComments(scheduleId);
    }

    @ExceptionHandler // 에러 핸들링
    private ResponseEntity<String> handleException(IllegalArgumentException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler // 유효성 검사 에러 핸들링
    private ResponseEntity<String> handleException(MethodArgumentNotValidException e) {
        return new ResponseEntity<>(e.getBindingResult().getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST);
    }


}
