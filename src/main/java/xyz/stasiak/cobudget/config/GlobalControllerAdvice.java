package xyz.stasiak.cobudget.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import xyz.stasiak.cobudget.common.UserIdNotFound;

@RestControllerAdvice
@Slf4j
class GlobalControllerAdvice {

    @ExceptionHandler(UserIdNotFound.class)
    private ResponseEntity<?> handleNoUserId(UserIdNotFound exception) {
        log.error("User id was not in jwt token for subject: {}", exception.getSubject());
        return ResponseEntity.badRequest().build();
    }

}
