<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="cn.brightcom.jraf.web.*"%>
<%@ page import="org.jdom.*"%>
<%@ page import="java.util.List"%>
<%@ taglib uri="/WEB-INF/brightcom.tld" prefix="bc" %>
<%
	String userid = (String)session.getAttribute("userid");
	String username =(String)session.getAttribute("username");
	String usertype =(String)session.getAttribute("usertype");
	// String usertype =(String)session.getAttribute("usertype");
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
	
%>	
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>新闻列表</title>

<link rel="shortcut icon" href="favicon.ico" type="image/x-icon" />
<link rel="stylesheet" href="/masterreview/Css/HuanYu.css">
  <link href="/js/jquery/plugin/fancybox/jquery.fancybox.css" rel="stylesheet" type="text/css" media="screen"/>
<link href="/js/jquery/plugin/zebra_dialog/zebra_dialog.css" rel="stylesheet" type="text/css" />

<!--<script type="text/javascript" src="/js/jquery/jquery-1.10.2.min.js"></script>-->
<script type="text/javascript" src="/js/jquery/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="/workflow/workflow.js"></script>
<script type="text/javascript" src="/workflow/common/jquery-ui-1.9.2.min.js"></script>
<script type="text/javascript" src="/workflow/js/dialog/js/dialog1.0.js"></script>
<script type="text/javascript" src="/workflow/js/picture-preview/js/picture_preview.js"></script>
<script src="/js/jquery/jsrender.js" type="text/javascript"></script>
<script type="text/javascript" src="/js/jquery/common.js"></script>
  <script src="/js/jquery/plugin/fancybox/jquery.mousewheel.pack.js" type="text/javascript"></script>
  <script src="/js/jquery/plugin/fancybox/jquery.fancybox.js" type="text/javascript" ></script>
<script src="/js/jquery/plugin/zebra_dialog/zebra_dialog.js" type="text/javascript"></script>
<script src="/js/jquery/plugin/zebra_dialog/zebra_dialog.src.js" type="text/javascript"></script>


<script id="newsListRec" type="text/x-jsrender">	
<ul class="clear-fix">
{{for Data}}
     <li><a class="cha" href="/masterreview/news/newsdisplay.jsp?newsId={{:id}}" >{{:news_title}}</a>
   {{if #index <4}}
      <img src="/masterreview/images/new.gif" alt="">
   {{/if}}
   </li>
{{/for}}
</ul>
</script>



<script type="text/javascript">
$(document).ready(function(){
	setMethod("loadGridData","22");
	loadGridData("22",1);
})

function loadGridData(gridNum,pageNo){
	var bcReq = new BcRequest('headmaster','newsAction','findNews');
	bcReq.setExtraPs({"PageSize":"10","PageNo":pageNo});
	bcReq.setSuccFn(function(data,status){
		 $("#newListDiv").empty();
		 if(data.Data.length>0){
			 var _stuContent=$("#newsListRec").render(data);
			 $("#newListDiv").append(_stuContent);
			 $("#ext-comp-1012").show();
	         setPageInfo(data);
			
		 }else{
		   $("#newListDiv").append("<span style='width:100%;color:red;'>暂无数据</span>");
		   $("#ext-comp-1012").hide();
		 }
	});
	bcReq.postData();
}


function viewNews(id){
	$.fancybox.close();
	$.fancybox.open({href:"/masterreview/news/newsdisplay.jsp?newsId="+id,type:'iframe',width:827,height:568,padding:0,margin:0,closeBtn:false,iframe:{scrolling:'no',preload:false}});
}


function updatepwd() {
	$.fancybox.open({href:"/masterreview/public/updatepwd.jsp",type:'iframe',width:327,height:298,padding:0,margin:0,closeBtn:false,iframe:{scrolling:'no',preload:false}});
}
</script> 
</head>

<body class="grey">
	<div class="top bbm">
		<div class="container">
		
			<div class="logo"><a href=""><img src="/masterreview/images/logo.jpg" alt=""></a></div>
			<div class="dang">当前用户：guest 
			<!--   <a href="javascript:void();" onclick="updatepwd()">修改密码</a>
			  <a href="/masterreview/public/logoff.jsp">退出</a> -->
			</div>
		</div>
	</div>
	
	<div class="container">
		<div class="xiao_bg">
			<div class="new-title new-center">通知公告</div>
			
			<div id="newListDiv" class="new-list">
	
			</div>
			
	    <%@ include file="/workflow/common/pageinfo.jsp"%>		
	</div>
	</div>

	<div class="footer">
		<div class="container">
			深州市教育局版权所有 | 亮信科技技术支持：0755-33018116（工作日9:00-12:00  14:00-18:00） <br>
			Copyright &copy; 2016 . All Rights Reserved.
		</div>
	</div>

</body>
</html>
