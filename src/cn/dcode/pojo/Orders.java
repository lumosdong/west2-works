package cn.dcode.pojo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Orders {
    //订单编号
    private int order_id;

    //下单时间
    private LocalDateTime order_date;

    //订单价格
    private BigDecimal total_price;

    public Orders() {
    }

    public Orders(int order_id, LocalDateTime order_date, BigDecimal total_price) {
        this.order_id = order_id;
        this.order_date = order_date;
        this.total_price = total_price;
    }

    /**
     * 获取
     * @return order_id
     */
    public int getOrder_id() {
        return order_id;
    }

    /**
     * 设置
     * @param order_id
     */
    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    /**
     * 获取
     * @return order_date
     */
    public LocalDateTime getOrder_date() {
        return order_date;
    }

    /**
     * 设置
     * @param order_date
     */
    public void setOrder_date(LocalDateTime order_date) {
        this.order_date = order_date;
    }

    /**
     * 获取
     * @return total_price
     */
    public BigDecimal getTotal_price() {
        return total_price;
    }

    /**
     * 设置
     * @param total_price
     */
    public void setTotal_price(BigDecimal total_price) {
        this.total_price = total_price;
    }

    public String toString() {
        return "Orders{order_id = " + order_id + ", order_date = " + order_date + ", total_price = " + total_price + "}";
    }
}
