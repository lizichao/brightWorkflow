<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="cn.brightcom.jraf.web.*"%>
<%@ page import="org.jdom.*"%>
<%@ page import="java.util.List"%>
<%@ taglib uri="/WEB-INF/brightcom.tld" prefix="bc" %>
<%
	String processInstanceId =(String)request.getParameter("processInstanceId");
	System.out.println("processInstanceId========="+processInstanceId);
	Document reqXml = HttpProcesser.createRequestPackage("workflow","formServiceOperate","getRenderedViewForm",request);
	reqXml.getRootElement().getChild("Request").getChild("Data").addContent((new Element("query_processInstanceId")).setText(processInstanceId));
	Document xmlDoc = SwitchCenter.doPost(reqXml);
	
	String processViewFormVO ="";
	String processViewFormKey ="";
	String processBusinessKey ="";
	String processDefKey ="";
	if (xmlDoc!=null){
		List recordList = xmlDoc.getRootElement().getChild("Response").getChild("Data").getChildren("Record");
		if(recordList.size()>0){
			Element record = (Element)recordList.get(0);
			//System.out.println("------"+record.getChildTextTrim("taskId"));
		  //  taskName = record.getChildTextTrim("taskName");
		    processViewFormKey = record.getChildTextTrim("processViewFormKey");
		    processBusinessKey = record.getChildTextTrim("processBusinessKey");
		  //  processInstanceId = record.getChildTextTrim("processInstanceId");
		    processDefKey = record.getChildTextTrim("processDefKey");
		    
		    request.setAttribute("processBusinessKey",processBusinessKey);
		    request.setAttribute("processDefKey",processDefKey);
		    request.setAttribute("processInstanceId",processInstanceId);
		    processViewFormVO = record.getChildTextTrim("processViewFormVO");
		}
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>查看流程详情</title>
<link rel="shortcut icon" href="favicon.ico" type="image/x-icon" />

<link href="/workflow/css/public.css" rel="stylesheet" type="text/css" />
<link href="/workflow/css/layout.css" rel="stylesheet" type="text/css" />
<link href="/workflow/js/dialog/css/custom-theme/jquery-ui-1.9.0.custom.css" rel="stylesheet" />
<link href="/workflow/js/picture-preview/css/picture_preview.css" rel="stylesheet" />

<script type="text/javascript" src="/js/jquery/jquery-1.10.2.min.js"></script>
<script type="text/javascript" src="/workflow/workflow.js"></script>
<script type="text/javascript" src="/workflow/common/jquery-ui-1.9.2.min.js"></script>
<script type="text/javascript" src="/workflow/js/dialog/js/dialog1.0.js"></script>
<script type="text/javascript" src="/workflow/js/picture-preview/js/picture_preview.js"></script>
<script src="/js/jquery/jsrender.js" type="text/javascript"></script>
<script type="text/javascript" src="/js/jquery/common.js"></script>

<script id="processLogRec" type="text/x-jsrender">	
<table style="width:744px;" border="0" cellspacing="0" cellpadding="0" class="table2">
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
var processInstanceId = "<%=processInstanceId%>";
$(document).ready(function(){
	Brightcom.workflow.buildProcessViewForm(<%=processViewFormVO%>);
})
</script> 
</head>


<body>
<div class="wrapper">
<form id="processViewForm" name="processViewForm">
<input type="hidden" id="processDefKey" name="processDefKey" value="">
<input type="hidden" id="processDefName" name="processDefName" value="">
<input type="hidden" id="processDefId" name="processDefId" value="">
<input type="hidden" id="processInstanceId" name="processInstanceId" value="">
<input type="hidden" id="processBusinessKey" name="processBusinessKey" value="">


<div class="main">
<div id="processViewTitleDiv" class="h3tit" style="height:25px;"></div>
    <div class="box2" >
         <div class="box2_content">

<div class="tab1">
<div id="processViewHandlerDiv">
	<table style="width: 724px;border-width: 0px;margin:0 auto; ">
      <tr style="background-color: white">
           <td width="300px" style="width:300px;height:23px;vertical-align:top;word-break: normal;word-wrap: break-word;">
                           处理人:<span id="processHandlerSpan"></span>
           </td>
           <td style="text-align: right;vertical-align:top;width:44%;">
                            流程创建人:<span id="processCreatorSpan"></span>
           </td>
         </tr>
         <tr style="background-color: white">
         	<td>
         	       流程图片跟踪查看:---<a class="color3" id="lnkShowImage" href="#" img=''>showImage</a>
			</td>
			<td style="text-align: right;width:44%;">
				流程创建时间 :<span id="processCreateTimeSpan"></span>
			</td>
         </tr>
  </table>
</div>

<div id="processViewFormDiv">
<%
out.flush();
request.getRequestDispatcher(processViewFormKey).include(request,response);
%>
</div>


<div id="processViewButtonDiv" style="height:20px;text-align:center;"></div>

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
<div id="processLogDiv" style="display:none">
<!--  
<div id="processLogDiv" style="height:30px;text-align:left;margin-top:15px">
<input type='button' id="" value="提交" onclick="Brightcom.workflow.showProcessLog()" />
<span style="float:left" onclick="Brightcom.workflow.showProcessLog()">></span>-->

</form>
</div>

</body>
</html>