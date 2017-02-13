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
<script id="workExperienceRec" type="text/x-jsrender">	
{{for Data}}
    <div class='show-list radius-3'>{{:#index+1}}<a href='javascript:void(0);' target='_self'  onclick="deleteSingleOption(this,'workExperienceType','{{:#index+1}}','{{:id}}');" class='del radius-3'>删除</a></div>
   <div class="container">
    <input type='hidden' id="workExperienceId{{:#index+1}}"  value='{{:id}}'>
	<input type='hidden' id="proveId{{:#index+1}}"  value='{{:proveAttachMentVO.attachmentId}}'>
	<ul class="clear-fix">
	<li>
        <span class="fl">起止年月：</span>		
        <div class="border_2 w_12 fl">
          <input type="text" id='startTimeExperience{{:#index+1}}' onFocus='selectDeleteTime()' value="{{timeCovert startTime/}}"/>
        </div>
		<span class="fl">至</span>
		<div class="border_2 w_12 fl">
          <input type="text" id='endTimeExperience{{:#index+1}}'  onFocus='selectDeleteTime()' value="{{timeCovert endTime/}}" />
        </div>
	</li>
	<li>
        <span class="fl">所在学校：</span>
        <div class="border_2 w_13 fl">
          <input type="text" id='workSchoolExperience{{:#index+1}}' value='{{:workSchool}}' placeholder="请输入学校全称" />
        </div>
    </li>
	<li>
         <span class="fl">担任职务：</span>
         <div class="border_2 w_13 fl"><input type="text" id='workProfession{{:#index+1}}' value='{{:workProfession}}' placeholder="请输入职务" /></div>
    </li>
	<li>
       <span class='fl'>行政管理岗位级别：</span>
       <div class="border_2 w_19 fl">
         <select   id="manage_level{{:#index+1}}" class='select_left'>
                   {{for headmaster_manage_level}}
                     {{if id===#parent.parent.data.manage_level }}
				       <option value='{{:id}}' selected='selected'>{{:text}}</option>
				    {{else}}
				       <option value='{{:id}}' >{{:text}}</option>
				    {{/if}}
                    {{/for}}
          </select>
      </div>
    </li>
	<li>
      <span class="fl">任职年限：</span>
      <div class="border_2 w_14 fl">
         <label id='workYear{{:#index+1}}'> {{:yearDiffDate}}</label>
      </div>
      <span class="fl">年</span>
      <div class="border_2 w_14 fl">
         <label id='workMonth{{:#index+1}}'> {{:monthDiffDate}}</label>
       </div>
      <span class="fl">月</span>
    </li>
    <li style='height:45px;' class='position_relative'>
     <span class="fl">证明材料：</span>
     <div style='padding-left:4px;' id='spanButtonPlaceHolder{{:#index+1}}' class='position_upload_button_professional'></div> </li>

     <div id='workExperienceTypeDiv{{:#index+1}}' class='only_attachments'>
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

function initWorkExperience(masterReviewVO){
	bulidWorkExperience(masterReviewVO.workExperienceVOs);
}

//任职年限
function bulidWorkExperience(workExperienceVOs){
	$("#workExperienceRowNum").val(workExperienceVOs.length);
	if(workExperienceVOs.length>0){
		 var dataObject = {'Data': []};
		 for(var i =0;i<workExperienceVOs.length;i++){
			 var diffDate = BcUtil.get_yearMonthDiffFormat(Brightcom.workflow.getDateStrByLong(workExperienceVOs[i].startTime),Brightcom.workflow.getDateStrByLong(workExperienceVOs[i].endTime));
			 
			 var month = diffDate.month;
			 var year = parseInt(month/12);
			 if (year>0) {
				month = month%12;
			 }
			
			 //workExperienceVOs[i]['yearDiffDate'] = diffDate.year || '';
			 //workExperienceVOs[i]['monthDiffDate'] = diffDate.month || '';
			 workExperienceVOs[i]['yearDiffDate'] = year || '';
			 workExperienceVOs[i]['monthDiffDate'] = month || '';
			 
			 var headmaster_manage_levels =  Brightcom.workflow.getSelectCombox('headmaster_manage_level');
			 workExperienceVOs[i]['headmaster_manage_level'] = headmaster_manage_levels;
			
			 dataObject.Data.push(workExperienceVOs[i]);
		 }
		 var subTaskContent= $("#workExperienceRec").render(dataObject);
		 $("#workExperienceRefill").append(subTaskContent);
		 
		 for(var i =0;i<workExperienceVOs.length;i++){
			 Headmaster.initWebUploader('spanButtonPlaceHolder',(i+1),'workExperienceType','点击上传','proveId','workExperienceTypeDiv');
		 }
	}
	//$("#workExperienceRefill").append("<div class='add'><a class='add-more' href='javascript:void(0);' onclick='addWorkExperienceSingle(this)' >+</a></div> ");
}

function addWorkExperienceSingle(obj){
	var workExperienceRowNum = parseInt($("#workExperienceRowNum").val());
	var workExperienceRowNumNext = parseInt(workExperienceRowNum+1);

	var educationArray =[];
	educationArray.push("<div class='show-list radius-3'>"+workExperienceRowNumNext+"、<a href='javascript:void(0);' target='_self' onclick=\"deleteSingleOption(this,'workExperience',\'"+workExperienceRowNumNext+"\','');\" class='del radius-3'>删除</a></div>");
	educationArray.push("<div class='container'>");
	educationArray.push("<ul id='' class='clear-fix'>");
	//educationArray.push("<div id='workExperienceHead"+workExperienceRowNumNext+"' class='tit2'><span><a href='javascript:void(0);' class='tit2-trigger' onclick=\"toggleDiv(this);\">收起</a>");
	//educationArray.push("<a href='javascript:void(0);' onclick=\"deleteSingleOption(this,'workExperienceType',\'"+workExperienceRowNumNext+"\','');\" >删除</a></span></div>");
	
	//educationArray.push("<div class='tab-pane-sub'>");
	educationArray.push("<input type='hidden' id='workExperienceId"+workExperienceRowNumNext+"'  value=''>");
	educationArray.push("<input type='hidden' id='proveId"+workExperienceRowNumNext+"' value=''>");
	//educationArray.push("<ul>");
	educationArray.push("<li><span class='fl'>起止年月：</span>");
	educationArray.push("<div class='border_2 w_12 fl'>");
	educationArray.push("<input type='text' id='startTimeExperience"+workExperienceRowNumNext+"' name='startTimeExperience"+workExperienceRowNumNext+"' onFocus='selectDeleteTime()'/>");
	educationArray.push("</div>");
	educationArray.push("<span class='fl'>至</span>");
	educationArray.push("<div class='border_2 w_12 fl'>");
	educationArray.push("<input type='text' id='endTimeExperience"+workExperienceRowNumNext+"' name='endTimeEducation"+workExperienceRowNumNext+"' onFocus='selectDeleteTime()'/>");
	educationArray.push("</div>");
	educationArray.push("</li>");
	
	educationArray.push("<li><span class='fl'>所在学校：</span>");
	educationArray.push("<div class='border_2 w_13 fl'>");
	educationArray.push("<input type='text' id='workSchoolExperience"+workExperienceRowNumNext+"' value='' placeholder='请输入学校全称'>");
	educationArray.push("</div>");
	educationArray.push("</li>");
	
	educationArray.push("<li><span class='fl'>担任职务：</span>");
	educationArray.push("<div class='border_2 w_13 fl'>");
	educationArray.push("<input type='text' id='workProfession"+workExperienceRowNumNext+"' value='' placeholder='请输入职务'>");
	educationArray.push("</div>");
	educationArray.push("</li>");
	
	
	educationArray.push("<li><span class='fl'>行政管理岗位级别：</span>");
	educationArray.push("<div class='border_2 w_19 fl'>");
	educationArray.push("<select id='manage_level"+workExperienceRowNumNext+"' class='select_left'></select>");
	educationArray.push("</div>");
	educationArray.push("</li>");
	
	educationArray.push("<li><span class='fl'>任职年限：</span>");
	educationArray.push("<div class='border_2 w_14 fl'>");
//	educationArray.push("<input type='number' id='workYear"+workExperienceRowNumNext+"'   value='' placeholder='请输入年限' />");
	educationArray.push("<label id='workYear"+workExperienceRowNumNext+"' ></label>");
	educationArray.push("</div>");
	educationArray.push("<span class='fl'>年</span>");
	
	educationArray.push("<div class='border_2 w_14 fl'>");
//	educationArray.push("<input type='number' id='workYear"+workExperienceRowNumNext+"'   value='' placeholder='请输入年限' />");
	educationArray.push("<label id='workMonth"+workExperienceRowNumNext+"' ></label>");
	educationArray.push("</div>");
	educationArray.push("<span class='fl'>月</span>");
	educationArray.push("</li>");
	
	educationArray.push("<li style='height:45px;' class='position_relative'>");
	educationArray.push("<span class='fl'>证明材料：</span><div id='spanButtonPlaceHolder"+workExperienceRowNumNext+"'  class='position_upload_button_professional'></div> ");
	educationArray.push("</li>");
	
	educationArray.push("<div id='workExperienceTypeDiv"+workExperienceRowNumNext+"' class='only_attachments'></div>");
	//educationArray.push("<li class='btn'><input type='button' value='保 存' onclick=\"saveSingleOption('workExperienceType',\'"+workExperienceRowNumNext+"\','');\" ></li>");
	educationArray.push("</ul>");
	educationArray.push("</div>");
	
	//educationArray.push("<div class='add'><a class='add-more' href='javascript:void(0);' onclick='addWorkExperienceSingle(this)' >+</a></div>");
	
	//$(obj).after(educationArray.join(""));
	//$(obj).remove();
	
	$("#workExperienceRefill").append(educationArray.join(""));
	
	$("#workExperienceRowNum").val(workExperienceRowNumNext);
	Brightcom.workflow.initSelectCombox('headmaster_manage_level','manage_level'+(workExperienceRowNumNext));
	
	Headmaster.initWebUploader('spanButtonPlaceHolder',workExperienceRowNumNext,'workExperienceType','点击上传','proveId','workExperienceTypeDiv');
}

function saveUpdateRefillData(){
	var isRefillTask = $("#isRefillTask").val();
	if(isRefillTask){
		var submitStrings =  getSubmitStrings();
		var bcReq = new BcRequest('headmaster','masterReviewAction','saveUpdateRefillData');
		bcReq.setExtraPs({
			    "option_tab_type":'workExperience',
			    "option_tab_values":submitStrings,
			    "businessKey":processBusinessKey
			}
		);
		bcReq.setSuccFn(function(data,status){
			changeOption(6);
		});
		bcReq.postData();
	}else{
		submitRefillTask();
	}
}

function getSubmitStrings(){
	var submitArray = [];
	var workExperienceRowNum = $("#workExperienceRowNum").val();
	for(var i=0;i<workExperienceRowNum;i++){
		var rowNum = (i+1);
		var id = $("#workExperienceId"+rowNum).val();
		var startTime = $("#startTimeExperience"+rowNum).val();
		var endTime = $("#endTimeExperience"+rowNum).val();
		var workSchool = $("#workSchoolExperience"+rowNum).val();
		var workProfession = $("#workProfession"+rowNum).val();
		var workYear = $("#workYear"+rowNum).val();
		var proveAttachMentId = $("#proveId"+rowNum).val();
		var manage_level = $("#manage_level"+rowNum).val();
		var businessKey = $("#id").val();
		if(!workSchool){
			continue;
		}
		var workExperienceObject = {
				"id":id,
				"businessKey":$("#id").val(),
				"startTime":startTime,
				"endTime":endTime,
				"workSchool":workSchool,
				'workProfession' :workProfession,
				'workYear' :workYear,
				 'manage_level':manage_level,
				'proveAttachMentId' :proveAttachMentId
		}
		submitArray.push(workExperienceObject);
	}
	return JSON.stringify(submitArray);
}

function headmasterBeforeSubmit(formJsonData){
	formJsonData.option_tab_type = "workExperience"
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
			<h2><i>5</i>校长(副校长)任职年限</h2>
			<p>1、填写任正、副校长年限情况。2、正、副校长任职年限按学年度计算。一学年内实际任职时间累积不足半年的（含寒暑假），当年不计为人追年限，超过半年的，当年任职年限按一年计算；校长任职年限计算截止至2016年*月*日；校长任职时间以任职文件为准。3、教育行政部门、教育系统其它事业单位与学校之间人员相互调任的，如调任前后行政管理岗位级别相同，其校领导任职年限可连续计算，其经历单独填写。</p>
		</div>
		<div class="select-step fr"><a href="javascript:void(0);" target="_self" title="" id="change">+&nbsp;切换步骤</a></div>
		<div class="clear-both"></div>
	</div>
	<!-- 标题 e -->
	<!-- 任职年限 s -->
	<div id= "workExperienceRefill" class="years">
		<input type="hidden" id="workExperienceRowNum" name="workExperienceRowNum" value="0">
		<!-- 
			<li><a href="#" target="_self" title="" class="add-more">+</a></li>
			<li>
				<span class="fl">起止年月：</span>
				<div class="border_2 w_12 fl"><input type="text" name="" value="" placeholder="年-月-日" id="date-04" /></div>
				<span class="fl">至</span>
				<div class="border_2 w_12 fl"><input type="text" name="" value="" placeholder="年-月-日" id="date-05" /></div>
			</li>
			<li>
				<span class="fl">所在学校：</span>
				<div class="border_2 w_13 fl"><input type="text" name="" value="" placeholder="请输入学校全称" /></div>
			</li>
			<li>
				<span class="fl">担任职务：</span>
				<div class="border_2 w_13 fl"><input type="text" name="" value="" placeholder="请输入职务" /></div>
			</li>
			<li>
				<span class="fl">任职年限：</span>
				<div class="border_2 w_14 fl"><input type="number" name="" value="" placeholder="请输入年限" /></div>
				<span class="fl">年</span>
			</li>
			<li>
				<span class="fl">证明材料：</span>
				<input type="button" value="点击上传" class="up-load fl" />
			</li>
			<li><a href="#" target="_self" title="" class="add-more">+</a></li>
			<li>
				<span class="fl">起止年月：</span>
				<div class="border_2 w_12 fl"><input type="text" name="" value="" placeholder="年-月-日" id="date-04" /></div>
				<span class="fl">至</span>
				<div class="border_2 w_12 fl"><input type="text" name="" value="" placeholder="年-月-日" id="date-05" /></div>
			</li>
			<li>
				<span class="fl">所在学校：</span>
				<div class="border_2 w_13 fl"><input type="text" name="" value="" placeholder="请输入学校全称" /></div>
			</li>
			<li>
				<span class="fl">担任职务：</span>
				<div class="border_2 w_13 fl"><input type="text" name="" value="" placeholder="请输入职务" /></div>
			</li>
			<li>
				<span class="fl">任职年限：</span>
				<div class="border_2 w_14 fl"><input type="number" name="" value="" placeholder="请输入年限" /></div>
				<span class="fl">年</span>
			</li>
			<li>
				<span class="fl">证明材料：</span>
				<input type="button" value="点击上传" class="up-load fl" />
			</li>
			<li><a href="#" target="_self" title="" class="add-more">+</a></li> -->
	</div>
	
	<div class="add"><a href="javascript:void(0);" onclick="addWorkExperienceSingle(this)" class="add-more">+</a></div>
	
	<!-- 任职年限 e -->
	<div class="next-step clear-fix">
	  <a href="javascript:void(0);" target="_self" title="" class="fl" onclick="changeOption(4)">上一步</a>
	  <a href="javascript:void(0);" target="_self" title="" class="fr" onclick="saveUpdateRefillData()">下一步</a>
	</div>

</body>
</html>