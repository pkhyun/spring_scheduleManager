package com.sparta.schedulemanager.service;

import com.sparta.schedulemanager.dto.ScheduleRequestDto;
import com.sparta.schedulemanager.dto.ScheduleResponseDto;
import com.sparta.schedulemanager.entity.Schedule;
import com.sparta.schedulemanager.repository.ScheduleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public ScheduleResponseDto createSchedule(ScheduleRequestDto requestDto) { // 일정 생성
        Schedule schedule = new Schedule(requestDto);
        Schedule savedSchedule = scheduleRepository.save(schedule);
        return new ScheduleResponseDto(schedule);
    }

    public ScheduleResponseDto getSchedule(int id) { // 선택 일정 조회
        Schedule schedule = findScheduleById(id);
        return new ScheduleResponseDto(schedule);
    }

    public List<ScheduleResponseDto> getSchedules() { // 전체 일정 조회
        return scheduleRepository.findAll().stream().map(ScheduleResponseDto::new).toList();
    }

    @Transactional
    public ScheduleResponseDto updateSchedule(int id, ScheduleRequestDto requestDto) { //선택 일정 수정
        Schedule schedule = findScheduleById(id);
        if (checkPassword(schedule, requestDto)) {
            schedule.update(requestDto);
            return new ScheduleResponseDto(schedule); // 수정된 일정의 정보를 반환 받아 확인
        } else {
            throw new IllegalArgumentException("패스워드가 일치하지 않습니다.");
        }
    }

    public void deleteSchedule(int id, ScheduleRequestDto requestDto) { // 선택 일정 삭제
        Schedule schedule = findScheduleById(id);
        if (checkPassword(schedule, requestDto)) {
            scheduleRepository.delete(schedule);
        } else {
            throw new IllegalArgumentException("패스워드가 일치하지 않습니다.");
        }
    }

    private Schedule findScheduleById(int id) { // id 존재 확인 메서드
        return scheduleRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 스케줄을 찾을 수 없습니다."));
    }

    private boolean checkPassword(Schedule schedule, ScheduleRequestDto requestDto) { // password 일치 여부 확인
        return schedule.getPassword().equals(requestDto.getPassword());
    }

}
