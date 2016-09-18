package cn.brightcom.system.dataswitch;

import java.util.TimerTask;

import cn.brightcom.jraf.util.Log;

/**
 * <p>Title:数据发送定时器</p>
 * <p>Description: 定时数据发送,启动周期在brightcom.xml中配置</p>
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: BrightCom Technologies.</p>
 * @author zxqing
 * @version 1.0
 * 
 * <p>Function List // 主要函数及其功能
 * 
 *      
 * <p>History: // 历史修改记录:</p>
 * <p> author    time             version      desc</p>
 * <p> zxqing    2011/07/04       1.0          build this moudle </p>
 *     
 */
public class SenderTask extends TimerTask {

	private Log log4j = new Log(this.getClass().toString());
	
	public void run() {		
		Sender sender = new Sender();
		try{
		   sender.transmit();
		}
		catch(Exception e){
			log4j.logError("定时数据交换异常:"+e.getMessage());	
		}
	}

}
