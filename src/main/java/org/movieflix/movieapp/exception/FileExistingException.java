package org.movieflix.movieapp.exception;

public class FileExistingException extends RuntimeException {
    public FileExistingException(String message) {
        super(message);
    }
}
