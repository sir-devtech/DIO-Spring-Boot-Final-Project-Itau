package dio.budgeting.infrastructure.http;

import dio.budgeting.domain.GastoInvalido;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(GastoInvalido.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> gastoInvalido(GastoInvalido erro) {
        return Map.of("erro", erro.getMessage());
    }
}
