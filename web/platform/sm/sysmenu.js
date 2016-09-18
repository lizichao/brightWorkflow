Ext.namespace("jsp.platform.sm");jsp.platform.sm.sysmenu=function(){var __caches__=[];this.__caches__=__caches__;function deleteNode(node){
	var pn=node.previousSibling||node.parentNode;
	node.remove();
	pn.select();
	loadRec(pn);
};
function loadRec(node){
	var menu_form=JrafUTIL.findCmp(MainPanel,'menuForm');

	var rec=node.attributes.record;
	if(rec){
		if(node!=menu_form.node){
			menu_form.node=node;
			menu_form.getForm().reset();
			menu_form.getForm().loadRecord(rec);
			menu_form.enable();
		}
	}
	else if(null!=menu_form.node){
		menu_form.getForm().reset();
		menu_form.node=null;
		menu_form.disable();
	}
};var menuRecord=Ext.data.Record.create([  {
    xtype : "Field",
    name : "subsysid",
    fieldLabel : "��ϵͳ��ˮ��",
    type : "int"
},  {
    xtype : "Field",
    name : "pmenuid",
    fieldLabel : "���˵���ˮ��",
    type : "int",
    allowBlank : true
},  {
    xtype : "Field",
    name : "levels",
    fieldLabel : "��κ�",
    type : "int",
    allowBlank : false
},  {
    xtype : "Field",
    name : "menuname",
    fieldLabel : "�˵�����",
    type : "string",
    allowBlank : false
},  {
    xtype : "Field",
    name : "imgurl",
    fieldLabel : "ͼƬ��ַ",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "linkurl",
    fieldLabel : "�����ӵ�ַ",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "isinternet",
    fieldLabel : "�Ƿ�������",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "remark",
    fieldLabel : "��ע",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "orderidx",
    fieldLabel : "������",
    type : "int",
    allowBlank : true
},  {
    xtype : "Field",
    name : "newwin",
    fieldLabel : "�´���",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "menuid",
    fieldLabel : "menuid",
    type : "int",
    allowBlank : true
}]);
this.menuRecord=menuRecord;this.__caches__.push(menuRecord);var iconRecord=Ext.data.Record.create([  {
    xtype : "Field",
    name : "icon",
    fieldLabel : "icon",
    type : "string"
},  {
    xtype : "Field",
    name : "icondesc",
    fieldLabel : "icondesc",
    type : "string",
    allowBlank : true
}]);
this.iconRecord=iconRecord;this.__caches__.push(iconRecord);var iconStore=Ext.create({
  xtype : "Store",
  classname : "iconStore",
  type : "JrafXmlStore",
  recordType : iconRecord,
  idProperty : "icon",
  api : {read:{sysName:"pcmc",oprID:"menu",actions:"getMenuIcons"}},
  autoLoad : true,
  autoSave : false,
  writeAllFields : true,
  paramsAsHash : true,
  remoteSort : false
},'Store');
this.iconStore=iconStore;this.__caches__.push(iconStore);var MainPanel=new Ext.Panel({layout:'fit',border:false,bodyBorder:false,items:[  {
    layout : "border",
    items : [               {
            xtype : "form",
            itemId : "menuForm",
			region : "center",
            disabled : true,
			title : "�˵�����",
            labelAlign : "right",
            items : [              {
                name : "subsysid",
                fieldLabel : "��ϵͳ��ˮ��",
                xtype : "hidden"
            },              {
                name : "menuid",
                fieldLabel : "menuid",
                allowBlank : true,
                xtype : "hidden"
            },              {
                name : "pmenuid",
                fieldLabel : "���˵���ˮ��",
                allowBlank : true,
                xtype : "hidden"
            },              {
                name : "menuname",
                fieldLabel : "�˵�����",
                allowBlank : false,
                xtype : "textfield"
            },              {
                name : "imgurl",
                fieldLabel : "�˵�ͼ��",
                allowBlank : true,
                xtype : "iconcombo",
                valueField : "icon",
				displayField : "icondesc",
				iconField : "icon",
				store : iconStore,
				mode : "local",
				editable:true,
				forceSelection:false,
				triggerAction : "all",
				customProperties : true,
                hiddenName:'imgurl',
                width : 300
            },              {
                name : "linkurl",
                fieldLabel : "�����ӵ�ַ",
                allowBlank : true,
                xtype : "textfield",
                width : 300
            },              {
                name : "isinternet",
                fieldLabel : "�Ƿ�������",
                allowBlank : true,
                xtype : "hidden"
            },              {
                name : "levels",
                fieldLabel : "��κ�",
                allowBlank : false,
                xtype : "numberfield",
                allowDecimals : false,
                width : 40
            },              {
                name : "orderidx",
                fieldLabel : "���",
                allowBlank : true,
                xtype : "numberfield",
                allowDecimals : false,
                width : 40
            },              {
                name : "newwin",
                fieldLabel : "��������",
                allowBlank : true,
                xtype : 'combo',
                width : 100,
                hiddenName:'newwin',
                valueField : "v",
				displayField : "t",
				store : new Ext.data.ArrayStore({id: 0,fields: ['v','t'], data: [['1', 'Window'],['0','Iframe'],['2','Script']]}),
				mode : "local",
				editable:true,
				forceSelection:true,
				triggerAction : "all"
            },              {
                name : "remark",
                fieldLabel : "��ע",
                allowBlank : true,
                xtype : "textarea",
                width : 300
            }],
            buttonAlign:'center',
			frame:true,
			buttons : [{
				text : '����',
				handler : function() {
					var _form=this.ownerCt.ownerCt;
					if(_form.node)
					{
						var rec=_form.node.attributes.record;
						var newFlag=Ext.isEmpty(rec.data['menuid']);
						if(newFlag)
						{
							jr=new JrafRequest('pcmc','menu','addMenu',{idProperty:'menuid'});
						}
						else
						{
							jr=new JrafRequest('pcmc','menu','updateMenu',{idProperty:'menuid'});
						}
						jr.setForm(_form);
	                    jr.setSuccFn(function(a,_resp,xr){
	                    	_form.getForm().updateRecord(rec);
							if(newFlag)
	                    	{
	                    		xr.realize(rec,a.records);
	                    	}
	                    	else
	                    	{
	                    		xr.update(rec,a.records);
	                    	}
	                    	_form.getForm().loadRecord(rec);
	                    	var att=_form.node.getOwnerTree().getLoader().createAttrByRec(rec);
	                    	_form.node.setText(att.text);
	                    	_form.node.id=att.id;
	                    });
						jr.postData();
					}
				}
			}]
    },      {
        region : "west",
        title : "�˵��б�",
        width : 280,
        split : true,
        collapsible : true,
        titleCollapse : false,
		frame:true,
        items : [          {
            xtype : "treepanel",
            height : 400,
            itemId : "menutree",
            animate : true,
            autoScroll : true,
            containerScroll : true,
            rootVisible : true,
            root : new Ext.tree.AsyncTreeNode({id:'0',text:'ϵͳ�˵�',draggable : false}),
            loader : new JrafXmlTreeLoader({nparams: {nid:'menuid',pid:'pmenuid',ntext:'{menuname}',loadAll:false,expanded:true,sm:'all',isLeaf:'isleaf'},action: {sysName:'pcmc',oprID:'menu',actions:'getMenuListByPid'},baseParams: {PageSize:'-1'}}),
            tbar : [{xtype:'syscombo',itemId:'syscombo',width:160},
        	{
        	tooltip:'ˢ��',
        	iconCls:'arrow-refresh',
        	handler:function(){
        	    var sv=this.ownerCt.getComponent('syscombo').getValue();
        	    if(''!=sv)
        	    {
	        		var mt=this.ownerCt.ownerCt;
	        		mt.getLoader().baseParams['subsysid']=sv;
	        		mt.getLoader().load(mt.getRootNode());
	        	}
        	}}],
            listeners : {
  contextmenu : function(node,e){
  e.preventDefault();
  node.select();
  node.expand();
  var tmenu=node.getOwnerTree().menu;
  if(!tmenu){
    tmenu =  new Ext.menu.Menu({
	items : [{
		id : 'addMenu',
		text : '�����˵�',
		iconCls:'add'
	},{
		id : 'delMenu',
		text : 'ɾ���˵�',
		iconCls:'delete'
	}],
	listeners: {
		itemclick: function(item) {
			var node = item.parentMenu.treeNode;
			var noderec = node.attributes.record
			switch (item.id) {
			case 'addMenu':
				var newRec = new menuRecord({menuid:'',pmenuid:node.id,menuname:'new Menu',isleaf:'true',subsysid:node.getOwnerTree().getLoader().baseParams['subsysid']});
				var newNode = node.getOwnerTree().getLoader().createNodeByRec(newRec);
				node.appendChild(newNode);
				newNode.select();
				loadRec(newNode);
				break;
			case 'delMenu':
				if(''==noderec.get('menuid'))
				{
					deleteNode(node);
					break;
				}
				Ext.Msg.show({
					title : '��ʾ',
					msg :"ȷ��ɾ��?",
					icon : Ext.Msg.WARNING,
					buttons : Ext.Msg.YESNO,
					fn:function(buttonId){
						if('yes'==buttonId){
							var jr=new JrafRequest('pcmc','menu','deleteMenu');
							jr.setExtraPs({menuid:node.id});
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
  tmenu.get('delMenu').setDisabled(node.parentNode==null);
  tmenu.showAt(e.getXY());
  tmenu.treeNode=node;
  loadRec(node);
},
  click : function(node,e){loadRec(node);}
}
        }],
        layout : "fit"
    }]
}]});
this.MainPanel=MainPanel;var __jrafonload=function(_params_){};
this.__jrafonload=__jrafonload;this.__caches__.push(__jrafonload);};