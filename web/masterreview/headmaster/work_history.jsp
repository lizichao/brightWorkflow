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
<script id="workHistoryRec" type="text/x-jsrender">	
{{for Data}}
    <div class='show-list radius-3'>{{:#index+1}}<a href='javascript:void(0);' target='_self'  onclick="deleteSingleOption(this,'workHistory','{{:#index+1}}','{{:id}}');" class='del radius-3'>ɾ��</a></div>
   <div class="container">
      <input type='hidden' id="workHistoryId{{:#index+1}}"  value='{{:id}}'>
	<ul class="clear-fix">
	<li>
        <span class="fl">��ֹ���£�</span>		
        <div class="border_2 w_12 fl">
          <input type="text" id='start_date{{:#index+1}}' onclick='selectDeleteTime()' value="{{timeCovert start_date/}}"/>
        </div>
		<span class="fl">��</span>
		<div class="border_2 w_12 fl">
          <input type="text" id='end_date{{:#index+1}}'  onclick='selectDeleteTime()' value="{{timeCovert end_date/}}" />
        </div>
	</li>

	<li>
        <span class="fl">������λ��</span>
        <div class="border_2 w_13 fl">
          <input type="text" id='work_company{{:#index+1}}' value='{{:work_company}}' placeholder="�����빤����λ" />
        </div>
    </li>
	<li>
         <span class="fl">֤���ˣ�</span>
         <div class="border_2 w_26 fl">
           <input type="text" id='prove_people{{:#index+1}}' value='{{:prove_people}}' placeholder="������֤����" />
         </div>
    </li>
   </ul>
</div>
{{/for}}
</script>

<script type="text/javascript">
$(function(){
});

function initWorkHistoryData(masterReviewVO){
	debugger
	bulidWorkHistory(masterReviewVO.workHistoryVOs);
}

//��ְ����
function bulidWorkHistory(workHistoryVOs){
	$("#workHistoryRowNum").val(workHistoryVOs.length);
	if(workHistoryVOs.length>0){
		 var dataObject = {'Data': []};
		 for(var i =0;i<workHistoryVOs.length;i++){
			 dataObject.Data.push(workHistoryVOs[i]);
		 }
		 var subTaskContent= $("#workHistoryRec").render(dataObject);
		 $("#workHistoryRefill").append(subTaskContent);
	}
	//$("#workHistoryRefill").append("<div class='add'><a class='add-more' href='javascript:void(0);' onclick='addWorkHistorySingle(this)' >+</a></div> ");
}

function addWorkHistorySingle(obj){
	var workHistoryRowNum = parseInt($("#workHistoryRowNum").val());
	var workHistoryRowNumNext = parseInt(workHistoryRowNum+1);

	var educationArray =[];
	educationArray.push("<div class='show-list radius-3'>"+workHistoryRowNumNext+"��<a href='javascript:void(0);' target='_self' onclick=\"deleteSingleOption(this,'workHistory',\'"+workHistoryRowNumNext+"\','');\" class='del radius-3'>ɾ��</a></div>");
	educationArray.push("<div class='container'>");
	educationArray.push("<ul id='' class='clear-fix'>");
	
	educationArray.push("<input type='hidden' id='workHistoryId"+workHistoryRowNumNext+"'  value=''>");
	
	educationArray.push("<li><span class='fl'>��ֹ���£�</span>");
	educationArray.push("<div class='border_2 w_12 fl'>");
	educationArray.push("<input type='text' id='start_date"+workHistoryRowNumNext+"' name='start_date"+workHistoryRowNumNext+"' onclick='selectDeleteTime()'/>");
	educationArray.push("</div>");
	educationArray.push("<span class='fl'>��</span>");
	educationArray.push("<div class='border_2 w_12 fl'>");
	educationArray.push("<input type='text' id='end_date"+workHistoryRowNumNext+"' name='end_date"+workHistoryRowNumNext+"' onclick='selectDeleteTime()'/>");
	educationArray.push("</div>");
	educationArray.push("</li>");
	
	educationArray.push("<li><span class='fl'>������λ��</span>");
	educationArray.push("<div class='border_2 w_13 fl'>");
	educationArray.push("<input type='text' id='work_company"+workHistoryRowNumNext+"' value='' placeholder='�����빤����λ'>");
	educationArray.push("</div>");
	educationArray.push("</li>");
	
	educationArray.push("<li><span class='fl'>֤���ˣ�</span>");
	educationArray.push("<div class='border_2 w_26 fl'>");
	educationArray.push("<input type='text' id='prove_people"+workHistoryRowNumNext+"' value='' placeholder='������֤����'>");
	educationArray.push("</div>");
	educationArray.push("</li>");
	
	educationArray.push("</ul>");
	educationArray.push("</div>");
	
	//educationArray.push("<div class='add'><a class='add-more' href='javascript:void(0);' onclick='addWorkHistorySingle(this)' >+</a></div>");
	
	
	//$(obj).after(educationArray.join(""));
	//$(obj).remove();
	$("#workHistoryRefill").append(educationArray.join(""));
	
	$("#workHistoryRowNum").val(workHistoryRowNumNext);
}

function saveUpdateRefillData(){
	var isRefillTask = $("#isRefillTask").val();
	
	if(isRefillTask){
		submitMasterApply();
		/*
		var submitStrings =  getSubmitStrings();
		var bcReq = new BcRequest('headmaster','masterReviewAction','saveUpdateRefillData');
		bcReq.setExtraPs({
			    "option_tab_type":'workHistory',
			    "option_tab_values":submitStrings,
			    "businessKey":processBusinessKey
		});
		bcReq.setSuccFn(function(data,status){
			changeOption(23);
		});
		bcReq.postData();*/
	}else{
		submitRefillTask();
    }
}

function getSubmitStrings(){
	var submitArray = [];
	var workHistoryRowNum = $("#workHistoryRowNum").val();
	for(var i=0;i<workHistoryRowNum;i++){
		var rowNum = (i+1);
		var id = $("#workHistoryId"+rowNum).val();
		var start_date = $("#start_date"+rowNum).val();
		var end_date = $("#end_date"+rowNum).val();
		var work_company = $("#work_company"+rowNum).val();
		var prove_people = $("#prove_people"+rowNum).val();
		var businessKey = $("#id").val();
		if(!work_company){
			continue;
		}
		var workExperienceObject = {
				"id":id,
				"start_date":start_date,
				"end_date":end_date,
				"work_company":work_company,
				"prove_people":prove_people,
				"businessKey":businessKey
		}
		submitArray.push(workExperienceObject);
	}
	return JSON.stringify(submitArray);
}

function headmasterBeforeSubmit(formJsonData){
	formJsonData.option_tab_type = "workHistory"
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
			<h2><i>23</i>��������</h2>
			<p>��дУ�����������Ĺ���������</p>
		</div>
		<div class="select-step fr"><a href="javascript:void(0);" target="_self" title="" id="change">+&nbsp;�л�����</a></div>
		<div class="clear-both"></div>
	</div>
	<!-- ���� e -->
	<!-- ��ְ���� s -->
	<div id="workHistoryRefill" class="years">
	  <input type="hidden" id="workHistoryRowNum" name="workHistoryRowNum" value="0">
	</div>
	<!-- ��ְ���� e -->
	
	<div class="add"><a href="javascript:void(0);" onclick="addWorkHistorySingle(this)" class="add-more">+</a></div>
	
	<div class="next-step clear-fix">
	  <a href="javascript:void(0);" target="_self" title="" class="fl" onclick="changeOption(22)">��һ��</a>
	  <a href="javascript:void(0);" target="_self" title="" class="fr" onclick="saveUpdateRefillData()">��һ��</a>
	</div>
</body>
</html>