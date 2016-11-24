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

<!--个人获奖情况  -->
<script id="personalAwardRec" type="text/x-jsrender">	
{{for Data}}
    <div class='show-list radius-3'>{{:#index+1}}<a href='javascript:void(0);' target='_self' onclick="deleteSingleOption(this,'personalAwardType','{{:#index+1}}','{{:id}}');"  class='del radius-3'>删除</a></div>
   <div class="container">
    <input type='hidden' id='personalAwardId{{:#index+1}}'  value='{{:id}}'>
	<input type='hidden' id='awards_attachment_id_personal{{:#index+1}}'  value='{{:personalAttachVO.attachmentId}}'>
    <input type='hidden' id='awards_attachment_id_personal1{{:#index+1}}'  value='{{:personalAttachVO1.attachmentId}}'>
	<ul class="clear-fix">
	<li>
      <span class='fl'>奖项名称：</span>
      <div class="border_2 w_13 fl">
        <input type='text' id='awards_name_personal{{:#index+1}}' value='{{:awardsName}}' placeholder=''>
      </div>
    </li>
	<li>
       <span class='fl'>表彰单位：</span>
       <div class="border_2 w_13 fl">
         <input type='text' id='awards_company_personal{{:#index+1}}' value='{{:awardsCompany}}' placeholder=''>
       </div>
    </li>
	<li>  
      <span class='fl'>获奖级别：</span>
       <div class="border_2 w_13 fl">
          <select id="awards_level_personal{{:#index+1}}" class='select_left'>
                   {{for headmaster_approve_level}}
                     {{if id===#parent.parent.data.awardsLevel }}
				       <option value='{{:id}}' selected='selected'>{{:text}}</option>
				    {{else}}
				       <option value='{{:id}}' >{{:text}}</option>
				    {{/if}}
                    {{/for}}
          </select>
      </div>
    </li>
	<li> 
       <span class='fl'>表彰时间：</span>
       <div class="border_2 w_13 fl">
        <input type='text' id='awards_time_personal{{:#index+1}}' value='{{timeCovert awardsTime/}}'  onclick='selectDeleteTime()'>
      </div>
    </li>


   <li>
      <span class='fl'>奖项类别1：</span>
      <div class="border_2 w_28 fl">
        <select id="awards_type{{:#index+1}}" class='select_left'>
                   {{for headmaster_awards_type}}
                     {{if id===#parent.parent.data.awards_type }}
				       <option value='{{:id}}' selected='selected'>{{:text}}</option>
				    {{else}}
				       <option value='{{:id}}' >{{:text}}</option>
				    {{/if}}
                    {{/for}}
          </select>
      </div>
    </li>
	
    <li class='position_relative'>
      <span class='fl'>获奖证书1：</span> 
      <div id='personalAwardUpload{{:#index+1}}' class='position_upload_button_professional'></div> 
    </li>

	<div id='personalAwardTypeDiv{{:#index+1}}' class='only_attachments'>
      {{if personalAttachVO.attachmentId !==null}}
         <a class='chachu' href="<%=basePath%>WorkflowAttachMentDownload?attachmentId={{:personalAttachVO.attachmentId}}">{{:personalAttachVO.fileName}}</a>
         &nbsp;&nbsp;&nbsp;&nbsp;
         <a class='chachu'  href='javascript:void(0);' onclick='Headmaster.deleteReceiveFileAttachment("{{:personalAttachVO.attachmentId}}",this);' >删除</a>
       {{/if}}
    </div>



     <li>
      <span class='fl'>奖项类别2：</span>
      <div class="border_2 w_28 fl">
        <select id="awards_type1{{:#index+1}}" class='select_left'>
                   {{for headmaster_awards_type}}
                     {{if id===#parent.parent.data.awards_type1 }}
				       <option value='{{:id}}' selected='selected'>{{:text}}</option>
				    {{else}}
				       <option value='{{:id}}' >{{:text}}</option>
				    {{/if}}
                    {{/for}}
          </select>
      </div>
    </li>
	
    <li class='position_relative'>
      <span class='fl'>获奖证书2：</span> 
      <div id='personalAwardUpload1{{:#index+1}}' class='position_upload_button_professional'></div> 
    </li>

	<div id='personalAwardTypeDiv1{{:#index+1}}' class='only_attachments'>
      {{if personalAttachVO1.attachmentId !==null}}
         <a class='chachu' href="<%=basePath%>WorkflowAttachMentDownload?attachmentId={{:personalAttachVO1.attachmentId}}">{{:personalAttachVO1.fileName}}</a>
         &nbsp;&nbsp;&nbsp;&nbsp;
         <a class='chachu'  href='javascript:void(0);' onclick='Headmaster.deleteReceiveFileAttachment("{{:personalAttachVO1.attachmentId}}",this);' >删除</a>
       {{/if}}
    </div>
</ul>
</div>
{{/for}}
</script>

<script type="text/javascript">
$(function(){
});

function initPersonalAwardData(masterReviewVO){
	bulidPersonalAward(masterReviewVO.personalAwardVOs);
}


//个人获奖
function bulidPersonalAward(personalAwardVOs){
	$("#personalAwardRowNum").val(personalAwardVOs.length);
	if(personalAwardVOs.length>0){
		 var dataObject = {'Data': []};
		 for(var i =0;i<personalAwardVOs.length;i++){
			 var headmaster_approve_levels =  Brightcom.workflow.getSelectCombox('headmaster_approve_level');
			 personalAwardVOs[i]['headmaster_approve_level'] = headmaster_approve_levels;
			 
			 
			 var headmaster_awards_types =  Brightcom.workflow.getSelectCombox('headmaster_awards_type');
			 personalAwardVOs[i]['headmaster_awards_type'] = headmaster_awards_types;
			 dataObject.Data.push(personalAwardVOs[i]);
		 }
		 //debugger;
		 var subTaskContent= $("#personalAwardRec").render(dataObject);
		 $("#personalAwardRefill").append(subTaskContent);
		
		 for(var i =0;i<personalAwardVOs.length;i++){
			 Headmaster.initWebUploader('personalAwardUpload',(i+1),'personalAwardType','上传获奖证书','awards_attachment_id_personal','personalAwardTypeDiv');
			 Headmaster.initWebUploader('personalAwardUpload1',(i+1),'personalAwardType','上传获奖证书','awards_attachment_id_personal1','personalAwardTypeDiv1');
		 }
		 
		 
	}
	//$("#personalAwardRefill").append("<div class='add'><a class='add-more' href='javascript:void(0);' onclick='addPersonalAwardSingle(this)' >+</a></div> ");
}

function addPersonalAwardSingle(obj){
	var personalAwardRowNum = parseInt($("#personalAwardRowNum").val());
	var personalAwardRowNumNext = parseInt(personalAwardRowNum+1);

	var educationArray =[];
	educationArray.push("<div class='show-list radius-3'>"+personalAwardRowNumNext+"、<a href='javascript:void(0);' target='_self' onclick=\"deleteSingleOption(this,'personalAward',\'"+personalAwardRowNumNext+"\','');\" class='del radius-3'>删除</a></div>");
	educationArray.push("<div class='container'>");
	educationArray.push("<ul  class='clear-fix'>");
	
	educationArray.push("<input type='hidden' id='personalAwardId"+personalAwardRowNumNext+"'  value=''>");
	educationArray.push("<input type='hidden' id='awards_attachment_id_personal"+personalAwardRowNumNext+"'  value=''>");
	educationArray.push("<input type='hidden' id='awards_attachment_id_personal1"+personalAwardRowNumNext+"'  value=''>");
	
	educationArray.push("<li>");
	educationArray.push("  <span class='fl'>奖项名称：</span>");
	educationArray.push("<div class='border_2 w_13 fl'>");
	educationArray.push("  <input type='text' id='awards_name_personal"+personalAwardRowNumNext+"' value='' placeholder=''>");
	educationArray.push("</div>");
	educationArray.push("</li>");
					
	educationArray.push("<li>");
	educationArray.push("<span class='fl'>表彰单位：</span>");
	educationArray.push("<div class='border_2 w_13 fl'>");
	educationArray.push("<input type='text' id='awards_company_personal"+personalAwardRowNumNext+"' value='' placeholder=''>");
	educationArray.push("</div>");
	educationArray.push("</li>");
							
	educationArray.push("<li>");
	educationArray.push("<span class='fl'>获奖级别：</span>");
	educationArray.push("<div class='border_2 w_13 fl'>");
	educationArray.push("<select id='awards_level_personal"+personalAwardRowNumNext+"' class='select_left'></select>");
	educationArray.push("</div>");
	educationArray.push("</li>");
							
	educationArray.push("<li>");
	educationArray.push("<span class='fl'>表彰时间：</span>");
	educationArray.push("<div class='border_2 w_13 fl'>");
	educationArray.push("<input type='text' id='awards_time_personal"+personalAwardRowNumNext+"' value='' placeholder='' onclick='selectDeleteTime()'>");
	educationArray.push("</div>");
	educationArray.push("</li>");
	
	educationArray.push("<li>");
	educationArray.push("<span class='fl'>奖项类别1：</span>");
	educationArray.push("<div  class='border_2 w_28 fl'>");
	educationArray.push("<select id='awards_type"+personalAwardRowNumNext+"' class='select_left'></select>");
	educationArray.push("</div>");
	educationArray.push("</li>");
	
	educationArray.push("<li class='position_relative'>");
	educationArray.push("<span class='fl'>获奖证书1：</span> ");
	educationArray.push("<div id='personalAwardUpload"+personalAwardRowNumNext+"' class='position_upload_button_professional'></div>");
	educationArray.push("</li>");
	
	
	educationArray.push("<div id='personalAwardTypeDiv"+personalAwardRowNumNext+"' class='only_attachments'></div>");
	
	
	
	educationArray.push("<li>");
	educationArray.push("<span class='fl'>奖项类别2：</span>");
	educationArray.push("<div class='border_2 w_28 fl'>");
	educationArray.push("<select id='awards_type1"+personalAwardRowNumNext+"' class='select_left'></select>");
	educationArray.push("</div>");
	educationArray.push("</li>");
	
	educationArray.push("<li class='position_relative'>");
	educationArray.push("<span class='fl'>获奖证书2：</span> ");
	educationArray.push("<div id='personalAwardUpload1"+personalAwardRowNumNext+"' class='position_upload_button_professional'></div>");
	educationArray.push("</li>");
	
	
	educationArray.push("<div id='personalAwardTypeDiv1"+personalAwardRowNumNext+"' class='only_attachments'></div>");
	
	educationArray.push("</ul>");
	educationArray.push("</div>");
	
	//educationArray.push("<div class='add'><a class='add-more' href='javascript:void(0);' onclick='addPersonalAwardSingle(this)' >+</a></div>");
	
	//$(obj).after(educationArray.join(""));
	//$(obj).remove();
	$("#personalAwardRefill").append(educationArray.join(""));
	
	$("#personalAwardRowNum").val(personalAwardRowNumNext);
	
	Brightcom.workflow.initSelectCombox('headmaster_approve_level','awards_level_personal'+(personalAwardRowNumNext));
	Brightcom.workflow.initSelectCombox('headmaster_awards_type','awards_type'+(personalAwardRowNumNext));
	Brightcom.workflow.initSelectCombox('headmaster_awards_type','awards_type1'+(personalAwardRowNumNext));
	
	Headmaster.initWebUploader('personalAwardUpload',personalAwardRowNumNext,'personalAwardType','上传获奖证书','awards_attachment_id_personal','personalAwardTypeDiv');
	
	Headmaster.initWebUploader('personalAwardUpload1',personalAwardRowNumNext,'personalAwardType1','上传获奖证书','awards_attachment_id_personal1','personalAwardTypeDiv1');
}


function saveUpdateRefillData(){
	var isRefillTask = $("#isRefillTask").val();
	if(isRefillTask){
		var submitStrings =  getSubmitStrings();
		var bcReq = new BcRequest('headmaster','masterReviewAction','saveUpdateRefillData');
		bcReq.setExtraPs({		   
			"option_tab_type":'personalAward',
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
	var personalAwardRowNum = $("#personalAwardRowNum").val();
	for(var i=0;i<personalAwardRowNum;i++){
		var rowNum = (i+1);
		var id = $("#personalAwardId"+rowNum).val();
		var awards_name = $("#awards_name_personal"+rowNum).val();
		var awards_company = $("#awards_company_personal"+rowNum).val();
		var awards_level = $("#awards_level_personal"+rowNum).val();
		var awards_time = $("#awards_time_personal"+rowNum).val();
		var awards_attachment_id = $("#awards_attachment_id_personal"+rowNum).val();
		var awards_type = $("#awards_type"+rowNum).val();
		var businessKey = $("#id").val();
		
		var awards_attachment_id1 = $("#awards_attachment_id_personal1"+rowNum).val();
		var awards_type1 = $("#awards_type1"+rowNum).val();
		if(!awards_name){
			continue;
		}
		var workExperienceObject = {
				"id":id,
				"businessKey":$("#id").val(),
				"awards_name":awards_name,
				"awards_company":awards_company,
				"awards_level":awards_level,
				"awards_time":awards_time,
				"awards_attachment_id":awards_attachment_id,
				"awards_type":awards_type,
				"awards_attachment_id1":awards_attachment_id1,
				"awards_type1":awards_type1
		}
		submitArray.push(workExperienceObject);
	}
	return JSON.stringify(submitArray);
}

function headmasterBeforeSubmit(formJsonData){
	formJsonData.option_tab_type = "personaward"
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
			<h2><i>19</i>个人获奖情况</h2>
			<p>填写个人获得市级以上党委政府、教育行政部门授予的表彰和奖励。</p>
		</div>
		<div class="select-step fr"><a href="javascript:void(0);" target="_self" title="" id="change">+&nbsp;切换步骤</a></div>
		<div class="clear-both"></div>
	</div>
	<!-- 标题 e -->
	<!-- 任职年限 s -->
	<div id="personalAwardRefill" class="years">
			<input type="hidden" id="personalAwardRowNum" name="personalAwardRowNum" value="0">
	</div>
	<!-- 任职年限 e -->
	
	<div class="add"><a href="javascript:void(0);" onclick="addPersonalAwardSingle(this)" class="add-more">+</a></div>
	
	<div class="next-step clear-fix">
	  <a href="javascript:void(0);" target="_self" title="" class="fl" onclick="changeOption(18)">上一步</a>
	  <a href="javascript:void(0);" target="_self" title="" class="fr" onclick="saveUpdateRefillData()">下一步</a>
	 </div>

</body>
</html>