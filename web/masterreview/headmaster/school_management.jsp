<%@ page contentType="text/html; charset=GBK" %>
<%
	String userid = (String)session.getAttribute("userid");
	String username =(String)session.getAttribute("username");
	String usertype =(String)session.getAttribute("usertype");
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%>	
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="keywords" content="" />
<meta name="description" content="" />
<title>深圳市校长职级评审系统</title>


<script type="text/javascript">
$(function(){
	$("#span1").customtip({
		'title':"制定科学可行的学校发展规划并落实，把握学校发展机遇，保持学校持续发展。"
	});
	$("#span2").customtip({
		'title':"学校班子科学分工，内设机构设置合理，职责明确，执行规范有效。"
	});
	$("#span3").customtip({
		'title':"推进现代学校制度建设，依法治校，校务公开；注重民主管理，教职工参与管理和监督机制健全，党政协调、群众组织机制良好。"
	});
	$("#span4").customtip({
		'title':"建立健全学校人事、财务、资产管理等制度，将信息化手段引入学校管理，做到公开、公平、公正、透明。"
	});
	$("#span5").customtip({
		'title':"建设平安校园。安全稳定工作机构健全，安全教育活动扎实有效，学校风险防范机制健全，无重大安全维稳责任事故发生。"
	});
});

function initSchoolManagementData(masterReviewVO){
	$("#school_management").val(masterReviewVO.school_management);
}

function saveUpdateRefillData(){
	var isRefillTask = $("#isRefillTask").val();
	if(isRefillTask){
		var submitStrings =  getSubmitStrings();
		var bcReq = new BcRequest('headmaster','masterReviewAction','saveUpdateRefillData');
		bcReq.setExtraPs({
		    "option_tab_type":'school_management',
		    "option_tab_values":submitStrings,
		    "businessKey":processBusinessKey
		});
		bcReq.setSuccFn(function(data,status){
			changeOption(12);
		});
		bcReq.postData();
	}else{
		submitRefillTask();
	}
}


function getSubmitStrings(){
	var submitArray = [];
	var school_management = $("#school_management").val();
	var businessKey = $("#id").val();
	
	var workExperienceObject = {
			"id":$("#id").val(),
			"businessKey":$("#id").val(),
			"school_management":school_management
	}
	submitArray.push(workExperienceObject);
	return JSON.stringify(submitArray);
}

function headmasterBeforeSubmit(formJsonData){
	formJsonData.option_tab_type = "school_management"
	formJsonData.option_tab_values = getSubmitStrings();
}
</script>
</head>
<body>
<!-- Main Start -->
	<!-- 进度 s -->
	<div class="grogress"><div class="line" style="width:92px;"><!-- 970/21 --></div></div>
	<!-- 进度 e -->
	<!-- 标题 s -->
	<div class="com-title">
		<div class="txt fl" >
			<h2><i>11</i>学校管理</h2>
			<p >
				<span id="span1" >1、规划发展  </span>
				<span id="span2" >2、机构设置  </span>
				<span id="span3" >3、民主管理  </span>
				<span id="span4" >4、制度建设  </span>
				<span id="span5" >5、安全管理  </span>
			<p>
		</div>
		<div class="select-step fr"><a href="javascript:void(0);" target="_self" title="" id="change">+&nbsp;切换步骤</a></div>
		<div class="clear-both"></div>
	</div>
	<!-- 标题 e -->
	<!-- 办学思想 s -->
	<div class="bxsx">
		<ul class="clear-fix">
			<li>
				<textarea id="school_management" name="school_management"></textarea>
				<p style="color:#999;text-align:right;">0/1000</p>
			</li>
			<li>
				<span class="fl">证明材料：</span>
				<input type="button" value="点击上传" class="up-load fl" />
			</li>
		</ul>
	</div>
	<!-- 办学思想 e -->
	<!-- 任职年限 e -->
	<div class="next-step clear-fix">
	 <a href="javascript:void(0);" target="_self" title="" class="fl" onclick="changeOption(10)">上一步</a>
	 <a href="javascript:void(0);" target="_self" title="" class="fr" onclick="saveUpdateRefillData()">下一步</a>
	</div>
</body>
</html>