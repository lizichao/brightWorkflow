package cn.com.bright.edu.weixin.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jdom.Element;

import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.id.UUIDHexGenerator;
import cn.brightcom.jraf.util.Log;
import cn.brightcom.jraf.util.StringUtil;
import cn.com.bright.edu.weixin.util.MD5;

/**
 * 
 * @author �ʾ���鴦����
 * @author aping	
 * @date 2014-03-20
 */
public class SupVoteHandle
 {

	String strArray = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
   /**
    * ִ�����
    * @param relpyMessage
    */
   public String process(Map<String, String> requestMap){
		String openid  = requestMap.get("FromUserName");
		String content = requestMap.get("Content");
		String eventKey = requestMap.get("EventKey");
		String userStatus = requestMap.get("SupUserStatus");

		if ("vote-2".equals(eventKey)){
			if ("4".equals(userStatus)){//�Ѿ�����ʾ��
				return getTipsInfo("4");
			}
			if (!"0".equals(userStatus)){//��û��ɵ��ʾ�ģ����ԭ����,���¿�ʼ
				clearCacheData(openid);
			}
			writeVoteMobile(openid,"opern_id",openid);			
			return getVoteTitle();
		}	
		else if ("2".equals(userStatus)){
			String deptCode = verificationVotePws(content);			
			if 	(StringUtil.isEmpty(deptCode)){
				return getTipsInfo(userStatus);//��ʾ�û�������ȷ���ʾ�����
			}
			else{
				String titleID = getVoteTitleID();
				writeVoteMobile(openid,"title_id",titleID);				
				writeVoteMobile(openid,"dept_code",deptCode);
				return getTopicInfo(openid);//������Ŀ��Ϣ
			}			
		}
		else if ("3".equals(userStatus)){
			String strMsg=writeVoteResult(openid,content);
			if (StringUtil.isEmpty(strMsg)){
			   return getTopicInfo(openid);//��һ��������Ŀ��Ϣ
			}
			else{
				return strMsg;
			}
		}
		else if ("4".equals(userStatus)){
			return getTopicInfo(userStatus);//��ʾ�û����Ѿ�������ʾ�
		}
	    return "";
   }
   /**
	 * �����û�״̬������ʾ��Ϣ
	 * @param userStatus
	 * @return
	 */
	public String getTipsInfo(String userStatus){
		StringBuffer sbResult = new StringBuffer();
		if ("0".equals(userStatus)){
			sbResult.append("������ʾ\n");
			sbResult.append("�𾴵��û�,����ʾ����˵�,ѡ���ʾ�����\n");
		}
		else if ("2".equals(userStatus)){
			sbResult.append("������ʾ\n");
            sbResult.append("��ظ��ʾ�����\n");
		}
		else if ("3".equals(userStatus)){
			sbResult.append("������ʾ\n");
			sbResult.append("�������Ŀ�������ѡ��\n");
		}
		else if ("4".equals(userStatus)){
			sbResult.append("������ʾ\n");
			sbResult.append("���Ѿ�����ʾ����,лл���Ĳ���!\n");			
		}		
		return sbResult.toString();
	}
	/**
	 * ����OpenID,�ж��û�״̬
	 * @param OpenID
	 * @return 0 û�вμ��ʾ�,vote_mobilû��OpenID��¼
	 *         1 ������ʾ�ť,δѡ���ʾ�
	 *         2 ѡ�����ʾ�,δѡ������
	 *         3 �������ʾ�����,������
	 *         4 �������
	 */	
	public  String  getUserStatus(String OpenID){
		Map<String, String> voteMap = new HashMap<String, String>();
		PlatformDao pdao = new PlatformDao("szedu_supvote",true);
		try{
			StringBuffer strSQL = new StringBuffer();
			strSQL.append("select * from vote_mobil where opern_id='"+OpenID+"'");
			pdao.setSql(strSQL.toString());
			Element resData = pdao.executeQuerySql(0,-1);
			List ls = resData.getChildren("Record");
			if (ls.size()==0){
				return "0";
			}
			else{
				Element rec = (Element)ls.get(0);
				String  dept_code = rec.getChildText("dept_code");
				voteMap.put("dept_code", dept_code);
				if (StringUtil.isEmpty(dept_code)){
					return "2";					
				}
				String  vote_status = rec.getChildText("vote_status");
				if (StringUtil.isEmpty(vote_status)){
					return "3";
				}
				else{
					return "4";
				}
			}			
		}catch (Exception e) {			
           e.printStackTrace();
           Log log4j = new Log(SupVoteHandle.class.toString()) ;
           log4j.logError("[΢���ʾ����-�ж��û�״̬.]"+e.getMessage()) ;            
       } finally {
       	pdao.releaseConnection();
       }
		return null;
	}   
	   /**
	    * ��ȡ��Ч���ʾ���Ϣ
	    * @return
	    */
	   public String getVoteTitle(){
		   StringBuffer sbResult = new StringBuffer();
			PlatformDao pdao = new PlatformDao("szedu_supvote",true);
			try{
				
				StringBuffer strSQL = new StringBuffer();
				strSQL.append("select * from vote_title where c_status='1' and vote_object='3'");//��ѯ��ǰ��Ч�ļҳ��ʾ�
				pdao.setSql(strSQL.toString());
				Element resData = pdao.executeQuerySql(0,-1);			
				List ls = resData.getChildren("Record");
				if(ls.size()<0){
				  return "��ǰ����Ч�ʾ�\n";
				}
				else{
					Element rec = (Element)ls.get(0);
					sbResult.append("��ӭ���μ��ʾ����.\n");
					String content = rec.getChildText("vote_content");
					sbResult.append(Html2Text(content)+"\n");
					sbResult.append(getTipsInfo("2"));					
				}
			}catch (Exception e) {			
		        e.printStackTrace();
		        Log log4j = new Log(this.getClass().toString()) ;
		        log4j.logError("[΢���ʾ����-��ȡ��Ч���ʾ���Ϣ.]"+e.getMessage()) ;            
		    } finally {
		    	pdao.releaseConnection();
		    }	   
		    return sbResult.toString();
	   }
   /**
    * ��ȡ��Ч���ʾ���Ϣ
    * @return
    */
   public String getVoteTitleID(){
	   String title_id = null;
		PlatformDao pdao = new PlatformDao("szedu_supvote",true);
		try{
			
			StringBuffer strSQL = new StringBuffer();
			strSQL.append("select title_id from vote_title where c_status='1' and vote_object='3'");//��ѯ��ǰ��Ч�ļҳ��ʾ�
			pdao.setSql(strSQL.toString());
			Element resData = pdao.executeQuerySql(0,-1);			
			List ls = resData.getChildren("Record");
			if(ls.size()<0){
			  return "��ǰ����Ч�ʾ�\n";
			}
			for (int i=0;i<ls.size();i++){
				Element rec = (Element)ls.get(0);
				title_id=rec.getChildText("title_id");
				return title_id;
			}		
		}catch (Exception e) {			
	        e.printStackTrace();
	        Log log4j = new Log(this.getClass().toString()) ;
	        log4j.logError("[΢���ʾ����-��ȡ��Ч���ʾ���Ϣ.]"+e.getMessage()) ;            
	    } finally {
	    	pdao.releaseConnection();
	    }	   
	    return title_id;
   }
   /**
    * д���ʾ�����,��������һ��Ľ��,�����޸�vote_mobile��״̬
    * @param relpyMessage A,B,C,D...
    * @return ����null ��ʾд��ɹ�,���򷵻ش�����Ϣ
    */
   public String writeVoteResult(String OpenId,String relpyMessage){
		PlatformDao pdao = new PlatformDao("szedu_supvote",true);
		try{
			String optionContent ="";	
			StringBuffer strSQL = new StringBuffer();
			strSQL.append("select st.*,sm.dept_code from vote_topic st,vote_mobil sm");//��ѯOpenID��ǰ����Ŀ
			strSQL.append(" where sm.opern_id='"+OpenId+"'");
			strSQL.append("  and st.deptcode='4403' and st.title_id=sm.title_id ");
			strSQL.append(" and not exists");
			strSQL.append(" (select null from vote_result sn where sn.created_by=sm.opern_id");
			strSQL.append("  and sn.topic_id=st.topic_id)");
			strSQL.append("  order by st.topic_num");
			ArrayList<String> val = new ArrayList<String>();
			pdao.setSql(strSQL.toString());
			Element resData = pdao.executeQuerySql(0,-1);			
			List ls = resData.getChildren("Record");			
			int rowCount = ls.size();
			if (rowCount>0){
				Element rec = (Element)ls.get(0);
				String topic_id=rec.getChildText("topic_id");
				String title_id=rec.getChildText("title_id");
				String dept_code=rec.getChildText("dept_code");
				String topic_type = rec.getChildText("topic_type");
				StringBuffer optionSQL = new StringBuffer();
				
				optionSQL.append("select * from vote_options st ");
				optionSQL.append(" where topic_id='"+topic_id+"'");
				pdao.setSql(optionSQL.toString());
				Element opResData = pdao.executeQuerySql(0,-1);
				List opList = opResData.getChildren("Record");
				int opCount = opList.size();
				int opNum=-1;
				String option_id="";
				//��Ŀ���Ͳ�Ϊ�����
				if(!"03".equals(topic_type)){
					if(relpyMessage.length()>1){
						return getTipsInfo("3");
					}else{
						opNum = strArray.indexOf(relpyMessage.toUpperCase());
						//�д�ѡ���һظ����ݳ���ѡ�Χ
						if (opCount>0 && opNum>=opCount){
							return "���볬��ѡ�Χ\n"+getTipsInfo("3");
						}else if(opNum==-1){
							return "���볬��ѡ�Χ\n"+getTipsInfo("3");
						}else{
							if (opCount>0 && opNum>=0){//�д�ѡ��,ȡ��ѡ��ID
							   Element opRec = (Element)opList.get(opNum);
							   option_id=opRec.getChildText("option_id");
							}
						}
					}
				}else{
					// Element opTextRec = (Element)opList.get(0);
					 //option_id=opTextRec.getChildText("option_id");
					 optionContent =  Html2Text(relpyMessage);
				}
					StringBuffer insertSQL = new StringBuffer();
					insertSQL.append("insert into vote_result ");
					insertSQL.append(" (result_id,title_id,topic_id,");
					insertSQL.append(" option_id,created_by,xxdm,c_content,vote_type,created_date,create_year)");
					insertSQL.append(" values(?,?,?,?,?,?,?,'1',sysdate,to_char(sysdate,'YYYY'))");
					val.add((String)new UUIDHexGenerator().generate(null));
					val.add(title_id);
					val.add(topic_id);
					val.add(option_id);
					val.add(OpenId);
					val.add(dept_code);
					val.add(optionContent);	
					pdao.setSql(insertSQL.toString());
					pdao.setBindValues(val);
					pdao.executeTransactionSql();					
			
				if (rowCount==1){//��ǰ��ĿΪ���һ��,д��״̬
					writeVoteMobile(OpenId,"vote_status","end");
					return getTipsInfo("4");
				}
			}
			else{
				return getTipsInfo("4");
			}
		}catch (Exception e) {			
	        e.printStackTrace();
	       Log log4j = new Log(this.getClass().toString()) ;
	        log4j.logError("[΢���ʾ����-д���ʾ�����.]"+e.getMessage()) ;            
	    } finally {
	    	pdao.releaseConnection();
	    }
	    return null;
   }
   
   /**
    * У���ʾ�����
    * @param votePws
    * @return ����ʾ��������,�򷵻�ѧУ����,���򷵻�null
    */
   public String verificationVotePws(String votePws){
	    votePws= MD5.getMD5(votePws.toUpperCase()).toUpperCase();
	    String deptcode = null;
		PlatformDao pdao = new PlatformDao("szedu_supvote",true);
		try{
			StringBuffer strSQL = new StringBuffer();
			strSQL.append(" select t1.deptpwd, t1.deptpwd_mi,t1.deptcode,t1.deptname from  pcmc_dept t1");
			strSQL.append(" where  t1.parpwd_mi = '"+votePws+"'");
			pdao.setSql(strSQL.toString());
			Element resData = pdao.executeQuerySql(0,-1);			
			List ls = resData.getChildren("Record");
			for (int i=0;i<ls.size();i++){
				Element rec = (Element)ls.get(0);
				deptcode= rec.getChildText("deptcode");
				return deptcode;
			}		
		}catch (Exception e) {			
	        e.printStackTrace();
	        Log log4j = new Log(this.getClass().toString()) ;
	        log4j.logError("[΢���ʾ����-У���ʾ�����.]"+e.getMessage()) ;            
	    } finally {
	    	pdao.releaseConnection();
	    }	   
	    return deptcode;
   }
	/**
	 * д��VoteMobile����Ϣ
	 * @param fieldName
	 * @param fieldValue
	 */
	public void writeVoteMobile(String OpenId,String fieldName,String fieldValue){
		PlatformDao pdao = new PlatformDao("szedu_supvote",true);
		ArrayList<String> val = new ArrayList<String>();
		try{
			StringBuffer strSQL = new StringBuffer();
			if ("opern_id".equals(fieldName)){
				strSQL.append("insert into vote_mobil (mobil_id, opern_id,  create_date) values (?,?,sysdate)");
				val.add((String)new UUIDHexGenerator().generate(null));
				val.add(OpenId);
				//val.add("1");//������ʾ�δѡ���ʾ�
			}
			else{
				strSQL.append("update vote_mobil set "+fieldName+"='"+fieldValue+"'");
				strSQL.append(" where opern_id=?");
				val.add(OpenId);
			}
			pdao.setSql(strSQL.toString());
			pdao.setBindValues(val);
			pdao.executeTransactionSql();
			
		}catch (Exception e) {			
	        e.printStackTrace();
	        Log log4j = new Log(this.getClass().toString()) ;
	        log4j.logError("[΢���ʾ����-д���û�״̬.]"+e.getMessage()) ;            
	    } finally {
	    	pdao.releaseConnection();
	    }		
	}
	/**
	 * ����OpenID������Ŀ��Ϣ,��ѡ����Ϣ
	 * @param OpenID
	 * @return
	 */
	public String getTopicInfo(String OpenID){
		StringBuffer sbResult = new StringBuffer();
		PlatformDao pdao = new PlatformDao("szedu_supvote",true);
		try{
			StringBuffer strSQL = new StringBuffer();
			strSQL.append("select st.topic_name,sd.*, ");
			strSQL.append(" (select count(topic_id) from vote_topic t1 where t1.title_id=st.title_id and sm.dept_code like t1.deptcode || '%' ) as topic_count,");
			strSQL.append(" (select count(result_id) from vote_result t2 where t2.title_id=st.title_id and t2.created_by=sm.opern_id) as result_count");			
			strSQL.append(" from vote_topic st,vote_options sd,vote_mobil sm");//��ѯOpenID��ǰ����Ŀ
			strSQL.append(" where sm.opern_id='"+OpenID+"'");
			strSQL.append(" and st.title_id=sm.title_id and st.topic_id=sd.topic_id(+) and sm.dept_code like st.deptcode || '%' ");
			strSQL.append(" and not exists");
			strSQL.append(" (select null from vote_result sn where sn.created_by=sm.opern_id");
			strSQL.append("  and sn.topic_id=st.topic_id)");
			strSQL.append("  order by st.attribute4,st.deptcode,st.topic_num,sd.option_id,sd.created_date");
			pdao.setSql(strSQL.toString());
			Element resData = pdao.executeQuerySql(0,-1);			
			List ls = resData.getChildren("Record");
			String topicID="";
			int rowCount = ls.size();
			if(rowCount>0){
				for (int i=0;i<ls.size();i++){
					Element rec = (Element)ls.get(i);
					String topic_id=rec.getChildText("topic_id");
					if (i==0){
						topicID=topic_id;
						sbResult.append("��"+rec.getChildText("topic_count")+"��,��"+(Integer.parseInt(rec.getChildText("result_count"))+1)+"��\n");
						sbResult.append(rec.getChildText("topic_name")+"\n");					
					}
					if (!topicID.equals(topic_id)){
						return sbResult.toString()+"\n��ظ�ѡ��";
					}
					else{
						String option_name=rec.getChildText("option_name");
						if (StringUtil.isEmpty(option_name)){
							return sbResult.toString()+"\n�����ֻظ�";
						}
						else{
						   sbResult.append(strArray.charAt(i)+":"+option_name+"\n");
						}
					}
					if (i==rowCount-1){//������һ����ѡ����
						return sbResult.toString()+"��ظ�ѡ��";
					}				
				}
			}else{
				return getTipsInfo("4");
			}
		}catch (Exception e) {			
	        e.printStackTrace();
	        Log log4j = new Log(this.getClass().toString()) ;
	        log4j.logError("[΢���ʾ����-������Ŀ��Ϣ.]"+e.getMessage()) ;            
	    } finally {
	    	pdao.releaseConnection();
	    }
		return null;
	}
	

	/**
	 * ���ԭ������
	 * @param open_id
	 */
	public void clearCacheData(String OpenId){
		PlatformDao pdao = new PlatformDao("szedu_supvote",true);		
		try{
			pdao.beginTransaction();
			
			StringBuffer delMobilSQL = new StringBuffer();
			StringBuffer delVoteResultSQL = new StringBuffer();

			//ɾ��΢�ŵ�������û���Ϣ
			delMobilSQL.append("delete from vote_mobil where opern_id='"+OpenId+"'");
			//ɾ���ʾ�����Ϣ
			delVoteResultSQL.append("delete from vote_result where CREATED_BY='"+OpenId+"'");
			
			pdao.setSql(delMobilSQL.toString());
			pdao.executeTransactionSql();
			
			pdao.setSql(delVoteResultSQL.toString());
			pdao.executeTransactionSql();
			pdao.commitTransaction();
		}catch (Exception e) {		
			pdao.rollBack();
	        e.printStackTrace();
	        Log log4j = new Log(this.getClass().toString()) ;
	        log4j.logError("[΢���ʾ����-д���û�״̬.]"+e.getMessage()) ;            
	    } finally {
	    	pdao.releaseConnection();
	    }
	}
	
	/**
	 * ����html��ǩ������
	 * @param inputString
	 * @return
	 */
	public String Html2Text(String inputString){
		 String htmlStr = inputString; //��html��ǩ���ַ��� 
         String textStr =""; 
	     Pattern p_script; 
	     Matcher m_script; 
	     Pattern p_style; 
	     Matcher m_style; 
	     Pattern p_html; 
	     Matcher m_html; 
	    
	    try { 
	    	htmlStr=htmlStr.replaceAll("&lt;", "<");
	    	htmlStr=htmlStr.replaceAll("&gt;", ">");
	    	htmlStr=htmlStr.replaceAll("&amp;", "&");
	    	htmlStr=htmlStr.replaceAll("&nbsp;", "");
            //����script��������ʽ{��<script[^>]*?>[\\s\\S]*?<\\/script> }
	        String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";  
            //����style��������ʽ{��<style[^>]*?>[\\s\\S]*?<\\/style> }
	        String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";  
            //����HTML��ǩ��������ʽ 
	        String regEx_html = "<[^>]+>"; 
	       
	        p_script = Pattern.compile(regEx_script,Pattern.CASE_INSENSITIVE); 
	        m_script = p_script.matcher(htmlStr); 
	        htmlStr = m_script.replaceAll(""); //����script��ǩ 
	
	        p_style = Pattern.compile(regEx_style,Pattern.CASE_INSENSITIVE); 
	        m_style = p_style.matcher(htmlStr); 
	        htmlStr = m_style.replaceAll(""); //����style��ǩ 
	       
	        p_html = Pattern.compile(regEx_html,Pattern.CASE_INSENSITIVE); 
	        m_html = p_html.matcher(htmlStr); 
	        htmlStr = m_html.replaceAll(""); //����html��ǩ 
	       
	        textStr = htmlStr; 
	       
	    }catch(Exception e) { 
	                System.err.println("Html2Text: " + e.getMessage()); 
	    }		    
	    return textStr;//�����ı��ַ���			
	}
	

}
