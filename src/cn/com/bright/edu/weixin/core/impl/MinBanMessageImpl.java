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
	 * 获取基本信息
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
				respContent = "请检查输入的证件号码是否有误！";
			}else{
			    Element info = (Element)ls.get(0);
				respContent+= "姓名："+info.getChildTextTrim("username");
				respContent+= "\n身份：中小学生";
				respContent+= "\n性别："+info.getChildTextTrim("sex");
				respContent+= "\n出生日期："+info.getChildTextTrim("child_birthday");
				respContent+= "\n证件类型："+info.getChildTextTrim("idtype");
				respContent+= "\n证件号码：\n"+info.getChildTextTrim("usercode");
				respContent+= "\n户籍："+info.getChildTextTrim("census");
				respContent+= "\n所在学校："+info.getChildTextTrim("deptname");
				respContent+= "\n入校日期："+info.getChildTextTrim("child_in_date");
				respContent+= "\n所在班级："+info.getChildTextTrim("child_class");
				respContent+= "\n联系电话："+info.getChildTextTrim("contact_tel");
			}
		} catch (Exception e) {
			log4j.logError("[获取基本信息]"+e.getMessage());
			respContent = "请检查输入的证件号码是否有误！";
		} finally {
			pdao.releaseConnection();
		}
		return respContent;
	}
	
	/**
	 * 获取核验结果
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
				respContent = "请检查输入的证件号码是否有误！";
			}else{
				Element info = (Element)ls.get(0);
				respContent+= "姓名："+info.getChildTextTrim("username");
				respContent+= "\n身份：中小学生";
				respContent+= "\n证件类型："+info.getChildTextTrim("idtype");
				respContent+= "\n证件号码：\n"+info.getChildTextTrim("idcard");
				respContent+= "\n户籍："+info.getChildTextTrim("census");
				respContent+= "\n所在学校："+info.getChildTextTrim("deptname");
				if("Y".equals(info.getChildTextTrim("child_tfold"))){
					respContent+= "\n核验结果："+(StringUtil.isEmpty(info.getChildTextTrim("approve_result"))?"待核验":info.getChildTextTrim("approve_result"));
				} else if("04".equals(info.getChildTextTrim("approve_status")) || "02".equals(info.getChildTextTrim("approve_status"))) {
					respContent+= "\n核验结果：待核验";
				} else {
					if("".equals(info.getChildTextTrim("approve_date"))){
						respContent+= "\n核验结果：不合格";
					} else {
						respContent+= "\n核验结果："+(StringUtil.isEmpty(info.getChildTextTrim("approve_result"))?"待核验":info.getChildTextTrim("approve_result"));
					}
				}
			}
		} catch (Exception e) {
			log4j.logError("[获取核验结果]"+e.getMessage());
			respContent = "请检查输入的证件号码是否有误！";
		} finally {
			pdao.releaseConnection();
		}
		return respContent;
	}
	
	/**
	 * 获取核验结果详情
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
				respContent = "未查询到核验结果详情！";
			}else{
				for(int i=0;i<ls.size();i++){
					Element info = (Element)ls.get(i);
					if(StringUtil.isEmpty(respContent)){
						respContent+= info.getChildTextTrim("username")+"的材料核验结果如下：\n";
					} else {
						respContent+= "\n--------\n";
					}
					respContent+= "核验内容："+info.getChildTextTrim("material");
					respContent+= "\n核验人："+info.getChildTextTrim("opertor");
					respContent+= "\n核验时间："+info.getChildTextTrim("operate_date");
					respContent+= "\n核验结果："+info.getChildTextTrim("approve_result");
					if(StringUtil.isNotEmpty(info.getChildTextTrim("approve_remark"))){
						respContent+= "\n备注："+info.getChildTextTrim("approve_remark");
					}
				}
			}
			if(StringUtil.isEmpty(respContent)){
				respContent = "未查询到核验结果详情！";
			}
		} catch (Exception e) {
			log4j.logError("[获取核验结果详情]"+e.getMessage());
			respContent = "未查询到核验结果详情！";
		} finally {
			pdao.releaseConnection();
		}
		return respContent;
	}
		
	/**
	 * 重置密码
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
				respContent = "请检查输入的证件号码是否有误！";
			}else{
				Element info = (Element)ls.get(0);
				String ci_id = info.getChildTextTrim("ci_id");					
				String usercode = info.getChildTextTrim("usercode");					
				String passWord = "";//密码（身份证号后六位）
				String passWordEncode = "";//密码（身份证号后六位）
	    		if(StringUtil.isNotEmpty(usercode)){
	    			usercode = usercode.toUpperCase().replace("（", "(").replace("）", ")");		                
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
			    		respContent = "重置密码成功！\n新密码为："+passWord+"，请牢记新密码！";
			    	} else {
			    		respContent = "重置密码失败，请重试！";
			    	}
	    		}						
			}
		} catch (Exception e) {
			log4j.logError("[重置密码]"+e.getMessage());
			respContent = "请检查输入的证件号码是否有误！";
		} finally {
			pdao.releaseConnection();
		}
		return respContent;
	}
	
	/**
	 * 验证账户是否有效
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
				respContent = "请检查输入的证件号码或联系电话是否有误！";
			} else {
				respContent = "OK";
			}
		} catch (Exception e) {
			log4j.logError("[验证账户是否有效]"+e.getMessage());
			respContent = "请检查输入的证件号码或联系电话是否有误！";
		} finally {
			pdao.releaseConnection();
		}
		return respContent;
	}
}
