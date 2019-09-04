package com.okair.pentaho.api;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;

public class CallPentahoTestpost {
	private static String url = "http://172.16.3.187:30001/pentaho/api/scheduler/triggerNow";

	public static void main(String[] args) {
		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("Authorization", "Basic YWRtaW46cGFzc3dvcmQ=");
		headerMap.put("Accept", "*/*");
		headerMap.put("Content-Type", "application/json;charset=UTF-8");
		JSONObject obj = new JSONObject();
		obj.put("jobId", "admin\tjob_1\t5d00281d-cebe-11e9-b7d3-0242ac11000a");
		String result = jsonPost(url, headerMap, obj);
		System.out.println(result);
	}

	public static String jsonPost(String url, Map<String, String> headerMap, JSONObject jsonObject) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		// CloseableHttpClient httpclient = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(url);
		String response = null;
		try {
			Iterator<Entry<String, String>> headerIterator = headerMap.entrySet().iterator(); // 循环增加header
			while (headerIterator.hasNext()) {
				Entry<String, String> elem = (Entry<String, String>) headerIterator.next();
				post.addHeader(elem.getKey(), elem.getValue());
			}
			System.out.println(jsonObject.toString());
			StringEntity s = new StringEntity(jsonObject.toString());
			s.setContentType("application/json");
			System.out.println(s);
			post.setEntity(s);
			HttpResponse res = httpclient.execute(post);
			System.out.println(res);
			if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String result = EntityUtils.toString(res.getEntity());// 返回json格式：
				response = JSONObject.toJSONString(result);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return response;
	}
}
