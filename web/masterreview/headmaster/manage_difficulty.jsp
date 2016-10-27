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
<link rel="stylesheet" type="text/css" href="/masterreview/Css/selectordie.css" />
<script type="text/javascript" src="/masterreview/js/selectordie.min.js"></script>



<script type="text/javascript">
$(function(){
	//$('select').selectOrDie({
	//	placeholder: '��ѡ��ѧУ����'
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
		attachmentArray.push("<a class=\"chachu\"  href=\"#\" onclick=\"deleteReceiveFileAttachment(\'"+manageDifficultySelId+"\',this);\" >ɾ��</a>");
		
		$("#manageDifficultyDiv2").append(attachmentArray.join(""));
	}
	
	var manageDifficultyAgoSelId = masterReviewVO.manageDifficultyAgoAttachMentVO.attachmentId;
	var manageDifficultyAgoFileName = masterReviewVO.manageDifficultyAgoAttachMentVO.fileName;
	if(manageDifficultyAgoSelId){
		var attachmentArray =[];
		attachmentArray.push("<a class=\"chachu\"  href=\"<%=basePath%>WorkflowAttachMentDownload?attachmentId="+manageDifficultyAgoSelId+"\">"+manageDifficultyAgoFileName+"</a>");
		attachmentArray.push("&nbsp;&nbsp;");
		attachmentArray.push("<a class=\"chachu\"  href=\"#\" onclick=\"deleteReceiveFileAttachment(\'"+manageDifficultyAgoSelId+"\',this);\" >ɾ��</a>");
		
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
	 * 1��professionalTitlespan:�ϴ���ť��ʾλ��id
	 4��buttonName �� ��ť����
	 5��hiddenAttachId�����ظ���id��ѡ�к�Ѹ���ֵ�����ڸ���������
	 6�� hiddenDisplayId:������ʾ��div id
	 */
	 Headmaster.initWebUploader('manageDifficultyAgo',1,'manageDifficulty','����ϴ�','manageDifficultyAgoAttachId','manageDifficultyAgoDiv');
	 Headmaster.initWebUploader('manageDifficulty',2,'manageDifficulty','����ϴ�','manageDifficultyAttachId','manageDifficultyDiv');
	 
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
	<!-- ���� s -->
	<div class="grogress"><div class="line" ><!-- 970/21 --></div></div>
	<!-- ���� e -->
	<!-- ���� s -->
	<div class="com-title">
		<div class="txt fl">
			<h2><i>10</i>�����Ѷ�</h2>
			<p>1���н�����ְ�����ģ���д����ְѧУ��2����ѡһ������������Ѷ�����ѧУ�����д��</p>
		</div>
		<div class="select-step fr"><a href="javascript:void(0);" target="_self" title="" id="change">+&nbsp;�л�����</a></div>
		<div class="clear-both"></div>
	</div>
	<!-- ���� e -->
	<!-- �����Ѷ� s -->
	<div class="manage clear-fix">
		<ul class="manage-ul clear-fix fl" style="margin-left:95px;">
			<li>
				<div class="border_1 w_22" style="z-index:99;">
					<span>ѧУ���ͣ�</span>
					<select id="schoolTypeAgo">
					  <option value="">��ѡ��ѧУ����</option>
					</select>
					<p>(��8����ְѧУ)</p>
				</div>
			</li>
			<li>
				<div class="border_1 w_22" style="z-index:98;">
					<span>ѧУ���ƣ�</span>
				    <input type="text" id="schoolNameSpaceAgo" />
				</div>
			</li>
			<li>
				<div class="border_1 w_22" style="z-index:97;">
					<span>У��������</span>
					<input type="text" id="schoolCountAgo" />
				</div>
			</li>
			<li>
				<div class="border_1 w_22" style="z-index:96;">
					<span>�༶������</span>
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
					<span>ѧУ���ͣ�</span>
					<select id="schoolType">
					</select>
					<p>(����ְѧУ)</p>
				</div>
			</li>
			<li>
				<div class="border_1 w_22" style="z-index:98;">
					<span>ѧУ���ƣ�</span>
					<input type="text" id="schoolNameSpace" />
				</div>
			</li>
			<li>
				<div class="border_1 w_22" style="z-index:97;">
					<span>У��������</span>
					<input type="text" id="schoolCount" />
				</div>
			</li>
			<li>
				<div class="border_1 w_22" style="z-index:96;">
					<span>�༶������</span>
					<input type="text" id="studentNumber"/>
				</div>
			</li>
			 <input type="hidden" id="manageDifficultyAttachId2" name="manageDifficultyAttachId2" value="">
			<li> <span id="manageDifficulty2"></span></li>
			 <div id="manageDifficultyDiv2" class="only_attachments"></div>
		</ul>
	</div>
	<!-- ��ְ���� e -->
	<div class="next-step clear-fix">
	   <a href="javascript:void(0);" target="_self" title="" class="fl" onclick="changeOption(9)">��һ��</a>
	   <a href="javascript:void(0);" target="_self" title="" class="fr" onclick="saveUpdateRefillData()">��һ��</a>
</div>
</body>
</html>