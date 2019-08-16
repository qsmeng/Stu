package csdn;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 递归多层 排列一
 * 
 * @author qsmeng
 *
 */
public class Spider4 {
	public static String type = "java";
	public static String indexurl = "https://www.csdn.net/nav/" + type + ""; //
	public static int 爬取计数 = -1,链接总数 = 0;
	public static Document doc = null;
	/**
	 * 定义四个文件类（链接存储，图片储存，文件存储，错误链接存储）
	 */
	public static File aLinkFile,docLinkFile,errorLinkFile;

	/**
	 * 
	 * @param path 目标地址
	 */
	public static void getAllLinks(String url) {
		try {
			doc = Jsoup.parse(getOkHttpClient(url), "GBK");
		} catch (Exception e) {
			// 接收到错误链接（404页面）
			writeTxtFile(errorLinkFile, url + "\r\n"); // 写入错误链接收集文件
			爬取计数++;
			if (链接总数 > 爬取计数) { // 如果文件总数（sum）大于num(当前坐标)则继续遍历
				getAllLinks(getFileLine(aLinkFile, 爬取计数));
			}
			return;
		}
		Elements aLinks = doc.select("a[href]");
		// System.out.println("开始链接：" + url);
		for (Element element : aLinks) {
			String href = element.attr("href");
			String fullurl = url + href;
			if (("../").equals(href)) {
				continue;
			}
			// 如果文件中没有这个链接，而且链接中不包含javascript:则继续(因为有的是用js语法跳转)
			if (!readTxtFile(aLinkFile).contains(fullurl) && !url.contains("javascript")) {
				// 路径必须包含网页主链接--->防止爬入别的网站
				if (fullurl.contains(indexurl)) {
					// 判断该a标签的内容是文件还是子链接
					if (href.contains(".")) {
						// 写入文件中，文件名+文件链接
						writeTxtFile(docLinkFile, element.text() + "\r\n\t" + fullurl + "\r\n");
					} else {
						// 将链接写入文件
						writeTxtFile(aLinkFile, fullurl + "\r\n");
						链接总数++; // 链接总数+1
					}
					System.out.println("\t" + element.text() + "：\t" + fullurl);
				}
			}
		}
		爬取计数++;
		if (链接总数 > 爬取计数) { // 如果文件总数（sum）大于num(当前坐标)则继续遍历
			try {
				Thread.sleep(100 + (int) (Math.random() * 150));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			getAllLinks(getFileLine(aLinkFile, 爬取计数));
		}
	}

	/**
	 * HttpUtil
	 */
	private static OkHttpClient okHttpClient;

	static {
		okHttpClient = new OkHttpClient.Builder().readTimeout(1, TimeUnit.SECONDS).connectTimeout(1, TimeUnit.SECONDS)
				.build();
	}

	public static String getOkHttpClient(String path) {
		// 创建连接客户端
		Request request = new Request.Builder().url(path).header("charset", "utf-8").header("User-Agent",
				"Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36")
				.build();
		// 创建"调用" 对象
		Call call = okHttpClient.newCall(request);
		try {
			Response response = call.execute();// 执行
			if (response.isSuccessful()) {
				return response.body().string();
			}
		} catch (IOException e) {
			System.out.println("链接格式有误:" + path);
		}
		return null;
	}

	/**
	 * 读取文件内容
	 * 
	 * @param file 文件类
	 * @return 文件内容
	 */
	public static String readTxtFile(File file) {
		String result = ""; // 读取結果
		String thisLine = ""; // 每次读取的行
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			try {
				while ((thisLine = reader.readLine()) != null) {
					result += thisLine + "\n";
				}
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 写入内容
	 * 
	 * @param file   文件类
	 * @param urlStr 要写入的文本
	 */
	public static void writeTxtFile(File file, String urlStr) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
			writer.write(urlStr);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取文件指定行数的数据，用于爬虫获取当前要爬的链接
	 * 
	 * @param file 目标文件
	 * @param num  指定的行数
	 */
	public static String getFileLine(File file, int num) {
		String thisLine = "";
		int thisNum = 0;
		try {
			@SuppressWarnings("resource")
			BufferedReader reader = new BufferedReader(new FileReader(file));
			while ((thisLine = reader.readLine()) != null) {
				if (num == thisNum) {
					return thisLine;
				}
				thisNum++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 获取文件总行数（有多少链接）
	 * 
	 * @param file 文件类
	 * @return 总行数
	 */
	public static int getFileCount(File file) {
		int count = 0;
		try {
			@SuppressWarnings("resource")
			BufferedReader reader = new BufferedReader(new FileReader(file));
			while (reader.readLine() != null) { // 遍历文件行
				count++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}

	public static void main(String[] args) {
		aLinkFile = new File("D:/Spider/ALinks" + type + ".txt");
		docLinkFile = new File("D:/Spider/DocLinks" + type + ".txt");
		errorLinkFile = new File("D:/Spider/ErrorLinks" + type + ".txt");
		// 用数组存储四个文件对象，方便进行相同操作
		File[] files = new File[] { aLinkFile, docLinkFile, errorLinkFile };
		try {
			for (File file : files) {
				if (file.exists()) { // 如果文件存在
					file.delete();
				} // 则先删除
				file.createNewFile(); // 再创建
				Thread.sleep(256);
			}
		} catch (Exception e) {
		}
		long startTime = System.currentTimeMillis(); // 获取开始时间
		Spider4.getAllLinks(indexurl); // 开始爬取目标内容
		System.out.println("" + "——————————————————爬取结束——————————————————" + "\n目标网址：" + indexurl + "\n链接总数：" + 链接总数
				+ "条" + "\n文件总数：" + getFileCount(docLinkFile) + "份");
		writeTxtFile(aLinkFile, "链接总数：" + getFileCount(aLinkFile) + "条");
		writeTxtFile(docLinkFile, "文件总数：" + getFileCount(docLinkFile) + "份");
		writeTxtFile(errorLinkFile, "问题链接总数：" + getFileCount(errorLinkFile) + "条");
		long endTime = System.currentTimeMillis(); // 获取结束时间
		System.out.println("\n程序运行时间：" + (endTime - startTime) + "ms"); // 输出程序运行时间
	}
}