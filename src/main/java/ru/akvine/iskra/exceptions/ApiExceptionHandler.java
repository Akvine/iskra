package ru.akvine.iskra.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.akvine.compozit.commons.dto.ErrorResponse;
import ru.akvine.iskra.constants.ApiErrorCodes;
import ru.akvine.iskra.exceptions.connection.ConnectionNotFoundException;
import ru.akvine.iskra.exceptions.plan.PlanNotFoundException;
import ru.akvine.iskra.exceptions.process.TableProcessNotFoundException;

@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ApiExceptionHandler.class);

    @ExceptionHandler({Exception.class})
    public ResponseEntity<?> handleException(Exception exception) {
        log.error("Error was occurred {}", exception);
        ErrorResponse errorResponse = new ErrorResponse(
                ApiErrorCodes.GENERAL_ERROR,
                exception.getMessage()
        );
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({PlanNotFoundException.class})
    public ResponseEntity<?> handlePlanNotFoundException(PlanNotFoundException exception) {
        ErrorResponse errorResponse = new ErrorResponse(
                ApiErrorCodes.Plan.PLAN_NOT_FOUND_ERROR,
                exception.getMessage()
        );
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({TableProcessNotFoundException.class})
    public ResponseEntity<?> handleTableProcessNotFoundException(TableProcessNotFoundException exception) {
        ErrorResponse errorResponse = new ErrorResponse(
                ApiErrorCodes.Process.PROCESS_NOT_FOUND_ERROR,
                exception.getMessage()
        );
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ConnectionNotFoundException.class})
    public ResponseEntity<?> handleConnectionNotFoundException(ConnectionNotFoundException exception) {
        ErrorResponse errorResponse = new ErrorResponse(
                ApiErrorCodes.Connection.CONNECTION_NOT_FOUND_ERROR,
                exception.getMessage()
        );
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
}
