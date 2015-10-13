package com.base.net;

import java.util.Vector;

import com.base.utility.LogCat;

/**
 * @Description 网络接收线程
 * @author xhAndroid@126.com
 * @version V2.1
 */
public class NetReceiveThread extends Thread {
	
	private static final String TAG = NetReceiveThread.class.getSimpleName();
	
	private Vector<NetMessage> queue = null;
	private static boolean isRunning = false;

	/** 接收实例 */
	private static NetReceiveThread instance;
	
	/**
	 * 接收线程（单例模式）
	 */
	private NetReceiveThread() {
		//super(NetReceiveThread.class.getSimpleName());
		queue = new Vector<NetMessage>(10, 10);
		isRunning = true;
		start();
		LogCat.v(TAG, "Receive----->>>Start");
	}
	
	/**
	 * 获取接收线程实例
	 * @return
	 */
	public static NetReceiveThread getInstance() {
		if (null == instance || !instance.isAlive()) {
			instance = null;
			instance = new NetReceiveThread();
		}
		
		return instance;
	}
	
	/**
	 * NetMessage 的接收队列
	 * @param msg
	 */
	public void postNetMsg(NetMessage msg) {
		synchronized (queue) {
			int size = queue.size();
			for (int i = 0; i < size; i++) {
				NetMessage netMsg = queue.get(i);
				if (netMsg.equalsObject(msg)) {
					//netMsg.netHandler = msg.netHandler;
					netMsg.setNetHandler(msg.getNetHandler());
					return;
				}
			}
		}
		
		queue.add(msg);
		if (1 == queue.size()) {
			synchronized (this) {
				notifyAll();
			}
		}
	}
	
	/**
	 * 销毁停止该网络接收线程
	 */
	public void destroyReceiveThread(NetMessage netAction) {
		instance = null;
		isRunning = false;
		queue.removeElement(netAction);
		//queue.removeAllElements();
		LogCat.v(TAG, "destroyReceiveThread----NetMessage---->>"+netAction);
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
							LogCat.v(TAG, "Receive---run():wait()");
						}
					}
				} catch (InterruptedException e) {
					isRunning = false;
					LogCat.e(TAG, "InterruptedException-------error:"+e.getMessage());
				}
			} else {
				NetMessage msg = null;
				msg = queue.firstElement();
				msg.receive();
				
				synchronized (queue) {
					queue.removeElement(msg);
				}
				
				if (null != msg.getRevData()) {
					LogCat.v(TAG, "Receive Data OK.");
				} else {
					LogCat.e(TAG, "Receive Data Error.");
				}
				
				msg = null;
			}
		}
	}
	
}
