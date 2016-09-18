<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="cn.brightcom.jraf.web.*"%>
<%@ page import="org.jdom.*"%>
<%@ page import="java.util.List"%>
<%@ taglib uri="/WEB-INF/brightcom.tld" prefix="bc" %>
<%
	String taskId =(String)request.getParameter("taskId");
	//TaskViewFormVO taskViewFormVO = (TaskViewFormVO) formResourceService.getRenderedTaskForm(taskId, "taskFormEngine");
	Document reqXml = HttpProcesser.createRequestPackage("workflow","formServiceOperate","getRenderedTaskForm",request);
	reqXml.getRootElement().getChild("Request").getChild("Data").addContent((new Element("query_taskId")).setText(taskId));
	Document xmlDoc = SwitchCenter.doPost(reqXml);
	
	String taskName ="";
	String taskFormKey ="";
	String processBusinessKey ="";
	String processInstanceId ="";
	String processDefKey ="";
	String taskViewFormVO ="";
	if (xmlDoc!=null && xmlDoc.getRootElement()!=null && xmlDoc.getRootElement().getChild("Response")!=null ){
		Element errorElement = xmlDoc.getRootElement().getChild("Response").getChild("Error");
		Element msgElement = errorElement.getChild("Msg");
		String msgElementText =  "";
		if(null!= msgElement && null !=msgElement.getText()){
			 msgElementText =  msgElement.getText();
%>	
<script type="text/javascript">
    alert('<%=msgElementText%>');
</script> 
<%		
		}else{
		Element elementData = xmlDoc.getRootElement().getChild("Response").getChild("Data");
		if(null != elementData){
			List recordList = elementData.getChildren("Record");
			if(recordList.size()>0){
				Element record = (Element)recordList.get(0);
			    taskFormKey = record.getChildTextTrim("taskFormKey");
			    processBusinessKey = record.getChildTextTrim("processBusinessKey");
			    processInstanceId = record.getChildTextTrim("processInstanceId");
			    processDefKey = record.getChildTextTrim("processDefKey");
			    
			    request.setAttribute("processBusinessKey",processBusinessKey);
			    request.setAttribute("processDefKey",processDefKey);
			    request.setAttribute("processInstanceId",processInstanceId);
			    taskViewFormVO = record.getChildTextTrim("taskViewFormVO");
			    //System.out.println("------taskViewFormVO"+record.getChildTextTrim("taskViewFormVO"));
			}
		}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>审批任务</title>

<link rel="shortcut icon" href="favicon.ico" type="image/x-icon" />
<!--<link href="/workflow/css/jalor.min.css" rel="stylesheet" type="text/css" />  -->

<link href="/workflow/css/public.css" rel="stylesheet" type="text/css" />
<link href="/workflow/css/layout.css" rel="stylesheet" type="text/css" />
<link href="/workflow/js/dialog/css/custom-theme/jquery-ui-1.9.0.custom.css" rel="stylesheet" />
<link href="/workflow/js/picture-preview/css/picture_preview.css" rel="stylesheet" />
<link href="/js/jquery/plugin/zebra_dialog/zebra_dialog.css" rel="stylesheet" type="text/css" />

<!--<script type="text/javascript" src="/js/jquery/jquery-1.10.2.min.js"></script>-->
<script type="text/javascript" src="/js/jquery/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="/workflow/workflow.js"></script>
<script type="text/javascript" src="/workflow/common/jquery-ui-1.9.2.min.js"></script>
<script type="text/javascript" src="/workflow/js/dialog/js/dialog1.0.js"></script>
<script type="text/javascript" src="/workflow/js/picture-preview/js/picture_preview.js"></script>
<script src="/js/jquery/jsrender.js" type="text/javascript"></script>
<script type="text/javascript" src="/js/jquery/common.js"></script>
<script src="/js/jquery/plugin/zebra_dialog/zebra_dialog.js" type="text/javascript"></script>
<script src="/js/jquery/plugin/zebra_dialog/zebra_dialog.src.js" type="text/javascript"></script>


<script id="subTaskRec" type="text/x-jsrender">	
<table style="width:735px;" border="0" cellspacing="0" cellpadding="0" class="table2" >
<thead> 
<tr>
<th>序号</th>
<th>子任务名称</th>
<th>处理人</th>
<th>处理意见</th>
<th>子任务结束时间</th>
</tr>
</thead>
{{for Data}}
    <tr>
      <td>{{:#index+1}}</td>
      <td>{{:taskName}}</td>
      <td>{{:assigneeName}}</td>
      <td>{{:comment}}</td>
      <td> {{subtaskEndTimeContent endTime/}}</td>
    </tr>
{{/for}}
</table>
</script>

<script id="processLogRec" type="text/x-jsrender">	
<table style="width:744px;" border="0" cellspacing="0" cellpadding="0" class="table2" >
<thead> 
<tr>
<th style="width: 6%;">序号</th>
<th style="width: 18%;">处理环节</th>
<th style="width: 20%;">处理人</th>
<th style="width: 18%;">处理时间</th>
<th style="width: 21%;">操作</th>
<th style="width: 17%;">意见</th>
</tr>
</thead>
{{for Data}}
    <tr>
      <td>{{:#index+1}}</td>
      <td>{{:taskdefname}}</td>
      <td>{{:handlername}}</td>
      <td>{{:createtime}}</td>
      <td>{{:operation}}</td>
      <td>{{:remark}}</td>
    </tr>
{{/for}}
</table>
</script>


<script type="text/javascript">
var taskId = "<%=taskId%>";
var selectHandlers = [];
$(document).ready(function(){
	//var taskViewFormObject = JSON.parse(taskViewFormVO);
	if('<%=taskViewFormVO%>'){
	   Brightcom.workflow.buildTaskForm(<%=taskViewFormVO%>);
	}else{
		//$("#taskViewHandlerDiv").hide();
	}
})


function showTaskLog(){
	var processInstanceId = $("#processInstanceId").val();
	var bcReq = new BcRequest('workflow','formServiceOperate','getApproveLog');
	bcReq.setExtraPs({"processInstanceId":processInstanceId});
	bcReq.setSuccFn(function(data,status){
		 $("#taskLogDiv").empty();
		 if(data.Data.length>0){
			 var _stuContent= $("#taskLogRec").render(data);
			 $("#taskLogDiv").append(_stuContent);
		 }else{
		   $("#taskLogDiv").append("<span style='width:100%;color:red;'>暂无数据</span>");
		 }
	});
	bcReq.postData();
}

$.views.tags({
	subtaskEndTimeContent:function(endTime){
	   return Brightcom.workflow.getDateByLongStr(endTime);
	}
})	
</script> 
</head>

<body>
<div class="wrapper">
<form id="taskViewForm" name="taskViewForm"  class='init jalor-form longtitle'>
<!--<input type="hidden" id="processTitle" name="processTitle" value="">-->
<input type="hidden" id="processDefKey" name="processDefKey" value="">
<input type="hidden" id="processDefName" name="processDefName" value="">
<input type="hidden" id="processDefId" name="processDefId" value="">
<input type="hidden" id="taskId" name="taskId" value="">
<input type="hidden" id="taskDefKey" name="taskDefKey" value="">
<input type="hidden" id="processInstanceId" name="processInstanceId" value="">
<input type="hidden" id="taskName" name="taskName" value="">
<input type="hidden" id="processBusinessKey" name="processBusinessKey" value="<%=processBusinessKey%>">
<input type="hidden" id="nextHandlers" name="nextHandlers" value="">
<input type="hidden" id="internalOperate" name="internalOperate" value="">

<div class="main">
<div class="h3tit" id="taskViewTitleDiv" style="heigth:25px"></div>
    <div class="box2" >
         <div class="box2_content">
  
               	  
<div class="tab1">
<div id="taskViewHandlerDiv" >
       <table style="width: 724px;border-width: 0px;margin:0 auto; ">
         <tr style="background-color: white">
           <td width="300px" style="width:300px;height:23px;vertical-align:top;word-break: normal;word-wrap: break-word;">
                           处理人:<span id="taskHandlerSpan"></span>
           </td>
           <td style="text-align: right;vertical-align:top;width:44%;">
                            流程创建人:<span id="taskCreatorSpan"></span>
           </td>
         </tr>
         <tr style="background-color: white">
         	<td>
         	       流程图片跟踪查看:---<a class="color3" id="lnkShowImage" href="#" img=''>showImage</a>
			</td>
			<td style="text-align: right;width:44%;">
				流程创建时间 :<span id="taskCreateTimeSpan"></span>
			</td>
         </tr>
       </table>
</div>

<div id="taskViewFormDiv" >
<%
out.flush();
request.getRequestDispatcher(taskFormKey).include(request,response);
%>
</div>



<div id="taskNameDiv" style="width:734px;margin:0 auto;background:url(../images/icon.png) 0 -460px repeat-x">
  <span id="taskNameSpan" style="margin-left:16px;margin-top:56px;font-weight:bold"></span>
</div>
<div id="taskRemarkDiv" style="height:52px;margin-left:20px;margin-top:5px;width:100%;">
   <span style="vertical-align:inherit;height:52px;font-weight:bold">意见</span>
   <textarea id="internalRemark" name="internalRemark" cols="80" rows="3"></textarea>	
</div>

<div id="processTextComponentDiv" style="margin-top:10px;" > </div>

<div id="subTaskDiv" style="display:none;width:734px;margin:0 auto;background:url(../images/icon.png) 0 -460px repeat-x">
  <span id="subTaskApan" style="margin-left:16px;margin-top:56px;font-weight:bold"> 子任务</span>
</div>

<div id="subTaskInfoDiv"></div>

<div id="taskViewButtonDiv" style="height:20px;text-align:center;"></div>


</div>

</div>
 <div class="box2_bottom"></div>
</div>



<div style="height:23px;width:744px;text-align: center;margin:0 auto;background:url(../images/icon.png) 0 -460px repeat-x">
  <span style="float:left;font-weight:bold">审批日志</span>
 <span style="height:13px;float:right;" onclick="Brightcom.workflow.showProcessLog()">
  <img class="Outline" id="Out1" style="cursor: hand;" src="../images/collapse.gif"/>
 </span> 
</div>
<div id="processLogDiv" style="display:none"></div>

</div>
</form>
</div>
</body>
</html>
<%
		}
	}
%>