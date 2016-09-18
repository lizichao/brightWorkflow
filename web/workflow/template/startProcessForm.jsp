<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="cn.brightcom.jraf.web.*"%>
<%@ page import="org.jdom.*"%>
<%@ page import="java.util.List"%>
<%@ taglib uri="/WEB-INF/brightcom.tld" prefix="bc" %>
<%
	String processkey =(String)request.getParameter("processkey");
	Document reqXml = HttpProcesser.createRequestPackage("workflow","formServiceOperate","getRenderedStartForm",request);
	reqXml.getRootElement().getChild("Request").getChild("Data").addContent((new Element("query_processKey")).setText(processkey));
	Document xmlDoc = SwitchCenter.doPost(reqXml);
	
	String processStartFormVO ="";
	String processStartFormKey ="";
	if (xmlDoc!=null && xmlDoc.getRootElement()!=null &&  xmlDoc.getRootElement().getChild("Response")!=null){
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
				//System.out.println("------"+record.getChildTextTrim("taskId"));
			  //  taskName = record.getChildTextTrim("taskName");
			   // taskFormKey = record.getChildTextTrim("taskFormKey");
			  // System.out.println("------taskFormKey"+record.getChildTextTrim("taskFormKey"));
			  //  processBusinessKey = record.getChildTextTrim("processBusinessKey");
			   // request.setAttribute("processBusinessKey",processBusinessKey);
			    processStartFormKey =  record.getChildTextTrim("processStartFormKey");
			    processStartFormVO = record.getChildTextTrim("processStartFormVO");
			}
		}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>发起流程</title>
<link rel="shortcut icon" href="favicon.ico" type="image/x-icon" />
<link href="/workflow/css/public.css" rel="stylesheet" type="text/css" />
<link href="/workflow/css/layout.css" rel="stylesheet" type="text/css" />
<link href="/workflow/js/dialog/css/custom-theme/jquery-ui-1.9.0.custom.css" rel="stylesheet" />

<script type="text/javascript" src="/js/jquery/jquery-1.10.2.min.js"></script>
<script type="text/javascript" src="/workflow/common/jquery-ui-1.9.2.min.js"></script>
<script type="text/javascript" src="/workflow/js/dialog/js/dialog1.0.js"></script>
<script type="text/javascript" src="/workflow/workflow.js"></script>
<script type="text/javascript" src="/js/jquery/common.js"></script>

<script type="text/javascript">
var processkey = "<%=processkey%>";
$(document).ready(function(){
	if('<%=processStartFormVO%>'){
		Brightcom.workflow.buildProcessStartForm(<%=processStartFormVO%>);
	}
	
})
</script> 
</head>

<body>
<div class="wrapper">
<form  id="startProcessform" name="startProcessform" action="/P.tojson" method="post" enctype="multipart/form-data">
<!--  <input type="hidden" id="processTitle" name="processTitle" value="">-->
<input type="hidden" id="processDefKey" name="processDefKey" value="">
<input type="hidden" id="processDefName" name="processDefName" value="">
<input type="hidden" id="processDefId" name="processDefId" value="">
  <!--[if !IE]><!--><!--头部开始--><!--<![endif]-->
    <!--  
<div class="header">
</div>
-->
<div class="main">

<div class="h3tit" id="processStartTitleDiv" ></div>
 <div class="box2" >
       <div class="box2_content">
         
<div class="tab1">
<div id="processStartFormDiv">
<%
out.flush();
request.getRequestDispatcher(processStartFormKey).include(request,response);
%>



<div class="th_right" id="processTextComponentDiv" style="margin-top:0px;margin-left:12px;" ></div>
  <!--  <div class="box2_bottom"></div>-->
</div>

  
</div>  
  
<div class="t_btn" id="processStartButtonDiv" style="width:740px;text-align:center;margin-top:3px;">
<!-- <input type="button" id="btn_start" name="btn_start" value="发起流程" class="submit" onclick="Brightcom.workflow.submitWorkflowRequest(Brightcom.workflow.processStartType);"/> -->
</div>

</div>
  <div class="box2_bottom"></div>
</div>

</div>
</form>
</div>
</body>
</html>
<%
		}
	}
%>