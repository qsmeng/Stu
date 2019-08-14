package meizi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * 
 * @author qsmeng
 *
 */
public class Mzi {

	public static void main(String[] args) throws Exception {
		String url = "https://www.mzitu.com/86778/1";
		checkQuietly();
		ArrayList<String> urls = new ArrayList<String>();
		for (int i = 0; i < 4; i++) {
			int lastnumber = url.indexOf("m") + 16;
			int ordernumber = Integer.parseInt(url.substring(lastnumber));
			ordernumber++;
			String preurl = url.substring(0, lastnumber);
			url = preurl + String.valueOf(ordernumber);
			String src = getSrc(url);
			urls.add(src);
		}
		// saveImage(urls);
	}

	/**
	 * 模拟登录获取cookie和sessionid
	 */
	public static Connection loginconnect(String url) throws IOException {
		Connection connect = Jsoup.connect(url);
		// 伪造请求头
		connect.header("Accept", "application/json, text/javascript, */*; q=0.01").header("Accept-Encoding",
				"gzip, deflate");
		connect.header("Accept-Language", "zh-CN,zh;q=0.9").header("Connection", "keep-alive");
		connect.header("Content-Length", "72").header("Content-Type",
				"application/x-www-form-urlencoded; charset=UTF-8");
		connect.header("Host", "qiaoliqiang.cn").header("Referer", "http://qiaoliqiang.cn/Exam/");
		connect.header("User-Agent",
				"Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36")
				.header("X-Requested-With", "XMLHttpRequest");

		// 携带登陆信息
		connect.data("username", "362501197407067215").data("password", "123456").data("user_type", "2")
				.data("isRememberme", "yes");

		// 请求url获取响应信息
		Response res = connect.ignoreContentType(true).method(Method.POST).execute();// 执行请求
		// 获取返回的cookie
		Map<String, String> cookies = res.cookies();
		for (Entry<String, String> entry : cookies.entrySet()) {
			System.out.println(entry.getKey() + "-" + entry.getValue());
		}
		System.out.println("---------华丽的分割线-----------");
		String body = res.body();// 获取响应体
		System.out.println(body);  
		return connect;
	}

	public static void checkQuietly() {
		try {
			HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
				@Override
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			});
			SSLContext context = SSLContext.getInstance("TLS");
			context.init(null, new X509TrustManager[] { new X509TrustManager() {
				@Override
				public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				}

				@Override
				public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				}

				@Override
				public X509Certificate[] getAcceptedIssuers() {
					return new X509Certificate[0];
				}
			} }, new SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getSrc(String url) throws IOException {
		Document doc = Jsoup.connect(url).userAgent(
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31")
				.ignoreContentType(true).get();
		Elements allsrc = doc.getElementsByTag("img");
		String src = allsrc.attr("src");
		System.out.println(src);
		return src;
	}

	/**
	 * 需要登陆所以下载失败
	 * 
	 * @param urls
	 * @throws Exception
	 */
	public static void saveImage(ArrayList<String> urls) throws Exception {
		System.out.println("开始下载!");
		File file = new File("D:\\pic");
		URLConnection imageconnection = null;
		InputStream imageInputStream = null;
		for (String url : urls) {
			URL oneurl = new URL(url);
			try {
				imageconnection = oneurl.openConnection();
				imageInputStream = imageconnection.getInputStream();
			} catch (Exception e) {
				System.out.println("下载失败");
				continue;
			}
			if (!file.exists()) {
				file.mkdir();
			}
			@SuppressWarnings("resource")
			OutputStream imageoutputStream = new FileOutputStream(
					new File("D:\\pic\\" + System.currentTimeMillis() + ".jpg"));

			byte[] b = new byte[2048];
			int len = 0;
			while ((len = imageInputStream.read(b)) != -1) {
				imageoutputStream.write(b, 0, len);
			}
		}
		System.out.println("下载完成");
	}
}