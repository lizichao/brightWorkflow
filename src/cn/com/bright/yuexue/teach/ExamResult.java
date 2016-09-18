package cn.com.bright.yuexue.teach;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.xpath.XPath;

import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.util.ConfigDocument;
import cn.brightcom.jraf.util.Constants;
import cn.brightcom.jraf.util.DatetimeUtil;
import cn.brightcom.jraf.util.Log;
import cn.brightcom.jraf.util.StringUtil;
import cn.brightcom.jraf.util.XmlDocPkgUtil;
import cn.com.bright.yuexue.student.Learning;
import cn.com.bright.yuexue.util.AttachmentUtil;

/**
 * <p>Title:考试结果</p>
 * <p>Description: 考试结果</p>
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
 * <p> zhangxq    2014/08/21       1.0          build this moudle </p>
 *     
 */
public class ExamResult extends Learning{
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
		
        if ("getPaperPraxesResult".equals(action)){
        	getPaperPraxesResult();
        }
        else if ("getPaperPraxesResultMOOC".equals(action)){
        	getPaperPraxesResultMOOC();
        }
        else if ("getPendingPraxes".equals(action)){
        	getPendingPraxes();
        }
        else if ("savaCorrectResult".equals(action)){
        	savaCorrectResult();
        }
        else if ("getStuPraxesResult".equals(action)){
        	getStuPraxesResult();
        } 
        else if ("getClassPaperResult".equals(action)){
        	getClassPaperResult();
        }
	    else if ("getClassPaperResultMOOC".equals(action)){
	    	getClassPaperResultMOOC();
	    }
        else if ("saveCorrectDraf".equals(action)){
        	saveCorrectDraf();
        }
        else if ("getExamResult".equals(action)){
        	getExamResult();
        }
        else if ("getPendingPraxesWocr".equals(action)){
        	getPendingPraxesWocr();
        }
        else if ("savaCorrectResultWocr".equals(action)){
        	savaCorrectResultWocr();
        }
        else if ("getPaperResultWocr".equals(action)){
        	getPaperResultWocr();
	    }
        else if ("getStuPraxesResultWocr".equals(action)){
        	getStuPraxesResultWocr();
        } 
        else if ("getPaperPraxesResultWocr".equals(action)){
        	getPaperPraxesResultWocr();
        }
        return xmlDoc;
	}
	/**取学生作业结果*/
	public void getExamResult(){
		Element reqElement =  xmlDocUtil.getRequestData();
		String qry_classid = reqElement.getChildText("qry_classid");
		String qry_paper_id = reqElement.getChildText("qry_paper_id");
		String qry_send_id = reqElement.getChildText("qry_send_id");
		String qry_status = reqElement.getChildText("qry_status");
		
		ArrayList<Object> paramList = new ArrayList<Object>();
		StringBuffer stuSQL = new StringBuffer();
		stuSQL.append("select t2.my_examination_id,t2.send_id,t2.paper_id,t3.classid,t5.classnm,t2.userid,");
		stuSQL.append(" t2.status,t4.username,t4.portrait,t2.consuming_time,");
		stuSQL.append(" (select	avg(sm.consuming_time) from learn_my_examination sm,base_studentinfo sn ");
		stuSQL.append(" where sm.userid = sn.userid	and sm.send_id = t2.send_id ) as avg_consuming_time");
		stuSQL.append(" from learn_my_examination t2,base_studentinfo t3,pcmc_user t4,base_class t5");
		stuSQL.append(" where t2.valid = 'Y' and t3.state > '0' and t5.state > '0'");
		stuSQL.append(" and t3.classid = t5.classid and t2.userid = t4.userid");
		stuSQL.append(" and t2.userid = t3.userid");
		
		if (StringUtil.isNotEmpty(qry_classid)){
			stuSQL.append(" and t3.classid=?");
			paramList.add(qry_classid);
		}
		if (StringUtil.isNotEmpty(qry_paper_id)){
			stuSQL.append(" and t2.paper_id=?");
			paramList.add(qry_paper_id);
		}		
		if (StringUtil.isNotEmpty(qry_send_id)){
			stuSQL.append(" and t2.send_id=?");
			paramList.add(qry_send_id);
		}
		if (StringUtil.isNotEmpty(qry_status)){
			stuSQL.append(" and t2.status>=?");
			paramList.add(qry_status);
		}
		stuSQL.append(" order by t3.studentno");
		
		StringBuffer resultSQL = new StringBuffer();
		resultSQL.append(" select * from learn_examination_result t1");
		resultSQL.append(" where t1.my_examination_id=? order by t1.paper_praxes_id");
		
		PlatformDao pdao = new PlatformDao();
		try {
			pdao.setSql(stuSQL.toString());
			pdao.setBindValues(paramList);
			Element result = pdao.executeQuerySql(-1, 0);
			List ls = result.getChildren("Record");
			for (int i=0;i<ls.size();i++){
				Element rec = (Element)ls.get(i);
				String my_examination_id = rec.getChildText("my_examination_id");
				
				ArrayList<Object> paramResult = new ArrayList<Object>();
				paramResult.add(my_examination_id);
				pdao.setSql(resultSQL.toString());
				pdao.setBindValues(paramResult);
				
				Element examResult = pdao.executeQuerySql(-1, 0);
	    		Element PraxesResult = new Element("PraxesResults");
	    		PraxesResult.addContent(examResult);
	    		rec.addContent(PraxesResult);	
			}
	    	xmlDocUtil.getResponse().addContent(result);
	    	xmlDocUtil.setResult("0");			
		}
		catch (Exception e) {
			e.printStackTrace();
			log4j.logError("[在线学习-取完成学生及作业结果]"+e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);            			
		} finally {
			pdao.releaseConnection();
		}		
	}
	/**
	 * 教师保存批改草稿图片附件
	 *
	 */
	public void saveCorrectDraf(){
		Element reqElement =  xmlDocUtil.getRequestData();
		String username = xmlDocUtil.getSession().getChildText("username");
		String userid = xmlDocUtil.getSession().getChildText("userid");		
		String resultid = reqElement.getChildText("result_id");
		String myExamid = reqElement.getChildText("my_examination_id");
		String paperPraxeId = reqElement.getChildText("paper_praxes_id");
		String showTop = reqElement.getChildText("show_top");
		
		Element draftRec = reqElement.getChild("draft_attachment");
		
		PlatformDao pdao = new PlatformDao();
		try {
		    if (StringUtil.isEmpty(resultid) && StringUtil.isEmpty(myExamid) && StringUtil.isEmpty(paperPraxeId)){
			   xmlDocUtil.writeErrorMsg("60100", "未明确修改记录行");
			   return;
		    }
		    else{
			   if (StringUtil.isEmpty(resultid)){
				   if (StringUtil.isEmpty(myExamid) || StringUtil.isEmpty(paperPraxeId)){
					   xmlDocUtil.writeErrorMsg("60100", "未明确修改记录行");
					   return;
				   }
			   }
		    }
		    ArrayList<Object> paramList = new ArrayList<Object>();
		    StringBuffer upSQL = new StringBuffer();
		    upSQL.append("update learn_examination_result set teacherid=?,modify_by=?,marking_time=now()");
		    paramList.add(userid);
		    paramList.add(username);
		    
		    if (StringUtil.isNotEmpty(showTop)){
			   upSQL.append(" ,show_top=? ");
			   paramList.add(showTop);
		    }
		    
			if (draftRec!=null){			   
			   String drafPath = AttachmentUtil.moveFile(draftRec, "draft");
			   upSQL.append(" ,teacher_draf_path=? ");
			   paramList.add(drafPath);
			}   
		    
			upSQL.append(" where 1=1 ");			   
		    if (StringUtil.isNotEmpty(resultid)){
			   upSQL.append(" and result_id=?");
			   paramList.add(resultid);
		    }
		    if (StringUtil.isNotEmpty(myExamid)){
			   upSQL.append(" and my_examination_id=?");
			   paramList.add(myExamid);
		    }
		    if (StringUtil.isNotEmpty(paperPraxeId)){
			   upSQL.append(" and paper_praxes_id=?");
			   paramList.add(paperPraxeId);
		    }			   
				
			pdao.setSql(upSQL.toString());
			pdao.setBindValues(paramList);
			pdao.executeTransactionSql();				
			
	    	xmlDocUtil.setResult("0");	
	    	xmlDocUtil.writeHintMsg("00103", "保存作业批改成功!");			
		}
		catch (Exception e) {
			e.printStackTrace();
			log4j.logError("[在线学习-保存作业]"+e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);            			
		} finally {
			pdao.releaseConnection();
		}		
	}
	/**
	 * 获取班级作业结果
	 *
	 */
	public void getClassPaperResult(){		
		Element reqElement =  xmlDocUtil.getRequestData();
		String classid = reqElement.getChildText("classid");
		String sendid = reqElement.getChildText("sendid");
		
		ArrayList<Object> paramList = new ArrayList<Object>();
		paramList.add(sendid);
		if (StringUtil.isNotEmpty(classid)){
		   paramList.add(classid);
		}
		
		StringBuffer groupSQL = new StringBuffer();
		groupSQL.append("select t1.paper_id,t2.my_examination_id,t2.status,t2.userid,t5.username,t3.group_id,t3.group_name");
		groupSQL.append(" from learn_paper_send t1,learn_my_examination t2,learn_group t3,learn_group_member t4,pcmc_user t5");
		groupSQL.append(" where t1.send_id=t2.send_id and t2.userid=t5.userid");
		groupSQL.append(" and t3.userid=t1.userid and t3.valid='Y' and t4.valid='Y'");
		groupSQL.append(" and t4.group_id=t3.group_id and t4.userid=t2.userid");
		groupSQL.append(" and t1.send_id=?");
		if (StringUtil.isNotEmpty(classid)){
		    groupSQL.append(" and t3.classid=?");
		}
		groupSQL.append(" order by t3.group_id,t2.userid");

		StringBuffer noGroupSQL = new StringBuffer();
		noGroupSQL.append("select t1.paper_id,t2.my_examination_id,t2.status,t2.userid,t5.username,'0' as group_id,'未分组学生' as group_name");
		noGroupSQL.append(" from learn_paper_send t1,learn_my_examination t2,pcmc_user t5");
		noGroupSQL.append(" where t1.send_id=t2.send_id and t2.userid=t5.userid");
		noGroupSQL.append(" and  t1.send_id=? and not exists");
		noGroupSQL.append(" (select null from learn_group t3,learn_group_member t4");
		noGroupSQL.append(" where t3.valid='Y' and t4.valid='Y' and t4.group_id=t3.group_id ");
		noGroupSQL.append(" and t4.userid=t2.userid and t3.userid=t1.userid and t3.classid=?)");
		noGroupSQL.append(" order by t2.userid");
		
		StringBuffer examResultSQL = new StringBuffer();
		
		examResultSQL.append("select tt1.userid,tt1.paper_praxes_id,tt1.praxes_type,tt1.display_order,sm.result_id,sm.praxes_result,sm.is_right,sm.score");
		examResultSQL.append(" from (select t3.userid,t2.paper_praxes_id,t2.praxes_type,t2.display_order,t3.my_examination_id");
		examResultSQL.append("   from learn_paper_send t1,learn_paper_praxes t2,learn_my_examination t3,base_studentinfo t4");
		examResultSQL.append("   where t2.valid='Y' and t1.paper_id=t2.paper_id and t1.send_id=t3.send_id and t4.userid=t3.userid");
		examResultSQL.append("   and t1.send_id=? ");
		if (StringUtil.isNotEmpty(classid)){
		   examResultSQL.append("   and t4.classid=? ");
		}
		examResultSQL.append("  ) tt1 left join learn_examination_result sm ");
		examResultSQL.append("   on sm.my_examination_id=tt1.my_examination_id and sm.paper_praxes_id=tt1.paper_praxes_id");
		examResultSQL.append(" order by tt1.userid,tt1.display_order");		
		
		PlatformDao pdao = new PlatformDao();
		try {
			pdao.setSql(examResultSQL.toString());
			pdao.setBindValues(paramList);
			Element examResult = pdao.executeQuerySql(-1, 0);
			
			pdao.setSql(groupSQL.toString());
			pdao.setBindValues(paramList);
			
			Element result = pdao.executeQuerySql(-1, 0);
			List groupStuList = result.getChildren("Record");
			for (int i=0;i<groupStuList.size();i++){
				Element stuRec = (Element)groupStuList.get(i);
				Element stuExamResult = filterRecordByField(examResult,"userid",stuRec.getChildTextTrim("userid"));
		    	
				Element stuExam = new Element("ExamResult");
		    	stuExam.addContent(stuExamResult);
		    	stuRec.addContent(stuExam);					
			}
			
			if (StringUtil.isNotEmpty(classid)){
				pdao.setSql(noGroupSQL.toString());
				pdao.setBindValues(paramList);
				Element noGroupResult = pdao.executeQuerySql(-1, 0);
				List noGroupStuList = noGroupResult.getChildren("Record");
				for (int i=0;i<noGroupStuList.size();i++){
					Element stuRec = (Element)noGroupStuList.get(i);			
			    	
					Element copyRec = new Element("Record");
					XmlDocPkgUtil.setChildText(copyRec, "paper_id",stuRec.getChildTextTrim("paper_id"));
					XmlDocPkgUtil.setChildText(copyRec, "my_examination_id",stuRec.getChildTextTrim("my_examination_id"));
					XmlDocPkgUtil.setChildText(copyRec, "status",stuRec.getChildTextTrim("status"));
					XmlDocPkgUtil.setChildText(copyRec, "userid",stuRec.getChildTextTrim("userid"));
					XmlDocPkgUtil.setChildText(copyRec, "username",stuRec.getChildTextTrim("username"));
					XmlDocPkgUtil.setChildText(copyRec, "group_id",stuRec.getChildTextTrim("group_id"));
					XmlDocPkgUtil.setChildText(copyRec, "group_name",stuRec.getChildTextTrim("group_name"));
					
					Element stuExamResult = filterRecordByField(examResult,"userid",stuRec.getChildTextTrim("userid"));
					
					Element stuExam = new Element("ExamResult");
			    	stuExam.addContent(stuExamResult);
			    	copyRec.addContent(stuExam);
			    	result.addContent(copyRec);
				}
			}
	    	xmlDocUtil.getResponse().addContent(result);
	    	xmlDocUtil.setResult("0");	    	
		}
		catch (Exception e) {
			e.printStackTrace();
			log4j.logError("[试卷结果-分组取学生作业结果]"+e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);            			
		} finally {
			pdao.releaseConnection();
		}		
	}
	/**
	 * 获取班级作业结果
	 *
	 */
	public void getClassPaperResultMOOC(){		
		Element reqElement =  xmlDocUtil.getRequestData();
		String classid = reqElement.getChildText("classid");
		String sendid = reqElement.getChildText("sendid");
		
		ArrayList<Object> paramList = new ArrayList<Object>();
		paramList.add(sendid);
		if (StringUtil.isNotEmpty(classid)){
		   paramList.add(classid);
		}
		
		StringBuffer groupSQL = new StringBuffer();
		groupSQL.append("select t1.paper_id,t2.my_examination_id,t2.status,t2.userid,t5.username,t3.group_id,t3.group_name");
		groupSQL.append(" from learn_paper_send t1,learn_my_examination t2,learn_group t3,learn_group_member t4,pcmc_user t5");
		groupSQL.append(" where t1.send_id=t2.send_id and t2.userid=t5.userid");
		groupSQL.append(" and t3.userid=t1.userid and t3.valid='Y' and t4.valid='Y'");
		groupSQL.append(" and t4.group_id=t3.group_id and t4.userid=t2.userid");
		groupSQL.append(" and t1.send_id=?");
		if (StringUtil.isNotEmpty(classid)){
		    groupSQL.append(" and t3.classid=?");
		}
		groupSQL.append(" order by t3.group_id,t2.userid");

		StringBuffer noGroupSQL = new StringBuffer();
		noGroupSQL.append("select t1.paper_id,t2.my_examination_id,t2.status,t2.userid,t5.username,'0' as group_id,'未分组学生' as group_name");
		noGroupSQL.append(" from learn_paper_send t1,learn_my_examination t2,pcmc_user t5");
		noGroupSQL.append(" where t1.send_id=t2.send_id and t2.userid=t5.userid");
		noGroupSQL.append(" and  t1.send_id=? and not exists");
		noGroupSQL.append(" (select null from learn_group t3,learn_group_member t4");
		noGroupSQL.append(" where t3.valid='Y' and t4.valid='Y' and t4.group_id=t3.group_id ");
		noGroupSQL.append(" and t4.userid=t2.userid and t3.userid=t1.userid and t3.classid=?)");
		noGroupSQL.append(" order by t2.userid");
		
		StringBuffer examResultSQL = new StringBuffer();
		ArrayList<Object> paramExamList = new ArrayList<Object>();
		paramExamList.add(sendid);
		examResultSQL.append("select tt1.userid,tt1.paper_praxes_id,tt1.display_order,sm.result_id,sm.praxes_result,sm.is_right,sm.score,tt1.praxes_type");
		examResultSQL.append(" from (select t3.userid,t2.paper_praxes_id,t2.display_order,t3.my_examination_id,t2.praxes_type");
		examResultSQL.append("   from learn_paper_send t1,learn_paper_praxes t2,learn_my_examination t3,base_studentinfo t4");
		examResultSQL.append("   where t2.valid='Y' and t1.paper_id=t2.paper_id and t1.send_id=t3.send_id and t4.userid=t3.userid");
		examResultSQL.append("   and t1.send_id=? ");
		examResultSQL.append("  ) tt1 left join learn_examination_result sm ");
		examResultSQL.append("   on sm.my_examination_id=tt1.my_examination_id and sm.paper_praxes_id=tt1.paper_praxes_id");
		examResultSQL.append(" order by tt1.userid,tt1.display_order");		
		
		PlatformDao pdao = new PlatformDao();
		try {
			pdao.setSql(examResultSQL.toString());
			pdao.setBindValues(paramExamList);
			Element examResult = pdao.executeQuerySql(-1, 0);
			
			pdao.setSql(groupSQL.toString());
			pdao.setBindValues(paramList);
			
			Element result = pdao.executeQuerySql(-1, 0);
			List groupStuList = result.getChildren("Record");
			for (int i=0;i<groupStuList.size();i++){
				Element stuRec = (Element)groupStuList.get(i);
				Element stuExamResult = filterRecordByField(examResult,"userid",stuRec.getChildTextTrim("userid"));
		    	
				Element stuExam = new Element("ExamResult");
		    	stuExam.addContent(stuExamResult);
		    	stuRec.addContent(stuExam);					
			}
			
			if (StringUtil.isNotEmpty(classid)){
				pdao.setSql(noGroupSQL.toString());
				pdao.setBindValues(paramList);
				Element noGroupResult = pdao.executeQuerySql(-1, 0);
				List noGroupStuList = noGroupResult.getChildren("Record");
				for (int i=0;i<noGroupStuList.size();i++){
					Element stuRec = (Element)noGroupStuList.get(i);			
			    	
					Element copyRec = new Element("Record");
					XmlDocPkgUtil.setChildText(copyRec, "paper_id",stuRec.getChildTextTrim("paper_id"));
					XmlDocPkgUtil.setChildText(copyRec, "my_examination_id",stuRec.getChildTextTrim("my_examination_id"));
					XmlDocPkgUtil.setChildText(copyRec, "status",stuRec.getChildTextTrim("status"));
					XmlDocPkgUtil.setChildText(copyRec, "userid",stuRec.getChildTextTrim("userid"));
					XmlDocPkgUtil.setChildText(copyRec, "username",stuRec.getChildTextTrim("username"));
					XmlDocPkgUtil.setChildText(copyRec, "group_id",stuRec.getChildTextTrim("group_id"));
					XmlDocPkgUtil.setChildText(copyRec, "group_name",stuRec.getChildTextTrim("group_name"));
					
					Element stuExamResult = filterRecordByField(examResult,"userid",stuRec.getChildTextTrim("userid"));
					
					Element stuExam = new Element("ExamResult");
			    	stuExam.addContent(stuExamResult);
			    	copyRec.addContent(stuExam);
			    	result.addContent(copyRec);
				}
			}
	    	xmlDocUtil.getResponse().addContent(result);
	    	xmlDocUtil.setResult("0");	    	
		}
		catch (Exception e) {
			e.printStackTrace();
			log4j.logError("[试卷结果-取学生作业结果]"+e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);            			
		} finally {
			pdao.releaseConnection();
		}		
	}
    private Element filterRecordByField(Element resData,String fieldName, String fieldValue) throws Exception{
    	String path = "Record["+fieldName+"=\""+fieldValue+"\"]";
    	XPath xpath = XPath.newInstance(path);  
    	List ls = xpath.selectNodes(resData);
    	Element dataElement = new Element("Data");
    	    	
    	for (int j=0;j<ls.size();j++){
			Element record = new Element("Record");
			
			Element currRec = (Element)ls.get(j);
			XmlDocPkgUtil.setChildText(record, "paper_praxes_id",currRec.getChildTextTrim("paper_praxes_id"));
			XmlDocPkgUtil.setChildText(record, "result_id",currRec.getChildTextTrim("result_id"));
			XmlDocPkgUtil.setChildText(record, "praxes_type", currRec.getChildTextTrim("praxes_type"));
			XmlDocPkgUtil.setChildText(record, "praxes_result",currRec.getChildTextTrim("praxes_result"));
			XmlDocPkgUtil.setChildText(record, "is_right",currRec.getChildTextTrim("is_right"));
			XmlDocPkgUtil.setChildText(record, "score",currRec.getChildTextTrim("score"));
			
			dataElement.addContent(record);			
    	}
    	return dataElement;
    }	
	/**
	private void addExamResult(Element stuRec,PlatformDao pdao) throws Exception{
		StringBuffer examResultSQL = new StringBuffer();
		examResultSQL.append("select t1.paper_praxes_id,t2.result_id,t2.praxes_result,t2.is_right,t2.score");
		examResultSQL.append(" from learn_paper_praxes t1 left join learn_examination_result t2 ");
		examResultSQL.append(" on t1.paper_id=t2.paper_id and t1.paper_praxes_id=t2.paper_praxes_id ");
		examResultSQL.append("    and t2.userid=? and t2.my_examination_id=?");
		examResultSQL.append(" where t1.valid='Y' and t1.paper_id=?");
		examResultSQL.append(" order by t1.paper_praxes_id");
		
		ArrayList<Object> paramExam = new ArrayList<Object>(); 
		paramExam.add(stuRec.getChildText("userid"));
		paramExam.add(stuRec.getChildText("my_examination_id"));
		paramExam.add(stuRec.getChildText("paper_id"));
		
		pdao.setSql(examResultSQL.toString());
		pdao.setBindValues(paramExam);
		
		Element examResult = pdao.executeQuerySql(-1, 0);
    	Element stuExam = new Element("ExamResult");
    	stuExam.addContent(examResult);
    	stuRec.addContent(stuExam);			
	}
	*/
	/**
	 * 获取学生试题作业成绩
	 *
	 */
	public void getStuPraxesResult(){
		Element reqElement =  xmlDocUtil.getRequestData();
		String usertype = xmlDocUtil.getSession().getChildText("usertype");
		
		String paper_id = reqElement.getChildText("paper_id");
		String my_examination_id = reqElement.getChildText("my_examination_id");		
		//String qry_user_id=reqElement.getChildText("qry_user_id");
		String paper_praxes_id=reqElement.getChildText("paper_praxes_id");
		
		Element result = super.getPaperPraxesInfo(paper_id,my_examination_id,paper_praxes_id,usertype);
		
		if (result!=null){
	    	xmlDocUtil.getResponse().addContent(result);
	    	xmlDocUtil.setResult("0");			
		}
		else{
			xmlDocUtil.writeErrorMsg("20206","取学生作业结果发生错误!");
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);  
		}		
	}
	/**
	 * 保存批改结果
	 *
	 */
	public void savaCorrectResult(){
		String userid = xmlDocUtil.getSession().getChildTextTrim("userid");
		Element reqElement =  xmlDocUtil.getRequestData();
		String send_id = reqElement.getChildText("send_id");
		List resultIDList = reqElement.getChildren("result_id");
		List scoreList = reqElement.getChildren("score");
		PlatformDao pdao = new PlatformDao();
		try {
			
			String resultid= ((Element)resultIDList.get(0)).getText();
			StringBuffer strSQL = new StringBuffer();
			strSQL.append("select t2.score from learn_examination_result t1,learn_paper_praxes t2");
			strSQL.append(" where t1.paper_praxes_id=t2.paper_praxes_id and t1.result_id=?");
			ArrayList<Object> qryParam = new ArrayList<Object>();
			qryParam.add(resultid);
			
			pdao.setSql(strSQL.toString());
			pdao.setBindValues(qryParam);
			Element result = pdao.executeQuerySql(-1, 0);
			String praxesScore = result.getChild("Record").getChildTextTrim("score");
			if (StringUtil.isEmpty(praxesScore)){
				praxesScore = "100";
			}
			
			pdao.beginTransaction();
			for (int i=0;i<resultIDList.size();i++){
				String resultScore = ((Element)scoreList.get(i)).getText();
				
				Element examResultRec = ConfigDocument.createRecordElement("yuexue", "learn_examination_result");
				XmlDocPkgUtil.copyValues(reqElement, examResultRec, i, true);
				String is_right="0";
				if (praxesScore.equals(resultScore)){
					is_right = "1";
				}
				else if (!"0".equals(resultScore)){
					is_right = "2";
				}
				XmlDocPkgUtil.setChildText(examResultRec, "is_right", is_right);
				XmlDocPkgUtil.setChildText(examResultRec, "teacherid", userid);
				XmlDocPkgUtil.setChildText(examResultRec, "marking_time", DatetimeUtil.getNow(""));
				XmlDocPkgUtil.setChildText(examResultRec, "modify_date", DatetimeUtil.getNow(""));				
				pdao.updateOneRecord(examResultRec); 
			}
			
			StringBuffer updateSQL = new StringBuffer();
			ArrayList<Object> paramList = new ArrayList<Object>();
			updateSQL.append("update learn_my_examination set status='70',paper_score=(select sum(st.score) from learn_examination_result st");
			updateSQL.append("  where st.my_examination_id=learn_my_examination.my_examination_id)");
			updateSQL.append(" where valid='Y' and status='60' and send_id = ?");
			updateSQL.append(" and exists (select null from learn_examination_result st ");
			updateSQL.append("            where st.my_examination_id=learn_my_examination.my_examination_id");
			updateSQL.append("            and   score is not null and teacherid is not null )");
			updateSQL.append(" and not exists (select null from learn_examination_result st");
			updateSQL.append("            where st.my_examination_id=learn_my_examination.my_examination_id");
			updateSQL.append("            and   score is  null)");			
			paramList.add(send_id);
			
			pdao.setSql(updateSQL.toString());
			pdao.setBindValues(paramList);			
			pdao.executeTransactionSql();
			
			pdao.commitTransaction();
	    	xmlDocUtil.setResult("0");	
	    	xmlDocUtil.writeHintMsg("00105", "批改结果保存成功!");
		}
		catch (Exception e) {
			pdao.rollBack();
			e.printStackTrace();
			log4j.logError("[试卷结果-保存批改结果]"+e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);            			
		} finally {
			pdao.releaseConnection();
		}
	}
	/**
	 * 获取待批改的试题及学生回答
	 *
	 */
	public void getPendingPraxes(){
		Element reqElement =  xmlDocUtil.getRequestData();
		String qry_classid = reqElement.getChildText("qry_classid");		
		String qry_send_id = reqElement.getChildText("qry_send_id");
		String qry_group_id = reqElement.getChildText("qry_group_id");
		String qry_type = reqElement.getChildText("qry_type");
		
		StringBuffer strSQL = new StringBuffer();
		ArrayList<Object> paramList = new ArrayList<Object>();	
		strSQL.append("select t2.paper_praxes_id,t2.praxes_type,getParamDesc('c_praxes_type',t2.praxes_type) as praxes_type_desc,");
		strSQL.append(" t2.praxes_content,t2.difficulty_level,t2.discrimination,t2.right_result,IFNULL(t2.score,-1) as score ");
		strSQL.append(" from learn_paper_send t1,learn_paper_praxes t2");
		strSQL.append(" where t2.valid='Y' and t1.paper_id=t2.paper_id");
		strSQL.append(" and t1.send_id= ? ");
		paramList.add(qry_send_id);
		strSQL.append(" and  exists ");
		strSQL.append(" (select null from learn_my_examination sd ");
		strSQL.append(" where sd.valid='Y' and sd.send_id=t1.send_id and sd.status='60')");
		strSQL.append(" and (t2.right_result is null or  t2.right_result='')");//没有正确答案(填空题,判断题)
		strSQL.append(" and not exists ");//没有正确答案,选择题
		strSQL.append(" (select null from learn_paper_options st");
		strSQL.append(" where st.valid='Y' and st.is_right='1'");
		strSQL.append(" and  st.paper_praxes_id=t2.paper_praxes_id)");
		
		String optionSQL = "select * from learn_paper_options where paper_praxes_id=? and valid='Y' and is_right='1' order by display_order";
		
		
    	StringBuffer pendingSQL = new StringBuffer();
    	ArrayList<Object> pendingParam = new ArrayList<Object>();
    	pendingSQL.append("select  t1.result_id,t1.student_draf_path,t1.teacher_draf_path,t1.paper_praxes_id,t1.consuming_time,t4.username,t4.portrait,t1.praxes_result,IFNULL(t1.score,-1) as score ");
    	pendingSQL.append(" from learn_examination_result t1,learn_my_examination t2,base_studentinfo t3,pcmc_user t4");
		if (StringUtil.isNotEmpty(qry_group_id)){
			pendingSQL.append(" ,learn_group_member sn");
		}    	
    	pendingSQL.append(" where t1.my_examination_id=t2.my_examination_id ");
    	if ("1".equals(qry_type)){//查显示待批改的学生
    	   pendingSQL.append(" and t1.score is null ");  
    	}
    	pendingSQL.append(" and t2.userid=t3.userid and t3.state>'0' and t3.userid=t4.userid");
    	pendingSQL.append(" and t2.send_id=?");
    	pendingParam.add(qry_send_id);
    	
		if (StringUtil.isNotEmpty(qry_classid)){
			pendingSQL.append(" and t3.classid = ?");
			pendingParam.add(qry_classid);
		}
		if (StringUtil.isNotEmpty(qry_group_id)){
			pendingSQL.append(" and  t4.userid=sn.userid and sn.valid='Y' and sn.group_id = ?");
			pendingParam.add(qry_group_id);
		}
		pendingSQL.append(" and t1.paper_praxes_id = ");
		
		PlatformDao pdao = new PlatformDao();
	    try{
	    	pdao.setSql(strSQL.toString());
	    	pdao.setBindValues(paramList);
	    	Element result = pdao.executeQuerySql(0,-1);
	    	List list = result.getChildren("Record");
	    	for (int i = 0; i < list.size() ;i++){
	    		Element praxesRec = (Element)list.get(i);
	    		String paper_praxes_id = praxesRec.getChildText("paper_praxes_id");
	    		String praxes_type = praxesRec.getChildText("praxes_type");	    		
	    		//如果单选或多选题,则增加正确选项内容
	    		if ("20".equals(praxes_type) || "30".equals(praxes_type)){
		    		ArrayList<Object> optionParam = new ArrayList<Object>();
		    		optionParam.add(praxesRec.getChildText("paper_praxes_id"));
		    		pdao.setSql(optionSQL);
		    		pdao.setBindValues(optionParam);	    			
		    		Element optionResult = pdao.executeQuerySql(0,-1);
		    		List optionList = optionResult.getChildren("Record");
		    		
		    		StringBuffer rightResult = new StringBuffer();
			    	for (int k = 0;k < optionList.size() ;k++){
			    		Element optionsRec = (Element)optionList.get(k);
			    		if (k==0){
			    			rightResult.append(optionsRec.getChildText("option_name"));
			    		}
			    		else{
			    			rightResult.append(","+optionsRec.getChildText("option_name"));
			    		}
			    	}
			    	XmlDocPkgUtil.setChildText(praxesRec, "right_result", rightResult.toString());		    		
	    		}
	    		
	    		pdao.setSql(pendingSQL.toString()+"'"+paper_praxes_id+"' order by t3.studentno");
	    		pdao.setBindValues(pendingParam);
	    		Element pendingResult = pdao.executeQuerySql(0,-1);		    		
	    		Element PraxesResult = new Element("PraxesResults");
	    		PraxesResult.addContent(pendingResult);
	    		praxesRec.addContent(PraxesResult);	    		
	    	}
	    	xmlDocUtil.getResponse().addContent(result);
	    	xmlDocUtil.setResult("0");		    	
	    }
	    catch (Exception e) {
			e.printStackTrace();
			log4j.logError("[试卷结果-获取待批改题目信息]"+e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
		} finally {
			pdao.releaseConnection();
		}
		
	}
	/**
	 * 获取试卷试题结果
	 *
	 */
	public void getPaperPraxesResult(){
		String optionStr = "A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z";
		String[] optionArray = optionStr.split(",");		
		Element reqElement =  xmlDocUtil.getRequestData();
		String qry_classid = reqElement.getChildText("qry_classid");
		String qry_paper_id = reqElement.getChildText("qry_paper_id");
		String qry_send_id = reqElement.getChildText("qry_send_id");
		String qry_group_id = reqElement.getChildText("qry_group_id");
		String qry_paper_praxes_id=reqElement.getChildText("qry_paper_praxes_id");

		StringBuffer strSQL = new StringBuffer();
		ArrayList<Object> paramList = new ArrayList<Object>();
		strSQL.append("select t1.paper_praxes_id,t1.paper_id,t1.praxes_id,t1.praxes_type,t1.praxes_content,t1.score,");
		strSQL.append("'"+qry_send_id+"' as send_id,'"+qry_classid+"' as classid,");//取答对学生列表时要用到
		strSQL.append("t1.right_result,getParamDesc('c_praxes_type',t1.praxes_type) as praxes_type_desc,");
        //发送给学生的人数人数
		strSQL.append(" (select count(st.my_examination_id) from learn_my_examination st");		
		strSQL.append(" where st.paper_id=t1.paper_id and st.send_id=? ");
		paramList.add(qry_send_id);		
		strSQL.append(" ) as send_stu_count,");//发送给学生的人数人数
		//答题人数
		strSQL.append(" (select count(sd.result_id) from learn_my_examination st,learn_examination_result sd,base_studentinfo sm");
		if (StringUtil.isNotEmpty(qry_group_id)){
			strSQL.append(" ,learn_group_member sn");
		}		
		strSQL.append(" where st.my_examination_id=sd.my_examination_id and st.status>'40' and  sd.paper_praxes_id=t1.paper_praxes_id and sd.userid=sm.userid and sm.state>'0' and st.send_id=?");
		paramList.add(qry_send_id);		
		if (StringUtil.isNotEmpty(qry_classid)){
			strSQL.append(" and sm.classid = ?");
			paramList.add(qry_classid);
		}
		if (StringUtil.isNotEmpty(qry_group_id)){
			strSQL.append(" and  sm.userid=sn.userid and sn.valid='Y' and sn.group_id = ?");
			paramList.add(qry_group_id);
		}
		strSQL.append(" ) as stu_count,");//答题人数
        //问答题教师批改后的平均分数	
		strSQL.append(" (select round(avg(sd.score),1) from learn_my_examination st,learn_examination_result sd,base_studentinfo sm");
		if (StringUtil.isNotEmpty(qry_group_id)){
			strSQL.append(" ,learn_group_member sn");
		}		
		strSQL.append(" where st.my_examination_id=sd.my_examination_id and st.status>'40' and  sd.paper_praxes_id=t1.paper_praxes_id ");
		strSQL.append(" and sd.score is not null and sd.userid=sm.userid and sm.state>'0' and st.send_id=?");
		paramList.add(qry_send_id);		
		if (StringUtil.isNotEmpty(qry_classid)){
			strSQL.append(" and sm.classid = ?");
			paramList.add(qry_classid);
		}
		if (StringUtil.isNotEmpty(qry_group_id)){
			strSQL.append(" and  sm.userid=sn.userid and sn.valid='Y' and sn.group_id = ?");
			paramList.add(qry_group_id);
		}
		strSQL.append(" ) as avg_score,");//问答题教师批改后的平均分数	
		
		//填空题部分正确人数
	    strSQL.append(" (select count(sd.result_id) from learn_my_examination st,learn_examination_result sd,base_studentinfo sm");
	    if (StringUtil.isNotEmpty(qry_group_id)) {
	      strSQL.append(" ,learn_group_member sn");
	    }
	    strSQL.append(" where st.my_examination_id=sd.my_examination_id and st.status>'40' and  sd.paper_praxes_id=t1.paper_praxes_id and sd.userid=sm.userid and sm.state>'0' and  st.send_id=?");
	    paramList.add(qry_send_id);
	    strSQL.append(" and sd.is_right=2");
	    if (StringUtil.isNotEmpty(qry_classid)) {
	      strSQL.append(" and sm.classid = ?");
	      paramList.add(qry_classid);
	    }
	    if (StringUtil.isNotEmpty(qry_group_id)) {
	      strSQL.append(" and  sm.userid=sn.userid and sn.valid='Y' and sn.group_id = ?");
	      paramList.add(qry_group_id);
	    }
	    strSQL.append(" ) as part_right_count,");
	    
        //答对人数
		strSQL.append(" (select count(sd.result_id) from learn_my_examination st,learn_examination_result sd,base_studentinfo sm");
		if (StringUtil.isNotEmpty(qry_group_id)){
			strSQL.append(" ,learn_group_member sn");
		}		
		strSQL.append(" where st.my_examination_id=sd.my_examination_id and st.status>'40' and  sd.paper_praxes_id=t1.paper_praxes_id and sd.userid=sm.userid and sm.state>'0' and  st.send_id=?");
		paramList.add(qry_send_id);	
		strSQL.append(" and sd.is_right=1");//回答正确条件
		if (StringUtil.isNotEmpty(qry_classid)){
			strSQL.append(" and sm.classid = ?");
			paramList.add(qry_classid);
		}
		if (StringUtil.isNotEmpty(qry_group_id)){
			strSQL.append(" and  sm.userid=sn.userid and sn.valid='Y' and sn.group_id = ?");
			paramList.add(qry_group_id);
		}
		strSQL.append(" ) as right_count");//答对人数		
		strSQL.append(" from learn_paper_praxes t1");
		strSQL.append("  where t1.valid='Y' and t1.paper_id =?");
		paramList.add(qry_paper_id);
		if (StringUtil.isNotEmpty(qry_paper_praxes_id)){
			strSQL.append("  and t1.paper_praxes_id=?");
			paramList.add(qry_paper_praxes_id);
		}
		strSQL.append(" order by t1.display_order");	
		
    	StringBuffer optionSQL = new StringBuffer();
    	ArrayList<Object> optionParam = new ArrayList<Object>();
    	optionSQL.append("select t1.paper_option_id,t1.paper_praxes_id,ifnull(t1.praxes_id,t1.paper_praxes_id) as praxes_id,ifnull(t1.option_id,t1.paper_option_id) as option_id,");    	
    	optionSQL.append(" t1.is_right,t1.option_content,");
        //选中该选项的人数
    	optionSQL.append(" (select count(sd.result_id) from learn_my_examination st,learn_examination_result sd,base_studentinfo sm");
		if (StringUtil.isNotEmpty(qry_group_id)){
			optionSQL.append(" ,learn_group_member sn");
		}		
		optionSQL.append(" where st.my_examination_id=sd.my_examination_id and  sd.paper_praxes_id=t1.paper_praxes_id and sd.userid=sm.userid and sm.state>'0' and st.send_id=?");
		optionParam.add(qry_send_id);	
		optionSQL.append(" and (sd.praxes_result like Concat('%',t1.option_id,'%')");
		optionSQL.append("     or sd.praxes_result like Concat('%',t1.paper_option_id,'%'))");
		if (StringUtil.isNotEmpty(qry_classid)){
			optionSQL.append(" and sm.classid = ?");
			optionParam.add(qry_classid);
		}
		if (StringUtil.isNotEmpty(qry_group_id)){
			optionSQL.append(" and  sm.userid=sn.userid and sn.valid='Y' and sn.group_id = ?");
			optionParam.add(qry_group_id);
		}
		optionSQL.append(" ) as select_count");//选中该选项的人数  	
    	optionSQL.append(" from learn_paper_options t1 ");
    	optionSQL.append(" where t1.valid='Y' and t1.paper_praxes_id= ");
    			
    	StringBuffer keywordSQL = new StringBuffer();
    	keywordSQL.append(" select * from (");
    	keywordSQL.append(" select st.k_point_name,(select count(sst.result_id) from learn_examination_result sst,learn_my_examination ssd");
    	keywordSQL.append(" where sst.my_examination_id=ssd.my_examination_id and ssd.send_id=? and sst.paper_praxes_id=?");
    	keywordSQL.append(" and sst.praxes_result like concat('%',st.k_point_name,'%')) as key_count");
    	keywordSQL.append(" from base_knowledge_point st");
    	keywordSQL.append(" where st.valid='Y' and exists ");
    	keywordSQL.append(" (select null from learn_paper_praxes t3,base_knowledge_point t4");
    	keywordSQL.append(" where t3.k_point_id=t4.k_point_id and t3.paper_praxes_id=?");
    	keywordSQL.append(" and t4.grade_code=st.grade_code and t4.subjectid=st.subjectid)) sm");    	
    	keywordSQL.append(" where sm.key_count>0 order by key_count desc");
    	
		
		PlatformDao pdao = new PlatformDao();
	    try{
	    	pdao.setSql(strSQL.toString());
	    	pdao.setBindValues(paramList);
	    	Element result = pdao.executeQuerySql(0,-1);
	    	List list = result.getChildren("Record");
	    	for (int i = 0; i < list.size() ;i++){
	    		Element praxesRec = (Element)list.get(i);
	    		String praxes_type = praxesRec.getChildText("praxes_type");
	    		String paper_praxes_id = praxesRec.getChildText("paper_praxes_id");
	    		//如果单选或多选题,则增加选项内容
	    		if ("20".equals(praxes_type) || "30".equals(praxes_type)){		    		
		    		pdao.setSql(optionSQL.toString()+"'"+paper_praxes_id+"' order by display_order");
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
	    		}//如果是问答题,则统计关键词出现次数
	    		else if ("90".equals(praxes_type)){
	    			ArrayList<Object> keywordParam = new ArrayList<Object>();
	    			keywordParam.add(qry_send_id);
	    			keywordParam.add(paper_praxes_id);	    			
	    			keywordParam.add(paper_praxes_id);
	    			
		    		pdao.setSql(keywordSQL.toString());
		    		pdao.setBindValues(keywordParam);
		    		Element keywordResult = pdao.executeQuerySql(0,-1);
			    	
		    		Element keywordStats = new Element("KeywordStat");
		    		keywordStats.addContent(keywordResult);
		    		praxesRec.addContent(keywordStats);	    			
	    		}
	    	}
	    	xmlDocUtil.getResponse().addContent(result);
	    	xmlDocUtil.setResult("0");	    	
	    }
	    catch (Exception e) {
			e.printStackTrace();
			log4j.logError("[试卷结果-获取试卷题目结果信息]"+e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
		} finally {
			pdao.releaseConnection();
		}
	}
	
	/**
	 * 获取试卷试题结果
	 *
	 */
	public void getPaperPraxesResultMOOC(){
		String optionStr = "A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z";
		String[] optionArray = optionStr.split(",");		
		Element reqElement =  xmlDocUtil.getRequestData();
		String qry_classid = reqElement.getChildText("qry_classid");
		String qry_paper_id = reqElement.getChildText("qry_paper_id");
		String qry_send_id = reqElement.getChildText("qry_send_id");
		String qry_group_id = reqElement.getChildText("qry_group_id");
		String qry_paper_praxes_id=reqElement.getChildText("qry_paper_praxes_id");

		StringBuffer strSQL = new StringBuffer();
		ArrayList<Object> paramList = new ArrayList<Object>();
		strSQL.append("select t1.paper_praxes_id,t1.paper_id,t1.praxes_id,t1.praxes_type,t1.praxes_content,t1.score,");
		strSQL.append("'"+qry_send_id+"' as send_id,'"+qry_classid+"' as classid,");//取答对学生列表时要用到
		strSQL.append("t1.right_result,getParamDesc('c_praxes_type',t1.praxes_type) as praxes_type_desc,");
        //发送给学生的人数人数
		strSQL.append(" (select count(st.my_examination_id) from learn_my_examination st");		
		strSQL.append(" where st.paper_id=t1.paper_id and st.send_id=? ");
		paramList.add(qry_send_id);		
		strSQL.append(" ) as send_stu_count,");//发送给学生的人数人数
		//答题人数
		strSQL.append(" (select count(sd.result_id) from learn_my_examination st,learn_examination_result sd,base_studentinfo sm");
		if (StringUtil.isNotEmpty(qry_group_id)){
			strSQL.append(" ,learn_group_member sn");
		}		
		strSQL.append(" where st.my_examination_id=sd.my_examination_id and st.status>'40' and  sd.paper_praxes_id=t1.paper_praxes_id and sd.userid=sm.userid and sm.state>'0' and st.send_id=?");
		paramList.add(qry_send_id);		
		/*if (StringUtil.isNotEmpty(qry_classid)){
			strSQL.append(" and sm.classid = ?");
			paramList.add(qry_classid);
		}
		if (StringUtil.isNotEmpty(qry_group_id)){
			strSQL.append(" and  sm.userid=sn.userid and sn.valid='Y' and sn.group_id = ?");
			paramList.add(qry_group_id);
		}*/
		strSQL.append(" ) as stu_count,");//答题人数
        //问答题教师批改后的平均分数	
		strSQL.append(" (select round(avg(sd.score),1) from learn_my_examination st,learn_examination_result sd,base_studentinfo sm");
		if (StringUtil.isNotEmpty(qry_group_id)){
			strSQL.append(" ,learn_group_member sn");
		}		
		strSQL.append(" where st.my_examination_id=sd.my_examination_id and st.status>'40' and  sd.paper_praxes_id=t1.paper_praxes_id ");
		strSQL.append(" and sd.score is not null and sd.userid=sm.userid and sm.state>'0' and st.send_id=?");
		paramList.add(qry_send_id);		
		/*if (StringUtil.isNotEmpty(qry_classid)){
			strSQL.append(" and sm.classid = ?");
			paramList.add(qry_classid);
		}
		if (StringUtil.isNotEmpty(qry_group_id)){
			strSQL.append(" and  sm.userid=sn.userid and sn.valid='Y' and sn.group_id = ?");
			paramList.add(qry_group_id);
		}*/
		strSQL.append(" ) as avg_score,");//问答题教师批改后的平均分数	
		
		//填空题部分正确人数
	    strSQL.append(" (select count(sd.result_id) from learn_my_examination st,learn_examination_result sd,base_studentinfo sm");
	    if (StringUtil.isNotEmpty(qry_group_id)) {
	      strSQL.append(" ,learn_group_member sn");
	    }
	    strSQL.append(" where st.my_examination_id=sd.my_examination_id and st.status>'40' and  sd.paper_praxes_id=t1.paper_praxes_id and sd.userid=sm.userid and sm.state>'0' and  st.send_id=?");
	    paramList.add(qry_send_id);
	    strSQL.append(" and sd.is_right=2");
	    /*if (StringUtil.isNotEmpty(qry_classid)) {
	      strSQL.append(" and sm.classid = ?");
	      paramList.add(qry_classid);
	    }
	    if (StringUtil.isNotEmpty(qry_group_id)) {
	      strSQL.append(" and  sm.userid=sn.userid and sn.valid='Y' and sn.group_id = ?");
	      paramList.add(qry_group_id);
	    }*/
	    strSQL.append(" ) as part_right_count,");
	    
        //答对人数
		strSQL.append(" (select count(sd.result_id) from learn_my_examination st,learn_examination_result sd,base_studentinfo sm");
		if (StringUtil.isNotEmpty(qry_group_id)){
			strSQL.append(" ,learn_group_member sn");
		}		
		strSQL.append(" where st.my_examination_id=sd.my_examination_id and st.status>'40' and  sd.paper_praxes_id=t1.paper_praxes_id and sd.userid=sm.userid and sm.state>'0' and  st.send_id=?");
		paramList.add(qry_send_id);	
		strSQL.append(" and sd.is_right=1");//回答正确条件
		/*if (StringUtil.isNotEmpty(qry_classid)){
			strSQL.append(" and sm.classid = ?");
			paramList.add(qry_classid);
		}
		if (StringUtil.isNotEmpty(qry_group_id)){
			strSQL.append(" and  sm.userid=sn.userid and sn.valid='Y' and sn.group_id = ?");
			paramList.add(qry_group_id);
		}*/
		strSQL.append(" ) as right_count");//答对人数		
		strSQL.append(" from learn_paper_praxes t1");
		strSQL.append("  where t1.valid='Y' and t1.paper_id =?");
		paramList.add(qry_paper_id);
		if (StringUtil.isNotEmpty(qry_paper_praxes_id)){
			strSQL.append("  and t1.paper_praxes_id=?");
			paramList.add(qry_paper_praxes_id);
		}
		strSQL.append(" order by t1.display_order");	
		
		StringBuffer singleSelectSQL = new StringBuffer();
    	ArrayList<Object> optionParam = new ArrayList<Object>();
    	singleSelectSQL.append("select t1.paper_option_id,t1.paper_praxes_id,ifnull(t1.praxes_id,t1.paper_praxes_id) as praxes_id,ifnull(t1.option_id,t1.paper_option_id) as option_id,");    	
    	singleSelectSQL.append(" t1.is_right,t1.option_content,");
        //选中该选项的人数
    	singleSelectSQL.append(" (select count(sd.result_id) from learn_my_examination st,learn_examination_result sd,base_studentinfo sm");
		
		singleSelectSQL.append(" where st.my_examination_id=sd.my_examination_id and  sd.paper_praxes_id=t1.paper_praxes_id and sd.userid=sm.userid and sm.state>'0' and st.send_id=?");
		optionParam.add(qry_send_id);	
		singleSelectSQL.append(" and (sd.praxes_result like Concat('%',t1.option_id,'%')");
		singleSelectSQL.append("     or sd.praxes_result like Concat('%',t1.paper_option_id,'%'))");
	
		singleSelectSQL.append(" ) as select_count");//选中该选项的人数  	
		singleSelectSQL.append(" from learn_paper_options t1 ");
		singleSelectSQL.append(" where t1.valid='Y' and t1.paper_praxes_id= ");
		
		StringBuffer multiSelectSQL = new StringBuffer();
		multiSelectSQL.append(" select t1.praxes_result as paper_option_id,t1.paper_praxes_id,ifnull(t2.praxes_id,t1.paper_praxes_id) as praxes_id,");
		multiSelectSQL.append(" t1.praxes_result as option_id,t1.is_right,count(t1.result_id) as select_count");
		multiSelectSQL.append(" from learn_examination_result t1,learn_paper_praxes t2,learn_my_examination t3");
		multiSelectSQL.append(" where t1.paper_praxes_id=t2.paper_praxes_id and t1.my_examination_id = t3.my_examination_id ");
		multiSelectSQL.append("  and t1.valid = 'Y' and t2.paper_praxes_id=? and t3.send_id=?");
		multiSelectSQL.append(" group by t1.praxes_result");
		multiSelectSQL.append(" order by t1.praxes_result");
    			
    	StringBuffer keywordSQL = new StringBuffer();
    	keywordSQL.append(" select * from (");
    	keywordSQL.append(" select st.k_point_name,(select count(sst.result_id) from learn_examination_result sst,learn_my_examination ssd");
    	keywordSQL.append(" where sst.my_examination_id=ssd.my_examination_id and ssd.send_id=? and sst.paper_praxes_id=?");
    	keywordSQL.append(" and sst.praxes_result like concat('%',st.k_point_name,'%')) as key_count");
    	keywordSQL.append(" from base_knowledge_point st");
    	keywordSQL.append(" where st.valid='Y' and exists ");
    	keywordSQL.append(" (select null from learn_paper_praxes t3,base_knowledge_point t4");
    	keywordSQL.append(" where t3.k_point_id=t4.k_point_id and t3.paper_praxes_id=?");
    	keywordSQL.append(" and t4.grade_code=st.grade_code and t4.subjectid=st.subjectid)) sm");    	
    	keywordSQL.append(" where sm.key_count>0 order by key_count desc");
    	
		
		PlatformDao pdao = new PlatformDao();
	    try{
	    	pdao.setSql(strSQL.toString());
	    	pdao.setBindValues(paramList);
	    	Element result = pdao.executeQuerySql(0,-1);
	    	List list = result.getChildren("Record");
	    	for (int i = 0; i < list.size() ;i++){
	    		Element praxesRec = (Element)list.get(i);
	    		String praxes_type = praxesRec.getChildText("praxes_type");
	    		String paper_praxes_id = praxesRec.getChildText("paper_praxes_id");
	    		//如果单选
	    		if ("20".equals(praxes_type)){	    			
		    		pdao.setSql(singleSelectSQL.toString()+"'"+paper_praxes_id+"' order by display_order");
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
	    		}//如果是多选题,则统计每种多选情况的人数
	    		else if ("30".equals(praxes_type)){	
	    			Map opMap = getOptionMap(paper_praxes_id);
	    			
	    			ArrayList<Object> multiSelectParam = new ArrayList<Object>();
	    			multiSelectParam.add(paper_praxes_id);	 
	    			multiSelectParam.add(qry_send_id);	
		    		pdao.setSql(multiSelectSQL.toString());
		    		pdao.setBindValues(multiSelectParam);
		    		Element optionResult = pdao.executeQuerySql(0,-1);
			    	
		    		List optionList = optionResult.getChildren("Record");
			    	for (int j = 0; j < optionList.size() ;j++){
			    		Element optionRec = (Element)optionList.get(j);
			    		String option_id = optionRec.getChildText("option_id");
			    		if (StringUtil.isEmpty(option_id)){
			    			XmlDocPkgUtil.setChildText(optionRec, "option_content","未选择"); 
			    		}
			    		else{
				    		String[] options = option_id.split(",");
				    		StringBuffer optionName = new StringBuffer();
				    		for (int k=0;k<options.length;k++){
				    			optionName.append(opMap.get(options[k]));
				    		}
				    		XmlDocPkgUtil.setChildText(optionRec, "option_content",optionName.toString());
			    		}
			    	}
		    		Element options = new Element("Options");
		    		options.addContent(optionResult);
		    		praxesRec.addContent(options);	    			
	    		}//如果是问答题,则统计关键词出现次数
	    		else if ("90".equals(praxes_type)){
	    			ArrayList<Object> keywordParam = new ArrayList<Object>();
	    			keywordParam.add(qry_send_id);
	    			keywordParam.add(paper_praxes_id);	    			
	    			keywordParam.add(paper_praxes_id);
	    			
		    		pdao.setSql(keywordSQL.toString());
		    		pdao.setBindValues(keywordParam);
		    		Element keywordResult = pdao.executeQuerySql(0,-1);
			    	
		    		Element keywordStats = new Element("KeywordStat");
		    		keywordStats.addContent(keywordResult);
		    		praxesRec.addContent(keywordStats);	    			
	    		}
	    	}
	    	xmlDocUtil.getResponse().addContent(result);
	    	xmlDocUtil.setResult("0");	    	
	    }
	    catch (Exception e) {
			e.printStackTrace();
			log4j.logError("[试卷结果-获取试卷题目结果信息]"+e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
		} finally {
			pdao.releaseConnection();
		}
	}
	
	/**
	 * 获取待批改的试题及学生回答
	 *
	 */
	public void getPendingPraxesWocr(){
		Element reqElement =  xmlDocUtil.getRequestData();
		String qry_classid = reqElement.getChildText("qry_classid");		
		String qry_send_id = reqElement.getChildText("qry_send_id");
		String qry_group_id = reqElement.getChildText("qry_group_id");
		String qry_type = reqElement.getChildText("qry_type");
		String qry_paper_id = reqElement.getChildText("qry_paper_id");
		
		StringBuffer strSQL = new StringBuffer();
		ArrayList<Object> paramList = new ArrayList<Object>();	
		strSQL.append("select t2.paper_praxes_id,t2.praxes_type,getParamDesc('c_praxes_type',t2.praxes_type) as praxes_type_desc,");
		strSQL.append(" t2.praxes_content,t2.difficulty_level,t2.discrimination,t2.right_result,IFNULL(t2.score,-1) as score ");
		strSQL.append(" from learn_paper_praxes t2");
		strSQL.append(" where t2.valid='Y'");
		strSQL.append(" and t2.paper_id= ? ");
		paramList.add(qry_paper_id);
		strSQL.append(" and  exists ");
		strSQL.append(" (select null from learn_my_examination sd ");
		strSQL.append(" where sd.valid='Y' and sd.paper_id = t2.paper_id and sd.status='60')");
		strSQL.append(" and (t2.right_result is null or  t2.right_result='')");//没有正确答案(填空题,判断题)
		strSQL.append(" and not exists ");//没有正确答案,选择题
		strSQL.append(" (select null from learn_paper_options st");
		strSQL.append(" where st.valid='Y' and st.is_right='1'");
		strSQL.append(" and  st.paper_praxes_id=t2.paper_praxes_id)");
		
		String optionSQL = "select * from learn_paper_options where paper_praxes_id=? and valid='Y' and is_right='1' order by display_order";
		
		
    	StringBuffer pendingSQL = new StringBuffer();
    	ArrayList<Object> pendingParam = new ArrayList<Object>();
    	pendingSQL.append("select  t1.result_id,t1.student_draf_path,t1.teacher_draf_path,t1.paper_praxes_id,t1.consuming_time,t4.username,t4.portrait,t1.praxes_result,IFNULL(t1.score,-1) as score ");
    	pendingSQL.append(" from learn_examination_result t1,learn_my_examination t2,base_studentinfo t3,pcmc_user t4");
		if (StringUtil.isNotEmpty(qry_group_id)){
			pendingSQL.append(" ,learn_group_member sn");
		}    	
    	pendingSQL.append(" where t1.my_examination_id=t2.my_examination_id ");
    	if ("1".equals(qry_type)){//查显示待批改的学生
    	   pendingSQL.append(" and t1.score is null ");  
    	}
    	pendingSQL.append(" and t2.userid=t3.userid and t3.state>'0' and t3.userid=t4.userid");
    	
		if (StringUtil.isNotEmpty(qry_classid)){
			pendingSQL.append(" and t3.classid = ?");
			pendingParam.add(qry_classid);
		}
		if (StringUtil.isNotEmpty(qry_group_id)){
			pendingSQL.append(" and  t4.userid=sn.userid and sn.valid='Y' and sn.group_id = ?");
			pendingParam.add(qry_group_id);
		}
		pendingSQL.append(" and t1.paper_praxes_id = ");
		
		PlatformDao pdao = new PlatformDao();
	    try{
	    	pdao.setSql(strSQL.toString());
	    	pdao.setBindValues(paramList);
	    	Element result = pdao.executeQuerySql(0,-1);
	    	List list = result.getChildren("Record");
	    	for (int i = 0; i < list.size() ;i++){
	    		Element praxesRec = (Element)list.get(i);
	    		String paper_praxes_id = praxesRec.getChildText("paper_praxes_id");
	    		String praxes_type = praxesRec.getChildText("praxes_type");	    		
	    		//如果单选或多选题,则增加正确选项内容
	    		if ("20".equals(praxes_type) || "30".equals(praxes_type)){
		    		ArrayList<Object> optionParam = new ArrayList<Object>();
		    		optionParam.add(praxesRec.getChildText("paper_praxes_id"));
		    		pdao.setSql(optionSQL);
		    		pdao.setBindValues(optionParam);	    			
		    		Element optionResult = pdao.executeQuerySql(0,-1);
		    		List optionList = optionResult.getChildren("Record");
		    		
		    		StringBuffer rightResult = new StringBuffer();
			    	for (int k = 0;k < optionList.size() ;k++){
			    		Element optionsRec = (Element)optionList.get(k);
			    		if (k==0){
			    			rightResult.append(optionsRec.getChildText("option_name"));
			    		}
			    		else{
			    			rightResult.append(","+optionsRec.getChildText("option_name"));
			    		}
			    	}
			    	XmlDocPkgUtil.setChildText(praxesRec, "right_result", rightResult.toString());		    		
	    		}
	    		
	    		pdao.setSql(pendingSQL.toString()+"'"+paper_praxes_id+"' order by t3.studentno");
	    		pdao.setBindValues(pendingParam);
	    		Element pendingResult = pdao.executeQuerySql(0,-1);		    		
	    		Element PraxesResult = new Element("PraxesResults");
	    		PraxesResult.addContent(pendingResult);
	    		praxesRec.addContent(PraxesResult);	    		
	    	}
	    	xmlDocUtil.getResponse().addContent(result);
	    	xmlDocUtil.setResult("0");		    	
	    }
	    catch (Exception e) {
			e.printStackTrace();
			log4j.logError("[试卷结果-获取待批改题目信息]"+e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
		} finally {
			pdao.releaseConnection();
		}
		
	}
	
	/**
	 * 保存批改结果
	 *
	 */
	public void savaCorrectResultWocr(){
		String userid = xmlDocUtil.getSession().getChildTextTrim("userid");
		Element reqElement =  xmlDocUtil.getRequestData();
		String paper_id = reqElement.getChildText("paper_id");
		List resultIDList = reqElement.getChildren("result_id");
		List scoreList = reqElement.getChildren("score");
		PlatformDao pdao = new PlatformDao();
		try {
			
			String resultid= ((Element)resultIDList.get(0)).getText();
			StringBuffer strSQL = new StringBuffer();
			strSQL.append("select t2.score from learn_examination_result t1,learn_paper_praxes t2");
			strSQL.append(" where t1.paper_praxes_id=t2.paper_praxes_id and t1.result_id=?");
			ArrayList<Object> qryParam = new ArrayList<Object>();
			qryParam.add(resultid);
			
			pdao.setSql(strSQL.toString());
			pdao.setBindValues(qryParam);
			Element result = pdao.executeQuerySql(-1, 0);
			String praxesScore = result.getChild("Record").getChildTextTrim("score");
			if (StringUtil.isEmpty(praxesScore)){
				praxesScore = "100";
			}
			
			pdao.beginTransaction();
			for (int i=0;i<resultIDList.size();i++){
				String resultScore = ((Element)scoreList.get(i)).getText();
				
				Element examResultRec = ConfigDocument.createRecordElement("yuexue", "learn_examination_result");
				XmlDocPkgUtil.copyValues(reqElement, examResultRec, i, true);
				String is_right="0";
				if (praxesScore.equals(resultScore)){
					is_right = "1";
				}
				else if (!"0".equals(resultScore)){
					is_right = "2";
				}
				XmlDocPkgUtil.setChildText(examResultRec, "is_right", is_right);
				XmlDocPkgUtil.setChildText(examResultRec, "teacherid", userid);
				XmlDocPkgUtil.setChildText(examResultRec, "marking_time", DatetimeUtil.getNow(""));
				XmlDocPkgUtil.setChildText(examResultRec, "modify_date", DatetimeUtil.getNow(""));				
				pdao.updateOneRecord(examResultRec); 
			}
			
			StringBuffer updateSQL = new StringBuffer();
			ArrayList<Object> paramList = new ArrayList<Object>();
			updateSQL.append("update learn_my_examination set status='70',paper_score=(select sum(st.score) from learn_examination_result st");
			updateSQL.append("  where st.my_examination_id=learn_my_examination.my_examination_id)");
			updateSQL.append(" where valid='Y' and status='60' and paper_id = ?");
			updateSQL.append(" and exists (select null from learn_examination_result st ");
			updateSQL.append("            where st.my_examination_id=learn_my_examination.my_examination_id");
			updateSQL.append("            and   score is not null and teacherid is not null )");
			updateSQL.append(" and not exists (select null from learn_examination_result st");
			updateSQL.append("            where st.my_examination_id=learn_my_examination.my_examination_id");
			updateSQL.append("            and   score is  null)");			
			paramList.add(paper_id);
			
			pdao.setSql(updateSQL.toString());
			pdao.setBindValues(paramList);			
			pdao.executeTransactionSql();
			
			pdao.commitTransaction();
	    	xmlDocUtil.setResult("0");	
	    	xmlDocUtil.writeHintMsg("00105", "批改结果保存成功!");
		}
		catch (Exception e) {
			pdao.rollBack();
			e.printStackTrace();
			log4j.logError("[试卷结果-保存批改结果]"+e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);            			
		} finally {
			pdao.releaseConnection();
		}
	}
	
	/**
	 * 获取作业结果
	 *
	 */
	public void getPaperResultWocr(){		
		Element reqElement =  xmlDocUtil.getRequestData();
		String paper_id = reqElement.getChildText("paperid");
		
		ArrayList<Object> paramList = new ArrayList<Object>();
		paramList.add(paper_id);

		//查询提交的学生
		StringBuffer userSQL = new StringBuffer();
		userSQL.append("SELECT t2.paper_id,t2.my_examination_id,t2.status,t2.userid,t5.username");
		userSQL.append(" FROM learn_my_examination t2, pcmc_user t5");
		userSQL.append(" WHERE  t2.userid = t5.userid AND t2.status > '10' AND t2.valid = 'Y'");
		userSQL.append(" AND t2.paper_id = ?");
		userSQL.append(" order by t2.userid");
		
		//查询学生答题的结果
		StringBuffer examResultSQL = new StringBuffer();
		ArrayList<Object> paramExamList = new ArrayList<Object>();
		paramExamList.add(paper_id);
		examResultSQL.append("select tt1.userid,tt1.paper_praxes_id,tt1.display_order,sm.result_id,sm.praxes_result,sm.is_right,sm.score,tt1.praxes_type");
		examResultSQL.append(" from (select t3.userid,t2.paper_praxes_id,t2.display_order,t3.my_examination_id,t2.praxes_type");
		examResultSQL.append("   from learn_paper_praxes t2,learn_my_examination t3,base_studentinfo t4");
		examResultSQL.append("   where t2.valid='Y' and t3.valid = 'Y' AND t3.status > '10' and t2.paper_id = t3.paper_id and t4.userid=t3.userid");
		examResultSQL.append("   and t2.paper_id=? ");
		examResultSQL.append("  ) tt1 left join learn_examination_result sm ");
		examResultSQL.append("   on sm.my_examination_id=tt1.my_examination_id and sm.paper_praxes_id=tt1.paper_praxes_id");
		examResultSQL.append(" order by tt1.userid,tt1.display_order");		
		
		PlatformDao pdao = new PlatformDao();
		try {
			pdao.setSql(examResultSQL.toString());
			pdao.setBindValues(paramExamList);
			Element examResult = pdao.executeQuerySql(-1, 0);
			
			pdao.setSql(userSQL.toString());
			pdao.setBindValues(paramList);
			
			Element result = pdao.executeQuerySql(-1, 0);
			List userList = result.getChildren("Record");
	
			for (int i=0;i<userList.size();i++){
				Element stuRec = (Element)userList.get(i);
				Element stuExamResult = filterRecordByField(examResult,"userid",stuRec.getChildTextTrim("userid"));
		    	
				Element stuExam = new Element("ExamResult");
		    	stuExam.addContent(stuExamResult);
		    	stuRec.addContent(stuExam);			
			}
			
	    	xmlDocUtil.getResponse().addContent(result);
	    	xmlDocUtil.setResult("0");	    	
		}
		catch (Exception e) {
			e.printStackTrace();
			log4j.logError("[试卷结果-取学生作业结果]"+e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);            			
		} finally {
			pdao.releaseConnection();
		}		
	}
	
	/**
	 * 获取学生试题作业成绩
	 *
	 */
	public void getStuPraxesResultWocr(){
		Element reqElement =  xmlDocUtil.getRequestData();
		String usertype = xmlDocUtil.getSession().getChildText("usertype");
		
		String paper_id = reqElement.getChildText("paper_id");
		String my_examination_id = reqElement.getChildText("my_examination_id");		
		//String qry_user_id=reqElement.getChildText("qry_user_id");
		String paper_praxes_id=reqElement.getChildText("paper_praxes_id");
		
		Element result = super.getPaperPraxesInfoWocr(paper_id,my_examination_id,paper_praxes_id,usertype);
		
		if (result!=null){
	    	xmlDocUtil.getResponse().addContent(result);
	    	xmlDocUtil.setResult("0");			
		}
		else{
			xmlDocUtil.writeErrorMsg("20206","取学生作业结果发生错误!");
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);  
		}		
	}
	
	/**
	 * 获取试卷试题结果
	 *
	 */
	public void getPaperPraxesResultWocr(){
		String optionStr = "A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z";
		String[] optionArray = optionStr.split(",");		
		Element reqElement =  xmlDocUtil.getRequestData();
		String qry_paper_id = reqElement.getChildText("qry_paper_id");
		String qry_paper_praxes_id=reqElement.getChildText("qry_paper_praxes_id");

		StringBuffer strSQL = new StringBuffer();
		ArrayList<Object> paramList = new ArrayList<Object>();
		strSQL.append("select t1.paper_praxes_id,t1.paper_id,t1.praxes_id,t1.praxes_type,t1.praxes_content,t1.score,");
		//strSQL.append("'"+qry_send_id+"' as send_id,'"+qry_classid+"' as classid,");//取答对学生列表时要用到
		strSQL.append("t1.right_result,getParamDesc('c_praxes_type',t1.praxes_type) as praxes_type_desc,");
        //发送给学生的人数人数
		strSQL.append(" (select count(st.my_examination_id) from learn_my_examination st");		
		strSQL.append(" where st.paper_id=t1.paper_id and st.status > '10' ");
		strSQL.append(" ) as send_stu_count,");//发送给学生的人数人数
		//答题人数
		strSQL.append(" (select count(sd.result_id) from learn_my_examination st,learn_examination_result sd,base_studentinfo sm");
			
		strSQL.append(" where st.my_examination_id=sd.my_examination_id and st.status>'40' and  sd.paper_praxes_id=t1.paper_praxes_id and sd.userid=sm.userid and sm.state>'0'");
		
		strSQL.append(" ) as stu_count,");//答题人数
        //问答题教师批改后的平均分数	
		strSQL.append(" (select round(avg(sd.score),1) from learn_my_examination st,learn_examination_result sd,base_studentinfo sm");
			
		strSQL.append(" where st.my_examination_id=sd.my_examination_id and st.status>'40' and  sd.paper_praxes_id=t1.paper_praxes_id ");
		strSQL.append(" and sd.score is not null and sd.userid=sm.userid and sm.state>'0'");
		strSQL.append(" ) as avg_score,");//问答题教师批改后的平均分数	
		
		//填空题部分正确人数
	    strSQL.append(" (select count(sd.result_id) from learn_my_examination st,learn_examination_result sd,base_studentinfo sm");
	   
	    strSQL.append(" where st.my_examination_id=sd.my_examination_id and st.status>'40' and  sd.paper_praxes_id=t1.paper_praxes_id and sd.userid=sm.userid and sm.state>'0'");
	    strSQL.append(" and sd.is_right=2");
	    strSQL.append(" ) as part_right_count,");
	    
        //答对人数
		strSQL.append(" (select count(sd.result_id) from learn_my_examination st,learn_examination_result sd,base_studentinfo sm");
				
		strSQL.append(" where st.my_examination_id=sd.my_examination_id and st.status>'40' and  sd.paper_praxes_id=t1.paper_praxes_id and sd.userid=sm.userid and sm.state>'0'");
		strSQL.append(" and sd.is_right=1");
		strSQL.append(" ) as right_count");//答对人数		
		strSQL.append(" from learn_paper_praxes t1");
		strSQL.append("  where t1.valid='Y' and t1.paper_id =?");
		paramList.add(qry_paper_id);
		if (StringUtil.isNotEmpty(qry_paper_praxes_id)){
			strSQL.append("  and t1.paper_praxes_id=?");
			paramList.add(qry_paper_praxes_id);
		}
		strSQL.append(" order by t1.display_order");	
		
    	StringBuffer optionSQL = new StringBuffer();
    	ArrayList<Object> optionParam = new ArrayList<Object>();
    	optionSQL.append("select t1.paper_option_id,t1.paper_praxes_id,ifnull(t1.praxes_id,t1.paper_praxes_id) as praxes_id,ifnull(t1.option_id,t1.paper_option_id) as option_id,");    	
    	optionSQL.append(" t1.is_right,t1.option_content,");
        //选中该选项的人数
    	optionSQL.append(" (select count(sd.result_id) from learn_my_examination st,learn_examination_result sd,base_studentinfo sm");
			
		optionSQL.append(" where st.my_examination_id=sd.my_examination_id and  sd.paper_praxes_id=t1.paper_praxes_id and sd.userid=sm.userid and sm.state>'0'");
		
		optionSQL.append(" and (sd.praxes_result like Concat('%',t1.option_id,'%')");
		optionSQL.append("     or sd.praxes_result like Concat('%',t1.paper_option_id,'%'))");
		
		optionSQL.append(" ) as select_count");//选中该选项的人数  	
    	optionSQL.append(" from learn_paper_options t1 ");
    	optionSQL.append(" where t1.valid='Y' and t1.paper_praxes_id= ");
    			
    	StringBuffer keywordSQL = new StringBuffer();
    	keywordSQL.append(" select * from (");
    	keywordSQL.append(" select st.k_point_name,(select count(sst.result_id) from learn_examination_result sst,learn_my_examination ssd");
    	keywordSQL.append(" where sst.my_examination_id=ssd.my_examination_id and sst.paper_praxes_id=?");
    	keywordSQL.append(" and sst.praxes_result like concat('%',st.k_point_name,'%')) as key_count");
    	keywordSQL.append(" from base_knowledge_point st");
    	keywordSQL.append(" where st.valid='Y' and exists ");
    	keywordSQL.append(" (select null from learn_paper_praxes t3,base_knowledge_point t4");
    	keywordSQL.append(" where t3.k_point_id=t4.k_point_id and t3.paper_praxes_id=?");
    	keywordSQL.append(" and t4.grade_code=st.grade_code and t4.subjectid=st.subjectid)) sm");    	
    	keywordSQL.append(" where sm.key_count>0 order by key_count desc");
    	
		
		PlatformDao pdao = new PlatformDao();
	    try{
	    	pdao.setSql(strSQL.toString());
	    	pdao.setBindValues(paramList);
	    	Element result = pdao.executeQuerySql(0,-1);
	    	List list = result.getChildren("Record");
	    	for (int i = 0; i < list.size() ;i++){
	    		Element praxesRec = (Element)list.get(i);
	    		String praxes_type = praxesRec.getChildText("praxes_type");
	    		String paper_praxes_id = praxesRec.getChildText("paper_praxes_id");
	    		//如果单选或多选题,则增加选项内容
	    		if ("20".equals(praxes_type) || "30".equals(praxes_type)){		    		
		    		pdao.setSql(optionSQL.toString()+"'"+paper_praxes_id+"' order by display_order");
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
	    		}//如果是问答题,则统计关键词出现次数
	    		else if ("90".equals(praxes_type)){
	    			ArrayList<Object> keywordParam = new ArrayList<Object>();
	    			
	    			keywordParam.add(paper_praxes_id);	    			
	    			keywordParam.add(paper_praxes_id);
	    			
		    		pdao.setSql(keywordSQL.toString());
		    		pdao.setBindValues(keywordParam);
		    		Element keywordResult = pdao.executeQuerySql(0,-1);
			    	
		    		Element keywordStats = new Element("KeywordStat");
		    		keywordStats.addContent(keywordResult);
		    		praxesRec.addContent(keywordStats);	    			
	    		}
	    	}
	    	xmlDocUtil.getResponse().addContent(result);
	    	xmlDocUtil.setResult("0");	    	
	    }
	    catch (Exception e) {
			e.printStackTrace();
			log4j.logError("[试卷结果-获取试卷题目结果信息]"+e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
		} finally {
			pdao.releaseConnection();
		}
	}
	
	private Map getOptionMap(String paper_praxes_id){
		Map<String,String> opMap = new HashMap<String,String>();
		
		StringBuffer multiOptionSQL = new StringBuffer();
		multiOptionSQL.append(" select t1.option_id,t1.option_content from learn_paper_options t1");
		multiOptionSQL.append(" where t1.paper_praxes_id=? and t1.valid='Y' order by t1.display_order");
		ArrayList<Object> paramList = new ArrayList<Object>();		
		paramList.add(paper_praxes_id);
		
		PlatformDao pdao = new PlatformDao();
		try{
	    	pdao.setSql(multiOptionSQL.toString());
	    	pdao.setBindValues(paramList);	    	
			Element opData = pdao.executeQuerySql(0, -1);
			
			List opList = opData.getChildren("Record");
			for (int j = 0; j < opList.size() ;j++){
				Element opRec = (Element)opList.get(j);
				opMap.put(opRec.getChildText("option_id"), opRec.getChildText("option_content"));
			}			
		}
	    catch (Exception e) {
	    	
			e.printStackTrace();
		} finally {
			pdao.releaseConnection();
		}
		return opMap;
	}
}
