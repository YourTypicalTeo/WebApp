package com.example.forum.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import com.google.gson.Gson;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ServletUtil {

	/** Ensure that we will not create an instance of this class */
	private ServletUtil() {
		
	}
	
	public static <T> T getRequestData(Class<T> cls, HttpServletRequest request) throws IOException {
		// Get HTTP Request Body as (JSON) String
		final StringBuilder sb = new StringBuilder();
	    try(BufferedReader reader = request.getReader()){
	        String line;
	        while ((line = reader.readLine()) != null){
	        	sb.append(line);
	        }
	    }
	    final String payload = sb.toString();
	    // Convert (JSON) String to Object
	    final T t = jsonStrToObj(cls, payload);
		return t;
	}
	
	public static void sendResponseData(Object obj, HttpServletResponse response) throws IOException {
		// Set HTTP Response Content-Type
		response.setContentType("text/json");
		// Write JSON (String) in the HTTP Response Body
        PrintWriter out = response.getWriter();
        out.println(objToJsonStr(obj));
        out.flush();
        out.close();
	}
	
	public static String getPathFirstParam(HttpServletRequest request) {
		final String path = request.getPathInfo();
		if (path == null) return null;
		String[] tokens = path.split("/");
		for(String token : tokens) {
			if (token.trim().equals("")) continue;
			return token;
		}
		return null;
	}
	
	/* The following two methods use Gson Library */
	
	private static <T> T jsonStrToObj(Class<T> cls, String str) {
		return new Gson().fromJson(str, cls);
	}
	
	private static String objToJsonStr(Object obj) {
		return new Gson().toJson(obj);
	}
	
	/** For testing purposes ... */
	
	// A Plain Old Java Object (POJO)
	// https://www.codecademy.com/resources/docs/java/pojo
	
	public static final class TestData {
		
		private int intValue;
		public final String strValue;
		public boolean booleanValue;
		
		public TestData(int intValue, String strValue, boolean booleanValue) {
			super();
			this.intValue = intValue;
			this.strValue = strValue;
			this.booleanValue = booleanValue;
		}

		@Override
		public String toString() {
			return "TestData [intValue=" + intValue + ", strValue=" + strValue + ", booleanValue=" + booleanValue + "]";
		}
		
	}
	
	public static void main(String[] args) {
		
		System.out.println(">> ServletUtil - main() - START\n");
		
		// Java Object to JSON String
		final TestData pojo = new TestData(123, "text", true);
		final String jsonStr = objToJsonStr(pojo);
		System.out.println("String: " + jsonStr);
		
		System.out.println("\n...\n");
		
		// JSON String to Java Object
		final TestData newpojo = jsonStrToObj(TestData.class, jsonStr);
		System.out.println("Object: " + newpojo);
		
		System.out.println("\n>> ServletUtil - main() - END");
	}
	
	
}
