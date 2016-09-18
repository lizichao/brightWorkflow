package cn.brightcom.system.pcmc.sm;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;

import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.util.ConfigDocument;
import cn.brightcom.jraf.util.Log;
import cn.brightcom.jraf.util.XmlDocPkgUtil;


/**
 * 
 * @author QIUMH
 *
 */
public class DepartmentBean
{
	private XmlDocPkgUtil xmlDocUtil = null;
    
    private static Log log4j = new Log(DepartmentBean.class.getName());
    
    public Document doPost(Document xmlDoc)
    {
    	xmlDocUtil = new XmlDocPkgUtil(xmlDoc);
    	String action = xmlDocUtil.getAction();
    	
    	if ("addDept".equals(action))
        {
            addDept();
        }
//    	if ("changeDept".equals(action))
//        {
//            changeDept();            //异动机构
//        }
    	
    	return xmlDoc;
    }
    
    private final void addDept()
    {
        PlatformDao dao = null;
        Element reqData = xmlDocUtil.getRequestData();
        String levelid = reqData.getChildTextTrim("levels");
        String deptcode = reqData.getChildTextTrim("deptcode");
        try
        {
        	dao = new PlatformDao();
        	StringBuffer sql = new StringBuffer();
            sql.append("select deptcode from pcmc_dept where deptcode = '");
            sql.append(deptcode+"'");
            dao.setSql(sql.toString());
            Element tmp = dao.executeQuerySql( -1, 1);
            if (tmp.getChildren("Record").size()>0) {
            	xmlDocUtil.setResult("-1");
            	xmlDocUtil.writeErrorMsg("10403","机构编号已经存在");
            	return;
            }
            if ( (null == levelid) || (levelid.length() == 0)) {
            	levelid = "1";
            }
            else {
            	levelid = String.valueOf(Integer.parseInt(levelid) + 1);
            }
            Element ele = ConfigDocument.createRecordElement("pcmc","pcmc_dept");
            XmlDocPkgUtil.copyValues(reqData,ele,0);
            ele.getChild("levels").setText(levelid);
            ele.removeChild("changetype");
            ele.removeChild("changedate");
            ele.removeChild("cdeptid");
          //System.out.println(JDomUtil.toXML(ele,"gb2312",true));
            Element resData = XmlDocPkgUtil.createMetaData(new String[]{"deptid"});
            
            List pkList = dao.insertOneRecord(ele);
            Object deptid = ((Object[])pkList.get(0))[1];
            //Element data = new Element("Data");
            Element record = new Element("Record");
            resData.addContent(record);
            XmlDocPkgUtil.setChildText(record, "deptid", ""+deptid);
            xmlDocUtil.getResponse().addContent(resData);
            //reqData.addContent(new Element("subdeptid").setText(String.valueOf(deptid)));
            xmlDocUtil.writeHintMsg("10401","新增机构成功");
            xmlDocUtil.setResult("0");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            log4j.logError("[新增部门发生异常.]"+e.getMessage());
            
            xmlDocUtil.setResult("-1");
        	xmlDocUtil.writeErrorMsg("10402","新增机构异常");
         }
    }
    
    private final void changeDept()
    {
        Element reqData = xmlDocUtil.getRequestData();
        String deptid = reqData.getChildTextTrim("deptid");
        String changetype = reqData.getChildTextTrim("changetype");
        String cdeptid = reqData.getChildTextTrim("cdeptid");
        String changedate = reqData.getChildText("changedate")+" 00:00:00";
        PlatformDao dao = new PlatformDao();
        StringBuffer sql = new StringBuffer();
        try
        {
            sql.append(" update pcmc_dept set changetype=?,changedate=?");
            if (cdeptid!=null && !cdeptid.equals("")) {
                sql.append(",cdeptid=?");
            }
            sql.append(" where deptid=?");
            dao.setSql(sql.toString());
            ArrayList bvals = new ArrayList();
            bvals.add(changetype);
            bvals.add(Timestamp.valueOf((changedate)));
            if (cdeptid!=null && !cdeptid.equals("")) {
                bvals.add(new Long(Long.parseLong(cdeptid)));
            }
            bvals.add(new Long(Long.parseLong(deptid)));
            dao.setBindValues(bvals);
            dao.executeTransactionSql();
            xmlDocUtil.setResult("0");
            xmlDocUtil.writeHintMsg("10405","异动机构成功");
        }
        catch (Exception e)
        {
            e.printStackTrace() ;
            log4j.logError(e);
            xmlDocUtil.writeErrorMsg("10406","异动机构异常");
        }
        finally
        {
            dao.releaseConnection();
        }
    }
}
