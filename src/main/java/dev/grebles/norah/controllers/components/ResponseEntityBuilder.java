package dev.grebles.norah.controllers.components;

import dev.grebles.norah.controllers.responses.HttpResponse;
import dev.grebles.norah.utils.Utils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.Map;

@Component
public class ResponseEntityBuilder {

    public ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message, Object data, String path, String requestMethod) {
        return ResponseEntity.status(httpStatus).body(
                HttpResponse.builder()
                        .timestamp(Utils.formattedTimestamp())
                        .data(data)
                        .message(message)
                        .status(httpStatus)
                        .statusCode(httpStatus.value())
                        .requestMethod(requestMethod)
                        .path(path)
                        .build()
        );
    }

    public ResponseEntity<HttpResponse> created(Object data, String path, String requestMethod) {
        return ResponseEntity.created(URI.create("")).body(
                HttpResponse.builder()
                        .timestamp(Utils.formattedTimestamp())
                        .data(data)
                        .message("Successfully created new resource")
                        .status(HttpStatus.CREATED)
                        .statusCode(HttpStatus.CREATED.value())
                        .requestMethod(requestMethod)
                        .path(path)
                        .build()
        );
    }



    public ResponseEntity<HttpResponse> ok(String message, Object data,
                                          String path, String requestMethod) {
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timestamp(Utils.formattedTimestamp())
                        .data(data)
                        .message(message)
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .requestMethod(requestMethod)
                        .path(path)
                        .build()
        );
    }
}

