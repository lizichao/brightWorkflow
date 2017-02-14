<%@ page contentType="text/html; charset=GBK" %>
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
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="keywords" content="" />
<meta name="description" content="" />
<title>深圳市校长职级评审系统</title>


<script type="text/javascript">
$(function(){
	$("#span1").customtip({
		'title':"教育理念先进，师生知晓度、认可度高；改革创新意识强；工作思路明确，办学措施得力。"
	});
	$("#span2").customtip({
		'title':"建设校园文化，凝聚学校精神，营造浓厚的育人文化。"
	});
});

function initRunSchoolData(masterReviewVO){
	$("#run_school").val(masterReviewVO.run_school);
	countChar($("#run_school"));//计算字数
	var manageDifficultySelId = masterReviewVO.run_school_attachMentVO.attachmentId;
	var manageDifficultyFileName = masterReviewVO.run_school_attachMentVO.fileName;
	if(manageDifficultySelId){
		var attachmentArray =[];
		attachmentArray.push("<a class=\"chachu\"  href=\"<%=basePath%>WorkflowAttachMentDownload?attachmentId="+manageDifficultySelId+"\">"+manageDifficultyFileName+"</a>");
		attachmentArray.push("&nbsp;&nbsp;");
		attachmentArray.push("<a class=\"chachu\"  href=\"javascript:void(0);\" onclick=\"Headmaster.deleteReceiveFileAttachment(\'"+manageDifficultySelId+"\',this);\" >删除</a>");
		
		$("#run_school_attach_div1").append(attachmentArray.join(""));
	}
	
	 $("#run_school_attachId1").val(manageDifficultySelId);
	 
	 Headmaster.initWebUploader('run_school_attach_span',1,'run_school','点击上传','run_school_attachId','run_school_attach_div');
}


function saveUpdateRefillData(){
	var isRefillTask = $("#isRefillTask").val();
	if(isRefillTask){
		var submitStrings =  getSubmitStrings();
		var bcReq = new BcRequest('headmaster','masterReviewAction','saveUpdateRefillData');
		bcReq.setExtraPs({
		    "option_tab_type":'run_school',
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
	var run_school = $("#run_school").val();
	var businessKey = $("#id").val();
	var run_school_attachId = $("#run_school_attachId1").val();
	
	var workExperienceObject = {
			"id":$("#id").val(),
			"businessKey":$("#id").val(),
			"run_school":run_school,
			"run_school_attachId":run_school_attachId
	}
	submitArray.push(workExperienceObject);
	return JSON.stringify(submitArray);
}

function headmasterBeforeSubmit(formJsonData){
	formJsonData.option_tab_type = "run_school"
	formJsonData.option_tab_values = getSubmitStrings();
}

function countChar(curObj) {//计算字数
	var maxLength = 1000;//1000个字符
	var $curObj;
	if (curObj instanceof jQuery) {
		$curObj = curObj;
	} else {
		$curObj = $(curObj);
	}
	$curObj = $(curObj);
	var $textPrompt = $curObj.siblings('p[name="text-prompt"]');
	if ($textPrompt[0]) {//判断是否存在
		var $numberSpan = $textPrompt.find("span[name='number']");
		if ($numberSpan[0]) {//判断是否存在
			var value = $curObj.val();
			var length = value.length;
			if (length>maxLength) {
				length=maxLength;
				value = value.substring(0,maxLength);
				$curObj.val(value);
			}
			$numberSpan.html(length);
		}
	}
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
		<div class="txt fl">
			<h2><i>11</i>办学思想</h2>
			<p ><span id="span1" >1、办学理念</span> <span id="span2" > 2、校园文化</span></p>
			<!-- <a href="javascript:void(0);" target="_self" title="">展开</a> -->
		</div>
		<div class="select-step fr"><a href="javascript:void(0);" target="_self" title="" id="change">+&nbsp;切换步骤</a></div>
		<div class="clear-both"></div>
	</div>
	<!-- 标题 e -->
	<!-- 办学思想 s -->
	<div class="bxsx">
		<ul class="clear-fix">
			<li>
				<textarea id="run_school" name="run_school" onkeydown='countChar(this);' onkeyup='countChar(this);'></textarea>
				<p name="text-prompt" style="color:#999;text-align:right;"><span name="number" style="padding: 0px 0px;">0</span>/1000</p>
			</li>
			
			
	
				
			<input type="hidden" id="run_school_attachId1" name="run_school_attachId" value="">
			<li  style='height:45px;' class='position_relative'>
			   <span class='fl'>证明材料：</span>
			   <div id='run_school_attach_span1' class='position_upload_button_professional'></div>
			</li>
			<div id="run_school_attach_div1" class="only_attachments"></div>
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