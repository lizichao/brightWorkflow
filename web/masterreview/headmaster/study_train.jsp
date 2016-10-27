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
      <span class='fl'>学习进修内容：</span>
     <div class="border_2 w_19 fl">
      <input type='text' id='content{{:#index+1}}' value='{{:content}}' placeholder=''>
     </div>
    </li>

	<li>
     <span class='fl'>学时：</span>
     <div class="border_2 w_13 fl">
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
		
		// for(var i =0;i<studyTrainVOs.length;i++){
		//	 Headmaster.initWebUploader('studyTrainbutton',(i+1),'studyTrainType','点击上传','studyTrainAttachId','studyTrainbuttonDiv');
		// }
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
	
	educationArray.push("<li><span class='fl'>学习进修内容：</span>");
	educationArray.push("<div class='border_2 w_19 fl'>");
	educationArray.push("<input type='text' id='content"+studyTrainRowNumNext+"' value='' placeholder='请输入学习进修内容'>");
	educationArray.push("</div>");
	educationArray.push("</li>");
	
	educationArray.push("<li><span class='fl'>学时：</span>");
	educationArray.push("<div class='border_2 w_13 fl'>");
	educationArray.push("<input type='text' id='class_hour"+studyTrainRowNumNext+"' value='' placeholder='请输入学时'>");
	educationArray.push("</div>");
	educationArray.push("</li>");
	
	
	educationArray.push("<li><span class='fl'>学习地点：</span>");
	educationArray.push("<div class='border_2 w_19 fl'>");
	educationArray.push("<input type='text' id='study_place"+studyTrainRowNumNext+"' value='' placeholder='请输入学习地点'>");
	educationArray.push("</div>");
	educationArray.push("</li>");
	
	
	educationArray.push("<li><span class='fl'>主办单位：</span>");
	educationArray.push("<div class='border_2 w_19 fl'>");
	educationArray.push("<input type='text' id='organizers"+studyTrainRowNumNext+"' value='' placeholder='请输入主办单位'>");
	educationArray.push("</div>");
	educationArray.push("</li>");
	
	
	educationArray.push("</ul>");
	educationArray.push("</div>");
	
	//educationArray.push("<div class='add'><a class='add-more' href='javascript:void(0);' onclick='addWorkExperienceSingle(this)' >+</a></div>");
	
	//$(obj).after(educationArray.join(""));
	//$(obj).remove();
	
	$("#studyTrainRefill").append(educationArray.join(""));
	
	$("#studyTrainRowNum").val(studyTrainRowNumNext);
	
	
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
			changeOption(10);
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
		var content = $("#content"+rowNum).val();
		var class_hour = $("#class_hour"+rowNum).val();
		var study_place = $("#study_place"+rowNum).val();
		var organizers = $("#organizers"+rowNum).val();
		var businessKey = $("#id").val();
		if(!content){
			continue;
		}
		var workExperienceObject = {
				"id":id,
				"businessKey":$("#id").val(),
				"start_date":start_date,
				"end_date":end_date,
				"content":content,
				"class_hour":class_hour,
				"study_place":study_place,
				"organizers":organizers
		}
		submitArray.push(workExperienceObject);
	}
	return JSON.stringify(submitArray);
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
			<h2><i>9</i>进修学习</h2>
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
	   <a href="javascript:void(0);" target="_self" title="" class="fl" onclick="changeOption(8)">上一步</a>
	   <a href="javascript:void(0);" target="_self" title="" class="fr" onclick="saveUpdateRefillData()">下一步</a>
	</div>

</body>
</html>