<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="org.jdom.*"%>
<%@ page import="java.util.List"%>
<%@ page import="cn.brightcom.jraf.web.*"%>
<%
  String userid = (String)session.getAttribute("userid");
  String username =(String)session.getAttribute("username");
  String usertype =(String)session.getAttribute("usertype");
 // String usertype =(String)session.getAttribute("usertype");
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
  
  
	//Element data = xmlDoc.getRootElement().getChild("Response").getChild("Data");
	//Element record = null==data?null:data.getChild("Record");
	List records = xmlDoc.getRootElement().getChild("Response").getChild("Data").getChildren("Record");
	String masterReviewVO ="";
	if (null != records) {
		Element record = (Element) records.get(0);
		masterReviewVO =  record.getChildTextTrim("masterReviewVO");
	}
%>

<script type="text/javascript" src="/masterreview/headmaster.js"></script>
<script type="text/javascript" src="/masterreview/js/bootstrap-tab.js"></script>
<script type="text/javascript" src="/js/swfupload.js"></script>
<script type="text/javascript" src="/js/upload/fileprogress.js"></script>
<script type="text/javascript" src="/js/upload/handlers.js"></script>
<script type="text/javascript" src="/js/jquery/jquery.form.js"></script>
<script type="text/javascript" src="/workflow/js/jquery.idTabs.min.js"></script>
<script src="/workflow/common/jspParamUtil.js"></script>
<script src="/platform/public/sysparam.js"></script>
<script type="text/javascript" src="/workflow/js/DatePicker/WdatePicker.js"></script>
<jsp:include page="/masterreview/arealeader/areaRenderTemplate.jsp" flush="true" >
 <jsp:param name="approveType" value="areaApprove" />
 <jsp:param name="basePath" value="<%=basePath%>" />
</jsp:include>


<script>
$.views.tags({
	timeContent:function(startTime,endTime){
	   return Brightcom.workflow.getDateStrByLong(startTime)+'-'+Brightcom.workflow.getDateStrByLong(endTime);
	},
	timeCovert:function(time){
		   return Brightcom.workflow.getDateStrByLong(time);
	},
})


$(document).ready(function(){	
	Brightcom.workflow.beforeSubmit = beforeSubmit;
	Brightcom.workflow.afterSubmit = afterSubmit;
	Headmaster.bulidMsaterView(<%=masterReviewVO%>,Headmaster.areaApprove);
});	

var refuseTypeSubmit = '';
var refuseTypeIdSubmit = '';

var isRefillFlag = '';
function beforeSubmit(formJsonData){
	var taskDefKey = Brightcom.workflow.getTaskDefKey();
	var processDefKey = Brightcom.workflow.getProcessDefKey();
	
	formJsonData.id=  $("#id").val();
	formJsonData.refuseType=  refuseTypeSubmit;
	formJsonData.refuseTypeId=  refuseTypeIdSubmit;
	if(isRefillFlag){
		formJsonData.base_info_approve_result = $("#base_info_approve_result").val();
		formJsonData.run_school_approve_result = $("#run_school_approve_result").val();
		formJsonData.school_management_approve_result = $("#school_management_approve_result").val();
		formJsonData.education_science_approve_result = $("#education_science_approve_result").val();
		formJsonData.external_environment_approve_result = $("#external_environment_approve_result").val();
		formJsonData.student_development_approve_result = $("#student_development_approve_result").val();
		formJsonData.teacher_development_approve_result = $("#teacher_development_approve_result").val();
		
		formJsonData.manage_difficulty_approve_result = $("#manage_difficulty_approve_result").val();
		formJsonData.manage_difficulty_ago_approve_result = $("#manage_difficulty_ago_approve_result").val();
		
		
		
		formJsonData.workHistory = getSubmitStrings('workHistory');
		formJsonData.education = getSubmitStrings('education');
		formJsonData.professionalTitle = getSubmitStrings('professionalTitle');
		formJsonData.workExperience = getSubmitStrings('workExperience');
		formJsonData.paper = getSubmitStrings('paper');
		formJsonData.workPublish = getSubmitStrings('workPublish');
		formJsonData.subject = getSubmitStrings('subject');
		formJsonData.personalAward = getSubmitStrings('personalAward');
		formJsonData.schoolAward = getSubmitStrings('schoolAward');
		formJsonData.gradeEvaluate = getSubmitStrings('gradeEvaluate');
		formJsonData.studyTrain = getSubmitStrings('studyTrain');
		formJsonData.schoolReform = getSubmitStrings('schoolReform');
		
		formJsonData.socialDuty = getSubmitStrings('socialDuty');
		formJsonData.accident = getSubmitStrings('accident');
		formJsonData.punishment = getSubmitStrings('punishment');
	}
	formJsonData.isRefillFlag = isRefillFlag
	//return false;
	
	//var variableMap = new Brightcom.workflow.HashMap(); 
//	variableMap.put("areaHeader",'4028814d5499edd2015499f003ca0006');
//	formJsonData.processParam =variableMap.toJsonObject();
	/*
	var variableMap = new Brightcom.workflow.HashMap(); 
	var ss = [];
	ss.push('4028814d5499edd2015499efc4670004');
	variableMap.put("multiUser",ss);
	formJsonData.processParam =variableMap.toJsonObject();*/
}

function getSubmitStrings(checkbox_type){
	//var workHistorys =  $(":checkbox[id='"+checkbox_type+"_check'][checked]");
	var submitArray = [];
	$(":checkbox[id='"+checkbox_type+"_check'][checked]").each(function(i){
			var workExperienceObject = {
					"id":$(this).val(),
					approve_result:'0'
					
			}
		 submitArray.push(workExperienceObject);
	 });
	
	return submitArray.length>0? JSON.stringify(submitArray): "";
	
}


function afterSubmit(){
	 window.location.href = "/masterreview/arealeader/arealeaderIndex.jsp";
}

function submitAreaTask(isrefill){
	if(isrefill){
		isRefillFlag = "1";
	}
	var submit_flow = isrefill? "flow8" : "flow2";
	Brightcom.workflow.completeButtonTask(submit_flow,Brightcom.workflow.taskType);
}



function rollBackOpen(refuseType,refuseTypeId){
	 refuseType = refuseType;
	 refuseTypeId = refuseTypeId;
	 $.fancybox.open({href:"/masterreview/arealeader/rollBackExecute.jsp?refuseType="+refuseType+"&refuseTypeId="+refuseTypeId,type:'iframe',width:327,height:298,padding:0,margin:0,closeBtn:true,iframe:{scrolling:'no',preload:false}});
}

/*
 * 弹出退回窗口的执行方法
 */
function rollBackExecute(refuseType,refuseTypeId){
	debugger
	refuseTypeSubmit = refuseType;
	refuseTypeIdSubmit = refuseTypeId;
	isRefillFlag= "1";
	Brightcom.workflow.completeButtonTask('flow8',Brightcom.workflow.taskType);
	$.fancybox.close();
}


function showOriginalImg(obj){
	//var imgObject = basePath+filePath;
	var theImage = new Image();
	theImage.src = obj.src;
	var sourceWidth = theImage.width+100;
	var sourceHeight = theImage.height+100;
	
	var imgSrc = obj.src;
	$.fancybox.open({href:"/masterreview/public/showImage.jsp?imgSrc="+imgSrc,type:'iframe',width:sourceWidth,height:sourceHeight,padding:0,margin:0,closeBtn:false,iframe:{scrolling:'auto',preload:false}});
}

function setRollCheckVal(obj){
	if($(obj).attr('checked')){
		$(obj).val('0')
	}else{
		$(obj).val('1')
	}
}

function setAllCheckVal(obj){
	//if($(obj).attr('checked')){
		$("input[type=checkbox]").not(obj ).click();
	//}else{
	//	$(obj).val('1')
	//}
}
</script>
<input type="hidden" id="workflowSubmitAction" name="workflowSubmitAction" value="headmaster@@masterReviewAction@@completeAreaCadresTask">
<input type="hidden" id="id" name="id" value="">
<input type="hidden" id="headerMasterId" name="headerMasterId" value="">

<input type="hidden" id="workHistoryRowNum" name="workHistoryRowNum" value="">
<input type="hidden" id="educationRowNum" name="educationRowNum" value="">
<input type="hidden" id="professionalTitleRowNum" name="professionalTitleRowNum" value="">
<input type="hidden" id="workExperienceRowNum" name="workExperienceRowNum" value="">
<input type="hidden" id="paperTitleRowNum" name="paperTitleRowNum" value="">
<input type="hidden" id="subjectRowNum" name="subjectRowNum" value="">
<input type="hidden" id="workPublishRowNum" name="workPublishRowNum" value="">
<input type="hidden" id="studyTrainRowNum" name="studyTrainRowNum" value="">
<input type="hidden" id="schoolAwardRowNum" name="schoolAwardRowNum" value="">
<input type="hidden" id="gradeEvaluateRowNum" name="gradeEvaluateRowNum" value="">

<input type="hidden" id="personalAwardRowNum" name="personalAwardRowNum" value="">

<input type="hidden" id="schoolReformRowNum" name="schoolReformRowNum" value="">
<input type="hidden" id="socialDutyRowNum" name="socialDutyRowNum" value="">
<input type="hidden" id="accidentRowNum" name="accidentRowNum" value="">
<input type="hidden" id="punishmentRowNum" name="punishmentRowNum" value="">

	
<div class="container">
		<div class="xiao_bg">
			<div class="tit1">申报人信息</div>
			<div class="tabletit">学校所属教育部门审核</div>
			<table cellpadding="0" cellspacing="1" border="0" class="table">
			    <tbody>
                    <tr>
			            <th width="88%" colspan="6">基础信息</th>
			            <th width="12%">
			              <input type='checkbox'  id="all_approve_result" name='all_approve_result'  onclick="setAllCheckVal(this)" />全选
			            <!--  <a class="allshu" href="javascript:void(0);" onclick="rollBackOpen('allRefuse')">退回</a>--> 
			            
			            </th>
			        </tr>
			        <tr>
			        	<td style="180px;" rowspan="3" colspan="2">
			        	   <img id="person_img_object" style="width:98px;height: 114px"  >
			        	</td>
			        	<td class="black text-right">姓名：</td>
			        	<td style="width:80px;"> <label id="headerMasterName" name="headerMasterName"></label> </td>
			        	<td class="black text-right">身份证号：</td>
			        	<td style="width:95px"><label id="identitycard" name="identitycard"></label> </td>
			        	<td class="tablelink" rowspan="7">
			        	  <input type='checkbox' value='1' id="base_info_approve_result" name='base_info_approve_result'  onclick="setRollCheckVal(this)" />
			        	  <!--  <a class="shu" href="javascript:void(0);" onclick="rollBackOpen('baseInfo')">退回</a>-->
			        	</td>
			        </tr>
			        <tr>
			        	<td class="black text-right">性别：</td>
			        	<td><label id="usersex" name="usersex"></label> </td>
			        	<td class="black text-right">手机号：</td>
			        	<td><label id="mobile" name="mobile"></label> </td>
		        	</tr>
			        <tr>
			        	<td class="black text-right">出生年月：</td>
			        	<td><label id="birth_date" name="birth_date"></label> </td>
			        	<td class="black text-right">所在学校：</td>
			        	<td><label id="school_name" name="school_name"></label> </td>
		        	</tr>
			        <tr>
			        	<td class="black text-right">户籍：</td>
			        	<td colspan="1" style="width:80px;"><label id="census_register" name="census_register"></label> </td>
			        	<td class="black text-right">民族：</td>
			        	<td colspan="1"><label id="nation" name="nation"></label> </td>
			        	<td class="black text-right">学校类型：</td>
			        	<td><label id="school_class" name="school_class"></label> </td>
		        	</tr>
		        	 <tr>
			        	<td class="black text-right">政治面貌：</td>
			        	<td colspan="1"><label id="politics_status" name="politics_status"></label> </td>
			        	<td class="black text-right">教龄：</td>
			        	<td colspan="1"><label id="teach_age" name="teach_age"></label> </td>
			        	 <td class="black text-right">现任职务：</td>
			        	<td colspan="1"><label id="present_occupation" name="present_occupation"></label> </td>
		        	</tr> 	
		        	<tr>
			        	<td class="black text-right">学段：</td>
			        	<td colspan="1"><label id="phasestudy" name="phasestudy"></label> </td>
			        	<td class="black text-right">邮箱：</td>
			        	<td colspan="1"><label id="email" name="email"></label> </td>
			  	       <td class="black text-right">现任专业技术职务：</td>
			        	<td colspan="1"><label id="present_major_occupation" name="present_major_occupation"></label> </td>
		        	</tr> 	
		        	<tr>
			            <td class="black text-right">籍贯：</td>
			        	<td colspan="1"><label id="native_place" name="native_place"></label> </td>
			            <td class="black text-right">参加工作时间：</td>
			        	<td><label id="join_work_time" name="join_work_time"></label> </td>
			        	 <td class="black text-right">参加教育工作时间：</td>
			        	<td><label id="join_educate_work_time" name="join_educate_work_time"></label> </td>
		        	</tr> 	
			   </tbody>
			</table>
			
			<div class="tabletit">非指标选项</div>
			  <div id="workHistoryDiv" ></div>
			
		    <div class="tabletit">个人素养</div>
		         <div id="educationDiv" ></div>
		         <div id="professionalTitleDiv" ></div>
			     <div id="workExperienceDiv" ></div>
		    	 <div id="paperDiv"></div>
		    	  <div id="subjectDiv"></div>
		    	 <div id="bookPublishDiv"></div>
		    	 <div id="studyTrainDiv"></div>
			 
		    
			<div class="tabletit">学校规模及类型</div>
			<table cellpadding="0" cellspacing="1" border="0" class="table text-center">
			    <tbody>
                    <tr>
			            <th width="88%" colspan="6">近八年任职学校</th>
			            <th width="12%"></th>
			        </tr>
			        <tr>
			        	<td class="black" colspan="3">学校名称：</td>
			        	<td><label id="schoolNameSpaceAgo" name="schoolNameSpaceAgo"></label> </td>
			        	<td class="black">学校类型：</td>
			        	<td><label id="schoolTypeAgo" name="schoolTypeAgo"></label> </td>
			        	<td class="tablelink" rowspan="2">
			        	  <input type='checkbox' value='1' id="manage_difficulty_ago_approve_result" name='manage_difficulty_ago_approve_result' onclick='setRollCheckVal(this)'/>
			        	<!--   <a class="shu" href="javascript:void(0);" onclick="rollBackOpen('managementDifficultyAgo')">退回</a>--> 
			        	</td>
			        </tr>
			         <tr>
			        	<td class="black">校区数量：</td>
			        	<td><label id="schoolCountAgo" name="schoolCountAgo"></label> </td>
			        	<td class="black">班级数量：</td>
			        	<td><label id="studentNumberAgo" name="studentNumberAgo"></label> </td>
			        	<td class="black">证明材料：</td>
			        	<td><span id="manageDifficultyAgoSpan"></span> </td>
			        </tr>
			   </tbody>
			</table>
			<table cellpadding="0" cellspacing="1" border="0" class="table text-center">
			    <tbody>
                    <tr>
			            <th width="88%" colspan="6">现任职学校</th>
			            <th width="12%"></th>
			        </tr>
			        <tr>
			        	<td class="black" colspan="3">学校名称：</td>
			        	<td><label id="schoolNameSpace" name="schoolNameSpace"></label> </td>
			        	<td class="black">学校类型：</td>
			        	<td><label id="schoolType" name="schoolType"></label> </td>
			        	<td class="tablelink" rowspan="2">
			        	  <input type='checkbox' value='1' id="manage_difficulty_approve_result" name='manage_difficulty_approve_result' onclick='setRollCheckVal(this)' />
			            <!--  	<a class="shu" href="javascript:void(0);" onclick="rollBackOpen('managementDifficulty')">退回</a>-->
			        	</td>
			        </tr>
			        <tr>
			        	<td class="black">校区数量：</td>
			        	<td><label id="schoolCount" name="schoolCount"></label> </td>
			        	<td class="black">班级数量：</td>
			        	<td><label id="studentNumber" name="studentNumber"></label> </td>
			        	<td class="black">证明材料：</td>
			        	<td><span id="manageDifficultySpan"></span></td>
			        </tr>
			   </tbody>
			</table>
			
			
           <div class="tabletit">专业能力</div>
              <div id="run_school_div"><!-- 办学思想 -->
				<table cellpadding="0" cellspacing="1" border="0" class="table text-center">
				    <tbody>
	                    <tr>
				            <th width="88%" colspan="6">办学思想</th>
				            <th width="12%"></th>
				        </tr>
				        <tr>
				        	<td  colspan="6"><label id="run_school_label" name="run_school_label"></label></td>
				        	<td rowspan="2" class="tablelink" >
				        	  <input type='checkbox' value='1' id="run_school_approve_result" name='run_school_approve_result' onclick='setRollCheckVal(this)'/>
				        	 <!--  <a class="shu" href="javascript:void(0);" onclick="rollBackOpen('run_school')">退回</a>-->
				        	</td>
				        </tr>
				        <tr>
				        	<td  colspan="5">证明材料：</td>
				        	<td  class="tablelink" >
                               <a id="run_school_attachment_id" class="cha" href="#"> 点击查看</a>
				        	</td>
				        </tr>
				   </tbody>
				</table>              
              </div>
              
              <div id="school_management_div"><!--学校管理  -->
              	<table cellpadding="0" cellspacing="1" border="0" class="table text-center">
				    <tbody>
	                    <tr>
				            <th width="88%" colspan="6">学校管理</th>
				            <th width="12%"></th>
				        </tr>
				        <tr>
				        	<td  colspan="6"><label id="school_management_label" name="school_management_label"></label></td>
				        	<td rowspan="2"  class="tablelink" >
				        	  <input type='checkbox' value='1' id="school_management_approve_result" name='school_management_approve_result' onclick='setRollCheckVal(this)'/>
				        	    <!--  <a class="shu" href="javascript:void(0);" onclick="rollBackOpen('school_management')">退回</a>-->
				        	</td>
				        </tr>
				         <tr>
				        	<td  colspan="5">证明材料：</td>
				        	<td  class="tablelink" >
                               <a id="school_management_attachment_id" class="cha" href="#"> 点击查看</a>
				        	</td>
				        </tr>
				   </tbody>
				 </table> 
              </div>
              
              <div id="education_science_div"><!--  教育教学-->
              	<table cellpadding="0" cellspacing="1" border="0" class="table text-center">
				    <tbody>
	                    <tr>
				            <th width="88%" colspan="6">教育教学</th>
				            <th width="12%"></th>
				        </tr>
				        <tr>
				        	<td  colspan="6"><label id="education_science_label" name="education_science_label"></label></td>
				        	<td rowspan="2"  class="tablelink" >
				        	  <input type='checkbox' value='1' id="education_science_approve_result" name='education_science_approve_result' onclick='setRollCheckVal(this)'/>
				        	    <!--  <a class="shu" href="javascript:void(0);" onclick="rollBackOpen('education_science')">退回</a>-->
				        	</td>
				        </tr>
				        <tr>
				        	<td  colspan="5">证明材料：</td>
				        	<td  class="tablelink" >
                               <a id="education_science_attachment_id" class="cha" href="#"> 点击查看</a>
				        	</td>
				        </tr>
				   </tbody>
				</table> 
              </div>
              
              <div id="external_environment_div"><!-- 外部环境 -->
              	 <table cellpadding="0" cellspacing="1" border="0" class="table text-center">
				    <tbody>
	                    <tr>
				            <th width="88%" colspan="6">外部环境</th>
				            <th width="12%"></th>
				        </tr>
				        <tr>
				        	<td  colspan="6"><label id="external_environment_label" name="external_environment_label"></label></td>
				        	<td rowspan="2" class="tablelink" >
				        	  <input type='checkbox' value='1' id="external_environment_approve_result" name='external_environment_approve_result' onclick='setRollCheckVal(this)'/>
				        	   <!--  <a class="shu" href="javascript:void(0);" onclick="rollBackOpen('external_environment')">退回</a>-->
				        	</td>
				        </tr>
				        
				        <tr>
				        	<td  colspan="5">证明材料：</td>
				        	<td  class="tablelink" >
                               <a id="external_environment_attachment_id" class="cha" href="#"> 点击查看</a>
				        	</td>
				        </tr>
				   </tbody>
				 </table> 
              </div>
			
			   
			<div class="tabletit">办学成效</div>
			   <div id="student_development_div">
			      <table cellpadding="0" cellspacing="1" border="0" class="table text-center">
				    <tbody>
	                    <tr>
				            <th width="88%" colspan="6">学生发展</th>
				            <th width="12%"></th>
				        </tr>
				        <tr>
				        	<td  colspan="6"><label id="student_development_label" name="student_development_label"></label></td>
				        	<td rowspan="2" class="tablelink" >
				        	  <input type='checkbox' value='1' id="student_development_approve_result" name='student_development_approve_result'onclick='setRollCheckVal(this)' />
				        	    <!--  <a class="shu" href="javascript:void(0);" onclick="rollBackOpen('student_development')">退回</a>-->
				        	</td>
				        </tr>
				        <tr>
				        	<td  colspan="5">证明材料：</td>
				        	<td  class="tablelink" >
                               <a id="student_development_attachment_id" class="cha" href="#"> 点击查看</a>
				        	</td>
				        </tr>
				    </tbody>
				  </table> 
			   </div>
			   <div id="teacher_development_div">
			      <table cellpadding="0" cellspacing="1" border="0" class="table text-center">
				    <tbody>
	                    <tr>
				            <th width="88%" colspan="6">教师发展</th>
				            <th width="12%"></th>
				        </tr>
				        <tr>
				        	<td  colspan="6"><label id="teacher_development_label" name="teacher_development_label"></label></td>
				        	<td rowspan="2" class="tablelink" >
				        	  <input type='checkbox' value='1' id="teacher_development_approve_result" name='teacher_development_approve_result' onclick='setRollCheckVal(this)'/>
				        	   <!--  <a class="shu" href="javascript:void(0);" onclick="rollBackOpen('teacher_development')">退回</a>-->
				        	</td>
				        </tr>
				        <tr>
				        	<td  colspan="5">证明材料：</td>
				        	<td  class="tablelink" >
                               <a id="teacher_development_attachment_id" class="cha" href="#"> 点击查看</a>
				        	</td>
				        </tr>
				   </tbody>
				  </table> 
			   </div>
			   <div id="schoolAwardDiv"></div>
			   <div id="gradeEvaluateDiv"></div>
			   
			<div class="tabletit">加分项</div>
			     <div id="personalAwardDiv"></div>
			     <div id="schoolReformDiv"></div>
			     <div id="socialDutyDiv"></div>
			<div class="tabletit">减分项</div>
			     <div id="accidentDiv"></div>
			     <div id="punishmentDiv"></div>
	
			<div id='opinionDiv' >
				<table cellpadding='0' cellspacing='1' border='0' class='table text-center'>  <tbody>
		           <tr>
		              <th colspan='8'>学校所属教育主管部门审核意见：</th>
		           </tr>
		           <tr>
		              <input type="hidden" id="opinionAttachId" name="opinionAttachId" value=""/>
		              <td  colspan='2'> <span id="opinionButtonSpan"></span></td>
		              <td class='red' colspan='6'> <div id="opinionDisplayDiv"></div> </td>
		           </tr>
		          <tbody> 
		       </table>
			</div>
			
		   <div id='signDiv' >
				<table cellpadding='0' cellspacing='1' border='0' class='table text-center'>  <tbody>
		           <tr>
		              <th colspan='8'>审核人签名档：</th>
		           </tr>
		           <tr>
		              <input type="hidden" id="signAttachId" name="signAttachId" value=""/>
		              <td  colspan='2'> <span id="signButtonSpan"></span></td>
		              <td class='red' colspan='6'> <div id="signDisplayDiv"></div> </td>
		           </tr>
		          <tbody> 
		       </table>
			</div>
				
			<div class="tabletit">
			  <a href="javascript:void(0);" onclick="submitAreaTask()" style="background-color:#44b549">提交</a>&nbsp;&nbsp;&nbsp;
			  <a href="javascript:void(0);" onclick="rollBackOpen()" style="background-color:#44b549">驳回</a>
			</div>
		</div>
	</div>

