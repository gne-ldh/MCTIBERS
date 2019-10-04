package com.mctibers;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.mctibers.DAO.SetUserLocDAO;

import java.util.ArrayList;

@WebServlet("/SetUserLoc")
public class SetUserLoc extends HttpServlet {
	private static final long serialVersionUID = 1L;
	PrintWriter wr;
   
   public void service(HttpServletRequest req,HttpServletResponse res) {
	   Long id=Long.parseLong(req.getParameter("id"));
	   String locDetails=req.getParameter("locDetails");
	   try {
		   wr=res.getWriter();
		//JSONObject jo=(JSONObject)new JSONParser().parse(locDetails);
		//ArrayList locList=(ArrayList)jo.get("locDetails");
		  ArrayList locList=(ArrayList)new JSONParser().parse(locDetails);
		new SetUserLocDAO(id,locList);
		 wr.print(1);
	} catch (ParseException e) {
		 wr.print(0);
		e.printStackTrace();
	} catch (ClassNotFoundException e) {
		 wr.print(0);
		e.printStackTrace();
	} catch (SQLException e) {
		e.printStackTrace();
	} catch (IOException e) {
	
		 wr.print(0);
		e.printStackTrace();
	}
	   
   }
	
	
}
