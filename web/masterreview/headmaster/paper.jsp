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

<!--���ķ������  -->
<script id="paperRec" type="text/x-jsrender">	
{{for Data}}
<div class='show-list radius-3 deleteDiv'>{{:#index+1}}<a href='#' target='_self' onclick="deleteSingleOption(this,'paperType','{{:#index+1}}','{{:id}}');" class='del radius-3'>ɾ��</a></div>
  <div class="container">
	<ul class="clear-fix">
	  <input type='hidden' id='paperId{{:#index+1}}'  value='{{:id}}'>
	  <input type='hidden' id='paperAttachId{{:#index+1}}'  value='{{:paperAttachMentVO.attachmentId}}'>
	<li><span class="fl">��Ŀ��</span>
      <div class="border_2 w_15 fl">
        <input type='text' id='title{{:#index+1}}' value='{{:title}}' placeholder=''>
      </div>
   </li>
	<li><span class='fl'>��������ʱ�䣺</span>
      <div class="border_2 w_16 fl">
        <input type='text' id='publish_time_paper{{:#index+1}}' value='{{timeCovert publishTime/}}' placeholder='' onclick='selectDeleteTime()'>
     </div>
    </li>
	<li><span class='fl'>��־������ѧ�����ƣ�</span>
       <div class="border_2 w_27 fl">
        <input type='text' id='magazine_meet_name{{:#index+1}}' value='{{:magazineMeetName}}'/>
    </li>
	<li><span class='fl'>���쵥λ��</span>
      <div class="border_2 w_18 fl">
       <input type='text' id='organizers{{:#index+1}}' value='{{:organizers}}' placeholder=''>
      </div>
    </li>
	<li><span class='fl'>�������Ļ������ƣ�</span>
      <div class="border_2 w_17 fl">
       <input type='text' id='paper_meet_name{{:#index+1}}' value='{{:paperMeetName}}' placeholder=''>
     </div>
    </li>
	<li><span class='fl'>���쵥λ����</span>
       <div class="border_2 w_19 fl">
         <select   id="organizers_level{{:#index+1}}" class='select_left'>
                   {{for headmaster_approve_level}}
                     {{if id===#parent.parent.data.organizersLevel }}
				       <option value='{{:id}}' selected='selected'>{{:text}}</option>
				    {{else}}
				       <option value='{{:id}}' >{{:text}}</option>
				    {{/if}}
                    {{/for}}
          </select>
      </div>
   </li>
	<li><span class='fl'>���ţ�</span>
     <div class="border_2 w_15 fl">
        <input type='text' id='paper_number{{:#index+1}}' value='{{:paperNumber}}' placeholder=''>
     </div>
    </li>
	
	<li><span class='fl'>���˳е����֣�</span> 
      <div class="border_2 w_19 fl">
       <input type='text' id='personal_part{{:#index+1}}' value='{{:personalPart}}' placeholder=''>
     </div>
   </li>
 
	<li><span class='fl'>��ɷ�ʽ��</span>
     <div class="border_2 w_18 fl">
         <select id="complete_way{{:#index+1}}" class='select_left'>
                   {{for headmaster_complete_form}}
                     {{if id===#parent.parent.data.complete_way }}
				       <option value='{{:id}}' selected='selected'>{{:text}}</option>
				    {{else}}
				       <option value='{{:id}}' >{{:text}}</option>
				    {{/if}}
                    {{/for}}
          </select>
      </div>
    </li>

	<li><span class='fl'>��������</span>
      <div class="border_2 w_18 fl">
        <select  id="author_order{{:#index+1}}" class='select_left'>
                   {{for headmaster_author_sort}}
                     {{if id===#parent.parent.data.author_order }}
				       <option value='{{:id}}' selected='selected'>{{:text}}</option>
				    {{else}}
				       <option value='{{:id}}' >{{:text}}</option>
				    {{/if}}
                    {{/for}}
          </select>
      </div>
    </li>

	<li><span class='fl'>���浥λ��</span>
      <div class="border_2 w_18 fl">
       <input type='text' id='publish_company{{:#index+1}}' value='{{:publish_company}}' placeholder=''>
      </div>
    </li>
	
	<li></li>	

    <li style='height:45px;' class='position_relative'><span class='fl'>����ɨ�����</span>
      <div id='paperbutton{{:#index+1}}' class='position_upload_button_professional' style='left: 96px;'></div>
    </li>
    <div class='attachments'>
		<div id='paperbuttonDiv{{:#index+1}}' class='listInfo'>
			{{if paperAttachMentVO.attachmentId !==null}}
         		<a class='chachu' href="<%=basePath%>WorkflowAttachMentDownload?attachmentId={{:paperAttachMentVO.attachmentId}}">{{:paperAttachMentVO.fileName}}</a>
         			&nbsp;&nbsp;&nbsp;&nbsp;
         		<a class='chachu'  href='javascript:void(0);' onclick='Headmaster.deleteReceiveFileAttachment("{{:paperAttachMentVO.attachmentId}}",this);' >ɾ��</a>
       		{{/if}}
		</div>
	</div>
    {{!--
    <div id='paperbuttonDiv{{:#index+1}}' style='heigth:0px'>
       {{if paperAttachMentVO.attachmentId !==null}}
         <a class='chachu' href="<%=basePath%>WorkflowAttachMentDownload?attachmentId={{:paperAttachMentVO.attachmentId}}">{{:paperAttachMentVO.fileName}}</a>
         &nbsp;&nbsp;&nbsp;&nbsp;
         <a class='chachu'  href='#' onclick='deleteReceiveFileAttachment({{:paperAttachMentVO.attachmentId}},'paperAttachId{{:#index+1}}',this);' >ɾ��</a>
       {{/if}}
    </div>
	--}}
	</ul>
</div>
{{/for}}
</script>

<script type="text/javascript">
$(function(){
});

function initPaperData(masterReviewVO){
	bulidPaper(masterReviewVO.paperVOs);
}

//����
function bulidPaper(paperVOs){
	$("#paperTitleRowNum").val(paperVOs.length);
	if(paperVOs.length>0){
		 var dataObject = {'Data': []};
		 for(var i =0;i<paperVOs.length;i++){
			 var headmaster_complete_forms =  Brightcom.workflow.getSelectCombox('headmaster_complete_form');
			 paperVOs[i]['headmaster_complete_form'] = headmaster_complete_forms;
			 
			 var headmaster_author_sorts =  Brightcom.workflow.getSelectCombox('headmaster_author_sort');
			 paperVOs[i]['headmaster_author_sort'] = headmaster_author_sorts;
			 
			 var headmaster_approve_levels =  Brightcom.workflow.getSelectCombox('headmaster_approve_level');
			 paperVOs[i]['headmaster_approve_level'] = headmaster_approve_levels;
			 
			 dataObject.Data.push(paperVOs[i]);
		 }
		 //debugger;
		 var subTaskContent= $("#paperRec").render(dataObject);
		 $("#paperRefill").append(subTaskContent);
		 
		 for(var i =0;i<paperVOs.length;i++){
			 Headmaster.initWebUploader('paperbutton',(i+1),'paperType','����ϴ�','paperAttachId','paperbuttonDiv');
		 }
	}
	
	//$("#paperRefill").append("<div style='margin:20px auto 0 auto;width:840px;'><a href='javascript:void(0);'  onclick='addPaperSingle(this)' class='add-more'>+</a></div>");
}

function addPaperSingle(obj){
	
	var paperTitleRowNum = parseInt($("#paperTitleRowNum").val());
	var paperTitleRowNumNext = parseInt(paperTitleRowNum+1);

	var educationArray =[];
	educationArray.push("<div class='show-list radius-3 deleteDiv'>"+paperTitleRowNumNext+"��<a href='javascript:void(0);' target='_self' title='' onclick=\"deleteSingleOption(this,'paperType',\'"+paperTitleRowNumNext+"\','');\" class='del radius-3'>ɾ��</a></div>");
	educationArray.push("<div class='container'>");
	//educationArray.push("<div class='container paper'>");
	educationArray.push("<ul id='' class='clear-fix'>");
	educationArray.push("<input type='hidden' id='paperId"+paperTitleRowNumNext+"'  value=''>");
	educationArray.push("<input type='hidden' id='paperAttachId"+paperTitleRowNumNext+"'  value=''>");
	educationArray.push("<li><span class='fl'>��Ŀ��</span>");
	educationArray.push("<div class='border_2 w_15 fl'>");
	educationArray.push(" <input type='text' id='title"+paperTitleRowNumNext+"' value='' placeholder=''>");
	educationArray.push("</div>");
	educationArray.push(" </li>");
	
	educationArray.push("<li><span class='fl'>��������ʱ�䣺</span>");
	educationArray.push("<div class='border_2 w_16 fl'>");
	educationArray.push("<input type='text' id='publish_time_paper"+paperTitleRowNumNext+"' value='' placeholder='' onclick='selectDeleteTime()'>");
	educationArray.push("</div>");
	educationArray.push("</li>");
	
	educationArray.push("<li><span class='fl'>��־������ѧ�����ƣ�</span>");
	educationArray.push("<div class='border_2 w_27 fl'>");
	educationArray.push("<input type='text' id='magazine_meet_name"+paperTitleRowNumNext+"' name='magazine_meet_name"+paperTitleRowNumNext+"' />");
	educationArray.push("</div>");
	educationArray.push("</li>");
	
	educationArray.push("<li><span class='fl'>���쵥λ��</span>");
	educationArray.push("<div class='border_2 w_18 fl'>");
	educationArray.push("<input type='text' id='organizers"+paperTitleRowNumNext+"' value='' placeholder=''>");
	educationArray.push("</li>");
	
	educationArray.push("<li><span class='fl'>�������Ļ������ƣ�</span>");
	educationArray.push("<div class='border_2 w_17 fl'>");
	educationArray.push("<input type='text' id='paper_meet_name"+paperTitleRowNumNext+"' value='' placeholder=''>");
	educationArray.push("</div>");
	educationArray.push("</li>");
	
	educationArray.push("<li><span class='fl'>���쵥λ����</span>");
	educationArray.push("<div class='border_2 w_19 fl'>");
	educationArray.push("<select id='organizers_level"+paperTitleRowNumNext+"' class='select_left'></select>");
	educationArray.push("</div>");
	educationArray.push("</li>");
	
	educationArray.push("<li><span class='fl'>���ţ�</span>");
	educationArray.push("<div class='border_2 w_15 fl'>");
	educationArray.push("<input type='text' id='paper_number"+paperTitleRowNumNext+"' value='' placeholder=''>");
	educationArray.push("</div>");
	educationArray.push("</li>");
	
	
	educationArray.push("<li><span class='fl'>���˳е����֣�</span>");
	educationArray.push("<div class='border_2 w_19 fl'>");
	educationArray.push("<input type='text' id='personal_part"+paperTitleRowNumNext+"' value='' placeholder=''>");
	educationArray.push("</div>");
	educationArray.push("</li>");
	
	educationArray.push("<li>");
	educationArray.push("<span class='fl'>��ɷ�ʽ��</span>");
	educationArray.push("<div class='border_2 w_18 fl'>");
	educationArray.push("<select  id='complete_way"+paperTitleRowNumNext+"' class='select_left'></select>");
	educationArray.push("</div>");
	educationArray.push("</li>");
	
	educationArray.push("<li>");
	educationArray.push("<span class='fl'>��������</span>");
	educationArray.push("<div class='border_2 w_18 fl'>");
	educationArray.push("<select  id='author_order"+paperTitleRowNumNext+"' class='select_left'></select>");
	educationArray.push("</div>");
	educationArray.push("</li>");
	
	educationArray.push("<li>");
	educationArray.push("<span class='fl'>���浥λ��</span>");
	educationArray.push("<div class='border_2 w_18 fl'>");
	educationArray.push("<input type='text' id='publish_company"+paperTitleRowNumNext+"' value='' placeholder=''>");
	educationArray.push("</div>");
	educationArray.push("</li>");
	
	educationArray.push("<li>");
	educationArray.push("</li>");
	
	educationArray.push("<li style='height:45px;' class='position_relative'><span class='fl'>����ɨ�����</span>");
	educationArray.push("<div  id='paperbutton"+paperTitleRowNumNext+"' class='position_upload_button_professional' style='left: 96px;'></div>");
	educationArray.push("</li>");
	educationArray.push("</ul>");
	
	educationArray.push("<div class='attachments'>");
	educationArray.push("<div id='paperbuttonDiv"+paperTitleRowNumNext+"' class='listInfo'></div>");
	educationArray.push("</div>");
	educationArray.push("</div>");
	
	//educationArray.push("<a class='add' href='javascript:void(0);' onclick='addPaperSingle(this)' >+</a>");
	//educationArray.push("<div style='margin:20px auto 0 auto;width:840px;'><a href='javascript:void(0);'  onclick='addPaperSingle(this)' class='add-more'>+</a></div>");
	
	//$(obj).after(educationArray.join(""));
	//$(obj).remove();
	$("#paperRefill").append(educationArray.join(""));
	
	$("#paperTitleRowNum").val(paperTitleRowNumNext);
	
	Brightcom.workflow.initSelectCombox('headmaster_complete_form','complete_way'+(paperTitleRowNumNext));
	Brightcom.workflow.initSelectCombox('headmaster_author_sort','author_order'+(paperTitleRowNumNext));
	Brightcom.workflow.initSelectCombox('headmaster_approve_level','organizers_level'+(paperTitleRowNumNext));
	
	/*
	 * 1��professionalTitlespan:�ϴ���ť��ʾλ��id
	 4��buttonName �� ��ť����
	 5��hiddenAttachId�����ظ���id��ѡ�к�Ѹ���ֵ�����ڸ���������
	 6�� hiddenDisplayId:������ʾ��div id
	 */
	 Headmaster.initWebUploader('paperbutton',paperTitleRowNumNext,'paperType','����ϴ�','paperAttachId','paperbuttonDiv');
}

function saveUpdateRefillData(){
	var isRefillTask = $("#isRefillTask").val();
	if(isRefillTask){
		var submitStrings =  getSubmitStrings();
		var bcReq = new BcRequest('headmaster','masterReviewAction','saveUpdateRefillData');
		bcReq.setExtraPs({
			   "option_tab_type":'paper',
			    "option_tab_values":submitStrings,
			    "businessKey":processBusinessKey
		});
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
	var paperTitleRowNum = $("#paperTitleRowNum").val();
	for(var i=0;i<paperTitleRowNum;i++){
		var rowNum = (i+1);
		var id = $("#paperId"+rowNum).val();
		var title = $("#title"+rowNum).val();
		var publish_time = $("#publish_time_paper"+rowNum).val();
		var magazine_meet_name = $("#magazine_meet_name"+rowNum).val();
		var paper_meet_name = $("#paper_meet_name"+rowNum).val();
		var paper_number = $("#paper_number"+rowNum).val();
		var organizers = $("#organizers"+rowNum).val();
		var organizers_level = $("#organizers_level"+rowNum).val();
		var personal_part = $("#personal_part"+rowNum).val();
		var paper_attachment_id = $("#paperAttachId"+rowNum).val();
		var complete_way = $("#complete_way"+rowNum).val();
		var author_order = $("#author_order"+rowNum).val();
		var publish_company = $("#publish_company"+rowNum).val();
		var businessKey = $("#id").val();
		
		if(!title){
			continue;
		}
		
		var workExperienceObject = {
				"id":id,
				"title":title,
				"businessKey":$("#id").val(),
				"publishTime":publish_time,
				"magazineMeetName":magazine_meet_name,
				"paperMeetName":paper_meet_name,
				"paperNumber":paper_number,
				"organizers":organizers,
				'organizersLevel' :organizers_level,
				'personalPart' :personal_part,
				'paperAttachmentId' :paper_attachment_id,
				'complete_way' :complete_way,
				'author_order' :author_order,
				'publish_company' :publish_company
		}
		submitArray.push(workExperienceObject);
	}
	return JSON.stringify(submitArray);
}

function headmasterBeforeSubmit(formJsonData){
	formJsonData.option_tab_type = "paper"
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
			<h2><i>5</i>���ķ���</h2>
			<p>��д�������ķ��������</p>
		</div>
		<div class="select-step fr"><a href="javascript:void(0);" target="_self" title="" id="change">+&nbsp;�л�����</a></div>
		<div class="clear-both"></div>
	</div>
	<!-- ���� e -->
	<!-- ��ְ���� s -->
	<div id="paperRefill" class="paper"><!--  -->
		<input type="hidden" id="paperTitleRowNum" name="paperTitleRowNum" value="0">
	</div>
	
	<!--  -->
	<div class="addDiv"><a href="javascript:void(0);" onclick="addPaperSingle(this)" class="add-more">+</a></div>
	
	<!-- ��ְ���� e -->
	<div class="next-step clear-fix">
	 <a href="javascript:void(0);" target="_self" title="" class="fl" onclick="changeOption(4)">��һ��</a>
	 <a href="javascript:void(0);" target="_self" title="" class="fr" onclick="saveUpdateRefillData()">��һ��</a>
	</div>
</body>
</html>