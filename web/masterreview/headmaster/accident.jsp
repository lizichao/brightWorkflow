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

<script id="accidentRec" type="text/x-jsrender">	
{{for Data}}
    <div class='show-list radius-3'>{{:#index+1}}<a href='javascript:void(0);' target='_self'  onclick="deleteSingleOption(this,'accident','{{:#index+1}}','{{:id}}');" class='del radius-3'>删除</a></div>
   <div class="container">
      <input type='hidden' id="accidentId{{:#index+1}}"  value='{{:id}}'>
	<ul class="clear-fix">
	<li>
        <span class="fl">责任事故名称：</span>
        <div class="border_2 w_23 fl">
          <input type="text" id='accident_name{{:#index+1}}' value='{{:accident_name}}' placeholder="请输入责任事故名称" />
        </div>
    </li>
	<li>
         <span class="fl">违纪描述：</span>
         <div class="border_2 w_13 fl">
          <input type="text" id='description{{:#index+1}}' value='{{:description}}' placeholder="请输入违纪描述" />
         </div>
    </li>
	<li>
      <span class="fl">时间：</span>
      <div class="border_2 w_20 fl">
       <input type="text" id='implement_time{{:#index+1}}' value='{{timeCovert implement_time/}}' onclick='selectDeleteTime()'  placeholder="请输入时间" />
      </div>
    </li>
	<li>
      <span class="fl">处分结果：</span>
      <div class="border_2 w_13 fl">
       <input type="text" id='approve_result{{:#index+1}}' value='{{:approve_result}}'  />
      </div>
    </li>

   </ul>
</div>
{{/for}}
</script>

<script type="text/javascript">
$(function(){
});

function initAccidentData(masterReviewVO){
	bulidAccident(masterReviewVO.accidentVOs);
}

//任职年限
function bulidAccident(accidentVOs){
	$("#accidentRowNum").val(accidentVOs.length);
	if(accidentVOs.length>0){
		 var dataObject = {'Data': []};
		 for(var i =0;i<accidentVOs.length;i++){
			 dataObject.Data.push(accidentVOs[i]);
		 }
		 var subTaskContent= $("#accidentRec").render(dataObject);
		 $("#accidentRefill").append(subTaskContent);
		 
	}
	//$("#accidentRefill").append("<div class='add'><a class='add-more' href='javascript:void(0);' onclick='addAccidentSingle(this)' >+</a></div> ");
}

function addAccidentSingle(obj){
	var accidentRowNum = parseInt($("#accidentRowNum").val());
	var accidentRowNumNext = parseInt(accidentRowNum+1);

	var educationArray =[];
	educationArray.push("<div class='show-list radius-3'>"+accidentRowNumNext+"、<a href='javascript:void(0);' target='_self' onclick=\"deleteSingleOption(this,'accident',\'"+accidentRowNumNext+"\','');\" class='del radius-3'>删除</a></div>");
	educationArray.push("<div class='container'>");
	educationArray.push("<ul  class='clear-fix'>");
	
	educationArray.push("<input type='hidden' id='accidentId"+accidentRowNumNext+"'  value=''>");
	
	educationArray.push("<li><span class='fl'>责任事故名称：</span>");
	educationArray.push("<div class='border_2 w_23 fl'>");
	educationArray.push("<input type='text' id='accident_name"+accidentRowNumNext+"' value='' placeholder='请输入责任事故名称'>");
	educationArray.push("</div>");
	educationArray.push("</li>");
	
	educationArray.push("<li><span class='fl'>违纪描述：</span>");
	educationArray.push("<div class='border_2 w_13 fl'>");
	educationArray.push("<input type='text' id='description"+accidentRowNumNext+"' value='' placeholder='请输入违纪描述'>");
	educationArray.push("</div>");
	educationArray.push("</li>");
	
	educationArray.push("<li><span class='fl'>时间：</span>");
	educationArray.push("<div class='border_2 w_20 fl'>");
	educationArray.push("<input type='text' id='implement_time"+accidentRowNumNext+"'   onclick='selectDeleteTime()'  placeholder='请输入时间' />");
	educationArray.push("</div>");
	educationArray.push("</li>");
	
	educationArray.push("<li><span class='fl'>处理结果：</span>");
	educationArray.push("<div class='border_2 w_13 fl'>");
	educationArray.push("<input type='text' id='process_result"+accidentRowNumNext+"'   value='' placeholder='请输入处理结果' />");
	educationArray.push("</div>");
	educationArray.push("</li>");
	
	
	
	educationArray.push("</ul>");
	educationArray.push("</div>");
	
	//educationArray.push("<div class='add'><a class='add-more' href='javascript:void(0);' onclick='addAccidentSingle(this)' >+</a></div>");
	
	//$(obj).after(educationArray.join(""));
	//$(obj).remove();
	$("#accidentRefill").append(educationArray.join(""));
	
	$("#accidentRowNum").val(accidentRowNumNext);
}


function saveUpdateRefillData(){
	var isRefillTask = $("#isRefillTask").val();
	if(isRefillTask){
		var submitStrings =  getSubmitStrings();
		var bcReq = new BcRequest('headmaster','masterReviewAction','saveUpdateRefillData');
		bcReq.setExtraPs({
			    "option_tab_type":'accident',
			    "option_tab_values":submitStrings,
			    "businessKey":processBusinessKey
			}
		);
		bcReq.setSuccFn(function(data,status){
			changeOption(22);
		});
		bcReq.postData();
	}else{
		submitRefillTask();
	}
}

function getSubmitStrings(){
	var submitArray = [];
	var accidentRowNum = $("#accidentRowNum").val();
	for(var i=0;i<accidentRowNum;i++){
		var rowNum = (i+1);
		var id = $("#accidentId"+rowNum).val();
		var accident_name = $("#accident_name"+rowNum).val();
		var description = $("#description"+rowNum).val();
		var implement_time = $("#implement_time"+rowNum).val();
		var process_result = $("#process_result"+rowNum).val();
		var businessKey = $("#id").val();
		if(!accident_name){
			continue;
		}
		var workExperienceObject = {
				"id":id,
				"businessKey":$("#id").val(),
				"accident_name":accident_name,
				"description":description,
				"implement_time":implement_time,
				'process_result' :process_result
		}
		submitArray.push(workExperienceObject);
	}
	return JSON.stringify(submitArray);
}

function headmasterBeforeSubmit(formJsonData){
	formJsonData.option_tab_type = "accident"
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
			<h2><i>21</i>责任事故</h2>
			<p>填写学校出现安全责任事故，或出现严重违纪现象，受到上级通报批评以上处理。</p>
		</div>
		<div class="select-step fr"><a href="javascript:void(0);" target="_self" title="" id="change">+&nbsp;切换步骤</a></div>
		<div class="clear-both"></div>
	</div>
	<!-- 标题 e -->
	<!-- 任职年限 s -->
	<div id="accidentRefill" class="years">
		<input type="hidden" id="accidentRowNum" name="accidentRowNum" value="0">
	</div>
	<!-- 任职年限 e -->
	
	<div class="add"><a href="javascript:void(0);" onclick="addAccidentSingle(this)" class="add-more">+</a></div>
	
	<div class="next-step clear-fix">
	   <a href="javascript:void(0);" target="_self" title="" class="fl" onclick="changeOption(20)">上一步</a>
	   <a href="javascript:void(0);" target="_self" title="" class="fr" onclick="saveUpdateRefillData()">下一步</a>
	</div>
</body>
</html>