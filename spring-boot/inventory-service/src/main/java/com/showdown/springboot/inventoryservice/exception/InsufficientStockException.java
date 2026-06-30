package com.showdown.springboot.inventoryservice.exception;

public class InsufficientStockException extends RuntimeException {

    public InsufficientStockException(String sku, int requested, int available) {
        super("Insufficient stock for SKU '" + sku + "': requested=" + requested
                + ", available=" + available);
    }

    public InsufficientStockException(String message) {
        super(message);
    }
}
