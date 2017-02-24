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

<script id="accidentRec" type="text/x-jsrender">	
{{for Data}}
    <div class='show-list radius-3'>{{:#index+1}}<a href='javascript:void(0);' target='_self'  onclick="deleteSingleOption(this,'accident','{{:#index+1}}','{{:id}}');" class='del radius-3'>ɾ��</a></div>
   <div class="container">
      <input type='hidden' id="accidentId{{:#index+1}}"  value='{{:id}}'>
     <input type='hidden' id='proveAttachId{{:#index+1}}'  value='{{:proveAttachMentVO.attachmentId}}'>
	<ul class="clear-fix">
	<li>
        <span class="fl">�����¹����ƣ�</span>
        <div class="border_2 w_23 fl">
          <input type="text" id='accident_name{{:#index+1}}' value='{{:accident_name}}' placeholder="�����������¹�����" />
        </div>
    </li>
	<li>
         <span class="fl">Υ��������</span>
         <div class="border_2 w_13 fl">
          <input type="text" id='description{{:#index+1}}' value='{{:description}}' placeholder="������Υ������" />
         </div>
    </li>
	<li>
      <span class="fl">ʱ�䣺</span>
      <div class="border_2 w_20 fl">
       <input type="text" id='implement_time{{:#index+1}}' value='{{timeCovert implement_time/}}' onclick='selectDeleteTime()'  placeholder="������ʱ��" />
      </div>
    </li>
	<li>
      <span class="fl">��������</span>
       <textarea title='���ó���100��' name='process_result{{:#index+1}}' id='process_result{{:#index+1}}' style='width:334px;' class='fl deooration' onkeydown='countChar(this);' onkeyup='countChar(this);'>{{:process_result}}</textarea>
   	   <p name='text-prompt' style='color:#999;text-align:right;'><span name='number' style='padding:0px 0px;'>{{if process_result && process_result.length}}{{:process_result.length}}{{else}}0{{/if}}</span>/100&nbsp;</p> 
   </li>

   <li style='height:45px;' class='position_relative'>
      <span class="fl">֤�����ϣ�</span>
      <div id='accident_upload_div{{:#index+1}}' class='position_upload_button_professional'></div>
    </li>

    <div id='accident_div{{:#index+1}}' class='only_attachments'>
       {{if proveAttachMentVO.attachmentId !==null}}
         <a class='chachu' href="<%=basePath%>WorkflowAttachMentDownload?attachmentId={{:proveAttachMentVO.attachmentId}}">{{:proveAttachMentVO.fileName}}</a>
         &nbsp;&nbsp;&nbsp;&nbsp;
         <a class='chachu'  href='javascript:void(0);' onclick='Headmaster.deleteReceiveFileAttachment("{{:proveAttachMentVO.attachmentId}}",this);' >ɾ��</a>
       {{/if}}
    </div>

   </ul>
</div>
{{/for}}
</script>

<script type="text/javascript">
$(function(){
});

function initAccidentData(masterReviewVO){
	var situationVOs = masterReviewVO.situationVOs;
	var isContinue = false;
	Brightcom.workflow.initSelectCombox('headmaster_yes_no','hasAccident');
	if (situationVOs!=null&&situationVOs.length>0) {
		for(var i =0;i<situationVOs.length;i++){
			if (situationVOs[i].tableName == "headmaster_accident") {
				isContinue = true;
				$("#situation_id").val(situationVOs[i].id);
				$("#hasAccident").val(situationVOs[i].hasSituation);
				break;
			}
		}
	}
	if (!isContinue) return;
	if ($("#hasAccident").val()=="2") {
		$("#accidentRefill").show();
		$("#addDiv").show();
	}
	bulidAccident(masterReviewVO.accidentVOs);
}

//��ְ����
function bulidAccident(accidentVOs){
	$("#accidentRowNum").val(accidentVOs.length);
	if(accidentVOs.length>0){
		 var dataObject = {'Data': []};
		 for(var i =0;i<accidentVOs.length;i++){
			 dataObject.Data.push(accidentVOs[i]);
		 }
		 var subTaskContent= $("#accidentRec").render(dataObject);
		 $("#accidentRefill").append(subTaskContent);
		 
		 
		 for(var i =0;i<accidentVOs.length;i++){
			 Headmaster.initWebUploader('accident_upload_div',(i+1),'accident_type','����ϴ�','proveAttachId','accident_div');
		 }
		 
	}
	//$("#accidentRefill").append("<div class='add'><a class='add-more' href='javascript:void(0);' onclick='addAccidentSingle(this)' >+</a></div> ");
	//$('select').selectOrDie();
}

function addAccidentSingle(obj){
	var accidentRowNum = parseInt($("#accidentRowNum").val());
	var accidentRowNumNext = parseInt(accidentRowNum+1);

	var educationArray =[];
	educationArray.push("<div class='show-list radius-3'>"+accidentRowNumNext+"��<a href='javascript:void(0);' target='_self' onclick=\"deleteSingleOption(this,'accident',\'"+accidentRowNumNext+"\','');\" class='del radius-3'>ɾ��</a></div>");
	educationArray.push("<div class='container'>");
	educationArray.push("<ul  class='clear-fix'>");
	
	educationArray.push("<input type='hidden' id='accidentId"+accidentRowNumNext+"'  value=''>");
	educationArray.push("<input type='hidden' id='proveAttachId"+accidentRowNumNext+"'  value=''>");
	
	educationArray.push("<li><span class='fl'>�����¹����ƣ�</span>");
	educationArray.push("<div class='border_2 w_23 fl'>");
	educationArray.push("<input type='text' id='accident_name"+accidentRowNumNext+"' value='' placeholder='�����������¹�����'>");
	educationArray.push("</div>");
	educationArray.push("</li>");
	
	educationArray.push("<li><span class='fl'>Υ��������</span>");
	educationArray.push("<div class='border_2 w_13 fl'>");
	educationArray.push("<input type='text' id='description"+accidentRowNumNext+"' value='' placeholder='������Υ������'>");
	educationArray.push("</div>");
	educationArray.push("</li>");
	
	educationArray.push("<li><span class='fl'>ʱ�䣺</span>");
	educationArray.push("<div class='border_2 w_20 fl'>");
	educationArray.push("<input type='text' id='implement_time"+accidentRowNumNext+"'   onclick='selectDeleteTime()'  placeholder='������ʱ��' />");
	educationArray.push("</div>");
	educationArray.push("</li>");
	
	educationArray.push("<li><span class='fl'>��������</span>");
	educationArray.push("<textarea title='���ó���100��' name='process_result"+accidentRowNumNext+"'  id='process_result"+accidentRowNumNext+"' style='width:334px;'  class='fl deooration' placeholder='�����봦����' onkeydown='countChar(this);' onkeyup='countChar(this);'></textarea>");
	educationArray.push("<p name='text-prompt' style='color:#999;text-align:right;'><span name='number' style='padding:0px 0px;'>0</span>/100&nbsp;</p>");
	educationArray.push("</li>");
	
	educationArray.push("<li style='height:45px;' class='position_relative'>");
	educationArray.push(" <span class='fl'>֤�����ϣ�</span>");
	educationArray.push(" <div id='accident_upload_div"+accidentRowNumNext+"' class='position_upload_button_professional'></div>");
	educationArray.push("</li>");
	
	
	educationArray.push("<div id='accident_div"+accidentRowNumNext+"' class='only_attachments'></div>");
	
	
	educationArray.push("</ul>");
	educationArray.push("</div>");
	
	//educationArray.push("<div class='add'><a class='add-more' href='javascript:void(0);' onclick='addAccidentSingle(this)' >+</a></div>");
	
	//$(obj).after(educationArray.join(""));
	//$(obj).remove();
	$("#accidentRefill").append(educationArray.join(""));
	
	$("#accidentRowNum").val(accidentRowNumNext);
	
	/*
	 * 1��professionalTitlespan:�ϴ���ť��ʾλ��id
	 4��buttonName �� ��ť����
	 5��hiddenAttachId�����ظ���id��ѡ�к�Ѹ���ֵ�����ڸ���������
	 6�� hiddenDisplayId:������ʾ��div id
	 */
	 Headmaster.initWebUploader('accident_upload_div',accidentRowNumNext,'accident_type','����ϴ�','proveAttachId','accident_div');
}


function saveUpdateRefillData(){
	var isRefillTask = $("#isRefillTask").val();
	if(isRefillTask){ 
		var submitStrings =  getSubmitStrings();
		var bcReq = new BcRequest('headmaster','masterReviewAction','saveUpdateRefillData');
		bcReq.setExtraPs({
			    "option_tab_type":'accident',
			    "option_tab_values":submitStrings,
			    "businessKey":processBusinessKey
			}
		);
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
	var accidentRowNum = $("#accidentRowNum").val();
	var situationObject = {
		"situation_id":$("#situation_id").val(),
		"situation_businessKey":$("#id").val(),
		"situation_hasSituation":$("#hasAccident").val()
	}
	submitArray.push(situationObject);
	for(var i=0;i<accidentRowNum;i++){
		var rowNum = (i+1);
		var id = $("#accidentId"+rowNum).val();
		var accident_name = $("#accident_name"+rowNum).val();
		var description = $("#description"+rowNum).val();
		var implement_time = $("#implement_time"+rowNum).val();
		var process_result = $("#process_result"+rowNum).val();
		var businessKey = $("#id").val();
		var proveAttachId = $("#proveAttachId"+rowNum).val();
		if(!accident_name){
			continue;
		}
		var workExperienceObject = {
				"id":id,
				"businessKey":$("#id").val(),
				"accident_name":accident_name,
				"description":description,
				"implement_time":implement_time,
				'process_result' :process_result,
				"proveAttachId":proveAttachId
		}
		submitArray.push(workExperienceObject);
	}
	return JSON.stringify(submitArray);
}

function headmasterBeforeSubmit(formJsonData){
	formJsonData.option_tab_type = "accident"
	formJsonData.option_tab_values = getSubmitStrings();
}

function countChar(curObj) {//��������
	var maxLength = 100;//100���ַ�
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

function showInfoFn(){
	var hasAccident = $("#hasAccident").val();
	if (hasAccident=='1') {//����ʾ����
		$("#accidentRefill").hide();
		$("#addDiv").hide();
	} else if (hasAccident=='2') { //��ʾ����
		$("#accidentRefill").show();
		$("#addDiv").show();
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
			<h2><i>22</i>�����¹����</h2>
			<p>��дѧУ���ְ�ȫ�����¹ʣ����������Υ�������ܵ��ϼ�ͨ���������ϴ���</p>
		</div>
		<div class="select-step fr"><a href="javascript:void(0);" target="_self" title="" id="change">+&nbsp;�л�����</a></div>
		<div class="clear-both"></div>
	</div>
	<!-- ���� e -->
	<!--   -->
	<div class="years">
		<div class="container">
			<ul class="clear-fix">
				<li>
					<div class="border_2" style="width: 180px;margin: 0 auto;">
						<input type="hidden" id="situation_id"/>
						<span class="fl">���������¹������</span>
						<select id="hasAccident" onchange="showInfoFn()"></select>
					</div>
				</li>
			</ul>
		</div>
	</div>
	<!--  -->
	<!-- ��ְ���� s -->
	<div id="accidentRefill" class="years" style="display: none">
		<input type="hidden" id="accidentRowNum" name="accidentRowNum" value="0">
	</div>
	<!-- ��ְ���� e -->
	
	<div id="addDiv" class="add" style="display: none"><a href="javascript:void(0);" onclick="addAccidentSingle(this)" class="add-more">+</a></div>
	
	<div class="next-step clear-fix">
	   <a href="javascript:void(0);" target="_self" title="" class="fl" onclick="changeOption(21)">��һ��</a>
	   <a href="javascript:void(0);" target="_self" title="" class="fr" onclick="saveUpdateRefillData()">��һ��</a>
	</div>
</body>
</html>