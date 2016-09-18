package cn.com.bright.edu.weixin.core.impl;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;

import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.util.PasswordEncoder;
import cn.brightcom.jraf.util.StringUtil;

public class TeacherMessageImpl extends MessageImpl{
	/**
	 * ��ȡ������Ϣ
	 * @param idCard
	 * @return
	 */
	public String getBaseInfo(String idCard){
		if(StringUtil.isEmpty(idCard)) return "";
		
		String respContent = "";
		PlatformDao pdao = null;
		ArrayList<String> val = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT t.teacher_id AS userid,t.id_number AS usercode,t.name AS username,d.deptname,t.contact_tel");
		sql.append(",TO_CHAR(t.birthday,'yyyy-mm-dd') AS birthday");
		sql.append(",TO_CHAR(t.in_date,'yyyy-mm-dd') AS in_date");
		sql.append(",Get_Param_Chinese('C_SEX',t.sex) AS sex");
		sql.append(",Get_Param_Chinese('C_HJM',t.census) as census");
		sql.append(",Get_Param_Chinese('C_ZJLXM',t.id_type) as idtype");
		sql.append("  FROM busi_teacher_info t, pcmc_dept d");
		sql.append("  WHERE t.dept_code = d.deptcode");
		sql.append("  AND t.del_flag='N'");
		sql.append("  AND t.id_number=?");
		try {
			pdao = new PlatformDao();
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
				respContent+= "\n��ݣ���ʦ";
				respContent+= "\n�Ա�"+info.getChildTextTrim("sex");
				respContent+= "\n�������ڣ�"+info.getChildTextTrim("birthday");
				respContent+= "\n֤�����ͣ�"+info.getChildTextTrim("idtype");
				respContent+= "\n֤�����룺\n"+info.getChildTextTrim("usercode");
				respContent+= "\n������"+info.getChildTextTrim("census");
				respContent+= "\n����ѧУ��"+info.getChildTextTrim("deptname");
				respContent+= "\n��У���ڣ�"+info.getChildTextTrim("in_date");
				respContent+= "\n��ϵ�绰��"+info.getChildTextTrim("contact_tel");
			}
		} catch (Exception e) {
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
		
		String respContent = "";
		PlatformDao pdao = null;
		ArrayList<String> val = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT t.id_number AS usercode,t.name AS username,t.id_number AS idcard,t.qjyj_audit_opinion,d.deptname");
		sql.append(",Get_Param_Chinese('C_HJM',t.census) as census");
		sql.append(",Get_Param_Chinese('C_ZJLXM',t.id_type) as idtype");
		sql.append(",Get_Param_Chinese('C_RESULT_STATUS',t.approve_result) as approve_result");
		sql.append("   FROM busi_teacher_info t, pcmc_dept d");
		sql.append("  WHERE t.dept_code = d.deptcode");
		sql.append("  AND t.del_flag='N'");
		sql.append("  AND t.id_number=?");
		try {
			pdao = new PlatformDao();
			val.add(idCard);
			pdao.setSql(sql.toString());
			pdao.setBindValues(val);
			Element el = pdao.executeQuerySql(0, -1);
			List ls = el.getChildren("Record");
			if(ls.size()==0){
				respContent = "���������֤�������Ƿ�����";
			}else{
				String approve_result = "";
				Element info = (Element)ls.get(0);
				approve_result = info.getChildTextTrim("approve_result");
				respContent+= "������"+info.getChildTextTrim("username");
				respContent+= "\n��ݣ���ʦ";
				respContent+= "\n֤�����ͣ�"+info.getChildTextTrim("idtype");
				respContent+= "\n֤�����룺\n"+info.getChildTextTrim("idcard");
				respContent+= "\n������"+info.getChildTextTrim("census");
				respContent+= "\n����ѧУ��"+info.getChildTextTrim("deptname");
				respContent+= "\n��������"+(StringUtil.isEmpty(approve_result)?"������":approve_result);
				if("N".equals(approve_result)){
					respContent+= "\n����������������������"+info.getChildTextTrim("qjyj_audit_opinion");
				}
			}
		} catch (Exception e) {
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
		return getApproveResult(idCard);
		/*
		String respContent = "";
		PlatformDao pdao = null;
		ArrayList<String> val = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT t.id_number AS usercode,t.name AS username,t.id_number AS idcard");
		sql.append(",d.opertor,d.approve_remark");
		sql.append(",Get_Param_Chinese('C_RESULT_STATUS',d.approve_result) as approve_result");
		sql.append(",Get_Param_Chinese('C_MATERIAL',d.subject) as material");
		sql.append(",to_char(d.operate_date,'yyyy-mm-dd hh24:mm') as operate_date");
		sql.append("  FROM busi_teacher_info t, busi_children_result d");
		sql.append("  WHERE t.ci_id = d.ci_id");
		sql.append("  	AND t.del_flag='N'");
		sql.append("  	AND t.id_number=?");
		sql.append("  ORDER BY t.id_number,d.operate_date DESC");
		try {
			pdao = new PlatformDao();
			val.add(idCard);
			pdao.setSql(sql.toString());
			pdao.setBindValues(val);
			Element el = pdao.executeQuerySql(0, -1);
			List ls = el.getChildren("Record");
			if(ls.size()==0){
				respContent = "δ��ѯ������������";
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
				}
			}
		} catch (Exception e) {
			respContent = "δ��ѯ������������";
		} finally {
			pdao.releaseConnection();
		}
		return respContent;*/
	}
		
	/**
	 * ��������
	 * @param idCard
	 * @return
	 */
	public String resetPW(String idCard){
		if(StringUtil.isEmpty(idCard)) return "";
		
		String respContent = "";
		PlatformDao pdao = null;
		StringBuffer sql = new StringBuffer();
		ArrayList<String> val = new ArrayList<String>();
		sql.append("SELECT t.teacher_id,t.id_number as usercode,t.login_passwd as userpwd");
		sql.append("  FROM busi_teacher_info t");
		sql.append(" WHERE t.del_flag='N'");
		sql.append("  AND t.id_number=?");
		try {
			pdao = new PlatformDao();
			val.add(idCard);
			pdao.setSql(sql.toString());
			pdao.setBindValues(val);
			Element el = pdao.executeQuerySql(0, -1);
			List ls = el.getChildren("Record");
			if(ls.size()==0){
				respContent = "���������֤�������Ƿ�����";
			}else{
				Element info = (Element)ls.get(0);
				String teacher_id = info.getChildTextTrim("teacher_id");					
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
					val.add(teacher_id);
					pdao.setSql("UPDATE busi_teacher_info SET lastloginerrdate=sysdate,loginerrtimes=1,login_passwd=? WHERE teacher_id=?");
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
			e.printStackTrace();
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
		String idcard = contents[2];
		String mobile = contents[3];
		PlatformDao pdao = null;
		StringBuffer sql = new StringBuffer();
		ArrayList<String> val = new ArrayList<String>();
		sql.append("SELECT t.teacher_id ");
		sql.append("  FROM busi_teacher_info t");
		sql.append(" WHERE t.del_flag='N'");
		sql.append("  AND t.contact_tel=? AND t.id_number=?");
		try {
			pdao = new PlatformDao();
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
			respContent = "���������֤���������ϵ�绰�Ƿ�����";
		}
		return respContent;
	}
}
