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

<!--任职年限  -->
<script id="gradeEvaluateRec" type="text/x-jsrender">	
{{for Data}}
<div class='show-list radius-3'>{{:#index+1}}<a href='javascript:void(0);' target='_self' onclick="deleteSingleOption(this,'gradeEvaluate','{{:#index+1}}','{{:id}}');" class='del radius-3'>删除</a></div>
 <div class="container">
	<ul class="clear-fix">
	 <input type='hidden' id="gradeEvaluateId{{:#index+1}}"  value='{{:id}}'>
    <input type='hidden' id='proveAttachId{{:#index+1}}'  value='{{:proveAttachMentVO.attachmentId}}'>

	<li>
     <span class='fl'>义务教育：</span>
     <div class="border_2 w_13 fl">
      <input type='text' id='compulsory_education{{:#index+1}}' value='{{:compulsory_education}}' placeholder=''>
     </div>
    </li>

	<li>
     <span class='fl'>担任职务：</span>
     <div class="border_2 w_13 fl">
       <input type='text' id='high_school{{:#index+1}}' value='{{:high_school}}' placeholder=''>
     </div>
    </li>

	<li>
       <span class='fl'> 中职：</span>
       <div class="border_2 w_20 fl">
        <input type='text' id='secondary_school{{:#index+1}}' value='{{:secondary_school}}' placeholder=''>
      </div>
    </li>

    <li style='height:45px;' class='position_relative'>
      <span class="fl">证明材料：</span>
      <div id='grade_evaluate_upload_div{{:#index+1}}' class='position_upload_button_professional'></div>
    </li>

    <div id='grade_evaluate_div{{:#index+1}}' class='only_attachments'>
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

function initGradeEvaluateData(masterReviewVO){
	bulidGradeEvaluate(masterReviewVO.gradeEvaluateVOs);
}

//任职年限
function bulidGradeEvaluate(gradeEvaluateVOs){
	$("#gradeEvaluateRowNum").val(gradeEvaluateVOs.length);
	if(gradeEvaluateVOs.length>0){
		 var dataObject = {'Data': []};
		 for(var i =0;i<gradeEvaluateVOs.length;i++){
			 dataObject.Data.push(gradeEvaluateVOs[i]);
		 }
		 var subTaskContent= $("#gradeEvaluateRec").render(dataObject);
		 $("#gradeEvaluateRefill").append(subTaskContent);
		 
		 
		 for(var i =0;i<gradeEvaluateVOs.length;i++){
			 Headmaster.initWebUploader('grade_evaluate_upload_div',(i+1),'grade_evaluate_type','点击上传','proveAttachId','grade_evaluate_div');
		 }
	}
	//$("#gradeEvaluateRefill").append("<div class='add'><a class='add-more' href='javascript:void(0);' onclick='addGradeEvaluateSingle(this)' >+</a></div> ");
}

function addGradeEvaluateSingle(obj){
	var gradeEvaluateRowNum = parseInt($("#gradeEvaluateRowNum").val());
	var gradeEvaluateRowNumNext = parseInt(gradeEvaluateRowNum+1);

	var educationArray =[];
	educationArray.push("<div class='show-list radius-3'>"+gradeEvaluateRowNumNext+"、<a href='javascript:void(0);' target='_self' onclick=\"deleteSingleOption(this,'gradeEvaluate',\'"+gradeEvaluateRowNumNext+"\','');\" class='del radius-3'>删除</a></div>");
	educationArray.push("<div class='container'>");
	educationArray.push("<ul class='clear-fix'>");
	educationArray.push("<input type='hidden' id='gradeEvaluateId"+gradeEvaluateRowNumNext+"'  value=''>");
	educationArray.push("<input type='hidden' id='proveAttachId"+gradeEvaluateRowNumNext+"'  value=''>");
	
	educationArray.push("<li>");
	educationArray.push("<span class='fl'>义务教育：</span>");
	educationArray.push("<div class='border_2 w_13 fl'>");
	educationArray.push("<input type='text' id='compulsory_education"+gradeEvaluateRowNumNext+"' value='' placeholder=''>");
	educationArray.push("</div>");
	educationArray.push("</li>");
	
	educationArray.push("<li>");
	educationArray.push("<span class='fl'>高中：</span>");
	educationArray.push("<div class='border_2 w_20 fl'>");
	educationArray.push("<input type='text' id='high_school"+gradeEvaluateRowNumNext+"' value='' placeholder=''>");
	educationArray.push("</div>");
	educationArray.push("</li>");
	
	educationArray.push("<li>");
	educationArray.push("<span class='fl'>中职：</span>");
	educationArray.push("<div class='border_2 w_20 fl'>");
	educationArray.push("<input type='text' id='secondary_school"+gradeEvaluateRowNumNext+"' value='' placeholder=''>");
	//educationArray.push("<select name='' id='secondary_school"+gradeEvaluateRowNumNext+"' class='select_left'><option value='2016' >2016</option></select>");
	educationArray.push("</div>");
	educationArray.push("</li>");
	
	educationArray.push("<li style='height:45px;' class='position_relative'>");
	educationArray.push(" <span class='fl'>证明材料：</span>");
	educationArray.push(" <div id='grade_evaluate_upload_div"+gradeEvaluateRowNumNext+"' class='position_upload_button_professional'></div>");
	educationArray.push("</li>");
	
	
	educationArray.push("<div id='grade_evaluate_div"+gradeEvaluateRowNumNext+"' class='only_attachments'></div>");
	
	
	educationArray.push("</ul>");
	educationArray.push("</div>");
	
	//educationArray.push("<div class='add'><a class='add-more' href='javascript:void(0);' onclick='addGradeEvaluateSingle(this)' >+</a></div>");
	
	
	//$(obj).after(educationArray.join(""));
	//$(obj).remove();
	
	$("#gradeEvaluateRefill").append(educationArray.join(""));
	
	$("#gradeEvaluateRowNum").val(gradeEvaluateRowNumNext);
	
	/*
	 * 1、professionalTitlespan:上传按钮显示位置id
	 4、buttonName ： 按钮名称
	 5、hiddenAttachId：隐藏附件id，选中后把附件值保存在该隐藏域里
	 6、 hiddenDisplayId:附件显示的div id
	 */
	 Headmaster.initWebUploader('grade_evaluate_upload_div',gradeEvaluateRowNumNext,'grade_evaluate_type','点击上传','proveAttachId','grade_evaluate_div');
}

function saveUpdateRefillData(){
	var isRefillTask = $("#isRefillTask").val();
	if(isRefillTask){
		var submitStrings =  getSubmitStrings();
		var bcReq = new BcRequest('headmaster','masterReviewAction','saveUpdateRefillData');
		bcReq.setExtraPs({
			    "option_tab_type":'gradeEvaluate',
			    "option_tab_values":submitStrings,
			    "businessKey":processBusinessKey
		});
		bcReq.setSuccFn(function(data,status){
			changeOption(20);
		});
		bcReq.postData();
	}else{
		submitRefillTask();
	}
}

function getSubmitStrings(){
	var submitArray = [];
	var gradeEvaluateRowNum = $("#gradeEvaluateRowNum").val();
	for(var i=0;i<gradeEvaluateRowNum;i++){
		var rowNum = (i+1);
		var id = $("#gradeEvaluateId"+rowNum).val();
		var compulsory_education = $("#compulsory_education"+rowNum).val();
		var high_school = $("#high_school"+rowNum).val();
		var secondary_school = $("#secondary_school"+rowNum).val();
		var businessKey = $("#id").val();
		var proveAttachId = $("#proveAttachId"+rowNum).val();
		if(!compulsory_education){
			continue;
		}
		var workExperienceObject = {
				"id":id,
				"compulsory_education":compulsory_education,
				"businessKey":$("#id").val(),
				"high_school":high_school,
				"secondary_school":secondary_school,
				"proveAttachId":proveAttachId
		}
		submitArray.push(workExperienceObject);
	}
	return JSON.stringify(submitArray);
}

function headmasterBeforeSubmit(formJsonData){
	formJsonData.option_tab_type = "gradeEvaluate"
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
			<h2><i>19</i>学校发展（学校等级评估）</h2>
			<p>填写义务教育标准化学校评估情况。</p>
		</div>
		<div class="select-step fr"><a href="javascript:void(0);" target="_self" title="" id="change">+&nbsp;切换步骤</a></div>
		<div class="clear-both"></div>
	</div>
	<!-- 标题 e -->
	<!-- 任职年限 s -->
	<div id="gradeEvaluateRefill" class="years">
			<input type="hidden" id="gradeEvaluateRowNum" name="gradeEvaluateRowNum" value="0">
	</div>
	
	<div class="add"><a href="javascript:void(0);" onclick="addGradeEvaluateSingle(this)" class="add-more">+</a></div>
	
	<!-- 任职年限 e -->
	<div class="next-step clear-fix">
	  <a href="javascript:void(0);" target="_self" title="" class="fl" onclick="changeOption(18)">上一步</a>
	  <a href="javascript:void(0);" target="_self" title="" class="fr"  onclick="saveUpdateRefillData()">下一步</a>
</body>
</html>