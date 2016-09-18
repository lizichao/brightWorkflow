package cn.brightcom.system.pcmc.sm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.xpath.XPath;

import cn.brightcom.jraf.conf.BrightComConfig;
import cn.brightcom.jraf.context.ApplicationContext;
import cn.brightcom.jraf.db.BasicOperation;
import cn.brightcom.jraf.db.DBOprProxy;
import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.db.SqlTypes;
import cn.brightcom.jraf.exception.BrightComException;
import cn.brightcom.jraf.util.ConfigDocument;
import cn.brightcom.jraf.util.Constants;
import cn.brightcom.jraf.util.DaoUtil;
import cn.brightcom.jraf.util.DatetimeUtil;
import cn.brightcom.jraf.util.Log;
import cn.brightcom.jraf.util.PasswordEncoder;
import cn.brightcom.jraf.util.StringUtil;
import cn.brightcom.jraf.util.TipsUtil;
import cn.brightcom.jraf.util.XmlDocPkgUtil;
import cn.brightcom.jraf.web.HttpProcesser;
import cn.brightcom.jraf.web.SwitchCenter;
import cn.brightcom.system.pcmc.sm.l.DefaultValidator;
import cn.brightcom.system.pcmc.sm.l.Validator;
import cn.brightcom.system.pcmc.util.LoginUtil;

/**
 * <p>Title: 系统权限管理</p>
 * <p>Description: 系统权限管理</p>
 * <p>Copyright: Copyright (c) 2009 Brightcom</p>
 * <p>Company: ShenZhen BrightCom Technological Co.,Ltd.</p>
 * @author <a href="http://www.szbrightcom.cn" target="_blank">QIUMH</a>
 * @version 1.0a
 */
public class SmPermissionBean {
	private XmlDocPkgUtil xmlDocUtil = null;
    
    private static Log log4j = new Log(SmPermissionBean.class.getName());
    
    private static String atimes = BrightComConfig.getConfiguration().getString("login.times", "5");
    private static String locktimes = BrightComConfig.getConfiguration().getString("login.locktime", "30");
    
    public Document doPost(Document xmlDoc)
    {
    	xmlDocUtil = new XmlDocPkgUtil(xmlDoc);
    	String action = xmlDocUtil.getAction();
    	
        if ("login".equals(action)) {
            login();
        }
        else if ("logout".equals(action)) {
            logout();
        }
        else if ("getRoleMenuPerms".equals(action))
        {
            getRoleMenuPerms();
        }
        else if ("uptRoleMenuPerms".equals(action))
        {
            uptRoleMenuPerms();
        }
        else if ("getActPermissions".equals(action))
        {
        	getActPermissions();
        }
        else if ("saveActPermissions".equals(action))
        {
            saveActPermissions();
        }
        else if ("getColPerms".equals(action))
        {
        	getColPerms();
        }
        else if ("saveColPerm".equals(action))
        {
        	saveColPerm();
        }
        else if("getRowPerms".equals(action))
        {
        	getRowPerms();
        }
        else if("saveRowPerm".equals(action))
        {
        	saveRowPerm();
        }
        
        return xmlDoc;
    }

    private final void login() {
        Element dataElement = xmlDocUtil.getRequestData();
        Element reqElement = xmlDocUtil.getRequest();
        String usercode = dataElement.getChildTextTrim("usercode");
        String username = dataElement.getChildTextTrim("username");
        String userpwd = dataElement.getChildTextTrim("userpwd");
        try {
            
            Element resultElement = login(usercode, userpwd,username,
            	reqElement.getAttributeValue(Constants.API_SOURCE), reqElement.getAttributeValue("ip"));
            
            resultElement = getUser(resultElement.getChildText("userid"));
            
            xmlDocUtil.setResult("0");
            xmlDocUtil.getResponse().addContent(resultElement);
        }
        catch(BrightComException bce)
        {
        	xmlDocUtil.setResult("-1");
        	xmlDocUtil.writeErrorMsg(bce.getErrorCode(),bce.getMessage());
        }
        catch(Exception ex)
        {
        	ex.printStackTrace();
        	xmlDocUtil.setResult("-1");
        	xmlDocUtil.writeErrorMsg("10104","系统登录异常");
        }
    }
    
    public static Element getUser(String userid)
    	throws Exception
    {
    	StringBuffer sqlBuf = new StringBuffer("select a.*,d.deptid,d.deptcode,d.deptname from pcmc_user a ")
    		.append("left join (select c.userid,b.* from pcmc_dept b,pcmc_user_dept c ")
    		.append("where b.deptid=c.deptid and c.state=?) d on a.userid=d.userid ")
    		.append("where a.userid=?");
        ArrayList bvals = new ArrayList();
        bvals.add("1");
        bvals.add(userid);
        return DaoUtil.getOneRecordData(sqlBuf.toString(), bvals);
    }
    
    public static Element login(String usernm,String userpwd,String username,String source,String ipaddr)
    {
    	Validator va = null;
    	List<Validator> loginFlds = LoginUtil.getLoginField();
    	for(Validator v : loginFlds)
    	{
    		if(v.isCheck(usernm))
    		{
    			va = v;
    			break;
    		}
    	}
    	if(null == va)
    		va = new DefaultValidator();
    	
    	Element userRec = va.query(usernm,username);
    	if(null == userRec)
    		throw new BrightComException("10101","身份证号码和用户名不匹配，请确认是否注册！");
    	
    	long interval = System.currentTimeMillis()
    		-DatetimeUtil.parseDate(userRec.getChildText("lastlogin"), null);    	
    	
    	String state = userRec.getChildText("state");
    	String attempttimes = userRec.getChildText("attempttimes");
    	if("1".equals(state)
    		|| ("7".equals(state) && interval > parseInt(locktimes)*1000))
    	{
    		userpwd = PasswordEncoder.encode(userpwd);
    		String dbuserpwd = userRec.getChildTextTrim("userpwd");
    		if (! (userpwd.equals( (dbuserpwd)))) {
            	if("7".equals(state))
            	{
            		attempttimes = "0";
            	}
            	updateLogin(userRec,"0",attempttimes);
            	//log
            	loginlog(userRec,source,ipaddr,"0");
            	int times = attemptTimes(attempttimes);
            	if(times>0)
            	{
            		throw new BrightComException("10102","密码错误,还可尝试"+times+"次");
            	}
            	else
            	{
            		throw new BrightComException("10103","用户锁定,"+parseInt(locktimes)+"分钟后重新登陆");
            	}
            }
            updateLogin(userRec,"1","0");
            //log
            loginlog(userRec,source,ipaddr,"1");
            return userRec;
    	}
    	
    	//log
    	loginlog(userRec,source,ipaddr,"0");
    	if("7".equals(state)||"8".equals(state))
    	{
    		throw new BrightComException("10103","用户已锁定");
    	}
    	throw new BrightComException("10104","无效用户");
    }
    
    private static int attemptTimes(String attempttimes)
    {
    	int times = parseInt(atimes)-parseInt(attempttimes)-1;
    	return times>0?times:0;
    }
    
    private static int parseInt(String str)
    {
    	if(StringUtil.isNotEmpty(str))
		{
			return new Integer(str).intValue();
		}
    	return 0;
    }
    
    private static void updateLogin(Element userRec,String stateTmp,String attempttimes)
    {
    	PlatformDao pDao = null;
    	try
    	{
    		pDao = new PlatformDao();
    		pDao.beginTransaction();
    		String userid = userRec.getChildText("userid");
    		String extuid = userRec.getChildText("extuid");
    		String stateOld = userRec.getChildText("state");
    		String stateNew = "1";
    		int times = parseInt(attempttimes)+1;
    		
    		Element record = ConfigDocument.createRecordElement("pcmc","pcmc_user_login");
    		record.removeContent();
    		XmlDocPkgUtil.setChildText(record, "userid", userid);
    		XmlDocPkgUtil.setChildText(record, "attempttimes", "0");
    		if(StringUtil.isEmpty(extuid))
    		{
    			pDao.insertOneRecord(record);
    		}
    		XmlDocPkgUtil.setChildText(record, "lastlogin", DatetimeUtil.getNow(null));
    		if("1".equals(stateTmp))
    		{
    			XmlDocPkgUtil.setChildText(record, "prelogon", userRec.getChildText("lastlogon"));
    			XmlDocPkgUtil.setChildText(record, "lastlogon", DatetimeUtil.getNow(null));
    			XmlDocPkgUtil.setChildText(record, "attempttimes", "0");
    			times = 0;
    		}
    		else
    		{
    			XmlDocPkgUtil.setChildText(record, "attempttimes", String.valueOf(times));
    			if(times>=parseInt(atimes))
    			{
    				stateNew = "7";
    			}
    		}
    		
    		pDao.updateOneRecord(record);
    		
    		if(!stateNew.equals(stateOld))
    		{
	    		String sql = "update pcmc_user set state=? where userid=?";
	    		ArrayList bvals = new ArrayList();
    			bvals.add(stateNew);
    			bvals.add(userid);
    			pDao.setSql(sql);
    			pDao.setBindValues(bvals);
    			pDao.executeTransactionSql();
    		}
    		
    		pDao.commitTransaction();
    	}
    	catch(Exception ex)
    	{
    		pDao.rollBack();
    		ex.printStackTrace();
    	}
    	finally
    	{
    		pDao.releaseConnection();
    	}
    }
    
    private static void loginlog(Element userRec,String source,String ipaddr,String state)
    {
    	Element record = ConfigDocument.createRecordElement("pcmc","pcmc_uslogin_log");
    	record.removeContent();
    	XmlDocPkgUtil.setChildText(record, "userid", userRec.getChildText("userid"));
    	XmlDocPkgUtil.setChildText(record, "logindt", DatetimeUtil.getNow(null));
    	XmlDocPkgUtil.setChildText(record, "ipaddr", ipaddr);
    	XmlDocPkgUtil.setChildText(record, "source", source);
    	XmlDocPkgUtil.setChildText(record, "state", state);

    	try
    	{
	    	PlatformDao pDao = new PlatformDao();
	    	pDao.insertOneRecord(record);
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}
    }
    
    private final void logout()
    {
    	xmlDocUtil.setResult("0");
    }
    
    /**
     * 获得指定角色的菜单权限
     */
    private final void getRoleMenuPerms()
    {
        Element reqData = xmlDocUtil.getRequestData();
        Element session = xmlDocUtil.getSession();
        String userid = session.getChildTextTrim("userid") ;
        String usercode = session.getChildTextTrim("usercode");
        String roleid = reqData.getChildTextTrim("roleid") ;
        String subsysid = reqData.getChildTextTrim("subsysid") ;
        
        PlatformDao pDao = null;
        try
        {
        	pDao = new PlatformDao();
        	StringBuffer permissionSql = new StringBuffer() ;
            ArrayList bvals = new ArrayList();
            bvals.add(new Long(roleid));
        	permissionSql.append("select menuid from pcmc_role_menu where roleid=?");
            pDao.setSql(permissionSql.toString());
            pDao.setBindValues(bvals);
            Element pElement = pDao.executeQuerySql(-1, 1);
            
            Element allMenu = MenuBean.getMenuByUserSubsys(userid, usercode, subsysid);
            List menuList = allMenu.getChildren("Record");
            for(int i=0; i<menuList.size(); i++)
            {
            	Element menuRec = (Element)menuList.get(i);
            	XPath xpath = XPath.newInstance("Record[menuid=\""+menuRec.getChildText("menuid")+"\"]");
            	Element pe = (Element)xpath.selectSingleNode(pElement);
            	XmlDocPkgUtil.setChildText(menuRec, "selected", null==pe?"":"checked");
            }
            
            xmlDocUtil.getResponse().addContent(allMenu);
            xmlDocUtil.setResult("0");
        }
        catch (Exception e)
        {
            e.printStackTrace() ;
            log4j.logError(e);
            xmlDocUtil.writeErrorMsg("10107","查询指定角色菜单权限列表异常");
        }
        finally
        {
            pDao.releaseConnection();
        }
    }
    
    private final void uptRoleMenuPerms()
    {
    	Element reqData = xmlDocUtil.getRequestData();
        String roleid = reqData.getChildTextTrim("roleid");
        List menuList = reqData.getChildren("menuid");
        PlatformDao pDao = null;
        try
        {
        	pDao = new PlatformDao();
            pDao.beginTransaction();
            StringBuffer sql = new StringBuffer();
            ArrayList bvals = new ArrayList();
            sql.append("delete from pcmc_role_menu where roleid=?");
            bvals.add(new Long(roleid));
            pDao.setSql(sql.toString());
            pDao.setBindValues(bvals);
            pDao.executeTransactionSql();
            for(int i=0; i<menuList.size(); i++)
            {
                Element rec = ConfigDocument.createRecordElement("pcmc", "pcmc_role_menu");
                XmlDocPkgUtil.copyValues(reqData, rec, i);
                XmlDocPkgUtil.setChildText(rec, "roleid", roleid);
                pDao.insertOneRecord(rec);
            }
            pDao.commitTransaction();
            xmlDocUtil.setResult("0");
            xmlDocUtil.writeHintMsg("10105","角色菜单授权成功");
        }
        catch (Exception e)
        {
            pDao.rollBack();
            e.printStackTrace();
            log4j.logError(e);
            xmlDocUtil.writeErrorMsg("10106","角色菜单授权异常");
        }
        finally
        {
            pDao.releaseConnection();
        }
    }
    
    //取角色操作权限列表
    private final void getActPermissions()
    {
    	Element reqData = xmlDocUtil.getRequestData();
    	String subsysid = reqData.getChildTextTrim("subsysid");
    	String roleid = reqData.getChildTextTrim("roleid");
    	String sysName = null;
    	
    	PlatformDao pDao = null;
    	try
    	{
    		pDao = new PlatformDao();
    		//取sysName
    		StringBuffer sqlBuf = new StringBuffer()
    			.append("select shortname from pcmc_subsys where subsysid=?");
    		ArrayList bvals = new ArrayList();
    		bvals.add(new Long(subsysid));
    		pDao.setSql(sqlBuf.toString());
    		pDao.setBindValues(bvals);
    		Element sysData = pDao.executeQuerySql(1,1);
    		Element sysRec = sysData.getChild("Record");
    		if(null == sysRec)
    		{
    			xmlDocUtil.writeErrorMsg("子系统未定义");
    			return;
    		}
    		sysName = sysRec.getChildText("shortname");
    		
    		//取role_act
    		sqlBuf.setLength(0);
    		sqlBuf.append("select * from pcmc_role_act where roleid=?");
    		bvals.clear();
    		bvals.add(new Long(roleid));
    		pDao.setSql(sqlBuf.toString());
    		pDao.setBindValues(bvals);
    		Element actData = pDao.executeQuerySql(-1,1);
    		List ractList = actData.getChildren("Record");
    		
    		//取操作定义
    		String[] flds = {"id","pid","sysname","oprid","action","desc","selected","colacc","rowacc","acctext"};
    		Element resData = XmlDocPkgUtil.createMetaData(flds);
    		
    		Hashtable oprMap = ConfigDocument.getSystemConfig(sysName).getOprConfig();
        	Collection oprList = oprMap.values();
        	Iterator it = oprList.iterator();
        	while(it.hasNext())
        	{
        		Element oprEle = (Element)it.next();
        		String oprid = oprEle.getAttributeValue("id");
        		String oprDesc = oprEle.getAttributeValue("desc");
        		List actList = oprEle.getChildren("Action");
        		List accList = new ArrayList();
        		boolean aflag = false;
        		for(int i=0; i<actList.size(); i++)
        		{
        			Element actEle = (Element)actList.get(i);
        			String actName = actEle.getAttributeValue("name");
        			String actDesc = actEle.getAttributeValue("desc");
        			String accredit = actEle.getAttributeValue("accredit");
        			String colacc = actEle.getAttributeValue("colacc");
        			String rowacc = actEle.getAttributeValue("rowacc");
        			String webservice = actEle.getAttributeValue("webservice");
        			
        			if(!"true".equals(accredit))
        				continue;
        			aflag=true;
        			String selected="";
        			for(int j=ractList.size()-1;j>=0; j--)
        			{
        				Element rActEle = (Element)ractList.get(j);
        				if(oprid.equals(rActEle.getChildText("oprid"))
        					&& actName.equals(rActEle.getChildText("action")))
        				{
        					selected="checked";
        					ractList.remove(j);
        					break;
        				}
        			}
        			StringBuffer acctext=new StringBuffer();
        			if("true".equals(colacc))
        			{
        				acctext.append("列|");
        			}
        			if("true".equals(rowacc))
        			{
        				acctext.append("行|");
        			}
        			if("true".equals(webservice))
        			{
        				acctext.append("WS|");
        			}
        			String[] vals = {oprid+"."+actName,oprid,sysName,oprid,actName,
        				actDesc,selected,colacc,rowacc,acctext.toString()};
        			accList.add(XmlDocPkgUtil.createRecord(flds, vals));
        		}
        		if(aflag)
        		{
        			String[] vals = {oprid,"-1",sysName,oprid,"",oprDesc,"","","",""};
        			resData.addContent(XmlDocPkgUtil.createRecord(flds, vals));
        			resData.addContent(accList);
        		}
        	}
        	xmlDocUtil.setResult("0");
        	xmlDocUtil.getResponse().addContent(resData);
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    		xmlDocUtil.writeErrorMsg("10108","查询角色操作权限异常");
    	}
    	finally
    	{
    		pDao.releaseConnection();
    	}
    }
    
    //给角色交易授权
    private final void saveActPermissions()
    {
        Element dataElement = xmlDocUtil.getRequestData();
        String roleid = dataElement.getChildTextTrim("roleid");
        java.util.List oprList = dataElement.getChildren("chkOpr");
        String oprid = "";
        String action = "";
        PlatformDao dao = null;
        StringBuffer sql = new StringBuffer();
        ArrayList bvals = new ArrayList();
        try
        {
        	dao = new PlatformDao();
            dao.beginTransaction();
            sql.append("delete from pcmc_role_act where roleid = ?");
            dao.setSql(sql.toString());
            bvals.add(SqlTypes.getConvertor("long").convert(roleid, null));
            dao.setBindValues(bvals);
            dao.executeTransactionSql();
            sql.setLength(0);
            sql.append("insert into pcmc_role_act(roleactid,roleid,oprid,action) values(?,?,?,?)");
            dao.setSql(sql.toString());
            for (int i = 0; i < oprList.size(); i ++)
            {
                Element oprElement = (Element)oprList.get(i);
                String [] opr = oprElement.getTextTrim().split(",");
                oprid = opr[0];
                action = opr[1];
                bvals = new ArrayList();
                bvals.add(new Long(DBOprProxy.getNextSequenceNumber("pcmc_role_act")));
                bvals.add(SqlTypes.getConvertor("long").convert(roleid,null));
                bvals.add(oprid);
                bvals.add(action);
                dao.addBatch(bvals);
            }
            if(0<oprList.size())
            	dao.executeBatch();
            dao.commitTransaction();
            xmlDocUtil.setResult("0");
            xmlDocUtil.writeHintMsg("10109","角色交易授权成功");
        }
        catch (Exception e)
        {
            dao.rollBack();
            e.printStackTrace();
            log4j.logError(e);
            xmlDocUtil.writeErrorMsg("10110","角色交易授权异常");
        }
        finally
        {
            dao.releaseConnection();
        }
    }
    
    //判断有否Action执行权限
    public static boolean checkActionPms(Document xmlDoc)
    {
        String oprID = xmlDoc.getRootElement().getChild("OprID").getTextTrim();
        String action = xmlDoc.getRootElement().getChild("Action").getTextTrim();
        String sysName = xmlDoc.getRootElement().getChild("SysName").getTextTrim();
        String userid = xmlDoc.getRootElement().getChild("Session").getChildTextTrim("userid");
        PlatformDao dao = null;
        try
        {
        	ApplicationContext.regSubSys("pcmc");
        	dao = new PlatformDao();
        	StringBuffer ridBuf = new StringBuffer();
			int ridlen = UserRoleBean.userRoleIDQuery(ridBuf);
        	StringBuffer sqlBuf = new StringBuffer()
        		.append("select a.* from pcmc_role_act a,pcmc_role b,")
        		.append(ridBuf).append(" c,pcmc_subsys d ")
        		.append("where a.roleid=b.roleid and b.roleid=c.roleid ")
        		.append("and b.subsysid=d.subsysid ")
        		.append("and d.shortname=? and a.oprid=? and a.action=?");
        	ArrayList bvals = new ArrayList();
        	for(int i=0;i<ridlen; i++)
			{
				bvals.add(userid);
			}
        	bvals.add(sysName);
        	bvals.add(oprID);
        	bvals.add(action);
            dao.setSql(sqlBuf.toString());
            dao.setBindValues(bvals);
            Element ele = dao.executeQuerySql(-1,1);
            int count = Integer.parseInt(ele.getChild("PageInfo").getChildTextTrim("RecordCount"));
            return (count > 0);
        }
        catch (Exception e)
        {
            return false;
        }
        finally
        {
            dao.releaseConnection();
        }
    }
    
    /**
     * 取列级授权列表
     */
    private final void getColPerms()
    {
    	Element reqData = xmlDocUtil.getRequestData();
        String roleid = reqData.getChildTextTrim("roleid");
        String sys = reqData.getChildText("sys");
    	String opr = reqData.getChildText("opr");
    	String act = reqData.getChildText("act");
    	
    	PlatformDao pDao = null;
    	try
    	{
    		pDao = new PlatformDao();
    		//取交易Response Field列表
    		Document qxmlDoc = HttpProcesser.createRequestPackage("pcmc", "sys", "loadFields");
    		Element qReqData = qxmlDoc.getRootElement().getChild("Request").getChild("Data");
    		XmlDocPkgUtil.setChildText(qReqData, "sys", sys);
    		XmlDocPkgUtil.setChildText(qReqData, "opr", opr);
    		XmlDocPkgUtil.setChildText(qReqData, "act", act);
    		XmlDocPkgUtil.setChildText(qReqData, "type", "res");
    		
    		Document rxmlDoc = SwitchCenter.doPost(qxmlDoc,true);
			XmlDocPkgUtil qxmlDocUtil = new XmlDocPkgUtil(rxmlDoc);
			if(!"0".equals(qxmlDocUtil.getResult()))
			{
				xmlDocUtil.writeErrorMsg("10111","取交易Response Field列表异常");
				xmlDocUtil.setResult("-1");
				return;
			}
			//取角色交易列级权限
			StringBuffer sqlBuf = new StringBuffer();
			sqlBuf.append("select fldname from pcmc_role_col where roleid=? ")
				.append("and sysname=? and oprname=? and actname=?");
			ArrayList bvals = new ArrayList();
			bvals.add(new Long(roleid));
			bvals.add(sys);
			bvals.add(opr);
			bvals.add(act);
			pDao.setSql(sqlBuf.toString());
			pDao.setBindValues(bvals);
			Element roleData = pDao.executeQuerySql(-1,1);
			
			//合并数据
			Element resData = XmlDocPkgUtil.createMetaData(new String[]{"name","title","colacc"});
			List fldList = qxmlDocUtil.getResponse().getChild("Data").getChildren("Record");
			for(int i=0; i<fldList.size(); i++)
			{
				Element fldrec = (Element)fldList.get(i);
				String fldname = fldrec.getChildText("name");
				Element newrec = new Element("Record");
				XmlDocPkgUtil.setChildText(newrec, "name", fldname);
				XmlDocPkgUtil.setChildText(newrec, "title", fldrec.getChildText("title"));
				
				XPath xpath = XPath.newInstance("Record[fldname=\""+fldname+"\"]");
            	Element pe = (Element)xpath.selectSingleNode(roleData);
            	XmlDocPkgUtil.setChildText(newrec, "colacc", null==pe?"":"true");
            	
            	resData.addContent(newrec);
			}
			
			xmlDocUtil.getResponse().addContent(resData);
			xmlDocUtil.setResult("0");
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    		log4j.logError(ex);
    		xmlDocUtil.writeErrorMsg("10112","取列级授权字段列表异常");
    	}
    }
    
    private final void saveColPerm()
    {
    	Element reqData = xmlDocUtil.getRequestData();
    	String roleid = reqData.getChildTextTrim("roleid");
        String sys = reqData.getChildText("sys");
    	String opr = reqData.getChildText("opr");
    	String act = reqData.getChildText("act");
    	
    	PlatformDao pDao = null;
    	try
    	{
    		pDao = new PlatformDao();
    		pDao.beginTransaction();
    		
    		//删除权限
    		StringBuffer sqlBuf = new StringBuffer("delete from pcmc_role_col where ")
    			.append("roleid=? and sysname=? and oprname=? and actname=?");
    		ArrayList bvals = new ArrayList();
    		bvals.add(new Long(roleid));
    		bvals.add(sys);
    		bvals.add(opr);
    		bvals.add(act);
    		
    		pDao.setSql(sqlBuf.toString());
    		pDao.setBindValues(bvals);
    		pDao.executeTransactionSql();
    		
    		Element dataEle = new Element("Data");
    		List nmList = reqData.getChildren("name");
    		List colaccList = reqData.getChildren("colacc");
    		for(int i=0; i<nmList.size(); i++)
    		{
    			String fldname = ((Element)nmList.get(i)).getText();
    			String colacc = ((Element)colaccList.get(i)).getText();
    			if("true".equals(colacc))
    			{
    				dataEle.addContent(new Element("fldname").setText(fldname));
    			}
    		}
    		if(0<dataEle.getContentSize())
    		{
    			XmlDocPkgUtil.setChildText(dataEle, "roleid",roleid);
    			XmlDocPkgUtil.setChildText(dataEle, "sysname",sys);
    			XmlDocPkgUtil.setChildText(dataEle, "oprname",opr);
    			XmlDocPkgUtil.setChildText(dataEle, "actname",act);
    			
    			BasicOperation.insert("pcmc", "pcmc_role_col", dataEle);
    		}
    		
    		pDao.commitTransaction();
    		xmlDocUtil.setResult("0");
    		xmlDocUtil.writeHintMsg("10113","角色列级权限成功");
    	}
    	catch(Exception ex)
    	{
    		pDao.rollBack();
    		ex.printStackTrace();
    		log4j.logError(ex);
            xmlDocUtil.writeErrorMsg("10114","角色列级授权异常");
    	}
    	finally
    	{
    		pDao.releaseConnection();
    	}
    }
    
    //过滤列级权限数据
    public static void checkColPms(Document xmlDoc)
    {
    	XmlDocPkgUtil xmlDocUtil = new XmlDocPkgUtil(xmlDoc);
    	Element data = xmlDocUtil.getResponse().getChild("Data");
    	if(null == data)
    		return;
    	List recList = data.getChildren("Record");
    	if(0==recList.size())
    		return;
    	
    	PlatformDao pDao = null;
        try
        {
        	ApplicationContext.regSubSys("pcmc");
        	pDao = new PlatformDao();
        	
        	//查询列级权限
        	String sysname = xmlDocUtil.getSysName();
        	String oprname = xmlDocUtil.getOprID();
        	String actname = xmlDocUtil.getAction();
        	String userid = xmlDocUtil.getSession().getChildText("userid");
        	
        	StringBuffer ridBuf = new StringBuffer();
			int ridlen = UserRoleBean.userRoleIDQuery(ridBuf);
			
        	StringBuffer sqlBuf = new StringBuffer();
        	sqlBuf.append("select a.fldname from pcmc_role_col a,")
        		.append(ridBuf).append(" r ")
        		.append("where a.roleid=r.roleid and a.sysname=? ")
        		.append("and a.oprname=? and a.actname=?");
        	ArrayList bvals = new ArrayList();
        	for(int i=0;i<ridlen; i++)
			{
				bvals.add(userid);
			}
        	bvals.add(sysname);
        	bvals.add(oprname);
        	bvals.add(actname);
        	
        	pDao.setSql(sqlBuf.toString());
        	pDao.setBindValues(bvals);
        	
        	Element qData = pDao.executeQuerySql(-1, 1);
        	ArrayList fldPms = new ArrayList();
        	List qList = qData.getChildren("Record");
        	for(int i=0; i<qList.size(); i++)
        	{
        		Element ele = (Element)qList.get(i);
        		fldPms.add(ele.getChildText("fldname"));
        	}
        	//无权限
        	if(0==fldPms.size())
        		data.removeChildren("Record");
        	
        	//过滤数据
        	for(int i=0; i<recList.size(); i++)
        	{
        		Element rec = (Element)recList.get(i);
        		List fList = rec.getChildren();
        		for(int j=fList.size()-1; j>=0; j--)
        		{
        			Element fldEle = (Element)fList.get(j);
        			if(!fldPms.contains(fldEle.getName()))
        			{
        				rec.removeContent(fldEle);
        			}
        		}
        	}
        }
        catch(Exception ex)
        {
        	ex.printStackTrace();
        	//列级权限数据处理异常
        	xmlDocUtil.writeErrorMsg(TipsUtil.getSysMsgByCode("00012"));
        }
        finally
        {
        	pDao.releaseConnection();
        }
    }
    
    private final void getRowPerms()
    {
    	Element reqData = xmlDocUtil.getRequestData();
        String sys = reqData.getChildText("sys");
    	String opr = reqData.getChildText("opr");
    	String act = reqData.getChildText("act");
    	
    	PlatformDao pDao = null;
    	try
    	{
    		pDao = new PlatformDao();
    		StringBuffer sqlBuf = new StringBuffer();
    		sqlBuf.append("select a.rolerowid,a.roleid,a.sysname as __sysname,")
    			.append("a.oprname,a.actname,a.sqlwhere")
    			.append(",r.rolename from pcmc_role_row a,pcmc_role r ")
    			.append("where a.roleid=r.roleid ")
    			.append("and a.sysname=? and a.oprname=? and a.actname=?");
    		ArrayList bvals = new ArrayList();
    		bvals.add(sys);
    		bvals.add(opr);
    		bvals.add(act);
    		
    		pDao.setSql(sqlBuf.toString());
    		pDao.setBindValues(bvals);
    		Element data = pDao.executeQuerySql(-1, 1);
    		
    		xmlDocUtil.getResponse().addContent(data);
    		xmlDocUtil.setResult("0");
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    		xmlDocUtil.writeErrorMsg("10115","查询角色行级权限异常");
    	}
    	finally
    	{
    		pDao.releaseConnection();
    	}
    }
    
    private final void saveRowPerm()
    {
    	Element reqData = xmlDocUtil.getRequestData();
    	String roleid = reqData.getChildText("roleid");
        String sys = reqData.getChildText("__sysname");
    	String opr = reqData.getChildText("oprname");
    	String act = reqData.getChildText("actname");
    	String sqlwhere = reqData.getChildTextTrim("sqlwhere");
    	
    	PlatformDao pDao = null;
    	try
    	{
    		pDao = new PlatformDao();
    		pDao.beginTransaction();
    		StringBuffer sqlBuf = new StringBuffer();
    		sqlBuf.append("delete from pcmc_role_row ")
    			.append("where roleid=? ")
    			.append("and sysname=? and oprname=? and actname=?");
    		ArrayList bvals = new ArrayList();
    		bvals.add(new Long(roleid));
    		bvals.add(sys);
    		bvals.add(opr);
    		bvals.add(act);
    		
    		pDao.setSql(sqlBuf.toString());
    		pDao.setBindValues(bvals);
    		pDao.executeTransactionSql();
    		
    		if(StringUtil.isNotEmpty(sqlwhere))
    		{
	    		Element rec = ConfigDocument.createRecordElement("pcmc", "pcmc_role_row");
	            XmlDocPkgUtil.copyValues(reqData, rec, 0);
	            XmlDocPkgUtil.setChildText(rec, "sysname", sys);
	            pDao.insertOneRecord(rec);
    		}
    		
            pDao.commitTransaction();
    		xmlDocUtil.setResult("0");
    		xmlDocUtil.writeHintMsg("10117","更新角色行级权限成功");
    	}
    	catch(Exception ex)
    	{
    		pDao.rollBack();
    		ex.printStackTrace();
    		xmlDocUtil.writeErrorMsg("10118","更新角色行级权限异常");
    	}
    	finally
    	{
    		pDao.releaseConnection();
    	}
    }
    
    public static String checkRowPms(String userid,String sys,String opr,String act)
    	throws Exception
    {
    	StringBuffer rpmBuf = new StringBuffer();
    	PlatformDao pDao = null;
    	try
    	{
    		ApplicationContext.regSubSys("pcmc");
    		pDao = new PlatformDao();
    		StringBuffer ridBuf = new StringBuffer();
			int ridlen = UserRoleBean.userRoleIDQuery(ridBuf);
			
    		StringBuffer sqlBuf = new StringBuffer();
    		sqlBuf.append("select a.sqlwhere from pcmc_role_row a,")
    			.append(ridBuf).append(" c ")
    			.append("where a.roleid=c.roleid ")
    			.append("and a.sysname=? and a.oprname=? and a.actname=?");
    		ArrayList bvals = new ArrayList();
    		for(int i=0;i<ridlen; i++)
			{
				bvals.add(userid);
			}
    		bvals.add(sys);
    		bvals.add(opr);
    		bvals.add(act);
    		
    		pDao.setSql(sqlBuf.toString());
    		pDao.setBindValues(bvals);
    		Element data = pDao.executeQuerySql(-1,1);
    		List list = data.getChildren("Record");
    		for(int i=0; i<list.size(); i++)
    		{
    			if(0==i)
    				rpmBuf.append("(");
    			Element rec = (Element)list.get(i);
    			rpmBuf.append("(").append(rec.getChildText("sqlwhere")).append(")")
    				.append(i==list.size()-1?")":" or ");    			
    		}
    	}
    	finally
    	{
    		pDao.releaseConnection();
    	}
    	
    	return rpmBuf.toString();
    }
}
