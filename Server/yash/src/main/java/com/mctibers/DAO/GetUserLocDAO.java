package com.mctibers.DAO;

import java.sql.SQLException;
import java.util.ArrayList;

public class GetUserLocDAO extends Connect {

	public GetUserLocDAO() throws SQLException, ClassNotFoundException {
     
		
		
	}
public String get(Long id) throws SQLException {
	String query="select longitude,latitude from userLoc where id="+id;
	statement=connection.createStatement();
	resultSet=statement.executeQuery(query);
	ArrayList<ArrayList<Double>> list=new ArrayList<>();
	while(resultSet.next()) {
		ArrayList<Double> l1=new ArrayList<>();
	    l1.add(0,resultSet.getDouble(1));
	    l1.add(1,resultSet.getDouble(2));
	    list.add(l1);
	}
	if(list.isEmpty()==false)
	return list.toString();
	else return "";
}
}
