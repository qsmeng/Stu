package thread;

import java.util.LinkedHashMap;
import java.util.Map;

import url.CrawlerUrl;
import url.SaveUrl;

public class ImgThread extends Thread {
	private String url;

	public void setUrl(String url) {
		this.url = url;
	};

	@Override
	public void run() {
		Map<String, String> map = new LinkedHashMap<String, String>();
		//List<String> list = new LinkedList<String>();
		map = CrawlerUrl.crawlerUrl(url);
		if(map!=null) {
			String result = SaveUrl.saveUrl(map);
			if (result == "saveUrl成功") {
				// System.out.println(saveUrl成功);
			}
		}
//		list = GetImgUrl.getImgUrl(url);
//		for (String imageURL : list) {
//			Imgdownloader.downloadImgByUrl(imageURL);
//		}
	}
}