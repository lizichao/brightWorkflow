package cn.brightcom.system.pcmc.util;

import org.jdom.*;

import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.util.ConfigDocument;
import cn.brightcom.jraf.util.DatetimeUtil;
import cn.brightcom.jraf.util.JDomUtil;

/**
 * <p>Title: 待办事宜工具类</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author Kories Kang
 * @version 1.0
 */

public class PendJobUtil
{
    //给指定的User发送待办事宜
    public static void sendJobToUser(long userid, String title, String url) throws Exception
    {
        PlatformDao dao = new PlatformDao();
        try
        {
            Element dataElement = ConfigDocument.createRecordElement("pcmc","pcmc_pendjob");
            dataElement.getChild("userid").setText(String.valueOf(userid));
            dataElement.getChild("title").setText(title);
            dataElement.getChild("url").setText(url);
            dataElement.getChild("isvisited").setText("0");
            dataElement.getChild("sendtime").setText(DatetimeUtil.getNow(""));
            System.out.println(JDomUtil.toXML(dataElement));
            dao.insertOneRecord(dataElement);
        }
        catch (Exception e)
        {
            throw e;
        }
        finally
        {
            dao.releaseConnection();
        }
    }
    //给指定角色的User群发待办事宜
    public static void sendJobToRole(long roleid, String title, String url) throws Exception
    {
        PlatformDao dao = new PlatformDao();
        try
        {
            dao.beginTransaction();
            StringBuffer sql = new StringBuffer();
            sql.append("select userid from pcmc_user_role where roleid = ");
            sql.append(roleid);
            dao.setSql(sql.toString());
            Element userElement = dao.executeQuerySql(-1,1);
            java.util.List userList = userElement.getChildren("Record");
            for (int i = 0; i < userList.size(); i ++)
            {
                String userid = ((Element)userList.get(i)).getChildTextTrim("userid");
                Element dataElement = ConfigDocument.createRecordElement("pcmc", "pcmc_pendjob") ;
                dataElement.getChild("userid").setText(userid) ;
                dataElement.getChild("title").setText(title) ;
                dataElement.getChild("url").setText(url) ;
                dataElement.getChild("isvisited").setText("0") ;
                dataElement.getChild("sendtime").setText(DatetimeUtil.getNow("")) ;
                dao.insertOneRecord(dataElement) ;
            }
            dao.commitTransaction();
        }
        catch (Exception e)
        {
            dao.rollBack();
            throw e;
        }
        finally
        {
            dao.releaseConnection();
        }
    }
}
