package com.finalProject.Back.exception;

public class AccessDeniedException extends RuntimeException {
    public AccessDeniedException() {
        super("권한이 없습니다.");
    }
}
