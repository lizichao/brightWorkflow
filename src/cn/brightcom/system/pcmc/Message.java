package cn.brightcom.system.pcmc;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;

import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.util.ConfigDocument;
import cn.brightcom.jraf.util.Constants;
import cn.brightcom.jraf.util.DatetimeUtil;
import cn.brightcom.jraf.util.Log;
import cn.brightcom.jraf.util.StringUtil;
import cn.brightcom.jraf.util.XmlDocPkgUtil;


/**
 * <p>Title:��Ϣ����ҵ��</p>
 * <p>Description: ƽ̨�ڲ���Ϣ����ҵ��</p>
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
 * <p> zxqing    2011/06/11       1.0          build this moudle </p>
 * <p> lhbo      2011/06/19       1.1          build this moudle </p>    
 */
public class Message {

	private XmlDocPkgUtil xmlDocUtil = null;
	private PlatformDao pdao = null;
	private Log log4j = new Log(this.getClass().toString());
	
    /**
     * ��̬ί�����
     * @param request xmlDoc 
     * @return response xmlDoc
     */
	public Document doPost(Document xmlDoc) {
		xmlDocUtil = new XmlDocPkgUtil(xmlDoc);
		String action = xmlDocUtil.getAction();
		
		if ("getMessagesList".equals(action)) {
			getMessagesList(); //��ȡ������Ϣ�б�
		}
		if("addMessage".equals(action)){
			addMessage(); //������Ϣ
		}
		if("deleteMessage".equals(action)){
			deleteMessage(); //ɾ����Ϣ
		}
		if("getReceivesList".equals(action)){
			getReceivesList(); //��ȡ������Ϣ�б�
		}
		if("updateReceives".equals(action)){
			updateReceives(); //������Ϣ
		}
		if("deleteReceives".equals(action)){
			deleteReceives(); //ɾ��������Ϣ
		}

		return xmlDoc;
	}

	/**
	 * ��ȡ������Ϣ�б�
	 *	 
	 **/		
	public void getMessagesList(){
		StringBuffer strSQL = new StringBuffer();		
		String userid = xmlDocUtil.getSession().getChildTextTrim("userid");//�û����	
		
		try {
			pdao = new PlatformDao();
			ArrayList<String> bvals=new ArrayList<String>();
			strSQL.append(" SELECT * FROM pcmc_messages t WHERE t.del_flag = 'N' AND t.sender = ? ");
			bvals.add(userid);
			
			String orderBy = xmlDocUtil.getOrderBy();
			if (StringUtil.isNotEmpty(orderBy)){
				strSQL.append(orderBy);
			}
			else{
			   strSQL.append(" ORDER BY t.send_time DESC");
			}
			
			pdao.setSql(strSQL.toString());
			pdao.setBindValues(bvals);
			Element resData = pdao.executeQuerySql(xmlDocUtil.getPageSize(),xmlDocUtil.getPageNo());
			xmlDocUtil.getResponse().addContent(resData);
			xmlDocUtil.setResult("0");
		} catch (Exception e) {
			e.printStackTrace();
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
			log4j.logError("[��ȡ������Ϣ�б����.]" + e.getMessage());
		} finally {
			pdao.releaseConnection();
		}
	}	
	
	/**
	 * ������Ϣ
	 * 
	 **/
	public void addMessage(){
		StringBuffer strSQL = new StringBuffer();
		Element reqElement = xmlDocUtil.getRequestData();		
		String userid = xmlDocUtil.getSession().getChildTextTrim("userid");//�û����
		String username = xmlDocUtil.getSession().getChildTextTrim("username");//�û�����
		String title = reqElement.getChildText("title");//����
		String message = reqElement.getChildText("message");//��Ϣ
	    
		String receivers = reqElement.getChildText("receivers");//�����߱����
		String receive_names = reqElement.getChildText("receive_names");//������������
		//String sendtype = reqElement.getChildText("sendtype");//��������
		
		try {
			pdao = new PlatformDao();
			pdao.beginTransaction();
			
			Element insertRecord = ConfigDocument.createRecordElement("pcmc", "pcmc_messages");
			XmlDocPkgUtil.copyValues(reqElement, insertRecord, 0, true);

			XmlDocPkgUtil.setChildText(insertRecord, "title", title);
			XmlDocPkgUtil.setChildText(insertRecord, "message", message);
			XmlDocPkgUtil.setChildText(insertRecord, "sender", userid);
			XmlDocPkgUtil.setChildText(insertRecord, "sender_name", username);
			XmlDocPkgUtil.setChildText(insertRecord, "receive_names", receive_names);
			XmlDocPkgUtil.setChildText(insertRecord, "send_time",DatetimeUtil.getNow("yyyy-MM-dd HH:mm:ss"));
			XmlDocPkgUtil.setChildText(insertRecord, "del_flag","N");
			 
            Object messages_id = pdao.insertOneRecordSeqPk(insertRecord);	
			
            String deptAry[] = receivers.split("1-"); //����ID��
    		String userAry[] = receivers.split("0-"); //�û�ID��
    		String deptids = "-1";
    		String userids = "-1";
    		for(int i=1;i<deptAry.length;i++){
    			deptids += "," + deptAry[i].substring(0, deptAry[i].indexOf(";"));			
    		}
    		for(int i=1;i<userAry.length;i++){
    			userids += "," + userAry[i].substring(0, userAry[i].indexOf(";"));			
    		}
    		
    		strSQL.append("SELECT t1.userid FROM pcmc_user t1,pcmc_dept t2 WHERE t1.deptid = t2.deptid AND (t2.deptid IN ("+deptids+") OR t1.userid IN ("+userids+"))");
			pdao.setSql(strSQL.toString());
			Element rs = pdao.executeQuerySql(-1,1);
			List userList= rs.getChildren("Record");	
            for(int i=0;i<userList.size();i++){
            	Element el = (Element) userList.get(i);
				insertRecord = ConfigDocument.createRecordElement("pcmc", "pcmc_message_receivers");
				XmlDocPkgUtil.copyValues(reqElement, insertRecord, 0, true);
				XmlDocPkgUtil.setChildText(insertRecord, "messages_id", messages_id.toString());
			    XmlDocPkgUtil.setChildText(insertRecord, "receiver", el.getChildTextTrim("userid"));
			    XmlDocPkgUtil.setChildText(insertRecord, "read_flag", "N");
			    XmlDocPkgUtil.setChildText(insertRecord, "del_flag", "N");
			    pdao.insertOneRecord(insertRecord);
            }
            
			pdao.commitTransaction();
			xmlDocUtil.setResult("0");
		} catch (Exception e) {
			e.printStackTrace();
			pdao.rollBack();
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
			log4j.logError("[������Ϣ����.]" + e.getMessage());
		} finally {
			pdao.releaseConnection();
		}	
	}	
	
	/**
	 * ɾ����Ϣ
	 * @param messages_ids ��Ϣ�����
	 * 
	 **/
	public void deleteMessage(){
		Element reqElement = xmlDocUtil.getRequestData();
		List list = reqElement.getChildren("messages_id");		
		String strSQL = "UPDATE pcmc_messages t SET t.del_flag = 'Y' WHERE t.MESSAGES_ID = ";
		try {
			pdao = new PlatformDao();
			for (int i = 0; i < list.size() ;i++){
				Element el = (Element)list.get(i);
				String  MessagesId = el.getText();
				pdao.setSql(strSQL+"'"+MessagesId+"'");
				pdao.executeTransactionSql();
			}
			xmlDocUtil.setResult("0");
			xmlDocUtil.writeHintMsg("ɾ����Ϣ�ɹ�!");
		} catch (Exception e) {
			e.printStackTrace();
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
			log4j.logError("[ɾ����Ϣ����.]" + e.getMessage());
		} finally {
			pdao.releaseConnection();
		}	
	}
	
	/**
	 * ��ȡ������Ϣ�б�
	 * @param read_type   �Ķ�����
	 * 
	 **/
	public void getReceivesList(){
		StringBuffer strSQL = new StringBuffer();
		Element reqElement = xmlDocUtil.getRequestData();		
		String userid = xmlDocUtil.getSession().getChildTextTrim("userid");//�û����
		String read_type = reqElement.getChildText("read_type");//�Ķ�����
		
		try {
			pdao = new PlatformDao();
			ArrayList<String> bvals=new ArrayList<String>();
			strSQL.append("SELECT t1.receiver_id,t1.received_time,t1.read_flag,t1.read_time,t1.notify_time,t2.* FROM pcmc_message_receivers t1,pcmc_messages t2 WHERE t1.messages_id = t2.messages_id AND t1.del_flag = 'N' AND t1.read_flag = ? AND t1.receiver = ? ");
			bvals.add(read_type);
			bvals.add(userid);
			
			String orderBy = xmlDocUtil.getOrderBy();
			if (StringUtil.isNotEmpty(orderBy)){
				strSQL.append(orderBy);
			}
			else{
			   strSQL.append(" ORDER BY t2.send_time DESC");
			}
			
			pdao.setSql(strSQL.toString());
			pdao.setBindValues(bvals);
			Element resData = pdao.executeQuerySql(xmlDocUtil.getPageSize(),xmlDocUtil.getPageNo());
			xmlDocUtil.getResponse().addContent(resData);
			xmlDocUtil.setResult("0");
		} catch (Exception e) {
			e.printStackTrace();
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
			log4j.logError("[��ȡ������Ϣ�б����.]" + e.getMessage());
		} finally {
			pdao.releaseConnection();
		}	
	}
	
	/**
	 * ������Ϣ
	 * @param receiver_id ���ձ��
	 * 
	 **/
	public void updateReceives(){
		StringBuffer strSQL = new StringBuffer();
		Element reqElement = xmlDocUtil.getRequestData();
		String receiver_id = reqElement.getChildText("receiver_id");//���ձ��
		
		try {
			pdao = new PlatformDao();
			ArrayList<String> bvals=new ArrayList<String>();
			strSQL.append("UPDATE pcmc_message_receivers t SET t.read_flag = 'Y',t.read_time = SYSDATE WHERE t.receiver_id = ?");
			bvals.add(receiver_id);
			pdao.setSql(strSQL.toString());
			pdao.setBindValues(bvals);
			pdao.executeTransactionSql();
			xmlDocUtil.setResult("0");
		} catch (Exception e) {
			e.printStackTrace();
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
			log4j.logError("[������Ϣ����.]" + e.getMessage());
		} finally {
			pdao.releaseConnection();
		}	
	}
	
	/**
	 * ɾ��������Ϣ
	 * @param receiver_ids ���ձ����
	 * 
	 **/
	public void deleteReceives(){
		StringBuffer strSQL = new StringBuffer();
		Element reqElement = xmlDocUtil.getRequestData();
		String receiver_ids = reqElement.getChildText("receiver_ids");//���ձ����
		
		try {
			pdao = new PlatformDao();
			ArrayList<String> bvals=new ArrayList<String>();
			strSQL.append("UPDATE pcmc_message_receivers t SET t.del_flag = 'Y' WHERE t.receiver_id IN (?)");
			bvals.add(receiver_ids);
			pdao.setSql(strSQL.toString());
			pdao.setBindValues(bvals);
			pdao.executeTransactionSql();
			xmlDocUtil.setResult("0");
		} catch (Exception e) {
			e.printStackTrace();
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
			log4j.logError("[ɾ��������Ϣ����.]" + e.getMessage());
		} finally {
			pdao.releaseConnection();
		}	
	}
}
