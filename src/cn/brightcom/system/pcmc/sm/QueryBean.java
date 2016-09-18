package cn.brightcom.system.pcmc.sm ;

import org.jdom.* ;

import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.util.Constants;
import cn.brightcom.jraf.util.JDomUtil;
import cn.brightcom.jraf.util.Log;
import cn.brightcom.jraf.util.XmlDocPkgUtil;

import java.util.List ;


public class QueryBean
{
    private XmlDocPkgUtil xmlDocUtil = null;
    private static Log log4j = new Log(QueryBean.class.getName());
    
    public Document doPost(Document xmlDoc)
    {
        xmlDocUtil = new XmlDocPkgUtil(xmlDoc);
        String action = xmlDocUtil.getAction();
        if ("getInfoList".equals(action))
        {
            getInfoList(xmlDoc) ;
        }
        else if ("getInfoKindList".equals(action))
        {
            getInfoKindList(xmlDoc) ;
        }
        else if ("getInfoKindDetail".equals(action))
        {
            getInfoKindDetail(xmlDoc) ;
        }
        else if ("getParamMasterDetail".equals(action))
        {
            getParamMasterDetail(xmlDoc) ;
        }
        else if ("getMessageList".equals(action))
        {
            getMessageList(xmlDoc);
        }
        return xmlDoc ;
    }

    private final void getInfoList(Document xmlDoc)
    {
        Element pageElement = xmlDoc.getRootElement().getChild("Request").getChild("PageInfo") ;
        String pageNo = pageElement.getChildTextTrim("PageNo") ;
        String pageSize = pageElement.getChildTextTrim("PageSize") ;
        Element dataElement = xmlDoc.getRootElement().getChild("Request").getChild("Data") ;
        String infotype = dataElement.getChildTextTrim("infotype") ;
        String title = dataElement.getChildTextTrim("title") ;

        StringBuffer sql = new StringBuffer(" select a.*,b.username from pcmc_info a,pcmc_user b ") ;
        sql.append(" where a.createuser = b.userid ") ;
        if ( (infotype != null) && (infotype.length() > 0))
        {
            sql.append(" and a.infotype = '" + infotype + "'") ;
        }
        if (title != null)
        {
            sql.append(" and a.title like '%" + title + "%'") ;
        }
        Element resultElement = null ;
        PlatformDao dao = new PlatformDao() ;
        try
        {
            //取主表结果
            dao.setSql(sql.toString()) ;
            resultElement = dao.executeQuerySql(Integer.parseInt(pageSize), Integer.parseInt(pageNo)) ;
            xmlDoc.getRootElement().getChild("Response").addContent(resultElement) ;
            xmlDoc.getRootElement().getChild("Response").getAttribute("result").setValue("0") ;

        }
        catch (Exception e)
        {
            e.printStackTrace() ;
            xmlDoc.getRootElement().getChild("Response").getAttribute("result").setValue(Constants.SYSTEM_ERROR_CODE) ;
            log4j.logError(e.getMessage()) ;
            log4j.logInfo(JDomUtil.toXML(xmlDoc, "gb2312", true)) ;
        }
        finally
        {
            dao.releaseConnection();
        }
    }

    private final void getInfoKindList(Document xmlDoc)
    {
        Element pageElement = xmlDoc.getRootElement().getChild("Request").getChild("PageInfo") ;
        String pageNo = pageElement.getChildTextTrim("PageNo") ;
        String pageSize = pageElement.getChildTextTrim("PageSize") ;
        Element dataElement = xmlDoc.getRootElement().getChild("Request").getChild("Data") ;
        String kindname = dataElement.getChildTextTrim("kindname") ;

        StringBuffer sql = new StringBuffer(" select a.*,b.username from pcmc_info_kind a,pcmc_user b ") ;
        sql.append(" where a.createuser = b.userid ") ;
        if (kindname != null && !kindname.equals(""))
        {
            sql.append(" and a.kindname like '%" + kindname + "%'") ;
        }
        sql.append(" order by a.sortno");
        Element resultElement = null ;
        PlatformDao dao = new PlatformDao() ;
        try
        {
            dao.setSql(sql.toString()) ;
            resultElement = dao.executeQuerySql(Integer.parseInt(pageSize), Integer.parseInt(pageNo)) ;
            xmlDoc.getRootElement().getChild("Response").addContent(resultElement) ;
            xmlDoc.getRootElement().getChild("Response").getAttribute("result").setValue("0") ;

        }
        catch (Exception e)
        {
            e.printStackTrace() ;
            xmlDoc.getRootElement().getChild("Response").getAttribute("result").setValue(Constants.SYSTEM_ERROR_CODE) ;
            log4j.logError(e.getMessage()) ;
            log4j.logInfo(JDomUtil.toXML(xmlDoc, "gb2312", true)) ;
        }
        finally
        {
            dao.releaseConnection();
        }
    }

    private final void getInfoKindDetail(Document xmlDoc)
    {
        Element dataElement = xmlDoc.getRootElement().getChild("Request").getChild("Data") ;
        String kindid = dataElement.getChildTextTrim("kindid") ;

        StringBuffer sql = new StringBuffer(" select a.*,b.username from pcmc_info_kind a,pcmc_user b ") ;
        sql.append(" where a.createuser = b.userid ") ;
        sql.append(" and a.kindid="+kindid);
        Element resultElement = null ;
        PlatformDao dao = new PlatformDao() ;
        try
        {
            dao.setSql(sql.toString()) ;
            resultElement = dao.executeQuerySql(-1, -1) ;
            sql.setLength(0);
            sql.append("select a.*,b.username from pcmc_info a,pcmc_user b");
            sql.append(" where a.createuser=b.userid");
            sql.append(" and a.kindid="+kindid+" order by a.createtime desc");
            dao.setSql(sql.toString());
            Element tmp = dao.executeQuerySql(-1,1);
            resultElement.getChild("Record").addContent(tmp);
            xmlDoc.getRootElement().getChild("Response").addContent(resultElement) ;
            xmlDoc.getRootElement().getChild("Response").getAttribute("result").setValue("0") ;

        }
        catch (Exception e)
        {
            e.printStackTrace() ;
            xmlDoc.getRootElement().getChild("Response").getAttribute("result").setValue(Constants.SYSTEM_ERROR_CODE) ;
            log4j.logError(e.getMessage()) ;
            log4j.logInfo(JDomUtil.toXML(xmlDoc, "gb2312", true)) ;
        }
        finally
        {
            dao.releaseConnection();
        }
    }

    private final void getParamMasterDetail(Document xmlDoc)
    {
        PlatformDao dao = new PlatformDao() ;
        Element dataElement = xmlDoc.getRootElement().getChild("Request").getChild("Data") ;
        String paramid = dataElement.getChildTextTrim("paramid") ;

        Element masterData ;
        Element detailData ;
        try
        {
            //取主表结果
            StringBuffer sql = new StringBuffer("select * from param_master ") ;
            sql.append(" where paramid = " + paramid) ;
            dao.setSql(sql.toString()) ;
            masterData = dao.executeQuerySql( -1, 1) ;
            sql.setLength(0) ;
            sql.append(" select * from param_detail ") ;
            sql.append(" where paramid = " + paramid) ;
            dao.setSql(sql.toString()) ;
            detailData = dao.executeQuerySql( -1, 1) ;
            List roleRecords = detailData.getChildren("Record") ;
            if (roleRecords != null)
            {
                for (int i = 0 ; i < roleRecords.size() ; i++)
                {
                    Element roleRecord = (Element) roleRecords.get(i) ;
                    masterData.getChild("Record").addContent( (Element) roleRecord.clone()) ;
                }
            }
            masterData.removeChild("PageInfo") ;
            masterData.addContent( (Element) detailData.getChild("PageInfo").clone()) ;
            xmlDoc.getRootElement().getChild("Response").addContent(masterData) ;
            xmlDoc.getRootElement().getChild("Response").getAttribute("result").setValue("0") ;

        }
        catch (Exception e)
        {
            e.printStackTrace() ;
            xmlDoc.getRootElement().getChild("Response").getAttribute("result").setValue(Constants.SYSTEM_ERROR_CODE) ;
            log4j.logError(e.getMessage()) ;
            log4j.logInfo(JDomUtil.toXML(xmlDoc, "gb2312", true)) ;

        }
        finally
        {
            dao.releaseConnection();
        }
    }
    
    private final void getMessageList(Document xmlDoc)
    {
        Element pageElement = xmlDoc.getRootElement().getChild("Request").getChild("PageInfo") ;
        String pageNo = pageElement.getChildTextTrim("PageNo") ;
        String pageSize = pageElement.getChildTextTrim("PageSize") ;

        String userid = xmlDoc.getRootElement().getChild("Session").getChildTextTrim("userid");
        String isread = xmlDoc.getRootElement().getChild("Request").getChild("Data").getChildTextTrim("isread");
        StringBuffer sql = new StringBuffer() ;
        sql.append(" select a.*,b.isread,b.sendgroupid,c.username from pcmc_message a,pcmc_sendgroup b, pcmc_user c ") ;
        sql.append(" where a.messageid = b.messageid ");
        sql.append(" and a.createuser = c.userid ");
        sql.append(" and b.targetuser = " + userid);
        sql.append(" and b.isread = '" + isread + "'");
        sql.append(" order by a.createtime desc ");
        PlatformDao dao = new PlatformDao() ;
        try
        {
            dao.setSql(sql.toString()) ;
            Element resultElement = dao.executeQuerySql(Integer.parseInt(pageSize), Integer.parseInt(pageNo)) ;
            xmlDoc.getRootElement().getChild("Response").addContent(resultElement) ;
            xmlDoc.getRootElement().getChild("Response").getAttribute("result").setValue("0") ;
        }
        catch (Exception e)
        {
            e.printStackTrace() ;
            xmlDoc.getRootElement().getChild("Response").getAttribute("result").setValue(Constants.SYSTEM_ERROR_CODE) ;
        }
        finally
        {
            dao.releaseConnection();
        }
    }



    

}
