package cn.com.bright.yuexue.base;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;

import cn.brightcom.jraf.context.ApplicationContext;
import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.util.ConfigDocument;
import cn.brightcom.jraf.util.Constants;
import cn.brightcom.jraf.util.Crypto;
import cn.brightcom.jraf.util.DaoUtil;
import cn.brightcom.jraf.util.DatetimeUtil;
import cn.brightcom.jraf.util.FileUtil;
import cn.brightcom.jraf.util.Log;
import cn.brightcom.jraf.util.PasswordEncoder;
import cn.brightcom.jraf.util.StringUtil;
import cn.brightcom.jraf.util.XmlDocPkgUtil;
import cn.brightcom.system.pcmc.sm.LogBean;
import cn.brightcom.system.pcmc.util.Des;
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
public class StudentManage extends UserManage
{
	private XmlDocPkgUtil xmlDocUtil = null;
	private String upFolder = "/upload/base/portrait/";
    
    private static Log log4j = new Log(StudentManage.class.getName());

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
    	else if("moveClass".equals(action))
    	{
    		moveClass();
    	}
    	else if("transactionApprove".equals(action))
    	{
    		transactionApprove();
    	}
    	else if("delStudentInfo".equals(action)){
    		delStudentInfo();
    	}
    	else if("uptStudentPwd".equals(action)){
    		uptStudentPwd();
    	}
		return xmlDoc;
	}
    
	private void add()
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
        	
			String userid = reqData.getChildText("userid");
			String usercode = reqData.getChildText("usercode");
			String userpwd = reqData.getChildText("userpwd");//用户登录密码
			if(StringUtil.isEmpty(userpwd)){
				userpwd ="123456";
			}
			String deptid = reqData.getChildText("deptid");
			String studinfoid = "";
			Element userData = (Element)reqData.clone();
        	//新增user
			XmlDocPkgUtil.setChildText(userData, "usertype", "1");
			//肖像
			Element imgEle1 = reqData.getChild("portraitFile");
        	String c_img1 = moveFile(imgEle1);
        	if(StringUtil.isNotEmpty(c_img1)){
        		XmlDocPkgUtil.setChildText(userData, "portrait", c_img1);
        	}
        	
			//String[] userids = addUser(userData, curUserid, curUsercode);
        	String[] userids = addUser(userData, curUserid, "admin",userpwd);
			userid = userids[1];
			usercode = userids[0];
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
			
			//关联机构
			if(StringUtil.isNotEmpty(curDeptid))
			{
				Element record = ConfigDocument.createRecordElement("pcmc","pcmc_user_dept");
				XmlDocPkgUtil.setChildText(record, "deptid", curDeptid);
				XmlDocPkgUtil.setChildText(record, "userid", userid);
				XmlDocPkgUtil.setChildText(record, "state", "1");
				XmlDocPkgUtil.setChildText(record, "indate", DatetimeUtil.getNow(null));
				
				pDao.insertOneRecord(record);
			}
			
			//学生基本信息
			Element record = ConfigDocument.createRecordElement("yuexue","base_studentinfo");
			XmlDocPkgUtil.copyValues(reqData,record,0,true);
			XmlDocPkgUtil.setChildText(record, "deptid", curDeptid);
			XmlDocPkgUtil.setChildText(record, "stu_name", userData.getChildTextTrim("username"));
			XmlDocPkgUtil.setChildText(record, "stu_sex", userData.getChildTextTrim("gender"));
			XmlDocPkgUtil.setChildText(record, "stu_idnumber", userData.getChildTextTrim("idnumber"));
			XmlDocPkgUtil.setChildText(record, "stu_email", userData.getChildTextTrim("email"));
			XmlDocPkgUtil.setChildText(record, "stu_mobile", userData.getChildTextTrim("mobile"));
			XmlDocPkgUtil.setChildText(record, "stbirth", userData.getChildTextTrim("birthday"));
			XmlDocPkgUtil.setChildText(record, "telephone", userData.getChildTextTrim("phone"));
			XmlDocPkgUtil.setChildText(record, "deptid", curDeptid);
			XmlDocPkgUtil.setChildText(record, "userid", userid);
			XmlDocPkgUtil.setChildText(record, "state", "1");
			studinfoid = (String)pDao.insertOneRecordSeqPk(record);
			
			/*//学生扩展属性表
			Element studentAttrRecord = ConfigDocument.createRecordElement("yuexue","base_studentinfo_attr");
			XmlDocPkgUtil.copyValues(reqData,studentAttrRecord,0,true);
			XmlDocPkgUtil.setChildText(studentAttrRecord, "userid", userid);
			XmlDocPkgUtil.setChildText(studentAttrRecord, "state", "1");
		    pDao.insertOneRecord(studentAttrRecord);
			pDao.commitTransaction();*/
			
			//新注册用户赠送100积分
			Element integralRec = ConfigDocument.createRecordElement("yuexue","sell_integral");
			XmlDocPkgUtil.setChildText(integralRec, "userid", userid);
			XmlDocPkgUtil.setChildText(integralRec, "valid", "Y");
			XmlDocPkgUtil.setChildText(integralRec, "create_by", "管理员");
			XmlDocPkgUtil.setChildText(integralRec, "integral_num", "100");
			XmlDocPkgUtil.setChildText(integralRec, "integral_type", "1");
			XmlDocPkgUtil.setChildText(integralRec, "remark", "注册");
			pDao.insertOneRecordSeqPk(integralRec);
			
			String[] flds = {"userid","usercode","deptid","studinfoid","portrait"};
			Element data = XmlDocPkgUtil.createMetaData(flds);
			data.addContent(XmlDocPkgUtil.createRecord(flds, new String[]{userid,usercode,deptid,studinfoid,c_img1}));
			
			xmlDocUtil.setResult("0");
			xmlDocUtil.getResponse().addContent(data);
			xmlDocUtil.writeHintMsg("10642", "新增学生成功");
		}
		catch(Exception ex)
		{
			pDao.rollBack();
			ex.printStackTrace();
			log4j.logError(ex);
			xmlDocUtil.writeErrorMsg("10644", "新增学生失败");
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
        String curUserid = session.getChildText("userid");
        String curUsercode = session.getChildText("usercode");
        String curDeptid = session.getChildText("deptid");
		
		PlatformDao pDao = null;
		try
		{
			pDao = new PlatformDao();
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
				xmlDocUtil.writeErrorMsg("10640", "用户名重复");
				return;
			}
			else if("-1".equals(usercode))
			{
				//idnumber
				xmlDocUtil.writeErrorMsg("10641", "身份证号重复");
				return;
			}
			
			Element oldUserRec = getUserById(userid);
			String oldDeptid = oldUserRec.getChildText("deptid");
			String studinfoid = oldUserRec.getChildText("studinfoid");
			pDao.beginTransaction();
			//修改学生信息
			Element infoRec = ConfigDocument.createRecordElement("yuexue","base_studentinfo");
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
			if(chg)
			{
				String sql = "update base_studentinfo set state=?,transferdt=? where studinfoid=?";
				ArrayList<Object> bvals = new ArrayList<Object>();
				bvals.add("0");
				bvals.add(DatetimeUtil.getNow());
				bvals.add(studinfoid);
				pDao.setSql(sql);
				pDao.setBindValues(bvals);
				pDao.executeTransactionSql();
				
				infoRec.removeChild("studinfoid");
				infoRec.removeChild("classid");
				XmlDocPkgUtil.setChildText(infoRec, "deptid", deptid);
				XmlDocPkgUtil.setChildText(infoRec, "state", "1");
				studinfoid = (String)pDao.insertOneRecordSeqPk(infoRec);
			}
			else
			{
				XmlDocPkgUtil.setChildText(infoRec, "stu_name", userData.getChildTextTrim("username"));
				XmlDocPkgUtil.setChildText(infoRec, "stu_sex", userData.getChildTextTrim("gender"));
				XmlDocPkgUtil.setChildText(infoRec, "stu_idnumber", userData.getChildTextTrim("idnumber"));
				XmlDocPkgUtil.setChildText(infoRec, "stu_email", userData.getChildTextTrim("email"));
				XmlDocPkgUtil.setChildText(infoRec, "stu_mobile", userData.getChildTextTrim("mobile"));
				XmlDocPkgUtil.setChildText(infoRec, "stbirth", userData.getChildTextTrim("birthday"));
				XmlDocPkgUtil.setChildText(infoRec, "telephone", userData.getChildTextTrim("phone"));
				pDao.updateOneRecord(infoRec);
			}
			
		/*	//修改学生属性扩展信息
			Element studentAttrRec = ConfigDocument.createRecordElement("yuexue","base_studentinfo_attr");
			XmlDocPkgUtil.copyValues(reqData,studentAttrRec,0,true);
			pDao.updateOneRecord(studentAttrRec);
			
			pDao.commitTransaction();
*/
			String[] flds = {"userid","usercode","deptid","studinfoid","portrait"};
			Element data = XmlDocPkgUtil.createMetaData(flds);
			data.addContent(XmlDocPkgUtil.createRecord(flds, new String[]{userid,usercode,deptid,studinfoid,c_img1}));
			
			xmlDocUtil.setResult("0");
			xmlDocUtil.getResponse().addContent(data);
			xmlDocUtil.writeHintMsg("10645", "修改学生信息成功");
		}
		catch(Exception ex)
		{
			pDao.rollBack();
			ex.printStackTrace();log4j.logError(ex);
			xmlDocUtil.writeErrorMsg("10646", "修改学生信息失败");
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
    	String sql = "select a.*,b.studinfoid,b.deptid,b.classid from pcmc_user a,base_studentinfo b where a.userid=b.userid and a.userid=?";
    	ArrayList<Object> bvals = new ArrayList<Object>();
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
    /**
     * 班主任调班
     */
    private void moveClass(){
    	Element reqData = xmlDocUtil.getRequestData();
    	List useridsList  = reqData.getChildren("student_id");//待调班学生编号
    	String classids = reqData.getChildTextTrim("class_id");//班级ID
    	StringBuffer strClassids = new StringBuffer();
    	StringBuffer stuinfoids = new StringBuffer();
    	StringBuffer userids = new StringBuffer();
    	StringBuffer strSQL = new StringBuffer();
    	StringBuffer msg = new StringBuffer("(");
    	StringBuffer targetObj = new StringBuffer();
    	StringBuffer targetUserName = new StringBuffer();
    	
    	String className = "";
    	int avgScore=0;
    	double arrayScore[],compareScore[];
    	String studentids[],compareUserName[];
    	StringBuffer objTarget = new StringBuffer();
    	StringBuffer hkxz =new StringBuffer();//户口性质
    	StringBuffer gender = new StringBuffer();;//性别
    	try{
	    	if(useridsList.size()>0){
	    		for(int i=0;i<useridsList.size();i++){
	    			Element userEl = (Element)useridsList.get(i);
	    			userids.append(userEl.getText());
	    			if((i+1)!=useridsList.size()){
	    				userids.append(",");
	    			}
	    		}
	    	}
	    	
	    	List class_list = this.getMoveClassList(userids.toString());
	    	arrayScore = new double[class_list.size()];
	    	for(int j=0;j<class_list.size();j++){
	    		Element el = (Element)class_list.get(j);
	    		arrayScore[j] = Double.valueOf(el.getChildText("avg_score"));
	    		hkxz.append(el.getChildTextTrim("hkxz"));//户口性质
	    		gender.append(el.getChildTextTrim("stu_sex"));//性别
	    		strClassids.append(el.getChildText("classid"));//班级ID
	    		stuinfoids.append(el.getChildText("studinfoid"));//学生编号
	    		className = el.getChildText("classnm");//班级名称
	    		msg.append(el.getChildText("stu_name"));
    			if((j+1)!=class_list.size()){
    				if(StringUtil.isNotEmpty(el.getChildTextTrim("hkxz")))hkxz.append(",");
    				if(StringUtil.isNotEmpty(el.getChildTextTrim("stu_sex")))gender.append(",");
    				if(StringUtil.isNotEmpty(el.getChildTextTrim("classid")))strClassids.append(",");
    				if(StringUtil.isNotEmpty(el.getChildTextTrim("studinfoid")))stuinfoids.append(",");
    				if(StringUtil.isNotEmpty(el.getChildTextTrim("stu_name")))msg.append(",");
    			}else{msg.append(") 成功调班");}
	    	}
	    	
	    	List data_list = this.compareData(classids, gender.toString(), hkxz.toString());
	    	compareScore = new double[data_list.size()];
	    	studentids = new String[data_list.size()];
	    	compareUserName = new String[data_list.size()];
			for(int j=0;j<data_list.size();j++){
    			Element el = (Element)data_list.get(j);
	    		if(StringUtil.isNotEmpty(el.getChildText("avg_score"))){
	    			compareScore[j] = Double.valueOf(el.getChildText("avg_score"));
	    			studentids[j] = el.getChildText("studinfoid");
	    			compareUserName[j] = el.getChildText("stu_name");
		    		//msg.append(""+el.getChildTextTrim("classnm")+"同时,("+el.getChildText("stu_name"));
		    	}
	    		if(j==(data_list.size()-1)){
	    			msg.append("至").append(el.getChildTextTrim("classnm")+"  同时,");
	    		}
    		}
			
			for(int i=0;i<arrayScore.length;i++){
				int low=0,middle=0;
				int high = compareScore.length-1;
				while(low<=high){
					middle=(low+high)/2;
					if(arrayScore[i] == compareScore[middle]){
						objTarget.append(studentids[middle]);
						break;
					}
					if(arrayScore[i] > compareScore[middle]){
						low = middle + 1;
					}
					if(arrayScore[i] < compareScore[middle]){
						high = middle - 1;
					}
				}
				if(high == -1) high=0;
				
				objTarget.append(studentids[high]);
				msg.append(compareUserName[high]).append("成功调班至").append(className);
			}
			this.modifyClassids(objTarget.toString(), classids,strClassids.toString(),stuinfoids.toString());
			xmlDocUtil.setResult("0");
			xmlDocUtil.writeHint(msg.toString());
    	}catch(Exception e){
    		e.printStackTrace();
    	}
     }
    /**
     * 获得待调班学生信息
     * @param str
     * @return
     */
    private List getMoveClassList(String str){
    	StringBuffer strBuffer = new StringBuffer();
    	PlatformDao pdao = new PlatformDao();
    	try {
			//根据学生ID获得待调班因素
			strBuffer.append("select tt.* from (select t.*,(SELECT IFNULL(AVG(final_score),0) FROM base_student_score WHERE studinfoid =t.studinfoid) as avg_score,c.classnm from base_studentinfo t,base_class c ");
			strBuffer.append("where t.classid = c.classid and t.studinfoid in('"+str.replace(",", "','")+"') and t.valid='Y') tt where tt.avg_score <>'0'");
			pdao.setSql(strBuffer.toString());
			Element scoreEl = (Element)pdao.executeQuerySql(0, -1);
			return scoreEl.getChildren("Record");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally{
			pdao.releaseConnection();
		}
    }
    
    /**
     * 获得调班后学生列表
     * @param classids
     */
    public List compareData(String classid,String param1,String param2){
    	StringBuffer strSQL = new StringBuffer();
    	PlatformDao pdao = new PlatformDao();
    	try{
	    	//根据班级ID获得该班学生待调班因素
			strSQL.append("select tt.* from (select t.*,(SELECT IFNULL(AVG(final_score),0) FROM base_student_score WHERE studinfoid =t.studinfoid) as avg_score,c.classnm from base_studentinfo t,base_class c ");
			strSQL.append("where t.classid = c.classid and  t.classid=? and t.valid='Y'");
			ArrayList<Object> paramVal = new ArrayList<Object>();
			paramVal.add(classid);
			
			if(StringUtil.isNotEmpty(param1)){
				strSQL.append(" and t.stu_sex ='"+param1+"'");
			}
			if(StringUtil.isNotEmpty(param2)){
				strSQL.append(" and t.hkxz = '"+param2+"'");
			}
			
			strSQL.append(" order by avg_score asc ) tt where tt.avg_score <>'0'");
			pdao.setSql(strSQL.toString());
			pdao.setBindValues(paramVal);
			Element scoreEl = (Element)pdao.executeQuerySql(0, -1);
			return scoreEl.getChildren("Record");
    	}catch(Exception e){
    		e.printStackTrace();
    		return null;
    	}finally{
    		pdao.releaseConnection();
    	}
    }
    
    
    private void modifyClassids(String stuids,String targetClassid,String classid,String studinfoid){
    	StringBuffer strSQL = new StringBuffer();
    	PlatformDao pdao = new PlatformDao();
    	try {
    		pdao.beginTransaction();
			strSQL.append("update base_studentinfo set classid=? where userid=? ");
			
			ArrayList<Object> paramVals = new ArrayList<Object>();
			paramVals.add(targetClassid);
			paramVals.add(studinfoid);

			pdao.setSql(strSQL.toString());
			pdao.setBindValues(paramVals);
			pdao.executeTransactionSql();
			
			pdao.commitTransaction();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			pdao.releaseConnection();
		}
    }
    
    /**
     * 学生异动信息审核
     */
    public void transactionApprove(){
    	Element reqData = xmlDocUtil.getRequestData();
    	String studinfoids = reqData.getChildTextTrim("studinfoid");//学生编号
    	String transactionids = reqData.getChildTextTrim("transaction_id");//学生异动编号
    	String transactiontypes = reqData.getChildTextTrim("transactionType");//学生异动类型
    	String approveDes = reqData.getChildTextTrim("approve_des");//审核说明
    	String approveStatus = reqData.getChildTextTrim("approve_status");//审核状态
    	PlatformDao pdao = new PlatformDao();
    	
    	try {
    		pdao.beginTransaction();
    		 // 将审核意见写入审核结果表
			Element approveRec = ConfigDocument.createRecordElement("student","base_approve");
			XmlDocPkgUtil.setChildText(approveRec, "valid", "Y");
			XmlDocPkgUtil.setChildText(approveRec, "obj_id", transactionids);
			XmlDocPkgUtil.setChildText(approveRec, "approve_des", approveDes);
			XmlDocPkgUtil.setChildText(approveRec, "approve_status", approveStatus);
			pdao.insertOneRecord(approveRec);
			
			if("true".equals(approveStatus)){
				String strSQL = "update base_studentinfo set stu_status =? where studinfoid = ?";
				ArrayList paramVals = new ArrayList();
				paramVals.add(transactiontypes);
				paramVals.add(studinfoids);
				
				pdao.setSql(strSQL);
				pdao.setBindValues(paramVals);
				pdao.executeTransactionSql();
			}
			pdao.commitTransaction();
			xmlDocUtil.setResult("0");
			xmlDocUtil.writeHintMsg("数据审核完成!");
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			pdao.releaseConnection();
		}
    }
    /**
     * 删除学生基本信息
     */
    public void delStudentInfo(){
    	Element reqData = xmlDocUtil.getRequestData();
    	PlatformDao pdao = new PlatformDao();
    	List list_stu = reqData.getChildren("studinfoids");
    	StringBuffer studentSQL = new StringBuffer();
    	StringBuffer userSQL = new StringBuffer();
    	try {

    		studentSQL.append("update base_studentinfo set valid='N' where userid=?");
    		userSQL.append("update pcmc_user set state ='9' where userid=? ");
    		
			for(int i=0;i<list_stu.size();i++){
				Element userEl = (Element)list_stu.get(i);
				
				ArrayList<Object> paramArray = new ArrayList<Object>();
				paramArray.add(userEl.getText());
				
				pdao.setSql(studentSQL.toString());
				pdao.setBindValues(paramArray);
				pdao.executeTransactionSql();
				
				pdao.setSql(userSQL.toString());
				pdao.setBindValues(paramArray);
				pdao.executeTransactionSql();				
			}
			xmlDocUtil.setResult("0");
			xmlDocUtil.writeHint("删除学生基本信息成功!");
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			pdao.releaseConnection();
		}    	
    }

    /**
     * 修改学生登录密码
     */
    public void uptStudentPwd(){
        Element reqElement = xmlDocUtil.getRequestData();
        String child_id = xmlDocUtil.getSession().getChildTextTrim("userid");//学生编号
        String new_pass = reqElement.getChildText("userpwd");//密码
        ArrayList<Object> values = new ArrayList<Object>();
        new_pass = new Des().strDec(new_pass);
		if(StringUtil.isEmpty(new_pass)){ 
			new_pass ="123456";
		}
        PlatformDao pdao = new PlatformDao();
        try {
        	//将修改密码的用户原密码放入session
        	StringBuffer sql = new StringBuffer();
        	ArrayList<String> bvals = new ArrayList<String>();
            sql.append("select * from pcmc_user where userid=?");
            bvals.add(child_id);
            pdao.setSql(sql.toString());
            pdao.setBindValues(bvals);
            Element resultElement = null;
            resultElement = pdao.executeQuerySql(-1,1);
            String dbuserpwd = Crypto.decodeByKey(Constants.DEFAULT_KEY,resultElement.getChild("Record").getChildTextTrim("userpwd"));
            ApplicationContext.getRequest().getSession().setAttribute("oldpwd", dbuserpwd);
        	
        	values.add(PasswordEncoder.encode(new_pass).toString());
        	values.add(child_id.toString());
        	pdao.beginTransaction();
            String SQL = "update pcmc_user set userpwd=? where userid=?";
            pdao.setSql(SQL);
            pdao.setBindValues(values);
            pdao.executeTransactionSql();
            pdao.commitTransaction();
            ArrayList<Object> pkList = new ArrayList<Object>();
 			pkList.add(new Object[]{"userid",child_id});
 			LogBean.setLogPkInf(reqElement, "pcmc_user", pkList);
            xmlDocUtil.writeHint("密码修改成功！");
            xmlDocUtil.setResult("0");
        }
        catch (Exception ex){
            pdao.rollBack();
            log4j.logError("[修改学生登录密码]:" + ex.getMessage());
            xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
        }
        finally{
            pdao.releaseConnection();
        }
      
    	
    }
}
