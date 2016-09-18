<%@ page contentType="text/html; charset=GBK" %>
<html>
<body>
  	<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"
			id="Main" width="100%" height="90%"
			codebase="http://fpdownload.macromedia.com/get/flashplayer/current/swflash.cab">
			<param name="movie" value="jasperreports.swf" />
			<param name="quality" value="high" />
			<param name="bgcolor" value="#869ca7" />
			<param name="allowScriptAccess" value="sameDomain" />
			<param name="FlashVars" value="jrpxml=/httpprocesserservlet?sysName=pcmc%26oprID=test%26actions=userprt%26forward=/platform/public/reports.jsp%26rptype=jrpxml%26t=2&fetchSize=3" />
			<embed src="jasperreports.swf" quality="high" bgcolor="#869ca7"
				width="100%" height="90%" name="Main" align="middle"
				play="true"
				loop="false"
				quality="high"
				allowScriptAccess="sameDomain"
				FlashVars="jrpxml=/httpprocesserservlet?sysName=pcmc%26oprID=test%26actions=userprt%26forward=/platform/public/reports.jsp%26rptype=jrpxml%26t=3&fetchSize=3" 
				type="application/x-shockwave-flash"
				pluginspage="http://www.adobe.com/go/getflashplayer">
			</embed>
	</object>
<input type="button" value="load" onclick="_refresh();">
</body>
</html>
<script language="javascript">
function _refresh()
{
	Main.loadReport('/httpprocesserservlet?sysName=pcmc&oprID=test&actions=userprt&forward=/platform/public/reports.jsp&rptype=jrpxml&t=5');
}
</script>
