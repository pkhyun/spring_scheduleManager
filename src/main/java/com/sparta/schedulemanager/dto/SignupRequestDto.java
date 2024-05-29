package com.sparta.schedulemanager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto {
    @NotBlank(message = "별명을 입력해주세요")
    private String nickname;

    @NotBlank(message = "사용자 이름을 입력해주세요")
    @Pattern(regexp = "^[a-z0-9]{4,10}$", message = "사용자 이름은 4자 이상 10자 이하의 소문자 알파벳과 숫자만 사용 가능합니다.")
    private String username;

    @NotBlank(message = "패스워드를 입력해주세요.")
    @Pattern(regexp = "^[a-zA-Z\\d]{8,15}$", message = "비밀번호는 소문자, 대문자, 숫자로 이루어진 8자에서 15자 사이의 문자열이어야 합니다.")
    private String password;

    private boolean admin = false;
    private String adminToken = "";
}
