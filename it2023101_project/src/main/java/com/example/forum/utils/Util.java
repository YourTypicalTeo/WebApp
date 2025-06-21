package com.example.forum.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Util {

	/** 
	 * Ensure that we will not create an instance of this class 
	 */
	private Util() {
		
	}
	
	/**
	 * SHA (Secure Hash Algorithm) 256
	 * @see <a href="https://www.baeldung.com/sha-256-hashing-java">Read more...</a>
	 */
	public static String getHash256(final String str) {
		if (str == null) return null;
		try {
			final MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] encodedhash = digest.digest(str.getBytes(StandardCharsets.UTF_8));
			return bytesToHex(encodedhash);
		} catch(NoSuchAlgorithmException e) {
			throw new RuntimeException("getHash256() problem !", e);
		}
	}
	
	private static String bytesToHex(byte[] hash) {
	    StringBuilder hexString = new StringBuilder(2 * hash.length);
	    for (int i = 0; i < hash.length; i++) {
	        String hex = Integer.toHexString(0xff & hash[i]);
	        if(hex.length() == 1) {
	            hexString.append('0');
	        }
	        hexString.append(hex);
	    }
	    return hexString.toString();
	}
	
	public static Connection getConn() throws SQLException {
	    String URL = "jdbc:mysql://localhost:3306/projectdb?useSSL=false&serverTimezone=UTC";
	    String USER = "root";
	    String PASS = "";

	    try {
	        Class.forName("com.mysql.cj.jdbc.Driver");  // Load and register MySQL driver
	    } catch (ClassNotFoundException e) {
	        e.printStackTrace();
	        throw new SQLException("MySQL Driver not found!", e);
	    }

	    return DriverManager.getConnection(URL, USER, PASS);
	}
	/** 
	 * For testing purposes 
	 */
	public static void main(String[] args) {
		System.out.println(">> Util - main() - START - " + new Date());
		System.out.println();

		final List<String> strList = Arrays.asList("t", "test", "tim", "vas", "maria", "eleni");
		strList.forEach( str -> { 
			final String hash_value = getHash256(str);
			System.out.println(str + "\t-->\t" + hash_value);
		});
		
		System.out.println();
		System.out.println(">> Util - main() - END - " + new Date());
		
	}
	
}
