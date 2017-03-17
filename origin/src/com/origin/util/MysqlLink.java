package com.origin.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class MysqlLink {
  public Connection con = null;
  public PreparedStatement ps = null;
  public ResultSet rs = null;
  protected static String driver = null;
  protected static String url = null;
  protected static String userName=null;
  protected static String userPwd=null;
//类的静态方法读取配置文件
	static{
		readConfig();
	}
  public static void readConfig(){
	   Properties prop = new Properties();
	   ClassLoader loader = Thread.currentThread().getContextClassLoader();
       InputStream in = loader.getResourceAsStream("/mysqlConfig.properties");
       try {
		prop.load(in);
		driver= prop.getProperty( "driver" ).trim();    
        userName = prop.getProperty( "userName" ).trim();
        userPwd = prop.getProperty( "userPwd" ).trim();
        url = prop.getProperty( "url" ).trim();
        
        System.out.println("驱动:"+driver);
        System.out.println("用户名:"+userName+" 密码:"+userPwd);
        System.out.println("url:"+url);
	} catch (IOException e) {
		// TODO 自动生成的 catch 块
		e.printStackTrace();
	}
  }
  
  public Connection getMysqlCon(){
	  try {
		Class.forName(driver);
		con = DriverManager.getConnection(url,userName, userPwd);
	} catch (ClassNotFoundException e) {
		System.out.println("驱动加载失败!");
		e.printStackTrace();
	} catch (SQLException e) {
		System.out.println("数据库连接失败!");
		e.printStackTrace();
	}
	  return con;
  }
  
  
}
