package cn.brightcom.system.dataswitch;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcServerConfigImpl;
import org.apache.xmlrpc.webserver.XmlRpcServletServer;

import cn.brightcom.jraf.util.Log;

/**
 * <p>Title:数据接收XML-RPC服务</p>
 * <p>Description: 数据接收XML-RPC服务</p>
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
public class XmlRpcServer extends HttpServlet {

	private static final long serialVersionUID = 2277842044353939545L;
	private Log log4j = new Log(this.getClass().toString());
	private XmlRpcServletServer server;
    /**
     * servlet初始化
     */
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
        try{
        	server = new XmlRpcServletServer();
        	PropertyHandlerMapping phm = new PropertyHandlerMapping();
        	phm.addHandler("Receiver", Receiver.class);
        	server.setHandlerMapping(phm);
        	
        	XmlRpcServerConfigImpl serverConfig = (XmlRpcServerConfigImpl)server.getConfig();
        	serverConfig.setEnabledForExtensions(true);
        	serverConfig.setContentLengthOptional(false);
        }
        catch (XmlRpcException e) {
        	log4j.logError(e.getMessage());
        }
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)throws IOException, ServletException {
		server.execute(request, response);
	}


}
