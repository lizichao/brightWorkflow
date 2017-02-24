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
<title>深圳市校长职级评审系统</title>

<script id="accidentRec" type="text/x-jsrender">	
{{for Data}}
    <div class='show-list radius-3'>{{:#index+1}}<a href='javascript:void(0);' target='_self'  onclick="deleteSingleOption(this,'accident','{{:#index+1}}','{{:id}}');" class='del radius-3'>删除</a></div>
   <div class="container">
      <input type='hidden' id="accidentId{{:#index+1}}"  value='{{:id}}'>
     <input type='hidden' id='proveAttachId{{:#index+1}}'  value='{{:proveAttachMentVO.attachmentId}}'>
	<ul class="clear-fix">
	<li>
        <span class="fl">责任事故名称：</span>
        <div class="border_2 w_23 fl">
          <input type="text" id='accident_name{{:#index+1}}' value='{{:accident_name}}' placeholder="请输入责任事故名称" />
        </div>
    </li>
	<li>
         <span class="fl">违纪描述：</span>
         <div class="border_2 w_13 fl">
          <input type="text" id='description{{:#index+1}}' value='{{:description}}' placeholder="请输入违纪描述" />
         </div>
    </li>
	<li>
      <span class="fl">时间：</span>
      <div class="border_2 w_20 fl">
       <input type="text" id='implement_time{{:#index+1}}' value='{{timeCovert implement_time/}}' onclick='selectDeleteTime()'  placeholder="请输入时间" />
      </div>
    </li>
	<li>
      <span class="fl">处理结果：</span>
       <textarea title='不得超过100字' name='process_result{{:#index+1}}' id='process_result{{:#index+1}}' style='width:334px;' class='fl deooration' onkeydown='countChar(this);' onkeyup='countChar(this);'>{{:process_result}}</textarea>
   	   <p name='text-prompt' style='color:#999;text-align:right;'><span name='number' style='padding:0px 0px;'>{{if process_result && process_result.length}}{{:process_result.length}}{{else}}0{{/if}}</span>/100&nbsp;</p> 
   </li>

   <li style='height:45px;' class='position_relative'>
      <span class="fl">证明材料：</span>
      <div id='accident_upload_div{{:#index+1}}' class='position_upload_button_professional'></div>
    </li>

    <div id='accident_div{{:#index+1}}' class='only_attachments'>
       {{if proveAttachMentVO.attachmentId !==null}}
         <a class='chachu' href="<%=basePath%>WorkflowAttachMentDownload?attachmentId={{:proveAttachMentVO.attachmentId}}">{{:proveAttachMentVO.fileName}}</a>
         &nbsp;&nbsp;&nbsp;&nbsp;
         <a class='chachu'  href='javascript:void(0);' onclick='Headmaster.deleteReceiveFileAttachment("{{:proveAttachMentVO.attachmentId}}",this);' >删除</a>
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

//任职年限
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
			 Headmaster.initWebUploader('accident_upload_div',(i+1),'accident_type','点击上传','proveAttachId','accident_div');
		 }
		 
	}
	//$("#accidentRefill").append("<div class='add'><a class='add-more' href='javascript:void(0);' onclick='addAccidentSingle(this)' >+</a></div> ");
	//$('select').selectOrDie();
}

function addAccidentSingle(obj){
	var accidentRowNum = parseInt($("#accidentRowNum").val());
	var accidentRowNumNext = parseInt(accidentRowNum+1);

	var educationArray =[];
	educationArray.push("<div class='show-list radius-3'>"+accidentRowNumNext+"、<a href='javascript:void(0);' target='_self' onclick=\"deleteSingleOption(this,'accident',\'"+accidentRowNumNext+"\','');\" class='del radius-3'>删除</a></div>");
	educationArray.push("<div class='container'>");
	educationArray.push("<ul  class='clear-fix'>");
	
	educationArray.push("<input type='hidden' id='accidentId"+accidentRowNumNext+"'  value=''>");
	educationArray.push("<input type='hidden' id='proveAttachId"+accidentRowNumNext+"'  value=''>");
	
	educationArray.push("<li><span class='fl'>责任事故名称：</span>");
	educationArray.push("<div class='border_2 w_23 fl'>");
	educationArray.push("<input type='text' id='accident_name"+accidentRowNumNext+"' value='' placeholder='请输入责任事故名称'>");
	educationArray.push("</div>");
	educationArray.push("</li>");
	
	educationArray.push("<li><span class='fl'>违纪描述：</span>");
	educationArray.push("<div class='border_2 w_13 fl'>");
	educationArray.push("<input type='text' id='description"+accidentRowNumNext+"' value='' placeholder='请输入违纪描述'>");
	educationArray.push("</div>");
	educationArray.push("</li>");
	
	educationArray.push("<li><span class='fl'>时间：</span>");
	educationArray.push("<div class='border_2 w_20 fl'>");
	educationArray.push("<input type='text' id='implement_time"+accidentRowNumNext+"'   onclick='selectDeleteTime()'  placeholder='请输入时间' />");
	educationArray.push("</div>");
	educationArray.push("</li>");
	
	educationArray.push("<li><span class='fl'>处理结果：</span>");
	educationArray.push("<textarea title='不得超过100字' name='process_result"+accidentRowNumNext+"'  id='process_result"+accidentRowNumNext+"' style='width:334px;'  class='fl deooration' placeholder='请输入处理结果' onkeydown='countChar(this);' onkeyup='countChar(this);'></textarea>");
	educationArray.push("<p name='text-prompt' style='color:#999;text-align:right;'><span name='number' style='padding:0px 0px;'>0</span>/100&nbsp;</p>");
	educationArray.push("</li>");
	
	educationArray.push("<li style='height:45px;' class='position_relative'>");
	educationArray.push(" <span class='fl'>证明材料：</span>");
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
	 * 1、professionalTitlespan:上传按钮显示位置id
	 4、buttonName ： 按钮名称
	 5、hiddenAttachId：隐藏附件id，选中后把附件值保存在该隐藏域里
	 6、 hiddenDisplayId:附件显示的div id
	 */
	 Headmaster.initWebUploader('accident_upload_div',accidentRowNumNext,'accident_type','点击上传','proveAttachId','accident_div');
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

function countChar(curObj) {//计算字数
	var maxLength = 100;//100个字符
	var $curObj;
	if (curObj instanceof jQuery) {
		$curObj = curObj;
	} else {
		$curObj = $(curObj);
	}
	$curObj = $(curObj);
	var $textPrompt = $curObj.siblings('p[name="text-prompt"]');
	if ($textPrompt[0]) {//判断是否存在
		var $numberSpan = $textPrompt.find("span[name='number']");
		if ($numberSpan[0]) {//判断是否存在
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
	if (hasAccident=='1') {//不显示数据
		$("#accidentRefill").hide();
		$("#addDiv").hide();
	} else if (hasAccident=='2') { //显示数据
		$("#accidentRefill").show();
		$("#addDiv").show();
	}
}
</script>

</head>
<body>
<!-- Main Start -->
	<!-- 进度 s -->
	<div class="grogress"><div class="line" style="width:92px;"><!-- 970/21 --></div></div>
	<!-- 进度 e -->
	<!-- 标题 s -->
	<div class="com-title">
		<div class="txt fl">
			<h2><i>22</i>责任事故情况</h2>
			<p>填写学校出现安全责任事故，或出现严重违纪现象，受到上级通报批评以上处理。</p>
		</div>
		<div class="select-step fr"><a href="javascript:void(0);" target="_self" title="" id="change">+&nbsp;切换步骤</a></div>
		<div class="clear-both"></div>
	</div>
	<!-- 标题 e -->
	<!--   -->
	<div class="years">
		<div class="container">
			<ul class="clear-fix">
				<li>
					<div class="border_2" style="width: 180px;margin: 0 auto;">
						<input type="hidden" id="situation_id"/>
						<span class="fl">有无责任事故情况：</span>
						<select id="hasAccident" onchange="showInfoFn()"></select>
					</div>
				</li>
			</ul>
		</div>
	</div>
	<!--  -->
	<!-- 任职年限 s -->
	<div id="accidentRefill" class="years" style="display: none">
		<input type="hidden" id="accidentRowNum" name="accidentRowNum" value="0">
	</div>
	<!-- 任职年限 e -->
	
	<div id="addDiv" class="add" style="display: none"><a href="javascript:void(0);" onclick="addAccidentSingle(this)" class="add-more">+</a></div>
	
	<div class="next-step clear-fix">
	   <a href="javascript:void(0);" target="_self" title="" class="fl" onclick="changeOption(21)">上一步</a>
	   <a href="javascript:void(0);" target="_self" title="" class="fr" onclick="saveUpdateRefillData()">下一步</a>
	</div>
</body>
</html>