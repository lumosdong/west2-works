package cn.dcode.pojo;

import java.math.BigDecimal;

public class Products {
    private Integer product_id;
    private String product_name;
    private BigDecimal price;

    public Products() {
    }

    public Products(int product_id, String product_name, BigDecimal price) {
        this.product_id = product_id;
        this.product_name = product_name;
        this.price = price;
    }

    /**
     * 获取
     * @return product_id
     */
    public int getProduct_id() {
        return product_id;
    }

    /**
     * 设置
     * @param product_id
     */
    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    /**
     * 获取
     * @return product_name
     */
    public String getProduct_name() {
        return product_name;
    }

    /**
     * 设置
     * @param product_name
     */
    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    /**
     * 获取
     * @return price
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * 设置
     * @param price
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String toString() {
        return "Products{product_id = " + product_id + ", product_name = " + product_name + ", price = " + price + "}";
    }
}
