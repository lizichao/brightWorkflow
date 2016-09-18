package cn.com.bright.edu.weixin.core.impl;

import java.util.ArrayList;
import java.util.List;
import org.jdom.Element;
import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.util.Log;
import cn.brightcom.jraf.util.PasswordEncoder;
import cn.brightcom.jraf.util.StringUtil;

public class MinBanMessageImpl extends MessageImpl{
	private Log log4j = new Log(this.getClass().toString());
	/**
	 * ��ȡ������Ϣ
	 * @param idCard
	 * @return
	 */
	public String getBaseInfo(String idCard){
		if(StringUtil.isEmpty(idCard)) return "";
		
		PlatformDao pdao = null;
		ArrayList<String> val = new ArrayList<String>();
		String respContent = "";
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT t.ci_id AS userid,t.child_id_number AS usercode,t.child_name AS username,d.deptname,t.contact_tel");
		sql.append(",TO_CHAR(t.child_birthday,'yyyy-mm-dd') AS child_birthday");
		sql.append(",TO_CHAR(t.child_in_date,'yyyy-mm-dd') AS child_in_date");
		sql.append(",Get_Param_Chinese('C_SEX',t.child_sex) AS sex");
		sql.append(",Get_Param_Chinese('C_HJM',t.child_census) as census");
		sql.append(",Get_Param_Chinese('C_ZJLXM',t.child_id_type) as idtype");
		sql.append(",Get_Param_Chinese('C_CLASSM',t.child_class) as child_class");
		sql.append("  FROM busi_children_info t, pcmc_dept d");
		sql.append("  WHERE t.dept_code = d.deptcode");
		sql.append("  AND sta_flag='Y' AND t.del_flag='N'");
		sql.append("  AND t.child_id_number=?");
		try {
			pdao = new PlatformDao("szedu_minban",true);
			val.add(idCard);
			pdao.setSql(sql.toString());
			pdao.setBindValues(val);
			Element el = pdao.executeQuerySql(0, -1);
			List ls = el.getChildren("Record");
			if(ls.size()==0){
				respContent = "���������֤�������Ƿ�����";
			}else{
			    Element info = (Element)ls.get(0);
				respContent+= "������"+info.getChildTextTrim("username");
				respContent+= "\n��ݣ���Сѧ��";
				respContent+= "\n�Ա�"+info.getChildTextTrim("sex");
				respContent+= "\n�������ڣ�"+info.getChildTextTrim("child_birthday");
				respContent+= "\n֤�����ͣ�"+info.getChildTextTrim("idtype");
				respContent+= "\n֤�����룺\n"+info.getChildTextTrim("usercode");
				respContent+= "\n������"+info.getChildTextTrim("census");
				respContent+= "\n����ѧУ��"+info.getChildTextTrim("deptname");
				respContent+= "\n��У���ڣ�"+info.getChildTextTrim("child_in_date");
				respContent+= "\n���ڰ༶��"+info.getChildTextTrim("child_class");
				respContent+= "\n��ϵ�绰��"+info.getChildTextTrim("contact_tel");
			}
		} catch (Exception e) {
			log4j.logError("[��ȡ������Ϣ]"+e.getMessage());
			respContent = "���������֤�������Ƿ�����";
		} finally {
			pdao.releaseConnection();
		}
		return respContent;
	}
	
	/**
	 * ��ȡ������
	 * @param idCard
	 * @return
	 */
	public String getApproveResult(String idCard){
		if(StringUtil.isEmpty(idCard)) return "";
		
		PlatformDao pdao = null;
		ArrayList<String> val = new ArrayList<String>();
    	
		String respContent = "";
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT t.child_id_number AS usercode,t.child_name AS username,t.child_id_number AS idcard,t.approve_status,t.approve_date,t.child_tfold,d.deptname");
		sql.append(",Get_Param_Chinese('C_HJM',t.child_census) as census");
		sql.append(",Get_Param_Chinese('C_ZJLXM',t.child_id_type) as idtype");
		sql.append(",Get_Param_Chinese('C_RESULT_STATUS',t.approve_result) as approve_result");
		sql.append("   FROM busi_children_info t, pcmc_dept d");
		sql.append("  WHERE t.dept_code = d.deptcode");
		sql.append("  AND sta_flag='Y' AND t.del_flag='N'");
		sql.append("  AND t.child_id_number=?");
		try {
			pdao = new PlatformDao("szedu_minban",true);
			val.add(idCard);
			pdao.setSql(sql.toString());
			pdao.setBindValues(val);
			Element el = pdao.executeQuerySql(0, -1);
			List ls = el.getChildren("Record");
			if(ls.size()==0){
				respContent = "���������֤�������Ƿ�����";
			}else{
				Element info = (Element)ls.get(0);
				respContent+= "������"+info.getChildTextTrim("username");
				respContent+= "\n��ݣ���Сѧ��";
				respContent+= "\n֤�����ͣ�"+info.getChildTextTrim("idtype");
				respContent+= "\n֤�����룺\n"+info.getChildTextTrim("idcard");
				respContent+= "\n������"+info.getChildTextTrim("census");
				respContent+= "\n����ѧУ��"+info.getChildTextTrim("deptname");
				if("Y".equals(info.getChildTextTrim("child_tfold"))){
					respContent+= "\n��������"+(StringUtil.isEmpty(info.getChildTextTrim("approve_result"))?"������":info.getChildTextTrim("approve_result"));
				} else if("04".equals(info.getChildTextTrim("approve_status")) || "02".equals(info.getChildTextTrim("approve_status"))) {
					respContent+= "\n��������������";
				} else {
					if("".equals(info.getChildTextTrim("approve_date"))){
						respContent+= "\n�����������ϸ�";
					} else {
						respContent+= "\n��������"+(StringUtil.isEmpty(info.getChildTextTrim("approve_result"))?"������":info.getChildTextTrim("approve_result"));
					}
				}
			}
		} catch (Exception e) {
			log4j.logError("[��ȡ������]"+e.getMessage());
			respContent = "���������֤�������Ƿ�����";
		} finally {
			pdao.releaseConnection();
		}
		return respContent;
	}
	
	/**
	 * ��ȡ����������
	 * @param idCard
	 * @return
	 */
	public String getApproveResultList(String idCard){
		if(StringUtil.isEmpty(idCard)) return "";
		
		PlatformDao pdao = null;
		ArrayList<String> val = new ArrayList<String>();
    	
		String respContent = "";
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT t.child_id_number AS usercode,t.child_name AS username,t.CHILD_ID_NUMBER AS idcard");
		sql.append(",d.opertor,d.approve_remark");
		sql.append(",Get_Param_Chinese('C_RESULT_STATUS',d.approve_result) as approve_result");
		sql.append(",Get_Param_Chinese('C_MATERIAL',d.subject) as material");
		sql.append(",to_char(d.operate_date,'yyyy-mm-dd hh24:mm') as operate_date");
		sql.append("  FROM busi_children_info t, busi_children_result d");
		sql.append("  WHERE t.ci_id = d.ci_id AND d.cr_status='1' ");
		sql.append("  	AND sta_flag='Y' AND t.del_flag='N'");
		sql.append("  	AND t.child_id_number=?");
		sql.append("  ORDER BY t.CHILD_ID_NUMBER,d.operate_date DESC");
		try {
			pdao = new PlatformDao("szedu_minban",true);
			val.add(idCard);
			pdao.setSql(sql.toString());
			pdao.setBindValues(val);
			Element el = pdao.executeQuerySql(0, -1);
			List ls = el.getChildren("Record");
			if(ls.size()==0){
				respContent = "δ��ѯ�����������飡";
			}else{
				for(int i=0;i<ls.size();i++){
					Element info = (Element)ls.get(i);
					if(StringUtil.isEmpty(respContent)){
						respContent+= info.getChildTextTrim("username")+"�Ĳ��Ϻ��������£�\n";
					} else {
						respContent+= "\n--------\n";
					}
					respContent+= "�������ݣ�"+info.getChildTextTrim("material");
					respContent+= "\n�����ˣ�"+info.getChildTextTrim("opertor");
					respContent+= "\n����ʱ�䣺"+info.getChildTextTrim("operate_date");
					respContent+= "\n��������"+info.getChildTextTrim("approve_result");
					if(StringUtil.isNotEmpty(info.getChildTextTrim("approve_remark"))){
						respContent+= "\n��ע��"+info.getChildTextTrim("approve_remark");
					}
				}
			}
			if(StringUtil.isEmpty(respContent)){
				respContent = "δ��ѯ�����������飡";
			}
		} catch (Exception e) {
			log4j.logError("[��ȡ����������]"+e.getMessage());
			respContent = "δ��ѯ�����������飡";
		} finally {
			pdao.releaseConnection();
		}
		return respContent;
	}
		
	/**
	 * ��������
	 * @param idCard
	 * @return
	 */
	public String resetPW(String idCard){
		if(StringUtil.isEmpty(idCard)) return "";
		
		PlatformDao pdao = null;
		ArrayList<String> val = new ArrayList<String>();
		
		String respContent = "";
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT t.ci_id,t.child_id_number as usercode,t.login_passwd as userpwd");
		sql.append("  FROM busi_children_info t");
		sql.append(" WHERE sta_flag='Y' and t.del_flag='N'");
		sql.append("  AND t.child_id_number=?");
		try {
			pdao = new PlatformDao("szedu_minban",true);
			val.add(idCard);
			pdao.setSql(sql.toString());
			pdao.setBindValues(val);
			Element el = pdao.executeQuerySql(0, -1);
			List ls = el.getChildren("Record");
			if(ls.size()==0){
				respContent = "���������֤�������Ƿ�����";
			}else{
				Element info = (Element)ls.get(0);
				String ci_id = info.getChildTextTrim("ci_id");					
				String usercode = info.getChildTextTrim("usercode");					
				String passWord = "";//���루���֤�ź���λ��
				String passWordEncode = "";//���루���֤�ź���λ��
	    		if(StringUtil.isNotEmpty(usercode)){
	    			usercode = usercode.toUpperCase().replace("��", "(").replace("��", ")");		                
		            if(usercode.length() > 6){
		            	passWord = usercode.substring(usercode.length() - 6, usercode.length());
		            } else {
		            	passWord = "123456";
		            }
		            passWordEncode = PasswordEncoder.encode(passWord);
		            
		            val.clear();
					val.add(passWordEncode);
					val.add(ci_id);
					pdao.setSql("UPDATE busi_children_info SET lastloginerrdate=SYSDATE,loginerrtimes=1,login_passwd=? WHERE ci_id=?");
					pdao.setBindValues(val);
			    	long modifyNum = pdao.executeTransactionSql();
			    	if(modifyNum!=0){
			    		respContent = "��������ɹ���\n������Ϊ��"+passWord+"�����μ������룡";
			    	} else {
			    		respContent = "��������ʧ�ܣ������ԣ�";
			    	}
	    		}						
			}
		} catch (Exception e) {
			log4j.logError("[��������]"+e.getMessage());
			respContent = "���������֤�������Ƿ�����";
		} finally {
			pdao.releaseConnection();
		}
		return respContent;
	}
	
	/**
	 * ��֤�˻��Ƿ���Ч
	 * @param contents
	 * @return
	 */
	public String validationAccount(String[] contents){		
		String respContent = "";
		
		PlatformDao pdao = null;
		ArrayList<String> val = new ArrayList<String>();    	
		String idcard = contents[2];
		String mobile = contents[3];
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT t.ci_id ");
		sql.append("  FROM busi_children_info t");
		sql.append(" WHERE sta_flag='Y' and t.del_flag='N'");
		sql.append("  AND t.contact_tel=? AND t.child_id_number=?");
		try {
			pdao = new PlatformDao("szedu_minban",true);
			val.add(mobile);
			val.add(idcard);
			pdao.setSql(sql.toString());
			pdao.setBindValues(val);
			Element el = pdao.executeQuerySql(0, -1);
			List ls = el.getChildren("Record");
			if(ls.size()==0){
				respContent = "���������֤���������ϵ�绰�Ƿ�����";
			} else {
				respContent = "OK";
			}
		} catch (Exception e) {
			log4j.logError("[��֤�˻��Ƿ���Ч]"+e.getMessage());
			respContent = "���������֤���������ϵ�绰�Ƿ�����";
		} finally {
			pdao.releaseConnection();
		}
		return respContent;
	}
}
