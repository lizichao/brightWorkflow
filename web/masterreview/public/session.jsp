<%@ page contentType="text/html; charset=GBK" %><%
	response.setHeader("Cache-Control", "no-cache");
	response.setHeader("Pragma", "no-cache");
	response.setDateHeader("Expires", 0);
	
	if(cn.brightcom.jraf.util.StringUtil.isNotEmpty((String)request.getParameter("set_session"))){
		session.setAttribute("subjectid",(String)request.getParameter("subjectid"));
		session.setAttribute("gradecode",(String)request.getParameter("gradecode"));
		session.setAttribute("foldercode",(String)request.getParameter("foldercode"));
	}
	
	String subjectid_session =(String)session.getAttribute("subjectid");
	String gradecode_session =(String)session.getAttribute("gradecode");
	String foldercode_session =(String)session.getAttribute("foldercode");
%>