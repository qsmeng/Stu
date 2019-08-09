package main;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import thread.ImgThread;

public class Main {
	// 将读取属性文件放在静态代码块中
	// 保证文件只被读取一次，节省资源
	static Properties prop = null;
	static {
		try {
			// 读取配置文件jdbc.properties
			prop = new Properties();
			String pathname = "src/main/resources/properties/crawler.properties";
			if (prop != null)
				prop.load(new BufferedReader(new InputStreamReader(new FileInputStream(pathname), "utf-8")));
			if (prop.getProperty("test") == "crawler") {
				// System.out.println("配置导入正確");
			} else {
				// System.out.println(prop.getProperty("test")+" and
				// "+prop.getProperty("baidu.keyword"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private static String keyword = prop.getProperty("baidu.keyword");

	// word是要搜索的关键字，pn是显示的页码，rn是一页显示多少个数据
	public static void main(String[] args) {
		ExecutorService threadPool = Executors.newFixedThreadPool(5);// 定义长度为5的线程池
		for (int page = 1; page < 20; page++) {
			String url = "http://image.baidu.com/search/avatarjson?tn=resultjsonavatarnew&ie=utf-8&word=" + keyword
					+ "&cg=star&pn=" + page * 30 + "&rn=" + 30
					+ "&itg=0&z=0&fr=&width=&height=&lm=-1&ic=0&s=0&st=-1&gsm=" + Integer.toHexString(page * 30);
			ImgThread thread = new ImgThread();
			thread.setUrl(url);
			threadPool.execute(thread);
			// result = getImgUrl(url);
			// System.out.println("page:" + page + ":" + result);
		}
		threadPool.shutdown();
	}
	// 画窗口
	// 导入配置
	// 起始页面url+keyword 或起始搜索引擎和关键字
	// 传入参数(完整url)调用爬虫
}
