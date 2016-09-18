package cn.com.bright.masterReview.professor;

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
import cn.com.bright.masterReview.headmaster.HeadMaster;

public class ProfessorAction extends UserManage {
    private Log log4j = new Log(this.getClass().toString());
    
    private XmlDocPkgUtil xmlDocUtil = null;

    /**
     * 动态委派入口
     * @param request xmlDoc 
     * @return response xmlDoc
     */ 
    public Document doPost(Document xmlDoc) {
        xmlDocUtil = new XmlDocPkgUtil(xmlDoc);
        String action = xmlDocUtil.getAction();
        if ("addProfessor".equals(action)){
            addProfessor();
        }else if ("findProfessor".equals(action)){
            findProfessor();
        }else if ("updateProfessor".equals(action)){
            updateProfessor();
        }else if ("deleteProfessor".equals(action)){
            deleteProfessor();
        }else if ("findNoApproveData".equals(action)){
            findNoApproveData();
        }else if ("findApproveData".equals(action)){
            findApproveData();
        }
        return xmlDoc;
    }
    
    private void findApproveData() {
        Element dataElement = xmlDocUtil.getRequestData();
        Element session = xmlDocUtil.getSession();
        String curUserId = session.getChildText("userid");
        
        String username = dataElement.getChildText("username");
        String usercode = dataElement.getChildText("usercode");
        String idnumber = dataElement.getChildText("idnumber");
        
        String ispositive = dataElement.getChildText("ispositive");

        PlatformDao dao = null;
        try {
            dao = new PlatformDao();
            ArrayList<String> bvals = new ArrayList<String>();
            StringBuffer sqlBuf = new StringBuffer("");
            
            sqlBuf.append(" SELECT DISTINCT h.headerMasterName,h.school_name,t.PROC_INST_ID_ as processInstanceId");
            sqlBuf.append(" FROM act_hi_taskinst t");
            sqlBuf.append("  LEFT JOIN ACT_HI_PROCINST a ON t.PROC_INST_ID_ = a.PROC_INST_ID_");
            sqlBuf.append(" INNER JOIN headmaster h ON a.BUSINESS_KEY_ = h.id");
            sqlBuf.append("       INNER JOIN headmaster_base_info hbase on h.headerMasterId = hbase.userid");
            sqlBuf.append(" WHERE t.ASSIGNEE_ = ? AND t.END_TIME_ IS NOT NULL");

            bvals.add(curUserId);
            if (StringUtil.isNotEmpty(username)) {
                sqlBuf.append(" and h.headerMasterName like ?");
                bvals.add("%"+username+"%");
            }
            
            
            if (StringUtil.isNotEmpty(ispositive)) {
                sqlBuf.append(" and hbase.ispositive = ?");
                bvals.add(ispositive);
            }

            dao.setSql(sqlBuf.toString());
            dao.setBindValues(bvals);
            Element resultElement = dao.executeQuerySql(-1, 1);
            xmlDocUtil.getResponse().addContent(resultElement);
            xmlDocUtil.setResult("0");
        } catch (Exception e) {
            e.printStackTrace();
            xmlDocUtil.setResult("-1");
        } finally {
            dao.releaseConnection();
        }        
    }

    private void findNoApproveData() {
        Element dataElement = xmlDocUtil.getRequestData();
        Element session = xmlDocUtil.getSession();
        String curUserId = session.getChildText("userid");
        
        String username = dataElement.getChildText("username");
        String usercode = dataElement.getChildText("usercode");
        String idnumber = dataElement.getChildText("idnumber");
        
        String ispositive = dataElement.getChildText("ispositive");
        

        PlatformDao dao = null;
        try {
            dao = new PlatformDao();
            ArrayList<String> bvals = new ArrayList<String>();
            StringBuffer sqlBuf = new StringBuffer("");
            
            sqlBuf.append(" SELECT DISTINCT h.headerMasterName,h.school_name,RES.*");
            sqlBuf.append(" FROM ACT_RU_TASK RES");
            sqlBuf.append("  LEFT JOIN ACT_RU_IDENTITYLINK I ON I.TASK_ID_ = RES.ID_");
            sqlBuf.append("  LEFT JOIN act_ru_execution RUE");
            sqlBuf.append("  ON RES.PROC_INST_ID_ = RUE.PROC_INST_ID_");
            sqlBuf.append(" INNER JOIN headmaster h ON RUE.BUSINESS_KEY_ = h.id");
            sqlBuf.append("       INNER JOIN headmaster_base_info hbase on h.headerMasterId = hbase.userid");
            sqlBuf.append("   WHERE     RES.SUSPENSION_STATE_ = 1");
            sqlBuf.append("    AND (   RES.ASSIGNEE_ = ?");
            sqlBuf.append("  OR (RES.ASSIGNEE_ IS NULL AND (I.USER_ID_ =?)))");
            sqlBuf.append(" AND RUE.NAME_ IS NOT NULL");
          
                
            bvals.add(curUserId);
            bvals.add(curUserId);
            
            if (StringUtil.isNotEmpty(username)) {
                sqlBuf.append(" and h.headerMasterName like ?");
                bvals.add("%"+username+"%");
            }
            
            if (StringUtil.isNotEmpty(ispositive)) {
                sqlBuf.append(" and hbase.ispositive = ?");
                bvals.add(ispositive);
            }
            
            sqlBuf.append("  order by RES.CREATE_TIME_");
            

            dao.setSql(sqlBuf.toString());
            dao.setBindValues(bvals);
            Element resultElement = dao.executeQuerySql(-1, 1);
            xmlDocUtil.getResponse().addContent(resultElement);
            xmlDocUtil.setResult("0");
        } catch (Exception e) {
            e.printStackTrace();
            xmlDocUtil.setResult("-1");
        } finally {
            dao.releaseConnection();
        }        
    }

    private void deleteProfessor() {
        Element dataElement = xmlDocUtil.getRequestData();
        List idList = dataElement.getChildren("id");
        PlatformDao dao = new PlatformDao();
        StringBuffer sql = new StringBuffer("");
        ArrayList bvals = new ArrayList();
        try {
            dao.beginTransaction();
            if (idList.size() > 0) {
                sql.append("UPDATE headmaster_professor_info");
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
            xmlDocUtil.writeHintMsg("10603","删除专家成功");
        }
        catch (Exception e)
        {
            e.printStackTrace() ;
            dao.rollBack();
            log4j.logError("[删除专家发生错误]"+e.getMessage()) ;
            log4j.logInfo(JDomUtil.toXML(xmlDocUtil.getXmlDoc(), "GBK", true)) ;
            xmlDocUtil.writeErrorMsg("10604","删除专家失败");
        }
        finally
        {
            dao.releaseConnection();
        }        
    }

    private void updateProfessor() {
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
            Element infoRec = ConfigDocument.createRecordElement("headmaster","headmaster_professor_info");
            XmlDocPkgUtil.copyValues(reqData,infoRec,0,true);
            boolean chg = false;
            

            XmlDocPkgUtil.setChildText(infoRec, "username", userData.getChildTextTrim("username"));
            XmlDocPkgUtil.setChildText(infoRec, "usersex", userData.getChildTextTrim("gender"));
            XmlDocPkgUtil.setChildText(infoRec, "idnumber", userData.getChildTextTrim("idnumber"));
            XmlDocPkgUtil.setChildText(infoRec, "mobile", userData.getChildTextTrim("mobile"));
            XmlDocPkgUtil.setChildText(infoRec, "address", userData.getChildTextTrim("address"));
            XmlDocPkgUtil.setChildText(infoRec, "deptid", deptid);
            XmlDocPkgUtil.setChildText(infoRec, "modify_by", curUserid);
            XmlDocPkgUtil.setChildText(infoRec, "modify_date", DatetimeUtil.getNow(null));
            pDao.updateOneRecord(infoRec);

            String[] flds = {"userid","usercode","deptid","id"};
            Element data = XmlDocPkgUtil.createMetaData(flds);
            data.addContent(XmlDocPkgUtil.createRecord(flds, new String[]{userid,usercode,deptid,headmasterId}));
            
            xmlDocUtil.setResult("0");
            xmlDocUtil.getResponse().addContent(data);
            xmlDocUtil.writeHintMsg("10645", "修改专家信息成功");
        }
        catch(Exception ex)
        {
            pDao.rollBack();
            ex.printStackTrace();log4j.logError(ex);
            xmlDocUtil.writeErrorMsg("10646", "修改专家信息失败");
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
        String sql = "select a.*,b.id from pcmc_user a,headmaster_professor_info b where a.userid=b.userid and a.userid=?";
        ArrayList<Object> bvals = new ArrayList<Object>();
        bvals.add(userid);
        return DaoUtil.getOneRecord(sql, bvals);
    }

    private void findProfessor() {
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
            sqlBuf.append(" FROM headmaster_professor_info where 1=1 and valid = '1'");
            
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

    private void addProfessor() {
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
            XmlDocPkgUtil.setChildText(userData, "usertype", HeadMaster.PROFESSOR_TYPE);
            String[] userids = addUser(userData,curUserid, curUsercode,userpwd);
            
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
            
            Element headmasterRecord = ConfigDocument.createRecordElement("headmaster","headmaster_professor_info");
            XmlDocPkgUtil.copyValues(reqData,headmasterRecord,0,true);
            XmlDocPkgUtil.setChildText(headmasterRecord, "userid", userid);
            XmlDocPkgUtil.setChildText(headmasterRecord, "createdate", DatetimeUtil.getNow(null));
            XmlDocPkgUtil.setChildText(headmasterRecord, "create_by", curUserid);
            XmlDocPkgUtil.setChildText(headmasterRecord, "valid", "1");
            pdao.insertOneRecordSeqPk(headmasterRecord);
            
            
            pdao.commitTransaction();
            xmlDocUtil.setResult("0");
            xmlDocUtil.writeHintMsg("10642", "专家注册成功");         
        }
        catch(Exception ex)
        {
            pdao.rollBack();
            ex.printStackTrace();
            log4j.logError(ex);
            xmlDocUtil.writeErrorMsg("10644", "专家注册失败");
        }
        finally
        {
            pdao.releaseConnection();
        }           
    }
   
}
