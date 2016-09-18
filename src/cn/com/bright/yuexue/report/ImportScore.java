package cn.com.bright.yuexue.report;

import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import jxl.Sheet;

import org.jdom.Element;

import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.db.SqlTypes;
import cn.brightcom.jraf.util.ConfigDocument;
import cn.brightcom.jraf.util.Constants;
import cn.brightcom.jraf.util.DatetimeUtil;
import cn.brightcom.jraf.util.FileUtil;
import cn.brightcom.jraf.util.Log;
import cn.brightcom.jraf.util.XmlDocPkgUtil;
import cn.com.bright.yuexue.base.BaseFamily;

/**
 * 学生成绩导入
 * @author E40
 *
 */
public class ImportScore extends AbstractExcel{
	private XmlDocPkgUtil xmlDocUtil = null;
	private Element reqElData=null;
	private PrintWriter logFile = null;
	private int sucCnt = 0;
	private int dupCnt = 0;
	private int errCnt = 0;
	private int singCnt =0;
	private int skipCnt = 0;
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
			
			Sheet sheet = parseScoreExcel(doc_id, FileUtil.getPhysicalPath(filenm));
			int rows = sheet.getRows();
			if(rows>0){
				parseRow( rows,  sheet);
			}
			StringBuffer info = new StringBuffer().append("成功:").append(
					sucCnt).append(",").append("重复:").append(dupCnt)
					.append(",").append("错误:").append(errCnt).append(",")
					.append("忽略:").append(dupCnt);
			logFile.println("学生成绩导入"+DatetimeUtil.getNow(null) + ",导入结束....");
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
		List excelList = getExlElement("score");
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
				saveTempScore(rec);
				sucCnt++;
			}
			StringBuffer checkSQL = new StringBuffer();
			checkSQL.append("SELECT s.studinfoid,t.* FROM temp_score t,base_studentinfo s WHERE  ");
			checkSQL.append("t.idnumber = s.stu_idnumber AND t.valid='Y' AND s.valid='Y' ");
			checkSQL.append("AND NOT EXISTS ");
			checkSQL.append("(SELECT NULL FROM base_student_score  WHERE studinfoid = s.studinfoid)");
			pdao.setSql(checkSQL.toString());
			Element tempElement = (Element)pdao.executeQuerySql(0, -1);
			List list= tempElement.getChildren("Record");
			if(list.size()>0){
				for(int k=0;k<list.size();k++){
					saveScore((Element)list.get(k));
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
	private void saveTempScore(Element reqcodeclasData){
		Element objEl = reqcodeclasData;
		PlatformDao pdao = new PlatformDao();
		try {
			pdao.beginTransaction();
			Element tempRecord = ConfigDocument.createRecordElement("student", "temp_score");
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
			String strSQL ="update temp_score set valid='N' where valid='Y'";
			pdao.setSql(strSQL);
			pdao.executeTransactionSql();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	 public void saveScore(Element reqcodeclasData){
		Element objEl = reqcodeclasData;
		PlatformDao pdao = new PlatformDao();
		try {
			    pdao.beginTransaction();
			    Element scoreRecord = ConfigDocument.createRecordElement("student", "base_student_score");
				XmlDocPkgUtil.copyValues(objEl, scoreRecord, 0 , true);
				pdao.insertOneRecord(scoreRecord);
				pdao.commitTransaction();
			    
			} catch (Exception e) {
				pdao.rollBack();
				e.printStackTrace();
		        xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
		        Log log4j = new Log(this.getClass().toString());
		        log4j.logError("[参数设置-增加导入学生成绩信息.]"+e.getMessage());
			} finally {
				pdao.releaseConnection();
			}
	}
}
