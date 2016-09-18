package cn.com.bright.yuexue.teach;

import java.io.File;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

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
import cn.com.bright.yuexue.converter.OfficeToHtml;
import cn.com.bright.yuexue.sell.preview.DocConverter;
import cn.com.bright.yuexue.util.PraxesBean;
import cn.com.bright.yuexue.util.Word2PraxesUtil;

/**
 * <p>Title:题库管理</p>
 * <p>Description: 题库管理</p>
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
 * <p> zhangxq    2014/07/21       1.0          build this moudle </p>
 *     
 */
public class Praxes {
	private XmlDocPkgUtil xmlDocUtil = null;
	private String upFolder = "/upload/doc/praxes/";
	private Log log4j = new Log(this.getClass().toString());

    /**
     * 动态委派入口
     * @param request xmlDoc 
     * @return response xmlDoc
     */
	public Document doPost(Document xmlDoc) {
		xmlDocUtil = new XmlDocPkgUtil(xmlDoc);
		String action = xmlDocUtil.getAction();
		
		if ("addPraxes".equals(action)){
			addPraxes();
		}
		else if ("updatePraxes".equals(action)){
			updatePraxes();
		}
		else if ("deletePraxes".equals(action)){
			deletePraxes();
		}
		else if ("queryPraxesOptions".equals(action)){
			queryPraxesOptions();
		}
		else if ("importPraxes".equals(action)){
			importPraxes();
		}
		else if ("getPraxesCount".equals(action)){
			getPraxesCount();
		}
		else if ("saveChooseRule".equals(action)){
			saveChooseRule();
		}
		
		return xmlDoc;
	}
	/**
	 * 删除题库试题
	 */
	public void deletePraxes(){
		Element reqElement =  xmlDocUtil.getRequestData();
		String userid = xmlDocUtil.getSession().getChildText("userid");
		String username = xmlDocUtil.getSession().getChildText("username");
		List praxesIdList = reqElement.getChildren("praxes_id");
		PlatformDao pdao = new PlatformDao();
		try {
			StringBuffer strSQL = new StringBuffer();
			strSQL.append("update learn_praxes set valid='N',modify_date=now(),");
			strSQL.append(" modify_by='"+username+"'");
			strSQL.append(" where praxes_id=? and userid=?");
			long upCount = 0 ;
			for (int i = 0; i < praxesIdList.size() ;i++){
				String praxesId = ((Element)praxesIdList.get(i)).getText();
				ArrayList<Object> paramList = new ArrayList<Object>();
				paramList.add(praxesId);
				paramList.add(userid);
				
				pdao.setSql(strSQL.toString());
				pdao.setBindValues(paramList);
				upCount += pdao.executeTransactionSql();
				
				// 同时删除已选中至试题栏中的试题 lhbo 2016-01-16
				strSQL.setLength(0);
				strSQL.append("delete from temp_praxes");
			    strSQL.append(" where userid=?");
			    strSQL.append(" and praxes_id=?");
			    paramList.clear();
			    paramList.add(userid);		
			    paramList.add(praxesId);
			    pdao.setSql(strSQL.toString());
				pdao.setBindValues(paramList);
				pdao.executeTransactionSql();
			}
			if (upCount>0){
				xmlDocUtil.setResult("0");	
				xmlDocUtil.writeHintMsg("00107", "删除试题成功!");					
			}
			else{
				xmlDocUtil.writeErrorMsg("30205","只能删除自己上传的试题!");
				xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE); 				
			}
		}
		catch (Exception e) {
			e.printStackTrace();			
			log4j.logError("[题库管理-删除试题]"+e.getMessage());
			xmlDocUtil.writeHintMsg("题库管理-删除试题!");
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
		} finally {
			pdao.releaseConnection();
		}		
	}
	
	/**
	 * 导入试题
	 *
	 */
	public void importPraxes(){
		Element reqElement =  xmlDocUtil.getRequestData();
		String userid = xmlDocUtil.getSession().getChildText("userid");
		String username = xmlDocUtil.getSession().getChildText("username");
		String deptcode = xmlDocUtil.getSession().getChildText("deptcode");
		String subject_id = reqElement.getChildText("subject_id");
		String grade_code = reqElement.getChildText("grade_code");
		String k_point_id = reqElement.getChildText("k_point_id");
		String attachment_id = reqElement.getChildText("attachment_id");
		
		PlatformDao pdao = new PlatformDao(true);
        try{
        	// 获取附件地址
        	String wordFilePath = "";
        	ArrayList<Object> param = new ArrayList<Object>();
        	param.add(attachment_id);
    		pdao.setSql("select * from learn_attachment where attachment_id=?");
    		pdao.setBindValues(param);
    		Element attachmentResult = pdao.executeQuerySql(0,-1);
	    	List attachmenList = attachmentResult.getChildren("Record");
	    	if(attachmenList!=null && attachmenList.size()>0){
	    		Element attachmentRec = (Element)attachmenList.get(0);
	    		wordFilePath = attachmentRec.getChildTextTrim("file_path");
	    		wordFilePath = FileUtil.getWebPath() + wordFilePath;
	    	}
	    	
	    	boolean isSuccConverter = false;
	    	if(StringUtil.isNotEmpty(wordFilePath)){
		    	DocConverter dc= new DocConverter();
		    	File docFile = new File(wordFilePath);
		    	String fileNameNoExt = docFile.getName();
		    	fileNameNoExt = fileNameNoExt.substring(0, fileNameNoExt.lastIndexOf("."));
		    	
		    	File htmlFile = new File(docFile.getParent() + File.separatorChar + fileNameNoExt +".html");
		    	isSuccConverter = dc.doc2html(docFile, htmlFile);
				if(isSuccConverter){
		        	List<PraxesBean> praxesList = Word2PraxesUtil.getQuestionListToHtml(htmlFile);
		        	
					pdao.beginTransaction();
					pdao.setSql("delete from temp_praxes where userid='"+userid+"'");
					pdao.executeTransactionSql();
					
					String[] option = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
					for(PraxesBean praxe : praxesList){
						String praxes_title = praxe.getTitle();
						String praxes_type = praxe.getType();
						String praxes_answer = praxe.getAnswer();
						String praxes_desc = praxe.getDesc();
						int praxes_num = praxe.getNum();
						boolean isChoose = false;
						if ("20".equals(praxes_type) || "30".equals(praxes_type)){
							isChoose = true; //单选或多选题
						}
						
						//题库表
						Element praxesRec = ConfigDocument.createRecordElement("yuexue", "learn_praxes");
						XmlDocPkgUtil.setChildText(praxesRec, "subject_id", subject_id);
						XmlDocPkgUtil.setChildText(praxesRec, "grade_code", grade_code);
						XmlDocPkgUtil.setChildText(praxesRec, "k_point_id", k_point_id);
						XmlDocPkgUtil.setChildText(praxesRec, "praxes_type", praxes_type);
						XmlDocPkgUtil.setChildText(praxesRec, "praxes_content", praxes_title);
						XmlDocPkgUtil.setChildText(praxesRec, "score", "10");
						XmlDocPkgUtil.setChildText(praxesRec, "share_level", "90");
						XmlDocPkgUtil.setChildText(praxesRec, "deptcode", deptcode);
						XmlDocPkgUtil.setChildText(praxesRec, "userid", userid);
						XmlDocPkgUtil.setChildText(praxesRec, "create_by", username);
						XmlDocPkgUtil.setChildText(praxesRec, "create_date", DatetimeUtil.getNow("yyyy-MM-dd HH:mm:ss"));
						XmlDocPkgUtil.setChildText(praxesRec, "right_result", isChoose ? "":praxes_answer);
						XmlDocPkgUtil.setChildText(praxesRec, "praxes_hint", praxes_desc);
						XmlDocPkgUtil.setChildText(praxesRec, "valid", "Y");
						String praxes_id = pdao.insertOneRecordSeqPk(praxesRec).toString();
						
			    		//如果单选或多选题,则增加选项内容
			    		if (isChoose){
							for (int i = 0; i < praxes_num ;i++){
								Element optionRec = ConfigDocument.createRecordElement("yuexue", "learn_options");
								XmlDocPkgUtil.setChildText(optionRec, "praxes_id", praxes_id);					
								XmlDocPkgUtil.setChildText(optionRec, "option_content", option[i]);
								XmlDocPkgUtil.setChildText(optionRec, "display_order", ""+(i+1));
								XmlDocPkgUtil.setChildText(optionRec, "is_right", praxes_answer.indexOf(option[i]) > -1 ? "1":null);
								XmlDocPkgUtil.setChildText(optionRec, "valid", "Y");
								pdao.insertOneRecordSeqPk(optionRec);
							}
			    		}
			    		
			    		//添加到选题表
			    		Element tempPraxesRec = ConfigDocument.createRecordElement("yuexue", "temp_praxes");
						XmlDocPkgUtil.setChildText(tempPraxesRec, "praxes_id", praxes_id);
						XmlDocPkgUtil.setChildText(tempPraxesRec, "userid", userid);
						pdao.insertOneRecordSeqPk(tempPraxesRec);
					}
					pdao.commitTransaction();
					
					xmlDocUtil.setResult("0");	
					xmlDocUtil.writeHintMsg("00104", "导入试题成功!");
	        	}
	    	}
			if(!isSuccConverter) {
				xmlDocUtil.setResult("0");	
				xmlDocUtil.writeHintMsg("00104", "导入试题失败[文档转换失败]!");
        	}
        }
        catch(ConnectException e){
        	pdao.rollBack();
			e.printStackTrace();
			log4j.logError("[习题管理-导入试题]"+e.getMessage());
			xmlDocUtil.writeErrorMsg("习题管理-导入试题失败，转换工具出现异常，请联系管理人员!");
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);    	
        } 
        catch (com.artofsolving.jodconverter.openoffice.connection.OpenOfficeException e) {
        	pdao.rollBack();
			e.printStackTrace();
			log4j.logError("[习题管理-导入试题]"+e.getMessage());
			xmlDocUtil.writeErrorMsg("习题管理-导入试题失败，请联系管理人员!<br><br>如果试用WPS或Word 2003编辑的试卷文档，请另存为2007/2010版本Word文档重新上传试试。");
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
		}
        catch(Exception e){
        	pdao.rollBack();
			e.printStackTrace();			
			log4j.logError("[习题管理-导入试题]"+e.getMessage());
			xmlDocUtil.writeErrorMsg("习题管理-导入试题失败!");
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);        	
        } finally {
			pdao.releaseConnection();
		}
	}
	
	private String moveFile(Element upFileEle) throws Exception {
		if(null == upFileEle || StringUtil.isEmpty(upFileEle.getText())) return null;
		String upPath = upFileEle.getText();
		File upFile = new File(FileUtil.getPhysicalPath(upPath));
		//移动
		String urlPath = upFolder+FileUtil.getFileName(upPath);
		String mvFolder = FileUtil.getWebPath()+upFolder;
		String mvPath = FileUtil.getWebPath()+urlPath;
		
		FileUtil.createDirs(mvFolder, true);
		FileUtil.moveFile(upFile, new File(mvPath));
		upFile.delete();
		
		return urlPath;
    }
	
	/**
	 * 保存选题规则(含新增和修改)
	 *
	 */
	public void saveChooseRule(){
		String userName = xmlDocUtil.getSession().getChildTextTrim("username");
		Element reqElement =  xmlDocUtil.getRequestData();
		String paper_id = reqElement.getChildText("paper_id");
		String folder_code = reqElement.getChildText("folder_code");
		List ruleIDList = reqElement.getChildren("rule_id");
		
		PlatformDao pdao = new PlatformDao();
		try {
			pdao.beginTransaction();
	    	
			for (int i=0;i<ruleIDList.size();i++){
				String ruleID = ((Element)ruleIDList.get(i)).getText();
				Element chooseRuleRec = ConfigDocument.createRecordElement("yuexue", "learn_choose_rule");
				XmlDocPkgUtil.copyValues(reqElement, chooseRuleRec, i, true);
				XmlDocPkgUtil.setChildText(chooseRuleRec, "paper_id", paper_id);
				XmlDocPkgUtil.setChildText(chooseRuleRec, "folder_code", folder_code);
                if 	(ruleID.indexOf("addRule_")>-1){
                	chooseRuleRec.removeChild("rule_id");                	
                	pdao.insertOneRecord(chooseRuleRec);
                }
                else{
					XmlDocPkgUtil.setChildText(chooseRuleRec, "modify_by", userName);
					XmlDocPkgUtil.setChildText(chooseRuleRec, "modify_date", DatetimeUtil.getNow(""));                	
                	pdao.updateOneRecord(chooseRuleRec);                	
                }								 
			}			
			
			pdao.commitTransaction();
	    	xmlDocUtil.setResult("0");	
	    	xmlDocUtil.writeHintMsg("00103", "保存选题规则成功!");
		}
		catch (Exception e) {
			pdao.rollBack();
			e.printStackTrace();			
			log4j.logError("[习题管理-保存选题规则]"+e.getMessage());
			xmlDocUtil.writeHintMsg("习题管理-保存选题规则失败!");
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE); 
            			
		} finally {
			pdao.releaseConnection();
		}			
	}
	/**
	 * 获取题库中试题数量
	 *
	 */
	public void getPraxesCount(){
		Element reqElement =  xmlDocUtil.getRequestData();
		String praxes_type = reqElement.getChildText("praxes_type");
		String folder_code = reqElement.getChildText("folder_code");
		String difficulty_all = reqElement.getChildText("difficulty_all");
		String difficulty_min = reqElement.getChildText("difficulty_min");
		String difficulty_max = reqElement.getChildText("difficulty_max");
		
		ArrayList<Object> praxesParam = new ArrayList<Object>();
		StringBuffer praxesSQL = new StringBuffer();
		praxesSQL.append("select count(t1.praxes_id) as praxes_count from learn_praxes t1,base_folder_point t2,base_book_folder t3");
		praxesSQL.append(" where t1.valid='Y' and t1.k_point_id=t2.k_point_id and t2.folder_id=t3.folder_id and t1.praxes_type <> '90' ");
		
		if (StringUtil.isNotEmpty(praxes_type)){
			praxesSQL.append(" and t1.praxes_type = ?");
			praxesParam.add(praxes_type);	    			
		}
		if (StringUtil.isNotEmpty(folder_code)){
			praxesSQL.append(" and t3.folder_code like ?");
			praxesParam.add("%"+folder_code+"%");	    			
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
		PlatformDao pdao = new PlatformDao(true);
		try {
	    	pdao.setSql(praxesSQL.toString());
	    	pdao.setBindValues(praxesParam);
	    	Element result = pdao.executeQuerySql(0, -1);
	    	
	    	xmlDocUtil.getResponse().addContent(result);
	    	xmlDocUtil.setResult("0");			
		}
		catch (Exception e) {
			e.printStackTrace();			
			log4j.logError("[习题管理-获取试题数量]"+e.getMessage());
			xmlDocUtil.writeHintMsg("习题管理-获取试题数量失败!");
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE); 
            			
		} finally {
			pdao.releaseConnection();
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
	 * 修改试题(含选项)
	 */
	public void updatePraxes(){
		String userName = xmlDocUtil.getSession().getChildTextTrim("username");	
		Element reqElement =  xmlDocUtil.getRequestData();
		PlatformDao pdao = new PlatformDao();
		try {
			String praxes_id = reqElement.getChildText("praxes_id");
			pdao.beginTransaction();
			Element praxesRec = ConfigDocument.createRecordElement("yuexue", "learn_praxes");
			XmlDocPkgUtil.copyValues(reqElement, praxesRec, 0, true);
			XmlDocPkgUtil.setChildText(praxesRec, "modify_by", userName);
			XmlDocPkgUtil.setChildText(praxesRec, "modify_date", DatetimeUtil.getNow(""));
			pdao.updateOneRecord(praxesRec);
			
			String praxes_type = reqElement.getChildText("praxes_type");

    		//如果单选或多选题,则增加选项内容
    		if ("20".equals(praxes_type) || "30".equals(praxes_type)){
    			String deleteOrginalSQL = "update learn_options set valid='N' where praxes_id=? and valid='Y'";
    			ArrayList<Object> paramList = new ArrayList<Object>();
    			paramList.add(praxes_id); 
    			pdao.setSql(deleteOrginalSQL);
    			pdao.setBindValues(paramList);
    			pdao.executeTransactionSql();    			
    			
				List contentList = reqElement.getChildren("option_content");
				List orderList  = reqElement.getChildren("display_order");
				List rightList  = reqElement.getChildren("is_right");
				List optionIDList  = reqElement.getChildren("option_id");
				
				for (int i = 0; i < contentList.size() ;i++){
					Element optionRec = ConfigDocument.createRecordElement("yuexue", "learn_options");
					Element newEle = new Element("RemoveChild");
					XmlDocPkgUtil.copyValues(newEle, optionRec, 0, true);
					
					XmlDocPkgUtil.setChildText(optionRec, "option_content", ((Element)contentList.get(i)).getText());
					XmlDocPkgUtil.setChildText(optionRec, "display_order", ((Element)orderList.get(i)).getText());
					XmlDocPkgUtil.setChildText(optionRec, "is_right", ((Element)rightList.get(i)).getText());
					XmlDocPkgUtil.setChildText(optionRec, "praxes_id", praxes_id);
					XmlDocPkgUtil.setChildText(optionRec, "valid", "Y");
					Element el = (Element)optionIDList.get(i);				
					if ((el == null ) || StringUtil.isEmpty(el.getText())){
					    pdao.insertOneRecordSeqPk(optionRec);
					}
					else{
						XmlDocPkgUtil.setChildText(optionRec, "option_id", el.getText());						
						XmlDocPkgUtil.setChildText(optionRec, "modify_by", userName);
						XmlDocPkgUtil.setChildText(optionRec, "modify_date", DatetimeUtil.getNow(""));					
						pdao.updateOneRecord(optionRec);
					}
				}
    		}
    		/**
			String deleteSQL = "update learn_options set valid='N' where option_id=?";
    		List deleteOptionList  = reqElement.getChildren("delete_option_id");
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
			xmlDocUtil.writeHintMsg("00105", "修改试题成功!");			
		}
		catch (Exception e) {
			pdao.rollBack();
			e.printStackTrace();			
			log4j.logError("[习题管理-修改试题]"+e.getMessage());
			xmlDocUtil.writeHintMsg("习题管理-修改试题失败!");
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
		} finally {
			pdao.releaseConnection();
		}		
	}
	/**
	 * 检索试题
	 * 每个题目下有
	 *
	 */
	public void queryPraxesOptions(){
		String optionStr = "A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z";
		String[] optionArray = optionStr.split(",");		
		String userid   = xmlDocUtil.getSession().getChildTextTrim("userid");	
		String deptcode = xmlDocUtil.getSession().getChildTextTrim("deptcode");		
		
		Element reqElement =  xmlDocUtil.getRequestData();
		ArrayList<Object> paramList = new ArrayList<Object>();
		
		String qry_folder_code = reqElement.getChildText("qry_folder_code");
		String paper_id = reqElement.getChildText("paper_id");
		
		StringBuffer strSQL = new StringBuffer();
		strSQL.append("select sm.k_point_name,st.*,");
		strSQL.append(" (case when st.userid='").append(userid).append("' then 1 else 0 end) as author,");  // 添加作者标识 lhbo 2016-01-16
		strSQL.append(" (select count(t1.paper_praxes_id) from learn_paper_praxes t1 where t1.valid='Y' and t1.praxes_id=st.praxes_id) as use_count,");
		if (StringUtil.isEmpty(paper_id)){
		    strSQL.append(" (select count(t2.temp_id) from temp_praxes t2 where t2.praxes_id=st.praxes_id and t2.userid=?) as select_count,");
		    paramList.add(userid);
		}
		else{
			strSQL.append(" (select count(t2.paper_praxes_id) from learn_paper_praxes t2 where t2.praxes_id=st.praxes_id and t2.valid='Y' and t2.paper_id=?) as select_count,");
			 paramList.add(paper_id);
		}
		strSQL.append(" getParamDesc('c_praxes_type',st.praxes_type) as praxes_type_desc");				
		strSQL.append(" from learn_praxes st ,base_knowledge_point sm");
		
		if (StringUtil.isNotEmpty(qry_folder_code)){
			strSQL.append(",base_folder_point sp,base_book_folder sq");
		}
		strSQL.append(" where st.valid = 'Y' and st.k_point_id = sm.k_point_id");
		if (StringUtil.isNotEmpty(qry_folder_code)){
		    strSQL.append(" and sm.k_point_id = sp.k_point_id and sp.folder_id = sq.folder_id");
		}
		//私有
		strSQL.append(" and ( (st.share_level = 10 and st.userid='"+userid+"')");
		//校内共享
		strSQL.append("       or (st.share_level =50 and st.deptcode ='"+deptcode+"' )");
		//完全公开
		strSQL.append("       or (st.share_level =90))");
		
		String qry_my_praxes = reqElement.getChildText("qry_my_praxes");
		if (StringUtil.isNotEmpty(qry_my_praxes)){
		    strSQL.append(" and st.userid='"+userid+"'");
		}
		
		String qry_subject_id = reqElement.getChildText("qry_subject_id");
		if (StringUtil.isNotEmpty(qry_subject_id)){
		    strSQL.append(" and st.subject_id=?");
		    paramList.add(qry_subject_id);
		}
		String qry_grade_id = reqElement.getChildText("qry_grade_code");
		if (StringUtil.isNotEmpty(qry_grade_id)){
		    strSQL.append(" and st.grade_code=?");
		    paramList.add(qry_grade_id);
		}		
		String qry_point_id = reqElement.getChildText("qry_point_id");
		if (StringUtil.isNotEmpty(qry_point_id)){
		    strSQL.append(" and st.k_point_id=?");
		    paramList.add(qry_point_id);
		}		
		String qry_point_name = reqElement.getChildText("qry_point_name");
		if (StringUtil.isNotEmpty(qry_point_name)){
		    strSQL.append(" and sm.k_point_name like ?");
		    paramList.add("%"+qry_point_name+"%");
		}		
		String qry_praxes_content = reqElement.getChildText("qry_praxes_content");
		if (StringUtil.isNotEmpty(qry_praxes_content)){
		    strSQL.append(" and st.praxes_content like ?");
		    paramList.add("%"+qry_praxes_content+"%");
		}
		String qry_difficulty_min = reqElement.getChildText("qry_difficulty_min");
		if (StringUtil.isNotEmpty(qry_difficulty_min)){
		    strSQL.append(" and st.difficulty_level>=?");
		    paramList.add(qry_difficulty_min);
		}
		String qry_difficulty_max = reqElement.getChildText("qry_difficulty_max");
		if (StringUtil.isNotEmpty(qry_difficulty_max)){
		    strSQL.append(" and st.difficulty_level<=?");
		    paramList.add(qry_difficulty_max);
		}		
		String qry_share_level = reqElement.getChildText("qry_share_level");
		if (StringUtil.isNotEmpty(qry_share_level)){
		    strSQL.append(" and st.share_level=?");
		    paramList.add(qry_share_level);
		}
		String qry_praxes_type = reqElement.getChildText("qry_praxes_type");
		if (StringUtil.isNotEmpty(qry_praxes_type)){
		    strSQL.append(" and st.praxes_type=?");
		    paramList.add(qry_praxes_type);
		}
		String qry_praxes_id = reqElement.getChildText("qry_praxes_id");
		if (StringUtil.isNotEmpty(qry_praxes_id)){
		    strSQL.append(" and st.praxes_id=?");
		    paramList.add(qry_praxes_id);
		}		
		if (StringUtil.isNotEmpty(qry_folder_code)){
		    strSQL.append(" and sq.folder_code like ?");
		    paramList.add(qry_folder_code+"%");
		}
		/**
		String paper_id = reqElement.getChildText("paper_id");
		if (StringUtil.isNotEmpty(paper_id)){
			strSQL.append(" and not exists (");
			strSQL.append(" select null from learn_paper_praxes t1 ");
			strSQL.append(" where t1.valid='Y' and t1.praxes_id=st.praxes_id");
			strSQL.append(" and t1.paper_id = ?)");
			paramList.add(paper_id);
		}
		*/
	    String orderBy = reqElement.getChildText("order_by");
	    if (StringUtil.isNotEmpty(orderBy)){
	    	strSQL.append(" order by "+orderBy+" desc");
	    }
	    else{
	    	strSQL.append(" order by st.create_date desc");
	    }
	    
	    PlatformDao pdao = new PlatformDao();
	    try{
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
			log4j.logError("[习题管理-检索习题]"+e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
		} finally {
			pdao.releaseConnection();
		}		
	}
}
