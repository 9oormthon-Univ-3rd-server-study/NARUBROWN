package me.na2ru2.narubrown.security.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionCode {

    // User
    USER_JPA_ERROR(HttpStatus.BAD_REQUEST, "JPA 에러가 발생했습니다."),
    USER_NOT_FOUND_ERROR(HttpStatus.BAD_REQUEST, "찾는 유저가 없습니다."),

    // Security
    SECURITY_RESOURCE_DENIED(HttpStatus.BAD_REQUEST, "리소스에 접근할 권한이 없습니다."),
    SECURITY_NOT_AUTH_USER(HttpStatus.BAD_REQUEST, "인증되지 않은 사용자입니다."),

    // WebClient
    WEB_CLIENT_ERROR(HttpStatus.BAD_REQUEST, "Web Client에서 요청을 가져오는데 실패했습니다.")
    ;

    // Post
    // Comment
    // Category

    private final HttpStatus httpStatus;
    private final String message;
    ExceptionCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
