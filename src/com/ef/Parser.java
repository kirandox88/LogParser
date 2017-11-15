package com.ef;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Kiran
 *
 */
public class Parser {
	 static ParserDAO parserDAO = new ParserDAO();

	public static void main(String[] args) {		
		if(args.length == 4){
				String filePath = args[0].split("=")[1].trim();
				String startDate = args[1].split("=")[1].trim();
				String duration = args[2].split("=")[1].trim();
				int threshold = Integer.parseInt(args[3].split("=")[1].trim());
				boolean status = loadLogFile(filePath);
				if(status){
					System.out.println("Successfully loaded the log file. PATH : " + filePath);
					List<ResponseObject> responseObjectList = parserDAO.queryLogs(startDate,duration,threshold);
					String hours = (duration.equals("hourly")) ? "1 hour":"24 hour"; 
					if(responseObjectList != null && responseObjectList.size() > 0){
						System.out.println("Below are the List of IPs that have more than " + threshold + " requests starting from " + startDate +" for next "+ hours);
						for(ResponseObject responseObject : responseObjectList){
							if(responseObject!=null){
								System.out.println(responseObject.getIpAddress() + " " + responseObject.getIpCount());
							}
						}
					}else{
						System.out.println("There are no records present for this condition.");
					}
				}else{
					System.out.println("Could not read the file from specified path.");
				}
		}else{
			System.out.println("Please provide path, startDate, duration, threshold as command line arguments to the parser.");
		}
	}

	private static boolean loadLogFile(String filePath) {
		BufferedReader logFile = null;
		FileReader fileReader = null;
		try {
			fileReader = new FileReader(filePath);
			 logFile = new BufferedReader(fileReader);
			 
			 String line = logFile.readLine();
			 List<LogObject> logObjectList = new ArrayList<>();
			 
			 //Parse and populate the object.
			 while(line != null){ 
				 LogObject logObject = new LogObject();
				 String[] strArray = null;
				 strArray = line.split("\\|");
				 logObject = populateLogObject(logObject, strArray);
				 logObjectList.add(logObject);
				 line = logFile.readLine();
			 }	
			 
			 parserDAO.saveLogInDB(logObjectList);
		} catch (FileNotFoundException e) {
			System.out.println("File not available in the specified location.");
			return false;
		} catch (IOException e) {
			System.out.println("Not able to open the file");
			e.printStackTrace();
			return false;
		}finally{
			try {
				if(logFile != null){
					logFile.close();
				}
				if(fileReader != null){
					fileReader.close();
				}
			} catch (IOException e) {
				System.out.println("Exception occured.");
				e.printStackTrace();
			}
		}
		return true;
	}

	/**
	 * @param logObject
	 * @param strArray
	 * @return 
	 */
	private static LogObject populateLogObject(LogObject logObject, String[] strArray) {
		if(strArray != null && strArray.length > 0){
			 logObject.setStartDate(strArray[0]);
			 logObject.setIpAddress(strArray[1]);
			 logObject.setRequest(strArray[2]);
			 logObject.setStatus(strArray[3]);
			 logObject.setUserAgent(strArray[4]);	 
		 }
		return logObject;
	}
}
