package cn.com.bright.yuexue.teach;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.jdom.Document;
import org.jdom.Element;

import cn.brightcom.jraf.conf.BrightComConfig;
import cn.brightcom.jraf.context.ApplicationContext;
import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.util.ConfigDocument;
import cn.brightcom.jraf.util.Constants;
import cn.brightcom.jraf.util.Log;
import cn.brightcom.jraf.util.StringUtil;
import cn.brightcom.jraf.util.XmlDocPkgUtil;
import cn.com.bright.edu.weixin.core.WeiXinTemplate;
import cn.com.bright.yuexue.exception.NotFoundDataException;
import cn.com.bright.yuexue.util.HttpWebWeiXinMessage;
import cn.com.bright.yuexue.util.XmlResultUtil;

/**
 * <p>Title:�Ծ��͹���</p>
 * <p>Description: �Ծ�(���͹���ά����</p>
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: BrightCom Technologies.</p>
 * @author zhangxq
 * @version 1.0
 * 
 * <p>Function List // ��Ҫ�������书��
 * 
 *      
 * <p>History: // ��ʷ�޸ļ�¼:</p>
 * <p> author    time             version      desc</p>
 * <p> zhangxq    2014/08/07       1.0          build this moudle </p>
 *     
 */
public class PaperSender {
	
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
		
		if ("addPaperSend".equals(action)){
			addPaperSend();
		}
		else if ("sendPaper".equals(action)){
			sendPaper();
		}
		else if ("resendPaper".equals(action)){
			resendPaper();
		}
		else if ("getReceiverMOOC".equals(action)){
			getReceiverMOOC();
		}
		else if ("revokeSendPaper".equals(action)){
			revokeSendPaper();
		}		
		else if ("getReceiver".equals(action)){
			getReceiver();
		}else if ("getSendPaperLog".equals(action)){
			getSendPaperLog();
		}
		
		return xmlDoc;
	}
	/**
	 * ��������
	 *
	 */
	public void revokeSendPaper(){
		String username   = xmlDocUtil.getSession().getChildTextTrim("username");
		Element reqElement =  xmlDocUtil.getRequestData();
		String send_id = reqElement.getChildText("send_id");		
		
		ArrayList<Object> paramList = new ArrayList<Object>();
		paramList.add(send_id);
		
		StringBuffer myExamSQL = new StringBuffer();
		myExamSQL.append("update learn_my_examination set valid='N',modify_by='"+username+"',modify_date=now()");
		myExamSQL.append(" where send_id=?");
		
		StringBuffer sendSQL = new StringBuffer();
		sendSQL.append("update learn_paper_send set valid='N',modify_by='"+username+"',modify_date=now()");
		sendSQL.append(" where send_id=?");

	    PlatformDao pdao = new PlatformDao();
	    try{
	    	pdao.beginTransaction();
	    	
	    	pdao.setSql(myExamSQL.toString());
	    	pdao.setBindValues(paramList);
	    	pdao.executeTransactionSql();
	    	
	    	pdao.setSql(sendSQL.toString());
	    	pdao.setBindValues(paramList);
	    	pdao.executeTransactionSql();
	    	
	    	pdao.commitTransaction();
	    	
	    	xmlDocUtil.setResult("0");
	    	xmlDocUtil.writeHintMsg("20203", "�����ɹ�!");
	    }catch (Exception e) {
	    	pdao.rollBack();
			e.printStackTrace();
			log4j.logError("[�Ծ���-�����Ծ���]"+e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
		} finally {
			pdao.releaseConnection();
		}		
	}
	/**
	 * ȡ���ն���
	 *
	 */
	public void getReceiver(){
		String userid   = xmlDocUtil.getSession().getChildTextTrim("userid");
		Element reqElement =  xmlDocUtil.getRequestData();
		//String pid= reqElement.getChildText("pid");
		String searchText= reqElement.getChildText("searchText");
		//System.out.println("searchText======="+searchText);
		StringBuffer strClassSQL = new StringBuffer();
		ArrayList<Object> classParam = new ArrayList<Object>();		
		//ȡ��ʦ�ν̰༶
		strClassSQL.append("select distinct Concat('C_',t1.classid) as id,'-1' as pid,t2.classnm as receiver_name,1 as childcount,1 as receiver_level");
		strClassSQL.append(" from base_teacher_subject t1,base_class t2");
		strClassSQL.append(" where t1.state>'0' and t1.classid=t2.classid and t2.state>'0'");
		strClassSQL.append(" and t1.deptid=t2.deptid and t1.userid=?");
		classParam.add(userid);
		if (StringUtil.isNotEmpty(searchText)){
			if (StringUtil.isInteger(searchText)){//�������֣�������Ӧ�İ༶
				strClassSQL.append(" and t2.classnm like ?");
				classParam.add("%"+searchText+"%");
			}
			else if (StringUtil.isChinese(searchText)){//���뺺�֣�����ѧ�����ڵİ༶
				strClassSQL.append(" and exists (select null from base_studentinfo st,pcmc_user sd");
				strClassSQL.append(" where st.state='1' and st.userid=sd.userid and st.classid=t1.classid");
				strClassSQL.append(" and  sd.username like ? )");
				classParam.add(searchText+"%");
			}
			else if (StringUtil.isLetter(searchText)){//������ĸ,����С�����ڵİ༶				
				strClassSQL.append(" and exists ( select null from learn_group st where st.valid='Y' ");
				strClassSQL.append(" and st.classid=t1.classid and st.userid=t1.userid ");
				strClassSQL.append(" and st.group_name like ? )");
				classParam.add(searchText+"%");
			}
			else{//����ַ�����һλ���ֶ�Ӧ�İ༶
				String firstChar = searchText.substring(0, 1);
				if (StringUtil.isInteger(firstChar)){
					strClassSQL.append(" and t2.classnm like ?");
					classParam.add("%"+firstChar+"%");					
				}
			}
		}
		strClassSQL.append(" and exists");
		strClassSQL.append(" (select null from base_studentinfo st where st.state='1' and st.classid=t1.classid)");
		
		
        //ȡ���µ����û�з��䵽���ѧ��
		StringBuffer strGroupSQL = new StringBuffer();
		ArrayList<Object> groupParam = new ArrayList<Object>();	
		strGroupSQL.append(" select Concat('G_',t1.group_id) as id,t1.classid as pid,");
		strGroupSQL.append(" Concat(t2.classnm,t1.group_name) as receiver_name,");
		strGroupSQL.append(" (select count(st.member_id) from learn_group_member st where st.valid='Y' and st.group_id=t1.group_id) as childcount,2 as receiver_level");
		strGroupSQL.append(" from learn_group t1,base_class t2,base_grade t5");
		strGroupSQL.append(" where valid='Y' and t1.classid=t2.classid and t2.state>'0'");
		strGroupSQL.append(" and t2.deptid = t5.deptid and t2.gradecode=t5.gradecode");
		strGroupSQL.append(" and t1.userid=?");
		groupParam.add(userid);
		if (StringUtil.isNotEmpty(searchText)){
			if (StringUtil.isInteger(searchText)){//�������֣�������Ӧ��С��
				strGroupSQL.append(" and t2.classnm like ?");
				groupParam.add("%"+searchText+"%");				
			}
			else if (StringUtil.isLetter(searchText)){//�����ַ���������Ӧ��С��
				strGroupSQL.append(" and t1.group_name like ?");
				groupParam.add(searchText+"%");				
			}
			else if (StringUtil.isChinese(searchText)){//���뺺�֣�����ѧ�����ڵ�С��
				strGroupSQL.append(" and exists (select null from learn_group_member st,pcmc_user sd");
				strGroupSQL.append(" where st.valid='Y' and st.group_id=t1.group_id and st.userid=sd.userid");
				strGroupSQL.append(" and sd.username like ? )");
				groupParam.add(searchText+"%");
			}
			else{//����ַ�����ѯ�ڶ�λ��ͷ���� ����1A��ʾ��ѯ1���A��
				String firstChar = searchText.substring(0, 1);
				if (StringUtil.isInteger(firstChar)){
					strGroupSQL.append(" and t2.classnm like ?");
					groupParam.add("%"+firstChar+"%");					
				}				
				String secondChar = searchText.substring(1);				
				strGroupSQL.append(" and t1.group_name like ?");
				groupParam.add(secondChar+"%");					
			}
		}
		//���˵�С�����ʾ
		strGroupSQL.append(" and exists (select null from learn_group_member st");
		strGroupSQL.append(" where st.valid='Y' and st.group_id=t1.group_id)");
		
		//strGroupSQL.append(" union");
		StringBuffer strNoGroupSQL = new StringBuffer();
		ArrayList<Object> noGroupParam = new ArrayList<Object>();			
		strNoGroupSQL.append(" select Concat('U_',t1.userid) as id,t1.classid as pid,");
		strNoGroupSQL.append(" t2.username as receiver_name,0 as childcount,2 as receiver_level");
		strNoGroupSQL.append(" from base_studentinfo t1,pcmc_user t2,base_teacher_subject t3,base_class t4");
		strNoGroupSQL.append(" where t1.state='1' and t1.userid=t2.userid and t1.classid=t4.classid");
		strNoGroupSQL.append(" and t3.state>'0' and t3.classid=t1.classid and t3.userid=?");
		noGroupParam.add(userid);
		if (StringUtil.isNotEmpty(searchText)){
			if (StringUtil.isInteger(searchText)){//�������֣�������Ӧ�༶��δ�������Ա
				strNoGroupSQL.append(" and t4.classnm like ?");
				noGroupParam.add("%"+searchText+"%");
			}
			else if (StringUtil.isLetter(searchText)){//������ĸ����ѯ�����ڰ༶��δ�������Ա
				strNoGroupSQL.append(" and exists ( select null from learn_group st where st.valid='Y' ");
				strNoGroupSQL.append(" and st.classid=t1.classid");
				strNoGroupSQL.append(" and st.group_name like ? )");
				noGroupParam.add(searchText+"%");				
			}
			else if (StringUtil.isChinese(searchText)){//���뺺��
				strNoGroupSQL.append(" and t2.username like ?");
				noGroupParam.add(searchText+"%");				
			}
			else{//����ַ�
				String firstChar = searchText.substring(0, 1);
				if (StringUtil.isInteger(firstChar)){
					strNoGroupSQL.append(" and t4.classnm like ?");
					noGroupParam.add("%"+firstChar+"%");					
				}				
				String secondChar = searchText.substring(1);				
				strNoGroupSQL.append(" and t2.username like ?");
				noGroupParam.add(secondChar+"%");					
			}
		}
		strNoGroupSQL.append(" and not exists");
		strNoGroupSQL.append(" (select null from learn_group_member st");
		strNoGroupSQL.append(" where st.valid='Y' and st.userid=t1.userid)");			
        
		//ȡ�������ѧ��
		StringBuffer strMemberSQL = new StringBuffer();
		ArrayList<Object> memberParam = new ArrayList<Object>();	
		strMemberSQL.append(" select Concat('U_',t1.userid) as id,t2.username as receiver_name,0 as childcount,3 as receiver_level");
		strMemberSQL.append(" from learn_group_member t1,pcmc_user t2,base_teacher_subject t3,base_class t4,learn_group t5");
		strMemberSQL.append(" where t1.valid='Y' and t1.userid=t2.userid and t1.group_id=t5.group_id");
		strMemberSQL.append(" and t5.classid=t4.classid and t4.classid=t3.classid and t3.userid=t5.userid and t3.userid=?");
		memberParam.add(userid);
		if (StringUtil.isNotEmpty(searchText)){//ȡ���������Ա
			if (StringUtil.isInteger(searchText)){//�������֣�������Ӧ�༶��δ�������Ա
				strMemberSQL.append(" and t4.classnm like ?");
				memberParam.add("%"+searchText+"%");
			}
			else if (StringUtil.isLetter(searchText)){//�����ַ���������Ӧ��С��
				strMemberSQL.append(" and t5.group_name like ?");
				memberParam.add(searchText+"%");				
			}
			else if (StringUtil.isChinese(searchText)){//���뺺��
				strMemberSQL.append(" and t2.username like ?");
				memberParam.add(searchText+"%");				
			}
			else{//����ַ� 1A��ѯ1����A�����Ա
				String firstChar = searchText.substring(0, 1);
				if (StringUtil.isInteger(firstChar)){
					strMemberSQL.append(" and t4.classnm like ?");
					memberParam.add("%"+firstChar+"%");					
				}				
				String secondChar = searchText.substring(1);				
				strMemberSQL.append(" and t5.group_name like ?");
				memberParam.add(secondChar+"%");					
			}
		}		
		
		PlatformDao pdao = new PlatformDao();
	    try{
	    	
	    	pdao.setSql(strClassSQL.toString());
	    	pdao.setBindValues(classParam);
	    	Element result = pdao.executeQuerySql(0,-1);
	    	
	    	Element data = new Element("Data");	    		    		
    		List classList = result.getChildren("Record");
    		int classCount =  classList.size();
    		for (int i=0;i<classCount;i++){
    			Element classRec = (Element)classList.get(i);
    			String id = classRec.getChildText("id");
    			String classid = id.substring(2);
    			
    			XmlResultUtil.addRecordToData(data,classRec);//���Ӱ༶

				String strSQL = strGroupSQL.toString()+" and t1.classid='"+classid+"'";
				pdao.setSql(strSQL);
				pdao.setBindValues(groupParam);
				
				Element groupResult = pdao.executeQuerySql(0, -1);
				List groupList = groupResult.getChildren("Record");
				//�������������ĸ����ʾ�༶��С��
				if (StringUtil.isEmpty(searchText) || StringUtil.isLetter(searchText)){
					XmlResultUtil.addListToData(data,groupList);//����С��
				}
				else{
					if (StringUtil.isInteger(searchText)){//��������,ȡ��Ӧ�༶�µ����δ�������
						XmlResultUtil.addListToData(data,groupList);//����С��
						
	    				strSQL = strNoGroupSQL.toString()+" and t4.classid='"+classid+"'";
	    				pdao.setSql(strSQL);
	    				pdao.setBindValues(noGroupParam);
	    				
	    				Element noGroupResult = pdao.executeQuerySql(0, -1);
	    				List noGroupList = noGroupResult.getChildren("Record");
	    				XmlResultUtil.addListToData(data,noGroupList);						
					}
					else if (StringUtil.isChinese(searchText)){//�������� ������������ѯ�������ڵİ࣬�飬δ������Ա�ͷ�����Ա
						for (int j=0;j<groupList.size();j++){
	    					Element groupRec = (Element)groupList.get(j);
	    	    			String _groupid = groupRec.getChildText("id");
	    	    			String groupid = _groupid.substring(2);	
	    	    			
	    	    			XmlResultUtil.addRecordToData(data,groupRec); 
	    	    			
		    				strSQL = strMemberSQL.toString()+" and t1.group_id='"+groupid+"'";
		    				pdao.setSql(strSQL);
		    				pdao.setBindValues(memberParam);
		    				
		    				Element memberResult = pdao.executeQuerySql(0, -1);
		    				List memberList = memberResult.getChildren("Record");
		    				XmlResultUtil.addListToData(data,memberList);		    	    			
	    				}
	    				
	    				strSQL = strNoGroupSQL.toString()+" and t4.classid='"+classid+"'";
	    				pdao.setSql(strSQL);
	    				pdao.setBindValues(noGroupParam);
	    				
	    				Element noGroupResult = pdao.executeQuerySql(0, -1);
	    				List noGroupList = noGroupResult.getChildren("Record");
	    				XmlResultUtil.addListToData(data,noGroupList);							
					}
					else{//�������ַ� ����1A��ȡ1��A�����
						XmlResultUtil.addListToData(data,groupList);	
	    				
	    				strSQL = strMemberSQL.toString()+" and t4.classid='"+classid+"'";
	    				pdao.setSql(strSQL);
	    				pdao.setBindValues(memberParam);
	    				
	    				Element memberResult = pdao.executeQuerySql(0, -1);
	    				List memberList = memberResult.getChildren("Record");
	    				XmlResultUtil.addListToData(data,memberList);							
					}
				}    			
    		}
	    	
	    	xmlDocUtil.getResponse().addContent(data);
	    	xmlDocUtil.setResult("0");
	    	
	    }catch (Exception e) {
			e.printStackTrace();
			log4j.logError("[�Ծ��͹���-ȡ���ն���]"+e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
		} finally {
			pdao.releaseConnection();
		}
	}
	
	/**
	 * ȡ���ն���
	 *
	 */
	public void getReceiverMOOC(){
		String userid   = xmlDocUtil.getSession().getChildTextTrim("userid");
		Element reqElement =  xmlDocUtil.getRequestData();
		String searchText= reqElement.getChildText("searchText");
		StringBuffer strClassSQL = new StringBuffer();
		ArrayList<Object> classParam = new ArrayList<Object>();		
		//ȡ��ʦ�ν̰༶
		strClassSQL.append("select distinct Concat('C_',t1.classid) as id,'-1' as pid,t2.classnm as receiver_name,1 as childcount,1 as receiver_level");
		strClassSQL.append(" from base_teacher_subject t1,base_class t2");
		strClassSQL.append(" where t1.state>'0' and t1.classid=t2.classid and t2.state>'0'");
		strClassSQL.append(" and t1.deptid=t2.deptid and t1.userid=?");
		classParam.add(userid);
		if (StringUtil.isNotEmpty(searchText)){
			if (StringUtil.isInteger(searchText)){//�������֣�������Ӧ�İ༶
				strClassSQL.append(" and t2.classnm like ?");
				classParam.add("%"+searchText+"%");
			}
			else if (StringUtil.isChinese(searchText)){//���뺺�֣�����ѧ�����ڵİ༶
				strClassSQL.append(" and exists (select null from base_studentinfo st,pcmc_user sd");
				strClassSQL.append(" where st.state='1' and st.userid=sd.userid and st.classid=t1.classid");
				strClassSQL.append(" and  sd.username like ? )");
				classParam.add(searchText+"%");
			}
			/*else if (StringUtil.isLetter(searchText)){//������ĸ,����С�����ڵİ༶				
				strClassSQL.append(" and exists ( select null from learn_group st where st.valid='Y' ");
				strClassSQL.append(" and st.classid=t1.classid and st.userid=t1.userid ");
				strClassSQL.append(" and st.group_name like ? )");
				classParam.add(searchText+"%");
			}*/
			else{//����ַ�����һλ���ֶ�Ӧ�İ༶
				String firstChar = searchText.substring(0, 1);
				if (StringUtil.isInteger(firstChar)){
					strClassSQL.append(" and t2.classnm like ?");
					classParam.add("%"+firstChar+"%");					
				}
			}
		}
		strClassSQL.append(" and exists");
		strClassSQL.append(" (select null from base_studentinfo st where st.state='1' and st.classid=t1.classid)");
		
		
        //ȡ���µ����û�з��䵽���ѧ��
		/*StringBuffer strGroupSQL = new StringBuffer();
		ArrayList<Object> groupParam = new ArrayList<Object>();	
		strGroupSQL.append(" select Concat('G_',t1.group_id) as id,t1.classid as pid,");
		strGroupSQL.append(" Concat(t2.classnm,t1.group_name) as receiver_name,");
		strGroupSQL.append(" (select count(st.member_id) from learn_group_member st where st.valid='Y' and st.group_id=t1.group_id) as childcount,2 as receiver_level");
		strGroupSQL.append(" from learn_group t1,base_class t2,base_grade t5");
		strGroupSQL.append(" where valid='Y' and t1.classid=t2.classid and t2.state>'0'");
		strGroupSQL.append(" and t2.deptid = t5.deptid and t2.gradecode=t5.gradecode");
		strGroupSQL.append(" and t1.userid=?");
		groupParam.add(userid);
		if (StringUtil.isNotEmpty(searchText)){
			if (StringUtil.isInteger(searchText)){//�������֣�������Ӧ��С��
				strGroupSQL.append(" and t2.classnm like ?");
				groupParam.add("%"+searchText+"%");				
			}
			else if (StringUtil.isLetter(searchText)){//�����ַ���������Ӧ��С��
				strGroupSQL.append(" and t1.group_name like ?");
				groupParam.add(searchText+"%");				
			}
			else if (StringUtil.isChinese(searchText)){//���뺺�֣�����ѧ�����ڵ�С��
				strGroupSQL.append(" and exists (select null from learn_group_member st,pcmc_user sd");
				strGroupSQL.append(" where st.valid='Y' and st.group_id=t1.group_id and st.userid=sd.userid");
				strGroupSQL.append(" and sd.username like ? )");
				groupParam.add(searchText+"%");
			}
			else{//����ַ�����ѯ�ڶ�λ��ͷ���� ����1A��ʾ��ѯ1���A��
				String firstChar = searchText.substring(0, 1);
				if (StringUtil.isInteger(firstChar)){
					strGroupSQL.append(" and t2.classnm like ?");
					groupParam.add("%"+firstChar+"%");					
				}				
				String secondChar = searchText.substring(1);				
				strGroupSQL.append(" and t1.group_name like ?");
				groupParam.add(secondChar+"%");					
			}
		}
		//���˵�С�����ʾ
		strGroupSQL.append(" and exists (select null from learn_group_member st");
		strGroupSQL.append(" where st.valid='Y' and st.group_id=t1.group_id)");*/
		
		//strGroupSQL.append(" union");
		/*StringBuffer strNoGroupSQL = new StringBuffer();
		ArrayList<Object> noGroupParam = new ArrayList<Object>();			
		strNoGroupSQL.append(" select Concat('U_',t1.userid) as id,t1.classid as pid,");
		strNoGroupSQL.append(" t2.username as receiver_name,0 as childcount,2 as receiver_level");
		strNoGroupSQL.append(" from base_studentinfo t1,pcmc_user t2,base_teacher_subject t3,base_class t4");
		strNoGroupSQL.append(" where t1.state='1' and t1.userid=t2.userid and t1.classid=t4.classid");
		strNoGroupSQL.append(" and t3.state>'0' and t3.classid=t1.classid and t3.userid=?");
		noGroupParam.add(userid);
		if (StringUtil.isNotEmpty(searchText)){
			if (StringUtil.isInteger(searchText)){//�������֣�������Ӧ�༶��δ�������Ա
				strNoGroupSQL.append(" and t4.classnm like ?");
				noGroupParam.add("%"+searchText+"%");
			}
			else if (StringUtil.isLetter(searchText)){//������ĸ����ѯ�����ڰ༶��δ�������Ա
				strNoGroupSQL.append(" and exists ( select null from learn_group st where st.valid='Y' ");
				strNoGroupSQL.append(" and st.classid=t1.classid");
				strNoGroupSQL.append(" and st.group_name like ? )");
				noGroupParam.add(searchText+"%");				
			}
			else if (StringUtil.isChinese(searchText)){//���뺺��
				strNoGroupSQL.append(" and t2.username like ?");
				noGroupParam.add(searchText+"%");				
			}
			else{//����ַ�
				String firstChar = searchText.substring(0, 1);
				if (StringUtil.isInteger(firstChar)){
					strNoGroupSQL.append(" and t4.classnm like ?");
					noGroupParam.add("%"+firstChar+"%");					
				}				
				String secondChar = searchText.substring(1);				
				strNoGroupSQL.append(" and t2.username like ?");
				noGroupParam.add(secondChar+"%");					
			}
		}
		strNoGroupSQL.append(" and not exists");
		strNoGroupSQL.append(" (select null from learn_group_member st");
		strNoGroupSQL.append(" where st.valid='Y' and st.userid=t1.userid)");*/			
        
		//ȡ�������ѧ��
		/*StringBuffer strMemberSQL = new StringBuffer();
		ArrayList<Object> memberParam = new ArrayList<Object>();	
		strMemberSQL.append(" select Concat('U_',t1.userid) as id,t2.username as receiver_name,0 as childcount,3 as receiver_level");
		strMemberSQL.append(" from learn_group_member t1,pcmc_user t2,base_teacher_subject t3,base_class t4,learn_group t5");
		strMemberSQL.append(" where t1.valid='Y' and t1.userid=t2.userid and t1.group_id=t5.group_id");
		strMemberSQL.append(" and t5.classid=t4.classid and t4.classid=t3.classid and t3.userid=t5.userid and t3.userid=?");
		memberParam.add(userid);
		if (StringUtil.isNotEmpty(searchText)){//ȡ���������Ա
			if (StringUtil.isInteger(searchText)){//�������֣�������Ӧ�༶��δ�������Ա
				strMemberSQL.append(" and t4.classnm like ?");
				memberParam.add("%"+searchText+"%");
			}
			else if (StringUtil.isLetter(searchText)){//�����ַ���������Ӧ��С��
				strMemberSQL.append(" and t5.group_name like ?");
				memberParam.add(searchText+"%");				
			}
			else if (StringUtil.isChinese(searchText)){//���뺺��
				strMemberSQL.append(" and t2.username like ?");
				memberParam.add(searchText+"%");				
			}
			else{//����ַ� 1A��ѯ1����A�����Ա
				String firstChar = searchText.substring(0, 1);
				if (StringUtil.isInteger(firstChar)){
					strMemberSQL.append(" and t4.classnm like ?");
					memberParam.add("%"+firstChar+"%");					
				}				
				String secondChar = searchText.substring(1);				
				strMemberSQL.append(" and t5.group_name like ?");
				memberParam.add(secondChar+"%");					
			}
		}*/		
		
		//ȡ�༶�µ�ѧ��
		StringBuffer strMemberSQL = new StringBuffer();
		ArrayList<Object> memberParam = new ArrayList<Object>();	
		strMemberSQL.append(" select distinct Concat('U_',t1.userid) as id,t1.classid as pid,");
		strMemberSQL.append(" t2.username as receiver_name,0 as childcount,2 as receiver_level");
		strMemberSQL.append(" from base_studentinfo t1,pcmc_user t2,base_teacher_subject t3,base_class t4");
		strMemberSQL.append(" where t1.state='1' and t1.userid=t2.userid and t1.classid=t4.classid");
		strMemberSQL.append(" and t3.state>'0' and t3.classid=t1.classid and t3.userid=?");
		memberParam.add(userid);
		if (StringUtil.isNotEmpty(searchText)){//ȡ�༶�������Ա
			
			strMemberSQL.append(" and t2.username like ?");
			memberParam.add(searchText+"%");				
			
		}
		
		PlatformDao pdao = new PlatformDao();
	    try{
	    	
	    	pdao.setSql(strClassSQL.toString());
	    	pdao.setBindValues(classParam);
	    	Element result = pdao.executeQuerySql(0,-1);
	    	
	    	Element data = new Element("Data");	    		    		
    		List classList = result.getChildren("Record");
    		int classCount =  classList.size();
    		for (int i=0;i<classCount;i++){
    			Element classRec = (Element)classList.get(i);
    			String id = classRec.getChildText("id");
    			String classid = id.substring(2);
    			
    			XmlResultUtil.addRecordToData(data,classRec);//���Ӱ༶
    			if (StringUtil.isNotEmpty(searchText)){	
	    				String strSQL = strMemberSQL.toString()+" and t4.classid='"+classid+"'";
	    				pdao.setSql(strSQL);
	    				pdao.setBindValues(memberParam);
	    				
	    				Element memberResult = pdao.executeQuerySql(0, -1);
	    				List memberList = memberResult.getChildren("Record");
	    				XmlResultUtil.addListToData(data,memberList);													
				}    			
    		}
	    	
	    	xmlDocUtil.getResponse().addContent(data);
	    	xmlDocUtil.setResult("0");
	    	
	    }catch (Exception e) {
			e.printStackTrace();
			log4j.logError("[�Ծ��͹���-ȡ���ն���]"+e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
		} finally {
			pdao.releaseConnection();
		}
		
	}

	/**
	 * ����������Ϣ ��Ԥ����ʵʱ��������
	 *
	 */
	public void addPaperSend(){
		Element reqElement =  xmlDocUtil.getRequestData();
		PlatformDao pdao = new PlatformDao();
		try {
			
			Element paperSendRec = ConfigDocument.createRecordElement("yuexue", "learn_paper_send");
			XmlDocPkgUtil.copyValues(reqElement, paperSendRec, 0, true);
			
			if ("Y".equals(reqElement.getChildText("is_delayed"))){
				XmlDocPkgUtil.setChildText(paperSendRec, "public_status", "1");
			}
			else{
				XmlDocPkgUtil.setChildText(paperSendRec, "public_status", "2");
			}
			
			String send_id = pdao.insertOneRecordSeqPk(paperSendRec).toString();
			
			
			if (!"Y".equals(reqElement.getChildText("is_delayed"))){
				implPaperSend(send_id);
			}			
			
			String[] returnData = { "send_id"};
			Element resData = XmlDocPkgUtil.createMetaData(returnData);	
			resData.addContent(XmlDocPkgUtil.createRecord(returnData,new String[] {send_id}));			
			
			xmlDocUtil.getResponse().addContent(resData);			
			xmlDocUtil.setResult("0");	
			xmlDocUtil.writeHintMsg("00103", "���ͳɹ�!");		
		}
		catch (Exception e) {	
			e.printStackTrace();
			log4j.logError("[���͹���-���ⷢ��]"+e.getMessage());
			xmlDocUtil.writeHintMsg("���͹���-��������ʧ��!");
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);  			
		} finally {
			pdao.releaseConnection();
		}	
	}
	/**
	 * �����Ծ�(��Ԥ��״̬���Ծ�,ִ�з���)
	 *
	 */	
	public void sendPaper(){
		String username   = xmlDocUtil.getSession().getChildTextTrim("username");
		Element reqElement =  xmlDocUtil.getRequestData();
		String send_id = reqElement.getChildText("send_id");
		ArrayList<Object> paramList = new ArrayList<Object>();
		
		PlatformDao pdao = new PlatformDao();
		try {
			implPaperSend(send_id);
			
			StringBuffer updateSQL = new StringBuffer();
			updateSQL.append("update learn_paper_send set modify_by='"+username+"',");
			updateSQL.append(" modify_date=now(),public_status='2',public_time=now()");
			updateSQL.append(" where send_id=?");
			
			pdao.setSql(updateSQL.toString());
			paramList.add(send_id);
			pdao.setBindValues(paramList);
			pdao.executeTransactionSql();
			
			String[] returnData = { "public_status"};
			Element resData = XmlDocPkgUtil.createMetaData(returnData);	
			resData.addContent(XmlDocPkgUtil.createRecord(returnData,new String[] {"2"}));			
			
			xmlDocUtil.getResponse().addContent(resData);			
			
			xmlDocUtil.setResult("0");	
			xmlDocUtil.writeHintMsg("20203", "���ͳɹ�!");			
		}
		catch (Exception e) {
			e.printStackTrace();
			log4j.logError("[���͹���-���ⷢ��]"+e.getMessage());
			xmlDocUtil.writeHintMsg("���͹���-��������ʧ��!");
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);  			
		} finally {
			pdao.releaseConnection();
		}		
	}
	/**
	 * ��ȡ���͵İ༶��¼
	 */
	public void getSendPaperLog(){
		String userid   = xmlDocUtil.getSession().getChildTextTrim("userid");
		Element reqElement =  xmlDocUtil.getRequestData();
		String paper_id = reqElement.getChildText("qry_paperid");		
		PlatformDao pdao = new PlatformDao();
		ArrayList<Object> paramList = new ArrayList<Object>();	
		try {
			StringBuffer praxesSQL = new StringBuffer();
			praxesSQL.append(" select * from learn_paper_send t1 where t1.valid='Y' and t1.paper_id=? ");
			paramList.add(paper_id);
			praxesSQL.append(" and t1.userid=? ");	
			paramList.add(userid);
			praxesSQL.append("  order by t1.create_date  ");	
	    	pdao.setSql(praxesSQL.toString());	   	
	    	pdao.setBindValues(paramList);
	    	Element result = pdao.executeQuerySql(0,-1);
			xmlDocUtil.getResponse().addContent(result);			
			xmlDocUtil.setResult("0");					
		}catch (Exception e) {	
			e.printStackTrace();
			log4j.logError("[���͹���-��ȡ��¼]"+e.getMessage());
			xmlDocUtil.writeHintMsg("���͹���-��ȡ��¼ʧ��!");
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);  			
		} finally {
			pdao.releaseConnection();
		}	
	}
	/**
	 * �ط��Ծ�
	 *
	 */
	public void resendPaper(){
		String username   = xmlDocUtil.getSession().getChildTextTrim("username");
		Element reqElement =  xmlDocUtil.getRequestData();
		String send_id = reqElement.getChildText("send_id");
		ArrayList<Object> paramList = new ArrayList<Object>();
		
		PlatformDao pdao = new PlatformDao();
		try {
			//implPaperSend(send_id);
			
			StringBuffer updateSQL = new StringBuffer();
			updateSQL.append("update learn_paper_send set show_result='N',modify_by='"+username+"',");
			updateSQL.append(" modify_date=now(),public_status='3',republic='Y'");
			updateSQL.append(" where send_id=?");
			
			pdao.setSql(updateSQL.toString());
			paramList.add(send_id);
			pdao.setBindValues(paramList);
			pdao.executeTransactionSql();
			
			StringBuffer updateMyExamSQL = new StringBuffer();
			updateMyExamSQL.append("update learn_my_examination set status='10',modify_by='"+username+"',modify_date=now(),");
			updateMyExamSQL.append(" receive_time=null,begin_time=null,end_time=null,paper_score=null,random_paper_id=null");
			updateMyExamSQL.append(" where send_id=?");
			
			pdao.setSql(updateMyExamSQL.toString());			
			pdao.setBindValues(paramList);
			pdao.executeTransactionSql();			
			
			String[] returnData = { "public_status"};
			Element resData = XmlDocPkgUtil.createMetaData(returnData);	
			resData.addContent(XmlDocPkgUtil.createRecord(returnData,new String[] {"3"}));			
			
			xmlDocUtil.getResponse().addContent(resData);				
			
			xmlDocUtil.setResult("0");	
			xmlDocUtil.writeHintMsg("20203", "�ط��ɹ�!");			
		}
		catch (Exception e) {	
			e.printStackTrace();
			log4j.logError("[���͹���-���ⷢ��]"+e.getMessage());
			xmlDocUtil.writeHintMsg("���͹���-��������ʧ��!");
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);  			
		} finally {
			pdao.releaseConnection();
		}		
	}	
	/**
	 * ִ���Ծ���
	 *
	 */
	public void implPaperSend(String send_id) throws Exception{	
		PlatformDao pdao = new PlatformDao(true);
		//String send_user_id = xmlDocUtil.getSession().getChildText("userid");
		try {
			StringBuffer strSQL = new StringBuffer();
			strSQL.append("select send_id,paper_id,receiver_ids,create_by ");
			strSQL.append(" from learn_paper_send where valid='Y' and send_id=?");
			ArrayList<Object> paramList = new ArrayList<Object>();
			paramList.add(send_id);
			
	    	pdao.setSql(strSQL.toString());
	    	pdao.setBindValues(paramList);
	    	Element result = pdao.executeQuerySql(0, -1);
	    	List list = result.getChildren("Record");
	    	if (list.size()>0){
	    		Element sendRec = (Element)list.get(0);
	    		String receiver_ids = sendRec.getChildText("receiver_ids");
                String[] receiverids = receiver_ids.split(",");
                for (int i=0;i<receiverids.length;i++){

	    		   String receiverid = receiverids[i];
	    		   if ("C_".equals(receiverid.substring(0,2)) || "G_".equals(receiverid.substring(0,2))){//���͵���
	    			   StringBuffer qryStuSQL = new StringBuffer();
	    			   if ("C_".equals(receiverid.substring(0,2))){//���͵���
	    				   String classid = receiverid.substring(2);
			    		   qryStuSQL.append("select t1.userid from base_studentinfo t1");
			    		   qryStuSQL.append(" where t1.classid='"+classid+"'");	    				   
	    			   }
	    			   else if ("G_".equals(receiverid.substring(0,2))){//���͵���
	    				   String group_id = receiverid.substring(2);
	    				   StringBuffer qryClassSQL=new StringBuffer();
	    				   qryClassSQL.append("select t1.classid from learn_group t1 ");
	    				   qryClassSQL.append("where t1.group_id='"+group_id+"'");
	    				   pdao.setSql(qryClassSQL.toString());
				    	   Element classResult = pdao.executeQuerySql(0, -1);
				    	   
				    	   Element classRec=(Element)classResult.getChildren("Record").get(0);
				    	   if(receiver_ids.indexOf(classRec.getChildText("classid"))>-1){
				    		   continue;
				    	   }
			    		   qryStuSQL.append("select t1.userid from learn_group_member t1");
			    		   qryStuSQL.append(" where t1.group_id='"+group_id+"'");		    				   
	    			   }
			    	   pdao.setSql(qryStuSQL.toString());
			    	   Element stuResult = pdao.executeQuerySql(0, -1);
			    	   List stuList = stuResult.getChildren("Record");
			    	   for (int j=0;j<stuList.size();j++){
			    			Element stuRec = (Element)stuList.get(j);
			    			
			    			Element myExamRec = ConfigDocument.createRecordElement("yuexue", "learn_my_examination");
			    			XmlDocPkgUtil.copyValues(sendRec, myExamRec, 0, true);
			    			
			    			XmlDocPkgUtil.setChildText(myExamRec, "userid", stuRec.getChildText("userid"));		    			
			    			XmlDocPkgUtil.setChildText(myExamRec, "status", "10");
			    			pdao.insertOneRecordSeqPk(myExamRec);
			    			
			    			//����΢����Ϣ
			    			sendWXMessage(stuRec.getChildText("userid"), sendRec.getChildText("paper_id"));
			    	   }	    			   
	    		   }
	    		   else if ("U_".equals(receiverid.substring(0,2))){//���͸�ָ���û�(ѧ�����ʦ)
	    			    String stuID = receiverid.substring(2);
	    			    StringBuffer qryClassSQL=new StringBuffer();
	    				   qryClassSQL.append("select t1.classid from base_studentinfo t1 ");
	    				   qryClassSQL.append("where t1.userid='"+stuID+"'");
	    				   pdao.setSql(qryClassSQL.toString());
				    	   Element classResult = pdao.executeQuerySql(0, -1);
				    	   
				    	   Element classRec=(Element)classResult.getChildren("Record").get(0);
				    	   if(receiver_ids.indexOf(classRec.getChildText("classid"))>-1){
				    		   continue;
				    	   }
				    	   
		    			Element myExamRec = ConfigDocument.createRecordElement("yuexue", "learn_my_examination");
		    			XmlDocPkgUtil.copyValues(sendRec, myExamRec, 0, true);
		    			XmlDocPkgUtil.setChildText(myExamRec, "userid", stuID);		    			
		    			XmlDocPkgUtil.setChildText(myExamRec, "status", "10");
		    			pdao.insertOneRecordSeqPk(myExamRec);
		    			
		    			//����΢����Ϣ
		    			sendWXMessage(stuID, sendRec.getChildText("paper_id"));
	    		   }		    		
                }
	    	}
	    	else{
	    		throw new NotFoundDataException("û���ҵ�IDΪ["+send_id+"]�ķ��ͼ�¼");
	    	}
		}
		catch (Exception e) {	
			throw e;
		}
		finally {
			pdao.releaseConnection();
		}
	}
	
	/**
	 * �ط�΢����Ϣ�����õ����ӿڣ�
	 * @param mobiles ��Ϣ�������ֻ���,���������ö��Ÿ���
	 * @param paper_id
	 * @throws SQLException executeQuerySql�׳��쳣
	 */
	@SuppressWarnings("serial")
	public static JSONObject reSendWXMessage(String mobiles,final String paper_id) throws SQLException {
		JSONObject jsonObject = new JSONObject();
		String errmsg = "����ʧ�ܣ�";
		//����userId��paperId��ѯ����Ϣģ����Ҫ����
		if (StringUtil.isEmpty(mobiles)) {
			errmsg = "��������ϢΪ�գ�";
			return jsonObject;
		}
		
		PlatformDao pdao =new PlatformDao();
		try {
			//��ȡ�����Ļ����ķ���������
			String serverDomain = (String)BrightComConfig.getSysConfiguration().getProperty("serverDomain.nwwh.url");
			String token = (String)BrightComConfig.getSysConfiguration().getProperty("serverDomain.nwwh.token");
			String status = (String)BrightComConfig.getSysConfiguration().getProperty("serverDomain.nwwh.status");
			
			if("1".equals(status)){
				pdao.setSql("	SELECT lep.paper_id,paper_name,t1.my_examination_id,t2.classid," +
							"	(select subjname from base_subject t where t.subjectid = lep.subject_id)as subname," +
							"	(select gradename from base_grade t1,pcmc_dept t2 where t1.deptid = t2.deptid and t1.gradecode = lep.grade_code and deptcode = lep.deptcode) as gradename" +
							" 	FROM learn_examination_paper lep,learn_my_examination t1,base_studentinfo t2 where lep.paper_id = t1.paper_id and t1.userid = t2.userid and lep.paper_id = ?");
				pdao.setBindValues(new ArrayList<String>(){{add(paper_id);}});
				Element result = pdao.executeQuerySql(0, -1);
		    	List list = result.getChildren("Record");
		    	if (list.size()>0){
		    		Element sendRec = (Element)list.get(0);
		    		String msg_name = "��ҵ��������";
		    		String msg_title = sendRec.getChildText("gradename") + sendRec.getChildText("subname");//�꼶��ѧ��
		    		String msg_content = sendRec.getChildText("paper_name");//�μ���
		    		String url = "";
		    		if(true){
		    			url = "/weixin/video_show.jsp?_paperid=" + paper_id;
		    		} else {
		    			url = "/weixin/student/exam.jsp?paperid="+paper_id+"&myexamid="+sendRec.getChildText("my_examination_id")+"&classid="+sendRec.getChildText("classid");
		    		}
		    		url = url.replaceAll("//", "/");
					JSONObject json = HttpWebWeiXinMessage.sendWebWeiXinMessage(token,mobiles,msg_name,msg_title,msg_content,serverDomain + url);
					errmsg = json.getString("errmsg");
		    	}
			} else {
				errmsg = "΢�Ŷ������ѹ����ѹرգ�����ϵ����Ա��";
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pdao.releaseConnection();
		}
		jsonObject.put("errmsg", errmsg);
		return jsonObject;
	}
	
	/**
	 * ����΢����Ϣ��������ϵͳ�ӿڣ�
	 * @param userid
	 * @param paper_id
	 */
	private void sendWXMessage(String userid,String paper_id) {
		String deptid = xmlDocUtil.getSession().getChildText("deptid");
		String templateId = WeiXinTemplate.gettemplateCode().get("2010");
		String url = "http://wo.szbrightcom.com";
		StringBuffer sb = new StringBuffer();
		//����userId��paperId��ѯ����Ϣģ����Ҫ����
		sb.append("	select * from");
		sb.append(" (SELECT winxincode FROM base_winxin_user where valid = 'Y' and winxincode is not null and usercode = (SELECT usercode FROM pcmc_user where userid = '"+userid+"')) tab1 ");
		sb.append("	join");
		sb.append("	(select (select subjname from base_subject where subjectid = t1.subject_id)as subname,(select gradename from base_grade where gradecode = t1.grade_code and deptid = '"+deptid+"')as gradename,t1.* from learn_examination_paper t1 where paper_id = '"+paper_id+"') tab2");
		PlatformDao pdao = new PlatformDao();
		pdao.setSql(sb.toString());
		try {
			List li = pdao.executeQuerySql(0, -1).getChildren("Record");
			//ͨ��jsonObject��ʽ����ģ������
			//pdao.setSql("select * from busi_winxin_message_template where template_id_short = '"+templateId+"' ");
			//Element el_Template = (Element) pdao.executeQuerySql(0, -1).getChildren("Record").get(0);
			//JSONObject jsonTemp = JSONObject.fromObject(el_Template.getChildText("template_sample"));
			if (li.size() <= 0) {
				//����΢��IDʱ��������Ϣģ��
				return;
			}
			Element el = (Element) li.get(0);
			String touserID = el.getChildText("winxincode");//�û�΢��ID
			Map<String, String> requestMap = new HashMap<String, String>();
			requestMap.put("����֪ͨ��WO����", "#173177");
			requestMap.put(el.getChildText("paper_name"), "#173177");//�Ծ�����
			requestMap.put(el.getChildText("gradename") + el.getChildText("subname"), "#173177");//�꼶+ѧ��
			requestMap.put("40".equals(el.getChildText("resource_type").substring(0, 1)) ? "����" : "�μ�" , "#173177");//�μ�����20*Ϊ�μ���40*Ϊ����
			requestMap.put("�뼰ʱ����", "#173177");
			//������Ϣ
			//JSONObject postObject = WeiXinTemplate.getInstance().sendTemplate(touserID,templateId,url,requestMap);
			JSONObject postObject  = JSONObject.fromObject("{'errcode':0,'errmsg':'ok','msgid':200228332}");
			//������Ϣ�ص������ӷ��ͼ�¼
			Element myExamRec = ConfigDocument.createRecordElement("yuexue", "busi_winxin_message_record");
			XmlDocPkgUtil.setChildText(myExamRec, "touser", touserID);
			XmlDocPkgUtil.setChildText(myExamRec, "template_id", templateId);
			XmlDocPkgUtil.setChildText(myExamRec, "url", url);
			XmlDocPkgUtil.setChildText(myExamRec, "post_data", "post");
			XmlDocPkgUtil.setChildText(myExamRec, "errcode", postObject.get("errcode").toString());
			XmlDocPkgUtil.setChildText(myExamRec, "errmsg", postObject.get("errmsg").toString());
			XmlDocPkgUtil.setChildText(myExamRec, "msgid", postObject.get("msgid").toString());
			XmlDocPkgUtil.setChildText(myExamRec, "send_user_id", userid);
			pdao.insertOneRecord(myExamRec);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log4j.logError("[���͹���-���ⷢ��]"+e.getMessage());
			xmlDocUtil.writeHintMsg("���͹���-��������ʧ��!");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			pdao.releaseConnection();
		}
	}
	
}
