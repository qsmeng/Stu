package okairfrombaidu;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Properties;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
/**
 * 
 * @author qsmeng
 *
 */
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Okair {
	/**
	 * 配置
	 */
	static Properties pro;

	/**
	 * 私有化构造器
	 */
	private Okair() {
	}

	/**
	 * dom对象
	 */
	private static Document doc;

	/**  
	 * @throws IOException
	 * 
	 */
	private static void getDoc(String url) throws IOException {
		System.out.println(url);
		doc = Jsoup.connect(url)
				.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
				.header("Accept-Encoding", "gzip, deflate,sdch")
				.header("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3").header("Referer", url)
				.header("User-Agent",
						"Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.102 Safari/537.36")
				.timeout(5000).get();
	}

	/**
	 * 基础地址
	 */
	static String url = "http://www.baidu.com/s?tn=news&rtt=4&bsst=1&cl=2&medium=1&wd=";
	/**
	 * 关键字
	 */
	static String wd = "奥凯航空";
	/**
	 * 所需数据条数
	 */
	static int num = 10;
	/**
	 * 响应
	 */
	static Connection.Response res = null;
	/**
	 * 超时时间
	 */
	static int itimeout = 60000;

	public static void main(String[] args) {
		try {
			getDoc(url + URLEncoder.encode(wd, "utf-8") + "&rn=" + num);
		} catch (IOException e) {
			System.out.println("访问失败");
		}
		Element element = doc.getElementById("content_left");
		for (int i = 1; i <= num; i++) {
			Element result = element.getElementById(String.valueOf(i));
			Elements a = result.select("a");
			System.out.println("--------------------- " + i + " ---------------------");
			// 标题
			System.out.println(a.first().text());
			// 百度地址
			String attr = a.first().attr("href");
			System.out.println(attr);
			// 实际地址
			System.out.println(getRealUrlFromBaiduUrl(attr));
			System.out.println();

		}
	}

	/**
	 * 转成真实url 由百度链接的地址发起一次访问 返回结果中的 header("Location") 就是相应的实际地址
	 * 
	 * @param url String 百度链接地址
	 * @return String 真实url
	 */
	public static String getRealUrlFromBaiduUrl(String url) {
		try {
			res = Jsoup.connect(url).timeout(itimeout).method(Connection.Method.GET).followRedirects(false).execute();
			return res.header("Location");
		} catch (IOException e) {
		}
		return "";
	}
}
