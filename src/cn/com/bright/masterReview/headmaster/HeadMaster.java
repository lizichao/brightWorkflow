package cn.com.bright.masterReview.headmaster;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;

import cn.brightcom.jraf.context.ApplicationContext;
import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.util.ConfigDocument;
import cn.brightcom.jraf.util.DaoUtil;
import cn.brightcom.jraf.util.DatetimeUtil;
import cn.brightcom.jraf.util.JDomUtil;
import cn.brightcom.jraf.util.Log;
import cn.brightcom.jraf.util.StringUtil;
import cn.brightcom.jraf.util.XmlDocPkgUtil;
import cn.brightcom.system.pcmc.util.Des;
import cn.com.bright.masterReview.base.UserManage;
import cn.com.bright.masterReview.util.MasterReviewConstant;
import cn.com.bright.workflow.core.spring.ApplicationContextHelper;

/**
 * 
 * @ClassName: Register 
 * @Description: TODO
 * @author: lzc
 * @date: 2016年4月28日 上午10:46:40
 */
public class HeadMaster extends UserManage{
    
    
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
		if ("addMaster".equals(action)){
		    addMaster();
		}else if ("findMaster".equals(action)){
		    findMaster();
        }else if ("updateMaster".equals(action)){
            updateMaster();
        }else if ("deleteMaster".equals(action)){
            deleteMaster();
        }else if ("findMasterApplyStatus".equals(action)){
            findMasterApplyStatus();
        }else if ("findMasterBaseInfo".equals(action)){
            findMasterBaseInfo();
        }else if ("findSaveDraftTaskId".equals(action)){
            findSaveDraftTaskId();
        }if ("registerMaster".equals(action)){
            registerMaster();
        }
		return xmlDoc;
	}


   


    private void registerMaster() {
        Element reqData = xmlDocUtil.getRequestData();
        PlatformDao pdao = new PlatformDao();
        try
        {   
            //验证验证码
            String phone_valid = reqData.getChildText("phone_valid");
            if (StringUtil.isNotEmpty(phone_valid)) {
                String valid = ApplicationContext.getRequest().getSession().getAttribute(reqData.getChildText("mobile")).toString();
                if (phone_valid.equals(ApplicationContext.getRequest().getSession().getAttribute(reqData.getChildText("mobile")))) {
                    
                } else {
                    xmlDocUtil.writeErrorMsg("10642", "验证码不匹配!");
                    return ;
                }
            }
            
            pdao.beginTransaction();
            String userpwd = reqData.getChildText("userpwd");//用户登录密码
            userpwd = new Des().strDec(userpwd);
            if(StringUtil.isEmpty(userpwd)){
                userpwd ="123456";
            }
            
            String deptid = reqData.getChildText("deptid"); 
            Element userData = (Element)reqData.clone();
            XmlDocPkgUtil.setChildText(userData, "usertype", HeadMaster.MASTER_TYPE);
            String[] userids = addUser(userData, "1", "admin",userpwd);
            
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
            
            Element headmasterRecord = ConfigDocument.createRecordElement("headmaster","headmaster_base_info");
            XmlDocPkgUtil.copyValues(reqData,headmasterRecord,0,true);
            XmlDocPkgUtil.setChildText(headmasterRecord, "userid", userid);
            XmlDocPkgUtil.setChildText(headmasterRecord, "createdate", DatetimeUtil.getNow(null));
            XmlDocPkgUtil.setChildText(headmasterRecord, "create_by", userid);
            XmlDocPkgUtil.setChildText(headmasterRecord, "valid", "1");
            pdao.insertOneRecordSeqPk(headmasterRecord);
            
            
            pdao.commitTransaction();
            xmlDocUtil.setResult("0");
            xmlDocUtil.writeHintMsg("10642", "校长注册成功");         
        }
        catch(Exception ex)
        {
            pdao.rollBack();
            ex.printStackTrace();
            log4j.logError(ex);
            xmlDocUtil.writeErrorMsg("10644", "校长注册失败");
        }
        finally
        {
            pdao.releaseConnection();
        }           
    }





    /**
	 * 
	 * @Title: findMaster 
	 * @Description: TODO
	 * @return: void
	 */
	private void findMaster() {
	    Element dataElement = xmlDocUtil.getRequestData();
	    String username = dataElement.getChildText("username");
	    String usercode = dataElement.getChildText("usercode");
	    String idnumber = dataElement.getChildText("idnumber");

        PlatformDao dao = null;
        try {
            dao = new PlatformDao();
            ArrayList<String> bvals = new ArrayList<String>();
            StringBuffer sqlBuf = new StringBuffer("");

            sqlBuf.append("SELECT t.*, a.deptname,c.deptname as districtname");
            sqlBuf.append(" FROM headmaster_base_info t ");
            sqlBuf.append(" LEFT JOIN pcmc_dept c ON t.districtid = c.deptid ");
            sqlBuf.append(" INNER JOIN pcmc_dept a ON t.deptid = a.deptid ");
            sqlBuf.append(" INNER JOIN pcmc_user_dept b ON t.userid = b.userid ");
            sqlBuf.append(" where valid = '1' ");
            
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
            sqlBuf.append(" order by t.create_date desc ");
            
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
	 * 校长后台注册
	 *
	 */
	public void addMaster(){
		Element reqData = xmlDocUtil.getRequestData();
		PlatformDao pdao = new PlatformDao();
		try
		{	
			pdao.beginTransaction();
			String userpwd = reqData.getChildText("userpwd");//用户登录密码
			if(StringUtil.isEmpty(userpwd)){
				userpwd ="123456";
			}
			 //userpwd = new Des().strDec(userpwd);
			String deptid = reqData.getChildText("deptid");	
			Element userData = (Element)reqData.clone();
			XmlDocPkgUtil.setChildText(userData, "usertype", HeadMaster.MASTER_TYPE);
			String[] userids = addUser(userData, "1", "admin",userpwd);
			
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
		//	Element headmasterRecord = ConfigDocument.createRecordElement("workflow","workflow_delegate_task");
		      // XmlDocPkgUtil.setChildText(headmasterRecord, "delegate_user", "333");
		       
		       
//		       Element headmasterRecord = ConfigDocument.createRecordElement("workflow","workflow_attachment");
//		       XmlDocPkgUtil.setChildText(headmasterRecord, "file_path", "44");
//               XmlDocPkgUtil.setChildText(headmasterRecord, "file_name",  "44");
//               XmlDocPkgUtil.setChildText(headmasterRecord, "file_size", "44");
//               XmlDocPkgUtil.setChildText(headmasterRecord, "file_type", "44");
               
		      // XmlDocPkgUtil.setChildText(headmasterRecord, "file_name", "333");
			
			Element headmasterRecord = ConfigDocument.createRecordElement("headmaster","headmaster_base_info");
			XmlDocPkgUtil.copyValues(reqData,headmasterRecord,0,true);
			XmlDocPkgUtil.setChildText(headmasterRecord, "userid", userid);
			XmlDocPkgUtil.setChildText(headmasterRecord, "createdate", DatetimeUtil.getNow(null));
			XmlDocPkgUtil.setChildText(headmasterRecord, "create_by", userid);
			XmlDocPkgUtil.setChildText(headmasterRecord, "valid", "1");
			pdao.insertOneRecordSeqPk(headmasterRecord);
		    
			
			pdao.commitTransaction();
			xmlDocUtil.setResult("0");
			xmlDocUtil.writeHintMsg("10642", "校长注册成功");			
		}
		catch(Exception ex)
		{
			pdao.rollBack();
			ex.printStackTrace();
			log4j.logError(ex);
			xmlDocUtil.writeErrorMsg("10644", "校长注册失败");
		}
		finally
		{
			pdao.releaseConnection();
		}		
	}
	
	
	private void deleteMaster() {
	    Element dataElement = xmlDocUtil.getRequestData();
        List idList = dataElement.getChildren("id");
        PlatformDao dao = new PlatformDao();
        StringBuffer sql = new StringBuffer("");
        ArrayList bvals = new ArrayList();
        try {
            dao.beginTransaction();
            if (idList.size() > 0) {
                sql.append("UPDATE headmaster_base_info");
                sql.append(" SET valid = '0'");
                sql.append(" WHERE userid = ?");
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
            xmlDocUtil.writeHintMsg("10603","删除校长成功");
        }
        catch (Exception e)
        {
            e.printStackTrace() ;
            dao.rollBack();
            log4j.logError("[删除校长发生错误]"+e.getMessage()) ;
            log4j.logInfo(JDomUtil.toXML(xmlDocUtil.getXmlDoc(), "GBK", true)) ;
            xmlDocUtil.writeErrorMsg("10604","删除校长失败");
        }
        finally
        {
            dao.releaseConnection();
        }
    }

    private void updateMaster() {
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
            Element infoRec = ConfigDocument.createRecordElement("headmaster","headmaster_base_info");
            XmlDocPkgUtil.copyValues(reqData,infoRec,0,true);
            boolean chg = false;
            
//            if(!deptid.equals(oldDeptid))
//            {
//                if(PcmcUtil.isSysManager(curUsercode))
//                {
//                    chg = true;
//                }
//                else
//                {
//                    throw new Exception("所在学校不正确");
//                }
//            }
//            else if(!curDeptid.equals(oldDeptid))
//            {
//                deptid = curDeptid;
//                chg = true;
//            }
//            if(chg)
//            {
//                String sql = "update headmaster_base_info set state=?,transferdt=? where studinfoid=?";
//                ArrayList<Object> bvals = new ArrayList<Object>();
//                bvals.add("0");
//                bvals.add(DatetimeUtil.getNow());
//                bvals.add(headmasterId);
//                pDao.setSql(sql);
//                pDao.setBindValues(bvals);
//                pDao.executeTransactionSql();
//                
//                infoRec.removeChild("studinfoid");
//                XmlDocPkgUtil.setChildText(infoRec, "deptid", deptid);
//                XmlDocPkgUtil.setChildText(infoRec, "state", "1");
//                headmasterId = (String)pDao.insertOneRecordSeqPk(infoRec);
//            }
//            else
//            {
                XmlDocPkgUtil.setChildText(infoRec, "username", userData.getChildTextTrim("username"));
                XmlDocPkgUtil.setChildText(infoRec, "usersex", userData.getChildTextTrim("gender"));
                XmlDocPkgUtil.setChildText(infoRec, "idnumber", userData.getChildTextTrim("idnumber"));
                XmlDocPkgUtil.setChildText(infoRec, "mobile", userData.getChildTextTrim("mobile"));
                XmlDocPkgUtil.setChildText(infoRec, "address", userData.getChildTextTrim("address"));
                XmlDocPkgUtil.setChildText(infoRec, "deptid", deptid);
                XmlDocPkgUtil.setChildText(infoRec, "modify_by", curUserid);
                XmlDocPkgUtil.setChildText(infoRec, "modify_date",  DatetimeUtil.getNow(null));
                pDao.updateOneRecord(infoRec);
          //  }
                pDao.commitTransaction();
            String[] flds = {"userid","usercode","deptid","id"};
            Element data = XmlDocPkgUtil.createMetaData(flds);
            data.addContent(XmlDocPkgUtil.createRecord(flds, new String[]{userid,usercode,deptid,headmasterId}));
            
            xmlDocUtil.setResult("0");
            xmlDocUtil.getResponse().addContent(data);
            xmlDocUtil.writeHintMsg("10645", "修改校长信息成功");
        }
        catch(Exception ex)
        {
            pDao.rollBack();
            ex.printStackTrace();log4j.logError(ex);
            xmlDocUtil.writeErrorMsg("10646", "修改校长信息失败");
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
        String sql = "select a.*,b.id,b.deptid from pcmc_user a,headmaster_base_info b where a.userid=b.userid and a.userid=?";
        ArrayList<Object> bvals = new ArrayList<Object>();
        bvals.add(userid);
        return DaoUtil.getOneRecord(sql, bvals);
    }
    

    /**
     * 查询校长的申请状态，是没申请，还是提交了部分资料，还是已经提交了申请被驳回
     * @Title: findMasterApplyStatus 
     * @Description: TODO
     * @return: void
     */
    public void findMasterApplyStatus() {
        Element dataElement = xmlDocUtil.getRequestData();
        String headerMasterId = dataElement.getChildText("headerMasterId");//获取当前登录校长id

        PlatformDao dao = null;
        try {
            dao = new PlatformDao();
            ArrayList<String> bvals = new ArrayList<String>();
            StringBuffer sqlBuf = new StringBuffer("");

            sqlBuf.append("SELECT t.*");
            sqlBuf.append(" FROM headmaster t where 1=1 and headerMasterId = ? order by create_date desc ");
            
            bvals.add(headerMasterId);

            dao.setSql(sqlBuf.toString());
            dao.setBindValues(bvals);
            Element resultElement = dao.executeQuerySql(-1, 1);
            String applyStatus = "";
            String processInstanceId = "";
            String taskId = "";
            //如果在申请主表里面没有记录，说明还没申请，如果为0，说明被区级审核人驳回，为1说明正在申请中或者申请结束
            if (resultElement.getChildren("Record").size() > 0) {
                String apply_result = resultElement.getChild("Record").getChildTextTrim("apply_status");
                 processInstanceId = resultElement.getChild("Record").getChildTextTrim("processinstanceid");
                if(MasterReviewConstant.APPLY_REFUSE.equals(apply_result)){
                    applyStatus = "0";
                    taskId = findRefillTaskId(processInstanceId,headerMasterId);
                }else{
                    applyStatus = "1";
                }
            }
            
            System.out.println("---------------------"+headerMasterId);
            String[] flds = {"applyStatus","processInstanceId","taskId"};
            Element data = XmlDocPkgUtil.createMetaData(flds);
            data.addContent(XmlDocPkgUtil.createRecord(flds, new String[]{applyStatus,processInstanceId,taskId}));
            
            xmlDocUtil.setResult("0");
            xmlDocUtil.getResponse().addContent(data);
            //xmlDocUtil.writeHintMsg("10645", "修改校长信息成功");
//            
//            xmlDocUtil.getResponse().addContent(resultElement);
//            xmlDocUtil.setResult("0");
        } catch (Exception e) {
            e.printStackTrace();
            xmlDocUtil.setResult("-1");
        } finally {
            dao.releaseConnection();
        }
    }
    
    private String findRefillTaskId(String processInstanceId,String headerMasterId ){
        StringBuffer sql = new StringBuffer("");
        sql.append(" SELECT DISTINCT RES.ID_ AS taskId ");
        sql.append(" FROM ACT_RU_TASK RES ");
        sql.append(" LEFT JOIN ACT_RU_IDENTITYLINK I ON I.TASK_ID_ = RES.ID_ ");
        sql.append("LEFT JOIN act_ru_execution RUE ");
        sql.append(" ON RES.PROC_INST_ID_ = RUE.PROC_INST_ID_ ");
        sql.append("  WHERE     RES.SUSPENSION_STATE_ = 1 ");
        sql.append(" AND (   RES.ASSIGNEE_ = ? ");
        sql.append("   OR (RES.ASSIGNEE_ IS NULL AND (I.USER_ID_ = ?))) ");
        sql.append("AND RUE.NAME_ IS NOT NULL ");
        sql.append(" AND RES.TASK_DEF_KEY_ = 'usertask5' ");
        sql.append(" AND RES.PROC_INST_ID_ = ? ");
        sql.append(" order by RES.CREATE_TIME_ desc  ");
             
        List<Map<String, Object>> list  = ApplicationContextHelper.getJdbcTemplate().queryForList(sql.toString(),
           headerMasterId, headerMasterId,processInstanceId);
         return list.get(0).get("taskId").toString();
    }
    
    
    private void findSaveDraftTaskId() {
        Element session = xmlDocUtil.getSession();
        String curUserid = session.getChildText("userid");

        Element dataElement = xmlDocUtil.getRequestData();
        String headerMasterId = dataElement.getChildText("headerMasterId");// 获取当前登录校长id
        String processInstanceId = dataElement.getChildText("processInstanceId");// 获取当前登录校长id

        String taskId = findRefillTaskId(processInstanceId, curUserid);

        String[] flds = { "taskId" };
        Element data = XmlDocPkgUtil.createMetaData(flds);
        data.addContent(XmlDocPkgUtil.createRecord(flds, new String[] { taskId }));

        xmlDocUtil.setResult("0");
        xmlDocUtil.getResponse().addContent(data);
    }
    


    private void findMasterBaseInfo() {
        Element dataElement = xmlDocUtil.getRequestData();
        String userid = dataElement.getChildText("userid");

        PlatformDao dao = null;
        try {
            dao = new PlatformDao();
            ArrayList<String> bvals = new ArrayList<String>();
            StringBuffer sqlBuf = new StringBuffer("");

            sqlBuf.append(" SELECT t.*, a.deptname,");
            sqlBuf.append(" c.deptname as districtname,");
            sqlBuf.append("b.attachment_id AS headImgId,");
            sqlBuf.append("b.file_path AS headImgPath");
            sqlBuf.append(" FROM headmaster_base_info t");
            sqlBuf.append("  LEFT JOIN pcmc_dept a ON t.deptid = a.deptid");
            sqlBuf.append("  LEFT JOIN pcmc_dept c ON t.districtid = c.deptid");
            sqlBuf.append(" LEFT JOIN workflow_attachment b");
            sqlBuf.append(" ON t.person_img_attachId = b.attachment_id");
            sqlBuf.append(" WHERE userid = ? AND valid = '1'");
           
            
            bvals.add(userid);
        

            dao.setSql(sqlBuf.toString());
            dao.setBindValues(bvals);
            Element resultElement = dao.executeQuerySql(-1,1);
            xmlDocUtil.getResponse().addContent(resultElement);
            xmlDocUtil.setResult("0");
        } catch (Exception e) {
            e.printStackTrace();
            xmlDocUtil.setResult("-1");
        } finally {
            dao.releaseConnection();
        }
    }
    
    
}
