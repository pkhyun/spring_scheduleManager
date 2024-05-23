package com.sparta.schedulemanager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequestDto {
//    @NotBlank
//    private int scheduleId;

    @NotBlank(message = "내용을 입력해주세요.")
    private String contents;
}
