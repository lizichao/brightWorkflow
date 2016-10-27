<%@ page contentType="text/html; charset=GBK" %>
<%
  String approveType = (String)request.getParameter("approveType");
  String basePath = (String)request.getParameter("basePath");
%>


<!--学历情况 -->
<script id="educationRec" type="text/x-jsrender">	
<table cellpadding="0" cellspacing="1" border="0" class="table text-center">
			    <tbody>
                    <tr>
			            <th width="100%" colspan="9">学历情况</th>
			        </tr>
			        <tr>
			        	<td class="black">序号</td>
			        	<td class="black">起止年月</td>
			        	<td class="black">就读学校</td>
			        	<td class="black">就读专业</td>
			        	<td class="black">学历</td>
                        <td class="black">学历证明材料</td>
			        	<td class="black">学位</td>
			        	<td class="black">学位证明材料</td>
			        	<td class="black">得分</td>
			        </tr>
{{for Data ~education_grade_rows = education_grade_rows}}
    <tr>
      <td> {{:#index+1}}</td>
      <td>{{timeContent startTime endTime/}}</td>
      <td>{{:studySchool}}</td>
      <td>{{:studyProfession}}</td>
      <td>{{:education}}</td>
      <td>
         {{if educationAttachMentVO.attachmentId !==null}}
             <a class="cha" href="<%=basePath%>WorkflowAttachMentDownload?attachmentId={{:educationAttachMentVO.attachmentId}}"> 点击查看</a>
        {{/if}}
      </td>
      <td>{{:degree}}</td>
      <td>
         {{if degreeAttachMentVO.attachmentId !==null}}
             <a class="cha" href="<%=basePath%>WorkflowAttachMentDownload?attachmentId={{:degreeAttachMentVO.attachmentId}}"> 点击查看</a>
        {{/if}}
      </td>
      {{if #index===0 }}
        <td class="tablelink" rowspan="{{:~education_grade_rows}}">   
           <label id="education_grade" name="education_professor_grade"></label>
        </td>
	 {{else}}
	 {{/if}}
    </tr>
{{/for}}
</tbody>
</table>
</script>


<!--任职年限  -->
<script id="workExperienceRec" type="text/x-jsrender">	
<table cellpadding="0" cellspacing="1" border="0" class="table text-center">
<tbody>
                    <tr>
			            <th width="100%" colspan="7">任职年限</th>
			        </tr>
			        <tr>
			        	<td class="black">序号</td>
			        	<td class="black">起止年月</td>
			        	<td class="black">所在学校</td>
			        	<td class="black">担任职务</td>
			        	<td class="black">任职年限</td>
			        	<td class="black">证明材料</td>
			        	<td class="black">得分</td>
			        </tr>
{{for Data ~workExperience_grade_rows = workExperience_grade_rows}}
    <tr>
      <td>{{:#index+1}}</td>
      <td>{{timeContent startTime endTime/}}</td>
      <td>{{:workSchool}}</td>
      <td>{{:workProfession}}</td>
      <td>{{:workYear}}</td>
      <td>
         {{if proveAttachMentVO.attachmentId !==null}}
             <a class="cha" href="<%=basePath%>WorkflowAttachMentDownload?attachmentId={{:proveAttachMentVO.attachmentId}}"> 点击查看</a>
        {{/if}}
      </td>
     {{if #index===0 }}
        <td class="tablelink" rowspan="{{:~workExperience_grade_rows}}">   
           <label id="work_experience_grade" name="work_experience_grade"></label>
        </td>
	 {{else}}
	 {{/if}}
    </tr>
{{/for}}

</tbody>
</table>
</script>


<!--职称情况 -->
<script id="professionalTitleRec" type="text/x-jsrender">	
		<table cellpadding="0" cellspacing="1" border="0" class="table text-center">
			    <tbody>
                    <tr>
			            <th width="100%" colspan="5">职称情况</th>
			        </tr>
			        <tr>
			        	<td class="black">序号</td>
			        	<td class="black">职称</td>
			        	<td class="black">取得职称时间</td>
			        	<td class="black">证明材料</td>
			        	<td class="black">得分</td>
			        </tr>
{{for Data ~professionalTitle_grade_rows =professionalTitle_grade_rows  }}
    <tr>
      <td>{{:#index+1}}</td>
      <td>{{:professionaltitle_name}}</td>
      <td>{{timeCovert obtainTime/}}</td>
      <td>  
        {{if attachMentVO.attachmentId !==null}}
             <a class="cha" href="<%=basePath%>WorkflowAttachMentDownload?attachmentId={{:attachMentVO.attachmentId}}"> 点击查看</a>
         {{/if}}
      </td>
      {{if #index===0 }}
        <td class="tablelink" rowspan="{{:~professionalTitle_grade_rows}}">   
           <label id="professional_title_grade" name="professional_title_grade"></label>
        </td>
	 {{else}}
	 {{/if}}
    </tr>
{{/for}}

</tbody>
</table>
</script>

<!--论文发表情况  -->
<script id="paperRec" type="text/x-jsrender">	
<table cellpadding="0" cellspacing="1" border="0" class="table text-center">
			    <tbody>
                    <tr>
			            <th width="79%" colspan="7">论文发表情况</th>
			            <th width="21%">得分</th>
			        </tr>
{{for Data ~paper_grade_rows = paper_grade_rows}}
                     <tr>
			        	<td rowspan="5">{{:#index+1}}</a>
			        	<td class="black">题目：</td>
			        	<td colspan="2">{{:title}}</td>
			        	<td class="black" colspan="2">杂志名称：</td>
			        	<td>{{:magazineMeetName}}/{{:paper_grade_rows}}</td>
                       {{if #index===0 }}

				       <td class="tablelink" rowspan="{{:~paper_grade_rows}}">   
                            <label id="paper_grade" name="paper_grade"></label>
                       </td>
				       {{else}}
				       {{/if}}
			        </tr>
                    <tr>
			        	<td class="black">宣读论文会议名称：</td>
			        	<td>{{:paperMeetName}}</td>
			        	<td class="black">刊号：</td>
			        	<td>{{:paperNumber}}</td>
			        	<td class="black">发表及宣读时间：</td>
			        	<td>{{timeCovert publishTime/}}</td>
			        </tr>
                    <tr>
			        	<td class="black">主办单位：</td>
			        	<td>{{:organizers}}</td>
			        	<td class="black">主办单位级别：</td>
			        	<td>{{:organizersLevel}}</td>
			        	<td class="black">本人承担部分：</td>
			        	<td>{{:personalPart}}</td>
			        </tr>

                     <tr>
			        	<td class="black">完成方式：</td>
			        	<td>{{:complete_way}}</td>
			        	<td class="black">作者排序：</td>
			        	<td>{{:author_order}}</td>
			        	<td class="black">出版单位：</td>
			        	<td>{{:publish_company}}</td>
			        </tr>


                     <tr>
			        	<td colspan="4" class="black">论文扫描件：</td>
			        	<td colspan="2">
                          {{if paperAttachMentVO.attachmentId !==null}}
                         <a class="cha" href="<%=basePath%>WorkflowAttachMentDownload?attachmentId={{:paperAttachMentVO.attachmentId}}"> 点击查看</a>
                         {{/if}}
                        </td>
			        </tr>
{{/for}}
</tbody>
</table>
</script>

<!--专著出版情况  -->
<script id="workPublishRec" type="text/x-jsrender">	
<table cellpadding="0" cellspacing="1" border="0" class="table text-center">
		<tbody>
                    <tr>
			            <th width="79%" colspan="7">专著出版情况</th>
			            <th width="21%">得分</th>
			        </tr>
{{for Data ~bookPublish_grade_rows = bookPublish_grade_rows}}
			        <tr>
			        	<td rowspan="3">{{:#index+1}}</a>
			        	<td class="black">书名：</td>
			        	<td colspan="5">{{:book_name}}</td>
                        {{if #index===0 }}
				         <td class="tablelink" rowspan="{{:~bookPublish_grade_rows}}">
                                <label id="work_publish_grade" name="work_publish_grade"></label>
                         </td>
				       {{else}}
				       {{/if}}
			        </tr>
                    <tr>
			        	<td class="black">完成方式：</td>
			        	<td>{{:complete_way}}</td>
			        	<td class="black">出版时间：</td>
			        	<td>{{timeCovert publish_time/}} </td>
			        	<td class="black">本人完成字数：</td>
			        	<td>{{:complete_word}}</td>
			        </tr>
                    <tr>
			        	<td class="black">本人承担部分：</td>
			        	<td>{{:complete_chapter}}</td>
			        	<td class="black">作者排序：</td>
			        	<td colspan="1">{{:author_order}}</td>
                    <td class="black">出版社：</td>
			        	<td colspan="1">{{:publish_company}}</td>
		        	</tr>
{{/for}}
		</tbody>
</table>
</script>


<!--课题情况  -->
<script id="subjectRec" type="text/x-jsrender">	
			<table cellpadding="0" cellspacing="1" border="0" class="table text-center">
			    <tbody>
                    <tr>
			            <th width="79%" colspan="7">教科研情况</th>
			            <th width="21%">得分</th>
			        </tr>
{{for Data ~subject_grade_rows = subject_grade_rows}}
			        <tr>
			        	<td rowspan="5">{{:#index+1}}</a>
			        	<td class="black">课题名称：</td>
			        	<td colspan="2">{{:subjectName}}</td>
			        	<td class="black" colspan="2">课题立项单位：</td>
			        	<td>{{:subjectCompany}}</td>
                        {{if #index===0 }}
				         <td class="tablelink" rowspan="{{:~subject_grade_rows}}">
                              <label id="subject_grade" name="subject_grade"></label>
                         </td>
				       {{else}}
				       {{/if}}
			        </tr>
                    <tr>
			        	<td class="black">课题级别：</td>
			        	<td colspan="2">{{:subjectLevel}}</td>
			        	<td class="black">是否结题：</td>
			        	<td colspan="2">{{:isfinishSubject}}</td>
			        </tr>
                    <tr>
			        	<td class="black">结题时间：</td>
			        	<td>{{timeCovert finishTime/}}</td>
			        	<td class="black">获奖情况：</td>
			        	<td colspan="3">{{:finishResult}}</td>
		        	</tr>

                      <tr>
	                    <td class="black" colspan="2">课题职责：</td>
			        	<td colspan="4">{{:subjectRresponsibility}}</td>
                   </tr>

                     <tr>
	                    <td class="black" colspan="2">课题简介：</td>
			        	<td colspan="4">    
                      {{if subjectAttachVO.attachmentId !==null}}
                       <a class="cha" href="<%=basePath%>WorkflowAttachMentDownload?attachmentId={{:subjectAttachVO.attachmentId}}"> 点击查看</a>
                      {{/if}}
                      </td>
                   </tr>
{{/for}}
			   </tbody>
			</table>
</script>


<!--个人获奖情况  -->
<script id="personalAwardRec" type="text/x-jsrender">	
			<table cellpadding="0" cellspacing="1" border="0" class="table text-center">
			    <tbody>
                    <tr>
			            <th width="79%" colspan="7">个人获奖情况</th>
			            <th width="21%">得分</th>
			        </tr>
{{for Data}}
			        <tr>
			        	<td rowspan="4">{{:#index+1}}</td>
			        	<td class="black">获奖名称：</td>
			        	<td colspan="2">{{:awardsName}}</td>
			        	<td class="black">表彰单位：</td>
			        	<td colspan="2">{{:awardsCompany}}</td>
                        {{if #index===0 }}
				         <td class="tablelink" rowspan="{{:personRowNum}}">
                            <label id="personal_award_grade" name="schoolReform_grade"></label>
                         </td>
				       {{else}}
				       {{/if}}
			        </tr>
                    <tr>
			        	<td class="black">获奖级别：</td>
			        	<td>{{:awardsLevel}}</td>
			        	<td class="black">表彰时间：</td>
			        	<td colspan="3">{{timeCovert awardsTime/}}</td>
                      
		        	</tr>

              <tr>
                <td class="black">奖项类别：</td>
			    <td >{{:awards_type}}</td>
			    <td colspan="2" class="black">获奖证书：</td>
			    <td colspan="2">
                    {{if personalAttachVO.attachmentId !==null}}
                     <a class="cha" href="<%=basePath%>WorkflowAttachMentDownload?attachmentId={{:personalAttachVO.attachmentId}}"> 点击查看</a>
                    {{/if}}
                </td>
			 </tr>

              <tr>
                        <td class="black">奖项类别2：</td>
			        	<td >{{:awards_type1}}</td>
			        	<td colspan="2" class="black">获奖证书2：</td>
			        	<td colspan="2">  
                         {{if personalAttachVO1.attachmentId !==null}}
                         <a class="cha" href="<%=basePath%>WorkflowAttachMentDownload?attachmentId={{:personalAttachVO1.attachmentId}}"> 点击查看</a>
                         {{/if}}
                      </td>
		        	</tr>
{{/for}}
			   </tbody>
			</table>
</script>


<!--学校获奖情况  -->
<script id="schoolAwardRec" type="text/x-jsrender">	
			<table cellpadding="0" cellspacing="1" border="0" class="table text-center">
			    <tbody>
                    <tr>
			            <th width="79%" colspan="7">学校获奖情况</th>
			            <th width="21%"></th>
			        </tr>
{{for Data}}
			        <tr>
			        	<td rowspan="3">{{:#index+1}}</a>
			        	<td class="black">奖项名称：</td>
			        	<td colspan="2">{{:awardsName}}</td>
			        	<td class="black" colspan="2">表彰单位：</td>
			        	<td>{{:awardsCompany}}</td>
                        {{if #index===0 }}
				         <td class="tablelink" rowspan="{{:personRowNum}}">
                            <input class="shuru" id="school_award_grade" type="text"  placeholder="输入">
                            <img class="qa" src="/masterreview/images/icon_qa.png" alt="">
                         </td>
				       {{else}}
				       {{/if}}
			        </tr>
                    <tr>
			        	<td class="black">任职学校：</td>
			        	<td>{{:workSchool}}</td>
			        	<td class="black">获奖级别：</td>
			        	<td>{{:awardsLevel}}</td>
			        	<td class="black">表彰时间：</td>
			        	<td>{{timeCovert awardsTime/}}</td>
			        </tr>


             <tr>
			   <td colspan="4" class="black">获奖证书：</td>
			   <td colspan="2">
                    {{if schoolAttachVO.attachmentId !==null}}
                     <a class="cha" href="<%=basePath%>WorkflowAttachMentDownload?attachmentId={{:schoolAttachVO.attachmentId}}"> 点击查看</a>
                    {{/if}}
               </td>
			 </tr>
{{/for}}
			   </tbody>
			</table>
</script>



<!--进修学习情况  -->
<script id="studyTrainRec" type="text/x-jsrender">	
			<table cellpadding="0" cellspacing="1" border="0" class="table text-center">
			    <tbody>
                    <tr>
			            <th width="100%" colspan="7">进修学习情况</th>
                       <th width="21%"></th>
			        </tr>
{{for Data}}
			        <tr>
			        	<td rowspan="2">{{:#index+1}}</a>
			        	<td class="black">时间：</td>
			        	<td colspan="2">{{timeContent start_date end_date/}}</td>
			        	<td class="black" colspan="2">学习进修内容：</td>
			        	<td>{{:content}}</td>
                        {{if #index===0 }}
				         <td class="tablelink" rowspan="{{:personRowNum}}">
                            <input class="shuru" id="studyTrain_grade" type="text"  placeholder="输入" onblur="countSumGrade()">
                            <img class="qa" src="/masterreview/images/icon_qa.png" alt="">
                         </td>
				       {{else}}
				       {{/if}}
			        </tr>
                    <tr>
			        	<td class="black">学时：</td>
			        	<td>{{:class_hour}}</td>
			        	<td class="black">学习地点：</td>
			        	<td>{{:study_place}}</td>
			        	<td class="black">主办单位：</td>
			        	<td>{{:organizers}}</td>
			        </tr>
{{/for}}
			   </tbody>
			</table>
</script>


<!--学校等级评估 -->
<script id="gradeEvaluateRec" type="text/x-jsrender">	
<table cellpadding="0" cellspacing="1" border="0" class="table text-center">
			    <tbody>
                    <tr>
			            <th width="100%" colspan="6">学校等级评估</th>
			        </tr>
			        <tr>
			        	<td class="black">序号</td>
			        	<td class="black">义务教育</td>
			        	<td class="black">高中</td>
			        	<td class="black">中职</td>
                        <td class="black">证明材料</td>
	                    <td class="black">得分</td>
			        </tr>
{{for Data}}
    <tr>
      <td>{{:#index+1}}</td>
      <td>{{:compulsory_education}}</td>
      <td>{{:high_school}}</td>
      <td>{{:secondary_school}}</td>
  <td>
         {{if proveAttachMentVO.attachmentId !==null}}
             <a class="cha" href="<%=basePath%>WorkflowAttachMentDownload?attachmentId={{:proveAttachMentVO.attachmentId}}"> 点击查看</a>
        {{/if}}
      </td>
      <td class="tablelink" >
            <input class="shuru" id="gradeEvaluate_grade{{:#index+1}}" type="text"  placeholder="输入" onblur="countSumGrade()">
            <img class="qa" src="/masterreview/images/icon_qa.png" alt="">
      </td>
    </tr>
{{/for}}
</tbody>
</table>
</script>

<!--学校特色及改革  -->
<script id="schoolReformRec" type="text/x-jsrender">	
			<table cellpadding="0" cellspacing="1" border="0" class="table text-center">
			    <tbody>
                    <tr>
			            <th width="80%" colspan="7">学校特色及改革 </th>
                        <th width="20%">得分</th>
			        </tr>
{{for Data}}
			        <tr>
			        	<td rowspan="3">{{:#index+1}}</a>
			        	<td class="black">时间：</td>
			        	<td colspan="2">{{timeCovert implement_time/}}</td>
			        	<td class="black" colspan="2">学校特色创建及改革项目名称：</td>
			        	<td>{{:project_name}}</td>
                       {{if #index===0 }}
				         <td class="tablelink" rowspan="{{:personRowNum}}">
                             <label id="schoolReform_grade" name="schoolReform_grade"></label>
                         </td>
				       {{else}}
				       {{/if}}
			        </tr>
                    <tr>
			        	<td class="black">项目级别：</td>
			        	<td>{{:class_project_level}}</td>
			        	<td class="black">项目主管部门：</td>
			        	<td>{{:charge_department}}</td>
			        	<td class="black">项目完成情况：</td>
			        	<td>{{:performance}}</td>
			        </tr>

              <tr>
			    <td colspan="4" class="black">获奖证书：</td>
			    <td colspan="2">
                    {{if proveAttachMentVO.attachmentId !==null}}
                     <a class="cha" href="<%=basePath%>WorkflowAttachMentDownload?attachmentId={{:proveAttachMentVO.attachmentId}}"> 点击查看</a>
                    {{/if}}
                </td>
			 </tr>
{{/for}}
			   </tbody>
			</table>
</script>


<!--社会责任  -->
<script id="socialDutyRec" type="text/x-jsrender">	
			<table cellpadding="0" cellspacing="1" border="0" class="table text-center">
			    <tbody>
                    <tr>
			            <th width="80%" colspan="7">社会责任  </th>
                        <th width="20%">得分</th>
			        </tr>
{{for Data}}
			        <tr>
			        	<td rowspan="2">{{:#index+1}}</a>
			        	<td class="black">时间：</td>
			        	<td colspan="2">{{timeCovert implement_time/}}</td>
			        	<td class="black" colspan="2">承担上级部门安排的社会责任工作：</td>
			        	<td>{{:superior_task}}</td>
                        {{if #index===0 }}
				         <td class="tablelink" rowspan="{{:personRowNum}}">
                             <label id="socialDuty_grade" name="socialDuty_grade"></label>
                         </td>
				       {{else}}
				       {{/if}}
			        </tr>
                    <tr>
			        	<td class="black" colspan="1">工作安排部门：</td>
			        	<td>{{:arrange_department}}</td>
			        	<td class="black">完成情况：</td>
			        	<td colspan="1">{{:complete_state}}</td>
                        <td  class="black">获奖证书：</td>
			            <td >
                         {{if proveAttachMentVO.attachmentId !==null}}
                           <a class="cha" href="<%=basePath%>WorkflowAttachMentDownload?attachmentId={{:proveAttachMentVO.attachmentId}}"> 点击查看</a>
                         {{/if}}
                       </td>
			        </tr>
{{/for}}
			   </tbody>
			</table>
</script>

<!--减分责任事故 -->
<script id="accidentRec" type="text/x-jsrender">	
			<table cellpadding="0" cellspacing="1" border="0" class="table text-center">
			    <tbody>
                    <tr>
			            <th width="80%" colspan="7">责任事故 </th>
                        <th width="20%">得分</th>
			        </tr>
{{for Data}}
			        <tr>
			        	<td rowspan="2">{{:#index+1}}</a>
			        	<td class="black">时间：</td>
			        	<td colspan="2">{{timeCovert implement_time/}}</td>
			        	<td class="black" colspan="2">责任事故：</td>
			        	<td>{{:accident_name}}</td>
                       {{if #index===0 }}
				         <td class="tablelink" rowspan="{{:personRowNum}}">
                            <label id="accident_grade" name="accident_grade"></label>
                         </td>
				       {{else}}
				       {{/if}}
			        </tr>
                    <tr>
			        	<td class="black">违纪描述：</td>
			        	<td colspan="1">{{:description}}</td>
			        	<td class="black">处理结果：</td>
			        	<td colspan="1">{{:approve_result}}</td>
                        <td  class="black">证明材料：</td>
			            <td >
                         {{if proveAttachMentVO.attachmentId !==null}}
                           <a class="cha" href="<%=basePath%>WorkflowAttachMentDownload?attachmentId={{:proveAttachMentVO.attachmentId}}"> 点击查看</a>
                         {{/if}}
                       </td>
			        </tr>
{{/for}}
			   </tbody>
			</table>
</script>


<!--减分处分 -->
<script id="punishmentRec" type="text/x-jsrender">	
			<table cellpadding="0" cellspacing="1" border="0" class="table text-center">
			    <tbody>
                    <tr>
			            <th width="80%" colspan="9">处分 </th>
                        <th width="20%">得分</th>
			        </tr>
{{for Data}}
			        <tr>
			        	<td rowspan="2">{{:#index+1}}</a>
			        	<td class="black">时间：</td>
			        	<td colspan="2">{{timeCovert implement_time/}}</td>
			        	<td class="black" colspan="2">处分事件描述：</td>
			        	<td colspan="3">{{:description}}</td>
                        {{if #index===0 }}
				         <td class="tablelink" rowspan="{{:personRowNum}}">
                             <label id="punishment_grade" name="punishment_grade"></label>
                         </td>
				       {{else}}
				       {{/if}}
			        </tr>
                    <tr>
			        	<td class="black">受处分人：</td>
			        	<td>{{:people}}</td>
	                    <td class="black">处分部门：</td>
			        	<td>{{:department}}</td>
			        	<td class="black">处分结果：</td>
			        	<td>{{:process_result}}</td>
                         <td  class="black">证明材料：</td>
			            <td >
                         {{if proveAttachMentVO.attachmentId !==null}}
                           <a class="cha" href="<%=basePath%>WorkflowAttachMentDownload?attachmentId={{:proveAttachMentVO.attachmentId}}"> 点击查看</a>
                         {{/if}}
                       </td>
			        </tr>
{{/for}}
			   </tbody>
			</table>
</script>


<!--工作经历 -->
<script id="workHistoryRec" type="text/x-jsrender">	
<table cellpadding="0" cellspacing="1" border="0" class="table text-center">
			    <tbody>
                    <tr>
			            <th width="80%" colspan="4">工作经历</th>
			            <th width="20%"></th>
			        </tr>
			        <tr>
			        	<td class="black">序号</td>
			        	<td class="black">起止年月</td>
			        	<td class="black">工作单位</td>
			        	<td class="black">证明人</td>
	                    <td class="black">得分</td>
			        </tr>
{{for Data ~workHistory_grade_rows = workHistory_grade_rows}}
    <tr>
      <td>{{:#index+1}}</td>
      <td>{{timeContent start_date end_date/}}</td>
      <td>{{:work_company}}</td>
      <td>{{:prove_people}}</td>
      {{if #index===0 }}
        <td class="tablelink" rowspan="{{:~workHistory_grade_rows}}">   
           <label id="workHistory_grade" name="workHistory_grade"></label>
        </td>
	 {{else}}
	 {{/if}}
    </tr>
{{/for}}
</tbody>
</table>
</script>


<script>
$.views.tags({
	timeContent:function(startTime,endTime){
	   return Brightcom.workflow.getDateStrByLong(startTime)+'-'+Brightcom.workflow.getDateStrByLong(endTime);
	},
	timeCovert:function(time){
		   return Brightcom.workflow.getDateStrByLong(time);
	},
})
</script>




