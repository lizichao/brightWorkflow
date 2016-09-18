Ext.namespace("jsp.platform.sm");jsp.platform.sm.deptlist=function(){var __caches__=[];this.__caches__=__caches__;
function loadRec(n){
	var node = n||MainPanel.get(0).getComponent('dept-tree').getSelectionModel().getSelectedNode();
	var _form=JrafUTIL.findCmp(MainPanel,'dept-form');
	_form.getForm().reset();
	_form.getForm().loadRecord(node.attributes.record);
	_form.record=node.attributes.record;
	_form.treeNode=node;
	loadSysUser(_form.record.data['deptid']);
};
function deleteNode(node){
	var pn=node.previousSibling||node.parentNode;
	node.remove();
	pn.select();
	loadRec(pn);
};
function updateNode(node,rec){
	var att=node.getOwnerTree().getLoader().createAttrByRec(rec);
	node.setText(att.text);
};
function loadSysUser(deptid){
	var usGrid=JrafUTIL.findCmp(MainPanel,'sysUserGrid');
	if(Ext.isEmpty(deptid)){
		usGrid.setDisabled(true);
	}
	else{
		usGrid.setDisabled(false);
		usGrid.getStore().setBaseParam('deptid',deptid);
	    usGrid.getStore().load();
	}
};
function initDeptData(){
	Ext.Msg.confirm('数据初始化','确认初始化当前机构基础数据?',function(btn){if(btn == 'yes'){
		var _form=JrafUTIL.findCmp(MainPanel,'dept-form');
		var _deptid=_form.record.data['deptid'];
		var jr=new JrafRequest('pcmc','dept','init');
		jr.setExtraPs({deptid:_deptid});
		jr.postData();
	}});
};
function resetData(){
	Ext.Msg.confirm('重置密码','确认重置选择用户的密码?',function(btn){if(btn == 'yes'){
		var usGrid=JrafUTIL.findCmp(MainPanel,'sysUserGrid');
		var selUsers = usGrid.getSelectionModel().getSelections();
		var uids=[];
		Ext.each(selUsers,function(item,idx){
			uids[idx]=item.data['userid'];
		});
		var jr=new JrafRequest('pcmc','user','reset');
		jr.setExtraPs({userid:uids});
		jr.postData();
	}});
};
    var deptRecord=Ext.data.Record.create([  {
    xtype : "Field",
    name : "deptid",
    fieldLabel : "deptid",
    type : "int",
    allowBlank : true
},  {
    xtype : "Field",
    name : "pdeptid",
    fieldLabel : "pdeptid",
    type : "int",
    allowBlank : true
},  {
    xtype : "Field",
    name : "deptcode",
    fieldLabel : "机构码",
    type : "string",
    allowBlank : false
},  {
    xtype : "Field",
    name : "deptname",
    fieldLabel : "机构名称",
    type : "string",
    allowBlank : false
},  {
    xtype : "Field",
    name : "depttype",
    fieldLabel : "机构类别",
    type : "string",
    allowBlank : false
},  {
    xtype : "Field",
    name : "linkman",
    fieldLabel : "联系人",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "phone",
    fieldLabel : "联系电话",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "email",
    fieldLabel : "电子邮件",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "addr",
    fieldLabel : "地址",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "zip",
    fieldLabel : "邮编",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "manager",
    fieldLabel : "负责人",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "state",
    fieldLabel : "状态",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "remark",
    fieldLabel : "备注",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "levels",
    fieldLabel : "levels",
    type : "float",
    allowBlank : true
},  {
    xtype : "Field",
    name : "childnum",
    fieldLabel : "childnum",
    type : "string",
    allowBlank : true
}]);
this.deptRecord=deptRecord;this.__caches__.push(deptRecord);var sysUserRecord=Ext.data.Record.create([  {
    xtype : "Field",
    name : "userid",
    fieldLabel : "userid",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "usercode",
    fieldLabel : "用户名",
    type : "string",
    allowBlank : false
},  {
    xtype : "Field",
    name : "username",
    fieldLabel : "名称",
    type : "string",
    allowBlank : false
},  {
    xtype : "Field",
    name : "state",
    fieldLabel : "状态",
    type : "string",
    allowBlank : true
}]);
this.sysUserRecord=sysUserRecord;this.__caches__.push(sysUserRecord);var sysUserStore=Ext.create({
  xxtype : "Store",
  xtype : "Store",
  classname : "sysUserStore",
  recordType : sysUserRecord,
  idProperty : "userid",
  api : { read:{sysName:"pcmc",oprID:"deptManage",actions:"deptmgr"},
    create:{sysName:'pcmc',oprID:'user',actions:'add'},
	update:{sysName:'pcmc',oprID:'user',actions:'upt'},
  	destroy:{sysName:"pcmc",oprID:"user",actions:"del"}},
  autoLoad : false,
  autoSave : false,
  writeAllFields : true,
  paramsAsHash : true,
  remoteSort : false
},'Store');
this.sysUserStore=sysUserStore;this.__caches__.push(sysUserStore);var stateCombo=Ext.create({
  xxtype : "Jpanel",
  xtype : "paracombo",
  classname : "stateCombo",
  baseParams : {paramname : 'userstate'}
},'panel');
this.stateCombo=stateCombo;this.__caches__.push(stateCombo);var MainPanel=new Ext.Panel({layout:'fit',border:false,bodyBorder:false,items:[  {
    xtype : "panel",
    layout : "border",
    items : [      {
        xtype : "panel",
        region : "center",
        layout : "border",
        items : [{
	        xtype : "form",
	        title : "机构信息",
	        itemId : "dept-form",
	        region : "north",
	        titleCollapse: true,
	    	collapsible: true,
	        height : 320,
	        labelAlign : "right",
	        bodyStyle : "padding:5px 15px 0",
	        items : [    {
	            name : "deptid",
	            fieldLabel : "deptid",
	            allowBlank : true,
	            xtype : "hidden"
	        },          {
	            name : "pdeptid",
	            fieldLabel : "pdeptid",
	            allowBlank : true,
	            xtype : "hidden"
	        },          {
                layout : "column",
                bodyBorder : false,
                items : [{
                    layout : "form",
                    columnWidth : 0.5,
                    bodyBorder : false,
                    items : [{
			            name : "deptcode",
			            fieldLabel : "机构码",
			            allowBlank : false,
			            xtype : "textfield"
			        }]
                },{
                    layout : "form",
                    columnWidth : 0.5,
                    bodyBorder : false,
                    items : [{
			            name : "deptname",
			            fieldLabel : "机构名称",
			            allowBlank : false,
			            xtype : "textfield"
			        }]
                }]
            },{
	            xtype : "paracombo",
	            fieldLabel : "机构类别",
	            name : "depttype",
	            hiddenName : "depttype",
	            baseParams : {paramname : 'depttype'},
	            allowBlank : false
	        },          {
                layout : "column",
                bodyBorder : false,
                items : [{
                    layout : "form",
                    columnWidth : 0.5,
                    bodyBorder : false,
                    items : [{
			            name : "linkman",
			            fieldLabel : "联系人",
			            allowBlank : true,
			            xtype : "textfield"
			        }]
                },{
                    layout : "form",
                    columnWidth : 0.5,
                    bodyBorder : false,
                    items : [{
			            name : "phone",
			            fieldLabel : "联系电话",
			            allowBlank : true,
			            xtype : "textfield"
			        }]
                }]
            },          {
                layout : "column",
                bodyBorder : false,
                items : [{
                    layout : "form",
                    columnWidth : 0.5,
                    bodyBorder : false,
                    items : [{
			            name : "manager",
			            fieldLabel : "负责人",
			            allowBlank : true,
			            xtype : "textfield"
			        }]
                },{
                    layout : "form",
                    columnWidth : 0.5,
                    bodyBorder : false,
                    items : [{
			            name : "email",
			            fieldLabel : "电子邮件",
			            allowBlank : true,
			            vtype : 'email',
			            xtype : "textfield"
			        }]
                }]
            },          {
	            name : "addr",
	            fieldLabel : "地址",
	            allowBlank : true,
	            xtype : "textfield",
	            width : 300
	        },          {
	            name : "zip",
	            fieldLabel : "邮编",
	            allowBlank : true,
	            xtype : "textfield",
	            width : 80
	        },          {
	            xtype : "combo",
	            fieldLabel : "状态",
	            name : "state",
	            hiddenName : "state",
	            valueField : "v",
	            displayField : "t",
	            forceSelection : false,
	            triggerAction : "all",
	            editable : false,
	            mode : "local",
	            store : new Ext.data.ArrayStore({id: 0,fields: ['v','t'],data: [['1', '启用'],['0','停用']]}),
	            width : 80
	        },          {
	            xtype : "textarea",
	            fieldLabel : "备注",
	            name : "remark",
	            width : 300,
	            height : 50
	        },          {
	            name : "levels",
	            fieldLabel : "levels",
	            allowBlank : true,
	            xtype : "hidden"
	        }],
	        buttonAlign : "center",
	        buttons : [{
				text : '保存',
				handler : function() {
					var _form=this.ownerCt.ownerCt;
					if(_form.record && _form.treeNode)
					{
						var newFlag=Ext.isEmpty(_form.record.data['deptid']);
						var jr;
						if(newFlag)
						{
							jr=new JrafRequest('pcmc','dept','addDept',{idProperty:'deptid'});
						}
						else
						{
							jr=new JrafRequest('pcmc','dept','updateDept',{idProperty:'deptid'});
						}
						jr.setForm(_form);
	                    jr.setSuccFn(function(a,_resp,xr){
	                    	_form.getForm().updateRecord(_form.record);
							if(newFlag)
	                    	{
	                    		xr.realize(_form.record,a.records);
	                    		loadSysUser(_form.record.data['deptid']);
	                    	}
	                    	else
	                    	{
	                    		xr.update(_form.record,a.records);
	                    	}
	                    	_form.getForm().loadRecord(_form.record);
	                    	updateNode(_form.treeNode,_form.record);
	                    });
						jr.postData();
					}
				}
			}]
	    },      {
	        xtype : "editorgrid",
	        region : "center",
	        itemId : "sysUserGrid",
	        frame : true,
	        title : "机构管理员",
	        viewConfig : {forceFit:true},
	        columnLines : true,
	        autoWidth : true,
	        autoHeight : false,
	        disabled : true,
	        height : 320,
	        store : sysUserStore,
	        tbar : [{text:'新增',iconCls:'add',ref: '../addBtn',handler:function(){var _grid=this.ownerCt.ownerCt;var recordType=_grid.getStore().recordType;var nr=new recordType();_grid.getStore().add(nr);_grid.getSelectionModel().selectLastRow();var rec=_grid.getSelectionModel().getSelected();}},
	{xtype: 'tbseparator'},
	{text:'保存',iconCls:'disk',ref: '../saveBtn',handler:function(){var _grid=this.ownerCt.ownerCt;Ext.Msg.confirm('保存数据','确认保存已修改数据?',function(btn){if(btn == 'yes'){_grid.getStore().save();}});}},
	{xtype: 'tbseparator'},
	{text:'初始化',iconCls:'arrow-rotate-clockwise',ref: '../initBtn',handler:function(){initDeptData();}},
	{xtype: 'tbseparator'},
	{text:'重置密码',iconCls:'key-delete',ref: '../resetBtn',disabled: true,handler:function(){resetData();}}],
	        bbar : new Ext.PagingToolbar({pageSize: JrafSession.get('PageSize'),store:sysUserStore,displayInfo: true}),
	        colModel : new Ext.grid.ColumnModel([new Ext.grid.CheckboxSelectionModel(),
	{width:40,sortable:true,header:'用户名',dataIndex:'usercode',editor: new Ext.form.TextField({allowBlank: false})},
	{width:40,sortable:true,header:'名称',dataIndex:'username',editor: new Ext.form.TextField({allowBlank: false})},
	{width:40,sortable:true,header:'状态',dataIndex:'state',editor: stateCombo,renderer:Ext.util.Format.paramRenderer('userstate')}]),
			sm : new Ext.grid.CheckboxSelectionModel({listeners:{selectionchange:function(sm){var _grid=this.grid;if(sm.getCount()){_grid.resetBtn.enable();}else{_grid.resetBtn.disable();}}}}),
	        anchor : "100% 50%"
	    }]
    },      {
        xtype : "treepanel",
        region : "west",
        title : "机构列表",
        itemId : "dept-tree",
        height : 400,
        animate : true,
        autoScroll : true,
        containerScroll : true,
        split : true,
        collapsible : true,
        titleCollapse : false,
        rootVisible : true,
        root : new Ext.tree.AsyncTreeNode({id:"-1"}),
        loader : new JrafXmlTreeLoader({
            nparams: {nid:'deptid',pid:'pdeptid',ntext:'{deptcode} {deptname}',isLeaf:'childnum',expanded:true},
            action: {sysName:'pcmc',oprID:'dept',actions:'getCurrentDeptList'},
            baseParams:{PageSize:'-1'}
        }),
        width : 320,
        listeners : {
  render : function(p) {
	        	var jr = new JrafRequest('pcmc','dept','getDeptDetail',{idProperty:'deptid'});
	        	jr.setSuccFn(function(a,_resp){
	        		var rec = a.records[0];
	        		var r = new Ext.tree.AsyncTreeNode({
						text : rec.data['deptcode']+' '+rec.data['deptname'],
						draggable : false,
						id : rec.id,
						record: rec
					});
					p.setRootNode(r);
	        	});
	        	jr.setExtraPs({deptid:JrafSession.get('deptid')});
                jr.postData();
	        },
  contextmenu : function(node,e){
	  e.preventDefault();
	  node.select();
	  node.expand();
	  var noderec=node.attributes.record;
	  var tmenu=node.getOwnerTree().menu;
	  if(!tmenu){
	    tmenu =  new Ext.menu.Menu({
		items : [{
			id : 'addNode',
			text : '新增',
			iconCls:'add'
		},{
			id : 'delNode',
			text : '删除',
			iconCls:'delete'
		}],
		listeners: {
			itemclick: function(item) {
				var node = item.parentMenu.treeNode;
				var noderec = node.attributes.record
				switch (item.id) {
				case 'addNode':
					if (node.isLeaf()){
						node.leaf = false;
					}
					var lv = noderec.data['levels'];
					lv = Ext.isEmpty(lv)?'0':lv;
					var nlevels = new Number(lv)+1;
					var newRec = new deptRecord({deptid:'',pdeptid:node.id,deptname:'新机构',levels:nlevels,childnum:0});
					var newNode = node.getOwnerTree().getLoader().createNodeByRec(newRec);
					node.appendChild(newNode);
					newNode.select();
					loadRec(newNode);
					break;
				case 'delNode':
					if(''==noderec.get('deptid'))
					{
						deleteNode(node);
						break;
					}
					Ext.Msg.show({
						title : '提示',
						msg :"确认删除?",
						icon : Ext.Msg.WARNING,
						buttons : Ext.Msg.YESNO,
						fn:function(buttonId){
							if('yes'==buttonId){
								var jr=new JrafRequest('pcmc','dept','deleteDept',{recordType:deptRecord,idProperty:'deptid'});
								jr.setExtraPs({deptid:noderec.data['deptid']});
								jr.setSuccFn(function(a,_resp,xr){
									deleteNode(node);
			                    });
								jr.postData();
							}
						}
					});
					break;
				}
			}
		}
	    });
	    node.getOwnerTree().menu=tmenu;
	  }
	  tmenu.showAt(e.getXY());
	  tmenu.treeNode=node;
	  loadRec(node);
  	
  },
  click : function(node,e){
  	loadRec(node);
  }
}
    }]
}]});
this.MainPanel=MainPanel;var __jrafonload=function(_params_){};
this.__jrafonload=__jrafonload;this.__caches__.push(__jrafonload);};