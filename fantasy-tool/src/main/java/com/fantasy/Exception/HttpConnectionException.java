package com.fantasy.Exception;

import java.net.ConnectException;

public class HttpConnectionException extends ConnectException {
    public HttpConnectionException(String message) {
        super(message);
    }
}
