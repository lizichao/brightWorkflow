package cn.brightcom.system.dataswitch;

import java.util.TimerTask;

import cn.brightcom.jraf.util.Log;

/**
 * <p>Title:���ݷ��Ͷ�ʱ��</p>
 * <p>Description: ��ʱ���ݷ���,����������brightcom.xml������</p>
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: BrightCom Technologies.</p>
 * @author zxqing
 * @version 1.0
 * 
 * <p>Function List // ��Ҫ�������书��
 * 
 *      
 * <p>History: // ��ʷ�޸ļ�¼:</p>
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
			log4j.logError("��ʱ���ݽ����쳣:"+e.getMessage());	
		}
	}

}
