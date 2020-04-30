package com.example.demo.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

public class JdbcCommit {

	@Autowired(required=false)
	private RedisTemplate redisTemplate;

	public static Connection getConnection() {
		String url = "jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf-8&serverTimezone=GMT%2B8";
		String user = "root";
		String password = "123456";
		Connection conn = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(url, user, password);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return conn;
	}

	public static boolean dealAddDataInfo() {
		boolean flag = false;
		Connection conn = getConnection();
		Statement stmt = null;
		try {
			conn.setAutoCommit(false);
			stmt = conn.createStatement();
			 for (int i = 0; i < 1000; i++) {
	                String sql = "insert into batch values ('"+i+"', '第"+i+"条数据')";
	                //利用addBatch方法将SQL语句加入到stmt对象中
	                stmt.addBatch(sql);
	                if (i % 100 == 0 && i != 0) {
	                    //利用executeBatch方法执行1000条SQL语句
	                    stmt.executeBatch();
	                    stmt.clearBatch();
	                    conn.commit();
	                }
            }
			 stmt.executeBatch();
			 stmt.clearBatch();
			 conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return flag;
	}

	public static void Truncate() {
		Connection conn = getConnection();
		try {
			Statement st = conn.createStatement();
			st.execute("truncate table batch");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public static void main(String[] args) {
//		dealAddDataInfo();
		Truncate();
	}

}
