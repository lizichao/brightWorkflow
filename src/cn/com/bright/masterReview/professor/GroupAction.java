package cn.com.bright.masterReview.professor;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;

import cn.brightcom.jraf.db.DBOprProxy;
import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.util.ConfigDocument;
import cn.brightcom.jraf.util.DatetimeUtil;
import cn.brightcom.jraf.util.JDomUtil;
import cn.brightcom.jraf.util.Log;
import cn.brightcom.jraf.util.StringUtil;
import cn.brightcom.jraf.util.XmlDocPkgUtil;
import cn.com.bright.masterReview.base.UserManage;
import cn.com.bright.masterReview.util.GroupUtils;

public class GroupAction extends UserManage {
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
        if ("getGroupList".equals(action)){
            getGroupList();
        }else if ("deleteGroup".equals(action)) {
            deleteGroup();
        }else if ("addGroup".equals(action)) {
            addGroup();
        }else if ("findProfessors".equals(action)) {
            findProfessors();
        }else if ("findHeadMasters".equals(action)) {
            findHeadMasters();
        }else if ("addHeadMasterList".equals(action)) {
            addHeadMasterList();
        }else if ("addProfessorList".equals(action)) {
            addProfessorList();
        }else if ("deleteGroupProfessor".equals(action)) {
            deleteGroupProfessor();
        }else if ("deleteGroupMaster".equals(action)) {
            deleteGroupMaster();
        }
        return xmlDoc;
    }

    private void deleteGroupMaster() {
        Element dataElement = xmlDocUtil.getRequestData();
        List deviceIdList = dataElement.getChildren("user_group_id");
        
        PlatformDao dao = new PlatformDao();
        StringBuffer sql = new StringBuffer("");
        ArrayList bvals = new ArrayList();
        try {
            dao.beginTransaction();
            if (deviceIdList.size() > 0) {
                sql.append("UPDATE headmaster_user_group");
                sql.append(" SET valid = '0'");
                sql.append(" WHERE user_group_id = ?");
                 dao.setSql(sql.toString());
                for (int i = 0; i < deviceIdList.size(); i++) {
                    Element deviceId = (Element)deviceIdList.get(i);
                    bvals.clear();
                    bvals.add(deviceId.getText());
                    dao.addBatch(bvals);
                }
                dao.executeBatch();
            }
            dao.commitTransaction();
            xmlDocUtil.setResult("0");
            xmlDocUtil.writeHintMsg("10603","删除群组校长成功");
        }
        catch (Exception e)
        {
            e.printStackTrace() ;
            dao.rollBack();
            log4j.logError("[删除群组校长发生错误]"+e.getMessage()) ;
            log4j.logInfo(JDomUtil.toXML(xmlDocUtil.getXmlDoc(), "GBK", true)) ;
            xmlDocUtil.writeErrorMsg("10604","删除群组校长失败");
        }
        finally
        {
            dao.releaseConnection();
        }           
    }

    private void deleteGroupProfessor() {
        Element dataElement = xmlDocUtil.getRequestData();
        List deviceIdList = dataElement.getChildren("user_group_id");
        
        PlatformDao dao = new PlatformDao();
        StringBuffer sql = new StringBuffer("");
        ArrayList bvals = new ArrayList();
        try {
            dao.beginTransaction();
            if (deviceIdList.size() > 0) {
                sql.append("UPDATE headmaster_user_group");
                sql.append(" SET valid = '0'");
                sql.append(" WHERE user_group_id = ?");
                 dao.setSql(sql.toString());
                for (int i = 0; i < deviceIdList.size(); i++) {
                    Element deviceId = (Element)deviceIdList.get(i);
                    bvals.clear();
                    bvals.add(deviceId.getText());
                    dao.addBatch(bvals);
                }
                dao.executeBatch();
            }
            dao.commitTransaction();
            xmlDocUtil.setResult("0");
            xmlDocUtil.writeHintMsg("10603","删除群组专家成功");
        }
        catch (Exception e)
        {
            e.printStackTrace() ;
            dao.rollBack();
            log4j.logError("[删除群组专家发生错误]"+e.getMessage()) ;
            log4j.logInfo(JDomUtil.toXML(xmlDocUtil.getXmlDoc(), "GBK", true)) ;
            xmlDocUtil.writeErrorMsg("10604","删除群组专家失败");
        }
        finally
        {
            dao.releaseConnection();
        }             
    }

    private void addProfessorList() {
        Element session = xmlDocUtil.getSession();
        String curUserid = session.getChildText("userid");
        String curUsercode = session.getChildText("usercode");
        String curDeptid = session.getChildText("deptid");
        
        Element reqData = xmlDocUtil.getRequestData();
        String group_id = reqData.getChildTextTrim("group_id");
        String deptId = reqData.getChildTextTrim("deptId");
        List<Element> addRoomDeviceIds = reqData.getChildren("addProfessorIds");
        
        
        PlatformDao dao = null;
        StringBuffer sql = new StringBuffer();
        ArrayList<String> bvals = new ArrayList<String>();
        //ArrayList bvals = new ArrayList();
        try {
            dao = new PlatformDao();
            
            sql.setLength(0);
            bvals.clear();
            
            sql.append("    INSERT INTO headmaster_user_group(");
                sql.append("  user_group_id");
                sql.append(" ,group_id");
               sql.append("  ,user_id");
               sql.append("  ,user_type");
               sql.append("   ,create_by");
               sql.append("  ,create_date");
               sql.append("  ,valid");
               sql.append(" ) VALUES (");
                 sql.append(" ?");
                 sql.append(" ,?");
                 sql.append(" ,?");
                 sql.append(" ,?");
                 sql.append(" ,?");
                 sql.append(" ,?");
                 sql.append(" ,?");
                 sql.append("    )");
            
            dao.beginTransaction();
            dao.setSql(sql.toString());
            for (Element addRoomDeviceId : addRoomDeviceIds) {
                String userid = addRoomDeviceId.getText();
                
                
                ArrayList bvalss = new ArrayList();
                String seq = (String) DBOprProxy.getNextSequence("uuid", null);
                bvalss.add(seq);
                bvalss.add(group_id);
                bvalss.add(userid);
                bvalss.add(UserManage.PROFESSOR_TYPE);
                bvalss.add(curUserid);
                bvalss.add( DatetimeUtil.getNow(""));
                bvalss.add("1");
                dao.addBatch(bvalss);
            }
            dao.executeBatch();
            
            
            
            dao.commitTransaction();
            xmlDocUtil.setResult("0");
            //xmlDocUtil.writeHintMsg("10609", "增加教室设备成功");
        } catch (Exception e) {
            dao.rollBack();
            e.printStackTrace();
            log4j.logError("[ 增加专家发生异常.]" + e.getMessage());
            xmlDocUtil.setResult("-1");
            xmlDocUtil.writeErrorMsg("10402", " 增加专家异常");
        } finally {
            dao.releaseConnection();
        }         
    }

    private void addHeadMasterList() {
        Element session = xmlDocUtil.getSession();
        String curUserid = session.getChildText("userid");
        String curUsercode = session.getChildText("usercode");
        String curDeptid = session.getChildText("deptid");
        
        Element reqData = xmlDocUtil.getRequestData();
        String group_id = reqData.getChildTextTrim("group_id");
        String deptId = reqData.getChildTextTrim("deptId");
        List<Element> addRoomDeviceIds = reqData.getChildren("addHeadMasterIds");
        
        
        PlatformDao dao = null;
        StringBuffer sql = new StringBuffer();
        ArrayList<String> bvals = new ArrayList<String>();
        //ArrayList bvals = new ArrayList();
        try {
            dao = new PlatformDao();
            
            sql.setLength(0);
            bvals.clear();
            
            sql.append("    INSERT INTO headmaster_user_group(");
                sql.append("  user_group_id");
                sql.append(" ,group_id");
               sql.append("  ,user_id");
               sql.append("  ,user_type");
               sql.append("   ,create_by");
               sql.append("  ,create_date");
               sql.append("  ,valid");
               sql.append(" ) VALUES (");
                 sql.append(" ?");
                 sql.append(" ,?");
                 sql.append(" ,?");
                 sql.append(" ,?");
                 sql.append(" ,?");
                 sql.append(" ,?");
                 sql.append(" ,?");
                 sql.append("    )");
            
            dao.beginTransaction();
            dao.setSql(sql.toString());
            for (Element addRoomDeviceId : addRoomDeviceIds) {
                String userid = addRoomDeviceId.getText();
                
                
                ArrayList bvalss = new ArrayList();
                String seq = (String) DBOprProxy.getNextSequence("uuid", null);
                bvalss.add(seq);
                bvalss.add(group_id);
                bvalss.add(userid);
                bvalss.add(UserManage.MASTER_TYPE);
                bvalss.add(curUserid);
                bvalss.add( DatetimeUtil.getNow(""));
                bvalss.add( "1");
                dao.addBatch(bvalss);
            }
            dao.executeBatch();
            
            
            
            dao.commitTransaction();
            xmlDocUtil.setResult("0");
            //xmlDocUtil.writeHintMsg("10609", "增加教室设备成功");
        } catch (Exception e) {
            dao.rollBack();
            e.printStackTrace();
            log4j.logError("[ 增加校长发生异常.]" + e.getMessage());
            xmlDocUtil.setResult("-1");
            xmlDocUtil.writeErrorMsg("10402", " 增加校长异常");
        } finally {
            dao.releaseConnection();
        }        
    }

    /**
     * 查找某个专家组下面的校长列表
     * @Title: findHeadMasters 
     * @Description: TODO
     * @return: void
     */
    private void findHeadMasters() {
        Element reqData = xmlDocUtil.getRequestData();
        String group_id = reqData.getChildTextTrim("group_id");
        //String fristDeptId = reqData.getChildTextTrim("fristDeptId");
        String group_pid = reqData.getChildTextTrim("group_pid");
        
        PlatformDao dao = null;
        try {
            dao = new PlatformDao();
            ArrayList<String> bvals = new ArrayList<String>();
            StringBuffer sqlBuf = new StringBuffer("");
            
            
            sqlBuf.append(" SELECT b.*,t.user_group_id");
            sqlBuf.append(" FROM headmaster_user_group t");
            sqlBuf.append("  LEFT JOIN headmaster_group a ON t.group_id = a.group_id");
            sqlBuf.append("  LEFT JOIN headmaster_base_info b");
            sqlBuf.append("  ON t.user_id = b.userid    ");
            sqlBuf.append("  where t.group_id = ?  and  t.user_type = ?   ");
            sqlBuf.append("  and t.valid = '1'   ");

            bvals.add(group_id);
            bvals.add(UserManage.MASTER_TYPE);
            

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
     * 查找某个专家组下面的专家列表
     * @Title: findProfessors 
     * @Description: TODO
     * @return: void
     */
    private void findProfessors() {
        Element reqData = xmlDocUtil.getRequestData();
        String group_id = reqData.getChildTextTrim("group_id");
        //String fristDeptId = reqData.getChildTextTrim("fristDeptId");
        String group_pid = reqData.getChildTextTrim("group_pid");
        
        PlatformDao dao = null;
        try {
            dao = new PlatformDao();
            ArrayList<String> bvals = new ArrayList<String>();
            StringBuffer sqlBuf = new StringBuffer("");
            
            
            sqlBuf.append(" SELECT b.*,t.user_group_id");
            sqlBuf.append(" FROM headmaster_user_group t");
            sqlBuf.append("  LEFT JOIN headmaster_group a ON t.group_id = a.group_id");
            sqlBuf.append("  LEFT JOIN headmaster_professor_info b");
            sqlBuf.append("  ON t.user_id = b.userid ");
            sqlBuf.append("  where t.group_id = ?  and t.user_type = ?   ");
            sqlBuf.append("  and t.valid = '1'   ");

            bvals.add(group_id);
            bvals.add(UserManage.PROFESSOR_TYPE);
            

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

    private void addGroup() {
        PlatformDao dao = null;
        Element reqData = xmlDocUtil.getRequestData();
        String roomCode = reqData.getChildTextTrim("room_code");
        try
        {
            dao = new PlatformDao();
            dao.beginTransaction();
            StringBuffer sql = new StringBuffer();
//            sql.append("select room_code from iot_room_info where room_code = '");
//            sql.append(roomCode+"'");
//            dao.setSql(sql.toString());
//            Element tmp = dao.executeQuerySql( -1, 1);
//            if (tmp.getChildren("Record").size()>0) {
//              xmlDocUtil.setResult("-1");
//              xmlDocUtil.writeErrorMsg("10403","教室编号已经存在");
//              return;
//            }
            Element ele = ConfigDocument.createRecordElement("headmaster","headmaster_group");
            XmlDocPkgUtil.copyValues(reqData,ele,0);
            XmlDocPkgUtil.setChildText(ele, "valid", "1");
           // ele.getChild("levels").setText(levelid);
         //   ele.removeChild("changetype");
           // ele.removeChild("changedate");
           // ele.removeChild("cdeptid");
          //System.out.println(JDomUtil.toXML(ele,"gb2312",true));
            List pkList = dao.insertOneRecord(ele);
            
            dao.commitTransaction();
            
            Element resData = XmlDocPkgUtil.createMetaData(new String[]{"group_id"});
            Object group_id = ((Object[])pkList.get(0))[1];
            //Element data = new Element("Data");
            Element record = new Element("Record");
            resData.addContent(record);
            XmlDocPkgUtil.setChildText(record, "group_id", ""+group_id);
            xmlDocUtil.getResponse().addContent(resData);
            xmlDocUtil.writeHintMsg("10401","新增教室成功");
            xmlDocUtil.setResult("0");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            dao.rollBack();
            log4j.logError("[新增教室发生异常.]"+e.getMessage());
            xmlDocUtil.setResult("-1");
            xmlDocUtil.writeErrorMsg("10402","新增教室异常");
         }        
    }

    private void deleteGroup() {
        Element dataElement = xmlDocUtil.getRequestData();
        String group_id = dataElement.getChildTextTrim("group_id");
        PlatformDao dao = new PlatformDao();
        StringBuffer sql = new StringBuffer("");
        try {
            List<String> results = GroupUtils.findRooms(group_id) ;
            results.add(group_id);

            dao.beginTransaction();

            sql.append("UPDATE headmaster_group");
            sql.append(" SET valid = '0' ");
            sql.append(" WHERE group_id = ?");
            dao.setSql(sql.toString());
            for (String childRoomId : results) {
                ArrayList bvals = new ArrayList();
                bvals.add(childRoomId);
                dao.addBatch(bvals);
            }
            dao.executeBatch();
            
              
            dao.commitTransaction();
            xmlDocUtil.setResult("0");
            xmlDocUtil.writeHintMsg("10603", "删除群组信息成功");
        } catch (Exception e) {
            e.printStackTrace();
            dao.rollBack();
            log4j.logError("[删除群组信息失败]" + e.getMessage());
            log4j.logInfo(JDomUtil.toXML(xmlDocUtil.getXmlDoc(), "GBK", true));
            xmlDocUtil.writeErrorMsg("10604", "删除群组信息失败");
        } finally {
            dao.releaseConnection();
        }        
    }

    private void getGroupList() {
        Element session = xmlDocUtil.getSession();
        String curUserid = session.getChildText("userid");
        String curUsercode = session.getChildText("usercode");
        String curDeptid = session.getChildText("deptid");
        
        Element reqData = xmlDocUtil.getRequestData();
        String group_id = reqData.getChildTextTrim("group_id");
        //String fristDeptId = reqData.getChildTextTrim("fristDeptId");
        String group_pid = reqData.getChildTextTrim("group_pid");
        if(!StringUtil.isEmpty(group_pid) && group_pid.equals("-1")){
            group_pid = curDeptid;
        }
      
//        if (StringUtil.isEmpty(fristDeptId)) {
//            xmlDocUtil.writeErrorMsg("10107", "请选择机构，刷新！");
//            xmlDocUtil.setResult("-1");
//            return;
//        }
        
        PlatformDao dao = null;
        try {
            dao = new PlatformDao();
            ArrayList<String> bvals = new ArrayList<String>();
            StringBuffer sqlBuf = new StringBuffer("");
            sqlBuf.append("SELECT a.*,");
            sqlBuf.append(" (SELECT count(*)");
            sqlBuf.append(" FROM headmaster_group");
            sqlBuf.append(" WHERE group_pid = a.group_id AND group_id <> group_pid and valid='1')");
            sqlBuf.append(" AS childNum");
            sqlBuf.append(" FROM headmaster_group a");
            sqlBuf.append(" WHERE group_id <> group_pid  AND group_pid = ?");
            sqlBuf.append(" and valid='1'");
            sqlBuf.append(" ORDER BY group_id");

            bvals.add(group_pid);
            

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
    
   
}
