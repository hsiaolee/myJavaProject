package model;

import java.util.Arrays;

import model.dao.CustomerDao_jdbc;

public class CustomerService {
	CustomerDao_jdbc customerDao = new CustomerDao_jdbc();

	public CustomerBean login(String username, String password) {
		CustomerBean bean = customerDao.select(username);
		if (bean != null) {
			byte[] pass = bean.getPassword();
			byte[] temp = password.getBytes();
			if (Arrays.equals(pass, temp)) {
				return bean;
			}
		}
		return null;
	}

	public boolean changePassword(String username, String oldPassword, String newPassword) {
		CustomerBean bean = this.login(username, oldPassword);
		if(bean!=null){
			if(newPassword!=null&& newPassword.trim().length()!=0){
			return customerDao.update(newPassword.getBytes(), bean.getEmail(), bean.getBirth(), username);
			}
		}
		return false;
	}
//    測試
//	public static void main(String[] args) {
//		CustomerService service = new CustomerService();
//		CustomerBean bean = service.login("Alex", "A");
//		System.out.println(bean.getEmail());
//		service.changePassword("Ellen", "E", "EEE");
//	}
}
