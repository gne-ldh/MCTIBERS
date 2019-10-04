package com.mctibers.DAO;

import java.sql.SQLException;

public class FirstHelpRequestProcessorDAO extends Connect {

	public FirstHelpRequestProcessorDAO(long id,String name,Double longitude,Double latitude,String message) throws SQLException, ClassNotFoundException {
		super();
		//String query="create table req"+id+"(name varchar 50,longitude double(10,6),latitude double(10,6))";
		String query="delete from helpreq where id = "+id;
		statement=connection.createStatement();
		statement.executeUpdate(query);
		String query1="insert into helpreq values(?,?,?,?,?)";
		//statement=connection.createStatement();
		//statement.execute(query);
		preparedStatement=connection.prepareStatement(query1);
		preparedStatement.setLong(1, id);
		preparedStatement.setString(2, name);
		preparedStatement.setDouble(3,longitude);
		preparedStatement.setDouble(4, latitude);
		preparedStatement.setString(5, message);
		preparedStatement.executeUpdate();
		new SearchNearest(id,longitude,latitude);
		
	}

}
