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

<!--ѧУ�����  -->
<script id="schoolAwardRec" type="text/x-jsrender">	
{{for Data}}
    <div class='show-list radius-3'>{{:#index+1}}<a href='javascript:void(0);' target='_self'  onclick="deleteSingleOption(this,'schoolAwardType','{{:#index+1}}','{{:id}}');" class='del radius-3'>ɾ��</a></div>
	 <div class="container">
    <input type='hidden' id='schoolAwardId{{:#index+1}}'  value='{{:id}}'>
	<input type='hidden' id='schoolAwardAttachId{{:#index+1}}'  value='{{:schoolAttachVO.attachmentId}}'>
	<ul class="clear-fix">
	<li>
     <span class='fl'>�������ƣ�</span>
     <div class="border_2 w_13 fl">
      <input type='text' id='awards_name{{:#index+1}}' value='{{:awardsName}}' placeholder=''>
     </div>
   </li>

	<li>
      <span class='fl'>��ְѧУ��</span>
     <div class="border_2 w_13 fl">
      <input type='text' id='work_school_award{{:#index+1}}' value='{{:workSchool}}' placeholder=''>
     </div>
    </li>

	<li>
     <span class='fl'>���õ�λ��</span>
     <div class="border_2 w_13 fl">
		<input type='text' id='awards_company{{:#index+1}}' value='{{:awardsCompany}}' placeholder=''>
     </div>
    </li>

	<li>
      <span class='fl'>�񽱼���</span>
     <div class="border_2 w_13 fl">
          <select  id="awards_level{{:#index+1}}" class='select_left'>
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
      <span class='fl'>����ʱ�䣺</span>
     <div class="border_2 w_13 fl">
      <input type='text' id='awards_time{{:#index+1}}' value='{{timeCovert awardsTime/}}' placeholder='' onclick='selectDeleteTime()'/>
     </div>
    </li>

	<li class='position_relative' style='height:45px;'><span class='fl'>��֤�飺</span> 
       <div class='position_upload_button_professional' id='schoolAwardbutton{{:#index+1}}'></div> 
    </li>

	<div id='schoolAwardbuttonDiv{{:#index+1}}' class='only_attachments'>
      {{if schoolAttachVO.attachmentId !==null}}
         <a class='chachu' href="<%=basePath%>WorkflowAttachMentDownload?attachmentId={{:schoolAttachVO.attachmentId}}">{{:schoolAttachVO.fileName}}</a>
         &nbsp;&nbsp;&nbsp;&nbsp;
         <a class='chachu'  href='javascript:void(0);' onclick='Headmaster.deleteReceiveFileAttachment("{{:schoolAttachVO.attachmentId}}",this);' >ɾ��</a>
       {{/if}}
    </div>

	</ul>
	</div>
{{/for}}
</script>

<script type="text/javascript">
$(function(){
});

function initSchoolAwardData(masterReviewVO){
	bulidSchoolAward(masterReviewVO.schoolAwardVOs);
}

//ѧУ��
function bulidSchoolAward(schoolAwardVOs){
	$("#schoolAwardRowNum").val(schoolAwardVOs.length);
	if(schoolAwardVOs.length>0){
		 var dataObject = {'Data': []};
		 for(var i =0;i<schoolAwardVOs.length;i++){
			 var headmaster_approve_levels =  Brightcom.workflow.getSelectCombox('headmaster_approve_level');
			 schoolAwardVOs[i]['headmaster_approve_level'] = headmaster_approve_levels;
			 
			 dataObject.Data.push(schoolAwardVOs[i]);
		 }
		 //debugger;
		 var subTaskContent= $("#schoolAwardRec").render(dataObject);
		 $("#schoolAwardRefill").append(subTaskContent);
		
		 for(var i =0;i<schoolAwardVOs.length;i++){
			 Headmaster.initWebUploader('schoolAwardbutton',(i+1),'schoolAwardType','����ϴ�','schoolAwardAttachId','schoolAwardbuttonDiv');
		 }
	}
	//$("#schoolAwardRefill").append("<div class='add'><a class='add-more' href='javascript:void(0);' onclick='addSchoolAwardSingle(this)' >+</a></div> ");
}


function addSchoolAwardSingle(obj){
	var schoolAwardRowNum = parseInt($("#schoolAwardRowNum").val());
	var schoolAwardRowNumNext = parseInt(schoolAwardRowNum+1);

	var educationArray =[];
	educationArray.push("<div class='show-list radius-3'>"+schoolAwardRowNumNext+"��<a href='javascript:void(0);' target='_self' onclick=\"deleteSingleOption(this,'schoolAward',\'"+schoolAwardRowNumNext+"\','');\" class='del radius-3'>ɾ��</a></div>");
	educationArray.push("<div class='container'>");
	educationArray.push("<ul class='clear-fix'>");
	educationArray.push("<input type='hidden' id='schoolAwardId"+schoolAwardRowNumNext+"'  value=''>");
	educationArray.push("<input type='hidden' id='schoolAwardAttachId"+schoolAwardRowNumNext+"'  value=''>");
	
	educationArray.push("<li>");
	educationArray.push("<span class='fl'>�������ƣ�</span>");
	educationArray.push("<div class='border_2 w_13 fl'>");
	educationArray.push("<input type='text' id='awards_name"+schoolAwardRowNumNext+"' value='' placeholder=''>");
	educationArray.push("</div>");
	educationArray.push("</li>");
	
	educationArray.push("<li>");
	educationArray.push("<span class='fl'>��ְѧУ��</span>");
	educationArray.push("<div class='border_2 w_13 fl'>");
	educationArray.push("<input type='text' id='work_school_award"+schoolAwardRowNumNext+"' value='' placeholder=''>");
	educationArray.push("</div>");
	educationArray.push("</li>");
	
	educationArray.push("<li>");
	educationArray.push("<span class='fl'>���õ�λ��</span>");
	educationArray.push("<div class='border_2 w_13 fl'>");
	educationArray.push("<input type='text' id='awards_company"+schoolAwardRowNumNext+"' value='' placeholder=''>"); 
	educationArray.push("</div>");
    educationArray.push("</li>");
	
	educationArray.push("<li>");
	educationArray.push("<span class='fl'>�񽱼���</span>");
	educationArray.push("<div class='border_2 w_13 fl'>");
	educationArray.push("<select id='awards_level"+schoolAwardRowNumNext+"' class='select_left'></select>");

	educationArray.push("</div>");
	educationArray.push("</li>");
	
	educationArray.push("<li>");
	educationArray.push("<span class='fl'>����ʱ�䣺</span>");
	educationArray.push("<div class='border_2 w_13 fl'>");
	educationArray.push("<input type='text' id='awards_time"+schoolAwardRowNumNext+"' value='' placeholder='' onclick='selectDeleteTime()'>");
	educationArray.push("</div>");
	educationArray.push("</li>");
	
	educationArray.push("<li style='height:45px;' class='position_relative'>");
	educationArray.push("<span class='fl'>��֤�飺</span>");
	educationArray.push("<div class='position_upload_button_professional' id='schoolAwardbutton"+schoolAwardRowNumNext+"'></div>");
	educationArray.push("</li>");
	
	educationArray.push("<div id='schoolAwardbuttonDiv"+schoolAwardRowNumNext+"' class='only_attachments'></div>");
	
	
	educationArray.push("</ul>");
	educationArray.push("</div>");
	
	//educationArray.push("<div class='add'><a class='add-more' href='javascript:void(0);' onclick='addSchoolAwardSingle(this)' >+</a></div>");
	
	//$(obj).after(educationArray.join(""));
	//$(obj).remove();
	$("#schoolAwardRefill").append(educationArray.join(""));
	
	$("#schoolAwardRowNum").val(schoolAwardRowNumNext);
	
	Brightcom.workflow.initSelectCombox('headmaster_approve_level','awards_level'+(schoolAwardRowNumNext));
	
	/*
	 * 1��professionalTitlespan:�ϴ���ť��ʾλ��id
	 4��buttonName �� ��ť����
	 5��hiddenAttachId�����ظ���id��ѡ�к�Ѹ���ֵ�����ڸ���������
	 6�� hiddenDisplayId:������ʾ��div id
	 */
	 Headmaster.initWebUploader('schoolAwardbutton',schoolAwardRowNumNext,'schoolAwardType','����ϴ�','schoolAwardAttachId','schoolAwardbuttonDiv');
}

function saveUpdateRefillData(){
	var isRefillTask = $("#isRefillTask").val();
	if(isRefillTask){
		var submitStrings =  getSubmitStrings();
		var bcReq = new BcRequest('headmaster','masterReviewAction','saveUpdateRefillData');
		bcReq.setExtraPs({ 
			"option_tab_type":'schoolAward',
		    "option_tab_values":submitStrings,
		    "businessKey":processBusinessKey
		    });
		bcReq.setSuccFn(function(data,status){
			changeOption(17);
		});
		bcReq.postData();
	}else{
		submitRefillTask();
	}
}

function getSubmitStrings(){
	var submitArray = [];
	var schoolAwardRowNum = $("#schoolAwardRowNum").val();
	for(var i=0;i<schoolAwardRowNum;i++){
		var rowNum = (i+1);
		var id = $("#schoolAwardId"+rowNum).val();
		var awards_name = $("#awards_name"+rowNum).val();
		var work_school_award = $("#work_school_award"+rowNum).val();
		var awards_company = $("#awards_company"+rowNum).val();
		var awards_level = $("#awards_level"+rowNum).val();
		var awards_time = $("#awards_time"+rowNum).val();
		var awards_attachment_id = $("#schoolAwardAttachId"+rowNum).val();
		var businessKey = $("#id").val();
		if(!awards_name){
			continue;
		}
		var workExperienceObject = {
				"id":id,
				"businessKey":$("#id").val(),
				"awards_name":awards_name,
				"work_school":work_school_award,
				"awards_company":awards_company,
				"awards_level":awards_level,
				"awards_time":awards_time,
				"awards_attachment_id":awards_attachment_id,
		}
		submitArray.push(workExperienceObject);
	}
	return JSON.stringify(submitArray);
}

function headmasterBeforeSubmit(formJsonData){
	formJsonData.option_tab_type = "schoolAward"
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
			<h2><i>16</i>ѧУ�����</h2>
			<p>��д��У���ڼ䣬ѧУ����м����ϵ�ί����������������������������ͱ��á�</p>
		</div>
		<div class="select-step fr"><a href="javascript:void(0);" target="_self" title="" id="change">+&nbsp;�л�����</a></div>
		<div class="clear-both"></div>
	</div>
	<!-- ���� e -->
	<!-- ��ְ���� s -->
	<div id="schoolAwardRefill" class="years">
		<input type="hidden" id="schoolAwardRowNum" name="schoolAwardRowNum" value="0">
	</div>
	
	<div class="add"><a href="javascript:void(0);" onclick="addSchoolAwardSingle(this)" class="add-more">+</a></div>
	
	
	<!-- ��ְ���� e -->
	<div class="next-step clear-fix">
	   <a href="javascript:void(0);" target="_self" title="" class="fl" onclick="changeOption(15)">��һ��</a>
	   <a href="javascript:void(0);" target="_self" title="" class="fr" onclick="saveUpdateRefillData()">��һ��</a>
	</div>

</body>
</html>