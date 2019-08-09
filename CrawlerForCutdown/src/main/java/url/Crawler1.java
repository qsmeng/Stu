package url;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.LinkedHashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import utils.JdbcPoll;

public class Crawler1 {
	// 1,私有化构造函数，
	// 防止外界直接new 对象
	private Crawler1() {
	}

	public static void main(String[] args) {
		String result = "null";
		try {
			result = crawlerUrl();
		} catch (IOException e) {
			// e.printStackTrace();
		}
		System.out.println(result);
	}

	// 通过url获取完整的网页
	public static String crawlerUrl() throws IOException {
		String result = "null";
		String url = "https://jingyan.baidu.com/article/36d6ed1f510dfd1bcf4883cb.html";
		// 起始页面url
		System.out.println("爬取:" + url);
		// Jsoup 获取整个页面
		Document doc = Jsoup.connect(url).get();
		// 解析获取到的页面
		// 添加url到队列
		Elements links = doc.select("a[href]");
		Elements imgs = doc.select("img[src]");
		// 保存了记录的插入顺序，在用Iterator遍历LinkedHashMap时，先得到的记录肯定是先插入的.在遍历的时候会比HashMap慢。key和value均允许为空，非同步的。

		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("url", url);
		map.put("imgs", imgs.size() + "");
		for (Element src : imgs)
			map.put("imgs" + imgs.indexOf(src), src.attr("abs:src"));
		map.put("links", links.size() + "");
		for (Element src : links)
			map.put("links" + links.indexOf(src), src.attr("abs:href"));
		result = "爬取url成功";
		result = saveUrl(map);
		if (result == "保存url爬取结果成功")
			result = "爬取url并入库成功";
		return result;
	}

	public static String saveUrl(Map<String, String> map) {
// 		取值时使用entrySet()迭代器遍历
//		Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
//		while (iterator.hasNext()) {
//			Map.Entry<String, String> entry = iterator.next();
//			System.out.println(entry.getKey() + " ：" + entry.getValue());
//		}
		String result = "null";
		String url = map.get("url");
		try {
			PreparedStatement pstmt;
			int rs = 0;
			Connection conn = JdbcPoll.getConn();
			String sql = "INSERT INTO url (url,imgurl,imged,linkurl,linked,imgorlink) VALUES ( ? ,'imgurl',0,'linkurl',0,0)";
			pstmt = (PreparedStatement) conn.prepareStatement(sql);
			pstmt.setString(1, url);
			System.out.println(pstmt);
			rs = pstmt.executeUpdate();
			System.out.println(rs);
			if (rs > 0) {
				System.out.println("jDBCPool is ok");
			}
		} catch (Exception e) {
			result = "保存url爬取结果 插入失败";
			// e.printStackTrace();
		}
		return result;
	}
}

// 持久化(入库)格式//redis//mysql//systemfile//mapdb
// 已遍历表 urled
// urlid(pk) url
// img待下载表 imgpd
// urlid imgurl
// 待遍历url
// urlid linksurl
// img下载结果表 imged
// urlid imgurl
// 持久化方式 文件或数据库

// 判断队列长度>最大页面队列长度
// 从持久化的页面url中获取url
