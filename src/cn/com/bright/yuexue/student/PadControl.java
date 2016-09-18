package cn.com.bright.yuexue.student;

import java.util.ArrayList;
import java.util.List;

import nl.justobjects.pushlet.core.Dispatcher;
import nl.justobjects.pushlet.core.Event;

import org.jdom.Document;
import org.jdom.Element;

import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.util.Constants;
import cn.brightcom.jraf.util.DatetimeUtil;
import cn.brightcom.jraf.util.Log;
import cn.brightcom.jraf.util.StringUtil;
import cn.brightcom.jraf.util.XmlDocPkgUtil;
import cn.com.bright.yuexue.util.PadSessionManager;

/**
 * <p>Title:pad锁屏控制管理</p>
 * <p>Description: pad锁屏控制管理</p>
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
 * <p> zhangxq    2014/09/15       1.0          build this moudle </p>
 *     
 */
public class PadControl {
	private XmlDocPkgUtil xmlDocUtil = null;
	private Log log4j = new Log(this.getClass().toString());
    /**
     * 动态委派入口
     * @param request xmlDoc 
     * @return response xmlDoc
     */	
	public Document doPost(Document xmlDoc) {
		xmlDocUtil = new XmlDocPkgUtil(xmlDoc);
		String action = xmlDocUtil.getAction();
		if ("getPadStatus".equals(action)){
			getPadStatus();
		}
		else if ("lockPad".equals(action)){
			lockPad();
		}
		else if ("unlockPad".equals(action)){
			unlockPad();
		}
		return xmlDoc;
	}
	/**
	 * 解锁
	 *
	 */
	public void unlockPad(){
		Element reqElement =  xmlDocUtil.getRequestData();
		List userList=reqElement.getChildren("stu_user_id");
		
		StringBuffer strSQL = new StringBuffer();
		strSQL.append("update base_studentinfo set pad_lock='N',lock_time=null");
		strSQL.append(" where userid=?");
		PlatformDao pdao = new PlatformDao();
		try {	
			pdao.beginTransaction();
			for (int i=0;i<userList.size();i++){
				Element userRec = (Element)userList.get(i);
				String stuUserID = userRec.getText();
				
				ArrayList<Object> paramList = new ArrayList<Object>();
				paramList.add(stuUserID);
				
				pdao.setSql(strSQL.toString());
				pdao.setBindValues(paramList);	
				pdao.executeTransactionSql();
				
				StringBuffer eventType= new StringBuffer("/padcontrol");
				eventType.append("-user-"+stuUserID);
				
				Event event = Event.createDataEvent(eventType.toString());
				event.setField("userid", stuUserID);
				event.setField("pad_lock", "N");
				event.setField("lock_time", "");				
				Dispatcher.getInstance().multicast(event);					
			}
			pdao.commitTransaction();
			xmlDocUtil.setResult("0");	
			xmlDocUtil.writeHintMsg("20110", "解锁成功!");
		}
		catch (Exception e) {
			pdao.rollBack();
			e.printStackTrace();
			log4j.logError("[pad状态管理-pad解锁]"+e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);            			
		} finally {
			pdao.releaseConnection();
		}
	}	
	/**
	 * 锁定pad
	 *
	 */
	public void lockPad(){
		Element reqElement =  xmlDocUtil.getRequestData();
		List userList=reqElement.getChildren("stu_user_id");
		
		StringBuffer strSQL = new StringBuffer();
		strSQL.append("update base_studentinfo set pad_lock='Y',lock_time=now()");
		strSQL.append(" where userid=?");
		PlatformDao pdao = new PlatformDao();
		try {	
			pdao.beginTransaction();
			for (int i=0;i<userList.size();i++){
				Element userRec = (Element)userList.get(i);
				String stuUserID = userRec.getText();
				ArrayList<Object> paramList = new ArrayList<Object>();
				paramList.add(stuUserID);
				
				pdao.setSql(strSQL.toString());
				pdao.setBindValues(paramList);	
				pdao.executeTransactionSql();
				
				StringBuffer eventType= new StringBuffer("/padcontrol");
				eventType.append("-user-"+stuUserID);
				
				Event event = Event.createDataEvent(eventType.toString());
				event.setField("userid", stuUserID);
				event.setField("pad_lock", "Y");
				event.setField("lock_time", DatetimeUtil.getNow(""));				
				Dispatcher.getInstance().multicast(event);				
				
			}
			pdao.commitTransaction();
			xmlDocUtil.setResult("0");	
			xmlDocUtil.writeHintMsg("20109", "锁屏成功!");
		}
		catch (Exception e) {
			pdao.rollBack();
			e.printStackTrace();
			log4j.logError("[pad状态管理-pad锁屏]"+e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);            			
		} finally {
			pdao.releaseConnection();
		}
	}
	/**
	 * 取pad状态
	 *
	 */
	public void getPadStatus(){
		Element reqElement =  xmlDocUtil.getRequestData();
		String classid=reqElement.getChildText("classid");
		String stu_user_id=reqElement.getChildText("stu_user_id");
		
		ArrayList<Object> paramList = new ArrayList<Object>();
		StringBuffer strSQL = new StringBuffer();
		strSQL.append("select t1.userid,t2.usercode,t2.username,t2.gender,t2.portrait,t1.pad_lock,t1.lock_time,now() as curr_time");
		strSQL.append(" from base_studentinfo t1,pcmc_user t2");
		strSQL.append(" where t1.userid=t2.userid and t1.state>'0' ");
		
		if (StringUtil.isEmpty(classid) && StringUtil.isEmpty(stu_user_id)){
			xmlDocUtil.writeErrorMsg("20201", "班级ID和学生ID不能同时为空");
			return;
		}
		
		if (StringUtil.isNotEmpty(classid)){
			strSQL.append(" and t1.classid=?");
			paramList.add(classid);
		}
		if (StringUtil.isNotEmpty(stu_user_id)){
			strSQL.append(" and t1.userid=?");
			paramList.add(stu_user_id);
		}		
		
		PlatformDao pdao = new PlatformDao();
		try {			
			pdao.setSql(strSQL.toString());
			pdao.setBindValues(paramList);	
			
			Element result = pdao.executeQuerySql(0,-1);
			List list = result.getChildren("Record");			
			for (int i=0;i<list.size();i++){
				Element padStatusRec = (Element)list.get(i);
				String userid=padStatusRec.getChildText("userid");				
				if (PadSessionManager.getInstance().getSession(userid)!=null){
					XmlDocPkgUtil.setChildText(padStatusRec, "online_status", "Y");
				}
				else{
					XmlDocPkgUtil.setChildText(padStatusRec, "online_status", "N");
				}
			}
	    	xmlDocUtil.getResponse().addContent(result);
	    	xmlDocUtil.setResult("0");				
		}
		catch (Exception e) {
			e.printStackTrace();
			log4j.logError("[pad状态管理-取pad状态]"+e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);            			
		} finally {
			pdao.releaseConnection();
		}		
	}
}
