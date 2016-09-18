<%@ page contentType="text/html; charset=GBK" %>
<%
if (cn.brightcom.jraf.util.StringUtil.isEmpty((String)session.getAttribute("userid"))
 || null == cn.brightcom.jraf.auth.OnlineUserManager.getInstance().getOnlineUser((String)session.getAttribute("userid"))){
 session.invalidate();
%>
<SCRIPT LANGUAGE="JavaScript">
	<!--
		window.top.location = "/";
	//-->
</SCRIPT>
<%
	return;  
}
%>