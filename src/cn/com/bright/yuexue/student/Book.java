package cn.com.bright.yuexue.student;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;

import cn.brightcom.jraf.conf.BrightComConfig;
import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.db.SqlTypes;
import cn.brightcom.jraf.util.ConfigDocument;
import cn.brightcom.jraf.util.Constants;
import cn.brightcom.jraf.util.DatetimeUtil;
import cn.brightcom.jraf.util.Log;
import cn.brightcom.jraf.util.StringUtil;
import cn.brightcom.jraf.util.XmlDocPkgUtil;
import cn.com.bright.yuexue.util.AttachmentUtil;

/**
 * <p>Title:书本阅读管理</p>
 * <p>Description: 书本阅读管理</p>
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: BrightCom Technologies.</p>
 * @author zhangxq
 * @version 1.0
 * 
 * <p>Function List // 主要函数及其功能
 * 
 *      
 * <p>History: // 历史修改记录:</p>
 * <p> author    time             version      desc</p>
 * <p> zhangxq    2014/09/15       1.0          build this moudle </p>
 *     
 */
public class Book {
	private XmlDocPkgUtil xmlDocUtil = null;
	private Log log4j = new Log(this.getClass().toString());
	private String bookLogLevel = (String) BrightComConfig.getConfiguration().getProperty("book.log_level");
    /**
     * 动态委派入口
     * @param request xmlDoc 
     * @return response xmlDoc
     */	
	public Document doPost(Document xmlDoc) {
		xmlDocUtil = new XmlDocPkgUtil(xmlDoc);
		String action = xmlDocUtil.getAction();
		if ("saveBookRead".equals(action)){
			saveBookRead();
		}
		else if ("getBookComment".equals(action)){
			getBookComment();
		}
		else if ("saveBookComment".equals(action)){
			saveBookComment();
		}
		else if ("uploadBook".equals(action)){
			uploadBook();
		}
		else if ("recommendBook".equals(action)){
			recommendBook();
		}
		else if ("addCommPaper".equals(action)){
			addCommPaper();
		}
		else if ("saveBookAnnotate".equals(action)){
			saveBookAnnotate();
		}
		else if ("shareBookAnnotate".equals(action)){
			shareBookAnnotate();
		}		
		return xmlDoc;
	}
	/**
	 * 将试卷加入注释
	 */	
	public void addCommPaper(){
		Element reqElement =  xmlDocUtil.getRequestData();
		String book_comment_id=reqElement.getChildText("book_comment_id");
		String receiver_names=reqElement.getChildText("receiver_names");
		String receiver_ids=reqElement.getChildText("receiver_ids");
		List paperList=reqElement.getChildren("paper_id");
		
		PlatformDao pdao = new PlatformDao();
		try {	
			pdao.beginTransaction();
			for (int i=0;i<paperList.size();i++){				
				Element bookPaperRec = ConfigDocument.createRecordElement("yuexue","learn_book_paper");
				XmlDocPkgUtil.copyValues(reqElement, bookPaperRec,i,true);	
				XmlDocPkgUtil.setChildText(bookPaperRec, "book_comment_id", book_comment_id);
				XmlDocPkgUtil.setChildText(bookPaperRec, "valid", "Y");
				XmlDocPkgUtil.setChildText(bookPaperRec, "receiver_names", receiver_names);
				XmlDocPkgUtil.setChildText(bookPaperRec, "receiver_ids", receiver_ids);
				String bookPaperID = pdao.insertOneRecordSeqPk(bookPaperRec).toString();
				//写入试卷发送记录
				Element paperSendRec = ConfigDocument.createRecordElement("yuexue","learn_paper_send");
				XmlDocPkgUtil.copyValues(bookPaperRec, paperSendRec,0,true);
				XmlDocPkgUtil.setChildText(paperSendRec, "send_id",bookPaperID);
				XmlDocPkgUtil.setChildText(paperSendRec, "public_status", "1");
				XmlDocPkgUtil.setChildText(paperSendRec, "is_delayed", "Y");
				XmlDocPkgUtil.setChildText(paperSendRec, "send_time", DatetimeUtil.getNow(""));
				pdao.insertOneRecordSeqPk(paperSendRec);				
			}
			pdao.commitTransaction();
		    xmlDocUtil.setResult("0");	
		    xmlDocUtil.writeHintMsg("00109", "试卷加入成功!");			
		}
		catch (Exception e) {
			pdao.rollBack();
			e.printStackTrace();
			log4j.logError("[书本注释管理-关联试卷]"+e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);            			
		} finally {
			pdao.releaseConnection();
		}		
	}	
	/**
	 * 推荐书本
	 */	
	public void recommendBook(){
		Element reqElement =  xmlDocUtil.getRequestData();
		String book_attachment_id=reqElement.getChildText("book_attachment_id");
		PlatformDao pdao = new PlatformDao();
		try {
			ArrayList<Object> paramList = new ArrayList<Object>();
			StringBuffer strSQL = new StringBuffer();
			strSQL.append("select * from base_book_attachment");
			strSQL.append(" where book_attachment_id=?");
			paramList.add(book_attachment_id);
			
			pdao.setSql(strSQL.toString());
			pdao.setBindValues(paramList);	
			
			Element result = pdao.executeQuerySql(0,-1);
			List list = result.getChildren("Record");
			for (int i=0;i<list.size();i++){
				Element bookAttachRec = (Element)list.get(i);
				bookAttachRec.removeChild("book_attachment_id");
				bookAttachRec.removeChild("userid");
				bookAttachRec.removeChild("create_by");
				bookAttachRec.removeChild("create_date");
				Element newBookAttachRec = ConfigDocument.createRecordElement("yuexue","base_book_attachment");
				XmlDocPkgUtil.copyValues(bookAttachRec, newBookAttachRec, 0 , true);
				pdao.insertOneRecordSeqPk(newBookAttachRec);
			}
		    xmlDocUtil.setResult("0");	
		    xmlDocUtil.writeHintMsg("00109", "推荐成功!");			
		}
		catch (Exception e) {
			e.printStackTrace();
			log4j.logError("[书本管理-推荐书本]"+e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);            			
		} finally {
			pdao.releaseConnection();
		}		
	}
	/**
	 * 上传书本
	 */
	public void uploadBook(){
		String username = xmlDocUtil.getSession().getChildTextTrim("username");
		Element reqElement =  xmlDocUtil.getRequestData();
		Element book_attachment =  reqElement.getChild("book_attachment");
		String  attachment_id = reqElement.getChildText("attachment_id");
		String book_attachment_id="";
		PlatformDao pdao = new PlatformDao();
		try {
			pdao.beginTransaction();
			if (book_attachment!=null){
				if (StringUtil.isEmpty(attachment_id)){
					attachment_id = AttachmentUtil.moveFile(pdao, book_attachment, "book");
					Element bookAttachmentRec = ConfigDocument.createRecordElement("yuexue","base_book_attachment");
					XmlDocPkgUtil.copyValues(reqElement, bookAttachmentRec, 0 , true);
					XmlDocPkgUtil.setChildText(bookAttachmentRec, "attachment_id", attachment_id);
					book_attachment_id=pdao.insertOneRecordSeqPk(bookAttachmentRec).toString();
					
					Element myBookRec = ConfigDocument.createRecordElement("yuexue","learn_my_book");
					XmlDocPkgUtil.copyValues(reqElement, myBookRec, 0 , true);
					XmlDocPkgUtil.setChildText(myBookRec, "attachment_id", attachment_id);
					XmlDocPkgUtil.setChildText(myBookRec, "book_attachment_id", book_attachment_id);
					pdao.insertOneRecordSeqPk(myBookRec);
				}
				else{//增加章节
					AttachmentUtil.repalceFile(pdao, book_attachment, "book",attachment_id,username);
				}
			}
			pdao.commitTransaction();
		    String[] returnData = { "book_attachment_id"};
		    Element resData = XmlDocPkgUtil.createMetaData(returnData);	
		    resData.addContent(XmlDocPkgUtil.createRecord(returnData,new String[] {book_attachment_id}));
		    xmlDocUtil.getResponse().addContent(resData);	
		   
		    xmlDocUtil.setResult("0");	
		    xmlDocUtil.writeHintMsg("00109", "上传成功!");			
		}
		catch (Exception e) {
			pdao.rollBack();
			e.printStackTrace();
			log4j.logError("[书本管理-上传书本]"+e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);            			
		} finally {
			pdao.releaseConnection();
		}	
	}
	/**
	 * 获取教师注释
	 */
	public void getBookComment(){
		Element reqElement =  xmlDocUtil.getRequestData();
		String userid = xmlDocUtil.getSession().getChildTextTrim("userid");
		String usertype = xmlDocUtil.getSession().getChildTextTrim("usertype");//用户类型
		String attachment_id = reqElement.getChildText("attachment_id");
		String page_num = reqElement.getChildText("page_num");
		String subject_id = reqElement.getChildText("subject_id");
		
		StringBuffer paperSQL = new StringBuffer();
		if ("1".equals(usertype)){
			paperSQL.append("select t3.create_by as teacher_name,t3.my_examination_id,t3.send_id,t2.paper_id,t2.resource_type,getParamDesc('c_resource_type',t2.resource_type) as resource_type_desc,t2.paper_name");
			paperSQL.append(" from learn_book_paper t1,learn_examination_paper t2,learn_my_examination t3");
			paperSQL.append(" where t1.valid='Y' and t3.valid='Y' and t1.paper_id = t2.paper_id and  t1.paper_id = t3.paper_id");		
			paperSQL.append(" and t3.userid='"+userid+"' and t1.book_comment_id = ");
		}
		else{
			paperSQL.append("select t1.book_paper_id,t2.paper_id,t2.resource_type,getParamDesc('c_resource_type',t2.resource_type) as resource_type_desc,t2.paper_name,t1.receiver_names");
			paperSQL.append(" from learn_book_paper t1,learn_examination_paper t2");
			paperSQL.append(" where t1.valid='Y' and t1.paper_id = t2.paper_id");		
			paperSQL.append(" and t1.book_comment_id = ");			
		}
		StringBuffer attachSQL = new StringBuffer();
		attachSQL.append("select t1.comment_attach_id as file_id,t1.book_comment_id,t2.*");
		attachSQL.append(" from learn_book_comment_attachment t1,learn_attachment t2");
		attachSQL.append(" where t1.attachment_id=t2.attachment_id");
		attachSQL.append(" and t1.book_comment_id = ");
		
		PlatformDao pdao = new PlatformDao();
		try {
			ArrayList<Object> paramList = new ArrayList<Object>();
			StringBuffer strSQL = new StringBuffer();
			if ("1".equals(usertype)){
				strSQL.append("select t1.*,(select count(book_paper_id) from learn_book_paper st");
				strSQL.append("            where st.valid='Y' and st.book_comment_id=t1.book_comment_id) as paper_count,");
				strSQL.append("            (select count(sd.attachment_id) from learn_book_comment_attachment sd");
				strSQL.append("            where sd.book_comment_id=t1.book_comment_id) as attachment_count");				
				strSQL.append(" from learn_book_comment t1,base_studentinfo t2,base_teacher_subject t3");
				strSQL.append(" where t1.userid = t3.userid and  t2.classid=t3.classid and t1.valid='Y'");
				strSQL.append(" and  t3.state >'0' and  t2.state >'0'");
				strSQL.append(" and  t2.userid=? and  t3.subjectid=?");
				strSQL.append(" and t1.attachment_id=? and  t1.attach_page_num=?");
				paramList.add(userid);
				paramList.add(subject_id);
				paramList.add(attachment_id);
				paramList.add(page_num);
			}
			else{
				strSQL.append("select t1.*,(select count(book_paper_id) from learn_book_paper st");
				strSQL.append("            where st.valid='Y' and st.book_comment_id=t1.book_comment_id) as paper_count,");
				strSQL.append("            (select count(sd.attachment_id) from learn_book_comment_attachment sd");
				strSQL.append("            where sd.book_comment_id=t1.book_comment_id) as attachment_count");				
				strSQL.append(" from learn_book_comment t1");
				strSQL.append(" where t1.valid='Y'");				
				strSQL.append(" and  t1.userid=?");
				strSQL.append(" and t1.attachment_id=? and  t1.attach_page_num=?");
				paramList.add(userid);				
				paramList.add(attachment_id);
				paramList.add(page_num);				
			}
			
			strSQL.append(" order by t1.create_date");
			
			pdao.setSql(strSQL.toString());
			pdao.setBindValues(paramList);
			Element commetResult = pdao.executeQuerySql(0,-1);
			List list = commetResult.getChildren("Record");
			for (int i=0;i<list.size();i++){
				Element commetRec = (Element)list.get(i);
				String paper_count = commetRec.getChildText("paper_count");
				String attachment_count = commetRec.getChildText("attachment_count");
				String book_comment_id = commetRec.getChildText("book_comment_id");
				if (!"0".equals(paper_count)){
					pdao.setSql(paperSQL.toString()+"'"+book_comment_id+"'");
					Element paperResult = pdao.executeQuerySql(0,-1);
					Element Papers = new Element("Papers");
					Papers.addContent(paperResult);
					commetRec.addContent(Papers);
				}
				if (!"0".equals(attachment_count)){
					pdao.setSql(attachSQL.toString()+"'"+book_comment_id+"'");
					Element attachrResult = pdao.executeQuerySql(0,-1);
					Element Attachments = new Element("Attachments");
					Attachments.addContent(attachrResult);
					commetRec.addContent(Attachments);
				}				
			}
	    	xmlDocUtil.getResponse().addContent(commetResult);
	    	xmlDocUtil.setResult("0");	    	
		}
		catch (Exception e) {
			e.printStackTrace();			
			log4j.logError("[书本阅读-取教师注释]"+e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);             			
		} finally {
			pdao.releaseConnection();
		}		
	}
	/**
	 * 保存阅读记录
	 *
	 */
	public void saveBookRead(){
		Element reqElement =  xmlDocUtil.getRequestData();
		String read_page = reqElement.getChildText("read_page");
		String attachment_id = reqElement.getChildText("attachment_id");
		String leave_book = reqElement.getChildText("leave_book");
		String userid = xmlDocUtil.getSession().getChildTextTrim("userid");
		String username = xmlDocUtil.getSession().getChildTextTrim("username");
		
		PlatformDao pdao = new PlatformDao();
		try {
			ArrayList<Object> paramList = new ArrayList<Object>();
			StringBuffer updateSQL = new StringBuffer();
			updateSQL.append("update learn_book_read set read_page=?,modify_date=now(),modify_by='"+username+"'");
			updateSQL.append(" where userid=? and attachment_id=?");
			paramList.add(read_page);
			paramList.add(userid);
			paramList.add(attachment_id);
			
			pdao.setSql(updateSQL.toString());
			pdao.setBindValues(paramList);
			long uptRowCount = pdao.executeTransactionSql();
			if (uptRowCount==0){
				Element bookReadRec = ConfigDocument.createRecordElement("yuexue", "learn_book_read");
				XmlDocPkgUtil.copyValues(reqElement, bookReadRec, 0, true);
				pdao.insertOneRecordSeqPk(bookReadRec);
			}
			if (StringUtil.isNotEmpty(bookLogLevel) && "detail".equals(bookLogLevel)){
				//记录上一页的离开时间
				ArrayList<Object> readHisParam = new ArrayList<Object>();
				StringBuffer readHisSQL = new StringBuffer();
				readHisSQL.append("update learn_book_read_his set leave_time=?,modify_date=now(),modify_by='"+username+"'");
				readHisSQL.append(" where userid=? and attachment_id=?");
				readHisSQL.append(" and  leave_time is null");	
				readHisParam.add(SqlTypes.getConvertor("Timestamp").convert(DatetimeUtil.getNow("yyyy-MM-dd HH:mm:ss"),"yyyy-MM-dd HH:mm:ss"));
				readHisParam.add(userid);
				readHisParam.add(attachment_id);
				pdao.setSql(readHisSQL.toString());
				pdao.setBindValues(readHisParam);
				pdao.executeTransactionSql();
				
				if (StringUtil.isEmpty(leave_book) || "false".equals(leave_book)){//如果不是离开书本则记录新页面开始时间		
					Element readHisRec = ConfigDocument.createRecordElement("yuexue", "learn_book_read_his");
					XmlDocPkgUtil.copyValues(reqElement, readHisRec, 0, true);
					XmlDocPkgUtil.setChildText(readHisRec, "attachment_page", read_page);
					pdao.insertOneRecordSeqPk(readHisRec);			
				}
			}
	    	xmlDocUtil.setResult("0");	
	    	xmlDocUtil.writeHintMsg("00103", "保存读书记录成功!");			
		}
		catch (Exception e) {
			e.printStackTrace();			
			log4j.logError("[书本阅读-保存阅读记录]"+e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);             			
		} finally {
			pdao.releaseConnection();
		}		
	}
	/**
	 * 保存书本注释(教师用)
	 *
	 */
	public void saveBookComment(){
		Element reqElement =  xmlDocUtil.getRequestData();
		String username = xmlDocUtil.getSession().getChildTextTrim("username");
		String book_comment_id = reqElement.getChildText("book_comment_id");
		String attach_page_num = reqElement.getChildText("attach_page_num");
		String attachment_id = reqElement.getChildText("attachment_id");
		List draftList = reqElement.getChildren("draft_attachment");
		if (StringUtil.isEmpty(book_comment_id) && 
			StringUtil.isEmpty(attach_page_num) &&
			StringUtil.isEmpty(attachment_id)
			){
			   xmlDocUtil.writeErrorMsg("20101", "批注ID、书本附件ID和页码不能同时为空!");			
		}
		else{
			PlatformDao pdao = new PlatformDao();
			try {		
			   Element bookCommentRec = ConfigDocument.createRecordElement("yuexue", "learn_book_comment");
			   XmlDocPkgUtil.copyValues(reqElement, bookCommentRec, 0, true);
			   if (StringUtil.isEmpty(book_comment_id)){
				   book_comment_id = pdao.insertOneRecordSeqPk(bookCommentRec).toString();
			   }
			   else{
				   XmlDocPkgUtil.setChildText(bookCommentRec, "modify_by", username);
				   XmlDocPkgUtil.setChildText(bookCommentRec, "modify_date", DatetimeUtil.getNow("")); 
				   pdao.updateOneRecord(bookCommentRec); 
			   }
			   
				//保存作业草稿
				for (int i=0;i<draftList.size();i++){
					Element draftRec = (Element)draftList.get(i);
					if (draftRec!=null){
						String draft_attachment_id = AttachmentUtil.moveFile(pdao, draftRec, "book");					
						if (StringUtil.isNotEmpty(book_comment_id)){
							Element bookCommentAttachRec = ConfigDocument.createRecordElement("yuexue","learn_book_comment_attachment");
							XmlDocPkgUtil.copyValues(reqElement, bookCommentAttachRec, 0 , true);
							XmlDocPkgUtil.setChildText(bookCommentAttachRec, "attachment_id", draft_attachment_id);
							XmlDocPkgUtil.setChildText(bookCommentAttachRec, "book_comment_id", book_comment_id);
							pdao.insertOneRecordSeqPk(bookCommentAttachRec);
						}
					}
				}		   
			   String[] returnData = { "book_comment_id"};
			   Element resData = XmlDocPkgUtil.createMetaData(returnData);	
			   resData.addContent(XmlDocPkgUtil.createRecord(returnData,new String[] {book_comment_id}));
			   xmlDocUtil.getResponse().addContent(resData);	
			   
			   xmlDocUtil.setResult("0");	
			   xmlDocUtil.writeHintMsg("00109", "保存成功!");		   
			  
			}
			catch (Exception e) {
				e.printStackTrace();			
				log4j.logError("[书本阅读-保存书本注释]"+e.getMessage());
				xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);             			
			} finally {
				pdao.releaseConnection();
			}		   
		}
	}
	/**
	 * 保存书本批注(学生用)
	 *
	 */
	public void saveBookAnnotate(){
		String userid = xmlDocUtil.getSession().getChildTextTrim("usernid");
		String username = xmlDocUtil.getSession().getChildTextTrim("username");
		
		Element reqElement =  xmlDocUtil.getRequestData();
		String attach_page_num = reqElement.getChildText("attach_page_num");
		String attachment_id = reqElement.getChildText("attachment_id");
		Element draft_attachment = reqElement.getChild("draft_attachment");
		
		if (draft_attachment==null){
			xmlDocUtil.writeErrorMsg("20101", "批注附件不能为空!");
		}
		else{
			PlatformDao pdao = new PlatformDao();
			try {
			   String draft_attachment_id = AttachmentUtil.moveFile(pdao, draft_attachment, "book");
			   
			   StringBuffer strSQL = new StringBuffer();
			   strSQL.append("select book_annotate_id from learn_book_annotate ");
			   strSQL.append(" where attachment_id=? and attach_page_num=?");
			   strSQL.append(" and userid=?");
			   ArrayList<Object> paramList = new ArrayList<Object>();
			   paramList.add(attachment_id);
			   paramList.add(attach_page_num);
			   paramList.add(userid);
			   
			   pdao.setSql(strSQL.toString());
			   pdao.setBindValues(paramList);
			   Element result = pdao.executeQuerySql(-1, 0);
			   Element  dbAnnotate = result.getChild("Record");
			   
			   Element bookAnnotatrRec = ConfigDocument.createRecordElement("yuexue", "learn_book_annotate");			   		   
			   String book_annotate_id=null;
			   if (dbAnnotate==null){
				   XmlDocPkgUtil.copyValues(reqElement, bookAnnotatrRec, 0, true);	
				   XmlDocPkgUtil.setChildText(bookAnnotatrRec, "annotate_attachment_id", draft_attachment_id);
				   book_annotate_id = pdao.insertOneRecordSeqPk(bookAnnotatrRec).toString();
			   }
			   else{
				   book_annotate_id = dbAnnotate.getChildText("book_annotate_id");
				   XmlDocPkgUtil.setChildText(bookAnnotatrRec, "book_annotate_id", book_annotate_id);
				   XmlDocPkgUtil.setChildText(bookAnnotatrRec, "annotate_attachment_id", draft_attachment_id);
				   XmlDocPkgUtil.setChildText(bookAnnotatrRec, "modify_by", username);
				   XmlDocPkgUtil.setChildText(bookAnnotatrRec, "modify_date", DatetimeUtil.getNow("")); 
				   pdao.updateOneRecord(bookAnnotatrRec);				   
			   }
			   String[] returnData = { "book_annotate_id"};
			   Element resData = XmlDocPkgUtil.createMetaData(returnData);	
			   resData.addContent(XmlDocPkgUtil.createRecord(returnData,new String[] {book_annotate_id}));
			   xmlDocUtil.getResponse().addContent(resData);	
			   
			   xmlDocUtil.setResult("0");	
			   xmlDocUtil.writeHintMsg("00109", "书本批注保存成功!");				   
			}
			catch (Exception e) {
				e.printStackTrace();			
				log4j.logError("[书本阅读-保存书本批注]"+e.getMessage());
				xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);             			
			} finally {
				pdao.releaseConnection();
			}			
		}
	}
	/**
	 * 分享批注(将书本批注分享到小组/班级/教师/学生)
	 *
	 */
	public void shareBookAnnotate(){
		Element reqElement =  xmlDocUtil.getRequestData();
		String book_annotate_id = reqElement.getChildText("book_annotate_id");
		String share_type = reqElement.getChildText("share_type");
		String share_object_id = reqElement.getChildText("share_object_id");
		String share_comment = reqElement.getChildText("share_comment");
		
		PlatformDao pdao = new PlatformDao();
		try {
			StringBuffer strSQL = new StringBuffer();
			strSQL.append("select t1.attachment_id,t1.attach_page_num,t1.annotate_attachment_id,t2.file_name");
			strSQL.append(" from learn_book_annotate t1,learn_attachment t2");
			strSQL.append(" where t1.attachment_id=t2.attachment_id and t1.book_annotate_id=?");
			ArrayList<Object> paramList = new ArrayList<Object>();
			paramList.add(book_annotate_id);
			
			pdao.setSql(strSQL.toString());
			pdao.setBindValues(paramList);
			Element result = pdao.executeQuerySql(-1, 0);
			Element  dbAnnotate = result.getChild("Record");			
			
			Element commentRec = ConfigDocument.createRecordElement("yuexue", "learn_comment");
			XmlDocPkgUtil.setChildText(commentRec, "attachment_id", dbAnnotate.getChildText("attachment_id"));
			if ("class".equals(share_type)){
			    XmlDocPkgUtil.setChildText(commentRec, "classid", share_object_id);
			}
			else if ("group".equals(share_type)){
				XmlDocPkgUtil.setChildText(commentRec, "group_id", share_object_id);
			}
			else if ("user".equals(share_type)){
				XmlDocPkgUtil.setChildText(commentRec, "receive_id", share_object_id);
			}
			if (StringUtil.isNotEmpty(share_comment)){
				XmlDocPkgUtil.setChildText(commentRec, "comment", share_comment);
			}
			else{
				XmlDocPkgUtil.setChildText(commentRec, "comment", dbAnnotate.getChildText("file_name")+";第["+dbAnnotate.getChildText("attach_page_num")+"]页");
			}
			String comment_id = pdao.insertOneRecordSeqPk(commentRec).toString();
			
			Element commentAttachRec = ConfigDocument.createRecordElement("yuexue", "learn_comment_attachment");
			XmlDocPkgUtil.setChildText(commentAttachRec, "attachment_id", dbAnnotate.getChildText("annotate_attachment_id"));
			XmlDocPkgUtil.setChildText(commentRec, "comment_id", comment_id);

		   String[] returnData = { "comment_id"};
		   Element resData = XmlDocPkgUtil.createMetaData(returnData);	
		   resData.addContent(XmlDocPkgUtil.createRecord(returnData,new String[] {comment_id}));
		   xmlDocUtil.getResponse().addContent(resData);	
		   
		   xmlDocUtil.setResult("0");	
		   xmlDocUtil.writeHintMsg("00109", "书本批注分享成功!");				
		}
		catch (Exception e) {
			e.printStackTrace();			
			log4j.logError("[书本阅读-分享书本批注]"+e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);             			
		} finally {
			pdao.releaseConnection();
		}		
	}
}
