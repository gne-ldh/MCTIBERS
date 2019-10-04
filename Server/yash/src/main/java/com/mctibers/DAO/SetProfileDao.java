package com.mctibers.DAO;

import java.sql.SQLException;

public class SetProfileDao extends Connect {

	public SetProfileDao(long id,String profession,String professionDesc) throws SQLException, ClassNotFoundException {
		String query="select * from profile where id="+id;
		statement=connection.createStatement();
		resultSet=statement.executeQuery(query);
		if(resultSet.next()) {}
		else {String query1="insert into profile values ?,?,?";
		      preparedStatement=connection.prepareStatement(query1);
		      preparedStatement.setLong(1, id);
		      preparedStatement.setString(2,profession);
		      preparedStatement.setString(3,professionDesc);
		      preparedStatement.executeUpdate();
		}
	}

}
