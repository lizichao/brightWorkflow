package cn.com.bright.edu.weixin.core;

import java.util.TimerTask;

/**
 * ���ڵ���΢���û������鶨ʱ��
 * 
 * @author lhbo
 * @date 2014-04-20
 */
public class WeiXinAccountTask extends TimerTask{	
	public void run(){
		System.out.println("���ڵ���΢���û������鶨ʱ��������");
		WeiXinAccount.getInstance().adjustmentUserGroup();
		System.out.println("���ڵ���΢���û������鶨ʱ���رգ�");
	}
}
