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

<!--减分处分  -->
<script id="punishmentRec" type="text/x-jsrender">	
{{for Data}}
    <div class='show-list radius-3'>{{:#index+1}}<a href='javascript:void(0);' target='_self'  onclick="deleteSingleOption(this,'punishment','{{:#index+1}}','{{:id}}');" class='del radius-3'>删除</a></div>
   <div class="container">
     <input type='hidden' id="punishmentId{{:#index+1}}"  value='{{:id}}'>
     <input type='hidden' id='proveAttachId{{:#index+1}}'  value='{{:proveAttachMentVO.attachmentId}}'>
	<ul class="clear-fix">

	<li>
      <span class="fl">时间：</span>
      <div class="border_2 w_20 fl">
        <input type="text" id='implement_time{{:#index+1}}' value='{{timeCovert implement_time/}}' onclick='selectDeleteTime()' placeholder="请输入时间" />
      </div>
    </li>

	<li>
        <span class="fl">处分事件描述：</span>
        <div class="border_2 w_23 fl">
          <input type="text" id='description{{:#index+1}}' value='{{:description}}' placeholder="请输入处分事件描述" />
        </div>
    </li>

	<li>
         <span class="fl">受处分人：</span>
         <div class="border_2 w_13 fl">
          <input type="text" id='people{{:#index+1}}' value='{{:people}}' placeholder="请输入受处分人" />
         </div>
    </li>

	<li>
      <span class="fl">处分部门：</span>
      <div class="border_2 w_13 fl">
       <input type="text" id='department{{:#index+1}}' value='{{:department}}'  placeholder="请输入处分部门" />
      </div>
    </li>

	<li>
      <span class="fl">处分结果：</span>
       <textarea maxlength='100' title='不得超过100字' name='approve_result{{:#index+1}}' id='approve_result{{:#index+1}}' class='fl deooration' >{{:approve_result}}</textarea>
    </li>

    <li style='height:45px;' class='position_relative'>
      <span class="fl">证明材料：</span>
      <div id='punishment_upload_div{{:#index+1}}' class='position_upload_button_professional'></div>
    </li>

    <div id='punishment_div{{:#index+1}}' class='only_attachments'>
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

function initPunishmentData(masterReviewVO){
	bulidPunishment(masterReviewVO.punishmentVOs);
}

//任职年限
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
			 Headmaster.initWebUploader('punishment_upload_div',(i+1),'punishment_type','点击上传','proveAttachId','punishment_div');
		 }
	}
	//$("#punishmentRefill").append("<div class='add'><a class='add-more' href='javascript:void(0);' onclick='addPunishmentSingle(this)' >+</a></div> ");
}

function addPunishmentSingle(obj){
	var punishmentRowNum = parseInt($("#punishmentRowNum").val());
	var punishmentRowNumNext = parseInt(punishmentRowNum+1);

	var educationArray =[];
	educationArray.push("<div class='show-list radius-3'>"+punishmentRowNumNext+"、<a href='javascript:void(0);' target='_self' onclick=\"deleteSingleOption(this,'punishment',\'"+punishmentRowNumNext+"\','');\" class='del radius-3'>删除</a></div>");
	educationArray.push("<div class='container'>");
	educationArray.push("<ul  class='clear-fix'>");
	
	
	educationArray.push("<input type='hidden' id='punishmentId"+punishmentRowNumNext+"'  value=''>");
	educationArray.push("<input type='hidden' id='proveAttachId"+punishmentRowNumNext+"'  value=''>");

	
	educationArray.push("<li><span class='fl'>时间：</span>");
	educationArray.push("<div class='border_2 w_20 fl'>");
	educationArray.push("<input type='text' id='implement_time"+punishmentRowNumNext+"'   onclick='selectDeleteTime()' placeholder='请输入时间' />");
	educationArray.push("</div>");
	educationArray.push("</li>");
	
	educationArray.push("<li><span class='fl'>处分事件描述：</span>");
	educationArray.push("<div class='border_2 w_23 fl'>");
	educationArray.push("<input type='text' id='description"+punishmentRowNumNext+"' value='' placeholder='请输入处分事件描述'>");
	educationArray.push("</div>");
	educationArray.push("</li>");
	
	educationArray.push("<li><span class='fl'>受处分人：</span>");
	educationArray.push("<div class='border_2 w_13 fl'>");
	educationArray.push("<input type='text' id='people"+punishmentRowNumNext+"'   value='' placeholder='请输入受处分人' />");
	educationArray.push("</div>");
	educationArray.push("</li>");
	
	
	educationArray.push("<li><span class='fl'>处分部门：</span>");
	educationArray.push("<div class='border_2 w_13 fl'>");
	educationArray.push("<input type='text' id='department"+punishmentRowNumNext+"'   value='' placeholder='请输入处分部门' />");
	educationArray.push("</div>");
	educationArray.push("</li>");
	
	
	educationArray.push("<li><span class='fl'>处理结果：</span>");
	educationArray.push("<textarea maxlength='100' title='不得超过100字' name='process_result"+punishmentRowNumNext+"'  id='process_result"+punishmentRowNumNext+"'  class='fl deooration' placeholder='请输入处理结果' ></textarea>");
	educationArray.push("</li>");
	
	educationArray.push("<li style='height:45px;' class='position_relative'>");
	educationArray.push(" <span class='fl'>证明材料：</span>");
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
	 * 1、professionalTitlespan:上传按钮显示位置id
	 4、buttonName ： 按钮名称
	 5、hiddenAttachId：隐藏附件id，选中后把附件值保存在该隐藏域里
	 6、 hiddenDisplayId:附件显示的div id
	 */
	 Headmaster.initWebUploader('punishment_upload_div',punishmentRowNumNext,'punishment_type','点击上传','proveAttachId','punishment_div');
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
			<h2><i>23</i>处分</h2>
			<p>填写校长本人受到党纪、行政处分或处理情况。</p>
		</div>
		<div class="select-step fr"><a href="javascript:void(0);" target="_self" title="" id="change">+&nbsp;切换步骤</a></div>
		<div class="clear-both"></div>
	</div>
	<!-- 标题 e -->
	<!-- 任职年限 s -->
	<div id="punishmentRefill" class="years">
		<input type="hidden" id="punishmentRowNum" name="punishmentRowNum" value="0">
	</div>
	<!-- 任职年限 e -->
	
	<div class="add"><a href="javascript:void(0);" onclick="addPunishmentSingle(this)" class="add-more">+</a></div>
	
	<div class="next-step clear-fix">
	  <a href="javascript:void(0);" target="_self" title="" class="fl" onclick="changeOption(22)">上一步</a>
	  <a href="javascript:void(0);" target="_self" title="" class="fr" onclick="saveUpdateRefillData()">提交申请</a>
	</div>
</body>
</html>