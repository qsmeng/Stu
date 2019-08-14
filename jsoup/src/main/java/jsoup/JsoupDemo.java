package jsoup;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 通过Jsoup的connect方法获取Connection对象
 * 再用Connection对象的execute方法获取Connection.Response对象
 * 使用Response对象，即可获取html原始文本内容
 * cookies的使用
 * 返回图片真实地址
 */
public class JsoupDemo {

	public static final String WEBURL = "http://www.baidu.com"; // 站点URL，注意要加上协议(http://)

	public static Map<String, String> cookies = new HashMap<String, String>(); // cookie

	static {
		cookies.put("cookie_name", "cookie_value"); // 初始化cookie
	}

	public static void main(String[] args) {
		try {
			Connection.Response response = Jsoup.connect(WEBURL).timeout(60000).method(Connection.Method.GET)
					.maxBodySize(0).followRedirects(false).execute();
			byte[] body = response.bodyAsBytes(); // 获取html原始文本内容
			// System.out.println(new String(response.bodyAsBytes()));
			String fileName = WEBURL.substring(WEBURL.lastIndexOf("/") + 1);
			String filePath = save(body, fileName);
			System.out.println(filePath);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 直接保存为文件
	 * 
	 * @param body
	 * @param fileName
	 * @return
	 */
	public static String save(byte[] body, String fileName) {
		String filePath = null;
		try {
			File file = new File("G:/html");
			if (!file.exists()) {
				file.mkdirs(); // 如果文件不存在则创建文件夹
			}
			fileName = fileName + ".html";
			filePath = "G:/html/" + fileName;
			@SuppressWarnings("resource")
			OutputStream outputStream = new FileOutputStream(new File(filePath));
			outputStream.write(body);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return filePath;
	}

	/**
	 * 下载图片 之后返回图片的路径
	 * 
	 * @param imageUrl
	 * @return
	 */
	public String findImage(String imageUrl) {
		String filePath = null;
		try {
			Response response = Jsoup.connect(imageUrl).ignoreContentType(true).execute();
			byte[] bytes = response.bodyAsBytes();
			File file = new File("G:/image");
			if (!file.exists()) {
				file.mkdirs(); // 如果文件不存在则创建文件夹
			}
			String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
			filePath = "G:/image/" + fileName;
			// 关于流的说明 现在学习的都是BIO 问题:会出现阻塞 改进 非阻塞IO NIO
			OutputStream outputStream = new FileOutputStream(new File(filePath));
			outputStream.write(bytes);
			// 自己编辑缓存流进行数据输出....
			outputStream.close();// 关闭流
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		// 返回图片真实地址
		return filePath;
	}
}
