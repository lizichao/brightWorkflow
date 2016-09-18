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
		'title':"��Чͳ����ҡ��ط���ѧУ�����γ̣�ȷ�����ҿγ̡��ط��γ̵���ʵ�������Կ���У���γ̣�����ѧ����Ԫ����"
	});
	$("#span2").customtip({
		'title':"����Ϊ�������뿪��������������������ۺ�ʵ����ȿγ̣�ʵʩ���ʽ�����ȷ��ѧ��ÿ��һСʱУ԰�������"
	});
	$("#span3").customtip({
		'title':"���ý�ѧ�����Ƚ��������¿θ����ʵʩ��Ч���ã���ѧ����淶���򣻶�������������Ρ�"
	});
	$("#span4").customtip({
		'title':"������ȫ��ѧ������ء������͸Ľ����ƣ������ؽ�ѧ���̣�����ѧ��������ʱ�Ľ�����ѧ��������������"
	});
	$("#span5").customtip({
		'title':"�̿��й����ƽ������������о��ɹ���˶��������ѧ����������ߡ�"
	});
});

function initEducationScienceData(masterReviewVO){
	$("#education_science").val(masterReviewVO.education_science);
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
			changeOption(13);
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
	
	var workExperienceObject = {
			"id":$("#id").val(),
			"businessKey":$("#id").val(),
			"education_science":education_science
	}
	submitArray.push(workExperienceObject);
	return JSON.stringify(submitArray);
}

function headmasterBeforeSubmit(formJsonData){
	formJsonData.option_tab_type = "education_science"
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
			<h2><i>12</i>������ѧ</h2>
			<p class="hide">
				 <span id="span1" >1���γ̽���  </span>
				 <span id="span2" >2���������������������ۺ�ʵ����ȿγ̿���  </span>
				 <span id="span3" > 3�����ý�ѧ </span>
				 <span id="span4" >4���������  </span>
				 <span id="span5" > 5����ѧ�о�</span>
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
				<textarea id="education_science" name="education_science"></textarea>
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
	 <a href="javascript:void(0);" target="_self" title="" class="fl" onclick="changeOption(11)">��һ��</a>
	 <a href="javascript:void(0);" target="_self" title="" class="fr" onclick="saveUpdateRefillData()">��һ��</a>
	</div>

</body>
</html>