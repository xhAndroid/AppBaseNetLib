package com.base.net;

import com.szmg.ciep.R;

/**
 * @Description 网络错误提示
 * @author xhAndroid@126.com
 * @version V2.0
 */
public class NetError {
	
	/** 网络错误ID标识 */
	static int NetErrorCode = 0;
	
	public static int/*String*/ getErrorString(int code) {
		//String errorString;
		int resid;
		switch (code) {
		case 101:
			resid = R.string.net_error_01;
			//errorString = "联网失败,请检查手机网络设置!";
			break;
		case 201:
			resid = R.string.net_error_02;
			//errorString = "http传输格式异常!";
			break;
		case 301:
			resid = R.string.net_error_03;
			//errorString = "网络异常!";
			break;
		case 401:
			resid = R.string.net_error_04;
			//errorString = "系统异常!";
			break;
		default:
			resid = R.string.net_error_05;
			//errorString = "服务器异常!";
			break;
		}
		return resid;
	}
	
}
