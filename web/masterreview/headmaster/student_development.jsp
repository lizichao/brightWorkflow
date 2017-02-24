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
<title>������У��ְ������ϵͳ</title>


<script type="text/javascript">
$(function(){
	$("#span1").customtip({
		'title':"���ʽ�����ʵ��Ч��ѧ����Ʒ�¡����ġ�ѧϰ�����¡����ʡ���������Ϣ������Ȱ˴�������������õ�ȫ�淢չ��"
	});
	$("#span2").customtip({
		'title':"����ѧ���ۺ������������ۻ��ƣ����ڿ�չѧ���ۺ��������ۣ��õ�ѧ�����ҳ�������Ͽɡ�"
	});
});

function initStudentDevelopmentData(masterReviewVO){
	$("#student_development").val(masterReviewVO.student_development);
	countChar($("#student_development"));
	var manageDifficultySelId = masterReviewVO.student_development_attachMentVO.attachmentId;
	var manageDifficultyFileName = masterReviewVO.student_development_attachMentVO.fileName;
	if(manageDifficultySelId){
		var attachmentArray =[];
		attachmentArray.push("<a class=\"chachu\"  href=\"<%=basePath%>WorkflowAttachMentDownload?attachmentId="+manageDifficultySelId+"\">"+manageDifficultyFileName+"</a>");
		attachmentArray.push("&nbsp;&nbsp;");
		attachmentArray.push("<a class=\"chachu\"  href=\"javascript:void(0);\" onclick=\"Headmaster.deleteReceiveFileAttachment(\'"+manageDifficultySelId+"\',this);\" >ɾ��</a>");
		
		$("#student_development_attach_div1").append(attachmentArray.join(""));
	}
	
	 $("#student_development_attachId1").val(manageDifficultySelId);
	 
	 Headmaster.initWebUploader('student_development_attach_span',1,'student_development','����ϴ�','student_development_attachId','student_development_attach_div');
}

function saveUpdateRefillData(){
	var isRefillTask = $("#isRefillTask").val();
	if(isRefillTask){
		var submitStrings =  getSubmitStrings();
		var bcReq = new BcRequest('headmaster','masterReviewAction','saveUpdateRefillData');
		bcReq.setExtraPs({
		    "option_tab_type":'student_development',
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
	var student_development = $("#student_development").val();
	var businessKey = $("#id").val();
	var student_development_attachId = $("#student_development_attachId1").val();
	
	var workExperienceObject = {
			"id":$("#id").val(),
			"businessKey":$("#id").val(),
			"student_development":student_development,
			"student_development_attachId":student_development_attachId
	}
	submitArray.push(workExperienceObject);
	return JSON.stringify(submitArray);
}

function headmasterBeforeSubmit(formJsonData){
	formJsonData.option_tab_type = "student_development"
	formJsonData.option_tab_values = getSubmitStrings();
}

function countChar(curObj) {//��������
	var maxLength = 1000;//1000���ַ�
	var $curObj;
	if (curObj instanceof jQuery) {
		$curObj = curObj;
	} else {
		$curObj = $(curObj);
	}
	$curObj = $(curObj);
	var $textPrompt = $curObj.siblings('p[name="text-prompt"]');
	if ($textPrompt[0]) {//�ж��Ƿ����
		var $numberSpan = $textPrompt.find("span[name='number']");
		if ($numberSpan[0]) {//�ж��Ƿ����
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
	<!-- ���� s -->
	<div class="grogress"><div class="line" style="width:92px;"><!-- 970/21 --></div></div>
	<!-- ���� e -->
	<!-- ���� s -->
	<div class="com-title">
		<div class="txt fl" id="showorhide">
			<h2><i>16</i>ѧ����չ</h2>
			<p class="hide">
			   <span id="span1" >1��ȫ�淢չ   </span>
			   <span id="span2" >2������ </span>
			 </p>
		</div>
		<div class="select-step fr"><a href="javascript:void(0);" target="_self" title="" id="change">+&nbsp;�л�����</a></div>
		<div class="clear-both"></div>
	</div>
	<!-- ���� e -->
	<!-- ��ѧ˼�� s -->
	<div class="bxsx">
		<ul class="clear-fix">
			<li>
				<p style="color:#999;margin-bottom: 5px;">���ò�����1000��������</p>
				<textarea id="student_development" name="student_development" onkeydown='countChar(this);' onkeyup='countChar(this);'></textarea>
				<p name="text-prompt" style="color:#999;text-align:right;"><span name="number" style="padding: 0px 0px;">0</span>/1000</p>
			</li>
			
		   <input type="hidden" id="student_development_attachId1" name="student_development_attachId" value="">
			<li  style='height:45px;' class='position_relative'>
			   <span class='fl'>֤�����ϣ�</span>
			   <div id='student_development_attach_span1' class='position_upload_button_professional'></div>
			</li>
			<div id="student_development_attach_div1" class="only_attachments"></div>
		</ul>
	</div>
	<!-- ��ѧ˼�� e -->
	<!-- ��ְ���� e -->
	<div class="next-step clear-fix">
	 <a href="javascript:void(0);" target="_self" title="" class="fl" onclick="changeOption(15)">��һ��</a>
	 <a href="javascript:void(0);" target="_self" title="" class="fr" onclick="saveUpdateRefillData()">��һ��</a>
	</div>

</body>
</html>