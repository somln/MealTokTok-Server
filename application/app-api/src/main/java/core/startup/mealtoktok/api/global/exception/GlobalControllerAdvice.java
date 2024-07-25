package core.startup.mealtoktok.api.global.exception;

import core.startup.mealtoktok.common.dto.Response;
import core.startup.mealtoktok.common.exception.CustomException;
import core.startup.mealtoktok.common.exception.DomainException;
import core.startup.mealtoktok.common.exception.InfraException;
import core.startup.mealtoktok.common.exception.WebException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(value = WebException.class)
    public ResponseEntity<?> customError(WebException e, HttpServletRequest request) {
        return ResponseEntity
                .status(e.getStatus())
                .body(Response.error(e.getErrorCode().getErrorReason(), request.getRequestURI(), e.getMessage()));
    }

    @ExceptionHandler(value = DomainException.class)
    public ResponseEntity<?> domainError(DomainException e, HttpServletRequest request) {
        return ResponseEntity
                .status(e.getStatus())
                .body(Response.error(e.getErrorCode().getErrorReason(), request.getRequestURI(), e.getMessage()));
    }

    @ExceptionHandler(value = InfraException.class)
    public ResponseEntity<?> infraError(InfraException e, HttpServletRequest request) {
        return ResponseEntity
                .status(e.getStatus())
                .body(Response.error(e.getErrorCode().getErrorReason(), request.getRequestURI(), e.getMessage()));
    }
}