package ru.masha.error;

import lombok.Value;

import java.util.List;

@Value
public class ErrorResponse {
    List<ApiError> errors;
}
