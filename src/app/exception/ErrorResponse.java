package app.exception;

public class ErrorResponse extends RuntimeException {
    private String errorMessage;
    private Integer errorCode;
    private String path;


    public ErrorResponse(String errorMessage, Integer errorCode, String path) {
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
        this.path = path;
    }

    public ErrorResponse(String message, String errorMessage, Integer errorCode, String path) {
        super(message);
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
        this.path = path;
    }

    public ErrorResponse(String message, Throwable cause, String errorMessage, Integer errorCode, String path) {
        super(message, cause);
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
        this.path = path;
    }

    public ErrorResponse(Throwable cause, String errorMessage, Integer errorCode, String path) {
        super(cause);
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
        this.path = path;
    }

    public ErrorResponse(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, String errorMessage, Integer errorCode, String path) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
        this.path = path;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
