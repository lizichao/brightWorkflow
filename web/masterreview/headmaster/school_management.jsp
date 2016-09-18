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
	<!-- ���� s -->
	<div class="grogress"><div class="line" style="width:92px;"><!-- 970/21 --></div></div>
	<!-- ���� e -->
	<!-- ���� s -->
	<div class="com-title">
		<div class="txt fl" >
			<h2><i>11</i>ѧУ����</h2>
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
				<textarea id="school_management" name="school_management"></textarea>
				<p style="color:#999;text-align:right;">0/1000</p>
			</li>
			<li>
				<span class="fl">֤�����ϣ�</span>
				<input type="button" value="����ϴ�" class="up-load fl" />
			</li>
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