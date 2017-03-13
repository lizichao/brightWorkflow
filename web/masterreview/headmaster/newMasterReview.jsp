<%@ page contentType="text/html; charset=GBK" %>
<%
  String userid = (String)session.getAttribute("userid");
  String username =(String)session.getAttribute("username");
  String usertype =(String)session.getAttribute("usertype");
 // String usertype =(String)session.getAttribute("usertype");
  String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
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
var headerMasterId = '<%=userid%>';
var headerMasterName = '<%=username%>';

var isSaveDraft = '1';
function beforeSubmit(formJsonData){
	formJsonData.id = $("#id").val();
	formJsonData.headerMasterId = headerMasterId;
	formJsonData.headerMasterName = headerMasterName;
	formJsonData.mobile = $("#mobile").val();
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
	formJsonData.isSaveDraft = isSaveDraft;
	headmasterBeforeSubmit && headmasterBeforeSubmit(formJsonData);
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
	//Headmaster.findMasterBaseInfo();
	getMasterReviewBusinessKey();
	//initSwfuploadContainer();
	initTabComponent();
	//Headmaster.initSelectCom();
	//alert(Headmaster.getBasePath())
	
	/*
	 * 1、professionalTitlespan:上传按钮显示位置id
	 4、buttonName ： 按钮名称
	 5、hiddenAttachId：隐藏附件id，选中后把附件值保存在该隐藏域里
	 6、 hiddenDisplayId:附件显示的div id
	 */
	 Headmaster.initWebUploader('manageDifficultyAgo',1,'manageDifficulty','点击上传','manageDifficultyAgoAttachId','manageDifficultyAgoDiv');
	 Headmaster.initWebUploader('manageDifficulty',2,'manageDifficulty','点击上传','manageDifficultyAttachId','manageDifficultyDiv');
	
	$("#mainDiv").load("/masterreview/headmaster/base_info.jsp?sourceType=1", {limit: 25}, function(){
		$('#change').click(function(){
			alert("请先填写好个人信息！")
			return false;
			$('.change-channel').layerModel({
				'head' : false,
				isClose : false
			});
		});
		
		$('#close').click(function(){
			$('.change-channel').close();
		});
    });
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
<!--<input type="hidden" id="school_id" name="school_id" value=""> -->


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
			<a href="javascript:void(0);" target="_self" title="" onclick="changeOption(4)">任职年限</a>
			<span class="tx">已填写</span>
		</li>
		<li>
			<i>5</i>
			<a href="javascript:void(0);" target="_self" title="" onclick="changeOption(5)">论文发表</a>
			<span class="tx">已填写</span>
		</li>
		<li>
			<i>6</i>
			<a href="javascript:void(0);" target="_self" title="" onclick="changeOption(6)">教科研情况</a>
			<span>未填写</span>
		</li>
		<li>
		    <i>7</i>
		    <a href="javascript:void(0);" target="_self" title="" onclick="changeOption(7)">专著出版</a>
			<span>未填写</span>
		</li>
		<li>
		    <i>8</i>
		    <a href="javascript:void(0);" target="_self" title="" onclick="changeOption(8)">进修学习</a>
			<span class="bh">被驳回</span>
		</li>
		<li>
		    <i>9</i>
		    <a href="javascript:void(0);" target="_self" title="" onclick="changeOption(9)">管理难度</a>
			<span class="bh">被驳回</span>
		</li>
		<li>
		    <i>10</i>
		    <a href="javascript:void(0);" target="_self" title="" onclick="changeOption(10)">办学思想</a>
			<span class="tx">已填写</span>
		</li>
		<li>
		    <i>11</i>
		    <a href="javascript:void(0);" target="_self" title="" onclick="changeOption(11)">学校管理</a>
			<span>未填写</span>
		</li>
		<li>
		    <i>12</i>
		    <a href="javascript:void(0);" target="_self" title="" onclick="changeOption(12)">教育教学</a>
			<span class="bh">被驳回</span>
		</li>
		<li>
		    <i>13</i>
		    <a href="javascript:void(0);" target="_self" title="" onclick="changeOption(13)">外部环境</a>
			<span>未填写</span>
		</li>
		<li>
		    <i>14</i>
		    <a href="javascript:void(0);" target="_self" title="" onclick="changeOption(14)">学生发展</a>
			<span>未填写</span>
		</li>
		<li>
		    <i>15</i>
		    <a href="javascript:void(0);" target="_self" title="" onclick="changeOption(15)">教师发展</a>
			<span class="bh">被驳回</span>
		</li>
		<li>
		    <i>16</i>
		    <a href="javascript:void(0);" target="_self" title="" onclick="changeOption(16)">学校获奖情况</a>
			<span class="bh">被驳回</span>
		</li>
		<li>
		    <i>17</i>
		    <a href="javascript:void(0);" target="_self" title="" onclick="changeOption(17)">学校等级评估</a>
			<span class="bh">被驳回</span>
		</li>
		<li>
		    <i>18</i>
		   	<a href="javascript:void(0);" target="_self" title="" onclick="changeOption(18)">个人获奖情况</a>
			<span class="bh">被驳回</span>
		</li>
		<li>
		    <i>19</i>
		   	<a href="javascript:void(0);" target="_self" title="" onclick="changeOption(19)">学校特色及改革</a>
			<span>未填写</span>
		</li>
		<li>
			<i>20</i>
			<a href="javascript:void(0);" target="_self" title="" onclick="changeOption(20)">社会责任</a>
			<span>未填写</span>
		</li>
		<li>
		    <i>21</i>
		    <a href="javascript:void(0);" target="_self" title="" onclick="changeOption(21)">责任事故</a>
			<span>未填写</span>
		</li>
		<li>
	    	<i>22</i>
	    	<a href="javascript:void(0);" target="_self" title="" onclick="changeOption(22)">处分</a>
			<span class="tx">已填写</span>
		</li>
		<li>
		   <i>23</i>
		   	<a href="javascript:void(0);" target="_self" title="" onclick="changeOption(23)">工作经历</a>
			<span class="tx">已填写</span>
		</li>
	</ul>
</div>
<!-- 切换步骤 e -->
<script type="text/javascript">
function changeOption(optionType){
	switch (optionType) {
    case 1:
    	$('.change-channel').close();
    	$("#mainDiv").load("/masterreview/headmaster/base_info.jsp", {limit: 25}, function(){
    		initOptionClick();
        });
        break;
   case 2:
       	$('.change-channel').close();
       	$("#mainDiv").load("/masterreview/headmaster/education.jsp", {limit: 25}, function(){
       		 initOptionClick();
        });
       break;
   case 3:
       	$('.change-channel').close();
       	$("#mainDiv").load("/masterreview/headmaster/professional_title.jsp", {limit: 25}, function(){
       		 initOptionClick();
        });
      break;
    case 4:
    	$('.change-channel').close();
    	$("#mainDiv").load("/masterreview/headmaster/work_experience.jsp", {limit: 25}, function(){
    		initOptionClick();
        });
        break;
    case 5:
    	$('.change-channel').close();
    	$("#mainDiv").load("/masterreview/headmaster/paper.jsp", {limit: 25}, function(){
    		initOptionClick();
        });
        break;
    case 6:
    	$('.change-channel').close();
    	$("#mainDiv").load("/masterreview/headmaster/subject.jsp", {limit: 25}, function(){
    		initOptionClick();
        });
        break;
    case 7:
    	$('.change-channel').close();
    	$("#mainDiv").load("/masterreview/headmaster/work_publish.jsp", {limit: 25}, function(){
    		initOptionClick();
        });
        break;
    case 8:
    	$('.change-channel').close();
    	$("#mainDiv").load("/masterreview/headmaster/study_train.jsp", {limit: 25}, function(){
    		initOptionClick();
        });
        break;
     case 9:
	    	$('.change-channel').close();
	    	$("#mainDiv").load("/masterreview/headmaster/manage_difficulty.jsp", {limit: 25}, function(){
	    		initOptionClick();
	        });
	  break;
     case 10:
	    	$('.change-channel').close();
	    	$("#mainDiv").load("/masterreview/headmaster/run_school.jsp", {limit: 25}, function(){
	    		initOptionClick();
	        });
	  break;  
     case 11:
	    	$('.change-channel').close();
	    	$("#mainDiv").load("/masterreview/headmaster/school_management.jsp", {limit: 25}, function(){
	    		initOptionClick();
	        });
	  break;  
     case 12:
	    	$('.change-channel').close();
	    	$("#mainDiv").load("/masterreview/headmaster/education_science.jsp", {limit: 25}, function(){
	    		initOptionClick();
	        });
	  break;  
     case 13:
	    	$('.change-channel').close();
	    	$("#mainDiv").load("/masterreview/headmaster/external_environment.jsp", {limit: 25}, function(){
	    		initOptionClick();
	        });
	  break;  
     case 14:
	    	$('.change-channel').close();
	    	$("#mainDiv").load("/masterreview/headmaster/student_development.jsp", {limit: 25}, function(){
	    		initOptionClick();
	        });
	  break;  
     case 15:
	    	$('.change-channel').close();
	    	$("#mainDiv").load("/masterreview/headmaster/teacher_development.jsp", {limit: 25}, function(){
	    		initOptionClick();
	        });
	  break; 
     case 16:
	    	$('.change-channel').close();
	    	$("#mainDiv").load("/masterreview/headmaster/school_award.jsp", {limit: 25}, function(){
	    		initOptionClick();
	        });
	  break;  
     case 17:
	    	$('.change-channel').close();
	    	$("#mainDiv").load("/masterreview/headmaster/grade_evaluate.jsp", {limit: 25}, function(){
	    		initOptionClick();
	        });
	  break;  
     case 18:
	    	$('.change-channel').close();
	    	$("#mainDiv").load("/masterreview/headmaster/personal_award.jsp", {limit: 25}, function(){
	    		initOptionClick();
	        });
	  break;  
     case 19:
	    	$('.change-channel').close();
	    	$("#mainDiv").load("/masterreview/headmaster/school_reform.jsp", {limit: 25}, function(){
	    		initOptionClick();
	        });
	  break;  
     case 20:
	    	$('.change-channel').close();
	    	$("#mainDiv").load("/masterreview/headmaster/social_duty.jsp", {limit: 25}, function(){
	    		initOptionClick();
	        });
	  break;  
     case 21:
	    	$('.change-channel').close();
	    	$("#mainDiv").load("/masterreview/headmaster/accident.jsp", {limit: 25}, function(){
	    		initOptionClick();
	        });
	  break;  
     case 22:
	    	$('.change-channel').close();
	    	$("#mainDiv").load("/masterreview/headmaster/punishment.jsp", {limit: 25}, function(){
	    		initOptionClick();
	        });
	  break;  
     case 23:
	    	$('.change-channel').close();
	    	$("#mainDiv").load("/masterreview/headmaster/work_history.jsp", {limit: 25}, function(){
	    		initOptionClick();
	        });
	  break;  
    default:
        break
	}
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



