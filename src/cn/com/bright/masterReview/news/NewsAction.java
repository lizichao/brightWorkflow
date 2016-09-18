package cn.com.bright.masterReview.news;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;

import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.util.ConfigDocument;
import cn.brightcom.jraf.util.DatetimeUtil;
import cn.brightcom.jraf.util.JDomUtil;
import cn.brightcom.jraf.util.Log;
import cn.brightcom.jraf.util.StringUtil;
import cn.brightcom.jraf.util.XmlDocPkgUtil;
import cn.com.bright.masterReview.base.UserManage;

public class NewsAction extends UserManage{
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
        if ("addNews".equals(action)){
            addNews();
        }else if ("findNews".equals(action)){
            findNews();
        }else if ("updateNews".equals(action)){
            updateNews();
        }else if ("deleteNews".equals(action)){
            deleteNews();
        }else if ("findSingleNews".equals(action)){
            findSingleNews();
        }else if ("findIndexNews".equals(action)){
            findIndexNews();
        }
        return xmlDoc;
    }

   

    private void findIndexNews() {
        Element dataElement = xmlDocUtil.getRequestData();
        String news_title = dataElement.getChildText("news_title");
        String news_content = dataElement.getChildText("news_content");

        PlatformDao dao = null;
        try {
            dao = new PlatformDao();
            ArrayList<String> bvals = new ArrayList<String>();
            StringBuffer sqlBuf = new StringBuffer("");

            sqlBuf.append(" SELECT *");
            sqlBuf.append(" FROM headmaster_news where 1=1 and valid = '1' order by create_date desc limit 0,5 ");
            
         
         
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



    private void findSingleNews() {
        Element dataElement = xmlDocUtil.getRequestData();
        String id = dataElement.getChildText("id");

        PlatformDao dao = null;
        try {
            dao = new PlatformDao();
            ArrayList<String> bvals = new ArrayList<String>();
            StringBuffer sqlBuf = new StringBuffer("");

            sqlBuf.append(" SELECT *");
            sqlBuf.append(" FROM headmaster_news where 1=1 and valid = '1'");
            
            if (StringUtil.isNotEmpty(id)) {
                sqlBuf.append(" and id = ?");
                bvals.add(id);
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



    private void deleteNews() {
        Element dataElement = xmlDocUtil.getRequestData();
        List idList = dataElement.getChildren("id");
        PlatformDao dao = new PlatformDao();
        StringBuffer sql = new StringBuffer("");
        ArrayList bvals = new ArrayList();
        try {
            dao.beginTransaction();
            if (idList.size() > 0) {
                sql.append("UPDATE headmaster_news");
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
              
            }
            dao.commitTransaction();
            xmlDocUtil.setResult("0");
            xmlDocUtil.writeHintMsg("10603","删除新闻成功");
        }
        catch (Exception e)
        {
            e.printStackTrace() ;
            dao.rollBack();
            log4j.logError("[删除新闻发生错误]"+e.getMessage()) ;
            log4j.logInfo(JDomUtil.toXML(xmlDocUtil.getXmlDoc(), "GBK", true)) ;
            xmlDocUtil.writeErrorMsg("10604","删除新闻失败");
        }
        finally
        {
            dao.releaseConnection();
        }        
    }

    private void updateNews() {
        Element reqData = xmlDocUtil.getRequestData();
        Element session = xmlDocUtil.getSession();
        String curUserid = session.getChildText("userid");
        String curUsercode = session.getChildText("usercode");
        String curDeptid = session.getChildText("deptid");
        
        String id =  reqData.getChildText("id");
        String news_title =  reqData.getChildText("news_title");
        String news_content =  reqData.getChildText("news_content");
        
        PlatformDao pDao = null;
        try
        {
            pDao = new PlatformDao();
            
            pDao.beginTransaction();
            //修改学生信息
            Element infoRec = ConfigDocument.createRecordElement("headmaster","headmaster_news");
           // XmlDocPkgUtil.copyValues(reqData,infoRec,0,true);
            

            XmlDocPkgUtil.setChildText(infoRec, "id", id);
            XmlDocPkgUtil.setChildText(infoRec, "news_title", news_title);
            XmlDocPkgUtil.setChildText(infoRec, "news_content", news_content);
            XmlDocPkgUtil.setChildText(infoRec, "valid", "1");
            XmlDocPkgUtil.setChildText(infoRec, "modify_by", curUserid);
            XmlDocPkgUtil.setChildText(infoRec, "modify_date", DatetimeUtil.getNow(null));
            pDao.updateOneRecord(infoRec);

//            String[] flds = {"userid","usercode","deptid","id"};
//            Element data = XmlDocPkgUtil.createMetaData(flds);
//            data.addContent(XmlDocPkgUtil.createRecord(flds, new String[]{userid,usercode,deptid,headmasterId}));
            
            xmlDocUtil.setResult("0");
         //   xmlDocUtil.getResponse().addContent(data);
            xmlDocUtil.writeHintMsg("10645", "修改区级干部信息成功");
        }
        catch(Exception ex)
        {
            pDao.rollBack();
            ex.printStackTrace();log4j.logError(ex);
            xmlDocUtil.writeErrorMsg("10646", "修改区级干部信息失败");
        }
        finally
        {
            pDao.releaseConnection();
        }        
    }
    

    private void findNews() {
        Element dataElement = xmlDocUtil.getRequestData();
        String news_title = dataElement.getChildText("news_title");
        String news_content = dataElement.getChildText("news_content");

        PlatformDao dao = null;
        try {
            dao = new PlatformDao();
            ArrayList<String> bvals = new ArrayList<String>();
            StringBuffer sqlBuf = new StringBuffer("");

            sqlBuf.append(" SELECT *");
            sqlBuf.append(" FROM headmaster_news where 1=1 and valid = '1'");
            
            if (StringUtil.isNotEmpty(news_title)) {
                sqlBuf.append(" and news_title like ?");
                bvals.add("%"+news_title+"%");
            }
            
            if (StringUtil.isNotEmpty(news_content)) {
                sqlBuf.append(" and news_content like ?");
                bvals.add("%"+news_content+"%");
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

    private void addNews() {
        Element session = xmlDocUtil.getSession();
        String curUserid = session.getChildText("userid");
        String curUsercode = session.getChildText("usercode");
        String curDeptid = session.getChildText("deptid");
        
        Element reqData = xmlDocUtil.getRequestData();
        PlatformDao pdao = new PlatformDao();
        try
        {   
            pdao.beginTransaction();
            
            Element headmasterRecord = ConfigDocument.createRecordElement("headmaster","headmaster_news");
            XmlDocPkgUtil.copyValues(reqData,headmasterRecord,0,true);
            XmlDocPkgUtil.setChildText(headmasterRecord, "createdate", DatetimeUtil.getNow(null));
            XmlDocPkgUtil.setChildText(headmasterRecord, "create_by", curUserid);
            XmlDocPkgUtil.setChildText(headmasterRecord, "valid", "1");
            pdao.insertOneRecordSeqPk(headmasterRecord);
            
            
            pdao.commitTransaction();
            xmlDocUtil.setResult("0");
            xmlDocUtil.writeHintMsg("10642", "新闻添加成功");         
        }
        catch(Exception ex)
        {
            pdao.rollBack();
            ex.printStackTrace();
            log4j.logError(ex);
            xmlDocUtil.writeErrorMsg("10644", "新闻添加失败");
        }
        finally
        {
            pdao.releaseConnection();
        }           
    }
}
