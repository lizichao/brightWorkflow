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

   Document reqXml = HttpProcesser.createRequestPackage("workflow","receiveFileAction","viewReceiveFile",request);
   reqXml.getRootElement().getChild("Request").getChild("Data").addContent((new Element("query_businessKey")).setText(processBusinessKey));
   reqXml.getRootElement().getChild("Request").getChild("Data").addContent((new Element("processInstanceId")).setText(processInstanceId));
   reqXml.getRootElement().getChild("Request").getChild("Data").addContent((new Element("processDefKey")).setText(processDefKey));
   Document xmlDoc = SwitchCenter.doPost(reqXml);
   request.setAttribute("xmlDoc",xmlDoc);
   
   
	//Element data = xmlDoc.getRootElement().getChild("Response").getChild("Data");
	//Element record = null==data?null:data.getChild("Record");
	List records = xmlDoc.getRootElement().getChild("Response").getChild("Data").getChildren("Record");
	//String fwtm = "";
	//String fldgwlx = "";
	String receiveFileVO ="";
	String officeApprover ="";
	if (null != records) {
		Element record = (Element) records.get(0);
		receiveFileVO =  record.getChildTextTrim("receiveFileVO");
		officeApprover =  record.getChildTextTrim("officeApprover");
		//officeApprover =  record.getChildTextTrim("officeApprover");
		//fwtm = record.getChildTextTrim("fwtm");
		//fldgwlx = record.getChildTextTrim("fldgwlx");
	}
%>
<script type="text/javascript" src="/workflow/js/jquery.idTabs.min.js"></script>
<script src="/workflow/common/jspParamUtil.js"></script>
<script src="/platform/public/sysparam.js"></script>
<script type="text/javascript" src="/workflow/js/DatePicker/WdatePicker.js"></script>
<script>
Brightcom.workflow.afterBuildTaskForm = afterBuildTaskForm;
$(document).ready(function(){	
	Brightcom.workflow.beforeSubmit = beforeSubmit;
	Brightcom.workflow.beforeCloseCounterSign = beforeCloseCounterSign;
	bulidReceiveView(<%=receiveFileVO%>);
});	

/*
 * 收文来文分办是监控人，发文是主办人
 */
function beforeSubmit(formJsonData){
	var variableMap = new Brightcom.workflow.HashMap(); 
	var taskDefKey = Brightcom.workflow.getTaskDefKey();
	var processDefKey = Brightcom.workflow.getProcessDefKey();
	var officeApprover = "";
	if(taskDefKey == 'usertask4' ){
		officeApprover = '<%=userid%>';
		//if(processDefKey == 'receiveFile'){
			//variableMap.put("nextMonitor",officeApprover);
		//}
		
		if(Brightcom.workflow.selectFlowId == 'flow13'){
			variableMap.put("nextMonitor",officeApprover);
		}
	}
	
	if(taskDefKey == 'usertask10' ||  taskDefKey == 'usertask9'  ||  taskDefKey == 'usertask11'){//串行的设置监控人保证分办节点有监控人权限
		variableMap.put("nextMonitor",'<%=officeApprover%>');
	}
	
	//if(taskDefKey =='usertask4' && Brightcom.workflow.selectFlowId == 'flow13'){
		//Brightcom.workflow.setNextPrincipalHandlers('<%=userid%>');
	//}
	
	if(taskDefKey =='usertask6'){
	    if(Brightcom.workflow.selectFlowId == 'flow19' && processDefKey == 'receiveFile'){//如果是收文流程，只有管理员才有权限操作flow19(送各部门办理)，则把当前管理员加入监控人，这样下次再来文分办就可以有监控人来做分发操作
		//	Brightcom.workflow.setNextPrincipalHandlers('<%=userid%>');
			variableMap.put("nextMonitor",'<%=userid%>');
		}
	
		//variableMap.put("nextMonitor",officeApprover);
		if(Brightcom.workflow.selectFlowId == 'flow29'){//只针对串行收文流程
			variableMap.put("multiCompleteMark",'1' );
		}
	}
	
	if(taskDefKey == 'usertask8'){
		if(processDefKey == 'receiveFile' && Brightcom.workflow.selectFlowId == 'flow14'){
			variableMap.put("nextMonitor",'<%=userid%>');
		}
		if(Brightcom.workflow.selectFlowId == 'flow32'){//只针对串行收文流程
			variableMap.put("multiCompleteMark",'1' );
		}
	}
	
	if(taskDefKey == 'usertask6' || taskDefKey == 'usertask8' || taskDefKey == 'usertask7'){
		var gatewaySelectObject = Brightcom.workflow.flowInfos[gatewayFlowIdSelected || Brightcom.workflow.selectFlowId];
		variableMap.put("internalOperate",gatewaySelectObject ? gatewaySelectObject.name : "" );
	}
	
	variableMap.put("isCondition",$("#selectGatewayId").val());
	variableMap.put("processTitle",$("#receiveTitleLabel").text());
	if(officeApprover){
		variableMap.put("officeApprover",officeApprover);
	}
	
	formJsonData.id=  $("#id").val();
	formJsonData.officeApprover=  officeApprover;
	formJsonData.processParam=  variableMap.toJsonObject();
}

function afterBuildTaskForm(taskViewFormVO){
	var processDefKey = Brightcom.workflow.getProcessDefKey();
	
	$("#taskViewButtonDiv").append("&nbsp;&nbsp;<a class='btn1' id=\'back_btn\' name='back_btn'  onclick=\"saveReceiveFileVO();\" >保存<span></span></a>");
	var currentTaskKey = taskViewFormVO.taskKey;
	if((processDefKey=='receiveFile' && (currentTaskKey == 'usertask6' || currentTaskKey == 'usertask7' || currentTaskKey == 'usertask8')) || (processDefKey=='receiveFileSerial' && currentTaskKey == 'usertask7')){
		var operateButtonArray = [];
		if(taskViewFormVO.monitor){
			operateButtonArray.push("&nbsp;&nbsp;<a class='btn1' id=\'back_btn\' name='back_btn'  onclick=\"editCounterInfo('"+taskViewFormVO.multi_kind+"\');\" >修改流程<span></span></a>");
		}
		for (var j=0;j<taskViewFormVO.flows.length;j++){
			var flowObject = taskViewFormVO.flows[j];
			var flowId = flowObject.id;
			Brightcom.workflow.flowInfos[flowId] = flowObject;
			
			//taskViewFormVO.multiUserTask && taskViewFormVO.multiCreator && taskViewFormLen>1
			//会签发起人也就是监控人，需要以下操作
			if(flowObject.dest.type=='exclusiveGateway' && ('<%=officeApprover%>' =="<%=userid%>" || taskViewFormVO.monitor)){
				if(taskViewFormVO.multiUserTask){
					var gatewayFlows = flowObject.dest.outgoingFlowVOs;
					for (var k=0;k<gatewayFlows.length;k++){
						var gatewayObject =  gatewayFlows[k];
						Brightcom.workflow.flowInfos[gatewayObject.id] = gatewayObject;
						if(taskViewFormVO.lastMultiTask){//其实这里也可以不用判断是否是最后一个多实例节点，直接都用completeReceiveTask，只是设置下multiCompleteMark为1，就会结束会签节点，同时判断不是最后一个任务需要提示是否要强制结束
							operateButtonArray.push("<a class='btn1' id=\'"+gatewayObject.flowId+"\'  value=\'"+gatewayObject.name+"\'  onclick=\"completeReceiveTask(\'"+flowId+"\',\'"+gatewayObject.id+"\');\" >"+gatewayObject.name+"<span></span></a>");
						}else{
							operateButtonArray.push("<a class='btn1' id=\'"+gatewayObject.flowId+"\'  value=\'"+gatewayObject.name+"\'  onclick=\"closeReceiveTask(\'"+flowId+"\',\'"+gatewayObject.id+"\',\'"+taskViewFormVO.taskKey+"\');\" >"+gatewayObject.name+"<span></span></a>");
						}
					}
					/*
					if(taskViewFormVO.multiCreator){
						operateButtonArray.push("<a  class='btn1' id='addCounterSign' name='addCounterSign' value='加签' >加签<span></span></a>");
						operateButtonArray.push("<a  class='btn1' id='removeCounterSign' name='removeCounterSign' value='减签' >减签<span></span></a>");
					}*/
				}else{//这里只是针对收文流程串行节点完成审批节点，保证能结束节点在重新生成节点
					var gatewayFlows = flowObject.dest.outgoingFlowVOs;
					for (var k=0;k<gatewayFlows.length;k++){
						var gatewayObject =  gatewayFlows[k];
						Brightcom.workflow.flowInfos[gatewayObject.id] = gatewayObject;
						if(gatewayObject.id !='flow24' && gatewayObject.id !='flow27' ){//这里的情况是收文流程串行节点完成设置完成路由的条件
							operateButtonArray.push("<a class='btn1' id=\'"+gatewayObject.flowId+"\'  value=\'"+gatewayObject.name+"\'  onclick=\"completeReceiveTask(\'"+flowId+"\',\'"+gatewayObject.id+"\');\" >"+gatewayObject.name+"<span></span></a>");
						}
					}
					//operateButtonArray.push("<a class='btn1' id='addCounterSignSingle' name='addCounterSignSingle' value='加签' onclick=\"addReceiveCounterSign(\'"+taskViewFormVO.processInstanceId+"\');\" >加签<span></span></a>");
					//operateButtonArray.push("<a class='btn1' id='removeCounterSignSingle' name='removeCounterSignSingle' value='减签'  onclick=\"removeReceiveCounterSign(\'"+taskViewFormVO.processInstanceId+"\');\" >减签<span></span></a>");
				}
			}else{
				if(taskViewFormVO.multiUserTask){
					if(taskViewFormVO.principal){//这里的主办就没有可以选择是强制结束协办人或者不处理的功能
						//operateButtonArray.push("<input type='button' id=\'"+flowId+"\' name='qry_btn' value=\'办结任务\'  onclick=\"Brightcom.workflow.completePrincipalTask(\'"+taskViewFormVO.taskKey+"\',\'"+taskViewFormVO.flows[j].id+"\');\" />");
						operateButtonArray.push("<a  class='btn1' id=\'"+flowId+"\' name='qry_btn'   onclick=\"Brightcom.workflow.completeButtonTask(\'"+taskViewFormVO.flows[j].id+"\',\'"+Brightcom.workflow.taskType+"\');\" >办结任务<span></span></a>");
					}else{
						//operateButtonArray.push("<input type='button' id=\'"+flowId+"\' name='qry_btn' value='提交协办任务'  onclick=\"Brightcom.workflow.completeButtonTask(\'"+taskViewFormVO.flows[j].id+"\',\'"+operateType+"\');\" />");
						operateButtonArray.push("<a  class='btn1' id=\'"+flowId+"\' name='qry_btn'   onclick=\"Brightcom.workflow.completeButtonTask(\'"+taskViewFormVO.flows[j].id+"\',\'"+Brightcom.workflow.taskType+"\');\" >办结任务<span></span></a>");
					}
				}else{
					operateButtonArray.push("<a class='btn1' id=\'"+flowId+"\'  onclick=\"completeReceiveTask(\'"+taskViewFormVO.flows[j].id+"\');\" >"+taskViewFormVO.flows[j].name+"<span></span></a>");
				}
			}
		}
		$("#taskViewButtonDiv").html("");
		operateButtonArray.push("<a class='btn1' id=\'back_btn\' name='back_btn' value=\'保存\'  onclick=\"saveReceiveFileVO();\" >保存<span></span></a>");
		operateButtonArray.push("<a class='btn1' id=\'back_btn\' name='back_btn' value=\'返回\'  onclick=\"javascript:history.go(-1);\" >返回<span></span></a>");
		$("#taskViewButtonDiv").append(operateButtonArray.join("&nbsp;&nbsp;"));
		
		$("#addCounterSign").click(function () {
			Brightcom.workflow.componentOperation[ $(this).attr('id')].call($(this),taskViewFormVO.multiUserTasks[0]);
		});
		$("#removeCounterSign").click(function () {
			Brightcom.workflow.componentOperation[ $(this).attr('id')].call($(this),taskViewFormVO.multiUserTasks[0]);
	    });
	}
}

var gatewayFlowIdSelected = "";
function completeReceiveTask(flowId,gatewayFlowId){
	var isCondition = 'default';
	if(gatewayFlowId){
		isCondition = gatewayFlowId;
	}
	$("#selectGatewayId").val(isCondition);
	if(gatewayFlowId){
		gatewayFlowIdSelected = gatewayFlowId;
		Brightcom.workflow.completeButtonTask(gatewayFlowId,Brightcom.workflow.taskType);
	}else{
		Brightcom.workflow.submitWorkflowRequest(Brightcom.workflow.taskType,flowId);
	}
}

function closeReceiveTask(flowId,gatewayFlowId,currentTaskKey){
	$("#selectGatewayId").val(gatewayFlowId);
	/*	var gatewayObject =Brightcom.workflow.flowInfos[gatewayFlowId];
		var nextHandlers = gatewayObject.dest.configHandlers;
		var nextHandlerIds = [];
		for(var i=0;i<nextHandlers.length;i++){
			nextHandlerIds.push(nextHandlers[i].userId);
		}*/
		
		var gatewayObject =Brightcom.workflow.flowInfos[gatewayFlowId];
		var targetNode = gatewayObject.dest;
		if(targetNode.configHandlers.length==1){
			var paramVariable= {
					"isCondition":flowId,
					"nextHandlers":targetNode.configHandlers[0].userId
			};
			Brightcom.workflow.closeCounterSignOperate(currentTaskKey,gatewayFlowId,paramVariable);
		}else if(targetNode.configHandlers.length==0){
			Brightcom.workflow.submitWorkflowRequest(Brightcom.taskType,gatewayFlowId);
		}else{
			openReceiveDialog(targetNode,gatewayFlowId,currentTaskKey);
		}
		/*
		var paramObject = {
				"isCondition":gatewayFlowId,
				"nextHandlers":nextHandlerIds.join(",")
		};
		Brightcom.workflow.closeCounterSignOperate(currentTaskKey,paramObject);*/
		
	//var multiConfirm = window.confirm("还有其他人未完成审批，需要强制结束会签任务吗？");
	//if (multiConfirm){

	//}else{
		//return false;
	//}
}


function openReceiveDialog(targetNode,flowId,currentTaskKey,isPrincipal){
    var url = "/workflow/receiveFile/selectTaskUser.jsp";
    var dlgid = "selectTaskUserDialog";
    var options = {};
    options.title = "选择审批人";
    options.width = "465";
    options.height = "469";
    
    var taskId = $("#taskId").val();
   // var selectedUsers = $("#selectHandlers").val();
    options.param = {
    		'taskId':taskId,
    		'flowId':flowId,
    		'configHandlers': targetNode.configHandlers,
    		'currentTaskKey': currentTaskKey,
    		'isPrincipal': isPrincipal
    };
    $.Dialog.open(url, dlgid, options);
}


function addReceiveCounterSign(processInstanceId){
	var url = "/workflow/receiveFile/counterSign.jsp";
    var dlgid = "addCounterSignDialog";
    var options = {};
    options.title = "加签";
    options.width = "472";
    options.height = "478";
    options.url = url;
    options.dlgid = dlgid;
    
    getReceiveCounterInfo(processInstanceId,'add',options);
}

function getReceiveCounterInfo(processInstanceId,operate,options){
	   var bcReq = new BcRequest('workflow','receiveFileAction','getCounterInfo');
		bcReq.setExtraPs({
			"processInstanceId":processInstanceId,
			'counterOperate':operate,
			'officeApprover':$("#officeApprover").val()
		});
		bcReq.setSuccFn(function(data,status){
			if(data.Data.length == 0){
				var prompt = (operate =='add')? '加签' : '减签';
				alert("可以"+prompt+"的人为空！");
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
		        //这是回调函数 ，data自行定义，在模态窗口关闭后对本页面进行操作 
		    };
		    $.Dialog.open(options.url, options.dlgid, options);
		
		});
		bcReq.postData();
}

function removeReceiveCounterSign(processInstanceId){
	var url = "/workflow/receiveFile/counterSign.jsp";
    var dlgid = "removeCounterSignDialog";
    var options = {};
    options.title = "减签";
    options.width = "420";
    options.height = "458";
    options.url = url;
    options.dlgid = dlgid;
    
    getReceiveCounterInfo(processInstanceId,'remove',options);
    /*
    options.param = {
    		 'counterOperate':'remove',
    		 'officeApprover':$("#officeApprover").val(),
    		 'processInstanceId':processInstanceId,
    		 'taskId': $("#taskId").val(),
    		 'id': $("#id").val()
    };
    options.callback = function(data) {
    	window.location.reload();
        //这是回调函数 ，data自行定义，在模态窗口关闭后对本页面进行操作 
    };
    $.Dialog.open(url, dlgid, options);*/
}


function bulidReceiveView(receiveFileVO){
	var taskDefKey = Brightcom.workflow.getTaskDefKey();
	if(taskDefKey == 'usertask6' || taskDefKey == 'usertask4' || taskDefKey == 'usertask9' || taskDefKey == 'usertask8' || taskDefKey == 'usertask10' || taskDefKey == 'usertask11'){
		Brightcom.workflow.departmentPrincipal = true;
	}
	$("#taskRemarkDiv").hide();
	$("#id").val(receiveFileVO.id);
	$("#officeApprover").val(receiveFileVO.officeApprover);
	
	$("#receiveOfficeLabel").text(receiveFileVO.receiveOffice);
	$("#receiveOffice").val(receiveFileVO.receiveOffice);
	
	$("#receiveWordLabel").text(receiveFileVO.receiveWord);
	$("#receiveWord").val(receiveFileVO.receiveWord);
	
	$("#registerNumberLabel").text(receiveFileVO.registerNumber);
	$("#fldDJH").text(receiveFileVO.registerNumber);
	
	$("#receiveDateLabel").text(Brightcom.workflow.getDateStrByLong(receiveFileVO.receiveDate));
	$("#receiveDate").val(Brightcom.workflow.getDateStrByLong(receiveFileVO.receiveDate));
	
	$("#urgentLabel").text(JspParamUtil.paramVal('urgency_level',receiveFileVO.urgent));
	$("#urgent").val(receiveFileVO.urgent);
	
	$("#receiveTitleLabel").text(receiveFileVO.receiveTitle);
	$("#receiveTitle").val(receiveFileVO.receiveTitle);
	
	$("#securityLevel").val(receiveFileVO.securityLevel);
	$("#finishDate").val(Brightcom.workflow.getDateStrByLong(receiveFileVO.finishDate));
	
	//$("#fileType").val(receiveFileVO.fileType);
	 $("input[id=fileType][value="+receiveFileVO.fileType+"]").attr("checked",true); 
	 
	 if(receiveFileVO.separateDepts.length>0){
		 $("#separateTr").show();
		   var separateDeptArray = [];
			separateDeptArray.push("<tr><td>发出时间</td><td>姓名/单位</td><td>办理类型</td><td>返回时间</td></tr>");
			for(var k=0;k<receiveFileVO.separateDepts.length;k++){
				var separateDeptsEach = receiveFileVO.separateDepts[k];
				var dealType = separateDeptsEach.isPrincipal =='1' ? '主办' : '分办';
				var createTime = Brightcom.workflow.getDateStrByLong(separateDeptsEach.createTime);
				var returnTime = Brightcom.workflow.getDateStrByLong(separateDeptsEach.returnTime || "");
				
				separateDeptArray.push("<tr>");
				separateDeptArray.push("<td>"+createTime+"</td>");
				separateDeptArray.push("<td>"+separateDeptsEach.deptname+"</td>");
				separateDeptArray.push("<td>"+dealType+"</td>");
				separateDeptArray.push("<td>"+returnTime+"</td>");
				separateDeptArray.push("</tr>");
			}
			$("#separateTab").append(separateDeptArray.join(""));
	 }
	
	
	
	var approveLogs =  receiveFileVO.approveLogs;
	var startApprove =  approveLogs["startEvent"].concat(approveLogs["usertask1"]|| []).concat(approveLogs["usertask2"]|| []).concat(approveLogs["usertask3"]|| []);//发起流程 办公室拟办意见
	var deptMultiApprove =  approveLogs["usertask7"];//部门办理意见
	var curatorApprove =  approveLogs["usertask8"];//馆领导核示
	var curatorAuditLog =  approveLogs["usertask6"];//馆领导批示
	
	$("#startLog").html(bulidApproveStr(startApprove,receiveFileVO));
	$("#curatorLog").html(bulidApproveStr(curatorApprove,receiveFileVO));
	$("#deptMultiLog").html(bulidApproveStr(deptMultiApprove,receiveFileVO));
	$("#curatorAuditLog").html(bulidApproveStr(curatorAuditLog,receiveFileVO));

	if(receiveFileVO.officeApprover !="<%=userid%>" ){
		bulidEditRemark();
	}
	
	
	var attachmentArray = [];
	for(var j=0;j<receiveFileVO.attachMents.length;j++){
		var attachMentObject =  receiveFileVO.attachMents[j];
		//attachmentArray.push("<a href=\"javascript:downLoadPublishAttachment(\'"+attachMentObject.attachmentId+"\',\'"+attachMentObject.fileName+"\')\" >"+attachMentObject.fileName+"</a>");
		//attachmentArray.push("<a href=\"<%=basePath%>WorkflowAttachMentDownload?attachmentName="+attachMentObject.fileName+"\">"+attachMentObject.fileName+"</a>");
        // return attachmentArray.join("\r\n<br>");
		attachmentArray.push("<tr>");
		attachmentArray.push("<td><a class=\"color3\"  href=\"<%=basePath%>WorkflowAttachMentDownload?attachmentId="+attachMentObject.attachmentId+"\">"+attachMentObject.fileName+"</a></td>");
		attachmentArray.push("<td>"+attachMentObject.fileSize+"</td>");
		attachmentArray.push("<td><a class=\"color3\"  href=\"<%=basePath%>WorkflowAttachMentDownload?attachmentId="+attachMentObject.attachmentId+"\">下载</a></td>");
		attachmentArray.push("</tr>");
	}
	$("#attachMentTby").append(attachmentArray.join(""));
	/*
	var attachmentArray = [];
	for(var j=0;j<receiveFileVO.attachMents.length;j++){
		var attachMentObject =  receiveFileVO.attachMents[j];
		attachmentArray.push("<a href=\"<%=basePath%>WorkflowAttachMentDownload?attachmentName="+attachMentObject.fileName+"\">"+attachMentObject.fileName+"</a>");
	}
	$("#attachMentTby").append(attachmentArray.join(""));*/
	//$("#attachmentText").html(attachmentArray.join("\r\n<br>"));
	
//	$("#taskViewButtonDiv").append("&nbsp;&nbsp;<input type='button' id=\'back_btn\' name='back_btn' value=\'保存\'  onclick=\"saveReceiveFileVO();\" />");
}

function bulidEditRemark(){
	var currentTaskKey = Brightcom.workflow.getTaskDefKey();
	var editRemarkDiv = "";
	switch (currentTaskKey) {
		case 'usertask8'://馆领导核示
			editRemarkDiv = "curatorLog";
		   break;
		case 'usertask7'://部门会签意见
			editRemarkDiv = "deptMultiLog";				   
		    break;
		case 'usertask6'://馆领导批示
			editRemarkDiv = "curatorAuditLog";				   
		    break;
		case 'usertask1'://主任核示意见
			editRemarkDiv = "startLog";				   
		    break;
		case 'usertask2'://主任核示意见
			editRemarkDiv = "startLog";				   
		    break;
		case 'usertask3'://主任核示意见
			editRemarkDiv = "startLog";				   
		    break;
   }
	
	var editRemarkArray = [];
	editRemarkArray.push("<div id='publishEditRemark' style='float:left'>");
	editRemarkArray.push("<span style='float:left;text-align:left'>");
	editRemarkArray.push("   <b>典型批注 </b>");
	editRemarkArray.push("  <select id='regComments' name='regComments' onchange='insertRegComment();' >");
	editRemarkArray.push("     	<OPTION selected value=''></OPTION>");
	editRemarkArray.push("		<OPTION value=拟同意>拟同意.</OPTION>");
	editRemarkArray.push("	<OPTION value=同意>同意</OPTION>");
	editRemarkArray.push("	<OPTION value=同意签发>同意签发</OPTION>");
	editRemarkArray.push("	<OPTION value=已审>已审</OPTION>");
	editRemarkArray.push("	<OPTION value=已阅>已阅</OPTION>");
	editRemarkArray.push("	<OPTION value=已核>已核</OPTION>");
	editRemarkArray.push("	<OPTION value=退回>退回</OPTION>");
	editRemarkArray.push("<OPTION value=已办>已办</OPTION>");
	editRemarkArray.push("<OPTION value=重办>重办</OPTION>");
	editRemarkArray.push("<OPTION value=办结>办结</OPTION>");
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

function bulidApproveStr(approveLogs,receiveFileVO){
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
		
		if((taskDefKey =='usertask6' ||taskDefKey =='usertask7' ||taskDefKey =='usertask8') && handlerId==receiveFileVO.officeApprover){//部门会签意见
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

function saveReceiveFileVO(){
	var receiveTitle = $("#receiveTitle").val();
	var receiveOffice = $("#receiveOffice").val();
	var receiveWord =$("#receiveWord").val();
	var fileType =$("#fileType").val();
	var receiveDate =$("#receiveDate").val();
	var urgent =$("#urgent").val();
	var securityLevel =$("#securityLevel").val();
	var finishDate =$("#finishDate").val();
	var id =$("#id").val();
	
	 
	var bcReq = new BcRequest('workflow','receiveFileAction','saveReceiveFileData');
	bcReq.setExtraPs({
		"receiveTitle":$("#receiveTitle").val(),
		"receiveOffice":$("#receiveOffice").val(),
		"receiveWord":$("#receiveWord").val(),
		"fileType":$("#fileType:checked").val(),
		"receiveDate":$("#receiveDate").val(),
		"urgent":$("#urgent").val(),
		"securityLevel":$("#securityLevel").val(),
		"finishDate":$("#finishDate").val(),
		"receiveRemark":$("#internalRemark").val(),
		"id" : id
	});
	bcReq.setSuccFn(function(data,status){
		 window.location.reload();
	});
	bcReq.postData();
}

function selectDeleteTime(timeObject){
	WdatePicker({
		readOnly:true,
		isShowClear:false,
		isShowOthers:false,
		maxDate:'2099-12-31 23:59:59',
		onpicked:function(dp){
			//getMessageRecord(dp)
		}
	});
}

function beforeCloseCounterSign(toCloseCounterNodeId,processParam){
	var processDefKey = Brightcom.workflow.getProcessDefKey();
	if(processDefKey =='receiveFileSerial'){
		 switch (toCloseCounterNodeId) {
	         case "usertask6":
	        	 !processParam.sequenceFlow && (processParam.sequenceFlow='flow19')
	             break;
	         case "usertask7":
	        	 !processParam.sequenceFlow && (processParam.sequenceFlow='flow7')
	             break;
	         case "usertask8":
	        	 !processParam.sequenceFlow && (processParam.sequenceFlow='flow31')
	             break;
	         default:
	             break
       }
	//	if(toCloseCounterNodeId =='usertask6' || toCloseCounterNodeId =='usertask7' || toCloseCounterNodeId =='usertask8' ){
			//if(!processParam.sequenceFlow){
				//processParam.sequenceFlow
			//}
		//}
	}
	
	if(processDefKey =='receiveFile' && toCloseCounterNodeId=='usertask7'){
		 !processParam.sequenceFlow && (processParam.sequenceFlow='flow7')
	}
}

function editCounterInfo(multiKind){
	Brightcom.workflow.editCounterInfo(multiKind);
}
/*
function editCounterInfo(){
	var bcReq = new BcRequest('workflow','receiveFileAction','getEditCounterInfo');
	bcReq.setExtraPs({
		"processInstanceId":Brightcom.workflow.getProcessInstanceId(),
		"processDefKey":Brightcom.workflow.getProcessDefKey(),
		"taskDefKey":Brightcom.workflow.getTaskDefKey(),
		"businessKey":Brightcom.workflow.getProcessBusinessKey()
	});
	bcReq.setSuccFn(function(data,status){
		var url = "/workflow/receiveFile/editCounterInfo.jsp";
		var dlgid = "editCounterInfoDialog";
        var options = {};
        options.title = "修改流程";
        options.width = "468";
        options.height = "508";
        
        options.param = {
        		"processInstanceId":Brightcom.workflow.getProcessInstanceId(),
        		"processDefKey":Brightcom.workflow.getProcessDefKey(),
        		"taskDefKey":Brightcom.workflow.getTaskDefKey(),
        		"businessKey":Brightcom.workflow.getProcessBusinessKey(),
        		"deptData":data
        };
        
        options.callback = function(data) {
        };
        $.Dialog.open(url, dlgid, options);
	});
	bcReq.postData();
}*/
</script>
<input type="hidden" id="workflowSubmitAction" name="workflowSubmitAction" value="workflow@@receiveFileAction@@completeReceiveFileTask">
<input type="hidden" id="id" name="id" value="">
<input type="hidden" id="selectGatewayId" name="selectGatewayId" value="">
<input type="hidden" id="officeApprover" name="officeApprover" value="">
<bc:with name="/DataPacket/Response/Data[1]">
<bc:foreach name="Record">
 	<h3 class="tit" style="padding:0px;width:735px">
 	  <a href="#tab1c1">收文处理表</a><a href="#tab1c2">基本属性</a><a href="#tab1c3">附件</a>
 	</h3>
   	         <div class="box2" id="tab1c1">
               	  <div class="box2_content" >
               	  
<table border="0" cellspacing="0" cellpadding="0" class="table3" >
      <tr>
		<td width="89">来文单位</td>
		<td width="52" colspan="2">
		    <label id="receiveOfficeLabel" name="receiveOfficeLabel"></label> 
		</td>
		<td >来文字号</td>
		<td colspan="2">
		    <label id="receiveWordLabel" name="receiveWordLabel"></label> 
		</td>
		<td >办文编号</td>
		<td width="52" colspan="2">
		    <label id="registerNumberLabel" name="registerNumberLabel"></label> 
		</td>
	  </tr>
	  
	  <tr >
		<td >收文日期</td>
		<td colspan="3">
		    <label id="receiveDateLabel" name="receiveDateLabel"></label> 
		</td>
		<td width="206">紧急程度</td>
		<td colspan="4">
		    <label id="urgentLabel" name="urgentLabel"></label> 
		</td>
		
		
	  </tr>
	   <tr>
	     <td >文件标题</td>
		 <td colspan="8">
		    <label id="receiveTitleLabel" name="receiveTitleLabel"></label> 
		 </td>
	   </tr>
	    <tr id="separateTr" style="display:none">
	      <td colspan="9" >
	         <table id="separateTab" border="0" cellspacing="0" cellpadding="0" class="table3" >
	     
	         </table>
	      </td>
	     </tr>
	   
	   <!-- 
	   <tr>
	     <td >发出日期</td>
		 <td width="65">
		    <label id="leaveDate1" name="leaveDate1"></label> 
		 </td>
		  <td width="65">
		   <label id="leaveDate2" name="leaveDate2"></label> 
		 </td>
		  <td width="65">
		    <label id="leaveDate3" name="leaveDate3"></label> 
		 </td>
		  <td width="15">
		    <label id="leaveDate4" name="leaveDate4"></label> 
		 </td>
		  <td width="65">
		    <label id="leaveDate5" name="leaveDate5"></label> 
		 </td>
		  <td width="65">
		   <label id="leaveDate6" name="leaveDate6"></label> 
		 </td>
		  <td width="65">
		     <label id="leaveDate7" name="leaveDate7"></label> 
		 </td>
		  <td width="65">
		    <label id="leaveDate8" name="leaveDate8"></label> 
		 </td>
	   </tr>
	   
	   
	    <tr>
	     <td >姓名/单位</td>
		 <td >
		    <label id="nameCompany1" name="nameCompany1"></label> 
		 </td>
		 <td >
		    <label id="nameCompany2" name="nameCompany2"></label> 
		 </td>
		<td >
		    <label id="nameCompany3" name="nameCompany3"></label> 
		 </td>
		 <td >
		    <label id="nameCompany4" name="nameCompany4"></label> 
		 </td>
		<td >
		    <label id="nameCompany5" name="nameCompany5"></label> 
		 </td>
		 <td >
		    <label id="nameCompany6" name="nameCompany6"></label> 
		 </td>
		 <td >
		    <label id="nameCompany7" name="nameCompany7"></label> 
		 </td>
		 <td >
		    <label id="nameCompany8" name="nameCompany8"></label> 
		 </td>
	   </tr>
	   
	   
	    <tr>
	     <td >办理类型</td>
		 <td >
		    <label id="manageType1" name="manageType1"></label> 
		 </td>
		  <td >
		    <label id="manageType2" name="manageType2"></label> 
		 </td>
		  <td >
		    <label id="manageType3" name="manageType3"></label> 
		 </td>
		  <td >
		    <label id="manageType4" name="manageType4"></label> 
		 </td>
		  <td >
		    <label id="manageType5" name="manageType5"></label> 
		 </td>
		  <td >
		    <label id="manageType6" name="manageType6"></label> 
		 </td>
		  <td >
		    <label id="manageType7" name="manageType7"></label> 
		 </td>
		 <td >
		    <label id="manageType7" name="manageType7"></label> 
		 </td>
	   </tr>
	   
	   
	   <tr>
	     <td >返回日期</td>
		 <td >
		    <label id="backDate1" name="backDate"></label> 
		 </td>
		  <td >
		    <label id="backDate" name="backDate"></label> 
		 </td>
		  <td >
		    <label id="backDate" name="backDate"></label> 
		 </td>
		  <td >
		    <label id="backDate" name="backDate"></label> 
		 </td>
		  <td >
		    <label id="backDate" name="backDate"></label> 
		 </td>
		  <td >
		    <label id="backDate" name="backDate"></label> 
		 </td>
		  <td >
		    <label id="backDate" name="backDate"></label> 
		 </td>
		  <td >
		    <label id="backDate" name="backDate"></label> 
		 </td>
	   </tr> -->
	   
	  <tr style="height:110px">
		<td >办公室拟办意见</td>
		<td colspan="8">
		    <label id="startLog" name="startLog"></label> 
		</td>
	  </tr>
	  <tr style="height:110px">
		<td >馆领导批示</td>
		<td colspan="8">
		    <label id="curatorAuditLog" name="curatorAuditLog"></label> 
		</td>
	  </tr>
	  <tr style="height:110px">
		<td >部门办理意见</td>
		<td colspan="8">
		    <label id="deptMultiLog" name="deptMultiLog"></label> 
		</td>
	  </tr>
	   <tr style="height:110px">
		<td >馆领导核示</td>
		<td colspan="8">
		    <label id="curatorLog" name="curatorLog"></label> 
		</td>
	  </tr>
	
	   <tr style="height:110px">
		<td>备注</td>
		<td colspan="8">
		    <label id="remark" name="remark"></label> 
		</td>
	  </tr>
</table>

   <div class="clear cH1"></div>
</div>
</div>

<!-- ---------------------------------------------- -->
<div class="box2" id="tab1c2">
               	  <div class="box2_content">
                  <!--  <h4 class="tit">外发公文登记</h4> --> 
    <table border=0 cellpadding=0 cellspacing=0>
	    <tbody>
	    <tr>
	      <td colspan="4" height="5"></td>
	    </tr>
	    <tr>
	      <td><img border=0 src="/workflow/images/tab_left.gif"></td>
	      <td align="center" style="font-size:9pt" background="/workflow/images/tab_bg.gif" nowrap>&nbsp;登记号:
	         <span id="fldDJH" style="color:red"></span></td>
	      <td><img border=0 src="/workflow/images/tab_right.gif"></td>
	      <td>&nbsp;&nbsp;</td>
	    </tr>
	    </tbody>
   </table>
  
  
<table border="0" cellspacing="0" cellpadding="0" class="table3" >
    <tbody>
     <tr>
		<td class="tit" >标题</td>
		<td colspan="3">
		    <textarea id="receiveTitle" name="receiveTitle" cols="90" rows="2"  style="float:left"></textarea>	
		</td>
	  </tr>
	  <tr >
		<td class="tit" >来文机关</td>
		<td >
		  <input type="text" id="receiveOffice" name="receiveOffice" style="float:left">
		</td>
		
		<td class="tit" >来文字号</td>
		<td >
		    <input type="text" id="receiveWord" name="receiveWord" style="float:left">
		</td>
	  </tr>
	  <tr >
		<td class="tit" >文件分类:</td>
		<td >
		  <div style="float:left">
		    <input type='radio' class="praxes_type" id="fileType" name='fileType' value="1" />办件
            <input type='radio' class="praxes_type" id="fileType" name='fileType' value="2" />阅件
            </div>
		</td>
		
		<td class="tit" >收文日期</td>
		<td >
		    <input type="text" id="receiveDate" name="receiveDate" onclick="selectDeleteTime()" style="float:left">
		</td>
	  </tr>
	 <tr>
	 	<td class="tit" >紧急程度</td>
		<td>
		   <script language="JavaScript">
            document.writeln(JspParamUtil.paramData('urgent','0','urgency_level','',[['style','width:100%']]));
          </script>
          <!--  
		   <select name="urgent" id="urgent"  style="width:100%">  
		      <option value = "0" selected>正常</option>
	          <option value = "1">急件</option>
	          <option value = "2">特急</option>
           </select> -->
		</td>
		
		<td class="tit" >秘密等级:</td>
		<td >
		   <script language="JavaScript">
		      document.writeln(JspParamUtil.paramData('securityLevel','0','security_level','',[['style','width:100%']]));
		   </script>
		<!--  
		   <select id="securityLevel" name="securityLevel" style="width:234px;">
	          <option value = "0" selected>平件</option>
	          <option value = "2">秘密</option>
	          <option value = "3">机密</option>
	          <option value = "4">绝密</option>
	        </select>-->
		</td>
	  </tr>
	  
	 <tr>
	 	<td class="tit" >办结日期</td>
		<td colspan="3">
		   <input type="text" id="finishDate" name="finishDate" onclick="selectDeleteTime()" style="float:left"/>
		</td>
	  </tr>
	
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
			    <th>文件名</th>
			    <th>大小(byte)</th>
			    <th>操作</th>
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
	//表格效果
		$(".table2 tr:nth-child(2n)").addClass("tab");
		$(".table2tr").hover(function(){
			$(this).addClass("hover");},function(){
			$(this).removeClass("hover")
		})
		$(".tab1 h3").idTabs("!mouseover"); 
});
</script>
