package com.campus.exception;

public class CampusException extends Exception {

	private static final long serialVersionUID = -3602087607957629032L;
	
	private int errorCode = -1;

    public CampusException() {
        super();
    }

    public CampusException(String message) {
        super(message);
    }

    public CampusException(String message, Throwable cause) {
        super(message, cause);
    }

    public CampusException(int errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public CampusException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

}
