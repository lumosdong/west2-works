package cn.dcode.exceptions;

public class InvalidProductPriceException extends Exception{

    public InvalidProductPriceException() {
        super("商品价格无效");
    }

    public InvalidProductPriceException(String message) {
        super(message);
    }
}
