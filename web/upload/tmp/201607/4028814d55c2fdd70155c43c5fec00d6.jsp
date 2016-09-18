<%@ page contentType="text/html; charset=GBK" %>
<%
  String userid = (String)session.getAttribute("userid");
  String username =(String)session.getAttribute("username");
  String usertype =(String)session.getAttribute("usertype");
 // String usertype =(String)session.getAttribute("usertype");
  String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
  System.out.println("basePathbasePathbasePath==="+basePath);
  
%>

<style type="text/css">
	.chachu {
		outline: none;
	    color: #0000FF;!important
		text-decoration: none;	
	}
	a:hover{
		color: #ff7200;
		text-decoration: underline;
	}
	.rightnav .current a {
			color:#ff7200;
	}
</style> 



<!-- <script type="text/javascript" src="js/jquery-1.11.3.min.js"></script> -->




<script type="text/javascript" src="/masterreview/headmaster.js"></script>
<script type="text/javascript" src="/masterreview/js/bootstrap-tab.js"></script>
<script type="text/javascript" src="/masterreview/js/tip.js"></script>
<script type="text/javascript" src="/js/swfupload.js"></script>
<script type="text/javascript" src="/js/upload/fileprogress.js"></script>
<script type="text/javascript" src="/js/upload/handlers.js"></script>
<script type="text/javascript" src="/js/jquery/jquery.form.js"></script>
<script type="text/javascript" src="/workflow/js/jquery.idTabs.min.js"></script>
<script src="/workflow/common/jspParamUtil.js"></script>
<script src="/platform/public/sysparam.js"></script>
<script type="text/javascript" src="/workflow/js/DatePicker/WdatePicker.js"></script>
<script type="text/javascript">
$(function(){
	$('select').selectOrDie({
		placeholder: '请选择'
	});
});
</script>
<script>
$(function(){
	$('#change').click(function(){
		$('.change-channel').layerModel({
			'head' : false,
			isClose : false
		});
	});
	
	$('#close').click(function(){
		$('.change-channel').close();
	});
});
</script>
<script>
var headerMasterId = '<%=userid%>';
var headerMasterName = '<%=username%>';

var isSaveDraft = '1';
function beforeSubmit(formJsonData){
	var mobile = $("#mobile").val();
	
	formJsonData.id = $("#id").val();
	formJsonData.headerMasterId = headerMasterId;
	formJsonData.headerMasterName = headerMasterName;
	formJsonData.mobile = mobile;
	formJsonData.identitycard = $("#identitycard").val();
	formJsonData.ispositive = $("#ispositive").val();
	
	formJsonData.school_id = $("#school_id").val();
	formJsonData.school_name = $("#school_name").val();
	formJsonData.apply_level = $("#apply_level").val();
	
	formJsonData.present_occupation = $("#present_occupation").val();
	formJsonData.schoolType = $("#schoolType").val();
	formJsonData.schoolCount = $("#schoolCount").val();
	formJsonData.studentNumber = $("#studentNumber").val();
	formJsonData.manage_difficulty_attachment_id = $("#manageDifficultyAttachId2").val();
	
	formJsonData.schoolTypeAgo = $("#schoolTypeAgo").val();
	formJsonData.schoolCountAgo = $("#schoolCountAgo").val();
	formJsonData.studentNumberAgo = $("#studentNumberAgo").val();
	formJsonData.manage_difficulty_ago_attachment_id = $("#manageDifficultyAgoAttachId1").val();
	
	formJsonData.work_report = $("#work_report").val();
	formJsonData.isSaveDraft = isSaveDraft;
	//var attachments = attachmentIdsArray.length==0 ? "" : attachmentIdsArray.join(",");
	//formJsonData.attachments = attachments;
	
	var variableMap = new Brightcom.workflow.HashMap(); 
	variableMap.put("areaHeader",'4028814d5499edd2015499f003ca0006');
	formJsonData.processParam =variableMap.toJsonObject();
}

function afterSubmit(returnData){
	 var result = returnData.Data[0];
	 if(isSaveDraft == '1'){
		 window.location.href =  "/workflow/template/processViewForm.jsp?processInstanceId="+result.processInstanceId;
	 }else{
		 window.location.href ="/masterreview/headmaster/headmasterFoward.jsp?processInstanceId="+result.processInstanceId;
	 }
}

$(document).ready(function(){	
	Brightcom.workflow.beforeSubmit = beforeSubmit;
	Brightcom.workflow.afterSubmit = afterSubmit;
	Headmaster.findMasterBaseInfo();
	getMasterReviewBusinessKey();
	//initSwfuploadContainer();
	initTabComponent();
	Headmaster.initSelectCom();
	//alert(Headmaster.getBasePath())
	
	/*
	 * 1、professionalTitlespan:上传按钮显示位置id
	 4、buttonName ： 按钮名称
	 5、hiddenAttachId：隐藏附件id，选中后把附件值保存在该隐藏域里
	 6、 hiddenDisplayId:附件显示的div id
	 */
	initWebUploader('manageDifficultyAgo',1,'manageDifficulty','点击上传','manageDifficultyAgoAttachId','manageDifficultyAgoDiv');
	initWebUploader('manageDifficulty',2,'manageDifficulty','点击上传','manageDifficultyAttachId','manageDifficultyDiv');
	
	//$("#eeeeeeeeee").customtip();
});	




function initTabComponent(){
   $('#myTab a').click(function (e) {
      e.preventDefault();
      $(this).tab('show');
	  if( this.href.indexOf('#two') > -1){
			$('.renzhi h4').each(function(i, k){
				var t = this, $t = $(t);
				$('.rightnav li:eq('+ i +')').attr('data-top', $t.offset().top);
			})
	  }
   })
		// 展开收起
		$('.renzhi .tit2-trigger').click(function(){
			var t = this, $t = $(t);
			if( $t.html() == '展开' ){
				$t.html('收起');
				$t.parents('.tit2').next('.tab-pane-sub').show();
			}else{
				$t.html('展开');
				$t.parents('.tit2').next('.tab-pane-sub').hide();
			}
		})
		// 展开收起 end
		
		// 右侧导航
		/*
		$(window).scroll(function(){
			var top = $(window).scrollTop();
			$('.rightnav li').each(function(){
				var t = this, $t = $(t);
				if( top > $t.attr('data-top') - 200 ){
					$t.addClass('current').siblings().removeClass('current');
				}
			})
		})*/
		$('.rightnav li:not(.backtop)').click(function(){
			var t = this, $t = $(t);
			$t.addClass('current').siblings().removeClass('current');
		})
		// 右侧导航 end
}

function toggleDiv(t){
	$t = $(t);
	if( $t.html() == '展开' ){
		$t.html('收起');
		$t.parents('.tit2').next('.tab-pane-sub').show();
	}else{
		$t.html('展开');
		$t.parents('.tit2').next('.tab-pane-sub').hide();
	}
}

function addEducationSingle(obj){
	var educationRowNum = parseInt($("#educationRowNum").val());
	var educationRowNumNext = parseInt(educationRowNum+1);

	var educationArray =[];
	educationArray.push("<div id='educationHead"+educationRowNumNext+"' class='tit2'><span><a href='javascript:void(0);' class='tit2-trigger' onclick=\"toggleDiv(this);\">收起</a>");
	educationArray.push("<a href='javascript:void(0);' onclick=\"deleteSingleOption(this,'educationType',\'"+educationRowNumNext+"\','');\" >删除</a></span></div>");
	educationArray.push("<div class='tab-pane-sub'>");
	educationArray.push("<input type='hidden' id='educationId"+educationRowNumNext+"' name='educationId' value=''>");
	educationArray.push("<input type='hidden' id='educationAttachId"+educationRowNumNext+"' value=''>");
	educationArray.push("<input type='hidden' id='degreeAttachId"+educationRowNumNext+"'  value=''>");
	educationArray.push("<ul>");
	educationArray.push("<li><p>起止年月：</p>");
	educationArray.push("<input type='text' id='startTimeEducation"+educationRowNumNext+"' name='startTimeEducation"+educationRowNumNext+"' onclick='selectDeleteTime()' style='width:100px;'/>");
	educationArray.push("<span>—</span>");
	educationArray.push("<input type='text' id='endTimeEducation"+educationRowNumNext+"' name='endTimeEducation"+educationRowNumNext+"' onclick='selectDeleteTime()' style='width:100px;'/>");
	educationArray.push("</li>");
	educationArray.push("<li><p>就读院校：</p><input type='text' id='study_schoolEducation"+educationRowNumNext+"' value='' placeholder=''></li>");
	educationArray.push("<li><p>就读专业：</p><input type='text' id='study_professionEducation"+educationRowNumNext+"' value='' placeholder=''></li>");
	
	educationArray.push("<li style='height:45px;'><p>学历：</p><select style='float:left;' id='education"+educationRowNumNext+"'></select> ");
	educationArray.push("<div style='margin-top:-3px;'  id='educationButton"+educationRowNumNext+"'></div> </li>");
	
//	educationArray.push("<li style='height:45px;'><p>学历证明材料：</p><div style='padding-left:65px;' id='educationButton"+educationRowNumNext+"'></div> </li>");
	educationArray.push("<div id='educationButtonDiv"+educationRowNumNext+"' style='heigth:0px'></div>");
	
	educationArray.push("<li><p>学位：</p><select style='float:left;' id='degree"+educationRowNumNext+"'><option value='1'>本科</option></select>");
	educationArray.push("<div style='margin-top:-3px;'  id='degreeButton"+educationRowNumNext+"'></div> </li>");
	
	//educationArray.push("<li style='height:45px;'><p>学位证明材料：</p><div style='padding-left:65px;' id='degreeButton"+educationRowNumNext+"'></div> </li>");
	educationArray.push("<div id='degreeButtonDiv"+educationRowNumNext+"' style='heigth:0px'></div>");
	
	educationArray.push("<li class='btn'><input type='button' value='保 存' onclick=\"saveSingleOption('educationType',\'"+educationRowNumNext+"\','');\" ></li>");
	educationArray.push("</ul>");
	educationArray.push("</div>");
	
	educationArray.push("	<a class='add' href='javascript:void(0);' onclick='addEducationSingle(this)' >+</a>");
	
	$(obj).after(educationArray.join(""));
	$(obj).remove();
	
	$("#educationRowNum").val(educationRowNumNext);
	Brightcom.workflow.initSelectCombox('headmaster_education','education'+(educationRowNumNext));
	
	/*
	 * 1、professionalTitlespan:上传按钮显示位置id
	 4、buttonName ： 按钮名称
	 5、hiddenAttachId：隐藏附件id，选中后把附件值保存在该隐藏域里
	 6、 hiddenDisplayId:附件显示的div id
	 */
	initWebUploader('educationButton',educationRowNumNext,'educationType','上传学历证明','educationAttachId','educationButtonDiv');
	
	/*
	 * 1、professionalTitlespan:上传按钮显示位置id
	 4、buttonName ： 按钮名称
	 5、hiddenAttachId：隐藏附件id，选中后把附件值保存在该隐藏域里
	 6、 hiddenDisplayId:附件显示的div id
	 */
	initWebUploader('degreeButton',educationRowNumNext,'educationType','上传学位证明','degreeAttachId','degreeButtonDiv');
	
	$('#educationHead'+educationRowNumNext).tip({
	      maxwidth: 400,   //最大宽度
	      opacity:0.8,     //透明度
	      bg: "#f00",      //背景样式
	      border: "#FEFFD4 solid 1px",//边框样式
	      color: "#FFFFFE", //正文色
	      font: "12px verdana,arial,sans-serif"//字体样式
	  });
}


function addWorkExperienceSingle(obj){
	var workExperienceRowNum = parseInt($("#workExperienceRowNum").val());
	var workExperienceRowNumNext = parseInt(workExperienceRowNum+1);

	var educationArray =[];
	educationArray.push("<div id='workExperienceHead"+workExperienceRowNumNext+"' class='tit2'><span><a href='javascript:void(0);' class='tit2-trigger' onclick=\"toggleDiv(this);\">收起</a>");
	educationArray.push("<a href='javascript:void(0);' onclick=\"deleteSingleOption(this,'workExperienceType',\'"+workExperienceRowNumNext+"\','');\" >删除</a></span></div>");
	educationArray.push("<div class='tab-pane-sub'>");
	educationArray.push("<input type='hidden' id='workExperienceId"+workExperienceRowNumNext+"'  value=''>");
	educationArray.push("<input type='hidden' id='proveId"+workExperienceRowNumNext+"' value=''>");
	educationArray.push("<ul>");
	educationArray.push("<li><p>起止年月：</p>");
	educationArray.push("<input type='text' id='startTimeExperience"+workExperienceRowNumNext+"' name='startTimeExperience"+workExperienceRowNumNext+"' onclick='selectDeleteTime()' style='width:100px;'/>");
	educationArray.push("<span>—</span>");
	educationArray.push("<input type='text' id='endTimeExperience"+workExperienceRowNumNext+"' name='endTimeEducation"+workExperienceRowNumNext+"' onclick='selectDeleteTime()' onblur=\"countWorkExperience(this,\'"+workExperienceRowNumNext+"\')\" style='width:100px;'/>");
	educationArray.push("</li>");
	educationArray.push("<li><p>所在学校：</p><input type='text' id='workSchoolExperience"+workExperienceRowNumNext+"' value='' placeholder=''></li>");
	educationArray.push("<li><p>担任职务：</p><input type='text' id='workProfession"+workExperienceRowNumNext+"' value='' placeholder=''></li>");
	educationArray.push("<li><p>任职年限：</p><select name='' id='workYear"+workExperienceRowNumNext+"'><option value='2016' >2016</option></select></li>");
	educationArray.push("<li style='height:45px;'><p>证明材料：</p><div style='padding-left:65px;' id='spanButtonPlaceHolder"+workExperienceRowNumNext+"'></div> </li>");
	educationArray.push("<div id='workExperienceTypeDiv"+workExperienceRowNumNext+"' style='heigth:0px'></div>");
	educationArray.push("<li class='btn'><input type='button' value='保 存' onclick=\"saveSingleOption('workExperienceType',\'"+workExperienceRowNumNext+"\','');\" ></li>");
	educationArray.push("</ul>");
	educationArray.push("</div>");
	
	educationArray.push("<a class='add' href='javascript:void(0);' onclick='addWorkExperienceSingle(this)' >+</a>");
	
	$(obj).after(educationArray.join(""));
	$(obj).remove();
	
	$("#workExperienceRowNum").val(workExperienceRowNumNext);
	
	initWebUploader('spanButtonPlaceHolder',workExperienceRowNumNext,'workExperienceType','点击上传','proveId','workExperienceTypeDiv');
	
	/*
	var swfuploadWorkExperience = 'swfupload'+workExperienceRowNumNext;
	var gg= {"rownum":workExperienceRowNumNext}

	var setting1 =  jQuery.extend(defaultSettings, {
			   button_placeholder_id :"spanButtonPlaceHolder"+workExperienceRowNumNext,
			   file_dialog_complete_handler : new function()  
			    {  
				       this.temp = workExperienceRowNumNext;  
						try {
							$("#fsUploadProgress").show();
							//document.getElementById("ScrollContent").scrollTop=document.getElementById("ScrollContent").scrollHeight;
							//$("#submit_flag").val("Y");
							var dd = 'swfupload'+this.temp;
							//swfuploadWorkExperience.addPostParam("row_number",gg.rownum);
							dd.addPostParam("file_upload_type",'workExperience');
							dd.startUpload(); //选择文件后立刻上传
						} catch (ex)  {		
							//dd.debug(ex);
						}
				}  
	*/
	
	/*
	function(){  
				  //  this.row_number = workExperienceRowNumNext;  
					try {
						$("#fsUploadProgress").show();
						//document.getElementById("ScrollContent").scrollTop=document.getElementById("ScrollContent").scrollHeight;
						//$("#submit_flag").val("Y");
						var dd = swfuploadWorkExperience;
						swfuploadWorkExperience.addPostParam("row_number",gg.rownum);
						swfuploadWorkExperience.addPostParam("file_upload_type",'workExperience');
						swfuploadWorkExperience.startUpload(); //选择文件后立刻上传
					} catch (ex)  {		
						swfuploadWorkExperience.debug(ex);
					}
			   }
	 })*/
	//setting1.rowNum = workExperienceRowNumNext;
//	swfuploadWorkExperience = new SWFUpload(setting1);
	//swfuploadWorkExperience.addPostParam("row_number",workExperienceRowNumNext);
}

function countWorkExperience(obj,rownum){
	//var startDate =  $('#startTimeExperience'+rownum).val();
	//var endDate =  $(obj).val();
	//BcUtil.get_dateDiffFormat(startDate,endDate)
}

/*
 * uploadSingleId:上传按钮显示位置id
 buttonName ： 按钮名称
 hiddenAttachId：隐藏附件id，选中后把附件值保存在该隐藏域里
 hiddenDisplayId:附件显示的div id
 */
function initWebUploader(uploadSingleId,rownum,file_upload_type,buttonName,hiddenAttachId,hiddenDisplayId) {
    $("#"+uploadSingleId+rownum).append("<iframe name ='ifm"+uploadSingleId+rownum+"' id ='ifm"+uploadSingleId+rownum+"' src='' width='200px;' height='50' scrolling='no' frameborder='0'></iframe>"); 
    $("#ifm"+uploadSingleId+rownum).attr("src", "/masterreview/public/webuploader.jsp?t="+Math.random()+"&rownum="+rownum+"&file_upload_type="+file_upload_type+"&hiddenAttachId="+hiddenAttachId+"&hiddenDisplayId="+hiddenDisplayId+"&uploadSingleId="+uploadSingleId+"&buttonName="+encodeURI(buttonName));
    $("#ifm"+uploadSingleId+rownum).load(function() {
	   	var config = {
		   		pickTitle: buttonName,
		   		//queuedTitle: "等待生成试卷",
				accept: {
		      	  title: 'MP3'
		        //extensions: 'mp3',
				//mimeTypes: 'audio/mpeg'
		      },
		      fileNumLimit:1,
		      //threads:1,
		      fileSingleSizeLimit: 50 * 1024 * 1024
		 };
     	$("#ifm"+uploadSingleId+rownum)[0].contentWindow.init(config);
	});
	file_num = 0;
}

/*
function selectWorkExperienceComplete(){
	try {
		$("#fsUploadProgress").show();
		//document.getElementById("ScrollContent").scrollTop=document.getElementById("ScrollContent").scrollHeight;
		//$("#submit_flag").val("Y");
		//swfupload1.addPostParam("row_number",row_number);
		swfupload1.addPostParam("file_upload_type",'workExperience');
		swfupload1.startUpload(); //选择文件后立刻上传
	} catch (ex)  {		
        swfupload.debug(ex);
	}    
}*/
/*
function startUploadFileWorkExperience(row_number,file_upload_type){
	swfupload1.addPostParam("row_number",row_number);
	swfupload1.addPostParam("file_upload_type",file_upload_type);
    swfupload1.startUpload();
}*/

function addProfessionalTitleSingle(obj){
	var professionalTitleRowNum = parseInt($("#professionalTitleRowNum").val());
	var professionalTitleRowNumNext = parseInt(professionalTitleRowNum+1);

	var educationArray =[];
	educationArray.push("<div id='professionalTitleHead"+professionalTitleRowNumNext+"' class='tit2'><span><a href='javascript:void(0);' class='tit2-trigger' onclick=\"toggleDiv(this);\">收起</a>");
	educationArray.push("<a href='javascript:void(0);' onclick=\"deleteSingleOption(this,'professionalTitleType',\'"+professionalTitleRowNumNext+"\','');\" >删除</a></span></div>");
	educationArray.push("<div class='tab-pane-sub'>");
	educationArray.push("<input type='hidden' id='professionalTitleId"+professionalTitleRowNumNext+"'  value=''>");
	educationArray.push("<input type='hidden' id='professionalAttachId"+professionalTitleRowNumNext+"'  value=''>");
	educationArray.push("<ul>");
	educationArray.push("<li><p>职称名称：</p><input type='text' id='professionaltitle_name"+professionalTitleRowNumNext+"' value='' placeholder=''></li>");
	educationArray.push("<li><p>获得时间：</p><input type='text' id='obtain_time"+professionalTitleRowNumNext+"' name='obtain_time"+professionalTitleRowNumNext+"' onclick='selectDeleteTime()' style='width:100px;'/></li>");
	educationArray.push("<li><p>获得学校：</p><input type='text' id='obtain_school"+professionalTitleRowNumNext+"' value='' placeholder=''></li>");
	
	educationArray.push("<li style='height:45px;'><p>证明材料：</p><div style='padding-left:65px;' id='professionalTitlespan"+professionalTitleRowNumNext+"'></div> </li>");
	educationArray.push("<div id='professionalTitleDiv"+professionalTitleRowNumNext+"' style='heigth:0px'></div>");
	
	educationArray.push("<li class='btn'><input type='button' value='保 存' onclick=\"saveSingleOption('professionalTitleType',\'"+professionalTitleRowNumNext+"\','');\" ></li>");
	educationArray.push("</ul>");
	educationArray.push("</div>");
	
	educationArray.push("<a class='add' href='javascript:void(0);' onclick='addProfessionalTitleSingle(this)' >+</a>");
	
	$(obj).after(educationArray.join(""));
	$(obj).remove();
	
	$("#professionalTitleRowNum").val(professionalTitleRowNumNext);
	
	/*
	 * 1、professionalTitlespan:上传按钮显示位置id
	 4、buttonName ： 按钮名称
	 5、hiddenAttachId：隐藏附件id，选中后把附件值保存在该隐藏域里
	 6、 hiddenDisplayId:附件显示的div id
	 */
	initWebUploader('professionalTitlespan',professionalTitleRowNumNext,'professionalTitleType','点击上传','professionalAttachId','professionalTitleDiv');
}

function addPaperSingle(obj){
	var paperTitleRowNum = parseInt($("#paperTitleRowNum").val());
	var paperTitleRowNumNext = parseInt(paperTitleRowNum+1);

	var educationArray =[];
	educationArray.push("<div id='paperHead"+paperTitleRowNumNext+"' class='tit2'><span><a href='javascript:void(0);' class='tit2-trigger' onclick=\"toggleDiv(this);\">收起</a>");
	educationArray.push("<a href='javascript:void(0);' onclick=\"deleteSingleOption(this,'paperType',\'"+paperTitleRowNumNext+"\','');\" >删除</a></span></div>");
	educationArray.push("<div class='tab-pane-sub'>");
	educationArray.push("<input type='hidden' id='paperId"+paperTitleRowNumNext+"'  value=''>");
	educationArray.push("<input type='hidden' id='paperAttachId"+paperTitleRowNumNext+"'  value=''>");
	educationArray.push("<ul>");
	educationArray.push("<li><p style='width : 126px;'>题目：</p><input type='text' id='title"+paperTitleRowNumNext+"' value='' placeholder=''></li>");
	educationArray.push("<li><p style='width : 126px;'>杂志宣读学术名称：</p><input type='text' id='magazine_meet_name"+paperTitleRowNumNext+"' name='magazine_meet_name"+paperTitleRowNumNext+"'  style='width:100px;'/></li>");
	educationArray.push("<li><p style='width : 126px;'>宣读论文会议名称：</p><input type='text' id='paper_meet_name"+paperTitleRowNumNext+"' value='' placeholder=''></li>");
	educationArray.push("<li><p style='width : 126px;'>刊号：</p><input type='text' id='paper_number"+paperTitleRowNumNext+"' value='' placeholder=''></li>");
	educationArray.push("<li><p style='width : 126px;'>发表及宣读时间：</p><input type='text' id='publish_time_paper"+paperTitleRowNumNext+"' value='' placeholder='' onclick='selectDeleteTime()'></li>");
	educationArray.push("<li><p style='width : 126px;'>主办单位：</p><input type='text' id='organizers"+paperTitleRowNumNext+"' value='' placeholder=''></li>");
	educationArray.push("<li><p style='width : 126px;'>主办单位级别：</p><input type='text' id='organizers_level"+paperTitleRowNumNext+"' value='' placeholder=''></li>");
	educationArray.push("<li><p style='width : 126px;'>本人承担部分：</p><input type='text' id='personal_part"+paperTitleRowNumNext+"' value='' placeholder=''></li>");
	
	educationArray.push("<li style='height:45px;'><p style='width : 126px;'>论文扫描件：</p><div style='padding-left:65px;' id='paperbutton"+paperTitleRowNumNext+"'></div></li>");
	educationArray.push("<div id='paperbuttonDiv"+paperTitleRowNumNext+"' style='heigth:0px'></div>");
	
	educationArray.push("<li class='btn'><input type='button' value='保 存' onclick=\"saveSingleOption('paperType',\'"+paperTitleRowNumNext+"\','');\" ></li>");
	educationArray.push("</ul>");
	educationArray.push("</div>");
	
	educationArray.push("<a class='add' href='javascript:void(0);' onclick='addPaperSingle(this)' >+</a>");
	
	$(obj).after(educationArray.join(""));
	$(obj).remove();
	
	$("#paperTitleRowNum").val(paperTitleRowNumNext);
	
	/*
	 * 1、professionalTitlespan:上传按钮显示位置id
	 4、buttonName ： 按钮名称
	 5、hiddenAttachId：隐藏附件id，选中后把附件值保存在该隐藏域里
	 6、 hiddenDisplayId:附件显示的div id
	 */
	initWebUploader('paperbutton',paperTitleRowNumNext,'paperType','点击上传','paperAttachId','paperbuttonDiv');
}


function addWorkPublishSingle(obj){
	var workPublishRowNum = parseInt($("#workPublishRowNum").val());
	var workPublishRowNumNext = parseInt(workPublishRowNum+1);

	var educationArray =[];
	educationArray.push("<div id='workPublishHead"+workPublishRowNumNext+"' class='tit2'><span><a href='javascript:void(0);' class='tit2-trigger' onclick=\"toggleDiv(this);\">收起</a>");
	educationArray.push("<a href='javascript:void(0);' onclick=\"deleteSingleOption(this,'workPublishType',\'"+workPublishRowNumNext+"\','');\" >删除</a></span></div>");
	educationArray.push("<div class='tab-pane-sub'>");
	educationArray.push("<input type='hidden' id='workPublishId"+workPublishRowNumNext+"'  value=''>");
	educationArray.push("<ul>");
	educationArray.push("<li><p style='width : 100px;'>书名：</p><input type='text' id='book_name"+workPublishRowNumNext+"' value='' placeholder=''></li>");
	educationArray.push("<li><p style='width : 100px;'>完成方式：</p><select  id='complete_way"+workPublishRowNumNext+"'><option value='2' >2016</option></select></li>");
	educationArray.push("<li><p style='width : 100px;'>出版时间：</p><input type='text' id='publish_time"+workPublishRowNumNext+"' value='' placeholder='' onclick='selectDeleteTime()'></li>");
	educationArray.push("<li><p style='width : 100px;'>本人完成字数：</p><input type='text' id='complete_word"+workPublishRowNumNext+"' value='' placeholder=''></li>");
	educationArray.push("<li><p style='width : 100px;'>本人承担部分：</p><input type='text' id='complete_chapter"+workPublishRowNumNext+"' value='' placeholder='' ></li>");
	educationArray.push("<li><p style='width : 100px;'>作者排序：</p><input type='text' id='author_order"+workPublishRowNumNext+"' value='' placeholder=''></li>");
	educationArray.push("<li class='btn'><input type='button' value='保 存' onclick=\"saveSingleOption('workPublishType',\'"+workPublishRowNumNext+"\','');\" ></li>");
	educationArray.push("</ul>");
	educationArray.push("</div>");
	
	educationArray.push("	<a class='add' href='javascript:void(0);' onclick='addWorkPublishSingle(this)' >+</a>");
	
	$(obj).after(educationArray.join(""));
	$(obj).remove();
	
	$("#workPublishRowNum").val(workPublishRowNumNext);
}


function addSubjectSingle(obj){
	var subjectRowNum = parseInt($("#subjectRowNum").val());
	var subjectRowNumNext = parseInt(subjectRowNum+1);

	var educationArray =[];
	educationArray.push("<div id='subjectHead"+subjectRowNumNext+"' class='tit2'><span><a href='javascript:void(0);' class='tit2-trigger' onclick=\"toggleDiv(this);\">收起</a>");
	educationArray.push("<a href='javascript:void(0);' onclick=\"deleteSingleOption(this,'subjectType',\'"+subjectRowNumNext+"\','');\" >删除</a></span></div>");
	educationArray.push("<div class='tab-pane-sub'>");
	educationArray.push("<input type='hidden' id='subjectId"+subjectRowNumNext+"'  value=''>");
	educationArray.push("<input type='hidden' id='subjectAttachId"+subjectRowNumNext+"'  value=''>");
	educationArray.push("<ul>");
	educationArray.push("<li><p style='width : 100px;'>课题名称：</p><input type='text' id='subject_name"+subjectRowNumNext+"' value='' placeholder=''></li>");
	educationArray.push("<li><p style='width : 100px;'>课题立项单位：</p><input type='text' id='subject_company"+subjectRowNumNext+"' value='' > </li>");
	educationArray.push("<li><p style='width : 100px;'>课题级别：</p><input type='text' id='subject_level"+subjectRowNumNext+"' value='' ></li>");
	educationArray.push("<li><p style='width : 100px;'>课题职责：</p><input type='text' id='subject_responsibility"+subjectRowNumNext+"' value='' > </li>");
	educationArray.push("<li><p style='width : 100px;'>是否结题：</p><select  id='is_finish_subject"+subjectRowNumNext+"'><option value='1' >是</option><option value='2' >否</option></select></li>");
	educationArray.push("<li><p style='width : 100px;'>结题时间：</p><input type='text' id='finish_time"+subjectRowNumNext+"' onclick='selectDeleteTime()' value='' placeholder=''></li>");
	educationArray.push("<li><p style='width : 100px;'>获奖情况：</p><input type='text' id='finish_result"+subjectRowNumNext+"' value='' placeholder=''></li>");
	
	educationArray.push("<li style='height:45px;'><p style='width : 100px;'>课题材料：</p><div style='padding-left:65px;' id='subjectbutton"+subjectRowNumNext+"'></div></li>");
	educationArray.push("<div id='subjectbuttonDiv"+subjectRowNumNext+"' style='heigth:0px'></div>");
	
	educationArray.push("<li class='btn'><input type='button' value='保 存' onclick=\"saveSingleOption('subjectType',\'"+subjectRowNumNext+"\','');\" ></li>");
	educationArray.push("</ul>");
	educationArray.push("</div>");
	
	educationArray.push("<a class='add' href='javascript:void(0);' onclick='addSubjectSingle(this)' >+</a>");
	
	$(obj).after(educationArray.join(""));
	$(obj).remove();
	
	$("#subjectRowNum").val(subjectRowNumNext);
	
	/*
	 * 1、professionalTitlespan:上传按钮显示位置id
	 4、buttonName ： 按钮名称
	 5、hiddenAttachId：隐藏附件id，选中后把附件值保存在该隐藏域里
	 6、 hiddenDisplayId:附件显示的div id
	 */
	initWebUploader('subjectbutton',subjectRowNumNext,'subjectType','点击上传','subjectAttachId','subjectbuttonDiv');
}

function addPersonalAwardSingle(obj){
	var personalAwardRowNum = parseInt($("#personalAwardRowNum").val());
	var personalAwardRowNumNext = parseInt(personalAwardRowNum+1);

	var educationArray =[];
	educationArray.push("<div id='personalAwardHead"+personalAwardRowNumNext+"' class='tit2'><span><a href='javascript:void(0);' class='tit2-trigger' onclick=\"toggleDiv(this);\">收起</a>");
	educationArray.push("<a href='javascript:void(0);' onclick=\"deleteSingleOption(this,'personalAwardType',\'"+personalAwardRowNumNext+"\','');\" >删除</a></span></div>");
	educationArray.push("<div class='tab-pane-sub'>");
	educationArray.push("<input type='hidden' id='personalAwardId"+personalAwardRowNumNext+"'  value=''>");
	educationArray.push("<input type='hidden' id='awards_attachment_id_personal"+personalAwardRowNumNext+"'  value=''>");
	educationArray.push("<ul>");
	educationArray.push("<li><p>奖项名称：</p><input type='text' id='awards_name_personal"+personalAwardRowNumNext+"' value='' placeholder=''></li>");
	educationArray.push("<li><p>表彰单位：</p><input type='text' id='awards_company_personal"+personalAwardRowNumNext+"' value='' placeholder=''></li>");
	educationArray.push("<li><p>获奖级别：</p><input type='text' id='awards_level_personal"+personalAwardRowNumNext+"' value='' placeholder=''></li>");
	educationArray.push("<li><p>表彰时间：</p><input type='text' id='awards_time_personal"+personalAwardRowNumNext+"' value='' placeholder='' onclick='selectDeleteTime()'></li>");
	
	educationArray.push("<li style='height:45px;'><p>获奖证书：</p> <span id='personalAwardUpload"+personalAwardRowNumNext+"'></span> </li>");
	educationArray.push("<div id='personalAwardTypeDiv"+personalAwardRowNumNext+"' style='heigth:0px'></div>");
	
	educationArray.push("<li class='btn'><input type='button' value='保 存' onclick=\"saveSingleOption('personalAwardType',\'"+personalAwardRowNumNext+"\','');\" ></li>");
	educationArray.push("</ul>");
	educationArray.push("</div>");
	
	educationArray.push("	<a class='add' href='javascript:void(0);' onclick='addPersonalAwardSingle(this)' >+</a>");
	
	$(obj).after(educationArray.join(""));
	$(obj).remove();
	
	$("#personalAwardRowNum").val(personalAwardRowNumNext);
	
	initWebUploader('personalAwardUpload',personalAwardRowNumNext,'personalAwardType','上传获奖证书','awards_attachment_id_personal','personalAwardTypeDiv');
	

}


function addSchoolAwardSingle(obj){
	var schoolAwardRowNum = parseInt($("#schoolAwardRowNum").val());
	var schoolAwardRowNumNext = parseInt(schoolAwardRowNum+1);

	var educationArray =[];
	educationArray.push("<div id='schoolAwardHead"+schoolAwardRowNumNext+"' class='tit2'><span><a href='javascript:void(0);' class='tit2-trigger' onclick=\"toggleDiv(this);\">收起</a>");
	educationArray.push("<a href='javascript:void(0);' onclick=\"deleteSingleOption(this,'schoolAwardType',\'"+schoolAwardRowNumNext+"\','');\" >删除</a></span></div>");
	educationArray.push("<div class='tab-pane-sub'>");
	educationArray.push("<input type='hidden' id='schoolAwardId"+schoolAwardRowNumNext+"'  value=''>");
	educationArray.push("<input type='hidden' id='schoolAwardAttachId"+schoolAwardRowNumNext+"'  value=''>");
	educationArray.push("<ul>");
	educationArray.push("<li><p>奖项名称：</p><input type='text' id='awards_name"+schoolAwardRowNumNext+"' value='' placeholder=''></li>");
	educationArray.push("<li><p>任职学校：</p><input type='text' id='work_school_award"+schoolAwardRowNumNext+"' value='' placeholder=''></li>");
	educationArray.push("<li><p>表彰单位：</p><input type='text' id='awards_company"+schoolAwardRowNumNext+"' value='' placeholder=''> </li>");
	educationArray.push("<li><p>获奖级别：</p><select  id='awards_level"+schoolAwardRowNumNext+"'><option value='2' >2016</option></select></li>");
	educationArray.push("<li><p>表彰时间：</p><input type='text' id='awards_time"+schoolAwardRowNumNext+"' value='' placeholder='' onclick='selectDeleteTime()'></li>");
	
	educationArray.push("<li style='height:45px;'><p>获奖证书：</p><div style='padding-left:65px;' id='schoolAwardbutton"+schoolAwardRowNumNext+"'></div></li>");
	educationArray.push("<div id='schoolAwardbuttonDiv"+schoolAwardRowNumNext+"' style='heigth:0px'></div>");
	
	
	educationArray.push("<li class='btn'><input type='button' value='保 存' onclick=\"saveSingleOption('schoolAwardType',\'"+schoolAwardRowNumNext+"\','');\" ></li>");
	educationArray.push("</ul>");
	educationArray.push("</div>");
	
	educationArray.push("	<a class='add' href='javascript:void(0);' onclick='addSchoolAwardSingle(this)' >+</a>");
	
	$(obj).after(educationArray.join(""));
	$(obj).remove();
	
	$("#schoolAwardRowNum").val(schoolAwardRowNumNext);
	
	/*
	 * 1、professionalTitlespan:上传按钮显示位置id
	 4、buttonName ： 按钮名称
	 5、hiddenAttachId：隐藏附件id，选中后把附件值保存在该隐藏域里
	 6、 hiddenDisplayId:附件显示的div id
	 */
	initWebUploader('schoolAwardbutton',schoolAwardRowNumNext,'schoolAwardType','点击上传','schoolAwardAttachId','schoolAwardbuttonDiv');
}


function deleteSingleOption(obj,optionType,rowNum){
    $t = $(obj);
	$t.parents('.tit2').next('.tab-pane-sub').remove();
	$t.parents('.tit2').remove();
	
	 switch (optionType) {
	     case "educationType":
	    	 deleteEducationDeal(rowNum);
	         break;
	     case "workExperienceType":
	    	 deleteWorkExperienceDeal();
	         break;
	     case "professionalTitleType":
	    	 deleteProfessionalTitleDeal();
	         break;
	     case "paperType":
	    	 deletePaperDeal();
	         break;
	     case "workPublishType":
	    	 deleteWorkPublishDeal();
	         break;
	     case "subjectType":
	    	 deleteSubjectDeal();
	         break;
	     case "personalAwardType":
	    	 deletePersonalAwardDeal();
	         break;
	     case "schoolAwardType":
	    	 deleteSchoolAwardDeal();
	         break;
	     default:
	         break
     }
}

function deleteEducationDeal(rowNum){
	var id = $("#educationId"+rowNum).val();
	if(!id){
		return;
	}
	var bcReq = new BcRequest('headmaster','masterReviewAction','deleteEducation');
	bcReq.setExtraPs({
		  "id":id
		}
	);
	bcReq.setSuccFn(function(data,status){
		
	});
	bcReq.postData();
}

function deleteWorkExperienceDeal(rowNum){
	var id = $("#workExperienceId"+rowNum).val();
	if(!id){
		return;
	}
	var bcReq = new BcRequest('headmaster','masterReviewAction','deleteWorkExperience');
	bcReq.setExtraPs({
		  "id":id
		}
	);
	bcReq.setSuccFn(function(data,status){
		
	});
	bcReq.postData();
}

function deleteProfessionalTitleDeal(rowNum){
	var id = $("#professionalTitleId"+rowNum).val();
	if(!id){
		return;
	}
	var bcReq = new BcRequest('headmaster','masterReviewAction','deleteProfessionalTitle');
	bcReq.setExtraPs({
		  "id":id
		}
	);
	bcReq.setSuccFn(function(data,status){
		
	});
	bcReq.postData();
}

function deletePaperDeal(rowNum){
	var id = $("#paperId"+rowNum).val();
	if(!id){
		return;
	}
	var bcReq = new BcRequest('headmaster','masterReviewAction','deletePaper');
	bcReq.setExtraPs({
		  "id":id
		}
	);
	bcReq.setSuccFn(function(data,status){
		
	});
	bcReq.postData();
}

function deleteWorkPublishDeal(rowNum){
	var id = $("#workPublishId"+rowNum).val();
	if(!id){
		return;
	}
	var bcReq = new BcRequest('headmaster','masterReviewAction','deleteWorkPublish');
	bcReq.setExtraPs({
		  "id":id
		}
	);
	bcReq.setSuccFn(function(data,status){
		
	});
	bcReq.postData();
}

function deleteSubjectDeal(rowNum){
	var id = $("#subjectId"+rowNum).val();
	if(!id){
		return;
	}
	var bcReq = new BcRequest('headmaster','masterReviewAction','deleteSubject');
	bcReq.setExtraPs({
		  "id":id
		}
	);
	bcReq.setSuccFn(function(data,status){
		
	});
	bcReq.postData();
}

function deletePersonalAwardDeal(rowNum){
	var id = $("#personalAwardId"+rowNum).val();
	if(!id){
		return;
	}
	var bcReq = new BcRequest('headmaster','masterReviewAction','deletePersonalAward');
	bcReq.setExtraPs({
		  "id":id
		}
	);
	bcReq.setSuccFn(function(data,status){
		
	});
	bcReq.postData();
}

function deleteSchoolAwardDeal(rowNum){
	var id = $("#schoolAwardId"+rowNum).val();
	if(!id){
		return;
	}
	var bcReq = new BcRequest('headmaster','masterReviewAction','deleteSchoolAward');
	bcReq.setExtraPs({
		  "id":id
		}
	);
	bcReq.setSuccFn(function(data,status){
		
	});
	bcReq.postData();
}

function saveSingleOption(optionType,rowNum,id){
	 switch (optionType) {
	     case "educationType":
	    	 educationDeal(optionType,rowNum,id);
	         break;
	     case "workExperienceType":
	    	 workExperienceDeal(optionType,rowNum,id);
	         break;
	     case "professionalTitleType":
	    	 professionalTitleDeal(optionType,rowNum,id);
	         break;
	     case "paperType":
	    	 paperDeal(optionType,rowNum,id);
	         break;
	     case "workPublishType":
	    	 workPublishDeal(optionType,rowNum,id);
	         break;
	     case "subjectType":
	    	 subjectDeal(optionType,rowNum,id);
	         break;
	     case "personalAwardType":
	    	 personalAwardDeal(optionType,rowNum,id);
	         break;
	     case "schoolAwardType":
	    	 schoolAwardDeal(optionType,rowNum,id);
	         break;
	     default:
	         break
   }
}

function educationDeal(optionType,rowNum,id){
	var id = $("#educationId"+rowNum).val();
	var startTime = $("#startTimeEducation"+rowNum).val();
	var endTime = $("#endTimeEducation"+rowNum).val();
	var study_school = $("#study_schoolEducation"+rowNum).val();
	var study_profession = $("#study_professionEducation"+rowNum).val();
	var education = $("#education"+rowNum).val();
	var degree = $("#degree"+rowNum).val();
	var educationAttachmentId = $("#educationAttachId"+rowNum).val();
	var degreeAttachmentId = $("#degreeAttachId"+rowNum).val();
	var bcReq;
	if(id){
		bcReq = new BcRequest('headmaster','masterReviewAction','updateEducation');
	}else{
		bcReq = new BcRequest('headmaster','masterReviewAction','addEducation');
	}
	bcReq.setExtraPs({
		"id":id,
		"businessKey":$("#id").val(),
		"startTime":startTime,
		"endTime":endTime,
		"studySchool":study_school,
		'studyProfession' :study_profession,
		'education' :education,
		'degree' :degree,
		'educationAttachmentId' :educationAttachmentId,
		'degreeAttachmentId' :degreeAttachmentId
	});
	bcReq.setSuccFn(function(data,status){
		var result = data.Data[0];
		if(id){
			$("#startTimeEducation"+rowNum).val(startTime);
			$("#endTimeEducation"+rowNum).val(endTime);
			$("#study_schoolEducation"+rowNum).val(study_school);
			$("#study_professionEducation"+rowNum).val(study_profession);
			$("#education"+rowNum).val(education);
			$("#degree"+rowNum).val(degree);
		}else{
			$("#educationId"+rowNum).val(result.id);
			$("#educationHead"+rowNum).append(rowNum+"、"+startTime+"-"+endTime+" "+study_school);
		}
		$("#educationHead"+rowNum ).find('.tit2-trigger').click();
		zebra_alert("保存成功!");
	});
	bcReq.postData();
}


function workExperienceDeal(optionType,rowNum,id){
	var id = $("#workExperienceId"+rowNum).val();
	var startTime = $("#startTimeExperience"+rowNum).val();
	var endTime = $("#endTimeExperience"+rowNum).val();
	var workSchool = $("#workSchoolExperience"+rowNum).val();
	var workProfession = $("#workProfession"+rowNum).val();
	var workYear = $("#workYear"+rowNum).val();
	var proveAttachMentId = $("#proveId"+rowNum).val();
	var bcReq;
	if(id){
		bcReq = new BcRequest('headmaster','masterReviewAction','updateWorkExperience');
	}else{
		bcReq = new BcRequest('headmaster','masterReviewAction','addWorkExperience');
	}
	bcReq.setExtraPs({
		"id":id,
		"businessKey":$("#id").val(),
		"startTime":startTime,
		"endTime":endTime,
		"workSchool":workSchool,
		'workProfession' :workProfession,
		'workYear' :workYear,
		'proveAttachMentId' :proveAttachMentId
	});
	bcReq.setSuccFn(function(data,status){
		var result = data.Data[0];
		if(id){
			$("#startTimeExperience"+rowNum).val(startTime);
			$("#endTimeExperience"+rowNum).val(endTime);
			$("#workSchoolExperience"+rowNum).val(workSchool);
			$("#workProfession"+rowNum).val(workProfession);
			$("#workYear"+rowNum).val(workYear);
		}else{
			$("#workExperienceId"+rowNum).val(result.id);
			$("#workExperienceHead"+rowNum).append(rowNum+"、"+startTime+"-"+endTime+" "+workSchool);
		}
		$("#workExperienceHead"+rowNum ).find('.tit2-trigger').click();
		zebra_alert("保存成功!");
	});
	bcReq.postData();
}

function professionalTitleDeal(optionType,rowNum,id){
	var id = $("#professionalTitleId"+rowNum).val();
	var professionaltitle_name = $("#professionaltitle_name"+rowNum).val();
	var obtain_time = $("#obtain_time"+rowNum).val();
	var obtain_school = $("#obtain_school"+rowNum).val();
	var attachment_id = $("#professionalAttachId"+rowNum).val();
	var bcReq;
	if(id){
		bcReq = new BcRequest('headmaster','masterReviewAction','updateProfessionalTitle');
	}else{
		bcReq = new BcRequest('headmaster','masterReviewAction','addProfessionalTitle');
	}
	bcReq.setExtraPs({
		"id":id,
		"businessKey":$("#id").val(),
		"professionaltitle_name":professionaltitle_name,
		"obtainTime":obtain_time,
		"obtainSchool":obtain_school,
		"professionalAttachId":attachment_id
	});
	bcReq.setSuccFn(function(data,status){
		var result = data.Data[0];
		if(id){
			$("#professionaltitle_name"+rowNum).val(professionaltitle_name);
			$("#obtain_time"+rowNum).val(obtain_time);
			$("#obtain_school"+rowNum).val(obtain_school);
		}else{
			$("#professionalTitleId"+rowNum).val(result.id);
			$("#professionalTitleHead"+rowNum).append(rowNum+"、"+professionaltitle_name);
		}
		$("#professionalTitleHead"+rowNum ).find('.tit2-trigger').click();
		zebra_alert("保存成功!");
	});
	bcReq.postData();
}


function paperDeal(optionType,rowNum,id){
	//var startTime = $("#startTimeProfessional"+rowNum).val();
	//var endTime = $("#endTimeProfessional"+rowNum).val();
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
	var bcReq;
	if(id){
		bcReq = new BcRequest('headmaster','masterReviewAction','updatePaper');
	}else{
		bcReq = new BcRequest('headmaster','masterReviewAction','addPaper');
	}
	bcReq.setExtraPs({
		"id":id,
		"title":title,
		"businessKey":$("#id").val(),
		"publish_time":publish_time,
		"magazine_meet_name":magazine_meet_name,
		"paper_meet_name":paper_meet_name,
		"paper_number":paper_number,
		"organizers":organizers,
		'organizers_level' :organizers_level,
		'personal_part' :personal_part,
		'paper_attachment_id' :paper_attachment_id,
	});
	bcReq.setSuccFn(function(data,status){
		var result = data.Data[0];
		if(id){
			$("#title"+rowNum).val(title);
			$("#magazine_meet_name"+rowNum).val(magazine_meet_name);
			$("#paper_meet_name"+rowNum).val(paper_meet_name);
			$("#paper_number"+rowNum).val(paper_number);
			$("#publish_time_paper"+rowNum).val(publish_time);
			$("#organizers"+rowNum).val(organizers);
			$("#organizers_level"+rowNum).val(organizers_level);
			$("#personal_part"+rowNum).val(personal_part);
		}else{
			$("#paperId"+rowNum).val(result.id);
			$("#paperHead"+rowNum).append(rowNum+"、"+title);
		}
		$("#paperHead"+rowNum ).find('.tit2-trigger').click();
		zebra_alert("保存成功!");
	});
	bcReq.postData();
}

function workPublishDeal(optionType,rowNum,id){
	var id = $("#workPublishId"+rowNum).val();
	var book_name = $("#book_name"+rowNum).val();
	var complete_way = $("#complete_way"+rowNum).val();
	var publish_time = $("#publish_time"+rowNum).val();
	var complete_chapter = $("#complete_chapter"+rowNum).val();
	var complete_word = $("#complete_word"+rowNum).val();
	var author_order = $("#author_order"+rowNum).val();
	var coverAttachmentId = $("#coverAttachmentId"+rowNum).val();
	var contentsAttachmentId = $("#contentsAttachmentId"+rowNum).val();
	var bcReq;
	if(id){
		bcReq = new BcRequest('headmaster','masterReviewAction','updateWorkPublish');
	}else{
		bcReq = new BcRequest('headmaster','masterReviewAction','addWorkPublish');
	}
	bcReq.setExtraPs({
		"id":id,
		"businessKey":$("#id").val(),
		"book_name":book_name,
		"complete_way":complete_way,
		"publish_time":publish_time,
		"complete_chapter":complete_chapter,
		"complete_word":complete_word,
		"author_order":author_order,
		'coverAttachmentId' :coverAttachmentId,
		'contentsAttachmentId' :contentsAttachmentId
	});
	bcReq.setSuccFn(function(data,status){
		var result = data.Data[0];
		if(id){
			$("#book_name"+rowNum).val(book_name);
			$("#complete_way"+rowNum).val(complete_way);
			$("#publish_time"+rowNum).val(publish_time);
			$("#complete_word"+rowNum).val(complete_word);
			$("#complete_chapter"+rowNum).val(complete_chapter);
			$("#author_order"+rowNum).val(author_order);
		}else{
			$("#workPublishId"+rowNum).val(result.id);
			$("#workPublishHead"+rowNum).append(rowNum+"、"+book_name);
		}
		$("#workPublishHead"+rowNum ).find('.tit2-trigger').click();
		zebra_alert("保存成功!");
	});
	bcReq.postData();
}

function subjectDeal(optionType,rowNum,id){
	var id = $("#subjectId"+rowNum).val();
	var subject_name = $("#subject_name"+rowNum).val();
	var subject_company = $("#subject_company"+rowNum).val();
	var subject_level = $("#subject_level"+rowNum).val();
	var subject_responsibility = $("#subject_responsibility"+rowNum).val();
	var is_finish_subject = $("#is_finish_subject"+rowNum).val();
	var finish_result = $("#finish_result"+rowNum).val();
	var finish_time = $("#finish_time"+rowNum).val();
	var subjectAttachmentId = $("#subjectAttachId"+rowNum).val();
	var bcReq;
	if(id){
		bcReq = new BcRequest('headmaster','masterReviewAction','updateSubject');
	}else{
		bcReq = new BcRequest('headmaster','masterReviewAction','addSubject');
	}
	bcReq.setExtraPs({
		"id":id,
		"businessKey":$("#id").val(),
		"subject_name":subject_name,
		"subject_company":subject_company,
		"subject_level":subject_level,
		"subject_responsibility":subject_responsibility,
		"is_finish_subject":is_finish_subject,
		"finish_result":finish_result,
		'finish_time' :finish_time,
		'subjectAttachmentId' :subjectAttachmentId
	});
	bcReq.setSuccFn(function(data,status){
		var result = data.Data[0];
		if(id){
			$("#subject_name"+rowNum).val(subject_name);
			$("#subject_company"+rowNum).val(subject_company);
			$("#subject_level"+rowNum).val(subject_level);
			$("#subject_responsibility"+rowNum).val(subject_responsibility);
			$("#is_finish_subject"+rowNum).val(is_finish_subject);
			$("#finish_time"+rowNum).val(finish_time);
			$("#finish_result"+rowNum).val(finish_result);
		}else{
			$("#subjectId"+rowNum).val(result.id);
			$("#subjectHead"+rowNum).append(rowNum+"、"+subject_name);
		}
		$("#subjectHead"+rowNum ).find('.tit2-trigger').click();
		zebra_alert("保存成功!");
	});
	bcReq.postData();
}

function personalAwardDeal(optionType,rowNum,id){
	var id = $("#personalAwardId"+rowNum).val();
	var awards_name = $("#awards_name_personal"+rowNum).val();
	var awards_company = $("#awards_company_personal"+rowNum).val();
	var awards_level = $("#awards_level_personal"+rowNum).val();
	var awards_time = $("#awards_time_personal"+rowNum).val();
	var awards_attachment_id = $("#awards_attachment_id_personal"+rowNum).val();
	var bcReq;
	if(id){
		bcReq = new BcRequest('headmaster','masterReviewAction','updatePersonalAward');
	}else{
		bcReq = new BcRequest('headmaster','masterReviewAction','addPersonalAward');
	}
	bcReq.setExtraPs({
		"id":id,
		"businessKey":$("#id").val(),
		"awards_name":awards_name,
		"awards_company":awards_company,
		"awards_level":awards_level,
		"awards_time":awards_time,
		"awards_attachment_id":awards_attachment_id
	});
	bcReq.setSuccFn(function(data,status){
		var result = data.Data[0];
		if(id){
			$("#awards_name_personal"+rowNum).val(awards_name);
			$("#awards_company_personal"+rowNum).val(awards_company);
			$("#awards_level_personal"+rowNum).val(awards_level);
			$("#awards_time_personal"+rowNum).val(awards_time);
		}else{
			$("#personalAwardId"+rowNum).val(result.id);
			$("#personalAwardHead"+rowNum).append(rowNum+"、"+awards_name);
		}
		$("#personalAwardHead"+rowNum ).find('.tit2-trigger').click();
		zebra_alert("保存成功!");
	});
	bcReq.postData();
}


function schoolAwardDeal(optionType,rowNum,id){
	var id = $("#schoolAwardId"+rowNum).val();
	var awards_name = $("#awards_name"+rowNum).val();
	var work_school_award = $("#work_school_award"+rowNum).val();
	var awards_company = $("#awards_company"+rowNum).val();
	var awards_level = $("#awards_level"+rowNum).val();
	var awards_time = $("#awards_time"+rowNum).val();
	var awards_attachment_id = $("#schoolAwardAttachId"+rowNum).val();
	var bcReq;
	if(id){
		bcReq = new BcRequest('headmaster','masterReviewAction','updateSchoolAward ');
	}else{
		bcReq = new BcRequest('headmaster','masterReviewAction','addSchoolAward');
	}
	bcReq.setExtraPs({
		"id":id,
		"businessKey":$("#id").val(),
		"awards_name":awards_name,
		"work_school":work_school_award,
		"awards_company":awards_company,
		"awards_level":awards_level,
		"awards_time":awards_time,
		"awards_attachment_id":awards_attachment_id
	});
	bcReq.setSuccFn(function(data,status){
		var result = data.Data[0];
		if(id){
			$("#awards_name"+rowNum).val(awards_name);
			$("#work_school_award"+rowNum).val(work_school_award);
			$("#awards_company"+rowNum).val(awards_company);
			$("#awards_level"+rowNum).val(awards_level);
			$("#awards_time"+rowNum).val(awards_time);
		}else{
			$("#schoolAwardId"+rowNum).val(result.id);
			$("#schoolAwardHead"+rowNum).append(rowNum+"、"+awards_name);
		}
		$("#schoolAwardHead"+rowNum ).find('.tit2-trigger').click();
		zebra_alert("保存成功!");
	});
	bcReq.postData();
}


var swfupload1;
var swfupload2;
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
			button_image_url : "/js/swfupload/button_notext.png",
			button_placeholder_id : "",
			button_text: "<span >选择文件</span>",
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
		//file_dialog_complete_handler : selectFileComplete,//fileDialogComplete,
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
		 debug: true  //是否显示调试窗口
	}; 
	
	//近八年
	var setting1 =  jQuery.extend(defaultSettings, {
		button_placeholder_id :"manageDifficultyAgo",
		file_dialog_complete_handler:selectFileComplete1
		})
	swfupload1 = new SWFUpload(setting1);
	//swfupload1.uploadStopped = false;
	
	var setting2 =  jQuery.extend(defaultSettings, {
		button_placeholder_id :"manageDifficulty",
		file_dialog_complete_handler:selectFileComplete2
	})
	swfupload2 = new SWFUpload(setting2);
	//swfupload2.uploadStopped = false;
}


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
	if(fileName && fileName.length > 12) fileName = fileName.substring(0,12)+'...';
	
	//var file_upload_type = jsonData.file_upload_type;
	var selectDivId = jsonData.selectDivId;
	var row_number = jsonData.row_number;
	
	var attachmentArray =[];
	//attachmentArray.push("<tr>");
	attachmentArray.push("<a class=\"color3\"  href=\"<%=basePath%>WorkflowAttachMentDownload?attachmentId="+attachmentId+"\">"+fileName+"</a>");
	attachmentArray.push(fileSize);
	attachmentArray.push("<a class=\"color3\"  href=\"#\" onclick=\"deleteReceiveFileAttachment(\'"+attachmentId+"\',this);\" >删除</a>");
	//attachmentArray.push("</tr>");
	
	$("#"+selectDivId+"Div").append(attachmentArray.join(""));
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


function selectDeleteTime(timeObject){
	WdatePicker({
		readOnly:true,
		isShowClear:false,
		isShowOthers:false,
		maxDate:'2099-12-31 23:59:59',
		onpicked:function(dp,aa){
			var selDateId = dp.srcEl.id;
			if(selDateId && selDateId.substring(0,(selDateId.length-1)) =='endTimeExperience'){
				var rownum = selDateId.substring((selDateId.length-1)) 
				var startDate =  $('#startTimeExperience'+rownum).val();
				var endDate =  $(dp.srcEl).val();
				var diffDate = BcUtil.get_yearMonthDiffFormat(startDate,endDate)
			}
			//getMessageRecord(dp)
		}
	});
}

function getMasterReviewBusinessKey(){
    var bcReq = new BcRequest('headmaster','masterReviewAction','getMasterReviewBusinessKey');
	bcReq.setSuccFn(function(data,status){
		$("#id").val(data.Data[0].mainBusinessKey);
	});
	bcReq.postData();
}


function submitMasterApply(){
	Brightcom.workflow.completeButtonTask('flow1',Brightcom.workflow.processStartType);
}

function submitRefillTask(){
	isSaveDraft = '0';
	Brightcom.workflow.completeButtonTask('flow12',Brightcom.workflow.processStartType);
}

function submitNextStep(currentStep){
	 switch (currentStep) {
	     case "one":
	    	 $($('#myTab li').get(0)).removeClass('active');
	    	 $($('#myTab li').get(1)).addClass('active');
	    	 
	    	 $('#one').removeClass('active');
	    	 $('#two').addClass('active');
	         break;
	     case "two":
	    	 $($('#myTab li').get(1)).removeClass('active');
	    	 $($('#myTab li').get(2)).addClass('active');
	    	 
	    	 $('#two').removeClass('active');
	    	 $('#three').addClass('active');
	         break;
	     case "three":
	    	 $($('#myTab li').get(2)).removeClass('active');
	    	 $($('#myTab li').get(3)).addClass('active');
	    	 
	    	 $('#three').removeClass('active');
	    	 $('#four').addClass('active');
	         break;
	     case "four":
	    	 $($('#myTab li').get(3)).removeClass('active');
	    	 $($('#myTab li').get(4)).addClass('active');
	    	 
	    	 $('#four').removeClass('active');
	    	 $('#five').addClass('active');
	         break;
	     default:
	         break
    }
}
</script>
<input type="hidden" id="workflowSubmitAction" name="workflowSubmitAction" value="headmaster@@masterReviewAction@@startMasterReviewProcess">
<input type="hidden" id="id" name="id" value="">
<!-- 
<input type="hidden" id="mobile" name="mobile" value="">
<input type="hidden" id="identitycard" name="identitycard" value=""> -->
<input type="hidden" id="school_id" name="school_id" value="">


<!-- Main Start -->
<div class="main">
	<!-- 进度 s -->
	<div class="grogress"><div class="line" style="width:46px;"><!-- 970/21 --></div></div>
	<!-- 进度 e -->
	<!-- 标题 s -->
	<div class="com-title">
		<div class="txt fl">
			<h2><i>1</i>基础信息</h2>
		</div>
		<div class="select-step fr"><a href="javascript:void(0);" target="_self" title="" id="change">+&nbsp;切换步骤</a></div>
		<div class="clear-both"></div>
	</div>
	<!-- 标题 e -->
	<!-- 基础信息填写 s -->
	<div class="base-info">
		<div class="photo">
			<p><img src="images/u184.jpg" alt="" title="" /></p>
			<p><a href="javascript:void(0);" target="_self" title="">上传照片</a></p>
		</div>
		<ul class="clear-fix">
			<li>
				<div class="border_1 w_1 fl">
					<span>姓名：</span>
					<input type="text" name="" value="" placeholder="请输入姓名" />
				</div>
				<div class="border_1 w_2 fr" style="z-index:99;">
					<span>性别：</span>
					<select name="">
						<option>男</option>
						<option>女</option>
					</select>
				</div>
			</li>
			<li>
				<div class="border_1 w_3 fl" style="z-index:98;">
					<span>学校类型：</span>
					<select name="">
						<option>小学</option>
						<option>初中</option>
						<option>高中</option>
					</select>
				</div>
			</li>
			<li>
				<div class="border_1 w_4 fl" style="z-index:97;">
					<span>出生年月：</span>
					<input type="text" name="" value="" placeholder="年-月-日" id="date-01" />
				</div>
			</li>
			<li>
				<div class="border_1 w_5 fl" style="z-index:96;">
					<span>参加工作时间：</span>
					<input type="text" name="" value="" placeholder="年-月-日" id="date-02" />
				</div>
			</li>
			<li>
				<div class="border_1 w_6 fl" style="z-index:95;">
					<span>身份证号码：</span>
					<input type="text" name="" value="" placeholder="请输入18位身份证号码" />
				</div>
			</li>
			<li>
				<div class="border_1 w_7 fl" style="z-index:94;">
					<span>参加教育工作时间：</span>
					<input type="text" name="" value="" placeholder="年-月-日" id="date-03" />
				</div>
			</li>
			<li>
				<div class="border_1 w_3 fl" style="z-index:93;">
					<span>政治面貌：</span>
					<select name="">
						<option>党员</option>
						<option>预备党员</option>
						<option>共青团员</option>
						<option>群众</option>
					</select>
				</div>
			</li>
			<li>
				<div class="border_1 w_8 fl" style="z-index:92;">
					<span>教龄：</span>
					<input type="number" name="" value="" />
				</div>
			</li>
			<li>
				<div class="border_1 w_9 fl" style="z-index:91;">
					<span>籍贯：</span>
					<input type="text" name="" value="" placeholder="请输入省份地区" />
				</div>
				<div class="border_1 w_9 fr" style="z-index:90;">
					<span>户籍：</span>
					<select name="">
						<option>深户</option>
						<option>非深户</option>
					</select>
				</div>
			</li>
			<li>
				<div class="border_1 w_8 fl" style="z-index:89;">
					<span>学段：</span>
					<input type="text" name="" value="" />
				</div>
			</li>
			<li>
				<div class="border_1 w_10 fl" style="z-index:88;">
					<span>邮箱：</span>
					<input type="text" name="" value="" placeholder="邮箱" />
				</div>
				<div class="border_1 w_11 fr" style="z-index:87;">
					<span>民族：</span>
					<input type="text" name="" value="" placeholder="民族" />
				</div>
			</li>
			<li>
				<div class="border_1 w_3 fl" style="z-index:86;">
					<span>现任职务：</span>
					<select name="">
						<option>正校长</option>
						<option>副校长</option>
					</select>
				</div>
			</li>
			<li>
				<div class="border_1 w_3 fl" style="z-index:85;">
					<span>所在学校：</span>
					<input type="text" name="" value="" placeholder="请输入学校全称" />
				</div>
			</li>
			<li>
				<div class="border_1 w_7 fl" style="z-index:84;">
					<span>现任专业技术职务：</span>
					<input type="text" name="" value="" placeholder="请输入现任专业技术职务" />
				</div>
			</li>
		</ul>
	</div>
	<!-- 基础信息填写 e -->
	<div class="next-step" style="text-align:center;"><a href="javascript:void(0);" target="_self" title="">下一步</a></div>
</div>
<!-- Main End -->


<div class="container">
		<div class="xiao_bg">
			<ul class="nav nav-tabs" id="myTab">
				<li  ><a href="#one">1、基础信息</a></li>
				<li class="active" ><a href="#two">2、任职资历</a></li>
				<li><a href="#three">3、管理难度</a></li>
				<li><a href="#four">4、专业能力</a></li>
				<li><a href="#five">5、工作成效</a></li>
			</ul>
			
		 <div class="tab-content">
		  <!-- 1、基础信息 -->
		   <div id="eeeeeeeeee" title="ddd">的省道是多少</div>
			<div class="tab-pane  " id="one">
				<ul class="iput">
					<li><p>姓名：</p><input type="text" id="headerMasterName"  placeholder="请输入您的姓名"></li>
					<li><p>手机：</p><input type="text" id="mobile" value="" placeholder="请输入您的手机号码"></li>
					<li><p>身份证号码：</p><input type="text" id="identitycard" value="" placeholder="请输入18位身份证号码"></li>
					<li><p>所在学校：</p><input type="text" id="school_name" value="" placeholder="请输入学校名称"></li>
					<li><p>现任职务：</p><input type="text" id="present_occupation" value="" ></li>
					<li><p>申请职级：</p><select name="apply_level" id="apply_level"><option value="">请选择</option></select></li>
					<li><p>正副校长：</p><select name="ispositive" id="ispositive"><option value="">请选择</option></select></li>
					<li class="btn">
					   <input type="button" value="下一步" onclick="submitNextStep('one')"> 
					   <input type="button" value="提交" onclick="submitMasterApply()"> 
					   <input type="button" value="保存草稿" onclick="submitRefillTask()" >
					 </li>
				</ul>
			</div>
			
			  <!-- 2、任职资历 -->
			 <div class="tab-pane active " id="two">
					<div class="renzhi">
						<h4 id="01" >学历情况</h4>
						<input type="hidden" id="educationRowNum" name="educationRowNum" value="0">
						<a class="add" href="javascript:void(0);" onclick="addEducationSingle(this)" >+</a>
						<!--  
						<div class="tit2"><em>1、</em>2016/01-2016-05<span>清华大学</span></div>
						<ul>
							<li><p>起止年月：</p>
								<select name="" id=""><option value="">2016</option></select><select name="" id=""><option value="">05</option></select><span>—</span> 
								<select name="" id=""><option value="">2016</option></select>
								<select name="" id=""><option value="">05</option></select>
							</li>
							<li><p>就读院校：</p><input type="text" value="" placeholder=""></li>
							<li><p>就读专业：</p><input type="text" value="" placeholder=""></li>
							<li><p>学历：</p><select name="" id=""><option value="">2016</option></select></li>
							<li><p>学位：</p><select name="" id=""><option value="">2016</option></select></li>
							<li class="btn"><input type="submit" value="保 存"></li>
						</ul>-->
						
						

						<h4 class="mt30" id="02">任职年限</h4>
						<input type="hidden" id="workExperienceRowNum" name="workExperienceRowNum" value="0">
						<a class="add" href="javascript:void(0);" onclick="addWorkExperienceSingle(this)" >+</a>
						<!--  
						<div class="tit2"><em>1、</em>2016/01-2016-05<span>清华大学</span></div>
						<ul>
							<li><p>起止年月：</p>
								<select name="" id=""><option value="">2016</option></select><select name="" id=""><option value="">05</option></select><span>—</span> 
								<select name="" id=""><option value="">2016</option></select>
								<select name="" id=""><option value="">05</option></select>
							</li>
							<li><p>所在学校：</p><input type="text" value="" placeholder=""></li>
							<li><p>担任职务：</p><input type="text" value="" placeholder=""></li>
							<li><p>任职年限：</p><select name="" id=""><option value="">2016</option></select></li>
							<li><p>证明材料：</p><a class="file_link" href="#">选择文件上传<input class="file" type="file" value="" /></a></li>
							<li class="btn"><input type="submit" value="保 存"></li>
						</ul>-->
             
						<h4 class="mt30" id="03">职称情况</h4>
						<input type="hidden" id="professionalTitleRowNum" name="professionalTitleRowNum" value="0">
						<a class="add" href="javascript:void(0);" onclick="addProfessionalTitleSingle(this)" >+</a>
						<!--
						<div class="tit2"><em>1、</em>2016/01-2016-05<span>清华大学</span></div>
						-->
						
						<ul>
							<li class="btn">
							   <input type="button" value="下一步" onclick="submitNextStep('two')"> 
					           <input type="button" value="提交" onclick="submitMasterApply()"> 
					           <input type="button" value="保存草稿" onclick="submitRefillTask()" >
							</li>
						</ul>
					</div>
					<!-- 右导航 -->
					<div class="rightnav">
						<ul>
							<li><img src="/masterreview/images/mid.png" alt=""> <a href="#01">学历情况</a></li>
							<li><img src="/masterreview/images/mid.png" alt=""> <a href="#02">任职年限</a></li>
							<li><img src="/masterreview/images/mid.png" alt=""> <a href="#03">职称情况</a></li>
							<li class="backtop"><a href="#top"><img src="/masterreview/images/backTop.png" alt=""></a></li>
						</ul>
					</div>
				
				</div>

				<!-- 3、管理难度 -->
				<div class="tab-pane" id="three">
					<div class="old_school">
						<ul>
							<li>学校类型：
							   <select name="" id="schoolTypeAgo">
							       <option value="">请选择学校类型</option>
							   </select>
							   <span>（近八年任职学校）</span></li>
							<li>校区数量：<input id="schoolCountAgo" type="text" value="" placeholder=""></li>
							<li>班级数量：<input id="studentNumberAgo" type="text" value="" placeholder=""></li>
							 
							 <input type="hidden" id="manageDifficultyAgoAttachId1" name="manageDifficultyAgoAttachId1" value="">
							 <span id="manageDifficultyAgo1"></span>
							 <div id="manageDifficultyAgoDiv1"></div>
						</ul>
					</div>
					<div class="new_school">
						<ul>
							<li>学校类型：<select  id="schoolType"><option value="">请选择学校类型</option></select><span>（现任职学校）</span></li>
							<li>校区数量：<input id="schoolCount" type="text" value="" placeholder=""></li>
							<li>班级数量：<input id="studentNumber" type="text" value="" placeholder=""></li>
							
						     <input type="hidden" id="manageDifficultyAttachId2" name="manageDifficultyAttachId2" value="">
							 <span id="manageDifficulty2"></span>
							 <div id="manageDifficultyDiv2"></div>
							 
						</ul>
					</div>
					<div class="clear"></div>
					<div class="renzhi">
						<ul>
							<li class="btn"> 
							   <input type="button" value="下一步" onclick="submitNextStep('three')"> 
					           <input type="button" value="提交" onclick="submitMasterApply()"> 
					           <input type="button" value="保存草稿" onclick="submitRefillTask()" >
					         </li>
						</ul>
					</div>
			
					
					<!--  
					<div class="renzhi">
				
					    <ul>
							<li class="btn"> 
							   <input type="button" value="下一步" onclick="submitNextStep('three')"> 
					           <input type="button" value="提交" onclick="submitMasterApply()"> 
					           <input type="button" value="保存草稿" onclick="submitRefillTask()" >
					         </li>
						</ul>
					</div>-->
				</div>

				<!-- 4、专业能力 -->
				<div class="tab-pane" id="four">
					<div class="renzhi">
						<h4 id="04">论文发表情况</h4>
						<input type="hidden" id="paperTitleRowNum" name="paperTitleRowNum" value="0">
						<a class="add" href="javascript:void(0);" onclick="addPaperSingle(this)" >+</a>
						

						<h4 id="05" class="mt30">著作出版情况</h4>
						<input type="hidden" id="workPublishRowNum" name="workPublishRowNum" value="0">
						<a class="add" href="javascript:void(0);" onclick="addWorkPublishSingle(this)" >+</a>
					
             
						<h4 id="06" class="mt30">课题情况</h4>
						<input type="hidden" id="subjectRowNum" name="subjectRowNum" value="0">
						<a class="add" href="javascript:void(0);" onclick="addSubjectSingle(this)" >+</a>
						
						
						<h4 id="07" class="mt30">个人获奖情况</h4>
						<input type="hidden" id="personalAwardRowNum" name="personalAwardRowNum" value="0">
						<a class="add" href="javascript:void(0);" onclick="addPersonalAwardSingle(this)" >+</a>
						
						<h4 id="08" class="mt30">学校获得荣誉</h4>
						<input type="hidden" id="schoolAwardRowNum" name="schoolAwardRowNum" value="0">
						<a class="add" href="javascript:void(0);" onclick="addSchoolAwardSingle(this)" >+</a>
						
						<ul>
							<li class="btn"> 
							   <input type="button" value="下一步" onclick="submitNextStep('four')"> 
					           <input type="button" value="提交" onclick="submitMasterApply()"> 
					           <input type="button" value="保存草稿" onclick="submitRefillTask()" >
					         </li>
						</ul>
					</div>
					
						<!-- 右导航 -->
					<div class="rightnavmajor" >
						<ul >
							<li><img src="/masterreview/images/mid.png" alt=""> <a href="#04">论文发表</a></li>
							<li><img src="/masterreview/images/mid.png" alt=""> <a href="#05">著作出版</a></li>
							<li><img src="/masterreview/images/mid.png" alt=""> <a href="#06">课题情况</a></li>
							<li><img src="/masterreview/images/mid.png" alt=""> <a href="#07">个人获奖</a></li>
							<li><img src="/masterreview/images/mid.png" alt=""> <a href="#08">学校荣誉</a></li>
							<li class="backtop"><a href="#top"><img src="/masterreview/images/backTop.png" alt=""></a></li>
						</ul>
					</div>
				</div>

				<!-- 5、工作成效 -->
				<div class="tab-pane" id="five">
						<table   cellpadding="0" cellspacing="1" border="0" class="table text-center">
						    <tbody >
			                    <tr>
						            <th>述职报告</th>
						        </tr>
						        <tr>
						        	<td class="text-center">
						        	   <textarea id="work_report" style="width:98%;" class="text" name="" id="" cols="30" rows="10"></textarea>
						        	</td>
						        </tr>
						   </tbody>
						</table>
				
					<div class="renzhi">
					    <ul>
							<li class="btn"> 
					           <input type="button" value="提交" onclick="submitMasterApply()"> 
					           <input type="button" value="保存草稿" onclick="submitRefillTask()" >
					         </li>
						</ul>
					</div>
				</div>
		 </div>
	</div>
</div>
<!--  
    <span id="spanButtonPlaceHolder" style="margin-left:16px;margin-top:56px;font-weight:bold"></span>
    
    <input id="btnUpload" type="button" value="上  传1" onclick="startUploadFile1();" class="btn3_mouseout" />

    <span id="spanButtonPlaceHolder2" style="margin-left:16px;margin-top:56px;font-weight:bold">333333333</span>
    
    <input id="btnUpload" type="button" value="上  传2" onclick="startUploadFile2();" class="btn3_mouseout" /> -->
