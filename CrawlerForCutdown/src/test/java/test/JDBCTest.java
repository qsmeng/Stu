package test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.jupiter.api.Test;

import com.mysql.cj.jdbc.Driver;

import utils.JdbcPoll;

/**
 * 这个类是jdbc的入门案例
 */
public class JDBCTest {
	/**
	 * 开发步骤： 1，注册驱动 2，获取数据库的连接 3，获取传输器 4，执行SQL 5，遍历结果集 6，关闭资源
	 * 
	 * @throws Exception
	 */
	// @Test // 单元测试
	public void JDBC() {
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			// 1，注册驱动 com.mysql.jdbc.Driver
			DriverManager.registerDriver(new Driver());
			// 2，获取数据库的连接 java.sql.Connection
			conn = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/test?characterEncoding=utf-8&serverTimezone=UTC&useSSL=false", "root",
					"root");
			// 3，获取传输器java.sql.Statement
			st = conn.createStatement();
			// 4，执行SQL,查询account表,java.sql.ResultSet
			String sql = "select * from test";
			rs = st.executeQuery(sql);
			// 5，遍历结果集
			while (rs.next()) {
				String id = rs.getString(1);
				String name = rs.getString(2);
				System.out.println("id:" + id + ",name:" + name);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 为了保证资源一定会被释放，需要放在fanilly中
			// 6，关闭资源
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					// 当52行发生异常时需要加finally
					conn = null;
				}
			}
			// 为了提高程序健壮性，需要加判断
			if (st != null) {
				try {
					st.close();
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					st = null;
				}
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					rs = null;
				}
			}
		}
	}

	@Test // 单元测试
	public void jDBCPool() {
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement pstmt;
		try {
			// 获取数据库连接
			conn = JdbcPoll.getConn();
			String sql = "select 1";
			pstmt = (PreparedStatement) conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			// 5，遍历结果集
			while (rs.next()) {
				System.out.println("jDBCPool is ok");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcPoll.returnConn(conn);
		}
	}

	// @Test // 单元测试
	public void jDBCPool2() {
		Connection conn = null;
		int rs;
		PreparedStatement pstmt;
		try {
			// 获取数据库连接
			conn = JdbcPoll.getConn();
			String sql = "INSERT INTO url (url,imgurl,imged,linkurl,linked,imgorlink) VALUES ( ? ,'imgurl',0,'linkurl',0,0)";
			pstmt = (PreparedStatement) conn.prepareStatement(sql);
			// pstmt.setString(1, "url");
			System.out.println(pstmt);
			rs = pstmt.executeUpdate();
			System.out.println(rs);
			if (rs > 0) {
				System.out.println("jDBCPool is ok");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcPoll.returnConn(conn);
		}
	}

	// @Test
	public void name() {
		System.out.println("/CrawlerForCutdown/src/test/java/test/JDBC.java");
	}
}
