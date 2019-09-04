package com.okair.pentaho.api;

import java.io.Closeable;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;

/**
 * @author Qsmeng
 * @version 1567582823327
 */
public class CallPentaho {
	private static String GET_URL = "http://172.16.3.187:30001/pentaho/api/scheduler/jobs";
	private static String POST_URL = "http://172.16.3.187:30001/pentaho/api/scheduler/triggerNow";
	private static Map<String, String> HEADER_MAP = new HashMap<String, String>();

	/**
	 * 通过构造函数进行初始化
	 * 
	 */
	CallPentaho() {
		HEADER_MAP.put("Accept", "*/*");
		HEADER_MAP.put("Content-Type", "application/json");
		HEADER_MAP.put("Authorization", "Basic YWRtaW46cGFzc3dvcmQ=");
		test();
	}

	public static void main(String[] args) {
		CallPentaho cp = new CallPentaho();
		String result = cp.getjobs(GET_URL, HEADER_MAP);
		System.out.println(result);
	}

	/**
	 * 测试方法 会调用 job_1 (计划管理列表的第一个job)会向 3.142 的mysql数据库的test库test表插入一条 包含当前时间的数据
	 */
	private void test() {
		String jobId = "admin\tjob_1\t5d00281d-cebe-11e9-b7d3-0242ac11000a";
		String result = callPentahoByPost(jobId);
		System.out.println(result);
	}

	/**
	 * 常规调用方法
	 * 
	 * @param jobId 想要执行的job的jobId
	 * @return 返回 NORMAL 代表执行成功
	 */
	private String callPentahoByPost(String jobId) {
		JSONObject obj = new JSONObject();
		obj.put("jobId", jobId);
		return jsonPost(POST_URL, HEADER_MAP, obj);
	}

	/**
	 * 一次执行多个job
	 * 
	 * @param jobIds
	 * @return
	 */
	@SuppressWarnings({ "null", "unused" })
	private List<String> callPentahoByPosts(List<String> jobIds) {
		List<String> resList = null;
		for (String jobId : jobIds) {
			JSONObject obj = new JSONObject();
			obj.put("jobId", jobId);
			resList.add(jsonPost(POST_URL, HEADER_MAP, obj));
		}
		return resList;
	}

	/**
	 * 发起post请求
	 * 
	 * @param url        目标url
	 * @param headerMap  请求头
	 * @param jsonObject post数据对应的jsonObject
	 * @return
	 */
	private String jsonPost(String url, Map<String, String> headerMap, JSONObject jsonObject) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost post = new HttpPost(url);
		String result = null;
		HttpResponse res = null;
		try {
			// 循环增加header
			Iterator<Entry<String, String>> headerIterator = headerMap.entrySet().iterator();
			while (headerIterator.hasNext()) {
				Entry<String, String> elem = (Entry<String, String>) headerIterator.next();
				post.addHeader(elem.getKey(), elem.getValue());
			}
			StringEntity stringEntity = new StringEntity(jsonObject.toString());
			stringEntity.setContentType("application/json");
			post.setEntity(stringEntity);
			res = httpclient.execute(post);
			if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				result = EntityUtils.toString(res.getEntity());
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				httpclient.close();
				if (res != null) {
					((Closeable) res).close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * get方法主要用来 获取当前已计划的job的列表
	 * 
	 * @param url
	 * @param headerMap
	 * @return
	 */
	private String getjobs(String url, Map<String, String> headerMap) {
		String result = null;
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet get = new HttpGet(url);
		CloseableHttpResponse response = null;
		try {
			// 循环增加header
			Iterator<Entry<String, String>> headerIterator = headerMap.entrySet().iterator();
			while (headerIterator.hasNext()) {
				Entry<String, String> elem = (Entry<String, String>) headerIterator.next();
				get.addHeader(elem.getKey(), elem.getValue());
			}
			// 发送请求并接收返回数据
			response = httpClient.execute(get);
			if (response != null && response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				// 获取response的body部分
				HttpEntity entity = response.getEntity();
				// 读取reponse的body部分并转化成字符串
				result = EntityUtils.toString(entity);
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
