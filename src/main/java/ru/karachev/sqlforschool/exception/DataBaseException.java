package ru.karachev.sqlforschool.exception;

public class DataBaseException extends RuntimeException {

    public DataBaseException() {
        super();
    }
    public DataBaseException(String message) {
        super(message);
    }
    public DataBaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataBaseException(Throwable cause){
        super(cause);
    }

}
