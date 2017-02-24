<%@ page contentType="text/html; charset=GBK" %>
<%
	String userid = (String)session.getAttribute("userid");
	String username =(String)session.getAttribute("username");
	String usertype =(String)session.getAttribute("usertype");
	// String usertype =(String)session.getAttribute("usertype");
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%>	
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="keywords" content="" />
<meta name="description" content="" />
<title>深圳市校长职级评审系统</title>

<!--学历情况 -->
<script id="educationRec" type="text/x-jsrender">	
{{for Data}}
<div class='show-list radius-3'>{{:#index+1}}<a href='javascript:void(0);' target='_self' onclick="deleteSingleOption(this,'educationType','{{:#index+1}}','{{:id}}');" class='del radius-3'>删除</a></div>
 <div class="container">
	<ul class="clear-fix">
        <input type='hidden' id='educationId{{:#index+1}}' name='educationId' value='{{:id}}'>
	    <input type='hidden' id='educationAttachId{{:#index+1}}'  value='{{:educationAttachMentVO.attachmentId}}'>
	    <input type='hidden' id='degreeAttachId{{:#index+1}}'  value='{{:degreeAttachMentVO.attachmentId}}'>
			<li>
              <span class="fl">起止年月：</span>		
              <div class="border_2 w_12 fl">
			   <input type="text" value="{{timeCovert startTime/}}" id="startTimeEducation{{:#index+1}}" name="finishDate" onclick="selectDeleteTime()"/>
              </div>
		      <span class="fl">至</span>
              <div class="border_2 w_12 fl">
			    <input type="text" value="{{timeCovert endTime/}}" id="endTimeEducation{{:#index+1}}" name="finishDate" onclick="selectDeleteTime()"/>
              </div>
			</li>
			<li>
               <span class="fl">就读院校：</span>	
               <div class="border_2 w_13 fl">
                <input type="text" id="study_schoolEducation{{:#index+1}}" value="{{:studySchool}}" placeholder="">
               </div>
            </li>
			<li><span class="fl">就读专业：</span>
                <div class="border_2 w_13 fl">
                  <input type="text" id="study_professionEducation{{:#index+1}}" value="{{:studyProfession}}" placeholder="">
                </div>
           </li>
		   <li><span class="fl">学习形式：</span>
                <div class="border_2 w_13 fl">
                  <select class='select_left' id="study_form{{:#index+1}}">
                     {{for headmaster_study_form}}
                       	{{if id===#parent.parent.data.study_form }}
				         	<option value='{{:id}}' selected='selected'>{{:text}}</option>
				     	{{else}}
				        	<option value='{{:id}}' >{{:text}}</option>
				     	{{/if}}
                     {{/for}}
                    </select>
                </div>
           </li>
		   <li><span class="fl">教育类别：</span>
                <div class="border_2 w_13 fl">
                  <select class='select_left'  id="education_type{{:#index+1}}">
                     {{for headmaster_education_type}}
                       	{{if id===#parent.parent.data.education_type}}
				         	<option value='{{:id}}' selected='selected'>{{:text}}</option>
				     	{{else}}
				        	<option value='{{:id}}' >{{:text}}</option>
				     	{{/if}}
                     {{/for}}
                    </select>
                </div>
           </li>
		   <li>
				<dl class="clear-fix">
					<dt class="clear-fix position_relative">
						<span class="fl">学历：</span>
						<div class="border_2 w_21 fl" style="z-index:97;">
							<select id="education{{:#index+1}}">
                   				{{for headmaster_education}}
                     				{{if id===#parent.parent.data.education }}
				       					<option value='{{:id}}' selected='selected'>{{:text}}</option>
				    				{{else}}
				       					<option value='{{:id}}' >{{:text}}</option>
				    				{{/if}}
                    			{{/for}}
               				</select>
						</div>
						<div id="educationButton{{:#index+1}}" class="position_upload_button"></div>
					</dt>
					<div id="educationButtonDiv{{:#index+1}}" style="width:300px;heigth:0px;margin-left:10px;">
						{{if educationAttachMentVO.attachmentId !==null}}
         					<a class='chachu' href="<%=basePath%>WorkflowAttachMentDownload?attachmentId={{:educationAttachMentVO.attachmentId}}">{{:educationAttachMentVO.fileName}}</a>
         					&nbsp;&nbsp;&nbsp;&nbsp;
         					<a class='chachu'  href='javascript:void(0);' onclick="Headmaster.deleteReceiveFileAttachment('{{:educationAttachMentVO.attachmentId}}',this);">删除</a>
       					{{/if}}
					</div>
					<dt class="clear-fix position_relative" style="margin-top:20px;">
						<span class="fl">学位：</span>
						<div class="border_2 w_21 fl" style="z-index:97;">
							<select id="degree{{:#index+1}}">
                   				{{for headmaster_degree}}
                     				{{if id===#parent.parent.data.degree }}
				       					<option value='{{:id}}' selected='selected'>{{:text}}</option>
				    				{{else}}
				       					<option value='{{:id}}' >{{:text}}</option>
				    				{{/if}}
                    			{{/for}}
               				</select>
						</div>
						<div id="degreeButton{{:#index+1}}" class="position_upload_button"></div>
					</dt>	
					<div id="degreeButtonDiv{{:#index+1}}" style="width:300px;heigth:0px;margin-left:10px">
						{{if degreeAttachMentVO.attachmentId !==null}}
						    <a class='chachu' href="<%=basePath%>WorkflowAttachMentDownload?attachmentId={{:degreeAttachMentVO.attachmentId}}">{{:degreeAttachMentVO.fileName}}</a>
         					&nbsp;&nbsp;&nbsp;&nbsp;
         					<a class='chachu'  href='javascript:void(0);' onclick="Headmaster.deleteReceiveFileAttachment('{{:degreeAttachMentVO.attachmentId}}',this);" >删除</a>
       					{{/if}}
					</div>
				</dl>
		   </li>
	</ul>
</div>
{{/for}}
</script>

<script type="text/javascript">
$(function(){
});

function initEducationData(masterReviewVO){
	bulidEducation(masterReviewVO.educationVOs);
}

//学历情况
function bulidEducation(educationVOs){
    //debugger;
	$("#educationRowNum").val(educationVOs.length);
	//$("#educationRefill").empty();
	if(educationVOs.length>0){
		 var dataObject = {'Data': []};
		 for(var i =0;i<educationVOs.length;i++){
			 var headmaster_educations =  Brightcom.workflow.getSelectCombox('headmaster_education');
			 educationVOs[i]['headmaster_education'] = headmaster_educations;
			 
			 var headmaster_degrees =  Brightcom.workflow.getSelectCombox('headmaster_degree');
			 educationVOs[i]['headmaster_degree'] = headmaster_degrees;
			 
			 var headmaster_study_forms =  Brightcom.workflow.getSelectCombox('headmaster_study_form');
			 educationVOs[i]['headmaster_study_form'] = headmaster_study_forms;
			 
			 var headmaster_education_types =  Brightcom.workflow.getSelectCombox('headmaster_education_type');
			 educationVOs[i]['headmaster_education_type'] = headmaster_education_types;
			 
			 
			 dataObject.Data.push(educationVOs[i]);
		 }
		 //debugger;
		// dataObject.Data['headmaster_education'] = headmaster_educations;
		 var subTaskContent= $("#educationRec").render(dataObject);
		 /*
		 for(var i =0;i<educationVOs.length;i++){
			 Brightcom.workflow.initSelectCombox('headmaster_education','education'+(i+1));
		 }*/
		 $("#educationRefill").append(subTaskContent);
		 
		 for(var i =0;i<educationVOs.length;i++){
			 Headmaster.initWebUploader('educationButton',(i+1),'educationType','上传学历证明','educationAttachId','educationButtonDiv');
			 Headmaster.initWebUploader('degreeButton',(i+1),'degreeType','上传学位证明','degreeAttachId','degreeButtonDiv');
		 }
	}
	//$("#educationRefill").append("<div class='add'><a class='add-more' href='javascript:void(0);' onclick='addEducationSingle(this)' >+</a></div> ");
}


function addEducationSingle(obj){
	var educationRowNum = parseInt($("#educationRowNum").val());
	var educationRowNumNext = parseInt(educationRowNum+1);

	var educationArray =[];
	educationArray.push("<div class='show-list radius-3'>"+educationRowNumNext+"、<a href='javascript:void(0);' target='_self' title='' onclick=\"deleteSingleOption(this,'educationType',\'"+educationRowNumNext+"\','');\" class='del radius-3'>删除</a></div>");
	educationArray.push("<div class='container'>");
	educationArray.push("<ul id='' class='clear-fix'>");
	educationArray.push("<input type='hidden' id='educationId"+educationRowNumNext+"' name='educationId' value=''>");
	educationArray.push("<input type='hidden' id='educationAttachId"+educationRowNumNext+"' value=''>");
	educationArray.push("<input type='hidden' id='degreeAttachId"+educationRowNumNext+"'  value=''>");
	
	educationArray.push("<li><span class='fl'>起止年月：</span>");
	educationArray.push("<div class='border_2 w_12 fl'>");
	educationArray.push("<input type='text' id='startTimeEducation"+educationRowNumNext+"' name='startTimeEducation"+educationRowNumNext+"' onclick='selectDeleteTime()' />");
	educationArray.push("</div>");
	educationArray.push("<span class='fl'>至</span>");
	educationArray.push("<div class='border_2 w_12 fl'>");
	educationArray.push("<input type='text' id='endTimeEducation"+educationRowNumNext+"' name='endTimeEducation"+educationRowNumNext+"' onclick='selectDeleteTime()' />");
	educationArray.push("</div>");
	educationArray.push("</li>");
	
	
	educationArray.push("<li><span class='fl'>就读院校：</span>");
	educationArray.push("<div class='border_2 w_13 fl'>");
	educationArray.push("<input type='text' id='study_schoolEducation"+educationRowNumNext+"' value='' placeholder=''>");
	educationArray.push("</div>");
	educationArray.push("</li>");
	
	
	educationArray.push("<li><span class='fl'>就读专业：</span>");
	educationArray.push("<div class='border_2 w_13 fl'>");
	educationArray.push("<input type='text' id='study_professionEducation"+educationRowNumNext+"' value='' placeholder=''>");
	educationArray.push("</div>");
	educationArray.push("</li>");
	
	educationArray.push("<li><span class='fl'>学习形式：</span>");
	educationArray.push("<div class='border_2 w_13 fl'>");
	educationArray.push("<select id='study_form"+educationRowNumNext+"' class='select_left'></select>");
	educationArray.push("</div>");
	educationArray.push("</li>");
	
	
	educationArray.push("<li><span class='fl'>教育类别：</span>");
	educationArray.push("<div class='border_2 w_13 fl'>");
	educationArray.push("<select id='education_type"+educationRowNumNext+"' class='select_left'></select>");
	educationArray.push("</div>");
	educationArray.push("</li>");
	
	educationArray.push("<li >");
	educationArray.push("<dl class='clear-fix'>");
	educationArray.push("	<dt class='clear-fix position_relative'>");
	educationArray.push("	<span class='fl'>学历：</span>");
	educationArray.push("<div class='border_2 w_21 fl' style='z-index:97;'>");
	educationArray.push("<select id='education"+educationRowNumNext+"'>");
	educationArray.push("</select>");
	educationArray.push("</div>");
	educationArray.push("<div id='educationButton"+educationRowNumNext+"' class='position_upload_button'></div> ");
	educationArray.push("</dt>");
	
	educationArray.push("<div id='educationButtonDiv"+educationRowNumNext+"' style='width:300px;heigth:0px;margin-left:10px;'></div>");
			//	<dd><a href="#" target="_self" title="">1234567890.jpg</a><span onclick="" class="fl radius-3">删除</span></dd>
	educationArray.push("<dt class='clear-fix position_relative' style='margin-top:20px;'>");
	educationArray.push("<span class='fl'>学位：</span>");
	educationArray.push("<div class='border_2 w_21 fl' style='z-index:96;'>");
	educationArray.push("<select id='degree"+educationRowNumNext+"'>");
	educationArray.push("</select>");
	educationArray.push("</div>");
	educationArray.push("<div id='degreeButton"+educationRowNumNext+"' class='position_upload_button'></div> ");
		//<input type="button" value="上传学历证明" class="up-load fr" />
	educationArray.push("</dt>");
	
	educationArray.push("<div id='degreeButtonDiv"+educationRowNumNext+"' style='width:300px;heigth:0px;margin-left:10px'></div>");
				//<dd><a href="#" target="_self" title="">1234567890.jpg</a><span onclick="" class="fl radius-3">删除</span></dd>
	educationArray.push("</dl>");

	//educationArray.push("<select style='float:left;' id='education"+educationRowNumNext+"'></select> ");
	//educationArray.push("<div style='margin-top:-3px;'  id='educationButton"+educationRowNumNext+"'></div> ");
	educationArray.push("</li>");
	
//	educationArray.push("<li style='height:45px;'><p>学历证明材料：</p><div style='padding-left:65px;' id='educationButton"+educationRowNumNext+"'></div> </li>");
	//educationArray.push("<div id='educationButtonDiv"+educationRowNumNext+"' style='width:300px;heigth:0px;margin-left:10px'></div>");
	
	//educationArray.push("<li><span class='fl'>学位：</span>");
	//educationArray.push("<select style='float:left;' id='degree"+educationRowNumNext+"'><option value='1'>本科</option></select>");
	//educationArray.push("<div style='margin-top:-3px;'  id='degreeButton"+educationRowNumNext+"'></div> ");
	//educationArray.push("</li>");
	
	//educationArray.push("<li style='height:45px;'><p>学位证明材料：</p><div style='padding-left:65px;' id='degreeButton"+educationRowNumNext+"'></div> </li>");
//	educationArray.push("<div id='degreeButtonDiv"+educationRowNumNext+"' style='width:300px;heigth:0px;margin-left:10px'></div>");
	
	educationArray.push("</ul>");
	educationArray.push("</div>");
	
	//educationArray.push("<div class='add'><a class='add-more' href='javascript:void(0);' onclick='addEducationSingle(this)' >+</a></div>");
	
	//$(obj).after(educationArray.join(""));
	//$(obj).remove();
	
	$("#educationRefill").append(educationArray.join(""));
	
	$("#educationRowNum").val(educationRowNumNext);
	Brightcom.workflow.initSelectCombox('headmaster_education','education'+(educationRowNumNext));
	Brightcom.workflow.initSelectCombox('headmaster_degree','degree'+(educationRowNumNext));
	Brightcom.workflow.initSelectCombox('headmaster_study_form','study_form'+(educationRowNumNext));
	Brightcom.workflow.initSelectCombox('headmaster_education_type','education_type'+(educationRowNumNext));
	
	/*
	 * 1、professionalTitlespan:上传按钮显示位置id
	 4、buttonName ： 按钮名称
	 5、hiddenAttachId：隐藏附件id，选中后把附件值保存在该隐藏域里
	 6、 hiddenDisplayId:附件显示的div id
	 */
	 Headmaster.initWebUploader('educationButton',educationRowNumNext,'educationType','上传学历证明','educationAttachId','educationButtonDiv');
	
	/*
	 * 1、professionalTitlespan:上传按钮显示位置id
	 4、buttonName ： 按钮名称
	 5、hiddenAttachId：隐藏附件id，选中后把附件值保存在该隐藏域里
	 6、 hiddenDisplayId:附件显示的div id
	 */
	 Headmaster.initWebUploader('degreeButton',educationRowNumNext,'educationType','上传学位证明','degreeAttachId','degreeButtonDiv');
}


function saveUpdateRefillData(){
	var isRefillTask = $("#isRefillTask").val();
	if(isRefillTask){
		var submitStrings =  getSubmitStrings();
		var bcReq = new BcRequest('headmaster','masterReviewAction','saveUpdateRefillData');
		bcReq.setExtraPs({   
			"option_tab_type":'education',
		    "option_tab_values":submitStrings,
		    "businessKey":processBusinessKey
		  });
		bcReq.setSuccFn(function(data,status){
			changeOption(3);
		});
		bcReq.postData();
	}else{
		submitRefillTask();
	}
}

function getSubmitStrings(){
	var submitArray = [];
	var educationRowNum = $("#educationRowNum").val();
	for(var i=0;i<educationRowNum;i++){
		var rowNum = (i+1);
		var id = $("#educationId"+rowNum).val();
		var startTime = $("#startTimeEducation"+rowNum).val();
		var endTime = $("#endTimeEducation"+rowNum).val();
		var study_school = $("#study_schoolEducation"+rowNum).val();
		var study_profession = $("#study_professionEducation"+rowNum).val();
		var education = $("#education"+rowNum).val();
		var degree = $("#degree"+rowNum).val();
		var educationAttachmentId = $("#educationAttachId"+rowNum).val();
		var degreeAttachmentId = $("#degreeAttachId"+rowNum).val();
		var study_form = $("#study_form"+rowNum).val();
		var education_type = $("#education_type"+rowNum).val();
		var businessKey = $("#id").val();
		if(!study_school){
			continue;
		}
		var workExperienceObject = {
				"id":id,
				"businessKey":$("#id").val(),
				"startTime":startTime,
				"endTime":endTime,
				"studySchool":study_school,
				'studyProfession' :study_profession,
				'education' :education,
				'degree' :degree,
				'educationAttachmentId' :educationAttachmentId,
				'degreeAttachmentId' :degreeAttachmentId,
				'study_form' :study_form,
				'education_type' :education_type
		}
		submitArray.push(workExperienceObject);
	}
	return JSON.stringify(submitArray);
}

function headmasterBeforeSubmit(formJsonData){
	formJsonData.option_tab_type = "education"
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
			<h2><i>2</i>学历情况</h2>
			<p>填写个人获得学历学位情况(从高中/中专填起)。</span>
		</div>
		<div class="select-step fr"><a href="javascript:void(0);" target="_self" title="" id="change">+&nbsp;切换步骤</a></div>
		<div class="clear-both"></div>
	</div>
	<!-- 标题 e -->
	<!-- 任职年限 s -->
	<div id="educationRefill" class="years">
	   <input type="hidden" id="educationRowNum" name="educationRowNum" value="0">
	</div>
	
	<!--  -->
	<div class="add"><a href="javascript:void(0);" onclick="addEducationSingle(this)" class="add-more">+</a></div>
	
	<!-- 任职年限 e -->
	<div class="next-step clear-fix">
		<a href="javascript:void(0);" target="_self" title="" class="fl" onclick="changeOption(1)">上一步</a>
		<a href="javascript:void(0);" target="_self" title="" class="fr" onclick="saveUpdateRefillData()">下一步</a>
	</div>

</body>
</html>