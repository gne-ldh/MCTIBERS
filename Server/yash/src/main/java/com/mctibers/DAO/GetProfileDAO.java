package com.mctibers.DAO;

import java.sql.SQLException;

public class GetProfileDAO extends Connect {
 public String Profession="";
public String ProfessionDescription="";
	public GetProfileDAO(long id) throws SQLException, ClassNotFoundException {
	String query="select Profession ,ProfessionDesc from profile where id="+id;
	statement=connection.createStatement();
	resultSet=statement.executeQuery(query);
    if(resultSet.next()) {
    	Profession=resultSet.getString(1);
    	ProfessionDescription=resultSet.getString(2);
    }
	}


}
