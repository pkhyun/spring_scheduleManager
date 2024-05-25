package com.sparta.schedulemanager.service;

import com.sparta.schedulemanager.dto.CommentRequestDto;
import com.sparta.schedulemanager.dto.CommentResponseDto;
import com.sparta.schedulemanager.entity.Comment;
import com.sparta.schedulemanager.entity.Schedule;
import com.sparta.schedulemanager.entity.User;
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
    public CommentResponseDto createComment(int scheduleId, CommentRequestDto requestDto, User user) {
        Schedule schedule = findScheduleById(scheduleId);
        Comment comment = new Comment(requestDto, schedule, user);
        Comment savedComment = commentRepository.save(comment);
        return new CommentResponseDto(comment);
    }

    // 댓글 수정
    @Transactional
    public CommentResponseDto updateComment(int id, CommentRequestDto requestDto, User user) {
        Comment comment = findCommentById(id);
        if (comment.getUser().getId() == user.getId()) {
            comment.update(requestDto);
            return new CommentResponseDto(comment);
        } else throw new IllegalArgumentException("본인이 작성한 댓글만 수정할 수 있습니다.");
    }

    // 댓글 삭제
    public ResponseEntity<String> deleteComment(int id, User user) {
        Comment comment = findCommentById(id);
        if (comment.getUser().getId() == user.getId()) {
            commentRepository.delete(comment);
            return ResponseEntity.ok("댓글이 삭제되었습니다.");
        } else throw new IllegalArgumentException("본인이 작성한 댓글만 삭제할 수 있습니다.");
    }

    // id 존재 확인 메서드
    private Comment findCommentById(int id) {
        return commentRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 댓글을 찾을 수 없습니다."));
    }

    // 일정 id 존재 확인 메서드
    private Schedule findScheduleById(int id) {
        return scheduleRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 스케줄을 찾을 수 없습니다."));
    }
}
