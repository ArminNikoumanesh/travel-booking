package com.armin.utility.config.common;

import com.armin.utility.object.ErrorResult;
import com.armin.utility.object.SystemError;
import com.armin.utility.object.SystemException;
import com.armin.utility.object.SystemRuntimeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.util.unit.DataSize;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Controller Exception Handler
 * <p>
 * This class handles all exception throws from controllers
 *
 * @author : Armin.Nik
 * @project : shared
 * @date : 09.06.24
 */

@RestControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @Value("${spring.servlet.multipart.max-file-size}")
    private DataSize maxUploadSize;

    @ExceptionHandler(SystemException.class)
    public List<ErrorResult> handleSystemException(SystemException exception, HttpServletResponse response) {
        response.setStatus(exception.getError().getValue());
        if (exception.getErrorResults() != null) {
            return exception.getErrorResults();
        }
        return Collections.singletonList(new ErrorResult(exception));
    }

    @ExceptionHandler(SystemRuntimeException.class)
    public List<ErrorResult> handleSystemRuntimeException(SystemRuntimeException exception, HttpServletResponse response) {
        response.setStatus(exception.getError().getValue());
        return Collections.singletonList(new ErrorResult(exception.getError(), 0, exception.getArgument()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public List<ErrorResult> constraintViolationException(ConstraintViolationException exception, HttpServletResponse response) {
        List<ErrorResult> validationList = new ArrayList<>();

        for (ConstraintViolation violation : exception.getConstraintViolations()) {
            String argument = "";
            if (violation.getPropertyPath() != null) {
                for (Path.Node node : violation.getPropertyPath()) argument = node.getName();
                argument += " ";
            }
            argument += violation.getMessage();
            validationList.add(new ErrorResult(SystemError.VALIDATION_EXCEPTION, 5050, argument));
        }

        response.setStatus(SystemError.VALIDATION_EXCEPTION.getValue());
        return validationList;
    }

    @ExceptionHandler(MultipartException.class)
    public ErrorResult handleMultipartException(MultipartException ex, HttpServletResponse response) {
        response.setStatus(SystemError.VALIDATION_EXCEPTION.getValue());
        return new ErrorResult(SystemError.VALIDATION_EXCEPTION, 1010, maxUploadSize.toMegabytes());
    }

    @ExceptionHandler(Exception.class)
    public ErrorResult handleUnHandledException(Exception exception, HttpServletResponse response) {
        exception.printStackTrace();
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        return new ErrorResult(SystemError.SERVER_ERROR, 6060, exception.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public List<ErrorResult> dataIntegrityViolationException(DataIntegrityViolationException violationException, HttpServletResponse response) {
        List<ErrorResult> validationList = new ArrayList<>();
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        Map<String, Object> normalizedExceptions = DataIntegrityViolationExceptionNormalizer.normalize(violationException);
        StringBuilder argument = new StringBuilder();
        for (Map.Entry<String, Object> exception : normalizedExceptions.entrySet()) {
            argument.append(exception.getKey()).append(" :").append(exception.getValue()).append(", ");
        }
        validationList.add(new ErrorResult(SystemError.VALIDATION_EXCEPTION, 2020, argument.toString()));
        return validationList;
    }

    @ExceptionHandler(InvalidDataAccessApiUsageException.class)
    public List<ErrorResult> InvalidDataAccessApiUsageException(InvalidDataAccessApiUsageException violationException, HttpServletResponse response) {
        List<ErrorResult> validationList = new ArrayList<>();
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        Map<String, Object> normalizedExceptions = InvalidDataAccessApiUsageExceptionNormalizer.normalize(violationException);
        StringBuilder argument = new StringBuilder();
        for (Map.Entry<String, Object> exception : normalizedExceptions.entrySet()) {
            argument.append(exception.getKey()).append(" :").append(exception.getValue());
        }
        validationList.add(new ErrorResult(SystemError.VALIDATION_EXCEPTION, 3030, argument));
        return validationList;
    }
}
