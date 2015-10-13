package com.base.net;

/**
 * @Description 网络接口
 * @author xhAndroid@126.com
 * @version V2.0
 */
public interface NetHandler {
	
	//网络响应
	void netResponse(final Object obj);
	
	//网络错误
	void netError(Object obj, int error_code);
}
