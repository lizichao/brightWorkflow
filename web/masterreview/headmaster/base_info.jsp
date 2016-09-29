<%@ page contentType="text/html; charset=GBK" %>
<%
  String userid = (String)session.getAttribute("userid");
  String username =(String)session.getAttribute("username");
  String usertype =(String)session.getAttribute("usertype");
  String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
  
  //sourceType为表示是从开始节点进入这个页面还是从驳回充填节点进入这个页面，1表示从开始节点进入这个页面
  String sourceType =(String)request.getParameter("sourceType");
 %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="keywords" content="" />
<meta name="description" content="" />
<title>深圳市校长职级评审系统</title>
<script>
var sourceType = '<%=sourceType%>'
$(function(){
	//1表示从开始节点进入这个页面
	if(sourceType && sourceType == '1'){
		
		//$("#base_next_btn").text("保存");
		$("#base_change_div").hide();
	}
	Headmaster.findMasterBaseInfo('<%=userid%>');
	Headmaster.initSelectCom();
	/*
	 * uploadSingleId:上传按钮显示位置id
	 * file_upload_type:区分某种业务上传类型，比如是学历上传还是学位上传
	 buttonName ： 按钮名称
	 hiddenAttachId：隐藏附件id，选中后把附件值保存在该隐藏域里
	 hiddenDisplayId:附件显示的div id
	 uploadType:区分是图片上传还是其他上传
	 */
	 /*
	 var config = {
				accept: {
	      	      title: 'Images',
	      	      extensions: 'gif,jpg,jpeg,bmp,png',
			      mimeTypes: 'image/*' //video/*;application/msword
	            },
		    fileNumLimit:1,
		    param:{headImg:'1'}
	};*/
	//Headmaster.initWebUploader('personImgspan','','personImgType','上传照片','personImgAttachId','',"img",config);
	initSwfuploadContainer();
});





var swfupload1;
var defaultSettings ;
function initSwfuploadContainer(){
	/*** 附件上传 begin ***/
	  defaultSettings = {
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
	    use_query_string : true,		
		custom_settings : {
			progressTarget : "fsUploadProgress"
		},
		debug: false,

		// Button settings
		//button_placeholder : $("#personImg"),
			button_image_url : "/js/swfupload/button_notext.png",
			button_placeholder_id : "personImg",
		//	button_text: "<p style='color:red'><a  href='javascript:void(0);' target='_self' title=''>上传照片</a></p>",
		    button_text:"<span class='theFont'>上传照片</span>",
			button_width: 64,
			button_height: 24,
			button_window_mode : SWFUpload.WINDOW_MODE.TRANSPARENT,
			button_cursor : SWFUpload.CURSOR.HAND,
			button_text_style: ".theFont {color:#FCFAFA; line-height:26px;margin-top:5px;font-size:15px}",
			button_text_top_padding: 2,
			button_text_left_padding: 2,
						
		
		// The event handler functions are defined in handlers.js
		swfupload_preload_handler : preLoad,
		swfupload_load_failed_handler : loadFailed,
		file_queued_handler : fileQueued,
		file_queue_error_handler : fileQueueError,
		file_dialog_complete_handler : selectFileComplete1,//fileDialogComplete,
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
		},
		 debug: false  //是否显示调试窗口
	}; 
		swfupload1 = new SWFUpload(defaultSettings);
		swfupload1.uploadStopped = false;
	/*
	//近八年
	var setting1 =  jQuery.extend(defaultSettings, {
		button_placeholder_id :"manageDifficultyAgo",
		file_dialog_complete_handler:selectFileComplete1
		})
	swfupload1 = new SWFUpload(setting1);
	wfupload1.uploadStopped = false;
	
	var setting2 =  jQuery.extend(defaultSettings, {
		button_placeholder_id :"manageDifficulty",
		file_dialog_complete_handler:selectFileComplete2
	})
	swfupload2 = new SWFUpload(setting2);*/
	//swfupload2.uploadStopped = false;
}


var headAttachmentId  ;
function uploadmediaSuccess(file,serverData) {
  //  var xmlDoc = serverData.documentElement;
    //var responseObject = xmlDoc.getElementsByTagName("Response")[0];
	var jsonData=eval("("+serverData+")");//转换为json对象 
	var attachmentId=jsonData.attachmentId;
	headAttachmentId = attachmentId;
	//var fileName=jsonData.fileName;
	var fileSize=jsonData.fileSize;
	var path=jsonData.path;
	var fileName = file.name;
	if(fileName && fileName.length > 12) fileName = fileName.substring(0,12)+'...';
	
	$("#personHeadImg").attr("src",path);
	$("#person_img_attachId").val(attachmentId);
}

function deleteReceiveFileAttachment(attachmentId,tdObject,hiddenId){
	$("#"+hiddenId).val("");
	var bcReq = new BcRequest('workflow','receiveFileAction','deleteReceiveFileAttachment');
	bcReq.setExtraPs({"attachmentId":attachmentId});
	bcReq.setSuccFn(function(data,status){
		 $(tdObject).parent().empty();
	});
	bcReq.postData();
}

//近八年选择文件后
function selectFileComplete1(numFilesSelected, numFilesQueued) {
	try {
		$("#fsUploadProgress").show();
		//document.getElementById("ScrollContent").scrollTop=document.getElementById("ScrollContent").scrollHeight;
		//$("#submit_flag").val("Y");
		swfupload1.addPostParam("selectDivId","manageDifficultyAgo");
		swfupload1.startUpload(); //选择文件后立刻上传
	} catch (ex)  {		
        swfupload.debug(ex);
	}    
}

//选择文件后

function selectFileComplete2(numFilesSelected, numFilesQueued) {
	try {
		$("#fsUploadProgress").show();
		//document.getElementById("ScrollContent").scrollTop=document.getElementById("ScrollContent").scrollHeight;
		//$("#submit_flag").val("Y");
		swfupload2.addPostParam("selectDivId","manageDifficulty");
		swfupload2.startUpload(); //选择文件后立刻上传
	} catch (ex)  {		
        swfupload.debug(ex);
	}    
}

//全部附件都上传完成后执行的函数
function allUploadComplete(data){
	 //$("#submit_flag").val("N");
	// $("#comment").val("");
	 $("#fsUploadProgress").slideUp("slow");
	 $("#fsUploadProgress").empty();
     //toastr.success("附件上传成功！");
	//commentForm.reset();
}



function startUploadFile2(){
	swfupload2.addPostParam("row_number","3");
	swfupload2.addPostParam("file_upload_type","4");
	swfupload2.startUpload();
}


function saveUpdateRefillData(){
	var isRefillTask = $("#isRefillTask").val();
	if(isRefillTask){
		var submitStrings =  getSubmitStrings();
		var bcReq = new BcRequest('headmaster','masterReviewAction','saveUpdateRefillData');
		bcReq.setExtraPs({
			    "option_tab_type":'baseinfo',
			    "option_tab_values":submitStrings,
			    "businessKey":processBusinessKey
		});
		bcReq.setSuccFn(function(data,status){
			changeOption(2);
		});
		bcReq.postData();
	}else{
		submitRefillTask();
	}
}

function getSubmitStrings(){
	debugger
	var submitArray = [];
	var baseinfoObject = {
			'headerMasterId' : headerMasterId,
			'headerMasterName' : headerMasterName,
			'usersex': $("#usersex").val(),
			'school_name':$("#school_name").val(),
			'identitycard':$("#identitycard").val(),
			'isPositive':$("#ispositive").val(),
			'email':$("#email").val(),
			'mobile':$("#mobile").val(),
			'address':$("#address").val(),
			'phasestudy':$("#phasestudy").val(),
			'school_class':$("#school_class").val(),
			'join_work_time':$("#join_work_time").val(),
			'join_educate_work_time':$("#join_educate_work_time").val(),
			'politics_status':$("#politics_status").val(),
			'teach_age':$("#teach_age").val(),
			'census_register':$("#census_register").val(),
			'nation':$("#nation").val(),
			'native_place':$("#native_place").val(),
			'presentOccupation':$("#present_occupation").val(),
			'present_major_occupation':$("#present_major_occupation").val(),
			'person_img_attachId':$("#person_img_attachId").val(),
			"businessKey":$("#id").val(),
			"id":$("#id").val()
	}
	submitArray.push(baseinfoObject);
	return JSON.stringify(submitArray);
}

function headmasterBeforeSubmit(formJsonData){
	formJsonData.option_tab_type = "baseinfo";
	formJsonData.option_tab_values = getSubmitStrings();
	//formJsonData.school_name_space =  $("#ispositive").val();
//	formJsonData.school_name_space_ago =  $("#ispositive").val();
	
	formJsonData.usersex = $("#usersex").val();
	formJsonData.address = $("#address").val();
	formJsonData.ispositive = $("#ispositive").val();
	formJsonData.email = $("#email").val();
	formJsonData.school_class = $("#school_class").val();
	formJsonData.phasestudy = $("#phasestudy").val();
	formJsonData.present_major_occupation = $("#present_major_occupation").val();
	formJsonData.join_work_time = $("#join_work_time").val();
	formJsonData.join_educate_work_time = $("#join_educate_work_time").val();
	formJsonData.politics_status = $("#politics_status").val();
	formJsonData.teach_age = $("#teach_age").val();
	formJsonData.native_place = $("#native_place").val();
	formJsonData.census_register = $("#census_register").val();
	formJsonData.nation = $("#nation").val();
	formJsonData.person_img_attachId = $("#person_img_attachId").val();
}

function countBirthDate(identitycardObj){
	var birthDateVal = Headmaster.discriCard(identitycardObj.value);
	$("#birth_date").text(birthDateVal)
}
</script>

</head>
<body>
<input type="hidden" id="person_img_attachId" name="person_img_attachId" value="">
<!-- 进度 s -->
	<div class="grogress"><div  class="line" style="width:46px;"><!-- 970/21 --></div></div>
	<!-- 进度 e -->
	<!-- 标题 s -->
	<div class="com-title">
		<div class="txt fl">
			<h2><i>1</i>基础信息</h2>
		</div>
		<div id="base_change_div" class="select-step fr"><a href="javascript:void(0);" target="_self" title="" id="change">+&nbsp;切换步骤</a></div>
		<div class="clear-both"></div>
	</div>
	<!-- 标题 e -->
	<!-- 基础信息填写 s -->
	<div class="base-info">
		<div class="photo">
			<p style="height:110px;width:90px;"><img id="personHeadImg"  alt="" title="" /></p>
			<p><div ><span  style='color:#FFF;' id="personImg"></span></div></p>
			<!--  <span id="personImg"></span>-->
		</div>
		<ul class="clear-fix">
			<li>
				<div class="border_1 w_1 fl">
					<span>姓名：</span>
					<input type="text" id="headerMasterName" name="headerMasterName" value="" placeholder="请输入姓名" />
				</div>
				<div class="border_1 w_2 fr" style="z-index:99;">
					<span>性别：</span>
					<select name="usersex" id="usersex"><option value="">请选择</option></select>
				</div>
			</li>
			<li>
				<div class="border_1 w_3 fl" style="z-index:98;">
					<span>学校类型：</span>
				    <select name="school_class" id="school_class"><option value="">请选择</option></select>
				</div>
			</li>
			<li>
				<div class="border_1 w_4 fl" style="z-index:97;">
					<span>出生年月：</span>
				    <label id="birth_date" name="birth_date"></label>
				</div>
			</li>
			<li>
				<div class="border_1 w_5 fl" style="z-index:96;">
					<span>参加工作时间：</span>
					<input type="text" id="join_work_time" name="join_work_time" onclick='selectDeleteTime()'  placeholder="年-月-日"  />
				</div>
			</li>
			<li>
				<div class="border_1 w_6 fl" style="z-index:95;">
					<span>身份证号码：</span>
					<input type="text" id="identitycard" name="identitycard" value="" placeholder="请输入18位身份证号码" onblur="countBirthDate(this)" />
				</div>
			</li>
			<li>
				<div class="border_1 w_7 fl" style="z-index:94;">
					<span>参加教育工作时间：</span>
					<input type="text" id="join_educate_work_time" name="join_educate_work_time" onclick='selectDeleteTime()' placeholder="年-月-日" />
				</div>
			</li>
			<li>
				<div class="border_1 w_3 fl" style="z-index:85;">
					<span>手机号码：</span>
					<input type="text" id="mobile" name="mobile" value="" placeholder="请输入手机号码" />
				</div>
			</li>
			<li>
				<div class="border_1 w_3 fl" style="z-index:93;">
					<span>政治面貌：</span>
				     <select name="politics_status" id="politics_status"><option value="">请选择</option></select>
				</div>
			</li>
			<li>
				<div class="border_1 w_9 fl" style="z-index:91;">
					<span>籍贯：</span>
					<input type="text" id="native_place" name="native_place" value="" placeholder="请输入省份地区" />
				</div>
				<div class="border_1 w_9 fr" style="z-index:90;">
					<span>户籍：</span>
				    <select name="census_register" id="census_register"><option value="">请选择</option></select>
				</div>
			</li>
			<li>
				<div class="border_1 w_8 fl" style="z-index:92;">
					<span>教龄：</span>
					<input type="number" id="teach_age" name="teach_age" value="" />
				</div>
			</li>
			<li>
				<div class="border_1 w_10 fl" style="z-index:88;">
					<span>邮箱：</span>
					<input type="text" id="email" name="email" value="" placeholder="邮箱" />
				</div>
				<div class="border_1 w_11 fr" style="z-index:87;">
					<span>民族：</span>
					<input type="text" id="nation" name="nation" value="" placeholder="民族" />
				</div>
			</li>
			<li>
				<div class="border_1 w_3 fl" style="z-index:89;">
					<span>学段：</span>
					 <select name="phasestudy" id="phasestudy"><option value="">请选择</option></select>
				</div>
			</li>
		
			<li>
				<div class="border_1 w_3 fl" style="z-index:86;">
					<span>现任职务：</span>
					<select name="ispositive" id="ispositive"><option value="">请选择</option></select>
				</div>
			</li>
			<li>
				<div class="border_1 w_3 fl" style="z-index:85;">
					<span>所在学校：</span>
					<input type="text" id="school_name" name="school_name" value="" placeholder="请输入学校全称" />
				</div>
			</li>
			<li>
				<div class="border_1 w_7 fl" style="z-index:84;">
					<span>现任专业技术职务：</span>
					<input type="text" id="present_major_occupation" name="present_major_occupation" value="" placeholder="请输入现任专业技术职务" />
				</div>
			</li>
		</ul>
	</div>
	<!-- 基础信息填写 e -->
	   <div class="next-step" style="text-align:center;">
	   <a id="base_next_btn" href="javascript:void(0);" target="_self" onclick="saveUpdateRefillData()" title="">下一步</a></div>
</body>
</html>