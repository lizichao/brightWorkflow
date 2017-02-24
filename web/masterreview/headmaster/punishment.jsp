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
     <input type='hidden' id='proveAttachId{{:#index+1}}'  value='{{:proveAttachMentVO.attachmentId}}'>
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
      <span class="fl">��������</span>
       <textarea title='���ó���100��' name='process_result{{:#index+1}}' id='process_result{{:#index+1}}' style='width:334px;' class='fl deooration' onkeydown='countChar(this);' onkeyup='countChar(this);'>{{:process_result}}</textarea>
       <p name='text-prompt' style='color:#999;text-align:right;'><span name='number' style='padding:0px 0px;'>{{if process_result && process_result.length}}{{:process_result.length}}{{else}}0{{/if}}</span>/100&nbsp;</p>
	</li>

    <li style='height:45px;' class='position_relative'>
      <span class="fl">֤�����ϣ�</span>
      <div id='punishment_upload_div{{:#index+1}}' class='position_upload_button_professional'></div>
    </li>

    <div id='punishment_div{{:#index+1}}' class='only_attachments'>
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

function initPunishmentData(masterReviewVO){
	
	var situationVOs = masterReviewVO.situationVOs;
	var isContinue = false;debugger;
	Brightcom.workflow.initSelectCombox('headmaster_yes_no','hasPunishment');
	if (situationVOs!=null&&situationVOs.length>0) {
		for(var i =0;i<situationVOs.length;i++){
			if (situationVOs[i].tableName == "headmaster_punishment") {
				isContinue = true;
				$("#situation_id").val(situationVOs[i].id);
				$("#hasPunishment").val(situationVOs[i].hasSituation);
				break;
			}
		}
	}
	if (!isContinue) return;
	if ($("#hasPunishment").val()=="2") {
		$("#punishmentRefill").show();
		$("#addDiv").show();
	}
	
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
		 
		 for(var i =0;i<punishmentVOs.length;i++){
			 Headmaster.initWebUploader('punishment_upload_div',(i+1),'punishment_type','����ϴ�','proveAttachId','punishment_div');
		 }
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
	educationArray.push("<input type='hidden' id='proveAttachId"+punishmentRowNumNext+"'  value=''>");

	
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
	educationArray.push("<textarea title='���ó���100��' name='process_result"+punishmentRowNumNext+"'  id='process_result"+punishmentRowNumNext+"' style='width:334px;'  class='fl deooration' placeholder='�����봦����' onkeydown='countChar(this);' onkeyup='countChar(this);'></textarea>");
	educationArray.push("<p name='text-prompt' style='color:#999;text-align:right;'><span name='number' style='padding:0px 0px;'>0</span>/100&nbsp;</p>");
	educationArray.push("</li>");
	
	educationArray.push("<li style='height:45px;' class='position_relative'>");
	educationArray.push(" <span class='fl'>֤�����ϣ�</span>");
	educationArray.push(" <div id='punishment_upload_div"+punishmentRowNumNext+"' class='position_upload_button_professional'></div>");
	educationArray.push("</li>");
	
	
	educationArray.push("<div id='punishment_div"+punishmentRowNumNext+"' class='only_attachments'></div>");
	
	educationArray.push("</ul>");
	educationArray.push("</div>");
	
	//educationArray.push("<div class='add'><a class='add-more' href='javascript:void(0);' onclick='addPunishmentSingle(this)' >+</a></div>");
	
	//$(obj).after(educationArray.join(""));
	//$(obj).remove();
	$("#punishmentRefill").append(educationArray.join(""));
	
	$("#punishmentRowNum").val(punishmentRowNumNext);
	
	/*
	 * 1��professionalTitlespan:�ϴ���ť��ʾλ��id
	 4��buttonName �� ��ť����
	 5��hiddenAttachId�����ظ���id��ѡ�к�Ѹ���ֵ�����ڸ���������
	 6�� hiddenDisplayId:������ʾ��div id
	 */
	 Headmaster.initWebUploader('punishment_upload_div',punishmentRowNumNext,'punishment_type','����ϴ�','proveAttachId','punishment_div');
}

function saveUpdateRefillData(){
    var isRefillTask = $("#isRefillTask").val();
	
	if(isRefillTask){
		submitMasterApply();
		/*
		var submitStrings =  getSubmitStrings();
		var bcReq = new BcRequest('headmaster','masterReviewAction','saveUpdateRefillData');
		bcReq.setExtraPs({
			    "option_tab_type":'workHistory',
			    "option_tab_values":submitStrings,
			    "businessKey":processBusinessKey
		});
		bcReq.setSuccFn(function(data,status){
			changeOption(23);
		});
		bcReq.postData();*/
	}else{
		submitRefillTask();
    }
}

function getSubmitStrings(){
	var submitArray = [];
	var punishmentRowNum = $("#punishmentRowNum").val();
	var situationObject = {
		"situation_id":$("#situation_id").val(),
		"situation_businessKey":$("#id").val(),
		"situation_hasSituation":$("#hasPunishment").val()
	}
	submitArray.push(situationObject);
	
	for(var i=0;i<punishmentRowNum;i++){
		var rowNum = (i+1);
		var id = $("#punishmentId"+rowNum).val();
		var implement_time = $("#implement_time"+rowNum).val();
		var description = $("#description"+rowNum).val();
		var people = $("#people"+rowNum).val();
		var department = $("#department"+rowNum).val();
		var process_result = $("#process_result"+rowNum).val();
		var businessKey = $("#id").val();
		var proveAttachId = $("#proveAttachId"+rowNum).val();
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
				"process_result":process_result,
				"proveAttachId":proveAttachId
		}
		submitArray.push(workExperienceObject);
	}
	return JSON.stringify(submitArray);
}

function headmasterBeforeSubmit(formJsonData){
	formJsonData.option_tab_type = "punishment"
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
	var hasPunishment = $("#hasPunishment").val();
	if (hasPunishment=='1') {//����ʾ����
		$("#punishmentRefill").hide();
		$("#addDiv").hide();
	} else if (hasPunishment=='2') { //��ʾ����
		$("#punishmentRefill").show();
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
			<h2><i>23</i>�������</h2>
			<p>��дУ�������ܵ����͡��������ֻ��������</p>
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
						<span class="fl">���޴��������</span>
						<select id="hasPunishment" onchange="showInfoFn()"></select>
					</div>
				</li>
			</ul>
		</div>
	</div>
	<!--  -->
	
	<!-- ��ְ���� s -->
	<div id="punishmentRefill" class="years" style="display: none">
		<input type="hidden" id="punishmentRowNum" name="punishmentRowNum" value="0">
	</div>
	<!-- ��ְ���� e -->
	
	<div id="addDiv" class="add" style="display: none"><a href="javascript:void(0);" onclick="addPunishmentSingle(this)" class="add-more">+</a></div>
	
	<div class="next-step clear-fix">
	  <a href="javascript:void(0);" target="_self" title="" class="fl" onclick="changeOption(22)">��һ��</a>
	  <a href="javascript:void(0);" target="_self" title="" class="fr" onclick="saveUpdateRefillData()">�ύ����</a>
	</div>
</body>
</html>