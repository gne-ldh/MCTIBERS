package com.mctibers;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mctibers.DAO.GetUserLocDAO;

/**
 * Servlet implementation class GetUserLoc
 */
@WebServlet("/GetUserLoc")
public class GetUserLoc extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Long id=Long.parseLong(request.getParameter("id"));
		String locDetails="";
		GetUserLocDAO usrLoc;
		try {
			usrLoc = new GetUserLocDAO();
			locDetails=usrLoc.get(id);
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response.getWriter().print(locDetails);
	}


}
