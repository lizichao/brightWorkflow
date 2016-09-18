package cn.com.bright.yuexue.base;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;

import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.id.UUIDHexGenerator;
import cn.brightcom.jraf.util.Log;
import cn.brightcom.jraf.util.StringUtil;
import cn.brightcom.jraf.util.XmlDocPkgUtil;

/**
 * 
 * <p>Title: 年级管理</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2014 Brightcom</p>
 * <p>Company: ShenZhen BrightCom Technology Co.,Ltd.</p>
 * @author <a href="http://www.szbrightcom.cn" target="_blank">QIUMH</a>
 * @version 1.0a
 */
public class GradeManage
{
	private final static String[] GC_TREENODE = {"sid","pid","deptid","scode","sname","stype","isleaf"};
	
	private XmlDocPkgUtil xmlDocUtil = null;
    
    private static Log log4j = new Log(GradeManage.class.getName());
    
    public Document doPost(Document xmlDoc)
	{
    	xmlDocUtil = new XmlDocPkgUtil(xmlDoc);
    	String action = xmlDocUtil.getAction();
    	
    	if("gradecls".equals(action))
    	{
    		gradeClass();
    	}
    	
		return xmlDoc;
	}
    
    /**
     * 学校/年级/班级 tree
     */
    private void gradeClass()
    {
    	Element reqData = xmlDocUtil.getRequestData();
    	PlatformDao pDao = null;
        try
        {
        	pDao = new PlatformDao();
        	
        	String pid = reqData.getChildText("pid");
        	String deptid = reqData.getChildText("deptid");
        	String stype = reqData.getChildText("stype");
        	String scode = reqData.getChildText("scode");
        	
        	StringBuffer sqlBuf = new StringBuffer();
        	ArrayList bvals = new ArrayList();
        	//年级
        	if("-1".equals(pid))
        	{
        		sqlBuf.append("select gradeid as 'sid', '").append(pid).append("' as 'pid',deptid,")
        			.append("gradecode as 'scode',gradename as 'sname',")
        			.append("'2' as 'stype','false' as 'isleaf' ")
        			.append("from base_grade ")
        			.append("where deptid=? and enabled=?");
        		bvals.add(deptid);
        		bvals.add(new Integer(1));
        	}
        	//班级
        	else if("2".equals(stype))
        	{
        		sqlBuf.append("select classid as 'sid', '").append(pid).append("' as 'pid',deptid,")
	    			.append("gradecode as 'gcode',classcode as 'scode',classnm as 'sname',")
	    			.append("'3' as 'stype','true' as 'isleaf' ")
	    			.append("from base_class ")
	    			.append("where deptid=? and gradecode=? and state=?");
	    		bvals.add(deptid);
	    		bvals.add(scode);
	    		bvals.add("1");
        	}
        	pDao.setSql(sqlBuf.toString());
        	pDao.setBindValues(bvals);
        	
        	Element data = pDao.executeQuerySql(-1, 1);
        	xmlDocUtil.setResult("0");
        	xmlDocUtil.getResponse().addContent(data);
        }
        catch(Exception ex)
        {
        	ex.printStackTrace();
        	xmlDocUtil.writeErrorMsg("10642", "查询年级/班级错误");
        }
        finally
        {
        	pDao.releaseConnection();
        }
    }
}
