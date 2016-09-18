<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="cn.brightcom.jraf.web.*"%>
<%@ page import="org.jdom.*"%>
<%@ page import="java.util.List"%>
<%@ taglib uri="/WEB-INF/brightcom.tld" prefix="bc" %>
<%
   String processBusinessKey = (String)request.getAttribute("processBusinessKey");

   Document reqXml = HttpProcesser.createRequestPackage("workflow","publishAction","viewPublish",request);
   reqXml.getRootElement().getChild("Request").getChild("Data").addContent((new Element("query_businessKey")).setText(processBusinessKey));
   Document xmlDoc = SwitchCenter.doPost(reqXml);
   request.setAttribute("xmlDoc",xmlDoc);
   
   
	//Element data = xmlDoc.getRootElement().getChild("Response").getChild("Data");
	//Element record = null==data?null:data.getChild("Record");
	List records = xmlDoc.getRootElement().getChild("Response").getChild("Data").getChildren("Record");
	//String fwtm = "";
	//String fldgwlx = "";
		String publishVO ="";
	if (null != records) {
		Element record = (Element) records.get(0);
		publishVO =  record.getChildTextTrim("publishVO");
		//fwtm = record.getChildTextTrim("fwtm");
		//fldgwlx = record.getChildTextTrim("fldgwlx");
	}
%>
<script>
$(document).ready(function(){	
	bulidPublishView(<%=publishVO%>);
	
});	

function bulidPublishView(publishVO){
	debugger
	$("#fwtm").text(publishVO.fwtm);
	$("#fldgwlx").text(publishVO.fldgwlx);
	$("#fldzbbmmc").text(publishVO.fldzbbmmc);
	$("#fldngr").text(publishVO.fldngr);
	
	$("#fldjjcd").val(publishVO.fldjjcd);
	$("#fldmj").val(publishVO.fldmj);
	$("#fldwz").val(publishVO.fldwz);
	
}
</script>
<input type="hidden" id="workflowSubmitAction" name="workflowSubmitAction" value="workflow@@publishAction@@completePublishTask">
<bc:with name="/DataPacket/Response/Data[1]">
<bc:foreach name="Record">
<table width="440" border="0" cellpadding="0" cellspacing="0">
<tr align="center">
		<td height="14" width="130">��������</td>
		<td height="14" width="230">
		    <label id="fwtm" name="fwtm"></label> 
		</td>
	  </tr>
	  <tr align="center">
		<td height="14" width="130">��������</td>
		<td height="14" width="230">
		     <label id="fldgwlx" name="fldgwlx"></label>
		</td>
		
		<td height="14" width="130">���첿��</td>
		<td height="14" width="230">
		   <label id="fldzbbmmc" name="fldzbbmmc"></label>
		</td>
	  </tr>
	  <tr align="center">
		<td height="14" width="130">�����:</td>
		<td height="14" width="230">
		  <label id="fldngr" name="fldngr"></label>
		</td>
		
		<td height="14" width="130">�����̶�</td>
		<td height="14" width="230">
		   <select name="fldjjcd" id="fldjjcd"  style="width:100%">  
		      <option value = "0" selected>����</option>
	          <option value = "1">����</option>
	          <option value = "2">�ؼ�</option>
           </select> 
		</td>
	  </tr>
	 <tr align="center">
		<td height="14" width="130">�ܼ�:</td>
		<td height="14" width="230">
		   <select id="fldmj" name="fldmj" style="width:234px;">
	          <option value = "0" selected>ƽ��</option>
	          <option value = "2">����</option>
	          <option value = "3">����</option>
	          <option value = "4">����</option>
	        </select>
		</td>
		
		<td height="14" width="130">����</td>
		<td height="14" width="230">
		   <select name="fldwz" id="fldwz"  style="width:100%">  
		      <option value = "0" selected>����</option>
	          <option value = "1">����</option>
	          <option value = "2">�ؼ�</option>
           </select> 
		</td>
	  </tr>
</table>
</bc:foreach>
</bc:with>
