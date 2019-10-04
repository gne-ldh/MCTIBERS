package com.mctibers.DAO;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class findLoc extends Connect {
	HashMap hm;
	public findLoc() throws SQLException, ClassNotFoundException {
		super();
		 
	}
   public HashMap find(Long id) throws SQLException{
    	hm=new LinkedHashMap();
		 hm.put("id", id);
		 String query="select longitude,latitude,name,message from helpreq where id="+id;
		 statement=connection.createStatement();
		 resultSet=statement.executeQuery(query);
		 resultSet.next();
		 hm.put("longitude",resultSet.getDouble(1));
		 hm.put("latitude", resultSet.getDouble(2));
		// String query1="select name from helpreq where id="+id;
		 //resultSet=statement.executeQuery(query1);
		 //resultSet.next();
		 hm.put("name", resultSet.getString(3));
		 hm.put("message", resultSet.getString(4));
		 close();
		return hm;
    }
}
