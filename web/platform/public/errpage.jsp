<%@ page contentType="text/html; charset=GBK" %><%@ taglib uri="/WEB-INF/brightcom.tld" prefix="bc" %>
<table border="0" width="100%" height="4" cellspacing="0">
<%
	String errmsg=(String)request.getAttribute("ERROR_MESSAGE");
	if(null!=errmsg && !"".equals(errmsg)){
%>	  <tr> 
	    <td>
		 <table width="90%"  border="0" align="center" cellpadding="0" cellspacing="0" style="color:#FF0000">
	        <tr> 
	          <td width="30%" align="right">错误提示：</td><td width="70%">&nbsp;</td>
	        </tr>
	        <tr>
	          <td></td>
	          <td align="left"><%=errmsg%></td>
	        </tr>
	      </table>
		 </td>
	  </tr>
<%}else{%>
	<bc:with name="/DataPacket/Response" ref="xmlDoc">
	<bc:if name="Error/Msg" notEqual="">
	  <tr> 
	    <td>
		 <table width="90%"  border="0" align="center" cellpadding="0" cellspacing="0" style="color:#FF0000">
	        <tr> 
	          <td width="30%" align="right">错误提示：</td><td width="70%">&nbsp;</td>
	        </tr>
	        <bc:foreach name="Error/Msg">
	        <tr>
	          <td></td>
	          <td align="left"><bc:value name="."/></td>
	        </tr>
	        </bc:foreach>
	      </table>
		 </td>
	  </tr>
	</bc:if>
	<bc:if name="Hint/Msg" notEqual="">
	  <tr> 
	    <td>
		 <table width="90%"  border="0" align="center" cellpadding="0" cellspacing="0" style="color:#FF0000">
	        <tr> 
	          <td width="30%" align="right">系统提示：</td><td width="70%">&nbsp;</td>
	        </tr>
	        <bc:foreach name="Hint/Msg">
	        <tr>
	          <td></td>
	          <td align="left"><bc:value name="."/></td>
	        </tr>
	        </bc:foreach>
	      </table>
		 </td>
	  </tr>
	</bc:if>
	</bc:with>
<%}%>
</table>