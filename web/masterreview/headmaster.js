//工作流公共js
(function($) {
	Headmaster = {};
	page = Headmaster = $;
	page.areaApprove = 1;
	page.personalApprove = 2;
	page.professorApprove = 3;
	page.wholeApprove = 3;
	
	page.getBasePath = function(){
	     var pathName = window.location.pathname.substring(1);   
	     var webName = pathName == '' ? '' : pathName.substring(0, pathName.indexOf('/'));   
	   //  window.location.protocol + '//' + window.location.host + '/'+ webName + '/'
	     return window.location.protocol + '//' + window.location.host + '/';  
	}

	
	page.findMasterBaseInfo = function(headerMasterId){
		var bcReq = new BcRequest('headmaster','headMasterbase','findMasterBaseInfo');
		bcReq.setExtraPs({
			"userid":headerMasterId
			});
		bcReq.setSuccFn(function(data,status){
			var result = data.Data[0];
			$("#mobile").val(result.mobile);
			$("#identitycard").val(result.idnumber);
			$("#school_id").val(result.deptid);
			$("#school_name").val(result.deptname);
			$("#headerMasterName").val(headerMasterName);
			$("#present_occupation").val(result.present_occupation);
			
			$("#usersex ").val(result.usersex);
		    $("#address").val(result.address);
		    $("#ispositive").val(result.ispositive);
			$("#school_class").val(result.school_class);
			$("#present_major_occupation").val(result.present_major_occupation);
		    $("#join_work_time").val(result.join_work_time.substring(0,10));
			$("#join_educate_work_time").val(result.join_educate_work_time.substring(0,10));
			$("#politics_status").val(result.politics_status);
		    $("#teach_age").val(result.teach_age);
			$("#native_place").val(result.native_place);
			$("#phasestudy").val(result.phasestudy);
			$("#census_register").val(result.census_register);
			$("#birth_date").text(page.discriCard(result.idnumber));
			$("#nation").val(result.nation);
			$("#email").val(result.email);
			$("#person_img_attachId").val(result.headimgid);
			$("#lodge_school").val(result.lodge_school);
			var rootPath = page.getBasePath();
			if(result.headimgpath){
				$("#personHeadImg").attr("src",rootPath+ '\\' + result.headimgpath);
			}
			
		});
		bcReq.postData();
	}
	
	page.discriCard= function(UUserCard){ 
			//获取年龄 
		    return UUserCard.substring(6, 10) + "-" + UUserCard.substring(10, 12) + "-" + UUserCard.substring(12, 14); 
	} 
	
	page.initSelectCom = function(){
		   Brightcom.workflow.initSelectCombox('apply_level');
		   Brightcom.workflow.initSelectCombox('headmaster_school_type','schoolTypeAgo');
		   Brightcom.workflow.initSelectCombox('headmaster_school_type','schoolType');
		   
		   Brightcom.workflow.initSelectCombox('headmaster_ispositive','ispositive');
		   Brightcom.workflow.initSelectCombox('headmaster_user_sex','usersex');
		   Brightcom.workflow.initSelectCombox('headmaster_school_type','school_class');
		   Brightcom.workflow.initSelectCombox('headmaster_politics_status','politics_status');
		   Brightcom.workflow.initSelectCombox('headmaster_phase_study','phasestudy');
		   Brightcom.workflow.initSelectCombox('headmaster_census_register','census_register');
		   Brightcom.workflow.initSelectCombox('headmaster_ispositive','present_occupation');
		   Brightcom.workflow.initSelectCombox('global_yes_or_no','lodge_school');
		   Brightcom.workflow.initSelectCombox('headmaster_present_major_occupation','present_major_occupation');
		   
	}
	
	
	page.bulidMsaterView = function(masterReviewVO,approveType){
		$("#id").val(masterReviewVO.id);
		$("#headerMasterId").val(masterReviewVO.headerMasterId);
		$("#headerMasterName").text(masterReviewVO.headerMasterName);
		$("#identitycard").text(masterReviewVO.identitycard);
		$("#mobile").text(masterReviewVO.mobile);
		$("#school_name").text(masterReviewVO.schoolName);
		$("#present_occupation").text(JspParamUtil.paramVal('headmaster_ispositive',masterReviewVO.present_occupation));
		
		$("#usersex").text(JspParamUtil.paramVal('headmaster_user_sex',masterReviewVO.usersex));
		$("#census_register").text(JspParamUtil.paramVal('headmaster_census_register',masterReviewVO.census_register));
		$("#nation").text(masterReviewVO.nation);
		$("#school_class").text(JspParamUtil.paramVal('headmaster_school_type',masterReviewVO.school_class));
		$("#politics_status").text(JspParamUtil.paramVal('headmaster_politics_status',masterReviewVO.politics_status));
		$("#teach_age").text(masterReviewVO.teach_age);
		$("#phasestudy").text(JspParamUtil.paramVal('headmaster_phase_study',masterReviewVO.phasestudy));
		$("#email").text(masterReviewVO.email);
		$("#present_major_occupation").text(JspParamUtil.paramVal('headmaster_present_major_occupation',masterReviewVO.present_major_occupation));
		$("#birth_date").text(page.discriCard(masterReviewVO.identitycard));
		$("#native_place").text(masterReviewVO.native_place);
		$("#join_work_time").text(Brightcom.workflow.getDateStrByLong(masterReviewVO.join_work_time));
		$("#join_educate_work_time").text(Brightcom.workflow.getDateStrByLong(masterReviewVO.join_educate_work_time));
		
		var rootPath = page.getBasePath();
		$("#person_img_object").attr("src",rootPath+ '\\' +(masterReviewVO.person_img_attachMentVO.filePath || '/masterreview/images/Photo-bg.png'));
		
		
		$("#apply_level").text(JspParamUtil.paramVal('apply_level',masterReviewVO.applylevel));
		if(masterReviewVO.work_report){
			$("#workReportTab").show();
			$("#work_report").text(masterReviewVO.work_report);
		}else{
			var noDataArray = [];
			noDataArray.push("<table id='workReportTab' cellpadding='0' cellspacing='1' border='0' class='table text-center'>");
			noDataArray.push("<tbody>");
			noDataArray.push("<tr><th width='80%'>述职报告</th> <th width='20%'></th></tr>");
			noDataArray.push("<tr><td class='red' colspan='8'>暂无数据</td></tr>");
			noDataArray.push(" <tbody> </table>");
			$("#workReportDiv").after(noDataArray.join(""));
		}
		bulidWorkExperience(masterReviewVO.workExperienceVOs,approveType);
		bulidEducation(masterReviewVO.educationVOs,approveType);
		bulidProfessionalTitle(masterReviewVO.professionalTitleVOs,approveType);
		
		bulidPaper(masterReviewVO.paperVOs,approveType);
		bulidBookPublish(masterReviewVO.bookPublishVOs,approveType);
		bulidSubject(masterReviewVO.subjectVOs,approveType);
		bulidPersonalAward(masterReviewVO.personalAwardVOs,approveType);
		bulidSchoolAward(masterReviewVO.schoolAwardVOs,approveType);
		
		
		bulidStudyTrains(masterReviewVO.studyTrainVOs,approveType);
		bulidSchoolReforms(masterReviewVO.schoolReformVOs,approveType);
		bulidSocialDutys(masterReviewVO.socialDutyVOs,approveType);
		bulidAccidents(masterReviewVO.accidentVOs,approveType);
		bulidPunishments(masterReviewVO.punishmentVOs,approveType);
		
		page.bulidManageDiff(masterReviewVO);
		
		
		bulidNoDataTemplate(masterReviewVO,'run_school',"办学思想");
		bulidNoDataTemplate(masterReviewVO,'school_management',"学校管理");
		bulidNoDataTemplate(masterReviewVO,'education_science',"教育教学");
		bulidNoDataTemplate(masterReviewVO,'external_environment',"外部环境");
		bulidNoDataTemplate(masterReviewVO,'student_development',"学生发展");
		bulidNoDataTemplate(masterReviewVO,'teacher_development',"教师发展");
	
	
		bulidWorkHistory(masterReviewVO.workHistoryVOs,approveType);
		bulidGradeEvaluate(masterReviewVO.gradeEvaluateVOs,approveType);
		//bulidSchoolReform(masterReviewVO.schoolReformVOs,approveType);
		//bulidSocialDuty(masterReviewVO.socialDutyVOs,approveType);
		//bulidAccident(masterReviewVO.accidentVOs,approveType);
	//	bulidPunishment(masterReviewVO.punishmentVOs,approveType);
		
		if(approveType==page.areaApprove){//区级审核
			 var config = {
						accept: {
			      	      title: 'Images',
			      	      extensions: 'gif,jpg,jpeg,bmp,png',
					      mimeTypes: 'image/*' //video/*;application/msword
			            },
				    fileNumLimit:1,
				    param:{headImg:'1'}
				};
				Headmaster.initWebUploader('opinionButtonSpan','','opinionType','点击上传','opinionAttachId','opinionDisplayDiv',"img",config);
				Headmaster.initWebUploader('signButtonSpan','','signType','点击上传','signAttachId','signDisplayDiv',"img",config);
		}
		
		if(approveType==page.personalApprove){//人事干部审核
			$("#paper_grade_index").val(masterReviewVO.paperVOs.length);
		}
		
		if(approveType==page.professorApprove){//专家审核
			$("#paper_grade_index").val(masterReviewVO.paperVOs.length);
		}
	}
	
	
	/*
	 * uploadSingleId:上传按钮显示位置id
	 * file_upload_type:区分某种业务上传类型，比如是学历上传还是学位上传
	 buttonName ： 按钮名称
	 hiddenAttachId：隐藏附件id，选中后把附件值保存在该隐藏域里
	 hiddenDisplayId:附件显示的div id
	 uploadType:区分是图片上传还是其他上传
	 */
	page.initWebUploader = function(uploadSingleId,rownum,file_upload_type,buttonName,hiddenAttachId,hiddenDisplayId,uploadType,config) {
		$("#"+uploadSingleId+rownum).empty();
	    $("#"+uploadSingleId+rownum).append("<iframe name ='ifm"+uploadSingleId+rownum+"' id ='ifm"+uploadSingleId+rownum+"' src='' width='200px;' height='50' scrolling='no' frameborder='0'></iframe>"); 
	    $("#ifm"+uploadSingleId+rownum).attr("src", "/masterreview/public/webuploader.jsp?t="+Math.random()+"&uploadType="+uploadType+"&rownum="+rownum+"&file_upload_type="+file_upload_type+"&hiddenAttachId="+hiddenAttachId+"&hiddenDisplayId="+hiddenDisplayId+"&uploadSingleId="+uploadSingleId+"&buttonName="+encodeURI(buttonName));
	    $("#ifm"+uploadSingleId+rownum).load(function() {
	     	$("#ifm"+uploadSingleId+rownum)[0].contentWindow.init(config);
		});
		file_num = 0;
	}
	
	function bulidNoDataTemplate(masterReviewVO,propertyName,displayName){
		if(masterReviewVO[propertyName]){
			$("#"+propertyName+"_label").text(masterReviewVO[propertyName] || '暂无数据');
			var rootpath = page.getBasePath();
			$("#"+propertyName+"_attachment_id").attr("href",rootpath+"WorkflowAttachMentDownload?attachmentId="+masterReviewVO.school_management_attachMentVO.attachmentId);
		}else{
			$("#"+propertyName+"_div").empty();
			var noDataArray = [];
			noDataArray.push("<table  cellpadding='0' cellspacing='1' border='0' class='table text-center'>");
			noDataArray.push("<tbody>");
			noDataArray.push("<tr><th width='80%'>"+displayName+"</th> <th width='20%'></th></tr>");
			noDataArray.push("<tr><td class='red' colspan='8'>暂无数据</td></tr>");
			noDataArray.push(" <tbody> </table>");
			$("#"+propertyName+"_div").after(noDataArray.join(""));
		}
	}
	
	/*
	function bulidSchoolReform(schoolReformVOs,approveType){
		$("#schoolReformRowNum").val(schoolReformVOs.length);
		$("#schoolReformDiv").empty();
		if(schoolReformVOs.length>0){
			 var dataObject = {'Data': []};
			 for(var i =0;i<schoolReformVOs.length;i++){
				 dataObject.Data.push(schoolReformVOs[i]);
			 }
			 if(approveType==page.professorApprove){
				 dataObject['schoolReform_grade_rows'] = schoolReformVOs.length
			 }
			 var subTaskContent= $("#schoolReformRec").render(dataObject);
			 $("#schoolReformDiv").append(subTaskContent);
		}else{
			var noDataArray = [];
			noDataArray.push("<table cellpadding='0' cellspacing='1' border='0' class='table text-center'>  <tbody>");
			noDataArray.push("<tr><th width='80%' colspan='7'>学校特色和改革</th><th width='20%'></th></tr>");
			noDataArray.push("<tr><td class='red' colspan='8'>暂无数据</td></tr>");
			noDataArray.push(" <tbody> </table>");
			$("#schoolReformDiv").append(noDataArray.join(""));
		}
	}

	function bulidSocialDuty(socialDutyVOs,approveType){
		$("#socialDutyRowNum").val(socialDutyVOs.length);
		$("#socialDutyDiv").empty();
		if(socialDutyVOs.length>0){
			 var dataObject = {'Data': []};
			 for(var i =0;i<socialDutyVOs.length;i++){
				 dataObject.Data.push(socialDutyVOs[i]);
			 }
			 if(approveType==page.professorApprove){
				 dataObject['socialDuty_grade_rows'] = socialDutyVOs.length
			 }
			 var subTaskContent= $("#socialDutyRec").render(dataObject);
			 $("#socialDutyDiv").append(subTaskContent);
		}else{
			var noDataArray = [];
			noDataArray.push("<table cellpadding='0' cellspacing='1' border='0' class='table text-center'>  <tbody>");
			noDataArray.push("<tr><th width='80%' colspan='7'>社会责任</th><th width='20%'></th></tr>");
			noDataArray.push("<tr><td class='red' colspan='8'>暂无数据</td></tr>");
			noDataArray.push(" <tbody> </table>");
			$("#socialDutyDiv").append(noDataArray.join(""));
		}
	}

	function bulidAccident(accidentVOs,approveType){
		$("#accidentDiv").empty();
		if(accidentVOs.length>0){
			 var dataObject = {'Data': []};
			 for(var i =0;i<accidentVOs.length;i++){
				 dataObject.Data.push(accidentVOs[i]);
			 }
			 if(approveType==page.professorApprove){
				 dataObject['accident_grade_rows'] = accidentVOs.length
			 }
			 var subTaskContent= $("#accidentRec").render(dataObject);
			 $("#accidentDiv").append(subTaskContent);
		}else{
			var noDataArray = [];
			noDataArray.push("<table cellpadding='0' cellspacing='1' border='0' class='table text-center'>  <tbody>");
			noDataArray.push("<tr><th width='80%' colspan='7'>责任事故</th><th width='20%'></th></tr>");
			noDataArray.push("<tr><td class='red' colspan='8'>暂无数据</td></tr>");
			noDataArray.push(" <tbody> </table>");
			$("#accidentDiv").append(noDataArray.join(""));
		}
	}

	function bulidPunishment(punishmentVOs,approveType){
		$("#punishmentDiv").empty();
		if(punishmentVOs.length>0){
			 var dataObject = {'Data': []};
			 for(var i =0;i<punishmentVOs.length;i++){
				 dataObject.Data.push(punishmentVOs[i]);
			 }
			 if(approveType==page.professorApprove){
				 dataObject['punishment_grade_rows'] = punishmentVOs.length
			 }
			 var subTaskContent= $("#punishmentRec").render(dataObject);
			 $("#punishmentDiv").append(subTaskContent);
		}else{
			var noDataArray = [];
			noDataArray.push("<table cellpadding='0' cellspacing='1' border='0' class='table text-center'>  <tbody>");
			noDataArray.push("<tr><th width='80%' colspan='7'> 减分处分</th><th width='20%'></th></tr>");
			noDataArray.push("<tr><td class='red' colspan='8'>暂无数据</td></tr>");
			noDataArray.push(" <tbody> </table>");
			$("#punishmentDiv").append(noDataArray.join(""));
		}
	}*/

	function bulidGradeEvaluate(gradeEvaluateVOs,approveType){
		$("#gradeEvaluateRowNum").val(gradeEvaluateVOs.length);
		$("#gradeEvaluateDiv").empty();
		if(gradeEvaluateVOs.length>0){
			 var dataObject = {'Data': []};
			 for(var i =0;i<gradeEvaluateVOs.length;i++){
				 dataObject.Data.push(gradeEvaluateVOs[i]);
			 }
			 if(approveType==page.professorApprove){
				 dataObject['gradeEvaluate_grade_rows'] = gradeEvaluateVOs.length
			 }
			 var subTaskContent= $("#gradeEvaluateRec").render(dataObject);
			 $("#gradeEvaluateDiv").append(subTaskContent);
		}else{
			var noDataArray = [];
			noDataArray.push("<table cellpadding='0' cellspacing='1' border='0' class='table text-center'>  <tbody>");
			noDataArray.push("<tr><th width='80%' colspan='7'>学校等级评估</th><th width='20%'></th></tr>");
			noDataArray.push("<tr><td class='red' colspan='8'>暂无数据</td></tr>");
			noDataArray.push(" <tbody> </table>");
			$("#gradeEvaluateDiv").append(noDataArray.join(""));
		}
	}

	function bulidWorkHistory(workHistoryVOs,approveType){
		$("#workHistoryRowNum").val(workHistoryVOs.length);
		$("#workHistoryDiv").empty();
		if(workHistoryVOs.length>0){
			 var dataObject = {'Data': []};
			 for(var i =0;i<workHistoryVOs.length;i++){
				 dataObject.Data.push(workHistoryVOs[i]);
			 }
			 if(approveType==page.professorApprove){
				 dataObject['workHistory_grade_rows'] = workHistoryVOs.length
			 }
			 var subTaskContent= $("#workHistoryRec").render(dataObject);
			 $("#workHistoryDiv").append(subTaskContent);
		}else{
			var noDataArray = [];
			noDataArray.push("<table cellpadding='0' cellspacing='1' border='0' class='table text-center'>  <tbody>");
			noDataArray.push("<tr><th width='80%' colspan='7'>工作经历</th><th width='20%'></th></tr>");
			noDataArray.push("<tr><td class='red' colspan='8'>暂无数据</td></tr>");
			noDataArray.push(" <tbody> </table>");
			$("#workHistoryDiv").append(noDataArray.join(""));
		}
	}
	
	//学历情况
	function bulidEducation(educationVOs,approveType){
		$("#educationRowNum").val(educationVOs.length);
		$("#educationDiv").empty();
		if(educationVOs.length>0){
			 var education_grades = 0;
			 var dataObject = {'Data': [],'personRowNum':(educationVOs.length+1),'education_grade_rows':educationVOs.length};
			 for(var i =0;i<educationVOs.length;i++){
                // if(approveType==page.professorApprove){
                	// educationVOs[i]['education_grade_rows'] = educationVOs.length;
				// }
				 dataObject.Data.push(educationVOs[i]);
			 }
		
			 var subTaskContent= $("#educationRec").render(dataObject);
			 $("#educationDiv").append(subTaskContent);
		}else{
			var noDataArray = [];
			noDataArray.push("<table cellpadding='0' cellspacing='1' border='0' class='table text-center'>  <tbody>");
			noDataArray.push("<tr><th width='80%' colspan='7'>学历情况</th><th width='20%'></th></tr>");
			noDataArray.push("<tr><td class='red' colspan='8'>暂无数据</td></tr>");
			noDataArray.push(" <tbody> </table>");
			$("#educationDiv").append(noDataArray.join(""));
		}
	}

	//任职年限
	function bulidWorkExperience(workExperienceVOs,approveType ){
		$("#workExperienceRowNum").val(workExperienceVOs.length);
		$("#workExperienceDiv").empty();
		if(workExperienceVOs.length>0){
			 var dataObject = {'Data': [],'personRowNum':(workExperienceVOs.length+1)};
			 for(var i =0;i<workExperienceVOs.length;i++){
				 dataObject.Data.push(workExperienceVOs[i]);
			 }
			 if(approveType==page.professorApprove){
				 dataObject['workExperience_grade_rows'] = workExperienceVOs.length
			 }
			 var subTaskContent= $("#workExperienceRec").render(dataObject);
			 $("#workExperienceDiv").append(subTaskContent);
		}else{
			var noDataArray = [];
			noDataArray.push("<table cellpadding='0' cellspacing='1' border='0' class='table text-center'>  <tbody>");
			noDataArray.push("<tr><th width='80%' colspan='6'>任职年限</th><th width='20%'></th></tr>");
			noDataArray.push("<tr><td class='red' colspan='7'>暂无数据</td></tr>");
			noDataArray.push(" <tbody> </table>");
			$("#workExperienceDiv").append(noDataArray.join(""));
		}
	}

	//职称
	function bulidProfessionalTitle(professionalTitleVOs,approveType){
		$("#professionalTitleRowNum").val(professionalTitleVOs.length);
		$("#professionalTitleDiv").empty();
		if(professionalTitleVOs.length>0){
			 var dataObject = {'Data': [],'personRowNum':(professionalTitleVOs.length+1)};
			 for(var i =0;i<professionalTitleVOs.length;i++){
				 dataObject.Data.push(professionalTitleVOs[i]);
			 }
			 if(approveType==page.professorApprove){
				 dataObject['professionalTitle_grade_rows'] = professionalTitleVOs.length
			 }
			 var subTaskContent= $("#professionalTitleRec").render(dataObject);
			 $("#professionalTitleDiv").append(subTaskContent);
		}else{
			var noDataArray = [];
			noDataArray.push("<table cellpadding='0' cellspacing='1' border='0' class='table text-center'>  <tbody>");
			noDataArray.push("<tr><th width='80%' colspan='4'>职称情况</th><th width='20%'></th></tr>");
			noDataArray.push("<tr><td class='red' colspan='5'>暂无数据</td></tr>");
			noDataArray.push(" <tbody> </table>");
			$("#professionalTitleDiv").append(noDataArray.join(""));
		}
	}
	//---------------------------------------------

	//论文
	function bulidPaper(paperVOs,approveType){
		$("#paperTitleRowNum").val(paperVOs.length);
		$("#paperDiv").empty();
		if(paperVOs.length>0){
			 var dataObject = {'Data': [],'personRowNum':(paperVOs.length*3)};
			 for(var i =0;i<paperVOs.length;i++){
				// paperVOs[i]["personRowNum"]=(paperVOs.length*3);
				 dataObject.Data.push(paperVOs[i]);
			 }
			 if(approveType==page.professorApprove){
				 dataObject['paper_grade_rows'] =(paperVOs.length*5)
			 }
			 var subTaskContent= $("#paperRec").render(dataObject);
			 $("#paperDiv").append(subTaskContent);
		}else{
			var noDataArray = [];
			noDataArray.push("<table cellpadding='0' cellspacing='1' border='0' class='table text-center'>  <tbody>");
			noDataArray.push("<tr><th width='80%' colspan='7'>论文发表情况</th><th width='20%'></th></tr>");
			noDataArray.push("<tr><td class='red' colspan='8'>暂无数据</td></tr>");
			noDataArray.push(" <tbody> </table>");
			$("#paperDiv").append(noDataArray.join(""));
		}
	}

	//著作出版
	function bulidBookPublish(bookPublishVOs,approveType){
		$("#workPublishRowNum").val(bookPublishVOs.length);
		$("#bookPublishDiv").empty();
		if(bookPublishVOs.length>0){
			 var dataObject = {'Data': []};
			 for(var i =0;i<bookPublishVOs.length;i++){
				 bookPublishVOs[i]["personRowNum"]=(bookPublishVOs.length*3);
				 dataObject.Data.push(bookPublishVOs[i]);
			 }
			 if(approveType==page.professorApprove){
				 dataObject['bookPublish_grade_rows'] = (bookPublishVOs.length*3)
			 }
			 var subTaskContent= $("#workPublishRec").render(dataObject);
			 $("#bookPublishDiv").append(subTaskContent);
		}else{
			var noDataArray = [];
			noDataArray.push("<table cellpadding='0' cellspacing='1' border='0' class='table text-center'>  <tbody>");
			noDataArray.push("<tr><th width='80%' colspan='7'>著作发表情况</th><th width='20%'></th></tr>");
			noDataArray.push("<tr><td class='red' colspan='8'>暂无数据</td></tr>");
			noDataArray.push(" <tbody> </table>");
			 $("#bookPublishDiv").append(noDataArray.join(""));
		}
	}

	//课题
	function bulidSubject(subjectVOs,approveType){
		$("#subjectRowNum").val(subjectVOs.length);
		$("#subjectDiv").empty();
		if(subjectVOs.length>0){
			 var dataObject = {'Data': []};
			 for(var i =0;i<subjectVOs.length;i++){
				 subjectVOs[i]["personRowNum"]=(subjectVOs.length*5);
				 dataObject.Data.push(subjectVOs[i]);
			 }
			 if(approveType==page.professorApprove){
				 dataObject['subject_grade_rows'] =(subjectVOs.length*5);
			 }
			 var subTaskContent= $("#subjectRec").render(dataObject);
			 $("#subjectDiv").append(subTaskContent);
		}else{
			var noDataArray = [];
			noDataArray.push("<table cellpadding='0' cellspacing='1' border='0' class='table text-center'>  <tbody>");
			noDataArray.push("<tr><th width='80%' colspan='7'>课题情况</th><th width='20%'></th></tr>");
			noDataArray.push("<tr><td class='red' colspan='8'>暂无数据</td></tr>");
			noDataArray.push(" <tbody> </table>");
			 $("#subjectDiv").append(noDataArray.join(""));
		}
	}

	//个人获奖
	function bulidPersonalAward(personalAwardVOs,approveType){
		$("#personalAwardRowNum").val(personalAwardVOs.length);
		$("#personalAwardDiv").empty();
		if(personalAwardVOs.length>0){
			 var dataObject = {'Data': []};
			 for(var i =0;i<personalAwardVOs.length;i++){
				 personalAwardVOs[i]["personRowNum"]=(personalAwardVOs.length*4);
				 dataObject.Data.push(personalAwardVOs[i]);
			 }
			 if(approveType==page.professorApprove){
				 dataObject['personalAward_grade_rows'] = personalAwardVOs.length
			 }
			 var subTaskContent= $("#personalAwardRec").render(dataObject);
			 $("#personalAwardDiv").append(subTaskContent);
		}else{
			var noDataArray = [];
			noDataArray.push("<table cellpadding='0' cellspacing='1' border='0' class='table text-center'>  <tbody>");
			noDataArray.push("<tr><th width='80%' colspan='6'>个人获奖情况</th><th width='20%'></th></tr>");
			noDataArray.push("<tr><td class='red' colspan='7'>暂无数据</td></tr>");
			noDataArray.push(" <tbody> </table>");
			 $("#personalAwardDiv").append(noDataArray.join(""));
		}
	}

	//学校获奖
	function bulidSchoolAward(schoolAwardVOs,approveType){
		$("#schoolAwardRowNum").val(schoolAwardVOs.length);
		$("#schoolAwardDiv").empty();
		if(schoolAwardVOs.length>0){
			 var dataObject = {'Data': []};
			 for(var i =0;i<schoolAwardVOs.length;i++){
				 schoolAwardVOs[i]["personRowNum"]=(schoolAwardVOs.length*3);
				 dataObject.Data.push(schoolAwardVOs[i]);
			 }
			 if(approveType==page.professorApprove){
				 dataObject['schoolAward_grade_rows'] = schoolAwardVOs.length
			 }
			 var subTaskContent= $("#schoolAwardRec").render(dataObject);
			 $("#schoolAwardDiv").append(subTaskContent);
		}else{
			var noDataArray = [];
			noDataArray.push("<table cellpadding='0' cellspacing='1' border='0' class='table text-center'>  <tbody>");
			noDataArray.push("<tr><th width='80%' colspan='7'>学校获奖情况</th><th width='20%'></th></tr>");
			noDataArray.push("<tr><td class='red' colspan='8'>暂无数据</td></tr>");
			noDataArray.push(" <tbody> </table>");
			 $("#schoolAwardDiv").append(noDataArray.join(""));
		}
	}


	function bulidStudyTrains(studyTrainVOs,approveType){
		$("#studyTrainRowNum").val(studyTrainVOs.length);
		$("#studyTrainDiv").empty();
		if(studyTrainVOs.length>0){
			 var dataObject = {'Data': []};
			 for(var i =0;i<studyTrainVOs.length;i++){
				 studyTrainVOs[i]["personRowNum"]=(studyTrainVOs.length*2);
				 dataObject.Data.push(studyTrainVOs[i]);
			 }
			 if(approveType==page.professorApprove){
				 dataObject['studyTrain_grade_rows'] = studyTrainVOs.length
			 }
			 var subTaskContent= $("#studyTrainRec").render(dataObject);
			 $("#studyTrainDiv").append(subTaskContent);
		}else{
			var noDataArray = [];
			noDataArray.push("<table cellpadding='0' cellspacing='1' border='0' class='table text-center'>  <tbody>");
			noDataArray.push("<tr><th width='80%' colspan='7'>个人进修情况</th><th width='20%'></th></tr>");
			noDataArray.push("<tr><td class='red' colspan='8'>暂无数据</td></tr>");
			noDataArray.push(" <tbody> </table>");
			 $("#studyTrainDiv").append(noDataArray.join(""));
		}
	}


	function bulidSchoolReforms(schoolReformVOs,approveType){
		$("#schoolReformRowNum").val(schoolReformVOs.length);
		$("#schoolReformDiv").empty();
		if(schoolReformVOs.length>0){
			 var dataObject = {'Data': []};
			 for(var i =0;i<schoolReformVOs.length;i++){
				 schoolReformVOs[i]["personRowNum"]=(schoolReformVOs.length*3);
				 dataObject.Data.push(schoolReformVOs[i]);
			 }
			 if(approveType==page.professorApprove){
				 dataObject['schoolReform_grade_rows'] = schoolReformVOs.length
			 }
			 var subTaskContent= $("#schoolReformRec").render(dataObject);
			 $("#schoolReformDiv").append(subTaskContent);
		}else{
			var noDataArray = [];
			noDataArray.push("<table cellpadding='0' cellspacing='1' border='0' class='table text-center'>  <tbody>");
			noDataArray.push("<tr><th width='80%' colspan='7'>学校特色及改革</th><th width='20%'></th></tr>");
			noDataArray.push("<tr><td class='red' colspan='8'>暂无数据</td></tr>");
			noDataArray.push(" <tbody> </table>");
			 $("#schoolReformDiv").append(noDataArray.join(""));
		}
	}

	function bulidSocialDutys(socialDutyVOs,approveType){
		$("#socialDutyRowNum").val(socialDutyVOs.length);
		$("#socialDutyDiv").empty();
		if(socialDutyVOs.length>0){
			 var dataObject = {'Data': []};
			 for(var i =0;i<socialDutyVOs.length;i++){
				 socialDutyVOs[i]["personRowNum"]=(socialDutyVOs.length*2);
				 dataObject.Data.push(socialDutyVOs[i]);
			 }
			 if(approveType==page.professorApprove){
				 dataObject['socialDuty_grade_rows'] = socialDutyVOs.length
			 }
			 var subTaskContent= $("#socialDutyRec").render(dataObject);
			 $("#socialDutyDiv").append(subTaskContent);
		}else{
			var noDataArray = [];
			noDataArray.push("<table cellpadding='0' cellspacing='1' border='0' class='table text-center'>  <tbody>");
			noDataArray.push("<tr><th width='80%' colspan='7'>社会责任</th><th width='20%'></th></tr>");
			noDataArray.push("<tr><td class='red' colspan='8'>暂无数据</td></tr>");
			noDataArray.push(" <tbody> </table>");
			 $("#socialDutyDiv").append(noDataArray.join(""));
		}
	}

	function bulidAccidents(accidentVOs,approveType){
		$("#accidentRowNum").val(accidentVOs.length);
		$("#accidentDiv").empty();
		if(accidentVOs.length>0){
			 var dataObject = {'Data': []};
			 for(var i =0;i<accidentVOs.length;i++){
				 accidentVOs[i]["personRowNum"]=(accidentVOs.length*2);
				 dataObject.Data.push(accidentVOs[i]);
			 }
			 if(approveType==page.professorApprove){
				 dataObject['accident_grade_rows'] = accidentVOs.length
			 }
			 var subTaskContent= $("#accidentRec").render(dataObject);
			 $("#accidentDiv").append(subTaskContent);
		}else{
			var noDataArray = [];
			noDataArray.push("<table cellpadding='0' cellspacing='1' border='0' class='table text-center'>  <tbody>");
			noDataArray.push("<tr><th width='80%' colspan='7'>责任事故</th><th width='20%'></th></tr>");
			noDataArray.push("<tr><td class='red' colspan='8'>暂无数据</td></tr>");
			noDataArray.push(" <tbody> </table>");
			 $("#accidentDiv").append(noDataArray.join(""));
		}
	}

	function bulidPunishments(punishmentVOs,approveType){
		$("#punishmentRowNum").val(punishmentVOs.length);
		$("#punishmentDiv").empty();
		if(punishmentVOs.length>0){
			 var dataObject = {'Data': []};
			 for(var i =0;i<punishmentVOs.length;i++){
				 punishmentVOs[i]["personRowNum"]=(punishmentVOs.length*2);
				 dataObject.Data.push(punishmentVOs[i]);
			 }
			 if(approveType==page.professorApprove){
				 dataObject['punishment_grade_rows'] = punishmentVOs.length
			 }
			 var subTaskContent= $("#punishmentRec").render(dataObject);
			 $("#punishmentDiv").append(subTaskContent);
		}else{
			var noDataArray = [];
			noDataArray.push("<table cellpadding='0' cellspacing='1' border='0' class='table text-center'>  <tbody>");
			noDataArray.push("<tr><th width='80%' colspan='7'>处分</th><th width='20%'></th></tr>");
			noDataArray.push("<tr><td class='red' colspan='8'>暂无数据</td></tr>");
			noDataArray.push(" <tbody> </table>");
			 $("#punishmentDiv").append(noDataArray.join(""));
		}
	}
	
	page.bulidManageDiff = function (masterReviewVO){
		var rootPath = page.getBasePath();
		var manageDifficultySelId = masterReviewVO.manageDifficultyAttachMentVO.attachmentId;
		var manageDifficultyFileName = masterReviewVO.manageDifficultyAttachMentVO.fileName;
		if(manageDifficultySelId){
			var attachmentArray =[];
			attachmentArray.push("<a class=\"chachu\"  href=\""+rootPath+"WorkflowAttachMentDownload?attachmentId="+manageDifficultySelId+"\"><font style='color:#ffa200'>点击查看</font></a>");
			attachmentArray.push("&nbsp;&nbsp;");
		//	attachmentArray.push("<a class=\"chachu\"  href=\"#\" onclick=\"deleteReceiveFileAttachment(\'"+manageDifficultySelId+"\',this);\" >删除</a>");
			
			$("#manageDifficultySpan").append(attachmentArray.join(""));
		}
		
		var manageDifficultyAgoSelId = masterReviewVO.manageDifficultyAgoAttachMentVO.attachmentId;
		var manageDifficultyAgoFileName = masterReviewVO.manageDifficultyAgoAttachMentVO.fileName;
		if(manageDifficultyAgoSelId){
			var attachmentArray =[];
			attachmentArray.push("<a class=\"chachu\"  href=\""+rootPath+"WorkflowAttachMentDownload?attachmentId="+manageDifficultyAgoSelId+"\"><font style='color:#ffa200'>点击查看</font></a>");
			attachmentArray.push("&nbsp;&nbsp;");
		//	attachmentArray.push("<a class=\"chachu\"  href=\"#\" onclick=\"deleteReceiveFileAttachment(\'"+manageDifficultyAgoSelId+"\',this);\" >删除</a>");
			
			$("#manageDifficultyAgoSpan").append(attachmentArray.join(""));
		}
		
		
		 $("#schoolNameSpace").text(masterReviewVO.schoolNameSpace);
		$("#schoolType").text(JspParamUtil.paramVal('headmaster_school_type',masterReviewVO.schoolType));
	    $("#schoolCount").text(masterReviewVO.schoolCount);
		$("#studentNumber").text(masterReviewVO.studentNumber);
		
		 $("#schoolNameSpaceAgo").text(masterReviewVO.schoolNameSpaceAgo);
	    $("#schoolTypeAgo").text(JspParamUtil.paramVal('headmaster_school_type',masterReviewVO.schoolTypeAgo));
		$("#schoolCountAgo").text(masterReviewVO.schoolCountAgo);
		$("#studentNumberAgo").text(masterReviewVO.studentNumberAgo);
	}
	
	
	page.deleteReceiveFileAttachment= function(attachmentId,tdObject){
		var bcReq = new BcRequest('workflow','receiveFileAction','deleteReceiveFileAttachment');
		bcReq.setExtraPs({"attachmentId":attachmentId});
		bcReq.setSuccFn(function(data,status){
			 $(tdObject).parent().empty();
		});
		bcReq.postData();
	}
	
	
	page.getOptionGrades = function(indexName,gradeOptionName){
		 var result = 0;
		 var paper_grade_index_val =$("#"+indexName).val();
		 for(var i =0;i<paper_grade_index_val.length;i++){
			 var eachVal = $("#"+gradeOptionName+i).val();
			 result+=parseInt(eachVal)
		 }
		 return result || '0';
	}
	
})(jQuery); 