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
<title>������У��ְ������ϵͳ</title>

<!--ְ����� -->
<script id="professionalTitleRec" type="text/x-jsrender">	
{{for Data}}
    <div class='show-list radius-3'>{{:#index+1}}<a href='javascript:void(0);' target='_self'  onclick="deleteSingleOption(this,'professionalTitleType','{{:#index+1}}','{{:id}}');" class='del radius-3'>ɾ��</a></div>
	<div class="container">
	<ul class="clear-fix">
     <input type='hidden' id='professionalTitleId{{:#index+1}}'  value='{{:id}}'>
	 <input type='hidden' id='professionalAttachId{{:#index+1}}'  value='{{:attachMentVO.attachmentId}}'>
	<li>
      <span class="fl">ְ�����ƣ�</span>
      <div class="border_2 w_13 fl">
         <input type='text' id='professionaltitle_name{{:#index+1}}' value='{{:professionaltitle_name}}' placeholder=''>
      </div>
    </li>
	<li>
       <span class="fl">���ʱ�䣺</span>
      <div class="border_2 w_13 fl">
        <input type='text' id='obtain_time{{:#index+1}}' onclick='selectDeleteTime()' value='{{timeCovert obtainTime/}}'/>
      </div>
    </li>

    <li style='height:45px;' class='position_relative'>
      <span class="fl">֤�����ϣ�</span>
      <div id='professionalTitlespan{{:#index+1}}' class='position_upload_button_professional'></div>
    </li>

    <div id='professionalTitleDiv{{:#index+1}}' class='only_attachments'>
       {{if attachMentVO.attachmentId !==null}}
         <a class='chachu' href="<%=basePath%>WorkflowAttachMentDownload?attachmentId={{:attachMentVO.attachmentId}}">{{:attachMentVO.fileName}}</a>
         &nbsp;&nbsp;&nbsp;&nbsp;
         <a class='chachu'  href='javascript:void(0);' onclick='Headmaster.deleteReceiveFileAttachment("{{:attachMentVO.attachmentId}}",this);' >ɾ��</a>
       {{/if}}
    </div>

	</ul>
	</div>
{{/for}}	
</script>

<script type="text/javascript">
$(function(){
});

function initProfessionalData(masterReviewVO){
	bulidProfessionalTitle(masterReviewVO.professionalTitleVOs);
}

//ְ��
function bulidProfessionalTitle(professionalTitleVOs){
	$("#professionalTitleRowNum").val(professionalTitleVOs.length);
	if(professionalTitleVOs.length>0){
		 var dataObject = {'Data': []};
		 for(var i =0;i<professionalTitleVOs.length;i++){
			 dataObject.Data.push(professionalTitleVOs[i]);
		 }
		 var subTaskContent= $("#professionalTitleRec").render(dataObject);
		 $("#professionalTitleRefill").append(subTaskContent);
		 
		 for(var i =0;i<professionalTitleVOs.length;i++){
			 Headmaster.initWebUploader('professionalTitlespan',(i+1),'professionalTitleType','����ϴ�','professionalAttachId','professionalTitleDiv');
		 }
	}
	//$("#professionalTitleRefill").append("<div class='add'><a class='add-more' href='javascript:void(0);' onclick='addProfessionalTitleSingle(this)' >+</a></div> ");
}

function addProfessionalTitleSingle(obj){
	var professionalTitleRowNum = parseInt($("#professionalTitleRowNum").val());
	var professionalTitleRowNumNext = parseInt(professionalTitleRowNum+1);

	var educationArray =[];
	educationArray.push("<div class='show-list radius-3'>"+professionalTitleRowNumNext+"��<a href='javascript:void(0);' target='_self' onclick=\"deleteSingleOption(this,'professionalTitleType',\'"+professionalTitleRowNumNext+"\','');\" class='del radius-3'>ɾ��</a></div>");
	educationArray.push("<div class='container'>");
	educationArray.push("<ul id='' class='clear-fix'>");
	educationArray.push("<input type='hidden' id='professionalTitleId"+professionalTitleRowNumNext+"'  value=''>");
	educationArray.push("<input type='hidden' id='professionalAttachId"+professionalTitleRowNumNext+"'  value=''>");
	
	educationArray.push("<li>");
	educationArray.push(" <span class='fl'>ְ�����ƣ�</span>");
	educationArray.push("<div class='border_2 w_13 fl'>");
	educationArray.push("<input type='text' id='professionaltitle_name"+professionalTitleRowNumNext+"' value='' placeholder=''>");
	educationArray.push("</div>");
	educationArray.push(" </li>");
	
	educationArray.push("<li>");
	educationArray.push(" <span class='fl'>���ʱ�䣺</span>");
	educationArray.push("<div class='border_2 w_13 fl'>");
	educationArray.push("<input type='text' id='obtain_time"+professionalTitleRowNumNext+"' onclick='selectDeleteTime()' name='obtain_time"+professionalTitleRowNumNext+"' />");
	educationArray.push("</div>");
	educationArray.push(" </li>");
	
	/*
	educationArray.push("<li>");
	educationArray.push("<span class='fl'>���ѧУ��</span>");
	educationArray.push("<div class='border_2 w_13 fl'>");
	 educationArray.push(" <input type='text' id='obtain_school"+professionalTitleRowNumNext+"' value='' placeholder=''>");
	 educationArray.push("</div>");
	 educationArray.push("</li>");*/
	 
	educationArray.push("<li style='height:45px;' class='position_relative'>");
	educationArray.push(" <span class='fl'>֤�����ϣ�</span>");
	educationArray.push(" <div id='professionalTitlespan"+professionalTitleRowNumNext+"' class='position_upload_button_professional'></div>");
	educationArray.push("</li>");
	
	
	educationArray.push("<div id='professionalTitleDiv"+professionalTitleRowNumNext+"' class='only_attachments'></div>");
	educationArray.push("</ul>");
	educationArray.push("</div>");
	
	//educationArray.push("<div class='add'><a class='add-more' href='javascript:void(0);' onclick='addProfessionalTitleSingle(this)' >+</a></div>");
	
	//$(obj).after(educationArray.join(""));
	//$(obj).remove();
	$("#professionalTitleRefill").append(educationArray.join(""));
	$("#professionalTitleRowNum").val(professionalTitleRowNumNext);
	
	/*
	 * 1��professionalTitlespan:�ϴ���ť��ʾλ��id
	 4��buttonName �� ��ť����
	 5��hiddenAttachId�����ظ���id��ѡ�к�Ѹ���ֵ�����ڸ���������
	 6�� hiddenDisplayId:������ʾ��div id
	 */
	 Headmaster.initWebUploader('professionalTitlespan',professionalTitleRowNumNext,'professionalTitleType','����ϴ�','professionalAttachId','professionalTitleDiv');
}

function saveUpdateRefillData(){
	var isRefillTask = $("#isRefillTask").val();
	if(isRefillTask){
		var submitStrings =  getSubmitStrings();
		var bcReq = new BcRequest('headmaster','masterReviewAction','saveUpdateRefillData');
		bcReq.setExtraPs({			    
			"option_tab_type":'professionalTitle',
		    "option_tab_values":submitStrings,
		    "businessKey":processBusinessKey
		});
		bcReq.setSuccFn(function(data,status){
			changeOption(4);
		});
		bcReq.postData();
	}else{
		submitRefillTask();
	}
}

function getSubmitStrings(){
	var submitArray = [];
	var professionalTitleRowNum = $("#professionalTitleRowNum").val();
	for(var i=0;i<professionalTitleRowNum;i++){
		var rowNum = (i+1);
		var id = $("#professionalTitleId"+rowNum).val();
		var professionaltitle_name = $("#professionaltitle_name"+rowNum).val();
		var obtain_time = $("#obtain_time"+rowNum).val();
		//var obtain_school = $("#obtain_school"+rowNum).val();
		var attachment_id = $("#professionalAttachId"+rowNum).val();
		var businessKey = $("#id").val();
		if(!professionaltitle_name){
			continue;
		}
		var workExperienceObject = {
				"id":id,
				"businessKey":$("#id").val(),
				"professionaltitle_name":professionaltitle_name,
				"obtainTime":obtain_time,
			//	"obtainSchool":obtain_school,
				"professionalAttachId":attachment_id,
		}
		submitArray.push(workExperienceObject);
	}
	return JSON.stringify(submitArray);
}

function headmasterBeforeSubmit(formJsonData){
	formJsonData.option_tab_type = "professionalTitle"
	formJsonData.option_tab_values = getSubmitStrings();
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
			<h2><i>3</i>ְ�����</h2>
			<p>��д�������רҵ����ְ�񼴿ɡ�</span>
		</div>
		<div class="select-step fr"><a href="javascript:void(0);" target="_self" title="" id="change">+&nbsp;�л�����</a></div>
		<div class="clear-both"></div>
	</div>
	<!-- ���� e -->
	<!-- ��ְ���� s -->
	<div id="professionalTitleRefill" class="years">
		<input type="hidden" id="professionalTitleRowNum" name="professionalTitleRowNum" value="0"/>
		
	</div>
	
	
	<div class="add"><a href="javascript:void(0);" onclick="addProfessionalTitleSingle(this)" class="add-more">+</a></div>
	
	<!-- ��ְ���� e -->
	<div class="next-step clear-fix">
	 <a href="javascript:void(0);" target="_self" title="" class="fl" onclick="changeOption(2)">��һ��</a>
	 <a href="javascript:void(0);" target="_self" title="" class="fr" onclick="saveUpdateRefillData()">��һ��</a>
	</div>
</body>
</html>