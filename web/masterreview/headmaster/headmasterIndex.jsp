<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="cn.brightcom.jraf.util.Crypto" %>
<%@ page import="cn.brightcom.jraf.web.*"%>
<%@ page import="cn.brightcom.system.pcmc.util.ITEMS" %>
<%@ page import="cn.brightcom.tags.util.*" %>
<%@ page import="cn.brightcom.jraf.util.*"%>
<%@ page import="cn.brightcom.jraf.web.*"%>
<%@ page import="cn.brightcom.jraf.auth.*"%>
<%@ page import="org.jdom.*"%>
<%@ page import="java.util.List"%>
<%@ taglib uri="/WEB-INF/brightcom.tld" prefix="sl" %>
<%@ page import="org.apache.commons.lang.StringUtils"%>
<%@ include file="/masterreview/public/sessionoff.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%
	response.setHeader("Cache-Control", "no-cache");
	response.setHeader("Pragma", "no-cache");
	response.setDateHeader("Expires", 0);
	String _basePath = request.getServerName()+":"+request.getServerPort()+request.getContextPath();
	
	 String userid =(String)session.getAttribute("userid");
	 System.out.println("userid="+userid);
	
	 /*
	String id = request.getParameter("id");
	String username = request.getParameter("userid");
	String password = request.getParameter("passwd_md5");
	String referer = request.getHeader("Referer");
	String[] strArray = HttpWebLuohuedu.getUserInfo(id,username,password);
	
	if(StringUtils.isBlank(referer) || !referer.startsWith("http://web.luohuedu.net/") || strArray == null) {
		response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
		response.setHeader("Location","http://web.luohuedu.net/pxzx_login.aspx?url="+_basePath+"/mooc/teacher/login.jsp");
		return;
	}*/
	
	String applyStatus = "";
	String processInstanceId = "";
	String taskId = "";
	
	
	String fowardUrl = "";
	Document reqXml = HttpProcesser.createRequestPackage("headmaster","headMasterbase","findMasterApplyStatus",request);
	reqXml.getRootElement().getChild("Request").getChild("Data").addContent((new Element("headerMasterId")).setText(userid));
	Document xmlDoc = SwitchCenter.doPost(reqXml);
	if (xmlDoc!=null){
		List recordList = xmlDoc.getRootElement().getChild("Response").getChild("Data").getChildren("Record");
		if(recordList.size()>0){
			Element record = (Element)recordList.get(0);
			applyStatus = record.getChildTextTrim("applyStatus");
			processInstanceId = record.getChildTextTrim("processInstanceId");
			taskId = record.getChildTextTrim("taskId");
			if(StringUtils.isBlank(applyStatus)){
			   // fowardUrl = "/masterreview/headmaster/newMasterReview.jsp";
			    fowardUrl = "/workflow/template/startProcessForm.jsp?processkey=headmasterReview";
			}else{
			    if(applyStatus.equals("0")){
				   // fowardUrl = "/masterreview/headmaster/refillMasterReview.jsp";
				    fowardUrl = "/workflow/template/completeTaskForm.jsp?taskId="+taskId;
				}else{
				   // fowardUrl = "/masterreview/process/headerMasterReview.jsp";
				    fowardUrl =  "/workflow/template/processViewForm.jsp?processInstanceId="+processInstanceId;
				}
			}
		}
		response.sendRedirect(fowardUrl);
	}
%>
