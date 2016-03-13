package model.dao;

import java.util.List;

import model.ProductBean;

public interface ProductDao_interface {
   ProductBean select(Integer id);
   List<ProductBean> select();
   ProductBean update(String name,Double price,java.util.Date make,Integer expire,Integer id);
   ProductBean insert(ProductBean bean);
   boolean delete(Integer id);
}