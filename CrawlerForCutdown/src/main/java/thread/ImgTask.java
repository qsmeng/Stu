package thread;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import img.GetImgUrl;
import img.Imgdownloader;
import url.CrawlerUrl;
import url.SaveUrl;

public class ImgTask implements Runnable {

	static String url;

	public ImgTask(String url) {
		ImgTask.url = url;
	}

	@Override
	public String toString() {
		return "MyTask [name=" + url + "]";
	}

	@Override
	public void run() {
		Map<String, String> map = new LinkedHashMap<String, String>();
		List<String> list = new LinkedList<String>();
		map = CrawlerUrl.crawlerUrl(url);
		if (map != null) {
			String result = SaveUrl.saveUrl(map);
			if (result == "saveUrl成功") {
				// System.out.println(saveUrl成功);
			}
		}
		list = GetImgUrl.getImgUrl(url);
		if (list != null) {
			for (String imageURL : list) {
				Imgdownloader.downloadImgByUrl(imageURL);
			}
		}
	}
}