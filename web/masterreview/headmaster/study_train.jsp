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
<title>������У��ְ������ϵͳ</title>

<!--��ְ����  -->
<script id="workExperienceRec" type="text/x-jsrender">	
{{for Data}}
	<div id='workExperienceHead{{:#index+1}}' class='tit2'>{{:#index+1}}��{{timeContent startTime endTime/}}&nbsp;&nbsp;{{:workSchool}}
      <span>
        <a href='javascript:void(0);' class='tit2-trigger' onclick='toggleDiv(this);'>����</a>
	    <a href='javascript:void(0);' onclick="deleteSingleOption(this,'workExperienceType','{{:#index+1}}','');" >ɾ��</a>
      </span>
    </div>
	<div class='tab-pane-sub'>
	<input type='hidden' id="workExperienceId{{:#index+1}}"  value='{{:id}}'>
	<input type='hidden' id="proveId{{:#index+1}}"  value='{{:proveAttachMentVO.attachmentId}}'>
	<ul>
	<li><p>��ֹ���£�</p>
	<input type='text' id='startTimeExperience{{:#index+1}}'  onclick='selectDeleteTime()' value="{{timeCovert startTime/}}" style='width:100px;'/>
	<span>��</span>
	<input type='text' id='endTimeExperience{{:#index+1}}'  onclick='selectDeleteTime()' value="{{timeCovert endTime/}}" style='width:100px;'/>
	</li>
	<li><p>����ѧУ��</p><input type='text' id='workSchoolExperience{{:#index+1}}' value='{{:workSchool}}' placeholder=''></li>
	<li><p>����ְ��</p><input type='text' id='workProfession{{:#index+1}}' value='{{:workProfession}}' placeholder=''></li>
	<li><p>��ְ���ޣ�</p><select name='' id='workYear{{:#index+1}}'><option value='2016' >2016</option></select></li>
    <li style='height:45px;'><p>֤�����ϣ�</p><div style='padding-left:65px;' id='spanButtonPlaceHolder{{:#index+1}}'></div> </li>

    <div id='workExperienceTypeDiv{{:#index+1}}' style='heigth:0px'>
       {{if proveAttachMentVO.attachmentId !==null}}
         <a class='chachu' href="<%=basePath%>WorkflowAttachMentDownload?attachmentId={{:proveAttachMentVO.attachmentId}}">{{:proveAttachMentVO.fileName}}</a>
         &nbsp;&nbsp;&nbsp;&nbsp;
         <a class='chachu'  href='javascript:void(0);' onclick='Headmaster.deleteReceiveFileAttachment("{{:proveAttachMentVO.attachmentId}}",this);' >ɾ��</a>
       {{/if}}
    </div>
	<li class='btn'><input type='button' value='�� ��' onclick="saveSingleOption('workExperienceType','{{:#index+1}}','');" ></li>
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

//��ְ����
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
			 Headmaster.initWebUploader('spanButtonPlaceHolder',(i+1),'workExperienceType','����ϴ�','proveId','workExperienceTypeDiv');
		 }
	}else{
	
	}
	 $("#workExperienceRefill").append("<a class='add' href='javascript:void(0);' onclick='addWorkExperienceSingle(this)' >+</a>");
}

function addWorkExperienceSingle(obj){
	var workExperienceRowNum = parseInt($("#workExperienceRowNum").val());
	var workExperienceRowNumNext = parseInt(workExperienceRowNum+1);

	var educationArray =[];
	educationArray.push("<div id='workExperienceHead"+workExperienceRowNumNext+"' class='tit2'><span><a href='javascript:void(0);' class='tit2-trigger' onclick=\"toggleDiv(this);\">����</a>");
	educationArray.push("<a href='javascript:void(0);' onclick=\"deleteSingleOption(this,'workExperienceType',\'"+workExperienceRowNumNext+"\','');\" >ɾ��</a></span></div>");
	educationArray.push("<div class='tab-pane-sub'>");
	educationArray.push("<input type='hidden' id='workExperienceId"+workExperienceRowNumNext+"'  value=''>");
	educationArray.push("<input type='hidden' id='proveId"+workExperienceRowNumNext+"' value=''>");
	educationArray.push("<ul>");
	educationArray.push("<li><p>��ֹ���£�</p>");
	educationArray.push("<input type='text' id='startTimeExperience"+workExperienceRowNumNext+"' name='startTimeExperience"+workExperienceRowNumNext+"' onclick='selectDeleteTime()' style='width:100px;'/>");
	educationArray.push("<span>��</span>");
	educationArray.push("<input type='text' id='endTimeExperience"+workExperienceRowNumNext+"' name='endTimeEducation"+workExperienceRowNumNext+"' onclick='selectDeleteTime()' style='width:100px;'/>");
	educationArray.push("</li>");
	educationArray.push("<li><p>����ѧУ��</p><input type='text' id='workSchoolExperience"+workExperienceRowNumNext+"' value='' placeholder=''></li>");
	educationArray.push("<li><p>����ְ��</p><input type='text' id='workProfession"+workExperienceRowNumNext+"' value='' placeholder=''></li>");
	educationArray.push("<li><p>��ְ���ޣ�</p><select name='' id='workYear"+workExperienceRowNumNext+"'><option value='2016' >2016</option></select></li>");
	educationArray.push("<li style='height:45px;'><p>֤�����ϣ�</p><div style='padding-left:65px;' id='spanButtonPlaceHolder"+workExperienceRowNumNext+"'></div> </li>");
	educationArray.push("<div id='workExperienceTypeDiv"+workExperienceRowNumNext+"' style='heigth:0px'></div>");
	educationArray.push("<li class='btn'><input type='button' value='�� ��' onclick=\"saveSingleOption('workExperienceType',\'"+workExperienceRowNumNext+"\','');\" ></li>");
	educationArray.push("</ul>");
	educationArray.push("</div>");
	
	educationArray.push("<a class='add' href='javascript:void(0);' onclick='addWorkExperienceSingle(this)' >+</a>");
	
	$(obj).after(educationArray.join(""));
	$(obj).remove();
	
	$("#workExperienceRowNum").val(workExperienceRowNumNext);
	
	Headmaster.initWebUploader('spanButtonPlaceHolder',workExperienceRowNumNext,'workExperienceType','����ϴ�','proveId','workExperienceTypeDiv');
	
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
							dd.startUpload(); //ѡ���ļ��������ϴ�
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
						swfuploadWorkExperience.startUpload(); //ѡ���ļ��������ϴ�
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
	<!-- ���� s -->
	<div class="grogress"><div class="line" style="width:92px;"><!-- 970/21 --></div></div>
	<!-- ���� e -->
	<!-- ���� s -->
	<div class="com-title">
		<div class="txt fl">
			<h2><i>8</i>����ѧϰ</h2>
			<p>���ֱ�׼��ÿ������ְ�������1�꣬��0.5�֣���߲�����5�֡�</p>
		</div>
		<div class="select-step fr"><a href="javascript:void(0);" target="_self" title="" id="change">+&nbsp;�л�����</a></div>
		<div class="clear-both"></div>
	</div>
	<!-- ���� e -->
	<!-- ��ְ���� s -->
	<div class="years">
		<ul class="clear-fix">
			<li><a href="#" target="_self" title="" class="add-more">+</a></li>
			<li>
				<span class="fl">��ֹ���£�</span>
				<div class="border_2 w_12 fl"><input type="text" name="" value="" placeholder="��-��-��" id="date-04" /></div>
				<span class="fl">��</span>
				<div class="border_2 w_12 fl"><input type="text" name="" value="" placeholder="��-��-��" id="date-05" /></div>
			</li>
			<li>
				<span class="fl">����ѧУ��</span>
				<div class="border_2 w_13 fl"><input type="text" name="" value="" placeholder="������ѧУȫ��" /></div>
			</li>
			<li>
				<span class="fl">����ְ��</span>
				<div class="border_2 w_13 fl"><input type="text" name="" value="" placeholder="������ְ��" /></div>
			</li>
			<li>
				<span class="fl">��ְ���ޣ�</span>
				<div class="border_2 w_14 fl"><input type="number" name="" value="" placeholder="����������" /></div>
				<span class="fl">��</span>
			</li>
			<li>
				<span class="fl">֤�����ϣ�</span>
				<input type="button" value="����ϴ�" class="up-load fl" />
			</li>
			<li><a href="#" target="_self" title="" class="add-more">+</a></li>
			<li>
				<span class="fl">��ֹ���£�</span>
				<div class="border_2 w_12 fl"><input type="text" name="" value="" placeholder="��-��-��" id="date-04" /></div>
				<span class="fl">��</span>
				<div class="border_2 w_12 fl"><input type="text" name="" value="" placeholder="��-��-��" id="date-05" /></div>
			</li>
			<li>
				<span class="fl">����ѧУ��</span>
				<div class="border_2 w_13 fl"><input type="text" name="" value="" placeholder="������ѧУȫ��" /></div>
			</li>
			<li>
				<span class="fl">����ְ��</span>
				<div class="border_2 w_13 fl"><input type="text" name="" value="" placeholder="������ְ��" /></div>
			</li>
			<li>
				<span class="fl">��ְ���ޣ�</span>
				<div class="border_2 w_14 fl"><input type="number" name="" value="" placeholder="����������" /></div>
				<span class="fl">��</span>
			</li>
			<li>
				<span class="fl">֤�����ϣ�</span>
				<input type="button" value="����ϴ�" class="up-load fl" />
			</li>
			<li><a href="#" target="_self" title="" class="add-more">+</a></li>
		</ul>
	</div>
	<!-- ��ְ���� e -->
	<div class="next-step clear-fix">
	   <a href="javascript:void(0);" target="_self" title="" class="fl" onclick="changeOption(7)">��һ��</a>
	   <a href="javascript:void(0);" target="_self" title="" class="fr" onclick="changeOption(9)">��һ��</a></div>
</body>
</html>