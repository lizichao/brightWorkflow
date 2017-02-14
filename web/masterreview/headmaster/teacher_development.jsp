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
		'title':"扎实开展师德师风建设，落实教师职业道德规范要求和违反职业道德行为处理办法。严禁教师从事有偿家教；教师无违法违纪犯罪等现象。"
	});
	$("#span2").customtip({
		'title':"建立健全教师专业发展制度，指导开展教师专业培训，创造教师专业发展机会，落实每位教师五年一周期不少于360学时的培训要求。"
	});
	$("#span3").customtip({
		'title':"加强青年教师培养，培养学科骨干和名教师，形成老中青梯度发展机制，教师队伍整体专业水平进步显著。"
	});
	$("#span4").customtip({
		'title':"学校教师绩效考核制度完善，学校评优评先、绩效工资、职称评聘、年度考核等制度办法科学合理，调动和激发教师工作积极性，教师满意度高。"
	});
});

function initTeacherDevelopmentData(masterReviewVO){
	$("#teacher_development").val(masterReviewVO.teacher_development);
	countChar($("#teacher_development"));
	var manageDifficultySelId = masterReviewVO.teacher_development_attachMentVO.attachmentId;
	var manageDifficultyFileName = masterReviewVO.teacher_development_attachMentVO.fileName;
	if(manageDifficultySelId){
		var attachmentArray =[];
		attachmentArray.push("<a class=\"chachu\"  href=\"<%=basePath%>WorkflowAttachMentDownload?attachmentId="+manageDifficultySelId+"\">"+manageDifficultyFileName+"</a>");
		attachmentArray.push("&nbsp;&nbsp;");
		attachmentArray.push("<a class=\"chachu\"  href=\"javascript:void(0);\" onclick=\"Headmaster.deleteReceiveFileAttachment(\'"+manageDifficultySelId+"\',this);\" >删除</a>");
		
		$("#teacher_development_attach_div1").append(attachmentArray.join(""));
	}
	
	 $("#teacher_development_attachId1").val(manageDifficultySelId);
	 
	 Headmaster.initWebUploader('teacher_development_attach_span',1,'teacher_development','点击上传','teacher_development_attachId','teacher_development_attach_div');
}

function saveUpdateRefillData(){
	var isRefillTask = $("#isRefillTask").val();
	if(isRefillTask){
		var submitStrings =  getSubmitStrings();
		var bcReq = new BcRequest('headmaster','masterReviewAction','saveUpdateRefillData');
		bcReq.setExtraPs({
		    "option_tab_type":'teacher_development',
		    "option_tab_values":submitStrings,
		    "businessKey":processBusinessKey
		});
		bcReq.setSuccFn(function(data,status){
			changeOption(17);
		});
		bcReq.postData();
	}else{
		submitRefillTask();
	}
}


function getSubmitStrings(){
	var submitArray = [];
	var teacher_development = $("#teacher_development").val();
	var businessKey = $("#id").val();
	var teacher_development_attachId = $("#teacher_development_attachId1").val();
	
	var workExperienceObject = {
			"id":$("#id").val(),
			"businessKey":$("#id").val(),
			"teacher_development":teacher_development,
			"teacher_development_attachId":teacher_development_attachId
	}
	submitArray.push(workExperienceObject);
	return JSON.stringify(submitArray);
}

function headmasterBeforeSubmit(formJsonData){
	formJsonData.option_tab_type = "teacher_development"
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
		<div class="txt fl" id="showorhide">
			<h2><i>16</i>教师发展</h2>
			<p class="hide">
			  <span id="span1" >1、师德建设    </span>
			  <span id="span2" >2、专业发展    </span>
			  <span id="span3" >3、梯队培养   </span>
			  <span id="span4" >4、激励评价  </span>
			</p>
		</div>
		<div class="select-step fr"><a href="javascript:void(0);" target="_self" title="" id="change">+&nbsp;切换步骤</a></div>
		<div class="clear-both"></div>
	</div>
	<!-- 标题 e -->
	<!-- 办学思想 s -->
	<div class="bxsx">
		<ul class="clear-fix">
			<li>
				<textarea id="teacher_development" name="teacher_development" onkeydown='countChar(this);' onkeyup='countChar(this);'></textarea>
				<p name="text-prompt" style="color:#999;text-align:right;" ><span name="number" style="padding: 0px 0px;">0</span>/1000</p>
			</li>
			
			
	       <input type="hidden" id="teacher_development_attachId1" name="teacher_development_attachId1" value="">
			<li  style='height:45px;' class='position_relative'>
			   <span class='fl'>证明材料：</span>
			   <div id='teacher_development_attach_span1' class='position_upload_button_professional'></div>
			</li>
			<div id="teacher_development_attach_div1" class="only_attachments"></div>
		</ul>
	</div>
	<!-- 办学思想 e -->
	<!-- 任职年限 e -->
	<div class="next-step clear-fix">
	 <a href="javascript:void(0);" target="_self" title="" class="fl" onclick="changeOption(15)">上一步</a>
	 <a href="javascript:void(0);" target="_self" title="" class="fr" onclick="saveUpdateRefillData()">下一步</a>
	</div>

</body>
</html>