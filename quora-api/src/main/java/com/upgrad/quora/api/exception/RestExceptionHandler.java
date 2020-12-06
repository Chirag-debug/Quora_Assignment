package com.upgrad.quora.api.exception;

import com.upgrad.quora.api.model.ErrorResponse;
import com.upgrad.quora.service.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(SignUpRestrictedException.class)
    public ResponseEntity<ErrorResponse> signuprestricted(SignUpRestrictedException exc, WebRequest request)
    {
//        Handle the exception
        return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(exc.getCode()).message(exc.getErrorMessage()),
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AuthenticationFailedException.class)
    public ResponseEntity<ErrorResponse> authenticationfailed(AuthenticationFailedException exc,WebRequest request)
    {
        //        Handle the exception
        return new ResponseEntity<ErrorResponse>(
                new ErrorResponse().code(exc.getCode()).message(exc.getErrorMessage()),
                HttpStatus.UNAUTHORIZED);

    }

    @ExceptionHandler(SignOutRestrictedException.class)
    public  ResponseEntity<ErrorResponse> signoutfailed(SignOutRestrictedException exc, WebRequest request)
    {
        //        Handle the exception
        return new ResponseEntity<ErrorResponse>(
                new ErrorResponse().code(exc.getCode()).message(exc.getErrorMessage()), HttpStatus.UNAUTHORIZED
        );

    }

    @ExceptionHandler(AuthorizationFailedException.class)
    public ResponseEntity<ErrorResponse> authorizationfailed(AuthorizationFailedException exc, WebRequest request)
    {
//      Handle the exception for authorizationfailedexception
        return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(exc.getCode()).message(exc.getErrorMessage()),
                HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> userNotFound(UserNotFoundException exc, WebRequest request)
    {
//      Handle the exception for usernotfoundexception
        return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(exc.getCode()).message(exc.getErrorMessage()),
                HttpStatus.NOT_FOUND); // in proman we use NOT_FOUND (Response 404), but its not given in quora response definition
    }

    @ExceptionHandler(InvalidQuestionException.class)
    public ResponseEntity<ErrorResponse> invalidQuestion(InvalidQuestionException exc, WebRequest request)
    {
//      Handle the exception for invalidquestionexception
        return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(exc.getCode()).message(exc.getErrorMessage()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AnswerNotFoundException.class)
    public ResponseEntity<ErrorResponse> answerNotFound(AnswerNotFoundException exc, WebRequest request)
    {
//      Handle the exception for invalidquestionexception
        return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(exc.getCode()).message(exc.getErrorMessage()),
                HttpStatus.NOT_FOUND);
    }

}
