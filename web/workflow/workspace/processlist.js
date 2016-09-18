Ext.namespace("jsp.workflow.workspace");jsp.workflow.workspace.processlist=function(){var __caches__=[];this.__caches__=__caches__;
     	var componentSelRecord= Ext.data.Record.create([
		   	{name: 'component_type', type: 'string'},
		    {name: 'component_name', type: 'string'}
  		]);
  		
  		var roleSelRecord= Ext.data.Record.create([
		   	{name: 'roleid', type: 'string'},
		    {name: 'rolename', type: 'string'}
  		]);
  		
  		var departmentSelRecord= Ext.data.Record.create([
		   	{name: 'deptid', type: 'string'},
		    {name: 'deptname', type: 'string'}
  		]);
  		
    suspendRender=function(v){
	    if (v=='1'){
		   return '否';
		}
	    if (v=='2'){
		   return '是';
		}
    };
    
    procOperate = function(processDefId,suspensionState){
       var jr = new JrafRequest('workflow','ProcessManage','processDefinitionOperate');   
       suspensionState = suspensionState=='1' ? 'suspend' : 'active'           
       jr.setExtraPs({'query_processDefId':processDefId,'query_suspensionState':suspensionState});
       jr.setSuccFn(function(a,_resp,xr){
            // ProcessDefStore.setFormParam(Ext.getCmp('qryProcessDefForm'));
			// ProcessDefStore.setPageInfo(JrafSession.get('PageSize'),'1');
			 ProcessDefStore.load();
       });
       jr.postData();  
    }
    
    nodeTaskEdit = function(processdefId,processdefKey){
          nodeEditStore.setBaseParam('query_processDefId',processdefId);
          nodeEditStore.setBaseParam('query_processDefKey',processdefKey);
          nodeEditStore.load();
    /*
    
     economyStore.setBaseParam('qry_userid',param.cur_studentIds);
     economyStore.load({
       callback : function(r, options, success) {
         	var economyForm = Ext.getCmp("economyForm");
         	param.economy_ids = r[0].get('economy_id');
			economyForm.getForm().reset();
			economyForm.record=r[0];
			economyForm.getForm().loadRecord(r[0]);
      }
    })
       var jr = new JrafRequest('workflow','processEditManage','querySingleProcessDef');   
       jr.setExtraPs({'query_processDefId':processdefId,'query_processDefKey':processdefKey});
       jr.setSuccFn(function(a,_resp,xr){
	          var xmlDoc = _resp.responseXML.documentElement;
	          var responseObject = xmlDoc.getElementsByTagName("Response")[0];
	          
	          var nodeArray = []; 
	          var roleArray =[];
	          var departmentArray = [];
	          var datas = responseObject.getElementsByTagName("Data");
	          
	          var nodeRecord = datas[0].getElementsByTagName("Record");
	          nodeEditStore.removeAll();
	          for(var i=0;i<nodeRecord.length;i++){
	             var processdefkey=  nodeRecord[i].getElementsByTagName("processdefkey")[0].textContent;
	             var taskdefkey=  nodeRecord[i].getElementsByTagName("taskdefkey")[0].textContent;
	             var taskdefname=nodeRecord[i].getElementsByTagName("taskdefname")[0].textContent;
	             var node_type=nodeRecord[i].getElementsByTagName("node_type")[0].textContent;
	             var isRemind=nodeRecord[i].getElementsByTagName("isRemind")[0].textContent;
	             var duedate=nodeRecord[i].getElementsByTagName("duedate")[0].textContent;
	             var remind_mode=nodeRecord[i].getElementsByTagName("remind_mode")[0].textContent;
	             var id=nodeRecord[i].getElementsByTagName("id")[0].textContent;
	             nodeArray.push({
	               "processdefkey":processdefkey,
	               "taskdefkey":taskdefkey,
	               "taskdefname":taskdefname,
	               "node_type":node_type,
	               "isRemind":isRemind,
	               "duedate":duedate,
	               "remind_mode":remind_mode,
	               "id":id
	              });
	             
	             var recordType =  nodeEditStore.recordType;
	             var nr=new recordType();
	             nr.set("processdefkey",processdefkey);
	             nr.set("taskdefkey",taskdefkey);
	             nr.set("taskdefname",taskdefname);
	             nr.set("node_type",node_type);
	             nr.set("isRemind",isRemind);
	             nr.set("duedate",duedate);
	             nr.set("remind_mode",remind_mode);
	             nr.set("id",id);
	             nodeEditStore.add(nr);
	          }*/
	          
	          /*
	          var roleRecord = datas[1].getElementsByTagName("Record");
	         // roleStore.removeAll();
	          for(var i=0;i<roleRecord.length;i++){
	             var processdefkey=  roleRecord[i].getElementsByTagName("processdefkey")[0].textContent;
	             var taskdefkey=roleRecord[i].getElementsByTagName("taskdefkey")[0].textContent;
	             var roleid=roleRecord[i].getElementsByTagName("roleid")[0].textContent;
	             roleArray.push({"processdefkey":processdefkey,"taskdefkey":taskdefkey,"roleid":roleid});
	             
	             var recordType = roleStore.recordType;
	             var nr=new recordType();
	             nr.set("processdefkey",processdefkey);
	             nr.set("taskdefkey",taskdefkey);
	             nr.set("roleid",roleid);
	             nr.set("create_people",create_people);
	             nr.set("create_time",create_time);
	             nr.set("update_people",update_people);
	             nr.set("update_time",update_time);
	             roleStore.add(nr);
	          }
	          
	          var departmentRecord = datas[2].getElementsByTagName("Record");
	          for(var i=0;i<departmentRecord.length;i++){
	             var processdefkey=  departmentRecord[i].getElementsByTagName("processdefkey")[0].textContent;
	             var taskdefkey=departmentRecord[i].getElementsByTagName("taskdefkey")[0].textContent;
	             var department=departmentRecord[i].getElementsByTagName("deptid")[0].textContent;
	             departmentArray.push({"processdefkey":processdefkey,"taskdefkey":taskdefkey,"department":department});
	          }
       });
       jr.postData();  */
       
  
       var nodeEditWin=MainPanel['nodeEditId']; 
	   if(!nodeEditWin){
		     nodeEditWin=new Ext.Window({
			     title:'流程节点配置',
			     id:'nodeEditId',
			     layout:'fit',
			     width:850,
			     height:500,
			     closeAction:'hide',
			     plain:true,
			     modal: true,
			     items:nodePanel
		     });
		     MainPanel['nodeEditId']=nodeEditWin;
			__caches__.push(nodeEditWin);
	    } 
	    nodeEditWin.show();
       var tabpanel=JrafUTIL.findCmp(nodePanel,"nodeItemId");
	  
     // JrafUTIL.addTab(tabpanel,"/platform/sm/paramlist.jsp","功能组件","componentListId","2",{closable:false,border : false,jsurl:"jsp.platform.sm.paramlist"});
     // JrafUTIL.addTab(tabpanel,"/platform/sm/subsys.jsp","gg","roleListId","2",{closable:false,border : false,jsurl:"jsp.platform.sm.subsys"});
     // JrafUTIL.addTab(tabpanel,"/workflow/workspace/role.jsp","角色","roleListId","2",{closable:false,border : false,jsurl:"jsp.workflow.workspace.role"},true);
      //JrafUTIL.addTab(tabpanel,"/workflow/workspace/department.jsp","部门","departmentListId","2",{closable:false,border : false,jsurl:"jsp.workflow.workspace.department"});
     // JrafUTIL.addTab(tabpanel,"/workflow/workspace/component.jsp","功能组件","componentListId","2",{closable:false,border : false,jsurl:"jsp.workflow.workspace.component"});
     
     
          /*
        var _height = window.screen.availHeight-380;
       	var _width = window.screen.availWidth-560;
		var _top = (window.screen.availHeight-30-_height)/2;       //获得窗口的垂直位置;
	  	var _left = (window.screen.availWidth-10-_width)/2;           //获得窗口的水平位置;
	    var url = "/workflow/workspace/processnodeedit.jsp";
	    window.open(url,"流程节点编辑",'height='+_height+',width='+_width+',top='+_top+',left='+_left+',toolbar=no,menubar=yes,scrollbars=yes,resizable=no,location=no, status=no');
       */
   }
    
   string2XML =  function(xmlString) { 
		// for IE 
		if (window.ActiveXObject) { 
		var xmlobject = new ActiveXObject("Microsoft.XMLDOM"); 
		xmlobject.async = "false"; 
		xmlobject.loadXML(xmlString); 
		return xmlobject; 
		} 
		// for other browsers 
		else { 
		var parser = new DOMParser(); 
		var xmlobject = parser.parseFromString(xmlString, "text/xml"); 
		return xmlobject; 
		} 
   } 
   
   function XML2String(xmlObject) { 
		// for IE 
		if (window.ActiveXObject) { 
		return xmlObject.xml; 
		} 
		// for other browsers 
		else { 
		return (new XMLSerializer()).serializeToString(xmlObject); 
		} 
    } 
    
     startProcess = function(processdefkey){
         var url ="/workflow/template/startProcessForm.jsp?processkey="+processdefkey;
      	 //window.location.href =url
    }
    
    operationRender=function(value, meta, rec, rowIdx, colIdx, ds){
        var operationArray = [];
        var startUrl ="/workflow/template/startProcessForm.jsp?processkey="+rec.get('processdefkey');
        operationArray.push("<a href=\""+startUrl+"\"   >发起流程</a>");
        operationArray.push("<a href=\"javascript:processDefEdit(\'"+rec.get('processdefid')+"\',\'"+rec.get('processdefkey')+"\')\" >编辑</a>");
        operationArray.push("<a href=\"javascript:nodeTaskEdit(\'"+rec.get('processdefid')+"\',\'"+rec.get('processdefkey')+"\')\" >配置</a>");
        var suspensionState =  rec.get("suspension");
        var suspendText = suspensionState == '1' ? '挂起' : '激活';
        operationArray.push("<a href=\"javascript:procOperate(\'"+rec.get('processdefid')+"\',\'"+suspensionState+"\')\" >"+suspendText+"</a>");
        return operationArray.join(" ");
    };
    
    var processDefEditwin;
    var isCreate = false;
    processDefEdit = function(processdefId,processdefKey){
	        processDefEditwin=Ext.getCmp('processDefEditwin');
		    if(!processDefEditwin){ 
			    processDefEditwin=new Ext.Window({
					title:'流程定义编辑',
					id:'processDefEditwin',
					layout:'fit',
					width:410,
					height:220,
					closeAction:'hide',
					plain: true, 
					modal: true,
					autoDestroy:false,
					items: ProcessEditForm
				}); 
			} 
			jr=new JrafRequest('workflow','processEditManage','queryProcessEditDef');
			jr.setExtraPs({query_processDefKey:processdefKey});
			jr.setSuccFn(function(a,_resp,xr){
			    processDefEditwin.show();
			    ProcessEditForm.getForm().reset();
			    if(a.records.length ==0){
			      isCreate = true;
			    }else{
				    newRec=a.records[0];
					// ProcessEditForm.record=rec;
					ProcessEditForm.getForm().loadRecord(newRec);
			    }
				ProcessEditForm.getForm().findField('processdefkey').setValue(processdefKey);
			});
			jr.postData();
    }
    
   function impData(rec){
	   var xlsWin = MainPanel['xlswin'];
	   if(!xlsWin){
			xlsWin = new Ext.Window({
				title:'流程定义部署',
		        layout:'fit',
		        width:535,
		        height:320,
		        closeAction:'hide',
		        plain: true,
		        modal: true,
		        items:processUploadForm
			});
			MainPanel['xlswin']=xlsWin;
			__caches__.push(xlsWin);
	   }
     xlsWin.show();
   };
   
  downloadXml = function(processDefId){
       var jr = new JrafRequest('workflow','ProcessManage','downProcessResource');              
       jr.setExtraPs({'query_processDefId':processDefId,'resourceType':'xml'});
       jr.setSuccFn(function(a,_resp,xr){
		   //  _rec.set('state','1');
		//  _grid.unlockBtn.disable();
		 // _grid.lockBtn.enable();
       });
       jr.postData();       
  };
  
   downloadPng = function(processDefId){
       var jr = new JrafRequest('workflow','ProcessManage','downProcessResource');              
       jr.setExtraPs({'query_processDefId':processDefId,'resourceType':'image'});
       jr.setSuccFn(function(a,_resp,xr){
       });
       jr.postData();       
  };
  
  function xmlRender(value, meta, rec, rowIdx, colIdx, ds){
 	//var returnStr = "<a href=\"javascript:downloadXml()\"  title=\""+rec.get("processdefxml")+"\" target=\"_blank\" >"+rec.get("processdefxml")+"</a>";
 	var returnStr = "<a href=\"#\" onclick=\"downloadXml(\'"+rec.get('processdefid')+"\');\"  title=\""+rec.get("processdefxml")+"\" >"+rec.get("processdefxml")+"</a>";
	return returnStr;	
  };
  
 
  function pictureRender(value, meta, rec, rowIdx, colIdx, ds){
    var processdefid = rec.get('processdefid');
  //  var returnStr = "<a href=\"#\" onclick=\"downloadPng(\'"+processdefid+"\');\"  title=\""+rec.get("processdefpicture")+"\" >"+rec.get("processdefpicture")+"</a>";
    var returnStr = "<a href=\"/processDiagram?processDefinitionId="+processdefid+"&isProcessEnd=1\" target=\"_blank\" >"+rec.get("processdefpicture")+"</a>";
	return returnStr;	
  };
  
  function duedateRender(value, meta, rec, rowIdx, colIdx, ds){
    if(value == '0'){
      return "";
    }
    return value;
  }
  
  function isRemindRender(v){
       if (v=='1'){
		   return '否';
		}
	    if (v=='0'){
		   return '是';
		}
  }
  
  function remindModeRender(v){
        if (v=='1'){
		   return '邮件';
		}
	    if (v=='2'){
		   return '短信';
		}
		if (v=='3'){
		   return '邮件和短信';
		}
  }
  
  function ismultiRender(v){
        if (v=='0'){
		   return '否';
		}
	    if (v=='1'){
		   return '是';
		}
		
  }
  
   function refleshTab(selectTaskKey,processdefkey){
          componentStore.setBaseParam('query_taskDefKey',selectTaskKey);
          componentStore.setBaseParam('query_processDefKey',processdefkey);
          componentStore.load();
          
          
          departmentStore.setBaseParam('query_taskDefKey',selectTaskKey);
          departmentStore.setBaseParam('query_processDefKey',processdefkey);
          departmentStore.load();
          
          roleStore.setBaseParam('query_taskDefKey',selectTaskKey);
          roleStore.setBaseParam('query_processDefKey',processdefkey);
          roleStore.load();
          
   	    //var _grid =  Ext.getCmp("componentGridId");
       //_grid.getStore().load(_forma.record);
             
      //var tabpanel= Ext.getCmp("nodeItemId");
	 // var tabpanel=JrafUTIL.getCmp("nodeItemId");
	 // var tabpanel=JrafUTIL.findCmp(nodePanel,"nodeItemId");
	
	      /*
		  tabpanel.items.each(function(item){  
		     if(item.rendered){
		        if(item.tabid == 'roleListId'){
		             var jr = new JrafRequest('workflow','processEditManage','queryRole');
					 jr.setExtraPs({'query_processDefKey':processdefkey,'query_taskDefKey':selectTaskKey});
					 jr.setSuccFn(function(a,_resp,xr){
						 JrafUTIL.getCmp(item['jsurl']);
						//$("#dociframe-roleListId").length;
						// document.getElementById("dociframe-roleListId").reloadStore(a.records);
						 JrafUTIL.getCmp('jsp.workflow.workspace.role');
					 });
					 jr.postData();
		         }else if(item.tabid == 'departmentListId'){
		             JrafUTIL.getCmp(item['jsurl']);
						//$("#dociframe-roleListId").length;
					// document.getElementById("dociframe-roleListId").reloadStore(a.records);
				     JrafUTIL.getCmp('jsp.workflow.workspace.department');
		         
		         }else if(item.tabid == 'componentListId'){
		              // JrafUTIL.getCmp(item['jsurl']);
						//$("#dociframe-roleListId").length;
					 // document.getElementById("dociframe-roleListId").reloadStore(a.records);
					 
					 var jr = new JrafRequest('workflow','processEditManage','queryComponent');
					 jr.setExtraPs({'query_processDefKey':processdefkey,'query_taskDefKey':selectTaskKey});
					 jr.setSuccFn(function(a,_resp,xr){
					     JrafUTIL.getCmp('jsp.workflow.workspace.component').reloadStore(processdefkey,selectTaskKey);
					 });
					 jr.postData();
		         }
		     }
		   });*/
	}
	
	var multiKindCombo = new Ext.form.ComboBox({
	    typeAhead: true,
	    triggerAction: 'all',
	    lazyRender:true,
	    mode: 'local',
	    store: new Ext.data.ArrayStore({
	        id: 0,
	        fields: [
	            'multi_kind',
	            'multi_text'
	        ],
	        data: [['0', '无'],['1', '部门会签'], ['2', '人员会签']]
	    }),
	    valueField: 'multi_kind',
	    displayField: 'multi_text'
  });
	
	var remindModeCombo = new Ext.form.ComboBox({
	    typeAhead: true,
	    triggerAction: 'all',
	    lazyRender:true,
	    mode: 'local',
	    store: new Ext.data.ArrayStore({
	        id: 0,
	        fields: [
	            'remind_mode',
	            'remindText'
	        ],
	        data: [['1', '邮件'],['2', '短信'], ['3', '全部'],['0', '无']]
	    }),
	    valueField: 'remind_mode',
	    displayField: 'remindText'
  });
  
  var isRemindCombo = new Ext.form.ComboBox({
	    typeAhead: true,
	    triggerAction: 'all',
	    lazyRender:true,
	    mode: 'local',
	    store: new Ext.data.ArrayStore({
	        id: 0,
	        fields: [
	            'isremind',
	            'remindText'
	        ],
	        data: [['0', '否'],['1', '是']]
	    }),
	    valueField: 'isremind',
	    displayField: 'remindText'
  });
  
  /*
    componentSelectStore.on("beforeload", function() {
        var selects = Ext.getCmp("nodeRemindGrid").getSelectionModel().getSelected();
        componentSelectStore.baseParams = { nodeType: _strSupplier };
    });*/
    var ProcessDefRec=Ext.data.Record.create([ 
    {
    xtype : "Field",
    name : "processdefoperation",
    width : 150,
    fieldLabel : "操作",
    allowBlank : false,
    type : "string"
},
    {
    xtype : "Field",
    name : "processdefid",
    width : 150,
    fieldLabel : "流程定义ID",
    allowBlank : false,
    type : "string"
},  {
    xtype : "Field",
    name : "processdefname",
    width : 150,
    fieldLabel : "流程定义名称",
    allowBlank : false,
    type : "string"
},  {
    xtype : "Field",
    name : "processdefkey",
    width : 150,
    fieldLabel : "流程定义Key",
    allowBlank : false,
    type : "string"
},  {
    xtype : "Field",
    name : "processdefver",
    width : 150,
    fieldLabel : "流程定义版本",
    allowBlank : false,
    type : "string"
},  {
    xtype : "Field",
    name : "processdefxml",
    width : 150,
    fieldLabel : "XML",
    allowBlank : false,
    type : "string"
},  {
    xtype : "Field",
    name : "processdefpicture",
    width : 150,
    fieldLabel : "图片",
    allowBlank : false,
    type : "string"
},  {
    xtype : "Field",
    name : "processdefcreatetime",
    width : 150,
    fieldLabel : "部署时间",
    allowBlank : false,
    type : "date",
    dateFormat : "Y-m-d H:i:s"
}, {
    xtype : "Field",
    name : "suspension",
    width : 150,
    fieldLabel : "是否挂起",
    allowBlank : false,
    type : "string",
    dateFormat : "string"
}]);
this.ProcessDefRec=ProcessDefRec;this.__caches__.push(ProcessDefRec);var nodeEditRec=Ext.data.Record.create([{
    xtype : "Field",
    name : "id",
    width : 150,
    fieldLabel : "主键id",
    allowBlank : true,
    type : "string"
}, {
    xtype : "Field",
    name : "processdefkey",
    width : 150,
    fieldLabel : "流程定义key",
    allowBlank : true,
    type : "string"
},{
    xtype : "Field",
    name : "taskdefkey",
    width : 150,
    fieldLabel : "节点ID",
    allowBlank : true,
    type : "string"
},  {
    xtype : "Field",
    name : "taskdefname",
    width : 150,
    fieldLabel : "节点名称",
    allowBlank : true,
    type : "string"
},  {
    xtype : "Field",
    name : "node_type",
    width : 150,
    fieldLabel : "节点类型",
    allowBlank : true,
    type : "string"
},{
    xtype : "Field",
    name : "ismulti",
    width : 80,
    fieldLabel : "是否会签",
    allowBlank : true,
    type : "int"
},{
    xtype : "Field",
    name : "multi_kind",
    width : 80,
    fieldLabel : "会签类型",
    allowBlank : true,
    type : "int"
}, {
    xtype : "Field",
    name : "multi_type",
    width : 80,
    fieldLabel : "会签串并行类型",
    allowBlank : true,
    type : "string"
},  {
    xtype : "Field",
    name : "isremind",
    width : 80,
    fieldLabel : "是否提醒",
    allowBlank : true,
    type : "int"
},{
    xtype : "Field",
    name : "duedate",
    width : 80,
    fieldLabel : "延期天数",
    allowBlank : true,
    type : "int"
},{
    xtype : "Field",
    name : "remind_mode",
    width : 80,
    fieldLabel : "提醒方式",
    allowBlank : true,
    type : "int"
}]);
this.nodeEditRec=nodeEditRec;this.__caches__.push(nodeEditRec);var componentRec=Ext.data.Record.create([  {
    xtype : "Field",
    name : "processdefkey",
    width : 150,
    fieldLabel : "流程定义key",
    allowBlank : false,
    type : "string"
},  {
    xtype : "Field",
    name : "taskdefkey",
    width : 150,
    fieldLabel : "任务定义key",
    allowBlank : false,
    type : "string"
}, {
    xtype : "Field",
    name : "taskdefname",
    width : 150,
    fieldLabel : "任务定义名称",
    allowBlank : false,
    type : "string"
},   {
    xtype : "Field",
    name : "node_type",
    width : 150,
    fieldLabel : "节点类型",
    allowBlank : true,
    type : "string"
},{
    xtype : "Field",
    name : "node_type",
    width : 150,
    fieldLabel : "节点类型",
    allowBlank : false,
    type : "string"
},  {
    xtype : "Field",
    name : "component_type",
    width : 150,
    fieldLabel : "功能组件类型",
    allowBlank : false,
    type : "string"
},   {
    xtype : "Field",
    name : "component_name",
    width : 150,
    fieldLabel : "功能组件名称",
    allowBlank : false,
    type : "string"
}, {
    xtype : "Field",
    name : "create_people",
    width : 150,
    fieldLabel : "创建人",
    allowBlank : true,
    type : "string"
},  {
    xtype : "Field",
    name : "create_time",
    width : 150,
    fieldLabel : "创建时间",
    allowBlank : true,
    type : "date",
    dateFormat : "Y-m-d H:i:s"
},  {
    xtype : "Field",
    name : "update_people",
    width : 150,
    fieldLabel : "更新人",
    allowBlank : true,
    type : "string"
},  {
    xtype : "Field",
    name : "update_time",
    width : 150,
    fieldLabel : "更新时间",
    allowBlank : true,
    type : "date",
    dateFormat : "Y-m-d H:i:s"
}]);
this.componentRec=componentRec;this.__caches__.push(componentRec);var departmentRec=Ext.data.Record.create([  {
    xtype : "Field",
    name : "processdefkey",
    width : 150,
    fieldLabel : "流程定义key",
    allowBlank : false,
    type : "string"
},  {
    xtype : "Field",
    name : "taskdefkey",
    width : 150,
    fieldLabel : "任务定义key",
    allowBlank : false,
    type : "string"
},  {
    xtype : "Field",
    name : "taskdefname",
    width : 150,
    fieldLabel : "任务定义名称",
    allowBlank : false,
    type : "string"
}, {
    xtype : "Field",
    name : "node_type",
    width : 150,
    fieldLabel : "节点类型",
    allowBlank : true,
    type : "string"
},  {
    xtype : "Field",
    name : "deptid",
    width : 150,
    fieldLabel : "部门编号",
    allowBlank : false,
    type : "string"
}, {
    xtype : "Field",
    name : "deptname",
    width : 150,
    fieldLabel : "部门名称",
    allowBlank : false,
    type : "string"
},  {
    xtype : "Field",
    name : "create_people",
    width : 150,
    fieldLabel : "创建人",
    allowBlank : true,
    type : "string"
},  {
    xtype : "Field",
    name : "create_time",
    width : 150,
    fieldLabel : "创建时间",
    allowBlank : true,
    type : "date",
    dateFormat : "Y-m-d H:i:s"
},  {
    xtype : "Field",
    name : "update_people",
    width : 150,
    fieldLabel : "更新人",
    allowBlank : true,
    type : "string"
},  {
    xtype : "Field",
    name : "update_time",
    width : 150,
    fieldLabel : "更新时间",
    allowBlank : true,
    type : "date",
    dateFormat : "Y-m-d H:i:s"
}]);
this.departmentRec=departmentRec;this.__caches__.push(departmentRec);var roleRec=Ext.data.Record.create([  {
    xtype : "Field",
    name : "processdefkey",
    width : 150,
    fieldLabel : "流程定义key",
    allowBlank : false,
    type : "string"
},  {
    xtype : "Field",
    name : "taskdefkey",
    width : 150,
    fieldLabel : "任务定义key",
    allowBlank : false,
    type : "string"
},  {
    xtype : "Field",
    name : "taskdefname",
    width : 150,
    fieldLabel : "任务定义名称",
    allowBlank : false,
    type : "string"
}, {
    xtype : "Field",
    name : "node_type",
    width : 150,
    fieldLabel : "节点类型",
    allowBlank : true,
    type : "string"
}, {
    xtype : "Field",
    name : "roleid",
    width : 150,
    fieldLabel : "角色id",
    allowBlank : false,
    type : "string"
},{
    xtype : "Field",
    name : "rolename",
    width : 150,
    fieldLabel : "角色名称",
    allowBlank : false,
    type : "string"
},  {
    xtype : "Field",
    name : "create_people",
    width : 150,
    fieldLabel : "创建人",
    allowBlank : true,
    type : "string"
},  {
    xtype : "Field",
    name : "create_time",
    width : 150,
    fieldLabel : "创建时间",
    allowBlank : true,
    type : "date",
    dateFormat : "Y-m-d H:i:s"
},  {
    xtype : "Field",
    name : "update_people",
    width : 150,
    fieldLabel : "更新人",
    allowBlank : true,
    type : "string"
},  {
    xtype : "Field",
    name : "update_time",
    width : 150,
    fieldLabel : "更新时间",
    allowBlank : true,
    type : "date",
    dateFormat : "Y-m-d H:i:s"
}]);
this.roleRec=roleRec;this.__caches__.push(roleRec);var roleSelectStore=Ext.create({
  xxtype : "Store",
  xtype : "Store",
  classname : "roleSelectStore",
  recordType : roleSelRecord,
  idProperty : "roleid",
  api : {read:{sysName:"workflow",oprID:"processEditManage",actions:"getNodeRole"}},
  autoLoad : true,
  autoSave : false,
  writeAllFields : true,
  paramsAsHash : true,
  remoteSort : false
},'Store');
this.roleSelectStore=roleSelectStore;this.__caches__.push(roleSelectStore);var roleStore=Ext.create({
  xtype : "Store",
  classname : "roleStore",
  type : "JrafXmlStore",
  recordType : roleRec,
  idProperty : "id",
  api : {
    read :     {
      sysName : "workflow",
      oprID : "processEditManage",
      actions : "queryRole"
  },
    create :     {
      sysName : "workflow",
      oprID : "workflow_node_role",
      actions : "addrole"
  },
    update :     {
      sysName : "workflow",
      oprID : "workflow_node_role",
      actions : "updaterole"
  },
    destroy :     {
      sysName : "workflow",
      oprID : "processEditManage",
      actions : "deleteNodeRole"
  }
},
  autoLoad : false,
  autoSave : false,
  paramsAsHash : true,
   writeAllFields : true,
  remoteSort : true,
  listeners : {
    write : function (store,action,result,res,rs){
       if (res.success){
	     //  if (_editInfoWin){
	         //_editInfoWin.hide();
	      // }
       }
    }
}
},'Store');
this.roleStore=roleStore;this.__caches__.push(roleStore);var componentSelectStore=Ext.create({
  xxtype : "Store",
  xtype : "Store",
  classname : "componentSelectStore",
  recordType : componentSelRecord,
  idProperty : "component_type",
  api : {read:{sysName:"workflow",oprID:"processEditManage",actions:"getNodeComponent"}},
  autoLoad : false,
  autoSave : false,
  writeAllFields : true,
  paramsAsHash : false
 /* listeners:{ 
     load:function(){ 
        var selects = Ext.getCmp("nodeRemindGrid").getSelectionModel().getSelected();
	     var nodeType = selects.data.node_type;
	     var processdefkey = selects.data.processdefkey;
	     var taskdefkey = selects.data.taskdefkey;
	     componentSelectStore.setBaseParam('nodetype',nodeType);
	     componentSelectStore.setBaseParam('processdefkey',processdefkey);
	     componentSelectStore.setBaseParam('taskdefkey',taskdefkey);
	     componentSelectStore.load();
     } ,
	beforeload:function(){ 
	    var selects = Ext.getCmp("nodeRemindGrid").getSelectionModel().getSelected();
	    var nodeType = selects.data.node_type;
	       componentSelectStore.setBaseParam('nodetype',nodeType);
	}
 } */
},'Store');
this.componentSelectStore=componentSelectStore;this.__caches__.push(componentSelectStore);var componentStore=Ext.create({
  xtype : "Store",
  classname : "componentStore",
  type : "JrafXmlStore",
  recordType : componentRec,
  idProperty : "id",
  api : {
    read :     {
      sysName : "workflow",
      oprID : "processEditManage",
      actions : "queryComponent"
  },
    create :     {
      sysName : "workflow",
      oprID : "processEditManage",
      actions : "addNodeComponent"
  },
    update :     {
      sysName : "workflow",
      oprID : "processEditManage",
      actions : "updateComponent"
  },
    destroy :     {
      sysName : "workflow",
      oprID : "processEditManage",
      actions : "deleteNodeComponent"
  }
},
  autoLoad : false,
  autoSave : false,
   writeAllFields : true,
  paramsAsHash : true,
  remoteSort : true,
  listeners : {
    write : function (store,action,result,res,rs){
	    if (res.success){
	     //  if (_editInfoWin){
	         // _editInfoWin.hide();
	      // }
	    }
    }
}
},'Store');
this.componentStore=componentStore;this.__caches__.push(componentStore);var departmentSelectStore=Ext.create({
  xxtype : "Store",
  xtype : "Store",
  classname : "departmentSelectStore",
  recordType : departmentSelRecord,
  idProperty : "deptid",
  api : {read:{sysName:"workflow",oprID:"processEditManage",actions:"getNodeDepartment"}},
  autoLoad : true,
  autoSave : false,
  writeAllFields : true,
  paramsAsHash : true,
  remoteSort : false
},'Store');
this.departmentSelectStore=departmentSelectStore;this.__caches__.push(departmentSelectStore);var departmentStore=Ext.create({
  xtype : "Store",
  classname : "departmentStore",
  type : "JrafXmlStore",
  recordType : departmentRec,
  idProperty : "id",
  api : {
    read :     {
      sysName : "workflow",
      oprID : "processEditManage",
      actions : "queryDepartment"
  },
    create :     {
      sysName : "workflow",
      oprID : "workflow_node_department",
      actions : "addNodeDepartment"
  },
    update :     {
      sysName : "workflow",
      oprID : "workflow_node_department",
      actions : "updatedepartment"
  },
    destroy :     {
      sysName : "workflow",
      oprID : "processEditManage",
      actions : "deleteNodeDepartment"
  }
},
  autoLoad : false,
  autoSave : false,
   writeAllFields : true,
  paramsAsHash : true,
  remoteSort : true,
  listeners : {
    write : function (store,action,result,res,rs){
    if (res.success){
	    //if (_editInfoWin){
	      //_editInfoWin.hide();
	    //}
    }}
}
},'Store');
this.departmentStore=departmentStore;this.__caches__.push(departmentStore);var nodeEditStore=Ext.create({
  xxtype : "Store",
  xtype : "Store",
  classname : "nodeEditStore",
  type : "JrafXmlStore",
  recordType : nodeEditRec,
  idProperty : "id",
  api : {
    read :     {
      sysName : "workflow",
      oprID : "processEditManage",
      actions : "getNodeEdit"
  },
    create :     {
      sysName : "workflow",
      oprID : "processEditManage",
      actions : "insertNodeEdit"
  },
    update :     {
      sysName : "workflow",
      oprID : "processEditManage",
      actions : "updateNodeEdit"
  },
    destroy :     {
      sysName : "workflow",
      oprID : "processEditManage",
      actions : "deleteNodeEdit"
  }
},
  autoLoad : false,
  autoSave : false,
  writeAllFields : true,
  paramsAsHash : true,
  remoteSort : true,
  listeners : {
    write : function (store,action,result,res,rs){
      if (res.success){
     //_editInfoWin.hide();
      }
    }
  }
},'Store');
this.nodeEditStore=nodeEditStore;this.__caches__.push(nodeEditStore);var ProcessDefStore=Ext.create({
  xtype : "Store",
  classname : "ProcessDefStore",
  type : "JrafXmlStore",
  recordType : ProcessDefRec,
  idProperty : "processdefid",
  api : {
    read :     {
      sysName : "workflow",
      oprID : "ProcessManage",
      actions : "queryProcessDefinition"
  },
    create :     {
      sysName : "workflow",
      oprID : "ProcessManage",
      actions : "queryProcessDefinition"
  },
    update :     {
      sysName : "workflow",
      oprID : "ProcessManage",
      actions : "queryProcessDefinition"
  },
    destroy :     {
      sysName : "workflow",
      oprID : "ProcessManage",
      actions : "queryProcessDefinition"
  }
},
  autoLoad : true,
  autoSave : false,
  paramsAsHash : true,
  remoteSort : true,
  listeners : {
    write : function (store,action,result,res,rs){
      if (res.success){
       // _editInfoWin.hide();
      }
    }
  }
},'Store');
this.ProcessDefStore=ProcessDefStore;this.__caches__.push(ProcessDefStore);var processUploadForm=Ext.create({
	  xtype : "form",
	  classname : "processUploadForm",
	  fileUpload : true,
	  items : [   {
		xtype:'uploadpanel',
		itemId : 'uploadPanel',
		border : false,
		fileSize : 5024*3500,//限制文件大小
		uploadUrl :'/p.ajaxutf',
		filePostName : 'doc_file', //后台接收参数
		fileTypes : '*.bpmn',//可上传文件类型
		file_upload_limit:"5",
		height : 210,
		upBtn:false,
		stopBtn:false,
		fileTypesDescription:'所有文件',
		successFn:function(a){	
		    var xlsWin = MainPanel['xlswin'];
			xlsWin.hide();
			ProcessDefStore.load();
		}		
		}],
	  frame : true,
	  buttonAlign:'center',
	  buttons : [{
		text : '上 传',
		handler : function() {
			var _form=this.ownerCt.ownerCt;
			var up = _form.getComponent('uploadPanel');
			var ps = Ext.apply(
			  {'sysName':'workflow','oprID':'ProcessManage','actions':'importProcessDefinition'},
			  JrafUTIL.crParams([{doc_flag:"ImportProcessDefinition"}])
			);
			up.startUpload(this,null,ps);
			ProcessDefStore.load();
		}
  }]
},'panel');
this.processUploadForm=processUploadForm;this.__caches__.push(processUploadForm);var ProcessEditForm=Ext.create({
  xtype : "form",
  classname : "ProcessEditForm",
  defaultType : "textfield",
  frame:true,
  id : "ProcessEditForm",
  items : [    {
      fieldLabel : "流程key",
      name : "processdefkey",
	  maxLength : 20,
	  width : 180,
	  minLength : 2,
      allowBlank : false
   }, {
      fieldLabel : "流程整体信息表单路径",
      name : "processformkey",
	  maxLength : 60,
	  width : 220,
      allowBlank : false
   }
  ],
  buttonAlign : "center",
  labelAlign:"right",
  labelWidth : 150,
  buttons : [{
		text : "保存",				
		handler : function() {		 
		   // _forma = Ext.getCmp('ProcessEditForm');
		    var _forma=this.ownerCt.ownerCt;
			//if(_forma.record)
			//{
			    //var newFlag = Ext.isEmpty(_forma.record.data['userid']);
				var jr;
				
				//var processdDefKey =  _forma.record.data['processdDefKey'];
				//var processFormKey =  _forma.record.data['processFormKey'];
				
				var processDefKey =  _forma.getForm().findField("processdefkey").getValue();
				var processFormKey =  _forma.getForm().findField("processformkey").getValue();
				if (isCreate){
				   jr = new JrafRequest('workflow','processEditManage','addProcessEditDef');
				   //var _grid=Ext.getCmp('user-grid');
                  // _grid.getStore().add(_forma.record);
                  // _grid.getSelectionModel().selectLastRow();
				}
				else{
				   jr = new JrafRequest('workflow','processEditManage','updateProcessEditDef');
				}
				jr.setExtraPs({'processDefKey':processDefKey,'processFormKey':processFormKey});
				//jr.setForm('ProcessEditForm');
				jr.setSuccFn(function(a,_resp,xr){
					//_forma.getForm().updateRecord(_forma.record);
				  //	_forma.record.set('deptname',_forma.getForm().findField('udeptid').getRawValue());					
				   //	_forma.record.set('work_dept_name',_forma.getForm().findField('workdeptid').getRawValue());
					//if (newFlag){
					  // xr.realize(_forma.record,a.records);
					  // var _grid = Ext.getCmp('user-grid');
                      // _grid.getSelectionModel().selectLastRow();
					//}
					
					Ext.getCmp('processDefEditwin').hide();
				});
				jr.postData();
			//}
		}
   }]
},'panel');
this.ProcessEditForm=ProcessEditForm;this.__caches__.push(ProcessEditForm);var ComponentEditForm=Ext.create({
  xtype : "form",
  classname : "ComponentEditForm",
  frame:true,
  id:"ComponentEditForm",
  items : [
  {
      name : "processdefkey",
      fieldLabel : "processdefkey",
      allowBlank : true,
      xtype : "hidden"
  },
  
   {
      name : "taskdefkey",
      fieldLabel : "taskdefkey",
      allowBlank : true,
      xtype : "hidden"
  },
   {
      name : "taskdefname",
      fieldLabel : "taskdefname",
      allowBlank : true,
      xtype : "hidden"
  },
  {
      name : "node_type",
      fieldLabel : "rolename",
      allowBlank : true,
      xtype : "hidden"
  },{
      name : "component_name",
      fieldLabel : "component_name",
      allowBlank : true,
      xtype : "hidden"
  },
     {  
       //xtype : "multicombo",
       xtype : "combo",
       fieldLabel : "功能组件",
       id : "componentSelId",
       name : "component_type",
       store : componentSelectStore,
       hiddenName : "component_type",
       mode : "remote",
       displayField : "component_name",
       valueField : "component_type",
       triggerAction : "all",
       allowBlank : false,
       width : 150,
       listeners:{'select': function(combo) { 
			 Ext.getCmp('ComponentEditForm').getForm().findField('component_name').setValue(combo.getRawValue());  
	      }
	   }
     }
  ],
  buttonAlign : "center",
  labelAlign:"right",
  labelWidth : 90,
  buttons : [{
		text : "保存",				
		handler : function() {
		     var _form=this.ownerCt.ownerCt;
			 var rec=_form.record;
			 var processdefkey = rec.data.processdefkey;
			 var taskdefkey = rec.data.taskdefkey;
			 var taskdefname = rec.data.taskdefname;
			 var node_type = rec.data.node_type;
			 var componentType = _form.getForm().findField("componentSelId").getValue();
               
             var  _forma = Ext.getCmp('ComponentEditForm') || ComponentEditForm;
           	 var _grid =  Ext.getCmp("componentGridId");
             _grid.getStore().add(_forma.record);
             _grid.getSelectionModel().selectLastRow();
             
            var jr=new JrafRequest('workflow','processEditManage','addNodeComponent',{recordType:componentRec,idProperty:'id'});
			jr.setForm(_form);
            jr.setSuccFn(function(a,_resp,xr){
                _form.getForm().updateRecord(rec);
                xr.realize(rec,a.records);
                 	 _grid.getSelectionModel().selectLastRow();
                Ext.getCmp('componentEditwin').hide();
                _grid.getStore().load();
            });
            jr.postData();
            
               
			 /*
			var jr=new JrafRequest('workflow','processEditManage','findExistNodeComponent');
			jr.setExtraPs({
			  'processdefkey':processdefkey,
			  'taskdefkey':taskdefkey,
			  'component_type':componentType,
			 });
            jr.setSuccFn(function(a,_resp,xr){
            var isExist = a.records[0].get("isExist");
            
            });
            jr.postData();*/
            
             
               /* 
			var componentType = _form.getForm().findField("componentSelId").getValue();
			var componentName = _form.getForm().findField("componentSelId").lastSelectionText;
			var jr=new JrafRequest('workflow','processEditManage','addNodeComponent');
            jr.setExtraPs({
	            'processdefkey':processdefkey,
	            'taskdefkey':taskdefkey,
	            'taskdefname':taskdefname,
	            'node_type':node_type,
	            'component_type':componentType,
	            'component_name':componentName
            });
            jr.setSuccFn(function(a,_resp,xr){
                _forma.getForm().updateRecord(_forma.record);
                _forma.record.set('component_name',_forma.getForm().findField('componentSelId').getRawValue());	
                var componentId = a.records[0].get("componentId")
               _forma.record.set('component_id',componentId);	
               
				xr.realize(_forma.record,a.records);
                _grid.getSelectionModel().selectLastRow();
				Ext.getCmp('componentEditwin').hide();
            });
            jr.postData(); 	*/
		}
   }]
},'panel');
this.ComponentEditForm=ComponentEditForm;this.__caches__.push(ComponentEditForm);var RoleEditForm=Ext.create({
  xtype : "form",
  classname : "RoleEditForm",
  id:"RoleEditForm",
  frame:true,
  items : [
   {
      name : "processdefkey",
      fieldLabel : "processdefkey",
      allowBlank : true,
      xtype : "hidden"
  },
  
   {
      name : "taskdefkey",
      fieldLabel : "taskdefkey",
      allowBlank : true,
      xtype : "hidden"
  },
   {
      name : "taskdefname",
      fieldLabel : "taskdefname",
      allowBlank : true,
      xtype : "hidden"
  }, {
      name : "node_type",
      fieldLabel : "rolename",
      allowBlank : true,
      xtype : "hidden"
  },
  {
      name : "rolename",
      fieldLabel : "rolename",
      allowBlank : true,
      xtype : "hidden"
  },
     {  
       xtype : "combo",
       fieldLabel : "角色",
       id: "roleid",
       name : "roleid",
       hiddenName : "roleid",
       store : roleSelectStore,
       mode : "local",
       valueField : "roleid",
       displayField : "rolename",
       triggerAction : "all",
       allowBlank : false,
       width : 150,
       listeners:{'select': function(combo) { 
            Ext.getCmp('RoleEditForm').getForm().findField('rolename').setValue(combo.getRawValue());  
       }}   
     }
  ],
  buttonAlign : "center",
  labelAlign:"right",
  labelWidth : 90,
  buttons : [{
		text : "保存",				
		handler : function() {		
		    var _form=this.ownerCt.ownerCt;
			var rec=_form.record;
			 var processdefkey = rec.data.processdefkey;
			 var taskdefkey = rec.data.taskdefkey;
			 var taskdefname = rec.data.taskdefname;
			 var node_type = rec.data.node_type;
			// _form.record.set("roleid",_form.getForm().findField("roleid").getValue());
			// _form.record.set("rolename",_form.getForm().findField("roleid").getRawValue());
			
			 var  _forma = Ext.getCmp('RoleEditForm') || RoleEditForm;
              var _grid =  Ext.getCmp("roleGridId");
             _grid.getStore().add(_forma.record);
             _grid.getSelectionModel().selectLastRow();
                 
                   
                 if(_form.record)
				{
					var jr;
					jr=new JrafRequest('workflow','processEditManage','addNodeRole',{recordType:roleRec,idProperty:'id'});
					jr.setForm(_form);
                    jr.setSuccFn(function(a,_resp,xr){
                    	_form.getForm().updateRecord(_form.record);
                    	 xr.realize(_form.record,a.records);
                    	 _grid.getSelectionModel().selectLastRow();
				         Ext.getCmp('roleEditwin').hide();
				         _grid.getStore().load();
                    });
					jr.postData();
				}
				
                /*   
			var roleId = _form.getForm().findField("roleSelId").getValue();
			var roleName = _form.getForm().findField("roleSelId").lastSelectionText;
			var jr=new JrafRequest('workflow','processEditManage','addNodeRole');
            jr.setExtraPs({
	            'processdefkey':processdefkey,
	            'taskdefkey':taskdefkey,
	            'taskdefname':taskdefname,
	            'roleId':roleId,
	            'roleName':roleName
            });
            jr.setSuccFn(function(a,_resp,xr){
                _forma.getForm().updateRecord(_forma.record);
                _forma.record.set('rolename',_forma.getForm().findField('roleSelId').getRawValue());
                 var mainId = a.records[0].get("id");	
                 _forma.record.set('id',mainId);
                 
				xr.realize(_forma.record,a.records);
                _grid.getSelectionModel().selectLastRow();
				Ext.getCmp('roleEditwin').hide();
            });
            jr.postData(); */
		}
   }]
},'panel');
this.RoleEditForm=RoleEditForm;this.__caches__.push(RoleEditForm);var DepartmentEditForm=Ext.create({
  xtype : "form",
  classname : "DepartmentEditForm",
  frame:true,
  id :"DepartmentEditForm",
  items : [{
			      name : "processdefkey",
			      fieldLabel : "processdefkey",
			      allowBlank : true,
			      xtype : "hidden"
			  },
			  
			   {
			      name : "taskdefkey",
			      fieldLabel : "taskdefkey",
			      allowBlank : true,
			      xtype : "hidden"
			  },
			   {
			      name : "taskdefname",
			      fieldLabel : "taskdefname",
			      allowBlank : true,
			      xtype : "hidden"
			  }, {
			      name : "node_type",
			      fieldLabel : "rolename",
			      allowBlank : true,
			      xtype : "hidden"
			  },
			     {
			      name : "deptname",
			      fieldLabel : "deptname",
			      allowBlank : true,
			      xtype : "hidden"
			  },
			  {  
		       xtype : "combo",
		       fieldLabel : "部门名称",
		       id : "udeptid",
		       name : "deptid",
		       hiddenName : "deptid",
		       valueField : "deptid",
		       displayField : "deptname",
		       store : departmentSelectStore,
		       mode : "local",
		       triggerAction : "all",
		       allowBlank : false,
		       width : 150,
		       listeners:{'select': function(combo) { 
		             Ext.getCmp('DepartmentEditForm').getForm().findField('deptname').setValue(combo.getRawValue());   
		       }}   
             }
			  /*
              {
                    xtype : "depttreecombo",
                    fieldLabel : "部门名称",
                    id : "udeptid",
			        name : "deptid",
			        hiddenName : "deptid",
			        valueField : "deptid",
			        displayField : "deptname",
                    triggerAction : "all",
                    width : 150,
                    lazyRender : true,
                    editable : false,
                    forceSelection : true,
                    baseParams : {PageSize:'-1'},
                    allowBlank : false,
                    listeners:{'select': function(combo) { 
			            Ext.getCmp('DepartmentEditForm').getForm().findField('deptname').setValue(combo.getRawValue());  
			       }} 
                }*/
  ],
  buttonAlign : "center",
  labelAlign:"right",
  labelWidth : 90,
  buttons : [{
		text : "保存",				
		handler : function() {		 
		     var _form=this.ownerCt.ownerCt;
			 var rec=_form.record;
			 var processdefkey = rec.data.processdefkey;
			 var taskdefkey = rec.data.taskdefkey;
			 var taskdefname = rec.data.taskdefname;
			 var node_type = rec.data.node_type;
			 
			 var  _forma = Ext.getCmp('DepartmentEditForm') || DepartmentEditForm;
           	  var _grid =  Ext.getCmp("departmentGridId");
             _grid.getStore().add(_forma.record);
             _grid.getSelectionModel().selectLastRow();
                 
                   
                if(_form.record)
				{
					var jr;
					jr=new JrafRequest('workflow','processEditManage','addNodeDepartment',{recordType:departmentRec,idProperty:'id'});
					jr.setForm(_form);
                    jr.setSuccFn(function(a,_resp,xr){
                    	_form.getForm().updateRecord(_form.record);
                    	 xr.realize(_form.record,a.records);
                    	 _grid.getSelectionModel().selectLastRow();
				         Ext.getCmp('departmentEditwin').hide();
				         _grid.getStore().load();
                    });
					jr.postData();
				}
				
				/*
			var deptId = _form.getForm().findField("deptid").getValue();
			var deptName = _form.getForm().findField("deptid").lastSelectionText;
			var jr=new JrafRequest('workflow','processEditManage','addNodeDepartment');
            jr.setExtraPs({
	            'processdefkey':processdefkey,
	            'taskdefkey':taskdefkey,
	             'taskdefname':taskdefname,
	            'deptId':deptId,
	            'deptName':deptName
            });
            jr.setSuccFn(function(a,_resp,xr){
                _forma.getForm().updateRecord(_forma.record);
                _forma.record.set('deptname',_forma.getForm().findField('udeptid').getRawValue());	
                 var mainId = a.records[0].get("id");	
                 _forma.record.set('id',mainId);
				xr.realize(_forma.record,a.records);
                _grid.getSelectionModel().selectLastRow();
				Ext.getCmp('departmentEditwin').hide();
            });
            jr.postData();*/
		}
   }]
},'panel');
this.DepartmentEditForm=DepartmentEditForm;this.__caches__.push(DepartmentEditForm);var nodePanel=Ext.create({
    xtype : "panel",
    layout : "anchor",
    classname : "nodePanel",
    id:"nodeEditPanel",
    items : [{
        xtype : "editorgrid",
        frame : true,
        title : "节点信息",
        viewConfig : {forceFit:true},
        columnLines : true,
        autoHeight : false,
        height : 180,
        itemId : "nodeEditGrid",
        id : "nodeRemindGrid",
        store : nodeEditStore,
        bbar : new Ext.PagingToolbar({pageSize: JrafSession.get('PageSize'),store:nodeEditStore,displayInfo: true}),
        colModel : new Ext.grid.ColumnModel([new Ext.grid.CheckboxSelectionModel(),
				{width:40,sortable:true,header:'流程key',dataIndex:'processdefkey'},
				{width:40,sortable:true,header:'节点ID',dataIndex:'taskdefkey'},
				{width:40,sortable:true,header:'节点名称',dataIndex:'taskdefname'},
				{width:40,sortable:true,header:'节点类型',dataIndex:'node_type'},
				{width:40,sortable:true,header:'是否会签',dataIndex:'ismulti',renderer:ismultiRender},
				{width:40,sortable:true,header:'会签类型',dataIndex:'multi_kind',editor: multiKindCombo,
                   renderer : new Ext.util.Format.comboRenderer(multiKindCombo)
                },
				{width:40,sortable:true,header:'是否提醒',dataIndex:'isremind',editor: isRemindCombo,
                   renderer : new Ext.util.Format.comboRenderer(isRemindCombo)
                  // editor:new Ext.form.Checkbox(),renderer:new Ext.util.Format.checkboxRenderer()
				},
				{width:40,sortable:true,header:'超时天数',dataIndex:'duedate',editor: new Ext.form.TextField({allowBlank: false}),renderer:duedateRender},
				{width:40,sortable:true,header:'提醒方式',dataIndex:'remind_mode',editor: remindModeCombo,
                   renderer : new Ext.util.Format.comboRenderer(remindModeCombo) 
                 }
				]),
			sm : new Ext.grid.CheckboxSelectionModel({ listeners:{
				selectionchange:function(sm){
			        	var _grid=this.grid;
			        	if(sm.getCount()){
			        		//_grid.removeBtn.enable();
			        		//_grid.folderBtn.enable();
			        	}else{
			        		//_grid.removeBtn.disable();
			        		//_grid.folderBtn.disable();
			        	}
		        	},rowselect: function(sm, row, rec) {
		        	      var selectTaskKey = rec.data.taskdefkey;
		        	      var processdefkey = rec.data.processdefkey;
		        	      var node_type = rec.data.node_type;
		        	      refleshTab(selectTaskKey,processdefkey);
		        	      
		        	    //  componentSelectStore.setBaseParam('nodetype',node_type);
		        	     // componentSelectStore.setBaseParam('processdefkey',processdefkey);
	                     // componentSelectStore.setBaseParam('taskdefkey',selectTaskKey);
		        	     // componentSelectStore.load();
		        	      
		        	     // roleSelectStore.setBaseParam('nodetype',node_type);
		        	     // roleSelectStore.setBaseParam('processdefkey',processdefkey);
	                     // roleSelectStore.setBaseParam('taskdefkey',selectTaskKey);
		        	      //roleSelectStore.load();
		        	      
		        	     // departmentSelectStore.setBaseParam('nodetype',node_type);
		        	     // departmentSelectStore.setBaseParam('processdefkey',processdefkey);
	                     // departmentSelectStore.setBaseParam('taskdefkey',selectTaskKey);
		        	    //  departmentSelectStore.load();
	                }
        	}}),
        	listeners : {
        	    beforeedit :function(obj){
        	          var record= obj.record;
        	          var ismulti = record.data.ismulti;
        	          var multi_type = record.data.multi_type;
        	          var field = obj.field;
        	          if(field =='multi_kind'){
	        	          if(ismulti == 0){//判断是否是会签节点
	        	            return false;
	        	          }
	        	           if(multi_type == 'sequential'){//判断是否是串行会签
	        	            return false;
	        	          }
        	          }
        	           return true;
	            }
        	}
  	 },
  	   {
      xtype : "tabpanel",
      activeTab : 0,
      height : 400,
      plain : true,
      defaults : {autoScroll: true},
      items : [ {
    xtype : "panel",
    layout : "border",
       title : "功能组件",
       id:"componentTab",
    items : [       {
        xtype : "grid",
        itemId : "componentGrid",
        id : "componentGridId",
        region : "center",
        autoWidth : true,
        frame : true,
        viewConfig : {
        forceFit : false
        },
        stripeRows : true,
        columnLines : true,
        autoHeight : false,
        height : 320,
        store : componentStore,
        bbar : new Ext.PagingToolbar({pageSize: JrafSession.get('PageSize'),store:componentStore,displayInfo: true}),
        tbar : [
        {text:'新增',iconCls:'add',ref: '../addBtn',handler:function(){
                   //  JrafUTIL.getCmp("nodeEditGridd") ;
                // var rec= JrafUTIL.findCmp(nodePanel,"nodeEditGrid").getSelectionModel().getSelected()
                 var selectedRow =  Ext.getCmp("nodeRemindGrid").getSelectionModel().getSelected();
                 if(!selectedRow){
                    Ext.MessageBox.alert("提示","请先选择节点信息！！");
                     return false;
                 }
                 var selectedData =selectedRow.data;
                 var _grid=this.ownerCt.ownerCt;
	             var recordType=_grid.getStore().recordType;
	             var nr=new recordType();
	             nr.set("processdefkey",selectedData.processdefkey);
	             nr.set("taskdefkey",selectedData.taskdefkey);
	             nr.set("taskdefname",selectedData.taskdefname);
	             nr.set("node_type",selectedData.node_type);
	              
		         componentEditwin=Ext.getCmp('componentEditwin');
				    if(!componentEditwin){ 
					    componentEditwin=new Ext.Window({
							title:'新增功能组件',
							id:'componentEditwin',
							layout:'fit',
							width:300,
							height:180,
							closeAction:'hide',
							plain: true, 
							modal: true,
							autoDestroy:false,
							items: ComponentEditForm
						}); 
				  } 
                  componentSelectStore.setBaseParam('nodetype',selectedData.node_type);
        	      componentSelectStore.setBaseParam('processdefkey',selectedData.processdefkey);
                  componentSelectStore.setBaseParam('taskdefkey',selectedData.taskdefkey);
        	       componentSelectStore.load({callback:function(){
        	            componentEditwin.show();
			           ComponentEditForm.getForm().reset();
	                   ComponentEditForm.record=nr;
 	                    ComponentEditForm.getForm().loadRecord(nr);	
				   }});
        }},
        {xtype: 'tbseparator'},
        {text:'删除',iconCls:'delete',ref: '../removeBtn',disabled: true,handler:function(){
               var _grid=this.ownerCt.ownerCt;
	           var ckrs=_grid.getSelectionModel().getSelections();
	           for(var i=0;i<ckrs.length;i++){
	             _grid.getStore().remove(ckrs[i]);
	           }
	            _grid.getStore().save();
	            /*
	            var _grid=this.ownerCt.ownerCt;
	            var ckrs=_grid.getSelectionModel().getSelections();
	            var deleteComponentArray = [];
	            for(var i=0;i<ckrs.length;i++){
	                _grid.getStore().remove(ckrs[i]);
	                var chkData = ckrs[i].data;
	                var deleteComponentObject = {
	                   processdefkey : chkData.processdefkey,
	                   component_type :chkData.component_type,
	                   taskdefkey : chkData.taskdefkey
	                };
	                deleteComponentArray.push(deleteComponentObject);
	            }
	            var jr=new JrafRequest('workflow','processEditManage','deleteNodeComponent');
	            jr.setExtraPs({"deleteComponentIds":JSON.stringify(deleteComponentArray)});
	            jr.setExtraPs({"deleteComponentIds":deleteComponentArray});
	            jr.setSuccFn(function(a,_resp,xr){
	            });
	            jr.postData(); */
           }
         },
        {xtype: 'tbseparator'}
        ],
        colModel : new Ext.grid.ColumnModel([new Ext.grid.CheckboxSelectionModel(),
        {width:100,sortable:true,header:'流程定义key',dataIndex:'processdefkey'},
        {width:100,sortable:true,header:'任务定义key',dataIndex:'taskdefkey'},
        {width:100,sortable:true,header:'任务定义名称',dataIndex:'taskdefname'},
        {width:100,sortable:true,header:'节点类型',dataIndex:'node_type'},
        {width:100,sortable:true,header:'功能组件类型',dataIndex:'component_type'},
        {width:100,sortable:true,header:'功能组件名称',dataIndex:'component_name'},
        {width:100,sortable:true,header:'创建人',dataIndex:'create_people',hidden : true},
        {width:100,sortable:true,header:'创建时间',dataIndex:'create_time',renderer:Ext.util.Format.dateRenderer('Y-m-d H:i:s'),hidden : true},
        {width:100,sortable:true,header:'更新人',dataIndex:'update_people',hidden : true},
        {width:100,sortable:true,header:'更新时间',dataIndex:'update_time',renderer:Ext.util.Format.dateRenderer('Y-m-d H:i:s'),hidden : true}]),
        sm : new Ext.grid.CheckboxSelectionModel({ 
	        listeners:{selectionchange:function(sm){
		            var _grid=this.grid;
		            if(sm.getCount()){
		              _grid.removeBtn.enable();
		            }else{
		              _grid.removeBtn.disable();
		            }
	           }
	        }
	     }),
        listeners : {rowdblclick : function(g,rowIndex,e){
             var _rec=g.getSelectionModel().getSelected();
          }
        }
    }]
},  {
    xtype : "panel",
    layout : "border",
     title : "角色",
    items : [         {
        xtype : "grid",
        itemId : "roleGrid",
       id : "roleGridId",
        region : "center",
        autoWidth : true,
        frame : true,
        viewConfig : {
          forceFit : false
        },
        stripeRows : true,
        columnLines : true,
        autoHeight : false,
        height : 320,
        store : roleStore,
        bbar : new Ext.PagingToolbar({pageSize: JrafSession.get('PageSize'),store:roleStore,displayInfo: true}),
        tbar : [
        {text:'新增',iconCls:'add',ref: '../addBtn',handler:function(){
                 var selectedRow =  Ext.getCmp("nodeRemindGrid").getSelectionModel().getSelected();
                 if(!selectedRow){
                    Ext.MessageBox.alert("提示","请先选择节点信息！！");
                     return false;
                 }
                 var selectedData =selectedRow.data;
                 var _grid=this.ownerCt.ownerCt;
	             var recordType=_grid.getStore().recordType;
	             var nr=new recordType();
	           nr.set("processdefkey",selectedData.processdefkey);
	            nr.set("taskdefkey",selectedData.taskdefkey);
	            nr.set("taskdefname",selectedData.taskdefname);
	           nr.set("node_type",selectedData.node_type);
	         
	              
		          roleEditwin=Ext.getCmp('roleEditwin');
				    if(!roleEditwin){ 
					    roleEditwin=new Ext.Window({
							title:'新增角色',
							id:'roleEditwin',
							layout:'fit',
							width:300,
							height:230,
							closeAction:'hide',
							plain: true, 
							modal: true,
							autoDestroy:false,
							items: RoleEditForm
						}); 
					} 
					roleSelectStore.setBaseParam('nodetype',selectedData.node_type);
		        	roleSelectStore.setBaseParam('processdefkey',selectedData.processdefkey);
	                roleSelectStore.setBaseParam('taskdefkey',selectedData.taskdefkey);
		        	roleSelectStore.load({callback:function(){
        	           	  roleEditwin.show();
						  RoleEditForm.getForm().reset(); 
			              RoleEditForm.record=nr;
		 	              RoleEditForm.getForm().loadRecord(nr);
				   }});
         }
        },{xtype: 'tbseparator'},
        {text:'删除',iconCls:'delete',ref: '../removeBtn',disabled: true,handler:function(){
	           var _grid=this.ownerCt.ownerCt;
	           var ckrs=_grid.getSelectionModel().getSelections();
	           for(var i=0;i<ckrs.length;i++){
	             _grid.getStore().remove(ckrs[i]);
	           }
	            _grid.getStore().save();
        }},
       // {text:'保存',iconCls:'disk',ref: '../saveBtn',handler:function(){var _grid=this.ownerCt.ownerCt;Ext.Msg.confirm('保存数据','确认保存已修改数据?',function(btn){if(btn == 'yes'){_grid.getStore().save();}});}},
        {xtype: 'tbseparator'}
        ],
        colModel : new Ext.grid.ColumnModel([new Ext.grid.CheckboxSelectionModel(),
        {width:100,sortable:true,header:'流程定义key',dataIndex:'processdefkey'},
        {width:100,sortable:true,header:'任务定义key',dataIndex:'taskdefkey'},
        {width:100,sortable:true,header:'任务定义名称',dataIndex:'taskdefname'},
        {width:100,sortable:true,header:'角色id',dataIndex:'roleid',hidden : true},
        {width:100,sortable:true,header:'角色名称',dataIndex:'rolename'},
        {width:100,sortable:true,header:'创建人',dataIndex:'create_people',hidden : true},
        {width:100,sortable:true,header:'创建时间',dataIndex:'create_time',renderer:Ext.util.Format.dateRenderer('Y-m-d H:i:s'),hidden : true},
        {width:100,sortable:true,header:'更新人',dataIndex:'update_people',hidden : true},
        {width:100,sortable:true,header:'更新时间',dataIndex:'update_time',renderer:Ext.util.Format.dateRenderer('Y-m-d H:i:s'),hidden : true}]),
        sm : new Ext.grid.CheckboxSelectionModel({ 
        listeners:{selectionchange:function(sm){var _grid=this.grid;if(sm.getCount()){_grid.removeBtn.enable();}else{_grid.removeBtn.disable();}}}}),
        listeners : {rowdblclick : function(g,rowIndex,e){var _rec=g.getSelectionModel().getSelected();}}
    }]
},{
    xtype : "panel",
    layout : "border",
    title : "部门",
    id:"departmentTab",
    items : [{
        xtype : "grid",
        itemId : "departmentGrid",
        id : "departmentGridId",
        region : "center",
        autoWidth : true,
        frame : true,
        viewConfig : {
        forceFit : false
       },
        stripeRows : true,
        columnLines : true,
        autoHeight : false,
        height : 320,
        store : departmentStore,
        bbar : new Ext.PagingToolbar({pageSize: JrafSession.get('PageSize'),store:departmentStore,displayInfo: true}),
        tbar : [
        {text:'新增',iconCls:'add',ref: '../addBtn',handler:function(){
                 var selectedRow =  Ext.getCmp("nodeRemindGrid").getSelectionModel().getSelected();
                 if(!selectedRow){
                    Ext.MessageBox.alert("提示","请先选择节点信息！！");
                    return false;
                 }
                 var selectedData =selectedRow.data;
                 
	              var _grid=this.ownerCt.ownerCt;
	              var recordType=_grid.getStore().recordType;
	              var nr=new recordType();
	              nr.set("processdefkey",selectedData.processdefkey);
	              nr.set("taskdefkey",selectedData.taskdefkey);
	              nr.set("taskdefname",selectedData.taskdefname);
	              nr.set("node_type",selectedData.node_type);
	              
	              departmentEditwin=Ext.getCmp('departmentEditwin');
				    if(!departmentEditwin){ 
					    departmentEditwin=new Ext.Window({
							title:'新增部门',
							id:'departmentEditwin',
							layout:'fit',
							width:300,
							height:180,
							closeAction:'hide',
							plain: true, 
							modal: true,
							autoDestroy:false,
							items: DepartmentEditForm
						}); 
					} 
					departmentSelectStore.setBaseParam('nodetype',selectedData.node_type);
		        	departmentSelectStore.setBaseParam('processdefkey',selectedData.processdefkey);
	                departmentSelectStore.setBaseParam('taskdefkey',selectedData.taskdefkey);
		        	departmentSelectStore.load({callback:function(){
        	         	 departmentEditwin.show();
				         DepartmentEditForm.getForm().reset();
	                     DepartmentEditForm.record=nr;
 	                     DepartmentEditForm.getForm().loadRecord(nr);	
				   }});
         }},
        {xtype: 'tbseparator'},
        {text:'删除',iconCls:'delete',ref: '../removeBtn',disabled: true,handler:function(){
	           var _grid=this.ownerCt.ownerCt;
	           var ckrs=_grid.getSelectionModel().getSelections();
	           for(var i=0;i<ckrs.length;i++){
	              _grid.getStore().remove(ckrs[i]);
	           }
	           _grid.getStore().save();
        }},
        {xtype: 'tbseparator'}
        ],
        colModel : new Ext.grid.ColumnModel([new Ext.grid.CheckboxSelectionModel(),
        {width:100,sortable:true,header:'流程定义key',dataIndex:'processdefkey'},
        {width:100,sortable:true,header:'任务定义key',dataIndex:'taskdefkey'},
        {width:100,sortable:true,header:'任务定义名称',dataIndex:'taskdefname'},
        {width:100,sortable:true,header:'部门编号',dataIndex:'deptid',hidden : true},
        {width:100,sortable:true,header:'部门名称',dataIndex:'deptname'},
        {width:100,sortable:true,header:'创建人',dataIndex:'create_people',hidden : true},
        {width:100,sortable:true,header:'创建时间',dataIndex:'create_time',renderer:Ext.util.Format.dateRenderer('Y-m-d H:i:s'),hidden : true},
        {width:100,sortable:true,header:'更新人',dataIndex:'update_people',hidden : true},
        {width:100,sortable:true,header:'更新时间',dataIndex:'update_time',renderer:Ext.util.Format.dateRenderer('Y-m-d H:i:s'),hidden : true}]),
        sm : new Ext.grid.CheckboxSelectionModel({ listeners:{selectionchange:function(sm){var _grid=this.grid;if(sm.getCount()){_grid.removeBtn.enable();}else{_grid.removeBtn.disable();}}}}),
        listeners : {rowdblclick : function(g,rowIndex,e){var _rec=g.getSelectionModel().getSelected();}}
    }]
}    ],
      id : "FundDetailTab"
    }
  /*
  	 {border : false,
        autoHeight : true,
        height : 200,
        items : [{
          xtype : "tabpanel",
          id : "nodeTabId",
          itemId : "nodeItemId",
       	  region:'center',
          enableTabScroll: true,
          activeTab:0,
          height : 310,
          items:[{
            	id: 'welcome',
                title: '欢迎光临',
                autoScroll:true,
				html: "大神"
            }]
        }]
  }*/
  ],
   buttonAlign : "center",
   buttons : [{
		text : "保存",				
		handler : function() {		 
			 Ext.Msg.confirm('保存数据','确认保存已修改数据?',function(btn){
				   if(btn == 'yes'){
				         var _grid=Ext.getCmp('nodeEditGrid') || JrafUTIL.findCmp(nodePanel,"nodeEditGrid");
				         _grid.getStore().save();
				        // JrafUTIL.findCmp(MainPanel,"nodeEditId").hide();
				         //Ext.getCmp('nodeEditId').hide()
				        Ext.getCmp('nodeEditId').hide();
				   }
			 });
		}
   }]
},'panel');
this.nodePanel=nodePanel;this.__caches__.push(nodePanel);var MainPanel=new Ext.Panel({layout:'fit',border:false,bodyBorder:false,items:[  {
    xtype : "panel",
    layout : "border",
    items : [      {
        xtype : "form",
        region : "north",
        autoHeight : true,
        title : "查询条件",
        frame : true,
        items : [          {
            layout : "column",
            bodyBorder : false,
            items : [              {
                layout : "form",
                bodyBorder : false,
                columnWidth : 0.5,
                items : [                  {
                    name : "query_processDefKey",
                    fieldLabel : "流程定义key",
                    width : 150,
                    allowBlank : true,
                    xtype : "textfield"
                }]
            },              {
                layout : "form",
                bodyBorder : false,
                columnWidth : 0.5,
                items : [                  {
                    name : "query_processDefName",
                    fieldLabel : "流程定义名字",
                    width : 150,
                    allowBlank : true,
                    xtype : "textfield"
                }]
            }],
            anchor : "100%"
        }],
        buttonAlign : "center",
        itemId : "qryProcessDefForm",
        buttons : [        {
          text : "查询",
          handler : function(){var qStore=Ext.getCmp('ProcessDefGrid').getStore();qStore.setFormParam(Ext.getCmp('qryProcessDefForm'));qStore.setPageInfo(JrafSession.get('PageSize'),'1');qStore.load();}
      }]
    }, {
        xtype : "grid",
       // id : "ProcessDefGrid",
        region : "center",
        autoWidth : true,
        frame : true,
        title : "查询结果",
        viewConfig : {
           forceFit : false
        },
        stripeRows : true,
        columnLines : true,
        autoHeight : false,
        height : 320,
        store : ProcessDefStore,
        bbar : new Ext.PagingToolbar({pageSize: JrafSession.get('PageSize'),store:ProcessDefStore,displayInfo: true}),
        tbar : [
        {xtype: 'tbseparator'},
        {text:'部署流程',iconCls:'page-excel',ref: '../impBtn',handler:function(){impData();}}
        ],
        colModel : new Ext.grid.ColumnModel([
          {width:160,sortable:true,header:'操作',dataIndex:'processdefoperation',renderer:operationRender},
          {width:100,sortable:true,header:'流程定义ID',dataIndex:'processdefid',renderer:Ext.util.Format.paramRenderer('undefined','')},
          {width:100,sortable:true,header:'流程定义名称',dataIndex:'processdefname',renderer:Ext.util.Format.paramRenderer('undefined','')},
          {width:100,sortable:true,header:'流程定义Key',dataIndex:'processdefkey',renderer:Ext.util.Format.paramRenderer('undefined','')},
          {width:30,sortable:true,header:'版本',dataIndex:'processdefver',renderer:Ext.util.Format.paramRenderer('undefined','')},
          {width:120,sortable:true,header:'XML',dataIndex:'processdefxml',renderer:xmlRender},
          {width:120,sortable:true,header:'图片',dataIndex:'processdefpicture',renderer:pictureRender},
          {width:130,sortable:true,header:'部署时间',dataIndex:'processdefcreatetime',renderer:Ext.util.Format.dateRenderer('Y-m-d H:i:s')},
          {width:60,sortable:true,header:'是否挂起',dataIndex:'suspension',renderer:suspendRender}
        ])
    }]
}]});
this.MainPanel=MainPanel;var __jrafonload=function(_params_){ProcessDefStore.setFormParam(JrafUTIL.findCmp(MainPanel,'qryProcessDefForm'));
	  ProcessDefStore.setPageInfo(JrafSession.get('PageSize'),'1');
	  ProcessDefStore.load();};
this.__jrafonload=__jrafonload;this.__caches__.push(__jrafonload);};