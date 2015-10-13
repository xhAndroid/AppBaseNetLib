package com.base.net;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.app.config.AppConfig;
import com.base.utility.LogCat;
import com.base.utility.StringUtil;

/**
 * @Description 网络请求发送设置、接收实现
 * @author xhAndroid@126.com
 * @version V2.0
 */
public class NetSender {

	private static final String TAG = NetSender.class.getSimpleName();

	/** 网络Post请求方式 */
	private HttpPost httpPost = null;
	/** 网络Get请求方式 */
	private HttpGet httpGet = null;

	public NetSender() {
		
	}

	/**
	 * 发送Post网络请求
	 * 
	 * @param sendData
	 * @param url
	 * @return
	 */
	public boolean sendPostData(List<NameValuePair> param, String url) {
		LogCat.v(TAG, url);
		
		httpPost = new HttpPost(url);
		HttpConnectionParams.setConnectionTimeout(httpPost.getParams(), AppConfig.NET_REQUEST_TIMEOUT);
		httpPost.addHeader("charset", HTTP.UTF_8);
		
		try {
			HttpEntity enti = new UrlEncodedFormEntity(param, HTTP.UTF_8);
			httpPost.setEntity(enti);
			LogCat.v(TAG, "发送的数据---Data:"+ param);
			return true;
		} catch (IOException e) {
			NetError.NetErrorCode = 301;
			LogCat.e(TAG, e.getMessage());
		}

		return false;
	}
	
	/**
	 * 发送Get网络请求
	 * 
	 * @param url
	 * @return
	 */
	public boolean sendGetData(String url) {
		if (StringUtil.isEmpty(url)) return false;
		//mUrl = url;
		LogCat.v(TAG, url);
		
		httpGet = new HttpGet(url);
		/*HttpConnectionParams.setConnectionTimeout(httpPost.getParams(), AppConfig.NET_REQUEST_TIMEOUT);
		httpGet.addHeader("charset", HTTP.UTF_8);*/
		
		return true;
	}

	/**
	 * 接收Post/Get返回的网络数据包
	 * 
	 * @return
	 */
	public String receiveData() {
		int http_type;
		if (httpPost != null) {
			http_type = AppConfig.HTTP_POST;
			HttpConnectionParams.setSoTimeout(httpPost.getParams(), AppConfig.NET_RECEIVE_TIMEOUT);
		} else if (httpGet != null) {
			http_type = AppConfig.HTTP_GET;
			HttpConnectionParams.setSoTimeout(httpGet.getParams(), AppConfig.NET_RECEIVE_TIMEOUT);
		} else {
			return null;
		}

		try {
			//使用execute方法发送Http Post/Get请求，并返回HttpResponse对象
			HttpResponse httpResponse;
			if (AppConfig.HTTP_POST == http_type) {
				httpResponse = new DefaultHttpClient().execute(httpPost);
			} else /*if (AppConfig.HTTP_GET == http_type)*/ {
				httpResponse = new DefaultHttpClient().execute(httpGet);
			}
			
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = httpResponse.getEntity();
				String strResult = EntityUtils.toString(entity, "UTF-8");
				//打印接收到的数据
				LogCat.v(TAG, httpPost.getURI()+"\n接收到的数据---Data:\n"+strResult);
				return strResult;
			}
		} catch (HttpHostConnectException e) {
			NetError.NetErrorCode = 101;
			LogCat.e(TAG, NetError.NetErrorCode +" :/n" + e);
		} catch (ClientProtocolException e) {
			NetError.NetErrorCode = 201;
			LogCat.e(TAG, NetError.NetErrorCode +" :/n" + e);
		} catch (IOException e) {
			NetError.NetErrorCode = 301;
			LogCat.e(TAG, NetError.NetErrorCode +" :/n" + e);
		} catch (Exception e) {
			NetError.NetErrorCode = 401;
			LogCat.e(TAG, NetError.NetErrorCode +" :/n" + e);
		}
		return null;
	}
	
}
