package dev.jonathandlab.com.Coyn.server.exception.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoynAppExceptionResponse {
    private String date;
    private String message;
}
