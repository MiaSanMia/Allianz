package com.renren.ugc.comment.exception;

public class UGCCommentException extends RuntimeException {

    private int errorCode = -1;

    private static final long serialVersionUID = -5954077793935371938L;

    public UGCCommentException() {
        super();
    }

    public UGCCommentException(String message) {
        super(message);
    }

    public UGCCommentException(String message, Throwable cause) {
        super(message, cause);
    }

    public UGCCommentException(int errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public UGCCommentException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

}
