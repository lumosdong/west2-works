package cn.dcode.exceptions;

public class InsufficientStockException extends Exception{

    public InsufficientStockException() {
        super("库存不足");
    }

    public InsufficientStockException(String message) {
        super(message);
    }


}
