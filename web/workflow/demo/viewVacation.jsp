<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="cn.brightcom.jraf.web.*"%>
<%@ page import="org.jdom.*"%>
<%@ page import="java.util.List"%>
<%@ taglib uri="/WEB-INF/brightcom.tld" prefix="bc" %>
<%
   String userid =(String)session.getAttribute("userid");
   String processBusinessKey = (String)request.getAttribute("processBusinessKey");

    //Document xmlDoc = (Document)request.getAttribute("xmlDoc");
   Document reqXml = HttpProcesser.createRequestPackage("workflow","vacationWorkflow","viewVacationDetail",request);
   reqXml.getRootElement().getChild("Request").getChild("Data").addContent((new Element("query_businessKey")).setText(processBusinessKey));
   Document xmlDoc = SwitchCenter.doPost(reqXml);
   request.setAttribute("xmlDoc",xmlDoc);
   
   
	//Element data = xmlDoc.getRootElement().getChild("Response").getChild("Data");
	//Element record = null==data?null:data.getChild("Record");
	List records = xmlDoc.getRootElement().getChild("Response").getChild("Data").getChildren("Record");
	String days = "";
	String reason = "";
	if (null != records) {
		Element record = (Element) records.get(0);
		days = record.getChildTextTrim("days");
		reason = record.getChildTextTrim("reason");
	}
%>
<script>
$(document).ready(function(){	
	Brightcom.workflow.beforeSubmit = beforeSubmit;
	/*
	var vacationBusinessKey = Brightcom.workflow.getProcessBusinessKey();
	var bcReq= new BcRequest("workflow","vacationWorkflow","viewVacationDetail");
	bcReq.setExtraPs({
		"query_businessKey":vacationBusinessKey
	});
	bcReq.setSuccFn(function(data,status){
		var vacationVO = data.Data[0];
		$("#days").text(vacationVO.days);
		$("#reason").text(vacationVO.reason);
	});
	bcReq.postData();*/
});	

function beforeSubmit(formJsonData){
	var variableMap = new Brightcom.workflow.HashMap(); 
	var taskDefKey = Brightcom.workflow.getTaskDefKey();
	var processDefKey = Brightcom.workflow.getProcessDefKey();
    if(processDefKey == 'vacationMulti' && taskDefKey =='usertask1'){
		variableMap.put("nextMonitor",'<%=userid%>');
	}
    if(taskDefKey == 'usertask2'){
		var gatewaySelectObject = Brightcom.workflow.flowInfos[Brightcom.workflow.selectFlowId];
		variableMap.put("internalOperate",gatewaySelectObject ? gatewaySelectObject.name : "" );
	}
    
	formJsonData.processParam=  variableMap.toJsonObject();
	//variableMap.put("processTitle",$("#receiveTitleLabel").text());
	//formJsonData.processParam=  variableMap.toJsonObject();
}
</script>
<input type="hidden" id="workflowSubmitAction" name="workflowSubmitAction" value="workflow@@vacationWorkflow@@completeVacationTask">
<bc:with name="/DataPacket/Response/Data[1]">
<bc:foreach name="Record">
<!--<bc:value name='days'/>  -->
     <div class="box2" id="tab1c1">
               	  <div class="box2_content" >
<table border="0" cellspacing="0" cellpadding="0" class="table3" >
   <tr>
		<td style="width:230px">请假天数</td>
		<td>
		  <span id="daysSpan"><bc:value name='days'/></span>
		  <!--   <input  available="true" size="18" value="<bc:value name="days"/>" name="days" id="days" />-->
		</td>
   </tr>
   <tr >
		<td>
		  请假原因
		</td>
		<td>
		    <label id="reason" name="reason"><bc:value name='reason'/></label>
		</td>
   </tr>
</table>

  <div class="clear cH1"></div>
</div>
</div>
</bc:foreach>
</bc:with>
