package cn.brightcom.system.dataswitch;

import java.util.List;

import org.jdom.Document;
import org.jdom.Element;

import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.util.Constants;
import cn.brightcom.jraf.util.Log;
import cn.brightcom.jraf.util.XmlDocPkgUtil;

/**
 * <p>Title:数据交换监控查询类</p>
 * <p>Description: 数据交换监控查询</p>
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: BrightCom Technologies.</p>
 * @author zxqing
 * @version 1.0
 * 
 * <p>Function List // 主要函数及其功能
 *          
 * <p>History: // 历史修改记录:</p>
 * <p> author    time             version      desc</p>
 * <p> zxqing    2011/07/05       1.0          build this moudle </p>
 *     
 */
public class QueryBean {

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
		
		if ("retransmit".equals(action)) {
			retransmit();
		}		
		
		return xmlDoc;
	}
	/**
	 * 已发送数据重发
	 *
	 */
	public void retransmit(){
		Element reqElement =  xmlDocUtil.getRequestData();
		List list = reqElement.getChildren("send_id");	
		
		StringBuffer strSQL = new StringBuffer(); 
		strSQL.append("insert into pcmc_send_queue (send_id,sys_name,table_name,pk_name,pk_value,op_model,dest_ipaddress,dest_port,created_date,error_msg,try_count) ");
		strSQL.append(" select send_id,sys_name,table_name,pk_name,pk_value,op_model,dest_ipaddress,dest_port,created_date,error_msg,try_count from  pcmc_send_his");
		strSQL.append(" where send_id=");
		
		String backSQL = strSQL.toString();
		String delSQL  = "delete pcmc_send_his where send_id=";
		
		PlatformDao pdao = new PlatformDao();
		try {
			pdao.beginTransaction();
			for (int i = 0; i < list.size() ;i++){
				Element el = (Element)list.get(i);
				String  sendId = el.getText();
				pdao.setSql(backSQL+"'"+sendId+"'");
				pdao.executeTransactionSql();
				
				pdao.setSql(delSQL+"'"+sendId+"'");
				pdao.executeTransactionSql();
			}
			pdao.commitTransaction();
			xmlDocUtil.setResult("0");
			xmlDocUtil.writeHintMsg("已经将数据移至待发送数据中!");
			
		}catch (Exception e) {
			pdao.rollBack();
			e.printStackTrace();
			log4j.logError("[数据交换-数据重发]"+e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
		} finally {
			pdao.releaseConnection();
		}		
	}
	
}
