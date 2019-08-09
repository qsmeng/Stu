package utils;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.LinkedList;
import java.util.Properties;

public class JdbcPoll {
	// 1,私有化构造函数，
	// 防止外界直接new 对象
	private JdbcPoll() {
	}

	private static LinkedList<Connection> pool = new LinkedList<Connection>();
//将读取属性文件放在静态代码块中
//保证文件只被读取一次，节省资源
	static Properties prop = null;

	static {
		try {
			// 读取配置文件jdbc.properties
			prop = new Properties();
			String pathname = "src/main/resources/properties/jdbc.properties";
			prop.load(new FileInputStream(pathname));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static {
		try {
			Class.forName(prop.getProperty("jdbc.driver"));
			// System.out.println(prop.getProperty("jdbc.username"));
			// System.out.println(prop.getProperty("jdbc.url"));
			for (int i = 0; i < 5; i++) {
				Connection conn = DriverManager.getConnection(prop.getProperty("jdbc.url"),
						prop.getProperty("jdbc.username"), prop.getProperty("jdbc.password"));
				pool.add(conn);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Connection getConn() {
		try {
			if (pool.isEmpty()) {
				for (int i = 0; i < 3; i++) {
					Connection conn = DriverManager.getConnection(prop.getProperty("jdbcUrl"), prop.getProperty("user"),
							prop.getProperty("password"));
					pool.add(conn);
				}
			}
			Connection conn = pool.remove(0);
			return conn;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void returnConn(Connection conn) {
		try {
			if (conn != null && !conn.isClosed()) {
				pool.add(conn);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
