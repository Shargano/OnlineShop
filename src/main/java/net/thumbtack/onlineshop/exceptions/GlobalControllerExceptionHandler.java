package net.thumbtack.onlineshop.exceptions;

import net.thumbtack.onlineshop.dto.response.ErrorItem;
import net.thumbtack.onlineshop.dto.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalControllerExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ErrorResponse handleFieldValidation(MethodArgumentNotValidException ex) {
        List<ErrorItem> responses = new ArrayList<>();
        String stringErrorCode;
        String message;
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            stringErrorCode = fieldError.getField().toUpperCase();
            ValidationErrorCode errorCode = ValidationErrorCode.valueOf(stringErrorCode);
            if (fieldError.getDefaultMessage() == null)
                message = errorCode.getMessage();
            else
                message = fieldError.getDefaultMessage();
            responses.add(new ErrorItem(errorCode.toString(), errorCode.getField(), message));
        }
        return new ErrorResponse(responses);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(OnlineShopException.class)
    @ResponseBody
    public ErrorResponse handleOnlineShopException(OnlineShopException ex) {
        List<ErrorItem> responses = new ArrayList<>();
        OnlineShopErrorCode errorCode = ex.getErrorCode();
        responses.add(new ErrorItem(errorCode.toString(), errorCode.getField(), errorCode.getMessage()));
        return new ErrorResponse(responses);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseBody
    public ErrorResponse handleNotFoundException() {
        OnlineShopErrorCode errorCode = OnlineShopErrorCode.NOT_FOUND;
        ErrorItem item = new ErrorItem(errorCode.toString(), errorCode.getField(), errorCode.getMessage());
        List<ErrorItem> responses = new ArrayList<>();
        responses.add(item);
        return new ErrorResponse(responses);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MissingServletRequestParameterException.class, MissingServletRequestPartException.class, IllegalArgumentException.class})
    @ResponseBody
    public ErrorResponse handleBodyError() {
        OnlineShopErrorCode errorCode = OnlineShopErrorCode.BODY;
        ErrorItem item = new ErrorItem(errorCode.toString(), errorCode.getField(), errorCode.getMessage());
        List<ErrorItem> responses = new ArrayList<>();
        responses.add(item);
        return new ErrorResponse(responses);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public ErrorResponse handleMessageNotReadableException() {
        OnlineShopErrorCode errorCode = OnlineShopErrorCode.BODY;
        ErrorItem item = new ErrorItem(errorCode.toString(), errorCode.getField(), errorCode.getMessage());
        List<ErrorItem> responses = new ArrayList<>();
        responses.add(item);
        return new ErrorResponse(responses);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Throwable.class)
    @ResponseBody
    public ErrorResponse handleInternalError() {
        OnlineShopErrorCode errorCode = OnlineShopErrorCode.SERVER;
        ErrorItem item = new ErrorItem(errorCode.toString(), errorCode.getField(), errorCode.getMessage());
        List<ErrorItem> responses = new ArrayList<>();
        responses.add(item);
        return new ErrorResponse(responses);
    }

}