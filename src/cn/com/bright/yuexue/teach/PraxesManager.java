package cn.com.bright.yuexue.teach;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

/**
 * <p>Title:题库管理</p>
 * <p>Description: 题库管理维护类</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: BrightCom Technologies.</p>
 * @author E40
 * @version 1.0
 * 
 * <p>Function List // 主要函数及其功能
 * 
 *      
 * <p>History: // 历史修改记录:</p>
 * <p> author    time             version      desc</p>
 *     
 */
public class PraxesManager {
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
		
		if("addPraxes".equals(action)){
			addPraxes();
		}
		else if("getPaperPraxes".equals(action)){
			getPaperPraxes();
		}
		else if("savePaper".equals(action)){
			savePaper();
		}
		else if ("clearSelectPraxes".equals(action)){
			clearSelectPraxes();
		}
		else if ("delSelectPraxes".equals(action)){
			delSelectPraxes();
		}
		/**
		else if ("clearPaperPraxes".equals(action)){
			clearPaperPraxes();
		}*/
		else if ("getSelectPraxesCount".equals(action)){
			getSelectPraxesCount();
		}
		else if ("getPraxesStat".equals(action)){
			getPraxesStat();
		}
		else if ("addPraxes2Select".equals(action)){
			addPraxes2Select();
		}
		return xmlDoc;
	}
	/**
	 * 加入试题篮
	 *
	 */
	public void addPraxes2Select(){
		Element sessionEle =  xmlDocUtil.getSession();
		Element reqElement =  xmlDocUtil.getRequestData();
		String paper_id = reqElement.getChildText("paper_id");
		String praxes_id = reqElement.getChildText("praxes_id");
		
		PlatformDao pdao = new PlatformDao();
		try {
			if (StringUtil.isEmpty(paper_id)){
				Element tempPraxes = ConfigDocument.createRecordElement("yuexue","temp_praxes");
				XmlDocPkgUtil.copyValues(reqElement, tempPraxes, 0 , true);
				pdao.insertOneRecordSeqPk(tempPraxes);				
			}
			else{
				StringBuffer strSQL = new StringBuffer();
				strSQL.append("select * from learn_praxes where praxes_id = ?");
				ArrayList<Object> paramList = new ArrayList<Object>();
				paramList.add(praxes_id);
				
				pdao.setSql(strSQL.toString());
				pdao.setBindValues(paramList);
				Element result = pdao.executeQuerySql(0,-1);
				Element praxesRec = result.getChild("Record");
				
				ExamPaper ep = new ExamPaper();
				ep.writePraxesToPaper(pdao, praxesRec, 1, paper_id, sessionEle);
			}
			xmlDocUtil.setResult("0");	
			xmlDocUtil.writeHintMsg("00200", "加入试题篮成功!");			
		}catch (Exception e) {
			e.printStackTrace();
			log4j.logError("[试卷管理-人工选题]"+e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
		} finally {
			pdao.releaseConnection();
		}		
	}
	/**
	 * 试题分布情况
	 *
	 */
	public void getPraxesStat(){
		String userid = xmlDocUtil.getSession().getChildText("userid");
		Element reqElement =  xmlDocUtil.getRequestData();
		String paper_id = reqElement.getChildText("paper_id");
		String subject_id = reqElement.getChildText("subject_id");
		String grade_code = reqElement.getChildText("grade_code");
		
		StringBuffer strSQL = new StringBuffer();
		strSQL.append("select t2.paramcode as stat_name,t2.parammeanings as stat_desc");
		strSQL.append(" from param_master t1,param_detail t2");
		strSQL.append(" where t1.paramid=t2.paramid and t1.paramname='c_praxes_stat' ");
		PlatformDao pdao = new PlatformDao();
		try{
			pdao.setSql(strSQL.toString());
			Element result = pdao.executeQuerySql(0, -1);
			List ls  = result.getChildren("Record");
			
			for (int i=0;i<ls.size();i++){
				Element rec = (Element)ls.get(i);
				String statItem = rec.getChildText("stat_name");
				
				ArrayList<Object> paramList = new ArrayList<Object>();
				StringBuffer statSQL = new StringBuffer();				
				statSQL.append("select ");
				if ("praxes_type".equals(statItem)){
					statSQL.append("getParamDesc('c_praxes_type',t1.praxes_type) ");
				}
				else{
					statSQL.append("IFNULL(t1."+statItem+",'待计算')");
				}
				statSQL.append(" as stat_item,count(t1.praxes_id) as praxes_count");
				statSQL.append(" from learn_praxes t1 where t1.valid='Y'");
				if (StringUtil.isNotEmpty(paper_id)){
					statSQL.append(" and exists (select null from learn_paper_praxes t2");
					statSQL.append(" where t2.valid='Y' and t2.paper_id=? and t2.praxes_id=t1.praxes_id)");
					paramList.add(paper_id);
				}
				else{
					statSQL.append(" and exists (select null from temp_praxes t2");
					statSQL.append(" where t2.userid=? and t2.praxes_id=t1.praxes_id)");
					paramList.add(userid);
					
					if (StringUtil.isNotEmpty(subject_id)){
						statSQL.append(" and t1.subject_id=?");
						paramList.add(subject_id);
					}
					if (StringUtil.isNotEmpty(grade_code)){
						statSQL.append(" and t1.grade_code=?");
						paramList.add(grade_code);
					}					
				}
				statSQL.append("group by t1."+statItem+" order by t1."+statItem);
				
	    		pdao.setSql(statSQL.toString());
	    		pdao.setBindValues(paramList);
	    		Element statResult = pdao.executeQuerySql(0,-1);
	    		
	    		Element statElement = new Element("StatResult");
	    		statElement.addContent(statResult);
	    		rec.addContent(statElement);				
			}
			
			xmlDocUtil.getResponse().addContent(result);
			xmlDocUtil.setResult("0");					
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		finally{
			pdao.releaseConnection();
		}		
		
	}
	/**
	 * 取试题篮数量
	 *
	 */
	public void getSelectPraxesCount(){
		String userid = xmlDocUtil.getSession().getChildText("userid");
		Element reqElement =  xmlDocUtil.getRequestData();
		String paper_id = reqElement.getChildText("paper_id");
		String subjectid = reqElement.getChildText("subjectid");
		String gradecode = reqElement.getChildText("gradecode");
		
		StringBuffer strSQL = new StringBuffer();
		ArrayList<Object> paramList = new ArrayList<Object>();
		if (StringUtil.isNotEmpty(paper_id)){
			strSQL.append("select count(t1.paper_praxes_id) as praxes_count");
			strSQL.append(" from learn_paper_praxes t1 ");
			strSQL.append(" where t1.valid='Y'");
			strSQL.append(" and t1.paper_id=?");
			paramList.add(paper_id);
		}
		else{
			strSQL.append("select  count(t1.praxes_id) as praxes_count");
			strSQL.append(" from temp_praxes t1,learn_praxes t2");
			strSQL.append(" where t1.praxes_id=t2.praxes_id");
			strSQL.append(" and t1.userid=?");
			paramList.add(userid);
			if (StringUtil.isNotEmpty(subjectid)){
				strSQL.append(" and t2.subject_id=?");
				paramList.add(subjectid);
			}
			if (StringUtil.isNotEmpty(gradecode)){
				strSQL.append(" and t2.grade_code=?");
				paramList.add(gradecode);
			}			
		}
		PlatformDao pdao = new PlatformDao();
		try{
			pdao.setSql(strSQL.toString());
			pdao.setBindValues(paramList);
			Element result = pdao.executeQuerySql(0, -1);
			
			xmlDocUtil.getResponse().addContent(result);
			xmlDocUtil.setResult("0");					
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		finally{
			pdao.releaseConnection();
		}		
		
	}
	/**
	 * 清空试卷题目
	 *
	 */
	private void clearPaperPraxes(String praxes_type){
		String username = xmlDocUtil.getSession().getChildText("username");
		Element reqElement =  xmlDocUtil.getRequestData();
		String paper_id = reqElement.getChildText("paper_id");
		
		StringBuffer strSQL = new StringBuffer();
		strSQL.append("update learn_paper_praxes set");
		strSQL.append(" modify_by=?,modify_date=?,valid='N'");
		strSQL.append(" where paper_id=?");
		ArrayList<Object> paramList = new ArrayList<Object>();
		paramList.add(username);
		paramList.add(SqlTypes.getConvertor("Timestamp").convert(DatetimeUtil.getNow(""), "yyyy-MM-dd HH:mm:ss"));
		paramList.add(paper_id);
		
		// 增加试题类型
		if(StringUtil.isNotEmpty(praxes_type)){
			strSQL.append(" and praxes_type=?");
			paramList.add(praxes_type);
		}
		
		PlatformDao pdao = new PlatformDao();
		try{
			pdao.setSql(strSQL.toString());
			pdao.setBindValues(paramList);
			pdao.executeTransactionSql();
			
			xmlDocUtil.setResult("0");	
			xmlDocUtil.writeHintMsg("00107", "试题已经从试题篮中移除!");				
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		finally{
			pdao.releaseConnection();
		}		
	}
	/**
	 * 删除试题蓝中的试题
	 *
	 */
	public void delSelectPraxes(){
		Element reqElement =  xmlDocUtil.getRequestData();
		String userid = xmlDocUtil.getSession().getChildText("userid");
		String userName = xmlDocUtil.getSession().getChildText("username");
		String paper_id = reqElement.getChildText("paper_id");
		String praxes_id = reqElement.getChildText("praxes_id");
		
		StringBuffer strSQL = new StringBuffer();
		ArrayList<Object> paramList = new ArrayList<Object>();
		if (StringUtil.isEmpty(paper_id)){
		    strSQL.append("delete from temp_praxes");
		    strSQL.append(" where userid=?");
		    strSQL.append(" and praxes_id=?");		
		
		    paramList.add(userid);		
		    paramList.add(praxes_id);
		}
		else{
		    strSQL.append("update learn_paper_praxes set valid='N',modify_by='"+userName+"',modify_date=now()");
		    strSQL.append(" where paper_id=?");
		    strSQL.append(" and praxes_id=?");	
		    paramList.add(paper_id);		
		    paramList.add(praxes_id);		    
		}
		
		PlatformDao pdao = new PlatformDao();
		try{
			pdao.setSql(strSQL.toString());
			pdao.setBindValues(paramList);
			pdao.executeTransactionSql();
			
			xmlDocUtil.setResult("0");	
			xmlDocUtil.writeHintMsg("00107", "试题已经从试题篮中移除!");				
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		finally{
			pdao.releaseConnection();
		}		
	}
	/**
	 * 清空试题蓝中的试题
	 *
	 */
	public void clearSelectPraxes(){
		Element reqElement =  xmlDocUtil.getRequestData();
		String paper_id = reqElement.getChildText("paper_id");
		String praxes_type = reqElement.getChildText("praxes_type");
		if (StringUtil.isNotEmpty(paper_id)){
			this.clearPaperPraxes(praxes_type);
		}
		else{
			PlatformDao pdao = new PlatformDao();
			try{
				clearPraxesInfo(pdao,praxes_type);
				xmlDocUtil.setResult("0");	
				xmlDocUtil.writeHintMsg("00107", "试题蓝已经清空!");				
			}
			catch(Exception ex){
				ex.printStackTrace();
			}
			finally{
				pdao.releaseConnection();
			}
		}
	}
	/**
	 * 增加试题(含选项)
	 */
	public void addPraxes(){
		Element reqElement =  xmlDocUtil.getRequestData();
		
        try{
			String praxes_id = addPraxesOption(reqElement);
			
			String[] returnData = { "praxes_id"};
			Element resData = XmlDocPkgUtil.createMetaData(returnData);	
			resData.addContent(XmlDocPkgUtil.createRecord(returnData,new String[] {praxes_id}));			
			
			xmlDocUtil.getResponse().addContent(resData);			
			xmlDocUtil.setResult("0");	
			xmlDocUtil.writeHintMsg("00103", "新增试题成功!");				
        }
        catch(Exception e){
			e.printStackTrace();			
			log4j.logError("[习题管理-新增试题]"+e.getMessage());
			xmlDocUtil.writeHintMsg("习题管理-新增试题失败!");
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);        	
        }        
	}
	/**
	 * 增加试题及选项
	 * @param requestData
	 * @return 试题ID
	 * @throws Exception
	 */
	public String addPraxesOption(Element requestData) throws Exception{
		PlatformDao pdao = new PlatformDao(true);
		try {
			pdao.beginTransaction();
			Element praxesRec = ConfigDocument.createRecordElement("yuexue", "learn_praxes");
			XmlDocPkgUtil.copyValues(requestData, praxesRec, 0, true);
			String praxes_id = pdao.insertOneRecordSeqPk(praxesRec).toString();
			
			String praxes_type = requestData.getChildText("praxes_type");
			
    		//如果单选或多选题,则增加选项内容
    		if ("20".equals(praxes_type) || "30".equals(praxes_type)){			
				List optionList = requestData.getChildren("option_content");
				//List orderList  = requestData.getChildren("display_order");
				List rightList  = requestData.getChildren("is_right");
				
				for (int i = 0; i < optionList.size() ;i++){
					Element optionRec = ConfigDocument.createRecordElement("yuexue", "learn_options");
					XmlDocPkgUtil.setChildText(optionRec, "praxes_id", praxes_id);					
					XmlDocPkgUtil.setChildText(optionRec, "option_content", ((Element)optionList.get(i)).getText());
					XmlDocPkgUtil.setChildText(optionRec, "display_order", ""+(i+1));
					XmlDocPkgUtil.setChildText(optionRec, "is_right", ((Element)rightList.get(i)).getText());
					XmlDocPkgUtil.setChildText(optionRec, "valid", "Y");
					pdao.insertOneRecordSeqPk(optionRec);
				}
    		}
			pdao.commitTransaction();	
			//写入试题篮表
			Element tempRec = ConfigDocument.createRecordElement("yuexue", "temp_praxes");
			XmlDocPkgUtil.setChildText(tempRec, "praxes_id", praxes_id);					
			pdao.insertOneRecord(tempRec);
			return praxes_id;		
		}
		catch (Exception e) {
			pdao.rollBack();
			throw e;			
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
		//String paper_id = reqElement.getChildText("paper_id");
		String subject_id = reqElement.getChildText("subject_id");
		String grade_code = reqElement.getChildText("grade_code");
		//String folder_id = reqElement.getChildText("folder_id");
		String userid = xmlDocUtil.getSession().getChildText("userid");
		
		PlatformDao pdao = new PlatformDao();
	    try{	    	
	    	
			StringBuffer strSQL = new StringBuffer();
			strSQL.append("select t2.subjname,t3.k_point_name,");
			strSQL.append(" getParamDesc('c_praxes_type',t1.praxes_type) as praxes_type_desc,");
			strSQL.append(" (select count(sm.paper_praxes_id) from learn_paper_praxes sm where sm.valid='Y' and sm.praxes_id=t1.praxes_id) as use_count,");
			strSQL.append(" t1.*");
			strSQL.append(" ,(case when t1.userid='").append(userid).append("' then 1 else 0 end) as author");  // 添加作者标识 lhbo 2016-01-16
			strSQL.append(" from learn_praxes t1 left join base_subject t2 on t1.subject_id=t2.subjectid left join  base_knowledge_point t3 on t1.k_point_id=t3.k_point_id");
			strSQL.append(" where t1.valid='Y' AND EXISTS ");
			strSQL.append(" (SELECT NULL FROM temp_praxes st WHERE st.praxes_id = t1.praxes_id AND st.userid = ?)");			
			
			ArrayList<Object> paramList = new ArrayList<Object>();
			paramList.add(userid);
			
			if (StringUtil.isNotEmpty(subject_id)){
				strSQL.append(" and t1.subject_id=?");
				paramList.add(subject_id);
			}
			if (StringUtil.isNotEmpty(grade_code)){
				strSQL.append(" and t1.grade_code=?");
				paramList.add(grade_code);				
			}
			
	    	pdao.setSql(strSQL.toString());
	    	pdao.setBindValues(paramList);
	    	Element result = pdao.executeQuerySql(xmlDocUtil.getPageSize(), xmlDocUtil.getPageNo());
	    	
	    	String optionSQL = "select * from learn_options where praxes_id=? and valid='Y' order by display_order";
	    	
	    	List list = result.getChildren("Record");
	    	for (int i = 0; i < list.size() ;i++){
	    		Element praxesRec = (Element)list.get(i);
	    		String praxes_type = praxesRec.getChildText("praxes_type");
	    		//如果单选或多选题,则增加选项内容
	    		if ("20".equals(praxes_type) || "30".equals(praxes_type)){
		    		ArrayList<Object> optionParam = new ArrayList<Object>();
		    		optionParam.add(praxesRec.getChildText("praxes_id"));
		    		
		    		pdao.setSql(optionSQL);
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
	 * 生成试卷
	 */
	/**
	 * 
	 */
	public void savePaper(){
		Element reqElement =  xmlDocUtil.getRequestData();
		Element sessionEle =  xmlDocUtil.getSession();
		List ppList = reqElement.getChildren("paper_praxes_id");//获得试题ID
		List praxesList = reqElement.getChildren("praxes_id");//获得试题ID
		List scoreList = reqElement.getChildren("praxes_score");//获得试题分值
		List orderList = reqElement.getChildren("display_order");//获得试题分值
		//List difficultyList = reqElement.getChildren("difficulty_level");//难度系数
		//List discriminationList = reqElement.getChildren("discrimination");//区分度
		
		PlatformDao pdao = new PlatformDao();
		String req_paper_id=reqElement.getChildTextTrim("paper_id");//试卷ID
		String attachment_id=reqElement.getChildTextTrim("attachment_id");//附件ID
		String paper_id = req_paper_id;
		StringBuffer strSQL = new StringBuffer();
		
		if (StringUtil.isEmpty(req_paper_id)){
		    strSQL.append("select * from learn_praxes where praxes_id = ?");	
		}
		else{
			strSQL.append("select * from learn_paper_praxes where valid='Y' and paper_id=? and paper_praxes_id=? and praxes_id = ?");
		}
		
		try {
			pdao.beginTransaction();
			//存储试卷信息
			Element examinationRec = ConfigDocument.createRecordElement("yuexue", "learn_examination_paper");
			XmlDocPkgUtil.copyValues(reqElement, examinationRec, 0, true);
			XmlDocPkgUtil.setChildText(examinationRec, "valid", "Y");
			String[] returnData = { "paper_id" };
			Element resData = XmlDocPkgUtil.createMetaData(returnData);
			if(StringUtil.isNotEmpty(req_paper_id)){
				XmlDocPkgUtil.setChildText(examinationRec, "modify_by", sessionEle.getChildText("username"));
				XmlDocPkgUtil.setChildText(examinationRec, "modify_date", DatetimeUtil.getNow(""));
				pdao.updateOneRecord(examinationRec);
				resData.addContent(XmlDocPkgUtil.createRecord(returnData, new String[] { null }));
			}else{
				paper_id = (String)pdao.insertOneRecordSeqPk(examinationRec);
				resData.addContent(XmlDocPkgUtil.createRecord(returnData, new String[] { paper_id }));
			}
			if(StringUtil.isNotEmpty(attachment_id)){
				//解除绑定信息
				pdao.setSql("update learn_paper_attachment set valid = 'N' where paper_id = ? ");
				ArrayList<String> barry = new ArrayList<String>();
				barry.add(paper_id);
				pdao.setBindValues(barry);
				pdao.executeTransactionSql();
				//绑定附件信息
				Element attachmentRec = ConfigDocument.createRecordElement("yuexue", "learn_paper_attachment");
				XmlDocPkgUtil.setChildText(attachmentRec, "attachment_id", attachment_id);
				XmlDocPkgUtil.setChildText(attachmentRec, "paper_id", paper_id);
				XmlDocPkgUtil.setChildText(attachmentRec, "valid", "Y");
				pdao.insertOneRecordSeqPk(attachmentRec);
			}
			for (int i = 0; i < praxesList.size() ;i++){
				String praxes_id = ((Element)praxesList.get(i)).getText();
				
				ArrayList<Object> paramList = new ArrayList<Object>();
				if (StringUtil.isEmpty(req_paper_id)){
					paramList.add(praxes_id);
				}
				else{
					String paper_praxes_id = ((Element)ppList.get(i)).getText();
					paramList.add(req_paper_id);
					paramList.add(paper_praxes_id);
					paramList.add(praxes_id);
				}
				pdao.setSql(strSQL.toString());
				pdao.setBindValues(paramList);
				Element result = pdao.executeQuerySql(0,-1);
				Element praxesRec = result.getChild("Record");
				XmlDocPkgUtil.setChildText(praxesRec, "score", ((Element)scoreList.get(i)).getText());
				XmlDocPkgUtil.setChildText(praxesRec, "display_order", ((Element)orderList.get(i)).getText());
				if (StringUtil.isEmpty(req_paper_id)){					
					writePraxesToPaper(pdao,praxesRec,i+1,paper_id,sessionEle);
				}
				else{
					Element paperPraxesRec = ConfigDocument.createRecordElement("yuexue", "learn_paper_praxes");
					XmlDocPkgUtil.copyValues(praxesRec, paperPraxesRec, 0, true);
					XmlDocPkgUtil.setChildText(paperPraxesRec, "modify_by", sessionEle.getChildText("username"));
					XmlDocPkgUtil.setChildText(paperPraxesRec, "modify_date", DatetimeUtil.getNow(""));
					pdao.updateOneRecord(paperPraxesRec);					
				}
			}	    	
	    	clearPraxesInfo(pdao,null);
			
	    	pdao.commitTransaction();
	    	xmlDocUtil.getResponse().addContent(resData);
	    	xmlDocUtil.setResult("0");	
			xmlDocUtil.writeHintMsg("00105", "生成试卷成功!");	
		} catch (Exception e) {
			pdao.rollBack();
			e.printStackTrace();
		}finally{
			pdao.releaseConnection();
		}
	}
	
	
	
	/**
	 * 将试题写入问卷中
	 * @param pdao
	 * @param result
	 * @return 试卷问题ID
	 */
	private String writePraxesToPaper(PlatformDao pdao,Element praxesRec,int praxesDisplayOrder,String paper_id,Element sessionEle) throws Exception{
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
	
	//清空试题篮
	private void clearPraxesInfo(PlatformDao pdao,String praxes_type) throws Exception{
		Element sessionEle =  xmlDocUtil.getSession();
		String currUserId=sessionEle.getChildText("userid");
		try {
			StringBuffer strSQL=new StringBuffer();
			ArrayList<Object> paramList = new ArrayList<Object>();
			if(StringUtil.isNotEmpty(praxes_type)){
				strSQL.append("delete t1 from temp_praxes t1 where 1=1 ");
				strSQL.append(" and exists (select null from learn_praxes t2 where t2.praxes_id = t1.praxes_id and t2.praxes_type=?)");
				strSQL.append(" and t1.userid=?");
				paramList.add(praxes_type);
				paramList.add(currUserId);
			} else {
				strSQL.append("delete from temp_praxes where userid=?");
				paramList.add(currUserId);
			}
			
			pdao.setSql(strSQL.toString());
			pdao.setBindValues(paramList);
			pdao.executeTransactionSql();
			
		} catch (SQLException e) {
			throw e;
		}
	}
}
