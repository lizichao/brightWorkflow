<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="cn.brightcom.jraf.web.*"%>
<%@ page import="org.jdom.*"%>
<%@ page import="java.util.List"%>
<%@ taglib uri="/WEB-INF/brightcom.tld" prefix="bc" %>
<%@ include file="/masterreview/public/sessionoff.jsp"%>
<%
	String userid = (String)session.getAttribute("userid");
	String username =(String)session.getAttribute("username");
	String usertype =(String)session.getAttribute("usertype");
	// String usertype =(String)session.getAttribute("usertype");
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";

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
			    System.out.println("processStartFormKey========="+processStartFormKey);
			    processStartFormVO = record.getChildTextTrim("processStartFormVO");
			}
		}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>发起流程</title>
<!--  
<link href="/workflow/css/public.css" rel="stylesheet" type="text/css" />
<link href="/workflow/css/layout.css" rel="stylesheet" type="text/css" />
<link href="/workflow/js/dialog/css/custom-theme/jquery-ui-1.9.0.custom.css" rel="stylesheet" />
 <link rel="stylesheet" href="/masterreview/Css/HuanYu.css">
 -->
 <link rel="stylesheet" type="text/css" href="/masterreview/Css/zoonet.css" />
<link rel="stylesheet" type="text/css" href="/masterreview/Css/selectordie.css" />
<link rel="stylesheet" type="text/css" href="/masterreview/Css/jquery.datetimepicker.css" />
<link rel="stylesheet" type="text/css" href="/masterreview/Css/layerModel.css" />

  <link href="/js/jquery/plugin/fancybox/jquery.fancybox.css" rel="stylesheet" type="text/css" media="screen"/>
 
   <link href="/js/jquery/plugin/toastr/toastr.min.css" rel="stylesheet" type="text/css"/>
  <link href="/js/jquery/plugin/zebra_dialog/zebra_dialog.css" rel="stylesheet" type="text/css" />
 
  <script type="text/javascript" src="/js/jquery/jquery-1.7.1.min.js"></script>
 <script type="text/javascript" src="/masterreview/js/selectordie.min.js"></script>
<script type="text/javascript" src="/masterreview/js/jquery.datetimepicker.js"></script>
<script type="text/jscript" src="/masterreview/js/jquery.layerModel.js"></script>

 
  <script type="text/javascript" src="/workflow/workflow.js"></script>
  <script type="text/javascript" src="/js/jquery/common.js"></script>
     <script src="/js/jquery/plugin/toastr/toastr.min.js" type="text/javascript"></script>
  <script src="/js/jquery/plugin/fancybox/jquery.mousewheel.pack.js" type="text/javascript"></script>
  <script src="/js/jquery/plugin/fancybox/jquery.fancybox.js" type="text/javascript" ></script>
  <script src="/js/jquery/plugin/zebra_dialog/zebra_dialog.js" type="text/javascript"></script>
<script src="/js/jquery/plugin/zebra_dialog/zebra_dialog.src.js" type="text/javascript"></script>
  
  <!--  
<script type="text/javascript" src="/js/jquery/jquery-1.10.2.min.js"></script>
<script type="text/javascript" src="/workflow/common/jquery-ui-1.9.2.min.js"></script>
<script type="text/javascript" src="/workflow/js/dialog/js/dialog1.0.js"></script>
<script type="text/javascript" src="/workflow/workflow.js"></script>
<script type="text/javascript" src="/js/jquery/common.js"></script>
-->

<script type="text/javascript">
var processkey = "<%=processkey%>";
$(document).ready(function(){
  Brightcom.workflow.buildProcessStartForm(<%=processStartFormVO%>);
})


function updatepwd() {
	$.fancybox.open({href:"/masterreview/public/updatepwd.jsp",type:'iframe',width:327,height:298,padding:0,margin:0,closeBtn:false,iframe:{scrolling:'no',preload:false}});
}
</script> 
</head>

<body >
<!-- Header Start -->
<div class="header">
	<div class="auto">
		<h1>深圳市校长职级评审系统</h1>
		<p>当前用户：<%=username%> 
			<span> <a href="javascript:void();" onclick="updatepwd()">修改密码</a></span>
			<span> <a href="/masterreview/public/logoff.jsp">退出</a></span>
		</p>
	</div>
</div>
<!-- Header End -->
<!-- 
	<div class="top bbm">
		<div class="container">
			<div class="logo"><a href=""><img src="/masterreview/images/logo.jpg" alt=""></a></div>
			<div class="dang">当前用户：<%=username%> 
			  <a href="javascript:void();" onclick="updatepwd()">修改密码</a>
			  <a href="/masterreview/public/logoff.jsp">退出</a>
			</div>
		</div>
	</div> -->
	
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
<div>

<div class="h3tit" id="processStartTitleDiv" style="display: none;"></div>
<!--  
 <div class="box2" >
       <div class="box2_content">-->
         
<div id="processStartFormDiv">
<%
out.flush();
request.getRequestDispatcher(processStartFormKey).include(request,response);
%>

<!-- Footer Start -->
<div class="footer">
	<p>深圳市教育局版权所有&nbsp;|&nbsp;亮信科技技术支持：0755-33018116 （工作日9：00-12：00 14：00-18：00）</p>
	<p>Copyright&nbsp;&copy;&nbsp;2016.&nbsp;&nbsp;All Rights Reserved.</p>
</div>
<!-- Footer End -->
<!-- 
<div class="footer">
	<div class="container">
		深州市教育局版权所有 | 亮信科技技术支持：0755-33018116（工作日9:00-12:00  14:00-18:00） <br>
		Copyright &copy; 2016 . All Rights Reserved.
	</div>
</div>
 -->

<div class="th_right" id="processTextComponentDiv" style="margin-top:0px;margin-left:12px;" ></div>

</div>
  
<div class="t_btn" id="processStartButtonDiv" style="width:740px;text-align:center;margin-top:3px;display: none;">
<!-- <input type="button" id="btn_start" name="btn_start" value="发起流程" class="submit" onclick="Brightcom.workflow.submitWorkflowRequest(Brightcom.workflow.processStartType);"/> -->
</div>

<!--  
</div>
  <div class="box2_bottom"></div>
</div>-->

</div>
</form>
</div>
</body>
</html>
<%
		}
	}
%>