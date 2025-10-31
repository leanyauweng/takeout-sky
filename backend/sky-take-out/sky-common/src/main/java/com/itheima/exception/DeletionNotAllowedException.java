package com.itheima.exception;

public class DeletionNotAllowedException extends BaseException {
    public DeletionNotAllowedException() {
        super();
    }

    public DeletionNotAllowedException(String message) {
        super(message);
    }
}
