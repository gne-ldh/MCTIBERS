package com.mctibers.DAO;
import java.sql.Connection;
import java.sql.Statement;

import com.google.appengine.api.utils.SystemProperty;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.ResultSet;
public class Connect {
public Connection connection;
protected PreparedStatement preparedStatement;
protected Statement statement;
protected ResultSet resultSet;
private DriverManager driverManager;
/*
public static void main(String...connection ) throws ClassNotFoundException, SQLException {
	Connect obj=new Connect();
}*/
public Connect()throws SQLException,ClassNotFoundException {
	
if(SystemProperty.environment.value()==SystemProperty.Environment.Value.Production) {
	connection=driverManager.getConnection("jdbc:mysql://google/dataYASH?cloudSqlInstance=projectmctibers:asia-southeast1:datayash&socketFactory=com.google.cloud.sql.mysql.SocketFactory&user=root&password=20314&useSSL=false");
}	
else {
	Class.forName("com.mysql.jdbc.Driver");
	connection=driverManager.getConnection("jdbc:mysql://localhost:3306/dataYASH","root","20314");
	System.out.println("Connected");
}
}
public void close() 
throws SQLException{
connection.close();
}
}
