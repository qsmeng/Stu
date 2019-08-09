package jsoup;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JsoupForB2 {

	private static String urlStr = "http://www.baidu.com/s?wd=";

	private static void getNews(String url, int num) {
		try {
			System.out.println(url);
			Document doc = Jsoup.connect(url).get();
			System.out.println(doc.toString());
			Element element = doc.getElementById("content_left");
			System.out.println("--------------------- " + 1 + " ---------------------");
			System.out.println(element.text());

			for (int i = 1; i < (num + 1); i++) {
				Element result = element.getElementById(String.valueOf(i));
				Elements add = result.select("a");

				System.out.println("--------------------- " + i + " ---------------------");
				System.out.println(add.first().text());
				String attr = add.first().attr("href");
				System.out.println(attr);
				System.out.println(getRealUrlFromBaiduUrl(attr));
				System.out.println();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void getResult(int num, String question) {

		String url = "";
		try {
			url = urlStr + URLEncoder.encode(question, "utf-8") + "&rn=" + num;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		getNews(url, num);

	}

	// 转成真实url
	public static String getRealUrlFromBaiduUrl(String url) {
		Connection.Response res = null;
		int itimeout = 60000;
		try {
			res = Jsoup.connect(url).timeout(itimeout).method(Connection.Method.GET).followRedirects(false).execute();
			return res.header("Location");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static void main(String[] args) {
		getResult(100, "邱收");
	}
}