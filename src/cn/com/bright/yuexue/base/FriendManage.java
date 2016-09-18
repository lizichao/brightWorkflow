package cn.com.bright.yuexue.base;

import java.util.ArrayList;

import org.jdom.Document;
import org.jdom.Element;

import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.util.ConfigDocument;
import cn.brightcom.jraf.util.Constants;
import cn.brightcom.jraf.util.DaoUtil;
import cn.brightcom.jraf.util.Log;
import cn.brightcom.jraf.util.StringUtil;
import cn.brightcom.jraf.util.XmlDocPkgUtil;

/**
 * ���ѹ���
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2014 Brightcom</p>
 * <p>Company: ShenZhen BrightCom Technology Co.,Ltd.</p>
 *  @author��E40
 */
public class FriendManage {
private XmlDocPkgUtil xmlDocUtil = null;
    
    private static Log log4j = new Log(ClassManage.class.getName());
    
    public Document doPost(Document xmlDoc)
	{
    	xmlDocUtil = new XmlDocPkgUtil(xmlDoc);
    	String action = xmlDocUtil.getAction();
    	if("sendReqFriend".equals(action)){
    		sendReqFriend();
    	}
    	if("agreeFriendReq".equals(action)){
    		agreeFriendReq();
    	}
    	if("refuseFriendReq".equals(action)){
    		refuseFriendReq();
    	}
    	if("delFriend".equals(action)){
    		delFriend();
    	}
    	return xmlDoc;
	}
   /**
    * ���ͺ�������
    */
    private void sendReqFriend(){
    	Element reqData = xmlDocUtil.getRequestData();
    	PlatformDao pdao = new PlatformDao();
    	Element session = xmlDocUtil.getSession();
    	String curUserid = session.getChildText("userid");//��ǰ�û�ID
    	String curUsername = session.getChildText("username");//��ǰ�û�����
    	String curUsercode = session.getChildText("usercode");//��ǰ��¼�û��ʺ�
    	String friendId = reqData.getChildText("friendids");//ѡ�к����û�ID
    	String oldFriendId ="";
    	String friendStatus="";
    	try {
    		Element FriendElement = chkValid(curUserid, friendId);
    		if(FriendElement!=null){
	    		 oldFriendId = FriendElement.getChildText("friend_id");
	    		 friendStatus = FriendElement.getChildText("friend_status");
    		}
    		pdao.beginTransaction();
	    	if(StringUtil.isEmpty(friendId)){
	    		xmlDocUtil.writeErrorMsg("������ѡ��һλ����!");
	    		xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE); 
	    		return;
	    	}else if (friendId.equals(curUserid)){
	    		xmlDocUtil.writeErrorMsg("��������Լ�Ϊ����!");
	    		xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE); 
	    		return;
	    	} else if(friendId.equals(oldFriendId) && "Y".equals(friendStatus)){
				xmlDocUtil.writeErrorMsg("�Է�������ĺ����б���!");
				xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE); 
	    		return;
			} else if(friendId.equals(oldFriendId) && "N".equals(friendStatus)){
				xmlDocUtil.writeErrorMsg("�Է��ܾ���ĺ�������!");
				xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE); 
	    		return;
			} else if(friendId.equals(oldFriendId) && "S".equals(friendStatus) ){
				xmlDocUtil.writeErrorMsg("���ѷ��ͺ������룬�ȴ��Է���Ӧ!");
				xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE); 
	    		return;
			} else{
				//��Ӻ���������Ϣ
				Element reqMsgRecord = ConfigDocument.createRecordElement("yuexue", "base_message");
				XmlDocPkgUtil.copyValues(reqData,reqMsgRecord,0,true);
				xmlDocUtil.setChildText(reqMsgRecord, "fromuser_id", curUserid);
				xmlDocUtil.setChildText(reqMsgRecord, "touser_id", friendId);
				xmlDocUtil.setChildText(reqMsgRecord, "message", curUsername+"("+curUsercode+")�����Ϊ����");
				pdao.insertOneRecord(reqMsgRecord);
				
				//��Ӻ���
				Element friendRecord = ConfigDocument.createRecordElement("yuexue","base_friend");
				XmlDocPkgUtil.copyValues(reqData,friendRecord,0,true);
				xmlDocUtil.setChildText(friendRecord, "host_id", curUserid);
				xmlDocUtil.setChildText(friendRecord, "friend_id", friendId);
				pdao.insertOneRecord(friendRecord);
				pdao.commitTransaction();
				
				xmlDocUtil.setResult("0");
			}
	    	
    	} catch (Exception e) {
    		pdao.rollBack();
			// TODO Auto-generated catch block
			e.printStackTrace();
			xmlDocUtil.writeHint("����ʧ�ܣ����Ժ�����!");
		} finally{
			pdao.releaseConnection();
		}	
    	
    }
    /**
     * У���Ƿ��Ѿ��ں����б�
     * @param chkFld
     * @param chkVal
     * @return
     * @throws Exception 
     */
    protected Element chkValid(String userid,String friendids) throws Exception{
    	StringBuffer validSQL = new StringBuffer();
    	validSQL.append("select friend_id,friend_status from base_friend where host_id=? and friend_id = ? and del_flag='N'");
    	ArrayList bvals = new ArrayList();
    	bvals.add(userid);
    	bvals.add(friendids);
		
		Element rec = DaoUtil.getOneRecord(validSQL.toString(), bvals);
		
		return null == rec ? null : rec;	
    }
    /**
     * ͬ���������
     */
    private void agreeFriendReq(){
    	Element reqData = xmlDocUtil.getRequestData();
    	PlatformDao pdao = new PlatformDao();
    	Element session = xmlDocUtil.getSession();
    	String curUserid = session.getChildText("userid");//��ǰ�û�ID
    	String curUsername = session.getChildText("username");//��ǰ�û�����
    	String curUsercode = session.getChildText("usercode");//��ǰ��¼�û��ʺ�
    	String reqUserid = reqData.getChildText("userids");//���ͺ���������ID
    	String msgStatus = reqData.getChildText("msgstatus");//��Ϣ״̬ 0��ʾ"δ��" 1��ʾ"�Ѷ�"
    	StringBuffer reqSQL = new StringBuffer();
    	StringBuffer msgSQL = new StringBuffer();
    	ArrayList vals = new ArrayList();
    	try {
	    	pdao.beginTransaction();
	    	if("0".equals(msgStatus)){
		    	//��Ӻ���
				Element friendRecord = ConfigDocument.createRecordElement("yuexue","base_friend");
				XmlDocPkgUtil.copyValues(reqData,friendRecord,0,true);
				xmlDocUtil.setChildText(friendRecord, "host_id", curUserid);
				xmlDocUtil.setChildText(friendRecord, "friend_id", reqUserid);
				xmlDocUtil.setChildText(friendRecord, "friend_status", "Y");
				pdao.insertOneRecord(friendRecord);
				//�޸���Ϣ���¼
				msgSQL.append("update base_message set message_status='1' where touser_id=?");
				ArrayList paramentVals = new ArrayList();
				paramentVals.add(curUserid);
				pdao.setSql(msgSQL.toString());
				pdao.setBindValues(paramentVals);
				pdao.executeTransactionSql();
				
				//�޸ĺ����б�
				reqSQL.append("update base_friend set modify_date=NOW(),friend_status='Y' where host_id=? and friend_id=? and del_flag='N'");
				vals.add(reqUserid);
				vals.add(curUserid);
				pdao.setSql(reqSQL.toString());
				pdao.setBindValues(vals);
				pdao.executeTransactionSql();
				
				pdao.commitTransaction();
				xmlDocUtil.setResult("0");
	    	}else{
	    		xmlDocUtil.writeErrorMsg("�Է�������ĺ����б���!");
				xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE); 
	    		return;
	    	}
			
    	} catch (Exception e) {
    		pdao.rollBack();
			// TODO Auto-generated catch block
			e.printStackTrace();
			xmlDocUtil.writeHint("���ݳ�ʱ�����Ժ�����!");
		} finally{
			pdao.releaseConnection();
		}	
    }
    
    /**
     * �ܾ��Է���Ӻ���
     */
    public void refuseFriendReq(){
    	Element reqData = xmlDocUtil.getRequestData();
    	PlatformDao pdao = new PlatformDao();
    	Element session = xmlDocUtil.getSession();
    	String curUserid = session.getChildText("userid");//��ǰ�û�ID
    	String curUsername = session.getChildText("username");//��ǰ�û�����
    	String curUsercode = session.getChildText("usercode");//��ǰ��¼�û��ʺ�
    	String reqUserid = reqData.getChildText("userids");//���ͺ���������ID
    	ArrayList vals = new ArrayList();
    	try {
	    	StringBuffer friendSQL = new StringBuffer();
	    	friendSQL.append("update base_friend set friend_status='E' where host_id=? and friend_id=? and del_flag='N'");
	    	vals.add(reqUserid);
			vals.add(curUserid);
			pdao.setSql(friendSQL.toString());
			pdao.setBindValues(vals);
			pdao.executeTransactionSql();
			xmlDocUtil.setResult("0");
    	} catch (Exception e) {
    		pdao.rollBack();
			// TODO Auto-generated catch block
			e.printStackTrace();
			xmlDocUtil.writeHint("�ܾ���Ӻ���ʧ�ܣ����Ժ�����!");
		} finally{
			pdao.releaseConnection();
		}
    }
    
    /**
     * ���������
     */
    public void addBlackList(){
    	Element reqData = xmlDocUtil.getRequestData();
    	PlatformDao pdao = new PlatformDao();
    	Element session = xmlDocUtil.getSession();
    	String curUserid = session.getChildText("userid");//��ǰ�û�ID
    	String curUsername = session.getChildText("username");//��ǰ�û�����
    	String curUsercode = session.getChildText("usercode");//��ǰ��¼�û��ʺ�
    	String reqUserid = reqData.getChildText("userids");//���ͺ���������ID
    	ArrayList vals = new ArrayList();
    	try {
	    	StringBuffer blackSQL = new StringBuffer();
	    	blackSQL.append("update base_friend set friend_status='N' where host_id=? and friend_id=? and del_flag='N'");
	    	vals.add(curUserid);
			vals.add(reqUserid);
			pdao.setSql(blackSQL.toString());
			pdao.setBindValues(vals);
			pdao.executeTransactionSql();
			xmlDocUtil.setResult("0");
    	} catch (Exception e) {
    		pdao.rollBack();
			// TODO Auto-generated catch block
			e.printStackTrace();
			xmlDocUtil.writeHint("���������ʧ�ܣ����Ժ�����!");
		} finally{
			pdao.releaseConnection();
		}
    }
    
    /**
     * ɾ�������б�
     */
    public void delFriend(){
    	Element reqData = xmlDocUtil.getRequestData();
    	PlatformDao pdao = new PlatformDao();
    	Element session = xmlDocUtil.getSession();
    	String curUserid = session.getChildText("userid");//��ǰ�û�ID
    	String curUsername = session.getChildText("username");//��ǰ�û�����
    	String curUsercode = session.getChildText("usercode");//��ǰ��¼�û��ʺ�
    	String reqUserid = reqData.getChildText("userids");//���ͺ���������ID
    	ArrayList vals = new ArrayList();
    	ArrayList paramenVal = new ArrayList();
    	try {
	    	StringBuffer friendSQL = new StringBuffer();
	    	StringBuffer currSQL = new StringBuffer();
	    	pdao.beginTransaction();
	    	//�ӵ�ǰ�û������б��н�ѡ�к����Ƴ�
	    	currSQL.append("update base_friend set del_flag='Y' where host_id=? and friend_id=? and del_flag='N'");
	    	vals.add(curUserid);
			vals.add(reqUserid);
			pdao.setSql(currSQL.toString());
			pdao.setBindValues(vals);
			pdao.executeTransactionSql();
			//�ӶԷ������б��н���ǰ�û��Ƴ�
			friendSQL.append("update base_friend set del_flag='Y' where host_id=? and friend_id=? and del_flag='N'");
			paramenVal.add(reqUserid);
			paramenVal.add(curUserid);
			pdao.setSql(friendSQL.toString());
			pdao.setBindValues(vals);
			pdao.executeTransactionSql();
			pdao.commitTransaction();
			xmlDocUtil.setResult("0");
    	} catch (Exception e) {
    		pdao.rollBack();
			// TODO Auto-generated catch block
			e.printStackTrace();
			xmlDocUtil.writeHint("ɾ����ʧ�ܣ����Ժ�����!");
		} finally{
			pdao.releaseConnection();
		}
    }
}
