package cn.com.bright.yuexue.teach;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;

import cn.brightcom.jraf.auth.OnlineUser;
import cn.brightcom.jraf.auth.OnlineUserManager;
import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.util.ConfigDocument;
import cn.brightcom.jraf.util.Constants;
import cn.brightcom.jraf.util.Log;
import cn.brightcom.jraf.util.StringUtil;
import cn.brightcom.jraf.util.XmlDocPkgUtil;

/**
 * <p>Title:ѧϰС�����</p>
 * <p>Description: ѧϰС�����ά����</p>
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: BrightCom Technologies.</p>
 * @author zhangxq
 * @version 1.0
 * 
 * <p>Function List // ��Ҫ�������书��
 * ÿ����ʦ�����Խ��Լ��ν̵İ༶�ֳ�����С��
 *      
 * <p>History: // ��ʷ�޸ļ�¼:</p>
 * <p> author    time             version      desc</p>
 * <p> zhangxq    2014/07/25       1.0          build this moudle </p>
 *     
 */
public class LearningGroup {
	private XmlDocPkgUtil xmlDocUtil = null;
	private Log log4j = new Log(this.getClass().toString());

    /**
     * ��̬ί�����
     * @param request xmlDoc 
     * @return response xmlDoc
     */	
	public Document doPost(Document xmlDoc) {
		xmlDocUtil = new XmlDocPkgUtil(xmlDoc);
		String action = xmlDocUtil.getAction();
		if ("getGroupMember".equals(action)){
			getGroupMember();
		}
		else if ("getNoGroupStudent".equals(action)){
			getNoGroupStudent();
		}
		else if ("genGroupMemebr".equals(action)){
			genGroupMemebr();
		}
		else if ("saveGroupMember".equals(action)){
			saveGroupMember();
		}
		else if ("getGroupMemeberList".equals(action)){
			getGroupMemeberList();
		}
		else if ("getClassAllStudent".equals(action)){
			getClassAllStudent();
		}
		return xmlDoc;
	}
	/**
	 * ȡС���Ա�б�
	 *
	 */
	public void getGroupMemeberList(){	
		Element reqElement =  xmlDocUtil.getRequestData();
		String qry_group_id = reqElement.getChildText("qry_group_id");
		
		StringBuffer strSQL = new StringBuffer();	
		ArrayList<Object> paramList = new ArrayList<Object>();
		strSQL.append(" select t1.member_id,t1.userid,t2.studentno,t3.username,t3.portrait ");
		strSQL.append(" from learn_group_member t1,base_studentinfo t2,pcmc_user t3");
		strSQL.append(" where t1.valid='Y' and t2.state>'0' and t1.userid=t2.userid and t1.userid=t3.userid");
		strSQL.append(" and t1.group_id=?");
		paramList.add(qry_group_id);
		strSQL.append(" order by t2.studentno");
		
	    PlatformDao pdao = new PlatformDao();
	    try{
	    	pdao.setSql(strSQL.toString());
	    	pdao.setBindValues(paramList);
	    	Element result = pdao.executeQuerySql(0, -1);
	    	
	    	OnlineUserManager userMgr = OnlineUserManager.getInstance();
	    	List list = result.getChildren("Record");
	    	for (int i = 0; i < list.size() ;i++){
	    		Element memberRec = (Element)list.get(i);
	    		String userid = memberRec.getChildText("userid");
	    		OnlineUser user = userMgr.getOnlineUser(userid);
	    		if(null == user){
	    			XmlDocPkgUtil.setChildText(memberRec,"online","0");
	    		}
	    		else{
	    			XmlDocPkgUtil.setChildText(memberRec,"online","1");
	    		}	    			
	    	}	    	
	    	xmlDocUtil.getResponse().addContent(result);
	    	xmlDocUtil.setResult("0");
	    	
	    }catch (Exception e) {
			e.printStackTrace();
			log4j.logError("[�������-��ȡ�����Ա]"+e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
		} finally {
			pdao.releaseConnection();
		}
	}
	/**
	 * ��������Ա
	 *
	 */
	public void saveGroupMember(){		
		Element reqElement =  xmlDocUtil.getRequestData();
		String member_id = reqElement.getChildText("member_id");
		PlatformDao pdao = new PlatformDao();
		try{
			Element memberRec = ConfigDocument.createRecordElement("yuexue", "learn_group_member");
			XmlDocPkgUtil.copyValues(reqElement, memberRec, 0, true);
			if  (StringUtil.isNotEmpty(member_id)){
			    pdao.updateOneRecord(memberRec);
			}
			else{
				member_id = pdao.insertOneRecordSeqPk(memberRec).toString();
			}
			
			String[] returnData = { "member_id"};
			Element resData = XmlDocPkgUtil.createMetaData(returnData);	
			resData.addContent(XmlDocPkgUtil.createRecord(returnData,new String[] {member_id}));
			xmlDocUtil.getResponse().addContent(resData);
			
			xmlDocUtil.setResult("0");	
			xmlDocUtil.writeHintMsg("00109", "����ɹ�!");	
			
		}catch (Exception e) {			
			e.printStackTrace();
			log4j.logError("[�������-��������Ա]"+e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
		} finally {
			pdao.releaseConnection();
		}
	}
	/**
	 * ִ�з���
	 *
	 */
	public void genGroupMemebr(){
		String groupStr = "A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z";
		String[] groupArray = groupStr.split(",");
		
		String userid = xmlDocUtil.getSession().getChildText("userid");
		String username = xmlDocUtil.getSession().getChildText("username");
		
		Element reqElement =  xmlDocUtil.getRequestData();
		String classid = reqElement.getChildText("classid");
		String group_count = reqElement.getChildText("group_count");
		String grouping_type = reqElement.getChildText("grouping_type");		
		
		PlatformDao pdao = new PlatformDao();
		try{			
			int groupCount = Integer.parseInt(group_count);
			if (groupCount>26){
				xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
				xmlDocUtil.writeErrorMsg("20100", "�����������ܴ���26");
				return;
			}
			//ʧЧԭ������Ϣ
			StringBuffer disableSQL = new StringBuffer();
			disableSQL.append("update learn_group set valid='N',modify_by='"+username+"',modify_date=now()");
			disableSQL.append(" where valid='Y' and classid = ? and userid = ?");
			ArrayList<Object> disableParam = new ArrayList<Object>();
			disableParam.add(classid);
			disableParam.add(userid);
	    	pdao.setSql(disableSQL.toString());
	    	pdao.setBindValues(disableParam);
	    	pdao.executeTransactionSql();
	    	int groupMembers = 0;//С������
	    	int remainder = 0;//����
	    	if ("automatic".equals(grouping_type)){	    		
	    		String stuCountSQL=" select count(userid) as stu_count from base_studentinfo where state='1' and classid= ? ";
	    		ArrayList<Object> stuCountParam = new ArrayList<Object>();
	    		stuCountParam.add(classid);
	    		
		    	pdao.setSql(stuCountSQL);
		    	pdao.setBindValues(stuCountParam);
		    	Element stuCountResult = pdao.executeQuerySql(0, -1);
		    	int stuCount = Integer.parseInt(stuCountResult.getChild("Record").getChildText("stu_count"));
		    	if (stuCount==0){
					xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
					xmlDocUtil.writeErrorMsg("20101", "ѡ���༶ѧ������Ϊ0");
					return;		    		
		    	}
		    	if (stuCount<groupCount){
					xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
					xmlDocUtil.writeErrorMsg("20102", "ѡ���༶ѧ������С�ڷ�������");
					return;		    		
		    	}		    	
		    	groupMembers = stuCount/groupCount;
		    	remainder = stuCount%groupCount;
	    	}
	    	
	    	StringBuffer memberSQL = new StringBuffer();	    	
	    	memberSQL.append("select userid from base_studentinfo t");
	    	memberSQL.append(" where t.state>'0' and t.classid= ? ");
	    	memberSQL.append(" and not exists (select null from ");
	    	memberSQL.append(" learn_group st,learn_group_member sd");
	    	memberSQL.append(" where st.valid='Y' and sd.valid='Y'");
	    	memberSQL.append(" and st.classid=t.classid and st.group_id=sd.group_id");
	    	memberSQL.append(" and st.userid='"+userid+"'");
	    	memberSQL.append(" and sd.userid=t.userid)");
	    	memberSQL.append(" order by rand() limit ");
	    	
    		ArrayList<Object> memberParam = new ArrayList<Object>();
    		memberParam.add(classid);
    		
	    	for (int i=0;i<groupCount;i++){
	    		String group_name = groupArray[i]+"��";
	    		Element groupRec = ConfigDocument.createRecordElement("yuexue", "learn_group");
	    		XmlDocPkgUtil.copyValues(reqElement, groupRec, 0, true);
	    		XmlDocPkgUtil.setChildText(groupRec, "group_name", group_name);
	    		XmlDocPkgUtil.setChildText(groupRec, "group_status", "10");
	    		
	    		String group_id= pdao.insertOneRecordSeqPk(groupRec).toString();
	    		
	    		if ("automatic".equals(grouping_type)){	
		    		if (i<remainder){
			    	    pdao.setSql(memberSQL.toString()+(groupMembers+1));
		    		}
		    		else{
		    			pdao.setSql(memberSQL.toString()+groupMembers);
		    		}
			    	pdao.setBindValues(memberParam);
			    	Element memberResult = pdao.executeQuerySql(0, -1);
			    	
			    	List list = memberResult.getChildren("Record");
			    	for (int j=0;j<list.size();j++){
			    		Element userRec = (Element)list.get(j);
			    		
			    		Element memberRec = ConfigDocument.createRecordElement("yuexue", "learn_group_member");
			    		XmlDocPkgUtil.copyValues(reqElement, memberRec, 0, true);
			    		XmlDocPkgUtil.setChildText(memberRec, "group_id", group_id);
			    		XmlDocPkgUtil.setChildText(memberRec, "userid", userRec.getChildText("userid"));
			    		if (j==0){
			    			XmlDocPkgUtil.setChildText(memberRec, "group_manager", "Y");
			    		}
			    		pdao.insertOneRecordSeqPk(memberRec);
			    	}
	    		}
	    	}
	    	
			xmlDocUtil.setResult("0");
			xmlDocUtil.writeHintMsg("20102", "����ɹ�");
		}catch (Exception e) {			
			e.printStackTrace();
			log4j.logError("[�������-���·���]"+e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
		} finally {
			pdao.releaseConnection();
		}
		
	}
	private void getClassAllStudent(){
		Element reqElement =  xmlDocUtil.getRequestData();
		String classid = reqElement.getChildText("classid");
		String userid = reqElement.getChildText("userid");
		String qryText = reqElement.getChildText("qry_text");

		StringBuffer noGroupSQL = new StringBuffer();		
		noGroupSQL.append("select t2.userid as member_id,t2.userid,t1.studentno,t2.username,t2.portrait,t2.gender from base_studentinfo t1,pcmc_user t2");
		noGroupSQL.append(" where t1.state='1' and t1.userid=t2.userid");	
		noGroupSQL.append(" and not exists");	
		noGroupSQL.append(" (select null from learn_group st,learn_group_member sd");	
		noGroupSQL.append(" where st.valid='Y' and sd.valid='Y' and st.group_id=sd.group_id");
		noGroupSQL.append(" and sd.userid=t2.userid and st.classid=t1.classid");
		noGroupSQL.append(" and st.userid  = ? )");
		noGroupSQL.append(" and t1.classid = ? ");
		if (StringUtil.isNotEmpty(qryText)){
			noGroupSQL.append(" and t2.username like ?");
		}
		noGroupSQL.append(" order by t1.studentno");		
		
		StringBuffer strSQL = new StringBuffer();		
		strSQL.append("select t1.group_id,t1.group_name,t1.group_subject,t1.classid,(select count(member_id) from learn_group_member st");
		strSQL.append("     where st.valid='Y' and st.group_id=t1.group_id) as member_count");
		strSQL.append(" from learn_group t1");
		strSQL.append(" where t1.valid='Y' and t1.userid=? and t1.classid=?");

		ArrayList<Object> paramList = new ArrayList<Object>();
		paramList.add(userid);
		paramList.add(classid);
		if (StringUtil.isNotEmpty(qryText)){
			paramList.add("%"+qryText+"%");
		}
		
		ArrayList<Object> groupParam = new ArrayList<Object>();
		groupParam.add(userid);
		groupParam.add(classid);		
		

	    PlatformDao pdao = new PlatformDao();
	    try{
	    	pdao.setSql(strSQL.toString());
	    	pdao.setBindValues(groupParam);
	    	Element result = pdao.executeQuerySql(0, -1);
	    	
	    	List list = result.getChildren("Record");
	    	StringBuffer memberSQL = new StringBuffer();
	    	memberSQL.append("select t1.member_id,t1.userid,t3.studentno,t2.username,t2.portrait,t2.gender");
	    	memberSQL.append(" from learn_group_member t1,pcmc_user t2,base_studentinfo t3");
	    	memberSQL.append(" where t1.valid='Y' and t3.state>'0' and t1.userid=t2.userid");
	    	memberSQL.append(" and t2.userid=t3.userid and t1.group_id=?");
	    	if (StringUtil.isNotEmpty(qryText)){
	    		memberSQL.append(" and t2.username like ?");
	    	}
	    	memberSQL.append(" order by t1.group_manager desc,t3.studentno");
	    	
	    	String group_subject ="";
	    	for (int i = 0; i < list.size() ;i++){
	    		Element groupRec = (Element)list.get(i);
	    		String group_id = groupRec.getChildText("group_id");
	    		if (i==0){
	    			group_subject = groupRec.getChildText("group_subject");
	    		}
	    		ArrayList<Object> memberParam = new ArrayList<Object>();
	    		memberParam.add(group_id);
	    		if (StringUtil.isNotEmpty(qryText)){
	    			memberParam.add("%"+qryText+"%");
	    		}
	    		pdao.setSql(memberSQL.toString());
	    		pdao.setBindValues(memberParam);
	    		Element memberResult = pdao.executeQuerySql(0,-1);
	    		Element members = new Element("Members");
	    		members.addContent(memberResult);
	    		groupRec.addContent(members);	    		
	    	}
	    	
	    	pdao.setSql(noGroupSQL.toString());
	    	pdao.setBindValues(paramList);
	    	
	    	Element noGroupResult = pdao.executeQuerySql(0, -1);
	    	List noGroupList = noGroupResult.getChildren("Record");
	        if (noGroupList.size()>0){
	    	   Element noGroupRec = new Element("Record");
	    	   XmlDocPkgUtil.setChildText(noGroupRec, "group_id", "0");
	    	   XmlDocPkgUtil.setChildText(noGroupRec, "group_name", "δ����ѧ��");
	    	   XmlDocPkgUtil.setChildText(noGroupRec, "group_subject", group_subject);
	    	   XmlDocPkgUtil.setChildText(noGroupRec, "classid",classid);
	    	   
	    	   Element members = new Element("Members");
	    	   members.addContent(noGroupResult);
	    	   noGroupRec.addContent(members);	
	    	   result.addContent(noGroupRec);
	        }
	    	
	    	xmlDocUtil.getResponse().addContent(result);
	    	xmlDocUtil.setResult("0");
	    	
	    }catch (Exception e) {
			e.printStackTrace();
			log4j.logError("[�������-��ȡ�༶����ѧ����Ϣ]"+e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
		} finally {
			pdao.releaseConnection();
		}		
	}
	/**
	 * ��ȡ������Ϣ(��С���Ա)
	 *
	 */
	public void getGroupMember(){
		Element reqElement =  xmlDocUtil.getRequestData();
		String classid = reqElement.getChildText("classid");
		String userid = reqElement.getChildText("userid");
		
		StringBuffer strSQL = new StringBuffer();		
		strSQL.append("select t1.*,(select count(member_id) from learn_group_member st");
		strSQL.append("     where st.valid='Y' and st.group_id=t1.group_id) as member_count");
		strSQL.append(" from learn_group t1");
		strSQL.append(" where t1.valid='Y' and t1.userid=? and t1.classid=?");
		
		ArrayList<Object> paramList = new ArrayList<Object>();
		paramList.add(userid);
		paramList.add(classid);
		
	    PlatformDao pdao = new PlatformDao();
	    try{
	    	pdao.setSql(strSQL.toString());
	    	pdao.setBindValues(paramList);
	    	Element result = pdao.executeQuerySql(0, -1);
	    	
	    	List list = result.getChildren("Record");
	    	StringBuffer memberSQL = new StringBuffer();
	    	memberSQL.append("select t1.*,t3.studentno,t2.username,t2.portrait,t2.gender");
	    	memberSQL.append(" from learn_group_member t1,pcmc_user t2,base_studentinfo t3");
	    	memberSQL.append(" where t1.valid='Y' and t3.state>'0' and t1.userid=t2.userid");
	    	memberSQL.append(" and t2.userid=t3.userid and t1.group_id=?");
	    	memberSQL.append(" order by t1.group_manager desc,t3.studentno");
	    	for (int i = 0; i < list.size() ;i++){
	    		Element groupRec = (Element)list.get(i);
	    		String group_id = groupRec.getChildText("group_id");
	    		
	    		ArrayList<Object> memberParam = new ArrayList<Object>();
	    		memberParam.add(group_id);
	    		
	    		pdao.setSql(memberSQL.toString());
	    		pdao.setBindValues(memberParam);
	    		Element memberResult = pdao.executeQuerySql(0,-1);
	    		Element members = new Element("Members");
	    		members.addContent(memberResult);
	    		groupRec.addContent(members);  		
	    		
	    	}
	    	
	    	xmlDocUtil.getResponse().addContent(result);
	    	xmlDocUtil.setResult("0");
	    	
	    }catch (Exception e) {
			e.printStackTrace();
			log4j.logError("[�������-��ȡ������Ϣ]"+e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
		} finally {
			pdao.releaseConnection();
		}		
	}
	/**
	 * ��ȡδ����ѧ��
	 *
	 */
	public void getNoGroupStudent(){
		Element reqElement =  xmlDocUtil.getRequestData();
		
		String classid = reqElement.getChildText("classid");
		String userid = reqElement.getChildText("userid");
		
		StringBuffer strSQL = new StringBuffer();		
		strSQL.append("select t2.userid,t1.studentno,t2.username,t2.portrait from base_studentinfo t1,pcmc_user t2");
		strSQL.append(" where t1.state='1' and t1.userid=t2.userid");	
		strSQL.append(" and not exists");	
		strSQL.append(" (select null from learn_group st,learn_group_member sd");	
		strSQL.append(" where st.valid='Y' and sd.valid='Y' and st.group_id=sd.group_id");
		strSQL.append(" and sd.userid=t2.userid and st.classid=t1.classid");
		strSQL.append(" and st.userid  = ? )");
		strSQL.append(" and t1.classid = ? ");
		strSQL.append(" order by t1.studentno");
		
		ArrayList<Object> paramList = new ArrayList<Object>();
		paramList.add(userid);
		paramList.add(classid);
		
	    PlatformDao pdao = new PlatformDao();
	    try{
	    	pdao.setSql(strSQL.toString());
	    	pdao.setBindValues(paramList);
	    	Element result = pdao.executeQuerySql(0, -1);
	    	
	    	xmlDocUtil.getResponse().addContent(result);
	    	xmlDocUtil.setResult("0");
	    	
	    }catch (Exception e) {
			e.printStackTrace();
			log4j.logError("[�������-��ȡδ����ѧ��]"+e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
		} finally {
			pdao.releaseConnection();
		}		
	}
}
