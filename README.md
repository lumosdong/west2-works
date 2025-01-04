# 简单订单管理系统 
地址：https://github.com/lumosdong/west2-works/tree/master

## 1.JDBCUTILS

包括获取连接，释放资源，事务管理等操作

## 2.DAO层

### （1）ProductsDao接口

对商品进行增删改查操作

通过ProductsDaoImpl类进行实现操作

### （2）OrdersDao接口

对订单进行增删改查，排序等操作

通过OrdersDaoImpl类进行实现操作

## 3.exceptions

自定义异常类，包括库存不足异常，商品价格异常，商品不存在异常

## 4.pojo

包括OrderItems类，Orders类，Products类，对应mysql的三张表



## 补充：

1.使用了ThreadLocal来获取连接，避免了获取连接时的多次重复获取

2.使用了Durid连接池，避免频繁创建和销毁，优化效率

