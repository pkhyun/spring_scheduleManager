package com.sparta.schedulemanager.controller;

import com.sparta.schedulemanager.dto.ScheduleRequestDto;
import com.sparta.schedulemanager.dto.ScheduleResponseDto;
import com.sparta.schedulemanager.security.UserDetailsImpl;
import com.sparta.schedulemanager.service.ScheduleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    // 일정 생성
    @PostMapping("/schedule")
    public ScheduleResponseDto createSchedule(@Valid @RequestBody ScheduleRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return scheduleService.createSchedule(requestDto, userDetails.getUser());
    }

    // 선택 일정 조회
    @GetMapping("/schedule/{id}")
    public ScheduleResponseDto getSchedule(@PathVariable int id) {
        return scheduleService.getSchedule(id);
    }

    // 전체 일정 조회
    @GetMapping("/schedules")
    public List<ScheduleResponseDto> getSchedules() {
        return scheduleService.getSchedules();
    }

    // 선택 일정 수정
    @PutMapping("/schedule/{id}")
    public ScheduleResponseDto updateSchedule(@PathVariable int id, @Valid @RequestBody ScheduleRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return scheduleService.updateSchedule(id, requestDto, userDetails.getUser());
    }

    // 선택 일정 삭제
    @DeleteMapping("/schedule/{id}")
    public ResponseEntity<String> deleteSchedule(@PathVariable int id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return scheduleService.deleteSchedule(id, userDetails.getUser());
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