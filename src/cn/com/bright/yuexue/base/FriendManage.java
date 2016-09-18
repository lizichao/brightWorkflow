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
 * 好友管理
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2014 Brightcom</p>
 * <p>Company: ShenZhen BrightCom Technology Co.,Ltd.</p>
 *  @author　E40
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
    * 发送好友申请
    */
    private void sendReqFriend(){
    	Element reqData = xmlDocUtil.getRequestData();
    	PlatformDao pdao = new PlatformDao();
    	Element session = xmlDocUtil.getSession();
    	String curUserid = session.getChildText("userid");//当前用户ID
    	String curUsername = session.getChildText("username");//当前用户名称
    	String curUsercode = session.getChildText("usercode");//当前登录用户帐号
    	String friendId = reqData.getChildText("friendids");//选中好友用户ID
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
	    		xmlDocUtil.writeErrorMsg("请至少选择一位好友!");
	    		xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE); 
	    		return;
	    	}else if (friendId.equals(curUserid)){
	    		xmlDocUtil.writeErrorMsg("不能添加自己为好友!");
	    		xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE); 
	    		return;
	    	} else if(friendId.equals(oldFriendId) && "Y".equals(friendStatus)){
				xmlDocUtil.writeErrorMsg("对方已在你的好友列表中!");
				xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE); 
	    		return;
			} else if(friendId.equals(oldFriendId) && "N".equals(friendStatus)){
				xmlDocUtil.writeErrorMsg("对方拒绝你的好友申请!");
				xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE); 
	    		return;
			} else if(friendId.equals(oldFriendId) && "S".equals(friendStatus) ){
				xmlDocUtil.writeErrorMsg("您已发送好友申请，等待对方回应!");
				xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE); 
	    		return;
			} else{
				//添加好友申请消息
				Element reqMsgRecord = ConfigDocument.createRecordElement("yuexue", "base_message");
				XmlDocPkgUtil.copyValues(reqData,reqMsgRecord,0,true);
				xmlDocUtil.setChildText(reqMsgRecord, "fromuser_id", curUserid);
				xmlDocUtil.setChildText(reqMsgRecord, "touser_id", friendId);
				xmlDocUtil.setChildText(reqMsgRecord, "message", curUsername+"("+curUsercode+")添加您为好友");
				pdao.insertOneRecord(reqMsgRecord);
				
				//添加好友
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
			xmlDocUtil.writeHint("发送失败，请稍侯在试!");
		} finally{
			pdao.releaseConnection();
		}	
    	
    }
    /**
     * 校验是否已经在好友列表
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
     * 同意好友申请
     */
    private void agreeFriendReq(){
    	Element reqData = xmlDocUtil.getRequestData();
    	PlatformDao pdao = new PlatformDao();
    	Element session = xmlDocUtil.getSession();
    	String curUserid = session.getChildText("userid");//当前用户ID
    	String curUsername = session.getChildText("username");//当前用户名称
    	String curUsercode = session.getChildText("usercode");//当前登录用户帐号
    	String reqUserid = reqData.getChildText("userids");//发送好友申请人ID
    	String msgStatus = reqData.getChildText("msgstatus");//消息状态 0表示"未读" 1表示"已读"
    	StringBuffer reqSQL = new StringBuffer();
    	StringBuffer msgSQL = new StringBuffer();
    	ArrayList vals = new ArrayList();
    	try {
	    	pdao.beginTransaction();
	    	if("0".equals(msgStatus)){
		    	//添加好友
				Element friendRecord = ConfigDocument.createRecordElement("yuexue","base_friend");
				XmlDocPkgUtil.copyValues(reqData,friendRecord,0,true);
				xmlDocUtil.setChildText(friendRecord, "host_id", curUserid);
				xmlDocUtil.setChildText(friendRecord, "friend_id", reqUserid);
				xmlDocUtil.setChildText(friendRecord, "friend_status", "Y");
				pdao.insertOneRecord(friendRecord);
				//修改消息表记录
				msgSQL.append("update base_message set message_status='1' where touser_id=?");
				ArrayList paramentVals = new ArrayList();
				paramentVals.add(curUserid);
				pdao.setSql(msgSQL.toString());
				pdao.setBindValues(paramentVals);
				pdao.executeTransactionSql();
				
				//修改好友列表
				reqSQL.append("update base_friend set modify_date=NOW(),friend_status='Y' where host_id=? and friend_id=? and del_flag='N'");
				vals.add(reqUserid);
				vals.add(curUserid);
				pdao.setSql(reqSQL.toString());
				pdao.setBindValues(vals);
				pdao.executeTransactionSql();
				
				pdao.commitTransaction();
				xmlDocUtil.setResult("0");
	    	}else{
	    		xmlDocUtil.writeErrorMsg("对方已在你的好友列表中!");
				xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE); 
	    		return;
	    	}
			
    	} catch (Exception e) {
    		pdao.rollBack();
			// TODO Auto-generated catch block
			e.printStackTrace();
			xmlDocUtil.writeHint("数据超时，请稍侯在试!");
		} finally{
			pdao.releaseConnection();
		}	
    }
    
    /**
     * 拒绝对方添加好友
     */
    public void refuseFriendReq(){
    	Element reqData = xmlDocUtil.getRequestData();
    	PlatformDao pdao = new PlatformDao();
    	Element session = xmlDocUtil.getSession();
    	String curUserid = session.getChildText("userid");//当前用户ID
    	String curUsername = session.getChildText("username");//当前用户名称
    	String curUsercode = session.getChildText("usercode");//当前登录用户帐号
    	String reqUserid = reqData.getChildText("userids");//发送好友申请人ID
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
			xmlDocUtil.writeHint("拒绝添加好友失败，请稍侯在试!");
		} finally{
			pdao.releaseConnection();
		}
    }
    
    /**
     * 加入黑名单
     */
    public void addBlackList(){
    	Element reqData = xmlDocUtil.getRequestData();
    	PlatformDao pdao = new PlatformDao();
    	Element session = xmlDocUtil.getSession();
    	String curUserid = session.getChildText("userid");//当前用户ID
    	String curUsername = session.getChildText("username");//当前用户名称
    	String curUsercode = session.getChildText("usercode");//当前登录用户帐号
    	String reqUserid = reqData.getChildText("userids");//发送好友申请人ID
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
			xmlDocUtil.writeHint("加入黑名单失败，请稍侯在试!");
		} finally{
			pdao.releaseConnection();
		}
    }
    
    /**
     * 删除好友列表
     */
    public void delFriend(){
    	Element reqData = xmlDocUtil.getRequestData();
    	PlatformDao pdao = new PlatformDao();
    	Element session = xmlDocUtil.getSession();
    	String curUserid = session.getChildText("userid");//当前用户ID
    	String curUsername = session.getChildText("username");//当前用户名称
    	String curUsercode = session.getChildText("usercode");//当前登录用户帐号
    	String reqUserid = reqData.getChildText("userids");//发送好友申请人ID
    	ArrayList vals = new ArrayList();
    	ArrayList paramenVal = new ArrayList();
    	try {
	    	StringBuffer friendSQL = new StringBuffer();
	    	StringBuffer currSQL = new StringBuffer();
	    	pdao.beginTransaction();
	    	//从当前用户好友列表中将选中好友移除
	    	currSQL.append("update base_friend set del_flag='Y' where host_id=? and friend_id=? and del_flag='N'");
	    	vals.add(curUserid);
			vals.add(reqUserid);
			pdao.setSql(currSQL.toString());
			pdao.setBindValues(vals);
			pdao.executeTransactionSql();
			//从对方好友列表中将当前用户移除
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
			xmlDocUtil.writeHint("删除友失败，请稍侯在试!");
		} finally{
			pdao.releaseConnection();
		}
    }
}
