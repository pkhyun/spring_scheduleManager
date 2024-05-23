package com.sparta.schedulemanager.service;

import com.sparta.schedulemanager.dto.CommentRequestDto;
import com.sparta.schedulemanager.dto.CommentResponseDto;
import com.sparta.schedulemanager.entity.Comment;
import com.sparta.schedulemanager.entity.Schedule;
import com.sparta.schedulemanager.repository.CommentRepository;
import com.sparta.schedulemanager.repository.ScheduleRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final ScheduleRepository scheduleRepository;

    public CommentService(CommentRepository commentRepository, ScheduleRepository scheduleRepository) {
        this.commentRepository = commentRepository;
        this.scheduleRepository = scheduleRepository;
    }

    // 댓글 생성
    public CommentResponseDto createComment(int scheduleId, CommentRequestDto requestDto) {
        Schedule schedule = scheduleRepository.getReferenceById(scheduleId);
        Comment comment = new Comment(requestDto, schedule);
        Comment savedComment = commentRepository.save(comment);
        return new CommentResponseDto(comment);
    }

    // 댓글 수정
    @Transactional
    public CommentResponseDto updateComment(int id, CommentRequestDto requestDto) {
        Comment comment = findCommentById(id);
        comment.update(requestDto);
        return new CommentResponseDto(comment);
    }

    // 댓글 삭제
    public ResponseEntity<String> deleteComment(int id) {
        Comment comment = findCommentById(id);
        commentRepository.delete(comment);
        return ResponseEntity.ok("댓글이 삭제되었습니다.");
    }

    // id 존재 확인 메서드

    private Comment findCommentById(int id) {
        return commentRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 댓글을 찾을 수 없습니다."));
    }
}
