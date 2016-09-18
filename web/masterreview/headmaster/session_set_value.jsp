<%@ page contentType="textml; charset=GBK" %><%
	response.setHeader("Cache-Control", "no-cache");
	response.setHeader("Pragma", "no-cache");
	response.setDateHeader("Expires", 0);
	
	if(cn.brightcom.jraf.util.StringUtil.isNotEmpty((String)request.getParameter("set_session"))){
		session.setAttribute("current_option_num",(String)request.getParameter("current_option_num"));
	}
	
	String current_option_num =(String)session.getAttribute("current_option_num");
%>