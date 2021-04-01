package pl.lipinski.settlers_deckbuilder.util.exception.handler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApiErrorResponse {
    private Integer statusCode;
    private String message;
    private LocalDateTime errorTime;
}
