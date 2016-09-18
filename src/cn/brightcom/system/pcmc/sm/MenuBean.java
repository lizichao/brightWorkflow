package cn.brightcom.system.pcmc.sm;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.jdom.Document;
import org.jdom.Element;

import cn.brightcom.jraf.conf.BrightComConfig;
import cn.brightcom.jraf.db.DBOprProxy;
import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.util.Constants;
import cn.brightcom.jraf.util.JDomUtil;
import cn.brightcom.jraf.util.Log;
import cn.brightcom.jraf.util.StringUtil;
import cn.brightcom.jraf.util.XmlDocPkgUtil;
import cn.brightcom.system.pcmc.util.PcmcUtil;


public class MenuBean
{
	private XmlDocPkgUtil xmlDocUtil = null;
	
	private static String defaultIcons = BrightComConfig.getConfiguration().getString("brightcom.menuicon","");
    
    private static Log log4j = new Log(MenuBean.class.getName());
    
    public Document doPost(Document xmlDoc)
    {
    	xmlDocUtil = new XmlDocPkgUtil(xmlDoc);
    	String action = xmlDocUtil.getAction();

        if ("addShortCuts".equals(action))
        {
            addShortCuts(xmlDoc);
        }
        else if("addFrdLink".equals(action))
        {
        	addFrdLink(xmlDoc);				//增加友情链接
        }
        else if("getLinkTypeDetail".equals(action))
        {
        	getLinkTypeDetail(xmlDoc);				//获取友情链接分类详细信息
        }
        else if("getFrdLinkList".equals(action))
        {
        	getFrdLinkList(xmlDoc);				//获取友情链接列表
        }
        else if("getAllLinkDetail".equals(action))
        {
        	getAllLinkDetail(xmlDoc);				//获取友情链接所有信息
        }
        else if ("getMenuShortCuts".equals(action))
        {
            getMenuShortCuts(xmlDoc);
        }
        else if ("getMenuShortCutTree".equals(action))
        {
            getMenuShortCutTree(xmlDoc);
        }
        else if ("deleteMenu".equals(action))
        {
            deleteMenu();
        }
        else if ("getUserMenuBySubsys".equals(action))
        {
            getUserMenuBySubsys();
        }
        else if("getMenuIcons".equals(action))
        {
        	getMenuIcons();
        }
    	return xmlDoc;
    }
            
    /**
     * 删除菜单
     * @param xmlDoc
     * @return
     */
    private final void deleteMenu()
    {
        Element reqData = xmlDocUtil.getRequestData();
        String menuid = reqData.getChildTextTrim("menuid");
        PlatformDao pDao = null;
        try
        {
        	pDao = new PlatformDao();
        	pDao.beginTransaction();
        	ArrayList bvals = new ArrayList();
        	StringBuffer sql = new StringBuffer("select * from pcmc_menu where pmenuid=?");
        	bvals.add(new Long(menuid));
            pDao.setSql(sql.toString());
            pDao.setBindValues(bvals);
            Element result = pDao.executeQuerySql(-1, 1);
            if ((result!=null)&&(result.getChildren("Record")!=null)&&(result.getChildren("Record").size()>0))
            {
                xmlDocUtil.setResult("-1");
                xmlDocUtil.writeErrorMsg("10301","请先删除此菜单下所有的菜单项");
                return;
            }
            sql.setLength(0);
            sql.append("delete from pcmc_menu where menuid=?");
            pDao.setSql(sql.toString());
            pDao.setBindValues(bvals);
            pDao.executeTransactionSql();
            sql.setLength(0);
			//删除角色关联菜单
			sql.append("delete from pcmc_role_menu where menuid=?");
			pDao.setSql(sql.toString());
			pDao.setBindValues(bvals);
			pDao.executeTransactionSql();
            pDao.commitTransaction();
        
            xmlDocUtil.setResult("0");
            xmlDocUtil.writeHintMsg("10302","删除菜单成功");
        }
        catch (java.sql.SQLException e)
        {
        	pDao.rollBack();
            e.printStackTrace() ;
            log4j.logError(e);
            xmlDocUtil.writeErrorMsg("10303","删除菜单异常");
        }
        finally
        {
            pDao.releaseConnection();
        }
    }
    
    /**
     * 取用户子系统菜单
     */
    private final void getUserMenuBySubsys()
    {
    	Element reqData = xmlDocUtil.getRequestData();
    	Element session = xmlDocUtil.getSession();
    	String subsysid = reqData.getChildTextTrim("subsysid");
    	String userid = session.getChildText("userid");
    	String usercode = session.getChildText("usercode");
    	
    	try
    	{
    		Element resData = getMenuByUserSubsys(userid,usercode,subsysid);
    		xmlDocUtil.setResult("0");
    		xmlDocUtil.getResponse().addContent(resData);
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    		log4j.logError(ex);
    		xmlDocUtil.writeErrorMsg("10304","查询用户菜单异常");
    	}
    }
        
    public static Element getMenuByUserSubsys(String userid,String usercode,String subsysid)
    	throws Exception
    {    	
    	PlatformDao pDao = null;
    	try
    	{
    		pDao = new PlatformDao();
    		StringBuffer sqlBuf = new StringBuffer();
    		sqlBuf.append("select distinct c.*,'' selected from pcmc_menu c");
    		ArrayList bvals = new ArrayList();
    		if(PcmcUtil.isSysManager(usercode))
    		{
    			sqlBuf.append(" where c.subsysid=?");
    			bvals.add(new Long(subsysid));
    		}
    		else
    		{
    			StringBuffer ridBuf = new StringBuffer();
    			int ridlen = UserRoleBean.userRoleIDQuery(ridBuf);
    			sqlBuf.append(",").append(ridBuf).append(" a");
    			sqlBuf.append(",pcmc_role_menu b ")
    				.append("where a.roleid=b.roleid ")
    				.append("and b.menuid=c.menuid ")
    				.append("and c.subsysid=? ");
    			for(int i=0;i<ridlen; i++)
    			{
    				bvals.add(userid);
    			}
    			bvals.add(new Long(subsysid));
    		}
    		sqlBuf.append(" order by c.levels,c.pmenuid,c.orderidx,c.menuid");
    		pDao.setSql(sqlBuf.toString());
    		pDao.setBindValues(bvals);
    		
    		return pDao.executeQuerySql(-1, 1);
    	}
    	finally
    	{
    		pDao.releaseConnection();
    	}
    }    
    
    private final void getMenuIcons()
    {
    	try
    	{
	    	String[] flds = new String[]{"icon","icondesc"};
	    	Element resData = XmlDocPkgUtil.createMetaData(flds);
	    	
	    	StringTokenizer st = new StringTokenizer(defaultIcons,"|");
	    	while(st.hasMoreTokens())
	    	{
	    		String ss = st.nextToken();
	    		if(StringUtil.isNotEmpty(ss))
	    		{
	    			String[] sv = new String[]{ss,ss};
	    			resData.addContent(XmlDocPkgUtil.createRecord(flds, sv));
	    		}
	    	}
	    	xmlDocUtil.getResponse().addContent(resData);
	    	xmlDocUtil.setResult("0");
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    		xmlDocUtil.writeErrorMsg("10305","查询菜单图标列表异常");
    	}
    }

    private final void getMenuShortCuts(Document xmlDoc)
    {
        Element dataElement = xmlDoc.getRootElement().getChild("Request").getChild("Data") ;
        String subsysid = dataElement.getChildTextTrim("subsysid") ;
        String userid = xmlDoc.getRootElement().getChild("Session").getChildTextTrim("userid");
        StringBuffer sql = new StringBuffer(" select * from pcmc_shortcut a,pcmc_menu b ") ;
        sql.append(" where a.menuid = b.menuid ");
        sql.append(" and a.userid = "+ userid);
        if ((subsysid != null)&&(subsysid.length()>0))
        {
            sql.append(" and b.subsysid = " + subsysid);
        }
        PlatformDao dao = new PlatformDao() ;
        try
        {
            dao.setSql(sql.toString()) ;
            Element result = dao.executeQuerySql( -1, 1) ;
            xmlDoc.getRootElement().getChild("Response").getAttribute("result").setValue("0") ;
            xmlDoc.getRootElement().getChild("Response").addContent(result) ;
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

    private final void addShortCuts(Document xmlDoc)
    {
        Element dataElement = xmlDoc.getRootElement().getChild("Request").getChild("Data");
        String subsysid = dataElement.getChildTextTrim("subsysid");
        java.util.List menuList = dataElement.getChildren("menuid");
        String userid = xmlDoc.getRootElement().getChild("Session").getChildTextTrim("userid");
        String menuid = new String();
        PlatformDao dao = new PlatformDao();
        StringBuffer sql = new StringBuffer();
        long seq = 0;
        try
        {
            dao.beginTransaction();
            sql.append("delete from pcmc_shortcut a where a.userid = ");
            sql.append(userid);
            sql.append(" and exists (select b.menuid from pcmc_menu b where a.menuid = b.menuid ");
            sql.append(" and b.subsysid = " + subsysid+")");
            dao.setSql(sql.toString());
            dao.executeTransactionSql();
            for (int i = 0; i < menuList.size(); i ++)
            {
                Element menuElement = (Element) menuList.get(i);
                menuid = menuElement.getTextTrim();
                seq = DBOprProxy.getNextSequenceNumber("pcmc_shortcut");
                sql.setLength(0);
                sql.append("insert into pcmc_shortcut(shortcutid,userid,menuid) values(");
                sql.append(String.valueOf(seq));
                sql.append(",");
                sql.append(userid);
                sql.append(",");
                sql.append(menuid);
                sql.append(")");
                dao.setSql(sql.toString());
                dao.executeTransactionSql();
            }
            xmlDoc.getRootElement().getChild("Response").getAttribute("result").setValue("0");
            xmlDoc.getRootElement().getChild("Response").getChild("Hint").addContent((new Element("Msg")).setText("权限分配成功！"));
            dao.commitTransaction();
        }
        catch (Exception e)
        {
            dao.rollBack();
            e.printStackTrace();
            xmlDoc.getRootElement().getChild("Response").getAttribute("result").setValue(Constants.SYSTEM_ERROR_CODE);
        }
        finally
        {
            dao.releaseConnection();
        }
    }
    
    private final void addFrdLink(Document xmlDoc)
    {
        Element dataElement = xmlDoc.getRootElement().getChild("Request").getChild("Data");
        String linktypeid = dataElement.getChildTextTrim("linktypeid");
        String linkname = dataElement.getChildTextTrim("linkname");
        String imgurl = dataElement.getChildTextTrim("imgurl");
        String linkurl = dataElement.getChildTextTrim("linkurl");
        PlatformDao dao = new PlatformDao();
        StringBuffer sql = new StringBuffer("");
        try {
            sql.append("insert into pcmc_frdlink values(?,?,?,?,?,0)");
            dao.setSql(sql.toString());
            ArrayList bvals = new ArrayList();
            long seq = DBOprProxy.getNextSequenceNumber("pcmc_frdlink");
            bvals.add(new Long(seq));
            bvals.add(new Long(Long.parseLong(linktypeid)));
            bvals.add(linkname);
            bvals.add(imgurl);
            bvals.add(linkurl);
            dao.setBindValues(bvals);
            dao.executeTransactionSql();
            xmlDoc.getRootElement().getChild("Response").getAttribute("result").setValue("0");
        }
        catch (Exception e)
        {
            e.printStackTrace() ;
            xmlDoc.getRootElement().getChild("Response").getAttribute("result").setValue(Constants.SYSTEM_ERROR_CODE) ;
            log4j.logError("[增加友情链接发生错误:]"+e.getMessage()) ;
            log4j.logInfo(JDomUtil.toXML(xmlDoc, "gb2312", true)) ;
        }
        finally
        {
            dao.releaseConnection();
        }
    }
    
    private final void getMenuShortCutTree(Document xmlDoc)
    {
        Element dataElement = xmlDoc.getRootElement().getChild("Request").getChild("Data") ;
        String subsysid = dataElement.getChildTextTrim("subsysid") ;
        String roleid = dataElement.getChildTextTrim("roleid") ;
        String userid = dataElement.getChildTextTrim("userid") ;
        StringBuffer permissionSql = new StringBuffer() ;
        permissionSql.append(" select a.menuid from pcmc_shortcut a,pcmc_menu b ") ;
        permissionSql.append(" where a.menuid = b.menuid ") ;
        permissionSql.append(" and a.userid = " + userid) ;
        permissionSql.append(" and b.subsysid = " + subsysid) ;

        StringBuffer allMenuSql = new StringBuffer() ;
        allMenuSql.append(" select a.*,'' selected from pcmc_menu a, pcmc_role b,pcmc_role_menu c ") ;
        allMenuSql.append(" where  a.subsysid = b.subsysid ") ;
        allMenuSql.append(" and  a.menuid = c.menuid ") ;
        allMenuSql.append(" and  b.roleid = c.roleid ") ;
        allMenuSql.append(" and b.roleid = " + roleid) ;
        PlatformDao dao = new PlatformDao() ;
        try
        {
            dao.setSql(permissionSql.toString()) ;
            Element permissionElement = dao.executeQuerySql( -1, 1) ;
            List permissionRecords = permissionElement.getChildren("Record") ;
            dao.setSql(allMenuSql.toString()) ;
            Element allMenuElement = dao.executeQuerySql( -1, 1) ;
            if ( (permissionRecords != null) && (permissionRecords.size() > 0))
            {
                List allMenuRecords = allMenuElement.getChildren("Record") ;
                for (int i = 0 ; i < permissionRecords.size() ; i++)
                {
                    Element permissionRecord = (Element) permissionRecords.get(i) ;
                    for (int j = 0 ; j < allMenuRecords.size() ; j++)
                    {
                        Element allMenuRecord = (Element) allMenuRecords.get(j) ;
                        if (permissionRecord.getChildTextTrim("menuid").equals(allMenuRecord.getChildTextTrim("menuid")))
                        {
                            allMenuRecord.getChild("selected").setText("checked") ;
                        }
                    }
                }
            }
            xmlDoc.getRootElement().getChild("Response").addContent(allMenuElement) ;
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

    private final void getLinkTypeDetail(Document xmlDoc) {
        Element dataElement = xmlDocUtil.getRequestData();
        String linktypeid = dataElement.getChildText("linktypeid");
        PlatformDao dao = new PlatformDao();
        StringBuffer sql = new StringBuffer(
            "select * from pcmc_frdlink_type where linktypeid="+linktypeid);
        try {
            dao.setSql(sql.toString());
            Element result = dao.executeQuerySql( -1, 1);
//            sql.setLength(0);
//            sql.append("select * from pcmc_frdlink");
//            sql.append(" where linktypeid="+linktypeid+" order by linkname");
//            dao.setSql(sql.toString());
//            Element tmp = dao.executeQuerySql(-1,1);
//            result.addContent(tmp);
            xmlDocUtil.getResponse().addContent(result);
            xmlDocUtil.setResult("0");
        } catch (Exception e) {
            e.printStackTrace();
            xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
            log4j.logError("[获取友情链接分类详细信息发生错误.]"+e.getMessage()) ;
            log4j.logInfo(JDomUtil.toXML(xmlDoc, "gb2312", true)) ;
        } finally {
            dao.releaseConnection();
        }
    }

    private final void getFrdLinkList(Document xmlDoc) {
        Element dataElement = xmlDocUtil.getRequestData();
        String frdlinkid = dataElement.getChildText("frdlinkid");
        PlatformDao dao = new PlatformDao();
        StringBuffer sql = new StringBuffer(
            "select a.*,b.typename from pcmc_frdlink a,pcmc_frdlink_type b");
        sql.append(" where b.linktypeid=a.linktypeid ");
        try {
        	if (frdlinkid!=null && !frdlinkid.equals("")) {
        		sql.append(" and frdlinkid="+frdlinkid);
        	}
        	sql.append(" order by a.linkname");
            dao.setSql(sql.toString());
            Element result = dao.executeQuerySql( -1, 1);
            xmlDocUtil.getResponse().addContent(result);
            xmlDocUtil.setResult("0");
        } catch (Exception e) {
            e.printStackTrace();
            xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
            log4j.logError("[获取友情链接列表发生错误.]"+e.getMessage()) ;
            log4j.logInfo(JDomUtil.toXML(xmlDoc, "gb2312", true)) ;
        } finally {
            dao.releaseConnection();
        }
    }

    private final void getAllLinkDetail(Document xmlDoc) {
        Element dataElement = xmlDocUtil.getRequestData();
        PlatformDao dao = new PlatformDao();
        StringBuffer sql = new StringBuffer(
            "select * from pcmc_frdlink_type order by typename");
        try {
            dao.setSql(sql.toString());
            Element result = dao.executeQuerySql( -1, 1);
            List record = result.getChildren("Record");
            for (int i=0;i<record.size();i++) {
            	Element ele = (Element)record.get(i);
	              sql.setLength(0);
	              sql.append("select * from pcmc_frdlink");
	              sql.append(" where linktypeid="+ele.getChildText("linktypeid")+" order by linkname");
	              dao.setSql(sql.toString());
	              Element tmp = dao.executeQuerySql(-1,1);
	              ele.addContent(tmp);
            }
            xmlDocUtil.getResponse().addContent(result);
            xmlDocUtil.setResult("0");
        } catch (Exception e) {
            e.printStackTrace();
            xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
            log4j.logError("[获取友情链接所有详细信息发生错误.]"+e.getMessage()) ;
            log4j.logInfo(JDomUtil.toXML(xmlDoc, "gb2312", true)) ;
        } finally {
            dao.releaseConnection();
        }
    }

}
