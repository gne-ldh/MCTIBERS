package com.mctibers;
import javax.servlet.annotation.WebServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;

import com.mctibers.DAO.FirstHelpRequestProcessorDAO;
import com.mctibers.DAO.SearchNearest;
@WebServlet("/fstReq")
public class FirstHelpRequestProcessor extends HttpServlet{

@Override
public void service(HttpServletRequest request,HttpServletResponse response) throws IOException {
	//HttpSession session=request.getSession();
	//Long id=Long.parseLong(session.getAttribute("id").toString());
	Long id=Long.parseLong(request.getParameter("id"));
	String name =request.getParameter("name");
	String message=request.getParameter("message");
	double longitude=Double.parseDouble(request.getParameter("longitude"));
	double latitude=Double.parseDouble(request.getParameter("latitude"));
	HttpSession session=request.getSession();
	session.setAttribute("id",id);
	try {
		System.out.println(1);
		FirstHelpRequestProcessorDAO fstHelp=new FirstHelpRequestProcessorDAO(id,name,longitude,latitude,message);
		//new SearchNearest( id, longitude, latitude);
		response.getWriter().print(1);
	} catch (ClassNotFoundException | SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		response.getWriter().print(0);
	}
}
}
