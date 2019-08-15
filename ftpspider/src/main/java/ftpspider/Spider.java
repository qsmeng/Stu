package ftpspider;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Spider {

	public static String path = "http://code.tarena.com.cn/"; //
	public static int num = -1,sum = 0;
	/**
	 * 定义四个文件类（链接存储，图片储存，文件存储，错误链接存储）
	 */
	public static File aLinkFile,docLinkFile,errorLinkFile;

	/**
	 * 
	 * @param path 目标地址
	 */
	public static void getAllLinks(String path) {
		Document doc = null;
		try {
			doc = Jsoup.parse(HttpUtil.get(path));
		} catch (Exception e) {
			// 接收到错误链接（404页面）
			writeTxtFile(errorLinkFile, path + "\r\n"); // 写入错误链接收集文件
			num++;
			if (sum > num) { // 如果文件总数（sum）大于num(当前坐标)则继续遍历
				getAllLinks(getFileLine(aLinkFile, num));
			}
			return;
		}
		Elements aLinks = doc.select("a[href]");
		System.out.println("开始链接：" + path);
		String url = Spider.path;
		for (Element element : aLinks) {
			String href = element.attr("href");
			url += href;
			if (href == "../") {
				continue;
			}
			// 如果文件中没有这个链接，而且链接中不包含javascript:则继续(因为有的是用js语法跳转)
			if (!readTxtFile(aLinkFile).contains(url) && !url.contains("javascript")) {
				// 路径必须包含网页主链接--->防止爬入别的网站
				if (url.contains(Spider.path)) {
					// 判断该a标签的内容是文件还是子链接
					if (url.contains(".")) {
						// 写入文件中，文件名+文件链接
						writeTxtFile(docLinkFile, element.text() + "\r\n\t" + url + "\r\n");
					} else {
						// 将链接写入文件
						writeTxtFile(aLinkFile, url + "\r\n");
						sum++; // 链接总数+1
					}
					System.out.println("\t" + element.text() + "：\t" + url);
				}
			}
		}
		num++;
		if (sum > num) {
			getAllLinks(getFileLine(aLinkFile, num));
		}
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
		aLinkFile = new File("D:/Spider/ALinks.txt");
		docLinkFile = new File("D:/Spider/DocLinks.txt");
		errorLinkFile = new File("D:/Spider/ErrorLinks.txt");
		// 用数组存储四个文件对象，方便进行相同操作
		File[] files = new File[] { aLinkFile, docLinkFile, errorLinkFile };
		try {
			for (File file : files) {
				if (file.exists()) // 如果文件存在
					file.delete(); // 则先删除
				file.createNewFile(); // 再创建
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		long startTime = System.currentTimeMillis(); // 获取开始时间
		Spider.getAllLinks(path); // 开始爬取目标内容
		System.out.println("" + "——————————————————爬取结束——————————————————" + "\n目标网址：" + path + "\n链接总数：" + sum + "条"
				+ "\n文件总数：" + getFileCount(docLinkFile) + "份");
		writeTxtFile(aLinkFile, "链接总数：" + getFileCount(aLinkFile) + "条");
		writeTxtFile(docLinkFile, "文件总数：" + getFileCount(docLinkFile) + "份");
		writeTxtFile(errorLinkFile, "问题链接总数：" + getFileCount(errorLinkFile) + "条");
		long endTime = System.currentTimeMillis(); // 获取结束时间
		System.out.println("\n程序运行时间：" + (endTime - startTime) + "ms"); // 输出程序运行时间
	}
}