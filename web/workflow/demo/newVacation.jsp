<%@ page contentType="text/html; charset=GBK" %>
<script>
/*
function beforeSubmit(formJsonData){
	var variableMap = new Brightcom.workflow.HashMap(); 
	variableMap.put("days",$("#days").val());
	variableMap.put("processTitle",formJsonData.processTitle);
	formJsonData.processParam = variableMap.toJsonObject();
	if(!formJsonData.processTitle){
		alert("流程标题不能为空！");
		return false;
	}
	if(!formJsonData.days){
		alert("请假天数不能为空！");
		return false;
	}
	if(!isPInt(formJsonData.days)){
		alert("请假天数必须为正整数！");
		return false;
	}
	if(!formJsonData.reason){
		alert("请假原因不能为空！");
		return false;
	}
}*/

function beforeSubmit(formJsonData){
	var variableMap = new Brightcom.workflow.HashMap(); 
	variableMap.put("days",$("#days").val());
	variableMap.put("processTitle",formJsonData.processTitle);
	formJsonData.processParam = variableMap.toJsonObject();

	formJsonData.startTime = '2015-12-28';
	formJsonData.endTime = '2015-12-29';
	formJsonData.businessKey = '22';
	formJsonData.workSchool = '1';
	formJsonData.workProfession = '2';
	formJsonData.workYear = '3';
	formJsonData.proveAttachMentId = '4';
}

function addEducation(){
	var bcReq = new BcRequest('headmaster','masterReviewAction','addEducation');
	//var taskId = paramObject.taskId;
	 
	//var taskId = $("#taskId").val();
	//var componentParam = {'taskId':taskId,'transferUserId':transferSelVal};
	bcReq.setExtraPs({
		"startTime":"2015-12-28",
		"endTime":'2015-12-29',
		"studySchool":"add",
		'studyProfession' :'ggg',
		'educationAttachmentId' :'gg',
		'degreeAttachmentId' :'hh'
		}
	);
	bcReq.setSuccFn(function(data,status){
		
	});
	bcReq.postData();
}

function addProfessionalTitle(){
	var bcReq = new BcRequest('headmaster','masterReviewAction','addProfessionalTitle');
	//var taskId = paramObject.taskId;
	 
	//var taskId = $("#taskId").val();
	//var componentParam = {'taskId':taskId,'transferUserId':transferSelVal};
	bcReq.setExtraPs({
		"startTime":"2015-12-28",
		"endTime":'2015-12-29',
		"businessKey":'cxc',
		"obtainTime":"2015-12-29",
		'obtainSchool' :'是的'
		}
	);
	bcReq.setSuccFn(function(data,status){
		
	});
	bcReq.postData();
}


function addPaper(){
	var bcReq = new BcRequest('headmaster','masterReviewAction','addPaper');
	//var taskId = paramObject.taskId;
	 
	//var taskId = $("#taskId").val();
	//var componentParam = {'taskId':taskId,'transferUserId':transferSelVal};
	bcReq.setExtraPs({
		"title":'2015-12-29',
		"businessKey":'cxc',
		"publish_time":'2015-12-29',
		"magazine_meet_name":'cxc',
		"paper_meet_name":'cxc',
		"paper_number":'cxc',
		"organizers":"2015-12-29",
		'organizers_level' :'是的',
		'personal_part' :'是的',
		'paper_attachment_id' :'是的',
		}
	);
	bcReq.setSuccFn(function(data,status){
		
	});
	bcReq.postData();
}

function addWorkPublish(){
	var bcReq = new BcRequest('headmaster','masterReviewAction','addWorkPublish');
	//var taskId = paramObject.taskId;
	 
	//var taskId = $("#taskId").val();
	//var componentParam = {'taskId':taskId,'transferUserId':transferSelVal};
	bcReq.setExtraPs({
		"businessKey":'2015-12-29',
		"title":'2015-12-29',
		"book_name":'cxc',
		"complete_way":'2015-12-29',
		"publish_time":'2015-12-29',
		"complete_chapter":'cxc',
		"complete_word":'cxc',
		"author_order":"2015-12-29",
		'coverAttachmentId' :'是的',
		'contentsAttachmentId' :'是的'
		}
	);
	bcReq.setSuccFn(function(data,status){
		
	});
	bcReq.postData();
}



function addSubject(){
	var bcReq = new BcRequest('headmaster','masterReviewAction','addSubject');
	//var taskId = paramObject.taskId;
	 
	//var taskId = $("#taskId").val();
	//var componentParam = {'taskId':taskId,'transferUserId':transferSelVal};
	bcReq.setExtraPs({
		"businessKey":'2015-12-29',
		"subject_name":'a',
		"subject_company":'1',
		"subject_level":'2015-12-29',
		"subject_responsibility":'b',
		"is_finish_subject":'c',
		"finish_result":"2",
		'finish_time' :'2015-12-29'
		}
	);
	bcReq.setSuccFn(function(data,status){
		
	});
	bcReq.postData();
}




function addPersonalAward(){
	var bcReq = new BcRequest('headmaster','masterReviewAction','addPersonalAward');
	//var taskId = paramObject.taskId;
	 
	//var taskId = $("#taskId").val();
	//var componentParam = {'taskId':taskId,'transferUserId':transferSelVal};
	bcReq.setExtraPs({
		"businessKey":'2015-12-29',
		"awards_name":'a',
		"awards_company":'1',
		"awards_level":'2',
		"awards_time":'2015-12-29',
		"awards_attachment_id":'c'
		}
	);
	bcReq.setSuccFn(function(data,status){
		
	});
	bcReq.postData();
}

function addSchoolAward(){
	var bcReq = new BcRequest('headmaster','masterReviewAction','addSchoolAward');
	//var taskId = paramObject.taskId;
	 
	//var taskId = $("#taskId").val();
	//var componentParam = {'taskId':taskId,'transferUserId':transferSelVal};
	bcReq.setExtraPs({
		"businessKey":'2015-12-29',
		"awards_name":'a',
		"work_school":'af',
		"awards_company":'1',
		"awards_level":'2',
		"awards_time":'2015-12-29',
		"awards_attachment_id":'c'
		}
	);
	bcReq.setSuccFn(function(data,status){
		
	});
	bcReq.postData();
}

function deleteWorkExperience(){
	var bcReq = new BcRequest('headmaster','masterReviewAction','deleteWorkExperience');
	bcReq.setExtraPs({
		  "id":'402881ed51e797c60151e7998fd50004'
		}
	);
	bcReq.setSuccFn(function(data,status){
		
	});
	bcReq.postData();
}

function deleteEducation(){
	var bcReq = new BcRequest('headmaster','masterReviewAction','deleteEducation');
	bcReq.setExtraPs({
		  "id":'402881ed51eb95300151eb957ff30007'
		}
	);
	bcReq.setSuccFn(function(data,status){
		
	});
	bcReq.postData();
}

function deleteProfessionalTitle(){
	var bcReq = new BcRequest('headmaster','masterReviewAction','deleteProfessionalTitle');
	bcReq.setExtraPs({
		  "id":'402881ed51eb95300151eb9f36b2000a'
		}
	);
	bcReq.setSuccFn(function(data,status){
		
	});
	bcReq.postData();
}

function deletePaper(){
	var bcReq = new BcRequest('headmaster','masterReviewAction','deletePaper');
	bcReq.setExtraPs({
		  "id":'402881ed51ebba7f0151ebcb96ac0009'
		}
	);
	bcReq.setSuccFn(function(data,status){
		
	});
	bcReq.postData();
}

function deleteWorkPublish(){
	var bcReq = new BcRequest('headmaster','masterReviewAction','deleteWorkPublish');
	bcReq.setExtraPs({
		  "id":'402881ed51ec6d540151ec701cd10008'
		}
	);
	bcReq.setSuccFn(function(data,status){
		
	});
	bcReq.postData();
}

function deleteSubject(){
	var bcReq = new BcRequest('headmaster','masterReviewAction','deleteSubject');
	bcReq.setExtraPs({
		  "id":'402881ed51ec7a890151ec7b25ba0004'
		}
	);
	bcReq.setSuccFn(function(data,status){
		
	});
	bcReq.postData();
}

function deletePersonalAward(){
	var bcReq = new BcRequest('headmaster','masterReviewAction','deletePersonalAward');
	bcReq.setExtraPs({
		  "id":'402881ed51ec94650151ec9625ec0005'
		}
	);
	bcReq.setSuccFn(function(data,status){
		
	});
	bcReq.postData();
}

function deleteSchoolAward(){
	var bcReq = new BcRequest('headmaster','masterReviewAction','deleteSchoolAward');
	bcReq.setExtraPs({
		  "id":'402881ed51ec94650151ec9a56140008'
		}
	);
	bcReq.setSuccFn(function(data,status){
		
	});
	bcReq.postData();
}

function updateWorkExperience(){
	var bcReq = new BcRequest('headmaster','masterReviewAction','updateWorkExperience');
	
	bcReq.setExtraPs({
		"startTime":"2015-12-28",
		"endTime":'2015-12-29',
		"businessKey":'2015-12-29',
		"workSchool":"add",
		'workProfession' :'ggg',
		'workYear' :'gg',
		'proveAttachMentId' :'hh'
		}
	);
	bcReq.setSuccFn(function(data,status){
		
	});
	bcReq.postData();
}

function updateEducation(){
	var bcReq = new BcRequest('headmaster','masterReviewAction','updateEducation');
	
	bcReq.setExtraPs({
		"startTime":"2015-12-28",
		"endTime":'2015-12-29',
		"studySchool":"的地方地方",
		'studyProfession' :'ggg',
		'educationAttachmentId' :'gg',
		'degreeAttachmentId' :'hh'
		}
	);
	bcReq.setSuccFn(function(data,status){
		
	});
	bcReq.postData();
}

function updateProfessionalTitle(){
	var bcReq = new BcRequest('headmaster','masterReviewAction','updateProfessionalTitle');
	
	bcReq.setExtraPs({
		"startTime":"2015-12-28",
		"endTime":'2015-12-29',
		"businessKey":'cxc',
		"obtainTime":"2015-12-29",
		'obtainSchool' :'是的'
		}
	);
	bcReq.setSuccFn(function(data,status){
		
	});
	bcReq.postData();
}

function updatePaper(){
	var bcReq = new BcRequest('headmaster','masterReviewAction','updatePaper');
	
	bcReq.setExtraPs({
		"title":'2015-12-29',
		"businessKey":'cxc',
		"publish_time":'2015-12-29',
		"magazine_meet_name":'cxc',
		"paper_meet_name":'cxc',
		"paper_number":'cxc',
		"organizers":"2015-12-29",
		'organizers_level' :'是的',
		'personal_part' :'是的',
		'paper_attachment_id' :'是的',
		}
	);
	bcReq.setSuccFn(function(data,status){
		
	});
	bcReq.postData();
}


function updateWorkPublish(){
	var bcReq = new BcRequest('headmaster','masterReviewAction','updateWorkPublish');
	
	bcReq.setExtraPs({
		"businessKey":'2015-12-29',
		"title":'2015-12-29',
		"book_name":'cxc',
		"complete_way":'3',
		"publish_time":'2015-12-29',
		"complete_chapter":'cxc',
		"complete_word":'cxc',
		"author_order":"2",
		'cover_attachment_id' :'是的ddd',
		'contents_attachment_id' :'是的'
		}
	);
	bcReq.setSuccFn(function(data,status){
		
	});
	bcReq.postData();
}

function updateSubject(){
	var bcReq = new BcRequest('headmaster','masterReviewAction','updateSubject');
	
	bcReq.setExtraPs({
		"businessKey":'2015-12-29',
		"subject_name":'阿萨德a',
		"subject_company":'1',
		"subject_level":'2015-12-29',
		"subject_responsibility":'b',
		"is_finish_subject":'c',
		"finish_result":"2",
		'finish_time' :'2015-12-29'
		}
	);
	bcReq.setSuccFn(function(data,status){
		
	});
	bcReq.postData();
}

function updatePersonalAward(){
	var bcReq = new BcRequest('headmaster','masterReviewAction','updatePersonalAward');
	
	bcReq.setExtraPs({
		"businessKey":'2015-12-29',
		"awards_name":'a',
		"awards_company":'1',
		"awards_level":'2',
		"awards_time":'2015-12-29',
		"awards_attachment_id":'阿萨'
		}
	);
	bcReq.setSuccFn(function(data,status){
		
	});
	bcReq.postData();
}

function updateSchoolAward(){
	$("#usertt").append("<a class=\"color3\"  href=\"#\" onclick=\"deleteReceiveFileAttachment(\'\\\',this);\" >删除</a>");

	/*
	var bcReq = new BcRequest('headmaster','masterReviewAction','updateSchoolAward');
	
	bcReq.setExtraPs({
		"businessKey":'2015-12-29',
		"awards_name":'a',
		"work_school":'af',
		"awards_company":'1',
		"awards_level":'2',
		"awards_time":'2015-12-29',
		"awards_attachment_id":'是滴是滴'
		}
	);
	bcReq.setSuccFn(function(data,status){
		
	});
	bcReq.postData();*/
}

$(document).ready(function(){	
	Brightcom.workflow.beforeSubmit = beforeSubmit;
});	

//正整数
function isPInt(str) {
    var g = /^[1-9]*[1-9][0-9]*$/;
    return g.test(str);
}
</script>
<input type="hidden" id="workflowSubmitAction" name="workflowSubmitAction" value="workflow@@vacationWorkflow@@startVacationProcess"> 
<!--<input type="hidden" id="workflowSubmitAction" name="workflowSubmitAction" value="headmaster@@masterReviewAction@@addWorkExperience">-->
   	         <div class="box2" id="tab1c1">
               	  <div class="box2_content">
               	  
<div class="user" id = "usertt">

	<div class="date" style="text-align:center;color:#8B4513;">2016-02-24 09:28:16</div>
	<div class="userPhoto">
	   
	   <img src="/images/yuexue2/message/photo.png" alt="" title="" class="img_01" style="width:60px;height:60px;border-radius:2em;" />
	   
	   <div style="text-align:center;color:#8B4513;">小于</div>
	</div>
	<div class="userMessage">
		
		<div class="borderTop m_YellowBorderTop"></div>
		<div class="userMessageContent m_Yellow">
        
			<div class="m_c"> <input type='hidden' id='comment_id' name='comment_id' value='4028ed815310d89a015310e296d2000d' /> <input type='hidden' id='read_mark' name='read_mark' value='N' />  <img class='pointer audioImg4028ed815310d89a015310e296d2000d' src='/images/yuexue/web_lianluo_sy01.png' onclick='javascript:audioStart('/upload/comment/2016/02/4028ed815310d89a015310e29155000b.mp3','4028ed815310d89a015310e296d2000d')'   &nbsp;&nbsp;<span id='msg4028ed815310d89a015310e296d2000d'></span>&nbsp;&nbsp;  <audio id='4028ed815310d89a015310e296d2000d'></audio>
			   
			</div>
		</div>
		
		<div class="borderBottom m_YellowBorderBottom"></div>
		
	</div>
</div>
               	  
<table width="500px" border="0" cellspacing="0" cellpadding="0" class="table3" >
  <tbody>
     <tr >
		<td><font color="red">*</font>流程标题</td>
		<td >
		   <input type="text" id="processTitle" name="processTitle" style="float:left;width:100%" >
		</td>
	  </tr>
	  <tr>
		<td ><font color="red">*</font>请假天数</td>
		<td >
		   <input type="text" id="days" name="days" style="float:left;width:100%" >
		</td>
	  </tr>
	  <tr >
		<td >
		 <font color="red">*</font>
		  请假原因
		</td>
		<td >
		   <textarea  class="form-field" id="reason" name="reason" rows="3" style="float:left;width:100%" ></textarea> 
		</td>
	  </tr>
	  </tbody>
</table>
   <div class="clear cH1"></div>
</div>
 <input type="button" id="" name="" value="单纯测" onclick="updateSchoolAward()">
   <!-- <div class="box2_bottom"></div> -->
</div>
