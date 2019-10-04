package com.mctibers.DAO;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class SyncDao extends Connect {
	JSONArray jsnArray;
	public SyncDao() throws SQLException, ClassNotFoundException {
		super();
		
	}

	public JSONArray getNearPeople(long id) throws SQLException, ClassNotFoundException{
		 System.out.println("in");
		jsnArray=new JSONArray();
		String query ="select frm from helpTable where helper="+id;
	    statement=connection.createStatement();
	    resultSet=statement.executeQuery(query);
	    while(resultSet.next()) {
	     jsnArray.add(new findLoc().find(resultSet.getLong(1)));
	    //System.out.println(resultSet.getLong(1));
	    }
	    return jsnArray;
	}

}
