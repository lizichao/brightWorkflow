package cn.brightcom.system.dataswitch;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.jdom.Document;
import org.jdom.Element;

import cn.brightcom.jraf.context.ApplicationContext;
import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.util.JDomUtil;
import cn.brightcom.jraf.util.Log;

/**
 * <p>Title:数据交换-数据接收处理类</p>
 * <p>Description: 数据交换-数据接收处理类</p>
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: BrightCom Technologies.</p>
 * @author zxqing
 * @version 1.0
 * 
 * <p>Function List // 主要函数及其功能
 * 
 *      
 * <p>History: // 历史修改记录:</p>
 * <p> author    time             version      desc</p>
 * <p> zxqing    2011/07/04       V1.0          build this moudle </p>
 *     
 */
public class Receiver {
	private Log log4j = new Log(this.getClass().toString());
	
	/**
	 * 接收数据
	 * @param dataStr
	 * @return
	 */
	public String handle(String dataStr){

        Document xmlDoc = JDomUtil.getDocument(dataStr);
        Element data = xmlDoc.getRootElement();
        String  sysName = data.getAttributeValue("sysName");
        String  tabelName = data.getAttributeValue("name");
        String  opModel   = data.getAttributeValue("opModel");
        
        Element Response = new Element("Response");
        
    	ApplicationContext.regSubSys(sysName);
    	PlatformDao pdao = new PlatformDao();       
        try{	        
	        if ("delete".equals(opModel)){
	        	String pkName  = data.getChildText("pk_name");
	        	String pkValue = data.getChildText("pk_value");
	        	String strSQL = "delete "+tabelName+" where "+pkName+" ='"+pkValue+"'";
	        	
	        	pdao.setSql(strSQL);
	        	pdao.executeTransactionSql();
	        }
	        else if("add".equals(opModel)){
	        	pdao.insertOneRecord(data);
	        }
	        else{
	        	pdao.updateOneRecord(data);
	        }
	        
	        Response.setAttribute("result", "0");
	        
        }catch (Exception e) {        	
        	StringWriter writer = new StringWriter();
            e.printStackTrace(new PrintWriter(writer));
			String errorMsg = writer.getBuffer().toString();
			log4j.logError(errorMsg);
			if (errorMsg.length()>200)
				errorMsg = errorMsg.substring(0, 200);
			
			Response.setAttribute("result","-1");
			Element Error = new Element("Error");
			Error.setText(errorMsg);
			Response.addContent(Error);
			
		} finally {
			pdao.releaseConnection();			
		}
		return JDomUtil.toXML(Response);
        
	}
}

