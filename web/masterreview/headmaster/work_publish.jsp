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

<!--著作发表情况  -->
<script id="workPublishRec" type="text/x-jsrender">	
{{for Data}}
<div class='show-list radius-3'>{{:#index+1}}<a href='#' target='_self' onclick="deleteSingleOption(this,'workPublishType','{{:#index+1}}','{{:id}}');" class='del radius-3'>删除</a></div>
<div class="container">
<ul class="clear-fix">
	 <input type='hidden' id='workPublishId{{:#index+1}}'  value='{{:id}}'>

	<li><span class='fl'>书名：</span>
     <div class="border_2 w_20 fl">
      <input type='text' id='book_name{{:#index+1}}' value='{{:book_name}}' placeholder=''>
       </div>
    </li>
	<li><span class='fl'>完成方式：</span>
     <div class="border_2 w_13 fl">
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
	<li><span class='fl'>出版时间：</span>
      <div class="border_2 w_13 fl">
       <input type='text' id='publish_time{{:#index+1}}' value='{{timeCovert publish_time/}}' placeholder='' onclick='selectDeleteTime()'>
      </div>
    </li>
	<li><span class='fl'>出版社：</span>
      <div class="border_2 w_13 fl">
       <input type='text' id='publish_company{{:#index+1}}' value='{{:publish_company}}' placeholder=''>
      </div>
    </li>
	<li><span class='fl'>本人完成字数：</span>
      <div class="border_2 w_23 fl">
       <input type='text' id='complete_word{{:#index+1}}' value='{{:complete_word}}' placeholder=''>
      </div>
   </li>
	<li><span class='fl'>本人承担部分：</span>
      <div class="border_2 w_23 fl">
        <input type='text' id='complete_chapter{{:#index+1}}' value='{{:complete_chapter}}' placeholder=''>
      </div>
    </li>
	<li><span class='fl'>作者排序：</span>
      <div class="border_2 w_13 fl">
        <select id="author_order{{:#index+1}}" class="select_left">
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
</ul>
</div>
{{/for}}
</script>

<script type="text/javascript">
$(function(){
});

function initWorkPublishData(masterReviewVO){
	bulidBookPublish(masterReviewVO.bookPublishVOs);
}

//著作出版
function bulidBookPublish(bookPublishVOs){
	$("#workPublishRowNum").val(bookPublishVOs.length);
	if(bookPublishVOs.length>0){
		 var dataObject = {'Data': []};
		 for(var i =0;i<bookPublishVOs.length;i++){
			 var headmaster_complete_forms =  Brightcom.workflow.getSelectCombox('headmaster_complete_form');
			 bookPublishVOs[i]['headmaster_complete_form'] = headmaster_complete_forms;
			 
			 var headmaster_author_sorts =  Brightcom.workflow.getSelectCombox('headmaster_author_sort');
			 bookPublishVOs[i]['headmaster_author_sort'] = headmaster_author_sorts;
			 dataObject.Data.push(bookPublishVOs[i]);
		 }
		 var subTaskContent= $("#workPublishRec").render(dataObject);
		 $("#workPublishRefill").append(subTaskContent);
	}
	//$("#workPublishRefill").append("<div class='add'><a class='add-more' href='javascript:void(0);' onclick='addWorkPublishSingle(this)' >+</a></div> ");
}

function addWorkPublishSingle(obj){
	var workPublishRowNum = parseInt($("#workPublishRowNum").val());
	var workPublishRowNumNext = parseInt(workPublishRowNum+1);

	var educationArray =[];
	educationArray.push("<div class='show-list radius-3'>"+workPublishRowNumNext+"、<a href='javascript:void(0);' target='_self' title='' onclick=\"deleteSingleOption(this,'workPublishType',\'"+workPublishRowNumNext+"\','');\" class='del radius-3'>删除</a></div>");
	educationArray.push("<div class='container'>");
	educationArray.push("<ul class='clear-fix'>");
	
	educationArray.push("<input type='hidden' id='workPublishId"+workPublishRowNumNext+"'  value=''>");
	educationArray.push("<li>");
	educationArray.push(" <span class='fl'>书名：</span>");
	educationArray.push("<div class='border_2 w_20 fl'>");
	educationArray.push(" <input type='text' id='book_name"+workPublishRowNumNext+"' value='' placeholder=''>");
	educationArray.push("</div>");
	educationArray.push(" </li>");
	 
	educationArray.push("<li>");
	educationArray.push("<span class='fl'>完成方式：</span>");
	educationArray.push("<div class='border_2 w_13 fl'>");
	educationArray.push("<select  id='complete_way"+workPublishRowNumNext+"' class='select_left'></select>");
	educationArray.push("</div>");
	educationArray.push("</li>");
	
	educationArray.push("<li>");
	educationArray.push("<span class='fl'>出版时间：</span>");
	educationArray.push("<div class='border_2 w_13 fl'>");
	educationArray.push("<input type='text' id='publish_time"+workPublishRowNumNext+"' value='' placeholder='' onclick='selectDeleteTime()'>");
	educationArray.push("</div>");
	educationArray.push("</li>");
	
	educationArray.push("<li>");
	educationArray.push("<span class='fl'>出版社：</span>");
	educationArray.push("<div class='border_2 w_13 fl'>");
	educationArray.push("<input type='text' id='publish_company"+workPublishRowNumNext+"' value='' placeholder='' >");
	educationArray.push("</div>");
	educationArray.push("</li>");
	
	educationArray.push("<li>");
	educationArray.push("<span class='fl'>本人完成字数：</span>");
	educationArray.push("<div class='border_2 w_23 fl'>");
	educationArray.push("<input type='text' id='complete_word"+workPublishRowNumNext+"' value='' placeholder=''>");
	educationArray.push("</div>");
	educationArray.push("</li>");
	
	educationArray.push("<li>");
	educationArray.push("<span class='fl'>本人承担部分：</span>");
	educationArray.push("<div class='border_2 w_23 fl'>");
	educationArray.push("<input type='text' id='complete_chapter"+workPublishRowNumNext+"' value='' placeholder='' >");
	educationArray.push("</div>");
	educationArray.push("</li>");
	
	educationArray.push("<li>");
	educationArray.push("<span class='fl'>作者排序：</span>");
	educationArray.push("<div class='border_2 w_13 fl'>");
	educationArray.push("<select  id='author_order"+workPublishRowNumNext+"' class='select_left'></select>");
//	educationArray.push("<input type='text' id='author_order"+workPublishRowNumNext+"' value='' placeholder=''>");
	educationArray.push("</div>");
	educationArray.push("</li>");
	
	
	educationArray.push("</ul>");
	educationArray.push("</div>");
	
	//educationArray.push("	<div class='add'><a class='add-more' href='javascript:void(0);' onclick='addWorkPublishSingle(this)' >+</a></div> ");
	
	//$(obj).after(educationArray.join(""));
	//$(obj).remove();
	
	$("#workPublishRefill").append(educationArray.join(""));
	
	$("#workPublishRowNum").val(workPublishRowNumNext);
	
	Brightcom.workflow.initSelectCombox('headmaster_complete_form','complete_way'+(workPublishRowNumNext));
	Brightcom.workflow.initSelectCombox('headmaster_author_sort','author_order'+(workPublishRowNumNext));
}

function saveUpdateRefillData(){
	var isRefillTask = $("#isRefillTask").val();
	if(isRefillTask){
		var submitStrings =  getSubmitStrings();
		var bcReq = new BcRequest('headmaster','masterReviewAction','saveUpdateRefillData');
		bcReq.setExtraPs({		
			"option_tab_type":'workPublish',
		    "option_tab_values":submitStrings,
		    "businessKey":processBusinessKey
		});
		bcReq.setSuccFn(function(data,status){
			changeOption(9);
		});
		bcReq.postData();
	}else{
		submitRefillTask();
	}
}

function getSubmitStrings(){
	var submitArray = [];
	var workPublishRowNum = $("#workPublishRowNum").val();
	for(var i=0;i<workPublishRowNum;i++){
		var rowNum = (i+1);
		var id = $("#workPublishId"+rowNum).val();
		var book_name = $("#book_name"+rowNum).val();
		var complete_way = $("#complete_way"+rowNum).val();
		var publish_time = $("#publish_time"+rowNum).val();
		var publish_company = $("#publish_company"+rowNum).val();
		var complete_chapter = $("#complete_chapter"+rowNum).val();
		var complete_word = $("#complete_word"+rowNum).val();
		var author_order = $("#author_order"+rowNum).val();
		var coverAttachmentId = $("#coverAttachmentId"+rowNum).val();
		var contentsAttachmentId = $("#contentsAttachmentId"+rowNum).val();
		var businessKey = $("#id").val();
		if(!book_name){
			continue;
		}
		var workExperienceObject = {
				"id":id,
				"businessKey":$("#id").val(),
				"book_name":book_name,
				"complete_way":complete_way,
				"publish_time":publish_time,
				"publish_company":publish_company,
				"complete_chapter":complete_chapter,
				"complete_word":complete_word,
				"author_order":author_order,
				'coverAttachmentId' :coverAttachmentId,
				'contentsAttachmentId' :contentsAttachmentId,
		}
		submitArray.push(workExperienceObject);
	}
	return JSON.stringify(submitArray);
}

function headmasterBeforeSubmit(formJsonData){
	formJsonData.option_tab_type = "workPublish"
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
			<h2><i>8</i>专著出版</h2>
			<p>填写个人专著出版情况。</p>
		</div>
		<div class="select-step fr"><a href="javascript:void(0);" target="_self" title="" id="change">+&nbsp;切换步骤</a></div>
		<div class="clear-both"></div>
	</div>
	<!-- 标题 e -->
	<!-- 任职年限 s -->
	<div id="workPublishRefill" class="years">
			<input type="hidden" id="workPublishRowNum" name="workPublishRowNum" value="0">
	</div>
	
	<!--  -->
	<div class="add"><a href="javascript:void(0);" onclick="addWorkPublishSingle(this)" class="add-more">+</a></div>
	
	
	<!-- 任职年限 e -->
	<div class="next-step clear-fix">
	   <a href="javascript:void(0);" target="_self" title="" class="fl" onclick="changeOption(7)">上一步</a>
	   <a href="javascript:void(0);" target="_self" title="" class="fr" onclick="saveUpdateRefillData()">下一步</a>
	</div>
</body>
</html>