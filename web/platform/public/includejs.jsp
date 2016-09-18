<%@ page contentType="text/html; charset=GBK" %>
<%
	request.setCharacterEncoding("GBK");
	response.setHeader("Cache-Control", "no-cache"); //HTTP 1.1
	response.setHeader("Pragma", "no-cache"); //HTTP 1.0
	response.setDateHeader("Expires", 0); //prevents caching at the proxy server
%>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/ext/resources/css/ext-all.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/platform/icons/silk.css">
<script type="text/javascript" src="<%=request.getContextPath()%>/ext/adapter/ext/ext-base.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/ext/ext-all.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/ext/ext-lang-zh.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/ext/ext-patch.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/ext/ext-patch-destroy.js"></script>
<!--Í¬²½ext-basex/jit-->
<script type="text/javascript" src="<%=request.getContextPath()%>/ext/jitx.js"></script>
<!--miframe-->
<script type="text/javascript" src="<%=request.getContextPath()%>/ext/miframe2.js"></script>
<!--superboxselect
<script type="text/javascript" src="<%=request.getContextPath()%>/ext/SuperBoxSelect.js"></script>
<link href="<%=request.getContextPath()%>/ext/superboxselect.css" rel="Stylesheet" type="text/css" />-->
<!--ux-->
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/ext/examples/ux/css/ux-all.css">
<script type="text/javascript" src="<%=request.getContextPath()%>/ext/examples/ux/ux-all.js"></script>
<!--datetime-->
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/DateTimePicker.css" />
<script type="text/javascript" src="<%=request.getContextPath()%>/ext/DateTimeField.js"></script>
<!--htmleditorplugins-->
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/ext/resources/css/htmleditorplugins.css" />
<script type="text/javascript" src="<%=request.getContextPath()%>/ext/htmleditorplugins.js"></script>
<!--jraf-->
<link href="<%=request.getContextPath()%>/platform/css/MultiCombo.css" rel="Stylesheet" type="text/css" />
<script type="text/javascript" src="<%=request.getContextPath()%>/platform/public/session.jsp?r=<%=Math.random()%>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/platform/public/sysparam.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/platform/public/jrafajax.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/platform/login.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/UploadPanel.css">
<script type="text/javascript" src="<%=request.getContextPath()%>/js/swfupload.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/UploadPanel.js"></script>