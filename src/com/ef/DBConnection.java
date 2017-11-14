package com.ef;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author Kiran
 *
 */
public class DBConnection {
	public static Connection getConnection(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		
		Connection connection = null;
		try {
			connection = DriverManager
			.getConnection("jdbc:mysql://localhost:3306/assignment","root", "Apple@2017");

		} catch (SQLException e) {
			System.out.println("Exception Occured.");
			e.printStackTrace();
			return null;
		}
		return connection;
	}
}
