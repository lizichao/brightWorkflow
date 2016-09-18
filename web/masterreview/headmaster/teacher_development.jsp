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
			changeOption(16);
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
	
	var workExperienceObject = {
			"id":$("#id").val(),
			"businessKey":$("#id").val(),
			"teacher_development":teacher_development
	}
	submitArray.push(workExperienceObject);
	return JSON.stringify(submitArray);
}

function headmasterBeforeSubmit(formJsonData){
	formJsonData.option_tab_type = "teacher_development"
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
		<div class="txt fl" id="showorhide">
			<h2><i>15</i>��ʦ��չ</h2>
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
				<textarea id="teacher_development" name="teacher_development"></textarea>
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
	 <a href="javascript:void(0);" target="_self" title="" class="fl" onclick="changeOption(14)">��һ��</a>
	 <a href="javascript:void(0);" target="_self" title="" class="fr" onclick="saveUpdateRefillData()">��һ��</a>
	</div>

</body>
</html>