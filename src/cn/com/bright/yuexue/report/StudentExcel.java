package cn.com.bright.yuexue.report;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.jdom.Element;

import sun.awt.geom.AreaOp.NZWindOp;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;

import cn.brightcom.jraf.context.ApplicationContext;
import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.id.UUIDHexGenerator;
import cn.brightcom.jraf.util.DatetimeUtil;
import cn.brightcom.jraf.util.FileUtil;
import cn.brightcom.jraf.util.Log;
import cn.brightcom.jraf.util.StringUtil;
import cn.com.bright.yuexue.base.BaseFamily;
import cn.com.bright.yuexue.base.BaseObject;

/**
 * 学生Excel模板数据处理
 * @author E40
 *
 */
public class StudentExcel {
	private ImportFile imporFile = new ImportFile();
	protected Workbook rwb = null;//Excel工作薄
	protected Cell cell = null;//Excel列
	protected Sheet sheet = null;
	WorkbookSettings wbs = null ;
	protected InputStream stream =null;
	private BaseObject baseObj = new BaseObject();
	private BaseFamily baseFamilyOne = new BaseFamily();//家庭成员1
	private BaseFamily baseFamilyTwo = new BaseFamily();//家庭成员2
	protected String cur_user = "";//当前用户
	protected String cur_deptid = "";//当前组织机构编号
	protected String file_id = "";//文件文号
	protected String file_name="";//文件名称
	protected String file_url = "";//文件存储地址
	protected String file_flag ="";//文件标识
	private Set<BaseFamily> familySet = new HashSet<BaseFamily>();
	
	private Log log4j = new Log(this.getClass().toString());
	
	
	/**
	 * 根据代码描述获得代码标识码
	 * @param val
	 * @return
	 * @throws SQLException 
	 */
	  public HashMap getParamMap (String val) throws SQLException {
          String code = "";
          StringBuffer strSQL = new StringBuffer();
          PlatformDao pdao = new PlatformDao();
          try{
	          HashMap texMap = new HashMap();
	          strSQL.append("SELECT pd.PARAMMEANINGS,pd.PARAMCODE FROM param_detail pd, param_master pm WHERE pd.PARAMID = pm.PARAMID AND pm.paramname ='"+val+"'");
	          pdao.setSql(strSQL.toString());
	          Element rsData = pdao.executeQuerySql(-1, 0);
	          List ls = rsData.getChildren("Record");
	          for (int i=0;i<ls.size();i++){
	              Element rec = (Element)ls.get(i);
	              texMap.put(rec.getChildTextTrim("parammeanings"), rec.getChildTextTrim("paramcode"));                  
	          }
	          return texMap;
          }
          finally{
        	  pdao.releaseConnection();  
          }
      }
	  
	/**
	 * 获得文件存储路径
	 * @return
	 * @throws SQLException
	 */
	public void getFileURL()throws SQLException{
		PlatformDao pdao = new PlatformDao();
		StringBuffer fileSQL = new StringBuffer();
		
		//获得将要解析的文件
		fileSQL.append("select * from base_doc where doc_status='N' ORDER BY RAND() LIMIT 1 ");
		pdao.setSql(fileSQL.toString());
		Element elDocFile = pdao.executeQuerySql(0, 1);
		List lsFile = elDocFile.getChildren("Record");
		if(lsFile.size()>0){
			Element doc = (Element)lsFile.get(0);
			file_id = doc.getChildTextTrim("doc_id");
			baseObj.setUserClass(doc.getChildTextTrim("class_id"));
			cur_user = doc.getChildTextTrim("upload_by");
			file_url = doc.getChildTextTrim("doc_file_url");
			cur_deptid = doc.getChildTextTrim("deptid");
			file_flag = doc.getChildTextTrim("doc_flag");
		}
	}
	/**
	 * 解析Excel学生数据模板
	 */
	public void importStudent(){
		ApplicationContext.regSubSys("yuexue"); 
		
		PlatformDao pdao  = new PlatformDao();
		try{
			this.getFileURL();
			File file = new File(FileUtil.getWebPath() + file_url);
			
			if(StringUtil.isNotEmpty(file_url)){
				//证件类型
				HashMap zjlxMap = this.getParamMap("ZJLXM");
				//性别
				HashMap genderMap = this.getParamMap("gender");
				//民族
				HashMap mzMap = this.getParamMap("MZM");
				//国籍
				HashMap gjMap = this.getParamMap("GJM");
				//港澳台侨外
				HashMap gatqwMap = this.getParamMap("GATQWM");
				//健康状况
				HashMap healthMap = this.getParamMap("JKZKM");
				//政治面貌
				HashMap zzmmMap = this.getParamMap("ZZMM");
				//户口性质
				HashMap hkxzMap = this.getParamMap("HKXZM");
				//入学年份
				HashMap yearMap = this.getParamMap("global_year");
				//入学方式
				HashMap rxfsMap = this.getParamMap("RXFSM");
				//就读方式
				HashMap jdfsMap = this.getParamMap("JDFSM");
				//是与否
				HashMap booleanMap = this.getParamMap("global_boolean");
				//是否留守儿童
				HashMap lsetMap = this.getParamMap("SFLSET");
				//上下学方式
				HashMap sxfsMap = this.getParamMap("SXXFS");
				//血型
				HashMap xxMap = this.getParamMap("XXM");
				//学生来源
				HashMap sourceMap = this.getParamMap("XSLYM");
				//残疾类型
				HashMap cjlxMap = this.getParamMap("CJLXM");
				//随班就读
				HashMap sbjdMap = this.getParamMap("SBJDM");
				//关系
				HashMap relationMap = this.getParamMap("relation");
				
				stream = new FileInputStream(FileUtil.getWebPath() + file_url);
				wbs = new WorkbookSettings();
				
				wbs.setLocale(new Locale("zh", "CN"));
				wbs.setEncoding("ISO-8859-1");
					//获取Excel文件对象
				rwb = Workbook.getWorkbook(stream,wbs);
				Set<BaseFamily> family = new HashSet<BaseFamily>();
				//获取文件的指定工作表 默认的第一个
				sheet = rwb.getSheet(0);
				//写入到用户临时数据库表
				String strSQL = "insert into temp_base_user (userid,usercode,username,studentno,userpwd,idnumber,email,phone,usertype,usersource,state,gender) " +
		 		" values (?,?,?,?,?,?,?,?,?,?,?,?)";
				
				String stuSQL = "INSERT INTO temp_base_student_attr(studinfoid,origin,ctid,health,cid,psid,nid,oid,hkxz,stbirth,areacode,bid,studyway,mailaddress,houseaddress,telephone,postcode,singleflag,preflag,stayflag,helpflag,orphanflag,martyr,goway,carflag,effectdate,rollid,attendant,farmer,houseaid,did,cwid,helpneed,dtance,specialty,homepage,buydegree,soldierflag,userid,source)	" +
						"VALUES	(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				
				String familySQL = "INSERT INTO base_familyinfo(family_id,userid,name,nid,nuxes,relation,ctid,cno,unit,duty,address,houseaid,telephone,tutor)	" +
						"VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				
				for(int i=2;i<sheet.getRows();i++){
					//登录帐号
					 if (sheet.findCell("KD_F00")!=null){
						 baseObj.setLoginCode(sheet.getCell(sheet.findCell("KD_F00").getColumn(), i).getContents());
					 }
					 //姓名
					 if(sheet.findCell("KD_F02")!=null){
						 baseObj.setUserName(sheet.getCell(sheet.findCell("KD_F02").getColumn(), i).getContents());
					 }
					 //性别
					 if(sheet.findCell("KD_F03")!=null){
						 baseObj.setUserGender((String)genderMap.get(sheet.getCell(sheet.findCell("KD_F03").getColumn(), i).getContents()));
					 }
					 //出生日期
					 if(sheet.findCell("KD_F04")!=null){
						 baseObj.setUserBirth(sheet.getCell(sheet.findCell("KD_F04").getColumn(), i).getContents());
					 }
					 //出生地行政区域
					 if(sheet.findCell("KD_F05")!=null){
						 baseObj.setAreacode(sheet.getCell(sheet.findCell("KD_F05").getColumn(), i).getContents());
					 }
					 //籍贯
					 if(sheet.findCell("KD_F06")!=null){
						 baseObj.setOrigin(sheet.getCell(sheet.findCell("KD_F06").getColumn(), i).getContents());
					 }
					 //民族
					 if(sheet.findCell("KD_F07")!=null){
						 baseObj.setNid((String)mzMap.get(sheet.getCell(sheet.findCell("KD_F07").getColumn(), i).getContents()));
					 }
					//国籍、地区
					 if(sheet.findCell("KD_F08")!=null){
						 baseObj.setCid((String)gjMap.get(sheet.getCell(sheet.findCell("KD_F08").getColumn(), i).getContents()));
					 }
					 //身份证件类型
					 if(sheet.findCell("KD_F09")!=null){
						 baseObj.setCtid((String)zjlxMap.get(sheet.getCell(sheet.findCell("KD_F09").getColumn(), i).getContents()));
					 }
					 //港澳台侨外
					 if(sheet.findCell("KD_F10")!=null){
						 baseObj.setOid((String)gatqwMap.get(sheet.getCell(sheet.findCell("KD_F10").getColumn(), i).getContents()));
					 }
					 //健康状况
					 if(sheet.findCell("KD_F11")!=null){
						 baseObj.setHealth((String)healthMap.get(sheet.getCell(sheet.findCell("KD_F11").getColumn(), i).getContents()));
					 }
					 //政治面貌
					 if(sheet.findCell("KD_F12")!=null){
						 baseObj.setPsid((String)zzmmMap.get(sheet.getCell(sheet.findCell("KD_F12").getColumn(), i).getContents()));
					 }
					 //身份证件号
					 if(sheet.findCell("KD_F13")!=null){
						 baseObj.setCno(sheet.getCell(sheet.findCell("KD_F13").getColumn(), i).getContents());
					 }
					 //户口性质
					 if(sheet.findCell("KD_F14")!=null){
						 baseObj.setHkxz((String)hkxzMap.get(sheet.getCell(sheet.findCell("KD_F14").getColumn(), i).getContents()));
					 }
					 //户口所在地行政区域
					 if(sheet.findCell("KD_F15")!=null){
						 baseObj.setHouseaid(sheet.getCell(sheet.findCell("KD_F15").getColumn(), i).getContents());
					 }
					 //班号（学号）
					 if(sheet.findCell("KD_F16")!=null){
						 baseObj.setStu_no(sheet.getCell(sheet.findCell("KD_F16").getColumn(), i).getContents());
					 }
					 //入学年份
					 if(sheet.findCell("KD_F17")!=null){
						 baseObj.setFirstyear((String)yearMap.get(sheet.getCell(sheet.findCell("KD_F17").getColumn(), i).getContents()));
					 }
					 //入学方式
					 if(sheet.findCell("KD_F18")!=null){
						 baseObj.setCwid((String)rxfsMap.get(sheet.getCell(sheet.findCell("KD_F18").getColumn(), i).getContents()));
					 }
					 //就读方式
					 if(sheet.findCell("KD_F19")!=null){
						 baseObj.setStudyway((String)jdfsMap.get(sheet.getCell(sheet.findCell("KD_F19").getColumn(), i).getContents()));
					 }
					 //现住地址
					 if(sheet.findCell("KD_F20")!=null){
						 baseObj.setAddressnow(sheet.getCell(sheet.findCell("KD_F20").getColumn(), i).getContents());
					 }
					 //通信地址
					 if(sheet.findCell("KD_F21")!=null){
						 baseObj.setMailaddress(sheet.getCell(sheet.findCell("KD_F21").getColumn(), i).getContents());
					 }
					 //家庭地址
					 if(sheet.findCell("KD_F22")!=null){
						 baseObj.setHouseaddress(sheet.getCell(sheet.findCell("KD_F22").getColumn(), i).getContents());
					 } 
					 //联系电话
					 if(sheet.findCell("KD_F23")!=null){
						 baseObj.setTelephone(sheet.getCell(sheet.findCell("KD_F23").getColumn(), i).getContents());
					 }
					 //邮政编码
					 if(sheet.findCell("KD_F24")!=null){
						 baseObj.setPostcode(sheet.getCell(sheet.findCell("KD_F24").getColumn(), i).getContents());
					 }
					 //是否独生子女
					 if(sheet.findCell("KD_F25")!=null){
						 baseObj.setSingleflag((String)booleanMap.get(sheet.getCell(sheet.findCell("KD_F25").getColumn(), i).getContents()));
					 }
					 //是否受过学前教育
					 if(sheet.findCell("KD_F26")!=null){
						 baseObj.setPreflag((String)booleanMap.get(sheet.getCell(sheet.findCell("KD_F26").getColumn(), i).getContents()));
					 }
					 //是否留守儿童
					 if(sheet.findCell("KD_F27")!=null){
						 baseObj.setStayflag((String)lsetMap.get(sheet.getCell(sheet.findCell("KD_F27").getColumn(), i).getContents()));
					 }
					 //是否需要申请资助
					 if(sheet.findCell("KD_F28")!=null){
						 baseObj.setHelpneed((String)booleanMap.get(sheet.getCell(sheet.findCell("KD_F28").getColumn(), i).getContents()));
					 }
					//是否享受一补
					 if(sheet.findCell("KD_F29")!=null){
						 baseObj.setHelpflag((String)booleanMap.get(sheet.getCell(sheet.findCell("KD_F29").getColumn(), i).getContents()));
					 }
					 //是否孤儿
					 if(sheet.findCell("KD_F30")!=null){
						 baseObj.setOrphanflag((String)booleanMap.get(sheet.getCell(sheet.findCell("KD_F30").getColumn(), i).getContents()));
					 }
					 //是否烈士或扶优子女
					 if(sheet.findCell("KD_F31")!=null){
						 baseObj.setMartyr((String)booleanMap.get(sheet.getCell(sheet.findCell("KD_F31").getColumn(), i).getContents()));
					 }
					 //上下学距离
					 if(sheet.findCell("KD_F32")!=null){
						 baseObj.setDtance(sheet.getCell(sheet.findCell("KD_F32").getColumn(), i).getContents());
					 }
					//上下学方式
					 if(sheet.findCell("KD_F33")!=null){
						 baseObj.setGoway((String)sxfsMap.get(sheet.getCell(sheet.findCell("KD_F33").getColumn(), i).getContents()));
					 }
					 //是否需要乘坐校车
					 if(sheet.findCell("KD_F34")!=null){
						 baseObj.setCarflag((String)booleanMap.get(sheet.getCell(sheet.findCell("KD_F34").getColumn(), i).getContents()));
					 }
					 //曾用名
					 if(sheet.findCell("KD_F35")!=null){
						 baseObj.setUserOldName(sheet.getCell(sheet.findCell("KD_F35").getColumn(), i).getContents());
					 }
					 //身份证件有效期
					 if(sheet.findCell("KD_F36")!=null){
						 baseObj.setEffectdate(sheet.getCell(sheet.findCell("KD_F36").getColumn(), i).getContents());
					 }
					//血型
					 if(sheet.findCell("KD_F37")!=null){
						 baseObj.setBid((String)xxMap.get(sheet.getCell(sheet.findCell("KD_F37").getColumn(), i).getContents()));
					 }
					 //特长
					 if(sheet.findCell("KD_F38")!=null){
						 baseObj.setSpecialty(sheet.getCell(sheet.findCell("KD_F38").getColumn(), i).getContents());
					 }
					 //学习辅号
					 if(sheet.findCell("KD_F39")!=null){
						 baseObj.setRollid(sheet.getCell(sheet.findCell("KD_F39").getColumn(), i).getContents());
					 }
					 //班内学号
					 if(sheet.findCell("KD_F40")!=null){
						 baseObj.setStu_no(sheet.getCell(sheet.findCell("KD_F40").getColumn(), i).getContents());
					 }
					//学生来源
					 if(sheet.findCell("KD_F41")!=null){
						 baseObj.setSource((String)sourceMap.get(sheet.getCell(sheet.findCell("KD_F41").getColumn(), i).getContents()));
					 }
					 //电子邮箱
					 if(sheet.findCell("KD_F42")!=null){
						 baseObj.setUserEmail(sheet.getCell(sheet.findCell("KD_F42").getColumn(), i).getContents());
					 }
					 //主页地址
					 if(sheet.findCell("KD_F43")!=null){
						 baseObj.setHomepage(sheet.getCell(sheet.findCell("KD_F43").getColumn(), i).getContents());
					 }
					 //残疾类型
					 if(sheet.findCell("KD_F44")!=null){
						 baseObj.setDid((String)cjlxMap.get(sheet.getCell(sheet.findCell("KD_F44").getColumn(), i).getContents()));
					 }
					//是否由政府购买学位
					 if(sheet.findCell("KD_F45")!=null){
						 baseObj.setBuydegree((String)booleanMap.get(sheet.getCell(sheet.findCell("KD_F45").getColumn(), i).getContents()));
					 }
					 //随班就读
					 if(sheet.findCell("KD_F46")!=null){
						 baseObj.setAttendant((String)sbjdMap.get(sheet.getCell(sheet.findCell("KD_F46").getColumn(), i).getContents()));
					 }
					 //成员1姓名
					 if(sheet.findCell("KD_F48")!=null){
						 baseFamilyOne.setName_one(sheet.getCell(sheet.findCell("KD_F48").getColumn(), i).getContents());
					 }
					 //成员1关系
					 if(sheet.findCell("KD_F49")!=null){
						 baseFamilyOne.setNuxes_one((String)relationMap.get(sheet.getCell(sheet.findCell("KD_F49").getColumn(), i).getContents()));
					 }
					 //成员1关系说明
					 if(sheet.findCell("KD_F50")!=null){
						 baseFamilyOne.setRelation_one(sheet.getCell(sheet.findCell("KD_F50").getColumn(), i).getContents());
					 }
					 //成员1现住地址
					 if(sheet.findCell("KD_F51")!=null){
						 baseFamilyOne.setAddress_one(sheet.getCell(sheet.findCell("KD_F51").getColumn(), i).getContents());
					 }
					 //成员1户口所在地行政区
					 if(sheet.findCell("KD_F52")!=null){
						 baseFamilyOne.setHouseaid_one(sheet.getCell(sheet.findCell("KD_F52").getColumn(), i).getContents());
					 }
					 //成员1联系电话
					 if(sheet.findCell("KD_F53")!=null){
						 baseFamilyOne.setTelephone_one(sheet.getCell(sheet.findCell("KD_F53").getColumn(), i).getContents());
					 }
					 //成员1是否监护人
					 if(sheet.findCell("KD_F54")!=null){
						 baseFamilyOne.setTutor_one((String)booleanMap.get(sheet.getCell(sheet.findCell("KD_F54").getColumn(), i).getContents()));
					 }
					 //成员1身份证件类型
					 if(sheet.findCell("KD_F55")!=null){
						 baseFamilyOne.setCtid_one((String)zjlxMap.get(sheet.getCell(sheet.findCell("KD_F55").getColumn(), i).getContents()));
					 }
					 //成员1身份证件号码
					 if(sheet.findCell("KD_F56")!=null){
						 baseFamilyOne.setCno_one(sheet.getCell(sheet.findCell("KD_F56").getColumn(), i).getContents());
					 }
					 //成员1民族
					 if(sheet.findCell("KD_F57")!=null){
						 baseFamilyOne.setNid_one((String)mzMap.get(sheet.getCell(sheet.findCell("KD_F57").getColumn(), i).getContents()));
					 }
					 //成员1工作单位
					 if(sheet.findCell("KD_F58")!=null){
						 baseFamilyOne.setUnit_one(sheet.getCell(sheet.findCell("KD_F58").getColumn(), i).getContents());
					 }
					 //成员1职务
					 if(sheet.findCell("KD_F59")!=null){
						 baseFamilyOne.setDuty_one(sheet.getCell(sheet.findCell("KD_F59").getColumn(), i).getContents());
					 }
					 family.add(baseFamilyOne);
					 
					 
					//成员2姓名
					 if(sheet.findCell("KD_F60")!=null){
						 baseFamilyTwo.setName_one(sheet.getCell(sheet.findCell("KD_F60").getColumn(), i).getContents());
					 }
					 
					 //成员2关系
					 if(sheet.findCell("KD_F61")!=null){
						 baseFamilyTwo.setNuxes_one((String)relationMap.get(sheet.getCell(sheet.findCell("KD_F61").getColumn(), i).getContents()));
					 }
					 //成员2关系说明
					 if(sheet.findCell("KD_F62")!=null){
						 baseFamilyTwo.setRelation_one(sheet.getCell(sheet.findCell("KD_F62").getColumn(), i).getContents());
					 }
					 //成员2现住地址
					 if(sheet.findCell("KD_F63")!=null){
						 baseFamilyTwo.setAddress_one(sheet.getCell(sheet.findCell("KD_F63").getColumn(), i).getContents());
					 }
					 //成员2户口所在地行政区
					 if(sheet.findCell("KD_F64")!=null){
						 baseFamilyTwo.setHouseaid_one(sheet.getCell(sheet.findCell("KD_F64").getColumn(), i).getContents());
					 }
					 //成员2联系电话
					 if(sheet.findCell("KD_F65")!=null){
						 baseFamilyTwo.setTelephone_one(sheet.getCell(sheet.findCell("KD_F65").getColumn(), i).getContents());
					 }
					 //成员2是否监护人
					 if(sheet.findCell("KD_F66")!=null){
						 baseFamilyTwo.setTutor_one((String)booleanMap.get(sheet.getCell(sheet.findCell("KD_F66").getColumn(), i).getContents()));
					 }
					 //成员2身份证件类型
					 if(sheet.findCell("KD_F67")!=null){
						 baseFamilyTwo.setCtid_one((String)zjlxMap.get(sheet.getCell(sheet.findCell("KD_F67").getColumn(), i).getContents()));
					 }
					 //成员2身份证件号码
					 if(sheet.findCell("KD_F68")!=null){
						 baseFamilyTwo.setCno_one(sheet.getCell(sheet.findCell("KD_F68").getColumn(), i).getContents());
					 }
					 //成员2民族
					 if(sheet.findCell("KD_F69")!=null){
						 baseFamilyTwo.setNid_one((String)mzMap.get(sheet.getCell(sheet.findCell("KD_F69").getColumn(), i).getContents()));
					 }
					 //成员2工作单位
					 if(sheet.findCell("KD_F70")!=null){
						 baseFamilyTwo.setUnit_one(sheet.getCell(sheet.findCell("KD_F70").getColumn(), i).getContents());
					 }
					 //成员2职务
					 if(sheet.findCell("KD_F71")!=null){
						 baseFamilyTwo.setDuty_one(sheet.getCell(sheet.findCell("KD_F71").getColumn(), i).getContents());
					 }
					 family.add(baseFamilyTwo);
					 baseObj.setBaseFamily(family);
					 
					 //是否进城务工人员随迁子女
					 if(sheet.findCell("KD_F72")!=null){
						 baseObj.setHelpneed((String)booleanMap.get(sheet.getCell(sheet.findCell("KD_F72").getColumn(), i).getContents()));
					 }
					 
					 //从Excel文件中取出数据存入BUSI_CHILDREN_INFO数据库表
				     ArrayList bvals = new ArrayList();
				     String newUserid = (String) new UUIDHexGenerator().generate(null);
				     bvals.add(newUserid);
				     bvals.add(baseObj.getLoginCode());
				     bvals.add(baseObj.getUserName());
				     bvals.add(baseObj.getStu_no());
				     bvals.add(baseObj.getUserPwd());
				     bvals.add(baseObj.getCno());
				     bvals.add(baseObj.getUserEmail());
				     bvals.add(baseObj.getTelephone());
				     bvals.add("1");//学生
				     bvals.add("1");
				     bvals.add("1");
				     bvals.add(baseObj.getUserGender());
				     
				     pdao.setSql(strSQL);
				     pdao.setBindValues(bvals);
				     pdao.executeTransactionSql();
		    		 /**
		    		  * 写入学生信息表
		    		  */
				     ArrayList paramVals = new ArrayList();
				     String studentid = (String)new UUIDHexGenerator().generate(null);
				     paramVals.add(studentid);
				     paramVals.add(baseObj.getOrigin());//籍贯
				     paramVals.add(baseObj.getCtid());//证件类型
				     paramVals.add(baseObj.getHealth());//健康状况
				     paramVals.add(baseObj.getCid());//国籍
				     paramVals.add(baseObj.getPsid());//政治面貌
				     paramVals.add(baseObj.getNid());//民族
				     paramVals.add(baseObj.getOid());//港澳台侨外
				     paramVals.add(baseObj.getHkxz());//户口性质
				     paramVals.add(baseObj.getUserBirth());//出生日期
				     paramVals.add(baseObj.getAreacode());//出生地行政区
				     paramVals.add(baseObj.getBid());//血型
				     paramVals.add(baseObj.getStudyway());//就读方式
				     paramVals.add(baseObj.getMailaddress());//通信地址
				     paramVals.add(baseObj.getHouseaddress());//家庭地址
				     paramVals.add(baseObj.getTelephone());//联系电话
				     paramVals.add(baseObj.getPostcode());//邮政编码
				     paramVals.add(baseObj.getSingleflag());//是否独生子女
				     paramVals.add(baseObj.getPreflag());//是否受过学前教育
				     paramVals.add(baseObj.getStayflag());//是否留守儿童
				     paramVals.add(baseObj.getHelpflag());//是否享受一补
				     paramVals.add(baseObj.getOrphanflag());//是否孤儿
				     paramVals.add(baseObj.getMartyr());//是否烈士或优抚子女
				     paramVals.add(baseObj.getGoway());//上学方式
				     paramVals.add(baseObj.getCarflag());//是否需要乘坐校车
				     paramVals.add(baseObj.getEffectdate());//身份证有效期
				     paramVals.add(baseObj.getRollid());//学籍辅号
				     paramVals.add(baseObj.getAttendant());//随班就读
				     paramVals.add(baseObj.getFarmer());//是否进城务工人员随迁子女
				     paramVals.add(baseObj.getHouseaid());//户口所在地行政区
				     paramVals.add(baseObj.getDid());//残疾类型
				     paramVals.add(baseObj.getCwid());//入学方式
				     paramVals.add(baseObj.getHelpneed());//是否需要申请资助
				     paramVals.add(baseObj.getDtance());//上下学距离
				     paramVals.add(baseObj.getSpecialty());//特长
				     paramVals.add(baseObj.getHomepage());//主页地址
				     paramVals.add(baseObj.getBuydegree());//是否政府购买学位
				     paramVals.add(baseObj.getSoldierflag());//是否军人子女
				     paramVals.add(newUserid);//用户编号
				     paramVals.add(baseObj.getSource());//学生来源
				     
				     pdao.setSql(stuSQL);
				     pdao.setBindValues(paramVals);
				     pdao.executeTransactionSql();
				     /**
				      * 学生个人家庭成员信息
				      */
				     
				    Set<BaseFamily> familyItem =  baseObj.getBaseFamily();
				    if(!familyItem.isEmpty()){
				     Iterator<BaseFamily> familyIterator = familyItem.iterator();
				     while(familyIterator.hasNext()){
				    	 ArrayList familyVals = new ArrayList();
				    	 BaseFamily familyObj = familyIterator.next();
				    	 familyVals.add(new UUIDHexGenerator().generate(null));
				    	 familyVals.add(studentid);
				    	 familyVals.add(familyObj.getName_one());
				    	 familyVals.add(familyObj.getNid_one());
				    	 familyVals.add(familyObj.getNuxes_one());
				    	 familyVals.add(familyObj.getRelation_one());
				    	 familyVals.add(familyObj.getCtid_one());
				    	 familyVals.add(familyObj.getCno_one());
				    	 familyVals.add(familyObj.getUnit_one());
				    	 familyVals.add(familyObj.getDuty_one());
				    	 familyVals.add(familyObj.getAddress_one());
				    	 familyVals.add(familyObj.getHouseaid_one());
				    	 familyVals.add(familyObj.getTelephone_one());
				    	 familyVals.add(familyObj.getTutor_one());
				    	 
				    	 pdao.setSql(familySQL);
					     pdao.setBindValues(familyVals);
					     pdao.executeTransactionSql();
				     }
				    }
				}
				//修改Excle文件处理状态
				StringBuffer updateSQL = new StringBuffer("update base_doc set doc_status='Y' "); 
				updateSQL.append(" where doc_id='"+file_id+"'");
				updateSQL.append(" and   doc_file_url='"+file_url+"'");
				pdao.setSql(updateSQL.toString());
				pdao.executeTransactionSql();
				
				//写入正式表
				StringBuffer strSQLBuff = new StringBuffer();
				strSQLBuff.append("insert into pcmc_user ");
				strSQLBuff.append("SELECT t1.userid,t1.usercode,t1.username,'',t1.userpwd ")
					.append(",portrait,description,idnumber,email,phone,mobile,emailbind,'1' as mobilebind,gender,usertype,usersource,modifydt,state ")
					.append("FROM temp_base_user t1 where ")
					.append("not exists (select null from pcmc_user where usercode = t1.usercode)");
				pdao.setSql(strSQLBuff.toString());
				pdao.executeTransactionSql();
				
				//同时写入学生信息表
				StringBuffer strsSQL = new StringBuffer();
				strsSQL.append("insert into base_studentinfo ")
					.append("select sm.userid,'").append(cur_deptid).append("' as 'deptid','").append(baseObj.getUserClass())
					.append("' as 'classid',t1.studentno,NULL as 'firstyear',NULL as 'finishyear',NULL as 'transferdt','1' as 'state',")
					.append(" ss.userid AS 'studinfoid' ").append(",'N' as 'pad_lock',NULL as 'lock_time','Y' AS valid ")
					.append(" from temp_base_user t1,pcmc_user sm,temp_base_student_attr ss where t1.usercode = sm.usercode and t1.userid = ss.userid and not exists (select null from base_studentinfo where userid = sm.userid) ");
				pdao.setSql(strsSQL.toString());
				pdao.executeTransactionSql();
				
				//写入学生扩展属性表
				StringBuffer studAttrSQL = new StringBuffer();
				studAttrSQL.append("insert into base_studentinfo_attr ").append("select * from temp_base_student_attr t where exists")
				.append(" (select null from base_studentinfo where studinfoid = t.userid)");
				pdao.setSql(studAttrSQL.toString());
				pdao.executeTransactionSql();
				
				//写入用户所属单位表
				StringBuffer strDeptSQL = new StringBuffer();
				strDeptSQL.append("insert into pcmc_user_dept ")
					.append("select SUBSTR(sm.userid,10,LENGTH(sm.userid)) AS 'userdeptid','").append(cur_deptid).append("' as 'deptid',sm.userid,")
					.append("'1' as 'state','").append(DatetimeUtil.getNow()).append("' as 'in date',Null as 'outdate' ")
					.append(" from temp_base_user t1,pcmc_user sm where t1.usercode = sm.usercode and not exists (select null from pcmc_user_dept where userid = sm.userid)");
				pdao.setSql(strDeptSQL.toString());
				pdao.executeTransactionSql();
				//写入用户扩展属性表
				StringBuffer userExtSQL = new StringBuffer();
				userExtSQL.append("insert into pcmc_user_ext ")
					.append("select sm.userid,Null as 'birthday','").append(cur_user)
					.append("' as 'createuser',Null as 'pubname',Null as 'pubmail',Null as 'pubphone',")
					.append(" Null as 'pubbirthday',Null as 'remark' ")
					.append(" from temp_base_user t1,pcmc_user sm where t1.usercode = sm.usercode and not exists (select null from pcmc_user_ext where userid = sm.userid)");
				pdao.setSql(userExtSQL.toString());
				pdao.executeTransactionSql();
			}
		}catch (Exception e) {
			e.printStackTrace();
			String errMng = e.getMessage().toString();
			//格式错误的
			StringBuffer errSQL= new StringBuffer("update base_doc set doc_status=?,doc_remark=?");
			errSQL.append(" where doc_id='"+file_id+"'");
			errSQL.append(" and doc_file_url='"+file_url+"'");
			
			ArrayList bvals = new ArrayList();
			bvals.add("E");
			if (StringUtil.isNotEmpty(errMng)){
				errMng ="未知错误";
			}
			if (errMng.length()>400){
			    bvals.add(errMng.substring(0, 400));
			}
			else{
				bvals.add(errMng);
			}
			
			pdao.setSql(errSQL.toString());
			pdao.setBindValues(bvals);
			try {
				pdao.executeTransactionSql();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}		
			
		}finally {	
			try {
				//删除临时表
				StringBuffer SQL = new StringBuffer();
				SQL.append("delete from temp_base_user");
				pdao.setSql(SQL.toString());
				pdao.executeTransactionSql();
				/*StringBuffer delSQL = new StringBuffer();
				delSQL.append("delete from temp_base_student_attr");
				pdao.setSql(delSQL.toString());
				pdao.executeTransactionSql();*/
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}	
			pdao.releaseConnection();
			try {
			if(null!=stream)
				stream.close();
			} catch (IOException e) {
				
			}
			
		}
	}
}
