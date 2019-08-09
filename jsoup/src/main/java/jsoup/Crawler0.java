package jsoup;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Crawler0 {
	public static void main(String[] args) {
		// 画窗口
		// 导入配置
		// 起始页面url+keyword 或起始搜索引擎和关键字
		// 传入参数(完整url)调用爬虫

	}

	// 通过url获取完整的网页
	public Crawler0(String url) throws IOException {
		// 起始页面url
		System.out.println("爬取:" + url);
		// Jsoup 获取页面
		Document doc = Jsoup.connect(url).get();
		// 解析获取到的页面
		// 添加url到队列
		Elements links = doc.select("a[href]");

		// Elements medias = doc.select("[src]");

		Elements imgs = doc.select("img[src]");

//		Elements imports = doc.select("link[href]");

		// 持久化(入库)内容准备
		// 入库格式
		// 遍历结果概述表
		// urlid(pk) url links-size imgs-size
		// 遍历结果详情表
		// urlid(mk(实际不建立外键))
		// 待遍历url表

		// 保存了记录的插入顺序，在用Iterator遍历LinkedHashMap时，先得到的记录肯定是先插入的.在遍历的时候会比HashMap慢。key和value均允许为空，非同步的。
		// 取值时使用entrySet()迭代器遍历
		// Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
		// while (iterator.hasNext()) {
		// Map.Entry<String, String> entry = iterator.next();
		// System.out.println(entry.getKey() + " ：" + entry.getValue());
		// }
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("imgs", imgs.size() + "");
		for (Element src : imgs)
			map.put("imgs" + imgs.indexOf(src), src.attr("abs:src"));
//		map.put("medias",medias.size()-imgs.size()+"");
//		for (Element src : medias) {
//			if (src.tagName().equals("img")) {
//				map.put("map"+medias.indexOf(src), src.tagName());
//				print(" * %s: <%s> %sx%s (%s)", src.tagName(), src.attr("abs:src"), src.attr("width"),
//						src.attr("height"), trim(src.attr("alt"), 20));
//			}else
//				print(" * %s: <%s>", src.tagName(), src.attr("abs:src"));
//		}

//		for (Element src : imports)
//		map.put("imports"+imports.indexOf(src), src.attr("abs:href"));

//		print("\nImports: (%d)", imports.size());
//		for (Element link : imports) {
//			print(" * %s <%s> (%s)", link.tagName(), link.attr("abs:href"), link.attr("rel"));
//		}

		for (Element src : links)
			map.put("links" + links.indexOf(src), src.attr("abs:href"));

//		print("\nLinks: (%d)", links.size());
//		for (Element link : links) {
//			print(" * a: <%s>  (%s)", link.attr("abs:href"), trim(link.text(), 35));
//		}

	}

	// 输出优化
//	private static void print(String msg, Object... args) {
//		System.out.println(String.format(msg, args));
//	}

	// 截取优化
//	private static String trim(String s, int width) {
//		if (s.length() > width)
//			return s.substring(0, width - 1) + ".";
//		else
//			return s;
//	}
	// 持久化配置 文件或数据库

	// 判断队列长度>最大页面队列长度
	// 从持久化的页面url中获取url

	// 添加图片url到队列
	// 判断队列长度>最大图片队列长度(maximglistlength) 持久化当前队列
	// 图片下载线程池
	// 从持久化的图片url中获取url

}
