package cn.brightcom.system.pcmc.sys;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.jdom.Document;
import org.jdom.Element;

import cn.brightcom.jraf.conf.SystemConfig;
import cn.brightcom.jraf.context.ApplicationContext;
import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.util.ConfigDocument;
import cn.brightcom.jraf.util.Log;
import cn.brightcom.jraf.util.SessionFactory;
import cn.brightcom.jraf.util.StringUtil;
import cn.brightcom.jraf.util.XmlDocPkgUtil;
import cn.brightcom.jraf.web.HttpProcesser;
import cn.brightcom.jraf.web.SwitchCenter;


/**
 * <p>Title: 系统文件配置管理</p>
 * <p>Description: 系统文件配置管理</p>
 * <p>Copyright: Copyright (c) 2009 Brightcom</p>
 * <p>Company: ShenZhen BrightCom Technological Co.,Ltd.</p>
 * @author <a href="http://www.szbrightcom.cn" target="_blank">QIUMH</a>
 * @version 1.0a
 */
public class SystemCfgBean
{
	private XmlDocPkgUtil xmlDocUtil = null;
	
	private String[] node_flds = {"sid","pid","__sysName","enname","cnname","stype","isleaf","nodepath","icon"};
	private String[] tbl_flds = {"name","title","type","maxlen","minlen","notnull","pk","gen"};
    private String[] act_flds = {"name","title","type","maxlen","minlen","notnull","pk","gen","refname","op","defaultval","reqclass","format"};
	private String[] act_atts = {"actname","base","old","desc","accredit","colacc","rowacc","log","nologin","webservice","oauth"};
	private String[] act_qrys = {"basesql","orderby","groupby","tblname"};
	private String[] act_a_qs = {"actname","base","old","desc","accredit","colacc","rowacc","log","nologin","webservice","oauth","basesql","orderby","groupby","tblname"};
	private String[] act_flows = {"flowresult","flowtype","flowpath"};
	private String[] act_msgs = {"msgcode","msgdesc"};
	private String[] opr_flds = {"__sysName","__oprID","bean","jndi","desc"};
		
    private static Log log4j = new Log(SystemCfgBean.class.getName());
    
    public Document doPost(Document xmlDoc)
    {
    	xmlDocUtil = new XmlDocPkgUtil(xmlDoc);
    	String action = xmlDocUtil.getAction();
    	
    	//取子系统列表 sysName
    	if ("getSysList".equals(action))
        {
    		getSysList();
        }
    	//取操作列表 按 sysName/oprID/action生成树
    	else if ("getOprList".equals(action))
        {
    		getOprList();
        }
    	//取Table列表 按 sysName/Table生成树
    	else if ("getTblList".equals(action))
        {
    		getTblList();
        }
    	else if ("createOprFile".equals(action))
        {
    		createOprFile();
        }
    	else if ("createTblFile".equals(action))
        {
    		createTblFile();
        }
    	else if ("getOprDetail".equals(action))
        {
    		getOprDetail();
        }
    	else if ("getActDetail".equals(action))
        {
    		getActDetail();
        }
    	else if ("getTblDetail".equals(action))
        {
    		getTblDetail();
        }
    	else if ("delOpr".equals(action))
        {
    		delOpr();
        }
    	else if ("uptOpr".equals(action))
        {
    		uptOpr();
        }
    	else if ("delAct".equals(action))
        {
    		delAct();
        }
    	else if ("uptAct".equals(action))
        {
    		uptAct();
        }
    	else if ("uptTbl".equals(action))
        {
    		uptTbl();
        }
        else if ("loadFields".equals(action))
        {
        	loadFields();
        }
    	    	
    	return xmlDoc;
    }
    
    /**
     * 取子系统列表
     */
    private final void getSysList()
    {
    	String[] sys_flds = {"subsysid","shortname","cnname"};
    	Element resData = XmlDocPkgUtil.createMetaData(sys_flds);
    	
    	Hashtable sysMap = ConfigDocument.getSysMap();
    	Iterator it = sysMap.values().iterator();
    	
    	while(it.hasNext())
    	{
    		SystemConfig sysCfg = (SystemConfig)it.next();
    		String[] vals = {sysCfg.getSysName(),sysCfg.getSysName(),sysCfg.getDesc()};
    		resData.addContent(XmlDocPkgUtil.createRecord(sys_flds, vals));
    	}
    	
    	xmlDocUtil.getResponse().addContent(resData);
    	xmlDocUtil.setResult("0");
    }
    
    /**
     * 取Operation定义，树状结构用
     */
    private final void getOprList()
    {
    	try
    	{
	    	/*
	    	 * 定义返回结构体
	    	 * stype:节点类型 (1:sys,2:opr,3:act,4:file)
	    	 */
	    	Element resData = XmlDocPkgUtil.createMetaData(node_flds);
	    	
	    	Element reqData = xmlDocUtil.getRequestData();
	    	String sysName = null;
	    	/**
	    	 * __qtype:
	    	 * 1: sys->opr->act
	    	 * 2: sys->file->opr->act
	    	 * 3: sys  file->opr->act
	    	 */
	    	String queryTp = reqData.getChildText("__qtype");
	    	String pid = reqData.getChildText("pid");
	    	String stype = reqData.getChildText("stype");
	    		    	
	    	sysName = reqData.getChildText("__sysName");
	    	List dlist = null;
	    	
	    	if("-1".equals(pid) && !"3".equals(queryTp))
	    	{
	    		dlist = genSysList();
	    	}
	    	else if(("-1".equals(pid) && "3".equals(queryTp))
	    		|| ("1".equals(stype) && "2".equals(queryTp)))
	    	{
	    		dlist = genFileList(sysName,pid,0);
	    	}
	    	else if("2".equals(stype))
			{
	    		dlist = genActList(sysName,pid);
			}
	    	else
	    	{
	    		dlist = genOprList(sysName,pid);
	    	}
	    	
	    	resData.addContent(sortList("enname",dlist));
	    	
	    	xmlDocUtil.getResponse().addContent(resData);
	    	xmlDocUtil.setResult("0");
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    		xmlDocUtil.writeErrorMsg("10001","取Operation定义异常");
    	}
    }
    
    /**
     * 取Table定义，树状结构用
     */
    private final void getTblList()
    {
    	try
    	{
	    	/*
	    	 * 定义返回结构体
	    	 * stype:节点类型 (1:sys,2:opr,3:act,4:file,5:tbl)
	    	 */
	    	Element resData = XmlDocPkgUtil.createMetaData(node_flds);
	    	
	    	Element reqData = xmlDocUtil.getRequestData();
	    	String sysName = null;
	    	/**
	    	 * __qtype:
	    	 * 1: sys->tbl
	    	 * 2: sys->file->tbl
	    	 * 3: sys  file->tbl
	    	 * 4: sys  tbl
	    	 */
	    	String queryTp = reqData.getChildText("__qtype");
	    	String pid = reqData.getChildText("pid");
	    	String stype = reqData.getChildText("stype");	    	
	    	sysName = reqData.getChildText("__sysName");
	    	
	    	List dlist = null;
	    	
	    	if("-1".equals(pid) && !"3".equals(queryTp) && !"4".equals(queryTp))
	    	{
	    		dlist = genSysList();
	    	}
	    	else if(("-1".equals(pid) && "3".equals(queryTp))
	    		|| ("1".equals(stype) && "2".equals(queryTp)))
	    	{
	    		dlist = genFileList(sysName,pid,1);
	    	}
	    	else
	    	{
	    		dlist = genTblList(sysName,pid,"4".equals(queryTp));
	    	}
	    	
	    	resData.addContent(sortList("enname",dlist));
	    	
	    	xmlDocUtil.getResponse().addContent(resData);
	    	xmlDocUtil.setResult("0");
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    		xmlDocUtil.writeErrorMsg("10002","取Table定义异常");
    	}
    }
    
    /**
     * 取子系统列表
     * @return
     */
    private final ArrayList genSysList()
    {
    	ArrayList l = new ArrayList();
    	Hashtable sysMap = ConfigDocument.getSysMap();
    	Iterator it = sysMap.values().iterator();
    	
    	while(it.hasNext())
    	{
    		SystemConfig sysCfg = (SystemConfig)it.next();
    		String sysName = sysCfg.getSysName();
    		String[] vals = {sysName,"-1",sysName,
    			sysCfg.getSysName(),sysCfg.getDesc(),"1","false",sysName,""};
    		l.add(XmlDocPkgUtil.createRecord(node_flds, vals));
    	}
    	
    	return l;
    }
    
    /**
     * 取子系统Operation、Table配置文件列表
     * @param sysName 子系统名
     * @param pid 上级节点ID
     * @param oprOrTbl (0:Operation,1:Table)
     * @return
     */
    private final ArrayList genFileList(String sysName,String pid,int oprOrTbl)
    {
    	ArrayList l = new ArrayList();
    	Hashtable oprFileMap = null;
    	if(0==oprOrTbl)
    		oprFileMap = ConfigDocument.getSystemConfig(sysName).getOprFile();
    	else
    		oprFileMap = ConfigDocument.getSystemConfig(sysName).getTableFile();
    	Iterator it = oprFileMap.keySet().iterator();
    	
    	while(it.hasNext())
    	{
    		l.add(genFileRecord((String)it.next(),sysName,pid));
    	}
    	
    	return l;
    }
    
    private final Element genFileRecord(String filePath,String sysName,String pid)
    {
    	StringBuffer idBuf = new StringBuffer();
    	if(!"-1".equals(pid))
    	{
    		idBuf.append(pid).append("|");
    	}
    	String filenm = filePath.substring(filePath.lastIndexOf('/')+1);
		StringBuffer nodepath = new StringBuffer(sysName)
			.append("/").append(filenm);
		String[] vals = {idBuf.toString()+filePath,pid,sysName,"",filenm,"4","false",nodepath.toString(),""};
		return XmlDocPkgUtil.createRecord(node_flds, vals);
    }
    
    /**
     * 取Operation列表
     * @param sysName 所属子系统
     * @param pid 上级节点ID(子系统/文件)
     * @return
     */
    private final ArrayList genOprList(String sysName,String pid)
    {
    	ArrayList l = new ArrayList();
    	Collection oprList = null;
    	if(sysName.equals(pid))
    	{
    		Hashtable oprMap = ConfigDocument.getSystemConfig(sysName).getOprConfig();
    		oprList = oprMap.values();
    	}
    	else
    	{
    		int idx = pid.lastIndexOf("|");
    		String fname = pid.substring(idx+1);
    		Document oprDoc = (Document)ConfigDocument.getSystemConfig(sysName)
    			.getOprFile().get(fname);
    		oprList = oprDoc.getRootElement().getChildren("Operation");
    	}
    	Iterator it = oprList.iterator();
    	while(it.hasNext())
    	{
    		l.add(genOprRecord((Element)it.next(),sysName,pid));
    	}
    	
    	return l;
    }
    
    private final Element genOprRecord(Element oprEle,String sysName,String pid)
    {
    	StringBuffer nodepath = new StringBuffer(sysName)
			.append("/").append(oprEle.getAttributeValue("id"));
		String[] vals = {pid+"|"+oprEle.getAttributeValue("id"),pid,sysName,
			oprEle.getAttributeValue("id"),oprEle.getAttributeValue("desc"),"2",
			"false",nodepath.toString(),""}; 
		return XmlDocPkgUtil.createRecord(node_flds, vals);
    }
    
    /**
     * 取Action列表
     * @param sysName 所属子系统
     * @param pid Operation
     * @return
     */
    private final ArrayList genActList(String sysName,String pid)
    {
    	ArrayList l = new ArrayList();
    	int idx = pid.lastIndexOf("|");
		String oprID = pid.substring(idx+1);
    	Element oprEle = ConfigDocument.getOprElement(sysName, oprID);
    	if(null == oprEle) return l;
    	Collection actList = oprEle.getChildren("Action");
    	Iterator it = actList.iterator();
    	while(it.hasNext())
    	{
    		l.add(genActRecord((Element)it.next(),sysName,oprID,pid));
    	}
    	return l;
    }
    
    private final Element genActRecord(Element actEle,String sysName,String oprID,String pid)
    {
    	StringBuffer nodepath = new StringBuffer(sysName)
			.append("/").append(oprID).append("/").append(actEle.getAttributeValue("name"));
    	String icon="true".equals(actEle.getAttributeValue("accredit"))?"application-key":"";
		String[] vals = {pid+"|"+actEle.getAttributeValue("name"),pid,sysName,
			actEle.getAttributeValue("name"),actEle.getAttributeValue("desc"),"3",
			"true",nodepath.toString(),icon};
		return XmlDocPkgUtil.createRecord(node_flds, vals);
    }
    
    /**
     * 取Table列表
     * @param sysName 所属子系统
     * @param pid 上级节点ID(子系统/文件)
     * @return
     */
    private final ArrayList genTblList(String sysName,String pid,boolean all)
    {
    	ArrayList l = new ArrayList();
    	Collection tblList = null;
    	if(sysName.equals(pid) || all)
    	{
    		Hashtable tblMap = ConfigDocument.getSystemConfig(sysName).getTableConfig();
    		tblList = tblMap.values();
    	}
    	else
    	{
    		int idx = pid.lastIndexOf("|");
    		String fname = pid.substring(idx+1);
    		Document tblDoc = (Document)ConfigDocument.getSystemConfig(sysName)
    			.getTableFile().get(fname);
    		tblList = tblDoc.getRootElement().getChildren("Table");
    	}
    	Iterator it = tblList.iterator();
    	while(it.hasNext())
    	{
    		Element tblEle = (Element)it.next();
    		StringBuffer nodepath = new StringBuffer(sysName)
				.append("/").append(tblEle.getAttributeValue("name"));
    		String[] vals = {pid+"|"+tblEle.getAttributeValue("name"),pid,sysName,
    			tblEle.getAttributeValue("name"),tblEle.getAttributeValue("desc"),"5",
    			"true",nodepath.toString(),""};
    		l.add(XmlDocPkgUtil.createRecord(node_flds, vals));
    	}
    	
    	return l;
    }
    
    /**
     * 新增Operation配置文件
     */
    private final void createOprFile()
    {
    	try
    	{
    		Element reqData = xmlDocUtil.getRequestData();
        	String sysName = reqData.getChildText("__sysName");
    		String fname = reqData.getChildText("fname");
    		String pid = reqData.getChildText("pid");
    		
    		Document fDoc = ConfigDocument.getSystemConfig(sysName).createOprFile(fname);
    		String fpath = fDoc.getRootElement().getAttributeValue(SystemConfig.CONFIG_ATTR);
    		
    		Element resData = XmlDocPkgUtil.createMetaData(node_flds);
    		resData.addContent(genFileRecord(fpath, sysName, pid));
    		
    		xmlDocUtil.getResponse().addContent(resData);
    		xmlDocUtil.setResult("0");
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    		xmlDocUtil.writeErrorMsg("10003","新增Operation定义文件异常");
    	}
    }
    
    /**
     * 新增Table配置文件
     */
    private final void createTblFile()
    {
    	try
    	{
    		Element reqData = xmlDocUtil.getRequestData();
        	String sysName = reqData.getChildText("__sysName");
    		String fname = reqData.getChildText("fname");
    		String pid = reqData.getChildText("pid");
    		
    		Document fDoc = ConfigDocument.getSystemConfig(sysName).createTblFile(fname);
    		String fpath = fDoc.getRootElement().getAttributeValue(SystemConfig.CONFIG_ATTR);
    		
    		Element resData = XmlDocPkgUtil.createMetaData(node_flds);
    		resData.addContent(genFileRecord(fpath, sysName, pid));
    		
    		xmlDocUtil.getResponse().addContent(resData);
    		xmlDocUtil.setResult("0");
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    		xmlDocUtil.writeErrorMsg("10004","新增Table定义文件异常");
    	}
    }
    
    /**
     * 取Operation属性
     */
    private final void getOprDetail()
    {
    	try
    	{    		
    		Element reqData = xmlDocUtil.getRequestData();
        	String sysName = reqData.getChildText("__sysName");
    		String oprID = reqData.getChildText("__oprID");
    		
    		Element oprEle = ConfigDocument.getSystemConfig(sysName).getOprElement(oprID);
    		
    		Element resData = XmlDocPkgUtil.createMetaData(opr_flds);
        	
    		String[] vals = {sysName,oprID
    			,oprEle.getAttributeValue("bean")
    			,oprEle.getAttributeValue("jndi")
    			,oprEle.getAttributeValue("desc")};
        	
    		resData.addContent(XmlDocPkgUtil.createRecord(opr_flds, vals));
    		xmlDocUtil.getResponse().addContent(resData);
    		xmlDocUtil.setResult("0");
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    		xmlDocUtil.writeErrorMsg("10005","取Operation定义异常");
    	}
    }
    
    /**
     * 取Action定义,分2部分
     * 1 actFldsData Request中Field列表
     * 2 actAttData Action扩展属性
     */
    private final void getActDetail()
    {
    	try
    	{
    		Element reqData = xmlDocUtil.getRequestData();
        	String sysName = reqData.getChildText("__sysName");
    		String oprID = reqData.getChildText("__oprID");
    		String action = reqData.getChildText("__actions");
    		
    		Element actEle = ConfigDocument.getSystemConfig(sysName).getActionElement(oprID, action);
    		String base = actEle.getAttributeValue("base");
    		
    		Element actAttData = XmlDocPkgUtil.createMetaData(act_a_qs);
    		Element actAttRec = new Element("Record");
    		actAttData.addContent(actAttRec);
    		XmlDocPkgUtil.copyAttrToVal(actEle, actAttRec, act_atts, false);
    		XmlDocPkgUtil.setChildText(actAttRec, "actname", action);
    		XmlDocPkgUtil.setChildText(actAttRec, "base", base);
    		
    		Element tblEle = actEle.getChild("Table");
    		if(null != tblEle)
    			XmlDocPkgUtil.setChildText(actAttRec, "tblname", tblEle.getAttributeValue("name"));
    		    		
    		Element queryEle = actEle.getChild("Query");
    		Element reqEle = actEle.getChild("Request");
    		List fldList = new ArrayList();
    		if(ConfigDocument.isBasicQuery(base) && null != queryEle)
    		{
    			fldList = queryEle.getChildren("Field");
    			
    			XmlDocPkgUtil.setChildText(actAttRec, "basesql", queryEle.getAttributeValue("basesql"));
        		XmlDocPkgUtil.setChildText(actAttRec, "orderby", queryEle.getAttributeValue("orderby"));
        		XmlDocPkgUtil.setChildText(actAttRec, "groupby", queryEle.getAttributeValue("groupby"));
    		}
    		else if(null != reqEle)
    		{
    			fldList = reqEle.getChildren("Field");
    		}
    		Element actFldsData = XmlDocPkgUtil.createMetaData(act_flds);
    		for(int i=0; i<fldList.size(); i++)
        	{
    			Element fldEle = (Element)fldList.get(i);
        		String[] vals = new String[act_flds.length];
        		for(int j=0; j<act_flds.length; j++)
        		{
        			vals[j] = fldEle.getAttributeValue(act_flds[j]);
        		}
        		actFldsData.addContent(XmlDocPkgUtil.createRecord(act_flds, vals));
        	}
    		
    		Element actMsgData = XmlDocPkgUtil.createMetaData(act_msgs);
    		Element msgEle = actEle.getChild("Message");
    		if(null != msgEle)
    		{
    			List mList = msgEle.getChildren("Msg");
    			for(int i=0; i<mList.size(); i++)
    			{
    				Element mEle = (Element)mList.get(i);
    				String[] vals = new String[2];
    				vals[0] = mEle.getAttributeValue("code");
    				vals[1] = mEle.getTextTrim();
    				actMsgData.addContent(XmlDocPkgUtil.createRecord(act_msgs, vals));
    			}
    		}
    		
    		Element actFlowData = XmlDocPkgUtil.createMetaData(act_flows);
    		Element flowEle = actEle.getChild("Flow");
    		if(null != flowEle)
    		{
    			List mList = flowEle.getChildren("Forward");
    			for(int i=0; i<mList.size(); i++)
    			{
    				Element mEle = (Element)mList.get(i);
    				String[] vals = new String[3];
    				vals[0] = mEle.getAttributeValue("result");
    				vals[1] = mEle.getAttributeValue("ftype");
    				vals[2] = mEle.getAttributeValue("path");
    				actFlowData.addContent(XmlDocPkgUtil.createRecord(act_flows, vals));
    			}
    		}
    		
    		xmlDocUtil.getResponse().addContent(actFldsData);
    		xmlDocUtil.getResponse().addContent(actAttData);
    		xmlDocUtil.getResponse().addContent(actMsgData);
    		xmlDocUtil.getResponse().addContent(actFlowData);
    		xmlDocUtil.setResult("0");
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    		xmlDocUtil.writeErrorMsg("10006","取Action定义异常");
    	}
    }
    
    /**
     * 取Table Field定义
     */
    private final void getTblDetail()
    {
    	try
    	{
    		Element reqData = xmlDocUtil.getRequestData();
        	String sysName = reqData.getChildText("__sysName");
    		String tblName = reqData.getChildText("__tblName").toLowerCase();
    		
    		Element resData = XmlDocPkgUtil.createMetaData(tbl_flds);
    		
    		Element tblEle = ConfigDocument.getSystemConfig(sysName).getTableElement(tblName);
    		if(null == tblEle)
    		{
    			xmlDocUtil.writeErrorMsg("("+sysName+","+tblName+") 未定义");
    			return;
    		}
    		String tblPk = tblEle.getAttributeValue("pk");
    		if(StringUtil.isNotEmpty(tblPk))
    		{
    			String type = tblEle.getAttributeValue("type");
    			String gen = tblEle.getAttributeValue("gen");
    			String[] pkVals = {tblPk,tblPk,type,"","","true","true",gen};
    			
    			resData.addContent(XmlDocPkgUtil.createRecord(tbl_flds, pkVals));
    		}
    		List fldEles = tblEle.getChildren("Field");
        	for(int i=0; i<fldEles.size(); i++)
        	{
        		Element fldEle = (Element)fldEles.get(i);
        		String[] vals = new String[tbl_flds.length];
        		for(int j=0; j<tbl_flds.length; j++)
        		{
        			vals[j] = fldEle.getAttributeValue(tbl_flds[j]);
        		}
        		resData.addContent(XmlDocPkgUtil.createRecord(tbl_flds, vals));
        	}
        	
        	xmlDocUtil.getResponse().addContent(resData);
        	xmlDocUtil.setResult("0");
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    		xmlDocUtil.writeErrorMsg("10007","取Table定义异常");
    	}
    }
    
    /**
     * 删除Operation定义
     */
    private final void delOpr()
    {
    	try
    	{
    		Element reqData = xmlDocUtil.getRequestData();
        	String sysName = reqData.getChildText("__sysName");
    		String oprID = reqData.getChildText("__oprID");
    		
    		ConfigDocument.getSystemConfig(sysName).deleteOpr(oprID, true, true);    		
    		xmlDocUtil.setResult("0");
    		xmlDocUtil.writeHintMsg("10008","删除Operation定义成功");
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    		xmlDocUtil.writeErrorMsg("10009","删除Operation定义异常");
    	}
    }
    
    /**
     * 更新Operation
     */
    private final void uptOpr()
    {
    	try
    	{
    		Element reqData = xmlDocUtil.getRequestData();
        	String sysName = reqData.getChildText("__sysName");
    		String oldOprID = reqData.getChildText("oldOprID");
    		String pid = reqData.getChildText("pid");
    		String __oprID = reqData.getChildText("__oprID");
    		
    		String fpath=null;
    		int idx = pid.lastIndexOf("|");
    		if(0<idx)
    		{
    			fpath = pid.substring(idx+1);
    		}
    		
    		Element newOprEle = new Element("Operation");
    		XmlDocPkgUtil.setAttribute(newOprEle, "id", __oprID, false);
    		XmlDocPkgUtil.copyValToAttr(reqData,newOprEle, new String[]{"bean","jndi","desc"},true);
    		
    		ConfigDocument.getSystemConfig(sysName).updateOpr(newOprEle, oldOprID, fpath);
    		
    		xmlDocUtil.setResult("0");
    		xmlDocUtil.writeHintMsg("10010","更新Operation定义成功");
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    		xmlDocUtil.writeErrorMsg("10011","更新Operation定义异常");
    	}
    }
    
    private final void delAct()
    {
    	try
    	{
    		Element reqData = xmlDocUtil.getRequestData();
        	String sysName = reqData.getChildText("__sysName");
    		String oprID = reqData.getChildText("__oprID");
    		String action = reqData.getChildText("__actions");
    		
    		ConfigDocument.getSystemConfig(sysName).deleteAction(oprID,action, true, true);    		
    		xmlDocUtil.setResult("0");
    		xmlDocUtil.writeHintMsg("10012","删除Action定义成功");
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    		xmlDocUtil.writeErrorMsg("10013","删除Action定义异常");
    	}
    }
    
    private final void uptAct()
    {
    	try
    	{
    		Element reqData = xmlDocUtil.getRequestData();
        	String sysName = reqData.getChildText("__sysName");
    		String __oprID = reqData.getChildText("__oprID");
    		String oldAct = reqData.getChildText("__actions");
    		String action = reqData.getChildText("actname");
    		
    		Element newActEle = new Element("Action");
    		//act attr
    		XmlDocPkgUtil.copyValToAttr(reqData,newActEle, act_atts,false);
    		newActEle.removeAttribute("actname");
    		XmlDocPkgUtil.setAttribute(newActEle, "name", action, false);
    		//fields
    		List names = reqData.getChildren("name");
    		List flds = new ArrayList();
    		for(int i=0; i<names.size(); i++)
    		{
    			Element rec = new Element("Field");
    			XmlDocPkgUtil.copyValues(reqData, rec, act_flds, i);
    			Element fieldEle = new Element("Field");
    			XmlDocPkgUtil.copyValToAttr(rec,fieldEle, act_flds,true);
    			flds.add(fieldEle);
    		}
    		//base query
    		Element childEle = null;
    		String base = reqData.getChildText("base");
    		if(ConfigDocument.isBasicQuery(base))
    		{
    			String basesql = reqData.getChildText("basesql");
    			String orderby = reqData.getChildText("orderby");
    			String groupby = reqData.getChildText("groupby");
    			if(flds.size()>0 || StringUtil.isNotEmpty(basesql) || StringUtil.isNotEmpty(orderby) || StringUtil.isNotEmpty(groupby))
    			{
    				childEle = new Element("Query");
        			XmlDocPkgUtil.setAttribute(childEle, "basesql", basesql, true);
        			XmlDocPkgUtil.setAttribute(childEle, "orderby", orderby, true);
        			XmlDocPkgUtil.setAttribute(childEle, "groupby", groupby, true);
        			childEle.addContent(flds);
    			}
    		}
    		else
    		{
    			if(flds.size()>0)
    			{
    				childEle = new Element("Request");
    				childEle.addContent(flds);
    			}
    		}
    		if(null != childEle)
    			newActEle.addContent(childEle);
			//base table
    		String tblname = reqData.getChildText("tblname");
    		if(StringUtil.isNotEmpty(tblname) && 
    			(ConfigDocument.isBasicQuery(base) || ConfigDocument.isBasicTrans(base)))
    		{
    			newActEle.addContent(new Element("Table").setAttribute("name", tblname));
    		}
    		//hint/err msgs
    		List mlist = reqData.getChildren("msgcode");
    		List dlist = reqData.getChildren("msgdesc");
    		Element msgEle = new Element("Message");
    		for(int i=0; i<mlist.size(); i++)
    		{
    			String mcode = ((Element)mlist.get(i)).getTextTrim();
    			String mdesc = ((Element)dlist.get(i)).getTextTrim();
    			if(StringUtil.isNotEmpty(mcode) || StringUtil.isNotEmpty(mdesc))
    			{
	    			msgEle.addContent(new Element("Msg")
	    				.setAttribute("code",mcode)
	    				.setText(mdesc));
    			}
    		}
    		if(0<mlist.size())
    			newActEle.addContent(msgEle);
    		
    		//Flow
    		List rlist = reqData.getChildren("flowresult");
    		List tlist = reqData.getChildren("flowtype");
    		List plist = reqData.getChildren("flowpath");
    		Element flowEle = new Element("Flow");
    		for(int i=0; i<rlist.size();i++)
    		{
    			String mrst = ((Element)rlist.get(i)).getTextTrim();
    			String mtype = ((Element)tlist.get(i)).getTextTrim();
    			String mpath = ((Element)plist.get(i)).getTextTrim();
    			if(StringUtil.isNotEmpty(mrst) && StringUtil.isNotEmpty(mpath))
    			{
    				flowEle.addContent(new Element("Forward")
    					.setAttribute("result", mrst)
    					.setAttribute("ftype", mtype)
    					.setAttribute("path", mpath));
    			}
    		}
    		if(0<rlist.size())
    			newActEle.addContent(flowEle);
    		
    		ConfigDocument.getSystemConfig(sysName).updateAction(newActEle, __oprID, oldAct);
    		
    		xmlDocUtil.setResult("0");
    		xmlDocUtil.writeHintMsg("10014","更新Action定义成功");
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    		xmlDocUtil.writeErrorMsg("10015","更新Action定义异常");
    	}
    }
    
    private final void uptTbl()
    {
    	try
    	{
    		Element reqData = xmlDocUtil.getRequestData();
        	String sysName = reqData.getChildText("__sysname");
        	String tblname = reqData.getChildText("__table_name");
        	String tbldesc = reqData.getChildText("__table_desc");
        	String fname = reqData.getChildText("fname");
        	
        	Element newTblEle = new Element("Table");
    		//act attr
    		XmlDocPkgUtil.setAttribute(newTblEle, "name", tblname.toLowerCase(), false);
    		XmlDocPkgUtil.setAttribute(newTblEle, "desc", tbldesc, false);
    		//fields
    		List names = reqData.getChildren("name");
    		List flds = new ArrayList();
    		for(int i=0; i<names.size(); i++)
    		{
    			String nm = ((Element)names.get(i)).getValue().toLowerCase();
    			Element rec = new Element("Field");
    			XmlDocPkgUtil.copyValues(reqData, rec, tbl_flds, i);
    			XmlDocPkgUtil.setChildText(rec, "name", nm,false);
    			Element fieldEle = new Element("Field");
    			XmlDocPkgUtil.copyValToAttr(rec,fieldEle, tbl_flds,true);
    			flds.add(fieldEle);
    		}
    		newTblEle.addContent(flds);
    		
    		ConfigDocument.getSystemConfig(sysName).updateTable(newTblEle, fname);
    		
    		xmlDocUtil.setResult("0");
    		xmlDocUtil.writeHintMsg("10016","更新Table定义成功");
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    		xmlDocUtil.writeErrorMsg("10017","更新Table定义异常");
    	}
    }
    
    /**
     * 获取交易Request/Response Field列表
     */
    private final void loadFields()
    {
    	Element reqData = xmlDocUtil.getRequestData();
    	String sys = reqData.getChildText("sys");
    	String opr = reqData.getChildText("opr");
    	String act = reqData.getChildText("act");
    	String type = reqData.getChildText("type");//req res
    	
    	try
    	{
    		List fieldList = new ArrayList();
    		Element cfg = getActElement(sys, opr, act);
    		if(null != cfg)
    		{
	    		String base = cfg.getAttributeValue("base");
	    		if (StringUtil.isEmpty(base))
	    		{
	    			if("res".equals(type))
	    			{
	    				Element res = cfg.getChild("Response");
	    				if(null != res)
	    					fieldList = res.getChildren("Field");
	    				else
	    					xmlDocUtil.writeHintMsg("Response 未定义");
	    			}
	    			else
	    			{
	    				fieldList = cfg.getChild("Request").getChildren("Field");
	    			}
	    		}
	    		else
	    		{
	    			Element tableNode = cfg.getChild("Table");
	    			Element qEle = cfg.getChild("Query");
	    			String baseSql = null;
	    			if(null != qEle)
	    				baseSql = qEle.getAttributeValue("basesql");
	                Element tableConfig = null;
	                if(StringUtil.isNotEmpty(baseSql) && "res".equals(type))
	                {
	                	Document qxmlDoc = HttpProcesser.createRequestPackage(sys, opr, act);
		    			ApplicationContext.regSubSys(sys);
			            PlatformDao pDao = new PlatformDao();
			            SessionFactory.getSession().put("Sys.loadFields.response", "true");
		    			Document rxmlDoc = SwitchCenter.doPost(qxmlDoc,true,false);
		    			SessionFactory.getSession().remove("Sys.loadFields.response");
		    			XmlDocPkgUtil qxmlDocUtil = new XmlDocPkgUtil(rxmlDoc);
		    			if("0".equals(qxmlDocUtil.getResult()))
		    			{
		    				Element metaFlds = qxmlDocUtil.getResponse()
		    					.getChild("Data").getChild("Metadata").getChild("Fields");
		    				List fs = metaFlds.getChildren("Field");
		    				for(int i=0; i<fs.size(); i++)
		    				{
		    					Element fEle = (Element)fs.get(i);
		    					Element rField = new Element("Field");
		    		            rField.setAttribute("name", fEle.getAttributeValue("fieldname"));
			                	rField.setAttribute("title", fEle.getAttributeValue("fieldname"));
			                	String fldtype =  pDao.getDialect().getJrafTypeByDataType(fEle.getAttributeValue("fieldtype"));
			                	rField.setAttribute("type",null==fldtype?"":fldtype);
			                	rField.setAttribute("notnull", "false");
			                	
			                	fieldList.add(rField);
		    				}
		    			}
	                }
	                else if(null !=qEle && 0<qEle.getChildren("Field").size())
	    			{
	    				fieldList = qEle.getChildren("Field");
	    			}
	    			else if(null != tableNode)
	    			{
	    				String tableName = tableNode.getAttributeValue("name");	                
	    				tableConfig = ConfigDocument.getTableElement(sys, tableName);
	    				
	    				fieldList = tableConfig.getChildren("Field");
		                String pkName = tableConfig.getAttributeValue("pk");
		                if(StringUtil.isNotEmpty(pkName))
		                {
		                	Element pkField = new Element("Field");
		                	pkField.setAttribute("name", pkName);
		                	pkField.setAttribute("title", pkName);
		                	pkField.setAttribute("type", "long");
		                	pkField.setAttribute("notnull", "true");
		                	fieldList.add(pkField);
		                }
	    			}
	    		}
    		}
    		else
    		{
    			StringBuffer eb = new StringBuffer();
    			eb.append("sysName=").append(sys)
    				.append(",oprID=").append(opr)
    				.append(",actions=").append(act)
    				.append(" 未定义");
    			xmlDocUtil.writeHintMsg(eb.toString());
    		}
    		Element data = XmlDocPkgUtil.createMetaData(new String[]{"name","title","type","notnull"});
    		if(null != fieldList)
    		{
    			for(int i=0;i<fieldList.size(); i++)
    			{
    				Element f = (Element)fieldList.get(i);
    				Element record = new Element("Record");
    	    		XmlDocPkgUtil.setChildText(record, "name", f.getAttributeValue("name"));
    	    		XmlDocPkgUtil.setChildText(record, "title", f.getAttributeValue("title"));
    	    		XmlDocPkgUtil.setChildText(record, "type", f.getAttributeValue("type"));
    	    		XmlDocPkgUtil.setChildText(record, "notnull", f.getAttributeValue("notnull"));
    	    		XmlDocPkgUtil.setChildText(record, "row","1");
    	    		XmlDocPkgUtil.setChildText(record, "col",""+(i+1));
    	    		data.addContent(record);
    			}
    		}
        	xmlDocUtil.getResponse().addContent(data);
        	xmlDocUtil.setResult("0");
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    		xmlDocUtil.setResult("-1");
    		xmlDocUtil.writeErrorMsg("10020","Load fields error");
    	}
    }
    
    private final Element getActElement(String sys, String opr,String act)
    {
    	Element cfg = ConfigDocument.getActionElement(sys, opr, act);
    	if(null == cfg) return null;
    	String oldAction = cfg.getAttributeValue("old");
    	if(StringUtil.isNotEmpty(oldAction))
        {
            StringTokenizer st = new StringTokenizer(oldAction, ".");
            if(st.countTokens() > 1)
            {
                String tempOprID = st.nextToken();
                String tempAction = st.nextToken();
                return getActElement(sys, tempOprID, tempAction);
            }
        }
    	return cfg;
    }
    
    private final List sortList(String fldname, List recEleList)
    {
    	try
    	{
	    	for(int i=0; i<recEleList.size()-1; i++)
	    	{
	    		boolean exchange=false;
	        	for(int j=recEleList.size()-1; j>i; j--)
	    		{
	    			Element rec1 = (Element)recEleList.get(j-1);
	    			Element rec2 = (Element)recEleList.get(j);
	    			String key1 = rec1.getChildText(fldname);
	    			String key2 = rec2.getChildText(fldname);
	    			if(key1.compareTo(key2)>0)
	    			{
	    				exchange=true;
	    				recEleList.set(j, rec1);
	    				recEleList.set(j-1, rec2);
	    			}
	    		}
	        	if(!exchange) break;
	    	}
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}
    	
    	return recEleList;
    }
}
