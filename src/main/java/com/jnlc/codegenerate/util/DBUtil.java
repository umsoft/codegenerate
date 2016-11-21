package com.jnlc.codegenerate.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import java.util.ResourceBundle;


public class DBUtil {
	/**
	 * 获取Connection连接
	 * @return
	 */
	public static Connection getConnection(){
		Connection connection = null;
		String driverClass="";
		String jdbcUrl="";	
		String user="";
		String password="";
		try {			
			ResourceBundle bundle=ResourceBundle.getBundle("db");
			driverClass=bundle.getString("jdbc.driver");
			jdbcUrl=bundle.getString("jdbc.url");
			user=bundle.getString("jdbc.username");
			password=bundle.getString("jdbc.password");
			Class.forName(driverClass);
			connection=DriverManager.getConnection(jdbcUrl, user, password);
		} catch (Exception e) {
			System.out.println("获取连接异常："+e.getMessage());
			e.printStackTrace();
		}
		return connection;
	}
	
	/**
	 * 执行数据库更新语句
	 * @param sql 
	 * @param objects 参数
	 * @return 影响的行数
	 */
	public static Integer excuteUpdate(String sql,Object ...args){
		Connection connection=null;
		PreparedStatement preparedStatement=null;
		int recods=0;
		try {
			connection= getConnection();
			preparedStatement=connection.prepareStatement(sql);
			if(args!=null){
			for(int i=0;i<args.length;i++){
				preparedStatement.setObject(i+1, args[i]);
			}
			}
			recods= preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("执行更新语句异常："+e.getMessage());
			e.printStackTrace();
		}finally{
			relaseResource(connection,preparedStatement);
			//relaseResource(connection,preparedStatement);
		}
		return recods;
	}
	
	/**
	 * 释放数据库资源
	 * @param conn 数据库连接
	 * @param preparedStatement
	 */
 	public static void relaseResource(Connection conn,PreparedStatement preparedStatement){
		if(preparedStatement!=null){
			try {
				preparedStatement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if(conn!=null){
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
