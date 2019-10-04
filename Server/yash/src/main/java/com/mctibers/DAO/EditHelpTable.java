package com.mctibers.DAO;

import java.sql.SQLException;

public class EditHelpTable extends Connect {

	public EditHelpTable() throws SQLException, ClassNotFoundException {
		super();
		statement=connection.createStatement();
	}
public void add(Long from,Long helper) throws SQLException{
	
	 String query1="insert ignore  into helpTable value("+from+","+helper+")";
		  statement.executeUpdate(query1);
}
public void removeSelf(Long id) throws SQLException {
	
	String query1=" delete from helpTable where frm="+id+" and helper ="+id;
	
	 statement.executeUpdate(query1);
	 connection.close();
}
}
