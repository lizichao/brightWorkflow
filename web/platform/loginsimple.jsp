<%@ page contentType="text/html; charset=GBK" %>
<%session.invalidate();%>
<% 
	request.setCharacterEncoding("GBK"); 
	response.setHeader("Cache-Control", "no-cache"); //HTTP 1.1
	response.setHeader("Pragma", "no-cache"); //HTTP 1.0
	response.setDateHeader("Expires", 0); //prevents caching at the proxy server
%>
<html>
<head>
<%@ include file="/platform/public/includejs.jsp"%>
  <script language="javascript" type="text/javascript">
  	Ext.onReady(function() {
		Ext.QuickTips.init();
		// turn on validation errors beside the field globally
		Ext.form.Field.prototype.msgTarget = 'side';
	    
	    var l=new LoginPanel();
	    l.render('login-panel');
	});</script>
</head>
<body>
	<div id="login-panel"></div>

</body>
</html>