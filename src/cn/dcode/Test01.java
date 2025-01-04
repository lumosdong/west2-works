package cn.dcode;

import cn.dcode.dao.ProductsDao;
import cn.dcode.dao.impl.OrdersDaoImpl;
import cn.dcode.dao.impl.ProductsDaoImpl;
import cn.dcode.exceptions.InsufficientStockException;
import cn.dcode.exceptions.InvalidProductPriceException;
import cn.dcode.exceptions.ProductNotFoundException;
import cn.dcode.pojo.OrderItems;
import cn.dcode.pojo.Orders;
import cn.dcode.pojo.Products;
import cn.dcode.utils.JDBCUtil;
import com.mysql.cj.jdbc.exceptions.PacketTooBigException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Test01 {
    @Test
    public void Connectiontest()  {
        Connection c1 = JDBCUtil.getConnection();
        Connection c2 = JDBCUtil.getConnection();
        Connection c3 = JDBCUtil.getConnection();
        System.out.println(c1);
        System.out.println(c2);
        System.out.println(c3);
        JDBCUtil.closeConnection();
    }
    @Test
    public void ProductsDaoTest1(){
        ProductsDao productsDao = new ProductsDaoImpl();
        List<Products> list = productsDao.selectAll();
        for(Products p: list){
            System.out.println(p);
        }
    }

    @Test
    public void ProductsDaoTest2(){
        ProductsDaoImpl productsDao = new ProductsDaoImpl();
        Products products = productsDao.selectProduct(4);
        System.out.println(products);
    }

    @Test
    public void ProductsDaoTest3(){
        String name = "tony";
        BigDecimal price = new BigDecimal("60.00");
        Products p = new Products(8, name, price);
        ProductsDaoImpl productsDao = new ProductsDaoImpl();
        productsDao.insertProduct(p);

    }

    @Test
    public void ProductsDaoTest4(){
        ProductsDaoImpl productsDao = new ProductsDaoImpl();
        int i = productsDao.deleteProduct(8);
        System.out.println(i);
    }

    @Test
    public void ProductDaoTest5(){
        ProductsDaoImpl productsDao = new ProductsDaoImpl();
        BigDecimal price = new BigDecimal("56.00");
        String name = "eeeemo";
        int id = 7;
        Products p = new Products(id,name,price);
        int i = productsDao.updateProduct(p);
        System.out.println(i);

    }

    @Test
    public void OrderDaoTest1() throws SQLException, ProductNotFoundException, InvalidProductPriceException, InsufficientStockException {
        OrdersDaoImpl ordersDao = new OrdersDaoImpl();
        List<OrderItems> list = new ArrayList<>();
        String name = "eeeemo";
        BigDecimal price = new BigDecimal("56");
        Products p = new Products(7, name, price);
        String name2 = "axxxx";
        BigDecimal price2 = new BigDecimal("87.00");
        Products p2 = new Products(4, name, price);

        list.add(new OrderItems(p,1));
        list.add(new OrderItems(p2,2));

        LocalDateTime localTime = OrdersDaoImpl.getLocalTime();
        ordersDao.createOrders(list,localTime);
    }

    @Test
    public void OrderDaoTest2(){
        OrdersDaoImpl ordersDao = new OrdersDaoImpl();
        Orders orderById = ordersDao.getOrderById(12);
        System.out.println(orderById);
    }

    @Test
    public void OrderDaoTest3(){
        OrdersDaoImpl ordersDao = new OrdersDaoImpl();
        List<Orders> allOrders = ordersDao.getAllOrders();
        System.out.println(allOrders);
    }

    @Test
    public void OrderDaoTest4(){
        OrdersDaoImpl ordersDao = new OrdersDaoImpl();
        List<OrderItems> list = ordersDao.getOrderItemsByOrderId(12);
        System.out.println(list);
    }

    @Test
    public void OrderDaoTest5(){
        OrdersDaoImpl ordersDao = new OrdersDaoImpl();
        Products p = ordersDao.getProductById(3);
        System.out.println(p);
    }

    @Test
    public void OrderDaoTest6(){
        OrdersDaoImpl ordersDao = new OrdersDaoImpl();
        List<OrderItems> orderItems = new ArrayList<>();
        String name = "axxxx";
        BigDecimal price = new BigDecimal("87");
        Products p = new Products(4, name, price);
        String name2 = "aa";
        BigDecimal price2 = new BigDecimal("3.00");
        Products p2 = new Products(7, name2, price2);
        OrderItems orderItems1 = new OrderItems(p,56);
        OrderItems orderItems2 = new OrderItems(p2,5);
        orderItems.add(orderItems1);
        orderItems.add(orderItems2);
        ordersDao.updateOrderItems(5,orderItems);

    }

    @Test
    public void OrderDaoTest7(){
        OrdersDaoImpl ordersDao = new OrdersDaoImpl();
        ordersDao.deleteOrder(5,4);
    }

    @Test
    public void OrderDaoTest8(){
        OrdersDaoImpl ordersDao = new OrdersDaoImpl();
        List<Orders> list = new ArrayList<>();

        int orderId = 10;
        LocalDateTime orderDate = LocalDateTime.of(2023, 10, 1, 10, 0); // 2023年10月1日 10:00
        BigDecimal totalPrice = new BigDecimal("200.00"); // 总价 200.00 元
        Orders order1 = new Orders(orderId, orderDate, totalPrice);

        int orderId2 = 11;
        LocalDateTime orderDate2 = LocalDateTime.of(2022, 12, 1, 13, 0); // 2023年10月1日 10:00
        BigDecimal totalPrice2 = new BigDecimal("1700.00"); // 总价 200.00 元

        Orders order2 = new Orders(orderId2, orderDate2, totalPrice2);
        list.add(order1);
        list.add(order2);


        List<Orders> res = ordersDao.sortOrders(list, "date");
        System.out.println(res);
    }
}
