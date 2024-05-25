package com.sparta.schedulemanager.dto;

import com.sparta.schedulemanager.entity.Schedule;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ScheduleResponseDto {
    private int id;
    private String title;
    private String contents;
    private String manager;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public ScheduleResponseDto(Schedule schedule) {
        this.id = schedule.getId();
        this.title = schedule.getTitle();
        this.contents = schedule.getContents();
        this.manager = schedule.getManager();
        this.createdAt = schedule.getCreatedAt();
        this.modifiedAt = schedule.getModifiedAt();
    }
}
