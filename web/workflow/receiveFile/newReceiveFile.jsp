<%@ page contentType="text/html; charset=GBK" %>
<%
  String userid = (String)session.getAttribute("userid");
  String username =(String)session.getAttribute("username");
  String usertype =(String)session.getAttribute("usertype");
  String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
  String registerNumber =(String)session.getAttribute("registerNumber");
%>


<script type="text/javascript" src="/js/swfupload.js"></script>
<script type="text/javascript" src="/js/upload/fileprogress.js"></script>
<script type="text/javascript" src="/js/upload/handlers.js"></script>
<script type="text/javascript" src="/js/jquery/jquery.form.js"></script>
<script type="text/javascript" src="/workflow/js/jquery.idTabs.min.js"></script>
<script src="/workflow/common/jspParamUtil.js"></script>
<script src="/platform/public/sysparam.js"></script>
<script type="text/javascript" src="/workflow/js/DatePicker/WdatePicker.js"></script>

<script>
function beforeSubmit(formJsonData){
	var finishDate = $("#finishDate").val();
	if(!finishDate){
		alert("请选择办结日期！");
		return false;
	}
	
	var attachments = attachmentIdsArray.length==0 ? "" : attachmentIdsArray.join(",");
	formJsonData.receiveTitle = $("#receiveTitle").val();
	formJsonData.receiveOffice = $("#receiveOffice").val();
	formJsonData.receiveWord = $("#receiveWord").val();
	formJsonData.fileType = $("#fileType:checked").val(),
	formJsonData.receiveDate = $("#receiveDate").val();
	formJsonData.urgent = $("#urgent").val();
	formJsonData.securityLevel = $("#securityLevel").val();
	formJsonData.finishDate = finishDate;
	formJsonData.receiveRemark =  $("#receiveRemark").val();
	formJsonData.registerNumber =  $("#fldDJH").val();
	formJsonData.attachments = attachments;
	
	var variableMap = new Brightcom.workflow.HashMap(); 
	variableMap.put("processTitle",$("#receiveTitle").val());
	formJsonData.processParam =variableMap.toJsonObject();
	/*
	return {
		"receiveTitle":$("#receiveTitle").val(),
		"receiveOffice":$("#receiveOffice").val(),
		"receiveWord":$("#receiveWord").val(),
		"fileType":$("#fileType:checked").val(),
		"receiveDate":$("#receiveDate").val(),
		"urgent":$("#urgent").val(),
		"securityLevel":$("#securityLevel").val(),
		"finishDate":finishDate,
		"receiveRemark":$("#internalRemark").val(),
		"registerNumber":$("#fldDJH").text(),
		"attachments" :attachments,
		"processParam" : variableMap.toJsonObject()
	}*/
}

$(document).ready(function(){	
	Brightcom.workflow.beforeSubmit = beforeSubmit;
	
	var d = new Date()
	var vYear = d.getFullYear()
	var vMon = d.getMonth() + 1
	var vDay = d.getDate()
	s=vYear+"-"+(vMon<10 ? "0" + vMon : vMon)+"-"+(vDay<10 ? "0"+ vDay : vDay);
	    
	$("#receiveWord").val(" 〔"+vYear+"〕 号");
	$("#receiveDate").val(s);
	
	  
    if('null' == '<%=registerNumber%>'){
	   var bcReq = new BcRequest('workflow','receiveFileAction','getReceiveFileNumber');
		bcReq.setExtraPs({
			"processDefKey":$("#processDefKey").val()
		});
		bcReq.setSuccFn(function(data,status){
			$("#fldDJH").text(data.Data[0].registerNumber);
		});
		bcReq.postData();
    }else{
	   $("#fldDJH").text('<%=registerNumber%>');
    }
});	

function selectDeleteTime(timeObject){
	WdatePicker({
		readOnly:true,
		isShowClear:false,
		isShowOthers:false,
		maxDate:'2099-12-31 23:59:59',
		onpicked:function(dp){
			//getMessageRecord(dp)
		}
	});
}
</script>
<input type="hidden" id="workflowSubmitAction" name="workflowSubmitAction" value="workflow@@receiveFileAction@@startReceiveFileProcess">

<h3 class="tit" style="padding:0px;width:735px"><a href="#tab1c1">基本属性</a><a href="#tab1c2">附件</a></h3>
   	         <div class="box2" id="tab1c1">
               	  <div class="box2_content">
    <table border=0 cellpadding=0 cellspacing=0>
	    <tbody>
	    <tr>
	      <td colspan="4" height="5"></td>
	    </tr>
	    <tr>
	      <td><img border=0 src="/workflow/images/tab_left.gif" ></td>
	      <td align="center" style="font-size:9pt" background="/workflow/images/tab_bg.gif" nowrap>&nbsp;登记号:
	         <span id="fldDJH" style="color:red"></span></td>
	      <td><img border=0 src="/workflow/images/tab_right.gif"></td>
	      <td>&nbsp;&nbsp;</td>
	    </tr>
	    </tbody>
   </table>
  
  
<table border="0" cellspacing="0" cellpadding="0" class="table3" >
    <tbody>
     <tr>
		<td class="tit" >标题</td>
		<td colspan="3">
		    <textarea id="receiveTitle" name="receiveTitle" cols="90" rows="2"  style="float:left"></textarea>	
		</td>
	  </tr>
	  <tr >
		<td class="tit" >来文机关</td>
		<td >
		  <input type="text" id="receiveOffice" name="receiveOffice" style="float:left">
		</td>
		
		<td class="tit" >来文字号</td>
		<td >
		    <input type="text" id="receiveWord" name="receiveWord" style="float:left">
		</td>
	  </tr>
	  <tr >
		<td class="tit" >文件分类:</td>
		<td >
		  <div style="float:left">
		    <input type='radio' class="praxes_type" id="fileType" name='fileType' value="1" checked />办件
            <input type='radio' class="praxes_type" id="fileType" name='fileType' value="2"  onclick=""/>阅件
            </div>
		</td>
		
		<td class="tit" >收文日期</td>
		<td >
		    <input type="text" id="receiveDate" name="receiveDate" readonly="readonly" style="float:left">
		</td>
	  </tr>
	 <tr>
	 	<td class="tit" >紧急程度</td>
		<td>
		   <script language="JavaScript">
            document.writeln(JspParamUtil.paramData('urgent','0','urgency_level','',[['style','width:100%']]));
          </script>
          <!--  
		   <select name="urgent" id="urgent"  style="width:100%">  
		      <option value = "0" selected>正常</option>
	          <option value = "1">急件</option>
	          <option value = "2">特急</option>
           </select> -->
		</td>
		
		<td class="tit" >秘密等级:</td>
		<td >
		   <script language="JavaScript">
		      document.writeln(JspParamUtil.paramData('securityLevel','0','security_level','',[['style','width:100%']]));
		   </script>
		<!--  
		   <select id="securityLevel" name="securityLevel" style="width:234px;">
	          <option value = "0" selected>平件</option>
	          <option value = "2">秘密</option>
	          <option value = "3">机密</option>
	          <option value = "4">绝密</option>
	        </select>-->
		</td>
	  </tr>
	  
	 <tr>
	 	<td class="tit" >办结日期</td>
		<td colspan="3">
		   <input type="text" id="finishDate" name="finishDate" onclick="selectDeleteTime()" style="float:left"/>
		</td>
	  </tr>
	  
	  <tr>
	 	<td class="tit" >拟办意见</td>
		<td colspan="3">
		     <textarea id="internalRemark" name="internalRemark" cols="90" rows="3" style="float:left"></textarea>	
		</td>
	  </tr>
	 </tbody>
</table>
  <div class="clear cH1"></div>
</div>
   <!-- <div class="box2_bottom"></div> -->
</div>

<div class="box2" id="tab1c2">
         <div class="box2_content">
          <table border="0" cellspacing="0" cellpadding="0" class="table2">
            <thead>
              <tr>
			    <th>文件名</th>
			    <th>大小(byte)</th>
			    <th>操作</th>
              </tr>
            </thead>
            <tbody id="attachMentTby">
             </tbody>
          </table>
           <div style="width:726px;margin:0 auto;background:url(../images/icon.png) 0 -460px repeat-x">
                 <span id="spanButtonPlaceHolder" style="margin-left:16px;margin-top:56px;font-weight:bold"></span>
            </div>
	          
         </div>
</div>
<script>
$(function(){ 
	//表格效果
		$(".table2 tr:nth-child(2n)").addClass("tab");
		$(".table2tr").hover(function(){
			$(this).addClass("hover");},function(){
			$(this).removeClass("hover")
		})
		
		$(".tab1 h3").idTabs("!mouseover"); 
});

/*** 附件上传 begin ***/
var settings = {
	flash_url : "/js/swfupload.swf",
	//flash9_url : "../swfupload/swfupload_fp9.swf",
	//upload_url: "/p.ajaxutf",
	upload_url : "/servlet/WorkflowUploadServlet",
    file_post_name:"attachments",
	file_size_limit : "10 MB",
	file_types : "*.txt;*.rar;*.zip;*.xls;*.xlsx;*.doc;*.docx;*.pdf;*.ppt;*.pptx;*.bmp;*.jpg;*.jpeg;*.png;*.gif;*.mpg;*.mpeg;*.mp3;*.mp4;*.avi",
	file_types_description : "选择文件",
	file_upload_limit : 100,
	file_queue_limit : 0,
    use_query_string : false,		
	custom_settings : {
		progressTarget : "fsUploadProgress"
	},
	debug: false,

	// Button settings
			button_image_url : "/js/swfupload/button_notext.png",
					button_placeholder_id : "spanButtonPlaceHolder",
					button_text: "<span class='fl'>选择文件</span>",
					button_width: 64,
					button_height: 24,
					button_window_mode : SWFUpload.WINDOW_MODE.TRANSPARENT,
					button_cursor : SWFUpload.CURSOR.HAND,
					button_text_style: ".theFont { font-size: 16; }",
					button_text_top_padding: 2,
					button_text_left_padding: 2,
					
	// The event handler functions are defined in handlers.js
	swfupload_preload_handler : preLoad,
	swfupload_load_failed_handler : loadFailed,
	file_queued_handler : fileQueued,
	file_queue_error_handler : fileQueueError,
	file_dialog_complete_handler : selectFileComplete,//fileDialogComplete,
	upload_start_handler : uploadStart,
	upload_progress_handler : uploadProgress,
	upload_error_handler :uploadError,
	upload_complete_handler : uploadComplete,	
	upload_success_handler : function uploadSuccess(file, serverData){
		try {
			uploadmediaSuccess(file, serverData);
			var progress = new FileProgress(file, this.customSettings.progressTarget);
			progress.setComplete();
			progress.setStatus("上传成功.");
			progress.toggleCancel(false);
		} catch (ex) {
			this.debug(ex);
		}
	}
}; 

var attachmentIdsArray = [];
function uploadmediaSuccess(file,serverData) {
  //  var xmlDoc = serverData.documentElement;
    //var responseObject = xmlDoc.getElementsByTagName("Response")[0];
    
	var jsonData=eval("("+serverData+")");//转换为json对象 
	var attachmentId=jsonData.attachmentId;
	attachmentIdsArray.push(attachmentId);
	//var fileName=jsonData.fileName;
	var fileSize=jsonData.fileSize;
	var path=jsonData.path;
	var fileName = file.name;
	//if(fileName && fileName.length > 12) fileName = fileName.substring(0,12)+'...';
	
	var attachmentArray =[];
	attachmentArray.push("<tr>");
	attachmentArray.push("<td><a class=\"color3\"  href=\"<%=basePath%>WorkflowAttachMentDownload?attachmentId="+attachmentId+"\">"+fileName+"</a></td>");
	attachmentArray.push("<td>"+fileSize+"</td>");
	attachmentArray.push("<td><a class=\"color3\"  href=\"#\" onclick=\"deleteReceiveFileAttachment(\'"+attachmentId+"\',this);\" >删除</a></td>");
	attachmentArray.push("</tr>");
	
	$("#attachMentTby").append(attachmentArray.join(""));
	//$("#vidoe_name").show().find("span").html(fileName);
}

function deleteReceiveFileAttachment(attachmentId,tdObject){
	var bcReq = new BcRequest('workflow','receiveFileAction','deleteReceiveFileAttachment');
	bcReq.setExtraPs({"attachmentId":attachmentId});
	bcReq.setSuccFn(function(data,status){
		 $(tdObject).parent().parent().remove();
	});
	bcReq.postData();
}

swfupload = new SWFUpload(settings);
swfupload.uploadStopped = false;
//选择文件后
function selectFileComplete(numFilesSelected, numFilesQueued) {
	try {
		$("#fsUploadProgress").show();
		//document.getElementById("ScrollContent").scrollTop=document.getElementById("ScrollContent").scrollHeight;
		//$("#submit_flag").val("Y");
		swfupload.startUpload(); //选择文件后立刻上传
	} catch (ex)  {		
        swfupload.debug(ex);
	}    
}
//全部附件都上传完成后执行的函数
function allUploadComplete(data){
	 //$("#submit_flag").val("N");
	 $("#comment").val("");
	 $("#fsUploadProgress").slideUp("slow");
	 $("#fsUploadProgress").empty();
     //toastr.success("附件上传成功！");
	//commentForm.reset();
}
/*** 附件上传 end ***/
</script>