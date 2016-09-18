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
 * <p>Title:���ݽ���-���ݷ��ʹ�����</p>
 * <p>Description: ���ݽ���-���ݷ��ʹ�����</p>
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: BrightCom Technologies.</p>
 * @author zxqing
 * @version 1.0
 * 
 * <p>Function List // ��Ҫ�������书��
 * 
 *      
 * <p>History: // ��ʷ�޸ļ�¼:</p>
 * <p> author    time             version      desc</p>
 * <p> zxqing    2011/07/04       V1.0          build this moudle </p>
 *     
 */
public class Sender {
	
	private Log log4j = new Log(this.getClass().toString());
	/**
	 * ��������,ÿ��ֻ����50������
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
					String errorMsg = "[Զ�̷������쳣]:"+response.getChildText("Error").trim();
					updateSendQueue(sendID,errorMsg);
				}
			}catch (Exception e) {
				e.printStackTrace();
				String errorMsg = "[���������쳣]";
				updateSendQueue(sendID,errorMsg);
			}
		}
	}
	/**
	 * ��ȡ���������ݶ���
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
			log4j.logError("[��ȡ����������]"+e.getMessage());	
			return null;
		} finally {
			pdao.releaseConnection();			
		}		
	}
	/**
	 * �������Ͷ���
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
			log4j.logError("[�������Ͷ���]"+e.getMessage());	
			throw e;
		} finally {
			pdao.releaseConnection();			
		}
	}
	/**
	 * ����ʧ��,д�������Ϣ
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
			log4j.logError("[�޸ķ��Ͷ���]"+e.getMessage());	
		} finally {
			pdao.releaseConnection();			
		}   	
    }
    /**
     * ɾ�����Ͷ�������
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
			log4j.logError("[ɾ�����Ͷ���]"+e.getMessage());	
		} finally {
			pdao.releaseConnection();			
		}   	
    }
}
