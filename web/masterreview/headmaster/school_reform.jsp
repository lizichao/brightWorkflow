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
<script id="schoolReformRec" type="text/x-jsrender">	
{{for Data}}
    <div class='show-list radius-3'>{{:#index+1}}<a href='javascript:void(0);' target='_self'  onclick="deleteSingleOption(this,'schoolReformType','{{:#index+1}}','{{:id}}');" class='del radius-3'>ɾ��</a></div>
   <div class="container">
	<input type='hidden' id="schoolReformId{{:#index+1}}"  value='{{:id}}'>
	<ul class="clear-fix">
	<li>
         <span class="fl">ѧУ��ɫ�������ĸ���Ŀ���ƣ�</span>
         <div class="border_2 w_24 fl">
          <input type="text" id='project_name{{:#index+1}}' value='{{:project_name}}' placeholder="������ĸ���Ŀ����" />
         </div>
    </li>
	<li>
      <span class="fl">��Ŀ����</span>
      <div class="border_2 w_13 fl">

       <select id="project_level{{:#index+1}}" class='select_left'>
                   {{for headmaster_approve_level}}
                     {{if id=== #parent.parent.data.project_level }}
				       <option value='{{:id}}' selected='selected'>{{:text}}</option>
				    {{else}}
				       <option value='{{:id}}' >{{:text}}</option>
				    {{/if}}
                    {{/for}}
          </select>
      </div>
    </li>
	<li>
        <span class="fl">ʱ�䣺</span>
        <div class="border_2 w_20 fl">
          <input type="text" id='implement_time{{:#index+1}}' value='{{timeCovert implement_time/}}' onclick='selectDeleteTime()' placeholder="������ʱ��" />
        </div>
    </li>
	<li>
      <span class="fl">��Ŀ���ܲ��ţ�</span>
      <div class="border_2 w_23 fl">
       <input type="text" id='charge_department{{:#index+1}}' value='{{:charge_department}}'  />
      </div>
    </li>

	<li>
      <span class="fl">��Ŀ��������</span>
      <div class="border_2 w_23 fl">
       <input type="text" id='performance{{:#index+1}}' value='{{:performance}}'  />
      </div>
    </li>

   </ul>
</div>
{{/for}}
</script>

<script type="text/javascript">
$(function(){
});

function initSchoolReformData(masterReviewVO){
	bulidSchoolReform(masterReviewVO.schoolReformVOs);
}

//��ְ����
function bulidSchoolReform(schoolReformVOs){
	$("#schoolReformRowNum").val(schoolReformVOs.length);
	if(schoolReformVOs.length>0){
		 var dataObject = {'Data': []};
		 for(var i =0;i<schoolReformVOs.length;i++){
			 var headmaster_approve_levels =  Brightcom.workflow.getSelectCombox('headmaster_approve_level');
			 schoolReformVOs[i]['headmaster_approve_level'] = headmaster_approve_levels;
			 dataObject.Data.push(schoolReformVOs[i]);
		 }
		 var subTaskContent= $("#schoolReformRec").render(dataObject);
		 $("#schoolReformRefill").append(subTaskContent);
		 
	}
	//$("#schoolReformRefill").append("<div class='add'><a class='add-more' href='javascript:void(0);' onclick='addSchoolReformSingle(this)' >+</a></div> ");
}

function addSchoolReformSingle(obj){
	var schoolReformRowNum = parseInt($("#schoolReformRowNum").val());
	var schoolReformRowNumNext = parseInt(schoolReformRowNum+1);

	var educationArray =[];
	educationArray.push("<div class='show-list radius-3'>"+schoolReformRowNumNext+"��<a href='javascript:void(0);' target='_self' onclick=\"deleteSingleOption(this,'schoolReform',\'"+schoolReformRowNumNext+"\','');\" class='del radius-3'>ɾ��</a></div>");
	educationArray.push("<div class='container'>");
	educationArray.push("<ul id='' class='clear-fix'>");
	
	educationArray.push("<input type='hidden' id='schoolReformId"+schoolReformRowNumNext+"'  value=''>");
	
	
	educationArray.push("<li><span class='fl'>ѧУ��ɫ�������ĸ���Ŀ���ƣ�</span>");
	educationArray.push("<div class='border_2 w_24 fl'>");
	educationArray.push("<input type='text' id='project_name"+schoolReformRowNumNext+"' value='' placeholder='������ѧУȫ��'>");
	educationArray.push("</div>");
	educationArray.push("</li>");
	
	educationArray.push("<li><span class='fl'>��Ŀ����</span>");
	educationArray.push("<div class='border_2 w_13 fl'>");
	educationArray.push("<select id='project_level"+schoolReformRowNumNext+"' class='select_left'></select>");
	educationArray.push("</div>");
	educationArray.push("</li>");
	
	educationArray.push("<li><span class='fl'>ʱ�䣺</span>");
	educationArray.push("<div class='border_2 w_20 fl'>");
	educationArray.push("<input type='text' id='implement_time"+schoolReformRowNumNext+"'   onclick='selectDeleteTime()' placeholder='������ʱ��' />");
	educationArray.push("</div>");
	educationArray.push("</li>");
	
	educationArray.push("<li><span class='fl'>��Ŀ���ܲ��ţ�</span>");
	educationArray.push("<div class='border_2 w_23 fl'>");
	educationArray.push("<input type='text' id='charge_department"+schoolReformRowNumNext+"'   value='' placeholder='��������Ŀ���ܲ���' />");
	educationArray.push("</div>");
	educationArray.push("</li>");
	
	educationArray.push("<li><span class='fl'>��Ŀ��������</span>");
	educationArray.push("<div class='border_2 w_23 fl'>");
	educationArray.push("<input type='text' id='performance"+schoolReformRowNumNext+"'   value='' placeholder='��������Ŀ������' />");
	educationArray.push("</div>");
	educationArray.push("</li>");
	
	
	//educationArray.push("<div class='add'><a class='add-more' href='javascript:void(0);' onclick='addSchoolReformSingle(this)' >+</a></div>");
	
	//$(obj).after(educationArray.join(""));
	//$(obj).remove();
	$("#schoolReformRefill").append(educationArray.join(""));
	Brightcom.workflow.initSelectCombox('headmaster_approve_level','project_level'+(schoolReformRowNumNext));
	
	$("#schoolReformRowNum").val(schoolReformRowNumNext);
}


function saveUpdateRefillData(){
	var isRefillTask = $("#isRefillTask").val();
	if(isRefillTask){
		var submitStrings =  getSubmitStrings();
		var bcReq = new BcRequest('headmaster','masterReviewAction','saveUpdateRefillData');
		bcReq.setExtraPs({
			    "option_tab_type":'schoolReform',
			    "option_tab_values":submitStrings,
			    "businessKey":processBusinessKey
		});
		bcReq.setSuccFn(function(data,status){
			changeOption(20);
		});
		bcReq.postData();
	}else{
		submitRefillTask();
	}
}

function getSubmitStrings(){
	var submitArray = [];
	var paperTitleRowNum = $("#schoolReformRowNum").val();
	for(var i=0;i<paperTitleRowNum;i++){
		var rowNum = (i+1);
		var id = $("#schoolReformId"+rowNum).val();
		var project_name = $("#project_name"+rowNum).val();
		var project_level = $("#project_level"+rowNum).val();
		var implement_time = $("#implement_time"+rowNum).val();
		var charge_department = $("#charge_department"+rowNum).val();
		var performance = $("#performance"+rowNum).val();
		var businessKey = $("#id").val();
		if(!project_name){
			continue;
		}
		var workExperienceObject = {
				"id":id,
				"project_name":project_name,
				"businessKey":$("#id").val(),
				"project_level":project_level,
				"implement_time":implement_time,
				"charge_department":charge_department,
				"performance":performance
		}
		submitArray.push(workExperienceObject);
	}
	return JSON.stringify(submitArray);
}

function headmasterBeforeSubmit(formJsonData){
	formJsonData.option_tab_type = "schoolReform"
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
			<h2><i>19</i>ѧУ��ɫ���ĸ�</h2>
			<p>��дѧУ�ƽ����ʽ�����ɫѧУ�������е��ϼ����������Ե�ĸ���Ŀ�������</p>
		</div>
		<div class="select-step fr"><a href="javascript:void(0);" target="_self" title="" id="change">+&nbsp;�л�����</a></div>
		<div class="clear-both"></div>
	</div>
	<!-- ���� e -->
	<!-- ��ְ���� s -->
	<div id="schoolReformRefill" class="years">
		<input type="hidden" id="schoolReformRowNum" name="schoolReformRowNum" value="0">
	</div>
	<!-- ��ְ���� e -->
	
	<div class="add"><a href="javascript:void(0);" onclick="addSchoolReformSingle(this)" class="add-more">+</a></div>
	
	<div class="next-step clear-fix">
	   <a href="javascript:void(0);" target="_self" title="" class="fl" onclick="changeOption(18)">��һ��</a>
	   <a href="javascript:void(0);" target="_self" title="" class="fr" onclick="saveUpdateRefillData()">��һ��</a>
	</div>

</body>
</html>