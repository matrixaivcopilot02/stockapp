package com.learn.stockapp.exceptions;

public class StockNotFoundException extends Exception {
    
    public StockNotFoundException() {
        super();
    }
    
    public StockNotFoundException(String message) {
        super(message);
    }
    
    public StockNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public StockNotFoundException(Throwable cause) {
        super(cause);
    }
}
