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

<!--���ִ���  -->
<script id="punishmentRec" type="text/x-jsrender">	
{{for Data}}
    <div class='show-list radius-3'>{{:#index+1}}<a href='javascript:void(0);' target='_self'  onclick="deleteSingleOption(this,'punishment','{{:#index+1}}','{{:id}}');" class='del radius-3'>ɾ��</a></div>
   <div class="container">
     <input type='hidden' id="punishmentId{{:#index+1}}"  value='{{:id}}'>
	<ul class="clear-fix">

	<li>
      <span class="fl">ʱ�䣺</span>
      <div class="border_2 w_20 fl">
        <input type="text" id='implement_time{{:#index+1}}' value='{{timeCovert implement_time/}}' onclick='selectDeleteTime()' placeholder="������ʱ��" />
      </div>
    </li>

	<li>
        <span class="fl">�����¼�������</span>
        <div class="border_2 w_23 fl">
          <input type="text" id='description{{:#index+1}}' value='{{:description}}' placeholder="�����봦���¼�����" />
        </div>
    </li>

	<li>
         <span class="fl">�ܴ����ˣ�</span>
         <div class="border_2 w_13 fl">
          <input type="text" id='people{{:#index+1}}' value='{{:people}}' placeholder="�������ܴ�����" />
         </div>
    </li>

	<li>
      <span class="fl">���ֲ��ţ�</span>
      <div class="border_2 w_13 fl">
       <input type="text" id='department{{:#index+1}}' value='{{:department}}'  placeholder="�����봦�ֲ���" />
      </div>
    </li>

	<li>
      <span class="fl">���ֽ����</span>
      <div class="border_2 w_13 fl">
       <input type="text" id='process_result{{:#index+1}}' value='{{:process_result}}'  placeholder="�����봦�ֽ��"/>
      </div>
    </li>
  
   </ul>
</div>
{{/for}}
</script>

<script type="text/javascript">
$(function(){
});

function initPunishmentData(masterReviewVO){
	bulidPunishment(masterReviewVO.punishmentVOs);
}

//��ְ����
function bulidPunishment(punishmentVOs){
	$("#punishmentRowNum").val(punishmentVOs.length);
	if(punishmentVOs.length>0){
		 var dataObject = {'Data': []};
		 for(var i =0;i<punishmentVOs.length;i++){
			 dataObject.Data.push(punishmentVOs[i]);
		 }
		 var subTaskContent= $("#punishmentRec").render(dataObject);
		 $("#punishmentRefill").append(subTaskContent);
		 
	}
	//$("#punishmentRefill").append("<div class='add'><a class='add-more' href='javascript:void(0);' onclick='addPunishmentSingle(this)' >+</a></div> ");
}

function addPunishmentSingle(obj){
	var punishmentRowNum = parseInt($("#punishmentRowNum").val());
	var punishmentRowNumNext = parseInt(punishmentRowNum+1);

	var educationArray =[];
	educationArray.push("<div class='show-list radius-3'>"+punishmentRowNumNext+"��<a href='javascript:void(0);' target='_self' onclick=\"deleteSingleOption(this,'punishment',\'"+punishmentRowNumNext+"\','');\" class='del radius-3'>ɾ��</a></div>");
	educationArray.push("<div class='container'>");
	educationArray.push("<ul  class='clear-fix'>");
	
	
	educationArray.push("<input type='hidden' id='punishmentId"+punishmentRowNumNext+"'  value=''>");

	
	educationArray.push("<li><span class='fl'>ʱ�䣺</span>");
	educationArray.push("<div class='border_2 w_20 fl'>");
	educationArray.push("<input type='text' id='implement_time"+punishmentRowNumNext+"'   onclick='selectDeleteTime()' placeholder='������ʱ��' />");
	educationArray.push("</div>");
	educationArray.push("</li>");
	
	educationArray.push("<li><span class='fl'>�����¼�������</span>");
	educationArray.push("<div class='border_2 w_23 fl'>");
	educationArray.push("<input type='text' id='description"+punishmentRowNumNext+"' value='' placeholder='�����봦���¼�����'>");
	educationArray.push("</div>");
	educationArray.push("</li>");
	
	educationArray.push("<li><span class='fl'>�ܴ����ˣ�</span>");
	educationArray.push("<div class='border_2 w_13 fl'>");
	educationArray.push("<input type='text' id='people"+punishmentRowNumNext+"'   value='' placeholder='�������ܴ�����' />");
	educationArray.push("</div>");
	educationArray.push("</li>");
	
	
	educationArray.push("<li><span class='fl'>���ֲ��ţ�</span>");
	educationArray.push("<div class='border_2 w_13 fl'>");
	educationArray.push("<input type='text' id='department"+punishmentRowNumNext+"'   value='' placeholder='�����봦�ֲ���' />");
	educationArray.push("</div>");
	educationArray.push("</li>");
	
	
	educationArray.push("<li><span class='fl'>��������</span>");
	educationArray.push("<div class='border_2 w_13 fl'>");
	educationArray.push("<input type='text' id='process_result"+punishmentRowNumNext+"'   value='' placeholder='�����봦����' />");
	educationArray.push("</div>");
	educationArray.push("</li>");
	

	
	educationArray.push("</ul>");
	educationArray.push("</div>");
	
	//educationArray.push("<div class='add'><a class='add-more' href='javascript:void(0);' onclick='addPunishmentSingle(this)' >+</a></div>");
	
	//$(obj).after(educationArray.join(""));
	//$(obj).remove();
	$("#punishmentRefill").append(educationArray.join(""));
	
	$("#punishmentRowNum").val(punishmentRowNumNext);
}

function saveUpdateRefillData(){
	var isRefillTask = $("#isRefillTask").val();
	if(isRefillTask){
		var submitStrings =  getSubmitStrings();
		var bcReq = new BcRequest('headmaster','masterReviewAction','saveUpdateRefillData');
		bcReq.setExtraPs({
			    "option_tab_type":'punishment',
			    "option_tab_values":submitStrings,
			    "businessKey":processBusinessKey
		});
		bcReq.setSuccFn(function(data,status){
			changeOption(23);
		});
		bcReq.postData();
	}else{
		submitRefillTask();
	}
}

function getSubmitStrings(){
	var submitArray = [];
	var punishmentRowNum = $("#punishmentRowNum").val();
	for(var i=0;i<punishmentRowNum;i++){
		var rowNum = (i+1);
		var id = $("#punishmentId"+rowNum).val();
		var implement_time = $("#implement_time"+rowNum).val();
		var description = $("#description"+rowNum).val();
		var people = $("#people"+rowNum).val();
		var department = $("#department"+rowNum).val();
		var process_result = $("#process_result"+rowNum).val();
		var businessKey = $("#id").val();
		if(!implement_time){
			continue;
		}
		var workExperienceObject = {
				"id":id,
				"implement_time":implement_time,
				"businessKey":$("#id").val(),
				"description":description,
				"people":people,
				"department":department,
				"process_result":process_result
		}
		submitArray.push(workExperienceObject);
	}
	return JSON.stringify(submitArray);
}

function headmasterBeforeSubmit(formJsonData){
	formJsonData.option_tab_type = "punishment"
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
			<h2><i>22</i>����</h2>
			<p>��дУ�������ܵ����͡��������ֻ��������</p>
		</div>
		<div class="select-step fr"><a href="javascript:void(0);" target="_self" title="" id="change">+&nbsp;�л�����</a></div>
		<div class="clear-both"></div>
	</div>
	<!-- ���� e -->
	<!-- ��ְ���� s -->
	<div id="punishmentRefill" class="years">
		<input type="hidden" id="punishmentRowNum" name="punishmentRowNum" value="0">
	</div>
	<!-- ��ְ���� e -->
	
	<div class="add"><a href="javascript:void(0);" onclick="addPunishmentSingle(this)" class="add-more">+</a></div>
	
	<div class="next-step clear-fix">
	  <a href="javascript:void(0);" target="_self" title="" class="fl" onclick="changeOption(21)">��һ��</a>
	  <a href="javascript:void(0);" target="_self" title="" class="fr" onclick="saveUpdateRefillData()">��һ��</a>
	</div>
</body>
</html>