package com.game.exception;

import com.game.bean.RestResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class MvcExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(MvcExceptionHandler.class);
    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public RestResult handle(Exception exception) {
        logger.error("Bad request, " + exception.getMessage());
        return new RestResult(HttpStatus.BAD_REQUEST.value(), exception.getMessage(),null);
    }

    @ExceptionHandler(value = Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestResult handle(Throwable exception) {
        logger.error("Api unhandled exception occur ", exception);
        return new RestResult(HttpStatus.INTERNAL_SERVER_ERROR.value(), ErrorCode.SERVER_ERROR.getDesc(),null);
    }



    @ExceptionHandler(value = ServiceException.class)
    public RestResult handle(ServiceException exception) {
        logger.error("Api invoke failed, " + exception.getMessage());
        return new RestResult(exception);
    }
}