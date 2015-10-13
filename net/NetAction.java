package com.base.net;

import java.util.List;

import org.apache.http.NameValuePair;

import com.app.config.AppConfig;
import com.app.config.AppUrls;
import com.base.utility.StringUtil;


/**
 * @Description 网络请求Action
 * @author xhAndroid@126.com
 * @version V2.1.4
 */
public class NetAction extends NetMessage {

	/** 发送的字符串数据， */
	private List<NameValuePair> sendData;
	/** 发送url的拼接部分 */
	private String urlPart;
	
	/**
	 * 
	 * @param netHandler
	 * @param urlPart
	 */
	public NetAction(NetHandler netHandler, String urlPart) {
		super.setNetHandler(netHandler);
		this.urlPart = urlPart;
	}

	/**
	 * 设置请求参数
	 * @param jsonString
	 */
	public void setData(List<NameValuePair> list) {
		sendData = list;
	}

	@Override
	public boolean equalsUrl(String urlStr) {
		// TODO Auto-generated method stub
		if (StringUtil.isEmpty(urlStr)) {
			return false;
		}
		if (urlStr.equals(this.urlPart)) {
			return true;
		}
		return false;
	}

	@Override
	protected void loadData() {
		// TODO Auto-generated method stub
	
		//设置服务器请求地址地址
		if (super.getHttpType() == AppConfig.HTTP_POST) {
			super.setUrl(AppUrls.SERVER_URL + urlPart);//POST请求 
		} else {
			super.setUrl(urlPart);//GET请求
		}
		//设置方式请求String
		super.setSendData(sendData);
	}
	
	@Override
	protected void receive() {
		// TODO Auto-generated method stub
		super.receive();
		// 网络请求结果 函数
		super.netRevResult(this);
	}
	
}
