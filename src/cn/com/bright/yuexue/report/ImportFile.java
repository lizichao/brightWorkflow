package cn.com.bright.yuexue.report;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;

import org.jdom.Document;
import org.jdom.Element;

import cn.brightcom.jraf.context.ApplicationContext;
import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.id.UUIDHexGenerator;
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
import cn.brightcom.system.pcmc.util.ListenerUtil;
import cn.brightcom.system.pcmc.widetel.RealinkInterface;
import cn.com.bright.masterReview.base.UserManage;

/**
 * 用户导入
 * @author E40
 *
 */
public class ImportFile {
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
		if("importDocFile".equals(action)){
			importDocFile();//上传文档
		}
	   return xmlDoc;
	}
	/**
	 * 上传文档
	 */
	public void importDocFile(){
		Element reqElement =  xmlDocUtil.getRequestData();
		Element session = xmlDocUtil.getSession();
		Element doc_file = reqElement.getChild("doc_file");//文档文件
		//String doc_flag = reqElement.getChildTextTrim("doc_flag");//文档标识
	//	String classids = reqElement.getChildTextTrim("classids");//班级编号
		String curUserid = session.getChildText("userid");
	    //String curUsercode = session.getChildText("usercode");
	    String deptid = session.getChildText("deptid");
		PlatformDao pdao = new PlatformDao();
		//String doc_id = "";//文档编号
		//String fileFlag = "";
		try {
			Element docRecord = ConfigDocument.createRecordElement("yuexue","base_doc");
			XmlDocPkgUtil.copyValues(reqElement, docRecord, 0 , true);
			String fileName = doc_file.getAttributeValue("name");
        	String fileExt = doc_file.getAttributeValue("ext");
        	String fileSize = doc_file.getAttributeValue("size");
        	String upPath = doc_file.getText();
		    String srcFile =  FileUtil.getPhysicalPath(upPath);//文档路径
		    String desFileName = FileUtil.getFileName(srcFile);

		    FileUtil.createDirs(FileUtil.getWebPath()+"upload/base/doc/", true);		    
			FileUtil.moveFile(new File(srcFile), new File(FileUtil.getWebPath()+"upload/base/doc/"+desFileName));			
			FileUtil.deleteFile(srcFile);	
			
			XmlDocPkgUtil.setChildText(docRecord, "doc_file_url", "/upload/base/doc/"+desFileName);
			XmlDocPkgUtil.setChildText(docRecord, "doc_title", fileName);
			XmlDocPkgUtil.setChildText(docRecord, "doc_type", fileExt);
			XmlDocPkgUtil.setChildText(docRecord, "doc_size", fileSize);
		    
			XmlDocPkgUtil.setChildText(docRecord, "doc_status", "N");
			XmlDocPkgUtil.setChildText(docRecord, "del_flag", "N");
			XmlDocPkgUtil.setChildText(docRecord, "upload_by", curUserid);
			XmlDocPkgUtil.setChildText(docRecord, "deptid", deptid);
		    pdao.insertOneRecord(docRecord);
				
			xmlDocUtil.setResult("0");
			xmlDocUtil.writeHintMsg("文件上传成功!");
		} catch (Exception e) {
			e.printStackTrace();
			log4j.logError("[文档上传]"+e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
		} finally {
			pdao.releaseConnection();
		}
	}
	/**
	 * 数据解析
	 */
	public void importData() {
		ApplicationContext.regSubSys("yuexue"); 
		StringBuffer strBuffer = new StringBuffer();
		String doc_id="";//需要解析文档编号
		String fileURL ="";//文档地址
		String studentName="";//学生姓名
		String studentCode = "";//学生登录帐号
		String idNumber="";//证件号码
		String email = "";//电子邮箱
		String deptid ="";//学校编号 
		String studentNumber="";//学号
		String studentPwd=PasswordEncoder.encode("123456");//学生登录密码
		String curUsercode ="";//当前登录用户
		String iphone="";//手机号码
		String sex="";//性别
		String address="";//性别
		String doc_flag ="";//用户标识"professor"专家3，"headmaster"校长1，"personalLeader"人事干部2，"领导"4
		
		PlatformDao pdao = new PlatformDao();
		InputStream stream =null;
		//获得将要解析的文件
		strBuffer.append("select * from base_doc where doc_status='N' ORDER BY RAND() LIMIT 1 ");
		try {			
			pdao.setSql(strBuffer.toString());
			Element elDocFile = pdao.executeQuerySql(0, 1);
			List lsDocFile = elDocFile.getChildren("Record");
			if(lsDocFile.size()>0){
				Workbook rwb = null;
				Cell cell = null;
				for(int k=0;k<lsDocFile.size();k++){
					Element doc = (Element)lsDocFile.get(k);
					doc_id = doc.getChildTextTrim("doc_id");
					curUsercode = doc.getChildTextTrim("upload_by");					
					String strSQL = "";					
					fileURL = doc.getChildTextTrim("doc_file_url");
					deptid = doc.getChildTextTrim("deptid");
//					if ( StringUtil.isEmpty(deptid)){
//						if ("ImportProfessor".equals(doc_flag)) {
//							//学生信息导入需要班级信息
//							StringBuffer updateSQL = new StringBuffer("update base_doc set doc_status='E',doc_remark='单位ID为空,无法导入!' "); 
//							updateSQL.append(" where doc_id='"+doc_id+"'");
//							pdao.setSql(updateSQL.toString());
//							pdao.executeTransactionSql();
//							continue;
//						}
//					}
					doc_flag = doc.getChildTextTrim("doc_flag");
					File file = new File(FileUtil.getWebPath()+fileURL);
					if (file.exists()){
					    System.out.println("=== 开始处理["+doc.getChildText("upload_by")+"]上传的["+doc.getChildText("doc_title")+"]数据");					
						//创建输入流
					    stream= new FileInputStream(FileUtil.getWebPath()+fileURL);
						WorkbookSettings ws = new WorkbookSettings();
					    ws.setLocale(new Locale("zh", "CN"));
					    ws.setEncoding("ISO-8859-1");
						//获取Excel文件对象
						rwb = Workbook.getWorkbook(stream,ws);
						//获取文件的指定工作表 默认的第一个
						Sheet sheet = rwb.getSheet(0);
						
					    //行数(表头的目录不需要，从3开始)
						strSQL = "insert into temp_pcmc_user (userid,usercode,username,userpwd,idnumber,email,mobile,address,usertype,usersource,state,gender) " +
						 		" values (?,?,?,?,?,?,?,?,?,?,?,?)";
					    int xlsRowCount = 0;
						for(int i=2; i<sheet.getRows(); i++){
							 if (sheet.findCell("KD_XM")!=null){
								 studentName = sheet.getCell(sheet.findCell("KD_XM").getColumn(), i).getContents();//学生姓名
							 }else{
								 studentName=null;
							 }
							 if (sheet.findCell("KD_ZH")!=null){
								 studentCode = sheet.getCell(sheet.findCell("KD_ZH").getColumn(), i).getContents();//登录帐号
							 }else{
								 studentCode=null;
							 }
							 if(studentCode.length()>30){
								 studentCode = studentCode.substring(0, 20);
							 } 
							 if (sheet.findCell("KD_IPHONE")!=null){
								 iphone = sheet.getCell(sheet.findCell("KD_IPHONE").getColumn(),i).getContents();//手机号码
							 }else{
								 iphone=null;
							 }
							 //沃课堂按照手机号码作为账户，允许教师的账号为空，且用手机号充当账号
							 if(StringUtil.isEmpty(studentCode)) {
//								 if ("ImportStudent".equals(doc_flag)) {
//									 break;
//								 } else {
//									 if (StringUtil.isEmpty(iphone)) {
//										 break;
//									 } else {
										 studentCode = iphone;
									 //}
								 //}
							 }
							 if (sheet.findCell("KD_ZJHM")!=null){
								 idNumber = sheet.getCell(sheet.findCell("KD_ZJHM").getColumn(),i).getContents();//证件号码
							 }else{
								 idNumber=null;
							 }
							 if (sheet.findCell("KD_EMAIL")!=null){
								 email = sheet.getCell(sheet.findCell("KD_EMAIL").getColumn(),i).getContents();//电子邮箱
							 }else{
								 email=null;
							 }
								 
							 //根据身份证号码取得性别
							 if (StringUtil.isEmpty(idNumber)|| (idNumber.length() != 15 && idNumber.length() != 18)){
								 if (sheet.findCell("KD_SEX")!=null){
									 String xlsSex = sheet.getCell(sheet.findCell("KD_SEX").getColumn(),i).getContents();//性别
									 if (StringUtil.isNotEmpty(xlsSex)){
									    sex = "女".equals(xlsSex)?"0":"1";
									 }
								 }
							 }
							 else if (idNumber.length() == 15 || idNumber.length() == 18){
								 String lastValue = idNumber.substring(idNumber.length() - 2, idNumber.length()-1);
								 System.out.println(lastValue);
								 sex = Integer.parseInt(lastValue)%2 == 0?"0":"1";
							 }
							 
							 if (sheet.findCell("KD_ADDRESS")!=null){
							     address = sheet.getCell(sheet.findCell("KD_ADDRESS").getColumn(),i).getContents();//地址
                             }else{
                                 address=null;
                             }

						     //从Excel文件中取出数据存入BUSI_CHILDREN_INFO数据库表
						     ArrayList<Object> bvals = new ArrayList<Object>();
						     bvals.add(new UUIDHexGenerator().generate(null));
						     bvals.add(studentCode);
						     bvals.add(studentName);
						     bvals.add(studentPwd);
						     bvals.add(idNumber);
						     bvals.add(email);
						     bvals.add(iphone);
						     bvals.add(address);
						     if("ImportProfessor".equals(doc_flag)){
						    	 bvals.add(UserManage.PROFESSOR_TYPE);//专家
						     }else if("ImportHeadMaster".equals(doc_flag)){
						    	 bvals.add(UserManage.MASTER_TYPE);//校长
						     }else if("ImportPersonalLeader".equals(doc_flag)){
                                 bvals.add(UserManage.PERSONNEL_LEADER);//人事干部
                             }else if("ImportLeader".equals(doc_flag)){
                                 bvals.add(UserManage.LEADER_TYPE);//领导
                             }
						     bvals.add("1");
						     bvals.add("1");
						     bvals.add(sex);
						     
						     pdao.setSql(strSQL);
						     pdao.setBindValues(bvals);
						     pdao.executeTransactionSql();
						     xlsRowCount++;
						     
					    }
                        //有效记录
						StringBuffer strQuery = new StringBuffer();
						strQuery.append("select count(t1.userid) as usercount from temp_pcmc_user t1");
						strQuery.append(" where not exists (select null from pcmc_user where usercode = t1.usercode)");
						ArrayList bvals = new ArrayList();
						Element rec = DaoUtil.getOneRecord(strQuery.toString(), bvals);
						String usercount = rec.getChildText("usercount");
						
						//写入正式表 这个意思是在temp_pcmc_user里面有记录并且没有在pcmc_user有记录，在pcmc_user state等于9 的记录不算
						StringBuffer strSQLBuff = new StringBuffer();
						strSQLBuff.append("insert into pcmc_user (userid,usercode,username,nickname,userpwd,portrait,description,idnumber,email,phone,mobile,emailbind,mobilebind,gender,usertype,usersource,modifydt,state)");
						strSQLBuff.append("SELECT userid,usercode,username,'',userpwd ")
							.append(",portrait,description,idnumber,email,phone,mobile,emailbind,'1' as mobilebind,gender,usertype,usersource,modifydt,state ")
							.append("FROM temp_pcmc_user t1 where ")
							.append("not exists (select null from pcmc_user where state <>'9' and usercode = t1.usercode) ");
						pdao.setSql(strSQLBuff.toString());
						pdao.executeTransactionSql();
						if("ImportProfessor".equals(doc_flag)){
							//同时写入专家信息表
							StringBuffer strsSQL = new StringBuffer();
							strsSQL.append("insert into headmaster_professor_info (id,userid,usercode,username,user_sex,idnumber,deptid,mobile,address,create_by,create_date,valid) ")
								.append(" select SUBSTR(sm.userid,15,LENGTH(sm.userid)) AS 'id',sm.userid,sm.usercode,sm.username,sm.gender,sm.idnumber,'").append(deptid).append("' as deptid,")
								.append(" sm.mobile,t1.address,'").append(curUsercode).append("' as 'create_by','").append(DatetimeUtil.getNow()).append("' as 'create_date',")
								.append("'1' as 'valid' ")
								.append(" from temp_pcmc_user t1,pcmc_user sm where t1.usercode = sm.usercode and not exists (select null from headmaster_professor_info where valid='Y' and userid = sm.userid) ");
							pdao.setSql(strsSQL.toString());
							pdao.executeTransactionSql();
						} else {
							//同时写入教师信息表
							StringBuffer strsSQL = new StringBuffer();
							strsSQL.append("insert into base_teacher_info (teacher_id,userid,ctid,preflag,sfyjszgz,bzqk,sfqrzsf,mfshs,jsgw,valid,createdate) ")
								.append(" select SUBSTR(sm.userid,15,LENGTH(sm.userid)) AS 'teacher_id' ")
								.append(",sm.userid,'1' as ctid,true as preflag,true as sfyjszgz,'1' as bzqk,true as sfqrzsf,true as mfshs,true as jsgw,'Y' as valid,'")
								.append(DatetimeUtil.getNow()).append("' as createdate")
								.append(" from temp_pcmc_user t1,pcmc_user sm where t1.usercode = sm.usercode and not exists (select null from base_teacher_info where valid='Y' and userid = sm.userid) GROUP BY sm.usercode ");
							pdao.setSql(strsSQL.toString());
							pdao.executeTransactionSql();
						
						}
						//写入用户所属单位表
						StringBuffer strDeptSQL = new StringBuffer();
						strDeptSQL.append("insert into pcmc_user_dept ")
							.append("select sm.userid AS 'userdeptid','").append(deptid).append("' as 'deptid',sm.userid,")
							.append("'1' as 'state','").append(DatetimeUtil.getNow()).append("' as 'in date',Null as 'outdate' ")
							.append(" from temp_pcmc_user t1,pcmc_user sm where t1.usercode = sm.usercode and not exists (select null from pcmc_user_dept where userid = sm.userid)");
						pdao.setSql(strDeptSQL.toString());
						pdao.executeTransactionSql();
						//写入用户扩展属性表
						StringBuffer userExtSQL = new StringBuffer();
						userExtSQL.append("insert into pcmc_user_ext ")
							.append("select sm.userid,Null as 'birthday','").append(curUsercode)
							.append("' as 'createuser',Null as 'pubname',Null as 'pubmail',Null as 'pubphone',")
							.append(" Null as 'pubbirthday',Null as 'remark' ")
							.append(" from temp_pcmc_user t1,pcmc_user sm where t1.usercode = sm.usercode and not exists (select null from pcmc_user_ext where userid = sm.userid)");
						pdao.setSql(userExtSQL.toString());
						pdao.executeTransactionSql();
						
						//修改Excle文件处理状态
						StringBuffer updateSQL = new StringBuffer("update base_doc set doc_status='Y',doc_remark='Excel记录共["+xlsRowCount+"]行,导入成功"+usercount+"行' "); 
						updateSQL.append(" where doc_id='"+doc_id+"'");
						updateSQL.append(" and   doc_file_url='"+fileURL+"'");
						pdao.setSql(updateSQL.toString());
						pdao.executeTransactionSql();
						
						if(null!=ws){
							ws=null;
						}
						if(null!=rwb){
							rwb.close();
							rwb=null;
						}
						
				    }					
				}
			}
			
		} catch (Exception e) {	
			e.printStackTrace();
			String errMng = e.getMessage().toString();
			//格式错误的
			StringBuffer errSQL= new StringBuffer("update base_doc set doc_status=?,doc_remark=?");
			errSQL.append(" where doc_id='"+doc_id+"'");
			errSQL.append(" and doc_file_url='"+fileURL+"'");
			
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
			//删除临时表
			StringBuffer SQL = new StringBuffer();
			SQL.append("delete from temp_pcmc_user ");
			pdao.setSql(SQL.toString());
			try {
				pdao.executeTransactionSql();
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
