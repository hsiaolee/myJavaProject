package model.dao;

import model.CustomerBean;

public interface CustomerDao_interface {
   
	CustomerBean select(String custid);
    boolean update(byte[] password,String email,java.util.Date birth,String custid);
}
