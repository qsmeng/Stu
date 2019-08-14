package idea;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.text.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import utils.JdbcPoll;

/**
 * 
 * @author qsmeng
 *
 */
public class Crawler3 {
	private Crawler3() {
	}

	private static String result;
	private static Document doc;
	private static int timeOut = 1000;
	private static int number = 5;
	private static int page = 1;
	private static String keyword = "衣服";

	public static void main(String[] args) {
		result = crawlerUrl();
		System.out.println(result);
	}

	/**
	 * 通过url获取完整的网页
	 * 
	 * @return 爬取url成功
	 */
	public static String crawlerUrl() {
		// word是要搜索的关键字，pn是显示的页码，rn是一页显示多少个数据
		String url = "http://image.baidu.com/search/avatarjson?tn=resultjsonavatarnew&ie=utf-8&word=" + keyword
				+ "&cg=star&pn=" + page * number + "&rn=" + number
				+ "&itg=0&z=0&fr=&width=&height=&lm=-1&ic=0&s=0&st=-1&gsm=" + Integer.toHexString(page * number);
		// String url =
		// "http://image.baidu.com/search/index?tn=baiduimage&ps=1&ct=201326592&lm=-1&cl=2&nc=1&ie=utf-8&word=string%E8%BD%ACint";
		// 起始页面url
		System.out.println(url);
		try {
			// Jsoup 获取整个页面
			doc = Jsoup.connect(url).data("query", "Java")
					// 设置请求参数
					.userAgent("Mozilla/4.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)").timeout(timeOut)
					.get();
		} catch (IOException e) {
			result = "爬取url失败";
			// e.printStackTrace();
		}
		if (doc == null) {
			result = "爬取url返回null";
			return result;
		}
		// System.out.println(doc);
		// 解析获取到的页面
		Elements imgs = doc.select("img[src]");
		Elements links = doc.select("a[href]");
		// 获取全部图片url
		String reg = "objURL\":\"http://.+?\\.(gif|jpeg|png|jpg|bmp)";
		String docstr = doc.toString();
		System.out.println(docstr);
		// 字符串解析
		docstr = StringEscapeUtils.unescapeHtml4(docstr);
		// 正则匹配获取想要的数据
		Pattern pattern = Pattern.compile(reg);
		Matcher m = pattern.matcher(docstr);
		while (m.find()) {
			// 截取 objURL":"9个字符余下部分才是所需 http://.+?\\.(gif|jpeg|png|jpg|bmp)
			String imageurl = m.group().substring(9);
			System.out.println(imageurl);
		}
		// 保存了记录的插入顺序，在用Iterator遍历LinkedHashMap时，先得到的记录肯定是先插入的.在遍历的时候会比HashMap慢。key和value均允许为空，非同步的。
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("url", url);
		map.put("imgs", imgs.size() + "");
		if (links.size() > 0) {
			for (Element src : imgs) {
				if (src.toString() != "" || src.attr("abs:src") != "") {
					System.out.println(src.attr("abs:src"));
					map.put("img" + imgs.indexOf(src), src.attr("abs:src"));
					System.out.println(imgs.indexOf(src));
				}
			}
		}
		map.put("links", links.size() + "");
		if (links.size() > 0) {
			// System.out.println(links.get(links.size()-1));
			for (Element src : links) {
				map.put("link" + links.indexOf(src), src.attr("abs:href"));
				// System.out.println(links.indexOf(src));
			}
		}
		result = "爬取url成功";
		result = saveUrl(map);
		if ( "保存url爬取结果成功".equals(result)) {
			result = "爬取url并入库成功";
		}
		return result;
	}

	private static String sql;
	private static String urlid;

	public static String saveUrl(Map<String, String> map) {
		// urlid 序列(表模拟) 触发器 存储过程 查询
		// urlid 索引+自增
		PreparedStatement pstmt;
		ResultSet rs = null;
		int rows = 0;
		try {
			// 获取jdbc连接
			Connection conn = JdbcPoll.getConn();
			// 将已扫描url加入urled//存入imgpd//存入urlpd//删除urlpd
			sql = "INSERT INTO urled (url) VALUES ( ? )";
			pstmt = (PreparedStatement) conn.prepareStatement(sql);
			pstmt.setString(1, map.get("url"));
			rows = pstmt.executeUpdate();
			if (rows > 0) {
				sql = "select max(urlid) from urled";
				pstmt = (PreparedStatement) conn.prepareStatement(sql);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					urlid = rs.getString(1);
					System.out.println(urlid);
					// 存入img
					int imgs = Integer.valueOf(map.get("imgs"));
					int row = 0;
					for (int i = 0; i < imgs; i++) {
						System.out.println("准备新增imgpd条数:" + imgs);
						// 存入imgpd
						sql = "INSERT INTO imgpd (urlid,imgurl) VALUES ( ?,? )";
						pstmt.setString(1, urlid);
						pstmt.setString(2, map.get("url" + i));
						rows = pstmt.executeUpdate();
						row = row + rows;
						System.out.println("成功新增imgpd条数:" + row);
						if (rows == 0) {
							// 存入imged
							sql = "INSERT INTO imged (urlid,imgurl) VALUES ( ?,? )";
							pstmt.setString(1, urlid);
							pstmt.setString(2, map.get("url"));
							rows = pstmt.executeUpdate();
						}
					}
					// 存入links
					int links = Integer.valueOf(map.get("links"));
					System.out.println("准备新增imgpd条数:" + links);
					for (int i = 0; i < imgs; i++) {
						// 存入imgpd
						sql = "INSERT INTO imgpd (urlid,imgurl) VALUES ( ?,? )";
						pstmt.setString(1, urlid);
						pstmt.setString(2, map.get("url"));
						rows = pstmt.executeUpdate();
						if (rows > 0) {
							System.out.println("存入imgpd");
						}
					}
				}
			}
		} catch (Exception e) {
			result = "保存url爬取结果 插入失败";
			// e.printStackTrace();
		}
		return result;
	}
}