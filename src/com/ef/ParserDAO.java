package com.ef;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Kiran
 *
 */
public class ParserDAO {

	public boolean saveLogInDB(List<LogObject> logObjectList) {
		Connection con = DBConnection.getConnection();

		if(con != null){
			//System.out.println("Connected to DB succesfully.");
		}else{
			System.out.println("Exception while connecting to DB");
			return false;
		}
		PreparedStatement prepStmnt = null;
		try {
		for(LogObject logObject : logObjectList){
			int hashKey = generateHashCode(logObject);
			
				String preQueryStatement = "SELECT * FROM LOG_DETAILS WHERE HASHKEY = ?;";
				prepStmnt = con.prepareStatement(preQueryStatement);
				prepStmnt.setInt(1,hashKey);
				ResultSet rs = prepStmnt.executeQuery();
				if (!rs.next()){
					String insertStatement = "INSERT INTO LOG_DETAILS VALUES (?, ?, ?, ?, ?, ?)";
					PreparedStatement ps = con.prepareStatement(insertStatement);
					ps.setString(1, String.valueOf(hashKey));
					ps.setString(2, logObject.getStartDate());
					ps.setString(3, logObject.getIpAddress());
					ps.setString(4, logObject.getRequest());
					ps.setString(5, logObject.getStatus());
					ps.setString(6, logObject.getUserAgent());
				    ps.execute();
				}
			} 
		}catch (SQLException e) {
        	System.out.println("SQL Exception occured.");
			e.printStackTrace();
			return false;
		}finally {
            if (prepStmnt != null) {
                try {
                	prepStmnt.close();
                } 
                catch (SQLException e){
                	System.out.println("SQL Exception occured.");
                	return false;
                }
            }
            if (con!= null) {
                try {
                	con.close();
                } catch (SQLException sqlEx) {
                	System.out.println("SQL Exception occured.");
                	return false;
                }
            }
		}
		return true;
	}

	public List<ResponseObject> queryLogs(String startDate, String duration, int threshold) {
		Connection con = DBConnection.getConnection();

		if(con != null){
			//System.out.println("Connected to DB succesfully.");			
		}else{
			System.out.println("Could not establish connection successfully.");
		}
		PreparedStatement prepStmnt = null;
		String preQueryStatement = null;
		List<ResponseObject> responseObjectList = new ArrayList<>();

		if(duration.equals("hourly")){
			preQueryStatement = "select ipAddress, ipCount from (select ipAddress, count(1) as ipCount from LOG_DETAILS where startDate between ? and date_add(?, INTERVAL 1 hour) group by ipAddress) tempLogTable where ipCount >= ?;";	
		}else if(duration.equals("daily")){
			preQueryStatement = "select ipAddress, ipCount from (select ipAddress, count(1) as ipCount from LOG_DETAILS where startDate between ? and date_add(?, INTERVAL 24 hour) group by ipAddress) tempLogTable where ipCount >= ?;";
		}
		
		try {
			prepStmnt = con.prepareStatement(preQueryStatement);
			prepStmnt.setString(1,startDate);
			prepStmnt.setString(2,startDate);
			prepStmnt.setInt(3,threshold);
			ResultSet rs = prepStmnt.executeQuery();
			while(rs.next()){
				ResponseObject responseObject = new ResponseObject();
				responseObject.setIpAddress(rs.getString(1));
				responseObject.setIpCount(rs.getInt(2));
				responseObjectList.add(responseObject);
			}
		} catch (SQLException e) {
			System.out.println("Exception while fetching records from DB." + " ErrorCode : " + e.getErrorCode() + " ErrorMessage : "+ e.getMessage() );
			e.printStackTrace();
		}finally {
            if (prepStmnt != null) {
                try {
                	prepStmnt.close();
                } 
                catch (SQLException e){
                	System.out.println("SQL Exception occured." + " ErrorCode : " + e.getErrorCode() + " ErrorMessage : "+ e.getMessage());
                }
            }
            if (con!= null) {
                try {
                	con.close();
                } catch (SQLException sqlEx) {
                	System.out.println("SQL Exception occured." + " ErrorCode : " + sqlEx.getErrorCode() + " ErrorMessage : "+ sqlEx.getMessage());
                }
            }
		}
		return responseObjectList;
	}
	
	
	private int generateHashCode(LogObject logObject) {
		String str = logObject.getIpAddress() + logObject.getRequest() + logObject.getStatus() +logObject.getStartDate() + logObject.getUserAgent();
		return str.hashCode();
	}

}
