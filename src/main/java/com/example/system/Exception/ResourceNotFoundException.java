package com.example.system.Exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);  // 傳遞錯誤訊息到父類 RuntimeException
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);  // 可選：傳遞錯誤訊息和原因
    }
}

