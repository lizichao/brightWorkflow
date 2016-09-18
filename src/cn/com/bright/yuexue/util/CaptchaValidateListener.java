package cn.com.bright.yuexue.util;

import javax.servlet.http.HttpServletRequest;

import org.jdom.Document;

import cn.brightcom.jraf.context.ApplicationContext;
import cn.brightcom.jraf.event.Event;
import cn.brightcom.jraf.event.Listener;
import cn.brightcom.jraf.util.StringUtil;
import cn.brightcom.jraf.util.XmlDocPkgUtil;
/**
 * <p>Title:ͼƬ��֤����֤��</p>
 * <p>Description: ͼƬ��֤����֤��</p>
 * <p>Copyright: Copyright (c) 2010</p>
 * <p>Company: BrightCom Technologies.</p>
 * @author zxqing
 * @version 1.0
 * 
 * <p>Function List // ��Ҫ�������书��
 *      
 *      
 * <p>History: // ��ʷ�޸ļ�¼:</p>
 * <p> author    time          version      desc</p>
 * <p> zxqing    2014/11/03       1.0          build this moudle </p>
 *     
 */
public final class CaptchaValidateListener extends Listener{
	public void fire(Event paramEvent, Object paramObject){
	    if ((null == paramEvent) || (null == paramObject))
	        return;
	    try{
	    	HttpServletRequest request = ApplicationContext.getRequest();	    	
	    	String captchaCode = request.getParameter("captchaCode");
	    	String rand = (String)request.getSession().getAttribute("rand");
	    	String loginFailure = (String)request.getSession().getAttribute("loginFailure");
	    	if (StringUtil.isNotEmpty(loginFailure)){
		    	Document xmlDoc = (Document)paramObject;
		    	XmlDocPkgUtil xmlDocUtil = new XmlDocPkgUtil(xmlDoc);	    	
		    	if (StringUtil.isEmpty(captchaCode)){
		    		captchaCode ="";
		    	}
		    	if (StringUtil.isEmpty(rand)){
		    		rand ="";
		    	}
		    	if (rand.equals(captchaCode)){
		    		request.getSession().removeAttribute("rand");
		    	}
		    	else{
		    		xmlDocUtil.setResult("1");
		    		xmlDocUtil.writeErrorMsg("10104","��֤�����");
		    		throw new Exception("��֤�����");
		    	}
	    	}
	    	else{
	    		request.getSession().setAttribute("loginFailure", "1");
	    	}
	    }
	    catch (Exception ex){
	    	ex.printStackTrace();
	    	throw new java.lang.RuntimeException("��֤�����");
	    }
	 }
}
