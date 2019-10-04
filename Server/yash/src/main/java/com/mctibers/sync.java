package com.mctibers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.mctibers.DAO.SyncDao;
/**
 * Servlet implementation class sync
 */
@WebServlet("/sync")
public class sync extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
  
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Long id=Long.parseLong(request.getParameter("id"));
		try {
			String s=new SyncDao().getNearPeople(id).toString();
			/*JSONArray a=(JSONArray)new JSONParser().parse(s); 
			Map obj= (Map) a.get(0);
			System.out.println(obj.get("id"));
			System.out.println(a.get(0));*/
			response.getWriter().print(s);
		} catch (ClassNotFoundException | SQLException e) {
			response.getWriter().print("Error"+e);
		} 
	}

}
