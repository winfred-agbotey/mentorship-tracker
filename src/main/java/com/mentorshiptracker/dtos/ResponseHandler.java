package com.mentorshiptracker.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseHandler {
    private HttpStatus httpStatus;
    private Object data;


    public static ResponseEntity<Object> successResponse(HttpStatus httpStatus, Object data) {
        return response("success", httpStatus, data);
    }

    private static ResponseEntity<Object> response(String statusMessage, HttpStatus httpStatus, Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", statusMessage);
        response.put("data", data);
        return new ResponseEntity<>(response, httpStatus);
    }
}
