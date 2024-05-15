package com.sparta.schedulemanager.dto;

import lombok.Getter;

import java.util.Date;

@Getter
public class ScheduleRequestDto {
    private String title;
    private String contents;
    private String manager;
    private String password;
}
