package cn.dcode.dao;

import cn.dcode.exceptions.InsufficientStockException;
import cn.dcode.exceptions.InvalidProductPriceException;
import cn.dcode.exceptions.ProductNotFoundException;
import cn.dcode.pojo.OrderItems;
import cn.dcode.pojo.Orders;
import cn.dcode.pojo.Products;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public interface OrdersDao {
    int createOrders(List<OrderItems> orderItems, LocalDateTime orderTime) throws SQLException, ProductNotFoundException,InvalidProductPriceException,InsufficientStockException;
    //查询单个订单
    Orders getOrderById(int orderId);
    //查询所有订单
    List<Orders> getAllOrders();
    //查询某个订单的所有商品
    List<OrderItems> getOrderItemsByOrderId(int orderId);
    //获取商品信息
    Products getProductById(int productId);
    //计算订单总价格
    //BigDecimal calculateTotalPrice(List<OrderItems> orderItems);
    //更新订单中的商品
    void updateOrderItems(int orderId,List<OrderItems> orderItems);
    //删除订单
    void deleteOrder(int orderId,int productId);
    //排序订单
    List<Orders> sortOrders(List<Orders> orders,String soryBy);
    //检查商品是否存在
    boolean isProductExist(int productid) throws ProductNotFoundException;
    //验证商品价格是否合法
    BigDecimal getProductPrice(int productId) throws InvalidProductPriceException;
    //验证商品库存
    boolean isStockSufficient(int productId,int quantity) throws InsufficientStockException;
    //重新计算商品总价
    BigDecimal recalculateOrderTotalPrice(int orderId, Connection conn);
}
