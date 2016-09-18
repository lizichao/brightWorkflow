package cn.brightcom.system.pcmc.sm;

import java.util.List;

import org.jdom.Document;
import org.jdom.Element;

import cn.brightcom.jraf.db.DBOprProxy;
import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.util.Constants;
import cn.brightcom.jraf.util.DatetimeUtil;
import cn.brightcom.jraf.util.JDomUtil;
import cn.brightcom.jraf.util.Log;
import cn.brightcom.jraf.util.XmlDocPkgUtil;


public class PendJobBean
{
	private XmlDocPkgUtil xmlDocUtil = null;
    
    private static Log log4j = new Log(PendJobBean.class.getName());
    
    public Document doPost(Document xmlDoc)
    {
    	xmlDocUtil = new XmlDocPkgUtil(xmlDoc);
    	String action = xmlDocUtil.getAction();
    	
    	if("deletePendjob".equals(action))
        {
        	return deletePendjob(xmlDoc);
        }
        if("addPendjob".equals(action))
        {
        	return addPendjob(xmlDoc);
        }
        if("updatePendjob".equals(action))
        {
        	return updatePendjob(xmlDoc);
        }
        if("setJobStatus".equals(action))
        {
        	return setJobStatus(xmlDoc);
        }
        if("browPendjob".equals(action))
        {
        	return browPendjob(xmlDoc);				//查看待办事宜
        }
        
        if ("getPendJobListFirst".equals(action))
        {
            return getPendJobListFirst(xmlDoc);
        }
        if ("getPendjobList".equals(action))
        {
            return getPendjobList(xmlDoc);                   //获取根机构明细
        }
        if("getPendjobListById".equals(action))
        {
        	return getPendjobListById(xmlDoc);
        }
        if("getPendjobListByUser".equals(action))
        {
        	return getPendjobListByUser(xmlDoc);
        }
        if("getAllPendjobByUser".equals(action))
        {
        	return getAllPendjobByUser(xmlDoc);
        }
    	return xmlDoc;
    }
    
    private org.jdom.Document browPendjob(Document xmlDoc)
    {
        Element dataElement = xmlDoc.getRootElement().getChild("Request").getChild("Data");
        String pendjobid = dataElement.getChildText("pendjobid");
        StringBuffer sql = new StringBuffer();
        PlatformDao dao = new PlatformDao() ;
        try
        {
        	sql.append("update pcmc_pendjob set isvisited='1' where pendjobid =");
        	sql.append(pendjobid);
        	dao.setSql(sql.toString()) ;
            dao.executeTransactionSql();
             xmlDoc.getRootElement().getChild("Response").getAttribute("result").setValue("0");
        }
        catch (java.sql.SQLException e)
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
        return xmlDoc ;
    }

    private org.jdom.Document updatePendjob(Document xmlDoc)
    {
    	String createuser = xmlDoc.getRootElement().getChild("Session").getChildTextTrim("userid");
        Element dataElement = xmlDoc.getRootElement().getChild("Request").getChild("Data");
        String  pendjobid = dataElement.getChildTextTrim("pendjobid");
        String  userid = dataElement.getChildTextTrim("userid");
        String  title  = dataElement.getChildText("title");
        String  url    = dataElement.getChildText("url");
        String  sendtime = dataElement.getChildText("sendtime");
        StringBuffer sql = new StringBuffer();
        PlatformDao dao = new PlatformDao() ;

          sql.append("update pcmc_pendjob set userid ="+userid+" ,title ='"+title+"'"+" ,sendtime = to_date('"+sendtime+"','yyyy-mm-dd'), isvisited = '0'");
          if(url!=null&&!url.equals(""))
          {
        	  sql.append(" ,url= '"+url+"'");
          }
          sql.append(" where pendjobid = "+pendjobid+" and createuser = "+createuser);
          System.out.println(sql.toString());
          try
          {
              dao.setSql(sql.toString());
              dao.executeTransactionSql();
              xmlDoc.getRootElement().getChild("Response").getAttribute("result").setValue("0");
              xmlDoc.getRootElement().getChild("Response").getChild("Hint").addContent((new Element("Msg")).setText("修改待办事宜成功！"));
          }
          catch (java.sql.SQLException e)
          {
              e.printStackTrace();
              xmlDoc.getRootElement().getChild("Response").getAttribute("result").setValue(Constants.SYSTEM_ERROR_CODE) ;
              log4j.logError(e.getMessage()) ;
              log4j.logInfo(JDomUtil.toXML(xmlDoc, "gb2312", true)) ;
          }
          finally
          {
              dao.releaseConnection();
          }
        return xmlDoc ;
    }

    private org.jdom.Document addPendjob(Document xmlDoc)
    {
    	String createuser = xmlDoc.getRootElement().getChild("Session").getChildTextTrim("userid");
        Element dataElement = xmlDoc.getRootElement().getChild("Request").getChild("Data");
        String  userid = dataElement.getChildTextTrim("userid");
        String  title  = dataElement.getChildText("title");
        String  url    = dataElement.getChildText("url");
        String  senddate = dataElement.getChildText("sendtime");
        StringBuffer sql = new StringBuffer();
        PlatformDao dao = new PlatformDao() ;
        	long pendjobid = DBOprProxy.getNextSequenceNumber("pcmc_pendjob");
          sql.append("insert into pcmc_pendjob(pendjobid,userid,createuser,title,sendtime,isvisited");
          if(url!=null&&!url.equals(""))
          {
        	sql.append(",url");
         }
          sql.append(") values (");
          sql.append(pendjobid+","+userid+","+createuser+",'"+title+"',TO_DATE('"+senddate+"','yyyy-mm-dd'),'0'");
          if(url!=null&&!url.equals(""))
          {
          sql.append(",'"+url+"'");
          }
          sql.append(")");
          System.out.println(sql.toString());
          try
          {
              dao.setSql(sql.toString());
              dao.executeTransactionSql();
              xmlDoc.getRootElement().getChild("Response").getAttribute("result").setValue("0");
          }
          catch (java.sql.SQLException e)
          {
              e.printStackTrace();
              xmlDoc.getRootElement().getChild("Response").getAttribute("result").setValue(Constants.SYSTEM_ERROR_CODE) ;
              log4j.logError(e.getMessage()) ;
              log4j.logInfo(JDomUtil.toXML(xmlDoc, "gb2312", true)) ;
          }
          finally
          {
              dao.releaseConnection();
          }
        return xmlDoc ;
    }

    private org.jdom.Document deletePendjob(Document xmlDoc)
    {
        Element dataElement = xmlDoc.getRootElement().getChild("Request").getChild("Data");
        List pendjobList = dataElement.getChildren("pendjobid");
        StringBuffer sql = new StringBuffer();
        PlatformDao dao = new PlatformDao() ;
        try
        {
        	sql.append("delete from pcmc_pendjob where pendjobid =");
        	sql.append(((Element)(pendjobList.get(0))).getText());
        	for(int i=1;i<pendjobList.size();i++)
             {
        		 sql.append(" or pendjobid = "+((Element)(pendjobList.get(i))).getText());
        	}
        	dao.setSql(sql.toString()) ;
            dao.executeTransactionSql();
            dao.commitTransaction();
            xmlDoc.getRootElement().getChild("Response").getAttribute("result").setValue("0");
        }
        catch (java.sql.SQLException e)
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
        return xmlDoc ;
    }
    
    private org.jdom.Document setJobStatus(Document xmlDoc)
    {
        Element dataElement = xmlDoc.getRootElement().getChild("Request").getChild("Data");
        String pendjobid = dataElement.getChildTextTrim("pendjobid");
        PlatformDao dao = new PlatformDao();
        StringBuffer sql = new StringBuffer();
        String receivetime = DatetimeUtil.getNow().toString();
        try
        {
            sql.append(" update pcmc_pendjob set isvisited='1' ");
            sql.append("  where pendjobid = " + pendjobid);
            dao.setSql(sql.toString());
            dao.executeTransactionSql();
            xmlDoc.getRootElement().getChild("Response").getAttribute("result").setValue("0");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            xmlDoc.getRootElement().getChild("Response").getAttribute("result").setValue(Constants.SYSTEM_ERROR_CODE);
        }
        finally
        {
            dao.releaseConnection();
        }
        return xmlDoc;
    }
    
    private org.jdom.Document getAllPendjobByUser(Document xmlDoc)
    {
        String createuser = xmlDoc.getRootElement().getChild("Session").getChildTextTrim("userid");;
        String pendjobid = xmlDoc.getRootElement().getChild("Request").getChild("Data").getChildTextTrim("pendjobid");
        Element pageElement = xmlDoc.getRootElement().getChild("Request").getChild("PageInfo") ;
        String pageNo = pageElement.getChildTextTrim("PageNo") ;
        String pageSize = pageElement.getChildTextTrim("PageSize") ;
       PlatformDao dao = new PlatformDao() ;
        try
        {
            //取主表结果
            StringBuffer sql = new StringBuffer("select a.*,b.username from pcmc_pendjob a ,PCMC_USER b ");
            sql.append("where a.userid = b.userid and a.createuser ="+createuser) ;
             sql.append(" order by isvisited,sendtime desc");
            System.out.println("sql:"+sql.toString());
            dao.setSql(sql.toString()) ;
            Element resultData = dao.executeQuerySql(Integer.parseInt(pageSize), Integer.parseInt(pageNo)) ;
            xmlDoc.getRootElement().getChild("Response").addContent(resultData) ;
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
        return xmlDoc ;
    }
    /**
     * 根据创建用户得到待办事宜列表
     * */
    private org.jdom.Document getPendjobListByUser(Document xmlDoc)
    {
        String createuser = xmlDoc.getRootElement().getChild("Session").getChildTextTrim("userid");;
        Element pageElement = xmlDoc.getRootElement().getChild("Request").getChild("PageInfo") ;
        String pageNo = pageElement.getChildTextTrim("PageNo") ;
        String pageSize = pageElement.getChildTextTrim("PageSize") ;
        String username = xmlDoc.getRootElement().getChild("Request").getChild("Data").getChildTextTrim("username");
        String sendtime = xmlDoc.getRootElement().getChild("Request").getChild("Data").getChildTextTrim("sendtime");
        PlatformDao dao = new PlatformDao() ;
        try
        {
            //取主表结果
            StringBuffer sql = new StringBuffer("select a.*,b.username from pcmc_pendjob a ,PCMC_USER b ");
            sql.append("where a.userid = b.userid and (a.createuser ="+createuser) ;
            sql.append(" or a.userid="+createuser+")");
            if(username!=null&&!username.equals(""))
            {
            	sql.append(" and b.username like '%"+username+"%'");
            }
            if(sendtime!=null&&!sendtime.equals(""))
            {
            	sql.append(" and to_char(a.sendtime,'yyyy-mm-dd') = "+"'"+sendtime+"'");
            }
            sql.append(" order by isvisited,sendtime desc");
            dao.setSql(sql.toString()) ;
            Element resultData = dao.executeQuerySql(Integer.parseInt(pageSize), Integer.parseInt(pageNo)) ;
            xmlDoc.getRootElement().getChild("Response").addContent(resultData) ;
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
        return xmlDoc ;
    }

    /**
     * 根据待办事宜流水号得到待办事宜
     * */
    private org.jdom.Document getPendjobListById(Document xmlDoc)
    {
        String pendjobid = xmlDoc.getRootElement().getChild("Request").getChild("Data").getChildTextTrim("pendjobid");
        PlatformDao dao = new PlatformDao() ;
        try
        {
            //取主表结果
            StringBuffer sql = new StringBuffer("select a.*,b.username,c.username as createname from pcmc_pendjob a ,PCMC_USER b ,PCMC_USER C");
            sql.append(" where a.userid = b.userid and c.userid = a.createuser") ;
            sql.append(" and a.pendjobid="+pendjobid);
            dao.setSql(sql.toString()) ;
            Element resultData = dao.executeQuerySql(-1, 1) ;
            xmlDoc.getRootElement().getChild("Response").addContent(resultData) ;
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
        return xmlDoc ;
    }

    /**
     * 根据接受人得到待办事宜列表
     * */
    private org.jdom.Document getPendjobList(Document xmlDoc)
    {
    	System.out.println("进入getPendjobList");
        String userid = xmlDoc.getRootElement().getChild("Session").getChildTextTrim("userid");
        Element pageElement = xmlDoc.getRootElement().getChild("Request").getChild("PageInfo") ;
        String pageNo = pageElement.getChildTextTrim("PageNo") ;
        String pageSize = pageElement.getChildTextTrim("PageSize") ;
        PlatformDao dao = new PlatformDao() ;
        try
        {
            //取主表结果
            StringBuffer sql = new StringBuffer("select a.*,b.username from pcmc_pendjob a ,PCMC_USER b ");
            sql.append("where a.createuser = b.userid and a.userid ="+userid) ;
            sql.append(" and a.isvisited=0 ");
            sql.append(" order by isvisited,sendtime desc");
            System.out.println("sql:"+sql.toString());
            dao.setSql(sql.toString()) ;
            Element resultData = dao.executeQuerySql(Integer.parseInt(pageSize), Integer.parseInt(pageNo)) ;
            xmlDoc.getRootElement().getChild("Response").addContent(resultData) ;
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
        return xmlDoc ;
    }

    private org.jdom.Document getPendJobListFirst(Document xmlDoc)
    {
        String userid = xmlDoc.getRootElement().getChild("Session").getChildTextTrim("userid");
        StringBuffer sql = new StringBuffer() ;
        sql.append(" select * from pcmc_pendjob ") ;
        sql.append("  where isvisited = '0' ");
        sql.append("    and userid = " + userid);
        sql.append(" and rownum<=5");
        sql.append(" order by sendtime desc ");
        PlatformDao dao = new PlatformDao() ;
        try
        {
            dao.setSql(sql.toString()) ;
            System.out.println("sql:"+sql.toString());
            Element resultElement = dao.executeQuerySql(-1,1) ;
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
        return xmlDoc ;
    }
}
