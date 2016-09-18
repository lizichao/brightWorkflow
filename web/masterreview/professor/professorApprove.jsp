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
  
  
	List records = xmlDoc.getRootElement().getChild("Response").getChild("Data").getChildren("Record");
	String masterReviewVO ="";
	String apply_headmaster ="";
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
<jsp:include page="/masterreview/public/renderTemplate.jsp" flush="true" >
 <jsp:param name="approveType" value="professorApprove" />
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

var masterReviewVOVal = <%=masterReviewVO%>
$(document).ready(function(){	
	Brightcom.workflow.beforeSubmit = beforeSubmit;
	Brightcom.workflow.afterSubmit = afterSubmit;
	Headmaster.bulidMsaterView(<%=masterReviewVO%>,Headmaster.professorApprove);
	
	findGradeResult(masterReviewVOVal.headerMasterId);
});	

function beforeSubmit(formJsonData){
	var taskDefKey = Brightcom.workflow.getTaskDefKey();
	var processDefKey = Brightcom.workflow.getProcessDefKey();
	
	formJsonData.id=  $("#id").val();
	formJsonData.headerMasterId=  $("#headerMasterId").val();
	formJsonData.report_grade=  $("#report_grade").val();
	formJsonData.sumGrade=  $("#sumGrade").val()||'0';
	
	var variableMap = new Brightcom.workflow.HashMap(); 
	variableMap.put("isNeedReply",'0');
    formJsonData.processParam =variableMap.toJsonObject();
}

function afterSubmit(){
	 window.location.href = "/masterreview/professor/professorIndex.jsp";
}

function submitProfessorTask(){
	Brightcom.workflow.completeButtonTask('flow14',Brightcom.workflow.taskType);
}

function findGradeResult(headerMasterId){
	var bcReq = new BcRequest('headmaster','personalLeaderAction','findGradeResult');
	bcReq.setExtraPs({
		 "apply_headmaster":headerMasterId,
		 'processInstanceId':'<%=processInstanceId%>'
		});
	bcReq.setSuccFn(function(data,status){
		var result = data.Data[0];
		
		 $("#baseinfo_grade").val(result.baseinfo_grade);
		$("#work_experience_grade").text(result.work_experience_grade);
		$("#education_grade").text(result.education_grade);
		 $("#professional_title_grade").val(result.professional_title_grade);
		  $("#management_difficulty_grade").val(result.management_difficulty_grade);
		  $("#management_difficulty_grade_ago").val(result.management_difficulty_grade_ago);
		$("#paper_grade").val(result.paper_grade);
		  $("#work_publish_grade").val(result.work_publish_grade);
		 $("#subject_grade").val(result.subject_grade);
		 $("#personal_award_grade").val(result.personal_award_grade);
		  $("#school_award_grade").val(result.school_award_grade);
		  $("#school_award_grade").val(result.school_award_grade);
	});
	bcReq.postData();
}


function countSumGrade(){
	var baseinfo_grade = $("#baseinfo_grade").val() ||'0';
	var work_experience_grade =$("#work_experience_grade").val()||'0';
	var education_grade =$("#education_grade").val()||'0';
	var professional_title_grade =$("#professional_title_grade").val()||'0';
	var management_difficulty_grade =  $("#management_difficulty_grade").val()||'0';
	var management_difficulty_grade_ago = $("#management_difficulty_grade_ago").val()||'0';
	var paper_grade =$("#paper_grade").val()||'0';
	var work_publish_grade = $("#work_publish_grade").val()||'0';
	var subject_grade =$("#subject_grade").val()||'0';
	var personal_award_grade =  $("#personal_award_grade").val()||'0';
	var school_award_grade = $("#school_award_grade").val()||'0';
	
	var sumGrade = parseInt(baseinfo_grade)+parseInt(work_experience_grade)+parseInt(education_grade)+parseInt(professional_title_grade)
	+parseInt(management_difficulty_grade)+parseInt(management_difficulty_grade_ago)+parseInt(paper_grade+work_publish_grade)+parseInt(subject_grade)
	+parseInt(personal_award_grade)+parseInt(school_award_grade);
	
	$("#sumGrade").text(sumGrade)
}

</script>
<input type="hidden" id="workflowSubmitAction" name="workflowSubmitAction" value="headmaster@@masterReviewAction@@completeProfessorApproveTask">
<input type="hidden" id="id" name="id" value="">
<input type="hidden" id="headerMasterId" name="headerMasterId" value="">

	
<div class="container">
		<div class="xiao_bg">
			<div class="tit1">�걨����Ϣ</div>
			<div class="tabletit">ר��С�����</div>
			<table cellpadding="0" cellspacing="1" border="0" class="table">
			    <tbody>
                    <tr>
			            <th width="100%" colspan="7">������Ϣ</th>
			        </tr>
			        <tr>
			        	<td style="180px;" rowspan="3" colspan="2">
			        	   <img id="person_img_object" style="width:98px;height: 114px"  >
			        	</td>
			        	<td class="black text-right">������</td>
			        	<td style="width:80px;"> <label id="headerMasterName" name="headerMasterName"></label> </td>
			        	<td class="black text-right">���֤�ţ�</td>
			        	<td style="width:95px"><label id="identitycard" name="identitycard"></label> </td>
			        </tr>
			        <tr>
			        	<td class="black text-right">�Ա�</td>
			        	<td><label id="usersex" name="usersex"></label> </td>
			        	<td class="black text-right">�ֻ��ţ�</td>
			        	<td><label id="mobile" name="mobile"></label> </td>
		        	</tr>
			        <tr>
			        	<td class="black text-right">�������£�</td>
			        	<td><label id="birth_date" name="birth_date"></label> </td>
			        	<td class="black text-right">����ѧУ��</td>
			        	<td><label id="school_name" name="school_name"></label> </td>
		        	</tr>
			        <tr>
			        	<td class="black text-right">������</td>
			        	<td colspan="1" style="width:80px;"><label id="census_register" name="census_register"></label> </td>
			        	<td class="black text-right">���壺</td>
			        	<td colspan="1"><label id="nation" name="nation"></label> </td>
			        	<td class="black text-right">ѧУ���ͣ�</td>
			        	<td><label id="school_class" name="school_class"></label> </td>
		        	</tr>
		        	 <tr>
			        	<td class="black text-right">������ò��</td>
			        	<td colspan="1"><label id="politics_status" name="politics_status"></label> </td>
			        	<td class="black text-right">���䣺</td>
			        	<td colspan="1"><label id="teach_age" name="teach_age"></label> </td>
			        	 <td class="black text-right">����ְ��</td>
			        	<td colspan="1"><label id="presentOccupation" name="presentOccupation"></label> </td>
		        	</tr> 	
		        	<tr>
			        	<td class="black text-right">ѧ�Σ�</td>
			        	<td colspan="1"><label id="phasestudy" name="phasestudy"></label> </td>
			        	<td class="black text-right">���䣺</td>
			        	<td colspan="1"><label id="email" name="email"></label> </td>
			  	       <td class="black text-right">����רҵ����ְ��</td>
			        	<td colspan="1"><label id="present_major_occupation" name="present_major_occupation"></label> </td>
		        	</tr> 	
		        	<tr>
			            <td class="black text-right">���᣺</td>
			        	<td colspan="1"><label id="native_place" name="native_place"></label> </td>
			            <td class="black text-right">�μӹ���ʱ�䣺</td>
			        	<td><label id="join_work_time" name="join_work_time"></label> </td>
			        	 <td class="black text-right">�μӽ�������ʱ�䣺</td>
			        	<td><label id="join_educate_work_time" name="join_educate_work_time"></label> </td>
		        	</tr> 	
			   </tbody>
			</table>
			
			<div class="tabletit">��ָ��ѡ��</div>
			  <div id="workHistoryDiv" ></div>

			<div class="tabletit">��������</div>
				 <div id="educationDiv" ></div>
		         <div id="professionalTitleDiv" ></div>
			     <div id="workExperienceDiv" ></div>
		    	 <div id="paperDiv"></div>
		    	 <div id="subjectDiv"></div>
		    	 <div id="bookPublishDiv"></div>
		    	 <div id="studyTrainDiv"></div>

			<div class="tabletit">ѧУ��ģ������</div>
			<table cellpadding="0" cellspacing="1" border="0" class="table text-center">
			    <tbody>
			        <tr>
			            <th width="80%" colspan="6">��������ְѧУ</th>
			            <th width="20%">�÷�</th>
			        </tr>
			        <tr>
			        	<td class="black" colspan="3">ѧУ���ƣ�</td>
			        	<td><label id="schoolNameSpaceAgo" name="schoolNameSpaceAgo"></label> </td>
			        	<td class="black">ѧУ���ͣ�</td>
			        	<td><label id="schoolTypeAgo" name="schoolTypeAgo"></label> </td>
			        	<td class="tablelink" rowspan="2">
			        <!-- 	    <input id="management_difficulty_grade_ago" class="shuru" type="text" value="" placeholder="����" onblur="countSumGrade()">
			        	    <img class="qa" src="/masterreview/images/icon_qa.png" alt=""> -->
			        	    <label id="management_difficulty_grade_ago" name="management_difficulty_grade_ago"></label>
			        	</td>
			        </tr>
			         <tr>
			        	<td class="black">У��������</td>
			        	<td><label id="schoolCountAgo" name="schoolCountAgo"></label> </td>
			        	<td class="black">�༶������</td>
			        	<td><label id="studentNumberAgo" name="studentNumberAgo"></label> </td>
			        	<td class="black">֤�����ϣ�</td>
			        	<td><span id="manageDifficultyAgoSpan"></span> </td>
			        </tr>
			   </tbody>
			</table>
			<table cellpadding="0" cellspacing="1" border="0" class="table text-center">
			    <tbody>
                   <tr>
			            <th width="80%" colspan="6">����ְѧУ</th>
			            <th width="20%">�÷�</th>
			        </tr>
			        <tr>
			        	<td class="black" colspan="3">ѧУ���ƣ�</td>
			        	<td><label id="schoolNameSpace" name="schoolNameSpace"></label> </td>
			        	<td class="black">ѧУ���ͣ�</td>
			        	<td><label id="schoolType" name="schoolType"></label> </td>
			        	<td class="tablelink" rowspan="2">
			        	   <label id="management_difficulty_grade" name="management_difficulty_grade"></label>
			        	  	  <!--<input id="management_difficulty_grade" class="shuru" type="text" value="" placeholder="����" onblur="countSumGrade()">
			        	    <img class="qa" src="/masterreview/images/icon_qa.png" alt="">-->
			        	</td>
			        </tr>
			        <tr>
			        	<td class="black">У��������</td>
			        	<td><label id="schoolCount" name="schoolCount"></label> </td>
			        	<td class="black">�༶������</td>
			        	<td><label id="studentNumber" name="studentNumber"></label> </td>
			        	<td class="black">֤�����ϣ�</td>
			        	<td><span id="manageDifficultySpan"></span></td>
			        </tr>
			   </tbody>
			</table>
			
			
            <div class="tabletit">רҵ����</div>
            <div id="run_school_div"><!-- ��ѧ˼�� -->
				<table cellpadding="0" cellspacing="1" border="0" class="table text-center">
				    <tbody>
	                    <tr>
				            <th width="80%" colspan="6">��ѧ˼��</th>
				            <th width="20%"></th>
				        </tr>
				        <tr>
				        	<td  colspan="6"><label id="run_school_label" name="run_school_label"></label></td>
				        	<td class="tablelink" >
				        		<input id="run_school_grade" class="shuru" type="text" value="" placeholder="����" onblur="countSumGrade()">
			        	        <img class="qa" src="/masterreview/images/icon_qa.png" alt="">
				        	</td>
				        </tr>
				   </tbody>
				</table>              
              </div>
              
               <div id="school_management_div"><!--ѧУ����  -->
              	<table cellpadding="0" cellspacing="1" border="0" class="table text-center">
				    <tbody>
	                    <tr>
				            <th width="80%" colspan="6">ѧУ����</th>
				            <th width="20%"></th>
				        </tr>
				        <tr>
				        	<td  colspan="6"><label id="school_management_label" name="school_management_label"></label></td>
				        	<td class="tablelink" >
				        		<input id="school_management_grade" class="shuru" type="text" value="" placeholder="����" onblur="countSumGrade()">
			        	        <img class="qa" src="/masterreview/images/icon_qa.png" alt="">
				        	</td>
				        </tr>
				   </tbody>
				 </table> 
              </div>
              
              <div id="education_science_div"><!--  ������ѧ-->
              	<table cellpadding="0" cellspacing="1" border="0" class="table text-center">
				    <tbody>
	                    <tr>
				            <th width="80%" colspan="6">������ѧ</th>
				            <th width="20%"></th>
				        </tr>
				        <tr>
				        	<td  colspan="6"><label id="education_science_label" name="education_science_label"></label></td>
				        	<td class="tablelink" >
				        		<input id="education_science_grade" class="shuru" type="text" value="" placeholder="����" onblur="countSumGrade()">
			        	        <img class="qa" src="/masterreview/images/icon_qa.png" alt="">
				        	</td>
				        </tr>
				   </tbody>
				</table> 
              </div>
              
              <div id="external_environment_div"><!-- �ⲿ���� -->
              	 <table cellpadding="0" cellspacing="1" border="0" class="table text-center">
				    <tbody>
	                    <tr>
				            <th width="80%" colspan="6">�ⲿ����</th>
				            <th width="20%"></th>
				        </tr>
				        <tr>
				        	<td  colspan="6"><label id="external_environment_label" name="external_environment_label"></label></td>
	                        <td class="tablelink" >
				        		<input id="external_environment_grade" class="shuru" type="text" value="" placeholder="����" onblur="countSumGrade()">
			        	        <img class="qa" src="/masterreview/images/icon_qa.png" alt="">
				        	</td>				       
				        </tr>
				   </tbody>
				 </table> 
              </div>
              
			   
			<div class="tabletit">��ѧ��Ч</div>
			  <div id="student_development_div">
			      <table cellpadding="0" cellspacing="1" border="0" class="table text-center">
				    <tbody>
	                    <tr>
				            <th width="80%" colspan="6">ѧ����չ</th>
				            <th width="20%"></th>
				        </tr>
				        <tr>
				        	<td  colspan="6"><label id="student_development_label" name="student_development_label"></label></td>
				        	<td class="tablelink" >
				        		<input id="student_development_grade" class="shuru" type="text" value="" placeholder="����" onblur="countSumGrade()">
			        	        <img class="qa" src="/masterreview/images/icon_qa.png" alt="">
				        	</td>
				        </tr>
				    </tbody>
				  </table> 
			   </div>
			   <div id="teacher_development_div">
			      <table cellpadding="0" cellspacing="1" border="0" class="table text-center">
				    <tbody>
	                    <tr>
				            <th width="80%" colspan="6">��ʦ��չ</th>
				            <th width="20%"></th>
				        </tr>
				        <tr>
				        	<td  colspan="6"><label id="teacher_development_label" name="teacher_development_label"></label></td>
				        	<td class="tablelink" >
				        		<input id="teacher_development_grade" class="shuru" type="text" value="" placeholder="����" onblur="countSumGrade()">
			        	        <img class="qa" src="/masterreview/images/icon_qa.png" alt="">
				        	</td>
				        </tr>
				   </tbody>
				  </table> 
			   </div>
			   <div id="schoolAwardDiv"></div>
			   <div id="gradeEvaluateDiv"></div>
			
			<div class="tabletit">�ӷ���</div>
			      <div id="personalAwardDiv"></div>
			     <div id="schoolReformDiv"></div>
			     <div id="socialDutyDiv"></div>
			<div class="tabletit">������</div>
			     <div id="accidentDiv"></div>
			     <div id="punishmentDiv"></div>
			
			<div id='sumGradeDiv'>
				<table cellpadding='0' cellspacing='1' border='0' class='table text-center'>  <tbody>
		           <tr><th width='80%' colspan='8'>�ܷ�</th></tr>
		           <tr><td class='red' colspan='8'>	<label id="sumGrade" name="sumGrade"></label> </td></tr>
		          <tbody> 
		       </table>
			</div>
			
				<!-- 
			<div id='workReportDiv' class="tabletit">������Ч</div>
			<table id="workReportTab" cellpadding="0" cellspacing="1" border="0" class="table text-center" style="display: none">
			    <tbody>
                     <tr>
			               <th width="100%"><span><a href="">����</a></span>��ְ����</th>
			        </tr>
			        <tr>
			        	<td class="text-center">
			        	    <textarea id="work_report" class="text" name="" id="" cols="30" rows="10" readonly="readonly"></textarea>
			        		<p class="dafen">��֣�<input id="report_grade" type="text" placeholder="">�֣�����100��</p>
			        	</td>
			        </tr>
			   </tbody>
			</table>
 -->
			<div class="tabletit"><a href="javascript:void();" onclick="submitProfessorTask()">�ύ</a></div>
		</div>
	</div>

