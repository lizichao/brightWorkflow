package cn.brightcom.system.dataswitch;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.jdom.Document;
import org.jdom.Element;

import cn.brightcom.jraf.context.ApplicationContext;
import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.util.JDomUtil;
import cn.brightcom.jraf.util.Log;

/**
 * <p>Title:数据交换-数据发送处理类</p>
 * <p>Description: 数据交换-数据发送处理类</p>
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
public class Sender {
	
	private Log log4j = new Log(this.getClass().toString());
	/**
	 * 发送数据,每次只发送50行数据
	 *
	 */
	public void transmit()throws Exception{
		ApplicationContext.regSubSys("pcmc");
		List sendQueue = getSendQueue();
		for (int i=0;i<sendQueue.size();i++){
			Element sendRow  = (Element)sendQueue.get(i);
			String sendID    = sendRow.getChildTextTrim("send_id");
			String sysName   = sendRow.getChildTextTrim("sys_name").toLowerCase();
			String tabelName = sendRow.getChildTextTrim("table_name").toLowerCase();
			String opModel   = sendRow.getChildTextTrim("op_model").toLowerCase();
			
			String pkName    = sendRow.getChildTextTrim("pk_name");
			String pkValue   = sendRow.getChildTextTrim("pk_value");
			
			String destIpAddress = sendRow.getChildTextTrim("dest_ipaddress");
			String destPort = sendRow.getChildTextTrim("dest_port");
			
			Element data = new Element("Data");
			data.setAttribute("sysName", sysName);
			data.setAttribute("name", tabelName);
			data.setAttribute("opModel", opModel);
			
			if ("delete".equals(opModel)){
				Element pkNameEle = new Element("pk_name");
				pkNameEle.setText(pkName);
				Element pkValueEle = new Element("pk_value");
				pkValueEle.setText(pkValue);
				
				data.addContent(pkNameEle);
				data.addContent(pkValueEle);
			}
			else{
				Element rec = getTableData("select * from "+tabelName+" where "+pkName+"='"+pkValue+"'");
	            List ls = rec.getChildren();
	            for (int j=0;j<ls.size();j++){
	            	Element field = (Element)ls.get(j);
	            	Element fieldElement = new Element(field.getName().trim());
	            	fieldElement.setText(field.getText());
	            	data.addContent(fieldElement);		
	            }			    						
			}
			
			try{
				XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
				config.setServerURL(new URL("http://"+destIpAddress+":"+destPort+"/XmlRpcServer"));
				XmlRpcClient client = new XmlRpcClient();
				client.setConfig(config);
							
				Vector params = new Vector();
				params.addElement(JDomUtil.toXML(data));

				String result = (String)client.execute("Receiver.handle",params);
								
				Document xmlDoc = JDomUtil.getDocument(result);
				Element response = xmlDoc.getRootElement();
				
				if ("0".equals(response.getAttributeValue("result"))){
					deleteSendQueue(sendID);
				}
				else{
					String errorMsg = "[远程服务器异常]:"+response.getChildText("Error").trim();
					updateSendQueue(sendID,errorMsg);
				}
			}catch (Exception e) {
				e.printStackTrace();
				String errorMsg = "[发送数据异常]";
				updateSendQueue(sendID,errorMsg);
			}
		}
	}
	/**
	 * 获取待发送数据对象
	 * @param strSQL
	 * @return
	 */
	public Element getTableData(String strSQL){
		//ApplicationContext.regSubSys("pcmc");
		PlatformDao pdao = new PlatformDao();
		try {
			pdao.setSql(strSQL);
			Element resData = pdao.executeQuerySql(0, -1);
			return resData.getChild("Record");
		}catch (Exception e) {
			log4j.logError("[获取待发送数据]"+e.getMessage());	
			return null;
		} finally {
			pdao.releaseConnection();			
		}		
	}
	/**
	 * 检索发送队列
	 * @return
	 * @throws Exception
	 */
	public List getSendQueue()throws Exception{
		StringBuffer strSQL = new StringBuffer();
		strSQL.append("select * from pcmc_send_queue order by created_date");
		//ApplicationContext.regSubSys("pcmc");
		PlatformDao pdao = new PlatformDao();
		try {
			pdao.setSql(strSQL.toString());
			Element resData = pdao.executeQuerySql(50, 1);
			return resData.getChildren("Record");
		}catch (Exception e) {
			log4j.logError("[搜索发送队列]"+e.getMessage());	
			throw e;
		} finally {
			pdao.releaseConnection();			
		}
	}
	/**
	 * 发送失败,写入错误信息
	 * @param sendID
	 * @param erroeMsg
	 */
    public void updateSendQueue(String sendID,String erroeMsg){
    	
    	String strSQL = "update pcmc_send_queue set try_count=try_count+1,send_date=sysdate,error_msg=? where send_id='"+sendID+"'";
    	ArrayList bvals =  new ArrayList();
    	bvals.add(erroeMsg);
    	//ApplicationContext.regSubSys("pcmc");
		PlatformDao pdao = new PlatformDao();		
		try {
			pdao.setSql(strSQL);
			pdao.setBindValues(bvals);
			pdao.executeTransactionSql();			
		}catch (Exception e) {
			log4j.logError("[修改发送队列]"+e.getMessage());	
		} finally {
			pdao.releaseConnection();			
		}   	
    }
    /**
     * 删除发送队列数据
     * @param sendID
     */
    public void deleteSendQueue(String sendID){
    	StringBuffer strSQL = new StringBuffer();    	
    	
    	//ApplicationContext.regSubSys("pcmc");
		PlatformDao pdao = new PlatformDao();		
		try {
			pdao.beginTransaction();
			
			strSQL.append("insert into pcmc_send_his (send_id,sys_name,table_name,pk_name,pk_value,op_model,dest_ipaddress,dest_port,created_date,error_msg,try_count) ");
			strSQL.append(" select send_id,sys_name,table_name,pk_name,pk_value,op_model,dest_ipaddress,dest_port,created_date,error_msg,try_count from pcmc_send_queue ");
			strSQL.append(" where send_id='"+sendID+"'");
			pdao.setSql(strSQL.toString());
			pdao.executeTransactionSql();
			
			String delSQL = "delete pcmc_send_queue where send_id='"+sendID+"'";
			pdao.setSql(delSQL);
			pdao.executeTransactionSql();
			
			pdao.commitTransaction();
		}catch (Exception e) {
			pdao.rollBack();
			log4j.logError("[删除发送队列]"+e.getMessage());	
		} finally {
			pdao.releaseConnection();			
		}   	
    }
}
