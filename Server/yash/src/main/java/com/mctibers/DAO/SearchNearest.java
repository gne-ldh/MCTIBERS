package com.mctibers.DAO;


import java.sql.SQLException;

public class SearchNearest extends Connect {

	public SearchNearest(long id,double longitude,double latitude) throws SQLException, ClassNotFoundException {
		super();
		System.out.println(longitude);
		String query="select  id from userLoc where ( 6371*acos(cos(radians("+latitude+"))*cos(radians(latitude))*cos(radians(longitude)-radians("+longitude+"))+sin(radians("+latitude+"))*sin(radians(latitude))))<=1";
		//String query="select  id from checktemp where ( 6371*acos(cos(radians("+latitude+"))*cos(radians(lat))*cos(radians(lng)-radians("+longitude+"))+sin(radians("+latitude+"))*sin(radians(lat))))<=5";
		//checktemp
		statement=connection.createStatement();
	   resultSet=statement.executeQuery(query);
	   EditHelpTable edit=new EditHelpTable();
	   
		  while( resultSet.next()) {
			 System.out.println(resultSet.getLong(1));
          edit.add(id,resultSet.getLong(1));	
		  }
		  
		 edit.removeSelf(id);	 
	}

}
