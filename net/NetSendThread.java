package com.base.net;

import java.util.Vector;

import com.base.utility.LogCat;

/**
 * @Description 网络发送线程
 * @author xhAndroid@126.com
 * @version V2.1
 */
public class NetSendThread extends Thread {

	private static final String TAG = NetSendThread.class.getSimpleName();

	private Vector<NetMessage> queue = null;
	private static boolean isRunning = false;

	/** 发送实例 */
	private static NetSendThread instace;
	
	/**
	 * 发送线程（单例模式）
	 */
	private NetSendThread() {
		super("NetSendThread");
		queue = new Vector<NetMessage>(10, 10);
		isRunning = true;
		start();
		LogCat.v(TAG, "Send----->>>Start");
	}
	
	/**
	 * 获取发送线程实例
	 * @return
	 */
	public static NetSendThread getInstance() {
		if (null == instace || !instace.isAlive()) {
			instace = null;
			instace = new NetSendThread();
		}
		
		return instace;
	}
	
	/**
	 * NetMessage 的请求发送队列
	 * @param netAction
	 */
	public void actionNetMessage(NetMessage netAction) {
		synchronized (queue) {
			int size = queue.size();
			for (int i = 0; i < size; i++) {
				NetMessage netMsg = queue.get(i);
				if (netMsg.equalsObject(netAction)) {
					//netMsg.url = netAction.url;
					netMsg.setUrl(netAction.getUrl());
					//netMsg.netHandler = netAction.netHandler;
					netMsg.setNetHandler(netAction.getNetHandler());
					return;
				}
			}
		}
		
		queue.add(netAction);
		if (1 == queue.size()) {
			synchronized (this) {
				notifyAll();
			}
		}
	}
	
	/**
	 * 销毁停止该网络发送请求线程
	 */
	public void destroySendThread(NetMessage netAction) {
		instace = null;
		isRunning = false;
		queue.removeElement(netAction);
		//queue.removeAllElements();
		LogCat.v(TAG, "destroySendThread----NetMessage---->>"+netAction);
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (isRunning) {
			if (queue.isEmpty()) {
				try {
					synchronized (this) {
						if (queue.isEmpty()) {
							wait();
							LogCat.i(TAG, "Send---run():wait()");
						}
					}
				} catch (InterruptedException e) {
					isRunning = false;
					LogCat.e(TAG, "InterruptedException-------error:"+e.getMessage());
				}
			} else {
				NetMessage msg = null;
				boolean ret = false;
				msg = queue.firstElement();
				ret = msg.send();
				if (!ret) {
					ret = msg.send();
				}
				if (ret) {
					LogCat.v(TAG, "Send Data OK.");
				} else {
					LogCat.e(TAG, "Send Data Error.");
				}
				
				synchronized (queue) {
					queue.removeElement(msg);
				}
				
				NetReceiveThread.getInstance().postNetMsg(msg);
				msg = null;
			}
		}
	}

}
