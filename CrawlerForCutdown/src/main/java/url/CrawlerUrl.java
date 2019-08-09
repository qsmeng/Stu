package url;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CrawlerUrl {
	// 通过url获取完整的网页
	public static Map<String, String> crawlerUrl(String url) {
		Map<String, String> map = new LinkedHashMap<String, String>();
		Document doc;
		int timeOut = 1000;
		// String url
		// ="http://image.baidu.com/search/index?tn=baiduimage&ps=1&ct=201326592&lm=-1&cl=2&nc=1&ie=utf-8&word=白丝";
		// System.out.println("爬取:"+url);
		try {
			// Jsoup 获取整个页面
			doc = Jsoup.connect(url).data("query", "Java")// 请求参数
					.userAgent("Mozilla/4.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)")// 设置urer-agent get();
					.timeout(timeOut).get();
			// 避免空指针
			if (doc == null) {
				System.out.println("爬取url返回null" + url);
			}
			// System.out.println(doc);
			// 解析获取到的页面
			Elements links = doc.select("a[href]");
			// 保存了记录的插入顺序，在用Iterator遍历LinkedHashMap时，先得到的记录肯定是先插入的.在遍历的时候会比HashMap慢。key和value均允许为空，非同步的。
			map.put("url", url);
			map.put("links", links.size() + "");
			if (links.size() > 0) {
				for (Element src : links) {
					map.put("link" + links.indexOf(src), src.attr("abs:href"));
					System.out.println("_________");
					System.out.println(links.indexOf(src));
					System.out.println(map.get("link" + links.indexOf(src)));
				}
			}
		} catch (IOException e) {
			System.out.println("爬取url失败:" + url);
			// e.printStackTrace();
		}
		return map;
	}

}