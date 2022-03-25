package com.gitapp.exceptions;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorMessage {
    private int status;
    private String message;
}
