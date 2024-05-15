package com.sparta.schedulemanager.controller;

import com.sparta.schedulemanager.dto.ScheduleRequestDto;
import com.sparta.schedulemanager.dto.ScheduleResponseDto;
import com.sparta.schedulemanager.service.ScheduleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostMapping("/schedule")
    public ScheduleResponseDto createSchedule(@RequestBody ScheduleRequestDto requestDto) {
        return scheduleService.createSchedule(requestDto);
    }

    @GetMapping("/schedule")
    public ScheduleResponseDto getSchedule(@RequestParam int id) {
        return scheduleService.getSchedule(id);
    }

    @GetMapping("/schedules")
    public List<ScheduleResponseDto> getSchedules() {
        return scheduleService.getSchedules();
    }

    @PutMapping("/schedule/{id}")
    public ScheduleResponseDto updateSchedule(@PathVariable int id, @RequestBody ScheduleRequestDto requestDto) {
        return scheduleService.updateSchedule(id, requestDto);
    }

    @DeleteMapping("/schedule/{id}")
    public void deleteSchedule(@PathVariable int id, @RequestBody ScheduleRequestDto requestDto) {
        scheduleService.deleteSchedule(id, requestDto);
    }


}