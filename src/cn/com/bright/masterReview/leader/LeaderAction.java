package cn.com.bright.masterReview.leader;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;

import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.util.ConfigDocument;
import cn.brightcom.jraf.util.DaoUtil;
import cn.brightcom.jraf.util.DatetimeUtil;
import cn.brightcom.jraf.util.JDomUtil;
import cn.brightcom.jraf.util.Log;
import cn.brightcom.jraf.util.StringUtil;
import cn.brightcom.jraf.util.XmlDocPkgUtil;
import cn.com.bright.masterReview.base.UserManage;

/**
 * 
 * @ClassName: Register 
 * @Description: TODO
 * @author: lzc
 * @date: 2016年4月28日 上午10:46:40
 */
public class LeaderAction extends UserManage{
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
		if ("addLeader".equals(action)){
		    addLeader();
		}else if ("findLeader".equals(action)){
		    findLeader();
        }else if ("updateLeader".equals(action)){
            updateLeader();
        }else if ("deleteLeader".equals(action)){
            deleteLeader();
        }else if ("findApproveResult".equals(action)){
            findApproveResult();
        }
		return xmlDoc;
	}
	
	private void findApproveResult() {
	    Element dataElement = xmlDocUtil.getRequestData();
        String username = dataElement.getChildText("username");
        String usercode = dataElement.getChildText("usercode");
        String identitycard = dataElement.getChildText("identitycard");

        PlatformDao dao = null;
        try {
            dao = new PlatformDao();
            ArrayList<String> bvals = new ArrayList<String>();
            StringBuffer sqlBuf = new StringBuffer("");


            sqlBuf.append("SELECT t.headerMasterName,");
            sqlBuf.append(" ziligrade,");
            sqlBuf.append(" managementgrade,");
            sqlBuf.append(" zhuanyegrade,");
            sqlBuf.append("professorgrade,");
            sqlBuf.append("sum(ziligrade + managementgrade + zhuanyegrade + professorgrade) as sumgrade,");
            sqlBuf.append("   CASE");
            sqlBuf.append(" WHEN apply_level = '0' THEN '特级'");
            sqlBuf.append(" WHEN apply_level = '1' THEN '一级'");
            sqlBuf.append(" WHEN apply_level = '2' THEN '二级'");
            sqlBuf.append(" WHEN apply_level = '3' THEN '三级'");
            sqlBuf.append(" ELSE ''");
            sqlBuf.append("  END");
            sqlBuf.append("  AS apply_level,");
            
            //sqlBuf.append("apply_level,");
            sqlBuf.append("t.processInstanceId,");
            sqlBuf.append("a.usercode,");
            sqlBuf.append(" a.idnumber");
            sqlBuf.append(" FROM headmaster t");
            sqlBuf.append(" LEFT JOIN");
            sqlBuf.append("(SELECT avg(baseinfo_grade),");
            sqlBuf.append(" avg(work_experience_grade),");
            sqlBuf.append(" avg(education_grade),");
            sqlBuf.append("  avg(work_experience_grade)");
            sqlBuf.append(" + avg(education_grade)");
            sqlBuf.append(" + avg(professional_title_grade)");
            sqlBuf.append("   AS ziligrade,");
            sqlBuf.append("avg(professional_title_grade),");
            sqlBuf.append("avg(management_difficulty_grade),");
            sqlBuf.append(" avg(management_difficulty_grade_ago),");
            sqlBuf.append("   avg(management_difficulty_grade)");
            sqlBuf.append(" + avg(management_difficulty_grade_ago)");
            sqlBuf.append("    AS managementgrade,");
            sqlBuf.append(" avg(paper_grade),");
            sqlBuf.append("avg(work_publish_grade),");
            sqlBuf.append("avg(subject_grade),");
            sqlBuf.append("avg(personal_award_grade),");
            sqlBuf.append(" avg(school_award_grade),");
            sqlBuf.append(" avg(paper_grade)");
            sqlBuf.append(" + avg(work_publish_grade)");
            sqlBuf.append(" + avg(subject_grade)");
            sqlBuf.append(" + avg(personal_award_grade)");
            sqlBuf.append(" + avg(school_award_grade)");
            sqlBuf.append("  AS zhuanyegrade,");
            sqlBuf.append("apply_headmaster,");
            sqlBuf.append("processInstanceId");
            sqlBuf.append(" FROM headmaster_personnel_leader_grade");
            sqlBuf.append("  GROUP BY apply_headmaster,processInstanceId) p");
            sqlBuf.append(" ON t.headerMasterId = p.apply_headmaster");
            sqlBuf.append(" AND t.processInstanceId = p.processInstanceId");
            sqlBuf.append(" LEFT JOIN");
            sqlBuf.append("  (SELECT avg(report_grade) AS professorgrade, apply_headmaster,processInstanceId");
            sqlBuf.append("  FROM headmaster_professor_grade");
            sqlBuf.append(" GROUP BY apply_headmaster,processInstanceId) u");
            sqlBuf.append("  ON t.headerMasterId = u.apply_headmaster");
            sqlBuf.append("  AND t.processInstanceId = u.processInstanceId");
            sqlBuf.append(" inner join pcmc_user a");
            sqlBuf.append(" on t.headerMasterId = a.userid");
            sqlBuf.append(" where 1=1");
            sqlBuf.append("  AND t.create_date = (SELECT max(create_date)");
            sqlBuf.append("  FROM headmaster a");
            sqlBuf.append("  WHERE t.headerMasterId = a.headerMasterId)");
            
         
            
            if (StringUtil.isNotEmpty(username)) {
                sqlBuf.append(" and t.headerMasterName like ?");
                bvals.add("%"+username+"%");
            }
            
            if (StringUtil.isNotEmpty(usercode)) {
                sqlBuf.append(" and a.usercode like ?");
                bvals.add("%"+usercode+"%");
            }
            
            if (StringUtil.isNotEmpty(identitycard)) {
                sqlBuf.append(" and a.idnumber = ?");
                bvals.add(identitycard);
            }
            
            sqlBuf.append("  GROUP BY t.headerMasterId");
         

            dao.setSql(sqlBuf.toString());
            dao.setBindValues(bvals);
            Element resultElement = dao.executeQuerySql(xmlDocUtil.getPageSize(), xmlDocUtil.getPageNo());
            xmlDocUtil.getResponse().addContent(resultElement);
            xmlDocUtil.setResult("0");
        } catch (Exception e) {
            e.printStackTrace();
            xmlDocUtil.setResult("-1");
        } finally {
            dao.releaseConnection();
        }        
    }

    private void deleteLeader() {
	    Element dataElement = xmlDocUtil.getRequestData();
        List idList = dataElement.getChildren("id");
        PlatformDao dao = new PlatformDao();
        StringBuffer sql = new StringBuffer("");
        ArrayList bvals = new ArrayList();
        try {
            dao.beginTransaction();
            if (idList.size() > 0) {
                sql.append("UPDATE headmaster_leader_info");
                sql.append(" SET valid = '0'");
                sql.append(" WHERE id = ?");
                dao.setSql(sql.toString());
                for (int i = 0; i < idList.size(); i++) {
                    Element deviceId = (Element)idList.get(i);
                    bvals.clear();
                    bvals.add(deviceId.getText());
                    dao.addBatch(bvals);
                }
                dao.executeBatch();
                

                sql.setLength(0);
                bvals.clear();
                
                sql.append("UPDATE pcmc_user");
                sql.append(" SET state = '9'");
                sql.append(" WHERE userid = ?");
                 dao.setSql(sql.toString());
                 for (int i = 0; i < idList.size(); i++) {
                     Element deviceId = (Element)idList.get(i);
                     bvals.clear();
                     bvals.add(deviceId.getText());
                     dao.addBatch(bvals);
                 }
                dao.executeBatch();

              
            }
            dao.commitTransaction();
            xmlDocUtil.setResult("0");
            xmlDocUtil.writeHintMsg("10603","删除领导成功");
        }
        catch (Exception e)
        {
            e.printStackTrace() ;
            dao.rollBack();
            log4j.logError("[删除领导发生错误]"+e.getMessage()) ;
            log4j.logInfo(JDomUtil.toXML(xmlDocUtil.getXmlDoc(), "GBK", true)) ;
            xmlDocUtil.writeErrorMsg("10604","删除领导失败");
        }
        finally
        {
            dao.releaseConnection();
        }          
    }

    private void updateLeader() {
        Element reqData = xmlDocUtil.getRequestData();
        Element session = xmlDocUtil.getSession();
        String curUserid = session.getChildText("userid");
        String curUsercode = session.getChildText("usercode");
        String curDeptid = session.getChildText("deptid");
        
        PlatformDao pDao = null;
        try
        {
            pDao = new PlatformDao();
            String deptid = reqData.getChildTextTrim("deptid");
        
            
            Element userData = (Element)reqData.clone();
            //肖像
         //   Element imgEle1 = reqData.getChild("portraitFile");
            //String c_img1 = moveFile(imgEle1);
           // if(StringUtil.isNotEmpty(c_img1)){
            //    XmlDocPkgUtil.setChildText(userData, "portrait", c_img1);
           // }
            
            String[] userids = uptUser(userData);   
            String usercode = userids[0];
            String userid = userids[1];
            
            if("0".equals(usercode))
            {
                //usercode
                xmlDocUtil.writeErrorMsg("10640", "用户名重复");
                return;
            }
            else if("-1".equals(usercode))
            {
                //idnumber
                xmlDocUtil.writeErrorMsg("10641", "身份证号重复");
                return;
            }
            
            Element oldUserRec = getUserById(userid);
            String oldDeptid = oldUserRec.getChildText("deptid");
            String headmasterId = oldUserRec.getChildText("id");
            pDao.beginTransaction();
            //修改学生信息
            Element infoRec = ConfigDocument.createRecordElement("headmaster","headmaster_leader_info");
            XmlDocPkgUtil.copyValues(reqData,infoRec,0,true);
            boolean chg = false;
            

                XmlDocPkgUtil.setChildText(infoRec, "username", userData.getChildTextTrim("username"));
                XmlDocPkgUtil.setChildText(infoRec, "usersex", userData.getChildTextTrim("gender"));
                XmlDocPkgUtil.setChildText(infoRec, "idnumber", userData.getChildTextTrim("idnumber"));
                XmlDocPkgUtil.setChildText(infoRec, "mobile", userData.getChildTextTrim("mobile"));
                XmlDocPkgUtil.setChildText(infoRec, "address", userData.getChildTextTrim("address"));
                XmlDocPkgUtil.setChildText(infoRec, "deptid", deptid);
                XmlDocPkgUtil.setChildText(infoRec, "modify_by", curUserid);
                XmlDocPkgUtil.setChildText(infoRec, "modify_date",  DatetimeUtil.getNow(null));
                pDao.updateOneRecord(infoRec);

            String[] flds = {"userid","usercode","deptid","id"};
            Element data = XmlDocPkgUtil.createMetaData(flds);
            data.addContent(XmlDocPkgUtil.createRecord(flds, new String[]{userid,usercode,deptid,headmasterId}));
            
            xmlDocUtil.setResult("0");
            xmlDocUtil.getResponse().addContent(data);
            xmlDocUtil.writeHintMsg("10645", "修改领导信息成功");
        }
        catch(Exception ex)
        {
            pDao.rollBack();
            ex.printStackTrace();log4j.logError(ex);
            xmlDocUtil.writeErrorMsg("10646", "修改领导信息失败");
        }
        finally
        {
            pDao.releaseConnection();
        }          
    }
    
    @Override
    protected Element getUserById(String userid)
        throws Exception
    {
        String sql = "select a.*,b.id,b.deptid from pcmc_user a,headmaster_leader_info b where a.userid=b.userid and a.userid=?";
        ArrayList<Object> bvals = new ArrayList<Object>();
        bvals.add(userid);
        return DaoUtil.getOneRecord(sql, bvals);
    }

    /**
	 * 
	 * @Title: findMaster 
	 * @Description: TODO
	 * @return: void
	 */
	private void findLeader() {
	    Element dataElement = xmlDocUtil.getRequestData();
        String username = dataElement.getChildText("username");
        String usercode = dataElement.getChildText("usercode");
        String idnumber = dataElement.getChildText("idnumber");

        PlatformDao dao = null;
        try {
            dao = new PlatformDao();
            ArrayList<String> bvals = new ArrayList<String>();
            StringBuffer sqlBuf = new StringBuffer("");

            sqlBuf.append(" SELECT *");
            sqlBuf.append(" FROM headmaster_leader_info where 1=1 and valid = '1'");
            
            if (StringUtil.isNotEmpty(username)) {
                sqlBuf.append(" and username like ?");
                bvals.add("%"+username+"%");
            }
            
            if (StringUtil.isNotEmpty(usercode)) {
                sqlBuf.append(" and usercode like ?");
                bvals.add("%"+usercode+"%");
            }
            
            if (StringUtil.isNotEmpty(idnumber)) {
                sqlBuf.append(" and idnumber = ?");
                bvals.add(idnumber);
            }
         

            dao.setSql(sqlBuf.toString());
            dao.setBindValues(bvals);
            Element resultElement = dao.executeQuerySql(xmlDocUtil.getPageSize(), xmlDocUtil.getPageNo());
            xmlDocUtil.getResponse().addContent(resultElement);
            xmlDocUtil.setResult("0");
        } catch (Exception e) {
            e.printStackTrace();
            xmlDocUtil.setResult("-1");
        } finally {
            dao.releaseConnection();
        }
    }

    /**
	 * 校长注册
	 *
	 */
	public void addLeader(){
        Element session = xmlDocUtil.getSession();
        String curUserid = session.getChildText("userid");
        String curUsercode = session.getChildText("usercode");
        String curDeptid = session.getChildText("deptid");
        Element reqData = xmlDocUtil.getRequestData();
		PlatformDao pdao = new PlatformDao();
		try
		{	
			pdao.beginTransaction();
			String userpwd = reqData.getChildText("userpwd");//用户登录密码
			if(StringUtil.isEmpty(userpwd)){
				userpwd ="123456";
			}
			String deptid = reqData.getChildText("deptid");	
			Element userData = (Element)reqData.clone();
			XmlDocPkgUtil.setChildText(userData, "usertype", LeaderAction.LEADER_TYPE);
			String[] userids = addUser(userData, curUserid, curUsercode,userpwd);
			
			String userid = userids[1];
			String usercode = userids[0];
			if("0".equals(usercode))
			{
				//usercode
				xmlDocUtil.writeErrorMsg("10640", "用户名重复");
				return;
			}
			else if("-1".equals(usercode))
			{
				//idnumber
				xmlDocUtil.writeErrorMsg("10641", "身份证号重复");
				return;
			}
			if(StringUtil.isNotEmpty(deptid)){
				Element userDeptReC = ConfigDocument.createRecordElement("pcmc","pcmc_user_dept");
				XmlDocPkgUtil.setChildText(userDeptReC, "deptid", deptid);
				XmlDocPkgUtil.setChildText(userDeptReC, "userid", userid);
				XmlDocPkgUtil.setChildText(userDeptReC, "state", "1");
				XmlDocPkgUtil.setChildText(userDeptReC, "indate", DatetimeUtil.getNow(null));
				pdao.insertOneRecord(userDeptReC);				
			}
			
			Element headmasterRecord = ConfigDocument.createRecordElement("headmaster","headmaster_leader_info");
			XmlDocPkgUtil.copyValues(reqData,headmasterRecord,0,true);
			XmlDocPkgUtil.setChildText(headmasterRecord, "userid", userid);
			XmlDocPkgUtil.setChildText(headmasterRecord, "createdate", DatetimeUtil.getNow(null));
			XmlDocPkgUtil.setChildText(headmasterRecord, "create_by", userid);
			 XmlDocPkgUtil.setChildText(headmasterRecord, "valid", "1");
			pdao.insertOneRecordSeqPk(headmasterRecord);
		    
			
			pdao.commitTransaction();
			xmlDocUtil.setResult("0");
			xmlDocUtil.writeHintMsg("10642", "领导注册成功");			
		}
		catch(Exception ex)
		{
			pdao.rollBack();
			ex.printStackTrace();
			log4j.logError(ex);
			xmlDocUtil.writeErrorMsg("10644", "领导注册失败");
		}
		finally
		{
			pdao.releaseConnection();
		}		
	}
}
