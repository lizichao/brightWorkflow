package cn.com.bright.edu.weixin.core;

import java.util.TimerTask;

/**
 * 定期调整微信用户所在组定时器
 * 
 * @author lhbo
 * @date 2014-04-20
 */
public class WeiXinAccountTask extends TimerTask{	
	public void run(){
		System.out.println("定期调整微信用户所在组定时器开启！");
		WeiXinAccount.getInstance().adjustmentUserGroup();
		System.out.println("定期调整微信用户所在组定时器关闭！");
	}
}
