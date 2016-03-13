package model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import model.CustomerBean;

public class CustomerDao_jdbc implements CustomerDao_interface {
	
	private DataSource dataSource;
	public CustomerDao_jdbc(){
		try {
			Context ctxt = new InitialContext();
			dataSource = (DataSource)ctxt.lookup("java:comp/env/jdbc/SQLServerDB");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	private static final String SELECT_BY_CUSTID = "SELECT * FROM customer WHERE custid = ?";

	@Override
	public CustomerBean select(String custid) {
		CustomerBean result = null;

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rset = null;
		try {
			conn = dataSource.getConnection();
			stmt = conn.prepareStatement(SELECT_BY_CUSTID);

			stmt.setString(1, custid);
			rset = stmt.executeQuery();
			if (rset.next()) {
				result = new CustomerBean();
				result.setCustid(rset.getString("custid"));
				result.setPassword(rset.getBytes("password"));
				result.setEmail(rset.getString("email"));
				result.setBirth(rset.getDate("birth"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rset != null) {
				try {
					rset.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	private static final String UPDATE = "UPDATE customer SET password=?, email=?, birth=? WHERE custid=?";

	@Override
	public boolean update(byte[] password, String email, Date birth,
			String custid) {
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = dataSource.getConnection();
			stmt = conn.prepareStatement(UPDATE);

			stmt.setBytes(1, password);
			stmt.setString(2, email);
			if (birth != null) {
				long time = birth.getTime();
				stmt.setDate(3, new java.sql.Date(time));
			} else {
				stmt.setDate(3, null);
			}
			stmt.setString(4, custid);
			int i = stmt.executeUpdate();
			if (i == 1) {
				return true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}
//  測試程式
  public static void main(String[] args){
	  CustomerDao_jdbc dao = new CustomerDao_jdbc();
	  CustomerBean bean = dao.select("Ellen");
	  System.out.println(bean.getCustid());
	  System.out.println(bean.getEmail());
	  System.out.println(bean.getBirth());
	  java.util.Date temp;
	try {
		temp = new SimpleDateFormat("yyyy-MM-dd").parse("1976-09-18");
	
		System.out.println(dao.update("E".getBytes(), "ellen2@lab.com",temp , "Ellen"));
	} catch (ParseException e) {
		e.printStackTrace();
	}
	  
  }
}
