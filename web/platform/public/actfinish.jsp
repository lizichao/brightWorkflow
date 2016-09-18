<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="org.jdom.*"%>
<%@ page import="cn.brightcom.jraf.web.*"%>
<%@ page import="cn.brightcom.jraf.util.*"%>
<%@ taglib uri="/WEB-INF/brightcom.tld" prefix="bc" %>
<%
    String act = request.getParameter("act");  
%>

<bc:page login="no" cache="yes">
<script language="JavaScript">
<bc:if name="DataPacket/Response/Error/Msg" notEqual="">
	alert('<bc:foreach name="DataPacket/Response/Error/Msg"><bc:value name="." format="html"/></bc:foreach>');
</bc:if>
<bc:else>
	<bc:if name="DataPacket/Response/Hint/Msg" notEqual="">
	alert('<bc:foreach name="DataPacket/Response/Hint/Msg"><bc:value name="." format="html"/></bc:foreach>');
	if((<%=act %>)=="problemResult"){
	  window.parent.location.href="/loveread/problem/problem_profile.jsp";
  }
  if((<%=act %>)=="problem"){
	  window.parent.location.reload();
  }
  if((<%=act %>)=="problemO"){
	  window.close();
  }
	</bc:if>
</bc:else>
<%
	String[] params = request.getParameterValues("callparam");
	String[] pVals = new String[0];
	if(null != params)
	{
		pVals = new String[params.length];
%>
<bc:with name="/DataPacket/Response/Data/Record">
<%
		for(int i=0; i<params.length; i++)
		{
			String paramStr = params[i];
			request.setAttribute("callParamStr",paramStr);
	%>
		<bc:var name="${callParamStr}" refname="paramVal"/>
	<%
			pVals[i] = (String)request.getAttribute("paramVal");
		}
%>
</bc:with>
<%
	}
%>
	
<bc:if name="DataPacket/Response/@result" equal="0">
<%
	String call = request.getParameter("call");
	if(null != call)
	{
		System.out.println(call);
%>

window.parent.execScript('<%=call%>');
<%
	}
%>
</bc:if>
    
</script>
</bc:page>