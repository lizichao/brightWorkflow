<%@ page contentType="text/html; charset=GBK" %>
<%
  String approveType = (String)request.getParameter("approveType");
  String basePath = (String)request.getParameter("basePath");
%>



<!--ѧ����� -->
<script id="educationRec" type="text/x-jsrender">	
<table cellpadding="0" cellspacing="1" border="0" class="table text-center">
			    <tbody>
                    <tr>
			            <th width="80%" colspan="7">ѧ�����</th>
			            <th width="20%"></th>
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
			        </tr>
{{for Data}}
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
			            <th width="80%" colspan="5">��ְ����</th>
			            <th width="20%"></th>
			        </tr>
			        <tr>
			        	<td class="black">���</td>
			        	<td class="black">��ֹ����</td>
			        	<td class="black">����ѧУ</td>
			        	<td class="black">����ְ��</td>
			        	<td class="black">��ְ����</td>
			        	<td class="black">֤������</td>
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
			            <th width="80%" colspan="3">ְ�����</th>
			            <th width="20%"></th>
			        </tr>
			        <tr>
			        	<td class="black">���</td>
			        	<td class="black">ְ��</td>
			        	<td class="black">ȡ��ְ��ʱ��</td>
			        	<td class="black">֤������</td>
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
				<th width="100%" colspan="5">���ķ������</th>
			</tr>
			{{for Data}}
            	<tr>
					<td rowspan="6" width="5%">{{:#index+1}}</a>
					<td class="black" width="15%">��Ŀ��</td>
					<td width="32.5%">{{:title}}</td>
					<td class="black" width="15%">��������ʱ�䣺</td>
					<td width="32.5%">{{timeCovert publishTime/}}</td>
				</tr>
				<tr>
			        <td class="black">��־���ƣ�</td>
			        <td>{{:magazineMeetName}}</td>
			        <td class="black">���쵥λ��</td>
			        <td>{{:organizers}}</td>
				</tr>
				<tr>
			        <td class="black">�������Ļ������ƣ�</td>
			        <td>{{:paperMeetName}}</td>
			        <td class="black">���쵥λ����</td>
			        <td>{{:organizersLevelDesc}}</td>
				</tr>
				<tr>
					<td class="black">���ţ�</td>
			        <td>{{:paperNumber}}</td>
					<td class="black">���˳е����֣�</td>
			        <td>{{:personalPart}}</td>
				</tr>	
				<tr>
			        <td class="black">��ɷ�ʽ��</td>
			        <td>{{:complete_way_desc}}</td>
					<td class="black">��������</td>
			        <td>{{:author_order_desc}}</td>
				</tr>
                <tr>
			       	<td class="black">����ɨ�����</td>
			        <td colspan="3">
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
				<th width="100%" colspan="7">ר���������</th>
			</tr>
			{{for Data}}
				<tr>
			    	<td rowspan="4" width="5%">{{:#index+1}}</a>
			        <td class="black" width="15%">������</td>
			        <td colspan="5" width="85%">{{:book_name}}</td>
			    </tr>
               	<tr>
			        <td class="black" width="15%">��ɷ�ʽ��</td>
			        <td width="15%">{{:complete_way_desc}}</td>
			        <td class="black" width="15%">����ʱ�䣺</td>
			        <td width="15%">{{timeCovert publish_time/}} </td>
			        <td class="black" width="15%">�������������</td>
			        <td width="15%">{{:complete_word}}</td>
			    </tr>
                <tr>
			        <td class="black">���˳е����֣�</td>
			        <td>{{:complete_chapter}}</td>
			        <td class="black">��������</td>
			        <td>{{:author_order_desc}}</td>
                    <td class="black">�����磺</td>
			        <td>{{:publish_company}}</td>
		        </tr>
				<tr>
			        <td class="black">֤�����ϣ�</td>
			        <td colspan="6">
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
<script id="subjectRec" type="text/x-jsrender">	
	<table cellpadding="0" cellspacing="1" border="0" class="table text-center">
		<tbody>
        	<tr>
				<th width="100%" colspan="5">�̿������</th>
			</tr>
			{{for Data}}
				<tr>
			        <td rowspan="6" width="5%">{{:#index+1}}</a>
			        <td class="black" width="15%">�������ƣ�</td>
			        <td width="32.5%">{{:subjectName}}</td>
			        <td class="black" width="15%">�������λ��</td>
			        <td width="32.5%">{{:subjectCompany}}</td>
			    </tr>
				<tr>
			       	<td class="black">���⼶��</td>
			        <td>{{:subjectLevelDesc}}</td>
			        <td class="black">����ʱ�䣺</td>
			        <td>{{timeCovert projectTime/}}</td>
			    </tr>
				<tr>
			        <td class="black">�Ƿ���⣺</td>
			        <td>{{:isfinishSubjectDesc}}</td>
					<td class="black">����ʱ�䣺</td>
			        <td>{{timeCovert finishTime/}}</td>
			    </tr>
				<tr>
			        <td class="black">�������</td>
			        <td>{{:finishResult}}</td>
					<td class="black">&nbsp;</td>
					<td>&nbsp;</td>
		        </tr>
				<tr>
	                <td class="black">�����飺</td>
			        <td colspan="3">{{:subjectRresponsibility}}</td>
                </tr>
				<tr>
	                <td class="black">������ϣ�</td>
			        <td colspan="3">    
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
				<th width="100%" colspan="5">���˻����</th>
			</tr>
			{{for Data}}
				<tr>
			        <td rowspan="4" width="5%">{{:#index+1}}</a>
			        <td class="black" width="15%">�����ƣ�</td>
			        <td colspan="3" width="80%">{{:awardsName}}</td>
			    </tr>
				<tr>
					<td class="black">���õ�λ��</td>
			        <td colspan="3">{{:awardsCompany}}</td>
				</tr>
				<tr>
			        <td class="black" width="15%">�񽱼���</td>
			        <td width="32.5%">{{:awardsLevelDesc}}</td>
			        <td class="black" width="15%">����ʱ�䣺</td>
			        <td width="32.5%">{{timeCovert awardsTime/}}</td>
		        </tr>
				<tr>
                    <td class="black">�Ƿ��������½��</td>
			        <td>{{:awards_type_desc}}</td>
			        <td class="black">��֤�飺</td>
			        <td>  
                         {{if personalAttachVO.attachmentId !==null}}
                         	<a class="cha" href="<%=basePath%>WorkflowAttachMentDownload?attachmentId={{:personalAttachVO.attachmentId}}"> ����鿴</a>
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
				<th width="100%" colspan="5">ѧУ�����</th>
			</tr>
			{{for Data}}
				<tr>
			        <td rowspan="5" width="5%">{{:#index+1}}</a>
			        <td class="black" width="15%">�������ƣ�</td>
			        <td colspan="3" width="80%">{{:awardsName}}</td>
			    </tr>
				<tr>
			        <td class="black">��ְѧУ��</td>
			        <td colspan="3">{{:workSchool}}</td>
			    </tr>
				<tr>
			        <td class="black">���õ�λ��</td>
			        <td colspan="3">{{:awardsCompany}}</td>
			    </tr>
				<tr>
			        <td class="black" width="15%">�񽱼���</td>
			        <td width="32.5%">{{:awardsLevelDesc}}</td>
			        <td class="black" width="15%">����ʱ�䣺</td>
			        <td width="32.5%">{{timeCovert awardsTime/}}</td>
			    </tr>
				<tr>
			   		<td class="black">��֤�飺</td>
			   		<td colspan="3">
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
				<th width="100%" colspan="5">����ѧϰ���</th>
			</tr>
			{{for Data}}
				<tr>
			        <td rowspan="5" width="5%">{{:#index+1}}</a>
			        <td class="black" width="15%">ʱ�䣺</td>
			        <td width="32.5%">{{timeContent start_date end_date/}}</td>
			        <td class="black" width="15%">ѧϰ���޿������ƣ�</td>
			        <td width="32.5%">{{:title}}</td>
			    </tr>
				<tr>
			        <td class="black">ѧϰ�������ݣ�</td>
			        <td colspan="3">{{:content}}</td>
			    </tr>
				<tr>
			        <td class="black">ѧʱ��</td>
			        <td>{{:class_hour}}</td>
			        <td class="black">ѧϰ�ص㣺</td>
			        <td>{{:study_place}}</td>
			    </tr>
				<tr>
			        <td class="black">���쵥λ��</td>
			        <td>{{:organizers}}</td>
					<td class="black">&nbsp;</td>
			        <td>&nbsp;</td>
			    </tr>
				<tr>
			        <td class="black">֤�����ϣ�</td>
			        <td colspan="3">
						{{if proveAttachMentVO.attachmentId !==null}}
                        	<a class="cha" href="<%=basePath%>WorkflowAttachMentDownload?attachmentId={{:proveAttachMentVO.attachmentId}}"> ����鿴</a>
                      	{{/if}}
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
			            <th width="100%" colspan="5">ѧУ�ȼ�����</th>
			        </tr>
			        <tr>
			        	<td class="black">���</td>
			        	<td class="black">�������</td>
			        	<td class="black">����</td>
			        	<td class="black">��ְ</td>
                       <td class="black">֤������</td>
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
				<th width="100%" colspan="5">ѧУ��ɫ���ĸ� </th>
			</tr>
			{{for Data}}
				<tr>
			        <td rowspan="4">{{:#index+1}}</a>
					<td class="black" width="15%">ѧУ��ɫ�������ĸ���Ŀ���ƣ�</td>
			        <td width="32.5%">{{:project_name}}</td>
					<td class="black" width="15%">��Ŀ����</td>
			        <td width="32.5%">{{:project_level_desc}}</td>
				</tr>
				<tr>
					<td class="black">��Ŀ���ܲ��ţ�</td>
			        <td>{{:charge_department}}</td>
			        <td class="black">ʱ�䣺</td>
			        <td>{{timeCovert implement_time/}}</td>
			    </tr>
				<tr>
			        <td class="black">��Ŀ��������</td>
			        <td colspan="3">{{:performance}}</td>
			    </tr>
				<tr>
			    	<td class="black">֤�����ϣ�</td>
			    	<td colspan="3">
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
				<th width="100%" colspan="5">�������  </th>
			</tr>
			{{for Data}}
				<tr>
			        <td rowspan="4">{{:#index+1}}</a>
					<td class="black" width="15%">�е��ϼ����Ű��ŵ�������ι�����</td>
			        <td width="32.5%">{{:superior_task}}</td>
					<td class="black" width="15%">ʱ�䣺</td>
			        <td width="32.5%">{{timeCovert implement_time/}}</td>
			    </tr>
				<tr>
			        <td class="black">�������Ų��ţ�</td>
			        <td>{{:arrange_department}}</td>
					<td class="black">&nbsp;</td>
			        <td>&nbsp;</td>
				</tr>
				<tr>
			        <td class="black">��������</td>
			        <td colspan="3">{{:complete_state}}</td>
				</tr>
				<tr>
                    <td class="black">��֤�飺</td>
			        <td colspan="3">
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
				<th width="100%" colspan="5">�����¹����</th>
			</tr>
			{{for Data}}
				<tr>
			        <td rowspan="4">{{:#index+1}}</a>
					<td class="black" width="15%">�����¹����ƣ�</td>
			        <td width="32.5%">{{:accident_name}}</td>
					<td class="black" width="15%">Υ��������</td>
			        <td width="32.5%">{{:description}}</td>
			    </tr>
				<tr>
			        <td class="black">ʱ�䣺</td>
			        <td>{{timeCovert implement_time/}}</td>
					<td class="black">&nbsp;</td>
			        <td>&nbsp;</td>
				</tr>
				<tr>
			        <td class="black">��������</td>
			        <td colspan="3">{{:process_result}}</td>
				</tr>
				<tr>
                    <td class="black">֤�����ϣ�</td>
			        <td colspan="3">
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
				<th width="100%" colspan="5">�������</th>
			</tr>
			{{for Data}}
				<tr>
			        <td rowspan="4" width="5%">{{:#index+1}}</a>
			        <td class="black" width="15%">ʱ�䣺</td>
			        <td width="32.5%">{{timeCovert implement_time/}}</td>
			        <td class="black" width="15%">�����¼�������</td>
			        <td width="32.5%">{{:description}}</td>
			    </tr>
				<tr>
			        <td class="black">�ܴ����ˣ�</td>
			        <td>{{:people}}</td>
	                <td class="black">���ֲ��ţ�</td>
			        <td>{{:department}}</td>
				</tr>
				<tr>
			        <td class="black">���ֽ����</td>
			        <td colspan="3">{{:process_result}}</td>
				</tr>
				<tr>
                    <td class="black">֤�����ϣ�</td>
			        <td colspan="3">
                    	{{if proveAttachMentVO.attachmentId !==null}}
                        	<a class="cha" href="<%=basePath%>WorkflowAttachMentDownload?attachmentId={{:proveAttachMentVO.attachmentId}}"> ����鿴</a>
                        {{/if}}
                    </td>
			    </tr>
			{{/for}}
		</tbody>
	</table>
</script>

<!--�������� -->
<script id="workHistoryRec" type="text/x-jsrender">	
<table cellpadding="0" cellspacing="1" border="0" class="table text-center">
			    <tbody>
                    <tr>
			            <th width="100%" colspan="5">��������</th>
			        </tr>
			        <tr>
			        	<td class="black">���</td>
			        	<td class="black">��ֹ����</td>
			        	<td class="black">������λ</td>
			        	<td class="black">֤����</td>
						<td class="black">ְ��</td>
			        </tr>
{{for Data}}
    <tr>
      <td>{{:#index+1}}</td>
      <td>{{timeContent start_date end_date/}}</td>
      <td>{{:work_company}}</td>
      <td>{{:prove_people}}</td>
      <td>{{:prove_people_duty}}</td>
    </tr>
{{/for}}
</tbody>
</table>
</script>


<script>
$.views.tags({
	timeContent:function(startTime,endTime){
	   return Brightcom.workflow.getDateStrByLong(startTime)+'������'+Brightcom.workflow.getDateStrByLong(endTime);
	},
	timeCovert:function(time){
		   return Brightcom.workflow.getDateStrByLong(time);
	},
})
</script>




