package ajax;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.ProductBean;
import model.ProductService;

@WebServlet(
		urlPatterns={"/pages/product.view"}
)
public class ProductIdServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String method = request.getMethod();
		System.out.println("method="+method+", "+request.getRequestURI());
		
		String action = request.getParameter("action");
		System.out.println("action="+action);
		
		try {
			Thread.sleep(750);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		if(action!=null && action.equals("textText")) {
			this.textText(request, response);
		} else if("textXml".equals(action)) {
			this.textXml(request, response);
		} else if("textJson".equals(action)) {
			this.textJson(request, response);
		} else if(method.equals("POST")) {
			this.jsonJson(request, response);
		} else {
			throw new ServletException("使用GET呼叫必須輸入action參數值：textText, textXml, textJson");
		}
	}
	private void textText(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/plain; charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		StringBuilder output = new StringBuilder();
		String temp = request.getParameter("id");
		int id = 0;
		if(temp==null || temp.trim().length()==0) {
			output.append("ID是必要欄位");
		} else {
			try {
				id = Integer.parseInt(temp);
			} catch (NumberFormatException e) {
				output.append("ID必須是數字");
				e.printStackTrace();
			}
		}
		if(output!=null && output.length()!=0) {
			out.print(output);
			out.close();
			return;
		}
		ProductBean bean = new ProductBean();
		bean.setId(id);
		ProductService service = new ProductService();
		List<ProductBean> result = service.select(bean);
		if(result==null || result.isEmpty()) {
			output.append("ID不存在");
		} else {
			output.append("ID存在:");
			ProductBean data = result.get(0);
			output.append(data.getId()+",");
			output.append(data.getName()+",");
			output.append(data.getPrice()+",");
			output.append(data.getMake()+",");
			output.append(data.getExpire());
		}
		out.print(output);
		out.close();
		return;
	}
	private void textXml(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/xml; charset=UTF-8");
		PrintWriter out = response.getWriter();

		StringBuilder output = new StringBuilder();
		output.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
		output.append("<result>");
		
		String temp = request.getParameter("id");
		int id = 0;
		boolean flag = false;
		if(temp==null || temp.trim().length()==0) {
			output.append("<text>ID是必要欄位</text>");
			flag = true;
		} else {
			try {
				id = Integer.parseInt(temp);
			} catch (NumberFormatException e) {
				output.append("<text>ID必需是數字</text>");
				flag = true;
				e.printStackTrace();
			}
		}
		if(flag==true) {
			output.append("<hasMoreData>false</hasMoreData>");
			output.append("</result>");
			
			out.print(output);
			out.close();
			return;
		}
		
		ProductBean bean = new ProductBean();
		bean.setId(id);
		ProductService service = new ProductService();
		List<ProductBean> result = service.select(bean);

		if(result==null || result.isEmpty()) {
			output.append("<text>ID不存在</text>");
			output.append("<hasMoreData>false</hasMoreData>");
		} else {
			output.append("<text>ID存在</text>");
			output.append("<hasMoreData>true</hasMoreData>");
			
			ProductBean data = result.get(0);
			output.append("<id>"+data.getId()+"</id>");
			output.append("<name>"+data.getName()+"</name>");
			output.append("<price>"+data.getPrice()+"</price>");
			output.append("<make>"+data.getMake()+"</make>");
			output.append("<expire>"+data.getExpire()+"</expire>");
		}
		output.append("</result>");
		
		out.print(output);
		out.close();
		return;
	}
	private void textJson(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String temp = request.getParameter("id");
		this.writeJsonOutput(temp, response);
	}
	private void jsonJson(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		JsonReader reader = Json.createReader(request.getInputStream());
		JsonObject jsonInput = reader.readObject();
		String action = jsonInput.getString("action");
		if(action==null || !action.equals("jsonJson")) {
			throw new ServletException("使用POST呼叫必須輸入action參數值：textText, textXml, textJson, jsonJson");
		}

		String temp = jsonInput.getString("id");
		this.writeJsonOutput(temp, response);

	}
	private void writeJsonOutput(String temp, HttpServletResponse response) throws IOException{
		response.setContentType("text/plain; charset=UTF-8");
		PrintWriter out = response.getWriter();
		StringBuilder output = new StringBuilder();
		int id = 0;
		if(temp==null || temp.trim().length()==0) {
			output.append("ID是必要欄位");
		} else {
			try {
				id = Integer.parseInt(temp);
			} catch (NumberFormatException e) {
				output.append("ID必需是數字");
				e.printStackTrace();
			}
		}
	
		if(output!=null && output.length()!=0) {
			JsonObject obj = Json.createObjectBuilder()
				.add("text", output.toString())
				.add("hasMoreData", false).build();
			JsonArray jsonArray = 
					Json.createArrayBuilder().add(obj).build();
			out.write(jsonArray.toString());
			out.close();
			return;
		}
		ProductBean bean = new ProductBean();
		bean.setId(id);
		ProductService service = new ProductService();
		List<ProductBean> result = service.select(bean);
		
		JsonArrayBuilder builder = Json.createArrayBuilder();
		if(result==null || result.isEmpty()) {
			JsonObject obj = Json.createObjectBuilder()
					.add("text", "ID不存在")
					.add("hasMoreData", false).build();
			builder.add(obj);
		} else {
			JsonObject obj1 = Json.createObjectBuilder()
					.add("text", "ID存在")
					.add("hasMoreData", true).build();
			builder.add(obj1);

			ProductBean data = result.get(0);
			JsonObject obj2 = Json.createObjectBuilder()
					.add("id", data.getId())
					.add("name", data.getName())
					.add("price", data.getPrice())
					.add("make", data.getMake().toString())
					.add("expire", data.getExpire()).build();
			builder.add(obj2);
		}
		out.write(builder.build().toString());

		out.close();
		return;
	}
}