package ftpspider;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by XieTiansheng on 2018/3/7.
 */

public class HttpUtil {
	private static OkHttpClient okHttpClient;

	static {
		okHttpClient = new OkHttpClient.Builder().readTimeout(1, TimeUnit.SECONDS).connectTimeout(1, TimeUnit.SECONDS)
				.build();
	}

	public static String get(String path) {
		// 创建连接客户端
		Request request = new Request.Builder().url(path).header("Authorization",
				"Basic dGFyZW5hY29kZTpjb2RlXzIwMTk=").header("charset",
						"utf-8").header("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36").build();
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

}