package com.mctibers;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.simple.JSONObject; 

import com.mctibers.DAO.Connect;

@WebServlet("/login")
public class Login extends HttpServlet {
	
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id=request.getParameter("id");
        String password=request.getParameter("password");
       try {
		new Connect() {
       public void qry() throws SQLException, IOException {    	   
    	   String query="Select name,pass from user where phone='"+id+"'";
    	   statement=connection.createStatement();
    	   resultSet=statement.executeQuery(query);
    	   if(resultSet.next()) {
    	   String name=resultSet.getString(1);
			String pass=resultSet.getString(2);
			if(pass.equals(password))
				{
				/*HttpSession session =request.getSession();
				session.setAttribute("name", name);
				session.setAttribute("id",id);
				Cookie c1=new Cookie("JSESSIONID",session.getId());
				//Cookie c2=new Cookie("id",id);
				//Cookie c3=new Cookie("name",name);
				c1.setMaxAge(60*60*24*365);
				//c2.setMaxAge(60*60*24*365);
				//c3.setMaxAge(60*60*24*365);
				response.addCookie(c1);
				//response.addCookie(c2);
				//response.addCookie(c3);	*/
				response.getWriter().print(name);
				}
			else response.getWriter().print("Invalid Password");
			}
    	   else response.getWriter().print("Invalid User Name");
    	   close();
       }
		}.qry();
	} catch (ClassNotFoundException | SQLException e) {
		
		e.printStackTrace();
	}
       
	}

}
