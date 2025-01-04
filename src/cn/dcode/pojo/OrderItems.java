package cn.dcode.pojo;

public class OrderItems {
    private Products product;
    private int quantity;


    public OrderItems() {
    }

    public OrderItems(Products product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    /**
     * 获取
     * @return product
     */
    public Products getProduct() {
        return product;
    }

    /**
     * 设置
     * @param product
     */
    public void setProduct(Products product) {
        this.product = product;
    }

    /**
     * 获取
     * @return quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * 设置
     * @param quantity
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String toString() {
        return "OrderItems{product = " + product + ", quantity = " + quantity + "}";
    }
}
