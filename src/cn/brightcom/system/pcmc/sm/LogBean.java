package cn.brightcom.system.pcmc.sm;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;

import cn.brightcom.jraf.context.ApplicationContext;
import cn.brightcom.jraf.db.DBOprProxy;
import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.db.SqlTypes;
import cn.brightcom.jraf.util.ConfigDocument;
import cn.brightcom.jraf.util.DatetimeUtil;
import cn.brightcom.jraf.util.JDomUtil;
import cn.brightcom.jraf.util.Log;
import cn.brightcom.jraf.util.StringUtil;
import cn.brightcom.jraf.util.TipsUtil;
import cn.brightcom.jraf.util.XmlDocPkgUtil;


public class LogBean
{
	private XmlDocPkgUtil xmlDocUtil = null;
    
    private static Log log4j = new Log(LogBean.class.getName());
    
    public Document doPost(Document xmlDoc)
    {
    	xmlDocUtil = new XmlDocPkgUtil(xmlDoc);
    	String action = xmlDocUtil.getAction();
        if ("searchLog".equals(action))
        {
            searchLog(xmlDoc) ;
        }
    	
    	return xmlDoc;
    }
    
    private final void searchLog(Document xmlDoc)
    {   
        PlatformDao dao = new PlatformDao() ;
        try
        {
        	Element dataElement = xmlDocUtil.getRequestData() ;
            String userid = dataElement.getChildTextTrim("userid") ;
            String begindate = dataElement.getChildTextTrim("begindate") ;
            String enddate = dataElement.getChildTextTrim("enddate") ;
            String sysname = dataElement.getChildTextTrim("__sysname") ;
        	//组装SQL
	        StringBuffer sql = new StringBuffer() ;
	        sql.append("select a.logid,a.userid,"+dao.isNull()+"(b.username,'Anonymous') as username,");
	        sql.append("a.begintime");
	        sql.append(",a.endtime,a.sysname,a.oprid,a.actions,a.issuccess,a.ipaddr") ;
	        sql.append(" from pcmc_log a left outer join pcmc_user b ") ;
	        sql.append(" on a.userid = b.userid where 1=1 ") ;
	        ArrayList bvals = new ArrayList();
	        //组装条件
	        if (StringUtil.isNotEmpty(userid))
	        {
	        	sql.append(" and a.userid=? ") ;
	        	bvals.add(userid);
	        }
	        if (StringUtil.isNotEmpty(begindate))
	        {
	            sql.append(" and a.begintime>=? ") ;
	            bvals.add(SqlTypes.getConvertor("Timestamp").convert(begindate, null));
	        }
	        if (StringUtil.isNotEmpty(enddate))
	        {
	            sql.append(" and a.endtime<=? ") ;
	            bvals.add(SqlTypes.getConvertor("Timestamp").convert(enddate, null));
	        }
	        if (StringUtil.isNotEmpty(sysname))
	        {
	            if (!"-1".equals(sysname))
	            {
	                sql.append(" and a.sysname=? ") ;
	                bvals.add(sysname);
	            }
	        }
	        String orby = xmlDocUtil.getOrderBy();
	        if(StringUtil.isNotEmpty(orby))
	        {
	        	sql.append(orby);
	        }
	        //sql.append(" order by a.begintime ") ;
        
            dao.setSql(sql.toString()) ;
            dao.setBindValues(bvals);
            Element data = dao.executeQuerySql(xmlDocUtil.getPageSize(), xmlDocUtil.getPageNo()) ;
            List rs = data.getChildren("Record");
            for(int i=0; i<rs.size(); i++)
            {
            	Element rec = (Element)rs.get(i);
            	String s = rec.getChildText("sysname");
            	String o = rec.getChildText("oprid");
            	String a = rec.getChildText("actions");
            	XmlDocPkgUtil.setChildText(rec, "sysname2", ConfigDocument.getSystemDesc(s));
            	XmlDocPkgUtil.setChildText(rec, "oprid2", ConfigDocument.getOprDesc(s,o));
            	XmlDocPkgUtil.setChildText(rec, "actions2", ConfigDocument.getActionDesc(s,o,a));
            }
            xmlDocUtil.getResponse().addContent(data);
            xmlDocUtil.setResult("0");
        }
        catch (Exception e)
        {
            e.printStackTrace() ;
            log4j.logError(e);
            xmlDocUtil.setResult("-1");
            //日志查询失败
            xmlDocUtil.writeErrorMsg(TipsUtil.getSysMsgByCode("00082"));
        }
        finally
        {
            dao.releaseConnection();
        }
    }
    
    private final void write(Document xmlDoc)
    	throws Exception
    {
    	XmlDocPkgUtil xmlDocUtil = new XmlDocPkgUtil(xmlDoc);
    	Element reqData = xmlDocUtil.getRequestData();
        String actions = xmlDocUtil.getAction();
        //取得userid
        String userid = null;
        if ("login".equals(actions))
        {
            String usercode = reqData.getChildTextTrim("usercode");
            userid = getUserID(usercode);
        }
        else
        {
        	Element session = xmlDocUtil.getSession();
        	if(null != session)
        		userid = session.getChildTextTrim("userid");
        }
        if (StringUtil.isEmpty(userid))
        {
            return;
        }
        //判断是否需要将xml数据写入日志，如果是，则取得xml字符串。
        String xmldoc = null;
        if (ConfigDocument.isLogXml())
        {
            xmldoc = JDomUtil.toXML(xmlDoc,"GBK",false);
        }
        //交易是否成功。
        String successful = "0".equals(xmlDocUtil.getResult())?"1":"0";
        //执行写日志
        StringBuffer sql = new StringBuffer();
        PlatformDao dao = null;
        try
        {
        	dao = new PlatformDao(true);
        	Element logRec = ConfigDocument.getTableElement("pcmc", "pcmc_log");
        	String xmlType = null;
        	String userType = null;
        	boolean baseflag = false;
        	boolean keyflag = false;
        	List flds = logRec.getChildren("Field");
        	for(int i=0; i<flds.size(); i++)
        	{
        		Element fldEle = (Element)flds.get(i);
        		if("xmldoc".equals(fldEle.getAttributeValue("name")))
        		{
        			xmlType = fldEle.getAttributeValue("type");
        		}
        		if("userid".equals(fldEle.getAttributeValue("name")))
        		{
        			userType = fldEle.getAttributeValue("type");
        		}
        		if("basetbl".equals(fldEle.getAttributeValue("name")))
        		{
        			baseflag = true;
        		}
        		if("key_id".equals(fldEle.getAttributeValue("name")))
        		{
        			keyflag = true;
        		}
        	}
        	/**
        	 * 存储基本交易主键值
        	 */
        	String logsid = reqData.getAttributeValue("logsid");
        	String basetbl = null;
        	String key_id = null;
        	if(null != logsid)
        	{
        		Object[] logpkinf = (Object[])ApplicationContext.getJrafSession().get(logsid);
        		if(null != logpkinf)
        		{
        			basetbl = (String)logpkinf[0];
        			ArrayList pkrow = (ArrayList)logpkinf[1];
        			if(null != pkrow)
        			{
        				StringBuffer keys = new StringBuffer();
	        			for(int j=0; j<pkrow.size()&&j<20; j++)
	        			{
	        				Object[] pkobj = (Object[])pkrow.get(j);
	        				keys.append(pkobj[1]).append(",");
	        			}
	        			if(pkrow.size()>0)
	        			{
	        				keys.deleteCharAt(keys.length()-1);
	        			}
	        			key_id = keys.toString();
        			}
        		}
        	}
        	
        	String seq = (String)DBOprProxy.getNextSequence("uuid", null);
            sql.append("insert into pcmc_log(logid,userid,begintime,endtime,sysname,")
                .append("oprid,actions,issuccess,xmldoc,ipaddr")
                .append(baseflag?",basetbl":"").append(keyflag?",key_id":"")
                .append(") ")
                .append("values (?,?,?,?,?,?,?,?,?,?")
                .append(baseflag?",?":"").append(keyflag?",?":"")
                .append(")");
            ArrayList bvals = new ArrayList();
            bvals.add(seq);
            bvals.add(SqlTypes.getConvertor(userType).convert(userid,null));
            bvals.add(xmlDocUtil.getRequestTime());
            bvals.add(DatetimeUtil.getNow());
            bvals.add(xmlDocUtil.getSysName());
            bvals.add(xmlDocUtil.getOprID());
            bvals.add(xmlDocUtil.getAction());
            bvals.add(SqlTypes.getConvertor("long").convert(successful,null));
            bvals.add(SqlTypes.getConvertor(xmlType).convert(xmldoc,null));
            bvals.add(xmlDocUtil.getIP());
            if(baseflag)
            {
            	bvals.add(basetbl);
            }
            if(keyflag)
            {
            	bvals.add(key_id);
            }

            dao.setSql(sql.toString());
            dao.setBindValues(bvals);
            dao.executeTransactionSql();
        }
        finally
        {
            dao.releaseConnection();
        }
    }
    //根据username获得userid
    private final String getUserID(String usercode)
    {
    	String userid = null;
        StringBuffer sql = new StringBuffer("select userid from pcmc_user where usercode=?");
        PlatformDao dao = new PlatformDao();
        try
        {
            dao.setSql(sql.toString());
            ArrayList bvals = new ArrayList();
            bvals.add(usercode);
            dao.setBindValues(bvals);
            Element resElement = dao.executeQuerySql(-1,1);
            if (!("0".equals(resElement.getChild("PageInfo").getChildTextTrim("RecordCount"))))
            {
                userid = resElement.getChild("Record").getChildTextTrim("userid");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            dao.releaseConnection();
        }
        return userid;
    }

    //写日志
    public static void writeLog(Document xmlDoc)
    {
    	XmlDocPkgUtil xmlDocUtil = new XmlDocPkgUtil(xmlDoc);
        String sysName = xmlDocUtil.getSysName();
        String oprid   = xmlDocUtil.getOprID();
        String actions = xmlDocUtil.getAction();

        //判断当前交易（或其复用原交易）是否需要写日志
        Element actionElement = ConfigDocument.getActionElement(sysName, oprid, actions);

        String log = actionElement.getAttributeValue("log");
        String base = actionElement.getAttributeValue("base");
        if (StringUtil.isNotEmpty(base) && !ConfigDocument.isLogBasicAction(base))
        {
            return;
        }
        else
        {
            if (!("true".equals(log)))
            {
                return;
            }
        }
        try
        {
        	ApplicationContext.regSubSys("pcmc");
            LogBean writeLog = new LogBean();
            writeLog.write(xmlDoc);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            log4j.logError(e);
            //记录日志失败
            xmlDocUtil.writeErrorMsg(TipsUtil.getSysMsgByCode("00081"));
        }
    }
    
    public static void setLogPkInf(Element reqData,String tableName,ArrayList pkrow)
    {
    	String logsid="logPKinf"+ApplicationContext.getNextNum();
		reqData.setAttribute("logsid", logsid);
		Object[] logpkinf = new Object[2];
		logpkinf[0] = tableName;
		logpkinf[1] = pkrow;
		ApplicationContext.getJrafSession().put(logsid, logpkinf);
    }
}
