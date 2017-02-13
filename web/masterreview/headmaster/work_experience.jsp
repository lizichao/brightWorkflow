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

<!--��ְ����  -->
<script id="workExperienceRec" type="text/x-jsrender">	
{{for Data}}
    <div class='show-list radius-3'>{{:#index+1}}<a href='javascript:void(0);' target='_self'  onclick="deleteSingleOption(this,'workExperienceType','{{:#index+1}}','{{:id}}');" class='del radius-3'>ɾ��</a></div>
   <div class="container">
    <input type='hidden' id="workExperienceId{{:#index+1}}"  value='{{:id}}'>
	<input type='hidden' id="proveId{{:#index+1}}"  value='{{:proveAttachMentVO.attachmentId}}'>
	<ul class="clear-fix">
	<li>
        <span class="fl">��ֹ���£�</span>		
        <div class="border_2 w_12 fl">
          <input type="text" id='startTimeExperience{{:#index+1}}' onFocus='selectDeleteTime()' value="{{timeCovert startTime/}}"/>
        </div>
		<span class="fl">��</span>
		<div class="border_2 w_12 fl">
          <input type="text" id='endTimeExperience{{:#index+1}}'  onFocus='selectDeleteTime()' value="{{timeCovert endTime/}}" />
        </div>
	</li>
	<li>
        <span class="fl">����ѧУ��</span>
        <div class="border_2 w_13 fl">
          <input type="text" id='workSchoolExperience{{:#index+1}}' value='{{:workSchool}}' placeholder="������ѧУȫ��" />
        </div>
    </li>
	<li>
         <span class="fl">����ְ��</span>
         <div class="border_2 w_13 fl"><input type="text" id='workProfession{{:#index+1}}' value='{{:workProfession}}' placeholder="������ְ��" /></div>
    </li>
	<li>
       <span class='fl'>���������λ����</span>
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
      <span class="fl">��ְ���ޣ�</span>
      <div class="border_2 w_14 fl">
         <label id='workYear{{:#index+1}}'> {{:yearDiffDate}}</label>
      </div>
      <span class="fl">��</span>
      <div class="border_2 w_14 fl">
         <label id='workMonth{{:#index+1}}'> {{:monthDiffDate}}</label>
       </div>
      <span class="fl">��</span>
    </li>
    <li style='height:45px;' class='position_relative'>
     <span class="fl">֤�����ϣ�</span>
     <div style='padding-left:4px;' id='spanButtonPlaceHolder{{:#index+1}}' class='position_upload_button_professional'></div> </li>

     <div id='workExperienceTypeDiv{{:#index+1}}' class='only_attachments'>
       {{if proveAttachMentVO.attachmentId !==null}}
         <a class='chachu' href="<%=basePath%>WorkflowAttachMentDownload?attachmentId={{:proveAttachMentVO.attachmentId}}">{{:proveAttachMentVO.fileName}}</a>
         &nbsp;&nbsp;&nbsp;&nbsp;
         <a class='chachu'  href='javascript:void(0);' onclick='Headmaster.deleteReceiveFileAttachment("{{:proveAttachMentVO.attachmentId}}",this);' >ɾ��</a>
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

//��ְ����
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
			 Headmaster.initWebUploader('spanButtonPlaceHolder',(i+1),'workExperienceType','����ϴ�','proveId','workExperienceTypeDiv');
		 }
	}
	//$("#workExperienceRefill").append("<div class='add'><a class='add-more' href='javascript:void(0);' onclick='addWorkExperienceSingle(this)' >+</a></div> ");
}

function addWorkExperienceSingle(obj){
	var workExperienceRowNum = parseInt($("#workExperienceRowNum").val());
	var workExperienceRowNumNext = parseInt(workExperienceRowNum+1);

	var educationArray =[];
	educationArray.push("<div class='show-list radius-3'>"+workExperienceRowNumNext+"��<a href='javascript:void(0);' target='_self' onclick=\"deleteSingleOption(this,'workExperience',\'"+workExperienceRowNumNext+"\','');\" class='del radius-3'>ɾ��</a></div>");
	educationArray.push("<div class='container'>");
	educationArray.push("<ul id='' class='clear-fix'>");
	//educationArray.push("<div id='workExperienceHead"+workExperienceRowNumNext+"' class='tit2'><span><a href='javascript:void(0);' class='tit2-trigger' onclick=\"toggleDiv(this);\">����</a>");
	//educationArray.push("<a href='javascript:void(0);' onclick=\"deleteSingleOption(this,'workExperienceType',\'"+workExperienceRowNumNext+"\','');\" >ɾ��</a></span></div>");
	
	//educationArray.push("<div class='tab-pane-sub'>");
	educationArray.push("<input type='hidden' id='workExperienceId"+workExperienceRowNumNext+"'  value=''>");
	educationArray.push("<input type='hidden' id='proveId"+workExperienceRowNumNext+"' value=''>");
	//educationArray.push("<ul>");
	educationArray.push("<li><span class='fl'>��ֹ���£�</span>");
	educationArray.push("<div class='border_2 w_12 fl'>");
	educationArray.push("<input type='text' id='startTimeExperience"+workExperienceRowNumNext+"' name='startTimeExperience"+workExperienceRowNumNext+"' onFocus='selectDeleteTime()'/>");
	educationArray.push("</div>");
	educationArray.push("<span class='fl'>��</span>");
	educationArray.push("<div class='border_2 w_12 fl'>");
	educationArray.push("<input type='text' id='endTimeExperience"+workExperienceRowNumNext+"' name='endTimeEducation"+workExperienceRowNumNext+"' onFocus='selectDeleteTime()'/>");
	educationArray.push("</div>");
	educationArray.push("</li>");
	
	educationArray.push("<li><span class='fl'>����ѧУ��</span>");
	educationArray.push("<div class='border_2 w_13 fl'>");
	educationArray.push("<input type='text' id='workSchoolExperience"+workExperienceRowNumNext+"' value='' placeholder='������ѧУȫ��'>");
	educationArray.push("</div>");
	educationArray.push("</li>");
	
	educationArray.push("<li><span class='fl'>����ְ��</span>");
	educationArray.push("<div class='border_2 w_13 fl'>");
	educationArray.push("<input type='text' id='workProfession"+workExperienceRowNumNext+"' value='' placeholder='������ְ��'>");
	educationArray.push("</div>");
	educationArray.push("</li>");
	
	
	educationArray.push("<li><span class='fl'>���������λ����</span>");
	educationArray.push("<div class='border_2 w_19 fl'>");
	educationArray.push("<select id='manage_level"+workExperienceRowNumNext+"' class='select_left'></select>");
	educationArray.push("</div>");
	educationArray.push("</li>");
	
	educationArray.push("<li><span class='fl'>��ְ���ޣ�</span>");
	educationArray.push("<div class='border_2 w_14 fl'>");
//	educationArray.push("<input type='number' id='workYear"+workExperienceRowNumNext+"'   value='' placeholder='����������' />");
	educationArray.push("<label id='workYear"+workExperienceRowNumNext+"' ></label>");
	educationArray.push("</div>");
	educationArray.push("<span class='fl'>��</span>");
	
	educationArray.push("<div class='border_2 w_14 fl'>");
//	educationArray.push("<input type='number' id='workYear"+workExperienceRowNumNext+"'   value='' placeholder='����������' />");
	educationArray.push("<label id='workMonth"+workExperienceRowNumNext+"' ></label>");
	educationArray.push("</div>");
	educationArray.push("<span class='fl'>��</span>");
	educationArray.push("</li>");
	
	educationArray.push("<li style='height:45px;' class='position_relative'>");
	educationArray.push("<span class='fl'>֤�����ϣ�</span><div id='spanButtonPlaceHolder"+workExperienceRowNumNext+"'  class='position_upload_button_professional'></div> ");
	educationArray.push("</li>");
	
	educationArray.push("<div id='workExperienceTypeDiv"+workExperienceRowNumNext+"' class='only_attachments'></div>");
	//educationArray.push("<li class='btn'><input type='button' value='�� ��' onclick=\"saveSingleOption('workExperienceType',\'"+workExperienceRowNumNext+"\','');\" ></li>");
	educationArray.push("</ul>");
	educationArray.push("</div>");
	
	//educationArray.push("<div class='add'><a class='add-more' href='javascript:void(0);' onclick='addWorkExperienceSingle(this)' >+</a></div>");
	
	//$(obj).after(educationArray.join(""));
	//$(obj).remove();
	
	$("#workExperienceRefill").append(educationArray.join(""));
	
	$("#workExperienceRowNum").val(workExperienceRowNumNext);
	Brightcom.workflow.initSelectCombox('headmaster_manage_level','manage_level'+(workExperienceRowNumNext));
	
	Headmaster.initWebUploader('spanButtonPlaceHolder',workExperienceRowNumNext,'workExperienceType','����ϴ�','proveId','workExperienceTypeDiv');
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
	<!-- ���� s -->
	<div class="grogress"><div class="line" style="width:92px;"><!-- 970/21 --></div></div>
	<!-- ���� e -->
	<!-- ���� s -->
	<div class="com-title">
		<div class="txt fl">
			<h2><i>5</i>У��(��У��)��ְ����</h2>
			<p>1����д��������У�����������2��������У����ְ���ް�ѧ��ȼ��㡣һѧ����ʵ����ְʱ���ۻ��������ģ�������٣������겻��Ϊ��׷���ޣ���������ģ�������ְ���ް�һ����㣻У����ְ���޼����ֹ��2016��*��*�գ�У����ְʱ������ְ�ļ�Ϊ׼��3�������������š�����ϵͳ������ҵ��λ��ѧУ֮����Ա�໥���εģ������ǰ�����������λ������ͬ����У�쵼��ְ���޿��������㣬�侭��������д��</p>
		</div>
		<div class="select-step fr"><a href="javascript:void(0);" target="_self" title="" id="change">+&nbsp;�л�����</a></div>
		<div class="clear-both"></div>
	</div>
	<!-- ���� e -->
	<!-- ��ְ���� s -->
	<div id= "workExperienceRefill" class="years">
		<input type="hidden" id="workExperienceRowNum" name="workExperienceRowNum" value="0">
		<!-- 
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
			<li><a href="#" target="_self" title="" class="add-more">+</a></li> -->
	</div>
	
	<div class="add"><a href="javascript:void(0);" onclick="addWorkExperienceSingle(this)" class="add-more">+</a></div>
	
	<!-- ��ְ���� e -->
	<div class="next-step clear-fix">
	  <a href="javascript:void(0);" target="_self" title="" class="fl" onclick="changeOption(4)">��һ��</a>
	  <a href="javascript:void(0);" target="_self" title="" class="fr" onclick="saveUpdateRefillData()">��һ��</a>
	</div>

</body>
</html>