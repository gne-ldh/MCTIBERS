package com.mctibers;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.WebServlet;
import com.mctibers.DAO.SignupDAO;
@WebServlet("/signup")
public class Signup extends HttpServlet{
   @Override
	public void service(HttpServletRequest request,HttpServletResponse response) throws IOException {
	   response.setContentType("text/plain");
		String name=request.getParameter("name");
		Long phone=Long.parseLong(request.getParameter("phone"));
		String password=request.getParameter("password");
		try {
			SignupDAO signup=new SignupDAO(phone,name,password);
			response.getWriter().print("User Added Successfully");
		}catch(SQLException |ClassNotFoundException e) {
			//response.getWriter().println(e);
		 response.getWriter().println("User Name Already Exists");	
		}
		
	}

}
