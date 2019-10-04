package com.mctibers;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mctibers.DAO.SetProfileDao;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
@WebServlet("/SetProfile")
public class SetProfile extends HttpServlet {
	
@Override
public void service(HttpServletRequest request,HttpServletResponse response) {
	Long id=Long.parseLong(request.getParameter("id"));
	String profession=request.getParameter("profession");
	String professionDesc=request.getParameter("professionDesc");
	try {
		SetProfileDao obj=new SetProfileDao(id,profession,professionDesc);
		response.getWriter().write(1);
		return;
	} catch (ClassNotFoundException | SQLException e) {
		
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	try {
		response.getWriter().write(0);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
}

}
