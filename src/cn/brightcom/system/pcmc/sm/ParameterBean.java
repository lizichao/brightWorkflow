package cn.brightcom.system.pcmc.sm;

import java.io.FileWriter;
import java.util.*;

import org.jdom.*;

import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.dialect.Dialect;
import cn.brightcom.jraf.util.FileUtil;
import cn.brightcom.jraf.util.Log;
import cn.brightcom.jraf.util.StringUtil;
import cn.brightcom.jraf.util.XmlDocPkgUtil;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2009 Brightcom</p>
 * <p>Company: ShenZhen BrightCom Technological Co.,Ltd.</p>
 * @author <a href="http://www.szbrightcom.cn" target="_blank">QIUMH</a>
 * @version 1.0a
 */
public class ParameterBean
{
	private XmlDocPkgUtil xmlDocUtil = null;
	
	private String jfpath = "platform/public/sysparam.js";
    
    private static Log log4j = new Log(ParameterBean.class.getName());
    
    public Document doPost(Document xmlDoc)
    {
    	xmlDocUtil = new XmlDocPkgUtil(xmlDoc);
        String action = xmlDoc.getRootElement().getChild("Action").getText() ;
        if ("uptjs".equals(action))
        { 
            uptjs();
        }
        else if ("paratreelist".equals(action))
        {
            paratreelist();
        }
        return xmlDoc;
    }
    
    private void uptjs()
    {
    	PlatformDao pDao = null;
    	FileWriter fw = null;
    	try
    	{
    		pDao = new PlatformDao();
    		StringBuffer sqlBuf = new StringBuffer("select a.*,")
    			.append("b.paramdetailid,b.paramcode,b.parammeanings ")
    			.append("from param_master a,param_detail b ")
    			.append("where a.paramid=b.paramid ")
    			.append("order by a.paramid,b.paramdetailid");
    		pDao.setSql(sqlBuf.toString());    		
    		Element data = pDao.executeQuerySql(-1, 1);
    		List plist = data.getChildren("Record");
    		StringBuffer jsBuf = new StringBuffer("var _sYs_paRm_liSt={");
    		String _pmname=null;
    		for(int i=0; i<plist.size(); i++)
    		{
    			Element rec = (Element)plist.get(i);
    			String pmname = rec.getChildText("paramname");
    			if(!pmname.equals(_pmname))
    			{
    				if(null != _pmname)
    				{
    					jsBuf.deleteCharAt(jsBuf.length()-1);
    					jsBuf.append("]},");
    				}
    				jsBuf.append("'").append(pmname)
    					.append("':{'pmname':'").append(pmname).append("',")
						.append("'pmdesc':'").append(rec.getChildText("paramdesc")).append("',")
						.append("'items':[");
    				_pmname = pmname;
    			}
    			jsBuf.append("['").append(rec.getChildText("paramcode"))
    				.append("','").append(rec.getChildText("parammeanings"))
    				.append("'],");
    		}
    		if(0<plist.size())
    		{
    			jsBuf.deleteCharAt(jsBuf.length()-1);
    			jsBuf.append("]}");
    		}    			
    		jsBuf.append("};");
    		
    		fw = new FileWriter(FileUtil.getWebPath()+jfpath);
    		fw.write(jsBuf.toString());
    		fw.flush();
    		fw.close();
    		fw = null;
    		
    		xmlDocUtil.setResult("0");
    		xmlDocUtil.writeHintMsg("Success");
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    		log4j.logError(ex);    		
    	}
    	finally
    	{
    		pDao.releaseConnection();
    		if(null != fw)
    		{
    			try
    			{
    				fw.close();
    				fw = null;
    			}
    			catch(Exception ex){
    				
    			}
    		}
    	}
    }

    private void paratreelist()
    {
        Element reqData = xmlDocUtil.getRequestData();
        PlatformDao pDao = null;
        try
        {
            pDao = new PlatformDao();
            Dialect dialect = pDao.getDialect();

            String paramname = reqData.getChildTextTrim("paramname");
            String paramcode = reqData.getChildTextTrim("paramcode");
            String codelen = reqData.getChildTextTrim("codelen");

            Hashtable codes = new Hashtable();
            int maxlen = 0;
            String[] cs = codelen.split(",");
            for (int i = 0; i < cs.length; i++)
            {
                int c = new Integer(cs[i]).intValue();
                codes.put(new Integer(maxlen), new Integer(maxlen + c));
                maxlen += c;
            }
            int clength = 0;
            if (StringUtil.isNotEmpty(paramcode) && !"-1".equals(paramcode))
            {
                clength = paramcode.length();
            }
            int paramlen = ((Integer)codes.get(new Integer(clength))).intValue();

            StringBuffer sqlBuf = new StringBuffer("select b.*,").append(paramlen == maxlen ? "0" : "1").append(" as childnum ")
                .append("from param_master a,param_detail b ")
                .append("where a.paramid=b.paramid ")
                .append("and a.paramname='").append(paramname).append("' ")
                .append("and ").append(dialect.len()).append("(b.paramcode)=").append(paramlen);
            if (0 < clength)
            {
                sqlBuf.append(" and ").append(dialect.charIndex()).append("('").append(paramcode).append("',b.paramcode)=1");
            }
            sqlBuf.append(" order by b.paramdetailid");

            pDao.setSql(sqlBuf.toString());
            Element data = pDao.executeQuerySql(-1, 1);

            xmlDocUtil.setResult("0");
            xmlDocUtil.getResponse().addContent(data);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            log4j.logError(ex);
        }
        finally
        {
            pDao.releaseConnection();
        }
    }    
}
