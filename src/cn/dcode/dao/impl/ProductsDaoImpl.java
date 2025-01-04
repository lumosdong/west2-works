package cn.dcode.dao.impl;

import cn.dcode.dao.ProductsDao;
import cn.dcode.pojo.Products;
import cn.dcode.utils.JDBCUtil;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ProductsDaoImpl implements ProductsDao {
    @Override
    public List<Products> selectAll() {
        List<Products> list= new ArrayList<>();
        Connection conn = JDBCUtil.getConnection();
        PreparedStatement pstat = null;
        ResultSet rs = null;
        String selectAllQuery = "select product_id,product_name,price from products";
        try {
            pstat = conn.prepareStatement(selectAllQuery);
            rs = pstat.executeQuery();
            while(rs.next()){
                int product_id = rs.getInt(1);
                String product_name = rs.getString(2);
                BigDecimal price = rs.getBigDecimal(3);
                Products temp_product = new Products(product_id, product_name, price);
                list.add(temp_product);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        finally {
            JDBCUtil.closeResources(pstat,rs);
            JDBCUtil.closeConnection();
        }
        return list;
    }

    @Override
    public Products selectProduct(Integer id){
        Connection conn = JDBCUtil.getConnection();
        PreparedStatement pstat = null;
        Products p = new Products();
        ResultSet rs = null;
        String selectSql = "select product_id,product_name,price from products where product_id = ?";
        try {
            pstat = conn.prepareStatement(selectSql);
            pstat.setInt(1,id);
            rs = pstat.executeQuery();
            while(rs.next()){
                int product_id = rs.getInt(1);
                String product_name = rs.getString(2);
                BigDecimal price = rs.getBigDecimal(3);
                p = new Products(product_id, product_name, price);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        finally {
            JDBCUtil.closeResources(pstat,rs);
            JDBCUtil.closeConnection();
        }
        return p;
    }

    @Override
    public int insertProduct(Products p) {
        Connection conn = JDBCUtil.getConnection();
        PreparedStatement pstat = null;
        ResultSet gk = null;
        int flag = 0;
        String insertSql = "INSERT INTO products (product_name,price) VALUES (?,?)";
        try {
            pstat = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
            pstat.setString(1,p.getProduct_name());
            pstat.setBigDecimal(2,p.getPrice());
            flag = pstat.executeUpdate();
            if(flag > 0){
                System.out.println("成功");
                gk = pstat.getGeneratedKeys();
                while(gk.next()){
                    int anInt = gk.getInt(1);
                    p.setProduct_id(anInt);
                }
                System.out.println(p);
            }
            else{
                System.out.println("失败");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JDBCUtil.closeResources(pstat,null);
            JDBCUtil.closeConnection();
        }
        return flag;
    }

    @Override
    public int deleteProduct(int id) {
        Connection conn = JDBCUtil.getConnection();
        PreparedStatement pstat = null;
        ResultSet rs = null;
        int flag = 0;
        String sql = "DELETE FROM products WHERE product_id = ?";

        try {
            pstat = conn.prepareStatement(sql);
            pstat.setInt(1,id);
            flag = pstat.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JDBCUtil.closeResources(pstat,null);
            JDBCUtil.closeConnection();
        }
        return flag;
    }

    @Override
    public int updateProduct(Products p) {
        Connection conn = JDBCUtil.getConnection();
        PreparedStatement pstat = null;
        int flag = 0;
        String sql = "UPDATE products SET product_name = ?,price = ? WHERE product_id = ?";
        try {
            pstat = conn.prepareStatement(sql);
            pstat.setString(1,p.getProduct_name());
            pstat.setBigDecimal(2,p.getPrice());
            pstat.setInt(3,p.getProduct_id());
            flag = pstat.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JDBCUtil.closeResources(pstat,null);
            JDBCUtil.closeConnection();
        }
        return flag;
    }
}
