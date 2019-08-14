package baidu;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 最简单的百度网页爬取
 * 
 * @author qsmeng
 *
 */
public class SimpleCrawlerForBaidu {

	private static String urlStr = "http://www.baidu.com/s?wd=";

	/**
	 * 爬取
	 * 
	 * @param url 目标页面url
	 * @param num 所需爬取的数据条数
	 */
	private static void crawler(String url, int num) {
		try {
			System.out.println(url);
			// 全部内容
			Document doc = Jsoup.connect(url).get();
			/**
			 * <div id="content_left">
			 * <div class="result c-container " id="1" srcid="1599" tpl="se_com_default"
			 * data-click="{'rsv_bdr':'0' }">
			 * <h3 class="t"><a data-click="{ <div class="c-abstract"> <div class="f13">
			 * </div>
			 * <div class="result c-container " id="2" srcid="1599" tpl="se_com_default"
			 * data-click="{'rsv_bdr':'0' }">
			 * <h3 class="t"><a data-click="{
			 */
			// 包含所需信息的主要内容
			Element element = doc.getElementById("content_left");
			// 拆分每条信息
			for (int i = 1; i < (num + 1); i++) {
				Element result = element.getElementById(String.valueOf(i));
				/**
				 * <a data-click="{ 'F':'778317EA', 'F1':'9D73F1E4', 'F2':'4CA6DE6B',
				 * 'F3':'54E5243F', 'T':'1565748226', 'y':'ED871EF3' }" href=
				 * "http://www.baidu.com/link?url=sVxMiSZdzYJ7MEv2vjHFY5grWm3tojwZltZznNkxKez7BkmwBeAx0_EaQzva-jhE"
				 * target="_blank"><em>邱收</em>的主页 - 花椒直播,美颜椒友,疯狂卖萌</a> <a target="_blank" href=
				 * "http://www.baidu.com/link?url=sVxMiSZdzYJ7MEv2vjHFY5grWm3tojwZltZznNkxKez7BkmwBeAx0_EaQzva-jhE"
				 * class="c-showurl" style=
				 * "text-decoration:none;">www.huajiao.com/user/1...&nbsp;</a>
				 * <a class="c-tip-icon"><i class="c-icon c-icon-triangle-down-g"></i></a>
				 * <a data-click="{'rsv_snapshot':'1'}" href=
				 * "http://cache.baiducontent.com/c?m=9f65cb4a8c8507ed4fece763105392230e54f7397b84884e2c898448e435061e5a21a2ec673f1307d4c3786602ad4348afad7624381451b28cbc8b5ddccb85595c9f5133&amp;p=933bc64ad4941ce009bd9b7e0f40&amp;newp=817ed219cc904ead0dbd9b7e0f4292695d0fc20e3ad2da01298ffe0cc4241a1a1a3aecbf22221006d1c177670aae485bebfa32703d0034f1f689df08d2ecce7e7a&amp;user=baidu&amp;fm=sc&amp;query=%C7%F1%CA%D5&amp;qid=9b76f1020004d03e&amp;p1=1"
				 * target="_blank" class="m">百度快照</a>
				 */
				Elements add = result.select("a");

				System.out.println("--------------------- " + i + " ---------------------");
				// 主要内容 邱收的主页 - ...
				System.out.println(add.first().text());
				// 百度地址
				// http://www.baidu.com/link?url=sVxMiSZdzYJ7MEv2vjHFY5grWm3tojwZltZznNkxKez7BkmwBeAx0_EaQzva-jhE
				String attr = add.first().attr("href");
				System.out.println(attr);
				// 实际地址
				System.out.println(getRealUrlFromBaiduUrl(attr));
				System.out.println();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 拼接url
	 * 
	 * @param num      爬取条数
	 * @param question 关键字
	 */
	private static void getResult(int num, String question) {
		String url = "";
		try {
			// 关键字转码U8
			url = urlStr + URLEncoder.encode(question, "utf-8") + "&rn=" + num;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		crawler(url, num);
	}

	/**
	 * 转成真实url 由百度链接的地址发起一次访问 返回结果中的 header("Location") 就是相应的实际地址
	 * 
	 * @param url String 百度链接地址
	 * @return String 真实url
	 */
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
		getResult(10, "邱收");
	}
}