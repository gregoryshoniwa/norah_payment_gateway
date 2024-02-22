package dev.grebles.norah.exception;

import dev.grebles.norah.controllers.responses.HttpResponse;
import dev.grebles.norah.exception.types.PasswordValidationException;
import dev.grebles.norah.utils.Utils;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@ControllerAdvice
public class MainExceptionHandler {

//    @ExceptionHandler(RuntimeException.class)
//    public ResponseEntity<HttpResponse> handleRuntimeException(RuntimeException ex) {
//        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR; // or any other appropriate status
//        String message = ex.getMessage();
//
//        // You can customize the response body as needed
//        HttpResponse response = HttpResponse.builder()
//                .timestamp(Utils.formattedTimestamp())
//                .message(message)
//                .status(status)
//                .statusCode(status.value())
//                .build();
//
//        return ResponseEntity.status(status).body(response);
//    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<HttpResponse> handleRuntimeException(RuntimeException ex, WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String message = ex.getMessage();
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        String requestMethod = ((ServletWebRequest) request).getRequest().getMethod();

        HttpResponse response = HttpResponse.builder()
                .timestamp(String.valueOf(LocalDateTime.now()))
                .message(message)
                .status(status)
                .statusCode(status.value())
                .path(path)
                .requestMethod(requestMethod)
                .build();

        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(PasswordValidationException.class)
    public ResponseEntity<HttpResponse> handlePasswordValidationException(PasswordValidationException ex, WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST; // You can choose the appropriate status code
        String message = ex.getMessage();
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        String requestMethod = ((ServletWebRequest) request).getRequest().getMethod();


        // You can customize the response body as needed
        HttpResponse response = HttpResponse.builder()
                .timestamp(Utils.formattedTimestamp())
                .message(message)
                .status(status)
                .path(path)
                .requestMethod(requestMethod)
                .statusCode(status.value())
                .build();

        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<HttpResponse> handleExpiredJwtException(ExpiredJwtException ex, WebRequest request) {
        HttpStatus status = HttpStatus.UNAUTHORIZED; // or any other appropriate status
        String message = ex.getMessage();
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        String requestMethod = ((ServletWebRequest) request).getRequest().getMethod();

        Map<String, Object> data = new HashMap<>();
        data.put("message", message);
        data.put("expiredAt", ex.getClaims().getExpiration());
        data.put("currentTime", LocalDateTime.now());

        HttpResponse response = HttpResponse.builder()
                .timestamp(String.valueOf(LocalDateTime.now()))
                .message("JWT token has expired")
                .status(status)
                .statusCode(status.value())
                .path(path)
                .requestMethod(requestMethod)
                .data(data)
                .build();

        return ResponseEntity.status(status).body(response);
    }
}
