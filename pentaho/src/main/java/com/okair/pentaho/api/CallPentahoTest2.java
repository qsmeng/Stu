package com.okair.pentaho.api;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class CallPentahoTest2 {
	private static String url = "http://172.16.3.187:30001/kettle/runjob?job=home%2Ftest%2Fjob_1.kjb&level=Debug";

	public static void main(String[] args) {
		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("Authorization", "Basic YWRtaW46cGFzc3dvcmQ=");
		headerMap.put("Accept", "*/*");
		// headerMap.put("Accept-Encoding", "gzip, deflate");
		// headerMap.put("Accept-Language", "zh-CN,zh;q=0.9");
		// headerMap.put("Cache-Control", "max-age=0");
		headerMap.put("Content-Type", "application/json");
		// headerMap.put("Host", "172.16.3.187:30001");
		// headerMap.put("If-Modified-Since", "01 Jan 1970 00:00:00 GMT");
		headerMap.put("User-Agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36");
		// Map<String, String> contentMap = new HashMap<String, String>();
		// {"jobId":"admin\t?? 1\t21d329d0-ad26-11e9-9bca-0242ac11000a"}
		// contentMap.put("jobId",
		// "admin\tjob_1\t74b7c1df-c7ad-11e9-b7d3-0242ac11000a");
		// System.out.println(post(url, headerMap, contentMap));
		System.out.println(get(url, headerMap));
	}

	public static String post(String url, Map<String, String> headerMap, Map<String, String> contentMap) {
		String result = null;
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost post = new HttpPost(url);
		List<NameValuePair> content = new ArrayList<NameValuePair>();
		Iterator<Entry<String, String>> iterator = contentMap.entrySet().iterator(); // 将content生成entity
		while (iterator.hasNext()) {
			Entry<String, String> elem = (Entry<String, String>) iterator.next();
			content.add(new BasicNameValuePair(elem.getKey(), elem.getValue()));
		}
		CloseableHttpResponse response = null;
		try {
			Iterator<Entry<String, String>> headerIterator = headerMap.entrySet().iterator(); // 循环增加header
			while (headerIterator.hasNext()) {
				Entry<String, String> elem = (Entry<String, String>) headerIterator.next();
				post.addHeader(elem.getKey(), elem.getValue());
			}
			if (content.size() > 0) {
				// String json = JSONArray.parseArray(JSON.toJSONString(content)).toString();
				StringEntity se = new StringEntity(
						URLEncodedUtils.format(content, "UTF-8" != null ? "UTF-8" : HTTP.DEF_CONTENT_CHARSET.name()),
						ContentType.create(URLEncodedUtils.CONTENT_TYPE, "UTF-8"));
				se.setContentType("application/json");
				se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
				post.setEntity(se);
				System.out.println(se);
			}
			response = httpClient.execute(post); // 发送请求并接收返回数据
			if (response != null && response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity(); // 获取response的body部分
				result = EntityUtils.toString(entity); // 读取reponse的body部分并转化成字符串
			}
			return result;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				httpClient.close();
				if (response != null) {
					response.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static String get(String url, Map<String, String> headerMap) {
		String result = null;
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet get = new HttpGet(url);
		CloseableHttpResponse response = null;
		try {
			Iterator<Entry<String, String>> headerIterator = headerMap.entrySet().iterator(); // 循环增加header
			while (headerIterator.hasNext()) {
				Entry<String, String> elem = (Entry<String, String>) headerIterator.next();
				get.addHeader(elem.getKey(), elem.getValue());
			}
			response = httpClient.execute(get); // 发送请求并接收返回数据
			if (response != null && response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity(); // 获取response的body部分
				result = EntityUtils.toString(entity); // 读取reponse的body部分并转化成字符串
			}
			return result;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				httpClient.close();
				if (response != null) {
					response.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
