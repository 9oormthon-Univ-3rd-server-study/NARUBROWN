package me.na2ru2.narubrown.security.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException {
    private ExceptionCode exceptionCode;
}
