package cn.brightcom.system.pcmc.sm;

import java.util.ArrayList;

import org.jdom.Document;
import org.jdom.Element;

import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.util.Log;
import cn.brightcom.jraf.util.XmlDocPkgUtil;
import cn.brightcom.system.pcmc.util.PcmcUtil;


/**
 * <p>Title: 子系统维护</p>
 * <p>Description: 子系统维护</p>
 * <p>Copyright: Copyright (c) 2009 Brightcom</p>
 * <p>Company: ShenZhen BrightCom Technological Co.,Ltd.</p>
 * @author <a href="http://www.szbrightcom.cn" target="_blank">QIUMH</a>
 * @version 1.0a
 */
public class SubsysBean
{
	private XmlDocPkgUtil xmlDocUtil = null;
    
    private static Log log4j = new Log(SubsysBean.class.getName());
    
    public Document doPost(Document xmlDoc)
    {
    	xmlDocUtil = new XmlDocPkgUtil(xmlDoc);
    	String action = xmlDocUtil.getAction();
        if("deleteSubSys".equals(action))
        {
            deleteSubSys();
        }
        else if("getUserPcmcSubsys".equals(action))
        {
            getUserPcmcSubsys();
        }
    	
    	return xmlDoc;
    }    
    
    /**
     * 获取子系统所有相关信息
     * @param xmlDoc
     * @return
     */
    private final void deleteSubSys()
    {
        Element dataElement = xmlDocUtil.getRequestData();
        String subsysid = dataElement.getChildTextTrim("subsysid");
        PlatformDao pDao = null;
        try
        {
        	pDao = new PlatformDao();
        	pDao.beginTransaction();
        	Long sysid = new Long(subsysid);
        	//pcmc_role_act
        	deleteSubsys(pDao,"delete from pcmc_role_act where roleid in (select roleid from pcmc_role where subsysid=?)",sysid);
        	//pcmc_role_menu
        	deleteSubsys(pDao,"delete from pcmc_role_menu where roleid in (select roleid from pcmc_role where subsysid=?)",sysid);
        	//pcmc_user_role
        	deleteSubsys(pDao,"delete from pcmc_user_role where roleid in (select roleid from pcmc_role where subsysid=?)",sysid);
        	//pcmc_role
        	deleteSubsys(pDao,"delete from pcmc_role where subsysid=?",sysid);
        	//pcmc_menu
        	deleteSubsys(pDao,"delete from pcmc_menu where subsysid=?",sysid);
        	//pcmc_subsys
        	deleteSubsys(pDao,"delete from pcmc_subsys where subsysid=?",sysid);
        	
        	pDao.commitTransaction();
        	xmlDocUtil.setResult("0");
        	xmlDocUtil.writeHintMsg("10501","删除子系统成功");
        }
        catch (Exception e)
        {
        	pDao.rollBack();
            e.printStackTrace();
            log4j.logError(e);
            xmlDocUtil.setResult("-1");
        	xmlDocUtil.writeErrorMsg("10502","删除子系统失败");
        }
        finally
        {
            pDao.releaseConnection();
        }
    }
    
    private final void deleteSubsys(PlatformDao pDao,String sql,Long sysid)
    	throws Exception
    {
    	ArrayList bvals = new ArrayList();
    	bvals.add(sysid);
    	pDao.setSql(sql);
    	pDao.setBindValues(bvals);
    	pDao.executeTransactionSql();
    }
    
    /**
     * 获取当前登陆用户的子系统
     * @param xmlDoc
     * @return
     */
    private final void getUserPcmcSubsys()
    {
        PlatformDao pDao = null;
        try
        {
        	pDao = new PlatformDao();
        	
        	Element session = xmlDocUtil.getSession();
            String userid = session.getChildTextTrim("userid");
            String usercode = session.getChildText("usercode");
            
            StringBuffer sql = new StringBuffer();
            ArrayList bvals = new ArrayList();
            if(PcmcUtil.isSysManager(usercode))
            {
            	sql.append("select * from pcmc_subsys order by orderidx");
            }
            else
            {
            	StringBuffer ridBuf = new StringBuffer();
    			int ridlen = UserRoleBean.userRoleIDQuery(ridBuf);
            	sql.append("select distinct c.* from ");
            	sql.append(" ").append(ridBuf).append(" a");
	            sql.append(",pcmc_role b,pcmc_subsys c ")
	            	.append("where a.roleid=b.roleid ")
	            	.append("and b.subsysid=c.subsysid order by orderidx");
	            for(int i=0;i<ridlen; i++)
    			{
    				bvals.add(userid);
    			}
            }
            pDao.setSql(sql.toString());
            pDao.setBindValues(bvals);
            
            Element result = pDao.executeQuerySql(-1, 1);
            xmlDocUtil.setResult("0");
            xmlDocUtil.getResponse().addContent(result);
        }
        catch (Exception e)
        {
            e.printStackTrace() ;
            log4j.logError(e);
            xmlDocUtil.setResult("-1");
        	xmlDocUtil.writeErrorMsg("10503","获取登陆用户子系统失败");
        }
        finally
        {
            pDao.releaseConnection();
        }
    }
    /*
    private Document getRoleSubSysList(Document xmlDoc)
    {
        Element dataElement = xmlDocUtil.getRequestData();
        String userid = dataElement.getChildTextTrim("userid");
        PlatformDao dao = new PlatformDao();
        Element listElement = null;
        try {
            StringBuffer sql = new StringBuffer("select distinct c.subsysid,c.shortname,c.cnname from ");
            sql.append(" pcmc_user_role a left join pcmc_role b on b.roleid=a.roleid ");
            sql.append(" ,pcmc_subsys c where a.userid="+userid);
            sql.append(" and c.subsysid=b.subsysid order by c.cnname");
            dao.setSql(sql.toString());
            listElement = dao.executeQuerySql( -1, 1);
            xmlDoc.getRootElement().getChild("Response").addContent(listElement);
            xmlDoc.getRootElement().getChild("Response").getAttribute("result").setValue("0");
        }
        catch (Exception e)
        {
            e.printStackTrace() ;
            xmlDoc.getRootElement().getChild("Response").getAttribute("result").setValue(Constants.SYSTEM_ERROR_CODE) ;
            Log log4j = new Log(this.getClass().toString()) ;
            log4j.logError(e.getMessage()) ;
            log4j.logInfo(JDomUtil.toXML(xmlDoc, "gb2312", true)) ;
        }
        finally
        {
            dao.releaseConnection();
        }
        return xmlDoc;
    }*/
}
