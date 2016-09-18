package cn.com.bright.edu.weixin.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 数据库连接类
 * 
 * @author lhbo
 * @date 2014-01-09
 */
public class DMManagement {
	public static Connection connMS = null; 
	public static Connection connOrcl = null;
	
	/**
	 * MS sql 数据库
	 * @return
	 */
	public static Connection getConnectionMS()
	{
		try {
			if(connMS == null || connMS.isClosed()){
				Class.forName("com.microsoft.jdbc.sqlserver.SQLServerDriver");				
				String connURL="jdbc:microsoft:sqlserver://172.18.229.4:1433;databaseName=fwdt";
				// databaseName=model 中的model是数据库的名字
				connMS=DriverManager.getConnection(connURL,"sa","szedu");				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return connMS;
	}
	
	/**
	 * oracle 数据库
	 * @return
	 */
	public static Connection getConnectionOrcl()
	{
		try {
			if(connOrcl == null || connOrcl.isClosed()){		
				Class.forName("oracle.jdbc.driver.OracleDriver");
				String connURL="jdbc:oracle:thin:@210.39.44.60:1521:orcl";
				//connURL="jdbc:oracle:thin:@127.0.0.1:1521:orcl";
				connOrcl = DriverManager.getConnection(connURL, "vsts", "vsts_1234");			
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return connOrcl;
	}
	
	public static  void main(String[] args){
		Connection conn= getConnectionOrcl();
		Statement stm = null;
		try {
			stm = conn.createStatement();
			String sql="select * from t_basedata";
			ResultSet rs=stm.executeQuery(sql);
			while(rs.next()){
				System.out.println(rs.getString("mng_no"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
