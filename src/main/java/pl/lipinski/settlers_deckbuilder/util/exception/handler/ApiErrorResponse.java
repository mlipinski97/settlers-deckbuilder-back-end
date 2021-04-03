package pl.lipinski.settlers_deckbuilder.util.exception.handler;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApiErrorResponse {
    private Integer statusCode;
    private String message;
    @JsonFormat(pattern="dd-MM-yyyy HH:mm:ss")
    private LocalDateTime errorTime;
    private HttpStatus errorStatus;

}
