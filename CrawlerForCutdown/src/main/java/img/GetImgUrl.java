package img;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.text.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class GetImgUrl {

	private static Document doc;
	private static int timeOut = 1000;

	// 通过url获取完整的网页
	public static List<String> getImgUrl(String url) {
		// System.out.println(url);
		try {
			// Jsoup 获取整个页面
			doc = Jsoup.connect(url).data("query", "Java")// 请求参数
					.userAgent("Mozilla/4.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)")// 设置urer-agent get();
					.timeout(timeOut).get();
		} catch (IOException e) {
			System.out.println("爬取url失败");
			// e.printStackTrace();
		}
		if (doc == null) {
			System.out.println("爬取url返回null");
		}
		// 获取全部图片url
		String reg = "objURL\":\"(http|https)://.+?\\.(gif|jpeg|png|jpg|bmp)";
		String docstr = doc.toString();
		docstr = StringEscapeUtils.unescapeHtml4(docstr);
		Pattern pattern = Pattern.compile(reg);
		Matcher m = pattern.matcher(docstr);
		List<String> list = new LinkedList<String>();
		while (m.find()) {
			String imageURL = m.group().substring(9);
			list.add(imageURL);
			// 下載
			// System.out.println(imageURL);
			// Imgdownloader.downloadImgByUrl(imageURL);
		}
		// System.out.println("爬取url成功:" + url);
		return list;
	}
}