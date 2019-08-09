package url;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import utils.JdbcPoll;

public class SaveUrl {
	private static Connection conn = JdbcPoll.getConn();

	public static String saveUrl(Map<String, String> map) {
		String urlid = "1";
		String result;
		String url = map.get("url");
		// 存入urled
		result = saveUrled(url);
		if (result == "saveUrled成功") {
			// 获取urlid
			urlid = geturlid(url);
			if (urlid != "geturlid失败") {
				// 存入urlpd
				result = "geturlid成功";
				result = saveUrlpd(urlid, map);
				if (result == "saveUrlpd成功") {
					// 删除urlpd
					result = deleteUrlPd(url);
					if (urlid != "deleteUrlPd成功") {
						result = "saveUrl成功";}
				}
			}
		}
		return result;
	}

	// 存入urled
	private static String saveUrled(String url) {
		PreparedStatement pstmt;
		String result = "saveUrled失败";
		String sql = "INSERT INTO urled (urled.url) VALUES ( ? )";
		try {
			pstmt = (PreparedStatement) conn.prepareStatement(sql);
			pstmt.setString(1, url);
			//System.out.println("pstmt.toString:"+pstmt.toString());
			int rows = pstmt.executeUpdate();
			if (rows > 0)
				result = "saveUrled成功";
		} catch (Exception e) {
			System.out.println("saveUrled错误:url:" + url);
			// e.printStackTrace();
		}
		return result;
	}

	// 获取urlid
	private static String geturlid(String url) {
		PreparedStatement pstmt;
		String urlid = "geturlid失败";
		String sql = "select urled.urlid from urled where url=(?)";
		try {
			pstmt = (PreparedStatement) conn.prepareStatement(sql);
			pstmt.setString(1, url);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				urlid = rs.getString(1);
				// System.out.println(urlid);
			}
		} catch (Exception e) {
			System.out.println("geturlid错误:url:" + url);
			// e.printStackTrace();
		}
		return urlid;
	}

	// 存入urlpd
	private static String saveUrlpd(String urlid, Map<String, String> map) {
		PreparedStatement pstmt;
		String result = "saveUrlpd失败";
		Integer links = Integer.valueOf(map.get("links"));
		// System.out.println("准备新增urlpd条数:" + links);
		String sql = "INSERT INTO urlpd (urlpd.urlid,urlpd.url) VALUES ( ?,? )";
		int rows = 0;
		try {
			pstmt = (PreparedStatement) conn.prepareStatement(sql);
			for (int i = 0; i < links; i++) {
				pstmt.setString(1, urlid);
				pstmt.setString(2, map.get("link" + i));
			}
			rows = pstmt.executeUpdate();
			if (rows != links) {
				System.out.println("未成功saveUrlpd条数:" + (links - rows));
			}
		} catch (SQLException e) {
			System.out.println("saveUrlpd错误:map:"+map.toString());
			// e.printStackTrace();
		}
		result = "saveUrlpd成功";
		return result;
	}

	// 删除urlpd
	private static String deleteUrlPd(String url) {
		PreparedStatement pstmt;
		String result = "deleteUrlPd失败";
		String sql = "DELETE FROM urlpd WHERE urlpd.url=( ? )";
		int rows = 0;
		try {
			pstmt = (PreparedStatement) conn.prepareStatement(sql);
			pstmt.setString(1, url);
			rows = pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("deleteUrlPd错误:url:"+url);
			// e.printStackTrace();
		}
		if (rows > 0)
			System.out.println("存入imgpd");
		result = "deleteUrlPd成功";
		return result;
	}
}
