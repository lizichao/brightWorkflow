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

<script id="socialDutyRec" type="text/x-jsrender">	
{{for Data}}
    <div class='show-list radius-3'>{{:#index+1}}<a href='javascript:void(0);' target='_self'  onclick="deleteSingleOption(this,'socialDuty','{{:#index+1}}','{{:id}}');" class='del radius-3'>ɾ��</a></div>
   <div class="container">
     <input type='hidden' id="socialDutyId{{:#index+1}}"  value='{{:id}}'>
	<ul class="clear-fix">

	<li>
        <span class="fl">�е��ϼ����Ű��ŵ�������ι�����</span>
        <div class="border_2 w_25 fl">
          <input type="text" id='superior_task{{:#index+1}}' value='{{:superior_task}}' placeholder="���������ι���" />
        </div>
    </li>

	<li>
         <span class="fl">�������Ų��ţ�</span>
         <div class="border_2 w_23 fl"><input type="text" id='arrange_department{{:#index+1}}' value='{{:arrange_department}}' placeholder="�����빤�����Ų���" /></div>
    </li>

	<li>
      <span class="fl">ʱ�䣺</span>
      <div class="border_2 w_20 fl">
       <input type="text" id='implement_time{{:#index+1}}' value='{{timeCovert implement_time/}}' onclick='selectDeleteTime()' placeholder="������ʱ��" />
      </div>
    </li>

	<li>
      <span class="fl">��������</span>
      <div class="border_2 w_13 fl">
       <input type="text" id='complete_state{{:#index+1}}' value='{{:complete_state}}' placeholder="������������" />
      </div>
    </li>

   </ul>
</div>
{{/for}}
</script>

<script type="text/javascript">
$(function(){
});

function initSocialDutyData(masterReviewVO){
	bulidSocialDuty(masterReviewVO.socialDutyVOs);
}

//��ְ����
function bulidSocialDuty(socialDutyVOs){
	$("#socialDutyRowNum").val(socialDutyVOs.length);
	if(socialDutyVOs.length>0){
		 var dataObject = {'Data': []};
		 for(var i =0;i<socialDutyVOs.length;i++){
			 dataObject.Data.push(socialDutyVOs[i]);
		 }
		 var subTaskContent= $("#socialDutyRec").render(dataObject);
		 $("#socialDutyRefill").append(subTaskContent);
		 
	}
	//$("#socialDutyRefill").append("<div class='add'><a class='add-more' href='javascript:void(0);' onclick='addSocialDutySingle(this)' >+</a></div> ");
}

function addSocialDutySingle(obj){
	var socialDutyRowNum = parseInt($("#socialDutyRowNum").val());
	var socialDutyRowNumNext = parseInt(socialDutyRowNum+1);

	var educationArray =[];
	educationArray.push("<div class='show-list radius-3'>"+socialDutyRowNumNext+"��<a href='javascript:void(0);' target='_self' onclick=\"deleteSingleOption(this,'socialDuty',\'"+socialDutyRowNumNext+"\','');\" class='del radius-3'>ɾ��</a></div>");
	educationArray.push("<div class='container'>");
	educationArray.push("<ul  class='clear-fix'>");
	
	educationArray.push("<input type='hidden' id='socialDutyId"+socialDutyRowNumNext+"'  value=''>");
	educationArray.push("<input type='hidden' id='proveId"+socialDutyRowNumNext+"' value=''>");
	
	educationArray.push("<li><span class='fl'>�е��ϼ����Ű��ŵ�������ι�����</span>");
	educationArray.push("<div class='border_2 w_25 fl'>");
	educationArray.push("<input type='text' id='superior_task"+socialDutyRowNumNext+"' value='' placeholder='������������ι���'>");
	educationArray.push("</div>");
	educationArray.push("</li>");
	
	educationArray.push("<li><span class='fl'>�������Ų��ţ�</span>");
	educationArray.push("<div class='border_2 w_23 fl'>");
	educationArray.push("<input type='text' id='arrange_department"+socialDutyRowNumNext+"'   value='' placeholder='�����빤�����Ų���' />");
	educationArray.push("</div>");
	educationArray.push("</li>");
	
	educationArray.push("<li><span class='fl'>ʱ�䣺</span>");
	educationArray.push("<div class='border_2 w_20 fl'>");
	educationArray.push("<input type='text' id='implement_time"+socialDutyRowNumNext+"' onclick='selectDeleteTime()' placeholder='������ʱ��'>");
	educationArray.push("</div>");
	educationArray.push("</li>");
	
	
	educationArray.push("<li><span class='fl'>��������</span>");
	educationArray.push("<div class='border_2 w_13 fl'>");
	educationArray.push("<input type='text' id='complete_state"+socialDutyRowNumNext+"'   value='' placeholder='������������' />");
	educationArray.push("</div>");
	educationArray.push("</li>");
	
	educationArray.push("</ul>");
	educationArray.push("</div>");
	
	//educationArray.push("<div class='add'><a class='add-more' href='javascript:void(0);' onclick='addSocialDutySingle(this)' >+</a></div>");
	
	
	//$(obj).after(educationArray.join(""));
	//$(obj).remove();
	$("#socialDutyRefill").append(educationArray.join(""));
	
	$("#socialDutyRowNum").val(socialDutyRowNumNext);
}

function saveUpdateRefillData(){
	var isRefillTask = $("#isRefillTask").val();
	if(isRefillTask){
		var submitStrings =  getSubmitStrings();
		var bcReq = new BcRequest('headmaster','masterReviewAction','saveUpdateRefillData');
		bcReq.setExtraPs({
			    "option_tab_type":'socialDuty',
			    "option_tab_values":submitStrings,
			    "businessKey":processBusinessKey
		});
		bcReq.setSuccFn(function(data,status){
			changeOption(21);
		});
		bcReq.postData();
	}else{
		submitRefillTask();
	}
}

function getSubmitStrings(){
	var submitArray = [];
	var socialDutyRowNum = $("#socialDutyRowNum").val();
	for(var i=0;i<socialDutyRowNum;i++){
		var rowNum = (i+1);
		var id = $("#socialDutyId"+rowNum).val();
		var implement_time = $("#implement_time"+rowNum).val();
		var superior_task = $("#superior_task"+rowNum).val();
		var arrange_department = $("#arrange_department"+rowNum).val();
		var complete_state = $("#complete_state"+rowNum).val();
		var businessKey = $("#id").val();
		if(!superior_task){
			continue;
		}
		var workExperienceObject = {
				"id":id,
				"implement_time":implement_time,
				"businessKey":$("#id").val(),
				"superior_task":superior_task,
				"arrange_department":arrange_department,
				"complete_state":complete_state
		}
		submitArray.push(workExperienceObject);
	}
	return JSON.stringify(submitArray);
}

function headmasterBeforeSubmit(formJsonData){
	formJsonData.option_tab_type = "socialDuty"
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
		<div class="txt fl">
			<h2><i>20</i>�������</h2>
			<p>��дѧУ�����е��ϼ��������Ű��ŵİ����֧�̻�������</p>
		</div>
		<div class="select-step fr"><a href="javascript:void(0);" target="_self" title="" id="change">+&nbsp;�л�����</a></div>
		<div class="clear-both"></div>
	</div>
	<!-- ���� e -->
	<!-- ��ְ���� s -->
	<div id="socialDutyRefill" class="years">
		<input type="hidden" id="socialDutyRowNum" name="socialDutyRowNum" value="0">
	</div>
	<!-- ��ְ���� e -->
	
	<div class="add"><a href="javascript:void(0);" onclick="addSocialDutySingle(this)" class="add-more">+</a></div>
	
	<div class="next-step clear-fix">
	 <a href="javascript:void(0);" target="_self" title="" class="fl" onclick="changeOption(19)">��һ��</a>
	 <a href="javascript:void(0);" target="_self" title="" class="fr" onclick="saveUpdateRefillData()">��һ��</a>
	</div>
</body>
</html>