package robots;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class SearchCrawler extends Thread {
	private HashMap<String, ArrayList<String>> disallowListCache = new HashMap<String, ArrayList<String>>();

	private List<String> urlList;

	private static File resultFile = new File("result.txt");
	private static BufferedWriter writer;
	static {
		try {
			if (!resultFile.exists()) {
				resultFile.createNewFile();
			}
			writer = new BufferedWriter(new FileWriter(resultFile));
		} catch (Exception e) {
		}
	}

	public SearchCrawler(String str, boolean file) throws IOException {
		urlList = new ArrayList<String>();
		if (file) {
			File f = null;
			BufferedReader reader = null;
			try {
				f = new File(str);
				reader = new BufferedReader(new FileReader(f));
				String line = "";
				while (line != null) {
					line = reader.readLine();
					urlList.add(line);
				}
			} catch (Exception e) {
			} finally {
				if (reader != null) {
					reader.close();
				}
			}
		} else {
			urlList.add(str);
		}
	}

	public BufferedWriter getBufferedWriter() {
		return writer;
	}

	public void run() {
		checkUrl(urlList);
	}

	private void checkUrl(List<String> urls) {
		Iterator<String> urlIter = urls.iterator();
		while (urlIter.hasNext()) {
			String url = urlIter.next();
			if (url == null || url.equals("")) {
				continue;
			}
			url = removeWwwFromUrl(url);
			URL verifiedUrl = verifyUrl(url);
			System.out.println(url);
			try {
				if (isRobotAllowed(verifiedUrl)) {
					writer.write(url + ":true");
				} else {
					writer.write(url + ":false");
				}
				writer.newLine();
				writer.flush();
			} catch (Exception e) {
			}
		}
	}

	private URL verifyUrl(String url) {
		if (!url.toLowerCase().startsWith("http://"))
			return null;

		URL verifiedUrl = null;
		try {
			verifiedUrl = new URL(url);
		} catch (Exception e) {
			return null;
		}

		return verifiedUrl;
	}

	private boolean isRobotAllowed(URL urlToCheck) {
		String host = urlToCheck.getHost().toLowerCase();
		ArrayList<String> disallowList = disallowListCache.get(host);

		if (disallowList == null) {
			disallowList = new ArrayList<String>();
			try {
				URL robotsFileUrl = new URL("http://" + host + "/robots.txt");
				BufferedReader reader = new BufferedReader(new InputStreamReader(robotsFileUrl.openStream()));

				String line;
				while ((line = reader.readLine()) != null) {
					if (line.indexOf("Disallow:") == 0) {
						String disallowPath = line.substring("Disallow:".length());

						int commentIndex = disallowPath.indexOf("#");
						if (commentIndex != -1) {
							disallowPath = disallowPath.substring(0, commentIndex);
						}

						disallowPath = disallowPath.trim();
						disallowList.add(disallowPath);
					}
				}

				disallowListCache.put(host, disallowList);
			} catch (Exception e) {
				return true;
			}
		}

		String file = urlToCheck.getFile();
		for (int i = 0; i < disallowList.size(); i++) {
			String disallow = disallowList.get(i);
			if (file.startsWith(disallow)) {
				return false;
			}
		}

		return true;
	}

	private String removeWwwFromUrl(String url) {
		int index = url.indexOf("://www.");
		if (index != -1) {
			return url.substring(0, index + 3) + url.substring(index + 7);
		}

		return (url);
	}

	private static void addShutDownHook(final SearchCrawler searchCrawler) {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				BufferedWriter writer = searchCrawler.getBufferedWriter();
				try {
					writer.close();
				} catch (Exception e) {
					System.out.println("Add error");
				}
			}
		});
	}

	public static void main(String[] args) throws InterruptedException, IOException {
		if (args.length != 1 && args.length != 2) {
			System.out.println("Usage-1:java SearchCrawler url");
			System.out.println("Usage-2:java SearchCrawler -f filename");
			return;
		}

		SearchCrawler crawler = null;
		if (args.length == 1) {
			crawler = new SearchCrawler(args[0], false);
		} else {
			crawler = new SearchCrawler(args[1], true);
		}
		addShutDownHook(crawler);
		crawler.setDaemon(true);
		crawler.start();
		crawler.join();
	}
}