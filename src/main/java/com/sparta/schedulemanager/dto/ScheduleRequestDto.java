package com.sparta.schedulemanager.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScheduleRequestDto {
    @NotBlank(message = "제목을 입력해주세요.")
    @Size(max = 200, message = "제목은 200자 이내로 입력해주세요.")
    private String title;

    private String contents;

    @NotBlank(message = "담당자를 입력해주세요.")
    @Email(message = "이메일 형식이어야 합니다.")
    private String manager;

}
