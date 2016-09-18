package cn.com.bright.yuexue.student;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;

import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.db.SqlTypes;
import cn.brightcom.jraf.util.ConfigDocument;
import cn.brightcom.jraf.util.Constants;
import cn.brightcom.jraf.util.DaoUtil;
import cn.brightcom.jraf.util.DatetimeUtil;
import cn.brightcom.jraf.util.FileUtil;
import cn.brightcom.jraf.util.Log;
import cn.brightcom.jraf.util.StringUtil;
import cn.brightcom.jraf.util.XmlDocPkgUtil;
import cn.com.bright.yuexue.teach.ExamPaper;
import cn.com.bright.yuexue.util.AttachmentUtil;

/**
 * <p>Title:学生在线学习</p>
 * <p>Description: 学生在线学习</p>
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
 * <p> zhangxq    2014/08/12       1.0          build this moudle </p>
 *     
 */
/**
 * @author lxkj
 *
 */
public class Learning {
	private XmlDocPkgUtil xmlDocUtil = null;
	private Log log4j = new Log(this.getClass().toString());
	private static String  upFolder ="/upload/doc/teacherphoto/";	
    /**
     * 动态委派入口
     * @param request xmlDoc 
     * @return response xmlDoc
     */	
	public Document doPost(Document xmlDoc) {
		xmlDocUtil = new XmlDocPkgUtil(xmlDoc);
		String action = xmlDocUtil.getAction();
		
        if ("getPaperPraxes".equals(action)){
        	getPaperPraxes();
		}
        else if ("saveExamResult".equals(action)){
        	saveExamResult();
		}
        else if ("submitExamResult".equals(action)){
        	submitExamResult();
		}
        else if ("receiveExamPaper".equals(action)){
        	receiveExamPaper();
		}
        else if ("startExamPaper".equals(action)){
        	startExamPaper();
		}
        else if ("leaveMyExamPaper".equals(action)){
        	leaveMyExamPaper();
		}        
        else if ("redoExamPaper".equals(action)){
        	redoExamPaper();
		}        
        else if ("getMyExamPaper".equals(action)){
        	getMyExamPaper();
        }
        else if ("getMyExamError".equals(action)){
        	getMyExamError();
        }
        else if ("getPaperInfo".equals(action)){
			getPaperInfo();
		}
        else if ("getAllExamPaper".equals(action)){
        	getAllExamPaper();
        }
        else if ("getMyPaperCourse".equals(action)){
        	getMyPaperCourse();
        }
        else if ("saveRecommended".equals(action)){
        	saveRecommended();
        }
        else if ("getRecommendList".equals(action)){
        	getRecommendList();
        }
        else if ("updateRecommended".equals(action)){
        	updateRecommended();
        }
        else if ("getMyExamPaperInfo".equals(action)){
        	getMyExamPaperInfo();	
        }
        else if ("getMyExamPaperWocr".equals(action)){
        	getMyExamPaperWocr();
        }
        else if ("addMyExamPaper".equals(action)){
        	addMyExamPaper();
        }
        else if("getPaperPraxesWocr".equals(action)){
        	getPaperPraxesWocr();
		}
        else if ("getPaperInfoWocr".equals(action)){
			getPaperInfoWocr();
		}
        else if ("getPaperCourseWocr".equals(action)){
        	getPaperCourseWocr();
		}
		return xmlDoc;
	}
	private void updateRecommended() {
		Element reqData = xmlDocUtil.getRequestData();
    	PlatformDao pDao = null;
        try
        {
        	pDao = new PlatformDao();
        	
        	Element cover_path = reqData.getChild("cover_path_file");
        	String c_img1 = moveFile(cover_path);
        	
        	Element rec = ConfigDocument.createRecordElement("yuexue","learn_examination_recommend");
        	XmlDocPkgUtil.copyValues(reqData,rec,0,true);
        	if(StringUtil.isNotEmpty(c_img1)){
        		XmlDocPkgUtil.setChildText(rec, "cover_path", c_img1);
        	}
        	pDao.updateOneRecord(rec);
        	
        	String[] flds = {"cover_path"};
            Element rData = XmlDocPkgUtil.createMetaData(flds);
            Element pkRec = XmlDocPkgUtil.createRecord(flds, new String[]{c_img1==null?"":c_img1});
            rData.addContent(pkRec);
        	xmlDocUtil.getResponse().addContent(rData);
            
        	xmlDocUtil.writeHintMsg("10200", "修改推荐成功！");
        	xmlDocUtil.setResult("0");
        }
        catch(Exception ex)
        {
        	ex.printStackTrace();
        	log4j.logError(ex);
        	xmlDocUtil.writeErrorMsg("10220", "修改推荐异常");
        }
        finally
        {
        	pDao.releaseConnection();
        }
		
	}
	private void getRecommendList() {
		Element reqElement =  xmlDocUtil.getRequestData();
		String  create_by = reqElement.getChildText("create_by");
		String  grade_code = reqElement.getChildText("qry_grade_code");
		String  paper_name = reqElement.getChildText("qry_paper_name");
		String  subjectid = reqElement.getChildText("qry_subjectid");
		
		StringBuffer sb = new StringBuffer();
		ArrayList<String> list = new ArrayList<String>();
		
		sb.append("SELECT DISTINCT t1.recommended_id,t1.paper_id,t2.subject_id,t1.cover_path,t1.cover_color,t1.orderidx,t1.valid,t1.recommend,t5.gradeName grade_name,t5.gradecode grade_code,t4.coursenm,t6.folder_name,t6.folder_id,t6.folder_code,t2.resource_type,t2.paper_name,t7.attachment_id,");
		sb.append("t1.create_by,t3.username,t1.create_date,t1.remark FROM learn_examination_recommend t1,learn_examination_paper t2,pcmc_user t3,base_course t4,base_grade t5,base_book_folder t6,learn_paper_attachment t7 ");
		sb.append(" WHERE t2.user_id = t3.userid AND t2.subject_id = t4.subjectid AND t2.folder_id = t6.folder_id AND t1.paper_id = t2.paper_id and t2.paper_id = t7.paper_id");
		sb.append(" AND t1.valid = 'Y' AND t2.grade_code = t4.gradecode AND t4.gradecode = t5.gradecode ");
		
		if (StringUtil.isNotEmpty(create_by)){
			sb.append(" and t3.username like ?");
			list.add("%"+create_by+"%");
		}
		if (StringUtil.isNotEmpty(subjectid)){
			sb.append(" and t2.subject_id = ?");
			list.add(subjectid);
		}
		if (StringUtil.isNotEmpty(grade_code)){
			sb.append(" and t5.gradename like ?");
			list.add(grade_code+"%");
		}
		if (StringUtil.isNotEmpty(paper_name)){
			sb.append(" and t2.paper_name like ?");
			list.add("%"+paper_name+"%");
		}
		sb.append(" order by orderidx");
		PlatformDao pdao = new PlatformDao();
		try{
	    	pdao.setSql(sb.toString());
	    	pdao.setBindValues(list);
	    	Element result = pdao.executeQuerySql(xmlDocUtil.getPageSize(), xmlDocUtil.getPageNo());
	    	
	    	xmlDocUtil.getResponse().addContent(result);
	    	xmlDocUtil.setResult("0");
	    	
	    }catch (Exception e) {
			e.printStackTrace();
			log4j.logError("[在线学习-取所有的推荐(课件)]"+e.getMessage());
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
	private void saveRecommended() {
		Element reqElement =  xmlDocUtil.getRequestData();
		String  attachmentType = reqElement.getChildText("attachment_type");
		String  paper_id = reqElement.getChildText("paper_id");
		Element  cover_path = reqElement.getChild("cover_path_file");
		String  cover_color = reqElement.getChildText("cover_color");
		String  orderidx = reqElement.getChildText("orderidx");
		
		
		PlatformDao pdao = null;
		try{
			pdao = new PlatformDao();
			String moveFile = moveFile(cover_path);
			Element updateRecord = ConfigDocument.createRecordElement("yuexue", "learn_examination_recommend");
			XmlDocPkgUtil.copyValues(reqElement, updateRecord, 0, true);
			if(StringUtil.isNotEmpty(moveFile)){
				XmlDocPkgUtil.setChildText(updateRecord, "cover_path", moveFile);
				}
			
			Object pk = pdao.insertOneRecordSeqPk(updateRecord);        	
        	String[] flds = {"recommended_id","cover_path"};
            Element rData = XmlDocPkgUtil.createMetaData(flds);
            Element pkRec = XmlDocPkgUtil.createRecord(flds,
            new String[]{""+pk,moveFile});
            rData.addContent(pkRec);
			
			xmlDocUtil.writeHintMsg("10100", "新增推荐成功！");
        	xmlDocUtil.setResult("0");
        	xmlDocUtil.getResponse().addContent(rData);
		}catch (Exception ex) {
			ex.printStackTrace();
        	log4j.logError(ex);
        	xmlDocUtil.writeErrorMsg("10120", "新增推荐异常");
        }
		finally
        {
        	pdao.releaseConnection();
        }
	}
	/**
	 * 取试卷信息
	 *
	 */
	public void getPaperInfo(){
		Element reqElement =  xmlDocUtil.getRequestData();
		String usertype = xmlDocUtil.getSession().getChildText("usertype");
		String  attachmentType = reqElement.getChildText("attachment_type");
		String  paper_id = reqElement.getChildText("paper_id");
		String  my_examination_id = reqElement.getChildText("my_examination_id");
		
		StringBuffer strSQL = new StringBuffer();
		strSQL.append("select d.subjname,getParamDesc('c_grade',t.grade_code) as grade_name,m.folder_name,n.paper_score,n.send_id,");
		strSQL.append(" (select sum(t1.score) from learn_paper_praxes t1 where t1.valid='Y'  and t1.paper_id=t.paper_id) as total_score,");
		strSQL.append(" getParamDesc('c_resource_type',t.resource_type) as resource_type_desc,st.send_content,");
		if ("1".equals(usertype)){
			strSQL.append(" st.show_result,");			
		}
		else{
			strSQL.append(" 'Y' as show_result,");
		}
		strSQL.append(" st.complete_time,st.create_by as send_by,n.consuming_time as consume_time,n.status,t.*");		
		strSQL.append(" from learn_examination_paper t,base_subject d,base_book_folder m,");
		strSQL.append(" learn_my_examination n,learn_paper_send st");
		strSQL.append(" where d.state='1' and t.subject_id=d.subjectid and t.folder_id=m.folder_id");
		strSQL.append(" and t.paper_id=st.paper_id and st.send_id=n.send_id");
	    strSQL.append(" and t.paper_id=n.paper_id and t.paper_id=? and n.my_examination_id=?");
		
	    ArrayList<Object> paramList = new ArrayList<Object>();
		paramList.add(paper_id);
		paramList.add(my_examination_id);
		
	    PlatformDao pdao = new PlatformDao();
	    try{
	    	pdao.setSql(strSQL.toString());
	    	pdao.setBindValues(paramList);
	    	Element result = pdao.executeQuerySql(0, -1);
	    	
	    	if (StringUtil.isNotEmpty(attachmentType) && "draft".equals(attachmentType)){
		    	StringBuffer attSQL = new StringBuffer();
		    	attSQL.append("select t2.draft_id as file_id,t1.* from learn_attachment t1,learn_paper_draft t2");
		    	attSQL.append(" where t2.valid='Y' and t1.attachment_id=t2.attachment_id and t2.my_examination_id=?");
		    	attSQL.append(" order by t2.create_date");		    	
		    	
		    	ArrayList<Object> attachParam = new ArrayList<Object>();
		    	attachParam.add(my_examination_id);
		    	
		    	pdao.setSql(attSQL.toString());
		    	pdao.setBindValues(attachParam);
		    	
		    	Element attResult = pdao.executeQuerySql(0, -1);
		    	Element att = new Element("Drafts");
		    	att.addContent(attResult);
		    	result.getChild("Record").addContent(att);	    		
	    	}
	    		
	    	StringBuffer attSQL = new StringBuffer();
	    	attSQL.append("select t2.paper_attachment_id,t1.* from learn_attachment t1,learn_paper_attachment t2");
	    	attSQL.append(" where t2.valid='Y' and t1.attachment_id=t2.attachment_id and t2.paper_id=?");
	    	attSQL.append(" order by t2.create_date");		    	
	    	
	    	ArrayList<Object> attachParam = new ArrayList<Object>();
	    	attachParam.add(reqElement.getChildText("paper_id"));
	    	
	    	pdao.setSql(attSQL.toString());
	    	pdao.setBindValues(attachParam);
	    	
	    	Element attResult = pdao.executeQuerySql(0, -1);
	    	Element att = new Element("Attachments");
	    	att.addContent(attResult);
	    	result.getChild("Record").addContent(att);
	    	
	    	xmlDocUtil.getResponse().addContent(result);
	    	xmlDocUtil.setResult("0");
	    	
	    }catch (Exception e) {
			e.printStackTrace();
			log4j.logError("[在线学习-获取试卷信息]"+e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
		} finally {
			pdao.releaseConnection();
		}		
	}	
	/**
	 * 接收作业
	 *
	 */
	public void receiveExamPaper(){
		String username   = xmlDocUtil.getSession().getChildTextTrim("username");
		Element reqElement =  xmlDocUtil.getRequestData();
		String my_examination_id = reqElement.getChildText("my_examination_id");
		ArrayList<Object> paramList = new ArrayList<Object>();
		
		PlatformDao pdao = new PlatformDao();
		try {
			StringBuffer updateSQL = new StringBuffer();
			updateSQL.append("update learn_my_examination set status='30',receive_time=now(),");
			updateSQL.append(" modify_by='"+username+"',modify_date=now()");
			updateSQL.append(" where (status='10' or status='20') and my_examination_id = ? ");
			paramList.add(my_examination_id);
			
			pdao.setSql(updateSQL.toString());			
			pdao.setBindValues(paramList);
			pdao.executeTransactionSql();
			
			String[] returnData = { "status"};
			Element resData = XmlDocPkgUtil.createMetaData(returnData);	
			resData.addContent(XmlDocPkgUtil.createRecord(returnData,new String[] {"40"}));			
			
			xmlDocUtil.getResponse().addContent(resData);			
			
			xmlDocUtil.setResult("0");	
			xmlDocUtil.writeHintMsg("20205", "接收作业成功!");			
		}
		catch (Exception e) {			
			log4j.logError("[在线学习-接收作业]"+e.getMessage());
			xmlDocUtil.writeHintMsg("在线学习-接收作业失败!");
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);  			
		} finally {
			pdao.releaseConnection();
		}		
	}
	/**
	 * 重做试卷
	 *
	 */
	public void redoExamPaper(){
		String userName = xmlDocUtil.getSession().getChildTextTrim("username");
		Element reqElement =  xmlDocUtil.getRequestData();
		String my_examination_id = reqElement.getChildText("my_examination_id");
		PlatformDao pdao = new PlatformDao();
		try {
			StringBuffer strSQL = new StringBuffer();
			strSQL.append(" update learn_my_examination set begin_time=now(),end_time=null,status='40',");
			strSQL.append(" modify_by='"+userName+"',modify_date=now()");
			strSQL.append(" where my_examination_id=?");
			strSQL.append(" and exists ( select null from learn_paper_send st");
			strSQL.append(" where st.send_id=learn_my_examination.send_id and show_result='N')");
			ArrayList<Object> paramList = new ArrayList<Object>();
			paramList.add(my_examination_id);
			pdao.setSql(strSQL.toString());			
			pdao.setBindValues(paramList);
			long upCount = pdao.executeTransactionSql();			
			if (upCount==0){
				xmlDocUtil.writeErrorMsg("30101", "已下发答案的作业不能取回重做!");
				return;
			}
			String[] returnData = { "status"};
			Element resData = XmlDocPkgUtil.createMetaData(returnData);	
			resData.addContent(XmlDocPkgUtil.createRecord(returnData,new String[] {"40"}));			
			
			xmlDocUtil.getResponse().addContent(resData);			
			
			xmlDocUtil.setResult("0");	
			xmlDocUtil.writeHintMsg("20205", "取回作业成功!");				
		}
		catch (Exception e) {
			e.printStackTrace();			
			log4j.logError("[在线学习-重做试卷]"+e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);             			
		} finally {
			pdao.releaseConnection();
		}		
	}
	/**
	 * 离开试卷
	 *
	 */
	public void leaveMyExamPaper(){
		String userName = xmlDocUtil.getSession().getChildTextTrim("username");
		Element reqElement =  xmlDocUtil.getRequestData();
		String my_examination_id = reqElement.getChildText("my_examination_id");
		String consuming_time=reqElement.getChildText("consuming_time");
		PlatformDao pdao = new PlatformDao();
		try {
			StringBuffer strSQL = new StringBuffer();
			if(StringUtil.isEmpty(consuming_time)){
				strSQL.append(" update learn_my_examination set end_time=now(),consuming_time = consuming_time+TIMESTAMPDIFF(second,begin_time,now()),");
				strSQL.append(" modify_by='"+userName+"',modify_date=now()");
				strSQL.append(" where my_examination_id=?");
			}else{
				strSQL.append(" update learn_my_examination set end_time=now(),consuming_time =?,");
				strSQL.append(" modify_by=?,modify_date=now()");
				strSQL.append(" where my_examination_id=?");
			}
			ArrayList<Object> paramList = new ArrayList<Object>();
			paramList.add(consuming_time);
			paramList.add(userName);
			paramList.add(my_examination_id);
			
			pdao.setSql(strSQL.toString());			
			pdao.setBindValues(paramList);
			pdao.executeTransactionSql();			
			
			String[] returnData = { "status"};
			Element resData = XmlDocPkgUtil.createMetaData(returnData);	
			resData.addContent(XmlDocPkgUtil.createRecord(returnData,new String[] {"40"}));			
			
			xmlDocUtil.getResponse().addContent(resData);			
			
			xmlDocUtil.setResult("0");	
			xmlDocUtil.writeHintMsg("20205", "取回作业成功!");				
		}
		catch (Exception e) {
			e.printStackTrace();			
			log4j.logError("[在线学习-离开试卷]"+e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);             			
		} finally {
			pdao.releaseConnection();
		}		
	}	
	/**
	 * 开始答题，返回试卷ID
	 *
	 */
	public void startExamPaper(){
		String userName = xmlDocUtil.getSession().getChildTextTrim("username");
		Element reqElement =  xmlDocUtil.getRequestData();
		Element sessionEle = xmlDocUtil.getSession();
		
		String my_examination_id = reqElement.getChildText("my_examination_id");
		
		PlatformDao pdao = new PlatformDao();
		try {
			ArrayList<Object> paramList = new ArrayList<Object>();
			StringBuffer strSQL = new StringBuffer();
			strSQL.append(" select t2.paper_id,t2.resource_type,t2.choose_type,t3.complete_time,t1.status,t1.random_paper_id");
			strSQL.append(" from learn_my_examination t1,learn_examination_paper t2,learn_paper_send t3");
			strSQL.append(" where t1.paper_id=t2.paper_id and t1.send_id=t3.send_id and t1.my_examination_id= ? ");
			paramList.add(my_examination_id);
			
			pdao.setSql(strSQL.toString());
			pdao.setBindValues(paramList);
			
			Element result = pdao.executeQuerySql(0, -1);
			List list = result.getChildren("Record");
			if (list.size()==0){
				xmlDocUtil.writeErrorMsg("30205","没有找到ID为["+my_examination_id+"]的作业记录!");
				xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE); 
				return;
			}
			else{
				Element rec = (Element)list.get(0);
				String paper_id = rec.getChildText("paper_id");				
				String examStatus = rec.getChildText("status");	
				if (Integer.parseInt(examStatus)<=40){
				
					String complete_time = rec.getChildText("complete_time");
					if (StringUtil.isNotEmpty(complete_time)){
						Date now = new Date();
						DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						Date completeTime = format.parse(complete_time);
						if (now.after(completeTime)){
							xmlDocUtil.writeErrorMsg("30206","作业已经过期!");
							xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE); 
							return;						
						}
					}
					
					ArrayList<Object> updateParam = new ArrayList<Object>();
					StringBuffer updateSQL = new StringBuffer();
					updateSQL.append("update learn_my_examination set status='40',begin_time=now(),");
					updateSQL.append(" modify_by='"+userName+"',modify_date=now()");
					
					//如果是答题时随机组卷的作业
					if (("10".equals(rec.getChildText("resource_type")) || "40".equals(rec.getChildText("resource_type").substring(0, 2))) && "30".equals(rec.getChildText("choose_type"))){
						String radomPaperid = rec.getChildText("random_paper_id");
						if (StringUtil.isEmpty(radomPaperid)){
							ExamPaper ep = new ExamPaper();
							paper_id  = ep.executeAutomaticGeneratingPaper(paper_id, sessionEle);
							updateSQL.append(",random_paper_id=?");
							updateParam.add(paper_id);
						}
					}
					updateSQL.append(" where status<='30' and my_examination_id = ? ");
					updateParam.add(my_examination_id);
					
					if (Integer.parseInt(examStatus)<=30){
						pdao.setSql(updateSQL.toString());
						pdao.setBindValues(updateParam);
						pdao.executeTransactionSql();
				    }
	
					xmlDocUtil.setResult("0");
					xmlDocUtil.writeHintMsg("20205", "启动作业成功!");		
			    }
				else{
					xmlDocUtil.writeErrorMsg("30207","作业已经提交!");
					xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE); 
				}		    					
			}			
		}
		catch (Exception e) {
			e.printStackTrace();			
			log4j.logError("[在线学习-开始答题]"+e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);             			
		} finally {
			pdao.releaseConnection();
		}
	}
	/**
	 * 提交作业结果
	 *
	 */
	public void submitExamResult(){
		String userName = xmlDocUtil.getSession().getChildTextTrim("username");
		Element reqElement =  xmlDocUtil.getRequestData();
		
		String my_examination_id = reqElement.getChildText("my_examination_id");
		String paper_id = reqElement.getChildText("paper_id");
		this.saveExamResult();
		
		PlatformDao pdao = new PlatformDao();
		try {
			ArrayList<Object> paramList = new ArrayList<Object>();
			StringBuffer updateSQL = new StringBuffer();
			updateSQL.append("update learn_my_examination set status='50',end_time=now(),consuming_time = ifnull(consuming_time,0)+TIMESTAMPDIFF(second,begin_time,now()),");
			updateSQL.append(" modify_by='"+userName+"',modify_date=now()");
			updateSQL.append(" where my_examination_id = ? ");
			paramList.add(my_examination_id);
			
			pdao.setSql(updateSQL.toString());
			pdao.setBindValues(paramList);
			pdao.executeTransactionSql();
			
			xmlDocUtil.setResult("0");	
	    	xmlDocUtil.writeHintMsg("00103", "提交作业成功!");
		}
		catch (Exception e) {
			pdao.rollBack();
			e.printStackTrace();			
			log4j.logError("[在线学习-提交作业]"+e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE); 
            			
		} finally {
			pdao.releaseConnection();
		}
		
	}
	/**
	 * 保存作业结果
	 *
	 */
	public void saveExamResult(){
		String userName = xmlDocUtil.getSession().getChildText("username");
		String userid = xmlDocUtil.getSession().getChildText("userid");
		
		Element reqElement =  xmlDocUtil.getRequestData();
		String paper_id = reqElement.getChildText("paper_id");
		String classid = reqElement.getChildText("classid");
		String my_examination_id = reqElement.getChildText("my_examination_id");
		String consumingTime = reqElement.getChildText("consuming_time");
		List praxesResult = reqElement.getChildren("praxes_result");
		List resultIDList = reqElement.getChildren("result_id");
		List draftList = reqElement.getChildren("draft_attachment");
		List paperPraxeId = reqElement.getChildren("paper_praxes_id");
		
		
		String[] returnData = {"paper_id","paper_praxes_id","result_id"};
		Element resData = XmlDocPkgUtil.createMetaData(returnData);			
		PlatformDao pdao = new PlatformDao();
		try {
			if (StringUtil.isEmpty(classid)){
				StringBuffer sqlBuf = new StringBuffer();
				sqlBuf.append("select classid from base_studentinfo t1 where t1.state='1' and t1.userid=?");
				ArrayList<Object> bvals = new ArrayList<Object>();
				bvals.add(userid);
				Element stuiInfoRec = DaoUtil.getOneRecord(sqlBuf.toString(), bvals);
				classid = stuiInfoRec.getChildText("classid");
			}
			
			pdao.beginTransaction();
			int ruleIdCount = resultIDList.size();
			
			for (int i=0;i<praxesResult.size();i++){
				String paper_praxes_id = ((Element)paperPraxeId.get(i)).getText();
				String resultID = null;
				if (ruleIdCount>i){
					resultID = ((Element)resultIDList.get(i)).getText();
				}
				
				Element examResultRec = ConfigDocument.createRecordElement("yuexue", "learn_examination_result");
				XmlDocPkgUtil.copyValues(reqElement, examResultRec, i, true);
				XmlDocPkgUtil.setChildText(examResultRec, "paper_id", paper_id);
				XmlDocPkgUtil.setChildText(examResultRec, "classid", classid);
				XmlDocPkgUtil.setChildText(examResultRec, "my_examination_id", my_examination_id);
				
				/** 2.0 开始每个学生 每道题目一张草稿图,不再写入互动交流区 begin */
				String drafPath=null;
				if (draftList.size()>i){
				   Element draftRec = (Element)draftList.get(i);
				   drafPath = AttachmentUtil.moveFile(draftRec, "draf");
				   XmlDocPkgUtil.setChildText(examResultRec, "student_draf_path", drafPath);
				}
				/** 2.0 开始每个学生 每道题目一张草稿图,不再写入互动交流区 end */
				
				if 	(StringUtil.isEmpty(resultID)){
                	//避免因网络原因，造成重复结果记录,先update,没有记录再insert
					ArrayList<Object> updateParam = new ArrayList<Object>();
					StringBuffer updateSQL = new StringBuffer();    				
    				updateSQL.append("update learn_examination_result set ");
    				if (StringUtil.isNotEmpty(drafPath)){
    					updateSQL.append(" student_draf_path=?,");
    					updateParam.add(drafPath);
    				}
    				updateSQL.append(" classid=?,praxes_result=?,create_by=?,");    				
    				updateSQL.append(" consuming_time='"+consumingTime+"',create_date=now()");
    				updateSQL.append(" where my_examination_id=? and paper_id=? and paper_praxes_id=?");
    				
    				updateParam.add(classid);
    				updateParam.add(((Element)praxesResult.get(i)).getText());
    				updateParam.add(userName);
    				updateParam.add(my_examination_id);
    				updateParam.add(paper_id);
    				updateParam.add(paper_praxes_id);
    				
    				pdao.setSql(updateSQL.toString());
    				pdao.setBindValues(updateParam);
    				long updCount = pdao.executeTransactionSql();
    				
    				if (updCount==0){
                	   examResultRec.removeChild("result_id");                	
                	   String addResultId = pdao.insertOneRecordSeqPk(examResultRec).toString();                	
                	   resData.addContent(XmlDocPkgUtil.createRecord(returnData,new String[] {paper_id,paper_praxes_id,addResultId}));
    				}
                }
                else{
					XmlDocPkgUtil.setChildText(examResultRec, "modify_by", userName);
					XmlDocPkgUtil.setChildText(examResultRec, "modify_date", DatetimeUtil.getNow(""));                	
                	pdao.updateOneRecord(examResultRec); 
                	resData.addContent(XmlDocPkgUtil.createRecord(returnData,new String[] {paper_id,paper_praxes_id,resultID}));
                }                
			}
			/** 2.0 开始每个学生 每道题目一张草稿图,不再写入互动交流区
			if (draftList.size()>0){
				//如果有附件,同步写入习后感交流区
				StringBuffer strSQL = new StringBuffer();
				strSQL.append("select t1.send_id,t1.paper_id,t2.classid,t3.paper_name as comment");
				strSQL.append(" from learn_my_examination t1,base_studentinfo t2,learn_examination_paper t3");
				strSQL.append(" where t1.userid=t2.userid and t1.paper_id=t3.paper_id");
				strSQL.append(" and t1.my_examination_id=?");
				
				ArrayList<Object> paramList = new ArrayList<Object>();
				paramList.add(my_examination_id);
				
				pdao.setSql(strSQL.toString());
				pdao.setBindValues(paramList);				
				
				Element result = pdao.executeQuerySql(-1, 0);
				Element examComment = result.getChild("Record");
				
				Element commentRec = ConfigDocument.createRecordElement("yuexue", "learn_comment");
				XmlDocPkgUtil.copyValues(examComment, commentRec, 0, true);				
				
				String commentid = pdao.insertOneRecordSeqPk(commentRec).toString();
				
				//保存作业草稿
				for (int i=0;i<draftList.size();i++){
					Element draftRec = (Element)draftList.get(i);
					if (draftRec!=null){//写入作业附件				
						String attachment_id = AttachmentUtil.moveFile(pdao, draftRec, "draft");					
						if (StringUtil.isNotEmpty(my_examination_id)){
							Element paperDraftRec = ConfigDocument.createRecordElement("yuexue","learn_paper_draft");
							XmlDocPkgUtil.copyValues(reqElement, paperDraftRec, 0 , true);
							XmlDocPkgUtil.setChildText(paperDraftRec, "attachment_id", attachment_id);
							
							pdao.insertOneRecordSeqPk(paperDraftRec);
						}
						//写入习后感交流内容附件
						Element commAttachRec = ConfigDocument.createRecordElement("yuexue","learn_comment_attachment");
						XmlDocPkgUtil.copyValues(examComment, commAttachRec, 0, true);
						XmlDocPkgUtil.setChildText(commAttachRec, "attachment_id", attachment_id);
						XmlDocPkgUtil.setChildText(commAttachRec, "comment_id", commentid);				
						
						pdao.insertOneRecordSeqPk(commAttachRec);
					}
				}
			}
			*/
			pdao.commitTransaction();	
			
			xmlDocUtil.getResponse().addContent(resData);
	    	xmlDocUtil.setResult("0");	
	    	xmlDocUtil.writeHintMsg("00103", "保存作业!");
		}
		catch (Exception e) {
			pdao.rollBack();
			e.printStackTrace();
			log4j.logError("[在线学习-保存作业]"+e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);            			
		} finally {
			pdao.releaseConnection();
		}		
	}
	/**
	 * 获取待完成作业
	 *
	 */
	public void getMyExamPaper(){
		Element reqElement =  xmlDocUtil.getRequestData();
		String deptid = xmlDocUtil.getSession().getChildText("deptid");
		
		StringBuffer strSQL = new StringBuffer();
		ArrayList<Object> paramList = new ArrayList<Object>();
		
		strSQL.append("select DISTINCT t1.my_examination_id,t1.send_id,t1.arrived_time,t1.status,t2.paper_id,t2.resource_type,t8.attachment_id,t5.folder_code,t2.choose_type,t2.promoted_quantity,t9.file_path,t9.access_path,");
		strSQL.append(" t2.paper_name,t2.cover_path,t2.limit_times,t6.gradename, t4.classid,t4.userid,t2.remark,t1.create_by,t1.create_date,t2.folder_id,t2.grade_code, t2.subject_id,t5.folder_name,t3.coursenm,");
		strSQL.append(" (SELECT count(*) FROM sell_read_log tt1 WHERE t2.paper_id = tt1.paper_info_id) AS readcount");
		strSQL.append(" from learn_my_examination t1 left join  learn_paper_attachment t8 on t1.paper_id = t8.paper_id and t8.valid='Y' LEFT JOIN learn_attachment t9 ON t8.attachment_id = t9.attachment_id, ");
		strSQL.append(" learn_examination_paper t2,base_course t3,base_studentinfo t4,base_book_folder t5,base_grade t6 ");
		strSQL.append(" where t1.paper_id = t2.paper_id and t2.subject_id = t3.subjectid and t2.folder_id=t5.folder_id");
		strSQL.append(" and t2.grade_code = t3.gradecode and t3.gradecode = t6.gradecode and t1.valid = 'Y' and t2.valid = 'Y'");
		strSQL.append(" and t1.userid=t4.userid and t4.state>'0'");
		//strSQL.append(" and t2.resource_type != '30' ");
		strSQL.append(" and t3.deptid=?");
		paramList.add(deptid);
		
		String qry_userid = reqElement.getChildText("qry_userid");
		if (StringUtil.isEmpty(qry_userid)){
			qry_userid="-1";
		}
		strSQL.append(" and t1.userid=?");
		paramList.add(qry_userid);
		
		String qry_courseid = reqElement.getChildText("qry_courseid");
		if (StringUtil.isNotEmpty(qry_courseid)){
		    strSQL.append(" and t3.courseid=?");
		    paramList.add(qry_courseid);
		}
		
		String qry_folder_id=reqElement.getChildText("qry_folder_id");
		if(StringUtil.isNotEmpty(qry_folder_id)){
			strSQL.append(" and t2.folder_id=?");
			paramList.add(qry_folder_id);
		}
		
		String qry_status = reqElement.getChildText("qry_status");
		if (StringUtil.isNotEmpty(qry_status)){
			if ("10".equals(qry_status)){
				strSQL.append(" and t1.status<='40' ");
			}else if( "40".equals(qry_status) || "70".equals(qry_status)){
				strSQL.append(" and t1.status = ? ");
				paramList.add(qry_status);
			}
			else if  ("30".equals(qry_status)){//已接收
			    strSQL.append(" and t1.status>?");
			    paramList.add(qry_status);
			}
			else if  ("50".equals(qry_status)){//已提交
		       strSQL.append(" and t1.status >= '50' ");
			}		    
		}
		String qry_paper_name = reqElement.getChildText("qry_paper_name");
		if (StringUtil.isNotEmpty(qry_paper_name)){
		    strSQL.append(" and t2.paper_name like ?");
		    paramList.add(qry_paper_name+"%");
		}
		String qry_paperid = reqElement.getChildText("qry_paperid");
		if (StringUtil.isNotEmpty(qry_paperid)){
		    strSQL.append(" and t2.paper_id = ?");
		    paramList.add(qry_paperid);
		}
		String qry_resource_type = reqElement.getChildText("qry_resource_type");
		if (StringUtil.isNotEmpty(qry_resource_type)){
		    strSQL.append(" and t2.resource_type like ?");
		    paramList.add(qry_resource_type+"%");
		}
		String qry_subject_id = reqElement.getChildText("qry_subject_id");
		if (StringUtil.isNotEmpty(qry_subject_id)){
		    strSQL.append(" and t2.subject_id = ?");
		    paramList.add(qry_subject_id);
		}
		String qry_grade_code = reqElement.getChildText("qry_grade_code");
		if (StringUtil.isNotEmpty(qry_grade_code)){
		    strSQL.append(" and t2.grade_code = ?");
		    paramList.add(qry_grade_code);
		}		
		/*String qry_folder_id = reqElement.getChildText("qry_folder_id");
		if (StringUtil.isNotEmpty(qry_folder_id)){
		    strSQL.append(" and t5.folder_code like ?");
		    paramList.add("%"+qry_folder_id+"%");
		}*/
		String qry_begin_date = reqElement.getChildText("qry_begin_date");
		if (StringUtil.isNotEmpty(qry_begin_date)){
		    strSQL.append(" and t1.create_date >= ?");
		    paramList.add(SqlTypes.getConvertor("Timestamp").convert(qry_begin_date, "yyyy-MM-dd"));
		}		
		String qry_end_date = reqElement.getChildText("qry_end_date");
		if (StringUtil.isNotEmpty(qry_end_date)){
		    strSQL.append(" and t1.create_date <= ?");
		    paramList.add(SqlTypes.getConvertor("Timestamp").convert(qry_end_date, "yyyy-MM-dd"));
		}
		String qry_condition = reqElement.getChildText("qry_condition");
		if (StringUtil.isNotEmpty(qry_condition)){
		    strSQL.append(" and (t2.paper_name like ? or t2.create_by like ?)");
		    paramList.add("%"+qry_condition+"%");
		    paramList.add("%"+qry_condition+"%");
		}
		if (StringUtil.isNotEmpty(qry_begin_date)){
			strSQL.append(" order by t1.create_date");
		}
		else{
			strSQL.append(" order by t1.create_date desc");
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
			log4j.logError("[在线学习-取我的试卷(课件)]"+e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
		} finally {
			pdao.releaseConnection();
		}		
	}
	
	public void getMyExamError(){
		String optionStr = "A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z";
		String[] optionArray = optionStr.split(",");	
		Element reqElement =  xmlDocUtil.getRequestData();
		String qry_userid = xmlDocUtil.getSession().getChildText("userid");
		if (StringUtil.isEmpty(qry_userid)) {
			xmlDocUtil.setResult("0");
			return ;
		}
		StringBuffer strSQL = new StringBuffer();
		ArrayList<Object> paramList = new ArrayList<Object>();
		strSQL.append(" SELECT tt3.paper_praxes_id,tt3.paper_id,tt3.praxes_id,tt3.praxes_type,tt3.praxes_content,tt3.score AS praxes_score,tt3.difficulty_level,tt3.discrimination,	tt3.praxes_hint,tt3.right_result,tt4.is_right,format(tt4.score, 1) AS score,tt4.correct_result,tt1. STATUS,tt2.paper_name,tt4.result_id," +
				" tt4.student_draf_path,tt4.teacher_draf_path,tt4.praxes_result,tt4.consuming_time,getParamDesc ('c_praxes_type',tt3.praxes_type) AS praxes_type_desc" +
				" FROM	learn_my_examination tt1,	learn_examination_paper tt2,	learn_paper_praxes tt3, learn_examination_result tt4 " +
				" WHERE	tt1.paper_id = tt2.paper_id and tt1.my_examination_id = tt4.my_examination_id and tt3.paper_praxes_id = tt4.paper_praxes_id AND tt1.userid = ? AND tt4.userid = ? ");
		paramList.add(qry_userid);
		paramList.add(qry_userid);
		String qry_subject_id = reqElement.getChildText("qry_subject_id");
		if (StringUtil.isNotEmpty(qry_subject_id)){
			strSQL.append(" AND tt2.subject_id = ?");
		    paramList.add(qry_subject_id);
		}
		
		strSQL.append(" AND tt2.resource_type LIKE '40%'");
		strSQL.append(" AND tt1.valid = 'Y' AND tt2.valid = 'Y' AND tt3.valid = 'Y' AND tt4.valid = 'Y' AND tt4.score < tt3.score AND tt1. STATUS > '40'");
		strSQL.append(" ORDER BY tt4.modify_date desc");
	    PlatformDao pdao = new PlatformDao();
	    try{
	    	pdao.setSql(strSQL.toString());
	    	pdao.setBindValues(paramList);
	    	Element result = pdao.executeQuerySql(xmlDocUtil.getPageSize(), xmlDocUtil.getPageNo());
	    	//查询对应题目的选项
	    	StringBuffer optionSQL = new StringBuffer();
	    	optionSQL.append("select t1.paper_option_id,t1.paper_praxes_id,ifnull(t1.praxes_id,t1.paper_praxes_id) as praxes_id,ifnull(t1.option_id,t1.paper_option_id) as option_id,");
			//为支持在试卷中进阶,将答案一直显示，让pad程序进行业务逻辑判断
	    	//if ("70".equals(myExamStatus) || "60".equals(myExamStatus) || "50".equals(myExamStatus) || "2".equals(userType)){
				optionSQL.append("t1.is_right,");
			//}	    	
	    	optionSQL.append(" t1.option_content");
	    	optionSQL.append(" from learn_paper_options t1 ");
	    	optionSQL.append(" where t1.valid='Y' and t1.paper_praxes_id=? ");
	    	optionSQL.append(" order by display_order");
	    	List list = result.getChildren("Record");
	    	for (int i = 0; i < list.size() ;i++){
	    		Element praxesRec = (Element)list.get(i);
	    		String praxes_type = praxesRec.getChildText("praxes_type");
	    		String praxes_result = praxesRec.getChildText("praxes_result");
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
			    		XmlDocPkgUtil.setChildText(optionRec, "praxes_result",praxes_result);
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
			log4j.logError("[在线学习-取我的试卷(课件)]"+e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
		} finally {
			pdao.releaseConnection();
		}		
	
	}
	
	/**
	 * 获取试卷信息带
	 */
	public void getMyExamPaperInfo(){
		Element reqElement =  xmlDocUtil.getRequestData();
		String my_examination_id = reqElement.getChildText("my_examination_id");
		ArrayList<Object> paramList = new ArrayList<Object>();
		
		PlatformDao pdao = new PlatformDao();
		try {
			StringBuffer updateSQL = new StringBuffer();
			updateSQL.append("SELECT t1.*,t2.limit_times FROM learn_my_examination t1 " +
					"INNER JOIN learn_examination_paper t2 ON t1.paper_id = t2.paper_id" +
					" WHERE my_examination_id=?");
			paramList.add(my_examination_id);
			pdao.setSql(updateSQL.toString());
	    	pdao.setBindValues(paramList);
	    	Element result = pdao.executeQuerySql(-1,0);
	    	xmlDocUtil.getResponse().addContent(result);
	    	xmlDocUtil.setResult("0");
		}
		catch (Exception e) {			
			log4j.logError("[在线学习-试卷信息(时间)]"+e.getMessage());
			xmlDocUtil.writeHintMsg("在线学习-接受信息失败!");
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
		} finally {
			pdao.releaseConnection();
		}		
	}
	/**
	 * 获取所有的课件
	 *
	 */
	public void getAllExamPaper(){
		Element reqElement =  xmlDocUtil.getRequestData();
		
		StringBuffer strSQL = new StringBuffer();
		ArrayList<Object> paramList = new ArrayList<Object>();
		
		strSQL.append("select DISTINCT t2.paper_id,t2.resource_type,t2.create_date,t2.create_by,t2.choose_type,t2.promoted_quantity,t2.paper_id as send_id,t6.attachment_id,");
		strSQL.append(" t2.paper_name,t2.cover_path,t2.limit_times,t2.remark,t2.folder_id,t5.folder_name,t3.coursenm,t4.gradeName grade_name,t4.gradeName,t5.folder_code,t2.subject_id,t2.grade_code");
		strSQL.append(" from learn_examination_paper t2,base_course t3,base_grade t4,base_book_folder t5,learn_paper_attachment t6 ");
		strSQL.append(" where t2.subject_id = t3.subjectid and t2.folder_id=t5.folder_id  and t2.paper_id=t6.paper_id");
		strSQL.append(" and t2.grade_code = t3.gradecode and t3.gradecode=t4.gradecode and t2.valid = 'Y' and t2.resource_type like '20%'");
		String qry_courseid = reqElement.getChildText("qry_courseid");
		if (StringUtil.isNotEmpty(qry_courseid)){
		    strSQL.append(" and t3.courseid=?");
		    paramList.add(qry_courseid);
		}
		
		String qry_folder_id=reqElement.getChildText("qry_folder_id");
		if(StringUtil.isNotEmpty(qry_folder_id)){
			strSQL.append(" and t2.folder_id=?");
			paramList.add(qry_folder_id);
		}
		
		String qry_paper_name = reqElement.getChildText("qry_paper_name");
		if (StringUtil.isNotEmpty(qry_paper_name)){
		    strSQL.append(" and t2.paper_name like ?");
		    paramList.add("%"+qry_paper_name+"%");
		}
		String qry_resource_type = reqElement.getChildText("qry_resource_type");
		if (StringUtil.isNotEmpty(qry_resource_type)){
		    strSQL.append(" and t2.resource_type like ?");
		    paramList.add("%"+qry_resource_type+"%");
		}
/*		String qry_subject_id = reqElement.getChildText("qry_subject_id");
		if (StringUtil.isNotEmpty(qry_subject_id)){
		    strSQL.append(" and t2.subject_id like ?");
		    paramList.add(qry_subject_id+"%");
		}*/
		String qry_subject_id = reqElement.getChildText("qry_subject_id");
		if (StringUtil.isNotEmpty(qry_subject_id)){
			strSQL.append(" and t2.subject_id = ?");
			paramList.add(qry_subject_id);
		}
		
		String qry_grade_code = reqElement.getChildText("qry_grade_code");
		if (StringUtil.isNotEmpty(qry_grade_code)){
		    strSQL.append(" and t4.gradename like ? ");
		    paramList.add(qry_grade_code+"%");
		}	
		
		String qry_author = reqElement.getChildText("qry_author");
		if(StringUtil.isNotEmpty(qry_author)){
			strSQL.append(" and t2.create_by like ?");
			paramList.add("%"+qry_author+"%");
		}
		
		String qry_begin_date = reqElement.getChildText("qry_begin_date");
		if (StringUtil.isNotEmpty(qry_begin_date)){
		    strSQL.append(" and t2.create_date >= ?");
		    paramList.add(SqlTypes.getConvertor("Timestamp").convert(qry_begin_date, "yyyy-MM-dd"));
		}		
		String qry_end_date = reqElement.getChildText("qry_end_date");
		if (StringUtil.isNotEmpty(qry_end_date)){
		    strSQL.append(" and t2.create_date <= ?");
		    paramList.add(SqlTypes.getConvertor("Timestamp").convert(qry_end_date, "yyyy-MM-dd"));
		}
		
		if (StringUtil.isNotEmpty(qry_begin_date)){
			strSQL.append(" order by t2.create_date");
		}
		else{
			strSQL.append(" order by t2.create_date desc");
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
			log4j.logError("[在线学习-取所有的试卷(课件)]"+e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
		} finally {
			pdao.releaseConnection();
		}		
	}
	/**
	 * 获取我的课件
	 *
	 */
	public void getMyPaperCourse(){
		Element reqElement =  xmlDocUtil.getRequestData();	
		String deptid = xmlDocUtil.getSession().getChildText("deptid");
		StringBuffer strSQL = new StringBuffer();
		ArrayList<Object> paramList = new ArrayList<Object>();
		
		strSQL.append("select * from (");
		strSQL.append(" select DISTINCT t1.paper_id,t1.resource_type,t1.choose_type,t1.promoted_quantity, t1.paper_name,t1.cover_path,");
		strSQL.append(" t1.limit_times,t6.gradename,t2.classid,t2.userid,t1.remark,t1.folder_id,t5.folder_name, t4.coursenm, ");
		strSQL.append(" '' AS my_examination_id, t1.paper_id  AS send_id,'' AS arrived_time, '' AS status,t1.create_by,t1.create_date,t5.folder_code,t1.subject_id,t1.grade_code,t8.attachment_id ");
		strSQL.append("  from learn_examination_paper t1,base_studentinfo t2, base_teacher_subject t3,base_course t4,base_book_folder t5, base_grade t6,base_class t7,learn_paper_attachment t8 ");
		strSQL.append(" where t1.user_id=t3.userid and t3.classid=t2.classid and t1.subject_id=t4.subjectid and t1.grade_code = t4.gradecode");
		strSQL.append(" and t1.folder_id = t5.folder_id and t1.paper_id=t8.paper_id and t4.gradecode = t6.gradecode and t1.valid='Y' and t2.state >'0' ");
		strSQL.append(" and t1.grade_code=t7.gradecode and t2.classid=t7.classid and t3.state >'0' ");
		strSQL.append("  and t1.resource_type like '20%' ");
		String qry_userid = reqElement.getChildText("qry_userid");
		if (StringUtil.isEmpty(qry_userid)){
			qry_userid="-1";
		}
		strSQL.append(" and t2.userid=?");
		paramList.add(qry_userid);

		String qry_subject_id = reqElement.getChildText("qry_subject_id");
		if (StringUtil.isNotEmpty(qry_subject_id)){
		    strSQL.append(" and t1.subject_id = ?");
		    paramList.add(qry_subject_id);
		}	
		String qry_folder_id=reqElement.getChildText("qry_folder_id");
		if(StringUtil.isNotEmpty(qry_folder_id)){
			 strSQL.append(" and t1.folder_id = ?");
			    paramList.add(qry_folder_id);
		}
		String qry_resource_type= reqElement.getChildText("qry_resource_type");
		if(StringUtil.isEmpty(qry_resource_type)){
			strSQL.append(" UNION ALL ");
			strSQL.append(" select DISTINCT t1.paper_id,t1.resource_type,t1.choose_type,t1.promoted_quantity,t1.paper_name,t1.cover_path,t1.limit_times,t6.gradename,t4.classid,t4.userid,");
			strSQL.append(" t1.remark,t1.folder_id,t5.folder_name,t3.coursenm,t2.my_examination_id,t2.send_id,t2.arrived_time,t2.status,t2.create_by, t2.create_date,t5.folder_code,t1.subject_id,t1.grade_code,'' as attachment_id");
		    strSQL.append(" from learn_my_examination t2,learn_examination_paper t1, base_course t3,base_studentinfo t4, base_book_folder t5,base_grade t6 ");
		    strSQL.append(" where t2.paper_id = t1.paper_id and t1.subject_id = t3.subjectid and t1.folder_id = t5.folder_id and t1.grade_code = t3.gradecode and t3.gradecode = t6.gradecode and t2.valid = 'Y' and t1.valid = 'Y' and t2.userid = t4.userid and t4.state > '0' ");
		    strSQL.append(" and t3.deptid=?");
			paramList.add(deptid);
			strSQL.append("  and t2.userid = ? ");
			paramList.add(qry_userid);
			strSQL.append("  and t1.resource_type like '40%' ");
			if(StringUtil.isNotEmpty(qry_folder_id)){
				 strSQL.append(" and t1.folder_id = ?");
				 paramList.add(qry_folder_id);
			}
			if (StringUtil.isNotEmpty(qry_subject_id)){
			    strSQL.append(" and t1.subject_id = ?");
			    paramList.add(qry_subject_id);
			}	
		}
		strSQL.append(" ) paper");
		strSQL.append(" order by create_date desc");
		
	    		
		
	    PlatformDao pdao = new PlatformDao();
	    try{
	    	pdao.setSql(strSQL.toString());
	    	pdao.setBindValues(paramList);
	    	Element result = pdao.executeQuerySql(xmlDocUtil.getPageSize(), xmlDocUtil.getPageNo());
	    	
	    	xmlDocUtil.getResponse().addContent(result);
	    	xmlDocUtil.setResult("0");
	    	
	    }catch (Exception e) {
			e.printStackTrace();
			log4j.logError("[在线学习-取我的课件]"+e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
		} finally {
			pdao.releaseConnection();
		}		
	}
	/**
	 * 获取试卷题目(作业完成后会返回答案信息)
	 *
	 */	
	public void getPaperPraxes(){
		Element reqElement =  xmlDocUtil.getRequestData();
		String paper_id = reqElement.getChildText("paper_id");
		String my_examination_id = reqElement.getChildText("my_examination_id");
		String usertype = xmlDocUtil.getSession().getChildText("usertype");
		
		Element result = getPaperPraxesInfo(paper_id,my_examination_id,null,usertype);
		if (result!=null){
	    	xmlDocUtil.getResponse().addContent(result);
	    	xmlDocUtil.setResult("0");			
		}
		else{
			xmlDocUtil.writeErrorMsg("20206","取作业结果发生错误!");
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE); 
		}
		
	}
    /**
     * 取试卷题目信息(含结果)
     * @param paper_id
     * @param my_examination_id
     * @param paper_praxes_id
     * @param userType
     * @return
     */
	public Element getPaperPraxesInfo(String paper_id,String my_examination_id,String paper_praxes_id,String userType){
		String optionStr = "A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z";
		String[] optionArray = optionStr.split(",");	
		String qry_user_id="";
		String consuming_time="0";
		StringBuffer paperSQL = new StringBuffer();
		paperSQL.append("select t1.consuming_time,t2.paper_id,t2.resource_type,t2.choose_type,t1.status,t1.random_paper_id,t1.userid");
		paperSQL.append(" from learn_my_examination t1,learn_examination_paper t2");
		paperSQL.append(" where t1.paper_id=t2.paper_id and t1.my_examination_id=?");
		
		ArrayList<Object> paperParam = new ArrayList<Object>();	
		paperParam.add(my_examination_id);		
		
		PlatformDao pdao = new PlatformDao();
	    try{
	    	String myExamStatus="";
	    	if (StringUtil.isNotEmpty(my_examination_id)){
		    	pdao.setSql(paperSQL.toString());
		    	pdao.setBindValues(paperParam);
		    	Element paperResult = pdao.executeQuerySql(0,-1);
		    	List list = paperResult.getChildren("Record");
		    	if (list.size()==0){
					return null;		    		
		    	}
		    	else{
			    	Element paperRec = (Element)list.get(0);
			    	//String resourceType = paperRec.getChildText("resource_type");
			    	//if ("10".equals(resourceType) || "40".equals(resourceType)){
			    		myExamStatus = paperRec.getChildText("status");
			    		qry_user_id = paperRec.getChildText("userid");
			    		consuming_time = paperRec.getChildText("consuming_time");
			    		consuming_time=StringUtil.isEmpty(consuming_time)?"0":consuming_time;
			    		if ("30".equals(paperRec.getChildText("choose_type"))){
			    			paper_id = paperRec.getChildText("random_paper_id");
			    		}
			    		
			    	//}
		    	}
	    	}
	    	
			StringBuffer strSQL = new StringBuffer();
			strSQL.append("select t1.paper_praxes_id,t1.paper_id,t1.praxes_id,t1.praxes_type,t1.praxes_content,t1.score as praxes_score,t1.difficulty_level,t1.discrimination,t1.praxes_hint,");
//			//为支持在试卷中进阶,将答案一直显示，让pad程序进行业务逻辑判断
			//if ("70".equals(myExamStatus) || "60".equals(myExamStatus) || "50".equals(myExamStatus) || "2".equals(userType)){
				strSQL.append("t1.right_result,t2.is_right,t2.score,t2.correct_result,");
			//}
			if ("1".equals(userType)){
				strSQL.append("t3.show_result,");				
			}
			else{
				strSQL.append("'Y' as show_result,");
			}
			strSQL.append("(select avg(consuming_time) from learn_examination_result where paper_id = t1.paper_id and classid = t5.classid) as avg_consuming,");
			strSQL.append(" t4.status, t6.paper_name,t2.result_id,t2.student_draf_path,t2.teacher_draf_path,t2.praxes_result,t2.consuming_time,getParamDesc('c_praxes_type',t1.praxes_type) as praxes_type_desc");
			strSQL.append(" from learn_paper_praxes t1 left join learn_examination_result t2");
			strSQL.append(" on t1.paper_id=t2.paper_id and t1.paper_praxes_id=t2.paper_praxes_id and t2.userid=? and t2.my_examination_id=?");
			strSQL.append(" ,learn_paper_send t3,learn_my_examination t4,base_studentinfo t5,learn_examination_paper t6");
			strSQL.append(" where t1.valid='Y' and t1.paper_id = ? and t1.paper_id=t3.paper_id and t3.send_id=t4.send_id  and t1.paper_id=t6.paper_id");	
			strSQL.append(" and t4.my_examination_id=? and t5.state='1' and t5.userid=?");
			
			ArrayList<Object> paramList = new ArrayList<Object>();
			paramList.add(qry_user_id);
			paramList.add(my_examination_id);	
			paramList.add(paper_id);
			paramList.add(my_examination_id);
			paramList.add(qry_user_id);
			
			if (StringUtil.isNotEmpty(paper_praxes_id)){
				strSQL.append(" and t1.paper_praxes_id=?");
				paramList.add(paper_praxes_id);
			}
					
		
			strSQL.append(" order by t1.display_order,t1.paper_praxes_id");
			
	    	pdao.setSql(strSQL.toString());
	    	pdao.setBindValues(paramList);
	    	Element result = pdao.executeQuerySql(0,-1);
	    	//Element consuming=XmlDocPkgUtil.createFieldEle("consuming_time");
	    	//XmlDocPkgUtil.setChildText(consuming, "consuming_time",consuming_time);
	    	//result.addContent(consuming);
	    	StringBuffer optionSQL = new StringBuffer();
	    	optionSQL.append("select t1.paper_option_id,t1.paper_praxes_id,ifnull(t1.praxes_id,t1.paper_praxes_id) as praxes_id,ifnull(t1.option_id,t1.paper_option_id) as option_id,");
			//为支持在试卷中进阶,将答案一直显示，让pad程序进行业务逻辑判断
	    	//if ("70".equals(myExamStatus) || "60".equals(myExamStatus) || "50".equals(myExamStatus) || "2".equals(userType)){
				optionSQL.append("t1.is_right,");
			//}	    	
	    	optionSQL.append(" t1.option_content");
	    	optionSQL.append(" from learn_paper_options t1 ");
	    	optionSQL.append(" where t1.valid='Y' and t1.paper_praxes_id=? ");
	    	optionSQL.append(" order by display_order");
	    	
	    	List list = result.getChildren("Record");
	    	for (int i = 0; i < list.size() ;i++){
	    		Element praxesRec = (Element)list.get(i);
	    		String praxes_type = praxesRec.getChildText("praxes_type");
	    		String praxes_result = praxesRec.getChildText("praxes_result");
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
			    		XmlDocPkgUtil.setChildText(optionRec, "praxes_result",praxes_result);
			    	}
		    		Element options = new Element("Options");
		    		options.addContent(optionResult);
		    		praxesRec.addContent(options);
	    		}
	    	}
	    	return result;
	    	
	    }catch (Exception e) {
			e.printStackTrace();
			log4j.logError("[在线学习-检索习题]"+e.getMessage());			
			return null;
		} finally {
			pdao.releaseConnection();
		}		
	}	
	
	/**
	 * 获取待完成作业
	 *
	 */
	public void getMyExamPaperWocr(){
		Element reqElement =  xmlDocUtil.getRequestData();
		
		StringBuffer strSQL = new StringBuffer();
		ArrayList<Object> paramList = new ArrayList<Object>();
		
		strSQL.append("SELECT DISTINCT t1.paper_id,t2.my_examination_id,t1.create_date,t1.create_by,t2.userid,t2.send_id, t2.arrived_time,t2.status,t1.resource_type,t1.choose_type,");
		strSQL.append(" t1.promoted_quantity,t1.paper_name,t1.cover_path,t1.limit_times,t1.remark,t1.folder_id, t5.folder_name,t6.gradename,t3.coursenm,");
		strSQL.append(" (SELECT count(*) FROM sell_read_log t4 WHERE t1.paper_id = t4.paper_info_id) AS readcount");
		strSQL.append(" FROM    learn_examination_paper t1 LEFT JOIN learn_my_examination t2 ON t1.paper_id = t2.paper_id AND t2.userid = ? , ");
		String qry_userid = reqElement.getChildText("qry_userid");
		paramList.add(qry_userid);
		strSQL.append(" base_course t3,base_book_folder t5,base_grade t6,pcmc_dept t9");
		strSQL.append("  WHERE     t1.publish_status = 'Y' AND t1.valid = 'Y' AND t1.subject_id = t3.subjectid");
		strSQL.append(" AND t1.grade_code = t3.gradecode AND t1.folder_id = t5.folder_id AND t3.gradecode = t6.gradecode and t1.deptcode=t9.deptcode");
		
		String qry_courseid = reqElement.getChildText("qry_courseid");
		if (StringUtil.isNotEmpty(qry_courseid)){
		    strSQL.append(" and t3.courseid=?");
		    paramList.add(qry_courseid);
		}
		
		String qry_folder_id=reqElement.getChildText("qry_folder_id");
		if(StringUtil.isNotEmpty(qry_folder_id)){
			strSQL.append(" and t1.folder_id=?");
			paramList.add(qry_folder_id);
		}
		String qry_deptid = reqElement.getChildText("qry_deptid");
		if (StringUtil.isNotEmpty(qry_deptid)){
		    strSQL.append(" and t9.deptid = ?");
		    paramList.add(qry_deptid);
		}
		String qry_status = reqElement.getChildText("qry_status");
		if (StringUtil.isNotEmpty(qry_status)){
			if ("10".equals(qry_status) ){
				strSQL.append(" and (t2.status is null or t2.status = ? ");
				paramList.add(qry_status);
				strSQL.append(" )");
			}else if("40".equals(qry_status) || "70".equals(qry_status)){
				strSQL.append(" and t2.status=?");
				paramList.add(qry_status);
			}
			else if  ("30".equals(qry_status)){//已接收
			    strSQL.append(" and t2.status>=?");
			    paramList.add(qry_status);
			}
			else if  ("50".equals(qry_status)){//已提交
		       strSQL.append(" and t2.status in ('50','60') ");
			}		    
		}
		String qry_paper_name = reqElement.getChildText("qry_paper_name");
		if (StringUtil.isNotEmpty(qry_paper_name)){
		    strSQL.append(" and t1.paper_name like ?");
		    paramList.add(qry_paper_name+"%");
		}
		String qry_resource_type = reqElement.getChildText("qry_resource_type");
		if (StringUtil.isNotEmpty(qry_resource_type)){
		    strSQL.append(" and t1.resource_type like ?");
		    paramList.add(qry_resource_type+"%");
		}
		String qry_subject_id = reqElement.getChildText("qry_subject_id");
		if (StringUtil.isNotEmpty(qry_subject_id)){
		    strSQL.append(" and t1.subject_id = ?");
		    paramList.add(qry_subject_id);
		}
		String qry_grade_code = reqElement.getChildText("qry_grade_code");
		if (StringUtil.isNotEmpty(qry_grade_code)){
		    strSQL.append(" and t1.grade_code = ?");
		    paramList.add(qry_grade_code);
		}		

		String qry_begin_date = reqElement.getChildText("qry_begin_date");
		if (StringUtil.isNotEmpty(qry_begin_date)){
		    strSQL.append(" and t2.create_date >= ?");
		    paramList.add(SqlTypes.getConvertor("Timestamp").convert(qry_begin_date, "yyyy-MM-dd"));
		}		
		String qry_end_date = reqElement.getChildText("qry_end_date");
		if (StringUtil.isNotEmpty(qry_end_date)){
		    strSQL.append(" and t2.create_date <= ?");
		    paramList.add(SqlTypes.getConvertor("Timestamp").convert(qry_end_date, "yyyy-MM-dd"));
		}
		String qry_condition = reqElement.getChildText("qry_condition");
		if (StringUtil.isNotEmpty(qry_condition)){
		    strSQL.append(" and (t1.paper_name like ? or t1.create_by like ?)");
		    paramList.add("%"+qry_condition+"%");
		    paramList.add("%"+qry_condition+"%");
		}
		  
		String orderBy = xmlDocUtil.getOrderBy();
		if (StringUtil.isNotEmpty(orderBy)) {
			strSQL.append(orderBy);
		} else {
			strSQL.append(" order by t1.create_date desc ");
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
			log4j.logError("[在线学习-取我的试卷(课件)]"+e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
		} finally {
			pdao.releaseConnection();
		}		
	}
	
	/**
	 * 将试卷加入我的试卷表
	 */
	public void addMyExamPaper(){
		Element reqElement =  xmlDocUtil.getRequestData();
		String userid = xmlDocUtil.getSession().getChildText("userid");
		
		String paper_id= reqElement.getChildText("paper_id");
		
		PlatformDao pdao = new PlatformDao();
		try {
			Element myExamRec = ConfigDocument.createRecordElement("yuexue", "learn_my_examination");
			XmlDocPkgUtil.copyValues(reqElement, myExamRec, 0, true);
    		XmlDocPkgUtil.setChildText(myExamRec, "paper_id", paper_id);
    		XmlDocPkgUtil.setChildText(myExamRec, "status", "10");
    		XmlDocPkgUtil.setChildText(myExamRec, "userid", userid);
    		
    		String myexamid= pdao.insertOneRecordSeqPk(myExamRec).toString();
    		
    		ArrayList<Object> examParam = new ArrayList<Object>();
    		StringBuffer examPaperSQL=new StringBuffer();
    		examPaperSQL.append("select * from learn_my_examination t1 where t1.my_examination_id = ?");
    		examParam.add(myexamid);
    		pdao.setSql(examPaperSQL.toString());
	    	pdao.setBindValues(examParam);
	    	Element result=pdao.executeQuerySql(0, -1);
	    	
	    	xmlDocUtil.getResponse().addContent(result);
	    	xmlDocUtil.setResult("0");
		} catch (Exception e) {
			e.printStackTrace();
			log4j.logError("[新增我的试卷]"+e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
		}
		
	}
	
	/**
	 * 获取试卷题目(作业完成后会返回答案信息)
	 *
	 */	
	public void getPaperPraxesWocr(){
		Element reqElement =  xmlDocUtil.getRequestData();
		String paper_id = reqElement.getChildText("paper_id");
		String my_examination_id = reqElement.getChildText("my_examination_id");
		String usertype = xmlDocUtil.getSession().getChildText("usertype");
		
		Element result = getPaperPraxesInfoWocr(paper_id,my_examination_id,null,usertype);
		if (result!=null){
	    	xmlDocUtil.getResponse().addContent(result);
	    	xmlDocUtil.setResult("0");			
		}
		else{
			xmlDocUtil.writeErrorMsg("20206","取作业结果发生错误!");
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE); 
		}
		
	}
	
	 /**
     * 取试卷题目信息(含结果)
     * @param paper_id
     * @param my_examination_id
     * @param paper_praxes_id
     * @param userType
     * @return
     */
	public Element getPaperPraxesInfoWocr(String paper_id,String my_examination_id,String paper_praxes_id,String userType){
		String optionStr = "A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z";
		String[] optionArray = optionStr.split(",");	
		String qry_user_id="";
		String consuming_time="0";
		StringBuffer paperSQL = new StringBuffer();
		paperSQL.append("select t1.consuming_time,t2.paper_id,t2.resource_type,t2.choose_type,t1.status,t1.random_paper_id,t1.userid");
		paperSQL.append(" from learn_my_examination t1,learn_examination_paper t2");
		paperSQL.append(" where t1.paper_id=t2.paper_id and t1.my_examination_id=?");
		
		ArrayList<Object> paperParam = new ArrayList<Object>();	
		paperParam.add(my_examination_id);		
		
		PlatformDao pdao = new PlatformDao();
	    try{
	    	String myExamStatus="";
	    	if (StringUtil.isNotEmpty(my_examination_id)){
		    	pdao.setSql(paperSQL.toString());
		    	pdao.setBindValues(paperParam);
		    	Element paperResult = pdao.executeQuerySql(0,-1);
		    	List list = paperResult.getChildren("Record");
		    	if (list.size()==0){
					return null;		    		
		    	}
		    	else{
			    	Element paperRec = (Element)list.get(0);
			    	//String resourceType = paperRec.getChildText("resource_type");
			    	//if ("10".equals(resourceType) || "40".equals(resourceType)){
			    		myExamStatus = paperRec.getChildText("status");
			    		qry_user_id = paperRec.getChildText("userid");
			    		consuming_time = paperRec.getChildText("consuming_time");
			    		consuming_time=StringUtil.isEmpty(consuming_time)?"0":consuming_time;
			    		if ("30".equals(paperRec.getChildText("choose_type"))){
			    			paper_id = paperRec.getChildText("random_paper_id");
			    		}
			    		
			    	//}
		    	}
	    	}
	    	
			StringBuffer strSQL = new StringBuffer();
			strSQL.append("select DISTINCT t1.paper_praxes_id,t1.paper_id,t1.praxes_id,t1.praxes_type,t1.praxes_content,t1.score as praxes_score,t1.difficulty_level,t1.discrimination,t1.praxes_hint,");
            strSQL.append("t1.right_result,t2.is_right,format(t2.score,1) as score,t2.correct_result,");
			strSQL.append("(select avg(consuming_time) from learn_examination_result where paper_id = t1.paper_id and classid = t5.classid) as avg_consuming,");
			strSQL.append(" t4.status, t6.paper_name,t2.result_id,t2.student_draf_path,t2.teacher_draf_path,t2.praxes_result,t2.consuming_time,getParamDesc('c_praxes_type',t1.praxes_type) as praxes_type_desc");
			strSQL.append(" from learn_paper_praxes t1 left join learn_examination_result t2");
			strSQL.append(" on t1.paper_praxes_id=t2.paper_praxes_id and t2.userid=? and t2.my_examination_id=?");
			strSQL.append(" ,learn_my_examination t4,base_studentinfo t5,learn_examination_paper t6");
			strSQL.append(" where t1.valid='Y' and t1.paper_id = ?  and t1.paper_id=t6.paper_id");	
			strSQL.append(" and t4.my_examination_id=? and t5.state='1' and t5.userid=?");
			
			ArrayList<Object> paramList = new ArrayList<Object>();
			paramList.add(qry_user_id);
			paramList.add(my_examination_id);	
			paramList.add(paper_id);
			paramList.add(my_examination_id);
			paramList.add(qry_user_id);
			
			if (StringUtil.isNotEmpty(paper_praxes_id)){
				strSQL.append(" and t1.paper_praxes_id=?");
				paramList.add(paper_praxes_id);
			}
					
		
			strSQL.append(" order by t1.display_order,t1.paper_praxes_id");
			
	    	pdao.setSql(strSQL.toString());
	    	pdao.setBindValues(paramList);
	    	Element result = pdao.executeQuerySql(0,-1);
	    	//Element consuming=XmlDocPkgUtil.createFieldEle("consuming_time");
	    	//XmlDocPkgUtil.setChildText(consuming, "consuming_time",consuming_time);
	    	//result.addContent(consuming);
	    	StringBuffer optionSQL = new StringBuffer();
	    	optionSQL.append("select t1.paper_option_id,t1.paper_praxes_id,ifnull(t1.praxes_id,t1.paper_praxes_id) as praxes_id,ifnull(t1.option_id,t1.paper_option_id) as option_id,");
			//为支持在试卷中进阶,将答案一直显示，让pad程序进行业务逻辑判断
	    	//if ("70".equals(myExamStatus) || "60".equals(myExamStatus) || "50".equals(myExamStatus) || "2".equals(userType)){
				optionSQL.append("t1.is_right,");
			//}	    	
	    	optionSQL.append(" t1.option_content");
	    	optionSQL.append(" from learn_paper_options t1 ");
	    	optionSQL.append(" where t1.valid='Y' and t1.paper_praxes_id=? ");
	    	optionSQL.append(" order by display_order");
	    	
	    	List list = result.getChildren("Record");
	    	for (int i = 0; i < list.size() ;i++){
	    		Element praxesRec = (Element)list.get(i);
	    		String praxes_type = praxesRec.getChildText("praxes_type");
	    		String praxes_result = praxesRec.getChildText("praxes_result");
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
			    		XmlDocPkgUtil.setChildText(optionRec, "praxes_result",praxes_result);
			    	}
		    		Element options = new Element("Options");
		    		options.addContent(optionResult);
		    		praxesRec.addContent(options);
	    		}
	    	}
	    	return result;
	    	
	    }catch (Exception e) {
			e.printStackTrace();
			log4j.logError("[在线学习-检索习题]"+e.getMessage());			
			return null;
		} finally {
			pdao.releaseConnection();
		}		
	}	
	
	/**
	 * 取试卷信息
	 *
	 */
	public void getPaperInfoWocr(){
		Element reqElement =  xmlDocUtil.getRequestData();
		String usertype = xmlDocUtil.getSession().getChildText("usertype");
		String  attachmentType = reqElement.getChildText("attachment_type");
		String  paper_id = reqElement.getChildText("paper_id");
		String  my_examination_id = reqElement.getChildText("my_examination_id");
		
		StringBuffer strSQL = new StringBuffer();
		strSQL.append("select d.subjname,getParamDesc('c_grade',t.grade_code) as grade_name,m.folder_name,n.paper_score,n.send_id,");
		strSQL.append(" (select sum(t1.score) from learn_paper_praxes t1 where t1.valid='Y'  and t1.paper_id=t.paper_id) as total_score,");
		strSQL.append(" getParamDesc('c_resource_type',t.resource_type) as resource_type_desc,");
		
		strSQL.append(" n.end_time,n.consuming_time as consume_time,n.status,t.*");		
		strSQL.append(" from learn_examination_paper t,base_subject d,base_book_folder m,");
		strSQL.append(" learn_my_examination n");
		strSQL.append(" where d.state='1' and t.subject_id=d.subjectid and t.folder_id=m.folder_id");
	    strSQL.append(" and t.paper_id=n.paper_id and t.paper_id=? and n.my_examination_id=?");
		
	    ArrayList<Object> paramList = new ArrayList<Object>();
		paramList.add(paper_id);
		paramList.add(my_examination_id);
		
	    PlatformDao pdao = new PlatformDao();
	    try{
	    	pdao.setSql(strSQL.toString());
	    	pdao.setBindValues(paramList);
	    	Element result = pdao.executeQuerySql(0, -1);
	    	
	    	if (StringUtil.isNotEmpty(attachmentType) && "draft".equals(attachmentType)){
		    	StringBuffer attSQL = new StringBuffer();
		    	attSQL.append("select t2.draft_id as file_id,t1.* from learn_attachment t1,learn_paper_draft t2");
		    	attSQL.append(" where t2.valid='Y' and t1.attachment_id=t2.attachment_id and t2.my_examination_id=?");
		    	attSQL.append(" order by t2.create_date");		    	
		    	
		    	ArrayList<Object> attachParam = new ArrayList<Object>();
		    	attachParam.add(my_examination_id);
		    	
		    	pdao.setSql(attSQL.toString());
		    	pdao.setBindValues(attachParam);
		    	
		    	Element attResult = pdao.executeQuerySql(0, -1);
		    	Element att = new Element("Drafts");
		    	att.addContent(attResult);
		    	result.getChild("Record").addContent(att);	    		
	    	}
	    		
	    	StringBuffer attSQL = new StringBuffer();
	    	attSQL.append("select t2.paper_attachment_id,t1.* from learn_attachment t1,learn_paper_attachment t2");
	    	attSQL.append(" where t2.valid='Y' and t1.attachment_id=t2.attachment_id and t2.paper_id=?");
	    	attSQL.append(" order by t2.create_date");		    	
	    	
	    	ArrayList<Object> attachParam = new ArrayList<Object>();
	    	attachParam.add(reqElement.getChildText("paper_id"));
	    	
	    	pdao.setSql(attSQL.toString());
	    	pdao.setBindValues(attachParam);
	    	
	    	Element attResult = pdao.executeQuerySql(0, -1);
	    	Element att = new Element("Attachments");
	    	att.addContent(attResult);
	    	result.getChild("Record").addContent(att);
	    	
	    	xmlDocUtil.getResponse().addContent(result);
	    	xmlDocUtil.setResult("0");
	    	
	    }catch (Exception e) {
			e.printStackTrace();
			log4j.logError("[在线学习-获取试卷信息]"+e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
		} finally {
			pdao.releaseConnection();
		}		
	}	
	
	/**
	 * 获取课件
	 *
	 */
	public void getPaperCourseWocr(){
		Element reqElement =  xmlDocUtil.getRequestData();
		
		String userid   = xmlDocUtil.getSession().getChildTextTrim("userid");	
		StringBuffer strSQL = new StringBuffer();
		ArrayList<Object> paramList = new ArrayList<Object>();
		
		strSQL.append("SELECT t1.paper_id,t1.create_date,t1.create_by,t1.resource_type,t1.choose_type,t1.promoted_quantity,t1.paper_name,t1.cover_path, t1.limit_times, ");
		strSQL.append(" t1.remark,t1.folder_id, t5.folder_name, t6.gradename,t3.coursenm, t8.attachment_id, t1.grade_code,t1.subject_id,t5.folder_code, ");
		strSQL.append(" (SELECT count(*) FROM sell_read_log tt WHERE t1.paper_id = tt.paper_info_id) AS readcount,t4.file_path,t4.access_path");
		strSQL.append("  FROM learn_examination_paper t1 left join learn_paper_attachment t8 on t1.paper_id = t8.paper_id LEFT JOIN learn_attachment t4 ON t8.attachment_id = t4.attachment_id, pcmc_dept t9,");
		strSQL.append("  base_course t3,base_book_folder t5, base_grade t6 WHERE t1.folder_id = t5.folder_id  AND t3.gradecode = t6.gradecode AND t9.deptid = t6.deptid");
		strSQL.append("  AND t1.grade_code = t3.gradecode and t1.subject_id = t3.subjectid AND t9.deptid = t3.deptid AND t1.deptcode = t9.deptcode AND (t1.resource_id = '' OR t1.resource_id IS NULL) AND t1.valid = 'Y'");
		
		String qry_folder_id=reqElement.getChildText("qry_folder_id");
		if(StringUtil.isNotEmpty(qry_folder_id)){
			strSQL.append(" and t1.folder_id=?");
			paramList.add(qry_folder_id);
		}
		
		String qry_resource_type = reqElement.getChildText("qry_resource_type");
		if (StringUtil.isNotEmpty(qry_resource_type)){
		    strSQL.append(" and t1.resource_type like ?");
		    paramList.add(qry_resource_type+"%");
		}
		String qry_subject_id = reqElement.getChildText("qry_subject_id");
		if (StringUtil.isNotEmpty(qry_subject_id)){
		    strSQL.append(" and t1.subject_id = ?");
		    paramList.add(qry_subject_id);
		}
		String qry_deptid = reqElement.getChildText("qry_deptid");
		if (StringUtil.isNotEmpty(qry_deptid)){
		    strSQL.append(" and t9.deptid = ?");
		    paramList.add(qry_deptid);
		}
		String qry_teacher_id = reqElement.getChildText("qry_teacher_id");
		if (StringUtil.isNotEmpty(qry_teacher_id)){
		    strSQL.append(" and t1.user_id = ?");
		    paramList.add(qry_teacher_id);
		}
		String qry_resourceType = reqElement.getChildText("qry_resourceType");
		if (StringUtil.isNotEmpty(qry_resourceType)){
		    strSQL.append(" and t1.resource_type like ?");
		    paramList.add(qry_resourceType+"%");
		}
		String qry_condition = reqElement.getChildText("qry_condition");
		if (StringUtil.isNotEmpty(qry_condition)){
		    strSQL.append(" and (t1.paper_name like ? or t1.create_by like ?)");
		    paramList.add("%"+qry_condition+"%");
		    paramList.add("%"+qry_condition+"%");
		}
		String qry_conditionweixin = reqElement.getChildText("qry_conditionweixin");
		if (StringUtil.isNotEmpty(qry_conditionweixin)){
		    strSQL.append(" and (t1.paper_name like ? or t1.create_by like ? or  t6.gradename like ? or t3.coursenm like ?)");
		    paramList.add("%"+qry_conditionweixin+"%");
		    paramList.add("%"+qry_conditionweixin+"%");
		    paramList.add("%"+qry_conditionweixin+"%");
		    paramList.add("%"+qry_conditionweixin+"%");
		}
		String qry_paperid = reqElement.getChildText("qry_paperid");
		if (StringUtil.isNotEmpty(qry_paperid)){
		    strSQL.append(" and t1.paper_id = ?");
		    paramList.add(qry_paperid);
		}
	     // 根据创建时间排序
		String orderBy = xmlDocUtil.getOrderBy();
		if (StringUtil.isNotEmpty(orderBy)) {
			strSQL.append(orderBy);
		}else{
			strSQL.append(" order by t1.create_date desc");	
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
			log4j.logError("[在线学习-取我的试卷(课件)]"+e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
		} finally {
			pdao.releaseConnection();
		}		
	}
}
