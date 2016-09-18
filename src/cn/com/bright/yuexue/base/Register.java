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
 * <p>Title:�û�ע�����</p>
 * <p>Description: �û�ע�����</p>
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
 * <p> zhangxq    2015/05/08       1.0          build this moudle </p>
 *     
 */
public class Register extends UserManage{
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
	 * ��ʦע��
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
				xmlDocUtil.writeErrorMsg("10642", "ѧУû�иð༶");
				return;				
			}
			else{
				Element rec = (Element)ls.get(0);
				classid = rec.getChildText("classid");
				if(StringUtil.isEmpty(rec.getChildText("courseid"))){
					xmlDocUtil.writeErrorMsg("10643", "��ѡ���꼶û�иÿγ�");
					return;						
				}
			}
			
			
			pdao.beginTransaction();
			String userpwd = reqData.getChildText("userpwd");//�û���¼����
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
				xmlDocUtil.writeErrorMsg("10640", "�û����ظ�");
				return;
			}
			else if("-1".equals(usercode))
			{
				//idnumber
				xmlDocUtil.writeErrorMsg("10641", "���֤���ظ�");
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
		    
			//��ע���û�����10����
			Element integralRec = ConfigDocument.createRecordElement("yuexue","sell_integral");
			XmlDocPkgUtil.setChildText(integralRec, "userid", userid);
			XmlDocPkgUtil.setChildText(integralRec, "valid", "Y");
			XmlDocPkgUtil.setChildText(integralRec, "integral_type", "1");
			XmlDocPkgUtil.setChildText(integralRec, "create_by", "����Ա");
			XmlDocPkgUtil.setChildText(integralRec, "remark", "ע��");
			XmlDocPkgUtil.setChildText(integralRec, "integral_num", "10");
			pdao.insertOneRecordSeqPk(integralRec);
			//�ν̰༶
			Element teaSubRec = ConfigDocument.createRecordElement("yuexue","base_teacher_subject");
			XmlDocPkgUtil.copyValues(reqData,teaSubRec,0,true);
			XmlDocPkgUtil.setChildText(teaSubRec, "userid", userid);
			XmlDocPkgUtil.setChildText(teaSubRec, "classid", classid);
			XmlDocPkgUtil.setChildText(teaSubRec, "state", "1");
			XmlDocPkgUtil.setChildText(teaSubRec, "is_classteacher", "0");
			pdao.insertOneRecordSeqPk(teaSubRec);
			
			xmlDocUtil.setResult("0");
			xmlDocUtil.writeHintMsg("10642", "��ʦע��ɹ�");			
		}
		catch(Exception ex)
		{
			pdao.rollBack();
			ex.printStackTrace();
			log4j.logError(ex);
			xmlDocUtil.writeErrorMsg("10644", "��ʦע��ʧ��");
		}
		finally
		{
			pdao.releaseConnection();
		}		
	}
	
	/**
	 * ѧ��ע��
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
			//��֤��֤��
			String phone_valid = reqData.getChildText("phone_valid");
			if (StringUtil.isNotEmpty(phone_valid)) {
				String valid = ApplicationContext.getRequest().getSession().getAttribute(reqData.getChildText("usercode")).toString();
				if (phone_valid.equals(ApplicationContext.getRequest().getSession().getAttribute(reqData.getChildText("usercode")))) {
					
				} else {
					xmlDocUtil.writeErrorMsg("10642", "��֤�벻ƥ��!");
					return ;
				}
			}
			
			//��ȡѧУ��Ϣ
			if (StringUtil.isEmpty(deptid)) {
				pdao.setSql("select deptid,MIN(gradecode) as gradecode,MIN(classcode) as classcode FROM base_class GROUP BY deptid");
				Element result = pdao.executeQuerySql(-1,0);
				List ls = result.getChildren("Record");
				if (ls.size()==0){
					xmlDocUtil.writeErrorMsg("10642", "δ��ѯ���κ�ѧУ��Ϣ");
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
					xmlDocUtil.writeErrorMsg("10642", "ѧУû�иð༶");
					return;				
				}
				else{
					classid = ((Element)ls.get(0)).getChildText("classid");
				}
			pdao.beginTransaction();
			String userpwd = reqData.getChildText("userpwd");//�û���¼����
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
				xmlDocUtil.writeErrorMsg("10640", "�û����ظ�");
				return;
			}
			else if("-1".equals(usercode))
			{
				//idnumber
				xmlDocUtil.writeErrorMsg("10641", "���֤���ظ�");
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
			//��ע���û�����100����
			Element integralRec = ConfigDocument.createRecordElement("yuexue","sell_integral");
			XmlDocPkgUtil.setChildText(integralRec, "userid", userid);
			XmlDocPkgUtil.setChildText(integralRec, "valid", "Y");
			XmlDocPkgUtil.setChildText(integralRec, "create_by", "����Ա");
			XmlDocPkgUtil.setChildText(integralRec, "integral_num", "100");
			XmlDocPkgUtil.setChildText(integralRec, "integral_type", "1");
			XmlDocPkgUtil.setChildText(integralRec, "remark", "ע��");
			pdao.insertOneRecordSeqPk(integralRec);
			
			
			String father_name = reqData.getChildText("father_name");
			String father_mobile = reqData.getChildText("father_mobile");
			if (StringUtil.isNotEmpty(father_name) && StringUtil.isNotEmpty(father_mobile)){
				Element familyRecord = ConfigDocument.createRecordElement("yuexue","base_familyinfo");
				XmlDocPkgUtil.setChildText(familyRecord, "userid", userid);
				XmlDocPkgUtil.setChildText(familyRecord, "name", father_name);
				XmlDocPkgUtil.setChildText(familyRecord, "telephone", father_mobile);
				XmlDocPkgUtil.setChildText(familyRecord, "relation", "����");
				pdao.insertOneRecordSeqPk(familyRecord);
			}

			String mother_name = reqData.getChildText("mother_name");
			String mother_mobile = reqData.getChildText("mother_mobile");
			if (StringUtil.isNotEmpty(mother_name) && StringUtil.isNotEmpty(mother_mobile)){
				Element familyRecord = ConfigDocument.createRecordElement("yuexue","base_familyinfo");
				XmlDocPkgUtil.setChildText(familyRecord, "userid", userid);
				XmlDocPkgUtil.setChildText(familyRecord, "name", mother_name);
				XmlDocPkgUtil.setChildText(familyRecord, "telephone", mother_mobile);
				XmlDocPkgUtil.setChildText(familyRecord, "relation", "ĸ��");
				pdao.insertOneRecordSeqPk(familyRecord);
			}
			
			pdao.commitTransaction();
			
			xmlDocUtil.setResult("0");
			xmlDocUtil.writeHintMsg("10642", "ѧ��ע��ɹ�");			
		}
		catch(Exception ex)
		{
			pdao.rollBack();
			ex.printStackTrace();
			log4j.logError(ex);
			xmlDocUtil.writeErrorMsg("10644", "ѧ��ע��ʧ��");
		}
		finally
		{
			pdao.releaseConnection();
		}		
	}
	
	/**
	 * ѧ��ע��
	 * ��ѧУ�༶��Ϣ
	 */
	public void addStudentNoDeptInfo(){
		Element reqData = xmlDocUtil.getRequestData();
		PlatformDao pdao = new PlatformDao();
		try
		{	
			pdao.beginTransaction();
			String userpwd = reqData.getChildText("userpwd");//�û���¼����
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
				xmlDocUtil.writeErrorMsg("10640", "�û����ظ�");
				return;
			}
			else if("-1".equals(usercode))
			{
				//idnumber
				xmlDocUtil.writeErrorMsg("10641", "���֤���ظ�");
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
			//��ע���û�����1000ϰ��
			Element integralRec = ConfigDocument.createRecordElement("yuexue","sell_integral");
			XmlDocPkgUtil.setChildText(integralRec, "userid", userid);
			XmlDocPkgUtil.setChildText(integralRec, "valid", "Y");
			XmlDocPkgUtil.setChildText(integralRec, "create_by", "ϵͳ����Ա");
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
				XmlDocPkgUtil.setChildText(familyRecord, "relation", "����");
				pdao.insertOneRecordSeqPk(familyRecord);
			}

			String mother_name = reqData.getChildText("mother_name");
			String mother_mobile = reqData.getChildText("mother_mobile");
			if (StringUtil.isNotEmpty(mother_name) && StringUtil.isNotEmpty(mother_mobile)){
				Element familyRecord = ConfigDocument.createRecordElement("yuexue","base_familyinfo");
				XmlDocPkgUtil.setChildText(familyRecord, "userid", userid);
				XmlDocPkgUtil.setChildText(familyRecord, "name", mother_name);
				XmlDocPkgUtil.setChildText(familyRecord, "telephone", mother_mobile);
				XmlDocPkgUtil.setChildText(familyRecord, "relation", "ĸ��");
				pdao.insertOneRecordSeqPk(familyRecord);
			}*/
			pdao.commitTransaction();
			
			xmlDocUtil.setResult("0");
			xmlDocUtil.writeHintMsg("10642", "ѧ��ע��ɹ�");			
		}
		catch(Exception ex)
		{
			pdao.rollBack();
			ex.printStackTrace();
			log4j.logError(ex);
			xmlDocUtil.writeErrorMsg("10644", "ѧ��ע��ʧ��");
		}
		finally
		{
			pdao.releaseConnection();
		}		
	}
    
	/**
	 * �����˺Ų�ѯ�û�
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
     * ��ȡ������֤��
     */
    private void getPhoneValid() {
    	Element reqElement = xmlDocUtil.getRequestData();
	    String moblie_phone = reqElement.getChildTextTrim("mobile");//���������ֻ�����
	 	StringBuffer str = new StringBuffer();
		ArrayList val = new ArrayList();
		val.add(moblie_phone);
	    	 //��֤�ֻ����Ƿ�ʹ��
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
//		   		xmlDocUtil.writeErrorMsg("10642", "���ֻ�����ʹ��!");
//		   		return ;
//		   	}
              	if(StringUtil.isNotEmpty(moblie_phone) && moblie_phone.length()==11){
              		String msgid = SayHelloClient.sendSMS(moblie_phone, "����!������֤����:"+str.toString()+"����֤������ע���˺ţ��뾡��ʹ�ñ���ʧЧ!");
              		if("".equals(msgid)){
              			xmlDocUtil.writeHint("���ŷ���ʧ�ܣ�");
              			return ;
              		}
              		ApplicationContext.getRequest().getSession().setAttribute(moblie_phone,str.toString());
		            xmlDocUtil.writeHint("�����ѷ��͵������ֻ�,��ע����գ�");
           	}else{
           		xmlDocUtil.writeHint("ϵͳ�Ҳ��������ֻ����룬��ȷ���Ƿ���д�ֻ����룡");
           	}
            xmlDocUtil.setResult("0");
	     }catch (Exception e){
	         log4j.logError("[ͨ�����ŷ�ʽע���û�]" + e.getMessage());
	         xmlDocUtil.writeErrorMsg("10642", "��֤���ȡʧ��");
	     }finally{
	           pdao.releaseConnection();
	       }
	 }
    
    
    /**
     * �����һ�����
     */
    @SuppressWarnings({ "unused", "rawtypes", "unchecked" })
	private void findPwdByNote() {
    	Element reqElement = xmlDocUtil.getRequestData();
	    String usercode = reqElement.getChildTextTrim("usercode");//���������ֻ�����
	 	StringBuffer str = new StringBuffer();
		ArrayList val = new ArrayList();
		val.add(usercode);
	    //��֤�ֻ����Ƿ�ʹ��
	    PlatformDao pdao = new PlatformDao();
	    try
	    {
	            pdao.setSql("select * from PCMC_USER where usercode=? ");
	            pdao.setBindValues(val);
	            Element resData = pdao.executeQuerySql(-1, 0);
	            List ls_result = resData.getChildren("Record");
	    	
	 		    Random rand = new Random(); 
			   	if (ls_result.size() == 0) {
			   		xmlDocUtil.writeErrorMsg("10642", "���ֻ���δע��!");
			   		return ;
			   	}
		   		String password =((Element)ls_result.get(0)).getChildText("userpwd");

		   		password = Crypto.decodeByKey(Constants.DEFAULT_KEY,password);
		   		//password = PasswordEncoder.encode(password);
		   		String moblie = ((Element) ls_result.get(0)).getChildText("mobile");
              	if(moblie.equals(usercode)){
              		String msgid = SayHelloClient.sendSMS(usercode, "����!����������:  "+password+"  ���������ڵ�¼ϵͳ�������Ʊ������й¶!");
              		if("".equals(msgid)){
              			xmlDocUtil.writeHint("���ŷ���ʧ�ܣ�");
              			return ;
              		}
              	}else{
              		xmlDocUtil.writeErrorMsg("10642", "δͨ���ֻ���ע�ᣬ�һ�����ʧ��!");
              	}
              	xmlDocUtil.setResult("0");
              	xmlDocUtil.writeHint("�����ѷ��͵������ֻ�,��ע����գ�");
	     }catch (Exception e){
	         log4j.logError("[ͨ�����ŷ�ʽע���û�]" + e.getMessage());
	         xmlDocUtil.writeErrorMsg("10642", "��֤���ȡʧ��");
	     }finally{
	           pdao.releaseConnection();
	       }
	 }
    
    
    
    /**
	 * ��΢��
	 *
	 */
	public void addBindWeixin(){
		Element reqData = xmlDocUtil.getRequestData();
		PlatformDao pdao = new PlatformDao();
		try
		{	
			pdao.beginTransaction();
			String userpwd = reqData.getChildText("userpwd");//�û���¼����
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
			xmlDocUtil.writeHintMsg("10642", "�󶨳ɹ�");			
		}
		catch(Exception ex)
		{
			pdao.rollBack();
			ex.printStackTrace();
			log4j.logError(ex);
			xmlDocUtil.writeErrorMsg("10644", "��ʧ��");
		}
		finally
		{
			pdao.releaseConnection();
		}		
	}
    
    /**
     * �����΢�ź�
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
			log4j.logError("[��΢��]"+e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);             			
		} finally {
			pdao.releaseConnection();
		}		
		
	}
    
	/**
	 * �����ֻ������ѯ�û���Ϣ
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