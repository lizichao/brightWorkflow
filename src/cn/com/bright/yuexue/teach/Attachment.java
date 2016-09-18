package cn.com.bright.yuexue.teach;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.jdom.Document;
import org.jdom.Element;

import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.util.ConfigDocument;
import cn.brightcom.jraf.util.Constants;
import cn.brightcom.jraf.util.DatetimeUtil;
import cn.brightcom.jraf.util.FileUtil;
import cn.brightcom.jraf.util.Log;
import cn.brightcom.jraf.util.StringUtil;
import cn.brightcom.jraf.util.XmlDocPkgUtil;
import cn.com.bright.yuexue.util.AttachmentUtil;

/**
 * <p>Title:��������</p>
 * <p>Description: ��������</p>
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
 * <p> zhangxq    2014/08/26       1.0          build this moudle </p>
 *     
 */
public class Attachment {
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
		
		if ("getContent".equals(action)){
			getContent();
		}
		else if ("saveContent".equals(action)){
			saveContent();
		}
		else if ("getResource".equals(action)){
			getResource();
		}
		else if ("delResource".equals(action)){
			delResource();
		}	
		else if ("getRsAttachInfo".equals(action)){
			getRsAttachInfo();
		}	
		else if ("saveResource".equals(action)){
			saveResource();
		}
		else if ("addMyResource".equals(action)){
			addMyResource();
		}
		else if ("getBuyRsLog".equals(action)){
			getBuyRsLog();
		}
		else if ("getResourceByUserid".equals(action)){
			getResourceByUserid();
		}
		else if ("getResourceByUser".equals(action)){
			getResourceByUser();
		}
		else if ("getAttachmentByusierId".equals(action)){
			getAttachmentByusierId();
		}
		return xmlDoc;
	}
	private void getResourceByUserid() {
		Element reqElement =  xmlDocUtil.getRequestData();
		String contract_tid = reqElement.getChildTextTrim("userid");
		String PageNo = reqElement.getChildTextTrim("PageNo");
		String buyuser_id = xmlDocUtil.getSession().getChildText("userid");
		ArrayList<Object> paramList = new ArrayList<Object>();	
		String addSql ="";
		if (StringUtil.isNotEmpty(buyuser_id)) {
			paramList.add(buyuser_id);
			addSql = "(SELECT COUNT(*) from  learn_my_resource s WHERE s.resource_id=t1.resource_id and s.user_id=?) as isbuy,";
		}
		paramList.add(contract_tid);
		PlatformDao pdao = new PlatformDao();
		try {
	    	pdao.setSql("select " +addSql+
	    			"t1.*,t3.username from learn_resource t1,busi_contract_teacher t2,pcmc_user t3 where t2.teacher_id = t1.userid and t2.teacher_id = t3.userid and t2.contract_tid = ? and t1.valid='Y' and t1.status='0' ");	    	
	    	pdao.setBindValues(paramList);
	    	Element result = pdao.executeQuerySql(0,Integer.parseInt(PageNo));
			xmlDocUtil.getResponse().addContent(result);			
			xmlDocUtil.setResult("0");						
		}
		catch (Exception e) {
			e.printStackTrace();
			log4j.logError("[��Դ������-ȡ��Դ������]"+e.getMessage());
			xmlDocUtil.writeHintMsg("��Դ������-ȡ��Դ������ʧ��!");
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);  			
		} finally {
			pdao.releaseConnection();
		}
		
	}
	/**
	 * ���渽������
	 *
	 */
	public void saveContent(){
		Element reqElement =  xmlDocUtil.getRequestData();
		
		String attachment_id = reqElement.getChildText("attachment_id");
		String attachment_content = reqElement.getChildText("attachment_content");
		
		ArrayList<Object> paramList = new ArrayList<Object>();	
		paramList.add(attachment_id);
		
		PlatformDao pdao = new PlatformDao();
		try {
			
	    	pdao.setSql("select * from learn_attachment where attachment_id=?");	    	
	    	pdao.setBindValues(paramList);
	    	Element result = pdao.executeQuerySql(0,-1);
	    	List attachmentList = result.getChildren("Record");
	    	for (int i=0;i<attachmentList.size();i++){
	    		Element attachmentRec = (Element)attachmentList.get(i);
	    		String access_path = attachmentRec.getChildText("access_path");
	    		if (attachment_content.indexOf("mathquill.css")<0){
	    			String mathquillcss  = "<link href=\"/ueditor/JME/mathquill/mathquill.css\" rel=\"stylesheet\" type=\"text/css\"/>";	    			
	    			attachment_content = mathquillcss+attachment_content;	    			
	    		}
				FileWriter fw = new FileWriter(FileUtil.getWebPath()+access_path);			
				fw.write(attachment_content);
				fw.flush();
				fw.close();	    		
	    	}
						
			xmlDocUtil.setResult("0");						
		}
		catch (Exception e) {
			e.printStackTrace();
			log4j.logError("[��������-ȡ��������]"+e.getMessage());
			xmlDocUtil.writeHintMsg("��������-ȡ��������ʧ��!");
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);  			
		} finally {
			pdao.releaseConnection();
		}		
	}
	/**
	 * ��ȡ�û��ϴ��ĸ���
	 */
	public void getAttachmentByusierId(){
		Element reqElement = xmlDocUtil.getRequestData();
		//String userid = xmlDocUtil.getSession().getChildText("userid");		
		String openId = reqElement.getChildText("open_id");
		ArrayList<Object> paramList = new ArrayList<Object>();	
		PlatformDao pdao = new PlatformDao();
		try {		
	    	pdao.setSql("SELECT * from learn_attachment t1 where t1.valid='Y' and t1.userid=? ORDER BY t1.create_date desc ");	
	    	paramList.add(openId);
	    	pdao.setBindValues(paramList);
	    	Element result = pdao.executeQuerySql(5, xmlDocUtil.getPageNo());
			xmlDocUtil.getResponse().addContent(result);			
			xmlDocUtil.setResult("0");						
		}
		catch (Exception e) {
			e.printStackTrace();
			log4j.logError("[��Դ����-ȡ��Դ����]"+e.getMessage());
			xmlDocUtil.writeHintMsg("��Դ����-ȡ��Դ����ʧ��!");
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);  			
		} finally {
			pdao.releaseConnection();
		}
	}
	/**
	 * �޸���Դ��
	 */
	public void delResource(){
		Element reqElement = xmlDocUtil.getRequestData();
		String username = xmlDocUtil.getSession().getChildText("username");
		String resource_id = reqElement.getChildText("resource_id");
		String status = reqElement.getChildText("status");
		String[] returnData = { "resource_id" };
		Element resData = XmlDocPkgUtil.createMetaData(returnData);

		PlatformDao pdao = new PlatformDao();
		try {
			pdao.beginTransaction();

			StringBuffer strSQL = new StringBuffer();
			ArrayList<Object> paramList = new ArrayList<Object>();

			strSQL.append(" update learn_resource  ");
			if(StringUtil.isNotEmpty(status)){
				strSQL.append(" set status = ?,modify_date=now(),");
				paramList.add(status);
			}else{
				strSQL.append(" set valid='N',modify_date=now(),");
			}
			strSQL.append(" modify_by='"+username+"'");
			strSQL.append(" where resource_id = ? ");
			paramList.add(resource_id);

			pdao.setSql(strSQL.toString());
			pdao.setBindValues(paramList); 
			pdao.executeTransactionSql();

			resData.addContent(XmlDocPkgUtil.createRecord(returnData, new String[] { resource_id }));
			pdao.commitTransaction();

			xmlDocUtil.getResponse().addContent(resData);
			xmlDocUtil.setResult("0");
			xmlDocUtil.writeHintMsg("600112", "������Դ���ɹ���");
		} catch (Exception e) {
			pdao.rollBack();
			e.printStackTrace();
			log4j.logError("[��Դ������-������Դ��]" + e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
		} finally {
			pdao.releaseConnection();
		}
	}
	/**
	 * �����Դ��
	 */
	public void saveResource(){
		String username   = xmlDocUtil.getSession().getChildTextTrim("username");
		Element reqElement =  xmlDocUtil.getRequestData();
		String resource_id = reqElement.getChildText("resource_id");
		String resource_name = reqElement.getChildText("resource_name");
		String class_info = reqElement.getChildText("class_info");
		String class_counts = reqElement.getChildText("class_counts");
		String class_price = reqElement.getChildText("class_price");
		String class_pic = reqElement.getChildText("class_pic");
		//����
		List coverList = reqElement.getChildren("cover_attachment");
		PlatformDao pdao = new PlatformDao();
		try {		
			pdao.beginTransaction();
			if(class_pic==null){
				//����·��
				if (coverList.size() > 0) {
					Element coverPathRec = (Element) coverList.get(0);
					class_pic = getUploadFilePath(coverPathRec, "video");
				}
			}
			Element commentRec = ConfigDocument.createRecordElement("yuexue", "learn_resource");
			XmlDocPkgUtil.copyValues(reqElement, commentRec, 0, true);
			
			XmlDocPkgUtil.setChildText(commentRec, "resource_id",resource_id);
			XmlDocPkgUtil.setChildText(commentRec, "resource_name",URLDecoder.decode(resource_name,"utf-8"));
			XmlDocPkgUtil.setChildText(commentRec, "class_info",URLDecoder.decode(class_info,"utf-8"));
			XmlDocPkgUtil.setChildText(commentRec, "class_counts",class_counts);
			XmlDocPkgUtil.setChildText(commentRec, "class_price",class_price);
			XmlDocPkgUtil.setChildText(commentRec, "class_pic",class_pic);
			XmlDocPkgUtil.setChildText(commentRec, "valid","Y");
			
			if(StringUtil.isNotEmpty(resource_id)){				
				XmlDocPkgUtil.setChildText(commentRec, "modify_by", username);
				XmlDocPkgUtil.setChildText(commentRec, "modify_date", DatetimeUtil.getNow(""));					
				pdao.updateOneRecord(commentRec);
			}else{
				pdao.insertOneRecordSeqPk(commentRec).toString();
			}
			pdao.commitTransaction();
			xmlDocUtil.setResult("0");
		}
		catch (Exception e) {
			pdao.rollBack();
			e.printStackTrace();			
			log4j.logError("[��Դ������-�����Դ��]"+e.getMessage());
			xmlDocUtil.writeHintMsg("��Դ������-�����Դ��ʧ��");
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
		} finally {
			pdao.releaseConnection();
		}
	}
	/**
	 * ��ȡ������Դ����¼
	 */
	public void getBuyRsLog(){
		Element reqElement =  xmlDocUtil.getRequestData();
		String resource_id = reqElement.getChildText("resource_id");
		ArrayList<Object> paramList = new ArrayList<Object>();	
		PlatformDao pdao = new PlatformDao();
		try {
			StringBuffer praxesSQL = new StringBuffer();
			praxesSQL.append(" select * from learn_my_resource t1 where t1.valid='Y' and t1.resource_id=? ");
			paramList.add(resource_id);
		    praxesSQL.append(" ORDER BY t1.create_date DESC ");	
				
	    	pdao.setSql(praxesSQL.toString());	   	
	    	pdao.setBindValues(paramList);
	    	Element result = pdao.executeQuerySql(xmlDocUtil.getPageSize(), xmlDocUtil.getPageNo());
			xmlDocUtil.getResponse().addContent(result);			
			xmlDocUtil.setResult("0");						
		}
		catch (Exception e) {
			e.printStackTrace();
			log4j.logError("[��Դ������-��ȡ������Դ����¼]"+e.getMessage());
			xmlDocUtil.writeHintMsg("��Դ������-ȡ��Դ����¼ʧ��!");
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);  			
		} finally {
			pdao.releaseConnection();
		}
		
	}
	/** �����ҵ��YԴ�� */
	public void addMyResource() {
		Element reqElement =  xmlDocUtil.getRequestData();
		String userid   = xmlDocUtil.getSession().getChildTextTrim("userid");
		String resource_id = reqElement.getChildText("resource_id");
		String class_price = reqElement.getChildText("class_price");
		PlatformDao pdao = new PlatformDao();
		try {		
			//��ѯ�û����
			pdao.setSql("select money_num from pcmc_user WHERE userid ='"+userid+"' ");
	    	Element result = pdao.executeQuerySql(1, 1);
	    	List ls = result.getChildren("Record");
	    	Double my_price = 
	    			(ls.size()==0 || StringUtil.isEmpty(((Element)ls.get(0)).getChildText("money_num")) 
				? 0 : Double.parseDouble(((Element)ls.get(0)).getChildText("money_num")));
			if (my_price < Double.parseDouble(class_price)) {
				xmlDocUtil.writeErrorMsg("10642", "����");
				return;
			}
			my_price = my_price - Double.parseDouble(class_price);
			pdao.beginTransaction();
			//�û����䶯
			String SQL = "UPDATE pcmc_user set money_num = ? WHERE userid = ? ";
			pdao.setSql(SQL);
	        ArrayList<Object> li = new ArrayList<Object>();
	        li.add(my_price);
	        li.add(userid);
	        pdao.setBindValues(li);
	        pdao.executeTransactionSql();
			Element commentRec = ConfigDocument.createRecordElement("yuexue", "learn_my_resource");
			XmlDocPkgUtil.copyValues(reqElement, commentRec, 0, true);
			XmlDocPkgUtil.setChildText(commentRec, "resource_id",resource_id);
			XmlDocPkgUtil.setChildText(commentRec, "user_id",userid);
			XmlDocPkgUtil.setChildText(commentRec, "class_price",class_price);
			pdao.insertOneRecordSeqPk(commentRec).toString();
			pdao.commitTransaction();			
			xmlDocUtil.setResult("0");	
			xmlDocUtil.writeHintMsg("00103", "������Դ���ɹ�!");	
		}
		catch (Exception e) {
			pdao.rollBack();
			e.printStackTrace();			
			log4j.logError("[��Դ������-�����Դ��]"+e.getMessage());
			xmlDocUtil.writeHintMsg("��Դ������-�����Դ��ʧ��");
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
		} finally {
			pdao.releaseConnection();
		}
	} 
	public static String getUploadFilePath(Element fileElement, String destFile) throws IOException {
		String srcFile = FileUtil.getPhysicalPath(fileElement.getText());
		String desFileName = FileUtil.getFileName(srcFile);
		//ÿ��ÿ�´���һ���ļ���
		String currDate = DatetimeUtil.getCurrentDate();
		String desPath = "upload/" + destFile + "/" + currDate.substring(0, 4) + "/" + currDate.substring(5, 7) + "/";

		FileUtil.createDirs(FileUtil.getWebPath() + desPath, true);
		FileUtil.moveFile(new File(srcFile), new File(FileUtil.getWebPath() + desPath + desFileName));
		FileUtil.deleteFile(srcFile);

		return "/" + desPath + desFileName;
	}
	/**
	 * ��ȡ��Դ������ĸ�����Ϣ
	 */
	public void getRsAttachInfo(){
		Element reqElement =  xmlDocUtil.getRequestData();
		String resource_id = reqElement.getChildText("resource_id");
		ArrayList<Object> paramList = new ArrayList<Object>();	
		PlatformDao pdao = new PlatformDao();
		try {		
	    	pdao.setSql("select t1.*,t2.attachment_id,t2.file_name,t2.file_size,t2.file_type,t2.file_path,t2.video_time,t2.access_path,t2.avg_score " +
	    			"from learn_examination_paper t1,learn_paper_attachment t3,learn_attachment t2 " +
	    			"where t3.paper_id = t1.paper_id and t3.attachment_id = t2.attachment_id " +
	    			"and t1.valid='Y' and t2.valid='Y' and t3.valid='Y' " +
	    			"and  t1.resource_id=? ");	
	    	paramList.add(resource_id);
	    	pdao.setBindValues(paramList);
	    	Element result = pdao.executeQuerySql(xmlDocUtil.getPageSize(), xmlDocUtil.getPageNo());
			xmlDocUtil.getResponse().addContent(result);			
			xmlDocUtil.setResult("0");						
		}
		catch (Exception e) {
			e.printStackTrace();
			log4j.logError("[��Դ������-ȡ��Դ������]"+e.getMessage());
			xmlDocUtil.writeHintMsg("��Դ������-ȡ��Դ������ʧ��!");
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);  			
		} finally {
			pdao.releaseConnection();
		}
		
	}
	/**
	 * ��ȡ��ʦ����Դ��
	 */
	public void getResource(){
		String userid   = xmlDocUtil.getSession().getChildTextTrim("userid");
		Element reqElement =  xmlDocUtil.getRequestData();
		String resource_id = reqElement.getChildText("resource_id");
		ArrayList<Object> paramList = new ArrayList<Object>();	
		PlatformDao pdao = new PlatformDao();
		try {
			StringBuffer praxesSQL = new StringBuffer();
			praxesSQL.append(" select t6.classcount,t5.* from (select t3.buycount,t1.* from learn_resource t1   ");
			praxesSQL.append(" LEFT JOIN (select t2.resource_id,count(t2.resource_id) as buycount from learn_my_resource t2 where t2.valid='Y'  ");
			praxesSQL.append(" GROUP BY t2.resource_id) t3 ON t1.resource_id = t3.resource_id where t1.valid ='Y') t5 ");
			praxesSQL.append(" LEFT JOIN (select t4.resource_id,count(t4.resource_id) as classcount  ");
			praxesSQL.append(" from learn_examination_paper t4 where t4.valid='Y' GROUP BY t4.resource_id) t6 ");
			praxesSQL.append(" ON t5.resource_id= t6.resource_id ");
			praxesSQL.append(" where t5.valid='Y' and  t5.userid=? ");
			paramList.add(userid);
			if (StringUtil.isNotEmpty(resource_id)){
				praxesSQL.append(" and t5.resource_id=? ");
				paramList.add(resource_id);
			}
		    String orderBy = xmlDocUtil.getOrderBy();
		    if (StringUtil.isNotEmpty(orderBy)){
		    	praxesSQL.append(orderBy);
		    }
		    else{
		    	praxesSQL.append(" ORDER BY t5.status,t5.create_date desc ");
		    }	
				
	    	pdao.setSql(praxesSQL.toString());	   	
	    	pdao.setBindValues(paramList);
	    	Element result = pdao.executeQuerySql(xmlDocUtil.getPageSize(), xmlDocUtil.getPageNo());
			xmlDocUtil.getResponse().addContent(result);			
			xmlDocUtil.setResult("0");						
		}
		catch (Exception e) {
			e.printStackTrace();
			log4j.logError("[��Դ������-ȡ��Դ������]"+e.getMessage());
			xmlDocUtil.writeHintMsg("��Դ������-ȡ��Դ������ʧ��!");
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);  			
		} finally {
			pdao.releaseConnection();
		}
	}
	/**
	 * ��ȡ��������
	 *
	 */
	public void getContent(){
		Element reqElement =  xmlDocUtil.getRequestData();
		String attachment_id = reqElement.getChildText("attachment_id");
		ArrayList<Object> paramList = new ArrayList<Object>();	
		paramList.add(attachment_id);
		StringBuffer strSQL = new StringBuffer();
		PlatformDao pdao = new PlatformDao();
		try {
			strSQL.append("select t1.*,(select count(1) from learn_attachment_log t2 where t1.attachment_id=t2.attachment_id) as bfcount, ");
			strSQL.append(" (select count(DISTINCT t3.userid) from learn_attachment_log t3 where t3.attachment_id=t1.attachment_id) as glounnt, ");
			strSQL.append(" (select count(1) from learn_comment lc where t1.attachment_id= lc.attachment_id) as plcount, ");
			strSQL.append(" (select count(1) from learn_attachment_log la where la.praise='Y' and la.attachment_id=t1.attachment_id) as dzcount ");
			strSQL.append(" from learn_attachment t1 where t1.attachment_id=? ");
	    	pdao.setSql(strSQL.toString());	      	
	    	pdao.setBindValues(paramList);
	    	Element result = pdao.executeQuerySql(0,-1);
	    	List attachmentList = result.getChildren("Record");
	    	for (int i=0;i<attachmentList.size();i++){
	    		Element attachmentRec = (Element)attachmentList.get(i);
	    		String access_path = attachmentRec.getChildText("access_path");
	    		String file_type = attachmentRec.getChildText("file_type");
	    		if (StringUtil.isNotEmpty(access_path)){
	    			if ("doc".equals(file_type) || "docx".equals(file_type) || "xls".equals(file_type) || "xlsx".equals(file_type)){
	    		        String sAttachmentContent = AttachmentUtil.getFileContent(FileUtil.getWebPath()+access_path);
	    		        XmlDocPkgUtil.setChildText(attachmentRec, "attachment_content", sAttachmentContent);
	    			}
	    		}
	    	}
			xmlDocUtil.getResponse().addContent(result);			
			xmlDocUtil.setResult("0");						
		}
		catch (Exception e) {
			e.printStackTrace();
			log4j.logError("[��������-ȡ��������]"+e.getMessage());
			xmlDocUtil.writeHintMsg("��������-ȡ��������ʧ��!");
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);  			
		} finally {
			pdao.releaseConnection();
		}
	}	
	
	/** ��ȡ��Դ���б�  */
	public void getResourceByUser() {
		Element reqElement =  xmlDocUtil.getRequestData();
		String userid = reqElement.getChildText("userid");
		String user_id   = xmlDocUtil.getSession().getChildTextTrim("userid");
		ArrayList<Object> paramList = new ArrayList<Object>();	
		PlatformDao pdao = new PlatformDao();
		String addSql="";
		StringBuffer praxesSQL = new StringBuffer();
		try {
			if (StringUtil.isNotEmpty(user_id)) {
	    		addSql+="and t4.user_id = ?";
	    		paramList.add(user_id);
	    	}
			praxesSQL.append("SELECT t1.*,t3.username, " +
	    			"(select count(t4.resource_id) from learn_my_resource t4 where t4.resource_id=t1.resource_id "+addSql+") as buycount  " +
	    			"from learn_resource t1,busi_contract_teacher t2,pcmc_user t3 where t2.teacher_id = t1.userid and t2.teacher_id = t3.userid and t2.valid = 'Y' and t1.valid='Y' and t1.status='1' ");
			if (StringUtil.isNotEmpty(userid)) {
				praxesSQL.append("and t2.contract_tid = ? ");
	    		paramList.add(userid);
	    	}
			pdao.setSql(praxesSQL.toString());	
	    	pdao.setBindValues(paramList);
	    	Element result = pdao.executeQuerySql(xmlDocUtil.getPageSize(), xmlDocUtil.getPageNo());
			xmlDocUtil.getResponse().addContent(result);			
			xmlDocUtil.setResult("0");						
		}
		catch (Exception e) {
			e.printStackTrace();
			log4j.logError("[��Դ������-ȡ��Դ���б�]"+e.getMessage());
			xmlDocUtil.writeHintMsg("��Դ������-ȡ��Դ���б�ʧ��!");
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);  			
		} finally {
			pdao.releaseConnection();
		}
	}
}
