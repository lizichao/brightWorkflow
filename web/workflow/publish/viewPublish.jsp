<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="org.jdom.*"%>
<%@ page import="java.util.List"%>
<%@ page import="cn.brightcom.jraf.web.*"%>
<%
    String username =(String)session.getAttribute("username");
	String portrait =(String)session.getAttribute("portrait"); 
	String usertype =(String)session.getAttribute("usertype");
	String userid =(String)session.getAttribute("userid");
	
   String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
   String processBusinessKey = (String)request.getAttribute("processBusinessKey");
   String processInstanceId = (String)request.getAttribute("processInstanceId");
   String processDefKey = (String)request.getAttribute("processDefKey");

   Document reqXml = HttpProcesser.createRequestPackage("workflow","publishAction","viewPublish",request);
   reqXml.getRootElement().getChild("Request").getChild("Data").addContent((new Element("query_businessKey")).setText(processBusinessKey));
   reqXml.getRootElement().getChild("Request").getChild("Data").addContent((new Element("processInstanceId")).setText(processInstanceId));
   reqXml.getRootElement().getChild("Request").getChild("Data").addContent((new Element("processDefKey")).setText(processDefKey));
   Document xmlDoc = SwitchCenter.doPost(reqXml);
   request.setAttribute("xmlDoc",xmlDoc);
   
   
	//Element data = xmlDoc.getRootElement().getChild("Response").getChild("Data");
	//Element record = null==data?null:data.getChild("Record");
	List records = xmlDoc.getRootElement().getChild("Response").getChild("Data").getChildren("Record");
	//String fwtm = "";
	String publishVO ="";
	String officeApprover ="";
	if (null != records) {
		Element record = (Element) records.get(0);
		publishVO =  record.getChildTextTrim("publishVO");
		officeApprover =  record.getChildTextTrim("officeApprover");
		//fwtm = record.getChildTextTrim("fwtm");
	}
%>
<script type="text/javascript" src="/workflow/js/jquery.idTabs.min.js"></script>
<script src="/workflow/common/jspParamUtil.js"></script>
<script src="/platform/public/sysparam.js"></script>
<script>
Brightcom.workflow.afterBuildTaskForm = afterBuildTaskForm;
$(document).ready(function(){	
	Brightcom.workflow.beforeSubmit = beforeSubmit;
	Brightcom.workflow.beforeCloseCounterSign = beforeCloseCounterSign;
	bulidPublishView(<%=publishVO%>);
});	

function afterBuildTaskForm(taskViewFormVO){
	$("#taskViewButtonDiv").append("&nbsp;&nbsp;<a class='btn1' id=\'back_btn\' name='back_btn'  onclick=\"savePublishVO();\" >����<span></span></a>");
	var currentTaskKey = taskViewFormVO.taskKey;
	
	if(taskViewFormVO.processDefinitionKey=='publishSerial' || currentTaskKey != 'usertask6'){
		return;
	}
	var operateButtonArray = [];
	for (var j=0;j<taskViewFormVO.flows.length;j++){
		var flowObject = taskViewFormVO.flows[j];
		var flowId = flowObject.id;
		Brightcom.workflow.flowInfos[flowId] = flowObject;
		
		//taskViewFormVO.multiUserTask && taskViewFormVO.multiCreator && taskViewFormLen>1
		//��ǩ������Ҳ���Ǽ���ˣ�����Ҫ���²���
		if(flowObject.dest.type=='exclusiveGateway' && '<%=officeApprover%>' =="<%=userid%>"){
			var gatewayFlows = flowObject.dest.outgoingFlowVOs;
			for (var k=0;k<gatewayFlows.length;k++){
				var gatewayObject =  gatewayFlows[k];
				Brightcom.workflow.flowInfos[gatewayObject.id] = gatewayObject;
				if(gatewayObject.id !='flow24'){
					//operateButtonArray.push("<input type='button' id=\'"+gatewayObject.flowId+"\' name='qry_btn' value=\'"+gatewayObject.name+"\'  onclick=\"completePublishTask(\'"+flowId+"\',\'"+gatewayObject.id+"\');\" />");
					//operateButtonArray.push("<input type='button' id=\'"+gatewayObject.flowId+"\' name='qry_btn' value=\'"+gatewayObject.name+"\'  onclick=\"completePublishTask(\'"+flowId+"\',\'"+gatewayObject.id+"\');\" />");
					operateButtonArray.push("<a class='btn1' id=\'"+gatewayObject.flowId+"\'  value=\'"+gatewayObject.name+"\'  onclick=\"completePublishTask(\'"+flowId+"\',\'"+gatewayObject.id+"\');\" >"+gatewayObject.name+"<span></span></a>");
				}
			}
			//operateButtonArray.push("<input type='button' id='addCounterSign' name='addCounterSign' value='��ǩ' onclick=\"addPublishCounterSign(\'"+taskViewFormVO.processInstanceId+"\');\" />");
			//operateButtonArray.push("<input type='button' id='removeCounterSign' name='removeCounterSign' value='��ǩ'  onclick=\"removePublishCounterSign(\'"+taskViewFormVO.processInstanceId+"\');\"/>");
			
			//operateButtonArray.push("<a class='btn1' id='addCounterSign' name='addCounterSign' value='��ǩ' onclick=\"addPublishCounterSign(\'"+taskViewFormVO.processInstanceId+"\');\" >��ǩ<span></span></a>");
			//operateButtonArray.push("<a class='btn1' id='removeCounterSign' name='removeCounterSign' value='��ǩ'  onclick=\"removePublishCounterSign(\'"+taskViewFormVO.processInstanceId+"\');\" >��ǩ<span></span></a>");
		}else{
			//operateButtonArray.push("<input type='button' id=\'"+flowId+"\' name='operate_btn' value=\'�������\'  onclick=\"completePublishTask(\'"+taskViewFormVO.flows[j].id+"\');\" />");
			operateButtonArray.push("<a class='btn1' id=\'"+flowId+"\' name='operate_btn'   onclick=\"completePublishTask(\'"+taskViewFormVO.flows[j].id+"\');\" >�������<span></span></a>");
		}
	}
	$("#taskViewButtonDiv").html("");
	//operateButtonArray.push("<input type='button' id=\'back_btn\' name='back_btn' value=\'����\'  onclick=\"savePublishVO();\" />");
	//operateButtonArray.push("<input type='button' id=\'back_btn\' name='back_btn' value=\'����\'  onclick=\"javascript:history.go(-1);\" />");
	
	operateButtonArray.push("<a class='btn1' id=\'back_btn\' name='back_btn' value=\'����\'  onclick=\"savePublishVO();\" >����<span></span></a>");
	operateButtonArray.push("<a class='btn1' id=\'back_btn\' name='back_btn' value=\'����\'  onclick=\"javascript:history.go(-1);\" >����<span></span></a>");
	$("#taskViewButtonDiv").append(operateButtonArray.join("&nbsp;&nbsp;"));
}

function savePublishVO(){
	var fwtm = $("#fwtm").val();
	var fldjjcd = $("#fldjjcdLabel").val();
	var fldmj =$("#fldmj").val();
	var fldwz =$("#fldwz").val();
	var id =$("#id").val();
	 
	var bcReq = new BcRequest('workflow','publishAction','savePublishData');
	bcReq.setExtraPs({
		"fwtm":fwtm,
		"fldjjcd":fldjjcd,
		"fldmj":fldmj,
		"fldwz":fldwz,
		"id":id
	});
	bcReq.setSuccFn(function(data,status){
		 window.location.reload();
	});
	bcReq.postData();
}

function addPublishCounterSign(processInstanceId){
	var url = "/workflow/publish/counterSign.jsp";
    var dlgid = "addCounterSignDialog";
    var options = {};
    options.title = "��ǩ";
    options.width = "486";
    options.height = "458";
    options.url = url;
    options.dlgid = dlgid;
    
    getPublishCounterInfo(processInstanceId,'add',options);
    /*
    options.param = {
    		 'counterOperate':'add',
    		 'officeApprover':$("#officeApprover").val(),
    		 'processInstanceId':processInstanceId,
    		 'taskId': $("#taskId").val(),
    		 'id': $("#id").val()
    };
    options.callback = function(data) {
    	window.location.reload();
        //���ǻص����� ��data���ж��壬��ģ̬���ڹرպ�Ա�ҳ����в��� 
    };
    $.Dialog.open(url, dlgid, options);*/
}

function getPublishCounterInfo(processInstanceId,operate,options){
	   var bcReq = new BcRequest('workflow','publishAction','getCounterInfo');
		bcReq.setExtraPs({
			"processInstanceId":processInstanceId,
			'counterOperate':operate,
			'officeApprover':$("#officeApprover").val()
		});
		bcReq.setSuccFn(function(data,status){
			if(data.Data.length == 0){
				var prompt = (operate =='add')? '��ǩ' : '��ǩ';
				alert("����"+prompt+"����Ϊ�գ�");
	        	return false;
			}
		    options.param = {
		    		 'counterOperate':operate,
		    		 'officeApprover':$("#officeApprover").val(),
		    		 'processInstanceId':processInstanceId,
		    		 'taskId': $("#taskId").val(),
		    		 'id': $("#id").val(),
		    		 'data' : data
		    };
		    options.callback = function(data) {
		    	window.location.reload();
		        //���ǻص����� ��data���ж��壬��ģ̬���ڹرպ�Ա�ҳ����в��� 
		    };
		    $.Dialog.open(options.url, options.dlgid, options);
		
		});
		bcReq.postData();
}

function removePublishCounterSign(processInstanceId){
	var url = "/workflow/publish/counterSign.jsp";
    var dlgid = "addCounterSignDialog";
    var options = {};
    options.title = "��ǩ";
    options.width = "420";
    options.height = "458";
    options.url = url;
    options.dlgid = dlgid;
    
    getPublishCounterInfo(processInstanceId,'remove',options);
    /*
    options.param = {
    		 'counterOperate':'remove',
    		 'officeApprover':$("#officeApprover").val(),
    		 'processInstanceId':processInstanceId,
    		 'id': $("#id").val()
    };
    options.callback = function(data) {
    	window.location.reload();
        //���ǻص����� ��data���ж��壬��ģ̬���ڹرպ�Ա�ҳ����в��� 
    };
    $.Dialog.open(url, dlgid, options);*/
}

var gatewayFlowIdSelected = "";
function completePublishTask(flowId,gatewayFlowId){
	var isCondition = '1';
	switch (gatewayFlowId) {
		case 'flow24'://�͹��쵼��ʾ
			isCondition ='1';
		   break;
		case 'flow14'://�˻���岿��
			isCondition ='2';					   
		    break;
		case 'flow15'://���
			isCondition ='3';
		    break;
		case 'flow23'://�Ͱ칫�ҷ���
			isCondition ='4';
		    break;
	}
	$("#selectGatewayId").val(isCondition);
	gatewayFlowIdSelected = gatewayFlowId;
	Brightcom.workflow.submitWorkflowRequest(Brightcom.workflow.taskType,flowId);
}

function beforeSubmit(formJsonData){
	var taskDefKey = Brightcom.workflow.getTaskDefKey();
	var processDefKey = Brightcom.workflow.getProcessDefKey();
	var officeApprover = "";
	if(taskDefKey == 'usertask5'){
		officeApprover = '<%=userid%>';
	}
	//if(taskDefKey == 'usertask5'){
		//Brightcom.workflow.setInternalOperate("�������");
	//}
	if(taskDefKey =='usertask3' && Brightcom.workflow.selectFlowId == 'flow5'){
		Brightcom.workflow.setNextPrincipalHandlers('<%=userid%>');
		formJsonData.departmentPrincipalHandler = '<%=userid%>';
	}
	
	var variableMap = new Brightcom.workflow.HashMap(); 
	if(taskDefKey == 'usertask6'){
		var gatewaySelectObject = Brightcom.workflow.flowInfos[gatewayFlowIdSelected || Brightcom.workflow.selectFlowId];
		variableMap.put("internalOperate",gatewaySelectObject ? gatewaySelectObject.name : "" );
		
		if(Brightcom.workflow.selectFlowId == 'flow25'){
			variableMap.put("multiCompleteMark",'1' );
		}
	}
	
	//if(taskDefKey == 'usertask4'){
		//if('<%=officeApprover%>' !="<%=userid%>"){
		//}
		//variableMap.put("internalOperate",gatewaySelectObject ? gatewaySelectObject.name : "" );
		
	//}
	
	variableMap.put("isCondition",$("#selectGatewayId").val());
	variableMap.put("processTitle",$("#fwtmLabel").text());
	if(officeApprover){
		variableMap.put("officeApprover",officeApprover);
	}
	formJsonData.id=  $("#id").val();
	formJsonData.officeApprover=  officeApprover;
	formJsonData.processParam=  variableMap.toJsonObject();
//	return {
	//	"id" : $("#id").val(),
	//	"officeApprover" : officeApprover,
		//"processParam" : variableMap.toJsonObject()
	//}
}

function bulidPublishView(publishVO){
	$("#taskRemarkDiv").hide();
	$("#id").val(publishVO.id);
	$("#officeApprover").val(publishVO.officeApprover);
	
	$("#fwtm").text(publishVO.fwtm);//����
	$("#fwtmLabel").text(publishVO.fwtm);
	//$("#fldgwlx").text(publishVO.fldgwlx);
	
	$("#flddjh").text(publishVO.flddjh);//�ǼǺ�
	$("#fldDJH").text(publishVO.flddjh);
	
	$("#fldzbbmmc").text(publishVO.fldzbbmmc);//��岿��
	$("#fldzbbmmcLabel").text(publishVO.fldzbbmmc);//��岿��
	
	
	$("#fldngr").text(publishVO.fldngr);//�����
	$("#fldngrLabel").text(publishVO.fldngr);//�����
	
	$("#fldjjcd").text(JspParamUtil.paramVal('urgency_level',publishVO.fldjjcd));//�����̶�
	$("#fldjjcdLabel").val(publishVO.fldjjcd);//�����̶�
	
	
	$("#fldmj").val(publishVO.fldmj);
	$("#fldwz").val(publishVO.fldwz);
	var originalAttachment = publishVO.originalAttachment;
	if(originalAttachment.attachmentId){
		var originalAttachmentStr="<a class=\"color3\"  href=\"<%=basePath%>WorkflowAttachMentDownload?attachmentId="+originalAttachment.attachmentId+"\" >"+originalAttachment.fileName+"</a>"
		$("#originalAttachment").html(originalAttachmentStr);
	}

	
	var approveLogs =  publishVO.approveLogs;
	var startApprove =  approveLogs["startEvent"];//�������� ������
	var deptApprove =  approveLogs["usertask3"];//������� ��岿�����
	var deptMultiApprove =  approveLogs["usertask4"];//���Ż�ǩ���
	var officeApprove =  approveLogs["usertask5"];//�칫�����
	var curatorApprove =  approveLogs["usertask6"];//���쵼��ʾ
	
	$("#startLog").html(bulidApproveStr(startApprove,publishVO));
	$("#deptLog").html(bulidApproveStr(deptApprove,publishVO));
	$("#deptMultiLog").html(bulidApproveStr(deptMultiApprove,publishVO));
	$("#officeLog").html(bulidApproveStr(officeApprove,publishVO));
	$("#curatorLog").html(bulidApproveStr(curatorApprove,publishVO));
	
	//��ǰ�û����������˻��߼���˲ſ��Ա༭��ע
	if(!(publishVO.officeApprover =="<%=userid%>" || publishVO.departmentPrincipalHandler =="<%=userid%>")){
		bulidEditRemark();
	}


	
	var attachmentArray = [];
	for(var j=0;j<publishVO.attachMents.length;j++){
		var attachMentObject =  publishVO.attachMents[j];
		//attachmentArray.push("<a href=\"javascript:downLoadPublishAttachment(\'"+attachMentObject.attachmentId+"\',\'"+attachMentObject.fileName+"\')\" >"+attachMentObject.fileName+"</a>");
		//attachmentArray.push("<a href=\"<%=basePath%>WorkflowAttachMentDownload?attachmentName="+attachMentObject.fileName+"\">"+attachMentObject.fileName+"</a>");
        // return attachmentArray.join("\r\n<br>");
		attachmentArray.push("<tr>");
		attachmentArray.push("<td><a class=\"color3\"  href=\"<%=basePath%>WorkflowAttachMentDownload?attachmentId="+attachMentObject.attachmentId+"\">"+attachMentObject.fileName+"</a></td>");
		attachmentArray.push("<td>"+attachMentObject.fileSize+"</td>");
		attachmentArray.push("<td><a class=\"color3\"  href=\"<%=basePath%>WorkflowAttachMentDownload?attachmentId="+attachMentObject.attachmentId+"\">����</a></td>");
		attachmentArray.push("</tr>");
	}
	$("#attachMentTby").append(attachmentArray.join(""));
	//$("#attachmentText").html((""+attachmentArray.join("\r\n<br>")).replace(/&/g,"&#38;").replace(/</g,"&lt;").replace(/\x0d|\x0a/g,"<br>"));
	//$("#attachmentText").html(attachmentArray.join("\r\n<br>"));
}

function bulidEditRemark(){
	var currentTaskKey = Brightcom.workflow.getTaskDefKey();
	var editRemarkDiv = "";
	switch (currentTaskKey) {
		case 'usertask3'://������� ��岿�����
			editRemarkDiv = "deptLog";
		   break;
		case 'usertask4'://���Ż�ǩ���
			editRemarkDiv = "deptMultiLog";				   
		    break;
		case 'usertask5'://�칫�����
			editRemarkDiv = "officeLog";
		    break;
		case 'usertask6'://���쵼��ʾ
			editRemarkDiv = "curatorLog";
		    break;
   }
	
	var editRemarkArray = [];
	editRemarkArray.push("<div id='publishEditRemark' style='float:left'>");
	editRemarkArray.push("<span style='float:left;text-align:left'>");
	editRemarkArray.push("   <b>������ע </b>");
	editRemarkArray.push("  <select id='regComments' name='regComments' onchange='insertRegComment();' >");
	editRemarkArray.push("     	<OPTION selected value=''></OPTION>");
	editRemarkArray.push("		<OPTION value=��ͬ��>��ͬ��.</OPTION>");
	editRemarkArray.push("	<OPTION value=ͬ��>ͬ��</OPTION>");
	editRemarkArray.push("	<OPTION value=ͬ��ǩ��>ͬ��ǩ��</OPTION>");
	editRemarkArray.push("	<OPTION value=����>����</OPTION>");
	editRemarkArray.push("	<OPTION value=����>����</OPTION>");
	editRemarkArray.push("	<OPTION value=�Ѻ�>�Ѻ�</OPTION>");
	editRemarkArray.push("	<OPTION value=�˻�>�˻�</OPTION>");
	editRemarkArray.push("<OPTION value=�Ѱ�>�Ѱ�</OPTION>");
	editRemarkArray.push("<OPTION value=�ذ�>�ذ�</OPTION>");
	editRemarkArray.push("<OPTION value=���>���</OPTION>");
	editRemarkArray.push(" </select>");
	editRemarkArray.push(" <br>");
	editRemarkArray.push("</span>");
	editRemarkArray.push(" <textarea id='internalRemark' name='internalRemark' cols='90' rows='3' style='float:left'></textarea>");	
	editRemarkArray.push(" </div>");
	$("#"+editRemarkDiv).after(editRemarkArray.join(""));
}

function insertRegComment(){
	var regComments=  $('#regComments option:selected').text(); 
	$('#publishEditRemark').find("#internalRemark").val(regComments);
}

function downLoadPublishAttachment(attachmentId,attachmentName){
	//window.open ('/WorkflowAttachMentDownload?curfile='+attachmentId+'&path='+attachmentName,'newwindow','height=30,width=40,top=100,left=200,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');
	$.ajax({
		   type: "GET",
		   url: "/WorkflowAttachMentDownload?attachmentName="+attachmentName,
		   async: false,
		   beforeSend: function(res){
			},
		   complete: function(req,status){
		   }
		});
	/*
	 var jr = new JrafRequest('workflow','publishAction','downLoadPublishAttachment');              
     jr.setExtraPs({"attachmentId":attachmentId});
     jr.setSuccFn(function(a,_resp,xr){
		   //  _rec.set('state','1');
		//  _grid.unlockBtn.disable();
		 // _grid.lockBtn.enable();
     });
     jr.postData();     */
     
     /*
	var bcReq = new BcRequest('workflow','publishAction','downLoadPublishAttachment');
	bcReq.setExtraPs({"attachmentId":attachmentId});
	bcReq.setSuccFn(function(data,status){
	});
	bcReq.postData();*/
}

function bulidApproveStr(approveLogs,publishVO){
	var taskDefKey = Brightcom.workflow.getTaskDefKey();
	if(!approveLogs){
		return "";
	}
	var approveArray = [];
	for(var i=0;i<approveLogs.length;i++){
	
		var approveLog = approveLogs[i];
		
		var processInstanceId = approveLog.processInstanceId;
		var taskId = approveLog.taskId;
		var taskDefKey = approveLog.taskDefKey;
		var taskName = approveLog.taskName;
		var operation = approveLog.operation;
		var remark = approveLog.remark;
		var handlerId = approveLog.handlerId;
		if(taskDefKey =='usertask4' && handlerId==publishVO.departmentPrincipalHandler){//���Ż�ǩ���
			continue;
		}
		if(taskDefKey =='usertask6' && handlerId==publishVO.officeApprover){//���쵼��ʾ
			continue;
		}
		var handlerName = approveLog.handlerName;
		var handlerOrder = approveLog.handlerOrder;
		var handlerDepartmentId = approveLog.handlerDepartmentId;
		var handlerDepartmentName = approveLog.handlerDepartmentName;
		var handlerDepartmentOrder = approveLog.handlerDepartmentOrder;
		var createTime = approveLog.createTime;
		var createPeople = approveLog.createPeople;
		var updateTime = approveLog.updateTime;
		var updatePeople = approveLog.updatePeople;
		
		var singleLog= handlerDepartmentName +"&nbsp;&nbsp;&nbsp;&nbsp;"+ handlerName +"&nbsp;&nbsp;&nbsp;&nbsp;"+ remark +"&nbsp;&nbsp;&nbsp;&nbsp;"+ Brightcom.workflow.getDateByLongStr(createTime);
		approveArray.push(singleLog);
	}
	return approveArray.join("\r\n<br>");
}

function beforeCloseCounterSign(toCloseCounterNodeId,processParam){
	var processDefKey = Brightcom.workflow.getProcessDefKey();
	
	if(processDefKey =='publishSerial' && toCloseCounterNodeId=='usertask6'){
		 !processParam.sequenceFlow && (processParam.sequenceFlow='flow24')
	}
}
</script>
<input type="hidden" id="workflowSubmitAction" name="workflowSubmitAction" value="workflow@@publishAction@@completePublishTask">
<input type="hidden" id="id" name="id" value="">
<input type="hidden" id="selectGatewayId" name="selectGatewayId" value="">
<input type="hidden" id="officeApprover" name="officeApprover" value="">
<bc:with name="/DataPacket/Response/Data[1]">
<bc:foreach name="Record">

 	<h3 class="tit" style="padding:0px;width:735px">
 	  <a href="#tab1c1">���Ĵ����</a><a href="#tab1c2">��������</a><a href="#tab1c3">����</a>
 	</h3>
   	         <div class="box2" id="tab1c1">
               	  <div class="box2_content" >
                  	
    <table border="0" cellspacing="0" cellpadding="0" class="table3" >
     <tbody>
      <tr align="center" >
		<td class="tit" width="82">�ǼǺ�</td>
		<td width="72">
		    <label id="flddjh" name="flddjh"></label> 
		</td>
		<td class="tit" width="82">�����̶�</td>
		<td width="82">
		    <label id="fldjjcd" name="fldjjcd"></label> 
		</td>
		<td class="tit" width="82">��岿��</td>
		<td width="82">
		    <label id="fldzbbmmc" name="fldzbbmmc"></label> 
		</td>
		<td class="tit" width="82">�����</td>
		<td width="82" >
		    <label id="fldngr" name="fldngr"></label> 
		</td>
	  </tr>
	  <tr style="height:110px">
		<td width="32" class="tit" heigth="110px">����</td>
		<td  colspan="7" style="vertical-align: top;">
		    <label id="fwtmLabel" name="fwtmLabel"></label> 
		</td>
	  </tr>
	  <tr>
		<td width="32" class="tit">ԭ��</td>
		<td colspan="7" style="vertical-align: top;">
		    <label id="originalAttachment" name="originalAttachment"></label> 
		</td>
	  </tr>
	  <tr style="height:110px">
		<td width="32" class="tit" heigth="110px">������</td>
		<td heigth="110px" colspan="7" style="vertical-align: top;">
		    <label id="startLog" name="startLog"></label> 
		</td>
	  </tr>
	  <tr style="height:110px">
		<td class="tit" heigth="110">��岿�����</td>
		<td colspan="7" style="vertical-align: top;" >
		    <label id="deptLog" name="deptLog"></label> 
		    <!--  
		    <div style="float:left">
		      <span style="float:left;text-align:left">
		         <b>������ע </b>
		         <select name="regComments" onchange="InsertRegComment();" >
		            	<OPTION selected value=""></OPTION>
						<OPTION value=��ͬ��.>��ͬ��.</OPTION>
						<OPTION value=ͬ��>ͬ��</OPTION>
						<OPTION value=ͬ��ǩ��>ͬ��ǩ��</OPTION>
						<OPTION value=����>����</OPTION>
						<OPTION value=����>����</OPTION>
						<OPTION value=�Ѻ�>�Ѻ�</OPTION>
						<OPTION value=�˻�>�˻�</OPTION>
						<OPTION value=�Ѱ�>�Ѱ�</OPTION>
						<OPTION value=�ذ�>�ذ�</OPTION>
						<OPTION value=���>���</OPTION>
		         </select>
		         <br>
		      </span>
		      <textarea id="internalRemark" name="internalRemark" cols="90" rows="3" style="float:left"></textarea>	
		    </div>-->
		</td>
	  </tr>
	  <tr style="height:110px">
		<td class="tit" heigth="110">���Ż�ǩ���</td>
		<td colspan="7">
		    <label id="deptMultiLog" name="deptMultiLog"></label> 
		</td>
	  </tr >
	  <tr style="height:110px">
		<td class="tit" heigth="110">�칫�����</td>
		<td class="tit" colspan="7">
		    <label id="officeLog" name="officeLog"></label> 
		</td>
	  </tr>
	   <tr style="height:110px">
		<td class="tit" heigth="110">���쵼��ʾ</td>
		<td colspan="7">
		    <label id="curatorLog" name="curatorLog"></label> 
		</td>
	  </tr>
	   <tr>
		<td class="tit">��ע</td>
		<td colspan="7">
		    <label id="remark" name="remark"></label> 
		</td>
	  </tr>
	   </tr>
	
	 </tbody>
</table>
   <div class="clear cH1"></div>
</div>
</div>

<!-- ---------------------------------------------- -->
<div class="box2" id="tab1c2">
               	  <div class="box2_content">
                  <!--  <h4 class="tit">�ⷢ���ĵǼ�</h4> --> 
    <table border=0 cellpadding=0 cellspacing=0>
	    <tbody>
	    <tr>
	      <td colspan="4" height="5"></td>
	    </tr>
	    <tr>
	      <td><img border=0 src="/workflow/images/tab_left.gif"></td>
	      <td align="center" style="font-size:9pt" background="/workflow/images/tab_bg.gif" nowrap>&nbsp;�ǼǺ�:
	         <span id="fldDJH" style="color:red"></span></td>
	      <td><img border=0 src="/workflow/images/tab_right.gif"></td>
	      <td>&nbsp;&nbsp;</td>
	    </tr>
	    </tbody>
   </table>
  
   <table border="0" cellspacing="0" cellpadding="0" class="table3">
     <tbody>
    
      <tr >
		<td class="tit" >��������:</td>
		<td colspan="3">
		    <textarea id="fwtm" name="fwtm" cols="90" rows="2"  style="float:left"></textarea>	
		</td>
	  </tr>
	  <tr>
		<td class="tit"  width="130px">��������:</td>
		<td>
		    �ⷢ����    
		</td>
		<td class="tit" width="130px" >���첿��:</td>
		<td>
		  <label id="fldzbbmmcLabel" name="fldzbbmmcLabel"></label>
		</td>
	  </tr>
	  <tr>
		<td class="tit">�����:</td>
		<td>
		    <label id="fldngrLabel" name="fldngrLabel"></label>
		</td>
		
		<td class="tit">�����̶�:</td>
		<td>
		   <script language="JavaScript">
            document.writeln(JspParamUtil.paramData('fldjjcdLabel','','urgency_level','',[['style','width:100%']]));
           </script>
           <!--  
		   <select name="fldjjcdLabel" id="fldjjcdLabel"  style="width:100%">  
		      <option value = "0" selected>����</option>
	          <option value = "1">����</option>
	          <option value = "2">�ؼ�</option>
           </select> -->
		</td>
	  </tr>
	 <tr>
		<td class="tit">�ܼ�:</td>
		<td>
		  <script language="JavaScript">
		    document.writeln(JspParamUtil.paramData('fldmj','','security_level','',[['style','width:100%']]));
		  </script>
		    <!--  
		   <select id="fldmj" name="fldmj" style="width:100%" >
	          <option value = "0" selected>ƽ��</option>
	          <option value = "2">����</option>
	          <option value = "3">����</option>
	          <option value = "4">����</option>
	        </select>-->
		</td>
		
		<td class="tit">����:</td>
		<td>
		   <script language="JavaScript">
		      document.writeln(JspParamUtil.paramData('fldwz','','record_type',[['','��ѡ������']],[['style','width:100%']]));
		   </script>
		    <!-- 
		   <select name="fldwz" id="fldwz" style="width:100%">  
		      <option value = "0" selected>֪ͨ</option>
	          <option value = "1">ͨ��</option>
	          <option value = "2">�ؼ�</option>
           </select> -->
		</td>
	  </tr>
	  <!--  
	  <tr>
		<td class="tit">������:</td>
		<td colspan="3">
		    <textarea id="internalRemark" name="internalRemark" cols="90" rows="3" style="float:left"></textarea>	
		</td>
	  </tr>
	  
	  <tr class="tit">
		<td><span class="fl">ԭ�ģ�</span></td>
		<td colspan="3" style="text-align:left;">
		   <div style="float:left" id="originalAttachment">
		   </div>
         </td>
	  </tr>-->
    </tbody>
  </table>
  <div class="clear cH1"></div>
</div>
   <!-- <div class="box2_bottom"></div> -->
</div>
<!-- ---------------------------------------------- -->
 <div class="box2" id="tab1c3">
    <div class="box2_content">
          <table border="0" cellspacing="0" cellpadding="0" class="table2">
            <thead>
              <tr>
			    <th>�ļ���</th>
			    <th>��С(byte)</th>
			    <th>����</th>
              </tr>
            </thead>
            <tbody id="attachMentTby">
            </tbody>
          </table>
            
	         <div class="clear cH1"></div>
           </div>
        </div>
        <!-- ---------------------------------------------- -->
</bc:foreach>
</bc:with>
<script>
$(function(){ 
	//���Ч��
		$(".table2 tr:nth-child(2n)").addClass("tab");
		$(".table2tr").hover(function(){
			$(this).addClass("hover");},function(){
			$(this).removeClass("hover")
		})
		$(".tab1 h3").idTabs("!mouseover"); 
});
</script>
