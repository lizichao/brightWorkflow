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
		'title':"��ʵ��չʦ��ʦ�罨�裬��ʵ��ʦְҵ���¹淶Ҫ���Υ��ְҵ������Ϊ����취���Ͻ���ʦ�����г��ҽ̣���ʦ��Υ��Υ�ͷ��������"
	});
	$("#span2").customtip({
		'title':"������ȫ��ʦרҵ��չ�ƶȣ�ָ����չ��ʦרҵ��ѵ�������ʦרҵ��չ���ᣬ��ʵÿλ��ʦ����һ���ڲ�����360ѧʱ����ѵҪ��"
	});
	$("#span3").customtip({
		'title':"��ǿ�����ʦ����������ѧ�ƹǸɺ�����ʦ���γ��������ݶȷ�չ���ƣ���ʦ��������רҵˮƽ����������"
	});
	$("#span4").customtip({
		'title':"ѧУ��ʦ��Ч�����ƶ����ƣ�ѧУ�������ȡ���Ч���ʡ�ְ����Ƹ����ȿ��˵��ƶȰ취��ѧ���������ͼ�����ʦ���������ԣ���ʦ����ȸߡ�"
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
		attachmentArray.push("<a class=\"chachu\"  href=\"javascript:void(0);\" onclick=\"Headmaster.deleteReceiveFileAttachment(\'"+manageDifficultySelId+"\',this);\" >ɾ��</a>");
		
		$("#teacher_development_attach_div1").append(attachmentArray.join(""));
	}
	
	 $("#teacher_development_attachId1").val(manageDifficultySelId);
	 
	 Headmaster.initWebUploader('teacher_development_attach_span',1,'teacher_development','����ϴ�','teacher_development_attachId','teacher_development_attach_div');
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
			<h2><i>16</i>��ʦ��չ</h2>
			<p class="hide">
			  <span id="span1" >1��ʦ�½���    </span>
			  <span id="span2" >2��רҵ��չ    </span>
			  <span id="span3" >3���ݶ�����   </span>
			  <span id="span4" >4����������  </span>
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
				<textarea id="teacher_development" name="teacher_development" onkeydown='countChar(this);' onkeyup='countChar(this);'></textarea>
				<p name="text-prompt" style="color:#999;text-align:right;" ><span name="number" style="padding: 0px 0px;">0</span>/1000</p>
			</li>
			
			
	       <input type="hidden" id="teacher_development_attachId1" name="teacher_development_attachId1" value="">
			<li  style='height:45px;' class='position_relative'>
			   <span class='fl'>֤�����ϣ�</span>
			   <div id='teacher_development_attach_span1' class='position_upload_button_professional'></div>
			</li>
			<div id="teacher_development_attach_div1" class="only_attachments"></div>
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