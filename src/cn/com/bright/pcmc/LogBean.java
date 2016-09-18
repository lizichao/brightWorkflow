package cn.com.bright.pcmc;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;

import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.db.SqlTypes;
import cn.brightcom.jraf.util.ConfigDocument;
import cn.brightcom.jraf.util.Constants;
import cn.brightcom.jraf.util.Log;
import cn.brightcom.jraf.util.StringUtil;
import cn.brightcom.jraf.util.XmlDocPkgUtil;

public class LogBean {
	private XmlDocPkgUtil xmlDocUtil = null;
	private Log log4j = new Log(this.getClass().toString());
	
    /**
     * 动态委派入口
     * @param request xmlDoc 
     * @return response xmlDoc
     */
	public Document doPost(Document xmlDoc) {
		xmlDocUtil = new XmlDocPkgUtil(xmlDoc);
		String action = xmlDocUtil.getAction();
		if("searchLog".equals(action)){
			searchLog(); //检索日志
		}			
		return xmlDoc;
	}	
	/**
	 * 检索日志
	 *
	 */
	public void searchLog(){
		Element reqElement = xmlDocUtil.getRequestData();
		PlatformDao pdao = new PlatformDao();
		try {
			String userid = reqElement.getChildText("userid");
			String begindate = reqElement.getChildText("begindate");
			String enddate = reqElement.getChildText("enddate");
			String __sysname = reqElement.getChildText("__sysname");
			String __actions = reqElement.getChildText("__actions");
			String mytablename = reqElement.getChildText("mytablename");
			
			StringBuffer strSQL = new StringBuffer();
			strSQL.append("select a.logid,a.userid," + pdao.isNull() + "(b.username,'学生或老师') as username,");
			strSQL.append(" a.begintime,a.endtime,a.sysname,a.oprid,a.actions,a.issuccess,a.ipaddr");
			strSQL.append(" ,a.basetbl,a.key_id from pcmc_log a left join pcmc_user b");
			strSQL.append(" on a.userid = b.userid where 1=1");
			
			ArrayList<Object> bvals=new ArrayList<Object>();
		      if (StringUtil.isNotEmpty(userid))
		      {
		    	  strSQL.append(" and a.userid=? ");
		    	  bvals.add(userid);
		      }
		      if (StringUtil.isNotEmpty(begindate))
		      {
		    	  strSQL.append(" and a.begintime>=? ");
		          bvals.add(SqlTypes.getConvertor("Timestamp").convert(begindate, null));
		      }
		      if (StringUtil.isNotEmpty(enddate))
		      {
		    	  strSQL.append(" and a.endtime<=? ");
		    	  bvals.add(SqlTypes.getConvertor("Timestamp").convert(enddate, null));
		      }
		      if ((StringUtil.isNotEmpty(__sysname)) && (!"-1".equals(__sysname)))
		      {
		    	  strSQL.append(" and a.sysname=? ");
		    	  bvals.add(__sysname);
		      }	
		      if ((StringUtil.isNotEmpty(__actions)) && (!"-1".equals(__actions)))
		      {
		    	  strSQL.append(" and a.actions=? ");
		    	  bvals.add(__actions);
		      }		      
		      if (StringUtil.isNotEmpty(mytablename))
		      {
		    	  strSQL.append(" and a.basetbl=? ");
		    	  bvals.add(mytablename);
		      }		      
		      String order = xmlDocUtil.getOrderBy();
		      if (StringUtil.isNotEmpty(order)){
		    	  strSQL.append(order);
		      }
			pdao.setSql(strSQL.toString());
			pdao.setBindValues(bvals);
			Element resData = pdao.executeQuerySql(xmlDocUtil.getPageSize(),xmlDocUtil.getPageNo());
			List ls = resData.getChildren("Record");
			for (int i = 0; i < ls.size(); i++){
				Element rec = (Element)ls.get(i);
				String sysname = rec.getChildText("sysname");
				String oprid = rec.getChildText("oprid");
				String actions = rec.getChildText("actions");
				XmlDocPkgUtil.setChildText(rec, "sysname2", ConfigDocument.getSystemDesc(sysname));
				XmlDocPkgUtil.setChildText(rec, "oprid2", ConfigDocument.getOprDesc(sysname,oprid));
				XmlDocPkgUtil.setChildText(rec, "actions2", ConfigDocument.getActionDesc(sysname,oprid,actions));				
			}
			xmlDocUtil.getResponse().addContent(resData);
			xmlDocUtil.setResult("0");			
		}catch (Exception e) {
			e.printStackTrace();
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
			log4j.logError("[检索日志出错！.]" + e.getMessage());
		} finally {
			pdao.releaseConnection();
		}
	}	
}
