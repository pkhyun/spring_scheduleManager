package com.sparta.schedulemanager.repository;

import com.sparta.schedulemanager.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
}
