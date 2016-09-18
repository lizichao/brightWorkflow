<%@ page contentType="text/html; charset=GBK" %>
<%
  String userid = (String)session.getAttribute("userid");
  String username =(String)session.getAttribute("username");
  String usertype =(String)session.getAttribute("usertype");
  String deptname =(String)session.getAttribute("deptname");
  String deptid =(String)session.getAttribute("deptid");
  String registerNumber =(String)session.getAttribute("registerNumber");
  String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%>

<script type="text/javascript" src="/js/swfupload.js"></script>
<script type="text/javascript" src="/js/upload/fileprogress.js"></script>
<script type="text/javascript" src="/js/upload/handlers.js"></script>
<script type="text/javascript" src="/js/jquery/jquery.form.js"></script>
<script type="text/javascript" src="/workflow/js/jquery.idTabs.min.js"></script>
<script src="/workflow/common/jspParamUtil.js"></script>
<script src="/platform/public/sysparam.js"></script>


<script>
function beforeSubmit(formJsonData){
	var variableMap = new Brightcom.workflow.HashMap(); 
	variableMap.put("isDirector","1");
	variableMap.put("processTitle",$("#fwtm").val());
	
	var attachments = attachmentIdsArray.length==0 ? "" : attachmentIdsArray.join(",");
	
	formJsonData.flddjh = $("#fldDJH").text();
	formJsonData.fwtm = $("#fwtm").val();
	formJsonData.fldgwlx = "";
	formJsonData.fldzbbmid = '<%=deptid%>';
	formJsonData.fldzbbmmc = '<%=deptname%>';
	formJsonData.fldngr = $("#fldngr").val();
	formJsonData.fldjjcd = $("#fldjjcd").val();
	formJsonData.fldmj = $("#fldmj").val();
	formJsonData.fldwz = $("#fldwz").val();
	formJsonData.processDefKey = $("#processDefKey").val();
	formJsonData.originalAttachmentId = $("#originalAttachmentId").val();
	formJsonData.attachments = attachments;
	formJsonData.processParam =variableMap.toJsonObject()
}

$(document).ready(function(){	
	   Brightcom.workflow.beforeSubmit = beforeSubmit;
	  
     if('null' == '<%=registerNumber%>'){
	    var bcReq = new BcRequest('workflow','publishAction','getPublishNumber');
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
		
	
	 var video_bar = $("#video_bar");
	 var video_percent = $("#video_percent");
	 var video_time = $("#video_time");
	/****  包含附件,采用ajaxForm提交 begin ****/
	 var options = {
			beforeSubmit:function(formData, jqForm, options) {
	 			//var queryString = $.param(formData);
	 			//$("#paper_name_hide").val(encodeURI($("#paper_name_show").val()));
	 		//	return true;
	 		},
	 		uploadProgress: function(event, position, total, percentComplete) {
	 			//video_time.html(timer( new Date(), percentComplete));
	 			
	 			//var percentVal = percentComplete + '%'; //获得进度
	 			//video_bar.width(percentVal); //上传进度条宽度变宽
	 			//video_percent.html("已上传" + percentVal); //显示上传进度百分比
	 		}, 	
			success:(function(data,status) {
				var attachmentIdArray = [];
				var fileName = "";
				var attachmentId ="";
				for(var i=0;i<data.Data.length;i++){
					var _resultRec = data.Data[i];
					 attachmentId = _resultRec.attachmentId;
					fileName = _resultRec.file_name;
					attachmentIdArray.push(attachmentId);
				}
				$("#originalAttachmentId").val(attachmentIdArray.join(","));
				
				//var attachmentArray =[];
			    
			    $("#originalAttachment").html("");
				$("#originalAttachment").append("<a class=\"color3\"  href=\"<%=basePath%>WorkflowAttachMentDownload?attachmentId="+attachmentId+"\">"+fileName+"</a>");
				/*
				if(data.error_description) {
					toastr.error("保存失败！错误信息[" + data.error_description + "]", "错误信息");
					errer_courseware();
				} else {
					toastr.clear();
					toastr.success("提交成功!");
					cancel_courseware();
					loadCoursewareData("20",$("#folder_code").val(),$("#folder_id").val(),$("#folder_name").val(),1);
				}*/
			}),
			error:(function(data,status){
				toastr.error("保存失败！错误信息[" + data.error_description + "]", "错误信息");
			}),
			dataType:"json"
	  }
	  $('#startProcessform').submit(function() {
			$(this).ajaxSubmit(options);
			return false; //必须返回false,阻止页面提交
	  });
});	

function timer(initTime, percentComplete) {
	if (percentComplete == 100) {
		return "00:00:00";
	} else {
	    // 剩余时间 = 已进行时间 / 已完成百分比 * 剩下百分比
	    var ts = ((new Date()) - initTime) / percentComplete * (100 - percentComplete); //计算剩余的毫秒数      
		var dd = parseInt(ts / 1000 / 60 / 60 / 24, 10);//计算剩余的天数      
		var hh = parseInt(ts / 1000 / 60 / 60 % 24, 10);//计算剩余的小时数      
		var mm = parseInt(ts / 1000 / 60 % 60, 10);//计算剩余的分钟数      
		var ss = parseInt(ts / 1000 % 60, 10);//计算剩余的秒数      
		dd = checkTime(dd);      
		hh = checkTime(hh);      
		mm = checkTime(mm);      
		ss = checkTime(ss);
		return hh + ":" + mm + ":" + ss;
	}  
}

function selectVideo(obj) {
	// return selectInputFile(obj, ".mp4,.avi,.mp3", "请选择正确的视频文件！", true);
	//超过
/* 	var msgstr = "";
	if($("#resource_type_id").val()=="2030"){
	  var filezise=document.getElementById("video_attachment").files[0].size;
	  if(filezise>20*1024*1024){
	   msgstr = msgstr + "请选择小于20MB的Office文件！";
	    toastr.error(msgstr, "错误信息");    	
    	return;
	  }
	} */
	// 隐藏文件选择框 显示文件名
	  $("#startProcessform").submit();
}
</script>
<input type="hidden" id="workflowSubmitAction" name="workflowSubmitAction" value="workflow@@publishAction@@startPublish">
<input type="hidden" id="sysName" name="sysName" value="workflow">
<input type="hidden" id="oprID" name="oprID" value="publishAction">
<input type="hidden" id="actions" name="actions" value="uploadAttachMentList">
<input type="hidden" id="attachmentIds" name="attachmentIds" value="">
<input type="hidden" id="originalAttachmentId" name="originalAttachmentId" value="">


   	<h3 class="tit" style="padding:0px;width:735px"><a href="#tab1c1">基本属性</a><a href="#tab1c2">附件</a></h3>
   	         <div class="box2" id="tab1c1">
               	  <div class="box2_content">
                  <!--  <h4 class="tit">外发公文登记</h4> --> 
    <table border=0 cellpadding=0 cellspacing=0>
	    <tbody>
	    <tr>
	      <td colspan="4" height="5"></td>
	    </tr>
	    <tr>
	      <td><img border=0 src="/workflow/images/tab_left.gif"></td>
	      <td align="center" style="font-size:9pt" background="/workflow/images/tab_bg.gif" nowrap>&nbsp;登记号:
	         <span id="fldDJH" style="color:red"></span></td>
	      <td><img border=0 src="/workflow/images/tab_right.gif"></td>
	      <td>&nbsp;&nbsp;</td>
	    </tr>
	    </tbody>
   </table>
  
   <table border="0" cellspacing="0" cellpadding="0" class="table3">
     <tbody>
    
      <tr >
		<td class="tit" >发文题名:</td>
		<td colspan="3">
		    <textarea id="fwtm" name="fwtm" cols="90" rows="2"  style="float:left"></textarea>	
		</td>
	  </tr>
   
	  <tr>
		<td class="tit"  width="130px">公文类型:</td>
		<td >
		    外发公文    
		</td>
		<td class="tit" width="130px" >主办部门:</td>
		<td >
		   <%=deptname%>
		</td>
	  </tr>
	  <tr>
		<td class="tit">拟稿人:</td>
		<td>
		   <input type="hidden" id="fldngr" name="fldngr" value="<%=username%>" readonly="readonly">
		   <%=username%>
		</td>
		
		<td class="tit">紧急程度:</td>
		<td>
		 <script language="JavaScript">
           document.writeln(JspParamUtil.paramData('fldjjcd','0','urgency_level','',[['style','width:100%']]));
         </script>
		</td>
	  </tr>
	 <tr>
		<td class="tit">密级:</td>
		<td>
		   <script language="JavaScript">
		      document.writeln(JspParamUtil.paramData('fldmj','0','security_level','',[['style','width:100%']]));
		   </script>
		      <!--  
		   <select id="fldmj" name="fldmj" style="width:100%" >
	          <option value = "0" selected>平件</option>
	          <option value = "1">秘密</option>
	          <option value = "2">机密</option>
	          <option value = "3">绝密</option>
	        </select>-->
		</td>
		
		<td class="tit">文种:</td>
		<td>
		  <script language="JavaScript">
		      document.writeln(JspParamUtil.paramData('fldwz','','record_type',[['','请选择文种']],[['style','width:100%']]));
		   </script>
		<!--  
		   <select name="fldwz" id="fldwz" style="width:100%">  
		           <option value = "00" selected></option>
		          <option value = "01">决定</option>
		          <option value = "02">通告</option>
		          <option value = "03">通知</option>
		          <option value = "04">通报</option>
		          <option value = "05">报告</option>
		          <option value = "06">请示</option>
		          <option value = "07">批复</option>
		          <option value = "08">意见</option>
		          <option value = "09">函</option>
		          <option value = "10">会议纪要</option>
		          <option value = "11">命令</option>
		          <option value = "12">规定</option>
           </select> -->
		</td>
	  </tr>
	  
	  <tr>
		<td class="tit">拟办意见:</td>
		<td colspan="3">
		    <textarea id="internalRemark" name="internalRemark" cols="90" rows="3" style="float:left"></textarea>	
		</td>
	  </tr>
	  
	  <tr class="tit">
		<td><span class="fl">上传原文：</span></td>
		<td colspan="3" style="text-align:left;">
		   <div style="float:left">
		     <input id="attachments" name=attachments style="float:left" type="file" accept=".doc,.docx;" onchange="return selectVideo(this);"/>
		    <br>
		    <div id="originalAttachment" style="float:left;" >
		    </div>
		 </div>
		  
        <!--
		  <p>
		  
		  </p>
		  <p id="originalAttachment" ></p>
			  <ul class="select">
				<li>
				   <span id="vidoe_file" style="position:relative;width:94px;height:38px;"><p class="cBtnNormal">选择文件</p>
				     
				   </span>
				   <span id="vidoe_name" style="display:none;">
				   </span>
				</li>	
						
				<li class="li_03">
					<div class="jd"><img id="video_bar" name="video_bar" src="/images/yuexue2/video/Img_27.png" alt="" style="height:20px;width:0%;" /></div>
					<div class="jdp" id="video_percent" name="video_percent">已上传0%</div>
					<div class="time" id="video_time" name="video_time">00:00:00</div>
				</li>	
			</ul>-->
		<!-- 
             <ul class="select">
				<li style="height:40px;width:220px;">
				   <span id="vidoe_file" style="position:relative;width:74px;height:38px;margin-left:16px;margin-top:5px;">
                     <p class="cBtnNormal">选择文件</p>
				       <input id="attachments" name="attachments" type="file" style="width:94px;height:38px;position: absolute;opacity:0;right:0;top:0;filter:progid:DXImageTransform.Microsoft.Alpha(opacity=0);" />
				   </span>
				   <span id="vidoe_name" style="display:none;">
				   </span>
				</li>				
				<li class="li_03">
					<div class="jd"><img id="video_bar" name="video_bar" src="/workflow/image/Img_27.png" alt="" style="height:20px;width:0%;" /></div>
					<div class="jdp" id="video_percent" name="video_percent">已上传0%</div>
					<div class="time" id="video_time" name="video_time">00:00:00</div>
				</li>
			 </ul> -->
           </td>
	  </tr>
	
	  <!-- 
		<tr class="title">
			<td><span class="fl">上传文件：</span></td>
			<td colspan="3">
             <ul class="select">
				<li style="height:40px;width:220px;">
				   <span id="vidoe_file" style="position:relative;width:74px;height:38px;margin-left:16px;margin-top:5px;">
                     <p class="cBtnNormal">选择文件</p>
				      <input id="attachments" name="attachments" type="file" style="width:94px;height:38px;position: absolute;opacity:0;right:0;top:0;filter:progid:DXImageTransform.Microsoft.Alpha(opacity=0);" />
				   </span>
				   <span id="vidoe_name" style="display:none;">
				   </span>
				</li>				
				<li class="li_03">
					<div class="jd"><img id="video_bar" name="video_bar" src="/workflow/image/Img_27.png" alt="" style="height:20px;width:0%;" /></div>
					<div class="jdp" id="video_percent" name="video_percent">已上传0%</div>
					<div class="time" id="video_time" name="video_time">00:00:00</div>
				</li>
			 </ul>
            </td>
		</tr> -->
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
            <!-- 
         
			 <tr>
			   <td>
			 <a class="color3" href="#">收文处理单.doc</a></td>
			   <td>5KB</td>
			   <td><a class="color3" href="#">下载</a></td>
			   </tr> <tr>
			   <td>
			 <a class="color3" href="#">收文处理单.doc</a></td>
			   <td>5KB</td>
			   <td><a class="color3" href="#">下载</a></td>
			   </tr>
			    <tr>
			   <td>总计</td>
			   <td>5KB</td>
			   <td>&nbsp;</td>
			   </tr>
			    <tr>
				   <td>  <span id="spanButtonPlaceHolder"></span></td>
			   </tr>
			    <tr>
				   <td> <div id="fsUploadProgress" style="clear:both"></div></td>
			   </tr>
            </tbody> -->
          </table>
           <div style="width:726px;margin:0 auto;background:url(../images/icon.png) 0 -460px repeat-x">
                 <span id="spanButtonPlaceHolder" style="margin-left:16px;margin-top:56px;font-weight:bold"></span>
            </div>
	          
         </div>
            <!--  <div class="box2_bottom"></div>--> 
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
	//post_params : {'sysName':'workflow','oprID':'publishAction','actions':'uploadAttachMentList'},
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
	attachmentArray.push("<td><a class=\"color3\"  href=\"#\" onclick=\"deletePublishAttachment(\'"+attachmentId+"\',this);\" >删除</a></td>");
	attachmentArray.push("</tr>");
	
	$("#attachMentTby").append(attachmentArray.join(""));
	//$("#vidoe_name").show().find("span").html(fileName);
}

function deletePublishAttachment(attachmentId,tdObject){
	var bcReq = new BcRequest('workflow','publishAction','deletePublishAttachment');
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
/*
function uploadSuccess(file, serverData){
	try {
		//uploadmediaSuccess(file, serverData);
		var data = eval("(" + serverData + ")");  
		var progress = new FileProgress(file);
		progress.setComplete();
		progress.setStatus("上传成功.");
		progress.toggleCancel(false);
	} catch (ex) {
		this.debug(ex);
	}
}*/
/*** 附件上传 end ***/
</script>