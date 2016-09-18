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
	/****  ��������,����ajaxForm�ύ begin ****/
	 var options = {
			beforeSubmit:function(formData, jqForm, options) {
	 			//var queryString = $.param(formData);
	 			//$("#paper_name_hide").val(encodeURI($("#paper_name_show").val()));
	 		//	return true;
	 		},
	 		uploadProgress: function(event, position, total, percentComplete) {
	 			//video_time.html(timer( new Date(), percentComplete));
	 			
	 			//var percentVal = percentComplete + '%'; //��ý���
	 			//video_bar.width(percentVal); //�ϴ���������ȱ��
	 			//video_percent.html("���ϴ�" + percentVal); //��ʾ�ϴ����Ȱٷֱ�
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
					toastr.error("����ʧ�ܣ�������Ϣ[" + data.error_description + "]", "������Ϣ");
					errer_courseware();
				} else {
					toastr.clear();
					toastr.success("�ύ�ɹ�!");
					cancel_courseware();
					loadCoursewareData("20",$("#folder_code").val(),$("#folder_id").val(),$("#folder_name").val(),1);
				}*/
			}),
			error:(function(data,status){
				toastr.error("����ʧ�ܣ�������Ϣ[" + data.error_description + "]", "������Ϣ");
			}),
			dataType:"json"
	  }
	  $('#startProcessform').submit(function() {
			$(this).ajaxSubmit(options);
			return false; //���뷵��false,��ֹҳ���ύ
	  });
});	

function timer(initTime, percentComplete) {
	if (percentComplete == 100) {
		return "00:00:00";
	} else {
	    // ʣ��ʱ�� = �ѽ���ʱ�� / ����ɰٷֱ� * ʣ�°ٷֱ�
	    var ts = ((new Date()) - initTime) / percentComplete * (100 - percentComplete); //����ʣ��ĺ�����      
		var dd = parseInt(ts / 1000 / 60 / 60 / 24, 10);//����ʣ�������      
		var hh = parseInt(ts / 1000 / 60 / 60 % 24, 10);//����ʣ���Сʱ��      
		var mm = parseInt(ts / 1000 / 60 % 60, 10);//����ʣ��ķ�����      
		var ss = parseInt(ts / 1000 % 60, 10);//����ʣ�������      
		dd = checkTime(dd);      
		hh = checkTime(hh);      
		mm = checkTime(mm);      
		ss = checkTime(ss);
		return hh + ":" + mm + ":" + ss;
	}  
}

function selectVideo(obj) {
	// return selectInputFile(obj, ".mp4,.avi,.mp3", "��ѡ����ȷ����Ƶ�ļ���", true);
	//����
/* 	var msgstr = "";
	if($("#resource_type_id").val()=="2030"){
	  var filezise=document.getElementById("video_attachment").files[0].size;
	  if(filezise>20*1024*1024){
	   msgstr = msgstr + "��ѡ��С��20MB��Office�ļ���";
	    toastr.error(msgstr, "������Ϣ");    	
    	return;
	  }
	} */
	// �����ļ�ѡ��� ��ʾ�ļ���
	  $("#startProcessform").submit();
}
</script>
<input type="hidden" id="workflowSubmitAction" name="workflowSubmitAction" value="workflow@@publishAction@@startPublish">
<input type="hidden" id="sysName" name="sysName" value="workflow">
<input type="hidden" id="oprID" name="oprID" value="publishAction">
<input type="hidden" id="actions" name="actions" value="uploadAttachMentList">
<input type="hidden" id="attachmentIds" name="attachmentIds" value="">
<input type="hidden" id="originalAttachmentId" name="originalAttachmentId" value="">


   	<h3 class="tit" style="padding:0px;width:735px"><a href="#tab1c1">��������</a><a href="#tab1c2">����</a></h3>
   	         <div class="box2" id="tab1c1">
               	  <div class="box2_content">
                  <!--  <h4 class="tit">�ⷢ���ĵǼ�</h4> --> 
    <table border=0 cellpadding=0 cellspacing=0>
	    <tbody>
	    <tr>
	      <td colspan="4" height="5"></td>
	    </tr>
	    <tr>
	      <td><img border=0 src="/workflow/images/tab_left.gif"></td>
	      <td align="center" style="font-size:9pt" background="/workflow/images/tab_bg.gif" nowrap>&nbsp;�ǼǺ�:
	         <span id="fldDJH" style="color:red"></span></td>
	      <td><img border=0 src="/workflow/images/tab_right.gif"></td>
	      <td>&nbsp;&nbsp;</td>
	    </tr>
	    </tbody>
   </table>
  
   <table border="0" cellspacing="0" cellpadding="0" class="table3">
     <tbody>
    
      <tr >
		<td class="tit" >��������:</td>
		<td colspan="3">
		    <textarea id="fwtm" name="fwtm" cols="90" rows="2"  style="float:left"></textarea>	
		</td>
	  </tr>
   
	  <tr>
		<td class="tit"  width="130px">��������:</td>
		<td >
		    �ⷢ����    
		</td>
		<td class="tit" width="130px" >���첿��:</td>
		<td >
		   <%=deptname%>
		</td>
	  </tr>
	  <tr>
		<td class="tit">�����:</td>
		<td>
		   <input type="hidden" id="fldngr" name="fldngr" value="<%=username%>" readonly="readonly">
		   <%=username%>
		</td>
		
		<td class="tit">�����̶�:</td>
		<td>
		 <script language="JavaScript">
           document.writeln(JspParamUtil.paramData('fldjjcd','0','urgency_level','',[['style','width:100%']]));
         </script>
		</td>
	  </tr>
	 <tr>
		<td class="tit">�ܼ�:</td>
		<td>
		   <script language="JavaScript">
		      document.writeln(JspParamUtil.paramData('fldmj','0','security_level','',[['style','width:100%']]));
		   </script>
		      <!--  
		   <select id="fldmj" name="fldmj" style="width:100%" >
	          <option value = "0" selected>ƽ��</option>
	          <option value = "1">����</option>
	          <option value = "2">����</option>
	          <option value = "3">����</option>
	        </select>-->
		</td>
		
		<td class="tit">����:</td>
		<td>
		  <script language="JavaScript">
		      document.writeln(JspParamUtil.paramData('fldwz','','record_type',[['','��ѡ������']],[['style','width:100%']]));
		   </script>
		<!--  
		   <select name="fldwz" id="fldwz" style="width:100%">  
		           <option value = "00" selected></option>
		          <option value = "01">����</option>
		          <option value = "02">ͨ��</option>
		          <option value = "03">֪ͨ</option>
		          <option value = "04">ͨ��</option>
		          <option value = "05">����</option>
		          <option value = "06">��ʾ</option>
		          <option value = "07">����</option>
		          <option value = "08">���</option>
		          <option value = "09">��</option>
		          <option value = "10">�����Ҫ</option>
		          <option value = "11">����</option>
		          <option value = "12">�涨</option>
           </select> -->
		</td>
	  </tr>
	  
	  <tr>
		<td class="tit">������:</td>
		<td colspan="3">
		    <textarea id="internalRemark" name="internalRemark" cols="90" rows="3" style="float:left"></textarea>	
		</td>
	  </tr>
	  
	  <tr class="tit">
		<td><span class="fl">�ϴ�ԭ�ģ�</span></td>
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
				   <span id="vidoe_file" style="position:relative;width:94px;height:38px;"><p class="cBtnNormal">ѡ���ļ�</p>
				     
				   </span>
				   <span id="vidoe_name" style="display:none;">
				   </span>
				</li>	
						
				<li class="li_03">
					<div class="jd"><img id="video_bar" name="video_bar" src="/images/yuexue2/video/Img_27.png" alt="" style="height:20px;width:0%;" /></div>
					<div class="jdp" id="video_percent" name="video_percent">���ϴ�0%</div>
					<div class="time" id="video_time" name="video_time">00:00:00</div>
				</li>	
			</ul>-->
		<!-- 
             <ul class="select">
				<li style="height:40px;width:220px;">
				   <span id="vidoe_file" style="position:relative;width:74px;height:38px;margin-left:16px;margin-top:5px;">
                     <p class="cBtnNormal">ѡ���ļ�</p>
				       <input id="attachments" name="attachments" type="file" style="width:94px;height:38px;position: absolute;opacity:0;right:0;top:0;filter:progid:DXImageTransform.Microsoft.Alpha(opacity=0);" />
				   </span>
				   <span id="vidoe_name" style="display:none;">
				   </span>
				</li>				
				<li class="li_03">
					<div class="jd"><img id="video_bar" name="video_bar" src="/workflow/image/Img_27.png" alt="" style="height:20px;width:0%;" /></div>
					<div class="jdp" id="video_percent" name="video_percent">���ϴ�0%</div>
					<div class="time" id="video_time" name="video_time">00:00:00</div>
				</li>
			 </ul> -->
           </td>
	  </tr>
	
	  <!-- 
		<tr class="title">
			<td><span class="fl">�ϴ��ļ���</span></td>
			<td colspan="3">
             <ul class="select">
				<li style="height:40px;width:220px;">
				   <span id="vidoe_file" style="position:relative;width:74px;height:38px;margin-left:16px;margin-top:5px;">
                     <p class="cBtnNormal">ѡ���ļ�</p>
				      <input id="attachments" name="attachments" type="file" style="width:94px;height:38px;position: absolute;opacity:0;right:0;top:0;filter:progid:DXImageTransform.Microsoft.Alpha(opacity=0);" />
				   </span>
				   <span id="vidoe_name" style="display:none;">
				   </span>
				</li>				
				<li class="li_03">
					<div class="jd"><img id="video_bar" name="video_bar" src="/workflow/image/Img_27.png" alt="" style="height:20px;width:0%;" /></div>
					<div class="jdp" id="video_percent" name="video_percent">���ϴ�0%</div>
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
			    <th>�ļ���</th>
			    <th>��С(byte)</th>
			    <th>����</th>
              </tr>
            </thead>
            <tbody id="attachMentTby">
             </tbody>
            <!-- 
         
			 <tr>
			   <td>
			 <a class="color3" href="#">���Ĵ���.doc</a></td>
			   <td>5KB</td>
			   <td><a class="color3" href="#">����</a></td>
			   </tr> <tr>
			   <td>
			 <a class="color3" href="#">���Ĵ���.doc</a></td>
			   <td>5KB</td>
			   <td><a class="color3" href="#">����</a></td>
			   </tr>
			    <tr>
			   <td>�ܼ�</td>
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
	//���Ч��
		$(".table2 tr:nth-child(2n)").addClass("tab");
		$(".table2tr").hover(function(){
			$(this).addClass("hover");},function(){
			$(this).removeClass("hover")
		})
		
		$(".tab1 h3").idTabs("!mouseover"); 
});
/*** �����ϴ� begin ***/
var settings = {
	flash_url : "/js/swfupload.swf",
	//flash9_url : "../swfupload/swfupload_fp9.swf",
	//upload_url: "/p.ajaxutf",
	//post_params : {'sysName':'workflow','oprID':'publishAction','actions':'uploadAttachMentList'},
	upload_url : "/servlet/WorkflowUploadServlet",
    file_post_name:"attachments",
	file_size_limit : "10 MB",
	file_types : "*.txt;*.rar;*.zip;*.xls;*.xlsx;*.doc;*.docx;*.pdf;*.ppt;*.pptx;*.bmp;*.jpg;*.jpeg;*.png;*.gif;*.mpg;*.mpeg;*.mp3;*.mp4;*.avi",
	file_types_description : "ѡ���ļ�",
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
	button_text: "<span class='fl'>ѡ���ļ�</span>",
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
			progress.setStatus("�ϴ��ɹ�.");
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
    
	var jsonData=eval("("+serverData+")");//ת��Ϊjson���� 
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
	attachmentArray.push("<td><a class=\"color3\"  href=\"#\" onclick=\"deletePublishAttachment(\'"+attachmentId+"\',this);\" >ɾ��</a></td>");
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
//ѡ���ļ���
function selectFileComplete(numFilesSelected, numFilesQueued) {
	try {
		$("#fsUploadProgress").show();
		//document.getElementById("ScrollContent").scrollTop=document.getElementById("ScrollContent").scrollHeight;
		//$("#submit_flag").val("Y");
		swfupload.startUpload(); //ѡ���ļ��������ϴ�
	} catch (ex)  {		
        swfupload.debug(ex);
	}    
}
//ȫ���������ϴ���ɺ�ִ�еĺ���
function allUploadComplete(data){
	 //$("#submit_flag").val("N");
	 $("#comment").val("");
	 $("#fsUploadProgress").slideUp("slow");
	 $("#fsUploadProgress").empty();
     //toastr.success("�����ϴ��ɹ���");
	//commentForm.reset();
}
/*
function uploadSuccess(file, serverData){
	try {
		//uploadmediaSuccess(file, serverData);
		var data = eval("(" + serverData + ")");  
		var progress = new FileProgress(file);
		progress.setComplete();
		progress.setStatus("�ϴ��ɹ�.");
		progress.toggleCancel(false);
	} catch (ex) {
		this.debug(ex);
	}
}*/
/*** �����ϴ� end ***/
</script>