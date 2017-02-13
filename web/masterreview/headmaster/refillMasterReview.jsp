<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="org.jdom.*"%>
<%@ page import="java.util.List"%>
<%@ page import="cn.brightcom.jraf.web.*"%>
<%@ page import="org.apache.commons.lang.StringUtils"%>
<%
//isFristApply为表示是从开始节点提交下一步进入驳回充填节点
  String isFristApply =(String)request.getParameter("isFristApply");
isFristApply = StringUtils.isNotEmpty(isFristApply)? isFristApply : "0";

  String userid = (String)session.getAttribute("userid");
  String username =(String)session.getAttribute("username");
  String usertype =(String)session.getAttribute("usertype");
  String current_option_num =(String)session.getAttribute("current_option_num");
  String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
  
  String processBusinessKey = (String)request.getAttribute("processBusinessKey");
  String processInstanceId = (String)request.getAttribute("processInstanceId");
  String processDefKey = (String)request.getAttribute("processDefKey");

  Document reqXml = HttpProcesser.createRequestPackage("headmaster","masterReviewAction","viewMasterReview",request);
  reqXml.getRootElement().getChild("Request").getChild("Data").addContent((new Element("query_businessKey")).setText(processBusinessKey));
  reqXml.getRootElement().getChild("Request").getChild("Data").addContent((new Element("processInstanceId")).setText(processInstanceId));
  reqXml.getRootElement().getChild("Request").getChild("Data").addContent((new Element("processDefKey")).setText(processDefKey));
  Document xmlDoc = SwitchCenter.doPost(reqXml);
  request.setAttribute("xmlDoc",xmlDoc);
  
  
	List records = xmlDoc.getRootElement().getChild("Response").getChild("Data").getChildren("Record");
	String masterReviewVO ="";
	if (null != records) {
		Element record = (Element) records.get(0);
		masterReviewVO =  record.getChildTextTrim("masterReviewVO");
	}
%>
<style type="text/css">
	.chachu {
		outline: none;
	    color: #7CBAE5;!important
		text-decoration: none;	
	}
	.rightnav .current a {
			color:#ff7200;
	}
</style> 
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



<script>
$.views.tags({
	timeContent:function(startTime,endTime){
	   return Brightcom.workflow.getDateStrByLong(startTime)+'-'+Brightcom.workflow.getDateStrByLong(endTime);
	},
	timeCovert:function(time){
		   return Brightcom.workflow.getDateStrByLong(time);
	},
})

var headerMasterId = '<%=userid%>';
var headerMasterName = '<%=username%>';
var processInstanceId = '<%=processInstanceId%>';
var processBusinessKey = '<%=processBusinessKey%>';
var processDefKey = '<%=processDefKey%>';
var current_option_num = '<%=current_option_num%>';
var isFristApply = '<%=isFristApply%>';
function beforeSubmit(formJsonData){
	var mobile = $("#mobile").val();
	
	formJsonData.id = $("#id").val();
	formJsonData.headerMasterId = headerMasterId;
	formJsonData.headerMasterName = headerMasterName;
	formJsonData.mobile = mobile;
	formJsonData.identitycard = $("#identitycard").val();
	
	formJsonData.school_id = $("#school_id").val();
	formJsonData.school_name = $("#school_name").val();
	formJsonData.apply_level = $("#apply_level").val();
	
	formJsonData.present_occupation = $("#present_occupation").val();
	
	formJsonData.schoolNameSpace = $("#schoolNameSpace").val();
	formJsonData.schoolType = $("#schoolType").val();
	formJsonData.schoolCount = $("#schoolCount").val();
	formJsonData.studentNumber = $("#studentNumber").val();
	formJsonData.manage_difficulty_attachment_id = $("#manageDifficultyAttachId2").val();
	
	formJsonData.schoolNameSpaceAgo = $("#schoolNameSpaceAgo").val();
	formJsonData.schoolTypeAgo = $("#schoolTypeAgo").val();
	formJsonData.schoolCountAgo = $("#schoolCountAgo").val();
	formJsonData.studentNumberAgo = $("#studentNumberAgo").val();
	formJsonData.manage_difficulty_ago_attachment_id = $("#manageDifficultyAgoAttachId1").val();
	
	formJsonData.work_report = $("#work_report").val();
	
	headmasterBeforeSubmit && headmasterBeforeSubmit(formJsonData);
	
	var variableMap = new Brightcom.workflow.HashMap(); 
	variableMap.put("areaHeader",'4028814d5499edd2015499f003ca0006');
	formJsonData.processParam =variableMap.toJsonObject();
}

function afterSubmit(returnData){
	 window.location.href =  "/workflow/template/processViewForm.jsp?processInstanceId="+processInstanceId;
}





function initTabComponent(){
   $('#myTab a').click(function (e) {
      e.preventDefault();
      $(this).tab('show');
	  if( this.href.indexOf('#two') > -1 ||  this.href.indexOf('#four')){
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
		$(window).scroll(function(){
			var top = $(window).scrollTop();
			
			$('.rightnav li').each(function(){
				var t = this, $t = $(t);
				if( top > $t.attr('data-top') - 200 ){
					$t.addClass('current').siblings().removeClass('current');
				}
			})
			
		})
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



/*
 * uploadSingleId:上传按钮显示位置id
 buttonName ： 按钮名称
 hiddenAttachId：隐藏附件id，选中后把附件值保存在该隐藏域里
 hiddenDisplayId:附件显示的div id
 */


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




function deleteSingleOption(obj,optionType,rowNum,optionId){
    $t = $(obj);
	//$t.parents('.tit2').next('.tab-pane-sub').remove();
	//$t.parents('.tit2').remove();
	
	$t.parent().next('.container').remove();
	$t.parent().remove();
	
	 switch (optionType) {
	     case "educationType":
	    	 deleteEducationDeal(optionId);
	         break;
	     case "workExperienceType":
	    	 deleteWorkExperienceDeal(optionId);
	         break;
	     case "professionalTitleType":
	    	 deleteProfessionalTitleDeal(optionId);
	         break;
	     case "paperType":
	    	 deletePaperDeal(optionId);
	         break;
	     case "workPublishType":
	    	 deleteWorkPublishDeal(optionId);
	         break;
	     case "subjectType":
	    	 deleteSubjectDeal(optionId);
	         break;
	     case "personalAwardType":
	    	 deletePersonalAwardDeal(optionId);
	         break;
	     case "schoolAwardType":
	    	 deleteSchoolAwardDeal(optionId);
	         break;
	         
	     case "gradeEvaluate":
	    	 deleteGradeEvaluateDeal(optionId);
	         break;
	     case "schoolReformType":
	    	 deleteSchoolReformDeal(optionId);
	         break;
	     case "socialDuty":
	    	 deleteSocialDutyDeal(optionId);
	         break;
	     case "accident":
	    	 deleteAccidentDeal(optionId);
	         break;
	     case "punishment":
	    	 deletePunishmentDeal(optionId);
	         break;
	     case "workHistory":
	    	 deleteWorkHistoryDeal(optionId);
	         break;
	     case "studyTrain":
	    	 deleteStudyTrainDeal(optionId);
	         break;
	     default:
	         break
     }
}

function deleteEducationDeal(optionId){
	//var id = $("#educationId"+rowNum).val();
	if(!optionId){
		return;
	}
	var bcReq = new BcRequest('headmaster','masterReviewAction','deleteEducation');
	bcReq.setExtraPs({
		  "id":optionId
		}
	);
	bcReq.setSuccFn(function(data,status){
		
	});
	bcReq.postData();
}

function deleteWorkExperienceDeal(optionId){
//	var id = $("#workExperienceId"+rowNum).val();
	if(!optionId){
		return;
	}
	var bcReq = new BcRequest('headmaster','masterReviewAction','deleteWorkExperience');
	bcReq.setExtraPs({
		  "id":optionId
		}
	);
	bcReq.setSuccFn(function(data,status){
		
	});
	bcReq.postData();
}

function deleteProfessionalTitleDeal(optionId){
	if(!optionId){
		return;
	}
	var bcReq = new BcRequest('headmaster','masterReviewAction','deleteProfessionalTitle');
	bcReq.setExtraPs({
		  "id":optionId
		}
	);
	bcReq.setSuccFn(function(data,status){
		callBackOfDeleteProfessionalTitleDeal();//回调函数，进行相应的逻辑代码处理
	});
	bcReq.postData();
}

function deletePaperDeal(optionId){
	if(!optionId){
		return;
	}
	var bcReq = new BcRequest('headmaster','masterReviewAction','deletePaper');
	bcReq.setExtraPs({
		  "id":optionId
		}
	);
	bcReq.setSuccFn(function(data,status){
		
	});
	bcReq.postData();
}

function deleteWorkPublishDeal(optionId){
	if(!optionId){
		return;
	}
	var bcReq = new BcRequest('headmaster','masterReviewAction','deleteWorkPublish');
	bcReq.setExtraPs({
		  "id":optionId
		}
	);
	bcReq.setSuccFn(function(data,status){
		
	});
	bcReq.postData();
}

function deleteSubjectDeal(optionId){
	if(!optionId){
		return;
	}
	var bcReq = new BcRequest('headmaster','masterReviewAction','deleteSubject');
	bcReq.setExtraPs({
		  "id":optionId
		}
	);
	bcReq.setSuccFn(function(data,status){
		
	});
	bcReq.postData();
}

function deletePersonalAwardDeal(optionId){
	if(!optionId){
		return;
	}
	var bcReq = new BcRequest('headmaster','masterReviewAction','deletePersonalAward');
	bcReq.setExtraPs({
		  "id":optionId
		}
	);
	bcReq.setSuccFn(function(data,status){
		
	});
	bcReq.postData();
}

function deleteSchoolAwardDeal(optionId){
	if(!optionId){
		return;
	}
	var bcReq = new BcRequest('headmaster','masterReviewAction','deleteSchoolAward');
	bcReq.setExtraPs({
		  "id":optionId
		}
	);
	bcReq.setSuccFn(function(data,status){
		
	});
	bcReq.postData();
}



function deleteGradeEvaluateDeal(optionId){
	if(!optionId){
		return;
	}
	var bcReq = new BcRequest('headmaster','masterReviewAction','deleteGradeEvaluate');
	bcReq.setExtraPs({
		  "id":optionId
		}
	);
	bcReq.setSuccFn(function(data,status){
		
	});
	bcReq.postData();
}


function deleteSchoolReformDeal(optionId){
	if(!optionId){
		return;
	}
	var bcReq = new BcRequest('headmaster','masterReviewAction','deleteSchoolReform');
	bcReq.setExtraPs({
		  "id":optionId
		}
	);
	bcReq.setSuccFn(function(data,status){
		
	});
	bcReq.postData();
}


function deleteSocialDutyDeal(optionId){
	if(!optionId){
		return;
	}
	var bcReq = new BcRequest('headmaster','masterReviewAction','deleteSocialDuty');
	bcReq.setExtraPs({
		  "id":optionId
		}
	);
	bcReq.setSuccFn(function(data,status){
		
	});
	bcReq.postData();
}


function deleteAccidentDeal(optionId){
	if(!optionId){
		return;
	}
	var bcReq = new BcRequest('headmaster','masterReviewAction','deleteAccident');
	bcReq.setExtraPs({
		  "id":optionId
		}
	);
	bcReq.setSuccFn(function(data,status){
		
	});
	bcReq.postData();
}


function deletePunishmentDeal(optionId){
	if(!optionId){
		return;
	}
	var bcReq = new BcRequest('headmaster','masterReviewAction','deletePunishment');
	bcReq.setExtraPs({
		  "id":optionId
		}
	);
	bcReq.setSuccFn(function(data,status){
		
	});
	bcReq.postData();
}


function deleteWorkHistoryDeal(optionId){
	if(!optionId){
		return;
	}
	var bcReq = new BcRequest('headmaster','masterReviewAction','deleteWorkHistory');
	bcReq.setExtraPs({
		  "id":optionId
		}
	);
	bcReq.setSuccFn(function(data,status){
		
	});
	bcReq.postData();
}

function deleteStudyTrainDeal(optionId){
	if(!optionId){
		return;
	}
	var bcReq = new BcRequest('headmaster','masterReviewAction','deleteStudyTrain');
	bcReq.setExtraPs({
		  "id":optionId
		}
	);
	bcReq.setSuccFn(function(data,status){
		
	});
	bcReq.postData();
}



function selectDeleteTime(timeObject){
	WdatePicker({
		readOnly:true,
		isShowClear:false,
		isShowOthers:false,
		maxDate:'2099-12-31 23:59:59',
		dateFmt:'yyyy-MM-dd',
		onpicked:function(dp){
			var selDateId = dp.srcEl.id;
			if(selDateId && selDateId.substring(0,(selDateId.length-1)) =='endTimeExperience'){
				var rownum = selDateId.substring((selDateId.length-1)) 
				var startDate =  $('#startTimeExperience'+rownum).val();
				var endDate =  $(dp.srcEl).val();
				if(startDate && endDate){
					$('#workYear'+rownum).text('');
					$('#workMonth'+rownum).text('');
					var diffDate = BcUtil.get_yearMonthDiffFormat(startDate,endDate);
					//$('#workYear'+rownum).text(diffDate.year);
					//$('#workMonth'+rownum).text(diffDate.month);
					var month = diffDate.month;
					var year = parseInt(month/12);
					if (year>0) {
						month = month%12;
						$('#workYear'+rownum).text(year);
					}
					
					if (month>0) {
						$('#workMonth'+rownum).text(month);
					}
				}
			}
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
	Brightcom.workflow.completeButtonTask('flow9',Brightcom.workflow.taskType);
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

function saveDraft(){
	var id = $("#id").val();
	var present_occupation = $("#present_occupation").val();
	var apply_level = $("#apply_level").val();
	var schoolType = $("#schoolType").val();
	var schoolCount = $("#schoolCount").val();
	var studentNumber = $("#studentNumber").val();
	var schoolTypeAgo = $("#schoolTypeAgo").val();
	var schoolCountAgo = $("#schoolCountAgo").val();
	var studentNumberAgo = $("#studentNumberAgo").val();
	
	var work_report = $("#work_report").val();
	

	var bcReq = new BcRequest('headmaster','masterReviewAction','saveMasterReviewData');
	bcReq.setExtraPs({
		"id":id,
		"present_occupation":present_occupation,
		"apply_level":apply_level,
		"schoolType":schoolType,
		"schoolCount":schoolCount,
		"studentNumber":studentNumber,
		"schoolTypeAgo":schoolTypeAgo,
		"schoolCountAgo":schoolCountAgo,
		"studentNumberAgo":studentNumberAgo,
		"work_report":work_report
	});
	bcReq.setSuccFn(function(data,status){
		window.location.reload();
		zebra_alert("保存成功!");
		
	});
	bcReq.postData();
}

$(document).ready(function(){	
	$("#completeTitle").val("填写资料");
	Brightcom.workflow.beforeSubmit = beforeSubmit;
	Brightcom.workflow.afterSubmit = afterSubmit;
	$("#id").val(<%=masterReviewVO%>.id);
	initTabComponent();
	//Headmaster.initSelectCom();
	//Headmaster.findMasterBaseInfo();
    
    
	$('.renzhi .tit2-trigger').click();
	 if(current_option_num && current_option_num != 'null'){
		 changeOption(parseInt(current_option_num))
	 }else{
		 changeOption(<%=masterReviewVO%>.current_option_num || (isFristApply=='1'? '2' :'1'))
	 }
	

	//changeOption(5);
});	

//初始化选项卡的弹出方法和关闭方法
function initOptionClick(optionNumber){
	$('#currentOptionNum').val(optionNumber);
	
	$.get("/masterreview/headmaster/session_set_value.jsp",{set_session:true,current_option_num:optionNumber});
	
	$('#change').click(function(){
		$('.change-channel').layerModel({
			'head' : false,
			isClose : false
		});
	});
	
	$('#close').click(function(){
		$('.change-channel').close();
	});
	setLineWidth(optionNumber);
	
	var _this = $('#showorhide');
	$('a',_this).click(function(){
		$('p',_this).toggleClass('hide');
		if($('p',_this).attr('class') == 'hide'){
			$('a',_this).text('展开');
		}else{
			$('a',_this).text('收起');
		}
	});
}

function setLineWidth(optionNumber){
	var widthSet = (970/23)*optionNumber
	$('.line').width(widthSet);
}


function closeUnWindow(){
    var bcReq = new BcRequest('headmaster','masterReviewAction','saveCurrentOptionNum');
	bcReq.setExtraPs({
		  "current_option_num":$('#currentOptionNum').val(),
		  "businessKey":$('#id').val(),
		}
	);
	bcReq.setSuccFn(function(data,status){
	});
	bcReq.postData();
}
</script>
<input type="hidden" id="workflowSubmitAction" name="workflowSubmitAction" value="headmaster@@masterReviewAction@@completeRefillTask">
<input type="hidden" id="id" name="id" value="">
<input type="hidden" id="school_id" name="school_id" value="">
<input type="hidden" id="isRefillTask" name="isRefillTask" value="1">
<input type="hidden" id="currentOptionNum" name="currentOptionNum" value="">

<body onunload="closeUnWindow(event);"  >
<!-- Main Start -->
<div id="mainDiv"  class="main">

<!-- <iframe name ='' id ='' src='/masterreview/headmaster/base_info.jsp' width='200px;' height='500px' scrolling='no' frameborder='0'></iframe> -->
	
</div>
<!-- Main End -->

<!-- 切换步骤 s -->
<div class="change-channel">
	<a href="javascript:void(0);" target="_self" title="" class="close" id="close">x</a>
	<ul class="clear-fix">
		<li>
		     <i>1</i>
			 <a href="javascript:void(0);" onclick="changeOption(1)" target="_self" title="">基础信息</a>
			 <span class="tx">已填写</span>
		</li>
		<li>
			<i>2</i>
			<a href="javascript:void(0);" target="_self" title="" onclick="changeOption(2)">学历情况</a><span>未填写</span>
		</li>
		<li>
			<i>3</i>
			<a href="javascript:void(0);" target="_self" title="" onclick="changeOption(3)">职称情况</a><span>未填写</span>
		</li>
		<li>
		   <i>4</i>
		   	<a href="javascript:void(0);" target="_self" title="" onclick="changeOption(4)">工作经历</a>
			<span class="tx">已填写</span>
		</li>
		<li>
			<i>5</i>
			<a href="javascript:void(0);" target="_self" title="" onclick="changeOption(5)">任职年限</a>
			<span class="tx">已填写</span>
		</li>
		<li>
			<i>6</i>
			<a href="javascript:void(0);" target="_self" title="" onclick="changeOption(6)">论文发表</a>
			<span class="tx">已填写</span>
		</li>
		<li>
			<i>7</i>
			<a href="javascript:void(0);" target="_self" title="" onclick="changeOption(7)">教科研情况</a>
			<span>未填写</span>
		</li>
		<li>
		    <i>8</i>
		    <a href="javascript:void(0);" target="_self" title="" onclick="changeOption(8)">专著出版</a>
			<span>未填写</span>
		</li>
		<li>
		    <i>9</i>
		    <a href="javascript:void(0);" target="_self" title="" onclick="changeOption(9)">进修学习</a>
			<span class="bh">被驳回</span>
		</li>
		<li>
		    <i>10</i>
		    <a href="javascript:void(0);" target="_self" title="" onclick="changeOption(10)">管理难度</a>
			<span class="bh">被驳回</span>
		</li>
		<li>
		    <i>11</i>
		    <a href="javascript:void(0);" target="_self" title="" onclick="changeOption(11)">办学思想</a>
			<span class="tx">已填写</span>
		</li>
		<li>
		    <i>12</i>
		    <a href="javascript:void(0);" target="_self" title="" onclick="changeOption(12)">学校管理</a>
			<span>未填写</span>
		</li>
		<li>
		    <i>13</i>
		    <a href="javascript:void(0);" target="_self" title="" onclick="changeOption(13)">教育教学</a>
			<span class="bh">被驳回</span>
		</li>
		<li>
		    <i>14</i>
		    <a href="javascript:void(0);" target="_self" title="" onclick="changeOption(14)">外部环境</a>
			<span>未填写</span>
		</li>
		<li>
		    <i>15</i>
		    <a href="javascript:void(0);" target="_self" title="" onclick="changeOption(15)">学生发展</a>
			<span>未填写</span>
		</li>
		<li>
		    <i>16</i>
		    <a href="javascript:void(0);" target="_self" title="" onclick="changeOption(16)">教师发展</a>
			<span class="bh">被驳回</span>
		</li>
		<li>
		    <i>17</i>
		    <a href="javascript:void(0);" target="_self" title="" onclick="changeOption(17)">学校获奖情况</a>
			<span class="bh">被驳回</span>
		</li>
		<li>
		    <i>18</i>
		    <a href="javascript:void(0);" target="_self" title="" onclick="changeOption(18)">学校等级评估</a>
			<span class="bh">被驳回</span>
		</li>
		<li>
		    <i>19</i>
		   	<a href="javascript:void(0);" target="_self" title="" onclick="changeOption(19)">个人获奖情况</a>
			<span class="bh">被驳回</span>
		</li>
		<li>
		    <i>20</i>
		   	<a href="javascript:void(0);" target="_self" title="" onclick="changeOption(20)">学校特色及改革</a>
			<span>未填写</span>
		</li>
		<li>
			<i>21</i>
			<a href="javascript:void(0);" target="_self" title="" onclick="changeOption(21)">社会责任</a>
			<span>未填写</span>
		</li>
		<li>
		    <i>22</i>
		    <a href="javascript:void(0);" target="_self" title="" onclick="changeOption(22)">责任事故</a>
			<span>未填写</span>
		</li>
		<li>
	    	<i>23</i>
	    	<a href="javascript:void(0);" target="_self" title="" onclick="changeOption(23)">处分</a>
			<span class="tx">已填写</span>
		</li>
	
	</ul>
</div>
<!-- 切换步骤 e -->
<script type="text/javascript">
function changeOption(optionType){
	    optionType= parseInt(optionType);
		var bcReq = new BcRequest('headmaster','masterReviewAction','viewMasterReview');
		bcReq.setExtraPs({
			   'processInstanceId' : processInstanceId,
			   'query_businessKey' :processBusinessKey,
			   'processDefKey' :processDefKey,
			}
		);
		bcReq.setSuccFn(function(data,status){
			var masterReviewVO = JSON.parse(data.Data[0].masterReviewVO);
			switch (optionType) {
		       case 1:
			    	$('.change-channel').close();
			    	$("#mainDiv").load("/masterreview/headmaster/base_info.jsp?base_info=0", {limit: 25}, function(){
			    		initOptionClick(1);
			    	//	initRefillData(masterReviewVO);
			        });
			        break;
		       case 2:
			       	$('.change-channel').close();
			       	$("#mainDiv").load("/masterreview/headmaster/education.jsp", {limit: 25}, function(){
			       		 initOptionClick(2);
			       	 	 initEducationData(masterReviewVO);
			        });
		           break;
		       case 3:
			       	$('.change-channel').close();
			       	$("#mainDiv").load("/masterreview/headmaster/professional_title.jsp", {limit: 25}, function(){
			       		 initOptionClick(3);
			       	 	 initProfessionalData(masterReviewVO);
			        });
		          break;
		       case 4:
			    	$('.change-channel').close();
			    	$("#mainDiv").load("/masterreview/headmaster/work_history.jsp", {limit: 25}, function(){
			    		initOptionClick(4);
			    		initWorkHistoryData(masterReviewVO);
			        });
			   break;  
			    case 5:
			    	$('.change-channel').close();
			    	$("#mainDiv").load("/masterreview/headmaster/work_experience.jsp", {limit: 25}, function(){
			    		initOptionClick(5);
			    		initWorkExperience(masterReviewVO);
			        });
			        break;
			    case 6:
			    	$('.change-channel').close();
			    	$("#mainDiv").load("/masterreview/headmaster/paper.jsp", {limit: 25}, function(){
			    		initOptionClick(6);
			    		initPaperData(masterReviewVO);
			        });
			        break;
			    case 7:
			    	$('.change-channel').close();
			    	$("#mainDiv").load("/masterreview/headmaster/subject.jsp", {limit: 25}, function(){
			    		initOptionClick(7);
			    		initSubjectData(masterReviewVO);
			        });
			        break;
			    case 8:
			    	$('.change-channel').close();
			    	$("#mainDiv").load("/masterreview/headmaster/work_publish.jsp", {limit: 25}, function(){
			    		initOptionClick(8);
			    		initWorkPublishData(masterReviewVO);
			        });
			        break;
			    case 9:
			    	$('.change-channel').close();
			    	$("#mainDiv").load("/masterreview/headmaster/study_train.jsp", {limit: 25}, function(){
			    		initOptionClick(9);
			    		initStudyTrainData(masterReviewVO);
			        });
			        break;
			     case 10:
				    	$('.change-channel').close();
				    	$("#mainDiv").load("/masterreview/headmaster/manage_difficulty.jsp", {limit: 25}, function(){
				    		initOptionClick(10);
				    		initManageDifficulty(masterReviewVO);
				        });
				  break;
			     case 11:
				    	$('.change-channel').close();
				    	$("#mainDiv").load("/masterreview/headmaster/run_school.jsp", {limit: 25}, function(){
				    		initOptionClick(11);
				    		initRunSchoolData(masterReviewVO);
				        });
				  break;  
			     case 12:
				    	$('.change-channel').close();
				    	$("#mainDiv").load("/masterreview/headmaster/school_management.jsp", {limit: 25}, function(){
				    		initOptionClick(12);
				    		initSchoolManagementData(masterReviewVO);
				        });
				  break;  
			     case 13:
				    	$('.change-channel').close();
				    	$("#mainDiv").load("/masterreview/headmaster/education_science.jsp", {limit: 25}, function(){
				    		initOptionClick(13);
				    		initEducationScienceData(masterReviewVO);
				        });
				  break;  
			     case 14:
				    	$('.change-channel').close();
				    	$("#mainDiv").load("/masterreview/headmaster/external_environment.jsp", {limit: 25}, function(){
				    		initOptionClick(14);
				    		initExternalEnvironmentData(masterReviewVO);
				        });
				  break;  
			     case 15:
				    	$('.change-channel').close();
				    	$("#mainDiv").load("/masterreview/headmaster/student_development.jsp", {limit: 25}, function(){
				    		initOptionClick(15);
				    		initStudentDevelopmentData(masterReviewVO);
				        });
				  break;  
			     case 16:
				    	$('.change-channel').close();
				    	$("#mainDiv").load("/masterreview/headmaster/teacher_development.jsp", {limit: 25}, function(){
				    		initOptionClick(16);
				    		initTeacherDevelopmentData(masterReviewVO);
				        });
				  break; 
			     case 17:
				    	$('.change-channel').close();
				    	$("#mainDiv").load("/masterreview/headmaster/school_award.jsp", {limit: 25}, function(){
				    		initOptionClick(17);
				    		initSchoolAwardData(masterReviewVO);
				        });
				  break;  
			     case 18:
				    	$('.change-channel').close();
				    	$("#mainDiv").load("/masterreview/headmaster/grade_evaluate.jsp", {limit: 25}, function(){
				    		initOptionClick(18);
				    		initGradeEvaluateData(masterReviewVO);
				        });
				  break;  
			     case 19:
				    	$('.change-channel').close();
				    	$("#mainDiv").load("/masterreview/headmaster/personal_award.jsp", {limit: 25}, function(){
				    		initOptionClick(19);
				    		initPersonalAwardData(masterReviewVO);
				        });
				  break;  
			     case 20:
				    	$('.change-channel').close();
				    	$("#mainDiv").load("/masterreview/headmaster/school_reform.jsp", {limit: 25}, function(){
				    		initOptionClick(20);
				    		initSchoolReformData(masterReviewVO);
				        });
				  break;  
			     case 21:
				    	$('.change-channel').close();
				    	$("#mainDiv").load("/masterreview/headmaster/social_duty.jsp", {limit: 25}, function(){
				    		initOptionClick(21);
				    		initSocialDutyData(masterReviewVO);
				        });
				  break;  
			     case 22:
				    	$('.change-channel').close();
				    	$("#mainDiv").load("/masterreview/headmaster/accident.jsp", {limit: 25}, function(){
				    		initOptionClick(22);
				    		initAccidentData(masterReviewVO);
				        });
				  break;  
			     case 23:
				    	$('.change-channel').close();
				    	$("#mainDiv").load("/masterreview/headmaster/punishment.jsp", {limit: 25}, function(){
				    		initOptionClick(23);
				    		initPunishmentData(masterReviewVO);
				        });
				  break;  
			
			    default:
			        break
			}
		});
		bcReq.postData();
}

$('#date-01,#date-02,#date-03').datetimepicker({
	lang:"ch",
     	format:"Y-m-d",
     	timepicker:false,
     	yearStart:1900,
     	yearEnd:2050,
     	todayButton:false
});
</script>

</body>	
