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
<script id="schoolReformRec" type="text/x-jsrender">	
{{for Data}}
    <div class='show-list radius-3'>{{:#index+1}}<a href='javascript:void(0);' target='_self'  onclick="deleteSingleOption(this,'schoolReformType','{{:#index+1}}','{{:id}}');" class='del radius-3'>删除</a></div>
   <div class="container">
	<input type='hidden' id="schoolReformId{{:#index+1}}"  value='{{:id}}'>
    <input type='hidden' id='proveAttachId{{:#index+1}}'  value='{{:proveAttachMentVO.attachmentId}}'>
	<ul class="clear-fix">
	<li>
         <span class="fl">学校特色创建及改革项目名称：</span>
         <div class="border_2 w_24 fl">
          <input type="text" id='project_name{{:#index+1}}' value='{{:project_name}}' placeholder="请输入改革项目名称" />
         </div>
    </li>
	<li>
      <span class="fl">项目级别：</span>
      <div class="border_2 w_13 fl">

       <select id="project_level{{:#index+1}}" class='select_left'>
                   {{for headmaster_approve_level}}
                     {{if id=== #parent.parent.data.project_level }}
				       <option value='{{:id}}' selected='selected'>{{:text}}</option>
				    {{else}}
				       <option value='{{:id}}' >{{:text}}</option>
				    {{/if}}
                    {{/for}}
          </select>
      </div>
    </li>
	<li>
        <span class="fl">时间：</span>
        <div class="border_2 w_20 fl">
          <input type="text" id='implement_time{{:#index+1}}' value='{{timeCovert implement_time/}}' onclick='selectDeleteTime()' placeholder="请输入时间" />
        </div>
    </li>
	<li>
      <span class="fl">项目主管部门：</span>
      <div class="border_2 w_23 fl">
       <input type="text" id='charge_department{{:#index+1}}' value='{{:charge_department}}'  />
      </div>
    </li>

	<li>
      <span class="fl">项目完成情况：</span>
        <textarea maxlength='100' title='不得超过100字' name='performance{{:#index+1}}' id='performance{{:#index+1}}' class='fl deooration' placeholder='请输入项目完成情况' >{{:performance}}</textarea>
    </li>

	 <li style='height:45px;' class='position_relative'>
      <span class="fl">证明材料：</span>
      <div id='school_reform_span{{:#index+1}}' class='position_upload_button_professional'></div>
    </li>

    <div id='school_reform_div{{:#index+1}}' class='only_attachments'>
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

function initSchoolReformData(masterReviewVO){
	bulidSchoolReform(masterReviewVO.schoolReformVOs);
}

//任职年限
function bulidSchoolReform(schoolReformVOs){
	$("#schoolReformRowNum").val(schoolReformVOs.length);
	if(schoolReformVOs.length>0){
		 var dataObject = {'Data': []};
		 for(var i =0;i<schoolReformVOs.length;i++){
			 var headmaster_approve_levels =  Brightcom.workflow.getSelectCombox('headmaster_approve_level');
			 schoolReformVOs[i]['headmaster_approve_level'] = headmaster_approve_levels;
			 dataObject.Data.push(schoolReformVOs[i]);
		 }
		 var subTaskContent= $("#schoolReformRec").render(dataObject);
		 $("#schoolReformRefill").append(subTaskContent);
		 
		 
		 for(var i =0;i<schoolReformVOs.length;i++){
			 Headmaster.initWebUploader('school_reform_span',(i+1),'school_reform_type','点击上传','proveAttachId','school_reform_div');
		 }
	}
	//$("#schoolReformRefill").append("<div class='add'><a class='add-more' href='javascript:void(0);' onclick='addSchoolReformSingle(this)' >+</a></div> ");
}

function addSchoolReformSingle(obj){
	var schoolReformRowNum = parseInt($("#schoolReformRowNum").val());
	var schoolReformRowNumNext = parseInt(schoolReformRowNum+1);

	var educationArray =[];
	educationArray.push("<div class='show-list radius-3'>"+schoolReformRowNumNext+"、<a href='javascript:void(0);' target='_self' onclick=\"deleteSingleOption(this,'schoolReform',\'"+schoolReformRowNumNext+"\','');\" class='del radius-3'>删除</a></div>");
	educationArray.push("<div class='container'>");
	educationArray.push("<ul id='' class='clear-fix'>");
	
	educationArray.push("<input type='hidden' id='schoolReformId"+schoolReformRowNumNext+"'  value=''>");
	educationArray.push("<input type='hidden' id='proveAttachId"+schoolReformRowNumNext+"'  value=''>");
	
	
	educationArray.push("<li><span class='fl'>学校特色创建及改革项目名称：</span>");
	educationArray.push("<div class='border_2 w_24 fl'>");
	educationArray.push("<input type='text' id='project_name"+schoolReformRowNumNext+"' value='' placeholder='请输入学校全称'>");
	educationArray.push("</div>");
	educationArray.push("</li>");
	
	educationArray.push("<li><span class='fl'>项目级别：</span>");
	educationArray.push("<div class='border_2 w_13 fl'>");
	educationArray.push("<select id='project_level"+schoolReformRowNumNext+"' class='select_left'></select>");
	educationArray.push("</div>");
	educationArray.push("</li>");
	
	educationArray.push("<li><span class='fl'>时间：</span>");
	educationArray.push("<div class='border_2 w_20 fl'>");
	educationArray.push("<input type='text' id='implement_time"+schoolReformRowNumNext+"'   onclick='selectDeleteTime()' placeholder='请输入时间' />");
	educationArray.push("</div>");
	educationArray.push("</li>");
	
	educationArray.push("<li><span class='fl'>项目主管部门：</span>");
	educationArray.push("<div class='border_2 w_23 fl'>");
	educationArray.push("<input type='text' id='charge_department"+schoolReformRowNumNext+"'   value='' placeholder='请输入项目主管部门' />");
	educationArray.push("</div>");
	educationArray.push("</li>");
	
	educationArray.push("<li><span class='fl'>项目完成情况：</span>");
	educationArray.push("<textarea maxlength='100' title='不得超过100字' name='performance"+schoolReformRowNumNext+"' id='performance"+schoolReformRowNumNext+"' class='fl deooration' placeholder='请输入项目完成情况' ></textarea>");
	educationArray.push("</li>");
	
	educationArray.push("<li style='height:45px;' class='position_relative'>");
	educationArray.push(" <span class='fl'>证明材料：</span>");
	educationArray.push(" <div id='school_reform_span"+schoolReformRowNumNext+"' class='position_upload_button_professional'></div>");
	educationArray.push("</li>");
	
	
	educationArray.push("<div id='school_reform_div"+schoolReformRowNumNext+"' class='only_attachments'></div>");
	
	
	educationArray.push("</ul>");
	educationArray.push("</div>");
	
	//educationArray.push("<div class='add'><a class='add-more' href='javascript:void(0);' onclick='addSchoolReformSingle(this)' >+</a></div>");
	
	//$(obj).after(educationArray.join(""));
	//$(obj).remove();
	$("#schoolReformRefill").append(educationArray.join(""));
	Brightcom.workflow.initSelectCombox('headmaster_approve_level','project_level'+(schoolReformRowNumNext));
	
	$("#schoolReformRowNum").val(schoolReformRowNumNext);
	
	/*
	 * 1、professionalTitlespan:上传按钮显示位置id
	 4、buttonName ： 按钮名称
	 5、hiddenAttachId：隐藏附件id，选中后把附件值保存在该隐藏域里
	 6、 hiddenDisplayId:附件显示的div id
	 */
	 Headmaster.initWebUploader('school_reform_span',schoolReformRowNumNext,'school_reform_type','点击上传','proveAttachId','school_reform_div');
}


function saveUpdateRefillData(){
	var isRefillTask = $("#isRefillTask").val();
	if(isRefillTask){
		var submitStrings =  getSubmitStrings();
		var bcReq = new BcRequest('headmaster','masterReviewAction','saveUpdateRefillData');
		bcReq.setExtraPs({
			    "option_tab_type":'schoolReform',
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
	var paperTitleRowNum = $("#schoolReformRowNum").val();
	for(var i=0;i<paperTitleRowNum;i++){
		var rowNum = (i+1);
		var id = $("#schoolReformId"+rowNum).val();
		var project_name = $("#project_name"+rowNum).val();
		var project_level = $("#project_level"+rowNum).val();
		var implement_time = $("#implement_time"+rowNum).val();
		var charge_department = $("#charge_department"+rowNum).val();
		var performance = $("#performance"+rowNum).val();
		var businessKey = $("#id").val();
		var proveAttachId = $("#proveAttachId"+rowNum).val();
		if(!project_name){
			continue;
		}
		var workExperienceObject = {
				"id":id,
				"project_name":project_name,
				"businessKey":$("#id").val(),
				"project_level":project_level,
				"implement_time":implement_time,
				"charge_department":charge_department,
				"performance":performance,
				"proveAttachId":proveAttachId
		}
		submitArray.push(workExperienceObject);
	}
	return JSON.stringify(submitArray);
}

function headmasterBeforeSubmit(formJsonData){
	formJsonData.option_tab_type = "schoolReform"
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
			<h2><i>20</i>学校特色及改革</h2>
			<p>填写学校推进素质教育特色学校创建、承担上级行政部门试点改革项目等情况。</p>
		</div>
		<div class="select-step fr"><a href="javascript:void(0);" target="_self" title="" id="change">+&nbsp;切换步骤</a></div>
		<div class="clear-both"></div>
	</div>
	<!-- 标题 e -->
	<!-- 任职年限 s -->
	<div id="schoolReformRefill" class="years">
		<input type="hidden" id="schoolReformRowNum" name="schoolReformRowNum" value="0">
	</div>
	<!-- 任职年限 e -->
	
	<div class="add"><a href="javascript:void(0);" onclick="addSchoolReformSingle(this)" class="add-more">+</a></div>
	
	<div class="next-step clear-fix">
	   <a href="javascript:void(0);" target="_self" title="" class="fl" onclick="changeOption(19)">上一步</a>
	   <a href="javascript:void(0);" target="_self" title="" class="fr" onclick="saveUpdateRefillData()">下一步</a>
	</div>

</body>
</html>