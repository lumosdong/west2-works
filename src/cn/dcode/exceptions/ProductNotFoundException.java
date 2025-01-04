package cn.dcode.exceptions;

public class ProductNotFoundException extends Exception{

    public ProductNotFoundException() {
        super("商品不存在");
    }

    public ProductNotFoundException(String message) {
        super(message);
    }


}
