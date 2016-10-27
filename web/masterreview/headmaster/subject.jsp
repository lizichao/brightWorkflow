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

<!--课题情况  -->
<script id="subjectRec" type="text/x-jsrender">	
{{for Data}}
<div class='show-list radius-3 deleteDiv'>{{:#index+1}}<a href='#' target='_self' onclick="deleteSingleOption(this,'subjectType','{{:#index+1}}','{{:id}}');" class='del radius-3'>删除</a></div>
<div class="container">
	<ul class="clear-fix">
	<input type='hidden' id='subjectId{{:#index+1}}'  value='{{:id}}'>
	<input type='hidden' id='subjectAttachId{{:#index+1}}'  value='{{:subjectAttachVO.attachmentId}}'>
	<li><span class="fl">课题名称：</span>
        <div class="border_2 w_18 fl">
        	<input type='text' id='subject_name{{:#index+1}}' value='{{:subjectName}}' placeholder=''>
       	</div>
    </li>
	<li>
      <span class="fl">课题立项单位：</span>
       	<div class="border_2 w_19 fl">
       		<input type='text' id='subject_company{{:#index+1}}' value='{{:subjectCompany}}' >
       	</div>
    </li>
	<li>
       <span class="fl">课题级别：</span>
       <div class="border_2 w_18 fl">
         <select  id="subject_level{{:#index+1}}" class='select_left'>
                   {{for headmaster_approve_level}}
                     {{if id===#parent.parent.data.subjectLevel }}
				       <option value='{{:id}}' selected='selected'>{{:text}}</option>
				    {{else}}
				       <option value='{{:id}}' >{{:text}}</option>
				    {{/if}}
                    {{/for}}
          </select>
       </div>
   </li>

	<li>
		<span class="fl">是否结题：</span>
       	<div class="border_2 w_18 fl">
        <select  id='is_finish_subject{{:#index+1}}' class='select_left'>
	          {{for headmaster_is_finish_subject}}
                   {{if id==#parent.parent.data.isfinishSubject }}
				       	<option value='{{:id}}' selected='selected'>{{:text}}</option>
				    {{else}}
				       	<option value='{{:id}}' >{{:text}}</option>
				   {{/if}}
               {{/for}}
         </select>
       </div>
   </li>
	<li>
       <span class="fl">结题时间：</span>
       <div class="border_2 w_18 fl">
       <input type='text' id='finish_time{{:#index+1}}' value='{{timeCovert finishTime/}}' placeholder='' onclick='selectDeleteTime()'>
       </div>
    </li>
	<li>
       <span class="fl">获奖情况：</span>
       <div class="border_2 w_18 fl">
         <input type='text' id='finish_result{{:#index+1}}' value='{{:finishResult}}' placeholder=''>
       </div>
    </li>
	
	<li style='width:1000px'>
	  <span class='fl'>课题简介：</span>
	  <textarea maxlength='100' title='不得超过100字' name='subject_responsibility{{:#index+1}}' id='subject_responsibility{{:#index+1}}' class='fl deooration'>{{:subjectRresponsibility}}</textarea>
	</li>

    <li style='height:45px;' class='position_relative'>
		<span class='fl'>课题材料：</span>
      	<div id='subjectbutton{{:#index+1}}' class='position_upload_button_professional'></div>
    </li>
	<div class='attachments'>
		<div id='subjectbuttonDiv{{:#index+1}}' class='listInfo'>
			{{if subjectAttachVO.attachmentId !==null}}
         		<a class='chachu' href="<%=basePath%>WorkflowAttachMentDownload?attachmentId={{:subjectAttachVO.attachmentId}}">{{:subjectAttachVO.fileName}}</a>
         		&nbsp;&nbsp;&nbsp;&nbsp;
         		<a class='chachu'  href='javascript:void(0);' onclick='Headmaster.deleteReceiveFileAttachment("{{:subjectAttachVO.attachmentId}}",this);' >删除</a>
       		{{/if}}
		</div>
	</div>
	{{!--
    <div id='subjectbuttonDiv{{:#index+1}}' style='heigth:0px'>
       {{if subjectAttachVO.attachmentId !==null}}
         <a class='chachu' href="<%=basePath%>WorkflowAttachMentDownload?attachmentId={{:subjectAttachVO.attachmentId}}">{{:subjectAttachVO.fileName}}</a>
         &nbsp;&nbsp;&nbsp;&nbsp;
         <a class='chachu'  href='#' onclick='deleteReceiveFileAttachment({{:subjectAttachVO.attachmentId}},'subjectAttachId{{:#index+1}}',this);' >删除</a>
       {{/if}}
    </div>
    --}}
</ul>
</div>
{{/for}}
</script>

<script type="text/javascript">
$(function(){
});

function initSubjectData(masterReviewVO){
	bulidSubject(masterReviewVO.subjectVOs);
}

//课题
function bulidSubject(subjectVOs){
	$("#subjectRowNum").val(subjectVOs.length);
	if(subjectVOs.length>0){
		 var dataObject = {'Data': []};
		 for(var i =0;i<subjectVOs.length;i++){
			 //var headmaster_awards_types =  Brightcom.workflow.getSelectCombox('headmaster_awards_type');
			 //subjectVOs[i]['headmaster_awards_type'] = headmaster_awards_types;
			 var headmaster_approve_levels =  Brightcom.workflow.getSelectCombox('headmaster_approve_level');
			 subjectVOs[i]['headmaster_approve_level'] = headmaster_approve_levels;
			 var headmaster_is_finish_subjects =  Brightcom.workflow.getSelectCombox('headmaster_is_finish_subject');
			 subjectVOs[i]['headmaster_is_finish_subject'] = headmaster_is_finish_subjects;
			 
			 dataObject.Data.push(subjectVOs[i]);
		 }
		 var subTaskContent= $("#subjectRec").render(dataObject);
		 $("#subjectRefill").append(subTaskContent);
		
		 for(var i =0;i<subjectVOs.length;i++){
			 Headmaster.initWebUploader('subjectbutton',(i+1),'subjectType','点击上传','subjectAttachId','subjectbuttonDiv');
		 }
	}
	//$("#subjectRefill").append("<div class='add'><a class='add-more' href='javascript:void(0);' onclick='addSubjectSingle(this)' >+</a></div> ");
}

function addSubjectSingle(obj){
    
	var subjectRowNum = parseInt($("#subjectRowNum").val());
	var subjectRowNumNext = parseInt(subjectRowNum+1);

	var educationArray =[];
	//educationArray.push("<div class='show-list radius-3'>"+subjectRowNumNext+"、<a href='javascript:void(0);' target='_self' title='' onclick=\"deleteSingleOption(this,'subjectType',\'"+subjectRowNumNext+"\','');\" class='del radius-3'>删除</a></div>");
	educationArray.push("<input type='hidden' id='subjectId"+subjectRowNumNext+"'  value=''>");
	educationArray.push("<input type='hidden' id='subjectAttachId"+subjectRowNumNext+"'  value=''>");
	educationArray.push("<div class='show-list radius-3 deleteDiv'>"+subjectRowNumNext+"、<a href='javascript:void(0);' target='_self' title='' onclick=\"deleteSingleOption(this,'subjectType',\'"+subjectRowNumNext+"\','');\" class='del radius-3'>删除</a></div>");
	educationArray.push("<div class='container'>");
	educationArray.push("<ul class='clear-fix'>");
	
	educationArray.push("<li>");
	educationArray.push("<span class='fl'>课题名称：</span>");
	educationArray.push("<div class='border_2 w_18 fl'>");
	educationArray.push("<input type='text' id='subject_name"+subjectRowNumNext+"' value='' placeholder=''>");
	educationArray.push("</div>");
	educationArray.push("</li>");
	
	educationArray.push("<li>")
	educationArray.push("<span class='fl'>课题立项单位：</span>");
	educationArray.push("<div class='border_2 w_19 fl'>");
	educationArray.push("<input type='text' id='subject_company"+subjectRowNumNext+"' value='' > ");
	educationArray.push("</div>");
	educationArray.push("</li>");
	
	educationArray.push("<li>");
	educationArray.push("<span class='fl'>课题级别：</span>");
	educationArray.push("<div class='border_2 w_18 fl'>");
	educationArray.push("<select id='subject_level"+subjectRowNumNext+"' class='select_left'></select>");
	educationArray.push("</div>");
	educationArray.push("</li>");
	
	
	educationArray.push("<li>");
	educationArray.push("<span class='fl'>是否结题：</span>");
	educationArray.push("<div class='border_2 w_18 fl'>");
	educationArray.push("<select  id='is_finish_subject"+subjectRowNumNext+"' class='select_left'></select>");
	educationArray.push("</div>");
	educationArray.push("</li>");
	
	educationArray.push("<li>");
	educationArray.push("<span class='fl'>结题时间：</span>");
	educationArray.push("<div class='border_2 w_18 fl'>");
	educationArray.push("<input type='text' id='finish_time"+subjectRowNumNext+"' onclick='selectDeleteTime()' value='' placeholder=''>");
	educationArray.push("</div>");
	educationArray.push("</li>");
	
	educationArray.push("<li>");
	educationArray.push("<span class='fl'>获奖情况：</span>");
	educationArray.push("<div class='border_2 w_18 fl'>");
	educationArray.push("<input type='text' id='finish_result"+subjectRowNumNext+"' value='' placeholder=''>");
	educationArray.push("</div>");
	educationArray.push("</li>");
	
	
	
	educationArray.push("<li style='width:1000px'>");
	educationArray.push("<span class='fl'>课题简介：</span>");
	educationArray.push("<textarea title='不得超过100字' name='subject_responsibility"+subjectRowNumNext+"' id='subject_responsibility"+subjectRowNumNext+"' class='fl deooration' maxlength='100'></textarea>");
	educationArray.push("</li>");
	
	educationArray.push("<li style='height:45px;' class='position_relative'>");
	educationArray.push("<span class='fl'>课题材料：</span>");
	educationArray.push("<div id='subjectbutton"+subjectRowNumNext+"' class='position_upload_button_professional'></div>");
	educationArray.push("</li>");
	
	//educationArray.push("<div id='subjectbuttonDiv"+subjectRowNumNext+"' style='heigth:0px'></div>");
	
	
	educationArray.push("</ul>");
	
	educationArray.push("<div class='attachments'>");
	educationArray.push("<div id='subjectbuttonDiv"+subjectRowNumNext+"' class='listInfo'></div>");
	educationArray.push("</div>");
	
	educationArray.push("</div>");
	
	//educationArray.push("<div class='add'><a class='add-more' href='javascript:void(0);' onclick='addSubjectSingle(this)' >+</a></div>");
	
	//$(obj).after(educationArray.join(""));
	//$(obj).remove();
	$("#subjectRefill").append(educationArray.join(""));
	$("#subjectRowNum").val(subjectRowNumNext);
	
	Brightcom.workflow.initSelectCombox('headmaster_approve_level','subject_level'+(subjectRowNumNext));
	Brightcom.workflow.initSelectCombox('headmaster_is_finish_subject','is_finish_subject'+(subjectRowNumNext));
	/*
	 * 1、professionalTitlespan:上传按钮显示位置id
	 4、buttonName ： 按钮名称
	 5、hiddenAttachId：隐藏附件id，选中后把附件值保存在该隐藏域里
	 6、 hiddenDisplayId:附件显示的div id
	 */
	 Headmaster.initWebUploader('subjectbutton',subjectRowNumNext,'subjectType','点击上传','subjectAttachId','subjectbuttonDiv');
}

function saveUpdateRefillData(){
	var isRefillTask = $("#isRefillTask").val();
	if(isRefillTask){
		var submitStrings =  getSubmitStrings();
		var bcReq = new BcRequest('headmaster','masterReviewAction','saveUpdateRefillData');
		bcReq.setExtraPs({
		    "option_tab_type":'subject',
		    "option_tab_values":submitStrings,
		    "businessKey":processBusinessKey
		});
		bcReq.setSuccFn(function(data,status){
			changeOption(8);
		});
		bcReq.postData();
	}else{
		submitRefillTask();
	}
}

function getSubmitStrings(){
	var submitArray = [];
	var subjectRowNum = $("#subjectRowNum").val();
	for(var i=0;i<subjectRowNum;i++){
		var rowNum = (i+1);
		var id = $("#subjectId"+rowNum).val();
		var subject_name = $("#subject_name"+rowNum).val();
		var subject_company = $("#subject_company"+rowNum).val();
		var subject_level = $("#subject_level"+rowNum).val();
		var subject_responsibility = $("#subject_responsibility"+rowNum).val();
		var is_finish_subject = $("#is_finish_subject"+rowNum).val();
		var finish_result = $("#finish_result"+rowNum).val();
		var finish_time = $("#finish_time"+rowNum).val();
		var subjectAttachmentId = $("#subjectAttachId"+rowNum).val();
		var businessKey = $("#id").val();
		if(!subject_name){
			continue;
		}
		var workExperienceObject = {
				"id":id,
				"businessKey":$("#id").val(),
				"subject_name":subject_name,
				"subject_company":subject_company,
				"subject_level":subject_level,
				"subject_responsibility":subject_responsibility,
				"is_finish_subject":is_finish_subject,
				"finish_result":finish_result,
				'finish_time' :finish_time,
				'subjectAttachmentId' :subjectAttachmentId,
		}
		submitArray.push(workExperienceObject);
	}
	return JSON.stringify(submitArray);
}

function headmasterBeforeSubmit(formJsonData){
	formJsonData.option_tab_type = "subject"
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
			<h2><i>7</i>教科研情况</h2>
			<p>填写个人课题情况。</p>
		</div>
		<div class="select-step fr"><a href="javascript:void(0);" target="_self" title="" id="change">+&nbsp;切换步骤</a></div>
		<div class="clear-both"></div>
	</div>
	<!-- 标题 e -->
	<!-- 任职年限 s -->
	<div id="subjectRefill" class="paper">
			<input type="hidden" id="subjectRowNum" name="subjectRowNum" value="0" >
	</div>
	
	<!--  -->
	<div class="addDiv"><a href="javascript:void(0);" onclick="addSubjectSingle(this)" class="add-more">+</a></div>
	
	
	<!-- 任职年限 e -->
	<div class="next-step clear-fix">
	  <a href="javascript:void(0);" target="_self" title="" class="fl" onclick="changeOption(6)">上一步</a>
	  <a href="javascript:void(0);" target="_self" title="" class="fr" onclick="saveUpdateRefillData()">下一步</a>
	</div>
</body>
</html>