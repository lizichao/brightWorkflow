package cn.com.bright.yuexue.base;

import java.io.File;
import java.util.ArrayList;

import org.jdom.Document;
import org.jdom.Element;

import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.util.ConfigDocument;
import cn.brightcom.jraf.util.Constants;
import cn.brightcom.jraf.util.DaoUtil;
import cn.brightcom.jraf.util.FileUtil;
import cn.brightcom.jraf.util.Log;
import cn.brightcom.jraf.util.StringUtil;
import cn.brightcom.jraf.util.XmlDocPkgUtil;

/**
 * 
 * <p>Title: 课程管理</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2014 Brightcom</p>
 * <p>Company: ShenZhen BrightCom Technology Co.,Ltd.</p>
 * @author <a href="http://www.szbrightcom.cn" target="_blank">LHBO</a>
 * @version 1.0x
 */
public class CourseManage
{
	private XmlDocPkgUtil xmlDocUtil = null;
	private String upFolder = "/upload/base/courseicon/";
    
    private static Log log4j = new Log(CourseManage.class.getName());
    
    public Document doPost(Document xmlDoc)
	{
    	xmlDocUtil = new XmlDocPkgUtil(xmlDoc);
		String action = xmlDocUtil.getAction();
		if("addCourse".equals(action))
		{
			addCourse();
		}
		else if("updateCourse".equals(action))
		{
			updateCourse();
		}
		else if("getCourseByUseridMOOC".equals(action))
		{
			getCourseByUseridMOOC();
		}
		else if("getCourseWocr".equals(action))
		{
			getCourseWocr();
		}else if("getAllCourseWocr".equals(action))
		{
			getAllCourseWocr();
		}
		return xmlDoc;
	}
    
    private void addCourse()
	{
		Element reqData = xmlDocUtil.getRequestData();
    	PlatformDao pDao = null;
        try
        {
        	pDao = new PlatformDao();
        	if(!checkedUnique()){
        		xmlDocUtil.writeErrorMsg("10110", "课程重复");
				return;
        	}
        	
        	Element imgEle1 = reqData.getChild("courseiconFile");
        	String c_img1 = moveFile(imgEle1);
        	
        	Element rec = ConfigDocument.createRecordElement("yuexue","base_course");
        	XmlDocPkgUtil.copyValues(reqData,rec,0,true);
        	if(StringUtil.isNotEmpty(c_img1)){
        		XmlDocPkgUtil.setChildText(rec, "courseicon", c_img1);
        	}
        	
        	Object pk = pDao.insertOneRecordSeqPk(rec);        	
        	String[] flds = {"courseid","courseicon"};
            Element rData = XmlDocPkgUtil.createMetaData(flds);
            Element pkRec = XmlDocPkgUtil.createRecord(flds,
            new String[]{""+pk,c_img1});
            rData.addContent(pkRec);
        	
            xmlDocUtil.writeHintMsg("10100", "新增课程成功！");
        	xmlDocUtil.setResult("0");
        	xmlDocUtil.getResponse().addContent(rData);
        }
        catch(Exception ex)
        {
        	ex.printStackTrace();
        	log4j.logError(ex);
        	xmlDocUtil.writeErrorMsg("10120", "新增课程异常");
        }
        finally
        {
        	pDao.releaseConnection();
        }
	}
    
    private void updateCourse()
	{
		Element reqData = xmlDocUtil.getRequestData();
    	PlatformDao pDao = null;
        try
        {
        	pDao = new PlatformDao();
        	if(!checkedUnique()){
        		xmlDocUtil.writeErrorMsg("10210", "课程重复");
				return;
        	}
        	
        	Element imgEle1 = reqData.getChild("courseiconFile");
        	String c_img1 = moveFile(imgEle1);
        	
        	Element rec = ConfigDocument.createRecordElement("yuexue","base_course");
        	XmlDocPkgUtil.copyValues(reqData,rec,0,true);
        	if(StringUtil.isNotEmpty(c_img1)){
        		XmlDocPkgUtil.setChildText(rec, "courseicon", c_img1);
        	}
        	pDao.updateOneRecord(rec);
        	
        	String[] flds = {"courseicon"};
            Element rData = XmlDocPkgUtil.createMetaData(flds);
            Element pkRec = XmlDocPkgUtil.createRecord(flds, new String[]{c_img1==null?"":c_img1});
            rData.addContent(pkRec);
        	xmlDocUtil.getResponse().addContent(rData);
            
        	xmlDocUtil.writeHintMsg("10200", "修改课程成功！");
        	xmlDocUtil.setResult("0");
        }
        catch(Exception ex)
        {
        	ex.printStackTrace();
        	log4j.logError(ex);
        	xmlDocUtil.writeErrorMsg("10220", "修改课程异常");
        }
        finally
        {
        	pDao.releaseConnection();
        }
	}
    
    private boolean checkedUnique() throws Exception {
    	Element reqData = xmlDocUtil.getRequestData();
    	String deptid   = xmlDocUtil.getSession().getChildText("deptid");
    	String courseid = reqData.getChildTextTrim("courseid");
    	String gradecode = reqData.getChildTextTrim("gradecode");
    	String subjectid = reqData.getChildTextTrim("subjectid");
    	if(StringUtil.isEmpty(subjectid)) return true;
    	
    	ArrayList<String> bvals = new ArrayList<String>();
		StringBuffer sqlBuf = new StringBuffer();
		sqlBuf.append("select courseid from base_course where 1=1 ")
		.append(" and gradecode=? and subjectid = ? and state > '0'")
		.append(" and deptid=?");
		bvals.add(gradecode);
		bvals.add(subjectid);
		bvals.add(deptid);
		if(StringUtil.isNotEmpty(courseid))
		{
			sqlBuf.append(" and courseid <> ?");
			bvals.add(courseid);
		}
		Element rec = DaoUtil.getOneRecord(sqlBuf.toString(), bvals);
		return null == rec;
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
	 * 根据学生编号查询学科列表
	 *
	 */
	public void getCourseByUseridMOOC(){
		String userid   = xmlDocUtil.getSession().getChildTextTrim("userid");		
		
		Element reqElement =  xmlDocUtil.getRequestData();
		StringBuffer strSQL = new StringBuffer();
		ArrayList<Object> paramList = new ArrayList<Object>();
		String qry_resource_type = reqElement.getChildText("qry_resource_type");
		strSQL.append("select t1.coursenm,t1.courseid,t1.courseicon,t1.subjectid,t6.icon as subjecticon,t2.classnm,");
		//查询课件
		if("20".equals(qry_resource_type)){
			strSQL.append(" (select count(DISTINCT tt1.paper_id) from  learn_examination_paper tt1,base_teacher_subject tt2 ,base_book_folder tt3 where tt1.subject_id = t1.subjectid and tt1.grade_code=t2.gradecode");
			strSQL.append(" and tt1.user_id=tt2.userid and tt2.classid=t3.classid and tt1.valid = 'Y' and t3.state >'0' and tt2.state >'0' ");
			strSQL.append("  and tt1.grade_code=t1.gradecode and tt1.folder_id = tt3.folder_id and tt1.resource_type like ?");
			paramList.add(qry_resource_type+"%");
			strSQL.append("  and t3.userid = ? ");
			paramList.add(userid);
		}else{
			strSQL.append(" (select count(*) from learn_my_examination tt1, learn_examination_paper tt2 where tt1.paper_id = tt2.paper_id  and tt1.userid = t4.userid");
			strSQL.append(" and tt2.subject_id = t1.subjectid and tt1.valid = 'Y' and tt2.valid = 'Y'");
	        strSQL.append(" and tt2.resource_type like ?");
			paramList.add(qry_resource_type+"%");
		
			String qry_status = reqElement.getChildText("qry_status");
			if (StringUtil.isNotEmpty(qry_status)){
				if("10".equals(qry_status)){
					strSQL.append(" and tt1.status = ?");
				    paramList.add(qry_status);
				}else{
					strSQL.append(" and tt1.status != '10' ");
				}
			}
				
		}
		strSQL.append(" ) as init_count");	
		strSQL.append(" from base_course t1, base_class t2, base_studentinfo t3,pcmc_user t4,pcmc_user_dept t5,base_subject t6");
		strSQL.append(" where t1.gradecode = t2.gradecode and t2.classid = t3.classid  and t3.userid = t4.userid");
		strSQL.append(" and t4.userid = t5.userid  and t1.deptid = t5.deptid and t1.state > '0' and t1.subjectid = t6.subjectid");
		strSQL.append(" and t4.userid = ?");		
		 paramList.add(userid);
		 
	    String orderBy = xmlDocUtil.getOrderBy();
	    if (StringUtil.isNotEmpty(orderBy)){
	    	strSQL.append(" order by "+orderBy);
	    }
	    else{
	    	strSQL.append(" order by t1.orderidx");
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
			log4j.logError("[根据学生编号获取学科失败:]"+e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
		} finally {
			pdao.releaseConnection();
		}		
	}
	
	 /**
		 * 查询学科列表
		 *
		 */
		public void getCourseWocr(){
			String userid   = xmlDocUtil.getSession().getChildTextTrim("userid");		
			
			Element reqElement =  xmlDocUtil.getRequestData();
			StringBuffer strSQL = new StringBuffer();
			ArrayList<Object> paramList = new ArrayList<Object>();
			String qry_resource_type = reqElement.getChildText("qry_resource_type");
			String qry_status = reqElement.getChildText("qry_status");
			strSQL.append("SELECT t1.coursenm,t1.courseid,t1.courseicon,t1.subjectid,t6.icon AS subjecticon, t2.classnm,");
			//查询作业
			if("10".equals(qry_resource_type)){
				strSQL.append(" (SELECT count(*)FROM learn_my_examination tt1, learn_examination_paper tt2 ");
				strSQL.append("  WHERE tt1.paper_id = tt2.paper_id AND tt1.userid = t4.userid AND tt2.subject_id = t1.subjectid ");
				strSQL.append(" AND tt2.resource_type LIKE '10%' AND tt1.status = '10' AND tt1.valid = 'Y' AND tt2.valid = 'Y' ");
				
			//查询课件
			} else if("20".equals(qry_resource_type)){
				strSQL.append(" (SELECT count(*)FROM learn_my_examination tt1, learn_examination_paper tt2 ");
				strSQL.append("  WHERE tt1.paper_id = tt2.paper_id AND tt1.userid = t4.userid AND tt2.subject_id = t1.subjectid ");
				strSQL.append(" AND tt2.resource_type LIKE '20%' AND tt1.status = '10' AND tt1.valid = 'Y' AND tt2.valid = 'Y' ");
				
			//查询错题
			} else if ("90".equals(qry_resource_type)) {
				strSQL.append(" (SELECT count(*) FROM learn_my_examination tt1, learn_examination_paper tt2,learn_paper_praxes tt3 LEFT JOIN learn_examination_result tt4 ON tt3.paper_id = tt4.paper_id ");
				strSQL.append("  WHERE tt1.paper_id = tt2.paper_id and tt1.my_examination_id = tt4.my_examination_id and tt3.paper_praxes_id = tt4.paper_praxes_id AND tt1.userid = t4.userid AND tt2.subject_id = t1.subjectid ");
				strSQL.append(" AND tt2.resource_type LIKE '40%'  AND tt1.valid = 'Y' AND tt2.valid = 'Y' AND tt3.valid = 'Y' AND tt4.valid = 'Y' AND tt4.score < tt3.score ");				
				if (StringUtil.isNotEmpty(qry_status)){
					if("10".equals(qry_status)){
						strSQL.append(" and tt1.status <= '40' ");
					}else{
						strSQL.append(" and tt1.status > '40' ");
					}
				}
			}
			else{
				strSQL.append(" (SELECT count(*)FROM learn_my_examination tt1, learn_examination_paper tt2 ");
				strSQL.append("  WHERE tt1.paper_id = tt2.paper_id AND tt1.userid = t4.userid AND tt2.subject_id = t1.subjectid ");
				strSQL.append(" AND tt2.resource_type LIKE '40%'  AND tt1.valid = 'Y' AND tt2.valid = 'Y' ");
			
				
				if (StringUtil.isNotEmpty(qry_status)){
					if("10".equals(qry_status)){
						strSQL.append(" and tt1.status <='40' ");
					}else{
						strSQL.append(" and tt1.status > '40' ");
					}
				}
					
			}
			strSQL.append(" ) as init_count");	
			strSQL.append(" FROM base_course t1,base_class t2,base_studentinfo t3, pcmc_user t4, pcmc_user_dept t5,base_subject t6 ");
			strSQL.append(" WHERE t1.gradecode = t2.gradecode AND t2.classid = t3.classid AND t3.userid = t4.userid AND t4.userid = t5.userid "); 
			strSQL.append(" AND t1.deptid = t5.deptid AND t1.state > '0' AND t1.subjectid = t6.subjectid  AND  t4.userid= ? ");
			paramList.add(userid);
			strSQL.append(" order by t1.orderidx");
		    PlatformDao pdao = new PlatformDao();
		    try{
		    	pdao.setSql(strSQL.toString());
		    	pdao.setBindValues(paramList);
		    	Element result = pdao.executeQuerySql(xmlDocUtil.getPageSize(), xmlDocUtil.getPageNo());
		    	
		    	xmlDocUtil.getResponse().addContent(result);
		    	xmlDocUtil.setResult("0");
		    	
		    }catch (Exception e) {
				e.printStackTrace();
				log4j.logError("[根据学生编号获取学科失败:]"+e.getMessage());
				xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
			} finally {
				pdao.releaseConnection();
			}		
		}
		
		 /**
		 * 查询学科列表
		 *
		 */
		public void getAllCourseWocr(){	
			
			Element reqElement =  xmlDocUtil.getRequestData();
			StringBuffer strSQL = new StringBuffer();
			ArrayList<Object> paramList = new ArrayList<Object>();
			String qry_deptid=reqElement.getChildText("qry_deptid");
			strSQL.append("select DISTINCT t1.subjectid,t1.subjname AS coursenm,t1.icon,");
			strSQL.append(" (select count(*) from  learn_examination_paper tt1,pcmc_dept tt3  where tt1.subject_id = t1.subjectid and tt1.deptcode=tt3.deptcode");
			strSQL.append("  and tt1.valid = 'Y' and (tt1.resource_id = '' or tt1.resource_id is null) and tt1.folder_id <> '' and (tt1.resource_type like '20%' or tt1.resource_type like '10%')  ");
						
			if (StringUtil.isNotEmpty(qry_deptid)){
				strSQL.append(" and tt3.deptid = ?");
				paramList.add(qry_deptid);
			}
			strSQL.append(" ) as init_count");	
			strSQL.append(" from base_subject t1,base_course t2");
			strSQL.append(" where t1.subjectid = t2.subjectid"); 
			if (StringUtil.isNotEmpty(qry_deptid)){
				strSQL.append("  and t2.deptid = ?");
				paramList.add(qry_deptid);
			}		
			strSQL.append(" order by t2.orderidx");
		    PlatformDao pdao = new PlatformDao();
		    try{
		    	pdao.setSql(strSQL.toString());
		    	pdao.setBindValues(paramList);
		    	Element result = pdao.executeQuerySql(xmlDocUtil.getPageSize(), xmlDocUtil.getPageNo());
		    	
		    	xmlDocUtil.getResponse().addContent(result);
		    	xmlDocUtil.setResult("0");
		    	
		    }catch (Exception e) {
				e.printStackTrace();
				log4j.logError("[获取学科失败:]"+e.getMessage());
				xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
			} finally {
				pdao.releaseConnection();
			}		
		}
}
