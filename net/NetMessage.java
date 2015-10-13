package com.base.net;

import java.util.List;

import org.apache.http.NameValuePair;

import com.app.config.AppConfig;
import com.base.utility.StringUtil;

/**
 * @Description 网络请求Message
 * @author xhAndroid@126.com
 * @version V2.1
 */
public class NetMessage {

	/*private static final String TAG = NetMessage.class.getSimpleName();*/

	/** 网络请求 */
	private NetSender netSender = new NetSender(); 

	/** Http 请求方式，默认Post */
	private int httpType = AppConfig.HTTP_POST;
	
	/** 网络回调处理 接口*/
	private NetHandler netHandler;
	
	/** 请求地址URL */
	private String url;
	/** 发送的数据 */
	//private String sendData;
	private List<NameValuePair> sendData;
	
	/** 接收到的数据 */
	private String revData;

	/**
	 * 判断是否是同一个url
	 * @param url
	 * @return
	 */
	public boolean equalsUrl(String url) {
		return false;
	}
	
	/**
	 * 判断Object是否相等
	 * @param obj
	 * @see NetReceiveThread | NetSendThread
	 * @return
	 */
	public boolean equalsObject(Object obj) {
		return false;
	}
	
	/**
	 * 加载数据
	 */
	protected void loadData() {
		
	}

	/**
	 * 发送请求
	 * @see NetSendThread
	 * @return
	 */
	protected boolean send() {
		loadData();
		//注：此url已经被继承NetMessage的子类RspAction 修改了
		if (getHttpType() == AppConfig.HTTP_POST) {
			return netSender.sendPostData(sendData, url);
		} else {
			return netSender.sendGetData(url);
		}
	}
	
	/**
	 * 接收到的数据
	 * @see NetReceiveThread
	 */
	protected void receive() {
		revData = netSender.receiveData();
	}
	
	/**
	 * 网络请求结果
	 * @param msg
	 */
	protected void netRevResult(NetMessage msg) {
		if (null == netHandler) {
			return;
		}

		if (!StringUtil.isEmpty(revData)) {
			//接收成功
			netHandler.netResponse(msg);
		} else {
			// 网络接口：接收错误
			netHandler.netError(msg, NetError.NetErrorCode);
		}
	} 
	
	/**
	 * 设置为Get网络请求方式
	 * 注：默认为Post,不需要设置
	 */
	public void setHttpTypeToGET() {
		this.httpType = AppConfig.HTTP_GET;
	}
	
	/**
	 * 获取Http请求方式，用于判断是get还是post
	 * @return
	 */
	public int getHttpType() {
		return httpType;
	}

	/**
	 * 获取网络回调接口
	 * @return
	 */
	public NetHandler getNetHandler() {
		return netHandler;
	}

	/**
	 * 设置网络回调接口
	 * @param netHandler
	 */
	public void setNetHandler(NetHandler netHandler) {
		this.netHandler = netHandler;
	}

	/**
	 * 获取发送的url
	 * @return
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * 设置发送的url
	 * @param url
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * 获取发送的数据
	 * @return
	 */
	/*public String getSendData() {
		return sendData;
	}*/
	
	/**
	 * 设置发送的数据
	 * @param sendData
	 */
	public void setSendData(List<NameValuePair> sendData) {
		this.sendData = sendData;
	}
	
	/**
	 * 获取接收到的数据
	 * @return
	 */
	public String getRevData() {
		return revData;
	}
	
	/**
	 * 设置接收到的数据
	 * @return
	 */
	public void setRevData(String data) {
		revData = data;
	}
	
}
