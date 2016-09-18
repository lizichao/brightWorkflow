package cn.brightcom.system.pcmc.pm;

import org.jdom.*;

import cn.brightcom.jraf.db.DBOprProxy;
import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.util.Constants;
import cn.brightcom.jraf.util.DatetimeUtil;
import cn.brightcom.jraf.util.JDomUtil;
import cn.brightcom.jraf.util.Log;

import java.util.List;

public class PmInfoDealBean
{
    private PlatformDao platformDao;
    public Document doPost(Document xmlDoc)
    {
        String action = xmlDoc.getRootElement().getChild("Action").getText();
        platformDao = new PlatformDao();
        //获取信息方法
        if("getInfomList".equals(action))
        {
            getInfomList(xmlDoc);
        }
        if("editInfo".equals(action))
        {
            insertInfomation(xmlDoc);
        }
        if("getInfomSingle".equals(action))
        {
            getInfomSingle(xmlDoc);
        }
        if("getMessageNum".equals(action))
        {
            getMessageNum(xmlDoc);
        }
        if("getNewMessageList".equals(action))
        {
            return getNewMessageList(xmlDoc);
        }
        return xmlDoc;
    }
    //获取信息
    private Document getInfomList(Document xmlDoc)
    {
        String size = xmlDoc.getRootElement().getChild("Request").getChild("Data").getChildTextTrim("size");
        StringBuffer sqlBuf = new StringBuffer("select kindid,kindname from pcmc_info_kind order by sortno ");
        try
        {
            platformDao.setSql(sqlBuf.toString());
            Element values = platformDao.executeQuerySql(-1,1);
            List valList = values.getChildren("Record");
            for (int i=0;i<valList.size();i++) {
                Element ele = (Element)valList.get(i);
                String kindid = ele.getChildText("kindid");
                sqlBuf.setLength(0);
                sqlBuf.append("select infoid,title,createtime from pcmc_info where kindid=" + kindid +
                              " order by createtime desc");
                platformDao.setSql(sqlBuf.toString());
                Element infos = platformDao.executeQuerySql(Integer.parseInt(size),1);
                ele.addContent(infos);
            }
            xmlDoc.getRootElement().getChild("Response").addContent(values);
            xmlDoc.getRootElement().getChild("Response").getAttribute("result").setValue("0");
        }
        catch(Exception e)
        {
            e.printStackTrace() ;
            xmlDoc.getRootElement().getChild("Response").getAttribute("result").setValue(Constants.SYSTEM_ERROR_CODE) ;
            Log log4j = new Log(this.getClass().toString()) ;
            log4j.logError(e.getMessage()) ;
            log4j.logInfo(JDomUtil.toXML(xmlDoc, "gb2312", true)) ;
        }
        finally
        {
            platformDao.releaseConnection();
        }
        return xmlDoc;
    }
    //插入一条记录
    private Document insertInfomation(Document xmlDoc)
    {
        StringBuffer sqlBuf = new StringBuffer();
        Element res = xmlDoc.getRootElement().getChild("Request").getChild("Data");
        long seq = 0;
        try
        {
            seq = DBOprProxy.getNextSequenceNumber("pcmc_info");
        }
        catch(Exception e)
        {
            return xmlDoc;
        }
        sqlBuf.append("insert into pcmc_info values(");
        sqlBuf.append(seq);
        sqlBuf.append(",'");
        sqlBuf.append(res.getChildText("infotype"));
        sqlBuf.append("','");
        sqlBuf.append(res.getChildText("title"));
        sqlBuf.append("','");
        sqlBuf.append(res.getChildText("content"));
        sqlBuf.append("',");
        sqlBuf.append(res.getChildText("createuser"));
        sqlBuf.append(",'");
        sqlBuf.append(DatetimeUtil.getNow());
        sqlBuf.append("')");
        try
        {
            platformDao.setSql(sqlBuf.toString());
            platformDao.executeTransactionSql();
            xmlDoc.getRootElement().getChild("Response").getAttribute("result").setValue("0");
            xmlDoc.getRootElement().getChild("Response").getChild("Hint").addContent(new Element("Msg").setText("新增数据成功！"));

        }catch(Exception e)
        {
            e.printStackTrace();
            xmlDoc.getRootElement().getChild("Response").getAttribute("result").setValue(Constants.SYSTEM_ERROR_CODE);
            xmlDoc.getRootElement().getChild("Response").getChild("Error").addContent(new Element("Msg").setText("新增数据失败！"));
        }
        finally
        {
            platformDao.releaseConnection();
        }
        return xmlDoc;
    }
    private Document getInfomSingle(Document xmlDoc)
    {
        Element dataElement = xmlDoc.getRootElement().getChild("Request").getChild("Data");
        String infoid = dataElement.getChildText("infoid");
        StringBuffer sql = new StringBuffer("select a.*,b.kindname,c.username from pcmc_info a,pcmc_info_kind b,pcmc_user c");
        sql.append(" where infoid = "+infoid+" and b.kindid=a.kindid");
        sql.append(" and c.userid=a.createuser");
        try
        {
            platformDao.setSql(sql.toString());
            Element values = platformDao.executeQuerySql(-1,1);
            xmlDoc.getRootElement().getChild("Response").addContent(values);
            xmlDoc.getRootElement().getChild("Response").getAttribute("result").setValue("0");
        }
        catch(Exception e)
        {
            e.printStackTrace() ;
            xmlDoc.getRootElement().getChild("Response").getAttribute("result").setValue(Constants.SYSTEM_ERROR_CODE) ;
            Log log4j = new Log(this.getClass().toString()) ;
            log4j.logError(e.getMessage()) ;
            log4j.logInfo(JDomUtil.toXML(xmlDoc, "gb2312", true)) ;
        }
        finally
        {
            platformDao.releaseConnection();
        }
        return xmlDoc;
    }
    private Document getMessageNum(Document xmlDoc)
    {
        StringBuffer sqlBuf = new StringBuffer("select count(*) as num from pcmc_sendgroup where targetuser = ");
        sqlBuf.append(xmlDoc.getRootElement().getChild("Session").getChildTextTrim("userid"));
        sqlBuf.append(" and isread = '");
        sqlBuf.append(xmlDoc.getRootElement().getChild("Request").getChild("Data").getChildTextTrim("isread"));
        sqlBuf.append("'");
        try
        {
            platformDao.setSql(sqlBuf.toString());
            Element values = platformDao.executeQuerySql(-1,1);
            xmlDoc.getRootElement().getChild("Response").addContent(values);
            xmlDoc.getRootElement().getChild("Response").getAttribute("result").setValue("0");
        }
        catch(Exception e)
        {
            xmlDoc.getRootElement().getChild("Response").getAttribute("result").setValue(Constants.SYSTEM_ERROR_CODE);
        }
        finally
        {
            platformDao.releaseConnection();
        }
        return xmlDoc;
    }
    private Document getNewMessageList(Document xmlDoc)
    {
        StringBuffer sqlBuf = new StringBuffer("select a.title as title,a.content as content,a.createuser as createuser,");
        sqlBuf.append(" a.createtime as createtime,c.username as username ");
        sqlBuf.append("from pcmc_message a,pcmc_sendgroup b,pcmc_user c where b.targetuser = ");
        sqlBuf.append(xmlDoc.getRootElement().getChild("Session").getChildTextTrim("userid"));
        sqlBuf.append(" and b.isread = '");
        sqlBuf.append(xmlDoc.getRootElement().getChild("Request").getChild("Data").getChildTextTrim("isread"));
        sqlBuf.append("' and a.messageid = b.messageid and a.createuser = c.userid");
        int size = -1;
        int pageno = 1;
        if("Y".equals(xmlDoc.getRootElement().getChild("Request").getChild("Data").getChildTextTrim("isread")))
        {
              size = Integer.parseInt(xmlDoc.getRootElement().getChild("Request").getChild("PageInfo").getChildTextTrim("PageSize"));
              pageno = Integer.parseInt(xmlDoc.getRootElement().getChild("Request").getChild("PageInfo").getChildTextTrim("PageNo"));
        }
        try
        {
            platformDao.setSql(sqlBuf.toString());
            Element values = platformDao.executeQuerySql(size,pageno);
            xmlDoc.getRootElement().getChild("Response").addContent(values);
            xmlDoc.getRootElement().getChild("Response").getAttribute("result").setValue("0");
        }
        catch(Exception e)
        {
            xmlDoc.getRootElement().getChild("Response").getAttribute("result").setValue(Constants.SYSTEM_ERROR_CODE);
            platformDao.releaseConnection();
            return xmlDoc;
        }
        sqlBuf.delete(0,sqlBuf.capacity());
        sqlBuf.append("update pcmc_sendgroup set isread = 'Y' where targetuser = ");
        sqlBuf.append(xmlDoc.getRootElement().getChild("Session").getChildTextTrim("userid"));
        sqlBuf.append(" and isread ='N'");
        //对显示消息需要确认用户是否已经阅读，不需要返回错误信息，依旧可以显示
        try
        {
             platformDao.setSql(sqlBuf.toString());
             platformDao.executeTransactionSql();
        }
        catch(Exception e)
        {

        }
        finally
        {
            platformDao.releaseConnection();
        }
        return xmlDoc;
    }
}
