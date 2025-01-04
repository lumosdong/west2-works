package cn.dcode.dao;

import cn.dcode.pojo.Products;

import java.util.List;

public interface ProductsDao {
    //查询所有商品
    List<Products> selectAll();

    //查询单个商品
    Products selectProduct(Integer product_id);

    //添加商品
    int insertProduct(Products p);

    //删除商品
    int deleteProduct(int id);

    //更新商品
    int updateProduct(Products p);
}
