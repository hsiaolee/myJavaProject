package ajax;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

@WebServlet(
		urlPatterns={"/pages/photo.view"},
		initParams={
				@WebInitParam(name="defaultFile", value="/img/x.png")
		}
)
public class PhotoServlet extends HttpServlet {
	private File defaultPhoto;
	@Override
	public void init() throws ServletException {
		String defaultFile = this.getInitParameter("defaultFile");
		ServletContext application = this.getServletContext();
		String path = application.getRealPath(defaultFile);
		defaultPhoto = new File(path);
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doGet(req, resp);
	}
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String photoid = request.getParameter("photoid");

		InputStream photoInputStream = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rset = null;
		try {
			Context ctx = new InitialContext();
			DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/xxx");
			conn = ds.getConnection();
			stmt = conn.prepareStatement(
					"select * from detail where photoid = ?");
			stmt.setString(1, photoid);
			rset = stmt.executeQuery();
			
			if(rset.next()) {
				photoInputStream = rset.getBinaryStream("photo");
			} else {
				photoInputStream = new FileInputStream(defaultPhoto);
			}
			
			response.setContentType("image/png");
			OutputStream out = response.getOutputStream();
			byte[] buffer = new byte[4*1024];
			int len = photoInputStream.read(buffer);
			while(len!=-1) {
				out.write(buffer, 0, len);
				len = photoInputStream.read(buffer);
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(photoInputStream!=null) {
				photoInputStream.close();
			}
			if (rset!=null) {
				try {
					rset.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt!=null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn!=null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
