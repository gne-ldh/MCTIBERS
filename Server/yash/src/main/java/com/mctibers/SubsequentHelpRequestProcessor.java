package com.mctibers;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mctibers.DAO.SearchNearest;
import com.mctibers.DAO.SubsequentHelpRequestProcessorDAO;
@WebServlet("/subsequentReq")
public class SubsequentHelpRequestProcessor extends HttpServlet {
	
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session=request.getSession();
		Long id=Long.parseLong(request.getParameter("id"));
		double longitude=Double.parseDouble(request.getParameter("longitude"));
		double latitude=Double.parseDouble(request.getParameter("latitude"));
		try {
			SubsequentHelpRequestProcessorDAO obj=new SubsequentHelpRequestProcessorDAO(id,longitude,latitude);
			//new SearchNearest( id, longitude, latitude);
			response.getWriter().print("1");
			
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response.getWriter().print("0");
		}
	}

}
