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

<!--任职年限  -->
<script id="workExperienceRec" type="text/x-jsrender">	
{{for Data}}
	<div id='workExperienceHead{{:#index+1}}' class='tit2'>{{:#index+1}}、{{timeContent startTime endTime/}}&nbsp;&nbsp;{{:workSchool}}
      <span>
        <a href='javascript:void(0);' class='tit2-trigger' onclick='toggleDiv(this);'>收起</a>
	    <a href='javascript:void(0);' onclick="deleteSingleOption(this,'workExperienceType','{{:#index+1}}','');" >删除</a>
      </span>
    </div>
	<div class='tab-pane-sub'>
	<input type='hidden' id="workExperienceId{{:#index+1}}"  value='{{:id}}'>
	<input type='hidden' id="proveId{{:#index+1}}"  value='{{:proveAttachMentVO.attachmentId}}'>
	<ul>
	<li><p>起止年月：</p>
	<input type='text' id='startTimeExperience{{:#index+1}}'  onclick='selectDeleteTime()' value="{{timeCovert startTime/}}" style='width:100px;'/>
	<span>—</span>
	<input type='text' id='endTimeExperience{{:#index+1}}'  onclick='selectDeleteTime()' value="{{timeCovert endTime/}}" style='width:100px;'/>
	</li>
	<li><p>所在学校：</p><input type='text' id='workSchoolExperience{{:#index+1}}' value='{{:workSchool}}' placeholder=''></li>
	<li><p>担任职务：</p><input type='text' id='workProfession{{:#index+1}}' value='{{:workProfession}}' placeholder=''></li>
	<li><p>任职年限：</p><select name='' id='workYear{{:#index+1}}'><option value='2016' >2016</option></select></li>
    <li style='height:45px;'><p>证明材料：</p><div style='padding-left:65px;' id='spanButtonPlaceHolder{{:#index+1}}'></div> </li>

    <div id='workExperienceTypeDiv{{:#index+1}}' style='heigth:0px'>
       {{if proveAttachMentVO.attachmentId !==null}}
         <a class='chachu' href="<%=basePath%>WorkflowAttachMentDownload?attachmentId={{:proveAttachMentVO.attachmentId}}">{{:proveAttachMentVO.fileName}}</a>
         &nbsp;&nbsp;&nbsp;&nbsp;
         <a class='chachu'  href='javascript:void(0);' onclick='Headmaster.deleteReceiveFileAttachment("{{:proveAttachMentVO.attachmentId}}",this);' >删除</a>
       {{/if}}
    </div>
	<li class='btn'><input type='button' value='保 存' onclick="saveSingleOption('workExperienceType','{{:#index+1}}','');" ></li>
	</ul>
	</div>
{{/for}}
</script>

<script type="text/javascript">
$(function(){
});

function initStudyTrainData(masterReviewVO){
	bulidStudyTrain(masterReviewVO.workExperienceVOs);
}

//任职年限
function bulidStudyTrain(workExperienceVOs){
	$("#workExperienceRowNum").val(workExperienceVOs.length);
	if(workExperienceVOs.length>0){
		 var dataObject = {'Data': []};
		 for(var i =0;i<workExperienceVOs.length;i++){
			 dataObject.Data.push(workExperienceVOs[i]);
		 }
		 var subTaskContent= $("#workExperienceRec").render(dataObject);
		 $("#workExperienceRefill").append(subTaskContent);
		 
		 for(var i =0;i<workExperienceVOs.length;i++){
			 Headmaster.initWebUploader('spanButtonPlaceHolder',(i+1),'workExperienceType','点击上传','proveId','workExperienceTypeDiv');
		 }
	}else{
	
	}
	 $("#workExperienceRefill").append("<a class='add' href='javascript:void(0);' onclick='addWorkExperienceSingle(this)' >+</a>");
}

function addWorkExperienceSingle(obj){
	var workExperienceRowNum = parseInt($("#workExperienceRowNum").val());
	var workExperienceRowNumNext = parseInt(workExperienceRowNum+1);

	var educationArray =[];
	educationArray.push("<div id='workExperienceHead"+workExperienceRowNumNext+"' class='tit2'><span><a href='javascript:void(0);' class='tit2-trigger' onclick=\"toggleDiv(this);\">收起</a>");
	educationArray.push("<a href='javascript:void(0);' onclick=\"deleteSingleOption(this,'workExperienceType',\'"+workExperienceRowNumNext+"\','');\" >删除</a></span></div>");
	educationArray.push("<div class='tab-pane-sub'>");
	educationArray.push("<input type='hidden' id='workExperienceId"+workExperienceRowNumNext+"'  value=''>");
	educationArray.push("<input type='hidden' id='proveId"+workExperienceRowNumNext+"' value=''>");
	educationArray.push("<ul>");
	educationArray.push("<li><p>起止年月：</p>");
	educationArray.push("<input type='text' id='startTimeExperience"+workExperienceRowNumNext+"' name='startTimeExperience"+workExperienceRowNumNext+"' onclick='selectDeleteTime()' style='width:100px;'/>");
	educationArray.push("<span>—</span>");
	educationArray.push("<input type='text' id='endTimeExperience"+workExperienceRowNumNext+"' name='endTimeEducation"+workExperienceRowNumNext+"' onclick='selectDeleteTime()' style='width:100px;'/>");
	educationArray.push("</li>");
	educationArray.push("<li><p>所在学校：</p><input type='text' id='workSchoolExperience"+workExperienceRowNumNext+"' value='' placeholder=''></li>");
	educationArray.push("<li><p>担任职务：</p><input type='text' id='workProfession"+workExperienceRowNumNext+"' value='' placeholder=''></li>");
	educationArray.push("<li><p>任职年限：</p><select name='' id='workYear"+workExperienceRowNumNext+"'><option value='2016' >2016</option></select></li>");
	educationArray.push("<li style='height:45px;'><p>证明材料：</p><div style='padding-left:65px;' id='spanButtonPlaceHolder"+workExperienceRowNumNext+"'></div> </li>");
	educationArray.push("<div id='workExperienceTypeDiv"+workExperienceRowNumNext+"' style='heigth:0px'></div>");
	educationArray.push("<li class='btn'><input type='button' value='保 存' onclick=\"saveSingleOption('workExperienceType',\'"+workExperienceRowNumNext+"\','');\" ></li>");
	educationArray.push("</ul>");
	educationArray.push("</div>");
	
	educationArray.push("<a class='add' href='javascript:void(0);' onclick='addWorkExperienceSingle(this)' >+</a>");
	
	$(obj).after(educationArray.join(""));
	$(obj).remove();
	
	$("#workExperienceRowNum").val(workExperienceRowNumNext);
	
	Headmaster.initWebUploader('spanButtonPlaceHolder',workExperienceRowNumNext,'workExperienceType','点击上传','proveId','workExperienceTypeDiv');
	
	/*
	var swfuploadWorkExperience = 'swfupload'+workExperienceRowNumNext;
	var gg= {"rownum":workExperienceRowNumNext}

	var setting1 =  jQuery.extend(defaultSettings, {
			   button_placeholder_id :"spanButtonPlaceHolder"+workExperienceRowNumNext,
			   file_dialog_complete_handler : new function()  
			    {  
				       this.temp = workExperienceRowNumNext;  
						try {
							$("#fsUploadProgress").show();
							//document.getElementById("ScrollContent").scrollTop=document.getElementById("ScrollContent").scrollHeight;
							//$("#submit_flag").val("Y");
							var dd = 'swfupload'+this.temp;
							//swfuploadWorkExperience.addPostParam("row_number",gg.rownum);
							dd.addPostParam("file_upload_type",'workExperience');
							dd.startUpload(); //选择文件后立刻上传
						} catch (ex)  {		
							//dd.debug(ex);
						}
				}  
	*/
	
	/*
	function(){  
				  //  this.row_number = workExperienceRowNumNext;  
					try {
						$("#fsUploadProgress").show();
						//document.getElementById("ScrollContent").scrollTop=document.getElementById("ScrollContent").scrollHeight;
						//$("#submit_flag").val("Y");
						var dd = swfuploadWorkExperience;
						swfuploadWorkExperience.addPostParam("row_number",gg.rownum);
						swfuploadWorkExperience.addPostParam("file_upload_type",'workExperience');
						swfuploadWorkExperience.startUpload(); //选择文件后立刻上传
					} catch (ex)  {		
						swfuploadWorkExperience.debug(ex);
					}
			   }
	 })*/
	//setting1.rowNum = workExperienceRowNumNext;
//	swfuploadWorkExperience = new SWFUpload(setting1);
	//swfuploadWorkExperience.addPostParam("row_number",workExperienceRowNumNext);
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
			<h2><i>8</i>进修学习</h2>
			<p>评分标准：每超过任职最低年限1年，计0.5分；最高不超过5分。</p>
		</div>
		<div class="select-step fr"><a href="javascript:void(0);" target="_self" title="" id="change">+&nbsp;切换步骤</a></div>
		<div class="clear-both"></div>
	</div>
	<!-- 标题 e -->
	<!-- 任职年限 s -->
	<div class="years">
		<ul class="clear-fix">
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
			<li><a href="#" target="_self" title="" class="add-more">+</a></li>
		</ul>
	</div>
	<!-- 任职年限 e -->
	<div class="next-step clear-fix">
	   <a href="javascript:void(0);" target="_self" title="" class="fl" onclick="changeOption(7)">上一步</a>
	   <a href="javascript:void(0);" target="_self" title="" class="fr" onclick="changeOption(9)">下一步</a></div>
</body>
</html>