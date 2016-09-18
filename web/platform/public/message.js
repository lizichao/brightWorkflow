
var  MyMessageRecord=Ext.data.Record.create([
	{
		xtype : "Field",
		name : "receiver_id",
		fieldLabel : "���ձ��",
		type : "string",
		allowBlank : false
	},  {
		xtype : "Field",
		name : "messages_id",
		fieldLabel : "��Ϣ���",
		type : "string",
		allowBlank : false
	},  {
		xtype : "Field",
		name : "receiver",
		fieldLabel : "�����߱��",
		type : "float",
		allowBlank : true
	},  {
		xtype : "Field",
		name : "receive_names",
		fieldLabel : "������",
		type : "string",
		allowBlank : true
	},  {
		xtype : "Field",
		name : "read_flag",
		fieldLabel : "�Ķ���� N:δ�� Y :�Ѷ�",
		type : "string",
		allowBlank : true
	},  {
		xtype : "Field",
		name : "read_time",
		fieldLabel : "�Ķ�ʱ��",
		type : "date",
		allowBlank : true,
		dateFormat : "Y-m-d H:i:s"
	},  {
		xtype : "Field",
		name : "del_flag",
		fieldLabel : "ɾ����� N:��Ч Y :ɾ��",
		type : "string",
		allowBlank : true
	},  {
		xtype : "Field",
		name : "sender",
		fieldLabel : "������ID",
		type : "string",
		allowBlank : true
	},  {
		xtype : "Field",
		name : "sender_name",
		fieldLabel : "������",
		type : "string",
		allowBlank : true
	},  {
		xtype : "Field",
		name : "title",
		fieldLabel : "����",
		type : "string",
		allowBlank : true
	},  {
		xtype : "Field",
		name : "message",
		fieldLabel : "����",
		type : "string",
		allowBlank : true
	},  {
		xtype : "Field",
		name : "send_time",
		fieldLabel : "����ʱ��",
		type : "date",
		allowBlank : true,
		dateFormat : "Y-m-d H:i:s"
	},  {
		xtype : "Field",
		name : "received_time",
		fieldLabel : "����ʱ��",
		type : "date",
		allowBlank : true,
		dateFormat : "Y-m-d H:i:s"
	},  {
		xtype : "Field",
		name : "read_time",
		fieldLabel : "�Ķ�ʱ��",
		type : "date",
		allowBlank : true,
		dateFormat : "Y-m-d H:i:s"
	},  {
		xtype : "Field",
		name : "notify_time",
		fieldLabel : "֪ͨʱ��",
		type : "date",
		allowBlank : true,
		dateFormat : "Y-m-d H:i:s"
	}
]);

var MyMessageStore = new JrafXmlStore({
  xxtype : "Store",
  classname : "MyMessageStore",
  recordType : MyMessageRecord,
  idProperty : "receiver_id",
  api : {read:{sysName:"pcmc",oprID:"message",actions:"getReceivesList"}},
  baseParams : {read_type: 'N'},
  autoLoad : true,
  autoSave : false,
  writeAllFields : true,
  paramsAsHash : true,
  remoteSort : false
});

Ext.namespace("Ext.ux");

Ext.ux.ToastWindowMgr = {
    positions: []
};

Ext.ux.ToastWindow = Ext.extend(Ext.Window, {
    initComponent: function(){
          Ext.apply(this, {
            conCls: this.iconCls || 'information',
            width: 250,
            height: 150,
            autoScroll: true,
            autoDestroy: true,
            plain: false,
            shadow:false
          });
        this.task = new Ext.util.DelayedTask(this.hide, this);
        Ext.ux.ToastWindow.superclass.initComponent.call(this);
    },
    setMessage: function(msg){
        this.body.update(msg);
    },
    setTitle: function(title, iconCls){
        Ext.ux.ToastWindow.superclass.setTitle.call(this, title, iconCls||this.iconCls);
    },
    onRender:function(ct, position) {
        Ext.ux.ToastWindow.superclass.onRender.call(this, ct, position);
    },
    onDestroy: function(){
        Ext.ux.ToastWindowMgr.positions.remove(this.pos);
        Ext.ux.ToastWindow.superclass.onDestroy.call(this);
    },
    afterShow: function(){
        Ext.ux.ToastWindow.superclass.afterShow.call(this);
        this.on('move', function(){
               Ext.ux.ToastWindowMgr.positions.remove(this.pos);
            this.task.cancel();}
        , this);
        this.task.delay(4000000);
    },
    animShow: function(){
        this.pos = 0;
        while(Ext.ux.ToastWindowMgr.positions.indexOf(this.pos)>-1)
            this.pos++;
        Ext.ux.ToastWindowMgr.positions.push(this.pos);
        this.setSize(250,150);
        this.el.alignTo(document, "br-br", [ -20, -20-((this.getSize().height+10)*this.pos) ]);
        this.el.slideIn('b', {
            duration: 2,
            callback: this.afterShow,
            scope: this
        });   
    },
    animHide: function(){
           Ext.ux.ToastWindowMgr.positions.remove(this.pos);
        this.el.ghost("b", {
            duration: 2,
            remove: true,
         scope: this,
         callback: this.destroy
        });   
    }
}); 

function showMyMessage(){
   MyMessageStore.load({
       callback : function() {
		   var msgnum = MyMessageStore.getCount();
		   var ctitle = "��Ϣ��ʾ("+msgnum+")";
		   var ccontent = "";
		   var img = "<img src='/platform/icons/fam/clock.png'; width=16; height=16> ";
		   for(var i=0; i<msgnum; i++){
			   var rec = MyMessageStore.getAt(i);
			   ccontent = ccontent + " " + img + " " +rec.get('title')+"("+rec.get('sender_name')+")<br/>";
		   } 
		   if(msgnum > 0){
			   msgWin =Ext.getCmp('msgWin');
			   if(msgWin){					
				   msgWin.destroy();
			   }
			   msgWin = new Ext.ux.ToastWindow({
					 id : 'msgWin',
					 title: ctitle,
					 html: ccontent,
					 iconCls: 'error',					   
					 buttons: [
						   { 
							 text: "�´�����", 
							 handler: function(){
									this.ownerCt.ownerCt.animHide();
								} 
						   },{ 
							 text: "�鿴ȫ��", 
							 handler: function(){									 
								 showMyMessagePanel();
								 this.ownerCt.ownerCt.animHide();								     
							   } 
						   }						     
					 ]
			   }).show(document);				   
		   }
		   window.setTimeout("showMyMessage()",300000);//��ѯ���5����	       
	   }
   })
}

function showMyMessagePanel(){
	//var _href = '/platform/sm/message.aspx';
	//Ext.getCmp('basePanel').loadMenu(_href,'�ҵ���Ϣ','212');
	JrafUTIL.addTab(JrafUTIL.getCmp("MainPanel"),Jraf_ContextPath+'/platform/sm/message.jsp','�ҵ���Ϣ','n_msg_999','');
}