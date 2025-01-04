package cn.dcode.dao.impl;

import cn.dcode.dao.OrdersDao;
import cn.dcode.exceptions.InsufficientStockException;
import cn.dcode.exceptions.InvalidProductPriceException;
import cn.dcode.exceptions.ProductNotFoundException;
import cn.dcode.pojo.OrderItems;
import cn.dcode.pojo.Orders;
import cn.dcode.pojo.Products;
import cn.dcode.utils.JDBCUtil;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class OrdersDaoImpl implements OrdersDao {

    public static LocalDateTime getLocalTime(){
        return LocalDateTime.now();
    }

    //查询单个订单
    @Override
    public int createOrders(List<OrderItems> orderItems, LocalDateTime orderTime) throws SQLException ,ProductNotFoundException,InvalidProductPriceException,InsufficientStockException{
        Connection conn = JDBCUtil.getConnection();
        PreparedStatement pstat = null;
        ResultSet rs = null;
        int orderid = 0;
        String sql = "INSERT INTO orders (order_date,total_price) VALUES (?,?)";
        String insertOrderItem= "INSERT INTO order_items (orderid,productid,quantity) VALUES (?,?,?)";
        int flag = 0;
        double totalprice = 0;
        JDBCUtil.startTransaction();

        try {
            for(OrderItems item : orderItems){
                if(!isProductExist(item.getProduct().getProduct_id())) {
                    throw new ProductNotFoundException("商品" + item.getProduct().getProduct_id() + "不存在");
                }
                int id = item.getProduct().getProduct_id();
                BigDecimal price = getProductPrice(id);
                System.out.println(price);
                if(price == null || price.compareTo(BigDecimal.ZERO) <= 0){
                    throw new InvalidProductPriceException("商品价格无效");
                }
                if(!isStockSufficient(item.getProduct().getProduct_id(),item.getQuantity())){
                    throw new InsufficientStockException("库存不足");
                }
                totalprice += item.getQuantity() * price.doubleValue();
            }
            Timestamp timestamp = Timestamp.valueOf(orderTime);
            pstat = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstat.setTimestamp(1,timestamp);
            pstat.setBigDecimal(2, BigDecimal.valueOf(totalprice));
            flag = pstat.executeUpdate();
            rs = pstat.getGeneratedKeys();
            while(rs.next()){
                orderid = rs.getInt(1);
            }

            pstat = conn.prepareStatement(insertOrderItem);
            for(OrderItems item: orderItems){
                pstat.setInt(1,orderid);
                pstat.setInt(2,item.getProduct().getProduct_id());
                pstat.setInt(3,item.getQuantity());
                pstat.addBatch();
            }
            pstat.executeBatch();
            JDBCUtil.commitTransaction();

        } catch (SQLException throwables) {
            JDBCUtil.rollbackTransaction();
            throwables.printStackTrace();
        }finally {
            JDBCUtil.closeResources(pstat,rs);
            JDBCUtil.closeConnection();
        }
        return flag;
    }

    //查询所有订单
    @Override
    public Orders getOrderById(int orderId) {
        Connection conn = JDBCUtil.getConnection();
        PreparedStatement pstat = null;
        ResultSet rs = null;
        ArrayList<OrderItems> orderItems = new ArrayList<>();
        String query = "SELECT o.order_id,o.order_date,o.total_price,oi.productid,oi.quantity " +
                "FROM orders o " +
                "JOIN order_items oi ON o.order_id = oi.orderid " +
                "WHERE o.order_id = ?";

        try {
            pstat = conn.prepareStatement(query);
            pstat.setInt(1,orderId);
            rs = pstat.executeQuery();
            if(rs.next()) {
                Orders order = new Orders(orderId, rs.getTimestamp("order_date").toLocalDateTime(), rs.getBigDecimal("total_price"));
                do {
                    Products p = getProductById(rs.getInt("productid"));
                    OrderItems oi = new OrderItems(p, rs.getInt("quantity"));
                    orderItems.add(oi);
                } while (rs.next());
                return order;
            }
            return null;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        finally {
            JDBCUtil.closeResources(pstat,rs);
            JDBCUtil.closeConnection();
        }
        return null;
    }

    //查询某个订单的所有商品
    @Override
    public List<Orders> getAllOrders() {
        Connection conn = JDBCUtil.getConnection();
        PreparedStatement pstat = null;
        ResultSet rs = null;
        String query = "SELECT o.order_id,o.order_date,o.total_price FROM orders o ORDER BY o.order_date DESC";
        List<Orders> orders = new ArrayList<>();

        try {
            pstat = conn.prepareStatement(query);
            rs = pstat.executeQuery();
            while(rs.next()){
                int orderid = rs.getInt("order_id");
                LocalDateTime orderdate = rs.getTimestamp("order_date").toLocalDateTime();
                BigDecimal totalPrice = rs.getBigDecimal("total_price");
                orders.add(new Orders(orderid,orderdate,totalPrice));
            }
            return orders;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JDBCUtil.closeResources(pstat,rs);
            JDBCUtil.closeConnection();
        }
        return orders;
    }

    //按照订单编号获取商品信息
    @Override
    public List<OrderItems> getOrderItemsByOrderId(int orderId) {
        Connection conn = JDBCUtil.getConnection();
        PreparedStatement pstat = null;
        ResultSet rs = null;
        List<OrderItems> orderItems = new ArrayList<>();
        String sql = "SELECT oi.productid,oi.quantity FROM order_items oi WHERE oi.orderid = ?";

        try {
            pstat = conn.prepareStatement(sql);
            pstat.setInt(1,orderId);
            rs = pstat.executeQuery();
            while(rs.next()){
                Products p = getProductById(rs.getInt("productid"));
                OrderItems item = new OrderItems(p, rs.getInt("quantity"));
                orderItems.add(item);
            }
            return orderItems;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally{
            JDBCUtil.closeResources(pstat,rs);
            JDBCUtil.closeConnection();
        }
        return orderItems;
    }

    //按照商品编号获取商品
    @Override
    public Products getProductById(int productId) {
        Connection conn = JDBCUtil.getConnection();
        PreparedStatement pstat = null;
        ResultSet rs = null;
        String query = "SELECT product_id,product_name,price FROM products WHERE product_id = ?";

        try {
            pstat = conn.prepareStatement(query);
            pstat.setInt(1,productId);
            rs = pstat.executeQuery();
            while(rs.next()){
                String name = rs.getString("product_name");
                BigDecimal price = rs.getBigDecimal("price");
                return new Products(productId,name,price);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    //更新订单中的商品
    @Override
    public void updateOrderItems(int orderId, List<OrderItems> orderItems) {
        Connection conn = JDBCUtil.getConnection();
        PreparedStatement pstat = null;
        ResultSet rs = null;
        String deleteQuery = "DELETE FROM order_items WHERE orderid = ?";
        String insertQuery = "INSERT INTO order_items (orderid,productid,quantity) VALUES (?,?,?)";
        JDBCUtil.startTransaction();
        try {
            pstat = conn.prepareStatement(deleteQuery);
            pstat.setInt(1,orderId);
            pstat.executeUpdate();

            pstat = conn.prepareStatement(insertQuery);
            for(OrderItems item : orderItems){
                pstat.setInt(1,orderId);
                pstat.setInt(2,item.getProduct().getProduct_id());
                pstat.setInt(3,item.getQuantity());
                pstat.addBatch();
            }
            pstat.executeBatch();
            JDBCUtil.commitTransaction();
        } catch (SQLException throwables) {
            JDBCUtil.rollbackTransaction();
            throwables.printStackTrace();
        }finally {
            JDBCUtil.closeResources(pstat,rs);
            JDBCUtil.closeConnection();
        }
    }

    //删除订单
    @Override
    public void deleteOrder(int orderId,int produceId) {
        Connection conn = JDBCUtil.getConnection();
        PreparedStatement pstat = null;
        ResultSet rs = null;
        BigDecimal newPrice = new BigDecimal("0.0");
        String deleteSql = "DELETE FROM order_items WHERE orderid = ? AND productid = ?";
        String updateSql = "UPDATE orders SET total_price = ? WHERE order_id = ?";
        JDBCUtil.startTransaction();
        try {
            pstat = conn.prepareStatement(deleteSql);
            pstat.setInt(1,orderId);
            pstat.setInt(2,produceId);
            pstat.executeUpdate();
            newPrice = recalculateOrderTotalPrice(orderId,conn);
            pstat = conn.prepareStatement(updateSql);
            pstat.setBigDecimal(1,newPrice);
            pstat.setInt(2,orderId);
            pstat.executeUpdate();
            JDBCUtil.commitTransaction();
        } catch (SQLException throwables) {
            JDBCUtil.rollbackTransaction();
            throwables.printStackTrace();
        }finally {
            JDBCUtil.closeResources(pstat,rs);
            JDBCUtil.closeConnection();
        }
    }

    ////排序订单
    @Override
    public List<Orders> sortOrders(List<Orders> orders, String sortBy) {
        String sql = "SELECT order_id,order_date,total_price FROM orders ORDER BY total_price DESC,order_date ASC";
        List<Orders> list = new ArrayList<>();
        Connection conn = JDBCUtil.getConnection();
        ResultSet rs = null;
        PreparedStatement pstat = null;
        try {
            pstat = conn.prepareStatement(sql);
            rs = pstat.executeQuery();
            while(rs.next()){
                int orderId = rs.getInt("order_id");
                LocalDateTime orderDate = rs.getTimestamp("order_date").toLocalDateTime();
                BigDecimal totalPrice = rs.getBigDecimal("total_price");
                Orders o = new Orders(orderId, orderDate, totalPrice);
                list.add(o);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return list;
    }

    //商品不存在异常
    @Override
    public boolean isProductExist(int productId) throws ProductNotFoundException{
        Connection conn = JDBCUtil.getConnection();
        PreparedStatement pstat = null;
        ResultSet rs = null;
        String sql = "SELECT COUNT(*) FROM products WHERE product_id = ?";
        try {
            pstat = conn.prepareStatement(sql);
            pstat.setInt(1,productId);
            rs = pstat.executeQuery();
            if(rs.next() && rs.getInt(1) > 0){
                return true;
            }else{
                throw new ProductNotFoundException("商品" + productId + "不存在");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    //价格不合理异常
    @Override
    public BigDecimal getProductPrice(int productId) throws InvalidProductPriceException{
        Connection conn = JDBCUtil.getConnection();
        PreparedStatement pstat = null;
        ResultSet rs = null;
        String sql = "SELECT price FROM products WHERE product_id = ?";
        try {
            pstat = conn.prepareStatement(sql);
            pstat.setInt(1,productId);
            rs = pstat.executeQuery();
            if(rs.next() && rs.getInt(1) > 0){
                return rs.getBigDecimal("price");
            }else{
                throw new InvalidProductPriceException("商品价格不合理,无法获取价格");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    //订单要求数量大于库存异常
    @Override
    public boolean isStockSufficient(int productId, int number) throws InsufficientStockException {
        Connection conn = JDBCUtil.getConnection();
        PreparedStatement pstat = null;
        ResultSet rs = null;
        String sql = "SELECT quantity FROM order_items WHERE productid = ?";
        try {
            pstat = conn.prepareStatement(sql);
            pstat.setInt(1,productId);
            rs = pstat.executeQuery();
            if(rs.next()){
                int quantity = rs.getInt("quantity");
                return quantity >= number;
            }else{
                throw new InsufficientStockException("库存不足");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return false;
    }

    //重新计算总价格
    @Override
    public BigDecimal recalculateOrderTotalPrice(int orderId, Connection conn) {
        String selectSql = "SELECT SUM(quantity * price) FROM order_items oi JOIN products p ON oi.productid = p.product_id WHERE oi.orderid = ?";
        try {
            PreparedStatement pstat = conn.prepareStatement(selectSql);
            pstat.setInt(1,orderId);
            ResultSet rs = pstat.executeQuery();
            BigDecimal totalprice = new BigDecimal("0.0");
            if(rs.next()){
                totalprice = rs.getBigDecimal(1);
            }
            return totalprice;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
}
