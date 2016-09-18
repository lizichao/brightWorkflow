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
<jsp:include page="/masterreview/public/renderTemplate.jsp" flush="true" >
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
function beforeSubmit(formJsonData){
	var taskDefKey = Brightcom.workflow.getTaskDefKey();
	var processDefKey = Brightcom.workflow.getProcessDefKey();
	
	formJsonData.id=  $("#id").val();
	formJsonData.refuseType=  refuseTypeSubmit;
	formJsonData.refuseTypeId=  refuseTypeIdSubmit;
	
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

function afterSubmit(){
	 window.location.href = "/masterreview/arealeader/arealeaderIndex.jsp";
}

function submitAreaTask(){
	Brightcom.workflow.completeButtonTask('flow2',Brightcom.workflow.taskType);
}



function rollBackOpen(refuseType,refuseTypeId){
	 refuseType = refuseType;
	 refuseTypeId = refuseTypeId;
	$.fancybox.open({href:"/masterreview/arealeader/rollBackExecute.jsp?refuseType="+refuseType+"&refuseTypeId="+refuseTypeId,type:'iframe',width:327,height:298,padding:0,margin:0,closeBtn:true,iframe:{scrolling:'no',preload:false}});
}

/*
 * �����˻ش��ڵ�ִ�з���
 */
function rollBackExecute(refuseType,refuseTypeId){
	refuseTypeSubmit = refuseType;
	refuseTypeIdSubmit = refuseTypeId;
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
</script>
<input type="hidden" id="workflowSubmitAction" name="workflowSubmitAction" value="headmaster@@masterReviewAction@@completeAreaCadresTask">
<input type="hidden" id="id" name="id" value="">
<input type="hidden" id="headerMasterId" name="headerMasterId" value="">
	
<div class="container">
		<div class="xiao_bg">
			<div class="tit1">�걨����Ϣ</div>
			<div class="tabletit">ѧУ���������������</div>
			<table cellpadding="0" cellspacing="1" border="0" class="table">
			    <tbody>
                    <tr>
			            <th width="88%" colspan="6">������Ϣ</th>
			            <th width="12%"><a class="allshu" href="javascript:void(0);" onclick="rollBackOpen('allRefuse')">ȫ���˻�</a></th>
			        </tr>
			        <tr>
			        	<td style="180px;" rowspan="3" colspan="2">
			        	   <img id="person_img_object" style="width:98px;height: 114px"  >
			        	</td>
			        	<td class="black text-right">������</td>
			        	<td style="width:80px;"> <label id="headerMasterName" name="headerMasterName"></label> </td>
			        	<td class="black text-right">���֤�ţ�</td>
			        	<td style="width:95px"><label id="identitycard" name="identitycard"></label> </td>
			        	<td class="tablelink" rowspan="7"><a class="shu" href="javascript:void(0);" onclick="rollBackOpen('baseInfo')">�˻�</a></td>
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
			            <th width="88%" colspan="6">��������ְѧУ</th>
			            <th width="12%"></th>
			        </tr>
			        <tr>
			        	<td class="black" colspan="3">ѧУ���ƣ�</td>
			        	<td><label id="schoolNameSpaceAgo" name="schoolNameSpaceAgo"></label> </td>
			        	<td class="black">ѧУ���ͣ�</td>
			        	<td><label id="schoolTypeAgo" name="schoolTypeAgo"></label> </td>
			        	<td class="tablelink" rowspan="2"><a class="shu" href="javascript:void(0);" onclick="rollBackOpen('managementDifficultyAgo')">�˻�</a></td>
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
			            <th width="88%" colspan="6">����ְѧУ</th>
			            <th width="12%"></th>
			        </tr>
			        <tr>
			        	<td class="black" colspan="3">ѧУ���ƣ�</td>
			        	<td><label id="schoolNameSpace" name="schoolNameSpace"></label> </td>
			        	<td class="black">ѧУ���ͣ�</td>
			        	<td><label id="schoolType" name="schoolType"></label> </td>
			        	<td class="tablelink" rowspan="2"><a class="shu" href="javascript:void(0);" onclick="rollBackOpen('managementDifficulty')">�˻�</a></td>
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
				            <th width="88%" colspan="6">��ѧ˼��</th>
				            <th width="12%"></th>
				        </tr>
				        <tr>
				        	<td  colspan="6"><label id="run_school_label" name="run_school_label"></label></td>
				        	<td class="tablelink" ><a class="shu" href="javascript:void(0);" onclick="rollBackOpen('run_school')">�˻�</a></td>
				        </tr>
				   </tbody>
				</table>              
              </div>
              
              <div id="school_management_div"><!--ѧУ����  -->
              	<table cellpadding="0" cellspacing="1" border="0" class="table text-center">
				    <tbody>
	                    <tr>
				            <th width="88%" colspan="6">ѧУ����</th>
				            <th width="12%"></th>
				        </tr>
				        <tr>
				        	<td  colspan="6"><label id="school_management_label" name="school_management_label"></label></td>
				        	<td class="tablelink" ><a class="shu" href="javascript:void(0);" onclick="rollBackOpen('school_management')">�˻�</a></td>
				        </tr>
				   </tbody>
				 </table> 
              </div>
              
              <div id="education_science_div"><!--  ������ѧ-->
              	<table cellpadding="0" cellspacing="1" border="0" class="table text-center">
				    <tbody>
	                    <tr>
				            <th width="88%" colspan="6">������ѧ</th>
				            <th width="12%"></th>
				        </tr>
				        <tr>
				        	<td  colspan="6"><label id="education_science_label" name="education_science_label"></label></td>
				        	<td class="tablelink" ><a class="shu" href="javascript:void(0);" onclick="rollBackOpen('education_science')">�˻�</a></td>
				        </tr>
				   </tbody>
				</table> 
              </div>
              
              <div id="external_environment_div"><!-- �ⲿ���� -->
              	 <table cellpadding="0" cellspacing="1" border="0" class="table text-center">
				    <tbody>
	                    <tr>
				            <th width="88%" colspan="6">�ⲿ����</th>
				            <th width="12%"></th>
				        </tr>
				        <tr>
				        	<td  colspan="6"><label id="external_environment_label" name="external_environment_label"></label></td>
				        	<td class="tablelink" ><a class="shu" href="javascript:void(0);" onclick="rollBackOpen('external_environment')">�˻�</a></td>
				        </tr>
				   </tbody>
				 </table> 
              </div>
			
			   
			<div class="tabletit">��ѧ��Ч</div>
			   <div id="student_development_div">
			      <table cellpadding="0" cellspacing="1" border="0" class="table text-center">
				    <tbody>
	                    <tr>
				            <th width="88%" colspan="6">ѧ����չ</th>
				            <th width="12%"></th>
				        </tr>
				        <tr>
				        	<td  colspan="6"><label id="student_development_label" name="student_development_label"></label></td>
				        	<td class="tablelink" ><a class="shu" href="javascript:void(0);" onclick="rollBackOpen('student_development')">�˻�</a></td>
				        </tr>
				    </tbody>
				  </table> 
			   </div>
			   <div id="teacher_development_div">
			      <table cellpadding="0" cellspacing="1" border="0" class="table text-center">
				    <tbody>
	                    <tr>
				            <th width="88%" colspan="6">��ʦ��չ</th>
				            <th width="12%"></th>
				        </tr>
				        <tr>
				        	<td  colspan="6"><label id="teacher_development_label" name="teacher_development_label"></label></td>
				        	<td class="tablelink" ><a class="shu" href="javascript:void(0);" onclick="rollBackOpen('teacher_development')">�˻�</a></td>
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
	
			<div id='opinionDiv' >
				<table cellpadding='0' cellspacing='1' border='0' class='table text-center'>  <tbody>
		           <tr>
		              <th colspan='8'>ѧУ�����������ܲ�����������</th>
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
		              <th colspan='8'>�����ǩ������</th>
		           </tr>
		           <tr>
		              <input type="hidden" id="signAttachId" name="signAttachId" value=""/>
		              <td  colspan='2'> <span id="signButtonSpan"></span></td>
		              <td class='red' colspan='6'> <div id="signDisplayDiv"></div> </td>
		           </tr>
		          <tbody> 
		       </table>
			</div>
				
			<div class="tabletit"><a href="javascript:void();" onclick="submitAreaTask()" style="background-color:#44b549">�ύ</a></div>
		</div>
	</div>

