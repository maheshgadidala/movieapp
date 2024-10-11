package org.movieflix.movieapp.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MovieNotFoundException.class)
    public ProblemDetail handleMovieNotFound(MovieNotFoundException exception) {

        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
    }
    @ExceptionHandler(MovieAlreadyExistsException.class)
    public ProblemDetail handleMovieAlreadyExists(MovieAlreadyExistsException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, exception.getMessage());
    }

    @ExceptionHandler(FileExistingException.class)
    public ProblemDetail handleFileExistingException(FileExistingException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
    }
    @ExceptionHandler(EmptyFileException.class)
    public ProblemDetail handleEmptyFileException(EmptyFileException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
    }
}
