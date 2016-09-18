<%@ page contentType="text/html; charset=GBK" %>
<html>
<head>
<script language="javascript">
function report(tp)
{
	var baseUrl="/httpprocesserservlet?sysName=pcmc&oprID=test&actions=userprt&forward=/platform/public/reports.jsp";
	if(0==tp) rptFrame.location=baseUrl;
	if(1==tp) rptFrame.location=baseUrl+"&rptype=xls";
	if(2==tp) rptFrame.location=baseUrl+"&rptype=pdf";
	if(3==tp) rptFrame.location=baseUrl+"&rptype=doc";
	if(4==tp) rptFrame.location="flash.jsp";
}
	
</script>
</head>
<body>
±®±Ì≤‚ ‘<br>
<a href="javascript:report(0);">Html</a>&nbsp;&nbsp;&nbsp;&nbsp;
<a href="javascript:report(1);">Excel</a>&nbsp;&nbsp;&nbsp;&nbsp;
<a href="javascript:report(2);">Pdf</a>&nbsp;&nbsp;&nbsp;&nbsp;
<a href="javascript:report(3);">Doc</a>&nbsp;&nbsp;&nbsp;&nbsp;
<a href="javascript:report(4);">Flash</a>&nbsp;&nbsp;&nbsp;&nbsp;

<br>
<iframe id="rptFrame" style="width:100%;height:90%">
</body>
</html>
