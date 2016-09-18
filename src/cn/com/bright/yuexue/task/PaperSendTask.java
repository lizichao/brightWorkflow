package cn.com.bright.yuexue.task;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;

import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.util.DatetimeUtil;
import cn.brightcom.jraf.util.Log;
import cn.brightcom.jraf.util.TimerTask;
import cn.com.bright.yuexue.teach.PaperSender;

/**
 * <p>Title:试卷定时发送任务</p>
 * <p>Description: 试卷定时发送任务</p>
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: BrightCom Technologies.</p>
 * @author zhangxq
 * @version 1.0
 * 
 * <p>Function List // 主要函数及其功能
 *   
 *      
 * <p>History: // 历史修改记录:</p>
 * <p> author    time             version      desc</p>
 * <p> zhangxq    2014/08/25      1.0          build this moudle </p>
 *     
 */
public class PaperSendTask  extends TimerTask{
	private Log log4j = new Log(this.getClass().toString());
	
	public void run() {
		//System.out.println("----------  "+DatetimeUtil.getNow("")+" PaperSendTask run ----------");
		String sendId ="";		
		try{
			List sendList = getSendPaper();
			for (int i=0;i<sendList.size();i++){
				sendId = ((Element)sendList.get(i)).getChildText("send_id");
				PaperSender ps = new PaperSender();
				ps.implPaperSend(sendId);
				setPaperSendStatus(sendId,"2");			
			}
		}
		catch (Exception e) {	    	
			e.printStackTrace();
			setPaperSendStatus(sendId,"-1");			
			log4j.logError("[试卷发送-定时发送]"+e.getMessage());			
		}	
	}
	/**
	 * 设置发送任务状态
	 *
	 */
	public void setPaperSendStatus(String send_id,String status){
		StringBuffer updateSQL = new StringBuffer();
    	updateSQL.append(" update learn_paper_send set public_status=?,public_time=now()");
    	updateSQL.append(" where send_id=?");
    	
    	ArrayList<Object> updateParam = new ArrayList<Object>();
    	updateParam.add(status);
    	updateParam.add(send_id);
		
		PlatformDao pdao = new PlatformDao();
		try{
		    pdao.setSql(updateSQL.toString());
		    pdao.setBindValues(updateParam);
		    pdao.executeTransactionSql();			
		}
		catch (Exception e) {	    	
			e.printStackTrace();
			log4j.logError("[试卷发送-定时发送]"+e.getMessage());			
		} finally {
			pdao.releaseConnection();
		}		
	}
	/**
	 * 取待发送的任务
	 * @return
	 */
	public List getSendPaper(){
		StringBuffer strSQL = new StringBuffer();
		strSQL.append(" select send_id from learn_paper_send");
		strSQL.append(" where is_delayed='Y'");
		strSQL.append(" and send_time<=now()");
		strSQL.append(" and valid='Y'");
		strSQL.append(" and public_status='1'");
		
		PlatformDao pdao = new PlatformDao();
	    try{
	    	pdao.setSql(strSQL.toString());	    	
	    	Element result = pdao.executeQuerySql(20,1);	    	
	    	return result.getChildren("Record");	    	
	    }catch (Exception e) {	    	
			e.printStackTrace();
			log4j.logError("[试卷发送-定时发送]"+e.getMessage());	
			return null;
		} finally {
			pdao.releaseConnection();
		}		
	}
}
