package cn.com.bright.yuexue.base;

import java.io.File;
import java.sql.Types;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.jdom.Document;
import org.jdom.Element;

import cn.brightcom.jraf.conf.BrightComConfig;
import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.util.ConfigDocument;
import cn.brightcom.jraf.util.DaoUtil;
import cn.brightcom.jraf.util.DatetimeUtil;
import cn.brightcom.jraf.util.FileUtil;
import cn.brightcom.jraf.util.ImageUtils;
import cn.brightcom.jraf.util.JDomUtil;
import cn.brightcom.jraf.util.Log;
import cn.brightcom.jraf.util.PasswordEncoder;
import cn.brightcom.jraf.util.StringUtil;
import cn.brightcom.jraf.util.UriUtil;
import cn.brightcom.jraf.util.XmlDocPkgUtil;
import cn.brightcom.system.pcmc.util.PcmcUtil;

/**
 * 
 * <p>Title: 用户管理</p>
 * <p>Description: 学生/教师/家长</p>
 * <p>Copyright: Copyright (c) 2014 Brightcom</p>
 * <p>Company: ShenZhen BrightCom Technology Co.,Ltd.</p>
 * @author <a href="http://www.szbrightcom.cn" target="_blank">QIUMH</a>
 * @version 1.0a
 */
public class UserManage
{
	protected final static String[] CHK_FIELDS = {"usercode","idnumber","mobile","email"};
	protected final static String[] CHK_BINDS = {null,null,"mobilebind","emailbind"};
	protected final static String PORTRAIT_PATH=BrightComConfig.getConfiguration().getString("user.portrait", "upload/portrait/");
	
	private XmlDocPkgUtil xmlDocUtil = null;
    
    private static Log log4j = new Log(UserManage.class.getName());
    
    public Document doPost(Document xmlDoc)
    {
    	xmlDocUtil = new XmlDocPkgUtil(xmlDoc);
    	String action = xmlDocUtil.getAction();
    	
    	if("add".equals(action))//新增机构管理员
    	{
    		add();
    	}
    	else if("upt".equals(action))//修改机构管理员
    	{
    		upt();
    	}
    	else if("del".equals(action))
    	{
    		del();
    	}
    	else if("uptinfo".equals(action))//修改个人信息
    	{
    		uptinfo();
    	}
    	else if("reset".equals(action))//管理员 重置密码
    	{
    		reset();
    	}
    	else if("chgpwd".equals(action))//修改密码
    	{
    		chgpwd();
    	}
    	else if("chgmobile".equals(action))//修改手机号
    	{
    		chgmobile();
    	}
    	else if("chgmail".equals(action))//修改邮箱
    	{
    		chgmail();
    	}
    	else if("chkValid".equals(action))//校验是否唯一
    	{
    		chkValid();
    	}
    	else if("detail".equals(action))
    	{
    		getCurUserDetail();
    	}
    	else if("getMRoleList".equals(action))
    	{
    		getMRoleList();
    	}
    	else if("getUserDetail".equals(action))
    	{
    		getUserDetail();
    	}
    	else if("getUserGrowthDetail".equals(action)){
    		
    		getUserGrowthDetail();
    	}
         else if("getUserGrowthDetailRecode".equals(action)){
    		
        	 getUserGrowthDetailRecode();
    	}
         else if("getResourceGrowthDetailRecode".equals(action)){
        	 
        	 getResourceGrowthDetailRecode();
         }
         else if("getResourceGrowthDetail".equals(action)){
        	 
        	 getResourceGrowthDetail();
         }
         else if("getTeacherResourceDetail".equals(action)){
        	 
        	 getTeacherResourceDetail();
         }
         else if("getTeacherResourceSubjectRecode".equals(action)){
        	 
        	 getTeacherResourceSubjectRecode();
         }
         else if("getTeacherResourceStyleRecode".equals(action)){
        	 
        	 getTeacherResourceStyleRecode();
         }
         else if("studyHabitDetail".equals(action)){
        	 
        	 studyHabitDetail();
         }
         else if("studyHabitDetailRecord".equals(action)){
        	 
        	 studyHabitDetailRecord();
         }
    	return xmlDoc;
    }
    
    //学习时间段分析
    private void studyHabitDetailRecord() {
    	Element reqElement = xmlDocUtil.getRequestData();
    	String  deptcode = xmlDocUtil.getSession().getChildText("deptcode");
		String qry_deptcode = reqElement.getChildText("qry_deptcode");
		String qry_grade = reqElement.getChildText("qry_grade");
		String qry_username = reqElement.getChildText("qry_username");
		String qry_year = reqElement.getChildText("qry_years");
		String qry_userid = reqElement.getChildText("qry_userid");
		
		
		if (StringUtil.isEmpty(qry_deptcode)){
			qry_deptcode = deptcode; 
		}
		StringBuffer strSQL = new StringBuffer();
		ArrayList<Object> paramList = new ArrayList<Object>();
		strSQL.append("select a.deptname,a.deptcode,a.userid,a.username, ");
		strSQL.append("ifnull(sum(b.comment_time_01),0) as comment_time_01, ");
		strSQL.append("ifnull(sum(b.comment_time_02),0) as comment_time_02, ");
		strSQL.append("ifnull(sum(b.comment_time_03),0) as comment_time_03, ");
		strSQL.append("ifnull(sum(b.comment_time_04),0) as comment_time_04, ");
		strSQL.append("ifnull(sum(b.comment_time_05),0) as comment_time_05, ");
		strSQL.append("ifnull(sum(b.comment_time_06),0) as comment_time_06, ");
		strSQL.append("ifnull(sum(b.comment_time_07),0) as comment_time_07, ");
		strSQL.append("ifnull(sum(c.submit_time_01),0) as submit_time_01, ");
		strSQL.append("ifnull(sum(c.submit_time_02),0) as submit_time_02, ");
		strSQL.append("ifnull(sum(c.submit_time_03),0) as submit_time_03, ");
		strSQL.append("ifnull(sum(c.submit_time_04),0) as submit_time_04, ");
		strSQL.append("ifnull(sum(c.submit_time_05),0) as submit_time_05, ");
		strSQL.append("ifnull(sum(c.submit_time_06),0) as submit_time_06, ");
		strSQL.append("ifnull(sum(c.submit_time_07),0) as submit_time_07, ");
		strSQL.append("ifnull(sum(d.login_time_01),0) as login_time_01, ");
		strSQL.append("ifnull(sum(d.login_time_02),0) as login_time_02, ");
		strSQL.append("ifnull(sum(d.login_time_03),0) as login_time_03, ");
		strSQL.append("ifnull(sum(d.login_time_04),0) as login_time_04, ");
		strSQL.append("ifnull(sum(d.login_time_05),0) as login_time_05, ");
		strSQL.append("ifnull(sum(d.login_time_06),0) as login_time_06, ");
		strSQL.append("ifnull(sum(d.login_time_07),0) as login_time_07, ");
		strSQL.append("ifnull(sum(e.read_time_01),0) as read_time_01, ");
		strSQL.append("ifnull(sum(e.read_time_02),0) as read_time_02, ");
		strSQL.append("ifnull(sum(e.read_time_03),0) as read_time_03, ");
		strSQL.append("ifnull(sum(e.read_time_04),0) as read_time_04, ");
		strSQL.append("ifnull(sum(e.read_time_05),0) as read_time_05, ");
		strSQL.append("ifnull(sum(e.read_time_06),0) as read_time_06, ");
		strSQL.append("ifnull(sum(e.read_time_07),0) as read_time_07 ");
		strSQL.append("from (select t3.deptname,t3.deptcode,t1.userid,t1.username  ");
		strSQL.append("from pcmc_user t1,pcmc_user_dept t2,pcmc_dept t3,base_studentinfo t4 ");
		strSQL.append("where t1.userid = t2.userid and t2.deptid = t3.deptid and t1.userid = t4.userid and t3.state = '1' ");
		strSQL.append("	and t3.deptcode like ? ");
		paramList.add(qry_deptcode+"%");
		
		if(StringUtil.isNotEmpty(qry_userid)){
			strSQL.append(" and t1.userid=? ");
			paramList.add(qry_userid);
		}
		if(StringUtil.isNotEmpty(qry_username)){
			strSQL.append(" and t1.username like ?");
			paramList.add("%"+qry_username+"%");
		}
		strSQL.append(") a left join (select tt1.userid,");
		
		strSQL.append("COUNT(case when tt1.comment_time between str_to_date('00:00:01', '%H:%i:%s') and str_to_date('08:00:00', '%H:%i:%s') then 1 else null end) as comment_time_01,");
		strSQL.append("COUNT(case when tt1.comment_time between str_to_date('08:00:01', '%H:%i:%s') and str_to_date('12:00:00', '%H:%i:%s') then 1 else null end) as comment_time_02,");
		strSQL.append("COUNT(case when tt1.comment_time between str_to_date('12:00:01', '%H:%i:%s') and str_to_date('14:00:00', '%H:%i:%s') then 1 else null end) as comment_time_03,");
		strSQL.append("COUNT(case when tt1.comment_time between str_to_date('14:00:01', '%H:%i:%s') and str_to_date('18:00:00', '%H:%i:%s') then 1 else null end) as comment_time_04, ");
		strSQL.append("COUNT(case when tt1.comment_time between str_to_date('18:00:01', '%H:%i:%s') and str_to_date('20:00:00', '%H:%i:%s') then 1 else null end) as comment_time_05, ");
		strSQL.append("COUNT(case when tt1.comment_time between str_to_date('20:00:01', '%H:%i:%s') and str_to_date('22:00:00', '%H:%i:%s') then 1 else null end) as comment_time_06, ");
		strSQL.append("COUNT(case when tt1.comment_time between str_to_date('22:00:01', '%H:%i:%s') and str_to_date('23:59:59', '%H:%i:%s') then 1 else null end) as comment_time_07 ");
		strSQL.append("from (select str_to_date(DATE_FORMAT(ct.create_date,'%H:%i:%s'), '%H:%i:%s') as comment_time,ct.userid from learn_comment ct where ct.valid = 'Y' ");
		if(StringUtil.isNotEmpty(qry_userid)){
			strSQL.append(" and ct.userid=? ");
			paramList.add(qry_userid);
		}
		if(StringUtil.isNotEmpty(qry_year)){
			strSQL.append(" and DATE_FORMAT(ct.create_date, '%Y')=? ");
			paramList.add(qry_year);
		}
		strSQL.append(") tt1 group by userid ");
		strSQL.append(") b on a.userid = b.userid left join (select tt1.userid, ");
		
		strSQL.append("COUNT(case when tt1.submit_time between str_to_date('00:00:01', '%H:%i:%s') and str_to_date('08:00:00', '%H:%i:%s') then 1 else null end) as submit_time_01, ");
		strSQL.append("COUNT(case when tt1.submit_time between str_to_date('08:00:01', '%H:%i:%s') and str_to_date('12:00:00', '%H:%i:%s') then 1 else null end) as submit_time_02, ");
		strSQL.append("COUNT(case when tt1.submit_time between str_to_date('12:00:01', '%H:%i:%s') and str_to_date('14:00:00', '%H:%i:%s') then 1 else null end) as submit_time_03, ");
		strSQL.append("COUNT(case when tt1.submit_time between str_to_date('14:00:01', '%H:%i:%s') and str_to_date('18:00:00', '%H:%i:%s') then 1 else null end) as submit_time_04, ");
		strSQL.append("COUNT(case when tt1.submit_time between str_to_date('18:00:01', '%H:%i:%s') and str_to_date('20:00:00', '%H:%i:%s') then 1 else null end) as submit_time_05, ");
		strSQL.append("COUNT(case when tt1.submit_time between str_to_date('20:00:01', '%H:%i:%s') and str_to_date('22:00:00', '%H:%i:%s') then 1 else null end) as submit_time_06, ");
		strSQL.append("COUNT(case when tt1.submit_time between str_to_date('22:00:01', '%H:%i:%s') and str_to_date('23:59:59', '%H:%i:%s') then 1 else null end) as submit_time_07 ");
		strSQL.append("from (select str_to_date(DATE_FORMAT(me.create_date,'%H:%i:%s'), '%H:%i:%s') as submit_time,me.userid from learn_my_examination me where me.valid = 'Y' and me.status > '10' ");
		if(StringUtil.isNotEmpty(qry_userid)){
			strSQL.append(" and me.userid = ? ");
			paramList.add(qry_userid);
		}
		if(StringUtil.isNotEmpty(qry_year)){
			strSQL.append(" and DATE_FORMAT(me.create_date, '%Y')=? ");
			paramList.add(qry_year);
		}
		strSQL.append(") tt1 group by userid ");
		strSQL.append(") c on a.userid = c.userid  left join  (select tt1.userid,");
		
		strSQL.append("COUNT(case when tt1.login_time between str_to_date('00:00:01', '%H:%i:%s') and str_to_date('08:00:00', '%H:%i:%s') then 1 else null end) as login_time_01, ");
		strSQL.append("COUNT(case when tt1.login_time between str_to_date('08:00:01', '%H:%i:%s') and str_to_date('12:00:00', '%H:%i:%s') then 1 else null end) as login_time_02, ");
		strSQL.append("COUNT(case when tt1.login_time between str_to_date('12:00:01', '%H:%i:%s') and str_to_date('14:00:00', '%H:%i:%s') then 1 else null end) as login_time_03, ");
		strSQL.append("COUNT(case when tt1.login_time between str_to_date('14:00:01', '%H:%i:%s') and str_to_date('18:00:00', '%H:%i:%s') then 1 else null end) as login_time_04, ");
		strSQL.append("COUNT(case when tt1.login_time between str_to_date('18:00:01', '%H:%i:%s') and str_to_date('20:00:00', '%H:%i:%s') then 1 else null end) as login_time_05, ");
		strSQL.append("COUNT(case when tt1.login_time between str_to_date('20:00:01', '%H:%i:%s') and str_to_date('22:00:00', '%H:%i:%s') then 1 else null end) as login_time_06, ");
		strSQL.append("COUNT(case when tt1.login_time between str_to_date('22:00:01', '%H:%i:%s') and str_to_date('23:59:59', '%H:%i:%s') then 1 else null end) as login_time_07 ");
		strSQL.append("from (select str_to_date(DATE_FORMAT(log.logindt,'%H:%i:%s'), '%H:%i:%s') as login_time,log.userid from pcmc_uslogin_log log where log.state = '1' ");
		if(StringUtil.isNotEmpty(qry_userid)){
			strSQL.append(" and log.userid = ? ");
			paramList.add(qry_userid);
		}
		if(StringUtil.isNotEmpty(qry_year)){
			strSQL.append(" and DATE_FORMAT(log.logindt, '%Y')=? ");
			paramList.add(qry_year);
		}
		strSQL.append(") tt1 group by userid ");
		strSQL.append(") d on a.userid = d.userid  left join  (select tt1.userid, ");
		
		strSQL.append("COUNT(case when tt1.read_time between str_to_date('00:00:01', '%H:%i:%s') and str_to_date('08:00:00', '%H:%i:%s') then 1 else null end) as read_time_01, ");
		strSQL.append("COUNT(case when tt1.read_time between str_to_date('08:00:01', '%H:%i:%s') and str_to_date('12:00:00', '%H:%i:%s') then 1 else null end) as read_time_02, ");
		strSQL.append("COUNT(case when tt1.read_time between str_to_date('12:00:01', '%H:%i:%s') and str_to_date('14:00:00', '%H:%i:%s') then 1 else null end) as read_time_03, ");
		strSQL.append("COUNT(case when tt1.read_time between str_to_date('14:00:01', '%H:%i:%s') and str_to_date('18:00:00', '%H:%i:%s') then 1 else null end) as read_time_04, ");
		strSQL.append("COUNT(case when tt1.read_time between str_to_date('18:00:01', '%H:%i:%s') and str_to_date('20:00:00', '%H:%i:%s') then 1 else null end) as read_time_05, ");
		strSQL.append("COUNT(case when tt1.read_time between str_to_date('20:00:01', '%H:%i:%s') and str_to_date('22:00:00', '%H:%i:%s') then 1 else null end) as read_time_06, ");
		strSQL.append("COUNT(case when tt1.read_time between str_to_date('22:00:01', '%H:%i:%s') and str_to_date('23:59:59', '%H:%i:%s') then 1 else null end) as read_time_07 ");
		strSQL.append("from (select str_to_date(DATE_FORMAT(rl.create_date,'%H:%i:%s'), '%H:%i:%s') as read_time,rl.userid from sell_read_log rl where rl.valid = 'Y' ");
		if(StringUtil.isNotEmpty(qry_userid)){
			strSQL.append(" and rl.userid = ? ");
			paramList.add(qry_userid);
		}
		if(StringUtil.isNotEmpty(qry_year)){
			strSQL.append(" and DATE_FORMAT(rl.create_date, '%Y')=? ");
			paramList.add(qry_year);
		}
		strSQL.append(") tt1 group by userid ");
		strSQL.append(") e on a.userid = e.userid  ");
		
		strSQL.append(" group by a.deptname,a.deptcode ");
		PlatformDao pdao = new PlatformDao();
		try {
			pdao.setSql(strSQL.toString());
			pdao.setBindValues(paramList);
			Element resData = pdao.executeQuerySql(0, -1);
			xmlDocUtil.getResponse().addContent(resData);
			xmlDocUtil.setResult("0");
		}catch (Exception e) {
			e.printStackTrace();
			log4j.logError("[学习时间段分析情况]"+e.getMessage());
		} finally {
			pdao.releaseConnection();
		}
    	
	}
    
	//学习习惯分析
    private void studyHabitDetail() {
    	Element reqElement = xmlDocUtil.getRequestData();
    	String  deptcode = xmlDocUtil.getSession().getChildText("deptcode");
		String qry_deptcode = reqElement.getChildText("qry_deptcode");
		String qry_grade = reqElement.getChildText("qry_grade");
		String qry_username = reqElement.getChildText("qry_username");
		String qry_year = reqElement.getChildText("qry_years");
		
		
		if (StringUtil.isEmpty(qry_deptcode)){
			qry_deptcode = deptcode; 
		}
		StringBuffer strSQL = new StringBuffer();
		ArrayList<Object> paramList = new ArrayList<Object>();
		strSQL.append("select t3.deptname,t1.username,t5.classnm,t1.userid,t3.deptcode, ");
		strSQL.append(" (select count(*) from learn_comment st where st.valid = 'Y' and st.userid = t1.userid ");
		if(StringUtil.isNotEmpty(qry_year)){
			strSQL.append(" and DATE_FORMAT(st.create_date, '%Y')=? ");
			paramList.add(qry_year);
		}
		strSQL.append(" ) as comment_count,");
		
		strSQL.append(" (select count(*) from learn_my_examination me where me.userid = t1.userid and me.valid = 'Y' and me.status > '10' ");
		if(StringUtil.isNotEmpty(qry_year)){
			strSQL.append(" and DATE_FORMAT(me.create_date, '%Y')=? ");
			paramList.add(qry_year);
		}
		strSQL.append(" ) as submit_count,");
		
		strSQL.append(" (SELECT count(*) FROM sell_read_log rl WHERE rl.userid = t1.userid and rl.valid = 'Y' ");
		if(StringUtil.isNotEmpty(qry_year)){
			strSQL.append(" and DATE_FORMAT(rl.create_date, '%Y')=? ");
			paramList.add(qry_year);
		}
		strSQL.append(" ) AS read_count,");
		
		strSQL.append(" (SELECT count(*) FROM pcmc_uslogin_log log WHERE log.userid = t1.userid and log.state = '1' ");
		if(StringUtil.isNotEmpty(qry_year)){
			strSQL.append(" and DATE_FORMAT(log.logindt, '%Y')=? ");
			paramList.add(qry_year);
		}
		strSQL.append(" ) AS login_count");
		
		strSQL.append(" from pcmc_user t1,pcmc_user_dept t2,pcmc_dept t3,base_studentinfo t4,base_class t5 ");
	    strSQL.append("	where t1.userid = t2.userid and t2.deptid = t3.deptid and t1.userid = t4.userid and t4.classid = t5.classid");
	    strSQL.append(" and t3.state = '1' ");
	    	
	    if(StringUtil.isNotEmpty(qry_username)){
	    	strSQL.append(" and t1.username like ?");
	    	paramList.add("%"+qry_username+"%");
	    }
	    
        strSQL.append("	and t3.deptcode like ? ");
        paramList.add(qry_deptcode+"%");
        
        strSQL.append(" order by t3.deptcode,t5.classnm,t1.username");
		PlatformDao pdao = new PlatformDao();
		try {
			pdao.setSql(strSQL.toString());
			pdao.setBindValues(paramList);
			Element resData = pdao.executeQuerySql(0, -1);
			xmlDocUtil.getResponse().addContent(resData);
			xmlDocUtil.setResult("0");
		}catch (Exception e) {
			e.printStackTrace();
			log4j.logError("[学习习惯分析情况]"+e.getMessage());
		} finally {
			pdao.releaseConnection();
		}
	}
    
	//获取教师资源学科记录
    private void getTeacherResourceSubjectRecode() {
    	Element reqElement = xmlDocUtil.getRequestData();
    	String  deptcode = xmlDocUtil.getSession().getChildText("deptcode");
		String qry_deptcode = reqElement.getChildText("qry_deptcode");
		String qry_userid = reqElement.getChildText("qry_userid");
		String qry_username = reqElement.getChildText("qry_username");
		String qry_years = reqElement.getChildText("qry_years");
		
		if (StringUtil.isEmpty(qry_deptcode)){
			qry_deptcode = deptcode; 
		}
		StringBuffer strSQL = new StringBuffer();
		ArrayList<Object> paramList = new ArrayList<Object>();
		strSQL.append("SELECT COUNT(t1.subject_id) AS total,t4.subjname ");
	    strSQL.append("	FROM learn_examination_paper t1,pcmc_user_dept t2,pcmc_dept t3,base_subject t4,pcmc_user t5");
	    strSQL.append(" WHERE t1.user_id = t2.userid and t1.user_id=t5.userid  AND t2.deptid = t3.deptid and t1.subject_id = t4.subjectid and t1.valid='Y' and t3.state='1'");
	    	
	    if(StringUtil.isNotEmpty(qry_years)){
	    	strSQL.append(" and DATE_FORMAT(t1.create_date, '%Y') = ?");
	    	paramList.add(qry_years);
	    }
	    
	    if(StringUtil.isNotEmpty(qry_username)){
	    	strSQL.append(" and t5.username like ?");
	    	paramList.add("%"+qry_username+"%");
	    }
	    
	    if(StringUtil.isNotEmpty(qry_userid)){
	    	strSQL.append(" and t1.user_id=?");
	    	paramList.add(qry_userid);
	    }
	    
        strSQL.append("	and t3.deptcode like ? ");
        paramList.add(qry_deptcode+"%");
        
        strSQL.append(" GROUP BY t1.subject_id order by t1.subject_id");
		PlatformDao pdao = new PlatformDao();
		try {
			pdao.setSql(strSQL.toString());
			pdao.setBindValues(paramList);
			Element resData = pdao.executeQuerySql(0, -1);
			xmlDocUtil.getResponse().addContent(resData);
			xmlDocUtil.setResult("0");
		}catch (Exception e) {
			e.printStackTrace();
			log4j.logError("[查询资源增长情况]"+e.getMessage());
		} finally {
			pdao.releaseConnection();
		}
		
	}
    //获取教师资源类型记录
	private void getTeacherResourceStyleRecode() {
		Element reqElement = xmlDocUtil.getRequestData();
    	String  deptcode = xmlDocUtil.getSession().getChildText("deptcode");
		String qry_deptcode = reqElement.getChildText("qry_deptcode");
		String qry_userid = reqElement.getChildText("qry_userid");
		String qry_username = reqElement.getChildText("qry_username");
		String qry_years = reqElement.getChildText("qry_years");
		
		if (StringUtil.isEmpty(qry_deptcode)){
			qry_deptcode = deptcode; 
		}
		StringBuffer strSQL = new StringBuffer();
		ArrayList<Object> paramList = new ArrayList<Object>();
		strSQL.append("SELECT COUNT(CASE WHEN t1.resource_type LIKE '40%' THEN 1 ELSE NULL END) AS paper_total_count,");
		strSQL.append(" COUNT(CASE WHEN t1.resource_type = '2010' THEN 1 ELSE NULL END) AS video_total_count,");
		strSQL.append(" COUNT(CASE WHEN t1.resource_type = '2020' THEN 1 ELSE NULL END) AS flash_total_count,");
		strSQL.append(" COUNT(CASE WHEN t1.resource_type = '2030' THEN 1 ELSE NULL END) AS ppt_total_count,");
		strSQL.append(" COUNT(CASE WHEN t1.resource_type = '2040' THEN 1 ELSE NULL END) AS word_total_count,");
		strSQL.append(" COUNT(CASE WHEN t1.resource_type = '2050' THEN 1 ELSE NULL END) AS image_total_count");
	    strSQL.append("	FROM learn_examination_paper t1,pcmc_user_dept t2,pcmc_dept t3,pcmc_user t4");
	    strSQL.append(" WHERE t1.user_id = t2.userid AND t2.deptid = t3.deptid and t1.user_id = t4.userid and t1.valid='Y' and t3.state='1'");
	    	
	    if(StringUtil.isNotEmpty(qry_years)){
	    	strSQL.append(" and DATE_FORMAT(t1.create_date, '%Y') = ?");
	    	paramList.add(qry_years);
	    }
        
	    if(StringUtil.isNotEmpty(qry_userid)){
	    	strSQL.append(" and t4.userid=?");
	    	paramList.add(qry_userid);
	    }
	    
	    if(StringUtil.isNotEmpty(qry_username)){
	    	strSQL.append(" and t4.username like ?");
	    	paramList.add("%"+qry_username+"%");
	    }
	    
        strSQL.append("	and t3.deptcode like ? ");
        paramList.add(qry_deptcode+"%");
        
		PlatformDao pdao = new PlatformDao();
		try {
			pdao.setSql(strSQL.toString());
			pdao.setBindValues(paramList);
			Element resData = pdao.executeQuerySql(0, -1);
			xmlDocUtil.getResponse().addContent(resData);
			xmlDocUtil.setResult("0");
		}catch (Exception e) {
			e.printStackTrace();
			log4j.logError("[查询教师资源类型记录]"+e.getMessage());
		} finally {
			pdao.releaseConnection();
		}
		
	}
	//教师资源统计
    private void getTeacherResourceDetail() {
    	Element reqElement = xmlDocUtil.getRequestData();
    	String  deptcode = xmlDocUtil.getSession().getChildText("deptcode");
		String qry_deptcode = reqElement.getChildText("qry_deptcode");
		String qry_username = reqElement.getChildText("qry_username");
		String qry_years = reqElement.getChildText("qry_years");
		
		if (StringUtil.isEmpty(qry_deptcode)){
			qry_deptcode = deptcode; 
		}
		StringBuffer strSQL = new StringBuffer();
		ArrayList<Object> paramList = new ArrayList<Object>();
		strSQL.append(" SELECT tt4.deptcode,tt4.deptname,tt2.username,tt1.*,");
		strSQL.append("	CONCAT((SELECT g.gradename FROM base_grade g WHERE g.gradecode = tt1.grade_code AND g.deptid = tt4.deptid),(SELECT s.subjname FROM base_subject s WHERE s.subjectid = tt1.subject_id)) AS grade_subject,");
		strSQL.append(" (SELECT COUNT(comment_id) FROM learn_comment st WHERE st.valid = 'Y' AND st.paper_id = tt1.paper_id) AS comment_count,");
		strSQL.append(" (SELECT COUNT(*) FROM learn_my_examination me WHERE me.paper_id = tt1.paper_id AND me.valid = 'Y' AND me.status > '10') AS submit_count,");
		strSQL.append(" (SELECT COUNT(send_id) FROM learn_paper_send st  WHERE st.valid = 'Y' AND st.paper_id = tt1.paper_id) AS send_count,");
		strSQL.append(" (SELECT COUNT(*) FROM sell_read_log t2 WHERE t2.paper_info_id = tt1.paper_id) AS readcount ");
	    strSQL.append("	FROM learn_examination_paper tt1,pcmc_user tt2,pcmc_user_dept tt3,pcmc_dept tt4");
	    strSQL.append("	WHERE tt1.user_id = tt2.userid AND tt2.userid = tt3.userid AND tt3.deptid = tt4.deptid AND tt1.valid = 'Y' AND tt4.state = '1'");
	    
	    if(StringUtil.isNotEmpty(qry_years)){
	    	strSQL.append(" and DATE_FORMAT(tt1.create_date, '%Y') = ?");
	    	paramList.add(qry_years);
	    }
        
	    if(StringUtil.isNotEmpty(qry_username)){
	    	strSQL.append(" and tt2.username like ?");
	    	paramList.add("%"+qry_username+"%");
	    }
	    
	    strSQL.append("	and tt4.deptcode like ? ");
        paramList.add(qry_deptcode+"%");
		
        PlatformDao pdao = new PlatformDao();
		try {
			pdao.setSql(strSQL.toString());
			pdao.setBindValues(paramList);
			Element resData = pdao.executeQuerySql(0, -1);
			xmlDocUtil.getResponse().addContent(resData);
			xmlDocUtil.setResult("0");
		}catch (Exception e) {
			e.printStackTrace();
			log4j.logError("[查询教师资源统计]"+e.getMessage());
		} finally {
			pdao.releaseConnection();
		}
		
	}
	/**
     * 取用户详细信息
     *
     */
    private void getUserDetail(){
    	Element reqData = xmlDocUtil.getRequestData();
    	String userid = reqData.getChildText("userid");
    	ArrayList<Object> paramList = new ArrayList<Object>();
    	paramList.add(userid);
    	PlatformDao pDao = new PlatformDao();
    	try
    	{
    		StringBuffer strSQL = new StringBuffer();
    		strSQL.append("SELECT a.*,b.deptcode,b.deptname,'' as urole ");
    		strSQL.append(" FROM pcmc_user a left join pcmc_user_dept c on a.userid=c.userid ");
    		strSQL.append(" left join pcmc_dept b on c.deptid=b.deptid");  
    		strSQL.append(" WHERE a.state>0 and a.userid =?");
    		
			pDao.setSql(strSQL.toString());
			pDao.setBindValues(paramList);
			
			Element userResult = pDao.executeQuerySql(-1, 1); 
			
			StringBuffer roleSQL = new StringBuffer();
			roleSQL.append("select a.*,b.cnname from pcmc_role a left join pcmc_subsys b on b.subsysid=a.subsysid ");
			roleSQL.append(" where roleid in (select roleid from pcmc_user_role where userid=?)");
			roleSQL.append(" order by b.cnname,a.rolename");
			
			pDao.setSql(roleSQL.toString());
			pDao.setBindValues(paramList);
			Element roleResult = pDao.executeQuerySql(-1, 1); 
			
			Element userRec = userResult.getChild("Record");
			StringBuffer uroleSB = new StringBuffer();
			List list = roleResult.getChildren("Record");
			for (int i = 0; i < list.size(); i++){
				Element roleRec = (Element)list.get(i);
				uroleSB.append(roleRec.getChildText("roleid")).append(",");				
			}
			XmlDocPkgUtil.setChildText(userRec, "urole", uroleSB.toString());
			xmlDocUtil.getResponse().addContent(userResult);
			xmlDocUtil.setResult("0");
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    		xmlDocUtil.writeErrorMsg("10604", "取用户详细信息失败");
    	}
    	finally
    	{
    		pDao.releaseConnection();
    	} 
    }
    /**
     * 取管理的角色
     *
     */
    private void getMRoleList(){
    	Element session = xmlDocUtil.getSession();
    	String userid = session.getChildText("userid");
    	String usercode = session.getChildText("usercode");
    	PlatformDao pDao = new PlatformDao();
    	try
    	{
    		StringBuffer strSQL = new StringBuffer();
    		strSQL.append("select distinct a.*,b.cnname from pcmc_role a, pcmc_subsys b");
    		strSQL.append(" where b.subsysid=a.subsysid");
    		if (!PcmcUtil.isSysManager(usercode)){
    			strSQL.append(" and (a.createuser='" + userid + "' or a.roleid in (select roleid from pcmc_user_role where userid='" + userid + "')) ");
    		}
    		strSQL.append(" order by b.cnname,a.rolename");
    		pDao.setSql(strSQL.toString());
    		Element result = pDao.executeQuerySql(-1, 1);
    		List list = result.getChildren("Record");
    		
    		ArrayList<Object> paramList = new ArrayList<Object>();
    		StringBuffer queryCond = new StringBuffer("");
    		int i = 0;
    		for (int j = 0; j < list.size(); j++){
    			Element rec = (Element)list.get(j);
    			
    	        String createuser = rec.getChildText("createuser");
    	        String roleid = rec.getChildText("roleid");
    	        String mrole = rec.getChildText("mrole"); 
    	        if (userid.equals(createuser)){
    	        	queryCond.append(i != 0 ? ",?" : "(?");
    	        	paramList.add(roleid);
    	        	i = 1;
    	        }
    	        else{
    	        	if (StringUtil.isEmpty(mrole)){
    	        		continue;
    	        	}
    	        	StringTokenizer st = new StringTokenizer(mrole, ",");
    	        	while (st.hasMoreTokens()){
    	        		roleid = st.nextToken();
        	        	queryCond.append(i != 0 ? ",?" : "(?");
        	        	paramList.add(roleid);   
        	        	i = 1;
    	        	}
    	        	
    	        }
    		}
    		if (paramList.size()>0){
    			StringBuffer roleSQL = new StringBuffer();
    			roleSQL.append("select a.*,b.cnname from pcmc_role a, pcmc_subsys b where b.subsysid=a.subsysid");
    			roleSQL.append(" and a.roleid in ").append(queryCond).append(")");
    			roleSQL.append(" order by b.cnname,a.rolename");
    			pDao.setSql(roleSQL.toString());
    			pDao.setBindValues(paramList);
    			
    			Element resultData = pDao.executeQuerySql(-1, 1);
    			xmlDocUtil.getResponse().addContent(resultData);
    		}
    		xmlDocUtil.setResult("0");
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    		xmlDocUtil.writeErrorMsg("10604", "取管理角色失败");
    	}
    	finally
    	{
    		pDao.releaseConnection();
    	}    	
    }
    
    //平台管理员 添加学校管理用
    private void add()
    {
    	Element reqData = xmlDocUtil.getRequestData();
    	Element session = xmlDocUtil.getSession();
    	String curUserid = session.getChildText("userid");
    	String curUsercode = session.getChildText("usercode");
    	if(!PcmcUtil.isSysManager(curUsercode))
    	{
    		return;
    	}
    	PlatformDao pDao = null;
    	try
    	{
    		pDao = new PlatformDao();
    		pDao.beginTransaction();
    		
    		Element userData = (Element)reqData.clone();
			XmlDocPkgUtil.setChildText(userData, "usertype", "9");
			String[] userids = addUser(userData, curUserid, curUsercode,"");
    		String userid = userids[1];
			String usercode = userids[0];
			if("0".equals(usercode))
			{
				//usercode
				xmlDocUtil.writeErrorMsg("10601", "用户名重复");
				return;
			}
			else if("-1".equals(usercode))
			{
				//idnumber
				xmlDocUtil.writeErrorMsg("10602", "身份证号重复");
				return;
			}
			
			String deptid = userData.getChildText("deptid");
			Element record = ConfigDocument.createRecordElement("pcmc","pcmc_user_dept");
			XmlDocPkgUtil.setChildText(record, "deptid", deptid);
			XmlDocPkgUtil.setChildText(record, "userid", userid);
			XmlDocPkgUtil.setChildText(record, "state", "1");
			XmlDocPkgUtil.setChildText(record, "indate", DatetimeUtil.getNow(null));
			
			pDao.insertOneRecord(record);
			
    		String[] flds = {"userid","usercode"};
			Element data = XmlDocPkgUtil.createMetaData(flds);
			data.addContent(XmlDocPkgUtil.createRecord(flds, new String[]{userid,usercode}));
			
			xmlDocUtil.setResult("0");
			xmlDocUtil.getResponse().addContent(data);
			xmlDocUtil.writeHintMsg("10603", "新增机构管理员成功");
    	}
    	catch(Exception ex)
    	{
    		pDao.rollBack();
    		ex.printStackTrace();
    		xmlDocUtil.writeErrorMsg("10604", "新增机构管理员失败");
    	}
    	finally
    	{
    		pDao.releaseConnection();
    	}
    }
    
    private void upt()
    {
    	Element reqData = xmlDocUtil.getRequestData();
    	Element session = xmlDocUtil.getSession();
    	String curUsercode = session.getChildText("usercode");
    	if(!PcmcUtil.isSysManager(curUsercode))
    	{
    		return;
    	}
    	try
		{
			String[] userids = uptUser(reqData);
			
			String usercode = userids[0];
			String userid = userids[1];
			
			if("0".equals(usercode))
			{
				//usercode
				xmlDocUtil.writeErrorMsg("10630", "用户名重复");
				return;
			}
			else if("-1".equals(usercode))
			{
				//idnumber
				xmlDocUtil.writeErrorMsg("10631", "身份证号重复");
				return;
			}
			
			String[] flds = {"userid","usercode"};
			Element data = XmlDocPkgUtil.createMetaData(flds);
			data.addContent(XmlDocPkgUtil.createRecord(flds, new String[]{userid,usercode}));
			
			xmlDocUtil.setResult("0");
			xmlDocUtil.writeHintMsg("10635", "修改机构管理员成功");
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			log4j.logError(ex);
			xmlDocUtil.writeErrorMsg("10636", "修改机构管理员失败");
		}
    }
    
    /**
     * 新增用户基本表
     * @param reqData
     * @param createuser
     * @return 新增userid(0:usercode重复,-1:身份证重复)
     * @throws Exception
     */
    protected String[] addUser(Element reqData,String createid,String createCode,String password)
    	throws Exception
    {
    	return addUser(reqData,createid,createCode,password,false);
    }
    
    /**
     * 新增用户基本表
     * @param reqData 
     * @param createid 
     * @param createCode 
     * @param password 
     * @param isRegUser 是否注册用户，用于绑定手机 (true:绑定，false：不绑定)
     * @return 新增userid(0:usercode重复,-1:身份证重复)
     * @throws Exception
     */
    protected String[] addUser(Element reqData,String createid,String createCode,String password,boolean isRegUser)
    	throws Exception
    {
    	PlatformDao pDao = null;
    	try
    	{
    		pDao = new PlatformDao();
    		pDao.beginTransaction();
    		String idnumber = reqData.getChildTextTrim(CHK_FIELDS[1]);
    		if(StringUtil.isNotEmpty(idnumber))
    		{
    			String chkuid = chkValid("1", idnumber);
    			if(StringUtil.isNotEmpty(chkuid))
    				return new String[]{"-1",chkuid};
    		}
    		Element record = ConfigDocument.createRecordElement("pcmc","pcmc_user");
    		XmlDocPkgUtil.copyValues(reqData,record,0,true);
    		XmlDocPkgUtil.setChildText(record, "mobilebind", isRegUser?"1":"0");
    		XmlDocPkgUtil.setChildText(record, "emailbind", "0");
    		XmlDocPkgUtil.setChildText(record, "state", "1");
    		XmlDocPkgUtil.setChildText(record, "modifydt", DatetimeUtil.getNow(null));

    		//String userpwd = PasswordEncoder.encode(genDefaultPwd());
    		String userpwd = PasswordEncoder.encode(password);
    		XmlDocPkgUtil.setChildText(record, "userpwd", userpwd);
    		
    		String usercode = reqData.getChildText(CHK_FIELDS[0]);
    		if(StringUtil.isNotEmpty(usercode) && PcmcUtil.isSysManager(createCode))
    		{
    			String chkuid = chkValid("0", usercode);
    			if(StringUtil.isNotEmpty(chkuid))
    				return new String[]{"0",chkuid};
    		}
    		else
    		{
    			//usercode = formatLong(genUserSeqId());
    			usercode = createCode;
    		}
    		XmlDocPkgUtil.setChildText(record, "usercode", usercode);
    		
    		String userid = (String)pDao.insertOneRecordSeqPk(record);
    		
    		record = ConfigDocument.createRecordElement("pcmc","pcmc_user_ext");
    		XmlDocPkgUtil.copyValues(reqData,record,0,true);
    		XmlDocPkgUtil.setChildText(record, "userid", userid);
    		XmlDocPkgUtil.setChildText(record, "createuser", createid);
    		pDao.insertOneRecord(record);
    		
    		pDao.commitTransaction();
    		
    		return new String[]{usercode,userid};
    	}
    	finally
    	{
    		pDao.releaseConnection();
    	}
    }
    
    protected String genDefaultPwd()
    {
    	return "123456";
    }
    
    protected long genUserSeqId()
    	throws Exception
    {
    	PlatformDao pDao = null;
    	try
    	{
    		pDao = new PlatformDao(true);
    		String sql = "insert into pcmc_user_sequence (createtime) values (?)";
    		ArrayList bvals = new ArrayList();
    		bvals.add(DatetimeUtil.getNow());
    		pDao.setSql(sql);
    		pDao.setBindValues(bvals);
    		
    		return pDao.executeInsertSql(true, "pcmc_user_sequence", "user_seq", Types.BIGINT);
    	}
    	finally
    	{
    		pDao.releaseConnection();
    	}
    }
    
    protected String formatLong(long id)
    {
    	DecimalFormat df = new DecimalFormat("#0");
        df.setGroupingUsed(false);
        return df.format(id);
    }
    
    /**
     * 删除用户
     * 身份证/邮箱/手机 写入备注信息
     * 同时置空已避免新增时唯一性校验
     * 状态=9
     */
    private void del()
    {
    	Element reqData = xmlDocUtil.getRequestData();
    	Element session = xmlDocUtil.getSession();
        String curUserid = session.getChildText("userid");
    	
        try
    	{
    		List userids = reqData.getChildren("userid");
    		
    		ArrayList<String> uids = delUsers(curUserid, userids);
    		
    		String[] flds = {"userid"};
    		Element data = XmlDocPkgUtil.createMetaData(flds);
    		for(String uid : uids)
    		{
    			data.addContent(XmlDocPkgUtil.createRecord(flds, new String[]{uid}));
    		}
    		xmlDocUtil.setResult("0");
    		xmlDocUtil.getResponse().addContent(data);
    		xmlDocUtil.writeHintMsg("10605", "删除用户成功");
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    		xmlDocUtil.writeErrorMsg("10606", "删除用户失败");
    	}
    }
    
    protected ArrayList<String> delUsers(String curUserid,List userids)
    	throws Exception
    {
    	PlatformDao pDao = null;
    	try
    	{
    		pDao = new PlatformDao();
    		pDao.beginTransaction();
    		StringBuffer sqlBuf = new StringBuffer()
				.append("select a.*,b.userid as extuid from pcmc_user a ")
				.append("left join pcmc_user_ext b on a.userid=b.userid ")
				.append("where a.userid<>? and a.userid in (");
			ArrayList bvals = new ArrayList();
			bvals.add(curUserid);
			pDao.setSql(sqlBuf.toString());
			for(int i=0; i<userids.size(); i++)
			{
				Element useridRec = (Element)userids.get(i);
				sqlBuf.append("?,");
				bvals.add(useridRec.getText());
			}
			sqlBuf.deleteCharAt(sqlBuf.length()-1);
			sqlBuf.append(")");
			
			pDao.setSql(sqlBuf.toString());
			pDao.setBindValues(bvals);
			Element userData = pDao.executeQuerySql(-1, 1);
			
			ArrayList<String> noExts = new ArrayList<String>();
			ArrayList<String> uids = new ArrayList<String>();
			List userList = userData.getChildren("Record");
			for(int i=0; i<userList.size(); i++)
			{
				Element userRec = (Element)userList.get(i);
				String uid = userRec.getChildText("userid");
				String eid = userRec.getChildText("extuid");
				if(StringUtil.isEmpty(eid))
				{
					noExts.add(uid);
				}
				uids.add(uid);
			}
			
			//无扩展记录则新增
			if(0 < noExts.size())
			{
				sqlBuf.setLength(0);
				sqlBuf.append("insert into pcmc_user_ext (userid) values (?)");
				pDao.setSql(sqlBuf.toString());
				for(String uid : noExts)
				{
					bvals.clear();
					bvals.add(uid);
					pDao.addBatch(bvals);
				}
				pDao.executeBatch();
			}
			
			if(0 < userList.size())
			{
				sqlBuf.setLength(0);
				sqlBuf.append("update pcmc_user_ext set remark=? where userid=?");
				pDao.setSql(sqlBuf.toString());
				for(int i=0; i<userList.size(); i++)
				{
					Element userRec = (Element)userList.get(i);
					String uid = userRec.getChildText("userid");
					String idnumber = userRec.getChildTextTrim("idnumber");
					String email = userRec.getChildTextTrim("email");
					String mobile = userRec.getChildTextTrim("mobile");
					StringBuffer remark = new StringBuffer();
					remark.append("idnum:").append(idnumber)
						.append(",email:").append(email)
						.append(",mobile").append(mobile)
						.append(",deletetime:").append(DatetimeUtil.getNow(null));
					bvals.clear();
					bvals.add(remark.toString());
					bvals.add(uid);
					pDao.addBatch(bvals);
				}
				pDao.executeBatch();
				
				sqlBuf.setLength(0);
				sqlBuf.append("update pcmc_user set idnumber='',email='',mobile='',modifydt=?,state='9' where userid=?");
				pDao.setSql(sqlBuf.toString());
				for(String uid : uids)
				{
					bvals.clear();
					bvals.add(DatetimeUtil.getNow());
					bvals.add(uid);
					pDao.addBatch(bvals);
				}
				pDao.executeBatch();
			}
			pDao.commitTransaction();
			return uids;
    	}
    	catch(Exception ex)
    	{
    		pDao.rollBack();
    		throw ex;
    	}
    	finally
    	{
    		pDao.releaseConnection();
    	}
    }
    
    protected String[] uptUser(Element reqData)
    	throws Exception
    {
    	PlatformDao pDao = null;
		try
		{
			pDao = new PlatformDao();
			pDao.beginTransaction();
			
			String userid = reqData.getChildText("userid");
			
			String userpwd = reqData.getChildTextTrim("userpwd");
			if(StringUtil.isEmpty(userpwd)){
				userpwd = genDefaultPwd();
			}
			String usercode = reqData.getChildText(CHK_FIELDS[0]);
			
			Element record = ConfigDocument.createRecordElement("pcmc","pcmc_user");
			XmlDocPkgUtil.copyValues(reqData,record,0,true);
			XmlDocPkgUtil.setChildText(record, "modifydt", DatetimeUtil.getNow(null));
			XmlDocPkgUtil.setChildText(record, "userpwd",PasswordEncoder.encode(userpwd));//登录密码
			
			Element oldUserRec = getUserById(userid);
			String newIdnumber = record.getChildText(CHK_FIELDS[1]);
			String oldIdnumber = oldUserRec.getChildTextTrim(CHK_FIELDS[1]);
			if(StringUtil.isNotEmpty(newIdnumber) && !newIdnumber.equals(oldIdnumber))
			{
				String chkuid = chkValid("1", newIdnumber);
				if(StringUtil.isNotEmpty(chkuid) && !userid.equals(chkuid))
					{
						return new String[]{"-1",chkuid};
					}
			}
			String oldusercode = oldUserRec.getChildText(CHK_FIELDS[0]);
    		if(StringUtil.isNotEmpty(usercode) && !usercode.equals(oldusercode))
    		{
    			String chkuid = chkValid("0", usercode);
    			if(StringUtil.isNotEmpty(chkuid) && !userid.equals(chkuid))
    				return new String[]{"0",chkuid};
    		}
			String newmobile = record.getChildText(CHK_FIELDS[2]);
			String oldmobile = oldUserRec.getChildTextTrim(CHK_FIELDS[2]);
			if(null!=newmobile && !oldmobile.equals(newmobile))
			{
				XmlDocPkgUtil.setChildText(record, "mobilebind", "1");//默认绑定手机号码
			}
			String newemail = record.getChildText(CHK_FIELDS[3]);
			String oldemail = oldUserRec.getChildTextTrim(CHK_FIELDS[3]);
			if(null!=newemail && !oldemail.equals(newemail))
			{
				XmlDocPkgUtil.setChildText(record, "emailbind", "0");
			}
			pDao.updateOneRecord(record);
			
			// 防止在机构管理中修改管理员账号信息时出现异常（因为此时不会有扩展信息）
			String remark = reqData.getChildTextTrim("remark");
			if(remark != null){
				//更新扩展信息
				record = ConfigDocument.createRecordElement("pcmc","pcmc_user_ext");
	    		XmlDocPkgUtil.copyValues(reqData,record,0,true);
	    		pDao.updateOneRecord(record);
			}
    		
			pDao.commitTransaction();
			return new String[]{usercode,userid};
		}
		catch(Exception ex)
		{
			pDao.rollBack();
			throw ex;
		}
		finally
		{
			pDao.releaseConnection();
		}
    }
    
    /**
     * 修改个人信息
     */
    private void uptinfo()
    {
    	Element reqData = xmlDocUtil.getRequestData();
    	Element session = xmlDocUtil.getSession();
        String curUserid = session.getChildText("userid");
        
        PlatformDao pDao = null;
        try
        {
        	pDao = new PlatformDao();
        	pDao.beginTransaction();
        	Element record = ConfigDocument.createRecordElement("pcmc","pcmc_user");
    		XmlDocPkgUtil.copyValues(reqData,record,0,true);
    		XmlDocPkgUtil.setChildText(record, "userid", curUserid);
    		XmlDocPkgUtil.setChildText(record, "modifydt", DatetimeUtil.getNow(null));
    		
    		//处理上传头像
    		//portrait
    		String portraitPath = reqData.getChildText("portrait");
    		if(StringUtil.isNotEmpty(portraitPath))
    		{
    			portraitPath = FileUtil.getPhysicalPath(portraitPath);
    			String fileExt = FileUtil.getFileExt(portraitPath).toLowerCase();
    			String fileName = curUserid + "_" + DatetimeUtil.getNow("yyyyMMddHHmmss") +".jpg";
    			if(ImageUtils.isImage(fileExt))
    			{
    				String physicalPath = FileUtil.getWebPath() + PORTRAIT_PATH + curUserid +"/";
    				File oldFile = new File(physicalPath);
    				if(oldFile.exists()){
    					FileUtil.deleteDir(new File(physicalPath));
    				}
    				FileUtil.createDirs(physicalPath, true);
    				
    				FileUtil.moveFile(new File(portraitPath), new File(physicalPath+fileName));

    				XmlDocPkgUtil.setChildText(record, "portrait", "/"+PORTRAIT_PATH + curUserid + "/" + fileName);
    			}
    			else
    			{
    				record.removeChildren("portrait");
    			}
				FileUtil.deleteFile(portraitPath);
    		}    		
    		pDao.updateOneRecord(record);
    		
    		record = ConfigDocument.createRecordElement("pcmc","pcmc_user_ext");
    		XmlDocPkgUtil.copyValues(reqData,record,0,true);
    		if(record.getChildren().size()>0)
    		{
    			XmlDocPkgUtil.setChildText(record, "userid", curUserid);
    			pDao.updateOneRecord(record);
    		}    		
    		pDao.commitTransaction();
    		
    		getCurUserDetail();
        }
        catch(Exception ex)
        {
        	ex.printStackTrace();
        	log4j.logError(ex);
        	xmlDocUtil.writeErrorMsg("10608", "修改个人信息错误");
        }
        finally
        {
        	pDao.releaseConnection();
        }
    }
    
    protected Element getUserById(String userid)
    	throws Exception
    {
    	String sql = "select * from pcmc_user where userid=?";
    	ArrayList bvals = new ArrayList();
    	bvals.add(userid);
    	return DaoUtil.getOneRecord(sql, bvals);
    }
    
    protected void chkValid()
    {
    	Element reqData = xmlDocUtil.getRequestData();
    	try
    	{    		
    		String chkFld = reqData.getChildText("chkFld");
    		String chkVal = reqData.getChildText("chkVal");
    		
    		String userid = chkValid(chkFld,chkVal);
    		
    		String[] flds = new String[]{"isexist"};
    		String[] vals = new String[1];
    		
    		Element resData = XmlDocPkgUtil.createMetaData(flds);
    		vals[1] = StringUtil.isEmpty(userid)?"0":"1";
    		
    		resData.addContent(XmlDocPkgUtil.createRecord(flds, vals));
    		
    		xmlDocUtil.setResult("0");
    		xmlDocUtil.getResponse().addContent(resData);
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
            log4j.logError("[校验错误.]"+ex.getMessage());
            xmlDocUtil.writeErrorMsg("10617","校验错误");
    	}
    }
    
    protected String chkValid(String chkFld,String chkVal)
    	throws Exception
    {
    	int chk = new Integer(chkFld).intValue();
		chkFld = CHK_FIELDS[chk];
		String bind = CHK_BINDS[chk];
		
		StringBuffer sqlBuf = new StringBuffer();
		sqlBuf.append("select userid from pcmc_user where ")
			.append(chkFld).append("=?");
		if(null != bind)
		{
			sqlBuf.append(" and ").append(bind).append("='1'");
		}
		ArrayList bvals = new ArrayList();
		bvals.add(chkVal);
		
		Element rec = DaoUtil.getOneRecord(sqlBuf.toString(), bvals);
		return null == rec ? null : rec.getChildText("userid");
    }
    
    /**
     * 管理员用重置密码
     */
    private void reset()
    {
    	Element reqData = xmlDocUtil.getRequestData();
    	Element session = xmlDocUtil.getSession();
    	String curUsercode = session.getChildText("usercode");
    	String curDeptid = session.getChildText("deptid");
    	
    	PlatformDao pDao = null;
    	try
    	{
    		pDao = new PlatformDao();
    		
    		StringBuffer sqlBuf = new StringBuffer();
    		boolean isAdmin = false;
    		
    		if(PcmcUtil.isSysManager(curUsercode))
    		{
    			sqlBuf.append("update pcmc_user set userpwd=? where userid=?");
    			isAdmin = true;
    		}
    		else
    		{
    			sqlBuf.append("update pcmc_user a set a.userpwd=? ")
    				.append("where exists (select 1 from pcmc_user_dept b where a.userid=b.userid ")
    				.append("and a.userid=? and b.deptid=?)");
    		}
    		
    		List uids = reqData.getChildren("userid");
    		ArrayList bvals = new ArrayList();
    		pDao.setSql(sqlBuf.toString());
    		for(int i=0; i<uids.size(); i++)
    		{
    			Element uidEle = (Element)uids.get(i);
    			String userid = uidEle.getText();
    			String userpwd = PasswordEncoder.encode(genDefaultPwd());
    			bvals.clear();
    			bvals.add(userpwd);
        		bvals.add(userid);
        		if(!isAdmin)
        			bvals.add(curDeptid);
        		pDao.addBatch(bvals);
    		}
    		if(uids.size()>0)
    		{
    			pDao.executeBatch();
    		}
    		
    		xmlDocUtil.setResult("0");
    		xmlDocUtil.writeHintMsg("10620", "重置密码成功");
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    		xmlDocUtil.writeErrorMsg("10621", "重置密码失败");
    	}
    	finally
    	{
    		pDao.releaseConnection();
    	}
    }
    
    private void chgpwd()
    {
    	Element reqData = xmlDocUtil.getRequestData();
        Element session = xmlDocUtil.getSession();
        String userid = session.getChildText("userid");
        String usercode = session.getChildText("usercode");
        String oldpwd = reqData.getChildText("oldpwd");
        String newpwd = reqData.getChildText("newpwd");
        
        PlatformDao pDao = null;
        try
        {
        	pDao = new PlatformDao();
        	
        	StringBuffer sqlBuf = new StringBuffer();
        	ArrayList bvals = new ArrayList();
        	bvals.add(userid);
        	if(StringUtil.isNotEmpty(oldpwd))
        	{
            	sqlBuf.append("select userpwd from pcmc_user where userid=?");
            	pDao.setSql(sqlBuf.toString());
            	pDao.setBindValues(bvals);
            	Element data = pDao.executeQuerySql(1, 1);
            	Element pwdRec = data.getChild("Record");
            	oldpwd = PasswordEncoder.encode(oldpwd);
            	if(!oldpwd.equals(pwdRec.getChildTextTrim("userpwd")))
            	{
            		xmlDocUtil.setResult("1");
                    xmlDocUtil.writeErrorMsg("10615","原密码不正确");
                    return;
            	}
        	}
        	else if(!PcmcUtil.isSysManager(usercode))
            {
            	xmlDocUtil.setResult("1");
                xmlDocUtil.writeErrorMsg("10615","原密码不正确");
                return;
            }
        	sqlBuf.setLength(0);
        	sqlBuf.append("update pcmc_user set userpwd=? where userid=?");
        	bvals.clear();
        	bvals.add(0,PasswordEncoder.encode(newpwd));
        	bvals.add(userid);
        	
        	pDao.setSql(sqlBuf.toString());
        	pDao.setBindValues(bvals);
        	pDao.executeTransactionSql();
        	
        	xmlDocUtil.setResult("0");
            xmlDocUtil.writeHintMsg("10613","修改密码成功");
        }
        catch(Exception ex)
        {
        	 ex.printStackTrace();
             log4j.logError("[修改密码错误.]"+ex.getMessage());
             log4j.logInfo(JDomUtil.toXML(xmlDocUtil.getXmlDoc(), "GBK", true));
             xmlDocUtil.writeErrorMsg("10614","修改密码失败");
        }
        finally
        {
        	pDao.releaseConnection();
        }
    }
    
    private void chgmobile()
    {
    	
    }
    
    private void chgmail()
    {
    	
    }
    
    private void getCurUserDetail()
    {
        Element session = xmlDocUtil.getSession();
        String curuser = session.getChildText("userid");
        try
        {        	
        	StringBuffer sqlBuf = new StringBuffer("select ")
        		.append("a.userid,a.usercode,a.username,a.nickname,")
        		.append("a.portrait,a.description,a.email,a.phone,a.mobile,")
        		.append("a.emailbind,a.mobilebind,a.gender,a.usertype,")
        		.append("b.pubname,b.pubmail,b.pubphone,")
        		.append("d.deptid,d.deptcode,d.deptname,")
        		.append("g.classid,g.classnm ")
        		.append("from pcmc_user a ")
        		.append("left join pcmc_user_ext b on a.userid = b.userid ")
	    		.append("left join (select c.userid,b.* from pcmc_dept b,pcmc_user_dept c")
	    		.append(" where b.deptid=c.deptid and c.state=?) d")
	    		.append(" on a.userid=d.userid ")
	    		.append("left join (select e.userid,f.classid,f.classnm from base_studentinfo e,base_class f")
	    		.append(" where e.deptid=f.deptid and e.classid=f.classid and e.state=? and f.state=? ) g")
	    		.append(" on a.userid=g.userid ")
	    		.append("where a.userid=?");
        	ArrayList bvals = new ArrayList();
        	bvals.add("1");
        	bvals.add("1");
        	bvals.add("1");
        	bvals.add(curuser);
        	Element data = DaoUtil.getOneRecordData(sqlBuf.toString(), bvals);
        	Element userRec = data.getChild("Record");
        	UriUtil.formatUri(userRec, "portrait");
        	
        	xmlDocUtil.setResult("0");
        	xmlDocUtil.getResponse().addContent(data);
        }
        catch(Exception ex)
        {
        	ex.printStackTrace();
        	log4j.logError(ex);
        	xmlDocUtil.writeErrorMsg("10607", "查询当前用户信息错误");
        }
    }
    
    /**
     * 查询用户增长详情
     */
    public void getUserGrowthDetail(){
    	Element reqElement = xmlDocUtil.getRequestData();
    	String  deptcode = xmlDocUtil.getSession().getChildText("deptcode");
		String qry_deptcode = reqElement.getChildText("qry_deptcode");
		String qry_stat_type = reqElement.getChildText("qry_stat_type");
		String qry_years = reqElement.getChildText("qry_years");
		if (StringUtil.isEmpty(qry_deptcode)){
			qry_deptcode = deptcode; 
		}
		
		StringBuffer strSQL = new StringBuffer();
		ArrayList<Object> paramList = new ArrayList<Object>();
		strSQL.append(" select *,stu_total_count-stu_primary_school_count as stu_middle_school_count,th_total_count-th_primary_school_count as th_middle_school_count from ( ");
		strSQL.append("	SELECT t1.deptid, t1.deptname, t1.deptcode,");
		//学生总数
		strSQL.append(" (select count(distinct tt2.userid) from base_studentinfo tt2,pcmc_dept tt3,pcmc_user tt5 where  tt2.deptid = tt3.deptid ");
		strSQL.append(" AND tt2.userid = tt5.userid AND tt3.state='1' and tt5.usertype='1' and tt5.state='1' and tt3.deptcode like CONCAT(t1.deptcode,'%')");

		if("1".equals(qry_stat_type)){
	    	if(StringUtil.isNotEmpty(qry_years)){
	    		strSQL.append(" and DATE_FORMAT(tt5.create_date, '%Y') = ?");
		    	paramList.add(qry_years);
	    	}else {
	    		strSQL.append(" and DATE_FORMAT(tt5.create_date, '%Y') >=2015");
			}
	    }
		strSQL.append(" ) AS stu_total_count,");
		//教师总数
		strSQL.append("	(select count(distinct tt2.userid) from base_teacher_info tt2,pcmc_dept tt3,pcmc_user_dept tt4,pcmc_user tt5 where tt2.userid = tt4.userid and tt4.deptid = tt3.deptid AND tt2.userid=tt5.userid and tt5.usertype='2' and tt5.state='1' AND tt3.state='1' and tt3.deptcode like CONCAT(t1.deptcode,'%') ");

		if("1".equals(qry_stat_type)){
	    	if(StringUtil.isNotEmpty(qry_years)){
	    		strSQL.append(" and DATE_FORMAT(tt5.create_date, '%Y') = ?");
		    	paramList.add(qry_years);
	    	}else {
	    		strSQL.append(" and DATE_FORMAT(tt5.create_date, '%Y') >=2015");
			}
	    }
		strSQL.append(" ) AS th_total_count, ");
		//小学生总数
		strSQL.append("	(select count(DISTINCT tt2.userid) from base_class tt1,base_studentinfo tt2,pcmc_dept tt3,pcmc_user tt5 where tt1.classid=tt2.classid and tt2.deptid = tt3.deptid AND tt2.userid = tt5.userid  AND tt3.state='1' and tt5.usertype='1' and tt5.state='1' and tt3.deptcode like CONCAT(t1.deptcode,'%') and tt1.gradecode<=6  ");

		if("1".equals(qry_stat_type)){
	    	if(StringUtil.isNotEmpty(qry_years)){
	    		strSQL.append(" and DATE_FORMAT(tt5.create_date, '%Y') = ?");
		    	paramList.add(qry_years);
	    	}else {
	    		strSQL.append(" and DATE_FORMAT(tt5.create_date, '%Y') >=2015");
			}
	    }
		strSQL.append(" ) AS stu_primary_school_count,");
		// 小学教师总数
		strSQL.append("	(select count(distinct tt1.userid) from base_teacher_info tt1,base_teacher_subject tt2,pcmc_dept tt3,pcmc_user_dept tt4,pcmc_user tt5 where tt1.userid = tt4.userid and tt4.deptid = tt3.deptid and tt1.userid = tt5.userid AND tt2.userid = tt5.userid AND tt3.state='1' and tt5.usertype='2' and tt5.state='1' and tt3.deptcode like CONCAT(t1.deptcode,'%')  ");
		
		if("1".equals(qry_stat_type)){
	    	if(StringUtil.isNotEmpty(qry_years)){
	    		strSQL.append(" and DATE_FORMAT(tt5.create_date, '%Y') = ?");
		    	paramList.add(qry_years);
	    	}else {
	    		strSQL.append(" and DATE_FORMAT(tt5.create_date, '%Y') >=2015");
			}
	    }
		strSQL.append(" and exists(select null from base_class tt1 where tt1.classid=tt2.classid and tt1.gradecode<=6)) AS th_primary_school_count,");
		//小学班级总数
		strSQL.append("(select count(*) from base_class tt1,pcmc_dept tt2 where tt1.deptid=tt2.deptid and tt2.state='1' and tt2.deptcode like CONCAT(t1.deptcode,'%') and tt1.gradecode<=6 ");
		if("1".equals(qry_stat_type)){
	    	if(StringUtil.isNotEmpty(qry_years)){
	    		strSQL.append(" and DATE_FORMAT(tt1.createdt, '%Y') = ?");
		    	paramList.add(qry_years);
	    	}else {
	    		strSQL.append(" and DATE_FORMAT(tt1.createdt, '%Y') >=2015");
			}
	    }
		strSQL.append(" ) as primary_school_class_count,");
		//初中班级总数 
		strSQL.append("	(select count(*) from base_class tt1,pcmc_dept tt2 where tt1.deptid=tt2.deptid and tt2.state='1' and tt2.deptcode like CONCAT(t1.deptcode,'%') and tt1.gradecode>6 ");
		
		if("1".equals(qry_stat_type)){
	    	if(StringUtil.isNotEmpty(qry_years)){
	    		strSQL.append(" and DATE_FORMAT(tt1.createdt, '%Y') = ?");
		    	paramList.add(qry_years);
	    	}else {
	    		strSQL.append(" and DATE_FORMAT(tt1.createdt, '%Y') >=2015");
			}
	    }
		strSQL.append(" ) as middle_school_class_count");
		strSQL.append("	FROM pcmc_dept t1 WHERE t1.state='1'");

		if(qry_deptcode.length()>=8){
			strSQL.append(" and length(t1.deptcode)>8  AND t1.deptcode like '"+qry_deptcode+"%' ");
		}
		else if(qry_deptcode.length()<8){
			strSQL.append("	and length(t1.deptcode)=8 and t1.deptcode like '"+qry_deptcode+"%' ");
		}

		strSQL.append("	order by t1.deptcode) user");
		PlatformDao pdao = new PlatformDao();
		try {
			pdao.setSql(strSQL.toString());
			pdao.setBindValues(paramList);
			Element resData = pdao.executeQuerySql(0, -1);
			xmlDocUtil.getResponse().addContent(resData);
			xmlDocUtil.setResult("0");
		}catch (Exception e) {
			e.printStackTrace();
			log4j.logError("[查询用户增长情况]"+e.getMessage());
		} finally {
			pdao.releaseConnection();
		}
    	
    }
    
    /**
     * 查询资源增长详情
     */
    public void getResourceGrowthDetail(){
    	Element reqElement = xmlDocUtil.getRequestData();
    	String  deptcode = xmlDocUtil.getSession().getChildText("deptcode");
    	String qry_deptcode = reqElement.getChildText("qry_deptcode");
    	String qry_stat_type = reqElement.getChildText("qry_stat_type");
    	String qry_years = reqElement.getChildText("qry_years");
    	if (StringUtil.isEmpty(qry_deptcode)){
    		qry_deptcode = deptcode; 
    	}
    	
    	StringBuffer strSQL = new StringBuffer();
    	ArrayList<Object> paramList = new ArrayList<Object>();
    	if(qry_deptcode.length()!=10){
    		//全市、全区资源增长统计SQL
	    	strSQL.append("	SELECT t1.deptname,t1.deptcode,'' as userid,");
	    	strSQL.append("	ifnull(sum(t2.resource_total_count),0) as resource_total_count,");
	    	strSQL.append("	ifnull(sum(t2.paper_total_count),0) as paper_total_count,");
	    	strSQL.append("	ifnull(sum(t2.video_total_count),0) as video_total_count,");
	    	strSQL.append("	ifnull(sum(t2.flash_total_count),0) as flash_total_count,");
	    	strSQL.append("	ifnull(sum(t2.ppt_total_count),0) as ppt_total_count,");
	    	strSQL.append("	ifnull(sum(t2.word_total_count),0) as word_total_count,");
	    	strSQL.append("	ifnull(sum(t2.image_total_count),0) as image_total_count");
	    	strSQL.append(" FROM pcmc_dept t1 left join (");
	    	strSQL.append(" select tt4.deptcode,tt2.username,");
	    	strSQL.append(" COUNT(case when (tt1.resource_type like '40%' or tt1.resource_type like '20%') ");
	    	if("1".equals(qry_stat_type)){
		    	if(StringUtil.isNotEmpty(qry_years)){
		    		strSQL.append(" and DATE_FORMAT(tt1.create_date, '%Y') = ?");
			    	paramList.add(qry_years);
		    	}else {
		    		strSQL.append(" and DATE_FORMAT(tt1.create_date, '%Y') >=2015");
				}
		    }
	    	strSQL.append(" then 1 else null end) as resource_total_count,");
	    	strSQL.append(" COUNT(case when tt1.resource_type like '40%' ");
	    	if("1".equals(qry_stat_type)){
		    	if(StringUtil.isNotEmpty(qry_years)){
		    		strSQL.append(" and DATE_FORMAT(tt1.create_date, '%Y') = ?");
			    	paramList.add(qry_years);
		    	}else {
		    		strSQL.append(" and DATE_FORMAT(tt1.create_date, '%Y') >=2015");
				}
		    }
	    	strSQL.append(" then 1 else null end) as paper_total_count,");
	    	strSQL.append(" COUNT(case when tt1.resource_type = '2010' ");
	    	if("1".equals(qry_stat_type)){
		    	if(StringUtil.isNotEmpty(qry_years)){
		    		strSQL.append(" and DATE_FORMAT(tt1.create_date, '%Y') = ?");
			    	paramList.add(qry_years);
		    	}else {
		    		strSQL.append(" and DATE_FORMAT(tt1.create_date, '%Y') >=2015");
				}
		    }
	    	strSQL.append(" then 1 else null end) as video_total_count,");
	    	strSQL.append(" COUNT(case when tt1.resource_type = '2020' ");
	    	if("1".equals(qry_stat_type)){
		    	if(StringUtil.isNotEmpty(qry_years)){
		    		strSQL.append(" and DATE_FORMAT(tt1.create_date, '%Y') = ?");
			    	paramList.add(qry_years);
		    	}else {
		    		strSQL.append(" and DATE_FORMAT(tt1.create_date, '%Y') >=2015");
				}
		    }
	    	strSQL.append(" then 1 else null end) as flash_total_count,");
	    	strSQL.append(" COUNT(case when tt1.resource_type = '2030' ");
	    	if("1".equals(qry_stat_type)){
		    	if(StringUtil.isNotEmpty(qry_years)){
		    		strSQL.append(" and DATE_FORMAT(tt1.create_date, '%Y') = ?");
			    	paramList.add(qry_years);
		    	}else {
		    		strSQL.append(" and DATE_FORMAT(tt1.create_date, '%Y') >=2015");
				}
		    }
	    	strSQL.append(" then 1 else null end) as ppt_total_count,");
	    	strSQL.append(" COUNT(case when tt1.resource_type = '2040' ");
	    	if("1".equals(qry_stat_type)){
		    	if(StringUtil.isNotEmpty(qry_years)){
		    		strSQL.append(" and DATE_FORMAT(tt1.create_date, '%Y') = ?");
			    	paramList.add(qry_years);
		    	}else {
		    		strSQL.append(" and DATE_FORMAT(tt1.create_date, '%Y') >=2015");
				}
		    }
	    	strSQL.append(" then 1 else null end) as word_total_count,");
	    	strSQL.append(" COUNT(case when tt1.resource_type = '2050' ");
	    	if("1".equals(qry_stat_type)){
		    	if(StringUtil.isNotEmpty(qry_years)){
		    		strSQL.append(" and DATE_FORMAT(tt1.create_date, '%Y') = ?");
			    	paramList.add(qry_years);
		    	}else {
		    		strSQL.append(" and DATE_FORMAT(tt1.create_date, '%Y') >=2015");
				}
		    }
	    	strSQL.append(" then 1 else null end) as image_total_count");
	    	strSQL.append(" from learn_examination_paper tt1,pcmc_user tt2,pcmc_user_dept tt3,pcmc_dept tt4");
	    	strSQL.append(" where tt1.user_id = tt2.userid and tt2.userid = tt3.userid and tt3.deptid = tt4.deptid and tt1.valid = 'Y' and tt4.state = '1'");
	    	strSQL.append(" group by tt4.deptcode,tt2.username ) t2 on t2.deptcode like CONCAT(t1.deptcode,'%')");
	    	if(qry_deptcode.length()>=8){
	    		strSQL.append(" WHERE length(t1.deptcode)>8  AND t1.deptcode like '"+qry_deptcode+"%' ");
	    	}
	    	else if(qry_deptcode.length()<8){
	    		strSQL.append("	WHERE length(t1.deptcode)=8 and t1.deptcode like '"+qry_deptcode+"%' ");
	    	}  
	    	strSQL.append(" AND t1.state='1' GROUP BY t1.deptname, t1.deptcode ORDER BY t1.deptcode");
    	}else {
    		//具体学校所有老师上传资源SQL
    		strSQL.append(" select * from ( SELECT t1.username as deptname,'' as deptcode,t1.userid,");
        	strSQL.append(" ifnull(sum(t2.resource_total_count),0) as resource_total_count,");
        	strSQL.append(" ifnull(sum(t2.paper_total_count),0) as paper_total_count,");
        	strSQL.append(" ifnull(sum(t2.video_total_count),0) as video_total_count,");
        	strSQL.append(" ifnull(sum(t2.flash_total_count),0) as flash_total_count,");
        	strSQL.append(" ifnull(sum(t2.ppt_total_count),0) as ppt_total_count,");
        	strSQL.append(" ifnull(sum(t2.word_total_count),0) as word_total_count,");
        	strSQL.append(" ifnull(sum(t2.image_total_count),0) as image_total_count");
        	strSQL.append(" FROM pcmc_user t1 left join ( select tt2.username,tt2.userid,");
        	strSQL.append(" COUNT(case when (tt1.resource_type like '40%' or tt1.resource_type like '20%') ");
        	if("1".equals(qry_stat_type)){
		    	if(StringUtil.isNotEmpty(qry_years)){
		    		strSQL.append(" and DATE_FORMAT(tt1.create_date, '%Y') = ?");
			    	paramList.add(qry_years);
		    	}else {
		    		strSQL.append(" and DATE_FORMAT(tt1.create_date, '%Y') >=2015");
				}
		    }
	    	strSQL.append(" then 1 else null end) as resource_total_count,");
	    	strSQL.append(" COUNT(case when tt1.resource_type like '40%' ");
	    	if("1".equals(qry_stat_type)){
		    	if(StringUtil.isNotEmpty(qry_years)){
		    		strSQL.append(" and DATE_FORMAT(tt1.create_date, '%Y') = ?");
			    	paramList.add(qry_years);
		    	}else {
		    		strSQL.append(" and DATE_FORMAT(tt1.create_date, '%Y') >=2015");
				}
		    }
	    	strSQL.append(" then 1 else null end) as paper_total_count,");
	    	strSQL.append(" COUNT(case when tt1.resource_type = '2010' ");
	    	if("1".equals(qry_stat_type)){
		    	if(StringUtil.isNotEmpty(qry_years)){
		    		strSQL.append(" and DATE_FORMAT(tt1.create_date, '%Y') = ?");
			    	paramList.add(qry_years);
		    	}else {
		    		strSQL.append(" and DATE_FORMAT(tt1.create_date, '%Y') >=2015");
				}
		    }
	    	strSQL.append(" then 1 else null end) as video_total_count,");
	    	strSQL.append(" COUNT(case when tt1.resource_type = '2020' ");
	    	if("1".equals(qry_stat_type)){
		    	if(StringUtil.isNotEmpty(qry_years)){
		    		strSQL.append(" and DATE_FORMAT(tt1.create_date, '%Y') = ?");
			    	paramList.add(qry_years);
		    	}else {
		    		strSQL.append(" and DATE_FORMAT(tt1.create_date, '%Y') >=2015");
				}
		    }
	    	strSQL.append(" then 1 else null end) as flash_total_count,");
	    	strSQL.append(" COUNT(case when tt1.resource_type = '2030' ");
	    	if("1".equals(qry_stat_type)){
		    	if(StringUtil.isNotEmpty(qry_years)){
		    		strSQL.append(" and DATE_FORMAT(tt1.create_date, '%Y') = ?");
			    	paramList.add(qry_years);
		    	}else {
		    		strSQL.append(" and DATE_FORMAT(tt1.create_date, '%Y') >=2015");
				}
		    }
	    	strSQL.append(" then 1 else null end) as ppt_total_count,");
	    	strSQL.append(" COUNT(case when tt1.resource_type = '2040' ");
	    	if("1".equals(qry_stat_type)){
		    	if(StringUtil.isNotEmpty(qry_years)){
		    		strSQL.append(" and DATE_FORMAT(tt1.create_date, '%Y') = ?");
			    	paramList.add(qry_years);
		    	}else {
		    		strSQL.append(" and DATE_FORMAT(tt1.create_date, '%Y') >=2015");
				}
		    }
	    	strSQL.append(" then 1 else null end) as word_total_count,");
	    	strSQL.append(" COUNT(case when tt1.resource_type = '2050' ");
	    	if("1".equals(qry_stat_type)){
		    	if(StringUtil.isNotEmpty(qry_years)){
		    		strSQL.append(" and DATE_FORMAT(tt1.create_date, '%Y') = ?");
			    	paramList.add(qry_years);
		    	}else {
		    		strSQL.append(" and DATE_FORMAT(tt1.create_date, '%Y') >=2015");
				}
		    }
	    	strSQL.append(" then 1 else null end) as image_total_count");
        	strSQL.append(" from learn_examination_paper tt1,pcmc_user tt2,pcmc_user_dept tt3,pcmc_dept tt4");
        	strSQL.append(" where tt1.user_id = tt2.userid and tt2.userid = tt3.userid and tt3.deptid = tt4.deptid ");
        	strSQL.append(" and tt4.deptcode like '"+qry_deptcode+"%' and tt1.valid = 'Y'");
        	strSQL.append(" and tt4.state = '1' group by tt2.username ) t2 on t2.userid = t1.userid GROUP BY t1.username");
        	strSQL.append(" ) as u order by u.resource_total_count desc,u.deptname");
		}
    	PlatformDao pdao = new PlatformDao();
    	try {
    		pdao.setSql(strSQL.toString());
    		pdao.setBindValues(paramList);
    		Element resData = pdao.executeQuerySql(-1, 1);
    		xmlDocUtil.getResponse().addContent(resData);
    		xmlDocUtil.setResult("0");
    	}catch (Exception e) {
    		e.printStackTrace();
    		log4j.logError("[查询资源增长情况]"+e.getMessage());
    	} finally {
    		pdao.releaseConnection();
    	}
    	
    }
    
    /**
     * 查询用户增长详情(拆线图)
     */
    public void getUserGrowthDetailRecode(){
    	Element reqElement = xmlDocUtil.getRequestData();
    	String  deptcode = xmlDocUtil.getSession().getChildText("deptcode");
		String qry_deptcode = reqElement.getChildText("qry_deptcode");
		String qry_stat_type = reqElement.getChildText("qry_stat_type");
		String qry_years = reqElement.getChildText("qry_years");
		
		if (StringUtil.isEmpty(qry_deptcode)){
			qry_deptcode = deptcode; 
		}
		if (StringUtil.isEmpty(qry_stat_type)){
			qry_stat_type = "1"; 
		}
		StringBuffer strSQL = new StringBuffer();
		ArrayList<Object> paramList = new ArrayList<Object>();
		if("1".equals(qry_stat_type)){  //年
			strSQL.append(" SELECT DATE_FORMAT(t1.create_date, '%Y') AS group_date,");
		}else if("2".equals(qry_stat_type)){//季度
			strSQL.append(" SELECT quarter(t1.create_date) AS group_date,");
		}else if("3".equals(qry_stat_type)){//月份
			strSQL.append(" SELECT DATE_FORMAT(t1.create_date, '%m月') AS group_date,");
		}else if("4".equals(qry_stat_type)){//周
			strSQL.append(" SELECT DATE_FORMAT(t1.create_date, '第%V周') AS group_date,");
		}
		strSQL.append("	count(CASE WHEN t1.usertype = '1' THEN 1 ELSE NULL END)AS stu_total_count,");
		strSQL.append(" count(CASE WHEN t1.usertype = '2' THEN 1 ELSE NULL END)AS th_total_count");
	    strSQL.append("	FROM pcmc_user t1,pcmc_user_dept t2,pcmc_dept t3 WHERE t1.userid = t2.userid and t2.deptid = t3.deptid and t3.state='1' ");
	    if("1".equals(qry_stat_type)){
	    	if(StringUtil.isNotEmpty(qry_years)){
	    		strSQL.append(" and DATE_FORMAT(t1.create_date, '%Y') = ?");
		    	paramList.add(qry_years);
	    	}else {
	    		strSQL.append(" and DATE_FORMAT(t1.create_date, '%Y') >=2015");
			}
	    }
        
        strSQL.append("	and t3.deptcode like ? ");
        paramList.add(qry_deptcode+"%");

		strSQL.append("	GROUP BY group_date order by group_date");
		PlatformDao pdao = new PlatformDao();
		try {
			pdao.setSql(strSQL.toString());
			pdao.setBindValues(paramList);
			Element resData = pdao.executeQuerySql(0, -1);
			xmlDocUtil.getResponse().addContent(resData);
			xmlDocUtil.setResult("0");
		}catch (Exception e) {
			e.printStackTrace();
			log4j.logError("[查询用户增长情况]"+e.getMessage());
		} finally {
			pdao.releaseConnection();
		}
    	
    }
    
    /**
     * 查询资源增长详情(拆线图)
     */
    public void getResourceGrowthDetailRecode(){
    	Element reqElement = xmlDocUtil.getRequestData();
    	String  deptcode = xmlDocUtil.getSession().getChildText("deptcode");
		String qry_deptcode = reqElement.getChildText("qry_deptcode");
		String qry_userid = reqElement.getChildText("qry_userid");
		String qry_stat_type = reqElement.getChildText("qry_stat_type");
		String qry_years = reqElement.getChildText("qry_years");
		
		if (StringUtil.isEmpty(qry_deptcode)){
			qry_deptcode = deptcode; 
		}
		if (StringUtil.isEmpty(qry_stat_type)){
			qry_stat_type = "1"; 
		}
		StringBuffer strSQL = new StringBuffer();
		ArrayList<Object> paramList = new ArrayList<Object>();
		if("1".equals(qry_stat_type)){  //年
			strSQL.append(" SELECT DATE_FORMAT(t1.create_date, '%Y') AS group_date,");
		}else if("2".equals(qry_stat_type)){//季度
			strSQL.append(" SELECT quarter(t1.create_date) AS group_date,");
		}else if("3".equals(qry_stat_type)){//月份
			strSQL.append(" SELECT DATE_FORMAT(t1.create_date, '%m月') AS group_date,");
		}else if("4".equals(qry_stat_type)){//周
			strSQL.append(" SELECT DATE_FORMAT(t1.create_date, '第%V周') AS group_date,");
		}
		strSQL.append("	COUNT(CASE WHEN t1.resource_type LIKE '40%' THEN 1 ELSE NULL END) AS paper_total_count,");
		strSQL.append(" COUNT(CASE WHEN t1.resource_type = '2010' THEN 1 ELSE NULL END) AS video_total_count,");
		strSQL.append(" COUNT(CASE WHEN t1.resource_type = '2020' THEN 1 ELSE NULL END) AS flash_total_count,");
		strSQL.append(" COUNT(CASE WHEN t1.resource_type = '2030' THEN 1 ELSE NULL END) AS ppt_total_count,");
		strSQL.append(" COUNT(CASE WHEN t1.resource_type = '2040' THEN 1 ELSE NULL END) AS word_total_count,");
		strSQL.append(" COUNT(CASE WHEN t1.resource_type = '2050' THEN 1 ELSE NULL END) AS image_total_count");
	    strSQL.append("	FROM learn_examination_paper t1,pcmc_user_dept t2,pcmc_dept t3");
	    if(StringUtil.isNotEmpty(qry_userid)){
	    	strSQL.append(",pcmc_user t4");
	    }
	    strSQL.append(" WHERE t1.user_id = t2.userid AND t2.deptid = t3.deptid and t1.valid='Y' and t3.state='1'");
	    if(StringUtil.isNotEmpty(qry_userid)){
	    	strSQL.append(" AND t4.userid=t2.userid AND t4.userid=?");
	    	paramList.add(qry_userid);
	    }
	    if("1".equals(qry_stat_type)){
	    	if(StringUtil.isNotEmpty(qry_years)){
	    		strSQL.append(" and DATE_FORMAT(t1.create_date, '%Y') = ?");
		    	paramList.add(qry_years);
	    	}else {
	    		strSQL.append(" and DATE_FORMAT(t1.create_date, '%Y') >=2015");
			}
	    }
        
        strSQL.append("	and t3.deptcode like ? ");
        paramList.add(qry_deptcode+"%");

		strSQL.append("	GROUP BY group_date order by group_date");
		PlatformDao pdao = new PlatformDao();
		try {
			pdao.setSql(strSQL.toString());
			pdao.setBindValues(paramList);
			Element resData = pdao.executeQuerySql(0, -1);
			xmlDocUtil.getResponse().addContent(resData);
			xmlDocUtil.setResult("0");
		}catch (Exception e) {
			e.printStackTrace();
			log4j.logError("[查询资源增长情况]"+e.getMessage());
		} finally {
			pdao.releaseConnection();
		}
    }
}
