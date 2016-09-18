Ext.namespace("jsp.masterreview.news");jsp.masterreview.news.newsmanage=function(){var __caches__=[];this.__caches__=__caches__;
       var _editInfoWin; 
       function openEditInfoWin(_rec){ 
         _editInfoWin=Ext.getCmp('editInfoWin'); 
         if(!_editInfoWin){
          _editInfoWin=new Ext.Window(
            {title:'数据录入',
             id:'editInfoWin',
             layout:'fit',
             width:600,
             height:300,
             closeAction:'hide',
             plain:true,
             modal: true,
             items:EditInfoForm
             });
          } 
          _editInfoWin.show();
          EditInfoForm.getForm().reset();
          EditInfoForm.getForm().loadRecord(_rec);
          EditInfoForm.record=_rec;
        }
    var NewsManageRec=Ext.data.Record.create([  {
    xtype : "Field",
    name : "id",
    width : 150,
    fieldLabel : "主键id",
    allowBlank : false,
    type : "string"
},  {
    xtype : "Field",
    name : "news_title",
    width : 150,
    fieldLabel : "新闻标题",
    allowBlank : false,
    type : "string"
},  {
    xtype : "Field",
    name : "news_content",
    width : 150,
    fieldLabel : "新闻内容",
    allowBlank : false,
    type : "string"
}]);
this.NewsManageRec=NewsManageRec;this.__caches__.push(NewsManageRec);var NewsManageStore=Ext.create({
  xtype : "Store",
  classname : "NewsManageStore",
  type : "JrafXmlStore",
  recordType : NewsManageRec,
  idProperty : "id",
  api : {
    read :     {
      sysName : "headmaster",
      oprID : "newsAction",
      actions : "findNews"
  },
    create :     {
      sysName : "headmaster",
      oprID : "newsAction",
      actions : "addNews"
  },
    update :     {
      sysName : "headmaster",
      oprID : "newsAction",
      actions : "updateNews"
  },
    destroy :     {
      sysName : "headmaster",
      oprID : "newsAction",
      actions : "deleteNews"
  }
},
  autoLoad : true,
  autoSave : false,
  paramsAsHash : true,
  remoteSort : true,
  listeners : {
    write : function (store,action,result,res,rs){if (res.success){if (_editInfoWin){_editInfoWin.hide();}}}
}
},'Store');
this.NewsManageStore=NewsManageStore;this.__caches__.push(NewsManageStore);var EditInfoForm=Ext.create({
  xtype : "form",
  classname : "EditInfoForm",
  frame : true,
  bodyBorder : false,
  items : [    {
      layout : "column",
      bodyBorder : false,
      items : [        {
          layout : "form",
          bodyBorder : false,
          columnWidth : 0.5,
          labelAlign : "right",
          items : [            {
              name : "id",
              fieldLabel : "主键id",
              width : 150,
              allowBlank : false,
              xtype : "hidden"
          }]
      },        {
          layout : "form",
          bodyBorder : false,
          columnWidth : 1,
          labelAlign : "right",
          items : [            {
              name : "news_title",
              fieldLabel : "新闻标题",
              width : 400,
              allowBlank : false,
              xtype : "textfield"
          }]
      },        {
          layout : "form",
          bodyBorder : false,
          columnWidth : 1,
          labelAlign : "right",
          items : [            {
              name : "news_content",
              fieldLabel : "新闻内容",
              allowBlank : false,
              width : 650,
              height : 260,
              xtype : "htmleditor"
          }]
      }]
  }],
  id : "EditInfoForm",
  buttonAlign : "center",
  buttons : [    {
      text : "保存",
      width : 100,
      height : 25,
      iconCls : "disk",
      handler : function() {
                var _form=this.ownerCt.ownerCt;
	            if(!_form.getForm().isValid()) return;
				var rec=_form.record;
				var newFlag = Ext.isEmpty(rec.data['id']);
				var jr;
			    if(newFlag){
		            jr=new JrafRequest('headmaster','newsAction','addNews',{recordType:NewsManageRec,idProperty:'id'});
		        }else{
		            jr=new JrafRequest('headmaster','newsAction','updateNews',{recordType:NewsManageRec,idProperty:'id'});
		        }
				jr.setForm(_form);
		        jr.setSuccFn(function(a,_resp,xr){
		            _form.getForm().updateRecord(_form.record);
		            if(newFlag){
		               xr.realize(rec,a.records);
		            }
				    _form.ownerCt.hide();
				    NewsManageStore.load();
		        });
		        jr.postData();
      }
  },    {
      text : "取消",
      width : 100,
      height : 25,
      iconCls : "arrow-undo",
      handler : function() {_editInfoWin.hide();}
  }]
},'panel');
this.EditInfoForm=EditInfoForm;this.__caches__.push(EditInfoForm);var MainPanel=new Ext.Panel({layout:'fit',border:false,bodyBorder:false,items:[  {
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
                columnWidth : 0.33,
                items : [                  {
                    name : "news_title",
                    fieldLabel : "新闻标题",
                    width : 400,
                    allowBlank : true,
                    xtype : "textfield"
                }]
            },              {
                layout : "form",
                bodyBorder : false,
                columnWidth : 0.33,
                items : [                  {
                    name : "news_content",
                    fieldLabel : "新闻内容",
                    width : 150,
                    allowBlank : true,
                    xtype : "textfield"
                }]
            }],
            anchor : "100%"
        }],
        buttonAlign : "center",
        id : "NewsManageQryForm",
        buttons : [        {
          text : "查询",
          handler : function(){
            var qStore=Ext.getCmp('NewsManageGrid').getStore();
            qStore.setFormParam(Ext.getCmp('NewsManageQryForm'));
            qStore.setPageInfo(JrafSession.get('PageSize'),'1');
            qStore.load();
         }
      }]
    },      {
        xtype : "grid",
        id : "NewsManageGrid",
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
        store : NewsManageStore,
        bbar : new Ext.PagingToolbar({pageSize: JrafSession.get('PageSize'),store:NewsManageStore,displayInfo: true}),
        tbar : [{text:'新增',iconCls:'add',ref: '../addBtn',handler:function(){
	          var _grid=this.ownerCt.ownerCt;
	          var recordType=_grid.getStore().recordType;
	          var nr=new recordType();
	          _grid.getStore().add(nr);
	          _grid.getSelectionModel().selectLastRow();
	          openEditInfoWin(nr);
          }
        },{xtype: 'tbseparator'},
        {text:'删除',iconCls:'delete',ref: '../removeBtn',disabled: true,handler:function(){
          	        var _grid=this.ownerCt.ownerCt;
		        	var ckrs=_grid.getSelectionModel().getSelections();
		        	var param={};
					for(var i=0;i<ckrs.length;i++){
					    var id = ckrs[i].get('userid');
					 	param['id']=!param['id'] ? id:[].concat(param['id']).concat(id);
					 	_grid.getStore().remove(ckrs[i]);
					}
					var jr=new JrafRequest('headmaster','newsAction','deleteNews');
			 		jr.setExtraPs(param);
					jr.setSuccFn(function(data,status){
				    
				    });
					jr.postData();
          }
        },{xtype: 'tbseparator'},
        {text:'保存',iconCls:'disk',ref: '../saveBtn',handler:function(){
	         var _grid=this.ownerCt.ownerCt;
	         Ext.Msg.confirm('保存数据','确认保存已修改数据?',
		           function(btn){ 
		             if(btn == 'yes'){
		                _grid.getStore().save();
		             }
		            }
	          );
          }
        }],
        colModel : new Ext.grid.ColumnModel([new Ext.grid.CheckboxSelectionModel(),
        {width:200,sortable:true,header:'新闻标题22',dataIndex:'news_title',renderer:Ext.util.Format.paramRenderer('undefined','')},
        {width:700,sortable:true,header:'新闻内容',dataIndex:'news_content',renderer:Ext.util.Format.paramRenderer('undefined','')}
        ]),
        sm : new Ext.grid.CheckboxSelectionModel({ 
          listeners:{
	        selectionchange:function(sm){
	           var _grid=this.grid;if(sm.getCount()){_grid.removeBtn.enable();}else{_grid.removeBtn.disable();}
	        }
          }
        }),
        listeners : {rowdblclick : function(g,rowIndex,e){var _rec=g.getSelectionModel().getSelected();openEditInfoWin(_rec);}}
    }]
}]});
this.MainPanel=MainPanel;var __jrafonload=function(_params_){};
this.__jrafonload=__jrafonload;this.__caches__.push(__jrafonload);};