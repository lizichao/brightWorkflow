<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="org.jdom.*"%>
<%@ page import="java.util.List"%>
<%@ page import="cn.brightcom.jraf.web.*"%>
<%
  String userid = (String)session.getAttribute("userid");
  String username =(String)session.getAttribute("username");
  String usertype =(String)session.getAttribute("usertype");
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
 <jsp:param name="approveType" value="wholeApprove" />
 <jsp:param name="basePath" value="<%=basePath%>" />
</jsp:include>


<script>
var headerMasterId = '<%=userid%>';
var headerMasterName = '<%=username%>';
var processInstanceId = '<%=processInstanceId%>';
var processBusinessKey = '<%=processBusinessKey%>';


$(document).ready(function(){	
	Headmaster.bulidMsaterView(<%=masterReviewVO%>,Headmaster.wholeApprove);
	bulidSimpleCharts(<%=masterReviewVO%>.historyActinstVOs);
});	


function bulidSimpleCharts(historyActinstVOs){
	 for(var i =0;i<historyActinstVOs.length;i++){
		 var historyActinstVO = historyActinstVOs[i];
		 var act_id = historyActinstVO.act_id;
		 var end_time =Brightcom.workflow.getDateByLongStr( historyActinstVO.end_time);
		 var dateStr = end_time.substring(0,10);
		 var timeStr = end_time.substring(11);
		 switch (act_id) {
		     case "startevent1"://
		         currentDeal('startActivity',dateStr,timeStr);
		         break;
		     case "usertask1"://�����ɲ����
		    	 currentDeal('areaActivity',dateStr,timeStr);
		         break;
		     case "usertask2"://���¸ɲ����
		    	 currentDeal('personalActivity',dateStr,timeStr);
		         break;
		     case "usertask3"://ר������
		    	 currentDeal('professorActivity',dateStr,timeStr);
		         break;
		     case "usertask4"://��ְ���
		    	 currentDeal('replyActivity',dateStr,timeStr);
		         break;
		     case "usertask5"://������д����
		    	 currentDeal('rejectActivity',dateStr,timeStr);
		         break;
		     case "endevent1"://������д����
		    	 currentDeal('endActivity',dateStr,timeStr);
		         break;
		     default:
		         break
        }
	 }
}

function currentDeal(activityId,dateStr,timeStr){
	 $("#"+activityId).find("b").after("<br>"+dateStr+" <br>"+timeStr);
	 if(!dateStr){
		 $("#"+activityId).parent().addClass('hov');
	 }
}


function exportToWord(){
	 window.open('/exportWordService?processInstanceId='+processInstanceId+"&businessKey="+processBusinessKey);
}
</script>
<input type="hidden" id="id" name="id" value="">

	
<div class="container">
		<div class="xiao_bg">
			<div class="tit1">�걨����Ϣ</div>
				<ul class="step">
				<li>
					<a href="">
						<p></p>
						<p id="startActivity"><b>��ʼ</b></p>
					</a>
				</li>
				<li>
					<a href="">
						<p></p>
						<p id="rejectActivity"><b>��������</b></p>
					</a>
				</li>
				<li>
					<a  href="">
						<p></p>
						<p id="areaActivity"><b>�����쵼���</b></p>
					</a>
				</li>
				<li>
					<a href="">
						<p></p>
						<p id="personalActivity"><b>���¸ɲ����</b></p>
					</a>
				</li>
				<li>
					<a href="">
						<p></p>
						<p id="professorActivity"><b>ר�����</b></p>
					</a>
				</li>
				<li>
					<a href="">
						<p></p>
						<p id="replyActivity"><b>��ְ���</b></p>
					</a>
				</li>
				<li>
					<a href="">
						<p></p>
						<p id="endActivity"><b>����</b></p>
					</a>
				</li>
			</ul>
			<div class="tabletit">����������Ϣ</div>
			<table cellpadding="0" cellspacing="1" border="0" class="table">
				<tbody>
					<tr>
			            <th width="100%" colspan="7">������Ϣ</th>
			        </tr>
			        <tr>
			        	<td rowspan="3" width="10%">
			        	   <img id="person_img_object" style="width:98px;height: 114px"  >
			        	</td>
			        	<td class="black text-right" width="10%">������</td>
			        	<td width="20%"><label id="headerMasterName" name="headerMasterName"></label> </td>
			        	<td class="black text-right" width="10%">�Ա�</td>
			        	<td width="20%"><label id="usersex" name="usersex"></label> </td>
			        	<td class="black text-right" width="10%">�������£�</td>
			        	<td width="20%"><label id="birth_date" name="birth_date"></label> </td>
			        </tr>
			        <tr>
			        	<td class="black text-right">���壺</td>
			        	<td><label id="nation" name="nation"></label> </td>
			        	<td class="black text-right">���᣺</td>
			        	<td><label id="native_place" name="native_place"></label></td>
			        	<td class="black text-right">������</td>
			        	<td><label id="census_register" name="census_register"></label></td>
		        	</tr>
			        <tr>
			        	<td class="black text-right">������ò��</td>
			        	<td><label id="politics_status" name="politics_status"></label></td>
			        	<td class="black text-right">�ֻ��ţ�</td>
			        	<td><label id="mobile" name="mobile"></label></td>
			        	<td class="black text-right">���䣺</td>
			        	<td><label id="email" name="email"></label></td>
		        	</tr>
		        	<tr>
		        		<td colspan="7" style="padding: 0 0;">
		        			<table cellpadding="0" cellspacing="1" border="0" class="table" style="margin-top: 0px;">
		        				<tbody>
		        					<tr>
							        	<td class="black text-right" width="15%">���䣺</td>
			        					<td width="15%"><label id="teach_age" name="teach_age"></label> </td>
			        					<td width="15%" class="black text-right">������ѧУ��</td>
			        					<td width="15%"><label id="lodge_school" name="lodge_school"></label></td>
			        					<td width="15%" class="black text-right">ѧУ���ͣ�</td>
			        					<td width="25%"><label id="school_class" name="school_class"></label></td>
							        </tr>
							        <tr>
							        	<td class="black text-right">�μӹ���ʱ�䣺</td>
			        					<td><label id="join_work_time" name="join_work_time"></label> </td>
			        	 				<td class="black text-right">�μӽ�������ʱ�䣺</td>
			        					<td><label id="join_educate_work_time" name="join_educate_work_time"></label></td>
			        					<td class="black text-right">ѧ�Σ�</td>
			        					<td><label id="phasestudy" name="phasestudy"></label></td>
							        </tr>
							        <tr>
							        	<td class="black text-right">����ְ��</td>
			        					<td><label id="present_occupation" name="present_occupation"></label> </td>
			        					<td class="black text-right">����רҵ����ְ��</td>
			        					<td><label id="present_major_occupation" name="present_major_occupation"></label> </td>
			        					<td class="black text-right">ѧУ��������</td>
			        					<td><label id="district_name" name="district_name"></label></td>
							        </tr>
							        <tr>
							        	<td class="black text-right">����ѧУ��</td>
			        					<td><label id="school_name" name="school_name"></label></td>
			        					<td class="black text-right">&nbsp;</td>
			        					<td>&nbsp;</td>
			        					<td class="black text-right">&nbsp;</td>
			        					<td>&nbsp;</td>
							        </tr>
		        				</tbody>
		        			</table>
		        		</td>
		        	</tr>
				</tbody>
				<!-- 
			    <tbody>
                    <tr>
			            <th width="100%" colspan="6">������Ϣ</th>
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
			        	<td colspan="1"><label id="present_occupation" name="present_occupation"></label> </td>
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
			   -->
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
			            <th width="100%" colspan="4">��������ְѧУ</th>
			        </tr>
			        <tr>
			        	<td class="black" width="15%">ѧУ���ƣ�</td>
			        	<td width="35%"><label id="schoolNameSpaceAgo" name="schoolNameSpaceAgo"></label></td>
			        	<td class="black" width="15%">ѧУ���ͣ�</td>
			        	<td width="35%"><label id="schoolTypeAgo" name="schoolTypeAgo"></label> </td>
			        </tr>
			        <tr>
			        	<td class="black">У��������</td>
			        	<td><label id="schoolCountAgo" name="schoolCountAgo"></label> </td>
			        	<td class="black">�༶������</td>
			        	<td><label id="studentNumberAgo" name="studentNumberAgo"></label> </td>
			        </tr>
			        <tr>
			        	<td class="black">֤�����ϣ�</td>
			        	<td colspan="3"><span id="manageDifficultyAgoSpan"></span> </td>
			        </tr>
			   	</tbody>
			</table>
			<table cellpadding="0" cellspacing="1" border="0" class="table text-center">
			    <tbody>
                    <tr>
			            <th width="100%" colspan="4">����ְѧУ</th>
			        </tr>
			        <tr>
			        	<td class="black" width="15%">ѧУ���ƣ�</td>
			        	<td width="35%"><label id="schoolNameSpace" name="schoolNameSpace"></label> </td>
			        	<td class="black" width="15%">ѧУ���ͣ�</td>
			        	<td width="35%"><label id="schoolType" name="schoolType"></label> </td>
			        </tr>
			        <tr>
			        	<td class="black">У��������</td>
			        	<td><label id="schoolCount" name="schoolCount"></label> </td>
			        	<td class="black">�༶������</td>
			        	<td><label id="studentNumber" name="studentNumber"></label> </td>
			        </tr>
			        <tr>
			        	<td class="black">֤�����ϣ�</td>
			        	<td colspan="3"><span id="manageDifficultySpan"></span></td>
			        </tr>
			   </tbody>
			</table>

		    <div class="tabletit">רҵ����</div>
			  <div id="run_school_div"><!-- ��ѧ˼�� -->
				<table cellpadding="0" cellspacing="1" border="0" class="table text-center">
				    <tbody>
	                    <tr>
				            <th width="100%" colspan="6">��ѧ˼��</th>
				        </tr>
				        <tr>
				        	<td  colspan="6"><label id="run_school_label" name="run_school_label"></label></td>
				        </tr>
				         <tr>
				        	<td  colspan="5">֤�����ϣ�</td>
				        	<td  class="tablelink" >
                               <a id="run_school_attachment_id" class="cha" href="#"> ����鿴</a>
				        	</td>
				        </tr>
				   </tbody>
				</table>              
              </div>
              
               <div id="school_management_div"><!--ѧУ����  -->
              	<table cellpadding="0" cellspacing="1" border="0" class="table text-center">
				    <tbody>
	                    <tr>
				            <th width="100%" colspan="6">ѧУ����</th>
				        </tr>
				        <tr>
				        	<td  colspan="6"><label id="school_management_label" name="school_management_label"></label></td>
				        </tr>
				         <tr>
				        	<td  colspan="5">֤�����ϣ�</td>
				        	<td  class="tablelink" >
                               <a id="school_management_attachment_id" class="cha" href="#"> ����鿴</a>
				        	</td>
				        </tr>
				   </tbody>
				 </table> 
              </div>
              
              <div id="education_science_div"><!--  ������ѧ-->
              	<table cellpadding="0" cellspacing="1" border="0" class="table text-center">
				    <tbody>
	                    <tr>
				            <th width="100%" colspan="6">������ѧ</th>
				        </tr>
				        <tr>
				        	<td  colspan="6"><label id="education_science_label" name="education_science_label"></label></td>
				        </tr>
				          <tr>
				        	<td  colspan="5">֤�����ϣ�</td>
				        	<td  class="tablelink" >
                               <a id="education_science_attachment_id" class="cha" href="#"> ����鿴</a>
				        	</td>
				        </tr>
				   </tbody>
				</table> 
              </div>
              
              <div id="external_environment_div"><!-- �ⲿ���� -->
              	 <table cellpadding="0" cellspacing="1" border="0" class="table text-center">
				    <tbody>
	                    <tr>
				            <th width="100%" colspan="6">�ⲿ����</th>
				        </tr>
				        <tr>
				        	<td  colspan="6"><label id="external_environment_label" name="external_environment_label"></label></td>
				        </tr>
				        
				          <tr>
				        	<td  colspan="5">֤�����ϣ�</td>
				        	<td  class="tablelink" >
                               <a id="external_environment_attachment_id" class="cha" href="#"> ����鿴</a>
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
				            <th width="100%" colspan="6">ѧ����չ</th>
				        </tr>
				        <tr>
				        	<td  colspan="6"><label id="student_development_label" name="student_development_label"></label></td>
				        </tr>
				        
				         <tr>
				        	<td  colspan="5">֤�����ϣ�</td>
				        	<td  class="tablelink" >
                               <a id="student_development_attachment_id" class="cha" href="#"> ����鿴</a>
				        	</td>
				        </tr>
				    </tbody>
				  </table> 
			   </div>
			   <div id="teacher_development_div">
			      <table cellpadding="0" cellspacing="1" border="0" class="table text-center">
				    <tbody>
	                    <tr>
				            <th width="100%" colspan="6">��ʦ��չ</th>
				        </tr>
				        <tr>
				        	<td  colspan="6"><label id="teacher_development_label" name="teacher_development_label"></label></td>
				        </tr>
				        
				       <tr>
				        	<td  colspan="5">֤�����ϣ�</td>
				        	<td  class="tablelink" >
                               <a id="teacher_development_attachment_id" class="cha" href="#"> ����鿴</a>
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
			 
			<!--  
			<div id='workReportDiv'  class="tabletit">������Ч</div>
			<table id="workReportTab" cellpadding="0" cellspacing="1" border="0" class="table text-center" style="display: none">
			    <tbody>
                    <tr>
			              <th width="100%"><span><a href="">����</a></span>��ְ����</th>
			        </tr>
			        <tr>
			        	<td class="text-center">
			        	    <textarea id="work_report" class="text" name="" id="" cols="30" rows="10" readonly="readonly"></textarea>
			        		<p class="dafen" style="display: none">��֣�<input type="text" placeholder="">�֣�����100��</p>
			        	</td>
			        </tr>
			   </tbody>
			</table>-->	

			<div class="tabletit"><a href="">����</a></div>
		</div>
	</div>

