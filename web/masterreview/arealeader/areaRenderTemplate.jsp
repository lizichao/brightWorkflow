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
			            <th width="88%" colspan="4">��������</th>
			            <th width="12%"></th>
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
         <input type='checkbox' id="workHistory_check" name='workHistory{{:#index+1}}' value='{{:id}}'  />
      <!-- <a class="shu" href="javascript:void(0);" onclick="rollBackOpen('workHistory','{{:id}}')">�˻�</a> -->
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
			            <th width="88%" colspan="5">ѧУ�ȼ�����</th>
			            <th width="12%"></th>
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
        <input type='checkbox' id="gradeEvaluate_check" name='gradeEvaluate{{:#index+1}}' value='{{:id}}'/>
       <!-- <a class="shu" href="javascript:void(0);" onclick="rollBackOpen('gradeEvaluate','{{:id}}')">�˻�</a>--> 
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
			            <th width="88%" colspan="8">ѧ�����</th>
			            <th width="12%"></th>
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
{{for Data }}
    <tr>
      <td>{{:#index+1}}</td>
      <td>{{timeContent startTime endTime/}}</td>
      <td>{{:studySchool}}</td>
      <td>{{:studyProfession}}</td>
      <td>{{:educationDesc}}</td>
      <td>
         {{if educationAttachMentVO.attachmentId !==null}}
             <a class="cha" href="<%=basePath%>WorkflowAttachMentDownload?attachmentId={{:educationAttachMentVO.attachmentId}}"> ����鿴</a>
        {{/if}}
      </td>
      <td>{{:degreeDesc}}</td>
      <td>
         {{if degreeAttachMentVO.attachmentId !==null}}
             <a class="cha" href="<%=basePath%>WorkflowAttachMentDownload?attachmentId={{:degreeAttachMentVO.attachmentId}}"> ����鿴</a>
        {{/if}}
      </td>
      <td class="tablelink" >
          <input type='checkbox' id="education_check" name='education{{:#index+1}}' value='{{:id}}'/>
      <!-- <a class="shu" href="javascript:void(0);" onclick="rollBackOpen('education','{{:id}}')">�˻�</a> -->
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
			            <th width="88%" colspan="6">��ְ����</th>
			            <th width="12%"></th>
			        </tr>
			        <tr>
			        	<td class="black">���</td>
			        	<td class="black">��ֹ����</td>
			        	<td class="black">����ѧУ</td>
			        	<td class="black">����ְ��</td>
			        	<td class="black">��ְ����</td>
			        	<td class="black">֤������</td>
  	                    <td class="black"></td>
			        </tr>
{{for Data}}
    <tr>
      <td>{{:#index+1}}</td>
      <td>{{timeContent startTime endTime/}}</td>
      <td>{{:workSchool}}</td>
      <td>{{:workProfession}}</td>
      <td>{{:workYear}}</td>
      <td>
         {{if proveAttachMentVO.attachmentId !==null}}
             <a class="cha" href="<%=basePath%>WorkflowAttachMentDownload?attachmentId={{:proveAttachMentVO.attachmentId}}"> ����鿴</a>
         {{/if}}
      </td>
      <td class="tablelink" >
         <input type='checkbox' id="workExperience_check" name='workExperience{{:#index+1}}' value='{{:id}}'/>
        <!--  <a class="shu" href="javascript:void(0);" onclick="rollBackOpen('workExperience','{{:id}}')">�˻�</a>--> 
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
			            <th width="88%" colspan="4">ְ�����</th>
			            <th width="12%"></th>
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
      <td class="tablelink" >
           <input type='checkbox' id="professionalTitle_check" name='professionalTitle{{:#index+1}}' value='{{:id}}'/>
         <!--  <a class="shu" href="javascript:void(0);" onclick="rollBackOpen('professionalTitle','{{:id}}')">�˻�</a>-->
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
			            <th width="88%" colspan="7">���ķ������</th>
			            <th width="12%"></th>
			        </tr>
{{for Data}}
                     <tr>
			        	<td rowspan="5">{{:#index+1}}</a>
			        	<td class="black">��Ŀ��</td>
			        	<td colspan="2">{{:title}}</td>
			        	<td class="black" colspan="2">��־���ƣ�</td>
			        	<td>{{:magazineMeetName}}</td>
			        	<td class="tablelink" rowspan="5">
                           <input type='checkbox' id="paper_check" name='paper{{:#index+1}}' value='{{:id}}'/>
                          <!--  <a class="shu" href="javascript:void(0);" onclick="rollBackOpen('paper','{{:id}}')">�˻�</a>-->
                        </td>
			        </tr>
                    <tr>
			        	<td class="black">�������Ļ������ƣ�</td>
			        	<td>{{:paperMeetName}}</td>
			        	<td class="black">���ţ�</td>
			        	<td>{{:paperNumber}}</td>
			        	<td class="black">��������ʱ�䣺</td>
			        	<td>{{timeCovert publishTime/}}</td>
			        </tr>
                    <tr>
			        	<td class="black">���쵥λ��</td>
			        	<td>{{:organizers}}</td>
			        	<td class="black">���쵥λ����</td>
			        	<td>{{:organizersLevelDesc}}</td>
			        	<td class="black">���˳е����֣�</td>
			        	<td>{{:personalPart}}</td>
			        </tr>

                     <tr>
			        	<td class="black">��ɷ�ʽ��</td>
			        	<td>{{:complete_way_desc}}</td>
			        	<td class="black">��������</td>
			        	<td>{{:author_order_desc}}</td>
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
			            <th width="88%" colspan="7">ר���������</th>
			            <th width="12%"></th>
			        </tr>
{{for Data}}
			        <tr>
			        	<td rowspan="3">{{:#index+1}}</a>
			        	<td class="black">������</td>
			        	<td colspan="5">{{:book_name}}</td>
                        <td class="tablelink" rowspan="3">
                            <input type='checkbox' id="workPublish_check" name='workPublish{{:#index+1}}' value='{{:id}}'/>
                          <!-- <a class="shu" href="javascript:void(0);" onclick="rollBackOpen('workPublish','{{:id}}')">�˻�</a>-->
                        </td>
			        </tr>
                    <tr>
			        	<td class="black">��ɷ�ʽ��</td>
			        	<td>{{:complete_way_desc}}</td>
			        	<td class="black">����ʱ�䣺</td>
			        	<td>{{timeCovert publish_time/}} </td>
			        	<td class="black">�������������</td>
			        	<td>{{:complete_word}}</td>
			        </tr>
                    <tr>
			        	<td class="black">���˳е����֣�</td>
			        	<td>{{:complete_chapter}}</td>
			        	<td class="black">��������</td>
			        	<td colspan="1">{{:author_order_desc}}</td>
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
			            <th width="88%" colspan="7">�̿������</th>
			            <th width="12%"></th>
			        </tr>
{{for Data}}
			        <tr>
			        	<td rowspan="5">{{:#index+1}}</a>
			        	<td class="black">�������ƣ�</td>
			        	<td colspan="2">{{:subjectName}}</td>
			        	<td class="black" colspan="2">�������λ��</td>
			        	<td>{{:subjectCompany}}</td>
                        <td class="tablelink" rowspan="5">
                          <input type='checkbox' id="subject_check" name='subject{{:#index+1}}' value='{{:id}}'/>
                        <!--   <a class="shu" href="javascript:void(0);" onclick="rollBackOpen('subject','{{:id}}')">�˻�</a>-->
                        </td>
			        </tr>
                    <tr>
			        	<td class="black">���⼶��</td>
			        	<td colspan="2">{{:subjectLevelDesc}}</td>
			        	<td class="black"  colspan="2">�Ƿ���⣺</td>
			        	<td>{{:isfinishSubjectDesc}}</td>
			        </tr>
                    <tr>
			        	<td class="black">����ʱ�䣺</td>
			        	<td>{{timeCovert finishTime/}}</td>
			        	<td class="black">�������</td>
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
			            <th width="88%" colspan="7">���˻����</th>
			            <th width="12%"></th>
			        </tr>
{{for Data}}
			        <tr>
			        	<td rowspan="4">{{:#index+1}}</a>
			        	<td class="black">�����ƣ�</td>
			        	<td colspan="2">{{:awardsName}}</td>
			        	<td class="black">���õ�λ��</td>
			        	<td colspan="2">{{:awardsCompany}}</td>
                        <td class="tablelink" rowspan="4">
                          <input type='checkbox' id="personalAward_check" name='personalAward{{:#index+1}}' value='{{:id}}'/>
                          <!-- <a class="shu" href="javascript:void(0);" onclick="rollBackOpen('personalAward','{{:id}}')">�˻�</a>-->
                        </td>
			        </tr>
                    <tr>
			        	<td class="black">�񽱼���</td>
			        	<td>{{:awardsLevelDesc}}</td>
			        	<td class="black">����ʱ�䣺</td>
			        	<td colspan="3">{{timeCovert awardsTime/}}</td>
		        	</tr>

              <tr>
	            <td class="black">�������1��</td>
			    <td >{{:awards_type_desc}}</td>
			    <td colspan="2" class="black">��֤��1��</td>
			    <td colspan="2">
                    {{if personalAttachVO.attachmentId !==null}}
                     <a class="cha" href="<%=basePath%>WorkflowAttachMentDownload?attachmentId={{:personalAttachVO.attachmentId}}"> ����鿴</a>
                    {{/if}}
                </td>
			 </tr>

              <tr>
                        <td class="black">�������2��</td>
			        	<td >{{:awards_type1_desc}}</td>
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
			            <th width="88%" colspan="7">ѧУ�����</th>
			            <th width="12%"></th>
			        </tr>
{{for Data}}
			        <tr>
			        	<td rowspan="3">{{:#index+1}}</a>
			        	<td class="black">�������ƣ�</td>
			        	<td colspan="2">{{:awardsName}}</td>
			        	<td class="black" colspan="2">���õ�λ��</td>
			        	<td>{{:awardsCompany}}</td>
                        <td class="tablelink" rowspan="3">
                              <input type='checkbox' id="schoolAward_check" name='schoolAward{{:#index+1}}' value='{{:id}}'/>
                           <!--<a class="shu" href="javascript:void(0);" onclick="rollBackOpen('schoolAward','{{:id}}')">�˻�</a>-->
                        </td>
			        </tr>
                    <tr>
			        	<td class="black">��ְѧУ��</td>
			        	<td>{{:workSchool}}</td>
			        	<td class="black">�񽱼���</td>
			        	<td>{{:awardsLevelDesc}}</td>
			        	<td class="black">����ʱ�䣺</td>
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
			            <th width="88%" colspan="7">����ѧϰ���</th>
                        <th width="12%"></th>
			        </tr>
{{for Data}}
			        <tr>
			        	<td rowspan="2">{{:#index+1}}</a>
			        	<td class="black">ʱ�䣺</td>
			        	<td colspan="2">{{timeContent start_date end_date/}}</td>
			        	<td class="black" colspan="2">ѧϰ�������ݣ�</td>
			        	<td>{{:content}}</td>
                        <td class="tablelink" rowspan="2">
                           <input type='checkbox' id="studyTrain_check" name='studyTrain{{:#index+1}}' value='{{:id}}'/>
                           <!--<a class="shu" href="javascript:void(0);" onclick="rollBackOpen('studyTrain','{{:id}}')">�˻�</a>-->
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
			            <th width="88%" colspan="7">ѧУ��ɫ���ĸ� </th>
                        <th width="12%"></th>
			        </tr>
{{for Data}}
			        <tr>
			        	<td rowspan="3">{{:#index+1}}</a>
			        	<td class="black">ʱ�䣺</td>
			        	<td colspan="2">{{timeCovert implement_time/}}</td>
			        	<td class="black" colspan="2">ѧУ��ɫ�������ĸ���Ŀ���ƣ�</td>
			        	<td>{{:project_name}}</td>
                        <td class="tablelink" rowspan="3">
                           <input type='checkbox' id="schoolReform_check" name='schoolReform{{:#index+1}}' value='{{:id}}'/>
                          <!-- <a class="shu" href="javascript:void(0);" onclick="rollBackOpen('schoolReform','{{:id}}')">�˻�</a>-->
                        </td>
			        </tr>
                    <tr>
			        	<td class="black">��Ŀ����</td>
			        	<td>{{:project_level_desc}}</td>
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
			            <th width="88%" colspan="7">�������  </th>
                        <th width="12%"></th>
			        </tr>
{{for Data}}
			        <tr>
			        	<td rowspan="2">{{:#index+1}}</a>
			        	<td class="black">ʱ�䣺</td>
			        	<td colspan="2">{{timeCovert implement_time/}}</td>
			        	<td class="black" colspan="2">�е��ϼ����Ű��ŵ�������ι�����</td>
			        	<td>{{:superior_task}}</td>
                        <td class="tablelink" rowspan="2">
                          <input type='checkbox' id="socialDuty_check" name='socialDuty{{:#index+1}}' value='{{:id}}'/>
                          <!-- <a class="shu" href="javascript:void(0);" onclick="rollBackOpen('socialDuty','{{:id}}')">�˻�</a>-->
                        </td>
			        </tr>
                    <tr>
			        	<td class="black">�������Ų��ţ�</td>
			        	<td>{{:arrange_department}}</td>
			        	<td class="black">��������</td>
			        	<td>{{:complete_state}}</td>
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
			            <th width="88%" colspan="7">�����¹� </th>
                        <th width="12%"></th>
			        </tr>
{{for Data}}
			        <tr>
			        	<td rowspan="2">{{:#index+1}}</a>
			        	<td class="black">ʱ�䣺</td>
			        	<td colspan="2">{{timeCovert implement_time/}}</td>
			        	<td class="black" colspan="2">�����¹����ƣ�</td>
			        	<td>{{:accident_name}}</td>
                        <td class="tablelink" rowspan="2">
                            <input type='checkbox' id="accident_check" name='accident{{:#index+1}}' value='{{:id}}'/>
                           <!--<a class="shu" href="javascript:void(0);" onclick="rollBackOpen('accident','{{:id}}')">�˻�</a>-->
                        </td>
			        </tr>
                    <tr>
			        	<td class="black">Υ��������</td>
			        	<td>{{:description}}</td>
			        	<td class="black">��������</td>
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


<!--���ִ��� -->
<script id="punishmentRec" type="text/x-jsrender">	
			<table cellpadding="0" cellspacing="1" border="0" class="table text-center">
			    <tbody>
                    <tr>
			            <th width="88%" colspan="9">���� </th>
                        <th width="12%"></th>
			        </tr>
{{for Data}}
			        <tr>
			        	<td rowspan="2">{{:#index+1}}</a>
			        	<td class="black">ʱ�䣺</td>
			        	<td colspan="2">{{timeCovert implement_time/}}</td>
			        	<td class="black" colspan="2">�����¼�������</td>
			        	<td colspan="3">{{:description}}</td>
                        <td class="tablelink" rowspan="2">
                          <input type='checkbox' id="punishment_check" name='punishment{{:#index+1}}' value='{{:id}}'/>
                          <!-- <a class="shu" href="javascript:void(0);" onclick="rollBackOpen('punishment','{{:id}}')">�˻�</a>-->
                        </td>
			        </tr>
                    <tr>
			        	<td class="black">�ܴ����ˣ�</td>
			        	<td>{{:people}}</td>
	                    <td class="black">���ֲ��ţ�</td>
			        	<td>{{:department}}</td>
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




