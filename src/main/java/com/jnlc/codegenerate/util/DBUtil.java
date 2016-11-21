package com.jnlc.codegenerate.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DBUtil {
	/**
	 * 获取Connection连接
	 * @return
	 */
	private static Connection getConnection(){
		Connection connection = null;
		String driverClass="";
		String jdbcUrl="";	
		String user="";
		String password="";
		try {			
			InputStream propertiesInputStream=DBUtil.class.getClassLoader().getResourceAsStream("db.properties");
			Properties dbProperties=new Properties();		
			dbProperties.load(propertiesInputStream);
			driverClass=dbProperties.getProperty("driverClass");
			jdbcUrl=dbProperties.getProperty("url");
			user=dbProperties.getProperty("user");
			password=dbProperties.getProperty("password");
			Class.forName(driverClass);
			connection=DriverManager.getConnection(jdbcUrl, user, password);
		} catch (Exception e) {
			System.out.println("获取连接异常："+e.getMessage());
			e.printStackTrace();
		}
		return connection;
	}
}
