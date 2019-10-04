package com.mctibers;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.net.*;
/**
 * Servlet implementation class test
 */
@WebServlet("/test")
public class test extends HttpServlet {
	public void init(ServletConfig config)
	          throws ServletException {
		System.out.println("hello");
	}
	public void service(HttpServletRequest req,HttpServletResponse res)throws IOException {
		
		//URL u=new URL("");
		//HttpURLConnection con=(HttpURLConnection)u.openConnection();
		//Cookie c1[]=req.getCookies();
		//System.out.println(c1[0].getName());
     //  HttpSession s=req.getSession();
     /*   if(s.getAttribute("a")==null) {
        	s.setAttribute("a","1");
        }
        else System.out.println(s.getAttribute("a"));
        System.out.println(s.getId());*/
		//Cookie c=new Cookie("JSESSIONID",s.getId());
	  // res.addCookie(c);
	 //System.out.println( req.isRequestedSessionIdFromURL());
     // c=new Cookie("a","b");
	System.out.println("Connected");
		//res.getWriter().print("hello");
	}

}
