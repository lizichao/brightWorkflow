package cn.com.bright.yuexue.report;

import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import jxl.Sheet;

import org.jdom.Element;

import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.db.SqlTypes;
import cn.brightcom.jraf.util.ConfigDocument;
import cn.brightcom.jraf.util.Constants;
import cn.brightcom.jraf.util.Crypto;
import cn.brightcom.jraf.util.DatetimeUtil;
import cn.brightcom.jraf.util.FileUtil;
import cn.brightcom.jraf.util.Log;
import cn.brightcom.jraf.util.XmlDocPkgUtil;
import cn.com.bright.yuexue.base.BaseFamily;
import cn.com.bright.yuexue.base.BaseObject;

/**
 * 学生基本信息导入业务处理类
 * 
 * @author E40
 *
 */
public class ImportStudent extends AbstractExcel {

	private XmlDocPkgUtil xmlDocUtil = null;
	private Element reqElData=null;
	private BaseObject baseObj = new BaseObject();//学生信息
	private BaseFamily baseFamilyOne = new BaseFamily();//家庭成员1
	private BaseFamily baseFamilyTwo = new BaseFamily();//家庭成员2
	private Set<BaseFamily> family = new HashSet<BaseFamily>();
	private PrintWriter logFile = null;
	private int sucCnt = 0;
	private int dupCnt = 0;
	private int errCnt = 0;
	private int singCnt =0;
	private int skipCnt = 0;
	private String upload_by = "",exam_type="";
	private String cur_deptid = "";
	private String classCodes ="";
	//private String curUserIds = xmlDocUtil.getSession().getChildTextTrim("userid");	
	
	@Override
	public void saveExcle(Element reqData) throws Exception {
		try{
			String filenm = reqData.getChildTextTrim("doc_file_url");
			String doc_id = reqData.getChildTextTrim("doc_id");
			cur_deptid = reqData.getChildTextTrim("deptid");
			classCodes = reqData.getChildTextTrim("class_id");
			upload_by = reqData.getChildText("upload_by");
			System.out.println(DatetimeUtil.getNow(null) + ",导入文件" + filenm);
			String logname = filenm + ".log";
			logFile = new PrintWriter(FileUtil.getPhysicalPath(logname));
			sucCnt = dupCnt = errCnt = skipCnt = 0;
			
			Sheet sheet = parseExcel(doc_id, FileUtil.getPhysicalPath(filenm));
			int rows = sheet.getRows();
			if(rows>0){
				parseRow( rows,  sheet);
			}
			StringBuffer info = new StringBuffer().append("成功:").append(
					sucCnt).append(",").append("重复:").append(dupCnt)
					.append(",").append("错误:").append(errCnt).append(",")
					.append("忽略:").append(dupCnt);
			logFile.println("学生基本信息导入"+DatetimeUtil.getNow(null) + ",导入结束....");
			logFile.println(info.toString());
			upFlag(doc_id,info.toString());
			uptTempStudent();
			System.out.println(info.toString());
		}catch (Exception ex) {
			try {
				if (null != logFile) {
					ex.printStackTrace(logFile);
				}
			} catch (Exception e) {

			}
			ex.printStackTrace();
			log4j.logError(ex);
		} finally {
			if (null != logFile) {
				try {
					logFile.flush();
					logFile.close();
				} catch (Exception ex) {

				}
				logFile = null;
			}
		}
	}
	
	
	public void parseRow(int rows, Sheet sheet) throws Exception {
		String value = "",dbColName="",xlsColName="",extype="",conversion="",isnull="";
		PlatformDao pdao = new PlatformDao();
		List excelList = getExlElement("student");
		if (excelList.size()<=0) return;
		try {
			a:for (int i = 1; i < rows; i++) {
				Element rec = new Element("RequestData");
				b:for(int j=0;j<excelList.size();j++){  
					 Element ele = (Element)excelList.get(j);
					 dbColName = ele.getName();
					 xlsColName =  ele.getText();
					 extype = ele.getAttributeValue("extype");
					 conversion = ele.getAttributeValue("conversion");
					 isnull = ele.getAttributeValue("isnull");
					 value = sheet.getCell(sheet.findCell(xlsColName).getColumn(), i).getContents();
					if ("N".equals(isnull)) {
						 if(sheet.findCell(xlsColName) == null||"".equals(value)) 
						continue a;
					}
					if (sheet.findCell(xlsColName) != null) {
						if("date".equals(extype)){
							value = sheet.getCell(sheet.findCell(xlsColName).getColumn(), i).getContents();
							value=formatDatetTime(value).toString();
						}
						if (!"N".equals(conversion)) {
							value=AbstractExcel.convesionStr(conversion,value);
						}
						if("exam_type".equals(dbColName)) exam_type=value;
						rec.addContent(new Element(dbColName).setText(value));
					} else {
						continue a;
					}
				} 
				saveTempStudent(rec);
				sucCnt++;
			}
			StringBuffer checkSQL = new StringBuffer();
			checkSQL.append("SELECT t.* FROM temp_base_student t ");
			checkSQL.append("WHERE t.valid='Y' and NOT EXISTS  ");
			checkSQL.append("(SELECT NULL FROM pcmc_user WHERE idnumber = t.sfzjh)");
			pdao.setSql(checkSQL.toString());
			Element tempElement = (Element)pdao.executeQuerySql(0, -1);
			List list= tempElement.getChildren("Record");
			if(list.size()>0){
				for(int k=0;k<list.size();k++){
					saveStudentInfo((Element)list.get(k));
					singCnt++;
				}
			}
			dupCnt = sucCnt-singCnt;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	public static Object formatDatetTime(String dstr)
	{
		try
		{
			if(dstr.length()== 8){
				dstr = dstr.substring(0,4)+"-"+dstr.substring(4,6)+"-"+dstr.substring(6,8);
			}else{
				dstr = dstr.replaceAll("/", "-").replaceAll("—", "-").replaceAll("。", "-").replaceAll("、", "-").replaceAll("年", "-").replaceAll("月", "-").replaceAll("日", "-").replaceAll("-","-");
				dstr= dstr.replace(".", "-");
			}
			System.out.println(dstr);
			return SqlTypes.getConvertor("Timestamp").convert(dstr,"yyyy-MM-dd");
		}
		catch(Exception ex)
		{
		   ex.printStackTrace();	
		}
		return null;
	}
	
	/**
	 * 写入临时学生数据库表
	 * @param reqcodeclasData
	 */
	private void saveTempStudent(Element reqcodeclasData){
		Element objEl = reqcodeclasData;
		PlatformDao pdao = new PlatformDao();
		try {
			pdao.beginTransaction();
			Element tempRecord = ConfigDocument.createRecordElement("student", "temp_base_student");
			XmlDocPkgUtil.copyValues(objEl, tempRecord, 0 , true);
			pdao.insertOneRecord(tempRecord);
			pdao.commitTransaction();
		} catch (Exception e) {
			pdao.rollBack();
			e.printStackTrace();
		}finally{
			pdao.releaseConnection();
		}
	}
	
	/**
	 * 修改临时学生数据库表有效标识
	 * @param reqcodeclasData
	 */
	private void uptTempStudent(){
		PlatformDao pdao = new PlatformDao();
		try {
			String strSQL ="update temp_base_student set valid='N' where valid='Y'";
			pdao.setSql(strSQL);
			pdao.executeTransactionSql();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	 public void saveStudentInfo(Element reqcodeclasData){
		Element objEl = reqcodeclasData;
		PlatformDao pdao = new PlatformDao();
		try {
			    pdao.beginTransaction();
			    baseObj.setUserName(objEl.getChildTextTrim("xm"));//姓名
			    baseObj.setUserGender(objEl.getChildTextTrim("xb"));//性别
			    baseObj.setUserBirth(objEl.getChildTextTrim("csrq"));//出生日期
			    baseObj.setAreacode(objEl.getChildTextTrim("csdxzq"));//出生地行政区划代码
			    baseObj.setOrigin(objEl.getChildTextTrim("jg"));//籍贯
			    baseObj.setNid(objEl.getChildTextTrim("mz"));//民族
			    baseObj.setCid(objEl.getChildTextTrim("gj"));//国籍
				baseObj.setCtid(objEl.getChildTextTrim("sfzjlx"));//身份证件类型
				baseObj.setOid(objEl.getChildTextTrim("gatqw"));//港澳台侨外
				baseObj.setHealth(objEl.getChildTextTrim("jkzk"));//健康状况
				baseObj.setPsid(objEl.getChildTextTrim("zzmm"));//政治面貌
				baseObj.setCno(objEl.getChildTextTrim("sfzjh").toUpperCase());//证件号码
				baseObj.setHkxz(objEl.getChildTextTrim("hkxz"));//户口性质
				baseObj.setHouseaid(objEl.getChildTextTrim("hkszdxzqh"));//户口所在地行政区划
				baseObj.setClass_no(objEl.getChildTextTrim("bh"));//班号
				baseObj.setFirstyear(objEl.getChildTextTrim("rxny"));//入学年份
				baseObj.setCwid(objEl.getChildTextTrim("rxfs"));//入学方式
			    baseObj.setStudyway(objEl.getChildTextTrim("jdfs"));//就读方式
			    baseObj.setAddressnow(objEl.getChildTextTrim("xzz"));//现住地址
			    baseObj.setMailaddress(objEl.getChildTextTrim("txdz"));//通信地址
			    baseObj.setHouseaddress(objEl.getChildTextTrim("jtdz"));//家庭地址
			    baseObj.setTelephone(objEl.getChildTextTrim("lxdh"));//联系电话
			    baseObj.setPostcode(objEl.getChildTextTrim("yzbm"));//邮政编码
			    baseObj.setSingleflag(objEl.getChildTextTrim("sfdszn"));//是否独生子女
			    baseObj.setPreflag(objEl.getChildTextTrim("sfsgxqjy"));//是否受过学前教育
			    baseObj.setStayflag(objEl.getChildTextTrim("sflset"));//是否留守儿童
			    baseObj.setHelpneed(objEl.getChildTextTrim("sfxysqzz"));//是否需要申请资助
			    baseObj.setHelpflag(objEl.getChildTextTrim("sfxsyb"));//是否享受一补
			    baseObj.setOrphanflag(objEl.getChildTextTrim("sfge"));//是否孤儿
			    baseObj.setMartyr(objEl.getChildTextTrim("sfls"));//是否烈士
			    baseObj.setDtance(objEl.getChildTextTrim("sxxjl"));//上下学距离
			    baseObj.setGoway(objEl.getChildTextTrim("sxxfs"));//上下学方式
			    baseObj.setCarflag(objEl.getChildTextTrim("sfxyczxc"));//是否乘坐校车
			    baseObj.setUserOldName(objEl.getChildTextTrim("zym"));//曾用名
			    baseObj.setEffectdate(objEl.getChildTextTrim("sfzyxq"));//身份证有效期
			    baseObj.setBid(objEl.getChildTextTrim("xx"));//血型
			    baseObj.setSpecialty(objEl.getChildTextTrim("tc"));//特长
			    baseObj.setRollid(objEl.getChildTextTrim("xjfh"));//学籍辅号
			    baseObj.setStu_no(objEl.getChildTextTrim("bnxh"));//班内学号
			    baseObj.setSource(objEl.getChildTextTrim("xsly"));//学生来源
			    baseObj.setUserEmail(objEl.getChildTextTrim("dzxx"));//电子信箱
			    baseObj.setHomepage(objEl.getChildTextTrim("zydz"));//主页地址
			    baseObj.setDid(objEl.getChildTextTrim("cjlx"));//残疾类型
			    baseObj.setBuydegree(objEl.getChildTextTrim("sfyzfgmxw"));//是否由政府购买学位
			    baseObj.setAttendant(objEl.getChildTextTrim("sbjd"));//随班就读
			    //成员信息（成员一）
			    baseFamilyOne.setName_one(objEl.getChildTextTrim("cy1xm"));//成员1姓名
			    baseFamilyOne.setNuxes_one(objEl.getChildTextTrim("cy1gx"));//成员1关系
			    baseFamilyOne.setRelation_one(objEl.getChildTextTrim("cy1gxsm"));//成员1关系说明
			    baseFamilyOne.setAddress_one(objEl.getChildTextTrim("cy1xzz"));//成员1现住址
			    baseFamilyOne.setHouseaid_one(objEl.getChildTextTrim("cy1hkszd"));//成员1户口所在地行政区划
			    baseFamilyOne.setTelephone_one(objEl.getChildTextTrim("cy1lx"));//成员1联系电话
			    baseFamilyOne.setTutor_one(objEl.getChildTextTrim("cy1sfjhr"));//成员1是否监护人
			    baseFamilyOne.setCtid_one(objEl.getChildTextTrim("cy1sfzjlx"));//成员1身份证件类型
			    baseFamilyOne.setCno_one(objEl.getChildTextTrim("cy1sfzh"));//成员1身份证件号
			    baseFamilyOne.setNid_one(objEl.getChildTextTrim("cy1mz"));//成员1民族
			    baseFamilyOne.setUnit_one(objEl.getChildTextTrim("cy1gzdw"));//成员1工作单位
			    baseFamilyOne.setDuty_one(objEl.getChildTextTrim("cy1zw"));//成员1职务
			    family.add(baseFamilyOne);
			    //成员信息（成员二）
			    baseFamilyTwo.setName_one(objEl.getChildTextTrim("cy2xm"));//成员2姓名
			    baseFamilyTwo.setNuxes_one(objEl.getChildTextTrim("cy2gx"));//成员2关系
			    baseFamilyTwo.setRelation_one(objEl.getChildTextTrim("cy2gxsm"));//成员2关系说明
			    baseFamilyTwo.setAddress_one(objEl.getChildTextTrim("cy2xzz"));//成员2现住址
			    baseFamilyTwo.setHouseaid_one(objEl.getChildTextTrim("cy2hkszd"));//成员2户口所在地行政区划
			    baseFamilyTwo.setTelephone_one(objEl.getChildTextTrim("cy2lx"));//成员2联系电话
			    baseFamilyTwo.setTutor_one(objEl.getChildTextTrim("cy2sfjhr"));//成员2是否监护人
			    baseFamilyTwo.setCtid_one(objEl.getChildTextTrim("cy2sfzjlx"));//成员2身份证件类型
			    baseFamilyTwo.setCno_one(objEl.getChildTextTrim("cy2sfzh"));//成员2身份证件号
			    baseFamilyTwo.setNid_one(objEl.getChildTextTrim("cy2mz"));//成员2民族
			    baseFamilyTwo.setUnit_one(objEl.getChildTextTrim("cy2gzdw"));//成员2工作单位
			    baseFamilyTwo.setDuty_one(objEl.getChildTextTrim("cy2zw"));//成员2职务
			    family.add(baseFamilyTwo);
			    baseObj.setBaseFamily(family);
			    //增加用户至系统
			    Element userRecord = ConfigDocument.createRecordElement("pcmc", "pcmc_user");
				XmlDocPkgUtil.setChildText(userRecord, "username", baseObj.getUserName());
				XmlDocPkgUtil.setChildText(userRecord, "usercode", baseObj.getCno());
				XmlDocPkgUtil.setChildText(userRecord, "userpwd", baseObj.getUserPwd());
				XmlDocPkgUtil.setChildText(userRecord, "idnumber", baseObj.getCno());
				XmlDocPkgUtil.setChildText(userRecord, "phone", baseObj.getTelephone());
				XmlDocPkgUtil.setChildText(userRecord, "email", baseObj.getUserEmail());
				XmlDocPkgUtil.setChildText(userRecord, "mobile", baseObj.getTelephone());
				XmlDocPkgUtil.setChildText(userRecord, "gender", baseObj.getUserGender());
				XmlDocPkgUtil.setChildText(userRecord, "usertype", "1");
				XmlDocPkgUtil.setChildText(userRecord, "usersource", "1");
				XmlDocPkgUtil.setChildText(userRecord, "modifydt", DatetimeUtil.getNow(""));
				XmlDocPkgUtil.setChildText(userRecord, "state", "1");
			    String pk_userid = (String)pdao.insertOneRecordSeqPk(userRecord);
			    
			    //用户机构关系表
			    Element deptRecord = ConfigDocument.createRecordElement("pcmc", "pcmc_user_dept");
			    XmlDocPkgUtil.setChildText(deptRecord, "deptid", cur_deptid);
			    XmlDocPkgUtil.setChildText(deptRecord, "userid", pk_userid);
			    XmlDocPkgUtil.setChildText(deptRecord, "state", "1");
			    XmlDocPkgUtil.setChildText(deptRecord, "indate", DatetimeUtil.getNow(""));
			    pdao.insertOneRecord(deptRecord);
			    
			    //系统用户扩展信息
			    Element userExtRecord = ConfigDocument.createRecordElement("pcmc", "pcmc_user_ext");
			    XmlDocPkgUtil.setChildText(userExtRecord, "userid", pk_userid);
			    XmlDocPkgUtil.setChildText(userExtRecord, "birthday", baseObj.getUserBirth());
			    XmlDocPkgUtil.setChildText(userExtRecord, "createuser", upload_by);
			    XmlDocPkgUtil.setChildText(userExtRecord, "pubname", "0");
			    XmlDocPkgUtil.setChildText(userExtRecord, "pubmail","0");
			    XmlDocPkgUtil.setChildText(userExtRecord, "pubphone", "0");
			    pdao.insertOneRecord(userExtRecord);
			    
			    //学生基本信息
			    Element stuentRecord = ConfigDocument.createRecordElement("yuexue", "base_studentinfo");
			    XmlDocPkgUtil.setChildText(stuentRecord, "userid", pk_userid);
			    XmlDocPkgUtil.setChildText(stuentRecord, "deptid", cur_deptid);
			    XmlDocPkgUtil.setChildText(stuentRecord, "classid", classCodes);
			    XmlDocPkgUtil.setChildText(stuentRecord, "studentno", baseObj.getStu_no());
			    XmlDocPkgUtil.setChildText(stuentRecord, "state", "1");
			    XmlDocPkgUtil.setChildText(stuentRecord, "oldname", baseObj.getUserOldName());
			    XmlDocPkgUtil.setChildText(stuentRecord, "origin",baseObj.getOrigin());
			    XmlDocPkgUtil.setChildText(stuentRecord, "helpflag", baseObj.getHelpflag());
			    XmlDocPkgUtil.setChildText(stuentRecord, "ctid", baseObj.getCtid());
			    XmlDocPkgUtil.setChildText(stuentRecord, "health", baseObj.getHealth());
			    XmlDocPkgUtil.setChildText(stuentRecord, "cid", baseObj.getCid());
			    XmlDocPkgUtil.setChildText(stuentRecord, "psid", baseObj.getPsid());
			    XmlDocPkgUtil.setChildText(stuentRecord, "oid", baseObj.getOid());
			    XmlDocPkgUtil.setChildText(stuentRecord, "hkxz", baseObj.getHkxz());
			    XmlDocPkgUtil.setChildText(stuentRecord, "stbirth", baseObj.getUserBirth());
			    XmlDocPkgUtil.setChildText(stuentRecord, "areacode", baseObj.getAreacode());
			    XmlDocPkgUtil.setChildText(stuentRecord, "csd", baseObj.getCsd());
			    XmlDocPkgUtil.setChildText(stuentRecord, "bid", baseObj.getBid());
			    XmlDocPkgUtil.setChildText(stuentRecord, "studyway", baseObj.getStudyway());
			    XmlDocPkgUtil.setChildText(stuentRecord, "mailaddress", baseObj.getMailaddress());
			    XmlDocPkgUtil.setChildText(stuentRecord, "houseaddress", baseObj.getHouseaddress());
			    XmlDocPkgUtil.setChildText(stuentRecord, "telephone", baseObj.getTelephone());
			    XmlDocPkgUtil.setChildText(stuentRecord, "postcode", baseObj.getPostcode());
			    XmlDocPkgUtil.setChildText(stuentRecord, "singleflag", baseObj.getSingleflag());
			    XmlDocPkgUtil.setChildText(stuentRecord, "preflag", baseObj.getPreflag());
			    XmlDocPkgUtil.setChildText(stuentRecord, "stayflag", baseObj.getStayflag());
			    XmlDocPkgUtil.setChildText(stuentRecord, "orphanflag", baseObj.getOrphanflag());
			    XmlDocPkgUtil.setChildText(stuentRecord, "martyr", baseObj.getMartyr());
			    XmlDocPkgUtil.setChildText(stuentRecord, "goway", baseObj.getGoway());
			    XmlDocPkgUtil.setChildText(stuentRecord, "carflag", baseObj.getCarflag());
			    XmlDocPkgUtil.setChildText(stuentRecord, "effectdate", baseObj.getEffectdate());
			    XmlDocPkgUtil.setChildText(stuentRecord, "rollid", baseObj.getRollid());
			    XmlDocPkgUtil.setChildText(stuentRecord, "attendant", baseObj.getAttendant());
			    XmlDocPkgUtil.setChildText(stuentRecord, "farmer", baseObj.getFarmer());
			    XmlDocPkgUtil.setChildText(stuentRecord, "houseaid", baseObj.getHouseaid());
			    XmlDocPkgUtil.setChildText(stuentRecord, "did", baseObj.getDid());
			    XmlDocPkgUtil.setChildText(stuentRecord, "cwid", baseObj.getCwid());
			    XmlDocPkgUtil.setChildText(stuentRecord, "hard", baseObj.getHard());
			    XmlDocPkgUtil.setChildText(stuentRecord, "addressnow", baseObj.getAddressnow());
			    XmlDocPkgUtil.setChildText(stuentRecord, "helpneed", baseObj.getHelpneed());
			    XmlDocPkgUtil.setChildText(stuentRecord, "dtance", baseObj.getDtance());
			    XmlDocPkgUtil.setChildText(stuentRecord, "specialty", baseObj.getSpecialty());
			    XmlDocPkgUtil.setChildText(stuentRecord, "homepage", baseObj.getHomepage());
			    XmlDocPkgUtil.setChildText(stuentRecord, "soldierflag", baseObj.getSoldierflag());
			    XmlDocPkgUtil.setChildText(stuentRecord, "buydegree", baseObj.getBuydegree());
			    XmlDocPkgUtil.setChildText(stuentRecord, "source", baseObj.getSource());
			    XmlDocPkgUtil.setChildText(stuentRecord, "first_yearmoth", baseObj.getFirstyear());
			    XmlDocPkgUtil.setChildText(stuentRecord, "valid", "Y");
			    XmlDocPkgUtil.setChildText(stuentRecord, "stu_name", baseObj.getUserName());
				XmlDocPkgUtil.setChildText(stuentRecord, "stu_sex", baseObj.getUserGender());
				XmlDocPkgUtil.setChildText(stuentRecord, "stu_idnumber",baseObj.getCno());
				XmlDocPkgUtil.setChildText(stuentRecord, "stu_email", baseObj.getUserEmail());
				XmlDocPkgUtil.setChildText(stuentRecord, "stu_mobile", baseObj.getTelephone());
				XmlDocPkgUtil.setChildText(stuentRecord, "stbirth", baseObj.getUserBirth());
				XmlDocPkgUtil.setChildText(stuentRecord, "telephone",baseObj.getTelephone());
			    String studinfoIds = (String)pdao.insertOneRecordSeqPk(stuentRecord);
			    
			    //家庭成员信息
			    Set<BaseFamily> familyItem =  baseObj.getBaseFamily();
			    if(!familyItem.isEmpty()){
				     Iterator<BaseFamily> familyIterator = familyItem.iterator();
				     Element familyRecord = ConfigDocument.createRecordElement("yuexue", "base_familyinfo");
				     while(familyIterator.hasNext()){
				    	 BaseFamily familyObj = familyIterator.next();
				    	 XmlDocPkgUtil.setChildText(familyRecord, "userid", studinfoIds);
				    	 XmlDocPkgUtil.setChildText(familyRecord, "name", familyObj.getName_one());
				    	 XmlDocPkgUtil.setChildText(familyRecord, "nid",familyObj.getNid_one());
				    	 XmlDocPkgUtil.setChildText(familyRecord, "nuxes", familyObj.getNuxes_one());
				    	 XmlDocPkgUtil.setChildText(familyRecord, "relation", familyObj.getRelation_one());
				    	 XmlDocPkgUtil.setChildText(familyRecord, "ctid", familyObj.getCtid_one());
				    	 XmlDocPkgUtil.setChildText(familyRecord, "cno", familyObj.getCno_one());
				    	 XmlDocPkgUtil.setChildText(familyRecord, "unit", familyObj.getUnit_one());
				    	 XmlDocPkgUtil.setChildText(familyRecord, "duty",familyObj.getDuty_one());
				    	 XmlDocPkgUtil.setChildText(familyRecord, "address", familyObj.getAddress_one());
				    	 XmlDocPkgUtil.setChildText(familyRecord, "houseaid", familyObj.getHouseaid_one());
				    	 XmlDocPkgUtil.setChildText(familyRecord, "telephone", familyObj.getTelephone_one());
				    	 XmlDocPkgUtil.setChildText(familyRecord, "tutor", familyObj.getTutor_one());
				    	 XmlDocPkgUtil.setChildText(familyRecord, "valid", "Y");
				    	 pdao.insertOneRecord(familyRecord);
				     }
			    }
			    pdao.commitTransaction();
			} catch (Exception e) {
				pdao.rollBack();
				e.printStackTrace();
		        xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
		        Log log4j = new Log(this.getClass().toString());
		        log4j.logError("[参数设置-增加导入学生信息.]"+e.getMessage());
			} finally {
				pdao.releaseConnection();
			}
	}
}
