package com.mctibers;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.mctibers.DAO.GetProfileDAO;
import org.json.simple.JSONObject;
@WebServlet("/GetProfile")
public class GetProfile extends HttpServlet {
	private static final long serialVersionUID = 1L;
   
    public GetProfile() {
        super();
        // TODO Auto-generated constructor stub
    }

	
	
	
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	long id=Long.parseLong(request.getParameter("id"));
	String profession="";
	String professionDesc="";
	try {
			GetProfileDAO obj=new GetProfileDAO(id);
			profession=obj.Profession;
			professionDesc=obj.ProfessionDescription;
			JSONObject object=new JSONObject();
			object.put("profession",profession);
		    object.put("professionDesc",professionDesc);
		    response.getWriter().write(object.toString());
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
