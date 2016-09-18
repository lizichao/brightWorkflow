package cn.com.bright.yuexue.task;

import cn.brightcom.jraf.util.DatetimeUtil;
import cn.brightcom.jraf.util.Log;
import cn.brightcom.jraf.util.TimerTask;

public class SimpleTask1 extends TimerTask {
	
	public void run() {
		System.out.println("----------  "+DatetimeUtil.getNow("")+" SimpleTask1 run ----------");
		try{
			Thread.sleep(10000);
		}
		catch (Exception e){
			
		}
		System.out.println("----------  "+DatetimeUtil.getNow("")+" SimpleTask1 complete ----------");
	}
}
