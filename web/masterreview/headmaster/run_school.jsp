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
<title>������У��ְ������ϵͳ</title>


<script type="text/javascript">
$(function(){
	$("#span1").customtip({
		'title':"���������Ƚ���ʦ��֪���ȡ��Ͽɶȸߣ��ĸﴴ����ʶǿ������˼·��ȷ����ѧ��ʩ������"
	});
	$("#span2").customtip({
		'title':"����У԰�Ļ�������ѧУ����Ӫ��Ũ��������Ļ���"
	});
});

function initRunSchoolData(masterReviewVO){
	$("#run_school").val(masterReviewVO.run_school);
	countChar($("#run_school"));//��������
	var manageDifficultySelId = masterReviewVO.run_school_attachMentVO.attachmentId;
	var manageDifficultyFileName = masterReviewVO.run_school_attachMentVO.fileName;
	if(manageDifficultySelId){
		var attachmentArray =[];
		attachmentArray.push("<a class=\"chachu\"  href=\"<%=basePath%>WorkflowAttachMentDownload?attachmentId="+manageDifficultySelId+"\">"+manageDifficultyFileName+"</a>");
		attachmentArray.push("&nbsp;&nbsp;");
		attachmentArray.push("<a class=\"chachu\"  href=\"javascript:void(0);\" onclick=\"Headmaster.deleteReceiveFileAttachment(\'"+manageDifficultySelId+"\',this);\" >ɾ��</a>");
		
		$("#run_school_attach_div1").append(attachmentArray.join(""));
	}
	
	 $("#run_school_attachId1").val(manageDifficultySelId);
	 
	 Headmaster.initWebUploader('run_school_attach_span',1,'run_school','����ϴ�','run_school_attachId','run_school_attach_div');
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
		<div class="txt fl">
			<h2><i>11</i>��ѧ˼��</h2>
			<p ><span id="span1" >1����ѧ����</span> <span id="span2" > 2��У԰�Ļ�</span></p>
			<!-- <a href="javascript:void(0);" target="_self" title="">չ��</a> -->
		</div>
		<div class="select-step fr"><a href="javascript:void(0);" target="_self" title="" id="change">+&nbsp;�л�����</a></div>
		<div class="clear-both"></div>
	</div>
	<!-- ���� e -->
	<!-- ��ѧ˼�� s -->
	<div class="bxsx">
		<ul class="clear-fix">
			<li>
				<textarea id="run_school" name="run_school" onkeydown='countChar(this);' onkeyup='countChar(this);'></textarea>
				<p name="text-prompt" style="color:#999;text-align:right;"><span name="number" style="padding: 0px 0px;">0</span>/1000</p>
			</li>
			
			
	
				
			<input type="hidden" id="run_school_attachId1" name="run_school_attachId" value="">
			<li  style='height:45px;' class='position_relative'>
			   <span class='fl'>֤�����ϣ�</span>
			   <div id='run_school_attach_span1' class='position_upload_button_professional'></div>
			</li>
			<div id="run_school_attach_div1" class="only_attachments"></div>
		</ul>
	</div>
	<!-- ��ѧ˼�� e -->
	<!-- ��ְ���� e -->
	<div class="next-step clear-fix">
	 <a href="javascript:void(0);" target="_self" title="" class="fl" onclick="changeOption(10)">��һ��</a>
	 <a href="javascript:void(0);" target="_self" title="" class="fr" onclick="saveUpdateRefillData()">��һ��</a>
	</div>

</body>
</html>