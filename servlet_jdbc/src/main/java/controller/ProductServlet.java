package controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.ProductBean;
import model.ProductService;

public class ProductServlet extends HttpServlet{
	private SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd");
	private ProductService productService = new ProductService();
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		//接收HTML Form資料
		String temp1 = request.getParameter("id");
		String name = request.getParameter("name");
		String temp2 = request.getParameter("price");
		String temp3 = request.getParameter("make");
		String temp4 = request.getParameter("expire");
		String prodaction = request.getParameter("prodaction");
		//轉換HTML Form資料
		Map<String,String> error = new HashMap<String, String>();
		request.setAttribute("error", error);
		
		Integer id = 0;
		if(temp1!=null&&temp1.trim().length()!=0){
			try {
				id = Integer.parseInt(temp1.trim());
			} catch (NumberFormatException e) {
				e.printStackTrace();
				error.put("id", "ID has to be a Integer!");
			}
		}
		Double price = 0.0;
		if(temp2!=null&&temp2.trim().length()!=0){
			try {
				price = Double.parseDouble(temp2.trim());
			} catch (NumberFormatException e) {
				e.printStackTrace();
				error.put("price", "price has to be a Double!");
			}
		}
		java.util.Date make = null;
		if(temp3!=null&&temp3.trim().length()!=0){
			try {
				make =sFormat.parse(temp3);
			} catch (ParseException e) {
				e.printStackTrace();
				error.put("make", "Make必須是日期、格式為YYYY-MM-DD");
			}
		}
		Integer expire = 0;
		if(temp4!=null&&temp4.trim().length()!=0){
			try {
				expire = Integer.parseInt(temp4.trim());
			} catch (NumberFormatException e) {
				e.printStackTrace();
				error.put("expire", "expire has to be a Integer!");
			}
		}
		//驗證HTML Form資料
		if("Insert".equals(prodaction)|| "Update".equals(prodaction) || "Delete".equals(prodaction)){
			if(temp1==null ||temp1.trim().length()==0){
				error.put("id", "請輸入Id以便於執行"+prodaction);
			}
		}
		
		if(error!=null&&!error.isEmpty()){
			request.getRequestDispatcher("/pages/product.jsp").forward(request, response);
			return;
		}
		//呼叫Model
		ProductBean bean = new ProductBean();
		bean.setId(id);
		bean.setName(name);
		bean.setPrice(price);
		bean.setMake(make);
		bean.setExpire(expire);
		
		//根據Model執行結果顯示View
		if("Select".equals(prodaction)){
			List<ProductBean> result = productService.select(bean);
			request.setAttribute("select", result);
			request.getRequestDispatcher(
					"/pages/display.jsp").forward(request, response);
		}else if(prodaction!=null&&prodaction.equals("Insert")){
			ProductBean result = productService.insert(bean);
			if(result==null){
				error.put("action", "Insert fail");
			}else{
				request.setAttribute("insert", result);
			}
			request.getRequestDispatcher("/pages/product.jsp").forward(request, response);
		}else if(prodaction!=null && prodaction.equals("Update")) {
			ProductBean result = productService.update(bean);
			if(result==null) {
				error.put("action", "Update fail");
			} else {
				request.setAttribute("update", result);
			}
			request.getRequestDispatcher("/pages/product.jsp").forward(request, response);
		} else if(prodaction!=null && prodaction.equals("Delete")) {
			boolean result = productService.delete(bean);
			if(!result) {
				request.setAttribute("delete", 0);
			} else {
				request.setAttribute("delete", 1);
			}
			request.getRequestDispatcher("/pages/product.jsp").forward(request, response);
		} else  {
			error.put("action", "Unknown Action:"+prodaction);
			request.getRequestDispatcher("/pages/product.jsp").forward(request, response);
		}
		
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doGet(request, response);
	}
  
}
