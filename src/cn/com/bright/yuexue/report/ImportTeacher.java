package cn.com.bright.yuexue.report;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jxl.Sheet;

import org.jdom.Element;

import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.db.SqlTypes;
import cn.brightcom.jraf.util.ConfigDocument;
import cn.brightcom.jraf.util.Constants;
import cn.brightcom.jraf.util.DatetimeUtil;
import cn.brightcom.jraf.util.FileUtil;
import cn.brightcom.jraf.util.Log;
import cn.brightcom.jraf.util.PasswordEncoder;
import cn.brightcom.jraf.util.XmlDocPkgUtil;

public class ImportTeacher extends AbstractExcel{
	
	private XmlDocPkgUtil xmlDocUtil = null;
	private Element reqElData=null;
	private PrintWriter logFile = null;
	private int sucCnt = 0;
	private int dupCnt = 0;
	private int errCnt = 0;
	private int skipCnt = 0;
	private int singCnt =0;
	private String upload_by = "",exam_type="";
	private String cur_deptid = "";
	private String classCodes ="";
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
			
			Sheet sheet = parseTacherExcel(doc_id, FileUtil.getPhysicalPath(filenm));
			int rows = sheet.getRows();
			if(rows>0){
				parseRow( rows,  sheet);
			}
			StringBuffer info = new StringBuffer().append("成功:").append(
					sucCnt).append(",").append("重复:").append(dupCnt)
					.append(",").append("错误:").append(errCnt).append(",")
					.append("忽略:").append(dupCnt);
			logFile.println("教师基本信息导入"+DatetimeUtil.getNow(null) + ",导入结束....");
			logFile.println(info.toString());
			upFlag(doc_id,info.toString());
			uptTempTeacher();
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
		String value = "",dbColName="",xlsColName="",extype="",conversion="",isnull="",conversionid="";
		HashMap map=null;
		List excelList = getExlElement("teacher");
		PlatformDao pdao = new PlatformDao();
		if (excelList.size()<=0) return;
		try {
			a:for (int i = 2; i < rows; i++) {
				Element rec = new Element("RequestData");
				b:for(int j=0;j<excelList.size();j++){  
					 Element ele = (Element)excelList.get(j);
					 dbColName = ele.getName();
					 xlsColName =  ele.getText();
					 extype = ele.getAttributeValue("extype");
					 conversion = ele.getAttributeValue("conversion");
					 conversionid = ele.getAttributeValue("conversionid");
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
							map = new HashMap();
							map = strSplit(value);
							value=AbstractExcel.convesionStr(conversion,map.get("mapVal").toString());
						}
						if("exam_type".equals(dbColName)) exam_type=value;
						rec.addContent(new Element(dbColName).setText(value));
					} else {
						continue a;
					}
				}
				saveTempTeacher(rec);
				sucCnt++;
			}
		StringBuffer checkSQL = new StringBuffer();
		checkSQL.append("SELECT t.* FROM temp_base_teacher t ");
		checkSQL.append("WHERE t.valid='Y' and NOT EXISTS  ");
		checkSQL.append("(SELECT NULL FROM pcmc_user WHERE idnumber = t.idnumber)");
		pdao.setSql(checkSQL.toString());
		Element tempElement = (Element)pdao.executeQuerySql(0, -1);
		List list= tempElement.getChildren("Record");
		if(list.size()>0){
			for(int k=0;k<list.size();k++){
				saveTeacherInfo((Element)list.get(k));
				singCnt++;
			}
		}
		dupCnt = sucCnt-singCnt;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	/**
	 * 日期格式化
	 * @param dstr
	 * @return
	 */
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
			return SqlTypes.getConvertor("Timestamp").convert(dstr,"yyyy-MM-dd");
		}
		catch(Exception ex)
		{
		   ex.printStackTrace();	
		}
		return null;
	}
	
	
	
	private static HashMap strSplit(String key){
		HashMap texMap = new HashMap();
		StringBuffer strKey = new StringBuffer("");
		StringBuffer strValue = new StringBuffer("");
		String[] strArray =null;
		String[] objArray = null;
		if(key.contains("，") && key.contains("-")){
			strArray= key.split("，");
			for(int i=0;i<strArray.length;i++){
				objArray = strArray[i].split("-");
				strKey.append(objArray[0]);
				strValue.append(objArray[1]);
				if((i+1)<strArray.length){
					strKey.append(",");
					strValue.append(",");
				}
			}
		}else{
			if(key.contains("-")){
				strArray= key.split("-");
				strKey.append(strArray[0]);
				strValue.append(strArray[1]);
			}else{
				strKey.append(key);
			}
		}
		texMap.put("mapKey",strKey);
		texMap.put("mapVal", strValue);
		return texMap;
	}
	
	/**
	 * 写入临时教师数据库表
	 * @param reqcodeclasData
	 */
	private void saveTempTeacher(Element reqcodeclasData){
		Element objEl = reqcodeclasData;
		PlatformDao pdao = new PlatformDao();
		try {
			pdao.beginTransaction();
			Element tempRecord = ConfigDocument.createRecordElement("yuexue", "temp_base_teacher");
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
	 * 修改临时教师数据库表有效标识
	 * @param reqcodeclasData
	 */
	private void uptTempTeacher(){
		PlatformDao pdao = new PlatformDao();
		try {
			String strSQL ="update temp_base_teacher set valid='N' where valid='Y'";
			pdao.setSql(strSQL);
			pdao.executeTransactionSql();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void saveTeacherInfo(Element reqcodeclasData){
		Element objEl = reqcodeclasData;
		PlatformDao pdao = new PlatformDao();
		HashMap insuranceMap = null;
		HashMap subMap = null;
		HashMap eduMap = null;
		try {
			pdao.beginTransaction();
			String insurance = objEl.getChildTextTrim("gzfl_key");//五险一金
			String course_sub = objEl.getChildTextTrim("rjkc_key");//任教课程
			String job_edu = objEl.getChildTextTrim("pxqk_key");//近三年专任教师接受培训情况
			insuranceMap = strSplit(insurance);
			String insuranceKey = insuranceMap.get("mapKey").toString();
			String insuranceVal = insuranceMap.get("mapVal").toString();
			subMap = strSplit(course_sub);
			String subKey = subMap.get("mapKey").toString();
			String subVal = subMap.get("mapVal").toString();
			eduMap = strSplit(job_edu);
			String eduKey = eduMap.get("mapKey").toString();
			String eduVal = eduMap.get("mapVal").toString();
			
			Element userRecord = ConfigDocument.createRecordElement("pcmc", "pcmc_user");
			XmlDocPkgUtil.copyValues(objEl, userRecord, 0 , true);
			XmlDocPkgUtil.setChildText(userRecord, "usercode", objEl.getChildTextTrim("idnumber"));
			XmlDocPkgUtil.setChildText(userRecord, "userpwd", PasswordEncoder.encode("123456"));
			XmlDocPkgUtil.setChildText(userRecord, "usertype", "2");
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
		    XmlDocPkgUtil.copyValues(objEl, userExtRecord, 0 , true);
		    XmlDocPkgUtil.setChildText(userExtRecord, "userid", pk_userid);
		    XmlDocPkgUtil.setChildText(userExtRecord, "createuser", upload_by);
		    XmlDocPkgUtil.setChildText(userExtRecord, "pubname", "0");
		    XmlDocPkgUtil.setChildText(userExtRecord, "pubmail","0");
		    XmlDocPkgUtil.setChildText(userExtRecord, "pubphone", "0");
		    pdao.insertOneRecord(userExtRecord);
		    
		  //系统用户扩展信息
		    Element teacherRecord = ConfigDocument.createRecordElement("yuexue", "base_teacher_info");
		    XmlDocPkgUtil.copyValues(objEl, teacherRecord, 0 , true);
		    XmlDocPkgUtil.setChildText(teacherRecord, "userid", pk_userid);
		    XmlDocPkgUtil.setChildText(teacherRecord, "gzfl_key", insuranceKey);
		    XmlDocPkgUtil.setChildText(teacherRecord, "gzfl_value", insuranceVal);
		    XmlDocPkgUtil.setChildText(teacherRecord, "rjkc_key", subKey);
		    XmlDocPkgUtil.setChildText(teacherRecord, "rjkc_value", subVal);
		    XmlDocPkgUtil.setChildText(teacherRecord, "pxqk_key",eduKey);
		    XmlDocPkgUtil.setChildText(teacherRecord, "pxqk_value", eduVal);
		    pdao.insertOneRecord(teacherRecord);
		    pdao.commitTransaction();
		}catch (Exception e) {
			pdao.rollBack();
			e.printStackTrace();
	        xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
	        Log log4j = new Log(this.getClass().toString());
	        log4j.logError("[参数设置-增加导入教师信息.]"+e.getMessage());
		} finally {
			pdao.releaseConnection();
		}
	}
	
	
}
