package Utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class SpiderTest {
	public static void getHtml() throws Exception{
		//Document doc = Jsoup.connect("https://www.mzitu.com/86819").get();
		Document doc = Jsoup.connect("https://www.baidu.com/").get();
		System.out.println(doc);
	}
}
