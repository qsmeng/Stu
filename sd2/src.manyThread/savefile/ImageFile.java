package savefile;

import static java.lang.System.out;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 
 * @author Administrator
 *
 */
public class ImageFile implements Runnable {
	InputStream inputStream = null;
	FileOutputStream outputStream = null;
	static String dir = null;
	int begin = 0, last = 0;
	List<String> imageUrls = new ArrayList<>();

	// 设置线程需要的参数
	public ImageFile(int begin, int last, ArrayList<String> imageUrls) {
		this.begin = begin;
		this.last = last;
		this.imageUrls = imageUrls;
	}

	// 创建文件夹
	public static void createDir(String wd) {
		dir = "G:/Spider/major/" + wd + "/";
		File file = new File(dir);
		if (file.exists()) {
			out.println("dir is exists");
		} else {
			file.mkdir();
		}
	}

	@Override
	public void run() {
		for (int i = begin; i < last; i++) {
			String url = imageUrls.get(i);
			File file = null;
			FileOutputStream fos = null;
			HttpURLConnection httpCon = null;
			InputStream in = null;
			byte[] size = new byte[1024];
			int num = 0;
			try {
				String urlName = url.substring(url.lastIndexOf("/") + 1);
				urlName=urlName+(new Random(Integer.MAX_VALUE))+".jpg";
				file = new File(dir + urlName);
				fos = new FileOutputStream(file);
				if (url.startsWith("http")) {
					httpCon = (HttpURLConnection) new URL(url).openConnection();
					httpCon.setConnectTimeout(1000);
					httpCon.setReadTimeout(3000);
					in = httpCon.getInputStream();
					while ((num = in.read(size)) != -1) {
						for (int j = 0; j < num; j++) {
							fos.write(size[j]);
						}
					}
				}
			} catch (FileNotFoundException notFoundE) {
				System.out.println("找不到该网络图片....");
			} catch (NullPointerException nullPointerE) {
				System.out.println("找不到该网络图片....");
			} catch (IOException ioE) {
				System.out.println("产生IO异常.....");
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (fos != null)
						fos.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
