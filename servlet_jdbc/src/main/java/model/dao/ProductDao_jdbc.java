package model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import model.ProductBean;

public class ProductDao_jdbc implements ProductDao_interface{
    private DataSource dataSource;
	public ProductDao_jdbc(){
		try {
			Context ctxt = new InitialContext();
			dataSource = (DataSource)ctxt.lookup("java:comp/env/jdbc/SQLServerDB");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	private static final String SELECT_BY_ID = "SELECT * FROM product WHERE id = ?";
	@Override
	public ProductBean select(Integer id) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ProductBean result = null;
		
		try {
			conn = dataSource.getConnection();
			pstmt =  conn.prepareStatement(SELECT_BY_ID);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			if(rs.next()){
				result = new ProductBean();
				result.setId(rs.getInt("id"));
				result.setName(rs.getString("name"));
				result.setPrice(rs.getDouble("price"));
				result.setMake(rs.getDate("make"));
				result.setExpire(rs.getInt("expire"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			if(rs!=null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(pstmt!=null){
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(conn!=null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}
    
	private static final String SELECT_ALL = "SELECT * FROM product";
	@Override
	public List<ProductBean> select() {
		List<ProductBean> result = null;
		try(Connection conn = dataSource.getConnection();
			PreparedStatement stmt = conn.prepareStatement(SELECT_ALL);
			ResultSet rset = stmt.executeQuery();) {
			
			result = new ArrayList<ProductBean>();
			while(rset.next()) {
				ProductBean bean = new ProductBean();
				bean.setId(rset.getInt("id"));
				bean.setName(rset.getString("name"));
				bean.setPrice(rset.getDouble("price"));
				bean.setMake(rset.getDate("make"));
				bean.setExpire(rset.getInt("expire"));
				
				result.add(bean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	private static final String UPDATE = "UPDATE product SET name=?, price=?, make=?, expire=? WHERE id=?";
	@Override
	public ProductBean update(String name, Double price, Date make,Integer expire, Integer id) {
		ProductBean result = null;
		try(Connection conn = dataSource.getConnection();
			PreparedStatement stmt = conn.prepareStatement(UPDATE);) {
			stmt.setString(1, name);
			stmt.setDouble(2, price);
			if(make!=null) {
				long time = make.getTime();
				stmt.setDate(3, new java.sql.Date(time));
			} else {
				stmt.setDate(3, null);				
			}
			stmt.setInt(4, expire);
			stmt.setInt(5, id);
			int i = stmt.executeUpdate();
			if(i==1) {
				result = this.select(id);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
    private static final String INSERT = "INSERT INTO product (id,name,price,make,expire) VALUES(?,?,?,?,?)";
	@Override
	public ProductBean insert(ProductBean bean) {
		ProductBean result = null;
		try(Connection conn = dataSource.getConnection();
			PreparedStatement stmt = conn.prepareStatement(INSERT);) {
			if(bean!=null) {
				stmt.setInt(1, bean.getId());
				stmt.setString(2, bean.getName());
				stmt.setDouble(3, bean.getPrice());
				java.util.Date make = bean.getMake();
				if(make!=null) {
					long time = make.getTime();
					stmt.setDate(4, new java.sql.Date(time));
				} else {
					stmt.setDate(4, null);				
				}
				stmt.setInt(5, bean.getExpire());
				int i = stmt.executeUpdate();
				if(i==1) {
					result = bean;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
    private static final String DELETE = "DELETE FROM product WHERE id=?";
	@Override
	public boolean delete(Integer id) {
		try(Connection conn = dataSource.getConnection();
				PreparedStatement stmt = conn.prepareStatement(DELETE);) {			
				stmt.setInt(1, id);
				int i = stmt.executeUpdate();
				if(i==1) {
					return true;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return false;
	}
//  測試程式
//    public static void main(String[] args){
//    	ProductDao_jdbc dao = new ProductDao_jdbc();
//    	List<ProductBean> beans = dao.select();
//    	System.out.println(beans.size());
//    	
//    	ProductBean bean = dao.select(1);
//    	System.out.println(bean.getName());
//    	bean = dao.update("Coca Cola2", 122.0, bean.getMake(), 365, bean.getId());
//    	System.out.println(bean.getName());
//    	bean.setId(11);
//    	dao.insert(bean);
//    	dao.delete(11);
//    }
}
