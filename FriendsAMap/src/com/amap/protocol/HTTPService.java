package com.amap.protocol;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.content.Intent;

public class HTTPService implements Runnable {
	private Context context = null;
	private String[] keywords = null;
	private String url = null;
	private String tableId = null;

	private int action = 0;
	private List<NameValuePair> postDatas = null;

	public HTTPService() {
	}

	public HTTPService(int action, Context context) {
		this.action = action;
		this.context = context;
	}

	public HTTPService(int action, String tableName, Context context) {
		this.action = action;
		this.context = context;
		setTableName(tableName);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

		switch (action) {
		case HTTPUtils.DO_GET_USER:
		case HTTPUtils.DO_GET_FENCE:
		case HTTPUtils.DO_GET_HISTORY:
			doGet();
			break;
		case HTTPUtils.DO_POST:
			doPost();
			break;
		default:
			break;
		}
	}

	public void doGet() {
		if (url.equalsIgnoreCase(HTTPUtils.LOCAL_QUERY)) {
			doLocalQuery();
		}

	}

	public void doPost() {
		try {
			HttpResponse response = getPostResponse();
			int code = response.getStatusLine().getStatusCode();
			if (code == 200) {
				String recieve = EntityUtils.toString(response.getEntity());
				resultPublisher(HTTPUtils.DO_POST, getUrl(),recieve);
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String parseKeywords() {
		if (keywords.length > 0) {
			StringBuffer filter = new StringBuffer();
			for (int i = 0; i < keywords.length - 1; i++)
				filter.append(keywords[i] + "|");
			filter.append(keywords[keywords.length - 1]);
			return filter.toString();
		}
		return " ";
	}

	private void resultPublisher(int doGet, String func, String datas) {
		Intent intent = new Intent();
		intent.setAction("com.broadcasttest.http");
		intent.putExtra("datas", datas);
		intent.putExtra("action", doGet);
		intent.putExtra("func", func);
		context.sendBroadcast(intent);
	}

	private HttpResponse getGetResponse() throws ClientProtocolException,
			IOException {
		// 创建一个HttpClient对象
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet request = new HttpGet(getUrl());
		request.addHeader("Accept", "text/json");
		return httpclient.execute(request);
	}

	private void doLocalQuery() {
		// {city==""}搜索整个表
		RecombineURL buildUrl = new RecombineURL(getUrl());
		setUrl(buildUrl.setTableID(getTableId()).setCity("")
				.setKeywords(parseKeywords()).setKey(HTTPUtils.WEBAPIKEY)
				.build());
		try {
			HttpResponse response = getGetResponse();
			int code = response.getStatusLine().getStatusCode();
			if (code == 200) {
				String recieve = EntityUtils.toString(response.getEntity(),
						"UTF-8");
				resultPublisher(action, HTTPUtils.LOCAL_QUERY, recieve);
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setTableName(String tableName) {
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		formparams.add(new BasicNameValuePair("key", HTTPUtils.WEBAPIKEY));
		formparams.add(new BasicNameValuePair("name", tableName));
		setPostDatas(formparams);
	}

	private HttpResponse getPostResponse() throws ClientProtocolException,
			IOException {
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(getUrl());
		httppost.addHeader("Content-Type", "application/x-www-form-urlencoded");
		httppost.setEntity(new UrlEncodedFormEntity(getPostDatas(), "UTF-8"));
		return httpclient.execute(httppost);
	}


	public String[] getKeywords() {
		return keywords;
	}

	public void setKeywords(String[] keywords) {
		this.keywords = keywords;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<NameValuePair> getPostDatas() {
		return postDatas;
	}

	public void setPostDatas(List<NameValuePair> postDatas) {
		this.postDatas = postDatas;
	}

	public String getTableId() {
		return tableId;
	}

	public void setTableId(String tableId) {
		this.tableId = tableId;
	}

	private class RecombineURL {

		StringBuffer recombURL = null;

		public RecombineURL(String url) {
			recombURL = new StringBuffer(url);
		}

		public RecombineURL setTableID(String tableid) {
			recombURL.append("tableid=" + tableid);
			return RecombineURL.this;
		}

		public RecombineURL setCity(String city) {
			if (city == null || city.equals(""))
				city = "全国";
			recombURL.append("&city=" + city);
			return RecombineURL.this;
		}

		public RecombineURL setKeywords(String keywords) {
			if (keywords == null || keywords.equals(""))
				keywords = "%20";
			recombURL.append("&keywords=" + keywords);
			return RecombineURL.this;
		}

		public RecombineURL setKey(String Key) {
			recombURL.append("&key=" + Key);
			return RecombineURL.this;
		}

		public String build() {
			return recombURL.toString();
		}
	}
}