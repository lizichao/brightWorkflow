package cn.com.bright.yuexue.teach;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;

import cn.brightcom.jraf.db.DBOprProxy;
import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.util.ConfigDocument;
import cn.brightcom.jraf.util.Constants;
import cn.brightcom.jraf.util.Log;
import cn.brightcom.jraf.util.StringUtil;
import cn.brightcom.jraf.util.XmlDocPkgUtil;
import cn.com.bright.yuexue.util.AttachmentUtil;

/**
 * <p>Title:֪ʶ��</p>
 * <p>Description: �γ�/�̲�/Ŀ¼/֪ʶ��ά����</p>
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
 * <p> zhangxq    2014/07/17       1.0          build this moudle </p>
 *     
 */
public class Knowledge {
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
		
		if ("addKnowPoint".equals(action)){
			addKnowPoint();
		}
		else if ("deleteKnowPoint".equals(action)){
			deleteKnowPoint();
		}
		else if ("addBookFolder".equals(action)){
			addBookFolder();
		}
		else if ("addBookAttachment".equals(action)){
			addBookAttachment();
		}
		
		return xmlDoc;
	}
	/**
	 * ��ȡ�鱾Դ����
	 *
	 */
	public void addBookAttachment(){
		Element reqElement =  xmlDocUtil.getRequestData();
		String subject_book_id = reqElement.getChildText("subject_book_id");
		Element book_attachment = reqElement.getChild("book_attachment");
		PlatformDao pdao = new PlatformDao();
		try {
			if (book_attachment!=null){
				String attachment_id = AttachmentUtil.moveFile(pdao, book_attachment, "book");
				String fieldName  = "attachment_id";
				String fieldValue = attachment_id;
				if (StringUtil.isNotEmpty(subject_book_id)){
					Element bookAttRec = ConfigDocument.createRecordElement("yuexue","base_book_attachment");
					XmlDocPkgUtil.copyValues(reqElement, bookAttRec, 0 , true);
					XmlDocPkgUtil.setChildText(bookAttRec, "attachment_id", attachment_id);
					String book_attachment_id = pdao.insertOneRecordSeqPk(bookAttRec).toString();
					
					fieldName  = "book_attachment_id";
					fieldValue = book_attachment_id;
				}				
				String[] returnData = { fieldName};
				Element resData = XmlDocPkgUtil.createMetaData(returnData);	
				resData.addContent(XmlDocPkgUtil.createRecord(returnData,new String[] {fieldValue}));			
				xmlDocUtil.getResponse().addContent(resData);
			}
			
			xmlDocUtil.setResult("0");		
		}catch (Exception e) {
			e.printStackTrace();
			log4j.logError("[ѧ���鱾����-�ϴ�����]"+e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
		} finally {
			pdao.releaseConnection();
		}		
	}
	/**
	 * �����̲��½�
	 *
	 */
	public void addBookFolder(){
		Element reqElement =  xmlDocUtil.getRequestData();
		String folderCode = reqElement.getChildText("folder_code");
		PlatformDao pdao = new PlatformDao();
		try {
			Element bookFolderRec = ConfigDocument.createRecordElement("yuexue","base_book_folder");
			XmlDocPkgUtil.copyValues(reqElement, bookFolderRec, 0 , true);	
			
			long pkid = DBOprProxy.getNextSequenceNumber("base_book_folder");
			
			XmlDocPkgUtil.setChildText(bookFolderRec, "folder_id", String.valueOf(pkid));
			if(StringUtil.isEmpty(folderCode)){
				XmlDocPkgUtil.setChildText(bookFolderRec, "folder_code", String.valueOf(pkid));
			}else{
				XmlDocPkgUtil.setChildText(bookFolderRec, "folder_code", folderCode+","+String.valueOf(pkid));
			}
			pdao.insertOneRecord(bookFolderRec);
			
			String[] returnData = { "folder_id"};
			Element resData = XmlDocPkgUtil.createMetaData(returnData);
			resData.addContent(XmlDocPkgUtil.createRecord(returnData,new String[] {String.valueOf(pkid)}));
			
			xmlDocUtil.getResponse().addContent(resData);
			xmlDocUtil.setResult("0");	
			xmlDocUtil.writeHintMsg("00103", "�����̲��½ڳɹ�!");			
		}
		catch (Exception e) {
			e.printStackTrace();
			log4j.logError("[�̲Ĺ���-���ӽ̲��½�]"+e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
		} finally {
			pdao.releaseConnection();
		}		
	}	
	/**
	 * ����֪ʶ��
	 *
	 */
	public void addKnowPoint(){
		String userName = xmlDocUtil.getSession().getChildTextTrim("username");	
		Element reqElement =  xmlDocUtil.getRequestData();
		PlatformDao pdao = new PlatformDao();		
		try {
			pdao.beginTransaction();
			//�ж���ָ��ѧ��,�꼶��,֪ʶ���Ƿ��Ѿ�����
			String k_point_id = "";
			String kPointName = reqElement.getChildText("k_point_name");
			String subjectid  = reqElement.getChildText("subjectid");
			String grade_id  = reqElement.getChildText("grade_id");
			StringBuffer strSQL = new StringBuffer();
			strSQL.append("select k_point_id,valid from base_knowledge_point ");
			strSQL.append(" where subjectid=?");
			strSQL.append(" and k_point_name=?");
			strSQL.append(" and grade_code=?");
			ArrayList<Object> paramList = new ArrayList<Object>();
			paramList.add(subjectid);
			paramList.add(kPointName);
			paramList.add(grade_id);
			
			pdao.setSql(strSQL.toString());
			pdao.setBindValues(paramList);
			Element result = pdao.executeQuerySql(-1, 0);
			List list = result.getChildren("Record");
			if (list.size()==0){
				Element pointRecord = ConfigDocument.createRecordElement("yuexue", "base_knowledge_point");
				XmlDocPkgUtil.copyValues(reqElement, pointRecord, 0, true);
				k_point_id = pdao.insertOneRecordSeqPk(pointRecord).toString();
			}
			else{
				Element el = (Element)list.get(0);
				k_point_id = el.getChildText("k_point_id");
				String valid = el.getChildText("valid");
				if (!"Y".equals(valid)){
					StringBuffer updateSQL = new StringBuffer();
					updateSQL.append(" update base_knowledge_point set valid='Y'");
					updateSQL.append(" ,modify_by='"+userName+"',modify_date=now()");
					updateSQL.append(" where k_point_id='"+k_point_id+"'");
					pdao.setSql(updateSQL.toString());
					pdao.executeTransactionSql();
				}
			}
			Element fpRecord = ConfigDocument.createRecordElement("yuexue", "base_folder_point");
			XmlDocPkgUtil.copyValues(reqElement, fpRecord, 0, true);
			XmlDocPkgUtil.setChildText(fpRecord, "folder_id", reqElement.getChildText("folder_id"));
			XmlDocPkgUtil.setChildText(fpRecord, "k_point_id", k_point_id);
			
			String fp_id = pdao.insertOneRecordSeqPk(fpRecord).toString();	
			String[] returnData = { "k_point_id", "fp_id"};
			Element resData = XmlDocPkgUtil.createMetaData(returnData);	
			resData.addContent(XmlDocPkgUtil.createRecord(returnData,new String[] {k_point_id , fp_id}));
			
			pdao.commitTransaction();
			xmlDocUtil.getResponse().addContent(resData);
			xmlDocUtil.setResult("0");	
			xmlDocUtil.writeHintMsg("00103", "����֪ʶ��ɹ�!");		
		}
		catch (Exception e) {
			pdao.rollBack();
			e.printStackTrace();			
			log4j.logError("[֪ʶ�����-����֪ʶ��]"+e.getMessage());
			xmlDocUtil.writeHintMsg("֪ʶ�����-����֪ʶ��ʧ��!");
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
		} finally {
			pdao.releaseConnection();
		}	
	}
	/**
	 * ɾ��֪ʶ��
	 *
	 */
	public void deleteKnowPoint(){
		String userName = xmlDocUtil.getSession().getChildTextTrim("username");	
		Element reqElement =  xmlDocUtil.getRequestData();
		List fpList = reqElement.getChildren("fp_id");
		List kpList = reqElement.getChildren("k_point_id");
		
		PlatformDao pdao = new PlatformDao();		
		try {
			pdao.beginTransaction();
			StringBuffer kpSQL = new StringBuffer();
			kpSQL.append("update  base_knowledge_point set valid='N',modify_by='"+userName+"',modify_date=now()");
			kpSQL.append(" where k_point_id=?");
			kpSQL.append(" and not exists");
			kpSQL.append(" (select null from base_folder_point st");
			kpSQL.append(" where st.k_point_id = base_knowledge_point.k_point_id");
			kpSQL.append(" and st.fp_id<>?)");
			
			StringBuffer fpSQL = new StringBuffer();
			fpSQL.append("delete from base_folder_point");
			fpSQL.append(" where  fp_id=?");
			
			for (int i = 0; i < fpList.size() ;i++){
				Element fpRec = (Element)fpList.get(i);
				String  fp_id = fpRec.getText();
				
				Element kpRec = (Element)kpList.get(i);
				String  k_point_id = kpRec.getText();
			    
				ArrayList<Object> kpParam = new ArrayList<Object>();
				kpParam.add(k_point_id);
				kpParam.add(fp_id);
			    
			    pdao.setSql(kpSQL.toString());
			    pdao.setBindValues(kpParam);
			    pdao.executeTransactionSql();
			    
			    ArrayList<Object> fkParam = new ArrayList<Object>();
			    fkParam.add(fp_id);
			    pdao.setSql(fpSQL.toString());
			    pdao.setBindValues(fkParam);
			    pdao.executeTransactionSql();			    
			}
			
			pdao.commitTransaction();
			xmlDocUtil.setResult("0");
			xmlDocUtil.writeHintMsg("00107", "ɾ��֪ʶ��ɹ�!");						
		}catch (Exception e) {
			pdao.rollBack();
			e.printStackTrace();
			log4j.logError("[֪ʶ�����-ɾ��֪ʶ��]"+e.getMessage());
			xmlDocUtil.writeHintMsg("֪ʶ�����-ɾ��֪ʶ��ʧ��!");
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
		} finally {
			pdao.releaseConnection();
		}		
	}	
}
