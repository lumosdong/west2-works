package cn.dcode.utils;

import cn.dcode.pojo.Products;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class JDBCUtil {
    private static final String url = "jdbc:mysql://localhost:3306/project1";
    private static final String username = "root";
    private static final String password = "123456";
    private static Connection conn = null;
    private static DataSource dataSource;
    private static ThreadLocal<Connection> threadLocal = new ThreadLocal<>();

    //使用db.properties进行配置
    static{
        Properties properties = new Properties();
        InputStream is = JDBCUtil.class.getClassLoader().getResourceAsStream("db.properties");
        try {
            properties.load(is);
            dataSource = DruidDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection(){
            Connection conn = threadLocal.get();
                if(conn == null) {
                    try {
                        conn = dataSource.getConnection();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                    threadLocal.set(conn);
                }
        return conn;
    }

    public static void closeResources(Statement stat, ResultSet rs){
        try {
            if (rs != null) rs.close();
            if (stat != null) stat.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void closeConnection(){
        Connection conn = threadLocal.get();
        if(conn != null){
            try {
                conn.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            finally{
                threadLocal.remove();
            }
        }
    }

//    @Test
//    public static void executeUpdateProduct(Products p){
//        getConnection();
//        String sql = "UPDATE products SET price = ? where product_id = ?";
//        try {
//            PreparedStatement pstat = conn.prepareStatement(sql);
//            pstat.setBigDecimal(1,p.getPrice());
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
//    }

    public static void startTransaction(){
        Connection conn = getConnection();
        try {
            conn.setAutoCommit(false);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void commitTransaction(){
        Connection conn = getConnection();
        try {
            conn.commit();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void rollbackTransaction(){
        Connection conn = getConnection();
        try {
            conn.rollback();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
