<%@ page contentType="text/html; charset=GBK" %>
<%
  String approveType = (String)request.getParameter("approveType");
  String basePath = (String)request.getParameter("basePath");
%>


<!--�������� -->
<script id="workHistoryRec" type="text/x-jsrender">	
<table cellpadding="0" cellspacing="1" border="0" class="table text-center">
			    <tbody>
                    <tr>
			            <th width="80%" colspan="4">��������</th>
			            <th width="20%"></th>
			        </tr>
			        <tr>
			        	<td class="black">���</td>
			        	<td class="black">��ֹ����</td>
			        	<td class="black">������λ</td>
			        	<td class="black">֤����</td>
	                    <td class="black"></td>
			        </tr>
{{for Data}}
    <tr>
      <td>{{:#index+1}}</td>
      <td>{{timeContent start_date end_date/}}</td>
      <td>{{:work_company}}</td>
      <td>{{:prove_people}}</td>
      <td class="tablelink" >
            <input class="shuru" id="workHistory_grade{{:#index+1}}" type="text"  placeholder="����" onblur="countSumGrade()">
           <img class="qa" src="/masterreview/images/icon_qa.png" alt="">
      </td>
    </tr>
{{/for}}
</tbody>
</table>
</script>


<!--ѧУ�ȼ����� -->
<script id="gradeEvaluateRec" type="text/x-jsrender">	
<table cellpadding="0" cellspacing="1" border="0" class="table text-center">
			    <tbody>
                    <tr>
			            <th width="80%" colspan="5">ѧУ�ȼ�����</th>
			            <th width="20%"></th>
			        </tr>
			        <tr>
			        	<td class="black">���</td>
			        	<td class="black">�������</td>
			        	<td class="black">����</td>
			        	<td class="black">��ְ</td>
                        <td class="black">֤������</td>
	                    <td class="black"></td>
			        </tr>
{{for Data}}
    <tr>
      <td>{{:#index+1}}</td>
      <td>{{:compulsory_education}}</td>
      <td>{{:high_school}}</td>
      <td>{{:secondary_school}}</td>
     <td>
         {{if proveAttachMentVO.attachmentId !==null}}
             <a class="cha" href="<%=basePath%>WorkflowAttachMentDownload?attachmentId={{:proveAttachMentVO.attachmentId}}"> ����鿴</a>
        {{/if}}
      </td>
      <td class="tablelink" >
            <input class="shuru" id="gradeEvaluate_grade{{:#index+1}}" type="text"  placeholder="����" onblur="countSumGrade()">
           <img class="qa" src="/masterreview/images/icon_qa.png" alt="">
      </td>
    </tr>
{{/for}}
</tbody>
</table>
</script>

<!--ѧ����� -->
<script id="educationRec" type="text/x-jsrender">	
<table cellpadding="0" cellspacing="1" border="0" class="table text-center">
			    <tbody>
                    <tr>
			            <th width="79%" colspan="8">ѧ�����</th>
			            <th width="21%"></th>
			        </tr>
			        <tr>
			        	<td class="black">���</td>
			        	<td class="black">��ֹ����</td>
			        	<td class="black">�Ͷ�ѧУ</td>
			        	<td class="black">�Ͷ�רҵ</td>
			        	<td class="black">ѧ��</td>
                        <td class="black">ѧ��֤������</td>
			        	<td class="black">ѧλ</td>
			        	<td class="black">ѧλ֤������</td>
			        	<td class="black"></td>
			        </tr>
{{for Data}}
    <tr>
      <td>{{:#index+1}}</td>
      <td>{{timeContent startTime endTime/}}</td>
      <td>{{:studySchool}}</td>
      <td>{{:studyProfession}}</td>
      <td>{{:education}}</td>
      <td>
         {{if educationAttachMentVO.attachmentId !==null}}
             <a class="cha" href="<%=basePath%>WorkflowAttachMentDownload?attachmentId={{:educationAttachMentVO.attachmentId}}"> ����鿴</a>
        {{/if}}
      </td>
      <td>{{:degree}}</td>
      <td>
         {{if degreeAttachMentVO.attachmentId !==null}}
             <a class="cha" href="<%=basePath%>WorkflowAttachMentDownload?attachmentId={{:degreeAttachMentVO.attachmentId}}"> ����鿴</a>
        {{/if}}
      </td>
	 <td class="tablelink" >   
         <input id="education_grade" class="shuru" type="text"  placeholder="����" onblur="countSumGrade()">
		 <img class="qa" src="/masterreview/images/icon_qa.png" alt="">
      </td>
    </tr>
{{/for}}
</tbody>
</table>
</script>


<!--��ְ����  -->
<script id="workExperienceRec" type="text/x-jsrender">	
<table cellpadding="0" cellspacing="1" border="0" class="table text-center">
<tbody>
                    <tr>
			            <th width="79%" colspan="6">��ְ����</th>
			            <th width="21%"></th>
			        </tr>
			        <tr>
			        	<td class="black">���</td>
			        	<td class="black" style="width:213px;">��ֹ����</td>
			        	<td class="black">����ѧУ</td>
			        	<td class="black">����ְ��</td>
			        	<td class="black">��ְ����</td>
			        	<td class="black">֤������</td>
			        	<td class="black"></td>
			        </tr>
{{for Data}}
    <tr>
      <td>{{:#index+1}}</td>
      <td >{{timeContent startTime endTime/}}</td>
      <td>{{:workSchool}}</td>
      <td>{{:workProfession}}</td>
      <td>{{:workYear}}</td>
      <td>
         {{if proveAttachMentVO.attachmentId !==null}}
             <a class="cha" href="<%=basePath%>WorkflowAttachMentDownload?attachmentId={{:proveAttachMentVO.attachmentId}}"> ����鿴</a>
        {{/if}}
     </td>
     <td class="tablelink"  >   
         <input id="work_experience_grade" class="shuru" type="text"  placeholder="����" onblur="countSumGrade()">
		 <img class="qa" src="/masterreview/images/icon_qa.png" alt="">
     </td>
    </tr>
{{/for}}

</tbody>
</table>
</script>


<!--ְ����� -->
<script id="professionalTitleRec" type="text/x-jsrender">	
		<table cellpadding="0" cellspacing="1" border="0" class="table text-center">
			    <tbody>
                    <tr>
			            <th width="79%" colspan="4">ְ�����</th>
			            <th width="21%"></th>
			        </tr>
			        <tr>
			        	<td class="black">���</td>
			        	<td class="black">ְ��</td>
			        	<td class="black">ȡ��ְ��ʱ��</td>
			        	<td class="black">֤������</td>
	                    <td class="black"></td>
			        </tr>
{{for Data}}
    <tr>
      <td>{{:#index+1}}</td>
      <td>{{:professionaltitle_name}}</td>
      <td>{{timeCovert obtainTime/}}</td>
      <td>     
         {{if attachMentVO.attachmentId !==null}}
             <a class="cha" href="<%=basePath%>WorkflowAttachMentDownload?attachmentId={{:attachMentVO.attachmentId}}"> ����鿴</a>
         {{/if}}
      </td>
      <td class="tablelink"  >   
             <input id="professional_title_grade" class="shuru" type="text"  placeholder="����" onblur="countSumGrade()">
			 <img class="qa" src="/masterreview/images/icon_qa.png" alt="">
      </td>
    </tr>
{{/for}}

</tbody>
</table>
</script>

<!--���ķ������  -->
<script id="paperRec" type="text/x-jsrender">	
<table cellpadding="0" cellspacing="1" border="0" class="table text-center">
			    <tbody>
                    <tr>
			            <th width="79%" colspan="7">���ķ������</th>
			            <th width="21%"></th>
			        </tr>
{{for Data }}
                     <tr>
			        	<td rowspan="5">{{:#index+1}}</a>
			        	<td class="black text-right">��Ŀ��</td>
			        	<td colspan="2">{{:title}}</td>
			        	<td class="black text-right" colspan="2">��־���ƣ�</td>
			        	<td>{{:magazineMeetName}}</td>
				         <td class="tablelink" rowspan="5">
                             <input class="shuru" id="paper_grade{{:#index+1}}" type="text"  placeholder="����" onblur="countSumGrade()">
                             <img class="qa" src="/masterreview/images/icon_qa.png" alt="">
                         </td>
			        </tr>
                    <tr>
			        	<td class="black text-right">�������Ļ������ƣ�</td>
			        	<td>{{:paperMeetName}}</td>
			        	<td class="black text-right">���ţ�</td>
			        	<td>{{:paperNumber}}</td>
			        	<td class="black text-right">��������ʱ�䣺</td>
			        	<td>{{timeCovert publishTime/}}</td>
			        </tr>
                    <tr>
			        	<td class="black text-right">���쵥λ��</td>
			        	<td>{{:organizers}}</td>
			        	<td class="black" text-right>���쵥λ����</td>
			        	<td>{{:organizersLevel}}</td>
			        	<td class="black text-right">���˳е����֣�</td>
			        	<td>{{:personalPart}}</td>
			        </tr>

                     <tr>
			        	<td class="black">��ɷ�ʽ��</td>
			        	<td>{{:complete_way}}</td>
			        	<td class="black">��������</td>
			        	<td>{{:author_order}}</td>
			        	<td class="black">���浥λ��</td>
			        	<td>{{:publish_company}}</td>
			        </tr>


                     <tr>
			        	<td colspan="4" class="black">����ɨ�����</td>
			        	<td colspan="2">
                  {{if paperAttachMentVO.attachmentId !==null}}
                     <a class="cha" href="<%=basePath%>WorkflowAttachMentDownload?attachmentId={{:paperAttachMentVO.attachmentId}}"> ����鿴</a>
                  {{/if}}
                        </td>
			        </tr>
{{/for}}
</tbody>
</table>
</script>

<!--ר���������  -->
<script id="workPublishRec" type="text/x-jsrender">	
<table cellpadding="0" cellspacing="1" border="0" class="table text-center">
		<tbody>
                    <tr>
			            <th width="79%" colspan="7">ר���������</th>
			            <th width="21%"></th>
			        </tr>
{{for Data}}
			        <tr>
			        	<td rowspan="3">{{:#index+1}}</a>
			        	<td class="black text-right">������</td>
			        	<td colspan="5">{{:book_name}}</td>
				         <td class="tablelink" rowspan="3" >
                            <input class="shuru" id="work_publish_grade" type="text"  placeholder="����" onblur="countSumGrade()">
                           <img class="qa" src="/masterreview/images/icon_qa.png" alt="">
                         </td>
			        </tr>
                    <tr>
			        	<td class="black text-right">��ɷ�ʽ��</td>
			        	<td>{{:complete_way}}</td>
			        	<td class="black text-right">����ʱ�䣺</td>
			        	<td>{{timeCovert publish_time/}} </td>
			        	<td class="black text-right">�������������</td>
			        	<td>{{:complete_word}}</td>
			        </tr>
                    <tr>
			        	<td class="black text-right">���˳е����֣�</td>
			        	<td>{{:complete_chapter}}</td>
			        	<td class="black text-right">��������</td>
			        	<td colspan="1">{{:author_order}}</td>
                         <td class="black">�����磺</td>
			        	<td colspan="1">{{:publish_company}}</td>
		        	</tr>
{{/for}}
		</tbody>
</table>
</script>


<!--�������  -->
<script id="subjectRec" type="text/x-jsrender">	
			<table cellpadding="0" cellspacing="1" border="0" class="table text-center">
			    <tbody>
                    <tr>
			            <th width="79%" colspan="7">�̿������</th>
			            <th width="21%"></th>
			        </tr>
{{for Data}}
			        <tr>
			        	<td rowspan="5">{{:#index+1}}</a>
			        	<td class="black text-right">�������ƣ�</td>
			        	<td colspan="2">{{:subjectName}}</td>
			        	<td class="black text-right" colspan="2">�������λ��</td>
			        	<td>{{:subjectCompany}}</td>
				         <td class="tablelink" rowspan="5" >
                            <input class="shuru" id="subject_grade" type="text" placeholder="����" onblur="countSumGrade()">
                            <img class="qa" src="/masterreview/images/icon_qa.png" alt="" >
                         </td>
			        </tr>
                    <tr>
			        	<td class="black text-right">���⼶��</td>
			        	<td colspan="2">{{:subjectLevel}}</td>
			        	<td class="black text-right">�Ƿ���⣺</td>
			        	<td colspan="2">{{:isfinishSubject}}</td>
			        </tr>
                    <tr>
			        	<td class="black text-right">����ʱ�䣺</td>
			        	<td>{{timeCovert finishTime/}}</td>
			        	<td class="black text-right">�������</td>
			        	<td colspan="3">{{:finishResult}}</td>
		        	</tr>

                    <tr>
	                    <td class="black" colspan="2">����ְ��</td>
			        	<td colspan="4">{{:subjectRresponsibility}}</td>
                   </tr>

                     <tr>
	                    <td class="black" colspan="2">�����飺</td>
			        	<td colspan="4">    
                     {{if subjectAttachVO.attachmentId !==null}}
                       <a class="cha" href="<%=basePath%>WorkflowAttachMentDownload?attachmentId={{:subjectAttachVO.attachmentId}}"> ����鿴</a>
                      {{/if}}
                      </td>
                   </tr>
{{/for}}
			   </tbody>
			</table>
</script>


<!--���˻����  -->
<script id="personalAwardRec" type="text/x-jsrender">	
			<table cellpadding="0" cellspacing="1" border="0" class="table text-center">
			    <tbody>
                    <tr>
			            <th width="79%" colspan="7">���˻����</th>
			            <th width="21%"></th>
			        </tr>
{{for Data}}
			        <tr>
			        	<td rowspan="4">{{:#index+1}}</td>
			        	<td class="black text-right">�����ƣ�</td>
			        	<td colspan="2">{{:awardsName}}</td>
			        	<td class="black text-right">���õ�λ��</td>
			        	<td colspan="2"> {{:awardsCompany}}</td>
				         <td class="tablelink" rowspan="4" >
                            <input class="shuru" id="personal_award_grade" type="text"  placeholder="����" onblur="countSumGrade()">
                            <img class="qa" src="/masterreview/images/icon_qa.png" alt="">
                         </td>
			        </tr>
                    <tr>
			        	<td class="black text-right">�񽱼���</td>
			        	<td>{{:awardsLevel}}</td>
			        	<td class="black text-right">����ʱ�䣺</td>
			        	<td colspan="3">{{timeCovert awardsTime/}}</td>
		        	</tr>
              <tr>
               <td class="black">�������1��</td>
			   <td >{{:awards_type}}</td>
			    <td colspan="2" class="black">��֤��1��</td>
			    <td colspan="2">
                    {{if personalAttachVO.attachmentId !==null}}
                     <a class="cha" href="<%=basePath%>WorkflowAttachMentDownload?attachmentId={{:personalAttachVO.attachmentId}}"> ����鿴</a>
                    {{/if}}
                </td>
			 </tr>

                  <tr>
                        <td class="black">�������2��</td>
			        	<td >{{:awards_type1}}</td>
			        	<td colspan="2" class="black">��֤��2��</td>
			        	<td colspan="2">  
                         {{if personalAttachVO1.attachmentId !==null}}
                         <a class="cha" href="<%=basePath%>WorkflowAttachMentDownload?attachmentId={{:personalAttachVO1.attachmentId}}"> ����鿴</a>
                         {{/if}}
                      </td>
		        	</tr>
{{/for}}
			   </tbody>
			</table>
</script>


<!--ѧУ�����  -->
<script id="schoolAwardRec" type="text/x-jsrender">	
			<table cellpadding="0" cellspacing="1" border="0" class="table text-center">
			    <tbody>
                    <tr>
			            <th width="79%" colspan="7">ѧУ�����</th>
			            <th width="21%"></th>
			        </tr>
{{for Data}}
			        <tr>
			        	<td rowspan="3">{{:#index+1}}</a>
			        	<td class="black text-right">�������ƣ�</td>
			        	<td colspan="2">{{:awardsName}}</td>
			        	<td class="black text-right" colspan="2">���õ�λ��</td>
			        	<td>{{:awardsCompany}}</td>
				         <td class="tablelink" rowspan="3">
                            <input class="shuru" id="school_award_grade" type="text"  placeholder="����" onblur="countSumGrade()">
                            <img class="qa" src="/masterreview/images/icon_qa.png" alt="">
                         </td>
			        </tr>
                    <tr>
			        	<td class="black text-right">��ְѧУ��</td>
			        	<td>{{:workSchool}}</td>
			        	<td class="black text-right">�񽱼���</td>
			        	<td>{{:awardsLevel}}</td>
			        	<td class="black text-right">����ʱ�䣺</td>
			        	<td>{{timeCovert awardsTime/}}</td>
			        </tr>

             <tr>
			   <td colspan="4" class="black">��֤�飺</td>
			   <td colspan="2">
                    {{if schoolAttachVO.attachmentId !==null}}
                     <a class="cha" href="<%=basePath%>WorkflowAttachMentDownload?attachmentId={{:schoolAttachVO.attachmentId}}"> ����鿴</a>
                    {{/if}}
               </td>
			 </tr>
{{/for}}
			   </tbody>
			</table>
</script>



<!--����ѧϰ���  -->
<script id="studyTrainRec" type="text/x-jsrender">	
			<table cellpadding="0" cellspacing="1" border="0" class="table text-center">
			    <tbody>
                    <tr>
			            <th width="100%" colspan="7">����ѧϰ���</th>
			        </tr>
{{for Data}}
			        <tr>
			        	<td rowspan="2">{{:#index+1}}</a>
			        	<td class="black">ʱ�䣺</td>
			        	<td colspan="2">{{timeContent start_date end_date/}}</td>
			        	<td class="black" colspan="2">ѧϰ�������ݣ�</td>
			        	<td>{{:content}}</td>
				         <td class="tablelink" rowspan="2">
                            <input class="shuru" id="studyTrain_grade" type="text"  placeholder="����" onblur="countSumGrade()">
                            <img class="qa" src="/masterreview/images/icon_qa.png" alt="">
                         </td>
			        </tr>
                    <tr>
			        	<td class="black">ѧʱ��</td>
			        	<td>{{:class_hour}}</td>
			        	<td class="black">ѧϰ�ص㣺</td>
			        	<td>{{:study_place}}</td>
			        	<td class="black">���쵥λ��</td>
			        	<td>{{:organizers}}</td>
			        </tr>
{{/for}}
			   </tbody>
			</table>
</script>


<!--ѧУ��ɫ���ĸ�  -->
<script id="schoolReformRec" type="text/x-jsrender">	
			<table cellpadding="0" cellspacing="1" border="0" class="table text-center">
			    <tbody>
                    <tr>
			            <th width="79%" colspan="7">ѧУ��ɫ���ĸ� </th>
                        <th width="21%" colspan="1"></th>
			        </tr>
{{for Data}}
			        <tr>
			        	<td rowspan="3">{{:#index+1}}</a>
			        	<td class="black">ʱ�䣺</td>
			        	<td colspan="2">{{timeCovert implement_time/}}</td>
			        	<td class="black" colspan="2">ѧУ��ɫ�������ĸ���Ŀ���ƣ�</td>
			        	<td>{{:project_name}}</td>
				         <td class="tablelink" rowspan="3">
                            <input class="shuru" id="schoolReform_grade" type="text"  placeholder="����" onblur="countSumGrade()">
                            <img class="qa" src="/masterreview/images/icon_qa.png" alt="">
                         </td>
			        </tr>
                    <tr>
			        	<td class="black">��Ŀ����</td>
			        	<td>{{:class_project_level}}</td>
			        	<td class="black">��Ŀ���ܲ��ţ�</td>
			        	<td>{{:charge_department}}</td>
			        	<td class="black">��Ŀ��������</td>
			        	<td>{{:performance}}</td>
			        </tr>

              <tr>
			    <td colspan="4" class="black">��֤�飺</td>
			    <td colspan="2">
                    {{if proveAttachMentVO.attachmentId !==null}}
                     <a class="cha" href="<%=basePath%>WorkflowAttachMentDownload?attachmentId={{:proveAttachMentVO.attachmentId}}"> ����鿴</a>
                    {{/if}}
                </td>
			 </tr>
{{/for}}
			   </tbody>
			</table>
</script>


<!--�������  -->
<script id="socialDutyRec" type="text/x-jsrender">	
			<table cellpadding="0" cellspacing="1" border="0" class="table text-center">
			    <tbody>
                    <tr>
			            <th width="79%" colspan="7">�������  </th>
                        <th width="21%"></th>
			        </tr>
{{for Data}}
			        <tr>
			        	<td rowspan="2">{{:#index+1}}</a>
			        	<td class="black">ʱ�䣺</td>
			        	<td colspan="2">{{timeCovert implement_time/}}</td>
			        	<td class="black" colspan="2">�е��ϼ����Ű��ŵ�������ι�����</td>
			        	<td>{{:superior_task}}</td>
				        <td class="tablelink" rowspan="2">
                            <input class="shuru" id="socialDuty_grade" type="text"  placeholder="����" onblur="countSumGrade()">
                            <img class="qa" src="/masterreview/images/icon_qa.png" alt="">
                         </td>
			        </tr>
                    <tr>
			        	<td class="black">�������Ų��ţ�</td>
			        	<td colspan="1">{{:arrange_department}}</td>
			        	<td class="black">��������</td>
			        	<td colspan="1">{{:complete_state}}</td>
                        <td  class="black">��֤�飺</td>
			            <td >
                         {{if proveAttachMentVO.attachmentId !==null}}
                           <a class="cha" href="<%=basePath%>WorkflowAttachMentDownload?attachmentId={{:proveAttachMentVO.attachmentId}}"> ����鿴</a>
                         {{/if}}
                       </td>
			        </tr>
{{/for}}
			   </tbody>
			</table>
</script>

<!--���������¹� -->
<script id="accidentRec" type="text/x-jsrender">	
			<table cellpadding="0" cellspacing="1" border="0" class="table text-center">
			    <tbody>
                    <tr>
			            <th width="79%" colspan="7">�����¹� </th>
                        <th width="21%"></th>
			        </tr>
{{for Data}}
			        <tr>
			        	<td rowspan="2">{{:#index+1}}</a>
			        	<td class="black">ʱ�䣺</td>
			        	<td colspan="2">{{timeCovert implement_time/}}</td>
			        	<td class="black" colspan="2">�����¹ʣ�</td>
			        	<td>{{:accident_name}}</td>
				         <td class="tablelink" rowspan="2">
                            <input class="shuru" id="accident_grade" type="text"  placeholder="����" onblur="countSumGrade()">
                            <img class="qa" src="/masterreview/images/icon_qa.png" alt="">
                         </td>
			        </tr>
                    <tr>
			        	<td class="black">Υ��������</td>
			        	<td colspan="1">{{:description}}</td>
			        	<td class="black">��������</td>
			        	<td colspan="1">{{:approve_result}}</td>
                       <td  class="black">֤�����ϣ�</td>
			            <td >
                         {{if proveAttachMentVO.attachmentId !==null}}
                           <a class="cha" href="<%=basePath%>WorkflowAttachMentDownload?attachmentId={{:proveAttachMentVO.attachmentId}}"> ����鿴</a>
                         {{/if}}
                       </td>
			        </tr>
{{/for}}
			   </tbody>
			</table>
</script>


<!--���ִ��� -->
<script id="punishmentRec" type="text/x-jsrender">	
			<table cellpadding="0" cellspacing="1" border="0" class="table text-center">
			    <tbody>
                    <tr>
			            <th width="79%" colspan="9">���� </th>
                        <th width="21%"></th>
			        </tr>
{{for Data}}
			        <tr>
			        	<td rowspan="2">{{:#index+1}}</a>
			        	<td class="black">ʱ�䣺</td>
			        	<td colspan="2">{{timeCovert implement_time/}}</td>
			        	<td class="black" colspan="2">�����¼�������</td>
			        	<td colspan="3">{{:description}}</td>
				         <td class="tablelink" rowspan="2">
                            <input class="shuru" id="punishment_grade" type="text"  placeholder="����" onblur="countSumGrade()">
                            <img class="qa" src="/masterreview/images/icon_qa.png" alt="">
                         </td>
			        </tr>
                    <tr>
			        	<td class="black">�ܴ����ˣ�</td>
			        	<td colspan="1">{{:people}}</td>
	                    <td class="black">���ֲ��ţ�</td>
			        	<td colspan="1">{{:department}}</td>
			        	<td class="black">���ֽ����</td>
			        	<td>{{:process_result}}</td>
                        <td  class="black">֤�����ϣ�</td>
			            <td >
                         {{if proveAttachMentVO.attachmentId !==null}}
                           <a class="cha" href="<%=basePath%>WorkflowAttachMentDownload?attachmentId={{:proveAttachMentVO.attachmentId}}"> ����鿴</a>
                         {{/if}}
                       </td>
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




