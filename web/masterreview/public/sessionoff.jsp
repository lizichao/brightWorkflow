<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="cn.brightcom.jraf.web.*"%>
<%@ page import="org.jdom.*"%>
<%@ page import="java.util.List"%>
<%@ taglib uri="/WEB-INF/brightcom.tld" prefix="bc" %>
<%
if (cn.brightcom.jraf.util.StringUtil.isEmpty((String)session.getAttribute("userid"))
 || null == cn.brightcom.jraf.auth.OnlineUserManager.getInstance().getOnlineUser((String)session.getAttribute("userid"))){
 session.invalidate();
%>
<SCRIPT LANGUAGE="JavaScript">
		window.location = "/";
</SCRIPT>
<%
	return;  
}
%>