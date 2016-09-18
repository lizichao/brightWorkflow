package cn.com.bright.yuexue.teach;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.jdom.Document;
import org.jdom.Element;

import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.db.SqlTypes;
import cn.brightcom.jraf.util.ConfigDocument;
import cn.brightcom.jraf.util.Constants;
import cn.brightcom.jraf.util.DatetimeUtil;
import cn.brightcom.jraf.util.Log;
import cn.brightcom.jraf.util.StringUtil;
import cn.brightcom.jraf.util.XmlDocPkgUtil;
import cn.com.bright.yuexue.exception.NotFoundDataException;
import cn.com.bright.yuexue.util.AttachmentUtil;

/**
 * <p>Title:试卷(课件)管理</p>
 * <p>Description: 试卷(课件)管理维护类</p>
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
 * <p> zhangxq    2014/07/23       1.0          build this moudle </p>
 *     
 */
public class ExamPaper extends Praxes{
	
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
		
        if ("genExamPaperPraxes".equals(action)){
        	genExamPaperPraxes();
		}
        else if ("queryExamPaper".equals(action)){
        	queryExamPaper();
        }else if ("queryResRecord".equals(action)){
        	queryResRecord();
        }
        else if ("getPaperPraxes".equals(action)){
        	getPaperPraxes();
        }
		else if ("addPaperPraxes".equals(action)){
			addPaperPraxes();
		}
		else if ("updatePaperPraxes".equals(action)){
			updatePaperPraxes();
		}
		else if ("deletePaperPraxes".equals(action)){
			deletePaperPraxes();
		}
		else if ("addPaperAttachment".equals(action)){
			addPaperAttachment();
		}
		else if ("choosePraxes".equals(action)){
			choosePraxes();
		}
		else if ("getPaperInfo".equals(action)){
			getPaperInfo();
		}
		else if ("getPaperResult".equals(action)){
			getPaperResult();
		}
		else if ("deleteExamPaper".equals(action)){
			deleteExamPaper();
		}
		else if ("publishExamPaper".equals(action)){
			publishExamPaper();
		}
		else if ("getNoReadStu".equals(action)) {
			getNoReadStu();
		}
		return xmlDoc;
	}
	/**
	 * 删除试卷
	 *
	 */
	public void deleteExamPaper(){
		Element reqElement =  xmlDocUtil.getRequestData();
		String userid = xmlDocUtil.getSession().getChildText("userid");
		String username = xmlDocUtil.getSession().getChildText("username");
		List paperIdList = reqElement.getChildren("paper_id");
		PlatformDao pdao = new PlatformDao();
		try {
			StringBuffer strSQL = new StringBuffer();
			strSQL.append("update learn_examination_paper set valid='N',modify_date=now(),");
			strSQL.append(" modify_by='"+username+"'");
			strSQL.append(" where paper_id=? and user_id=?");
			long upCount = 0 ;
			for (int i = 0; i < paperIdList.size() ;i++){
				String paperID = ((Element)paperIdList.get(i)).getText();
				ArrayList<Object> paramList = new ArrayList<Object>();
				paramList.add(paperID);
				paramList.add(userid);
				
				pdao.setSql(strSQL.toString());
				pdao.setBindValues(paramList);
				upCount += pdao.executeTransactionSql();
			}
			if (upCount>0){
				xmlDocUtil.setResult("0");	
				xmlDocUtil.writeHintMsg("00107", "删除试卷成功!");					
			}
			else{
				xmlDocUtil.writeErrorMsg("30205","只能删除自己上传的内容!");
				xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE); 				
			}
		}
		catch (Exception e) {
			e.printStackTrace();			
			log4j.logError("[试卷管理-删除试卷]"+e.getMessage());
			xmlDocUtil.writeHintMsg("试卷管理-删除试卷!");
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
		} finally {
			pdao.releaseConnection();
		}
	}
	/**
	 * 获取试卷结果
	 *
	 */
	public void getPaperResult(){

		Element reqElement =  xmlDocUtil.getRequestData();
		String qry_classid = reqElement.getChildText("qry_classid");
		
		StringBuffer strSQL = new StringBuffer();
		ArrayList<Object> paramList = new ArrayList<Object>();
		
		strSQL.append("select t1.paper_id,t2.send_id,t2.show_result,t1.resource_type,t1.choose_type,t1.paper_name,t2.modify_date,t2.public_status,");
		strSQL.append(" t2.is_delayed,t2.send_time,t2.complete_time,public_time,t2.receiver_names,");
		strSQL.append(" (select sum(score) from learn_paper_praxes st where st.valid='Y' and st.paper_id=t1.paper_id) as total_score,");
		strSQL.append(" (select count(st.my_examination_id) from learn_my_examination st,base_studentinfo sd ");
		strSQL.append(" where st.valid='Y' and sd.state>'0' and st.userid=sd.userid ");
		if (StringUtil.isNotEmpty(qry_classid)){
			strSQL.append(" and sd.classid=?");
			paramList.add(qry_classid);
		}
		strSQL.append(" and st.send_id=t2.send_id) as stu_count,");
		strSQL.append(" (select count(st.my_examination_id) from learn_my_examination st,base_studentinfo sd ");
		strSQL.append(" where st.valid='Y' and sd.state>'0' and st.userid=sd.userid  ");
		if (StringUtil.isNotEmpty(qry_classid)){
			strSQL.append(" and sd.classid=?");
			paramList.add(qry_classid);
		}		
		strSQL.append(" and st.status='60' and st.send_id=t2.send_id) as pending_count,");
		strSQL.append(" (select count(st.my_examination_id) from learn_my_examination st,base_studentinfo sd ");
		strSQL.append(" where st.valid='Y' and sd.state>'0' and st.userid=sd.userid ");
		if (StringUtil.isNotEmpty(qry_classid)){
			strSQL.append(" and sd.classid=?");
			paramList.add(qry_classid);
		}			
		strSQL.append(" and st.status='70' and st.send_id=t2.send_id) as complete_count"); 
		strSQL.append(" from learn_examination_paper t1,learn_paper_send t2 ");
		strSQL.append(" where t1.valid='Y' and t2.valid='Y' and t1.paper_id=t2.paper_id");
		
		
		if (StringUtil.isNotEmpty(qry_classid)){
            //发送到班
		    strSQL.append(" and ( t2.receiver_ids like ? ");		    
		    paramList.add("%C_"+qry_classid+"%");
		    //发送到人
		    strSQL.append(" or exists (select null from base_studentinfo st where st.classid = ?");
		    strSQL.append("            and st.state='1' and t2.receiver_ids like Concat('%U_',st.userid,'%'))");
		    paramList.add(qry_classid);
		    //发送到组
		    strSQL.append(" or exists (select null from learn_group sm where sm.classid = ?");
		    strSQL.append("        and sm.valid='Y' and t2.receiver_ids like Concat('%G_',sm.group_id,'%'))");
		    strSQL.append(" )");
		    paramList.add(qry_classid);		    
		}
		String qry_paper_name = reqElement.getChildText("qry_paper_name");
		if (StringUtil.isNotEmpty(qry_paper_name)){
		    strSQL.append(" and t1.paper_name like ?");
		    paramList.add(qry_paper_name+"%");
		}
		String qry_paper_id = reqElement.getChildText("qry_paper_id");
		if (StringUtil.isNotEmpty(qry_paper_id)){
		    strSQL.append(" and t1.paper_id = ?");
		    paramList.add(qry_paper_id);
		}
		String qry_userid = reqElement.getChildText("qry_userid");
		if (StringUtil.isNotEmpty(qry_userid)){
		    strSQL.append(" and t2.userid=?");
		    paramList.add(qry_userid);
		}
		String qry_send_id = reqElement.getChildText("qry_send_id");
		if (StringUtil.isNotEmpty(qry_send_id)){
		    strSQL.append(" and t2.send_id=?");
		    paramList.add(qry_send_id);
		}
		String qry_status = reqElement.getChildText("qry_status");
		if (StringUtil.isNotEmpty(qry_status)){
			if ("60".equals(qry_status)){
			    strSQL.append(" and exists (");
				strSQL.append(" select null from learn_my_examination st,base_studentinfo sd ");
				strSQL.append(" where st.valid='Y' and sd.state>'0' and st.userid=sd.userid  ");
				if (StringUtil.isNotEmpty(qry_classid)){
					strSQL.append(" and sd.classid=?");
					paramList.add(qry_classid);
				}		
				strSQL.append(" and st.status=? and st.send_id=t2.send_id");		    
			    strSQL.append(" )");
			    paramList.add(qry_status);
			}
			else if ("70".equals(qry_status)){
			    strSQL.append(" and not exists (");
				strSQL.append(" select null from learn_my_examination st,base_studentinfo sd ");
				strSQL.append(" where st.valid='Y' and sd.state>'0' and st.userid=sd.userid  ");
				if (StringUtil.isNotEmpty(qry_classid)){
					strSQL.append(" and sd.classid=?");
					paramList.add(qry_classid);
				}		
				strSQL.append(" and st.status!=? and st.send_id=t2.send_id");		    
			    strSQL.append(" )");
			    paramList.add(qry_status);				
			}
		}		
		String qry_begin_date = reqElement.getChildText("qry_begin_date");
		if (StringUtil.isNotEmpty(qry_begin_date)){
		    strSQL.append(" and t2.modify_date >= ?");
		    paramList.add(SqlTypes.getConvertor("Timestamp").convert(qry_begin_date, "yyyy-MM-dd"));
		}		
		String qry_end_date = reqElement.getChildText("qry_end_date");
		if (StringUtil.isNotEmpty(qry_end_date)){
		    strSQL.append(" and t2.modify_date <= ?");
		    paramList.add(SqlTypes.getConvertor("Timestamp").convert(qry_end_date, "yyyy-MM-dd"));
		}
        
		if (StringUtil.isNotEmpty(qry_begin_date)){
			strSQL.append(" order by t2.modify_date");
		}
		else{
			strSQL.append(" order by t2.modify_date desc");
		}
	    		
		
	    PlatformDao pdao = new PlatformDao();
	    try{
	    	pdao.setSql(strSQL.toString());
	    	pdao.setBindValues(paramList);
	    	Element result = pdao.executeQuerySql(xmlDocUtil.getPageSize(), xmlDocUtil.getPageNo());
	    	
	    	xmlDocUtil.getResponse().addContent(result);
	    	xmlDocUtil.setResult("0");
	    	
	    }catch (Exception e) {
			e.printStackTrace();
			log4j.logError("[试卷管理-检索试卷结果]"+e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
		} finally {
			pdao.releaseConnection();
		}		
	}
	/**
	 * 获取试卷(课件)信息,包含附件
	 *
	 */
	public void getPaperInfo(){
		Element reqElement =  xmlDocUtil.getRequestData();
		StringBuffer strSQL = new StringBuffer();
		strSQL.append("select getParamDesc('c_choose_type',t.choose_type) as choose_type_desc,");
		strSQL.append(" getParamDesc('c_resource_type',t.resource_type) as resource_type_desc,");
		strSQL.append(" d.subjname,getParamDesc('c_grade',t.grade_code) as grade_name,m.folder_name,m.folder_code,");
		strSQL.append(" getParamDesc('c_share_level',t.share_level) as share_level_desc,t.*");
		strSQL.append(" from learn_examination_paper t,base_subject d,base_book_folder m");
		strSQL.append(" where d.state='1' and t.subject_id=d.subjectid and t.folder_id=m.folder_id");
	    strSQL.append(" and t.paper_id=?");
		ArrayList<Object> paramList = new ArrayList<Object>();
		paramList.add(reqElement.getChildText("paper_id"));
	    PlatformDao pdao = new PlatformDao();
	    try{
	    	pdao.setSql(strSQL.toString());
	    	pdao.setBindValues(paramList);
	    	Element result = pdao.executeQuerySql(0, -1);
	    	
	    	StringBuffer attSQL = new StringBuffer();
	    	attSQL.append("select t2.paper_attachment_id as file_id,t1.* from learn_attachment t1,learn_paper_attachment t2");
	    	attSQL.append(" where t2.valid='Y' and t1.attachment_id=t2.attachment_id and t2.paper_id=?");
	    	attSQL.append(" order by t2.create_date");
	    	pdao.setSql(attSQL.toString());
	    	pdao.setBindValues(paramList);
	    	Element attResult = pdao.executeQuerySql(0, -1);
	    	Element att = new Element("Attachments");
	    	att.addContent(attResult);
	    	result.getChild("Record").addContent(att);
	    	
	    	xmlDocUtil.getResponse().addContent(result);
	    	xmlDocUtil.setResult("0");
	    	
	    }catch (Exception e) {
			e.printStackTrace();
			log4j.logError("[试卷管理-获取试卷信息]"+e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
		} finally {
			pdao.releaseConnection();
		}		
	}
	/**
	 * 将试题写入问卷中
	 * @param pdao
	 * @param result
	 * @return 试卷问题ID
	 */
	public String writePraxesToPaper(PlatformDao pdao,Element praxesRec,int praxesDisplayOrder,String paper_id,Element sessionEle) throws Exception{

		String praxes_id  = praxesRec.getChildText("praxes_id");
		
		Element paperPraxesRec = ConfigDocument.createRecordElement("yuexue", "learn_paper_praxes");
		XmlDocPkgUtil.copyValues(praxesRec, paperPraxesRec, 0, true);
		XmlDocPkgUtil.setChildText(paperPraxesRec, "paper_id", paper_id);
		XmlDocPkgUtil.setChildText(paperPraxesRec, "display_order", ""+praxesDisplayOrder);
		XmlDocPkgUtil.setChildText(paperPraxesRec, "userid", sessionEle.getChildText("userid"));
		XmlDocPkgUtil.setChildText(paperPraxesRec, "deptcode", sessionEle.getChildText("deptcode"));
		XmlDocPkgUtil.setChildText(paperPraxesRec, "create_by", sessionEle.getChildText("username"));		    		
		XmlDocPkgUtil.setChildText(paperPraxesRec, "create_date", DatetimeUtil.getNow(""));					
		XmlDocPkgUtil.setChildText(paperPraxesRec, "modify_by", "");
		XmlDocPkgUtil.setChildText(paperPraxesRec, "modify_date", "");
		
		String paperPraxesID = pdao.insertOneRecordSeqPk(paperPraxesRec).toString();
		
		String praxes_type = praxesRec.getChildText("praxes_type");
		//如果是单选或多选题,则增加选项
		if ("20".equals(praxes_type) || "30".equals(praxes_type)){
			StringBuffer paperOptionsSQL = new StringBuffer();
			paperOptionsSQL.append("insert into learn_paper_options ");
			paperOptionsSQL.append(" select Concat('"+paperPraxesID+"_',t.option_id) as paper_option_id,");
			paperOptionsSQL.append(" '"+paperPraxesID+"' as paper_praxes_id,");
			paperOptionsSQL.append(" t.option_id,t.praxes_id,t.display_order,t.option_content,");
			paperOptionsSQL.append(" t.is_right,t.valid,'"+sessionEle.getChildText("username")+"' as create_by,");
			paperOptionsSQL.append(" now(),null,null,'"+sessionEle.getChildText("userid")+"' as userid");
			paperOptionsSQL.append(" from learn_options t");
			paperOptionsSQL.append(" where t.praxes_id='"+praxes_id+"'");
			
			pdao.setSql(paperOptionsSQL.toString());
			pdao.executeTransactionSql();
		}
		return paperPraxesID;
    			
	}
	/**
	 * 从题库选题
	 *
	 */
	public void choosePraxes(){
		Element reqElement =  xmlDocUtil.getRequestData();
		Element sessionEle =  xmlDocUtil.getSession();
		String paper_id = reqElement.getChildText("paper_id");
		List praxesList = reqElement.getChildren("praxes_id");
		
		PlatformDao pdao = new PlatformDao();
		try {
			pdao.beginTransaction();
			
			StringBuffer strSQL = new StringBuffer();
			strSQL.append("select * from learn_praxes where praxes_id in ( ?");
			
			ArrayList<Object> paramList = new ArrayList<Object>();
			paramList.add("'-1'");
			
			for (int i = 0; i < praxesList.size() ;i++){
				String praxes_id = ((Element)praxesList.get(i)).getText();
				strSQL.append(",?");
				paramList.add(praxes_id);				
			}
			strSQL.append(" )");
			
			pdao.setSql(strSQL.toString());
			pdao.setBindValues(paramList);
			Element result = pdao.executeQuerySql(0,-1);
			
	    	List list = result.getChildren("Record");
	    	for (int i = 0; i < list.size() ;i++){
	    		Element praxesRec = (Element)list.get(i);
			    writePraxesToPaper(pdao,praxesRec,i+1,paper_id,sessionEle);
	    	}
			
			pdao.commitTransaction();			
			xmlDocUtil.setResult("0");	
			xmlDocUtil.writeHintMsg("00200", "选题成功!");				
		}catch (Exception e) {
			pdao.rollBack();
			e.printStackTrace();
			log4j.logError("[试卷管理-人工选题]"+e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
		} finally {
			pdao.releaseConnection();
		}
		
	}
	/**
	 * 上传试卷(课件)附件
	 *
	 */
	public void addPaperAttachment(){
		Element reqElement =  xmlDocUtil.getRequestData();
		String paper_id = reqElement.getChildText("paper_id");
		Element paper_attachment = reqElement.getChild("paper_attachment");
		
		PlatformDao pdao = new PlatformDao();
		try {
			if (paper_attachment!=null){
				String attachment_id = AttachmentUtil.moveFile(pdao, paper_attachment, "exam");
				String fieldName  = "attachment_id";
				String fieldValue = attachment_id;
				if (StringUtil.isNotEmpty(paper_id)){
					Element paperAttRec = ConfigDocument.createRecordElement("yuexue","learn_paper_attachment");
					XmlDocPkgUtil.copyValues(reqElement, paperAttRec, 0 , true);
					XmlDocPkgUtil.setChildText(paperAttRec, "attachment_id", attachment_id);
					String paper_attachment_id = pdao.insertOneRecordSeqPk(paperAttRec).toString();
					
					fieldName  = "paper_attachment_id";
					fieldValue = paper_attachment_id;
				}				
				String[] returnData = { fieldName};
				Element resData = XmlDocPkgUtil.createMetaData(returnData);	
				resData.addContent(XmlDocPkgUtil.createRecord(returnData,new String[] {fieldValue}));			
				xmlDocUtil.getResponse().addContent(resData);
			}
			
			xmlDocUtil.setResult("0");		
		}catch (Exception e) {
			e.printStackTrace();
			log4j.logError("[试卷管理-上传附件]"+e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
		} finally {
			pdao.releaseConnection();
		}
	}
	/**
	 * 删除试卷题目
	 *
	 */
	public void deletePaperPraxes(){
		String userName = xmlDocUtil.getSession().getChildTextTrim("username");
		String userid = xmlDocUtil.getSession().getChildTextTrim("userid");
		Element reqElement =  xmlDocUtil.getRequestData();
		List paperPraxesList = reqElement.getChildren("paper_praxes_id");
		PlatformDao pdao = new PlatformDao();
		try {
			pdao.beginTransaction();
			String deleteSQL = "update learn_paper_praxes set valid='N',modify_by='"+userName+"',modify_date=now() where paper_praxes_id=?";
			
			StringBuffer delPraxesSQL = new StringBuffer("update learn_praxes set valid='N',modify_by='"+userName+"',modify_date=now()");
			delPraxesSQL.append(" where userid='"+userid+"' and praxes_id=");
			delPraxesSQL.append(" (select praxes_id from learn_paper_praxes st where st.paper_praxes_id=?) and create_date>=?");
			
			for (int i = 0; i < paperPraxesList.size() ;i++){
				String paperPraxesID = ((Element)paperPraxesList.get(i)).getText();
    			ArrayList<Object> paramList = new ArrayList<Object>();
    			paramList.add(paperPraxesID);
    			pdao.setSql(deleteSQL);
    			pdao.setBindValues(paramList);
    			pdao.executeTransactionSql();
    			
    			//同步删除对应题库数据(避免题库太多冗余无效题目)
    			paramList.add(SqlTypes.getConvertor("Timestamp").convert(DatetimeUtil.getPreDay(DatetimeUtil.getNow("yyyy-MM-dd")), "yyyy-MM-dd"));
    			pdao.setSql(delPraxesSQL.toString());
    			pdao.setBindValues(paramList);
    			pdao.executeTransactionSql();			
				
			}
			
			pdao.commitTransaction();			
			xmlDocUtil.setResult("0");	
			xmlDocUtil.writeHintMsg("00107", "删除试卷试题成功!");				
		}
		catch (Exception e) {
			pdao.rollBack();
			e.printStackTrace();			
			log4j.logError("[试卷管理-删除试题]"+e.getMessage());
			xmlDocUtil.writeHintMsg("试卷管理-删除试题失败!");
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
		} finally {
			pdao.releaseConnection();
		}		
	}
	/**
	 * 修改试卷题目(只修改试卷题目本身,对题库不做处理)
	 *
	 */
	public void updatePaperPraxes(){
		String userName = xmlDocUtil.getSession().getChildTextTrim("username");
		Element reqElement =  xmlDocUtil.getRequestData();
		PlatformDao pdao = new PlatformDao();
		try {
			String paper_praxes_id = reqElement.getChildText("paper_praxes_id");
			pdao.beginTransaction();
			Element praxesRec = ConfigDocument.createRecordElement("yuexue", "learn_paper_praxes");
			XmlDocPkgUtil.copyValues(reqElement, praxesRec, 0, true);
			XmlDocPkgUtil.setChildText(praxesRec, "modify_by", userName);
			XmlDocPkgUtil.setChildText(praxesRec, "modify_date", DatetimeUtil.getNow(""));
			pdao.updateOneRecord(praxesRec);
			
			String praxes_type = reqElement.getChildText("praxes_type");

    		//如果单选或多选题,则增加选项内容
    		if ("20".equals(praxes_type) || "30".equals(praxes_type)){			
				List contentList = reqElement.getChildren("option_content");
				//List orderList  = reqElement.getChildren("display_order");
				List rightList  = reqElement.getChildren("is_right");
				List paperOptionList  = reqElement.getChildren("paper_option_id");
				
				for (int i = 0; i < contentList.size() ;i++){
					Element optionRec = ConfigDocument.createRecordElement("yuexue", "learn_paper_options");
					Element newEle = new Element("RemoveChild");
					XmlDocPkgUtil.copyValues(newEle, optionRec, 0, true);
					
					XmlDocPkgUtil.setChildText(optionRec, "option_content", ((Element)contentList.get(i)).getText());					
					XmlDocPkgUtil.setChildText(optionRec, "is_right", ((Element)rightList.get(i)).getText());
					XmlDocPkgUtil.setChildText(optionRec, "display_order", ""+(i+1));
					
					Element el = (Element)paperOptionList.get(i);				
					if ((el == null ) || StringUtil.isEmpty(el.getText())){
						XmlDocPkgUtil.setChildText(optionRec, "paper_praxes_id", paper_praxes_id);
					    pdao.insertOneRecordSeqPk(optionRec);
					}
					else{
						XmlDocPkgUtil.setChildText(optionRec, "paper_option_id", el.getText());
						XmlDocPkgUtil.setChildText(optionRec, "modify_by", userName);
						XmlDocPkgUtil.setChildText(optionRec, "modify_date", DatetimeUtil.getNow(""));					
						pdao.updateOneRecord(optionRec);
					}
				}
    		}
    		/**
			String deleteSQL = "update learn_paper_options set valid='N' where paper_option_id=?";
    		List deleteOptionList  = reqElement.getChildren("delete_paper_option_id");
    		for (int i = 0; i < deleteOptionList.size() ;i++){
    			String delOptionID = ((Element)deleteOptionList.get(i)).getText();
    			ArrayList<Object> paramList = new ArrayList<Object>();
    			paramList.add(delOptionID);
    			pdao.setSql(deleteSQL);
    			pdao.setBindValues(paramList);
    			pdao.executeTransactionSql();
    		}
    		*/
			pdao.commitTransaction();			
			xmlDocUtil.setResult("0");	
			xmlDocUtil.writeHintMsg("00105", "修改试卷试题成功!");			
		}
		catch (Exception e) {
			pdao.rollBack();
			e.printStackTrace();			
			log4j.logError("[试卷管理-修改试题]"+e.getMessage());
			xmlDocUtil.writeHintMsg("试卷管理-修改试题失败!");
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
		} finally {
			pdao.releaseConnection();
		}		
	}
	/**
	 * 增加试卷题目(新增的试卷题目同时进入题库)
	 *
	 */
	public void addPaperPraxes(){
		Element reqElement =  xmlDocUtil.getRequestData();
		Element sessionEle =  xmlDocUtil.getSession();
		String paper_id = reqElement.getChildText("paper_id");
		
		PlatformDao pdao = new PlatformDao();
		try {
			
			pdao.beginTransaction();
			//先在试题库中增加试题
			String praxes_id = super.addPraxesOption(reqElement);			
			XmlDocPkgUtil.setChildText(reqElement, "praxes_id", praxes_id);				
			
			//再在试卷中增加试题
			Element praxesRec = ConfigDocument.createRecordElement("yuexue", "learn_praxes");
			XmlDocPkgUtil.copyValues(reqElement, praxesRec, 0, true);
			
			String paper_praxes_id = writePraxesToPaper(pdao,praxesRec,0,paper_id,sessionEle);
			
			String[] returnData = { "paper_praxes_id"};
			Element resData = XmlDocPkgUtil.createMetaData(returnData);	
			resData.addContent(XmlDocPkgUtil.createRecord(returnData,new String[] {paper_praxes_id}));			
			
			xmlDocUtil.getResponse().addContent(resData);			
			
			pdao.commitTransaction();			
			xmlDocUtil.setResult("0");	
			xmlDocUtil.writeHintMsg("00103", "新增试卷题目成功!");	
		}
		catch (Exception e) {
			pdao.rollBack();
			e.printStackTrace();			
			log4j.logError("[习题管理-新增习题]"+e.getMessage());
			xmlDocUtil.writeHintMsg("习题管理-新增习题失败!");
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
		} finally {
			pdao.releaseConnection();
		}		
	}
	
	/**
	 * 获取未浏览学生
	 */
	@SuppressWarnings({ "serial", "unchecked" })
	public void getNoReadStu() {
		Element reqElement =  xmlDocUtil.getRequestData();
		final String paperid = reqElement.getChildText("qry_paper_id");
		String resend = reqElement.getChildText("resend");
		PlatformDao pdao = new PlatformDao();
		StringBuffer sb = new StringBuffer();
		sb.append("	select t4.classid,t4.classnm,t3.username,t3.userid,t3.mobile,t3.mobile,t3.phone from learn_my_examination t1 ,base_studentinfo t2,pcmc_user t3,base_class t4"); 
		sb.append("	where t1.userid = t2.userid ");
		sb.append("	and t2.userid = t3.userid ");
		sb.append("	and t2.classid = t4.classid");
		sb.append("	and t1.paper_id = ?");
		sb.append("	and not EXISTS (");
		sb.append("	select null from sell_read_log tt1 where tt1.valid = 'Y' and tt1.paper_info_id = t1.paper_id and tt1.userid = t1.userid");
		sb.append("	) ORDER BY classnm desc");
		pdao.setSql(sb.toString());
		pdao.setBindValues(new ArrayList<String>(){{add(paperid);}});
		Element result = null;
		try {
			result = pdao.executeQuerySql(0,-1);
			if ("true".equals(resend)) {
				String mobiles = "";
				List<Element> li = result.getChildren("Record");
				for (Element el : li) {
					String mobile = el.getChildText("mobile");
					String phone = el.getChildText("phone");
					if(StringUtil.isMobile(mobile)){
						if(mobiles.indexOf(mobile) < 0){
							mobiles += mobile + ",";
						}
					}
					if(StringUtil.isMobile(phone)){
						if(!phone.equals(mobile) && mobiles.indexOf(phone) < 0){
							mobiles += phone + ",";
						}
					}
				}
				if(mobiles.length() != 0){
					int len = mobiles.length();
					mobiles = mobiles.substring(0,len-1);
					JSONObject jsonObject = PaperSender.reSendWXMessage(mobiles, paperid);
					xmlDocUtil.writeHintMsg("00103", jsonObject.getString("errmsg"));
				} else {
					xmlDocUtil.writeHintMsg("00103", "接收人信息为空！");	
				}
				xmlDocUtil.setResult("0");
			} else {
				xmlDocUtil.getResponse().addContent(result);
				xmlDocUtil.setResult("0");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pdao.releaseConnection();
		}
	}
	
	/**
	 * 获取试卷题目含选项
	 *
	 */			
	public void getPaperPraxes(){
		String optionStr = "A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z";
		String[] optionArray = optionStr.split(",");		
		Element reqElement =  xmlDocUtil.getRequestData();		
		String paper_id = reqElement.getChildText("paper_id");
		
		PlatformDao pdao = new PlatformDao();
	    try{	    	
	    	
			StringBuffer strSQL = new StringBuffer();
			strSQL.append("select t2.subjname,t3.k_point_name,'' as praxes_result,");
			strSQL.append(" getParamDesc('c_praxes_type',t1.praxes_type) as praxes_type_desc,");
			strSQL.append(" (select count(sm.paper_praxes_id) from learn_paper_praxes sm where sm.valid='Y' and sm.praxes_id=t1.praxes_id) as use_count,");
			strSQL.append(" t1.*");
			strSQL.append(" from learn_paper_praxes t1 left join base_subject t2 on t1.subject_id=t2.subjectid left join  base_knowledge_point t3 on t1.k_point_id=t3.k_point_id");
			strSQL.append(" where t1.valid='Y'");
			strSQL.append(" and t1.paper_id = ? ");			
			
			ArrayList<Object> paramList = new ArrayList<Object>();
			paramList.add(paper_id);
			
			String paper_praxes_id = reqElement.getChildText("paper_praxes_id");
			if (StringUtil.isNotEmpty(paper_praxes_id)){
				strSQL.append(" and t1.paper_praxes_id = ?");
				paramList.add(paper_praxes_id);
			}
			
			strSQL.append(" order by t1.display_order");
			
	    	pdao.setSql(strSQL.toString());
	    	pdao.setBindValues(paramList);
	    	Element result = pdao.executeQuerySql(xmlDocUtil.getPageSize(), xmlDocUtil.getPageNo());
	    	
	    	StringBuffer optionSQL = new StringBuffer();
	    	optionSQL.append("select t1.paper_option_id,t1.paper_praxes_id,ifnull(t1.praxes_id,t1.paper_praxes_id) as praxes_id,ifnull(t1.option_id,t1.paper_option_id) as option_id,");
	    	optionSQL.append(" t1.is_right,t1.option_content,t1.display_order ");
	    	optionSQL.append(" from learn_paper_options t1 ");
	    	optionSQL.append(" where t1.paper_praxes_id=? and t1.valid='Y' order by display_order");
	    	
	    	List list = result.getChildren("Record");
	    	for (int i = 0; i < list.size() ;i++){
	    		Element praxesRec = (Element)list.get(i);
	    		String praxes_type = praxesRec.getChildText("praxes_type");
	    		//如果单选或多选题,则增加选项内容
	    		if ("20".equals(praxes_type) || "30".equals(praxes_type)){
		    		ArrayList<Object> optionParam = new ArrayList<Object>();
		    		optionParam.add(praxesRec.getChildText("paper_praxes_id"));
		    		
		    		pdao.setSql(optionSQL.toString());
		    		pdao.setBindValues(optionParam);
		    		Element optionResult = pdao.executeQuerySql(0,-1);
			    	
		    		List optionList = optionResult.getChildren("Record");
			    	for (int j = 0; j < optionList.size() ;j++){
			    		Element optionRec = (Element)optionList.get(j);
			    		XmlDocPkgUtil.setChildText(optionRec, "option_name", optionArray[j]);
			    	}
		    		Element options = new Element("Options");
		    		options.addContent(optionResult);
		    		praxesRec.addContent(options);
	    		}
	    	}
	    	
	    	xmlDocUtil.getResponse().addContent(result);
	    	xmlDocUtil.setResult("0");
	    	
	    }catch (Exception e) {
			e.printStackTrace();
			log4j.logError("[试卷管理-检索习题]"+e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
		} finally {
			pdao.releaseConnection();
		}
		
	}
	/**
	 * 检索试卷
	 *
	 */
	public void queryExamPaper(){
		String userid   = xmlDocUtil.getSession().getChildTextTrim("userid");	
		String deptcode = xmlDocUtil.getSession().getChildTextTrim("deptcode");		
		
		Element reqElement =  xmlDocUtil.getRequestData();
		StringBuffer strSQL = new StringBuffer();
		ArrayList<Object> paramList = new ArrayList<Object>();
		strSQL.append("select t2.subjname,t3.folder_name,t1.*,");
		strSQL.append(" (select count(comment_id) from  learn_comment st where st.valid='Y' and st.paper_id=t1.paper_id) as comment_count,");
		strSQL.append(" (select count(paper_praxes_id) from learn_paper_praxes st where st.valid='Y' and st.paper_id=t1.paper_id) as praxes_count,");
		strSQL.append(" (select count(rule_id) from  learn_choose_rule st where st.valid='Y' and st.paper_id=t1.paper_id) as rule_count,");
		strSQL.append(" (select count(*) from learn_my_examination tt1 where tt1.paper_id=t1.paper_id and tt1.valid='Y' and tt1.status >'10') as submit_count,");
		strSQL.append(" (SELECT count(st.my_examination_id) FROM learn_my_examination st, base_studentinfo sd WHERE     st.valid = 'Y' AND sd.state > '0' AND st.userid = sd.userid AND st.status = '60' AND st.paper_id = t1.paper_id) AS pending_count,");
		strSQL.append(" (select count(send_id) from learn_paper_send st where st.valid='Y' and st.paper_id=t1.paper_id and st.userid=?) as send_count,");
		paramList.add(userid);
		strSQL.append(" (SELECT count(*) FROM sell_read_log t2 WHERE t1.paper_id = t2.paper_info_id) AS readcount");
		strSQL.append(" ,(case when t1.user_id='").append(userid).append("' then 1 else 0 end) as author");  // 添加作者标识 lhbo 2016-03-02
		strSQL.append(" from learn_examination_paper t1,base_subject t2,base_book_folder t3");
		strSQL.append(" where t1.valid='Y' and t1.subject_id=t2.subjectid and t1.folder_id=t3.folder_id");
		//是否根据共享级别查询
		String qry_myself=reqElement.getChildText("qry_myself");
		if (StringUtil.isEmpty(qry_myself)) {
			qry_myself = "0";
		}
		if ("0".equals(qry_myself)) {
			//私有
			strSQL.append(" and ( (t1.share_level = 10 and t1.user_id='"+userid+"')");
			//校内共享
			strSQL.append("       or (t1.share_level =50 and t1.deptcode ='"+deptcode+"' )");
			//完全公开
			strSQL.append("       or (t1.share_level =90))");		
		} else {
			// 我的上传
			strSQL.append(" and ( t1.user_id = ? ) ");
			paramList.add(userid);
		}
		String qry_subject_id = reqElement.getChildText("qry_subject_id");
		if (StringUtil.isNotEmpty(qry_subject_id)){
		    strSQL.append(" and t1.subject_id=?");
		    paramList.add(qry_subject_id);
		}
		String qry_grade_id = reqElement.getChildText("qry_grade_code");
		if (StringUtil.isNotEmpty(qry_grade_id)){
		    strSQL.append(" and t1.grade_code=?");
		    paramList.add(qry_grade_id);
		}
		String qry_resource_type = reqElement.getChildText("qry_resource_type");
		if (StringUtil.isNotEmpty(qry_resource_type)){
		    strSQL.append(" and t1.resource_type like ?");
		    paramList.add(qry_resource_type+"%");
		}
		String qry_user_id = reqElement.getChildText("qry_user_id");
		if (StringUtil.isNotEmpty(qry_user_id)){
		    strSQL.append(" and t1.user_id=?");
		    paramList.add(qry_user_id);
		}		
		String qry_folder_code = reqElement.getChildText("qry_folder_code");
		if (StringUtil.isNotEmpty(qry_folder_code)){
		    strSQL.append(" and t3.folder_code like ?");
		    paramList.add(qry_folder_code+"%");
		}
		String qry_folder_id = reqElement.getChildText("qry_folder_id");
		if (StringUtil.isNotEmpty(qry_folder_id)){
		    strSQL.append(" and t3.folder_code like ?");
		    paramList.add("%"+qry_folder_id+"%");
		}		
		String qry_subject_book_id = reqElement.getChildText("qry_subject_book_id");
		if (StringUtil.isNotEmpty(qry_subject_book_id)){
		    strSQL.append(" and t3.subject_book_id = ?");
		    paramList.add(qry_subject_book_id);
		}		
		String qry_paper_name = reqElement.getChildText("qry_paper_name");
		if (StringUtil.isNotEmpty(qry_paper_name)){
		    strSQL.append(" and t1.paper_name like ?");
		    paramList.add(qry_paper_name+"%");
		}		
		
	    String orderBy = xmlDocUtil.getOrderBy();
	    if (StringUtil.isNotEmpty(orderBy)){
	    	strSQL.append(orderBy);
	    }
	    else{
	    	strSQL.append(" order by readcount desc, t1.create_date desc");
	    }		
		
	    PlatformDao pdao = new PlatformDao();
	    try{
	    	pdao.setSql(strSQL.toString());
	    	pdao.setBindValues(paramList);
	    	Element result = pdao.executeQuerySql(xmlDocUtil.getPageSize(), xmlDocUtil.getPageNo());
	    	
	    	xmlDocUtil.getResponse().addContent(result);
	    	xmlDocUtil.setResult("0");
	    	
	    }catch (Exception e) {
			e.printStackTrace();
			log4j.logError("[试卷管理-检索试卷]"+e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
		} finally {
			pdao.releaseConnection();
		}		
	}
	/**
	 * 检索资源阅读记录
	 *
	 */
	public void queryResRecord(){
		String userid   = xmlDocUtil.getSession().getChildTextTrim("userid");	
		String deptcode = xmlDocUtil.getSession().getChildTextTrim("deptcode");		
		
		Element reqElement =  xmlDocUtil.getRequestData();
		StringBuffer strSQL = new StringBuffer();
		ArrayList<Object> paramList = new ArrayList<Object>();
		strSQL.append("select * from ( ");
		strSQL.append("select t2.subjname,t3.folder_name,t1.*,");
		strSQL.append(" (select count(*) from sell_read_log t2 where t1.paper_id = t2.paper_info_id) as readcount, ");
		strSQL.append(" (select count(1) from learn_comment lc where t1.paper_id= lc.paper_id) as plcount, ");
		strSQL.append(" (select count(1) from sell_read_log sl where sl.praise='Y' and sl.paper_info_id=t1.paper_id ) as dianzan ");
		strSQL.append(" from learn_examination_paper t1,base_subject t2,base_book_folder t3");
		strSQL.append(" where t1.valid='Y' and t1.subject_id=t2.subjectid and t1.folder_id=t3.folder_id");
		
		String qry_subject_id = reqElement.getChildText("qry_subject_id");
		if (StringUtil.isNotEmpty(qry_subject_id)){
		    strSQL.append(" and t1.subject_id=?");
		    paramList.add(qry_subject_id);
		}
		String qry_grade_id = reqElement.getChildText("qry_grade_code");
		if (StringUtil.isNotEmpty(qry_grade_id)){
		    strSQL.append(" and t1.grade_code=?");
		    paramList.add(qry_grade_id);
		}
		String qry_paper_id = reqElement.getChildText("qry_paper_id");
		if (StringUtil.isNotEmpty(qry_paper_id)){
		    strSQL.append(" and t1.paper_id=?");
		    paramList.add(qry_paper_id);
		}
		String qry_resource_type = reqElement.getChildText("qry_resource_type");
		if (StringUtil.isNotEmpty(qry_resource_type)){
		    strSQL.append(" and t1.resource_type like ?");
		    paramList.add(qry_resource_type+"%");
		}
		String qry_folder_code = reqElement.getChildText("qry_folder_code");
		if (StringUtil.isNotEmpty(qry_folder_code)){
		    strSQL.append(" and t3.folder_code like ?");
		    paramList.add(qry_folder_code+"%");
		}
 
	   strSQL.append(") a order by readcount desc");
		
		
	    PlatformDao pdao = new PlatformDao();
	    try{
	    	pdao.setSql(strSQL.toString());
	    	pdao.setBindValues(paramList);
	    	Element result = pdao.executeQuerySql(xmlDocUtil.getPageSize(), xmlDocUtil.getPageNo());
	    	
	    	xmlDocUtil.getResponse().addContent(result);
	    	xmlDocUtil.setResult("0");
	    	
	    }catch (Exception e) {
			e.printStackTrace();
			log4j.logError("[资源阅读记录]"+e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
		} finally {
			pdao.releaseConnection();
		}		
	}
	/**
	 * 生成试卷
	 *
	 */
	public void genExamPaperPraxes(){
		Element reqElement =  xmlDocUtil.getRequestData();
		Element sessionEle =  xmlDocUtil.getSession();

		try {
			String paper_id = reqElement.getChildText("paper_id");
			this.executeAutomaticGeneratingPaper(paper_id, sessionEle);
			
			xmlDocUtil.setResult("0");	
			xmlDocUtil.writeHintMsg("00105", "生成试卷成功!");				
		}
		catch (Exception e) {			
			e.printStackTrace();			
			log4j.logError("[试卷(课件)管理-生成试卷]"+e.getMessage());
			xmlDocUtil.writeHintMsg("试题(课件)管理-生成试卷失败!");
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
		} 		
	}
	/**
	 * 执行自动组卷(包括教师在发卷前的组卷和学生在答题时的自动组卷)
	 * @param paperID
	 * @return 完成组卷后的试卷ID(如果是教师组卷,则返回原ID)
	 */
	public String executeAutomaticGeneratingPaper (String paperID,Element sessionEle)throws Exception{
		
		String paper_id = null; 
		StringBuffer ruleSQL = new StringBuffer();
		ruleSQL.append("select sd.subject_id,sd.grade_code,sd.folder_id,sd.resource_type,sd.paper_name,");
		ruleSQL.append(" sd.choose_type,sd.limit_times,sd.qualified_score,sd.share_level,");
		ruleSQL.append(" sm.folder_code as paper_folder_code,st.rule_id,st.paper_id,st.praxes_type,st.folder_code, ");
		ruleSQL.append(" st.difficulty_all,st.difficulty_min,st.difficulty_max,st.praxes_count,st.praxes_score");
		ruleSQL.append(" from learn_choose_rule st,learn_examination_paper sd,base_book_folder sm");
		ruleSQL.append(" where st.paper_id=sd.paper_id and st.valid='Y' and sd.folder_id=sm.folder_id");
		ruleSQL.append(" and st.paper_id=?");
		
		ArrayList<Object> paramList = new ArrayList<Object>();
		paramList.add(paperID);
		
		PlatformDao pdao = new PlatformDao(true);
		try {
			//pdao.beginTransaction();//不能开启事务,Mysql对一个表的update时会锁定表,造成后面无法insert该表
			
			pdao.setSql(ruleSQL.toString());
	    	pdao.setBindValues(paramList);
	    	Element result = pdao.executeQuerySql(0,-1);
	    	List list = result.getChildren("Record");
	    	if (list.size()==0){
	    		throw new NotFoundDataException("没有找到ID为["+paper_id+"]的试卷组卷规则,无法生成试卷");
	    	}
	    	for (int i = 0; i < list.size() ;i++){
	    		Element ruleRec = (Element)list.get(i);
	    		
	    		if (i==0){
	    		    String choose_type = ruleRec.getChildText("choose_type");
	    		    if ("30".equals(choose_type)){//答题时随机组卷,建立一个新的试卷ID
	    		    	Element paperRec = ConfigDocument.createRecordElement("yuexue", "learn_examination_paper");
	    		    	XmlDocPkgUtil.copyValues(ruleRec, paperRec, 0, true);
	    		    	paperRec.removeChild("paper_id");
	    		    	XmlDocPkgUtil.setChildText(paperRec, "resource_type", "99");
	    		    	paper_id = pdao.insertOneRecordSeqPk(paperRec).toString();
	    		    }
	    		    else{//自动组卷 把试卷原题目全部删除	    	            
	    				StringBuffer updateSQL = new StringBuffer();
	    				updateSQL.append("update learn_paper_praxes set valid='N',modify_date=now(),");
	    				updateSQL.append(" modify_by='"+sessionEle.getChildText("username")+"'");
	    				updateSQL.append(" where valid='Y' and paper_id=?");
	    				pdao.setSql(updateSQL.toString());
	    		    	pdao.setBindValues(paramList);			
	    		    	pdao.executeTransactionSql();
	    		    	
	    		    	paper_id = paperID;
	    		    }
	    		}
	    		String paper_folder_code = ruleRec.getChildText("paper_folder_code");
	    		String praxes_type = ruleRec.getChildText("praxes_type");
	    		String folder_code = ruleRec.getChildText("folder_code");
	    		String difficulty_all = ruleRec.getChildText("difficulty_all");
	    		String difficulty_min = ruleRec.getChildText("difficulty_min");
	    		String difficulty_max = ruleRec.getChildText("difficulty_max");
	    		String praxes_count = ruleRec.getChildText("praxes_count");
	    		String praxes_score = ruleRec.getChildText("praxes_score");
	    		
	    		ArrayList<Object> praxesParam = new ArrayList<Object>();
	    		StringBuffer praxesSQL = new StringBuffer();
	    		praxesSQL.append("select t1.* from learn_praxes t1,base_folder_point t2,base_book_folder t3");
	    		praxesSQL.append(" where t1.valid='Y' and t1.k_point_id=t2.k_point_id and t2.folder_id=t3.folder_id and t1.praxes_type <> '90' ");
	    		
	    		if (StringUtil.isNotEmpty(paper_folder_code)){
	    			praxesSQL.append(" and t3.folder_code like ?");
	    			praxesParam.add(paper_folder_code+"%");	    			
	    		}
	    		if (StringUtil.isNotEmpty(praxes_type)){
	    			praxesSQL.append(" and t1.praxes_type = ?");
	    			praxesParam.add(praxes_type);	    			
	    		}
	    		if (StringUtil.isNotEmpty(folder_code)){
	    			praxesSQL.append(" and t3.folder_code like ?");
	    			praxesParam.add(folder_code+"%");	    			
	    		}
	    		if (StringUtil.isNotEmpty(difficulty_all) && "N".equals(difficulty_all)){
	    			if (StringUtil.isNotEmpty(difficulty_min)){
	    				praxesSQL.append(" and t1.difficulty_level >= ?");
	    				praxesParam.add(difficulty_min);
	    			}
	    			if (StringUtil.isNotEmpty(difficulty_max)){
	    				praxesSQL.append(" and t1.difficulty_level <= ?");
	    				praxesParam.add(difficulty_max);
	    			}	    			
	    		}
	    		if (StringUtil.isNotEmpty(praxes_count)){
	    			praxesSQL.append(" order by rand() limit "+praxes_count);
	    		}
	    		else{
	    			praxesSQL.append(" order by rand() limit 20");
	    		}
	    		
	    		
				pdao.setSql(praxesSQL.toString());
		    	pdao.setBindValues(praxesParam);
		    	Element praxesResult = pdao.executeQuerySql(0,-1);
		    	
		    	if (StringUtil.isEmpty(praxes_score)){
		    		praxes_score = "0";
		    	}
		    	int praxesTotalScore = Integer.parseInt(praxes_score);
		    	List praxesList = praxesResult.getChildren("Record");
		    	for (int j = 0; j < praxesList.size() ;j++){
		    		Element praxesRec = (Element)praxesList.get(j);
		    		
		    		if (praxesTotalScore>0){
		    			String score = praxesRec.getChildText("score");
		    			int praxesScore = Integer.parseInt(score);
		    			praxesTotalScore = praxesTotalScore-praxesScore;
		    			if (praxesTotalScore<0){
		    				break;
		    			}
		    		}
		    		
		    		writePraxesToPaper(pdao,praxesRec,i+j+1,paper_id,sessionEle);		    				    		
		    	}		    			    	
	    	}			
			//pdao.commitTransaction();
		}
		catch (Exception e) {
			//pdao.rollBack();
			throw e;
		} finally {
			pdao.releaseConnection();
		}
		return paper_id;
	}
	
	/**
	 * 发布试卷
	 *
	 */
	public void publishExamPaper(){
		Element reqElement =  xmlDocUtil.getRequestData();
		String userid = xmlDocUtil.getSession().getChildText("userid");
		String username = xmlDocUtil.getSession().getChildText("username");
		String paperid= reqElement.getChildText("paper_id");
		PlatformDao pdao = new PlatformDao();
		try {
			StringBuffer strSQL = new StringBuffer();
			strSQL.append("update learn_examination_paper set publish_status='Y',modify_date=now(),");
			strSQL.append(" modify_by='"+username+"'");
			strSQL.append(" where paper_id=? and user_id=?");
			
				ArrayList<Object> paramList = new ArrayList<Object>();
				paramList.add(paperid);
				paramList.add(userid);
				pdao.setSql(strSQL.toString());
				pdao.setBindValues(paramList);
				pdao.executeTransactionSql();
				xmlDocUtil.setResult("0");					
			
		}
		catch (Exception e) {
			e.printStackTrace();			
			log4j.logError("[试卷管理-发布试卷]"+e.getMessage());
			xmlDocUtil.writeHintMsg("试卷管理-发布试卷!");
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
		} finally {
			pdao.releaseConnection();
		}
	}
	
	/**
	 * 获取试卷结果
	 *
	 */
	public void getPaperResultWocr(){

		Element reqElement =  xmlDocUtil.getRequestData();
		String qry_classid = reqElement.getChildText("qry_classid");
		
		StringBuffer strSQL = new StringBuffer();
		ArrayList<Object> paramList = new ArrayList<Object>();
		
		strSQL.append("select t1.paper_id,t2.send_id,t2.show_result,t1.resource_type,t1.choose_type,t1.paper_name,t2.modify_date,t2.public_status,");
		strSQL.append(" t2.is_delayed,t2.send_time,t2.complete_time,public_time,t2.receiver_names,");
		strSQL.append(" (select sum(score) from learn_paper_praxes st where st.valid='Y' and st.paper_id=t1.paper_id) as total_score,");
		strSQL.append(" (select count(st.my_examination_id) from learn_my_examination st,base_studentinfo sd ");
		strSQL.append(" where st.valid='Y' and sd.state>'0' and st.userid=sd.userid ");
		if (StringUtil.isNotEmpty(qry_classid)){
			strSQL.append(" and sd.classid=?");
			paramList.add(qry_classid);
		}
		strSQL.append(" and st.send_id=t2.send_id) as stu_count,");
		strSQL.append(" (select count(st.my_examination_id) from learn_my_examination st,base_studentinfo sd ");
		strSQL.append(" where st.valid='Y' and sd.state>'0' and st.userid=sd.userid  ");
		if (StringUtil.isNotEmpty(qry_classid)){
			strSQL.append(" and sd.classid=?");
			paramList.add(qry_classid);
		}		
		strSQL.append(" and st.status='60' and st.send_id=t2.send_id) as pending_count,");
		strSQL.append(" (select count(st.my_examination_id) from learn_my_examination st,base_studentinfo sd ");
		strSQL.append(" where st.valid='Y' and sd.state>'0' and st.userid=sd.userid ");
		if (StringUtil.isNotEmpty(qry_classid)){
			strSQL.append(" and sd.classid=?");
			paramList.add(qry_classid);
		}			
		strSQL.append(" and st.status='70' and st.send_id=t2.send_id) as complete_count"); 
		strSQL.append(" from learn_examination_paper t1,learn_paper_send t2 ");
		strSQL.append(" where t1.valid='Y' and t2.valid='Y' and t1.paper_id=t2.paper_id");
		
		
		if (StringUtil.isNotEmpty(qry_classid)){
            //发送到班
		    strSQL.append(" and ( t2.receiver_ids like ? ");		    
		    paramList.add("%C_"+qry_classid+"%");
		    //发送到人
		    strSQL.append(" or exists (select null from base_studentinfo st where st.classid = ?");
		    strSQL.append("            and st.state='1' and t2.receiver_ids like Concat('%U_',st.userid,'%'))");
		    paramList.add(qry_classid);
		    //发送到组
		    strSQL.append(" or exists (select null from learn_group sm where sm.classid = ?");
		    strSQL.append("        and sm.valid='Y' and t2.receiver_ids like Concat('%G_',sm.group_id,'%'))");
		    strSQL.append(" )");
		    paramList.add(qry_classid);		    
		}
		String qry_paper_name = reqElement.getChildText("qry_paper_name");
		if (StringUtil.isNotEmpty(qry_paper_name)){
		    strSQL.append(" and t1.paper_name like ?");
		    paramList.add(qry_paper_name+"%");
		}
		String qry_paper_id = reqElement.getChildText("qry_paper_id");
		if (StringUtil.isNotEmpty(qry_paper_id)){
		    strSQL.append(" and t1.paper_id = ?");
		    paramList.add(qry_paper_id);
		}
		String qry_userid = reqElement.getChildText("qry_userid");
		if (StringUtil.isNotEmpty(qry_userid)){
		    strSQL.append(" and t2.userid=?");
		    paramList.add(qry_userid);
		}
		String qry_send_id = reqElement.getChildText("qry_send_id");
		if (StringUtil.isNotEmpty(qry_send_id)){
		    strSQL.append(" and t2.send_id=?");
		    paramList.add(qry_send_id);
		}
		String qry_status = reqElement.getChildText("qry_status");
		if (StringUtil.isNotEmpty(qry_status)){
			if ("60".equals(qry_status)){
			    strSQL.append(" and exists (");
				strSQL.append(" select null from learn_my_examination st,base_studentinfo sd ");
				strSQL.append(" where st.valid='Y' and sd.state>'0' and st.userid=sd.userid  ");
				if (StringUtil.isNotEmpty(qry_classid)){
					strSQL.append(" and sd.classid=?");
					paramList.add(qry_classid);
				}		
				strSQL.append(" and st.status=? and st.send_id=t2.send_id");		    
			    strSQL.append(" )");
			    paramList.add(qry_status);
			}
			else if ("70".equals(qry_status)){
			    strSQL.append(" and not exists (");
				strSQL.append(" select null from learn_my_examination st,base_studentinfo sd ");
				strSQL.append(" where st.valid='Y' and sd.state>'0' and st.userid=sd.userid  ");
				if (StringUtil.isNotEmpty(qry_classid)){
					strSQL.append(" and sd.classid=?");
					paramList.add(qry_classid);
				}		
				strSQL.append(" and st.status!=? and st.send_id=t2.send_id");		    
			    strSQL.append(" )");
			    paramList.add(qry_status);				
			}
		}		
		String qry_begin_date = reqElement.getChildText("qry_begin_date");
		if (StringUtil.isNotEmpty(qry_begin_date)){
		    strSQL.append(" and t2.modify_date >= ?");
		    paramList.add(SqlTypes.getConvertor("Timestamp").convert(qry_begin_date, "yyyy-MM-dd"));
		}		
		String qry_end_date = reqElement.getChildText("qry_end_date");
		if (StringUtil.isNotEmpty(qry_end_date)){
		    strSQL.append(" and t2.modify_date <= ?");
		    paramList.add(SqlTypes.getConvertor("Timestamp").convert(qry_end_date, "yyyy-MM-dd"));
		}
        
		if (StringUtil.isNotEmpty(qry_begin_date)){
			strSQL.append(" order by t2.modify_date");
		}
		else{
			strSQL.append(" order by t2.modify_date desc");
		}
	    		
		
	    PlatformDao pdao = new PlatformDao();
	    try{
	    	pdao.setSql(strSQL.toString());
	    	pdao.setBindValues(paramList);
	    	Element result = pdao.executeQuerySql(xmlDocUtil.getPageSize(), xmlDocUtil.getPageNo());
	    	
	    	xmlDocUtil.getResponse().addContent(result);
	    	xmlDocUtil.setResult("0");
	    	
	    }catch (Exception e) {
			e.printStackTrace();
			log4j.logError("[试卷管理-检索试卷结果]"+e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
		} finally {
			pdao.releaseConnection();
		}		
	}
}
