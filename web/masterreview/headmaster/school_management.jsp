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
		'title':"�ƶ���ѧ���е�ѧУ��չ�滮����ʵ������ѧУ��չ����������ѧУ������չ��"
	});
	$("#span2").customtip({
		'title':"ѧУ���ӿ�ѧ�ֹ�������������ú���ְ����ȷ��ִ�й淶��Ч��"
	});
	$("#span3").customtip({
		'title':"�ƽ��ִ�ѧУ�ƶȽ��裬������У��У�񹫿���ע������������ְ���������ͼල���ƽ�ȫ������Э����Ⱥ����֯�������á�"
	});
	$("#span4").customtip({
		'title':"������ȫѧУ���¡������ʲ�������ƶȣ�����Ϣ���ֶ�����ѧУ����������������ƽ��������͸����"
	});
	$("#span5").customtip({
		'title':"����ƽ��У԰����ȫ�ȶ�����������ȫ����ȫ�������ʵ��Ч��ѧУ���շ������ƽ�ȫ�����ش�ȫά�������¹ʷ�����"
	});
});

function initSchoolManagementData(masterReviewVO){
	$("#school_management").val(masterReviewVO.school_management);
	countChar($("#school_management"));//��������
	var manageDifficultySelId = masterReviewVO.school_management_attachMentVO.attachmentId;
	var manageDifficultyFileName = masterReviewVO.school_management_attachMentVO.fileName;
	if(manageDifficultySelId){
		var attachmentArray =[];
		attachmentArray.push("<a class=\"chachu\"  href=\"<%=basePath%>WorkflowAttachMentDownload?attachmentId="+manageDifficultySelId+"\">"+manageDifficultyFileName+"</a>");
		attachmentArray.push("&nbsp;&nbsp;");
		attachmentArray.push("<a class=\"chachu\"  href=\"javascript:void(0);\" onclick=\"Headmaster.deleteReceiveFileAttachment(\'"+manageDifficultySelId+"\',this);\" >ɾ��</a>");
		
		$("#school_management_attach_div1").append(attachmentArray.join(""));
	}
	
	 $("#school_management_attachId1").val(manageDifficultySelId);
	 
	 Headmaster.initWebUploader('school_management_attach_span',1,'school_management','����ϴ�','school_management_attachId','school_management_attach_div');
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
			changeOption(13);
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
	var school_management_attachId = $("#school_management_attachId1").val();
	
	var workExperienceObject = {
			"id":$("#id").val(),
			"businessKey":$("#id").val(),
			"school_management":school_management,
			"school_management_attachId":school_management_attachId
	}
	submitArray.push(workExperienceObject);
	return JSON.stringify(submitArray);
}

function headmasterBeforeSubmit(formJsonData){
	formJsonData.option_tab_type = "school_management"
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
		<div class="txt fl" >
			<h2><i>12</i>ѧУ����</h2>
			<p >
				<span id="span1" >1���滮��չ  </span>
				<span id="span2" >2����������  </span>
				<span id="span3" >3����������  </span>
				<span id="span4" >4���ƶȽ���  </span>
				<span id="span5" >5����ȫ����  </span>
			<p>
		</div>
		<div class="select-step fr"><a href="javascript:void(0);" target="_self" title="" id="change">+&nbsp;�л�����</a></div>
		<div class="clear-both"></div>
	</div>
	<!-- ���� e -->
	<!-- ��ѧ˼�� s -->
	<div class="bxsx">
		<ul class="clear-fix">
			<li>
				<textarea id="school_management" name="school_management" onkeydown='countChar(this);' onkeyup='countChar(this);'></textarea>
				<p name="text-prompt" style="color:#999;text-align:right;"><span name="number" style="padding: 0px 0px;">0</span>/1000</p>
			</li>
			
			<input type="hidden" id="school_management_attachId1" name="school_management_attachId" value="">
			<li  style='height:45px;' class='position_relative'>
			   <span class='fl'>֤�����ϣ�</span>
			   <div id='school_management_attach_span1' class='position_upload_button_professional'></div>
			</li>
			<div id="school_management_attach_div1" class="only_attachments"></div>
		</ul>
	</div>
	<!-- ��ѧ˼�� e -->
	<!-- ��ְ���� e -->
	<div class="next-step clear-fix">
	 <a href="javascript:void(0);" target="_self" title="" class="fl" onclick="changeOption(11)">��һ��</a>
	 <a href="javascript:void(0);" target="_self" title="" class="fr" onclick="saveUpdateRefillData()">��һ��</a>
	</div>
</body>
</html>