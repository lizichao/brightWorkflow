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
 * �û�����
 * @author E40
 *
 */
public class ImportFile {
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
		if("importDocFile".equals(action)){
			importDocFile();//�ϴ��ĵ�
		}
	   return xmlDoc;
	}
	/**
	 * �ϴ��ĵ�
	 */
	public void importDocFile(){
		Element reqElement =  xmlDocUtil.getRequestData();
		Element session = xmlDocUtil.getSession();
		Element doc_file = reqElement.getChild("doc_file");//�ĵ��ļ�
		//String doc_flag = reqElement.getChildTextTrim("doc_flag");//�ĵ���ʶ
	//	String classids = reqElement.getChildTextTrim("classids");//�༶���
		String curUserid = session.getChildText("userid");
	    //String curUsercode = session.getChildText("usercode");
	    String deptid = session.getChildText("deptid");
		PlatformDao pdao = new PlatformDao();
		//String doc_id = "";//�ĵ����
		//String fileFlag = "";
		try {
			Element docRecord = ConfigDocument.createRecordElement("yuexue","base_doc");
			XmlDocPkgUtil.copyValues(reqElement, docRecord, 0 , true);
			String fileName = doc_file.getAttributeValue("name");
        	String fileExt = doc_file.getAttributeValue("ext");
        	String fileSize = doc_file.getAttributeValue("size");
        	String upPath = doc_file.getText();
		    String srcFile =  FileUtil.getPhysicalPath(upPath);//�ĵ�·��
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
			xmlDocUtil.writeHintMsg("�ļ��ϴ��ɹ�!");
		} catch (Exception e) {
			e.printStackTrace();
			log4j.logError("[�ĵ��ϴ�]"+e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
		} finally {
			pdao.releaseConnection();
		}
	}
	/**
	 * ���ݽ���
	 */
	public void importData() {
		ApplicationContext.regSubSys("yuexue"); 
		StringBuffer strBuffer = new StringBuffer();
		String doc_id="";//��Ҫ�����ĵ����
		String fileURL ="";//�ĵ���ַ
		String studentName="";//ѧ������
		String studentCode = "";//ѧ����¼�ʺ�
		String idNumber="";//֤������
		String email = "";//��������
		String deptid ="";//ѧУ��� 
		String studentNumber="";//ѧ��
		String studentPwd=PasswordEncoder.encode("123456");//ѧ����¼����
		String curUsercode ="";//��ǰ��¼�û�
		String iphone="";//�ֻ�����
		String sex="";//�Ա�
		String address="";//�Ա�
		String doc_flag ="";//�û���ʶ"professor"ר��3��"headmaster"У��1��"personalLeader"���¸ɲ�2��"�쵼"4
		
		PlatformDao pdao = new PlatformDao();
		InputStream stream =null;
		//��ý�Ҫ�������ļ�
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
//							//ѧ����Ϣ������Ҫ�༶��Ϣ
//							StringBuffer updateSQL = new StringBuffer("update base_doc set doc_status='E',doc_remark='��λIDΪ��,�޷�����!' "); 
//							updateSQL.append(" where doc_id='"+doc_id+"'");
//							pdao.setSql(updateSQL.toString());
//							pdao.executeTransactionSql();
//							continue;
//						}
//					}
					doc_flag = doc.getChildTextTrim("doc_flag");
					File file = new File(FileUtil.getWebPath()+fileURL);
					if (file.exists()){
					    System.out.println("=== ��ʼ����["+doc.getChildText("upload_by")+"]�ϴ���["+doc.getChildText("doc_title")+"]����");					
						//����������
					    stream= new FileInputStream(FileUtil.getWebPath()+fileURL);
						WorkbookSettings ws = new WorkbookSettings();
					    ws.setLocale(new Locale("zh", "CN"));
					    ws.setEncoding("ISO-8859-1");
						//��ȡExcel�ļ�����
						rwb = Workbook.getWorkbook(stream,ws);
						//��ȡ�ļ���ָ�������� Ĭ�ϵĵ�һ��
						Sheet sheet = rwb.getSheet(0);
						
					    //����(��ͷ��Ŀ¼����Ҫ����3��ʼ)
						strSQL = "insert into temp_pcmc_user (userid,usercode,username,userpwd,idnumber,email,mobile,address,usertype,usersource,state,gender) " +
						 		" values (?,?,?,?,?,?,?,?,?,?,?,?)";
					    int xlsRowCount = 0;
						for(int i=2; i<sheet.getRows(); i++){
							 if (sheet.findCell("KD_XM")!=null){
								 studentName = sheet.getCell(sheet.findCell("KD_XM").getColumn(), i).getContents();//ѧ������
							 }else{
								 studentName=null;
							 }
							 if (sheet.findCell("KD_ZH")!=null){
								 studentCode = sheet.getCell(sheet.findCell("KD_ZH").getColumn(), i).getContents();//��¼�ʺ�
							 }else{
								 studentCode=null;
							 }
							 if(studentCode.length()>30){
								 studentCode = studentCode.substring(0, 20);
							 } 
							 if (sheet.findCell("KD_IPHONE")!=null){
								 iphone = sheet.getCell(sheet.findCell("KD_IPHONE").getColumn(),i).getContents();//�ֻ�����
							 }else{
								 iphone=null;
							 }
							 //�ֿ��ð����ֻ�������Ϊ�˻��������ʦ���˺�Ϊ�գ������ֻ��ų䵱�˺�
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
								 idNumber = sheet.getCell(sheet.findCell("KD_ZJHM").getColumn(),i).getContents();//֤������
							 }else{
								 idNumber=null;
							 }
							 if (sheet.findCell("KD_EMAIL")!=null){
								 email = sheet.getCell(sheet.findCell("KD_EMAIL").getColumn(),i).getContents();//��������
							 }else{
								 email=null;
							 }
								 
							 //�������֤����ȡ���Ա�
							 if (StringUtil.isEmpty(idNumber)|| (idNumber.length() != 15 && idNumber.length() != 18)){
								 if (sheet.findCell("KD_SEX")!=null){
									 String xlsSex = sheet.getCell(sheet.findCell("KD_SEX").getColumn(),i).getContents();//�Ա�
									 if (StringUtil.isNotEmpty(xlsSex)){
									    sex = "Ů".equals(xlsSex)?"0":"1";
									 }
								 }
							 }
							 else if (idNumber.length() == 15 || idNumber.length() == 18){
								 String lastValue = idNumber.substring(idNumber.length() - 2, idNumber.length()-1);
								 System.out.println(lastValue);
								 sex = Integer.parseInt(lastValue)%2 == 0?"0":"1";
							 }
							 
							 if (sheet.findCell("KD_ADDRESS")!=null){
							     address = sheet.getCell(sheet.findCell("KD_ADDRESS").getColumn(),i).getContents();//��ַ
                             }else{
                                 address=null;
                             }

						     //��Excel�ļ���ȡ�����ݴ���BUSI_CHILDREN_INFO���ݿ��
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
						    	 bvals.add(UserManage.PROFESSOR_TYPE);//ר��
						     }else if("ImportHeadMaster".equals(doc_flag)){
						    	 bvals.add(UserManage.MASTER_TYPE);//У��
						     }else if("ImportPersonalLeader".equals(doc_flag)){
                                 bvals.add(UserManage.PERSONNEL_LEADER);//���¸ɲ�
                             }else if("ImportLeader".equals(doc_flag)){
                                 bvals.add(UserManage.LEADER_TYPE);//�쵼
                             }
						     bvals.add("1");
						     bvals.add("1");
						     bvals.add(sex);
						     
						     pdao.setSql(strSQL);
						     pdao.setBindValues(bvals);
						     pdao.executeTransactionSql();
						     xlsRowCount++;
						     
					    }
                        //��Ч��¼
						StringBuffer strQuery = new StringBuffer();
						strQuery.append("select count(t1.userid) as usercount from temp_pcmc_user t1");
						strQuery.append(" where not exists (select null from pcmc_user where usercode = t1.usercode)");
						ArrayList bvals = new ArrayList();
						Element rec = DaoUtil.getOneRecord(strQuery.toString(), bvals);
						String usercount = rec.getChildText("usercount");
						
						//д����ʽ�� �����˼����temp_pcmc_user�����м�¼����û����pcmc_user�м�¼����pcmc_user state����9 �ļ�¼����
						StringBuffer strSQLBuff = new StringBuffer();
						strSQLBuff.append("insert into pcmc_user (userid,usercode,username,nickname,userpwd,portrait,description,idnumber,email,phone,mobile,emailbind,mobilebind,gender,usertype,usersource,modifydt,state)");
						strSQLBuff.append("SELECT userid,usercode,username,'',userpwd ")
							.append(",portrait,description,idnumber,email,phone,mobile,emailbind,'1' as mobilebind,gender,usertype,usersource,modifydt,state ")
							.append("FROM temp_pcmc_user t1 where ")
							.append("not exists (select null from pcmc_user where state <>'9' and usercode = t1.usercode) ");
						pdao.setSql(strSQLBuff.toString());
						pdao.executeTransactionSql();
						if("ImportProfessor".equals(doc_flag)){
							//ͬʱд��ר����Ϣ��
							StringBuffer strsSQL = new StringBuffer();
							strsSQL.append("insert into headmaster_professor_info (id,userid,usercode,username,user_sex,idnumber,deptid,mobile,address,create_by,create_date,valid) ")
								.append(" select SUBSTR(sm.userid,15,LENGTH(sm.userid)) AS 'id',sm.userid,sm.usercode,sm.username,sm.gender,sm.idnumber,'").append(deptid).append("' as deptid,")
								.append(" sm.mobile,t1.address,'").append(curUsercode).append("' as 'create_by','").append(DatetimeUtil.getNow()).append("' as 'create_date',")
								.append("'1' as 'valid' ")
								.append(" from temp_pcmc_user t1,pcmc_user sm where t1.usercode = sm.usercode and not exists (select null from headmaster_professor_info where valid='Y' and userid = sm.userid) ");
							pdao.setSql(strsSQL.toString());
							pdao.executeTransactionSql();
						} else {
							//ͬʱд���ʦ��Ϣ��
							StringBuffer strsSQL = new StringBuffer();
							strsSQL.append("insert into base_teacher_info (teacher_id,userid,ctid,preflag,sfyjszgz,bzqk,sfqrzsf,mfshs,jsgw,valid,createdate) ")
								.append(" select SUBSTR(sm.userid,15,LENGTH(sm.userid)) AS 'teacher_id' ")
								.append(",sm.userid,'1' as ctid,true as preflag,true as sfyjszgz,'1' as bzqk,true as sfqrzsf,true as mfshs,true as jsgw,'Y' as valid,'")
								.append(DatetimeUtil.getNow()).append("' as createdate")
								.append(" from temp_pcmc_user t1,pcmc_user sm where t1.usercode = sm.usercode and not exists (select null from base_teacher_info where valid='Y' and userid = sm.userid) GROUP BY sm.usercode ");
							pdao.setSql(strsSQL.toString());
							pdao.executeTransactionSql();
						
						}
						//д���û�������λ��
						StringBuffer strDeptSQL = new StringBuffer();
						strDeptSQL.append("insert into pcmc_user_dept ")
							.append("select sm.userid AS 'userdeptid','").append(deptid).append("' as 'deptid',sm.userid,")
							.append("'1' as 'state','").append(DatetimeUtil.getNow()).append("' as 'in date',Null as 'outdate' ")
							.append(" from temp_pcmc_user t1,pcmc_user sm where t1.usercode = sm.usercode and not exists (select null from pcmc_user_dept where userid = sm.userid)");
						pdao.setSql(strDeptSQL.toString());
						pdao.executeTransactionSql();
						//д���û���չ���Ա�
						StringBuffer userExtSQL = new StringBuffer();
						userExtSQL.append("insert into pcmc_user_ext ")
							.append("select sm.userid,Null as 'birthday','").append(curUsercode)
							.append("' as 'createuser',Null as 'pubname',Null as 'pubmail',Null as 'pubphone',")
							.append(" Null as 'pubbirthday',Null as 'remark' ")
							.append(" from temp_pcmc_user t1,pcmc_user sm where t1.usercode = sm.usercode and not exists (select null from pcmc_user_ext where userid = sm.userid)");
						pdao.setSql(userExtSQL.toString());
						pdao.executeTransactionSql();
						
						//�޸�Excle�ļ�����״̬
						StringBuffer updateSQL = new StringBuffer("update base_doc set doc_status='Y',doc_remark='Excel��¼��["+xlsRowCount+"]��,����ɹ�"+usercount+"��' "); 
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
			//��ʽ�����
			StringBuffer errSQL= new StringBuffer("update base_doc set doc_status=?,doc_remark=?");
			errSQL.append(" where doc_id='"+doc_id+"'");
			errSQL.append(" and doc_file_url='"+fileURL+"'");
			
			ArrayList bvals = new ArrayList();
			bvals.add("E");
			if (StringUtil.isNotEmpty(errMng)){
				errMng ="δ֪����";
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
			//ɾ����ʱ��
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
