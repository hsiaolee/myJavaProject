package model;

import java.util.ArrayList;
import java.util.List;

import model.dao.ProductDao_jdbc;

public class ProductService {
	private ProductDao_jdbc productDao = new ProductDao_jdbc();

	public ProductBean update(ProductBean bean) {
		ProductBean result = null;
		if (bean != null) {
			result=productDao.update(bean.getName(), bean.getPrice(), bean.getMake(),
					bean.getExpire(), bean.getId());
		}
		return result;
	}

	public ProductBean insert(ProductBean bean) {
		ProductBean result = null;
		if (bean != null) {
			result = productDao.insert(bean);
		}
		return result;
	}

	public List<ProductBean> select(ProductBean bean) {
		List<ProductBean> result = null;
		if (bean != null && bean.getId()!= 0) {System.out.println("c");
			ProductBean temp = productDao.select(bean.getId());
			if (temp != null) {
				result = new ArrayList<ProductBean>();
				result.add(temp);
			}
		} else {
			result = productDao.select();
		}
		return result;
		
		
		
	}

	public boolean delete(ProductBean bean) {
		boolean result = false;
		if (bean != null) {
			result = productDao.delete(bean.getId());
		}
		return result;
	}
//	測試
//	public static void main(String[] args) {
//		ProductService service = new ProductService();
//		List<ProductBean> beans = service.select(null);
//		System.out.println(beans.size());
//	}
}
