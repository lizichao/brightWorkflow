package cn.com.bright.yuexue.teach;

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
import cn.com.bright.yuexue.util.XmlResultUtil;

/**
 * <p>Title:作业管理</p>
 * <p>Description: 作业管理维护类</p>
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
 * <p> zhangxq    2015/02/27       1.0          build this moudle </p>
 *     
 */
public class HomeWork {
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
		if ("getHomeWork".equals(action)){
			getHomeWork();
		}
		else if ("saveHomeWork".equals(action)){
			saveHomeWork();
		}
		return xmlDoc;
	}
	/**
	 * 保存作业单
	 *
	 */
	public void saveHomeWork(){
		String userName = xmlDocUtil.getSession().getChildTextTrim("username");
		Element reqElement =  xmlDocUtil.getRequestData();
		String classid = reqElement.getChildText("classid");
		String subjectid = reqElement.getChildText("subjectid");
		List contentList = reqElement.getChildren("homework_content");
		List idList = reqElement.getChildren("homework_id");
		
		String[] returnData = {"homework_id"};
		Element resData = XmlDocPkgUtil.createMetaData(returnData);	
		
		PlatformDao pdao = new PlatformDao();
		try {
			pdao.beginTransaction();
			int idCount = idList.size();
			for (int i=0;i<contentList.size();i++){	
				String homework_content = ((Element)contentList.get(i)).getText();
				if (StringUtil.isNotEmpty(homework_content)){
					String homework_id = null;
					if (idCount>i){
						homework_id = ((Element)idList.get(i)).getText();
						if (homework_id.length()<20){
							homework_id=null;
						}
					}
					Element homeworkRec = ConfigDocument.createRecordElement("yuexue", "learn_homework");
					XmlDocPkgUtil.copyValues(reqElement, homeworkRec, i, true);
					XmlDocPkgUtil.setChildText(homeworkRec, "classid", classid);
					XmlDocPkgUtil.setChildText(homeworkRec, "subjectid", subjectid);
					if (StringUtil.isEmpty(homework_id)){
						homeworkRec.removeChild("homework_id");						
						homework_id = pdao.insertOneRecordSeqPk(homeworkRec).toString();					
					}
					else{
						XmlDocPkgUtil.setChildText(homeworkRec, "modify_by", userName);
						XmlDocPkgUtil.setChildText(homeworkRec, "modify_date", DatetimeUtil.getNow(""));                	
	                	pdao.updateOneRecord(homeworkRec); 					
					}
					resData.addContent(XmlDocPkgUtil.createRecord(returnData,new String[] {homework_id}));
				}				
			}
			
			pdao.commitTransaction();
			xmlDocUtil.getResponse().addContent(resData);
	    	xmlDocUtil.setResult("0");	
	    	xmlDocUtil.writeHintMsg("00103", "保存作业单成功!");			
		}
		catch (Exception e) {
			pdao.rollBack();
			e.printStackTrace();
			log4j.logError("[作业单管理-保存作业单]"+e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);            			
		} finally {
			pdao.releaseConnection();
		}		
	}
	/**
	 * 取作业单
	 *
	 */
	public void getHomeWork(){
		Element reqElement =  xmlDocUtil.getRequestData();
		Element sessElement =  xmlDocUtil.getSession();	
		
		String userid = sessElement.getChildText("userid");
		String username = sessElement.getChildText("username");
		
		String qry_begin_date = reqElement.getChildText("qry_begin_date");
		String qry_end_date = reqElement.getChildText("qry_end_date");
		String qry_subjectid = reqElement.getChildText("qry_subjectid");
		String qry_classid = reqElement.getChildText("qry_classid");
		
		StringBuffer strSQL = new StringBuffer();
		ArrayList<Object> paramList = new ArrayList<Object>();

		strSQL.append("select t1.*,t2.subjname,t4.userid as teacher_id,t4.assistant_id,t5.username as assistant_name");
		strSQL.append(" from  learn_homework t1,base_subject t2,base_teacher_subject t4");
		strSQL.append(" left join pcmc_user t5 on t4.assistant_id=t5.userid");
		strSQL.append(" where t1.valid='Y' and t2.state>'0' and t4.state>'0' and t1.subjectid=t2.subjectid");
		strSQL.append(" and t1.classid=t4.classid and t1.subjectid=t4.subjectid ");
		
		if (StringUtil.isEmpty(qry_begin_date) && StringUtil.isEmpty(qry_end_date)){
			strSQL.append(" and to_days(t1.create_date)=(select to_days(max(sd.create_date)) from learn_homework sd");
			strSQL.append(" where sd.valid='Y' ");
			if (StringUtil.isNotEmpty(qry_subjectid)){
			   strSQL.append(" and sd.subjectid=? ");
			   paramList.add(qry_subjectid);
			}
			if (StringUtil.isNotEmpty(qry_classid)){
			   strSQL.append(" and sd.classid=?");			
			   paramList.add(qry_classid);
			}
			strSQL.append(" )");		
		}
		else{
			if (StringUtil.isNotEmpty(qry_begin_date)){
			    strSQL.append(" and t1.create_date>=?");
			    paramList.add(SqlTypes.getConvertor("Timestamp").convert(qry_begin_date, "yyyy-MM-dd HH:mm:ss"));
			}
			if (StringUtil.isNotEmpty(qry_end_date)){
			    strSQL.append(" and t1.create_date <= ?");
			    paramList.add(SqlTypes.getConvertor("Timestamp").convert(qry_end_date, "yyyy-MM-dd HH:mm:ss"));
			    strSQL.append(" and to_days(t1.create_date)= (select to_days(max(sd.create_date)) from learn_homework sd");
			    strSQL.append(" where sd.valid='Y' and sd.create_date <= ?");
			    paramList.add(SqlTypes.getConvertor("Timestamp").convert(qry_end_date, "yyyy-MM-dd HH:mm:ss"));
			    if (StringUtil.isNotEmpty(qry_subjectid)){
				   strSQL.append(" and sd.subjectid=? ");
				   paramList.add(qry_subjectid);
				}
				if (StringUtil.isNotEmpty(qry_classid)){
				   strSQL.append(" and sd.classid=?");			
				   paramList.add(qry_classid);
				}	
				strSQL.append(" )");	
			}
			
		}
		if (StringUtil.isNotEmpty(qry_subjectid)){
			strSQL.append(" and t1.subjectid=?");
			paramList.add(qry_subjectid);
		}
		if (StringUtil.isNotEmpty(qry_classid)){
			strSQL.append(" and t1.classid=?");
			paramList.add(qry_classid);
		}		
		strSQL.append(" order by to_days(t1.create_date),t2.subjcode,t1.create_date desc");
		
		StringBuffer subjSQL = new StringBuffer();
		ArrayList<Object> subjParam = new ArrayList<Object>();
		subjSQL.append("select t1.subjectid,t1.classid,t2.subjname,t1.userid as teacher_id,t1.assistant_id,t3.username as assistant_name ");
		subjSQL.append(" from base_teacher_subject t1 left join pcmc_user t3 on t1.assistant_id=t3.userid,base_subject t2");
		subjSQL.append(" where t1.state>'0' and t1.subjectid=t2.subjectid");
		subjSQL.append(" and (t1.userid=? or t1.assistant_id=?)");
		subjParam.add(userid);
		subjParam.add(userid);
		if (StringUtil.isNotEmpty(qry_classid)){
			subjSQL.append(" and t1.classid=?");
			subjParam.add(qry_classid);
		}
		
	    PlatformDao pdao = new PlatformDao();
	    try{
	    	pdao.setSql(subjSQL.toString());
	    	pdao.setBindValues(subjParam);
	    	Element subjResult = pdao.executeQuerySql(0,-1);
	    	List subjList = subjResult.getChildren("Record");
	    	
	    	pdao.setSql(strSQL.toString());
	    	pdao.setBindValues(paramList);
	    	Element homeWorkResult = pdao.executeQuerySql(0,-1);	    	
	    	List homeWorkList = homeWorkResult.getChildren("Record");
	    	
	    	String subjectid="";
	    	String currDate = DatetimeUtil.getNow("yyyy-MM-dd");
	    	for (int i=0;i<homeWorkList.size();i++){
	    		Element homeWorkRec = (Element)homeWorkList.get(i);
	    		if (!subjectid.equals(homeWorkRec.getChildText("subjectid"))){
		    		subjectid = homeWorkRec.getChildText("subjectid");
		    		for (int j=0;j<subjList.size();j++){
		    			Element subjRec = (Element)subjList.get(j);
		    			if (subjectid.equals(subjRec.getChildText("subjectid")) && currDate.equals(homeWorkRec.getChildText("create_date").substring(0, 10))){
		    				subjList.remove(j);
		    			}
		    		}
	    		}
	    	}
	    	if (StringUtil.isEmpty(qry_end_date)){
		    	//将教师任教或学生担任课代表的学科增加
		    	for (int i=0;i<subjList.size();i++){
		    		Element subjRec = (Element)subjList.get(i);
		    		XmlDocPkgUtil.setChildText(subjRec,"homework_id",subjRec.getChildText("subjectid"));
		    		XmlDocPkgUtil.setChildText(subjRec,"homework_content","");
		    		XmlDocPkgUtil.setChildText(subjRec,"valid","Y");
		    		XmlDocPkgUtil.setChildText(subjRec,"create_userid",userid);
		    		XmlDocPkgUtil.setChildText(subjRec,"create_by",username);
		    		XmlDocPkgUtil.setChildText(subjRec,"create_date",DatetimeUtil.getNow(""));
		    		XmlDocPkgUtil.setChildText(subjRec,"modify_by",username);
		    		XmlDocPkgUtil.setChildText(subjRec,"modify_date",DatetimeUtil.getNow(""));
		    		
		    		XmlResultUtil.addRecordToData(homeWorkResult,subjRec);	    		
		    	}
	    	}
	    	xmlDocUtil.getResponse().addContent(homeWorkResult);
	    	xmlDocUtil.setResult("0");
	    	
	    }catch (Exception e) {
			e.printStackTrace();
			log4j.logError("[作业单-检索作业]"+e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
		} finally {
			pdao.releaseConnection();
		}		
	}
}
