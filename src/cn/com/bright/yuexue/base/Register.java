package cn.com.bright.yuexue.base;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.jdom.Document;
import org.jdom.Element;

import cn.brightcom.jraf.context.ApplicationContext;
import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.util.ConfigDocument;
import cn.brightcom.jraf.util.Constants;
import cn.brightcom.jraf.util.Crypto;
import cn.brightcom.jraf.util.DatetimeUtil;
import cn.brightcom.jraf.util.Log;
import cn.brightcom.jraf.util.PasswordEncoder;
import cn.brightcom.jraf.util.StringUtil;
import cn.brightcom.jraf.util.XmlDocPkgUtil;
import cn.brightcom.system.pcmc.util.Des;
import cn.com.bright.yuexue.util.SayHelloClient;
import empp.EmppClient;

/**
 * <p>Title:用户注册管理</p>
 * <p>Description: 用户注册管理</p>
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: BrightCom Technologies.</p>
 * @author zhangxq
 * @version 1.0
 * 
 * <p>Function List // 主要函数及其功能
 * 
 *      
 * <p>History: // 历史修改记录:</p>
 * <p> author    time             version      desc</p>
 * <p> zhangxq    2015/05/08       1.0          build this moudle </p>
 *     
 */
public class Register extends UserManage{
	private XmlDocPkgUtil xmlDocUtil = null;
	private Log log4j = new Log(this.getClass().toString());
    /**
     * 动态委派入口
     * @param request xmlDoc 
     * @return response xmlDoc
     */	
	public Document doPost(Document xmlDoc) {
		xmlDocUtil = new XmlDocPkgUtil(xmlDoc);
		String action = xmlDocUtil.getAction();
		if ("addStudent".equals(action)) {
			addStudent();
		}
		else if ("addTeacher".equals(action)) {
			addTeacher();
		}
        else if ("queryUserByUsercode".equals(action)) {
        	queryUserByUsercode();
        }
        else if ("addStudentNoDeptInfo".equals(action)) {
        	addStudentNoDeptInfo();
        }
        else if ("getPhoneValid".equals(action)) {
        	getPhoneValid();
        }
        else if ("findPwdByNote".equals(action)) {
        	findPwdByNote();
        }
        else if ("addBindWeixin".equals(action)) {
        	addBindWeixin();
        }
        else if ("delBindWeixin".equals(action)) {
        	delBindWeixin();
        }
        else if ("getMobileUserid".equals(action)) {
        	getMobileUserid();
        }
		return xmlDoc;
	}
	
	/**
	 * 教师注册
	 *
	 */
	public void addTeacher(){
		Element reqData = xmlDocUtil.getRequestData();
		PlatformDao pdao = new PlatformDao();
		try
		{	String classid="";
			StringBuffer strSQL = new StringBuffer();
			strSQL.append("select t1.classid,t2.courseid from base_class t1 left join base_course t2 ");
			strSQL.append(" on t2.state = '1' and t2.deptid=t1.deptid and t2.gradecode=t1.gradecode and t2.subjectid=?");
			strSQL.append(" where t1.state='1' and t1.gradecode=? and t1.classcode=? and t1.deptid=?");
			
			ArrayList<Object> paramList = new ArrayList<Object>();
			paramList.add(reqData.getChildText("subjectid"));
			paramList.add(reqData.getChildText("gradecode"));
			paramList.add(reqData.getChildText("classcode"));
			paramList.add(reqData.getChildText("deptid"));
			
			pdao.setSql(strSQL.toString());
			pdao.setBindValues(paramList);
			
			Element result = pdao.executeQuerySql(-1,0);			
			List ls = result.getChildren("Record");
			if (ls.size()==0){
				xmlDocUtil.writeErrorMsg("10642", "学校没有该班级");
				return;				
			}
			else{
				Element rec = (Element)ls.get(0);
				classid = rec.getChildText("classid");
				if(StringUtil.isEmpty(rec.getChildText("courseid"))){
					xmlDocUtil.writeErrorMsg("10643", "您选择年级没有该课程");
					return;						
				}
			}
			
			
			pdao.beginTransaction();
			String userpwd = reqData.getChildText("userpwd");//用户登录密码
			if(StringUtil.isEmpty(userpwd)){
				userpwd ="123456";
			}
			String deptid = reqData.getChildText("deptid");	
			Element userData = (Element)reqData.clone();
			XmlDocPkgUtil.setChildText(userData, "usertype", "2");
			String[] userids = addUser(userData, "1", "admin",userpwd);
			String userid = userids[1];
			String usercode = userids[0];
			if("0".equals(usercode))
			{
				//usercode
				xmlDocUtil.writeErrorMsg("10640", "用户名重复");
				return;
			}
			else if("-1".equals(usercode))
			{
				//idnumber
				xmlDocUtil.writeErrorMsg("10641", "身份证号重复");
				return;
			}
			if(StringUtil.isNotEmpty(deptid)){
				Element userDeptReC = ConfigDocument.createRecordElement("pcmc","pcmc_user_dept");
				XmlDocPkgUtil.setChildText(userDeptReC, "deptid", deptid);
				XmlDocPkgUtil.setChildText(userDeptReC, "userid", userid);
				XmlDocPkgUtil.setChildText(userDeptReC, "state", "1");
				XmlDocPkgUtil.setChildText(userDeptReC, "indate", DatetimeUtil.getNow(null));
				
				pdao.insertOneRecord(userDeptReC);				
			}
			
			Element teacherRecord = ConfigDocument.createRecordElement("yuexue","base_teacher_info");
			XmlDocPkgUtil.copyValues(reqData,teacherRecord,0,true);
			XmlDocPkgUtil.setChildText(teacherRecord, "userid", userid);
			XmlDocPkgUtil.setChildText(teacherRecord, "state", "1");
			XmlDocPkgUtil.setChildText(teacherRecord, "createdate", DatetimeUtil.getNow(null));
			pdao.insertOneRecord(teacherRecord);
		    
			//新注册用户赠送10积分
			Element integralRec = ConfigDocument.createRecordElement("yuexue","sell_integral");
			XmlDocPkgUtil.setChildText(integralRec, "userid", userid);
			XmlDocPkgUtil.setChildText(integralRec, "valid", "Y");
			XmlDocPkgUtil.setChildText(integralRec, "integral_type", "1");
			XmlDocPkgUtil.setChildText(integralRec, "create_by", "管理员");
			XmlDocPkgUtil.setChildText(integralRec, "remark", "注册");
			XmlDocPkgUtil.setChildText(integralRec, "integral_num", "10");
			pdao.insertOneRecordSeqPk(integralRec);
			//任教班级
			Element teaSubRec = ConfigDocument.createRecordElement("yuexue","base_teacher_subject");
			XmlDocPkgUtil.copyValues(reqData,teaSubRec,0,true);
			XmlDocPkgUtil.setChildText(teaSubRec, "userid", userid);
			XmlDocPkgUtil.setChildText(teaSubRec, "classid", classid);
			XmlDocPkgUtil.setChildText(teaSubRec, "state", "1");
			XmlDocPkgUtil.setChildText(teaSubRec, "is_classteacher", "0");
			pdao.insertOneRecordSeqPk(teaSubRec);
			
			xmlDocUtil.setResult("0");
			xmlDocUtil.writeHintMsg("10642", "教师注册成功");			
		}
		catch(Exception ex)
		{
			pdao.rollBack();
			ex.printStackTrace();
			log4j.logError(ex);
			xmlDocUtil.writeErrorMsg("10644", "教师注册失败");
		}
		finally
		{
			pdao.releaseConnection();
		}		
	}
	
	/**
	 * 学生注册
	 *
	 */
	public void addStudent(){
		Element reqData = xmlDocUtil.getRequestData();
		PlatformDao pdao = new PlatformDao();
		try
		{	
			String classid= "";
			String deptid = reqData.getChildText("deptid");
			String gradecode = reqData.getChildText("gradecode");
			String classcode = reqData.getChildText("classcode");
			//验证验证码
			String phone_valid = reqData.getChildText("phone_valid");
			if (StringUtil.isNotEmpty(phone_valid)) {
				String valid = ApplicationContext.getRequest().getSession().getAttribute(reqData.getChildText("usercode")).toString();
				if (phone_valid.equals(ApplicationContext.getRequest().getSession().getAttribute(reqData.getChildText("usercode")))) {
					
				} else {
					xmlDocUtil.writeErrorMsg("10642", "验证码不匹配!");
					return ;
				}
			}
			
			//获取学校信息
			if (StringUtil.isEmpty(deptid)) {
				pdao.setSql("select deptid,MIN(gradecode) as gradecode,MIN(classcode) as classcode FROM base_class GROUP BY deptid");
				Element result = pdao.executeQuerySql(-1,0);
				List ls = result.getChildren("Record");
				if (ls.size()==0){
					xmlDocUtil.writeErrorMsg("10642", "未查询到任何学校信息");
					return;				
				}
				deptid = ((Element)ls.get(0)).getChildText("deptid");
				gradecode = ((Element)ls.get(0)).getChildText("gradecode");
				classcode = ((Element)ls.get(0)).getChildText("classcode");
			}
				StringBuffer strSQL = new StringBuffer();
				strSQL.append("select classid from base_class t1 ");
				strSQL.append(" where t1.state='1' and t1.gradecode=? and t1.classcode=? and deptid=?");
				
				ArrayList<Object> paramList = new ArrayList<Object>();
				paramList.add(gradecode);
				paramList.add(classcode);
				paramList.add(deptid);
				pdao.setSql(strSQL.toString());
				pdao.setBindValues(paramList);
				Element result = pdao.executeQuerySql(-1,0);			
				List ls = result.getChildren("Record");
				if (ls.size()==0){
					xmlDocUtil.writeErrorMsg("10642", "学校没有该班级");
					return;				
				}
				else{
					classid = ((Element)ls.get(0)).getChildText("classid");
				}
			pdao.beginTransaction();
			String userpwd = reqData.getChildText("userpwd");//用户登录密码
			userpwd = new Des().strDec(userpwd);

			if(StringUtil.isEmpty(userpwd)){
				userpwd ="123456";
			}
			Element userData = (Element)reqData.clone();
			XmlDocPkgUtil.setChildText(userData, "usertype", "1");
			String[] userids = addUser(userData,"1","admin",userpwd,true);
			String userid = userids[1];
			String usercode = userids[0];
			if("0".equals(usercode))
			{
				//usercode
				xmlDocUtil.writeErrorMsg("10640", "用户名重复");
				return;
			}
			else if("-1".equals(usercode))
			{
				//idnumber
				xmlDocUtil.writeErrorMsg("10641", "身份证号重复");
				return;
			}
			if(StringUtil.isNotEmpty(deptid)){
				Element userDeptReC = ConfigDocument.createRecordElement("pcmc","pcmc_user_dept");
				XmlDocPkgUtil.setChildText(userDeptReC, "deptid", deptid);
				XmlDocPkgUtil.setChildText(userDeptReC, "userid", userid);
				XmlDocPkgUtil.setChildText(userDeptReC, "state", "1");
				XmlDocPkgUtil.setChildText(userDeptReC, "indate", DatetimeUtil.getNow(null));
				
				pdao.insertOneRecord(userDeptReC);				
			}
			
			Element stuRec = ConfigDocument.createRecordElement("yuexue","base_studentinfo");
			XmlDocPkgUtil.copyValues(reqData,stuRec,0,true);
			//XmlDocPkgUtil.setChildText(stuRec, "deptid", deptid);
			XmlDocPkgUtil.setChildText(stuRec, "classid", classid);
			XmlDocPkgUtil.setChildText(stuRec, "stu_name", userData.getChildTextTrim("username"));			
			XmlDocPkgUtil.setChildText(stuRec, "stu_idnumber", userData.getChildTextTrim("idnumber"));
			XmlDocPkgUtil.setChildText(stuRec, "stu_email", userData.getChildTextTrim("email"));
			XmlDocPkgUtil.setChildText(stuRec, "stu_mobile", userData.getChildTextTrim("mobile"));
			XmlDocPkgUtil.setChildText(stuRec, "deptid", deptid);
			XmlDocPkgUtil.setChildText(stuRec, "userid", userid);
			XmlDocPkgUtil.setChildText(stuRec, "state", "1");
			pdao.insertOneRecordSeqPk(stuRec);	
			//新注册用户赠送100积分
			Element integralRec = ConfigDocument.createRecordElement("yuexue","sell_integral");
			XmlDocPkgUtil.setChildText(integralRec, "userid", userid);
			XmlDocPkgUtil.setChildText(integralRec, "valid", "Y");
			XmlDocPkgUtil.setChildText(integralRec, "create_by", "管理员");
			XmlDocPkgUtil.setChildText(integralRec, "integral_num", "100");
			XmlDocPkgUtil.setChildText(integralRec, "integral_type", "1");
			XmlDocPkgUtil.setChildText(integralRec, "remark", "注册");
			pdao.insertOneRecordSeqPk(integralRec);
			
			
			String father_name = reqData.getChildText("father_name");
			String father_mobile = reqData.getChildText("father_mobile");
			if (StringUtil.isNotEmpty(father_name) && StringUtil.isNotEmpty(father_mobile)){
				Element familyRecord = ConfigDocument.createRecordElement("yuexue","base_familyinfo");
				XmlDocPkgUtil.setChildText(familyRecord, "userid", userid);
				XmlDocPkgUtil.setChildText(familyRecord, "name", father_name);
				XmlDocPkgUtil.setChildText(familyRecord, "telephone", father_mobile);
				XmlDocPkgUtil.setChildText(familyRecord, "relation", "父亲");
				pdao.insertOneRecordSeqPk(familyRecord);
			}

			String mother_name = reqData.getChildText("mother_name");
			String mother_mobile = reqData.getChildText("mother_mobile");
			if (StringUtil.isNotEmpty(mother_name) && StringUtil.isNotEmpty(mother_mobile)){
				Element familyRecord = ConfigDocument.createRecordElement("yuexue","base_familyinfo");
				XmlDocPkgUtil.setChildText(familyRecord, "userid", userid);
				XmlDocPkgUtil.setChildText(familyRecord, "name", mother_name);
				XmlDocPkgUtil.setChildText(familyRecord, "telephone", mother_mobile);
				XmlDocPkgUtil.setChildText(familyRecord, "relation", "母亲");
				pdao.insertOneRecordSeqPk(familyRecord);
			}
			
			pdao.commitTransaction();
			
			xmlDocUtil.setResult("0");
			xmlDocUtil.writeHintMsg("10642", "学生注册成功");			
		}
		catch(Exception ex)
		{
			pdao.rollBack();
			ex.printStackTrace();
			log4j.logError(ex);
			xmlDocUtil.writeErrorMsg("10644", "学生注册失败");
		}
		finally
		{
			pdao.releaseConnection();
		}		
	}
	
	/**
	 * 学生注册
	 * 无学校班级信息
	 */
	public void addStudentNoDeptInfo(){
		Element reqData = xmlDocUtil.getRequestData();
		PlatformDao pdao = new PlatformDao();
		try
		{	
			pdao.beginTransaction();
			String userpwd = reqData.getChildText("userpwd");//用户登录密码
			userpwd = new Des().strDec(userpwd);

			if(StringUtil.isEmpty(userpwd)){
				userpwd ="123456";
			}
			Element userData = (Element)reqData.clone();
			XmlDocPkgUtil.setChildText(userData, "usertype", "1");
			String[] userids = addUser(userData,"1","admin",userpwd);
			String userid = userids[1];
			String usercode = userids[0];
			if("0".equals(usercode))
			{
				//usercode
				xmlDocUtil.writeErrorMsg("10640", "用户名重复");
				return;
			}
			else if("-1".equals(usercode))
			{
				//idnumber
				xmlDocUtil.writeErrorMsg("10641", "身份证号重复");
				return;
			}
			Element stuRec = ConfigDocument.createRecordElement("yuexue","base_studentinfo");
			XmlDocPkgUtil.copyValues(reqData,stuRec,0,true);
			//XmlDocPkgUtil.setChildText(stuRec, "deptid", deptid);
			//XmlDocPkgUtil.setChildText(stuRec, "classid", classid);
			XmlDocPkgUtil.setChildText(stuRec, "stu_name", userData.getChildTextTrim("username"));			
			XmlDocPkgUtil.setChildText(stuRec, "stu_idnumber", userData.getChildTextTrim("idnumber"));
			XmlDocPkgUtil.setChildText(stuRec, "stu_email", userData.getChildTextTrim("email"));
			XmlDocPkgUtil.setChildText(stuRec, "stu_mobile", userData.getChildTextTrim("mobile"));
			//XmlDocPkgUtil.setChildText(stuRec, "deptid", deptid);
			XmlDocPkgUtil.setChildText(stuRec, "userid", userid);
			XmlDocPkgUtil.setChildText(stuRec, "state", "1");
			pdao.insertOneRecordSeqPk(stuRec);	
			//新注册用户赠送1000习豆
			Element integralRec = ConfigDocument.createRecordElement("yuexue","sell_integral");
			XmlDocPkgUtil.setChildText(integralRec, "userid", userid);
			XmlDocPkgUtil.setChildText(integralRec, "valid", "Y");
			XmlDocPkgUtil.setChildText(integralRec, "create_by", "系统管理员");
			XmlDocPkgUtil.setChildText(integralRec, "integral_num", "1000");
			pdao.insertOneRecordSeqPk(integralRec);
			
			/*
			String father_name = reqData.getChildText("father_name");
			String father_mobile = reqData.getChildText("father_mobile");
			if (StringUtil.isNotEmpty(father_name) && StringUtil.isNotEmpty(father_mobile)){
				Element familyRecord = ConfigDocument.createRecordElement("yuexue","base_familyinfo");
				XmlDocPkgUtil.setChildText(familyRecord, "userid", userid);
				XmlDocPkgUtil.setChildText(familyRecord, "name", father_name);
				XmlDocPkgUtil.setChildText(familyRecord, "telephone", father_mobile);
				XmlDocPkgUtil.setChildText(familyRecord, "relation", "父亲");
				pdao.insertOneRecordSeqPk(familyRecord);
			}

			String mother_name = reqData.getChildText("mother_name");
			String mother_mobile = reqData.getChildText("mother_mobile");
			if (StringUtil.isNotEmpty(mother_name) && StringUtil.isNotEmpty(mother_mobile)){
				Element familyRecord = ConfigDocument.createRecordElement("yuexue","base_familyinfo");
				XmlDocPkgUtil.setChildText(familyRecord, "userid", userid);
				XmlDocPkgUtil.setChildText(familyRecord, "name", mother_name);
				XmlDocPkgUtil.setChildText(familyRecord, "telephone", mother_mobile);
				XmlDocPkgUtil.setChildText(familyRecord, "relation", "母亲");
				pdao.insertOneRecordSeqPk(familyRecord);
			}*/
			pdao.commitTransaction();
			
			xmlDocUtil.setResult("0");
			xmlDocUtil.writeHintMsg("10642", "学生注册成功");			
		}
		catch(Exception ex)
		{
			pdao.rollBack();
			ex.printStackTrace();
			log4j.logError(ex);
			xmlDocUtil.writeErrorMsg("10644", "学生注册失败");
		}
		finally
		{
			pdao.releaseConnection();
		}		
	}
    
	/**
	 * 根据账号查询用户
	 *
	 */
    private void queryUserByUsercode()
    {
    	Element reqData = xmlDocUtil.getRequestData();
        String usercode = reqData.getChildTextTrim("usercode");
        String deptcodeAlias = reqData.getChildTextTrim("deptcodeAlias");
        
        PlatformDao dao = null;
        try
        {
        	dao = new PlatformDao() ;
        	ArrayList<String> bvals = new ArrayList<String>();
        	
        	StringBuffer sqlBuf = new StringBuffer("select (select u.userid from pcmc_user u where u.usercode = ?) as userid");
        	sqlBuf.append(",(select max(d.deptid) from pcmc_dept d where d.zip = ?) as deptid ");
        	sqlBuf.append("from pcmc_user t where t.usercode = 'admin'");
        	bvals.add(usercode);
        	bvals.add(deptcodeAlias);
            dao.setSql(sqlBuf.toString()) ;
            dao.setBindValues(bvals);
            Element resultElement = dao.executeQuerySql(xmlDocUtil.getPageSize(), xmlDocUtil.getPageNo()) ;
            xmlDocUtil.getResponse().addContent(resultElement) ;
            xmlDocUtil.setResult("0");
        }
        catch (Exception e)
        {
            e.printStackTrace() ;
            xmlDocUtil.setResult("-1");
        }
        finally
        {
            dao.releaseConnection();
        }
    }
    
    /**
     * 获取短信验证码
     */
    private void getPhoneValid() {
    	Element reqElement = xmlDocUtil.getRequestData();
	    String moblie_phone = reqElement.getChildTextTrim("mobile");//接收密码手机号码
	 	StringBuffer str = new StringBuffer();
		ArrayList val = new ArrayList();
		val.add(moblie_phone);
	    	 //验证手机号是否使用
	    	 PlatformDao pdao = new PlatformDao();
	         try
	         {
	             pdao.setSql("select * from PCMC_USER where usercode=? ");
	             pdao.setBindValues(val);
	             Element resData = pdao.executeQuerySql(-1, 0);
	             List ls_result = resData.getChildren("Record");
	    	
		    Random rand = new Random(); 
	   	    for (int i = 0; i < 4;i++) {
	   		  str.append(new Integer(rand.nextInt(10)));
	   	    }
//		   	if (ls_result.size() != 0) {
//		   		xmlDocUtil.writeErrorMsg("10642", "该手机号已使用!");
//		   		return ;
//		   	}
              	if(StringUtil.isNotEmpty(moblie_phone) && moblie_phone.length()==11){
              		String msgid = SayHelloClient.sendSMS(moblie_phone, "您好!您的验证码是:"+str.toString()+"该验证码用于注册账号，请尽快使用避免失效!");
              		if("".equals(msgid)){
              			xmlDocUtil.writeHint("短信发送失败！");
              			return ;
              		}
              		ApplicationContext.getRequest().getSession().setAttribute(moblie_phone,str.toString());
		            xmlDocUtil.writeHint("密码已发送到您的手机,请注意查收！");
           	}else{
           		xmlDocUtil.writeHint("系统找不到您的手机号码，请确认是否填写手机号码！");
           	}
            xmlDocUtil.setResult("0");
	     }catch (Exception e){
	         log4j.logError("[通过短信方式注册用户]" + e.getMessage());
	         xmlDocUtil.writeErrorMsg("10642", "验证码获取失败");
	     }finally{
	           pdao.releaseConnection();
	       }
	 }
    
    
    /**
     * 短信找回密码
     */
    @SuppressWarnings({ "unused", "rawtypes", "unchecked" })
	private void findPwdByNote() {
    	Element reqElement = xmlDocUtil.getRequestData();
	    String usercode = reqElement.getChildTextTrim("usercode");//接收密码手机号码
	 	StringBuffer str = new StringBuffer();
		ArrayList val = new ArrayList();
		val.add(usercode);
	    //验证手机号是否使用
	    PlatformDao pdao = new PlatformDao();
	    try
	    {
	            pdao.setSql("select * from PCMC_USER where usercode=? ");
	            pdao.setBindValues(val);
	            Element resData = pdao.executeQuerySql(-1, 0);
	            List ls_result = resData.getChildren("Record");
	    	
	 		    Random rand = new Random(); 
			   	if (ls_result.size() == 0) {
			   		xmlDocUtil.writeErrorMsg("10642", "该手机号未注册!");
			   		return ;
			   	}
		   		String password =((Element)ls_result.get(0)).getChildText("userpwd");

		   		password = Crypto.decodeByKey(Constants.DEFAULT_KEY,password);
		   		//password = PasswordEncoder.encode(password);
		   		String moblie = ((Element) ls_result.get(0)).getChildText("mobile");
              	if(moblie.equals(usercode)){
              		String msgid = SayHelloClient.sendSMS(usercode, "您好!您的密码是:  "+password+"  该密码用于登录系统，请妥善保存谨防泄露!");
              		if("".equals(msgid)){
              			xmlDocUtil.writeHint("短信发送失败！");
              			return ;
              		}
              	}else{
              		xmlDocUtil.writeErrorMsg("10642", "未通过手机号注册，找回密码失败!");
              	}
              	xmlDocUtil.setResult("0");
              	xmlDocUtil.writeHint("密码已发送到您的手机,请注意查收！");
	     }catch (Exception e){
	         log4j.logError("[通过短信方式注册用户]" + e.getMessage());
	         xmlDocUtil.writeErrorMsg("10642", "验证码获取失败");
	     }finally{
	           pdao.releaseConnection();
	       }
	 }
    
    
    
    /**
	 * 绑定微信
	 *
	 */
	public void addBindWeixin(){
		Element reqData = xmlDocUtil.getRequestData();
		PlatformDao pdao = new PlatformDao();
		try
		{	
			pdao.beginTransaction();
			String userpwd = reqData.getChildText("userpwd");//用户登录密码
			userpwd = PasswordEncoder.encode(userpwd);
           String winxincode=reqData.getChildText("winxincode");
           String usercode=reqData.getChildText("usercode");
           String usertype=reqData.getChildText("usertype");
		
				Element userDeptReC = ConfigDocument.createRecordElement("yuexue","base_winxin_user");
				XmlDocPkgUtil.setChildText(userDeptReC, "winxincode", winxincode);
				XmlDocPkgUtil.setChildText(userDeptReC, "userpwd", userpwd);
				XmlDocPkgUtil.setChildText(userDeptReC, "usertype", usertype);
				XmlDocPkgUtil.setChildText(userDeptReC, "usercode", usercode);
				XmlDocPkgUtil.setChildText(userDeptReC, "valid", "Y");
				
				pdao.insertOneRecord(userDeptReC);				
	
			pdao.commitTransaction();
			
			xmlDocUtil.setResult("0");
			xmlDocUtil.writeHintMsg("10642", "绑定成功");			
		}
		catch(Exception ex)
		{
			pdao.rollBack();
			ex.printStackTrace();
			log4j.logError(ex);
			xmlDocUtil.writeErrorMsg("10644", "绑定失败");
		}
		finally
		{
			pdao.releaseConnection();
		}		
	}
    
    /**
     * 解除绑定微信号
     */
	public void delBindWeixin(){
		Element reqElement =  xmlDocUtil.getRequestData();
		String winxinid = reqElement.getChildText("winxinid");
		PlatformDao pdao = new PlatformDao();
		try {
			StringBuffer strSQL = new StringBuffer();
				strSQL.append(" update base_winxin_user set valid='N' ");
				strSQL.append(" where winxinid=?");
	
			ArrayList<Object> paramList = new ArrayList<Object>();
			paramList.add(winxinid);

			pdao.setSql(strSQL.toString());			
			pdao.setBindValues(paramList);
			pdao.executeTransactionSql();			
		
			xmlDocUtil.setResult("0");				
		}
		catch (Exception e) {
			e.printStackTrace();			
			log4j.logError("[绑定微信]"+e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);             			
		} finally {
			pdao.releaseConnection();
		}		
		
	}
    
	/**
	 * 根据手机号码查询用户信息
	 *
	 */
	private void getMobileUserid()
    {
    	Element reqData = xmlDocUtil.getRequestData();
        String mobile = reqData.getChildTextTrim("qry_mobile");
        String phone = reqData.getChildTextTrim("qry_phone");
        String usertype = reqData.getChildTextTrim("qry_usertype");
        
        PlatformDao dao = null;
        try
        {
        	dao = new PlatformDao() ;
        	ArrayList<String> bvals = new ArrayList<String>();
        	StringBuffer sqlBuf = new StringBuffer("select * from pcmc_user t1 where t1.usertype in ('1','2','3') and t1.state in ('1','8') ");
        	if(StringUtil.isNotEmpty(mobile) && StringUtil.isNotEmpty(phone)){
        		sqlBuf.append(" and (t1.mobile=? or t1.phone=?) ");
        		bvals.add(mobile);
        		bvals.add(phone);
        	} else {
        		if(StringUtil.isNotEmpty(mobile)){
	        		sqlBuf.append(" and t1.mobile=? ");
	        		bvals.add(mobile);
	        	}
	        	if(StringUtil.isNotEmpty(phone)){
	        		sqlBuf.append(" and t1.phone=? ");
	        		bvals.add(phone);
	        	}
        	}
        	if(StringUtil.isNotEmpty(usertype)){
        		sqlBuf.append(" and t1.usertype=? ");
        		bvals.add(usertype);
        	}
            dao.setSql(sqlBuf.toString());
            dao.setBindValues(bvals);
            Element resultElement = dao.executeQuerySql(xmlDocUtil.getPageSize(), xmlDocUtil.getPageNo()) ;
            xmlDocUtil.getResponse().addContent(resultElement) ;
            xmlDocUtil.setResult("0");
        }
        catch (Exception e)
        {
            e.printStackTrace() ;
            xmlDocUtil.setResult("-1");
        }
        finally
        {
            dao.releaseConnection();
        }
    }
    
}