package com.sparta.schedulemanager.service;

import com.sparta.schedulemanager.dto.FileDto;
import com.sparta.schedulemanager.dto.ScheduleRequestDto;
import com.sparta.schedulemanager.dto.ScheduleResponseDto;
import com.sparta.schedulemanager.entity.File;
import com.sparta.schedulemanager.entity.Schedule;
import com.sparta.schedulemanager.entity.User;
import com.sparta.schedulemanager.repository.FileRepository;
import com.sparta.schedulemanager.repository.ScheduleRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final FileRepository fileRepository;

    public ScheduleService(ScheduleRepository scheduleRepository, FileRepository fileRepository) {
        this.scheduleRepository = scheduleRepository;
        this.fileRepository = fileRepository;
    }

    // 일정 생성
    public ScheduleResponseDto createSchedule(ScheduleRequestDto requestDto, User user, MultipartFile file) {
        Schedule schedule = new Schedule(requestDto, user);
        Schedule savedSchedule = scheduleRepository.save(schedule);

        if (file != null && !file.isEmpty()) {
            uploadFile(file, schedule);
        }

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
    public ScheduleResponseDto updateSchedule(int id, ScheduleRequestDto requestDto, User user, MultipartFile file) {
        Schedule schedule = findScheduleById(id);
        if (schedule.getUser().getId() == user.getId()) {

            if (file != null && !file.isEmpty()) {
                if (schedule.getFile() != null) {
                    updateFile(file, schedule.getFile());
                }
            }

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
        } else throw new IllegalArgumentException("본인이 작성한 일정만 삭제할 수 있습니다.");
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

    // 파일 업로드
    public FileDto uploadFile(MultipartFile multipartFile, Schedule schedule) {
        validateFile(multipartFile);

        String originalFileName = multipartFile.getOriginalFilename();
        String fileExtension = getFileExtension(multipartFile);
        int fileSize = (int) multipartFile.getSize();

        // 고유한 파일 이름 생성
        String uniqueFileName = UUID.randomUUID().toString() + "." + fileExtension;
        Path uploadDir = Paths.get("uploads");

        // 디렉토리 존재 여부 확인 및 생성
        if (Files.notExists(uploadDir)) {
            try {
                Files.createDirectories(uploadDir);
            } catch (IOException e) {
                throw new RuntimeException("업로드 디렉토리 생성 실패: " + uploadDir, e);
            }
        }

        Path filePath = uploadDir.resolve(uniqueFileName);

        try {
            // 파일 저장
            Files.copy(multipartFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패: " + originalFileName, e);
        }

        File file = new File(originalFileName, fileExtension, fileSize, filePath.toString(), schedule);
        File uploadedFile = fileRepository.save(file);
        return new FileDto(uploadedFile);
    }

    // 파일 업데이트
    private void updateFile(MultipartFile multipartFile, File existingFile) {
        validateFile(multipartFile);

        String originalFileName = multipartFile.getOriginalFilename();
        String fileExtension = getFileExtension(multipartFile);
        int fileSize = (int) multipartFile.getSize();

        // 기존 파일 정보를 업데이트
        existingFile.setFileName(originalFileName);
        existingFile.setFileExtension(fileExtension);
        existingFile.setFileSize(fileSize);

        // 파일 업로드 디렉토리
        Path uploadDir = Paths.get("uploads");

        // 디렉토리 존재 여부 확인 및 생성
        if (Files.notExists(uploadDir)) {
            try {
                Files.createDirectories(uploadDir);
            } catch (IOException e) {
                throw new RuntimeException("업로드 디렉토리 생성 실패: " + uploadDir, e);
            }
        }

        // 기존 파일의 경로를 유지하며 파일 업데이트
        Path filePath = Paths.get(existingFile.getFilePath());
        try {
            // 기존 파일을 업데이트
            Files.copy(multipartFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("파일 업데이트 실패: " + originalFileName, e);
        }

        // 파일 정보 업데이트
        existingFile.setFilePath(filePath.toString());
        fileRepository.save(existingFile);
    }


}
