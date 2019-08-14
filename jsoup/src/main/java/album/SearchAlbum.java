package album;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 
 * @author qsmeng
 *
 */
public class SearchAlbum {
	/**
	 * 起始url 喜马拉雅
	 */
	private static String host = "http://www.ximalaya.com/";

	private static String request = "search/album/#searchWord#/sc/p#currentPage#";

	/**
	 * 每一页最多20个数据
	 */
	private static final int 每页数据量 = 20;

	/**
	 * 搜索关键字的分页数
	 */
	private static int 总页数 = 1;

	/**
	 * 当前分页页数
	 */
	private static int 当前页数 = 1;

	/**
	 * 搜索关键字，修改该变量可以搜索不同关键字
	 */
	private static String searchWord = "周杰伦";

	/**
	 * 搜索结果
	 */
	private static ArrayList<Album> 专辑 = new ArrayList<Album>();

	/**
	 * 爬取网页数量
	 */
	private static int 搜索页面数 = 0;

	/**
	 * 获取相应请求的网页数据
	 */
	private Document getUrlContent(String url, String param) {
		if (url == null || param == null) {
			return null;
		}

		System.out.println("开始抓取网页:" + url + param);
		Document document = null;
		try {
			/*
			 * 根据抓包信息，设置HTTP HEADER信息
			 */
			document = Jsoup.connect(url + param)
					.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
					.header("Accept-Encoding", "gzip, deflate,sdch")
					.header("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3").header("Referer", url + param)
					.header("User-Agent",
							"Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.102 Safari/537.36")
					.timeout(5000).get();
		} catch (IOException e) {
			System.out.println("connect error...");
		}

		if (document != null) {
			搜索页面数++;
		}
		return document;
	}

	/**
	 * 将输入的搜索词做UTF-8编码
	 */
	private String getEncodeWord(String sw) {
		String encodeWord = "";
		try {
			encodeWord = URLEncoder.encode(sw, "utf-8");
		} catch (UnsupportedEncodingException e1) {
			System.out.println("UnsupportedEncodingException : " + sw);
		}

		return encodeWord;
	}

	/**
	 * 获取每个网页中的专辑数据
	 */
	private int getHref(Element ele) {
		if (ele == null) {
			return 0;
		}
		// 专辑链接
		ArrayList<String> hrefs = getHrefs(ele);
		// 专辑介绍
		Elements albumIntroElements = getIntros(hrefs);
		// 专辑名//this.document.getElementsByClassName("d-i")[0].innerText.trim()
		Elements albumTitleElements = ele.getElementsByAttributeValueMatching("class", Pattern.compile("d-i"));
		// 专辑播放量
		Elements playNumElements = ele.getElementsByAttributeValueMatching("class", Pattern.compile("listen-count"));
		// 专辑所属账号
		Elements albumUserElements = ele.getElementsByAttributeValueMatching("class",
				Pattern.compile("ellipsis createBy"));
		getAlbumArray(albumTitleElements, albumUserElements, albumIntroElements, playNumElements, hrefs);
		return hrefs.size();
	}

	/**
	 * 获取全部专辑链接并判断类型
	 */
	private static ArrayList<String> getHrefs(Element ele) {
		// 专辑地址
		Elements ehrefs = ele.getElementsByAttributeValueMatching("class",
				Pattern.compile("xm-album-title ellipsis-2"));
		ArrayList<String> hrefs = new ArrayList<>();
		for (Object ehref : ehrefs.toArray()) {
			Pattern hrefPattern = Pattern.compile("[a-z]{1,20}/\\d{1,10}");
			if (("yinyue").equals(type)) {
				hrefPattern = Pattern.compile("yinyue/\\d{1,10}");
			}
			Matcher hrefMatcherer = hrefPattern.matcher(ehref.toString());
			String request = "";
			try {
				hrefMatcherer.find();
				request = hrefMatcherer.group();
			} catch (Exception e2) {
				request = "Not Song";
				continue;
			}
			hrefs.add(request);
		}
		return hrefs;
	}

	/**
	 * 获取专辑介绍
	 */
	private static Elements getIntros(ArrayList<String> hrefs) {
		SearchAlbum searchAlbum = new SearchAlbum();
		// 专辑介绍
		Elements albumIntroElements = new Elements();
		// 可读性奇差的优雅的lambda
		hrefs.forEach(request -> {
			if (!("yinyue").equals(type) || ("Not Song").equals(request)) {
				albumIntroElements.add(null);
			} else {
				// 对每一个专辑进行网页数据抓取
				Document albumDocument = searchAlbum.getUrlContent(host, request);
				request = host + request;
				// 专辑介绍
				Elements albumIntro = albumDocument.getElementsByAttributeValueMatching("class",
						Pattern.compile("album-intro _Agw"));
				albumIntroElements.add(albumIntro.first());
			}
		});
		return albumIntroElements;
	}

	/**
	 * 全部专辑分页数
	 */
	private static void page(Element ele, int yinyue) {
		if (总页数 == 1) {
			Elements albumCount = ele.getElementsByAttributeValueMatching("class", Pattern.compile("em _gW_"));
			String element = albumCount.first().toString().trim();
			// <span class="em _gW_">213</span>
			int start = element.indexOf(">") + 1;
			int end = element.indexOf("</");
			int totalAlbumNum = Integer.parseInt(element.substring(start, end));
			总页数 = (totalAlbumNum / 每页数据量) + ((totalAlbumNum % 每页数据量 == 0) ? 0 : 1);
			System.out.println(
					"一共有" + totalAlbumNum + "条数据," + 总页数 + "个分页,第" + 当前页数 + "页已爬取完毕,音乐专辑有" + yinyue + "条,请耐心等待...");
		}
	}

	/**
	 * 解析获取的title,anchorman,info,totalPlayCount标签元素
	 */
	private static void getAlbumArray(Elements albumTitleElements, Elements albumUserElements,
			Elements albumIntroElements, Elements playNumElements, ArrayList<String> hrefs) {
		int i = 0;
		for (String href : hrefs) {
			String title = albumTitleElements.get(i).text().trim();
			Element element = albumUserElements.get(i);
			int start = element.toString().indexOf("title=\"");
			int end = element.toString().indexOf("\" class");
			String user = element.toString().substring(start + "title=\"".length(), end);
			String intro = "";
			try {
				intro = albumIntroElements.get(i).text().trim();
			} catch (Exception e) {
				continue;
			}

			String totalPlayCount = playNumElements.get(i).text().trim();
			Album album = new Album(title, user, intro, totalPlayCount, href);
			专辑.add(album);
			i++;
		}
	}

	/**
	 * 转换播放数量表达方式
	 */
	private static String transPlayCount(String pc) {
		int index;
		if ((index = pc.indexOf("万")) != -1) {
			String count = pc.substring(0, index);
			return String.valueOf((Double.parseDouble(count) * 10000));
		}

		return pc;
	}

	/**
	 * 类型 yinyue(音乐)/all(全部)
	 */
	private static String type = "yinyue";

	public static void main(String args[]) {
		SearchAlbum searchAlbum = new SearchAlbum();
		Scanner sc = new Scanner(System.in);
		while (true) {
			System.out.print("请输入搜索的关键词(输入\"exit\"退出程序):");
			searchWord = sc.nextLine().trim();
			if (searchWord.equals("exit")) {
				break;
			}
			long startTime = System.currentTimeMillis();
			// 开始爬虫
			System.out.println("开始搜索关键字:" + searchWord + ",请等待...");
			// 搜索所有分页
			while (总页数 >= 当前页数) {
				// 获得请求
				String param = request.replace("#searchWord#", searchAlbum.getEncodeWord(searchWord))
						.replace("#currentPage#", String.valueOf(当前页数));
				// 获取url+param请求的网页数据
				Document doc = searchAlbum.getUrlContent(host, param);
				// 定位body
				Element ele = doc.body();
				// 定位主要内容
				ele = ele.getElementsByAttributeValueMatching("class", Pattern.compile("main-content _22L")).first();
				// 网页数据分析
				int size=searchAlbum.getHref(ele);
				++当前页数;
				//播报爬取进度
				page(ele, size);
			}
			long endTime = System.currentTimeMillis();
			float spendTime = (endTime - startTime) / 1000F;
			// 排序
			extracted();
			System.out.println("爬取网页数量:" + 搜索页面数 + ",消耗时间:" + spendTime + "秒,搜索结果数量:" + 专辑.size() + "条,分页数量:" + 总页数);
			for (Album a : 专辑) {
				System.out.println(a);
			}

			当前页数 = 1;
			总页数 = 1;
			搜索页面数 = 0;
			专辑.clear();
		}
		sc.close();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static void extracted() {
		Collections.sort(专辑, new Comparator() {
			@Override
			public int compare(Object o1, Object o2) {
				if (o1 instanceof Album && o2 instanceof Album) {
					Album a1 = (Album) o1;
					Album a2 = (Album) o2;
					String c1 = a1.getTotalPlayCount();
					String c2 = a2.getTotalPlayCount();
					return new Double(transPlayCount(c2)).compareTo(new Double(transPlayCount(c1)));
				}
				return 0;
			}
		});
	}

}
