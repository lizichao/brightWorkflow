//工作流公共js
(function($) {
	Brightcom = {};
	page = Brightcom.workflow = $;
	page.processStartType = 1;
	page.taskType = 2;
	page.processViewType = 3;
	page.selectFlowId = ""; 
	page.nextPrincipalHandlers = []; 
	page.nextPrincipalDepartments = []; 
	
	
	page.getProcessBusinessKey = function(){
		return $("#processBusinessKey").val();
	}
	
	page.getProcessDefKey = function(){
		return $("#processDefKey").val();
	}
	
	page.getProcessDefName = function(){
		return $("#processDefName").val();
	}
	
	page.getTaskId = function(){
		return $("#taskId").val();
	}
	
	page.getTaskDefKey = function(){
		return $("#taskDefKey").val();
	}
	
	page.getTaskDefName = function(){
		return $("#taskDefName").val();
	}
	
	page.getProcessInstanceId = function(){
		return $("#processInstanceId").val();
	}
	
	page.setInternalOperate= function(value){
		$("#internalOperate").val(value);
	}
	
	page.setNextPrincipalHandlers= function(value){
		page.nextPrincipalHandlers.push(value)
	}
	
	
	page.setNextPrincipalDepartments= function(value){
		page.nextPrincipalDepartments.push(value)
	}
	
	/**
	 * 得到下拉框配置的选项集合
	 * @param selname
	 */
	page.initSelectCombox = function(dictionaryname,selname){
		  var bb
		  if (_sYs_paRm_liSt) {
		      var d = _sYs_paRm_liSt[dictionaryname];
		      if (d) {
		          bb = d.items ? d.items: []
		      }
		      selname = selname? selname : dictionaryname;
		      for(var i=0;i<bb.length;i++){
		    	  $("#"+selname).append("<option value='"+bb[i][0]+"'>"+bb[i][1]+"</option>"); 
		      }
		  }
	}
	
	/**
	 * 得到下拉框配置的选项集合
	 * @param selname
	 */
	page.getSelectCombox = function(dictionaryname){
		  var bb
		  var result_=[];
		  if (_sYs_paRm_liSt) {
		      var d = _sYs_paRm_liSt[dictionaryname];
		      if (d) {
		          bb = d.items ? d.items: []
		      }
		      for(var i=0;i<bb.length;i++){
		    	  var eachSel = {'id':bb[i][0],'text':bb[i][1]};
		    	  result_.push(eachSel);
		      }
		      return result_;
		  }
	}
	  
	
	page.buildProcessStartForm = function(processStartFormVO){
			$("#processStartTitleDiv").html("发起"+processStartFormVO.processDefinitionName+"流程");
			$("#processDefKey").val(processStartFormVO.processDefinitionKey);
			$("#processDefName").val(processStartFormVO.processDefinitionName);
			$("#processDefId").val(processStartFormVO.processDefinitionId);
			bulidComponentOperate(processStartFormVO.components,page.processStartType);
			bulidTaskOperateButton(processStartFormVO,page.processStartType);
	}
	
	page.taskHandlers = {};
	page.buildTaskForm = function(taskViewFormVO){
			var handlerArray = [];
			for(var i=0;i<taskViewFormVO.processHandlers.length;i++){
				handlerArray.push(taskViewFormVO.processHandlers[i].userName);
			}
			page.taskHandlers  = taskViewFormVO.processHandlers;
			$("#taskViewTitleDiv").text(taskViewFormVO.processTitle+"("+taskViewFormVO.processInstanceId+")");
			$("#taskNameSpan").text(taskViewFormVO.taskName);
			$("#taskHandlerSpan").text(handlerArray.join(","));
			$("#taskCreatorSpan").text(taskViewFormVO.processCreator.userName);
			$("#taskCreateTimeSpan").text(getSmpFormatDateByLong(taskViewFormVO.processCreateTime));
			
			$("#processDefKey").val(taskViewFormVO.processDefinitionKey);
			$("#processDefName").val(taskViewFormVO.processDefinitionName);
			$("#processDefId").val(taskViewFormVO.processDefinitionId);
			$("#processInstanceId").val(taskViewFormVO.processInstanceId);
			$("#taskId").val(taskViewFormVO.taskId);
			$("#taskDefKey").val(taskViewFormVO.taskKey);
			$("#taskName").val(taskViewFormVO.taskName);
			$("#processBusinessKey").val(taskViewFormVO.processBusinessKey);
		//	$("#taskViewFormDiv").load("/"+taskViewFormVO.taskFormKey);
			$("#lnkShowImage").attr("img","/processDiagram?processInstanceId="+taskViewFormVO.processInstanceId+"&processDefinitionId="+taskViewFormVO.processDefinitionId);
			$("#lnkShowImage").fancyZoom({scaleImg: true, closeOnClick: true});
			if(taskViewFormVO.subTask){
				//$("#taskViewButtonDiv").append("&nbsp;&nbsp;"+"<input type='button' id='submitSubTask' name='submitSubTask' value='提交' onclick=\"Brightcom.workflow.submitWorkflowRequest(\'"+page.taskType+"\');\" />"+"&nbsp;&nbsp;");
				//$("#taskViewButtonDiv").append("&nbsp;&nbsp;"+"<input type='button' id='returnSubTask' name='returnSubTask' value='返回' />"+"&nbsp;&nbsp;");
				$("#taskViewButtonDiv").append("<a id='submitSubTask' name='submitSubTask' class='btn1'   onclick=\"Brightcom.workflow.submitWorkflowRequest(\'"+page.taskType+"\');\" >提交<span></span></a>&nbsp;&nbsp;");
				$("#taskViewButtonDiv").append("<a id='returnSubTask' name='returnSubTask' class='btn1' onclick=\"javascript:history.go(-1);\" >返回<span></span></a>"+"&nbsp;&nbsp;");
				return;
			}
		//	$("#processImage").attr("src","/processDiagram?processInstanceId="+taskViewFormVO.processInstanceId);
	
			
			//Brightcom.workflow.beforeBuildTaskForm && Brightcom.workflow.beforeBuildTaskForm(taskViewFormVO);
			bulidTaskOperateButton(taskViewFormVO,page.taskType);
			//bulidMultiOperate(taskViewFormVO,page.taskType);
			Brightcom.workflow.afterBuildTaskForm && Brightcom.workflow.afterBuildTaskForm(taskViewFormVO);
			bulidComponentOperate(taskViewFormVO.components,page.taskType);
			bulidSubTaskRecords(taskViewFormVO.subTasks)
	}
	
	page.flowInfos = {};
	function bulidTaskOperateButton(taskViewFormVO,operateType){
		    var operateButtonArray = [];
		    var multiType = taskViewFormVO.multiType;
		    var taskViewFormLen = taskViewFormVO.flows.length;
		    if(taskViewFormVO.monitor){
			  operateButtonArray.push("&nbsp;&nbsp;<a class='btn1' id=\'back_btn\' name='back_btn'  onclick=\"Brightcom.workflow.editCounterInfo('"+taskViewFormVO.multi_kind+"\')\" >修改流程<span></span></a>");
            }
			for (var j=0;j<taskViewFormVO.flows.length;j++){
				var flowObject = taskViewFormVO.flows[j];
				var flowId = flowObject.id;
				page.flowInfos[flowId] = flowObject;
				/*
				 * 会签节点有三种角色，监控人、主办人和普通人
				 * 如果下个节点有分支（一般是会签任务需要配置分支，普通任务则画多条出去的线就行），则以分支后面的路由操作为主，
				 * 如果当前用户是监控人，则他可以进行的操作是分支后路由的操作，要区分是强制结束会签还是最后一个正常结束会签
				 * 结束会签有三种情况会发生，一个是会签发起人，在我参与的流程可以结束会签，一个是会签的监控人，一个是主办人
				 * 这里要注意如果有监控人，就不要在给主办的按钮提示是否要强制结束主办任务了，就是不要初始化呈completePrincipalTask
				 * 也就是会签节点有分支的后面的主办按钮不要初始化成completePrincipalTask，否则主办审批也会把监控人的任务干掉
				 */
				if(flowObject.dest.type=='exclusiveGateway' && taskViewFormVO.multiUserTask){//会签监控人，需要以下操作
					var gatewayFlows = flowObject.dest.outgoingFlowVOs;
					for (var k=0;k<gatewayFlows.length;k++){
						var gatewayObject =  gatewayFlows[k];
						page.flowInfos[gatewayObject.id] = gatewayObject;
						if(multiType == 'parallel'){//并行会签
							if(taskViewFormVO.monitor){
								if(taskViewFormVO.lastMultiTask){
									operateButtonArray.push("<a class='btn1' id=\'"+gatewayObject.flowId+"\'  value=\'"+gatewayObject.name+"\'  onclick=\"Brightcom.workflow.completeButtonTask('"+gatewayObject.id+"\',\'"+operateType+"\')\" >"+gatewayObject.name+"<span></span></a>");
								}else{
								//	window.parent.Brightcom.workflow.closeCounterSignOperate(currentTaskKey,flowId,paramVariable);
									operateButtonArray.push("<a class='btn1' id=\'"+gatewayObject.flowId+"\'  value=\'"+gatewayObject.name+"\'  onclick=\"Brightcom.workflow.closeCounterSignOperate(\'"+taskViewFormVO.taskKey+"\',\'"+gatewayObject.id+"\');\" >"+gatewayObject.name+"<span></span></a>");
								}
							}
						}else if(multiType == 'sequential'){//串行会签，一般没有监控人，只是普通的审核人，但是需要分支的操作
							operateButtonArray.push("<a class='btn1' id=\'"+gatewayObject.flowId+"\'  value=\'"+gatewayObject.name+"\'  onclick=\"Brightcom.workflow.completeButtonTask('"+gatewayObject.id+"\',\'"+operateType+"\')\" >"+gatewayObject.name+"<span></span></a>");
						}
					}
					
					if(multiType == 'parallel' && !taskViewFormVO.monitor){
						if(taskViewFormVO.principal){//这里的主办没用completePrincipalTask是因为正常审批如果是主办也会提示主办人
							//operateButtonArray.push("<a  class='btn1' id=\'"+flowId+"\' name='qry_btn'   onclick=\"Brightcom.workflow.completePrincipalTask(\'"+taskViewFormVO.taskKey+"\',\'"+taskViewFormVO.flows[j].id+"\');\" >"+flowObject.name+"<span></span></a>");
							operateButtonArray.push("<a  class='btn1' id=\'"+flowId+"\' name='qry_btn'   onclick=\"Brightcom.workflow.completeButtonTask(\'"+taskViewFormVO.flows[j].id+"\',\'"+operateType+"\');\" >"+taskViewFormVO.flows[j].name+"<span></span></a>");
						}else{
							operateButtonArray.push("<a  class='btn1' id=\'"+flowId+"\' name='qry_btn'   onclick=\"Brightcom.workflow.completeButtonTask(\'"+taskViewFormVO.flows[j].id+"\',\'"+operateType+"\');\" >"+taskViewFormVO.flows[j].name+"<span></span></a>");
						}
					}
				}else{
					if(taskViewFormVO.multiUserTask){//如果是会签任务，主办的按钮事件改写，协办按钮的名字改写
						/*if(taskViewFormVO.multiCreator){
							//operateButtonArray.push("<input type='button' id=\'closeCounterSign\' name='qry_btn' value=\'"+taskViewFormVO.flows[j].name+"\'   />");
							operateButtonArray.push("<a  class='btn1' id=\'closeCounterSign\'  value=\'"+taskViewFormVO.flows[j].name+"\'   >"+taskViewFormVO.flows[j].name+"<span></span></a>");
							$("#closeCounterSign").click(function () {
								page.componentOperation[ $(this).attr('id')].call($(this),taskViewFormVO.taskKey);
						    });
						}else*/
						if(taskViewFormVO.principal){
							operateButtonArray.push("<a  class='btn1' id=\'"+flowId+"\' name='qry_btn'   onclick=\"Brightcom.workflow.completePrincipalTask(\'"+taskViewFormVO.taskKey+"\',\'"+taskViewFormVO.flows[j].id+"\');\" >"+flowObject.name+"<span></span></a>");
						}else{
							operateButtonArray.push("<a  class='btn1' id=\'"+flowId+"\' name='qry_btn'   onclick=\"Brightcom.workflow.completeAssistTask(\'"+taskViewFormVO.flows[j].id+"\',\'"+operateType+"\');\" >办结任务<span></span></a>");
						}
					}else{
						operateButtonArray.push("<a id=\'"+flowId+"\' class='btn1' value=\'"+taskViewFormVO.flows[j].name+"\'  onclick=\"Brightcom.workflow.completeButtonTask(\'"+taskViewFormVO.flows[j].id+"\',\'"+operateType+"\');\" >"+taskViewFormVO.flows[j].name+"<span></span></a>");
					}
				}
			}
			
			var buttonDivName = (operateType == page.taskType)? "taskViewButtonDiv" : "processStartButtonDiv";
			var buttonDiv = $("#"+buttonDivName);
			if(operateType == page.taskType){
				operateButtonArray.push("<a  class='btn1' id=\'back_btn\' name='back_btn' value=\'返回\'  onclick=\"javascript:history.go(-1);\" >返回<span></span></a>");
			}else{
			}
			buttonDiv.append(operateButtonArray.join("&nbsp;&nbsp;"));
	}
	
	page.editCounterInfo = function(multiKind){
		//var multi_kind = taskViewFormVO.multi_kind;
		var bcReq = new BcRequest('workflow','componentAction','getEditCounterInfo');
		bcReq.setExtraPs({
			"processInstanceId":page.getProcessInstanceId(),
			"processDefKey":page.getProcessDefKey(),
			"taskDefKey":page.getTaskDefKey(),
			"taskId":page.getTaskId(),
			"multiKind":multiKind
		});
		bcReq.setSuccFn(function(data,status){
			var url = "";
			if(multiKind =='1'){
				 url = "/workflow/component/editCounterDept.jsp";
			}else{
				 url = "/workflow/component/editCounterUser.jsp";
			}
		
			var dlgid = "editCounterInfoDialog";
	        var options = {};
	        options.title = "修改流程";
	        options.width = "468";
	        options.height = "508";
	        
	        options.param = {
	        		"processInstanceId":page.getProcessInstanceId(),
	        		"processDefKey":page.getProcessDefKey(),
	        		"processDefName":page.getProcessDefName(),
	        		"taskDefKey":page.getTaskDefKey(),
	        		"taskId":page.getTaskId(),
	        		"businessKey":page.getProcessBusinessKey(),
	        		"multiKind":multiKind,
	        		"infoData":data
	        };
	        options.callback = function(data) {
	        };
	        $.Dialog.open(url, dlgid, options);
		});
		bcReq.postData();
	}
	
    function bulidMultiOperate(baseFormVO,operateType){
		//判断当前用户有会签权限，则增加三个会签功能组件
		if(baseFormVO.multiCreator){
			var buttonDivName = (operateType == page.taskType)? "taskViewButtonDiv" : "processViewButtonDiv";
			var buttonDiv = $("#"+buttonDivName);
			//buttonDiv.append("&nbsp;&nbsp;"+"<input type='button' id='addCounterSign' name='addCounterSign' value='加签' />"+"&nbsp;&nbsp;");
			//buttonDiv.append("&nbsp;&nbsp;"+"<input type='button' id='removeCounterSign' name='removeCounterSign' value='减签' />"+"&nbsp;&nbsp;");
			
			buttonDiv.append("&nbsp;&nbsp;"+"<a  class='btn1' id='addCounterSign' name='addCounterSign' value='加签' >加签<span></span></a>"+"&nbsp;&nbsp;");
			buttonDiv.append("&nbsp;&nbsp;"+"<a  class='btn1' id='removeCounterSign' name='removeCounterSign' value='减签' >减签<span></span></a>"+"&nbsp;&nbsp;");
			//if(operateType == page.processViewType){
				//buttonDiv.append("&nbsp;&nbsp;"+"<input type='button' id='closeCounterSign' name='closeCounterSign' value='结束会签' />"+"&nbsp;&nbsp;");
				buttonDiv.append("&nbsp;&nbsp;"+"<a  class='btn1' id='closeCounterSign' name='closeCounterSign' value='结束会签' >结束会签<span></span></a>"+"&nbsp;&nbsp;");
			//}
			
			$("#addCounterSign").click(function () {
					page.componentOperation[ $(this).attr('id')].call($(this),baseFormVO.multiUserTasks[0]);
			});
			$("#removeCounterSign").click(function () {
				page.componentOperation[ $(this).attr('id')].call($(this),baseFormVO.multiUserTasks[0]);
		    });
			$("#closeCounterSign").click(function () {
				page.componentOperation[ $(this).attr('id')].call($(this),baseFormVO.multiUserTasks[0].id);
		    });
		}
    }
	   
	   
	
   function bulidComponentOperate(components,operateType){
	   var componentButtonArray = [];
		for (var j=0;j<components.length;j++){
			var componentObject = components[j];
			var componentId = componentObject.componentId;
			var componentType = componentObject.componentType;
			var componentLabel = componentObject.componentLabel;
			var componentCategory = componentObject.componentCategory;
			if(componentCategory == "0"){
				//componentButtonArray.push("<input type='button' id=\'"+componentType+"\' name='componentName' value=\'"+componentLabel+"\' />");
				componentButtonArray.push("<a class='btn1' id=\'"+componentType+"\' name='componentName'>"+componentLabel+"<span></span></a>");
			}else{
				if(componentType =='copyto'){
					$("#processTextComponentDiv").append("<label id='copyToLabel' name='copyToLabel'>抄送</label> " +
							"<input type='hidden' id='copyToValue' name='copyToValue' value='' /> " +
							"<input type='text' id='copyToComponent' name='copyToComponent' value='' readonly='readonly' /> " +
							"<input type='button' id='copyToButton' name='copyToButton' value='选择抄送人' />");
					$("#copyToButton").click(function () {
							page.componentOperation["copyTo"].call($(this));
					});
				}
			}
		}
		$("#taskViewButtonDiv").append("&nbsp;&nbsp;"+componentButtonArray.join("&nbsp;&nbsp;"));
		
		$("a[name='componentName']").each(function(i){
			var component = $(this);
			 $(this).click(function () {
				page.componentOperation[component.attr('id')].call($(this));
			  });
		 });
   }
   
	function bulidSubTaskRecords(subTasks){
		 $("#subTaskInfoDiv").empty();
		 if(subTasks.length>0){
			 $("#subTaskDiv").show()
			 var dataObject = {'Data': []};
			 for(var i =0;i<subTasks.length;i++){
				 dataObject.Data.push(subTasks[i]);
			 }
			 var subTaskContent= $("#subTaskRec").render(dataObject);
			 $("#subTaskInfoDiv").append(subTaskContent);
		 }
	}
	

	/**
	 * 审批按钮提交方法，根据下个节点的情况判断是否弹出人员选择框还是部门选择框还是不弹
	 */
	page.completeButtonTask = function(flowId,operateType){
		page.selectFlowId = flowId;
		flowObject = page.flowInfos[flowId];
		var targetNode = flowObject.dest;
		if(targetNode.type == 'userTask'){
			if(targetNode.multiType){//弹出部门会签
				if(targetNode.multi_kind == '1'){
					if(targetNode.multiDepartments.length == 0){
						alert("下个节点"+targetNode.name+"没有配置会签部门，请联系管理员配置！");
						return false;
					}
					openDepartmentDialog(targetNode,flowId,operateType);
				}else{
					if(targetNode.configHandlers.length>1){
						openUserDialog(targetNode,flowId,operateType);
					}else if(targetNode.configHandlers.length==1){
						//$("#nextHandlers").val(targetNode.configHandlers[0]);
						page.submitWorkflowRequest(operateType,flowObject.id,targetNode.configHandlers[0].userId);
					}else if(targetNode.configHandlers.length==0) {
						page.submitWorkflowRequest(operateType,flowObject.id);
						//alert("下个节点"+targetNode.name+"没有配置审批人，请联系管理员配置！");
						//return false;
					}
				}
			}else{
				if(targetNode.configHandlers.length>1){
					openUserDialog(targetNode,flowId,operateType);
				}else if(targetNode.configHandlers.length==1){
					//$("#nextHandlers").val(targetNode.configHandlers[0]);
					page.submitWorkflowRequest(operateType,flowObject.id,targetNode.configHandlers[0].userId);
				}else if(targetNode.configHandlers.length==0) {
					//alert("下个节点"+targetNode.name+"没有配置审批人，请联系管理员配置！");
					//return false;
				  page.submitWorkflowRequest(operateType,flowObject.id);
				}
			}
		}else{
			page.submitWorkflowRequest(operateType,flowObject.id);
		}
	}
	
	
	page.submitWorkflowRequest = function(submitType,flowId,nextHandlers,nextDepartments,nextPrincipalHandlers,nextPrincipalDepartments){
		page.selectFlowId = flowId;
	//	var defaultParam = flowId ? {'submitType':submitType,'sequenceFlow':flowId} : {'submitType':submitType};
		var defaultParam =  {'submitType':submitType,'sequenceFlow':flowId} ;
		var actionConfig = $("#workflowSubmitAction").val();
		var actionConfigArray = actionConfig.split("@@");
		var bcReq= new BcRequest(actionConfigArray[0],actionConfigArray[1],actionConfigArray[2]);
		
		var submitFormName = (submitType == page.processStartType)? 'startProcessform' : 'taskViewForm'; 
		var formJsonData = BcUtil.crForm(submitFormName);
		if(Brightcom.workflow.beforeSubmit && Brightcom.workflow.beforeSubmit(formJsonData,submitFormName)===false){
			return;
		}
		/*
		if(Brightcom.workflow.beforeSubmit ){
			var businessVO = Brightcom.workflow.beforeSubmit();
			if(businessVO === false){
				return;
			}
		}*/
		var businessVO = formJsonData || {};
		var processVO = page.getProcessVO(nextHandlers,nextDepartments,nextPrincipalHandlers,nextPrincipalDepartments);
		var submitParamVO = jQuery.extend(defaultParam, businessVO, processVO);
		
		submitParamVO.processParam = page.getProcessParamStr( businessVO.processParam || {});
		submitParamVO.sysName = actionConfigArray[0] || submitParamVO.sysName;
		submitParamVO.oprID = actionConfigArray[1] || submitParamVO.oprID;
		submitParamVO.actions = actionConfigArray[2] || submitParamVO.actions;
		bcReq.setExtraPs(submitParamVO);
		//bcReq.setForm("startProcessform");
		bcReq.setSuccFn(function(data,status){
			if(Brightcom.workflow.afterSubmit && typeof Brightcom.workflow.afterSubmit == 'function' ){
				Brightcom.workflow.afterSubmit(data);
				return 
			}
			if(submitType == page.processStartType){
				 window.location.href = "/workflow/workspace/myApplicant.jsp";
			}else{
				 window.location.href = "/workflow/workspace/myTask.jsp";
			}
			//$("#processStartTitleDiv").text("发起"+processStartFormVO.processDefName+"流程");
			//$("#processStartFormDiv").load("/"+processStartFormVO.processStartFormKey);
		});
		bcReq.postData();
	}
	
	page.getProcessVO = function(nextHandlers,nextDepartments,nextPrincipalHandlers,nextPrincipalDepartments){
		nextPrincipalHandlers = (nextPrincipalHandlers&&nextPrincipalHandlers.length>0 )? nextPrincipalHandlers.join(",") : page.nextPrincipalHandlers.join(",");
		nextPrincipalDepartments = (nextPrincipalDepartments && nextPrincipalDepartments.length>0) ? nextPrincipalDepartments.join(",") : page.nextPrincipalDepartments.join(",");
		var processVO = {};
		processVO.processDefKey = $("#processDefKey").val();
		processVO.processDefName = $("#processDefName").val();
		processVO.processDefId = $("#processDefId").val();
		processVO.processInstanceId = $("#processInstanceId").val() || '';
		processVO.taskId = $("#taskId").val() || '';
		processVO.taskDefKey = $("#taskDefKey").val() || '';
		processVO.nextHandlers = nextHandlers || '';
		processVO.nextDepartments = nextDepartments || '';
		processVO.nextPrincipalHandlers = nextPrincipalHandlers || '';
		processVO.nextPrincipalDepartments = nextPrincipalDepartments || '';
		return processVO;
	}
	
	page.getProcessParamStr = function(businessProcessParamVO){
		//var resultParamArray = [];
		var processTitle =  $("#processTitle").val() || "";
		var internalRemark =  $("#internalRemark").val() || "";
		var internalOperate =  $("#internalOperate").val() || "";
		var copyToVariable =  $("#copyToValue").val() || "";
		var defaultParamVO = {
				'processTitle':processTitle,
				'internalRemark':internalRemark,
				'internalOperate':internalOperate,
				'copyToVariable':copyToVariable
		}
	//	resultParamArray.push({'internalRemark':internalRemark});
		var resultParamVO = jQuery.extend({}, defaultParamVO, businessProcessParamVO);
		return JSON.stringify(resultParamVO);
	}
	
	
	page.completePrincipalTask = function(multiTaskKey,flowId){
		page.selectFlowId = flowId;
		var processInstanceId = $("#processInstanceId").val();
		var bcReq = new BcRequest('workflow','formServiceOperate','getMultiNodes');
		bcReq.setExtraPs({
			"processInstanceId":processInstanceId,
			"multiTaskKey":multiTaskKey
		});
		bcReq.setSuccFn(function(data,status){
			  var isHaveAssist = data.Data[0].isHaveAssist;
			  var assistDepartmentArray = data.Data[0].AssistDepartment;//协办部门
			  var assistDepartmentStr="";
			  for(var j=0;j<assistDepartmentArray.length;j++){
				  assistDepartmentStr+=assistDepartmentArray[j].deptName;
				  if(j<(assistDepartmentArray.length-1)){
					  assistDepartmentStr+=",";
				  }
			  }
			  if(isHaveAssist && isHaveAssist=='1'){
					var multiConfirm = window.confirm("还有协办部门["+assistDepartmentStr+"]未完成审批，需要强制结束会签任务吗？");
					if (multiConfirm){
						page.componentOperation['closeCounterSign'](multiTaskKey);
					}else{
						return false;
					}
			  }else{
				  Brightcom.workflow.completeButtonTask(flowId,page.taskType);
			  }
		});
		bcReq.postData();
	}
	
	page.completeAssistTask = function(flowId,operateType){
		$("#internalOperate").val("办结任务");
		page.completeButtonTask(flowId,operateType);
	}
	
	page.closeCounterSignOperate = function(toCloseCounterNodeId,flowId,processParam){
		var multiConfirm = window.confirm("还有其他人未完成审批，需要强制结束会签任务吗？");
		if (multiConfirm){
			processParam = processParam || {};
			page.selectFlowId = flowId;
			processParam.sequenceFlow = flowId;
			page.componentOperation['closeCounterSign'].call(null,toCloseCounterNodeId,processParam);
		}else{
			return false;
		}
	}
	
	page.addCounterSign = function(toAddCounterNode){
		page.componentOperation['addCounterSign'].call(null,toAddCounterNode);
	}
	
	page.removeCounterSign = function(toRemoveCounterNode){
		page.componentOperation['removeCounterSign'].call(null,toRemoveCounterNode);
	}
	
	page.componentOperation = {
			transfer:function(){
				    var url = "/workflow/component/transfer.jsp";
		            var dlgid = "transferDialog";
		            var options = {};
		            options.title = "转审";
		            options.width = "228";
		            options.height = "226";
		            
		            var taskId = $("#taskId").val();
		            var taskDefKey = $("#taskDefKey").val();
		            var processInstanceId = $("#processInstanceId").val();
		            var processDefKey = $("#processDefKey").val();
		            var processDefName = $("#processDefName").val();
		            var processDefId = $("#processDefId").val();
		            options.param = {
		            		'taskId': taskId,
		            		'taskDefKey': taskDefKey,
		            		'processInstanceId':processInstanceId,
		            		'processDefKey':processDefKey,
		            		'processDefName':processDefName,
		            		'processDefId':processDefId
		            };
		            options.callback = function(data) {
		                //这是回调函数 ，data自行定义，在模态窗口关闭后对本页面进行操作 
		            };
		            $.Dialog.open(url, dlgid, options);
			},
			rollbackPrevious : function(){
				var bcReq = new BcRequest('workflow','componentAction','rollbackPrevious');
				
				 var processInstanceId = $("#processInstanceId").val();
				 var processDefinitionId = $("#processDefId").val();
				 var taskId = $("#taskId").val();
				 var taskDefKey = $("#taskDefKey").val();
				 
				//var taskId = $("#taskId").val();
				var componentParam = {'taskId':taskId,'processInstanceId':processInstanceId};
				bcReq.setExtraPs({
					"taskDefKey":taskDefKey,
					"taskId":taskId,
					"processInstanceId":processInstanceId,
					"processDefinitionId":processDefinitionId,
					"componentName":"rollbackPrevious",
					"componentParam":JSON.stringify(componentParam)
					});
				bcReq.setSuccFn(function(data,status){
					 window.location.href = "/workflow/workspace/myTask.jsp";
				});
				bcReq.postData();
			},/*
			counterSignOperate : function(){
				var url = "/workflow/component/counterSignOperate.jsp";
	            var dlgid = "counterSignOperateDialog";
	            var options = {};
	            options.title = "加减签";
	            options.width = "500";
	            options.height = "300";
	            
	            var taskId = $("#taskId").val();
	            var processInstanceId = $("#processInstanceId").val();
	            var processDefId = $("#processDefId").val();
	            options.param = {
	            		 'taskId': taskId,
	            		 'processInstanceId':processInstanceId,
	            		 'processDefId':processDefId
	            };
	            options.callback = function(data) {
	                //这是回调函数 ，data自行定义，在模态窗口关闭后对本页面进行操作 
	            };
	            $.Dialog.open(url, dlgid, options);
			},*/
			addSubTask : function(){
				var url = "/workflow/component/selectCurrentDeptUser.jsp";
				var dlgid = "addSubTaskDialog";
	            var options = {};
	            options.title = "添加子任务";
	            options.width = "788";
	            options.height = "468";
	            
	            var taskId = $("#taskId").val();
	            options.param = {
	            		 'taskId': taskId,
	            		 'componentType':'addSubTask',
	            	     'userOrigin':'nodeConfigUser'
	            };
	            options.callback = function(data) {
	            	var subTaskUserId = data.selectedUserIds.join(",");
	            	var subTaskUserName = data.selectedUserNames.join(",");
	            	var taskId = $("#taskId").val();
	            	var processInstanceId = $("#processInstanceId").val();
	            	var processDefKey = $("#processDefKey").val();
	                var processDefName = $("#processDefName").val();
	                var processDefId = $("#processDefId").val();
	                
	            	var bcReq = new BcRequest('workflow','componentAction','addSubTask');
	            	var componentParam = {
	            			'processInstanceId':processInstanceId,
	            			'processDefKey':processDefKey,
	            			'processDefName':processDefName,
	            			'processDefId':processDefId,
	            			'taskId':taskId,
	            			'subTaskUserId':subTaskUserId,
	            			'subTaskUserName':subTaskUserName,
	            			'subTaskName':'交办'
	            	};
	            	bcReq.setExtraPs({"componentName":"addSubTask","componentParam":JSON.stringify(componentParam)});
	            	bcReq.setSuccFn(function(data,status){
	            		window.location.href = "/workflow/template/completeTaskForm.jsp?taskId="+taskId;
	            	});
	            	bcReq.postData();
	            };
	            $.Dialog.open(url, dlgid, options);
			},
			addCounterSign : function(toAddCounterNode){
				var multi_kind = toAddCounterNode.multi_kind;
				var url = "";
				if(multi_kind == '1'){//部门会签
					url = "/workflow/component/addCounterSign.jsp";
				    if(toAddCounterNode.addCounterDepartments.length==0){
		            	alert("可以加签的部门为空！");
		            	return false;
		            }
				}else{
					url = "/workflow/component/addUserCounterSign.jsp";
				    if(toAddCounterNode.addCounterUsers.length==0){
		            	alert("可以加签的人为空！");
		            	return false;
		            }
				}
			
	            var dlgid = "addCounterSignDialog";
	            var options = {};
	            options.title = "加签";
	            options.width = "420";
	            options.height = "468";
	         
	            
	            var taskId = $("#taskId").val();
	            var processDefKey = $("#processDefKey").val();
	            var processDefName = $("#processDefName").val();
	            var processDefId = $("#processDefId").val();
	            var processInstanceId = $("#processInstanceId").val();
	            options.param = {
	            		 'toAddCounterNode': toAddCounterNode,
	            		 'processInstanceId':processInstanceId,
	            		 'processDefKey':processDefKey,
	            		 'processDefName':processDefName,
	            		 'processDefId':processDefId,
	            		 'multiKind':multi_kind
	            };
	            options.callback = function(data) {
	            	window.location.reload();
	                //这是回调函数 ，data自行定义，在模态窗口关闭后对本页面进行操作 
	            };
	            $.Dialog.open(url, dlgid, options);
			},
			removeCounterSign : function(toRemoveCounterNode){
				var multi_kind = toRemoveCounterNode.multi_kind;
				var url = "";
				if(multi_kind == '1'){//部门会签
					url = "/workflow/component/removeCounterSign.jsp";
	               // if(toRemoveCounterNode.removeCounterDepartments.length==1){
	                	//alert("当前会签节点只有一个部门，不能减签！");
		            	//return false;
					//}
					if(toRemoveCounterNode.removeCounterDepartments.length==0){
		            	alert("可以减签的部门为空！");
		            	return false;
			        }
				}else{
					url = "/workflow/component/removeUserCounterSign.jsp";
					if(toRemoveCounterNode.removeCounterUsers.length==1){
		            	alert("当前会签节点只有一个审核人，不能减签！");
		            	return false;
			        }
					if(toRemoveCounterNode.removeCounterUsers.length==0){
		            	alert("可以减签的人为空！");
		            	return false;
			        }
				}
	            var dlgid = "removeCounterSignDialog";
	            var options = {};
	            options.title = "减签";
	            options.width = "420";
	            options.height = "468";
	          
	            
	            var taskId = $("#taskId").val();
	            var processInstanceId = $("#processInstanceId").val();
	            var processDefId = $("#processDefId").val();
	            var processDefKey = $("#processDefKey").val();
	            var processDefName = $("#processDefName").val();
	            options.param = {
	            		 'toRemoveCounterNode': toRemoveCounterNode,
	            		 'processInstanceId':processInstanceId,
	            		 'processDefKey':processDefKey,
	            		 'processDefName':processDefName,
	            		 'processDefId':processDefId,
	            		 'multiKind':multi_kind
	            };
	            options.callback = function(data) {
	            	//window.location.reload();
	                //这是回调函数 ，data自行定义，在模态窗口关闭后对本页面进行操作 
	            };
	            $.Dialog.open(url, dlgid, options);
			},
			closeCounterSign : function(toCloseCounterNodeId,processParam){
				 var processParamObject = processParam || {};
				 Brightcom.workflow.beforeCloseCounterSign && Brightcom.workflow.beforeCloseCounterSign(toCloseCounterNodeId,processParamObject)
				
				 var bcReq = new BcRequest('workflow','componentAction','closeCounterSign');
				 var processInstanceId = $("#processInstanceId").val();
				 var processDefId = $("#processDefId").val();
				 var taskId = $("#taskId").val();
				 
				//var taskId = $("#taskId").val();
				//var componentParam = {'processInstanceId':processInstanceId,'multiNodeId':toCloseCounterNode.id};
				  
				bcReq.setExtraPs({
					'taskId':taskId,
					'query_processInstanceId':processInstanceId,
					'query_processDefId':processDefId,
					'multiNodeId':toCloseCounterNodeId,
					'processParam':JSON.stringify(processParamObject)
				});
				bcReq.setSuccFn(function(data,status){
					 window.location.href = "/workflow/workspace/myTask.jsp";
				});
				bcReq.postData();
			},
			addTaskHandler : function(){
				var url = "/workflow/component/addTaskHandler.jsp";
	            var dlgid = "addTaskHandlerDialog";
	            var options = {};
	            options.title = "新增处理人";
	            options.width = "470";
	            options.height = "469";
	            
	            var taskId = $("#taskId").val();
	            var processInstanceId = $("#processInstanceId").val();
	            var processDefId = $("#processDefId").val();
	            var processDefKey = $("#processDefKey").val();
	            var processDefName = $("#processDefName").val();
	            options.param = {
	            		 'taskId': taskId,
	            		 'processInstanceId':processInstanceId,
	            		 'processDefKey':processDefKey,
	            		 'processDefName':processDefName,
	            		 'processDefId':processDefId,
	            		  'taskHandlers':page.taskHandlers
	            };
	            options.callback = function(data) {
	                //这是回调函数 ，data自行定义，在模态窗口关闭后对本页面进行操作 
	            };
	            $.Dialog.open(url, dlgid, options);
			},
			copyTo : function(){
				var url = "/workflow/component/selectMultiUser.jsp";
				var dlgid = "copyToDialog";
	            var options = {};
	            options.title = "抄送";
	            options.width = "788";
	            options.height = "468";
	            
	            options.param = {
	            		// 'taskId': taskId,
	            		 'componentType':'copyTo'
	            };
	            options.callback = function(data) {
	          	  $("#copyToValue").val(data.selectedUserIds.join(","));
	        	  $("#copyToComponent").val(data.selectedUserNames.join(","));
	            };
	            $.Dialog.open(url, dlgid, options);
				/*
				var _height = window.screen.availHeight-380;
				var _width = window.screen.availWidth-560;

				var _top = (window.screen.availHeight-30-_height)/2;       //获得窗口的垂直位置;
			  	var _left = (window.screen.availWidth-10-_width)/2;           //获得窗口的水平位置;
				 
				var url = "/workflow/component/selectMultiUser.jsp?componentType=copyTo";
				window.open(url,"选择用户",'height='+_height+',width='+_width+',top='+_top+',left='+_left+',toolbar=no,menubar=yes,scrollbars=yes,resizable=no,location=no, status=no');
				*/
			},
			revokeTask : function(revokeTaskVO){
				var bcReq = new BcRequest('workflow','componentAction','revokeTask');
				bcReq.setExtraPs({
					"sourceTaskId":revokeTaskVO.sourceTaskId,
					"targetTaskKey":revokeTaskVO.targetTaskKey,
					"targetTaskAssignee":revokeTaskVO.targetTaskAssignee,
					"targetTaskName":revokeTaskVO.targetTaskName,
					"processInstanceId":revokeTaskVO.processInstanceId
				});
				bcReq.setSuccFn(function(data,status){
					 window.location.href = "/workflow/workspace/myTask.jsp";
				});
				bcReq.postData();
			}
	}
	
	
	page.userPrincipal = false;
	function openUserDialog(targetNode,flowId,operateType,isPrincipal){
	    var url = "/workflow/component/selectTaskUser.jsp";
        var dlgid = "selectTaskUserDialog";
        var options = {};
        options.title = "选择审批人";
        options.width = "465";
        options.height = "469";
        
        var taskId = $("#taskId").val();
       // var selectedUsers = $("#selectHandlers").val();
        options.param = {
        		'taskId':taskId,
        		'flowId':flowId,
        		//'selectedUsers': selectHandlers,
        		'configHandlers': targetNode.configHandlers,
        		'operateType': operateType,
        		'isPrincipal': isPrincipal || page.userPrincipal
        };
    
        $.Dialog.open(url, dlgid, options);
	}
	
	page.departmentPrincipal = false;
	function openDepartmentDialog(targetNode,flowId,operateType,isPrincipal){
	    var url = "/workflow/component/selectTaskDepartment.jsp";
        var dlgid = "selectTaskDepartmentDialog";
        var options = {};
        options.title = "选择会签部门";
        options.width = "465";
        options.height = "469";
        if(isPrincipal || page.departmentPrincipal){
        	  options.height = "498";
        }
       
        
        var taskId = $("#taskId").val();
       // var selectedUsers = $("#selectHandlers").val();
        options.param = {
        		'taskId':taskId,
        		'flowId':flowId,
        		//'selectedUsers': selectHandlers,
        		'multiDepartments': targetNode.multiDepartments,
        		'operateType': operateType,
        		'isPrincipal': isPrincipal ||page.departmentPrincipal
        };
        options.callback = function(data) {
        	
        };
        $.Dialog.open(url, dlgid, options);
	}
	
	
	page.showProcessImage = function(){
		$.ajax({
			url: url,
			dataType: 'jsonp',
			cache: false,
			async: true,
			success: function(data, textStatus) {
				console.log("ajax returned data");
			}
		})
		
		var processInstanceId = $("#processInstanceId").val();
		var processDefinitionId = $("#processDefId").val();
		var bcReq = new BcRequest('workflow','workSpaceAction','generateDiagram');
		bcReq.setExtraPs({"processInstanceId":processInstanceId,"processDefinitionId":processDefinitionId});
		bcReq.setSuccFn(function(data,status){
			// if(data.Data.length>0){
				 $.Dialog.openImg(data.Data[0].relativePath);
			// }
		});
		bcReq.postData();
	}
	
	page.buildProcessViewForm = function(processViewFormVO){
		var handlerArray = [];
		for(var i=0;i<processViewFormVO.processHandlers.length;i++){
			handlerArray.push(processViewFormVO.processHandlers[i].userName);
		}
		$("#processViewTitleDiv").text(processViewFormVO.processTitle+"("+processViewFormVO.processInstanceId+")");
		$("#processHandlerSpan").text(handlerArray.join(","));
		$("#processCreatorSpan").text(processViewFormVO.processCreator.userName);
		$("#processCreateTimeSpan").text(getSmpFormatDateByLong(processViewFormVO.processCreateTime));
		
		$("#processDefKey").val(processViewFormVO.processDefinitionKey);
		$("#processDefName").val(processViewFormVO.processDefinitionName);
		$("#processDefId").val(processViewFormVO.processDefinitionId);
		$("#processInstanceId").val(processViewFormVO.processInstanceId);
		$("#processBusinessKey").val(processViewFormVO.processBusinessKey);
		//$("#processViewButtonDiv").append("<input type='button' id='returnBackButton' name='returnBackButton' value='返回' />"+"&nbsp;&nbsp;");
		$("#processViewButtonDiv").append("<a class='btn1' id='returnBackButton' name='returnBackButton' value='返回' onclick=\"javascript:history.go(-1);\">返回<span></span></a>"+"&nbsp;&nbsp;");
	//	bulidMultiOperate(processViewFormVO,page.processViewType);
		bulidRevokeTaskOperate(processViewFormVO)
		bulidProcessImageLink(processViewFormVO)
	}
	
	function bulidProcessImageLink(processVO){
		$("#lnkShowImage").attr("img","/processDiagram?processInstanceId="+processVO.processInstanceId+"&processDefinitionId="+processVO.processDefinitionId);
       if(processVO.processEndTime){
    	  var imgProperty = $("#lnkShowImage").attr("img");
    	  $("#lnkShowImage").attr("img",imgProperty+"&isProcessEnd=1");
		}
		$("#lnkShowImage").fancyZoom({scaleImg: true, closeOnClick: true});
	}
	
	function bulidRevokeTaskOperate(processViewFormVO){
		if(processViewFormVO.haveRevokeTask){
			//var revokeTaskButton = "<input type='button' id='revokeTaskButton' name='revokeTaskButton'  value='收回任务' />"
			var revokeTaskButton = "<a class='btn1' id='revokeTaskButton' name='revokeTaskButton'  value='收回任务' >收回任务<span></span></a>"
			$("#processViewButtonDiv").append("&nbsp;&nbsp;"+revokeTaskButton);
			 $("#revokeTaskButton").click(function () {
					page.componentOperation["revokeTask"].call($(this),processViewFormVO.revokeTask);
			});
		}
	}
	
	
	page.showProcessLog = function(){
		var logIsHidden = $("#processLogDiv").is(":hidden");
		 $("#processLogDiv").toggle();
		if(logIsHidden){
			var processInstanceId = $("#processInstanceId").val();
			var bcReq = new BcRequest('workflow','formServiceOperate','getApproveLog');
			bcReq.setExtraPs({"processInstanceId":processInstanceId});
			bcReq.setSuccFn(function(data,status){
				 $("#processLogDiv").empty();
				 if(data.Data.length>0){
					 var processLogContent= $("#processLogRec").render(data);
					 $("#processLogDiv").append(processLogContent);
				 }else{
				   $("#processLogDiv").append("<span style='width:100%;color:red;'>暂无数据</span>");
				 }
			});
			bcReq.postData();
		}
	}
	
	page.selectMultiUserCallBack =function(componentType,selectedUsers){
		 switch (componentType) {
	          case "copyTo":
	        	  var selectedUserId="";
	        	  var selectedUserName="";
	        	  for(var i =0;i<selectedUsers.length;i++){
	        		  selectedUserId+=selectedUsers[i].userId;
	        		  selectedUserName+=selectedUsers[i].userName;
	        		  if(i<(selectedUsers.length-1)){
	        			  selectedUserId+= ",";
	        			  selectedUserName+= ",";
	        		  }
	        	  }
	        	  $("#copyToValue").val(selectedUserId);
	        	  $("#copyToComponent").val(selectedUserName);
	              break;
	          case "addTaskHandler":
	              h = "url";
	              break;
	          default:
	              break
       }
	}

	/*
	 * MAP对象，实现MAP功能
	 *
	 * 接口：
	 * size()     获取MAP元素个数
	 * isEmpty()    判断MAP是否为空
	 * clear()     删除MAP所有元素
	 * put(key, value)   向MAP中增加元素（key, value) 
	 * remove(key)    删除指定KEY的元素，成功返回True，失败返回False
	 * get(key)    获取指定KEY的元素值VALUE，失败返回NULL
	 * element(index)   获取指定索引的元素（使用element.key，element.value获取KEY和VALUE），失败返回NULL
	 * containsKey(key)  判断MAP中是否含有指定KEY的元素
	 * containsValue(value) 判断MAP中是否含有指定VALUE的元素
	 * values()    获取MAP中所有VALUE的数组（ARRAY）
	 * keys()     获取MAP中所有KEY的数组（ARRAY）
	 *
	 * 例子：
	 * var map = new Map();
	 *
	 * map.put("key", "value");
	 * var val = map.get("key")
	 * ……
	 *
	 */
	page.HashMap = function(){
	    this.elements = new Array();

	    //获取MAP元素个数
	    this.size = function() {
	        return this.elements.length;
	    };

	    //判断MAP是否为空
	    this.isEmpty = function() {
	        return (this.elements.length < 1);
	    };

	    //删除MAP所有元素
	    this.clear = function() {
	        this.elements = new Array();
	    };

	    //向MAP中增加元素（key, value) 
	    this.put = function(_key, _value) {
	        this.elements.push( {
	            key : _key,
	            value : _value
	        });
	    };

	    //删除指定KEY的元素，成功返回True，失败返回False
	    this.removeByKey = function(_key) {
	        var bln = false;
	        try {
	            for (i = 0; i < this.elements.length; i++) {
	                if (this.elements[i].key == _key) {
	                    this.elements.splice(i, 1);
	                    return true;
	                }
	            }
	        } catch (e) {
	            bln = false;
	        }
	        return bln;
	    };
	    
	    //删除指定VALUE的元素，成功返回True，失败返回False
	    this.removeByValue = function(_value) {//removeByValueAndKey
	        var bln = false;
	        try {
	            for (i = 0; i < this.elements.length; i++) {
	                if (this.elements[i].value == _value) {
	                    this.elements.splice(i, 1);
	                    return true;
	                }
	            }
	        } catch (e) {
	            bln = false;
	        }
	        return bln;
	    };
	    
	    //删除指定VALUE的元素，成功返回True，失败返回False
	    this.removeByValueAndKey = function(_key,_value) {
	        var bln = false;
	        try {
	            for (i = 0; i < this.elements.length; i++) {
	                if (this.elements[i].value == _value && this.elements[i].key == _key) {
	                    this.elements.splice(i, 1);
	                    return true;
	                }
	            }
	        } catch (e) {
	            bln = false;
	        }
	        return bln;
	    };

	    //获取指定KEY的元素值VALUE，失败返回NULL
	    this.get = function(_key) {
	        try {
	            for (i = 0; i < this.elements.length; i++) {
	                if (this.elements[i].key == _key) {
	                    return this.elements[i].value;
	                }
	            }
	        } catch (e) {
	            return false;
	        }
	        return false;
	    };

	    //获取指定索引的元素（使用element.key，element.value获取KEY和VALUE），失败返回NULL
	    this.element = function(_index) {
	        if (_index < 0 || _index >= this.elements.length) {
	            return null;
	        }
	        return this.elements[_index];
	    };

	    //判断MAP中是否含有指定KEY的元素
	    this.containsKey = function(_key) {
	        var bln = false;
	        try {
	            for (i = 0; i < this.elements.length; i++) {
	                if (this.elements[i].key == _key) {
	                    bln = true;
	                }
	            }
	        } catch (e) {
	            bln = false;
	        }
	        return bln;
	    };

	    //判断MAP中是否含有指定VALUE的元素
	    this.containsValue = function(_value) {
	        var bln = false;
	        try {
	            for (i = 0; i < this.elements.length; i++) {
	                if (this.elements[i].value == _value) {
	                    bln = true;
	                }
	            }
	        } catch (e) {
	            bln = false;
	        }
	        return bln;
	    };
	    
	    //判断MAP中是否含有指定VALUE的元素
	    this.containsObj = function(_key,_value) {
	        var bln = false;
	        try {
	            for (i = 0; i < this.elements.length; i++) {
	                if (this.elements[i].value == _value && this.elements[i].key == _key) {
	                    bln = true;
	                }
	            }
	        } catch (e) {
	            bln = false;
	        }
	        return bln;
	    };

	    //获取MAP中所有VALUE的数组（ARRAY）
	    this.values = function() {
	        var arr = new Array();
	        for (i = 0; i < this.elements.length; i++) {
	            arr.push(this.elements[i].value);
	        }
	        return arr;
	    };
	    
	    //获取MAP中所有VALUE的数组（ARRAY）
	    this.valuesByKey = function(_key) {
	        var arr = new Array();
	        for (i = 0; i < this.elements.length; i++) {
	            if (this.elements[i].key == _key) {
	                arr.push(this.elements[i].value);
	            }
	        }
	        return arr;
	    };

	    //获取MAP中所有KEY的数组（ARRAY）
	    this.keys = function() {
	        var arr = new Array();
	        for (i = 0; i < this.elements.length; i++) {
	            arr.push(this.elements[i].key);
	        }
	        return arr;
	    };
	    
	    //获取key通过value
	    this.keysByValue = function(_value) {
	        var arr = new Array();
	        for (i = 0; i < this.elements.length; i++) {
	            if(_value == this.elements[i].value){
	                arr.push(this.elements[i].key);
	            }
	        }
	        return arr;
	    };
	    
	    //获取MAP中所有KEY的数组（ARRAY）
	    this.keysRemoveDuplicate = function() {
	        var arr = new Array();
	        for (i = 0; i < this.elements.length; i++) {
	            var flag = true;
	            for(var j=0;j<arr.length;j++){
	                if(arr[j] == this.elements[i].key){
	                    flag = false;
	                    break;
	                } 
	            }
	            if(flag){
	                arr.push(this.elements[i].key);
	            }
	        }
	        return arr;
	    };
	    
	    //获取所有值得json字符串
	    this.toJsonString = function() {
	    	var arr = [];
	        for (i = 0; i < this.elements.length; i++) {
	        	 var singleKey = this.elements[i].key;
	        	 var singleValue = this.elements[i].value;
	        	 var jsonObject = {};
	        	 jsonObject[singleKey] = singleValue;
	        	 arr.push(jsonObject);
            }
	        return JSON.stringify(arr);
	    };
	    
	    this.toJsonObject = function() {
	    	var arr = {};
	        for (i = 0; i < this.elements.length; i++) {
	        	 var singleKey = this.elements[i].key;
	        	 var singleValue = this.elements[i].value;
	        	 arr[singleKey] = singleValue;
            }
	        return arr;
	    };
	    
	    this.getAllElement = function() {
	        return this.elements;
	    };
	}
	
	/*
	 * 得到日期，包括时分秒
	 */
	page.getDateByLongStr = function(longStr){
		return getSmpFormatDateByLong(longStr);
	}
	
	page.getDateStrByLong = function(longStr){
		return getSmpFormatDateByLong(longStr,false);
	}
	
	 //扩展Date的format方法   
    Date.prototype.format = function (format) {  
        var o = {  
            "M+": this.getMonth() + 1,  
            "d+": this.getDate(),  
            "h+": this.getHours(),  
            "m+": this.getMinutes(),  
            "s+": this.getSeconds(),  
            "q+": Math.floor((this.getMonth() + 3) / 3),  
            "S": this.getMilliseconds()  
        }  
        if (/(y+)/.test(format)) {  
            format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));  
        }  
        for (var k in o) {  
            if (new RegExp("(" + k + ")").test(format)) {  
                format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));  
            }  
        }  
        return format;  
    }  
    /**   
    *转换日期对象为日期字符串   
    * @param date 日期对象   
    * @param isFull 是否为完整的日期数据,   
    *               为true时, 格式如"2000-03-05 01:05:04"   
    *               为false时, 格式如 "2000-03-05"   
    * @return 符合要求的日期字符串   
    */    
    function getSmpFormatDate(date, isFull) {  
        var pattern = "";  
        if (isFull == true || isFull == undefined) {  
            pattern = "yyyy-MM-dd hh:mm:ss";  
        } else {  
            pattern = "yyyy-MM-dd";  
        }  
        return getFormatDate(date, pattern);  
    }  
    /**   
    *转换当前日期对象为日期字符串   
    * @param date 日期对象   
    * @param isFull 是否为完整的日期数据,   
    *               为true时, 格式如"2000-03-05 01:05:04"   
    *               为false时, 格式如 "2000-03-05"   
    * @return 符合要求的日期字符串   
    */    
  
    function getSmpFormatNowDate(isFull) {  
        return getSmpFormatDate(new Date(), isFull);  
    }  
    /**   
    *转换long值为日期字符串   
    * @param l long值   
    * @param isFull 是否为完整的日期数据,   
    *               为true时, 格式如"2000-03-05 01:05:04"   
    *               为false时, 格式如 "2000-03-05"   
    * @return 符合要求的日期字符串   
    */    
  
    function getSmpFormatDateByLong(l, isFull) {  
    	if(!l){
    		return "";
    	}
        return getSmpFormatDate(new Date(l), isFull);  
    }  
    /**   
    *转换long值为日期字符串   
    * @param l long值   
    * @param pattern 格式字符串,例如：yyyy-MM-dd hh:mm:ss   
    * @return 符合要求的日期字符串   
    */    
  
    function getFormatDateByLong(l, pattern) {  
    		return getFormatDate(new Date(l), pattern); 
    }  
    /**   
    *转换日期对象为日期字符串   
    * @param l long值   
    * @param pattern 格式字符串,例如：yyyy-MM-dd hh:mm:ss   
    * @return 符合要求的日期字符串   
    */    
    function getFormatDate(date, pattern) {  
    	 if (!date) {  
    		 return "";  
         } 
       // if (date == undefined) {  
          //  date = new Date();  
       // }  
        if (pattern == undefined) {  
            pattern = "yyyy-MM-dd hh:mm:ss";  
        }  
        return date.format(pattern);  
    }  
    

	Array.prototype.contains = function(element) { //利用Array的原型prototype点出一个我想要封装的方法名contains 
		for (var i = 0; i < this.length; i++) {
			if (this[i] == element) { //如果数组中某个元素和你想要测试的元素对象element相等，则证明数组中包含这个元素，返回true 
				return true;
			}
		}
	} 
})(jQuery); 