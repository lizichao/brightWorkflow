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
		     case "usertask1"://区级干部审核
		    	 currentDeal('areaActivity',dateStr,timeStr);
		         break;
		     case "usertask2"://人事干部审核
		    	 currentDeal('personalActivity',dateStr,timeStr);
		         break;
		     case "usertask3"://专家评分
		    	 currentDeal('professorActivity',dateStr,timeStr);
		         break;
		     case "usertask4"://述职答辩
		    	 currentDeal('replyActivity',dateStr,timeStr);
		         break;
		     case "usertask5"://重新填写申请
		    	 currentDeal('rejectActivity',dateStr,timeStr);
		         break;
		     case "endevent1"://重新填写申请
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
			<div class="tit1">申报人信息</div>
				<ul class="step">
				<li>
					<a href="">
						<p></p>
						<p id="startActivity"><b>开始</b></p>
					</a>
				</li>
				<li>
					<a href="">
						<p></p>
						<p id="rejectActivity"><b>驳回重填</b></p>
					</a>
				</li>
				<li>
					<a  href="">
						<p></p>
						<p id="areaActivity"><b>区级领导审核</b></p>
					</a>
				</li>
				<li>
					<a href="">
						<p></p>
						<p id="personalActivity"><b>人事干部审核</b></p>
					</a>
				</li>
				<li>
					<a href="">
						<p></p>
						<p id="professorActivity"><b>专家审核</b></p>
					</a>
				</li>
				<li>
					<a href="">
						<p></p>
						<p id="replyActivity"><b>述职答辩</b></p>
					</a>
				</li>
				<li>
					<a href="">
						<p></p>
						<p id="endActivity"><b>结束</b></p>
					</a>
				</li>
			</ul>
			<div class="tabletit">申请详情信息</div>
			<table cellpadding="0" cellspacing="1" border="0" class="table">
				<tbody>
					<tr>
			            <th width="100%" colspan="7">基础信息</th>
			        </tr>
			        <tr>
			        	<td rowspan="3" width="10%">
			        	   <img id="person_img_object" style="width:98px;height: 114px"  >
			        	</td>
			        	<td class="black text-right" width="10%">姓名：</td>
			        	<td width="20%"><label id="headerMasterName" name="headerMasterName"></label> </td>
			        	<td class="black text-right" width="10%">性别：</td>
			        	<td width="20%"><label id="usersex" name="usersex"></label> </td>
			        	<td class="black text-right" width="10%">出生年月：</td>
			        	<td width="20%"><label id="birth_date" name="birth_date"></label> </td>
			        </tr>
			        <tr>
			        	<td class="black text-right">民族：</td>
			        	<td><label id="nation" name="nation"></label> </td>
			        	<td class="black text-right">籍贯：</td>
			        	<td><label id="native_place" name="native_place"></label></td>
			        	<td class="black text-right">户籍：</td>
			        	<td><label id="census_register" name="census_register"></label></td>
		        	</tr>
			        <tr>
			        	<td class="black text-right">政治面貌：</td>
			        	<td><label id="politics_status" name="politics_status"></label></td>
			        	<td class="black text-right">手机号：</td>
			        	<td><label id="mobile" name="mobile"></label></td>
			        	<td class="black text-right">邮箱：</td>
			        	<td><label id="email" name="email"></label></td>
		        	</tr>
		        	<tr>
		        		<td colspan="7" style="padding: 0 0;">
		        			<table cellpadding="0" cellspacing="1" border="0" class="table" style="margin-top: 0px;">
		        				<tbody>
		        					<tr>
							        	<td class="black text-right" width="15%">教龄：</td>
			        					<td width="15%"><label id="teach_age" name="teach_age"></label> </td>
			        					<td width="15%" class="black text-right">寄宿制学校：</td>
			        					<td width="15%"><label id="lodge_school" name="lodge_school"></label></td>
			        					<td width="15%" class="black text-right">学校类型：</td>
			        					<td width="25%"><label id="school_class" name="school_class"></label></td>
							        </tr>
							        <tr>
							        	<td class="black text-right">参加工作时间：</td>
			        					<td><label id="join_work_time" name="join_work_time"></label> </td>
			        	 				<td class="black text-right">参加教育工作时间：</td>
			        					<td><label id="join_educate_work_time" name="join_educate_work_time"></label></td>
			        					<td class="black text-right">学段：</td>
			        					<td><label id="phasestudy" name="phasestudy"></label></td>
							        </tr>
							        <tr>
							        	<td class="black text-right">现任职务：</td>
			        					<td><label id="present_occupation" name="present_occupation"></label> </td>
			        					<td class="black text-right">现任专业技术职务：</td>
			        					<td><label id="present_major_occupation" name="present_major_occupation"></label> </td>
			        					<td class="black text-right">学校所在区：</td>
			        					<td><label id="district_name" name="district_name"></label></td>
							        </tr>
							        <tr>
							        	<td class="black text-right">所在学校：</td>
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
			            <th width="100%" colspan="6">基础信息</th>
			        </tr>
			        <tr>
			        	<td style="180px;" rowspan="3" colspan="2">
			        	   <img id="person_img_object" style="width:98px;height: 114px"  >
			        	</td>
			        	<td class="black text-right">姓名：</td>
			        	<td style="width:80px;"> <label id="headerMasterName" name="headerMasterName"></label> </td>
			        	<td class="black text-right">身份证号：</td>
			        	<td style="width:95px"><label id="identitycard" name="identitycard"></label> </td>
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
			   -->
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
			            <th width="100%" colspan="4">近八年任职学校</th>
			        </tr>
			        <tr>
			        	<td class="black" width="15%">学校名称：</td>
			        	<td width="35%"><label id="schoolNameSpaceAgo" name="schoolNameSpaceAgo"></label></td>
			        	<td class="black" width="15%">学校类型：</td>
			        	<td width="35%"><label id="schoolTypeAgo" name="schoolTypeAgo"></label> </td>
			        </tr>
			        <tr>
			        	<td class="black">校区数量：</td>
			        	<td><label id="schoolCountAgo" name="schoolCountAgo"></label> </td>
			        	<td class="black">班级数量：</td>
			        	<td><label id="studentNumberAgo" name="studentNumberAgo"></label> </td>
			        </tr>
			        <tr>
			        	<td class="black">证明材料：</td>
			        	<td colspan="3"><span id="manageDifficultyAgoSpan"></span> </td>
			        </tr>
			   	</tbody>
			</table>
			<table cellpadding="0" cellspacing="1" border="0" class="table text-center">
			    <tbody>
                    <tr>
			            <th width="100%" colspan="4">现任职学校</th>
			        </tr>
			        <tr>
			        	<td class="black" width="15%">学校名称：</td>
			        	<td width="35%"><label id="schoolNameSpace" name="schoolNameSpace"></label> </td>
			        	<td class="black" width="15%">学校类型：</td>
			        	<td width="35%"><label id="schoolType" name="schoolType"></label> </td>
			        </tr>
			        <tr>
			        	<td class="black">校区数量：</td>
			        	<td><label id="schoolCount" name="schoolCount"></label> </td>
			        	<td class="black">班级数量：</td>
			        	<td><label id="studentNumber" name="studentNumber"></label> </td>
			        </tr>
			        <tr>
			        	<td class="black">证明材料：</td>
			        	<td colspan="3"><span id="manageDifficultySpan"></span></td>
			        </tr>
			   </tbody>
			</table>

		    <div class="tabletit">专业能力</div>
			  <div id="run_school_div"><!-- 办学思想 -->
				<table cellpadding="0" cellspacing="1" border="0" class="table text-center">
				    <tbody>
	                    <tr>
				            <th width="100%" colspan="6">办学思想</th>
				        </tr>
				        <tr>
				        	<td  colspan="6"><label id="run_school_label" name="run_school_label"></label></td>
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
				            <th width="100%" colspan="6">学校管理</th>
				        </tr>
				        <tr>
				        	<td  colspan="6"><label id="school_management_label" name="school_management_label"></label></td>
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
				            <th width="100%" colspan="6">教育教学</th>
				        </tr>
				        <tr>
				        	<td  colspan="6"><label id="education_science_label" name="education_science_label"></label></td>
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
				            <th width="100%" colspan="6">外部环境</th>
				        </tr>
				        <tr>
				        	<td  colspan="6"><label id="external_environment_label" name="external_environment_label"></label></td>
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
				            <th width="100%" colspan="6">学生发展</th>
				        </tr>
				        <tr>
				        	<td  colspan="6"><label id="student_development_label" name="student_development_label"></label></td>
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
				            <th width="100%" colspan="6">教师发展</th>
				        </tr>
				        <tr>
				        	<td  colspan="6"><label id="teacher_development_label" name="teacher_development_label"></label></td>
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
			 
			<!--  
			<div id='workReportDiv'  class="tabletit">工作成效</div>
			<table id="workReportTab" cellpadding="0" cellspacing="1" border="0" class="table text-center" style="display: none">
			    <tbody>
                    <tr>
			              <th width="100%"><span><a href="">隐藏</a></span>述职报告</th>
			        </tr>
			        <tr>
			        	<td class="text-center">
			        	    <textarea id="work_report" class="text" name="" id="" cols="30" rows="10" readonly="readonly"></textarea>
			        		<p class="dafen" style="display: none">打分：<input type="text" placeholder="">分（满分100）</p>
			        	</td>
			        </tr>
			   </tbody>
			</table>-->	

			<div class="tabletit"><a href="">返回</a></div>
		</div>
	</div>

