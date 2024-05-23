package com.sparta.schedulemanager.entity;

import com.sparta.schedulemanager.dto.CommentRequestDto;
import com.sparta.schedulemanager.dto.CommentResponseDto;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "comment")
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int commentId;

    @Column(name = "contents", nullable = false)
    private String contents;

//    @Column(name = "user_id")
//    private String userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;

    @CreatedDate
    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt = LocalDateTime.now();

    @LastModifiedDate
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime modifiedAt = LocalDateTime.now();

    public Comment(CommentRequestDto requestDto, Schedule schedule){
        this.contents = requestDto.getContents();
        this.schedule = schedule;
    }



    public void update(CommentRequestDto requestDto){
        this.contents = requestDto.getContents();
        this.modifiedAt = LocalDateTime.now();
    }

}