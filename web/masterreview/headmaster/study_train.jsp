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

<!--学校获奖情况  -->
<script id="studyTrainRec" type="text/x-jsrender">	
{{for Data}}
    <div class='show-list radius-3'>{{:#index+1}}<a href='javascript:void(0);' target='_self'  onclick="deleteSingleOption(this,'studyTrain','{{:#index+1}}','{{:id}}');" class='del radius-3'>删除</a></div>
	 <div class="container">
    <input type='hidden' id='studyTrainId{{:#index+1}}'  value='{{:id}}'>
	<input type='hidden' id="proveId{{:#index+1}}"  value='{{:proveAttachMentVO.attachmentId}}'>
	<ul class="clear-fix">
	<li>
        <span class="fl">起止年月：</span>		
        <div class="border_2 w_12 fl">
          <input type="text" id='start_date{{:#index+1}}' onclick='selectDeleteTime()' value="{{timeCovert start_date/}}"/>
        </div>
		<span class="fl">至</span>
		<div class="border_2 w_12 fl">
          <input type="text" id='end_date{{:#index+1}}'  onclick='selectDeleteTime()' value="{{timeCovert end_date/}}" />
        </div>
	</li>

    <li>
		<span class='fl'>学习进修课题名称：</span>
		<div class='border_2 w_29 fl'>
			<input type='text' id='title{{:#index+1}}' value='{{:title}}' placeholder='请输入学习进修课题名称'>
		</div>
	</li>

	<li>
		<span class='fl' style='position:relative;'>学习进修主要内容：<span style='position:absolute;left:-2px;top:30px;width:140px;'>（不得超过100字）</span></span>
		<textarea title='不得超过100字' id='content{{:#index+1}}' style='width:278px;' class='fl deooration' onkeydown='countChar(this);' onkeyup='countChar(this);'>{{:content}}</textarea>
		<p name='text-prompt' style='color:#999;text-align:right;'><span name='number' style='padding:0px 0px;'>{{if content && content.length}}{{:content.length}}{{else}}0{{/if}}</span>/100&nbsp;</p>
	</li>

	<li>
     <span class='fl'>学时：</span>
     <div class="border_2 w_20 fl">
		<input type='text' id='class_hour{{:#index+1}}' value='{{:class_hour}}' placeholder=''>
     </div>
    </li>

	<li>
     <span class='fl'>学习地点：</span>
     <div class="border_2 w_13 fl">
		<input type='text' id='study_place{{:#index+1}}' value='{{:study_place}}' placeholder=''>
     </div>
    </li>


	<li>
     <span class='fl'>主办单位：</span>
     <div class="border_2 w_13 fl">
		<input type='text' id='organizers{{:#index+1}}' value='{{:organizers}}' placeholder=''>
     </div>
    </li>
	<li style='height:45px;' class='position_relative'>
     	<span class="fl">证明材料：</span>
     	<div style='padding-left:4px;' id='spanButtonPlaceHolder{{:#index+1}}' class='position_upload_button_professional'></div> 
	</li>
    <div id='studyTrainDiv{{:#index+1}}' class='only_attachments'>
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
 
function initStudyTrainData(masterReviewVO){
	bulidstudyTrain(masterReviewVO.studyTrainVOs);
}

//学校获奖
function bulidstudyTrain(studyTrainVOs){
	$("#studyTrainRowNum").val(studyTrainVOs.length);
	if(studyTrainVOs.length>0){
		 var dataObject = {'Data': []};
		 for(var i =0;i<studyTrainVOs.length;i++){
			// var headmaster_approve_levels =  Brightcom.workflow.getSelectCombox('headmaster_approve_level');
			// studyTrainVOs[i]['headmaster_approve_level'] = headmaster_approve_levels;
			 
			 dataObject.Data.push(studyTrainVOs[i]);
		 }
		 //debugger;
		 var subTaskContent= $("#studyTrainRec").render(dataObject);
		 $("#studyTrainRefill").append(subTaskContent);
		
		for(var i =0;i<studyTrainVOs.length;i++){
			Headmaster.initWebUploader('spanButtonPlaceHolder',(i+1),'studyTrain','点击上传','proveId','studyTrainDiv');
		}
	}
	//$("#studyTrainRefill").append("<div class='add'><a class='add-more' href='javascript:void(0);' onclick='addstudyTrainSingle(this)' >+</a></div> ");
}


function addstudyTrainSingle(obj){
	var studyTrainRowNum = parseInt($("#studyTrainRowNum").val());
	var studyTrainRowNumNext = parseInt(studyTrainRowNum+1);

	var educationArray =[];
	educationArray.push("<div class='show-list radius-3'>"+studyTrainRowNumNext+"、<a href='javascript:void(0);' target='_self' onclick=\"deleteSingleOption(this,'studyTrain',\'"+studyTrainRowNumNext+"\','');\" class='del radius-3'>删除</a></div>");
	educationArray.push("<div class='container'>");
	educationArray.push("<ul id='' class='clear-fix'>");
	//educationArray.push("<div id='workExperienceHead"+studyTrainRowNumNext+"' class='tit2'><span><a href='javascript:void(0);' class='tit2-trigger' onclick=\"toggleDiv(this);\">收起</a>");
	//educationArray.push("<a href='javascript:void(0);' onclick=\"deleteSingleOption(this,'workExperienceType',\'"+studyTrainRowNumNext+"\','');\" >删除</a></span></div>");
	
	//educationArray.push("<div class='tab-pane-sub'>");
	educationArray.push("<input type='hidden' id='studyTrainId"+studyTrainRowNumNext+"'  value=''>");
	educationArray.push("<input type='hidden' id='proveId"+studyTrainRowNumNext+"' value=''>");
	//educationArray.push("<ul>");
	educationArray.push("<li><span class='fl'>起止年月：</span>");
	educationArray.push("<div class='border_2 w_12 fl'>");
	educationArray.push("<input type='text' id='start_date"+studyTrainRowNumNext+"' name='start_date"+studyTrainRowNumNext+"' onclick='selectDeleteTime()'/>");
	educationArray.push("</div>");
	educationArray.push("<span class='fl'>至</span>");
	educationArray.push("<div class='border_2 w_12 fl'>");
	educationArray.push("<input type='text' id='end_date"+studyTrainRowNumNext+"' name='end_date"+studyTrainRowNumNext+"' onclick='selectDeleteTime()'/>");
	educationArray.push("</div>");
	educationArray.push("</li>");
	
	educationArray.push("<li><span class='fl'>学习进修课题名称：</span>");
	educationArray.push("<div class='border_2 w_29 fl'>");
	educationArray.push("<input type='text' id='title"+studyTrainRowNumNext+"' value='' placeholder='请输入学习进修课题名称'>");
	educationArray.push("</div>");
	educationArray.push("</li>");
	
	educationArray.push("<li><span class='fl' style='position:relative;'>学习进修主要内容：<span style='position:absolute;left:-2px;top:30px;width:140px;'>（不得超过100字）</span></span>");
	educationArray.push("<textarea title='不得超过100字' id='content"+studyTrainRowNumNext+"' style='width:278px;' class='fl deooration' onkeydown='countChar(this);' onkeyup='countChar(this);'></textarea>");
	educationArray.push("<p name='text-prompt' style='color:#999;text-align:right;'><span name='number' style='padding:0px 0px;'>0</span>/100&nbsp;</p>");
	educationArray.push("</li>");
	
	educationArray.push("<li><span class='fl'>学时：</span>");
	educationArray.push("<div class='border_2 w_20 fl'>");
	educationArray.push("<input type='text' id='class_hour"+studyTrainRowNumNext+"' value='' placeholder='请输入学时'>");
	educationArray.push("</div>");
	educationArray.push("</li>");
	
	
	educationArray.push("<li><span class='fl'>学习地点：</span>");
	educationArray.push("<div class='border_2 w_13 fl'>");
	educationArray.push("<input type='text' id='study_place"+studyTrainRowNumNext+"' value='' placeholder='请输入学习地点'>");
	educationArray.push("</div>");
	educationArray.push("</li>");
	
	
	educationArray.push("<li><span class='fl'>主办单位：</span>");
	educationArray.push("<div class='border_2 w_13 fl'>");
	educationArray.push("<input type='text' id='organizers"+studyTrainRowNumNext+"' value='' placeholder='请输入主办单位'>");
	educationArray.push("</div>");
	educationArray.push("</li>");
	
	educationArray.push("<li style='height:45px;' class='position_relative'>");
	educationArray.push("<span class='fl'>证明材料：</span><div id='spanButtonPlaceHolder"+studyTrainRowNumNext+"'  class='position_upload_button_professional'></div> ");
	educationArray.push("</li>");
	
	educationArray.push("<div id='studyTrainDiv"+studyTrainRowNumNext+"' class='only_attachments'></div>");
	
	educationArray.push("</ul>");
	educationArray.push("</div>");
	
	//educationArray.push("<div class='add'><a class='add-more' href='javascript:void(0);' onclick='addWorkExperienceSingle(this)' >+</a></div>");
	
	//$(obj).after(educationArray.join(""));
	//$(obj).remove();
	
	$("#studyTrainRefill").append(educationArray.join(""));
	
	$("#studyTrainRowNum").val(studyTrainRowNumNext);
	
	Headmaster.initWebUploader('spanButtonPlaceHolder',studyTrainRowNumNext,'studyTrain','点击上传','proveId','studyTrainDiv');
}

function saveUpdateRefillData(){
	var isRefillTask = $("#isRefillTask").val();
	if(isRefillTask){
		var submitStrings =  getSubmitStrings();
		var bcReq = new BcRequest('headmaster','masterReviewAction','saveUpdateRefillData');
		bcReq.setExtraPs({ 
			"option_tab_type":'studyTrain',
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
	var studyTrainRowNum = $("#studyTrainRowNum").val();
	for(var i=0;i<studyTrainRowNum;i++){
		var rowNum = (i+1);
		var id = $("#studyTrainId"+rowNum).val();
		var start_date = $("#start_date"+rowNum).val();
		var end_date = $("#end_date"+rowNum).val();
		var title = $("#title"+rowNum).val();
		var content = $("#content"+rowNum).val();
		var class_hour = $("#class_hour"+rowNum).val();
		var study_place = $("#study_place"+rowNum).val();
		var organizers = $("#organizers"+rowNum).val();
		var proveAttachMentId = $("#proveId"+rowNum).val();
		var businessKey = $("#id").val();
		if(!content){
			continue;
		}
		var workExperienceObject = {
				"id":id,
				"businessKey":$("#id").val(),
				"start_date":start_date,
				"end_date":end_date,
				"title":title,
				"content":content,
				"class_hour":class_hour,
				"study_place":study_place,
				"organizers":organizers,
				"proveAttachMentId":proveAttachMentId,
		}
		submitArray.push(workExperienceObject);
	}
	return JSON.stringify(submitArray);
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
function headmasterBeforeSubmit(formJsonData){
	formJsonData.option_tab_type = "studyTrain"
	formJsonData.option_tab_values = getSubmitStrings();
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
			<h2><i>10</i>进修学习</h2>
			<p>积极参加市、区教育部门组织的国内外教育考察、培训，符合继续教育的课时与质量。</p>
		</div>
		<div class="select-step fr"><a href="javascript:void(0);" target="_self" title="" id="change">+&nbsp;切换步骤</a></div>
		<div class="clear-both"></div>
	</div>
	<!-- 标题 e -->
	<!-- 任职年限 s -->
	<div id="studyTrainRefill" class="years">
		<input type="hidden" id="studyTrainRowNum" name="studyTrainRowNum" value="0">
	</div>
	
	<div class="add"><a href="javascript:void(0);" onclick="addstudyTrainSingle(this)" class="add-more">+</a></div>
	
	
	<!-- 任职年限 e -->
	<div class="next-step clear-fix">
	   <a href="javascript:void(0);" target="_self" title="" class="fl" onclick="changeOption(9)">上一步</a>
	   <a href="javascript:void(0);" target="_self" title="" class="fr" onclick="saveUpdateRefillData()">下一步</a>
	</div>

</body>
</html>