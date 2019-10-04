package com.mctibers.DAO;

import java.sql.SQLException;

public class SubsequentHelpRequestProcessorDAO extends Connect {

	public SubsequentHelpRequestProcessorDAO(long id,Double longitude,Double latitude) throws SQLException, ClassNotFoundException {
	super();
	String query1="update helpreq set longitude=?,latitude=? where id=?";
	preparedStatement=connection.prepareStatement(query1);
	preparedStatement.setDouble(1,longitude);
	preparedStatement.setDouble(2, latitude);
	preparedStatement.setLong(3, id);
	int i=preparedStatement.executeUpdate();
	new SearchNearest(id,longitude,latitude);
	}

}
