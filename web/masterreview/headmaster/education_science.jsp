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
		'title':"有效统筹国家、地方、学校三级课程，确保国家课程、地方课程的落实，创造性开设校本课程，满足学生多元需求。"
	});
	$("#span2").customtip({
		'title':"育人为本，开齐开足德育、体育、艺术、综合实践活动等课程，实施素质教育；确保学生每天一小时校园体育活动。"
	});
	$("#span3").customtip({
		'title':"课堂教学理念先进，体现新课改理念；实施有效课堂；教学管理规范有序；定期深入课堂听课。"
	});
	$("#span4").customtip({
		'title':"建立健全教学质量监控、分析和改进机制，有序监控教学过程，检测教学质量，及时改进；教学质量显著提升。"
	});
	$("#span5").customtip({
		'title':"教科研工作推进有力，课题研究成果丰硕，教育教学质量显著提高。"
	});
});

function initEducationScienceData(masterReviewVO){
	$("#education_science").val(masterReviewVO.education_science);
	countChar($("#education_science"));
	var manageDifficultySelId = masterReviewVO.education_science_attachMentVO.attachmentId;
	var manageDifficultyFileName = masterReviewVO.education_science_attachMentVO.fileName;
	if(manageDifficultySelId){
		var attachmentArray =[];
		attachmentArray.push("<a class=\"chachu\"  href=\"<%=basePath%>WorkflowAttachMentDownload?attachmentId="+manageDifficultySelId+"\">"+manageDifficultyFileName+"</a>");
		attachmentArray.push("&nbsp;&nbsp;");
		attachmentArray.push("<a class=\"chachu\"  href=\"#\" onclick=\"deleteReceiveFileAttachment(\'"+manageDifficultySelId+"\',this);\" >删除</a>");
		
		$("#education_science_attach_div1").append(attachmentArray.join(""));
	}
	
	 $("#education_science_attachId1").val(manageDifficultySelId);
	 
	 Headmaster.initWebUploader('education_science_attach_span',1,'education_science','点击上传','education_science_attachId','education_science_attach_div');
}

function saveUpdateRefillData(){
	var isRefillTask = $("#isRefillTask").val();
	if(isRefillTask){
		var submitStrings =  getSubmitStrings();
		var bcReq = new BcRequest('headmaster','masterReviewAction','saveUpdateRefillData');
		bcReq.setExtraPs({
		    "option_tab_type":'education_science',
		    "option_tab_values":submitStrings,
		    "businessKey":processBusinessKey
		});
		bcReq.setSuccFn(function(data,status){
			changeOption(14);
		});
		bcReq.postData();
	}else{
		submitRefillTask();
	}
}


function getSubmitStrings(){
	var submitArray = [];
	var education_science = $("#education_science").val();
	var businessKey = $("#id").val();
	var education_science_attachId = $("#education_science_attachId1").val();
	
	var workExperienceObject = {
			"id":$("#id").val(),
			"businessKey":$("#id").val(),
			"education_science":education_science,
			"education_science_attachId":education_science_attachId
	}
	submitArray.push(workExperienceObject);
	return JSON.stringify(submitArray);
}

function headmasterBeforeSubmit(formJsonData){
	formJsonData.option_tab_type = "education_science"
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
			<h2><i>13</i>教育教学</h2>
			<p class="hide">
				 <span id="span1" >1、课程建设  </span>
				 <span id="span2" >2、德育、体育、艺术、综合实践活动等课程开设  </span>
				 <span id="span3" > 3、课堂教学 </span>
				 <span id="span4" >4、质量监控  </span>
				 <span id="span5" > 5、教学研究</span>
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
				<textarea id="education_science" name="education_science" onkeydown='countChar(this);' onkeyup='countChar(this);'></textarea>
				<p name="text-prompt" style="color:#999;text-align:right;"><span name="number" style="padding: 0px 0px;">0</span>/1000</p>
			</li>
			
			<input type="hidden" id="education_science_attachId1" name="education_science_attachId" value="">
			<li  style='height:45px;' class='position_relative'>
			   <span class='fl'>证明材料：</span>
			   <div id='education_science_attach_span1' class='position_upload_button_professional'></div>
			</li>
			<div id="education_science_attach_div1" class="only_attachments"></div>
		</ul>
	</div>
	<!-- 办学思想 e -->
	<!-- 任职年限 e -->
	<div class="next-step clear-fix">
	 <a href="javascript:void(0);" target="_self" title="" class="fl" onclick="changeOption(12)">上一步</a>
	 <a href="javascript:void(0);" target="_self" title="" class="fr" onclick="saveUpdateRefillData()">下一步</a>
	</div>

</body>
</html>