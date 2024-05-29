package com.sparta.schedulemanager.entity;

public enum UserRoleEnum {
    USER(Authority.USER),  // 사용자 권한
    ADMIN(Authority.ADMIN);  // 관리자 권한

    private final String authority;

    // UserRoleEnum 생성자
    UserRoleEnum(String authority) {
        this.authority = authority;
    }

    // 권한 값을 반환하는 메서드
    public String getAuthority() {
        return this.authority;
    }

    // 권한 값을 정의하는 내부 클래스
    public static class Authority {
        public static final String USER = "ROLE_USER";
        public static final String ADMIN = "ROLE_ADMIN";
    }
}
