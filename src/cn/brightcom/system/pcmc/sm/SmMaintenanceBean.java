package cn.brightcom.system.pcmc.sm;

import org.jdom.Document ;
import org.jdom.Element ;

import cn.brightcom.jraf.db.DBOprProxy;
import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.util.Constants;
import cn.brightcom.jraf.util.DatetimeUtil;
import cn.brightcom.jraf.util.JDomUtil;
import cn.brightcom.jraf.util.Log;
import cn.brightcom.jraf.util.XmlDocPkgUtil;

import java.util.List ;
import java.util.ArrayList ;
import java.sql.Timestamp;

public class SmMaintenanceBean
{
	private XmlDocPkgUtil xmlDocUtil = null;
    
    private static Log log4j = new Log(SmMaintenanceBean.class.getName());
    
    public Document doPost(Document xmlDoc)
    {
    	xmlDocUtil = new XmlDocPkgUtil(xmlDoc);
        String action = xmlDoc.getRootElement().getChild("Action").getText() ;
        if ("addInfo".equals(action))
        { 
            addInfo(xmlDoc);
        }
        else if ("updateInfo".equals(action))
        {
            updateInfo(xmlDoc);
        }
        else if ("addMessage".equals(action))
        {
            addMessage(xmlDoc);
        }
        else if ("readMessage".equals(action))
        {
            readMessage(xmlDoc);
        }        
        else if ("updateInfoKind".equals(action))
        {
            updateInfoKind(xmlDoc);
        }
        else if ("deleteInfoKind".equals(action))
        {
            deleteInfoKind(xmlDoc);
        }
        else if ("addInfoKind".equals(action))
        {
            addInfoKind(xmlDoc);
        }
        return xmlDoc ;
    }

    //给角色授权
    private final void addInfo(Document xmlDoc)
    {
        Element dataElement = xmlDoc.getRootElement().getChild("Request").getChild("Data");
        String kindid = dataElement.getChildTextTrim("kindid");
        String title = dataElement.getChildTextTrim("title");
        String content = dataElement.getChildText("content");
        String userid = dataElement.getChildText("userid");
        Timestamp createtime = DatetimeUtil.getNow();
        PlatformDao dao = new PlatformDao();
        StringBuffer sql = new StringBuffer();
        try
        {
            long seq = DBOprProxy.getNextSequenceNumber("pcmc_info");
            sql.append(" insert into pcmc_info values(?,?,?,?,?,?)");
            dao.setSql(sql.toString());
            ArrayList bvals = new ArrayList();
            bvals.add(new Long(seq));
            bvals.add(new Long(Long.parseLong(kindid)));
            bvals.add(title);
            bvals.add(content);
            bvals.add(new Long(Long.parseLong(userid)));
            bvals.add(createtime);
            dao.setBindValues(bvals);
            dao.executeTransactionSql();
            xmlDoc.getRootElement().getChild("Response").getAttribute("result").setValue("0");
            xmlDoc.getRootElement().getChild("Response").getChild("Hint").addContent((new Element("Msg")).setText("增加新闻/公告成功！"));
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
    private final void updateInfo(Document xmlDoc)
    {
        Element dataElement = xmlDoc.getRootElement().getChild("Request").getChild("Data");
        String infoid = dataElement.getChildTextTrim("infoid");
        String title = dataElement.getChildTextTrim("title");
        String content = dataElement.getChildText("content");
        PlatformDao dao = new PlatformDao();
        StringBuffer sql = new StringBuffer();
        try
        {
            sql.append(" update pcmc_info set title=?,content=? where infoid=?");
            dao.setSql(sql.toString());
            ArrayList bvals = new ArrayList();
            bvals.add(title);
            bvals.add(content);
            bvals.add(new Long(Long.parseLong(infoid)));
            dao.setBindValues(bvals);
            dao.executeTransactionSql();
            xmlDoc.getRootElement().getChild("Response").getAttribute("result").setValue("0");
            xmlDoc.getRootElement().getChild("Response").getChild("Hint").addContent((new Element("Msg")).setText("修改新闻/公告成功！"));
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

    private final void addMessage(Document xmlDoc)
    {
        Element dataElement = xmlDoc.getRootElement().getChild("Request").getChild("Data");
        String title = dataElement.getChildTextTrim("title");
        String content = dataElement.getChildText("content");
        List targetuserList = dataElement.getChildren("targetuser");
        String userid = xmlDoc.getRootElement().getChild("Session").getChildTextTrim("userid");
        PlatformDao dao = new PlatformDao();
        StringBuffer sql = new StringBuffer();
        long seq;
        try
        {
            dao.beginTransaction();
            seq = DBOprProxy.getNextSequenceNumber("pcmc_message");
            sql.setLength(0);
            sql.append(" insert into pcmc_message(messageid,title,content,createuser,createtime) values(");
            sql.append(seq + ",'" +title + "','" + content + "'," +userid + ",'" + DatetimeUtil.getNow().toString() + "')");
            dao.setSql(sql.toString());
            dao.executeTransactionSql();
            if (targetuserList!=null)
            {
                for (int i=0;i<targetuserList.size();i++)
                {
                    Element record = (Element)targetuserList.get(i);
                    String targetuser = record.getTextTrim();
                    long groupseq = DBOprProxy.getNextSequenceNumber("pcmc_sendgroup");
                    sql.setLength(0);
                    sql.append(" insert into pcmc_sendgroup(sendgroupid,messageid,targetuser,isread) values(" + groupseq + "," + seq + "," + targetuser + ",'0')");
                    dao.setSql(sql.toString());
                    dao.executeTransactionSql();
                }
            }
            dao.commitTransaction();
            xmlDoc.getRootElement().getChild("Response").getChild("Hint").addContent((new Element("Msg")).setText("短消息已发送！"));
            xmlDoc.getRootElement().getChild("Response").getAttribute("result").setValue("0");
        }
        catch (Exception e)
        {
            dao.rollBack();
            e.printStackTrace();
            xmlDoc.getRootElement().getChild("Response").getAttribute("result").setValue("1");
            xmlDoc.getRootElement().getChild("Response").getChild("Hint").addContent((new Element("Msg")).setText("短消息发送失败！"));
        }
        finally
        {
            dao.releaseConnection();
        }
    }
    private final void readMessage(Document xmlDoc)
    {
        Element dataElement = xmlDoc.getRootElement().getChild("Request").getChild("Data");
        String sendgroupid = dataElement.getChildTextTrim("sendgroupid");
        PlatformDao dao = new PlatformDao();
        StringBuffer sql = new StringBuffer();
        String receivetime = DatetimeUtil.getNow().toString();
        try
        {
            sql.append(" update pcmc_sendgroup set ");
            sql.append(" isread = '1', receivetime =  '" + receivetime);
            sql.append("' where sendgroupid = " + sendgroupid);
            dao.setSql(sql.toString());
            dao.executeTransactionSql();
            sql.setLength(0) ;
            sql.append(" select a.*,c.username createusername from pcmc_message a,pcmc_sendgroup b,pcmc_user c ") ;
            sql.append(" where a.messageid = b.messageid ") ;
            sql.append(" and a.createuser = c.userid ");
            sql.append(" and b.sendgroupid = " + sendgroupid) ;
            dao.setSql(sql.toString());
            System.out.println("sql:"+sql);
            Element messageElement = dao.executeQuerySql(-1,1);
            xmlDoc.getRootElement().getChild("Response").getAttribute("result").setValue("0");
            xmlDoc.getRootElement().getChild("Response").addContent(messageElement) ;
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
    }
    
    private final void updateInfoKind(Document xmlDoc)
    {
        Element dataElement = xmlDoc.getRootElement().getChild("Request").getChild("Data");
        String kindid = dataElement.getChildTextTrim("kindid");
        String kindname = dataElement.getChildTextTrim("kindname");
        String sortno = dataElement.getChildTextTrim("sortno");
        PlatformDao dao = new PlatformDao();
        Element listElement = null;
        StringBuffer sql = new StringBuffer(
            "select * from pcmc_info_kind where kindid<>"+kindid+" and kindname='"+kindname+"'");
        try {
            dao.setSql(sql.toString());
            listElement = dao.executeQuerySql( -1, 1);
            if (listElement.getChildren("Record").size()>0) {
                Element msg = new Element("Msg");
                msg.setText("分类已经存在,请重新设置!");
                xmlDoc.getRootElement().getChild("Response").getChild("Error").addContent(msg);
                return;
            }
            sql.setLength(0);
            sql.append("update pcmc_info_kind set kindname='"+kindname+"',sortno="+sortno+" where kindid="+kindid);
            dao.setSql(sql.toString());
            dao.executeTransactionSql();
            xmlDoc.getRootElement().getChild("Response").getAttribute("result").setValue("0");
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

    private final void deleteInfoKind(Document xmlDoc)
    {
        Element dataElement = xmlDoc.getRootElement().getChild("Request").getChild("Data");
        List kindList = dataElement.getChildren("kindid");
        PlatformDao dao = new PlatformDao();
        Element listElement = null;
        StringBuffer sql = new StringBuffer("");
        try {
            for (int i=0;i<kindList.size();i++) {
                Element kind = (Element)kindList.get(i);
                sql.append("select * from pcmc_info where kindid="+kind.getText());
                dao.setSql(sql.toString());
                listElement = dao.executeQuerySql( -1, 1);
                if (listElement.getChildren("Record").size()>0) {
                    Element msg = new Element("Msg");
                    msg.setText("公告下面有详细内容,请先删除具体公告!");
                    xmlDoc.getRootElement().getChild("Response").getChild("Error").addContent(msg);
                    return;
                }
                sql.setLength(0);
                sql.append("delete from pcmc_info_kind where kindid="+kind.getText());
                dao.setSql(sql.toString());
                dao.executeTransactionSql();
            }
            xmlDoc.getRootElement().getChild("Response").getAttribute("result").setValue("0");
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

    private final void addInfoKind(Document xmlDoc)
    {
        Element dataElement = xmlDoc.getRootElement().getChild("Request").getChild("Data");
        String kindname = dataElement.getChildTextTrim("kindname");
        String sortno = dataElement.getChildTextTrim("sortno");
        String userid = dataElement.getChildTextTrim("userid");
        PlatformDao dao = new PlatformDao();
        Element listElement = null;
        StringBuffer sql = new StringBuffer(
            "select * from pcmc_info_kind where kindname='"+kindname+"'");
        try {
            dao.setSql(sql.toString());
            listElement = dao.executeQuerySql( -1, 1);
            if (listElement.getChildren("Record").size()>0) {
                Element msg = new Element("Msg");
                msg.setText("分类已经存在,请重新设置!");
                xmlDoc.getRootElement().getChild("Response").getChild("Error").addContent(msg);
                return;
            }
            sql.setLength(0);
            sql.append("insert into pcmc_info_kind values(?,?,?,?,?)");
            dao.setSql(sql.toString());
            ArrayList bvals = new ArrayList();
            long seq = DBOprProxy.getNextSequenceNumber("pcmc_info_kind");
            bvals.add(new Long(seq));
            bvals.add(kindname);
            bvals.add(new Long(Long.parseLong(sortno)));
            bvals.add(new Long(Long.parseLong(userid)));
            bvals.add(DatetimeUtil.getNow());
            dao.setBindValues(bvals);
            dao.executeTransactionSql();
            xmlDoc.getRootElement().getChild("Request").getChild("Data").
                addContent(new Element("kindid").setText(String.valueOf(seq)));
            xmlDoc.getRootElement().getChild("Response").getAttribute("result").setValue("0");
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
}
