package cn.com.bright.yuexue.student;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;

import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.util.ConfigDocument;
import cn.brightcom.jraf.util.Constants;
import cn.brightcom.jraf.util.DatetimeUtil;
import cn.brightcom.jraf.util.Log;
import cn.brightcom.jraf.util.StringUtil;
import cn.brightcom.jraf.util.XmlDocPkgUtil;
import cn.com.bright.yuexue.contact.Comment;
import cn.com.bright.yuexue.util.AttachmentUtil;

/**
 * <p>Title:智慧源</p>
 * <p>Description: 智慧源</p>
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
 * <p> zhangxq    2015/03/05       1.0          build this moudle </p>
 *     
 */
public class WisdomSource extends Comment{
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
		if ("addTaskReply".equals(action)){
			addTaskReply();
		}
		else if ("savaResultCorrect".equals(action)){
			savaResultCorrect();
		}
		else if ("saveTaskPaper".equals(action)){
			saveTaskPaper();
		}
		return xmlDoc;
	}
	/**
	 * 保存任务单
	 *
	 */
	public void saveTaskPaper(){
		String username = xmlDocUtil.getSession().getChildTextTrim("username");
		
		Element reqElement =  xmlDocUtil.getRequestData();
		String  paper_id = reqElement.getChildText("paper_id");
		String  subject_id = reqElement.getChildText("subject_id");
		String  grade_code = reqElement.getChildText("grade_code");
		String  share_level = reqElement.getChildText("share_level");
		
		Element cover_attachment = reqElement.getChild("cover_attachment");
		List praxesContentList = reqElement.getChildren("praxes_content");
		List praxesIDList = reqElement.getChildren("paper_praxes_id");
		List praxesTypeList = reqElement.getChildren("praxes_type");
		List optionCountList = reqElement.getChildren("option_count");
		
		String attachmentList=reqElement.getChildTextTrim("attachment_id");//附件ID
		
		PlatformDao pdao = new PlatformDao();
		try{
			String cover_path = AttachmentUtil.moveFile(cover_attachment, "paper");
			Element paperRec = ConfigDocument.createRecordElement("yuexue","learn_examination_paper");
			XmlDocPkgUtil.copyValues(reqElement, paperRec, 0 , true);
			if (StringUtil.isNotEmpty(cover_path)){
				XmlDocPkgUtil.setChildText(paperRec, "cover_path", cover_path);
			}
			if (StringUtil.isEmpty(paper_id) || paper_id.length()<30){
				paper_id = pdao.insertOneRecordSeqPk(paperRec).toString();
			}
			else{
				XmlDocPkgUtil.setChildText(paperRec, "modify_by", username);
				XmlDocPkgUtil.setChildText(paperRec, "modify_date", DatetimeUtil.getNow("")); 
				pdao.updateOneRecord(paperRec);
			}
			if(StringUtil.isNotEmpty(attachmentList)){
				//解除绑定信息
				pdao.setSql("update learn_paper_attachment set valid = 'N' where paper_id = ? ");
				ArrayList<String> barry = new ArrayList<String>();
				barry.add(paper_id);
				pdao.setBindValues(barry);
				pdao.executeTransactionSql();
				//绑定附件信息
				String [] attachment_Array=attachmentList.split(",");
			   for (int i = 0; i < attachment_Array.length; i++) {
				    Element attachmentRec = ConfigDocument.createRecordElement("yuexue", "learn_paper_attachment");
					XmlDocPkgUtil.setChildText(attachmentRec, "attachment_id", attachment_Array[i]);
					XmlDocPkgUtil.setChildText(attachmentRec, "paper_id", paper_id);
					XmlDocPkgUtil.setChildText(attachmentRec, "valid", "Y");
					pdao.insertOneRecordSeqPk(attachmentRec);
			   }
				
			}
			for (int i=0;i<praxesContentList.size();i++){
				//String praxes_content = ((Element)praxesContentList.get(i)).getText();
				String paper_praxes_id = ((Element)praxesIDList.get(i)).getText();
				String option_count = ((Element)optionCountList.get(i)).getText();
				String praxes_type = ((Element)praxesTypeList.get(i)).getText();
				
				Element parxesRec = ConfigDocument.createRecordElement("yuexue","learn_paper_praxes");
				XmlDocPkgUtil.copyValues(reqElement, parxesRec, i , true);
				XmlDocPkgUtil.setChildText(parxesRec, "paper_id", paper_id);
				XmlDocPkgUtil.setChildText(parxesRec, "subject_id", subject_id);
				XmlDocPkgUtil.setChildText(parxesRec, "grade_code", grade_code);
				XmlDocPkgUtil.setChildText(parxesRec, "share_level", share_level);
				XmlDocPkgUtil.setChildText(parxesRec, "display_order", ""+(i+1));
			
				if (StringUtil.isEmpty(paper_praxes_id) || paper_praxes_id.length()<30){
					parxesRec.removeChild("paper_praxes_id");
					paper_praxes_id = pdao.insertOneRecordSeqPk(parxesRec).toString();
				}
				else{
					XmlDocPkgUtil.setChildText(parxesRec, "modify_by", username);
					XmlDocPkgUtil.setChildText(parxesRec, "modify_date", DatetimeUtil.getNow("")); 
					pdao.updateOneRecord(parxesRec);
				}
				if ("20".equals(praxes_type)){//选择题 2.0版开始,选项内容都在题目内容中,选项内容全部为空
					int optionCount = Integer.parseInt(option_count);
					
					StringBuffer strSQL = new StringBuffer();
					strSQL.append(" select count(t1.paper_option_id) as option_count");
					strSQL.append(" from learn_paper_options t1 where t1.valid='Y'");
					strSQL.append(" and t1.paper_praxes_id=?");
					
					ArrayList<Object> paramList = new ArrayList<Object>();					
					paramList.add(paper_praxes_id);
					
					pdao.setSql(strSQL.toString());
					pdao.setBindValues(paramList);
					Element result = pdao.executeQuerySql(-1, 0);
					
					String db_option_count = result.getChild("Record").getChildText("option_count");
					int dbCount = 0;
					if (StringUtil.isNotEmpty(db_option_count)){
						dbCount = Integer.parseInt(db_option_count);
					}
					while (dbCount<optionCount){
						Element optionRec = ConfigDocument.createRecordElement("yuexue","learn_paper_options");
						XmlDocPkgUtil.copyValues(reqElement,optionRec,0,true);
						optionRec.removeChild("option_id");	
						//XmlDocPkgUtil.setChildText(optionRec, "option_id", "");
						XmlDocPkgUtil.setChildText(optionRec, "paper_praxes_id", paper_praxes_id);
						XmlDocPkgUtil.setChildText(optionRec, "display_order", ""+(dbCount+1));
						XmlDocPkgUtil.setChildText(optionRec, "option_content", ""+(char)(65+dbCount));
						pdao.insertOneRecordSeqPk(optionRec);						
						dbCount++;
					}
				}
			}
			
			String[] returnData = {"paper_id"};
			Element resData = XmlDocPkgUtil.createMetaData(returnData);	
			resData.addContent(XmlDocPkgUtil.createRecord(returnData,new String[] {paper_id}));
			
			xmlDocUtil.getResponse().addContent(resData);			
	    	xmlDocUtil.setResult("0");	
	    	xmlDocUtil.writeHintMsg("00103", "任务单保存成功!");				
		}
		catch (Exception e) {
			e.printStackTrace();
			log4j.logError("[智慧源-保存任务单]"+e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
		} finally {
			pdao.releaseConnection();
		}
	}
	/**
	 * 保存作业单批改
	 *
	 */
	public void savaResultCorrect(){
		Element reqElement =  xmlDocUtil.getRequestData();
		String correct_id = reqElement.getChildText("correct_id");
		Element draft_attachment = reqElement.getChild("draft_attachment");
		
		String[] returnData = {"correct_id"};
		Element resData = XmlDocPkgUtil.createMetaData(returnData);		
		PlatformDao pdao = new PlatformDao();
		try{
			String attachment_id = AttachmentUtil.moveFile(pdao, draft_attachment, "correct");
			Element examCorrentRec = ConfigDocument.createRecordElement("yuexue","learn_exam_result_correct");
			XmlDocPkgUtil.copyValues(reqElement, examCorrentRec, 0 , true);
			XmlDocPkgUtil.setChildText(examCorrentRec, "attachment_id", attachment_id);
			if (StringUtil.isEmpty(correct_id)){
				correct_id = pdao.insertOneRecordSeqPk(examCorrentRec).toString();
			}
			else{
				pdao.updateOneRecord(examCorrentRec);
			}
			resData.addContent(XmlDocPkgUtil.createRecord(returnData,new String[] {correct_id}));
			xmlDocUtil.getResponse().addContent(resData);
	    	xmlDocUtil.setResult("0");	
	    	xmlDocUtil.writeHintMsg("00103", "批改附件保存成功!");			
		}catch (Exception e) {
			e.printStackTrace();
			log4j.logError("[智慧源-保存作业单批改附件]"+e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
		} finally {
			pdao.releaseConnection();
		}
	}
	/**
	 * 增加任务单回复
	 *
	 */
	public void addTaskReply(){
		super.setXmlDocUtil(xmlDocUtil);
		super.addComment();	
		/***
		xmlDocUtil.getResponse().removeChild("Data");
		xmlDocUtil.getHint().removeChild("Msg");
		xmlDocUtil.getError().removeChild("Msg");				
		String userName = xmlDocUtil.getSession().getChildTextTrim("username");
		Element reqElement =  xmlDocUtil.getRequestData();
		String my_examination_id = reqElement.getChildText("my_examination_id");
		String paper_id = reqElement.getChildText("paper_id");
		String paper_praxes_id = reqElement.getChildText("paper_praxes_id");
		String submit_flag = reqElement.getChildText("submit_flag");
		
		List attachList = reqElement.getChildren("attachment");
		String praxes_result =  reqElement.getChildText("praxes_result");
		String commnet =  reqElement.getChildText("commnet");
		if (StringUtil.isEmpty(praxes_result) && StringUtil.isNotEmpty(commnet)){
			praxes_result = commnet;
		}
		if (StringUtil.isEmpty(praxes_result) && attachList.size()>0){
			praxes_result = "上传附件";
		}
		
		ArrayList<Object> paramList = new ArrayList<Object>();
		StringBuffer strSQL = new StringBuffer();
		strSQL.append("select result_id from learn_examination_result t1 where t1.valid='Y'");
		
		if (StringUtil.isNotEmpty(my_examination_id)){
			strSQL.append(" and t1.my_examination_id=?");
			paramList.add(my_examination_id);
		}
		if (StringUtil.isNotEmpty(paper_id)){
			strSQL.append(" and t1.paper_id=?");
			paramList.add(paper_id);
		}
		if (StringUtil.isNotEmpty(paper_praxes_id)){
			strSQL.append(" and t1.paper_praxes_id=?");
			paramList.add(paper_praxes_id);
		}
		String[] returnData = {"result_id"};
		Element resData = XmlDocPkgUtil.createMetaData(returnData);
		PlatformDao pdao = new PlatformDao();
		try{
	    	pdao.setSql(strSQL.toString());
	    	pdao.setBindValues(paramList);
	    	Element result = pdao.executeQuerySql(-1,0);
	    	if (result.getChildren("Record").size()==0){
	    		Element examResultRec = ConfigDocument.createRecordElement("yuexue", "learn_examination_result");
	    		XmlDocPkgUtil.copyValues(reqElement, examResultRec, 0, true);
	    		XmlDocPkgUtil.setChildText(examResultRec, "praxes_result", praxes_result);
	    		String result_id = pdao.insertOneRecordSeqPk(examResultRec).toString();
	    		resData.addContent(XmlDocPkgUtil.createRecord(returnData,new String[] {result_id}));
	    	}
	    	else{
	    		Element rec = (Element)result.getChildren("Record").get(0);
	    		Element examResultRec = ConfigDocument.createRecordElement("yuexue", "learn_examination_result");
	    		XmlDocPkgUtil.copyValues(reqElement, examResultRec, 0, true);
	    		XmlDocPkgUtil.setChildText(examResultRec, "praxes_result", praxes_result);
	    		XmlDocPkgUtil.setChildText(examResultRec, "result_id", rec.getChildText("result_id"));
	    		XmlDocPkgUtil.setChildText(examResultRec, "modify_by", userName);
	    		XmlDocPkgUtil.setChildText(examResultRec, "modify_date", DatetimeUtil.getNow(""));
	    		pdao.updateOneRecord(examResultRec);
	    	}
	    	if ("Y".equals(submit_flag)){
				ArrayList<Object> upParam = new ArrayList<Object>();
				StringBuffer updateSQL = new StringBuffer();
				updateSQL.append("update learn_my_examination set status='50',end_time=now(),consuming_time = consuming_time+TIMESTAMPDIFF(second,begin_time,now()),");
				updateSQL.append(" modify_by='"+userName+"',modify_date=now()");
				updateSQL.append(" where my_examination_id = ? ");
				updateSQL.append(" and status < '50' ");
				upParam.add(my_examination_id);
				
				pdao.setSql(updateSQL.toString());
				pdao.setBindValues(upParam);
				pdao.executeTransactionSql();	    		
	    	}
			xmlDocUtil.getResponse().addContent(resData);
	    	xmlDocUtil.setResult("0");	
	    	xmlDocUtil.writeHintMsg("00103", "回复任务单成功!");		    
	    }catch (Exception e) {
			e.printStackTrace();
			log4j.logError("[智慧源-回复任务单]"+e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
		} finally {
			pdao.releaseConnection();
		}
		*/		
	}
}
