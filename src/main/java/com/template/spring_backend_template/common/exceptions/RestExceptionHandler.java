package com.template.spring_backend_template.common.exceptions;

import com.auth0.jwt.exceptions.JWTCreationException;
import com.template.spring_backend_template.domain.RestErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        RestErrorResponse restErrorResponse = new RestErrorResponse(
                false,
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Um ou mais campos possuem valores inválidos ou estão ausentes",
                ex.getBindingResult()
                        .getFieldErrors()
                        .stream()
                        .map(error -> error.getField() + ": " + error.getDefaultMessage())
                        .toList()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(restErrorResponse);
    }

    @ExceptionHandler({AccessDeniedException.class, AuthenticationException.class})
    public ResponseEntity handleSecurityExceptions(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @ExceptionHandler(JWTCreationException.class)
    private ResponseEntity<RestErrorResponse> jwtCreationExceptionHandler(JWTCreationException ex){
        RestErrorResponse restErrorResponse = new RestErrorResponse(
                false, HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(restErrorResponse);
    }

    @ExceptionHandler(UserException.class)
    private ResponseEntity<RestErrorResponse> userExceptionHandler(UserException ex){
        RestErrorResponse restErrorResponse = new RestErrorResponse(
                false, HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase(), ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(restErrorResponse);
    }

    @ExceptionHandler(Exception.class)
    private ResponseEntity<RestErrorResponse> exceptionHandler(Exception ex){
        RestErrorResponse restErrorResponse = new RestErrorResponse(
                false, HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(restErrorResponse);
    }

    @ExceptionHandler(RuntimeException.class)
    private ResponseEntity<RestErrorResponse> runtimeExceptionHandler(RuntimeException ex){
        RestErrorResponse restErrorResponse = new RestErrorResponse(
                false, HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(restErrorResponse);
    }
}
