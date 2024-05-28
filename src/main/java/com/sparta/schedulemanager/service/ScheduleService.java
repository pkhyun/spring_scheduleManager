package com.sparta.schedulemanager.service;

import com.sparta.schedulemanager.dto.ScheduleRequestDto;
import com.sparta.schedulemanager.dto.ScheduleResponseDto;
import com.sparta.schedulemanager.entity.Schedule;
import com.sparta.schedulemanager.entity.User;
import com.sparta.schedulemanager.repository.ScheduleRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    // 일정 생성
    public ScheduleResponseDto createSchedule(ScheduleRequestDto requestDto, User user) {
        Schedule schedule = new Schedule(requestDto, user);
        Schedule savedSchedule = scheduleRepository.save(schedule);
        return new ScheduleResponseDto(schedule);
    }

    // 선택 일정 조회
    public ScheduleResponseDto getSchedule(int id) {
        Schedule schedule = findScheduleById(id);
        return new ScheduleResponseDto(schedule);
    }

    // 전체 일정 조회
    public List<ScheduleResponseDto> getSchedules() {
        return scheduleRepository.findAllByOrderByModifiedAtDesc().stream().map(ScheduleResponseDto::new).toList();
    }

    //선택 일정 수정
    @Transactional
    public ScheduleResponseDto updateSchedule(int id, ScheduleRequestDto requestDto, User user) {
        Schedule schedule = findScheduleById(id);
        if (schedule.getUser().getId() == user.getId()) {
            schedule.update(requestDto);
            return new ScheduleResponseDto(schedule);
        } else throw new IllegalArgumentException("본인이 작성한 일정만 수정할 수 있습니다.");

    }

    // 선택 일정 삭제
    public ResponseEntity<String> deleteSchedule(int id, User user) {
        Schedule schedule = findScheduleById(id);
        if (schedule.getUser().getId() == user.getId()) {
            scheduleRepository.delete(schedule);
            return ResponseEntity.ok("일정이 삭제되었습니다.");
        }else throw new IllegalArgumentException("본인이 작성한 일정만 삭제할 수 있습니다.");
    }

    private Schedule findScheduleById(int id) { // id 존재 확인 메서드
        return scheduleRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 스케줄을 찾을 수 없습니다."));
    }

    // 파일 형식 및 크기 제한 확인
    private void validateFile(MultipartFile file) {
        List<String> allowedExtensions = Arrays.asList("png", "jpg");
        String fileExtension = getFileExtension(file);
        if (!allowedExtensions.contains(fileExtension)) {
            throw new IllegalArgumentException("파일 형식은 PNG 또는 JPG만 허용됩니다.");
        }
        if (file.getSize() > 5 * 1024 * 1024) { // 5MB
            throw new IllegalArgumentException("파일 크기는 최대 5MB까지 허용됩니다.");
        }
    }

    // 파일 확장자 가져오기
    private String getFileExtension(MultipartFile file) {
        String originalFileName = file.getOriginalFilename();
        return originalFileName.substring(originalFileName.lastIndexOf(".") + 1).toLowerCase();
    }


}
