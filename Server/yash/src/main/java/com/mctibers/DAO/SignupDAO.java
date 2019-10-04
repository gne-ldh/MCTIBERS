package com.mctibers.DAO;

import java.sql.SQLException;

public class SignupDAO extends Connect {

	public SignupDAO(Long id,String name,String password) throws SQLException, ClassNotFoundException {
		super();
		String query="insert into user value(?,?,?)";
		
			preparedStatement=connection.prepareStatement(query);
			preparedStatement.setLong(1, id);
			preparedStatement.setString(2, name);
			preparedStatement.setString(3, password);
			preparedStatement.executeUpdate();
		    close();
	}


}
