package app.exception;

import java.io.IOException;

public class FileLoadException extends RuntimeException{

    public FileLoadException(String message) {
        super(message);
    }
}
