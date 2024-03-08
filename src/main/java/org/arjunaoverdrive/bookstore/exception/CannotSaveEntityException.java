package org.arjunaoverdrive.bookstore.exception;

public class CannotSaveEntityException extends RuntimeException{
    public CannotSaveEntityException(String message) {
        super(message);
    }
}
