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
			            <th width="80%" colspan="7">学历情况</th>
			            <th width="20%"></th>
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
             <a class="cha" href="<%=basePath%>WorkflowAttachMentDownload?attachmentId={{:educationAttachMentVO.attachmentId}}"> 点击查看</a>
        {{/if}}
      </td>
      <td>{{:degreeDesc}}</td>
      <td>
         {{if degreeAttachMentVO.attachmentId !==null}}
             <a class="cha" href="<%=basePath%>WorkflowAttachMentDownload?attachmentId={{:degreeAttachMentVO.attachmentId}}"> 点击查看</a>
        {{/if}}
      </td>
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
			            <th width="80%" colspan="5">任职年限</th>
			            <th width="20%"></th>
			        </tr>
			        <tr>
			        	<td class="black">序号</td>
			        	<td class="black">起止年月</td>
			        	<td class="black">所在学校</td>
			        	<td class="black">担任职务</td>
			        	<td class="black">任职年限</td>
			        	<td class="black">证明材料</td>
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
             <a class="cha" href="<%=basePath%>WorkflowAttachMentDownload?attachmentId={{:proveAttachMentVO.attachmentId}}"> 点击查看</a>
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
			            <th width="80%" colspan="3">职称情况</th>
			            <th width="20%"></th>
			        </tr>
			        <tr>
			        	<td class="black">序号</td>
			        	<td class="black">职称</td>
			        	<td class="black">取得职称时间</td>
			        	<td class="black">证明材料</td>
			        </tr>
{{for Data}}
    <tr>
      <td>{{:#index+1}}</td>
      <td>{{:professionaltitle_name}}</td>
      <td>{{timeCovert obtainTime/}}</td>
      <td>
        {{if attachMentVO.attachmentId !==null}}
             <a class="cha" href="<%=basePath%>WorkflowAttachMentDownload?attachmentId={{:attachMentVO.attachmentId}}"> 点击查看</a>
         {{/if}}

     </td>
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
				<th width="100%" colspan="5">论文发表情况</th>
			</tr>
			{{for Data}}
            	<tr>
					<td rowspan="6" width="5%">{{:#index+1}}</a>
					<td class="black" width="15%">题目：</td>
					<td width="32.5%">{{:title}}</td>
					<td class="black" width="15%">发表及宣读时间：</td>
					<td width="32.5%">{{timeCovert publishTime/}}</td>
				</tr>
				<tr>
			        <td class="black">杂志名称：</td>
			        <td>{{:magazineMeetName}}</td>
			        <td class="black">主办单位：</td>
			        <td>{{:organizers}}</td>
				</tr>
				<tr>
			        <td class="black">宣读论文会议名称：</td>
			        <td>{{:paperMeetName}}</td>
			        <td class="black">主办单位级别：</td>
			        <td>{{:organizersLevelDesc}}</td>
				</tr>
				<tr>
					<td class="black">刊号：</td>
			        <td>{{:paperNumber}}</td>
					<td class="black">本人承担部分：</td>
			        <td>{{:personalPart}}</td>
				</tr>	
				<tr>
			        <td class="black">完成方式：</td>
			        <td>{{:complete_way_desc}}</td>
					<td class="black">作者排序：</td>
			        <td>{{:author_order_desc}}</td>
				</tr>
                <tr>
			       	<td class="black">论文扫描件：</td>
			        <td colspan="3">
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
				<th width="100%" colspan="7">专著出版情况</th>
			</tr>
			{{for Data}}
				<tr>
			    	<td rowspan="4" width="5%">{{:#index+1}}</a>
			        <td class="black" width="15%">书名：</td>
			        <td colspan="5" width="85%">{{:book_name}}</td>
			    </tr>
               	<tr>
			        <td class="black" width="15%">完成方式：</td>
			        <td width="15%">{{:complete_way_desc}}</td>
			        <td class="black" width="15%">出版时间：</td>
			        <td width="15%">{{timeCovert publish_time/}} </td>
			        <td class="black" width="15%">本人完成字数：</td>
			        <td width="15%">{{:complete_word}}</td>
			    </tr>
                <tr>
			        <td class="black">本人承担部分：</td>
			        <td>{{:complete_chapter}}</td>
			        <td class="black">作者排序：</td>
			        <td>{{:author_order_desc}}</td>
                    <td class="black">出版社：</td>
			        <td>{{:publish_company}}</td>
		        </tr>
				<tr>
			        <td class="black">证明材料：</td>
			        <td colspan="6">
						{{if proveAttachMentVO.attachmentId !==null}}
                        	<a class="cha" href="<%=basePath%>WorkflowAttachMentDownload?attachmentId={{:proveAttachMentVO.attachmentId}}"> 点击查看</a>
                      	{{/if}}
					</td>
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
				<th width="100%" colspan="5">教科研情况</th>
			</tr>
			{{for Data}}
				<tr>
			        <td rowspan="6" width="5%">{{:#index+1}}</a>
			        <td class="black" width="15%">课题名称：</td>
			        <td width="32.5%">{{:subjectName}}</td>
			        <td class="black" width="15%">课题立项单位：</td>
			        <td width="32.5%">{{:subjectCompany}}</td>
			    </tr>
				<tr>
			       	<td class="black">课题级别：</td>
			        <td>{{:subjectLevelDesc}}</td>
			        <td class="black">立项时间：</td>
			        <td>{{timeCovert projectTime/}}</td>
			    </tr>
				<tr>
			        <td class="black">是否结题：</td>
			        <td>{{:isfinishSubjectDesc}}</td>
					<td class="black">结题时间：</td>
			        <td>{{timeCovert finishTime/}}</td>
			    </tr>
				<tr>
			        <td class="black">获奖情况：</td>
			        <td>{{:finishResult}}</td>
					<td class="black">&nbsp;</td>
					<td>&nbsp;</td>
		        </tr>
				<tr>
	                <td class="black">课题简介：</td>
			        <td colspan="3">{{:subjectRresponsibility}}</td>
                </tr>
				<tr>
	                <td class="black">课题材料：</td>
			        <td colspan="3">    
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
				<th width="100%" colspan="5">个人获奖情况</th>
			</tr>
			{{for Data}}
				<tr>
			        <td rowspan="4" width="5%">{{:#index+1}}</a>
			        <td class="black" width="15%">获奖名称：</td>
			        <td colspan="3" width="80%">{{:awardsName}}</td>
			    </tr>
				<tr>
					<td class="black">表彰单位：</td>
			        <td colspan="3">{{:awardsCompany}}</td>
				</tr>
				<tr>
			        <td class="black" width="15%">获奖级别：</td>
			        <td width="32.5%">{{:awardsLevelDesc}}</td>
			        <td class="black" width="15%">表彰时间：</td>
			        <td width="32.5%">{{timeCovert awardsTime/}}</td>
		        </tr>
				<tr>
                    <td class="black">是否属于以下奖项：</td>
			        <td>{{:awards_type_desc}}</td>
			        <td class="black">获奖证书：</td>
			        <td>  
                         {{if personalAttachVO.attachmentId !==null}}
                         	<a class="cha" href="<%=basePath%>WorkflowAttachMentDownload?attachmentId={{:personalAttachVO.attachmentId}}"> 点击查看</a>
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
				<th width="100%" colspan="5">学校获奖情况</th>
			</tr>
			{{for Data}}
				<tr>
			        <td rowspan="5" width="5%">{{:#index+1}}</a>
			        <td class="black" width="15%">奖项名称：</td>
			        <td colspan="3" width="80%">{{:awardsName}}</td>
			    </tr>
				<tr>
			        <td class="black">任职学校：</td>
			        <td colspan="3">{{:workSchool}}</td>
			    </tr>
				<tr>
			        <td class="black">表彰单位：</td>
			        <td colspan="3">{{:awardsCompany}}</td>
			    </tr>
				<tr>
			        <td class="black" width="15%">获奖级别：</td>
			        <td width="32.5%">{{:awardsLevelDesc}}</td>
			        <td class="black" width="15%">表彰时间：</td>
			        <td width="32.5%">{{timeCovert awardsTime/}}</td>
			    </tr>
				<tr>
			   		<td class="black">获奖证书：</td>
			   		<td colspan="3">
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
				<th width="100%" colspan="5">进修学习情况</th>
			</tr>
			{{for Data}}
				<tr>
			        <td rowspan="5" width="5%">{{:#index+1}}</a>
			        <td class="black" width="15%">时间：</td>
			        <td width="32.5%">{{timeContent start_date end_date/}}</td>
			        <td class="black" width="15%">学习进修课题名称：</td>
			        <td width="32.5%">{{:title}}</td>
			    </tr>
				<tr>
			        <td class="black">学习进修内容：</td>
			        <td colspan="3">{{:content}}</td>
			    </tr>
				<tr>
			        <td class="black">学时：</td>
			        <td>{{:class_hour}}</td>
			        <td class="black">学习地点：</td>
			        <td>{{:study_place}}</td>
			    </tr>
				<tr>
			        <td class="black">主办单位：</td>
			        <td>{{:organizers}}</td>
					<td class="black">&nbsp;</td>
			        <td>&nbsp;</td>
			    </tr>
				<tr>
			        <td class="black">证明材料：</td>
			        <td colspan="3">
						{{if proveAttachMentVO.attachmentId !==null}}
                        	<a class="cha" href="<%=basePath%>WorkflowAttachMentDownload?attachmentId={{:proveAttachMentVO.attachmentId}}"> 点击查看</a>
                      	{{/if}}
					</td>
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
			            <th width="100%" colspan="5">学校等级评估</th>
			        </tr>
			        <tr>
			        	<td class="black">序号</td>
			        	<td class="black">义务教育</td>
			        	<td class="black">高中</td>
			        	<td class="black">中职</td>
                       <td class="black">证明材料</td>
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
				<th width="100%" colspan="5">学校特色及改革 </th>
			</tr>
			{{for Data}}
				<tr>
			        <td rowspan="4">{{:#index+1}}</a>
					<td class="black" width="15%">学校特色创建及改革项目名称：</td>
			        <td width="32.5%">{{:project_name}}</td>
					<td class="black" width="15%">项目级别：</td>
			        <td width="32.5%">{{:project_level_desc}}</td>
				</tr>
				<tr>
					<td class="black">项目主管部门：</td>
			        <td>{{:charge_department}}</td>
			        <td class="black">时间：</td>
			        <td>{{timeCovert implement_time/}}</td>
			    </tr>
				<tr>
			        <td class="black">项目完成情况：</td>
			        <td colspan="3">{{:performance}}</td>
			    </tr>
				<tr>
			    	<td class="black">证明材料：</td>
			    	<td colspan="3">
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
				<th width="100%" colspan="5">社会责任  </th>
			</tr>
			{{for Data}}
				<tr>
			        <td rowspan="4">{{:#index+1}}</a>
					<td class="black" width="15%">承担上级部门安排的社会责任工作：</td>
			        <td width="32.5%">{{:superior_task}}</td>
					<td class="black" width="15%">时间：</td>
			        <td width="32.5%">{{timeCovert implement_time/}}</td>
			    </tr>
				<tr>
			        <td class="black">工作安排部门：</td>
			        <td>{{:arrange_department}}</td>
					<td class="black">&nbsp;</td>
			        <td>&nbsp;</td>
				</tr>
				<tr>
			        <td class="black">完成情况：</td>
			        <td colspan="3">{{:complete_state}}</td>
				</tr>
				<tr>
                    <td class="black">获奖证书：</td>
			        <td colspan="3">
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
				<th width="100%" colspan="5">责任事故情况</th>
			</tr>
			{{for Data}}
				<tr>
			        <td rowspan="4">{{:#index+1}}</a>
					<td class="black" width="15%">责任事故名称：</td>
			        <td width="32.5%">{{:accident_name}}</td>
					<td class="black" width="15%">违纪描述：</td>
			        <td width="32.5%">{{:description}}</td>
			    </tr>
				<tr>
			        <td class="black">时间：</td>
			        <td>{{timeCovert implement_time/}}</td>
					<td class="black">&nbsp;</td>
			        <td>&nbsp;</td>
				</tr>
				<tr>
			        <td class="black">处理结果：</td>
			        <td colspan="3">{{:process_result}}</td>
				</tr>
				<tr>
                    <td class="black">证明材料：</td>
			        <td colspan="3">
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
				<th width="100%" colspan="5">处分情况</th>
			</tr>
			{{for Data}}
				<tr>
			        <td rowspan="4" width="5%">{{:#index+1}}</a>
			        <td class="black" width="15%">时间：</td>
			        <td width="32.5%">{{timeCovert implement_time/}}</td>
			        <td class="black" width="15%">处分事件描述：</td>
			        <td width="32.5%">{{:description}}</td>
			    </tr>
				<tr>
			        <td class="black">受处分人：</td>
			        <td>{{:people}}</td>
	                <td class="black">处分部门：</td>
			        <td>{{:department}}</td>
				</tr>
				<tr>
			        <td class="black">处分结果：</td>
			        <td colspan="3">{{:process_result}}</td>
				</tr>
				<tr>
                    <td class="black">证明材料：</td>
			        <td colspan="3">
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
			            <th width="100%" colspan="5">工作经历</th>
			        </tr>
			        <tr>
			        	<td class="black">序号</td>
			        	<td class="black">起止年月</td>
			        	<td class="black">工作单位</td>
			        	<td class="black">证明人</td>
						<td class="black">职务</td>
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
	   return Brightcom.workflow.getDateStrByLong(startTime)+'———'+Brightcom.workflow.getDateStrByLong(endTime);
	},
	timeCovert:function(time){
		   return Brightcom.workflow.getDateStrByLong(time);
	},
})
</script>




