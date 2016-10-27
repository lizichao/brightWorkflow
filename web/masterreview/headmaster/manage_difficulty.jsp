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
<link rel="stylesheet" type="text/css" href="/masterreview/Css/selectordie.css" />
<script type="text/javascript" src="/masterreview/js/selectordie.min.js"></script>



<script type="text/javascript">
$(function(){
	//$('select').selectOrDie({
	//	placeholder: '请选择学校类型'
	//});
});

function initManageDifficulty(masterReviewVO){
	bulidManageDifficulty(masterReviewVO);
}

function bulidManageDifficulty(masterReviewVO){
	var manageDifficultySelId = masterReviewVO.manageDifficultyAttachMentVO.attachmentId;
	var manageDifficultyFileName = masterReviewVO.manageDifficultyAttachMentVO.fileName;
	if(manageDifficultySelId){
		var attachmentArray =[];
		attachmentArray.push("<a class=\"chachu\"  href=\"<%=basePath%>WorkflowAttachMentDownload?attachmentId="+manageDifficultySelId+"\">"+manageDifficultyFileName+"</a>");
		attachmentArray.push("&nbsp;&nbsp;");
		attachmentArray.push("<a class=\"chachu\"  href=\"#\" onclick=\"deleteReceiveFileAttachment(\'"+manageDifficultySelId+"\',this);\" >删除</a>");
		
		$("#manageDifficultyDiv2").append(attachmentArray.join(""));
	}
	
	var manageDifficultyAgoSelId = masterReviewVO.manageDifficultyAgoAttachMentVO.attachmentId;
	var manageDifficultyAgoFileName = masterReviewVO.manageDifficultyAgoAttachMentVO.fileName;
	if(manageDifficultyAgoSelId){
		var attachmentArray =[];
		attachmentArray.push("<a class=\"chachu\"  href=\"<%=basePath%>WorkflowAttachMentDownload?attachmentId="+manageDifficultyAgoSelId+"\">"+manageDifficultyAgoFileName+"</a>");
		attachmentArray.push("&nbsp;&nbsp;");
		attachmentArray.push("<a class=\"chachu\"  href=\"#\" onclick=\"deleteReceiveFileAttachment(\'"+manageDifficultyAgoSelId+"\',this);\" >删除</a>");
		
		$("#manageDifficultyAgoDiv1").append(attachmentArray.join(""));
	}
	//debugger;
	Brightcom.workflow.initSelectCombox('headmaster_school_type','schoolTypeAgo');
	Brightcom.workflow.initSelectCombox('headmaster_school_type','schoolType');
	
	$("#schoolNameSpace").val(masterReviewVO.schoolNameSpace);
	$("#schoolType").val(masterReviewVO.schoolType);
    $("#schoolCount").val(masterReviewVO.schoolCount);
	$("#studentNumber").val(masterReviewVO.studentNumber);
	 $("#manageDifficultyAttachId2").val(manageDifficultySelId);
	
    $("#schoolNameSpaceAgo").val(masterReviewVO.schoolNameSpaceAgo);
    $("#schoolTypeAgo").val(masterReviewVO.schoolTypeAgo);
	$("#schoolCountAgo").val(masterReviewVO.schoolCountAgo);
	$("#studentNumberAgo").val(masterReviewVO.studentNumberAgo);
	$("#manageDifficultyAgoAttachId1").val(manageDifficultyAgoSelId);
	

	/*
	 * 1、professionalTitlespan:上传按钮显示位置id
	 4、buttonName ： 按钮名称
	 5、hiddenAttachId：隐藏附件id，选中后把附件值保存在该隐藏域里
	 6、 hiddenDisplayId:附件显示的div id
	 */
	 Headmaster.initWebUploader('manageDifficultyAgo',1,'manageDifficulty','点击上传','manageDifficultyAgoAttachId','manageDifficultyAgoDiv');
	 Headmaster.initWebUploader('manageDifficulty',2,'manageDifficulty','点击上传','manageDifficultyAttachId','manageDifficultyDiv');
	 
	 $('select').selectOrDie();
}

function saveUpdateRefillData(){
	var isRefillTask = $("#isRefillTask").val();
	if(isRefillTask){
		var submitStrings =  getSubmitStrings();
		var bcReq = new BcRequest('headmaster','masterReviewAction','saveUpdateRefillData');
		bcReq.setExtraPs({   
			"option_tab_type":'managementDifficulty',
		    "option_tab_values":submitStrings,
		    "businessKey":processBusinessKey
		  });
		bcReq.setSuccFn(function(data,status){
			changeOption(11);
		});
		bcReq.postData();
	}else{
		submitRefillTask();
	}
}

function getSubmitStrings(){
	var submitArray = [];
	
	
	var baseinfoObject = {
			'schoolNameSpace' : $("#schoolNameSpace").val(),
			'schoolType' :  $("#schoolType").val(),
			'schoolCount': $("#schoolCount").val(),
			'studentNumber':$("#studentNumber").val(),
			'manage_difficulty_attachment_id':$("#manageDifficultyAttachId2").val(),
			
			'schoolNameSpaceAgo':$("#schoolNameSpaceAgo").val(),
			'schoolTypeAgo':$("#schoolTypeAgo").val(),
			'schoolCountAgo':$("#schoolCountAgo").val(),
			'studentNumberAgo':$("#studentNumberAgo").val(),
			'manage_difficulty_ago_attachment_id':$("#manageDifficultyAgoAttachId1").val(),
			
			"businessKey":$("#id").val(),
			"id":$("#id").val()
	}
	submitArray.push(baseinfoObject);
	return JSON.stringify(submitArray);
}

function headmasterBeforeSubmit(formJsonData){
	formJsonData.option_tab_type = "managementDifficulty"
	formJsonData.option_tab_values = getSubmitStrings();
}
</script>
</head>
<body>
<!-- Main Start -->
	<!-- 进度 s -->
	<div class="grogress"><div class="line" ><!-- 970/21 --></div></div>
	<!-- 进度 e -->
	<!-- 标题 s -->
	<div class="com-title">
		<div class="txt fl">
			<h2><i>10</i>管理难度</h2>
			<p>1、有交流任职经历的，填写现任职学校。2、任选一所近八年管理难度最大的学校情况填写。</p>
		</div>
		<div class="select-step fr"><a href="javascript:void(0);" target="_self" title="" id="change">+&nbsp;切换步骤</a></div>
		<div class="clear-both"></div>
	</div>
	<!-- 标题 e -->
	<!-- 管理难度 s -->
	<div class="manage clear-fix">
		<ul class="manage-ul clear-fix fl" style="margin-left:95px;">
			<li>
				<div class="border_1 w_22" style="z-index:99;">
					<span>学校类型：</span>
					<select id="schoolTypeAgo">
					  <option value="">请选择学校类型</option>
					</select>
					<p>(近8年任职学校)</p>
				</div>
			</li>
			<li>
				<div class="border_1 w_22" style="z-index:98;">
					<span>学校名称：</span>
				    <input type="text" id="schoolNameSpaceAgo" />
				</div>
			</li>
			<li>
				<div class="border_1 w_22" style="z-index:97;">
					<span>校区数量：</span>
					<input type="text" id="schoolCountAgo" />
				</div>
			</li>
			<li>
				<div class="border_1 w_22" style="z-index:96;">
					<span>班级数量：</span>
					<input type="text" id="studentNumberAgo" />
				</div>
			</li>
			
			<input type="hidden" id="manageDifficultyAgoAttachId1" name="manageDifficultyAgoAttachId1" value="">
			<li><span id="manageDifficultyAgo1"></span></li>
			<div id="manageDifficultyAgoDiv1" class="only_attachments"></div>
		</ul>
		
		<ul class="manage-ul clear-fix fr" style="margin-right:95px;">
			<li>
				<div class="border_1 w_22" style="z-index:99;">
					<span>学校类型：</span>
					<select id="schoolType">
					</select>
					<p>(现任职学校)</p>
				</div>
			</li>
			<li>
				<div class="border_1 w_22" style="z-index:98;">
					<span>学校名称：</span>
					<input type="text" id="schoolNameSpace" />
				</div>
			</li>
			<li>
				<div class="border_1 w_22" style="z-index:97;">
					<span>校区数量：</span>
					<input type="text" id="schoolCount" />
				</div>
			</li>
			<li>
				<div class="border_1 w_22" style="z-index:96;">
					<span>班级数量：</span>
					<input type="text" id="studentNumber"/>
				</div>
			</li>
			 <input type="hidden" id="manageDifficultyAttachId2" name="manageDifficultyAttachId2" value="">
			<li> <span id="manageDifficulty2"></span></li>
			 <div id="manageDifficultyDiv2" class="only_attachments"></div>
		</ul>
	</div>
	<!-- 任职年限 e -->
	<div class="next-step clear-fix">
	   <a href="javascript:void(0);" target="_self" title="" class="fl" onclick="changeOption(9)">上一步</a>
	   <a href="javascript:void(0);" target="_self" title="" class="fr" onclick="saveUpdateRefillData()">下一步</a>
</div>
</body>
</html>