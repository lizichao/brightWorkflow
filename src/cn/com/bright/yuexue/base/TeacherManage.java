package cn.com.bright.yuexue.base;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

import org.jdom.Document;
import org.jdom.Element;

import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.util.ConfigDocument;
import cn.brightcom.jraf.util.DaoUtil;
import cn.brightcom.jraf.util.DatetimeUtil;
import cn.brightcom.jraf.util.FileUtil;
import cn.brightcom.jraf.util.Log;
import cn.brightcom.jraf.util.StringUtil;
import cn.brightcom.jraf.util.XmlDocPkgUtil;
import cn.brightcom.system.pcmc.util.PcmcUtil;

/**
 * 
 * <p>Title: 教师管理</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2014 Brightcom</p>
 * <p>Company: ShenZhen BrightCom Technology Co.,Ltd.</p>
 * @author <a href="http://www.szbrightcom.cn" target="_blank">QIUMH</a>
 * @version 1.0a
 */
public class TeacherManage extends UserManage
{
	private XmlDocPkgUtil xmlDocUtil = null;
	private String upFolder = "/upload/base/portrait/";
	
    private static Log log4j = new Log(TeacherManage.class.getName());
    
	@Override
	public Document doPost(Document xmlDoc)
	{
		xmlDocUtil = new XmlDocPkgUtil(xmlDoc);
    	String action = xmlDocUtil.getAction();
    	
    	if("add".equals(action))
    	{
    		add();
    	}
    	else if("upt".equals(action))
    	{
    		upt();
    	}
    	else if("import".equals(action))
    	{
    		impt();
    	}
    	else if("getContractList".equals(action))
    	{
    		getContractList();
    	}
    	else if("insertContract".equals(action))
    	{
    		insertContract();
    	}
    	else if("updateContract".equals(action))
    	{
    		updateContract();
    	}
    	else if("getContractById".equals(action))
    	{
    		getContractById();
    	}
    	else if("getPointTo".equals(action))
    	{
    		getPointTo();
    	}
		return xmlDoc;
	}
	
	private void getPointTo(){
		Element reqData = xmlDocUtil.getRequestData();
		String classId = reqData.getChildTextTrim("classId");//教室编号
		String classTime = reqData.getChildTextTrim("classTime");//上课时间点
		boolean isSetTime = false;
		if (StringUtil.isEmpty(classTime)) {
			isSetTime = true;
			Calendar calendar = Calendar.getInstance();
			StringBuffer tempTime = new StringBuffer();
			tempTime.append(calendar.get(Calendar.YEAR)).append("-");
			tempTime.append((calendar.get(Calendar.MONTH)+1)).append("-");
			tempTime.append(calendar.get(Calendar.DAY_OF_MONTH)).append(" ");
			tempTime.append(calendar.get(Calendar.HOUR_OF_DAY)).append(":");
			tempTime.append(calendar.get(Calendar.MINUTE)).append(":");
			tempTime.append("00");
			classTime = tempTime.toString();
		}
		
		PlatformDao dao = null;
		ArrayList<String> list = new ArrayList<String>();
    	StringBuffer sql = new StringBuffer();
    	sql.append(" select a.classid, ");
    	sql.append(" a.studinfoid, ");
    	sql.append(" a.username, ");
    	sql.append(" max(log.logindt) as logindt ");
    	sql.append(" from ( ");
    	
    	sql.append(" 	select c.classid,c.classnm,s.studinfoid,u.userid,u.username  ");
    	sql.append(" 	from base_class c,base_studentinfo s,pcmc_user u  ");
    	sql.append(" 	where c.classid = s.classid and s.userid = u.userid  ");
    	sql.append(" 	and c.state=1 and s.state=1 and u.state=1  ");
    	if (StringUtil.isNotEmpty(classId)) {
    		sql.append(" and c.classid= ? ");
    		list.add(classId);
    	}
    	sql.append(" ) a LEFT JOIN pcmc_uslogin_log log ");
    	sql.append(" on a.userid = log.userid  ");
    	sql.append(" and log.state = 1  ");
    	if (StringUtil.isNotEmpty(classTime)) {
    		sql.append(" and (log.logindt >= date_sub(str_to_date(?, '%Y-%m-%d %H:%i:%s'),interval 1 hour)");
    		sql.append(" and log.logindt <= date_add(str_to_date(?, '%Y-%m-%d %H:%i:%s'),interval 1 hour))");
    		list.add(classTime);
    		list.add(classTime);
    	}
		sql.append(" group by a.studinfoid ");
		sql.append(" order by log.logindt desc ");
    	try {
    		dao = new PlatformDao();
    		dao.setSql(sql.toString());
        	dao.setBindValues(list);
			Element rData = dao.executeQuerySql(-1, 1);
			/*if (isSetTime){
				//reqData.addContent((new Element("classTime")).setText(classTime));//设置时间
				//reqData.re
				reqData.getChild("classTime").setText(classTime);
			}*/
			xmlDocUtil.getResponse().addContent(rData);
			xmlDocUtil.setResult("0");
		} catch (SQLException e) {
			log4j.logError(e);
        	xmlDocUtil.writeErrorMsg("获取签到学生信息异常");
			e.printStackTrace();
		} finally {
			if (dao!=null) dao.releaseConnection();
		}
	}

	private void getContractById() {
		Element reqData = xmlDocUtil.getRequestData();
		String contract_tid = reqData.getChildTextTrim("contract_tid");
		
		PlatformDao pDao = null;
		try
        {
        	pDao = new PlatformDao();
        	ArrayList<String> list = new ArrayList<String>();
        	StringBuffer sb = new StringBuffer();
        	sb.append("SELECT t1.*,t2.username,t2.userid,t4.deptname,t5.subjname FROM busi_contract_teacher t1,pcmc_user t2,pcmc_user_dept t3,pcmc_dept t4,base_subject t5 WHERE t1.teacher_id=t2.userid AND t2.userid=t3.userid AND t3.deptid=t4.deptid AND t1.teach_subject=t5.subjcode and t1.contract_tid = ?");
        	list.add(contract_tid);
        	
        	pDao.setSql(sb.toString());
        	pDao.setBindValues(list);
        	
        	Element rData = pDao.executeQuerySql(0, -1);
        	xmlDocUtil.getResponse().addContent(rData);
        	
        	xmlDocUtil.writeHintMsg("10200", "获取签约教师信息成功！");
        	xmlDocUtil.setResult("0");
        }
        catch(Exception ex)
        {
        	ex.printStackTrace();
        	log4j.logError(ex);
        	xmlDocUtil.writeErrorMsg("10220", "获取签约教师信息异常");
        }
        finally
        {
        	pDao.releaseConnection();
        }
		
		
	}

	private void updateContract() {
		Element reqData = xmlDocUtil.getRequestData();
		Element photo_path = reqData.getChild("photo_path_file");
    	PlatformDao pDao = null;
        try
        {
        	pDao = new PlatformDao();
        	String c_img1 = moveFile(photo_path);
        	
        	Element rec = ConfigDocument.createRecordElement("teacher","busi_contract_teacher");
        	XmlDocPkgUtil.copyValues(reqData,rec,0,true);
        	if(StringUtil.isNotEmpty(c_img1)){
        		XmlDocPkgUtil.setChildText(rec, "photo_path", c_img1);
        	}
        	pDao.updateOneRecord(rec);
        	
        	String[] flds = {"photo_path"};
            Element rData = XmlDocPkgUtil.createMetaData(flds);
            Element pkRec = XmlDocPkgUtil.createRecord(flds, new String[]{c_img1==null?"":c_img1});
            rData.addContent(pkRec);
        	xmlDocUtil.getResponse().addContent(rData);
            
        	xmlDocUtil.writeHintMsg("10200", "修改签约教师成功！");
        	xmlDocUtil.setResult("0");
        }
        catch(Exception ex)
        {
        	ex.printStackTrace();
        	log4j.logError(ex);
        	xmlDocUtil.writeErrorMsg("10220", "修改签约教师异常");
        }
        finally
        {
        	pDao.releaseConnection();
        }
		
	}
	
	private void insertContract() {
		Element reqElement =  xmlDocUtil.getRequestData();
		String  teacher_id = reqElement.getChildText("teacher_id");
		String  teacher_introduct = reqElement.getChildText("teacher_introduct");
		Element  photo_path = reqElement.getChild("photo_path_file");
		String  teach_grade = reqElement.getChildText("teach_grade");
		String  teach_subject = reqElement.getChildText("teach_subject");
		
		PlatformDao pdao = null;
		if(exist(teacher_id)){
		try{
			pdao = new PlatformDao();
			String moveFile = moveFile(photo_path);
			Element updateRecord = ConfigDocument.createRecordElement("teacher", "busi_contract_teacher");
			XmlDocPkgUtil.copyValues(reqElement, updateRecord, 0, true);
			if(StringUtil.isNotEmpty(moveFile)){
				XmlDocPkgUtil.setChildText(updateRecord, "photo_path", moveFile);
				}
			
			Object pk = pdao.insertOneRecordSeqPk(updateRecord);        	
        	String[] flds = {"contract_tid","photo_path"};
            Element rData = XmlDocPkgUtil.createMetaData(flds);
            Element pkRec = XmlDocPkgUtil.createRecord(flds,
            new String[]{""+pk,moveFile});
            rData.addContent(pkRec);
			
			xmlDocUtil.writeHintMsg("10100", "新增签约教师成功！");
        	xmlDocUtil.setResult("0");
        	xmlDocUtil.getResponse().addContent(rData);
		}catch (Exception ex) {
			ex.printStackTrace();
        	log4j.logError(ex);
        	xmlDocUtil.writeErrorMsg("10120", "新增签约教师异常");
        }
		finally
        {
        	pdao.releaseConnection();
        }
	}else {
		xmlDocUtil.writeErrorMsg("10120", "该教师已经是签约状态");
	}
	}
	public boolean exist(String id){
		PlatformDao pDao = null;
		Element record = null;
		boolean flag = true;
		try {
			pDao = new PlatformDao();
			String sql = "select t1.contract_tid from busi_contract_teacher t1 WHERE t1.teacher_id = ? and t1.valid='Y'";
			ArrayList<String> list = new ArrayList<String>();
			list.add(id);
			pDao.setSql(sql);
			pDao.setBindValues(list);
			Element element = pDao.executeQuerySql(0, -1);
			record = element.getChild("Record");
			String contract_tid = record.getChildText("contract_tid");
			if(StringUtil.isEmpty(contract_tid)){
				flag = true;
			}else {
				flag = false;
			}
		} catch (Exception e) {
			
		}
		return flag;
	}
	private void getContractList() {
		Element reqData = xmlDocUtil.getRequestData();
		String qry_userid = reqData.getChildText("qry_userid");
		String teach_grade = reqData.getChildText("teach_grade");
		String teach_subject = reqData.getChildText("teach_subject");
		String valid = reqData.getChildText("valid");
		
		PlatformDao pDao = null;
		ArrayList<String> list = new ArrayList<String>();
		try {
			pDao = new PlatformDao();
			StringBuffer sb = new StringBuffer();
			
			sb.append("SELECT t1.*,t2.username teacher_name,t2.userid,t3.subjname coursenm FROM busi_contract_teacher t1,pcmc_user t2,base_subject t3 WHERE t1.teacher_id=t2.userid and t1.teach_subject=t3.subjcode and t2.state='1'");
			if(StringUtil.isNotEmpty(qry_userid)){
				sb.append(" and t2.userid = ?");
				list.add(qry_userid);
			}
			
			if(StringUtil.isNotEmpty(teach_grade)){
				if("- - - 全部- - - ".equals(teach_grade)){
				
				}else {
					sb.append(" and t1.teach_grade = ?");
					list.add(teach_grade);
				}
			}
			
			if(StringUtil.isNotEmpty(teach_subject)){
				sb.append(" and t1.teach_subject = ?");
				list.add(teach_subject);
			}
			
			if(StringUtil.isNotEmpty(valid)){
				if("无效".equals(valid)){
					sb.append(" and t1.valid = 'N'");
				}else if("有效".equals(valid)){
					sb.append(" and t1.valid = 'Y'");
				}else {
					sb.append(" and t1.valid = 'Y'");
				}
			}else {
				sb.append(" and t1.valid = 'Y'");
			}
			
			pDao.setSql(sb.toString());
			pDao.setBindValues(list);
			
			Element querySql = pDao.executeQuerySql(0, -1);
			xmlDocUtil.getResponse().addContent(querySql);
	    	xmlDocUtil.setResult("0");
		} catch (Exception ex) {
			ex.printStackTrace();
			xmlDocUtil.writeErrorMsg("10617","查询签约名师错误");
		}finally{
			pDao.releaseConnection();
		}
		
	}

	/**
	 * 新增教师
	 */
	private void add()
	{
		Element reqData = xmlDocUtil.getRequestData();
		Element session = xmlDocUtil.getSession();
        String curUserid = session.getChildText("userid");
        String curUsercode = session.getChildText("usercode");
        String deptid = session.getChildText("deptid");
        String teacherinfoid = "";
        PlatformDao pDao = null;
		try
		{
			pDao = new PlatformDao();
			
			
			//新增user
			Element userData = (Element)reqData.clone();
			String userpwd = reqData.getChildTextTrim("userpwd");//登录密码
			String currusercode = reqData.getChildTextTrim("usercode");//登录帐号
			XmlDocPkgUtil.setChildText(userData, "usertype", "2");
			//肖像
			Element imgEle1 = reqData.getChild("portraitFile");
        	String c_img1 = moveFile(imgEle1);
        	if(StringUtil.isNotEmpty(c_img1)){
        		XmlDocPkgUtil.setChildText(userData, "portrait", c_img1);
        	}
			//String[] userids = addUser(userData, curUserid, curUsercode,userpwd);
        	String[] userids = addUser(userData, curUserid, "admin",userpwd);
			String userid = userids[1];
			String usercode = userids[0];
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
			
			//关联机构
			if(StringUtil.isNotEmpty(deptid))
			{
				Element record = ConfigDocument.createRecordElement("pcmc","pcmc_user_dept");
				XmlDocPkgUtil.setChildText(record, "deptid", deptid);
				XmlDocPkgUtil.setChildText(record, "userid", userid);
				XmlDocPkgUtil.setChildText(record, "state", "1");
				XmlDocPkgUtil.setChildText(record, "indate", DatetimeUtil.getNow(null));
				
				pDao.insertOneRecord(record);
				//teacherinfoid = (String)pDao.insertOneRecordSeqPk(record);
			}
			//新注册用户赠送10积分
			Element integralRec = ConfigDocument.createRecordElement("yuexue","sell_integral");
			XmlDocPkgUtil.setChildText(integralRec, "userid", userid);
			XmlDocPkgUtil.setChildText(integralRec, "valid", "Y");
			XmlDocPkgUtil.setChildText(integralRec, "integral_type", "1");
			XmlDocPkgUtil.setChildText(integralRec, "create_by", "管理员");
			XmlDocPkgUtil.setChildText(integralRec, "remark", "注册");
			XmlDocPkgUtil.setChildText(integralRec, "integral_num", "10");
			pDao.insertOneRecordSeqPk(integralRec);
			
			Element oldUserRec = getUserById(userid);
			String oldDeptid = oldUserRec.getChildText("deptid");
			//pDao.commitTransaction();
			pDao.beginTransaction();
			Element teacherRecord = ConfigDocument.createRecordElement("yuexue","base_teacher_info");
			XmlDocPkgUtil.copyValues(reqData,teacherRecord,0,true);
			XmlDocPkgUtil.setChildText(teacherRecord, "userid", userid);
			XmlDocPkgUtil.setChildText(teacherRecord, "state", "1");
			XmlDocPkgUtil.setChildText(teacherRecord, "createdate", DatetimeUtil.getNow(null));
		    pDao.insertOneRecord(teacherRecord);
			pDao.commitTransaction();
			
			String[] flds = {"userid","usercode","portrait"};
			Element data = XmlDocPkgUtil.createMetaData(flds);
			data.addContent(XmlDocPkgUtil.createRecord(flds, new String[]{userid,usercode,c_img1}));
			
			xmlDocUtil.setResult("0");
			xmlDocUtil.getResponse().addContent(data);
			xmlDocUtil.writeHintMsg("10632", "新增教师成功");
		}
		catch(Exception ex)
		{
			pDao.rollBack();
			ex.printStackTrace();
			log4j.logError(ex);
			xmlDocUtil.writeErrorMsg("10634", "新增教师失败");
		}
		finally
		{
			pDao.releaseConnection();
		}
	}
	
	/**
	 * 修改教师
	 */
	private void upt()
	{
		Element reqData = xmlDocUtil.getRequestData();
		Element session = xmlDocUtil.getSession();
        String curUserid = session.getChildText("userid");
        String curUsercode = session.getChildText("usercode");
        String curDeptid = session.getChildText("deptid");
        
        PlatformDao pDao = null;
		try
		{
			pDao = new PlatformDao();
			pDao.beginTransaction();
			
			String deptid = reqData.getChildTextTrim("deptid");
		
			
			Element userData = (Element)reqData.clone();
			//肖像
			Element imgEle1 = reqData.getChild("portraitFile");
        	String c_img1 = moveFile(imgEle1);
        	if(StringUtil.isNotEmpty(c_img1)){
        		XmlDocPkgUtil.setChildText(userData, "portrait", c_img1);
        	}
        	
			String[] userids = uptUser(userData);			
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
			
			Element oldUserRec = getUserById(userid);
			String oldDeptid = oldUserRec.getChildText("deptid");
			String teachinfoid = oldUserRec.getChildText("userid");
			
			//修改教师信息
			Element infoRec = ConfigDocument.createRecordElement("yuexue","base_teacher_info");
			XmlDocPkgUtil.copyValues(reqData,infoRec,0,true);
			boolean chg = false;
			
			if(!deptid.equals(oldDeptid))
			{
				if(PcmcUtil.isSysManager(curUsercode))
				{
					chg = true;
				}
				else
				{
					throw new Exception("所在学校不正确");
				}
			}
			else if(!curDeptid.equals(oldDeptid))
			{
				deptid = curDeptid;
				chg = true;
			}
			
			pDao.updateOneRecord(infoRec);
			pDao.commitTransaction();

			String[] flds = {"userid","usercode","deptid","teachinfoid","portrait"};
			Element data = XmlDocPkgUtil.createMetaData(flds);
			data.addContent(XmlDocPkgUtil.createRecord(flds, new String[]{userid,usercode,deptid,teachinfoid,c_img1}));
			
			//String[] flds = {"portrait"};
			//Element data = XmlDocPkgUtil.createMetaData(flds);
			//data.addContent(XmlDocPkgUtil.createRecord(flds, new String[]{c_img1}));
			
			xmlDocUtil.setResult("0");
			xmlDocUtil.getResponse().addContent(data);
			xmlDocUtil.writeHintMsg("10635", "修改教师信息成功");
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			log4j.logError(ex);
			xmlDocUtil.writeErrorMsg("10636", "修改教师信息失败");
		}
		finally
		{
			pDao.releaseConnection();
		}
	}
	@Override
	protected Element getUserById(String userid)
 	throws Exception
	 {
	 	String sql = "SELECT u.*,d.deptid FROM pcmc_user u,pcmc_user_dept d WHERE u.userid = d.userid AND u.userid=?";
	 	ArrayList bvals = new ArrayList();
	 	bvals.add(userid);
	 	return DaoUtil.getOneRecord(sql, bvals);
	 }
	
	private void impt()
	{
		
	}
    
    private String moveFile(Element upFileEle) throws Exception {
		if(null == upFileEle || StringUtil.isEmpty(upFileEle.getText())) return null;
		String upPath = upFileEle.getText();
		File upFile = new File(FileUtil.getPhysicalPath(upPath));
		//移动
		String urlPath = upFolder+FileUtil.getFileName(upPath);
		String mvFolder = FileUtil.getWebPath()+upFolder;
		String mvPath = FileUtil.getWebPath()+urlPath;
		
		FileUtil.createDirs(mvFolder, true);
		FileUtil.moveFile(upFile, new File(mvPath));
		upFile.delete();
		
		return urlPath;
    }
}
