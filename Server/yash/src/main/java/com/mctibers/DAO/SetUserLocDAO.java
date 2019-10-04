package com.mctibers.DAO;

import java.sql.SQLException;
import java.util.ArrayList;
public class SetUserLocDAO extends Connect {

	public SetUserLocDAO(Long id,ArrayList<ArrayList<Double>> coordinates) throws SQLException, ClassNotFoundException {
		super();
		String query="delete from userLoc";
		statement=connection.createStatement();
		statement.executeQuery(query);
		String query1 ="insert into userLoc values(?,?,?)";
		for(ArrayList<Double> l:coordinates) {
		preparedStatement=connection.prepareStatement(query1);
		preparedStatement.setLong(1, id);
		preparedStatement.setDouble(2,l.get(0));
		preparedStatement.setDouble(2, l.get(1));
		preparedStatement.executeUpdate();
		
		}
		close();
	}

}
