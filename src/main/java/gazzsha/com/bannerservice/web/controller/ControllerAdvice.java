package gazzsha.com.bannerservice.web.controller;

import gazzsha.com.bannerservice.domain.exception.BannerAlreadyExistWithParams;
import gazzsha.com.bannerservice.domain.exception.ErrorMessage;
import gazzsha.com.bannerservice.domain.exception.ForbiddenError;
import gazzsha.com.bannerservice.domain.exception.NotFoundException;
import gazzsha.com.bannerservice.domain.exception.UnauthorizedError;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ControllerAdvice extends ResponseEntityExceptionHandler {


    @ExceptionHandler({BannerAlreadyExistWithParams.class, NotFoundException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleResourceNotFound(final RuntimeException exception) {
        return new ErrorMessage(exception.getMessage());
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleMissingHeaderToken(final MissingRequestHeaderException exception) {
        return new ErrorMessage(exception.getMessage());
    }

    @ExceptionHandler(ForbiddenError.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorMessage handleForbidden(final RuntimeException exception) {
        return new ErrorMessage("Access denied");
    }

    @ExceptionHandler(UnauthorizedError.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorMessage handleUnauthorized(final RuntimeException exception) {
        return new ErrorMessage("Unauthorized");
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return ResponseEntity.badRequest().body(new ErrorMessage(ex.getMessage()));
    }
}
