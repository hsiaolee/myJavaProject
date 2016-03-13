package model;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class ProductBean {
	private Integer id;
	private String name;
	private Double price;
	private java.util.Date make;
	private Integer expire;

	// 預設建構子
	public ProductBean() {

	}
    private SimpleDateFormat sDate = new SimpleDateFormat("yyyy-MM-dd");
	public ProductBean(String[] temp) throws IllegalArgumentException{
		if (temp != null) {
			if(temp.length == 5){
			this.id = Integer.parseInt(temp[0]);
			this.name = temp[1];
            this.price = Double.parseDouble(temp[2]);
            try {
				this.make = sDate.parse(temp[3].trim());
			} catch (ParseException e) {
				e.printStackTrace();
			}
            this.expire = Integer.parseInt(temp[4]);
			}else{
				throw new IllegalArgumentException("argument length error "+ temp.length);
			}
		}
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public java.util.Date getMake() {
		return make;
	}

	public void setMake(java.util.Date make) {
		this.make = make;
	}

	public Integer getExpire() {
		return expire;
	}

	public void setExpire(Integer expire) {
		this.expire = expire;
	}

}
