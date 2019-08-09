package img;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

// 添加图片url到队列
// 判断队列长度>最大图片队列长度(maximglistlength) 持久化当前队列
// 图片下载线程池
// 从持久化的图片url中获取url
public class Imgdownloader {
	// 根据图片网络地址下载图片
	public static void downloadImgByUrl(String imgurl) {
		String path="G:\\"+new SimpleDateFormat("yyyy\\MM\\dd").format(new Date());
		//System.out.println(path);
		File file = null;
		File dirFile = null;
		FileOutputStream fos = null;
		HttpURLConnection httpCon = null;
		URLConnection con = null;
		URL urlObj = null;
		InputStream in = null;
		//截取url最后一段+8位随机数作为文件名
		String name=imgurl.substring(imgurl.lastIndexOf("/")+1, imgurl.lastIndexOf("."));
		name=name+Math.random()*100000000;
		//设置一次读取大小1024byte=1kb
		byte[] size = new byte[1024];
		int num = 0;
		try {
			dirFile = new File(path);
			//查找重复文件
			//FileDeletor.fileDeletor(dirFile);
			if (!dirFile.exists()) {
				dirFile.mkdirs();
			}
			dirFile.mkdir();
			file = new File(path + "//" + name + ".jpg");
			//System.out.println(file);
			fos = new FileOutputStream(file);
			if (imgurl.startsWith("http")) {
				urlObj = new URL(imgurl);
				con = urlObj.openConnection();
				httpCon = (HttpURLConnection) con;
				in = httpCon.getInputStream();
				while ((num = in.read(size)) != -1) {
					for (int i = 0; i < num; i++)
						fos.write(size[i]);
				}
			}
		} catch (FileNotFoundException notFoundE) {
			System.out.println("找不到该网络图片:notFoundE"+file.toString());
		} catch (NullPointerException nullPointerE) {
			System.out.println("找不到该网络图片:nullPointerE"+file.toString());
		} catch (IOException ioE) {
			System.out.println("产生IO异常:ioE"+file.toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
