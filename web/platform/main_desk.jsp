<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="cn.brightcom.jraf.util.*"%>
<%@ page import="cn.brightcom.jraf.web.*"%>
<%@ page import="cn.brightcom.system.pcmc.util.MenuUtil"%>
<%@ page import="cn.brightcom.system.pcmc.pm.PmInformations" %>
<%@ page import="org.jdom.*"%>
<%@ page import="java.util.List"%>
<%@ page import="java.net.URLEncoder"%>
<%@ taglib uri="/WEB-INF/brightcom.tld" prefix="sl" %>
<%
	request.setCharacterEncoding("GBK");
	response.setHeader("Cache-Control", "no-cache"); //HTTP 1.1
	response.setHeader("Pragma", "no-cache"); //HTTP 1.0
	response.setDateHeader("Expires", 0); //prevents caching at the proxy server
	String currUserid = (String)session.getAttribute("userid") == null ? "-1":(String)session.getAttribute("userid");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="keywords" content="" />
<meta name="description" content="" />
<title>����ʦ����ѧ�������ͽ�ԴѧУ</title>
<link rel="stylesheet" type="text/css" href="/images/yunan/yunan.css" />
<%@ include file="/platform/header.jsp"%>
<%@ include file="/platform/public/includejs.jsp"%>

<link href="/js/jquery/plugin/ui/jquery-ui.min.css" rel="stylesheet" type="text/css" />
<link href="/css/jquery.gridster.min.css" rel="stylesheet" type="text/css" />
<script src="/js/jquery/jquery-ui-1.8.16.custom.min.js"></script>
<script src="/js/jquery/jquery.contextmenu.r2.js"></script>
<style type="text/css">           
.SelectedRow           
{               
background: yellow;           
}           
.contextMenu           
{               
display: none;           
}       
</style>   
<script id="DeskGroup" type="text/x-jsrender">
{{for Data}}
<style>
  #groupSorTable{{:#index+1}}{
    border: 0px solid #eee;
    width: 100%;
    min-height: 150px;
    list-style-type: none;
    margin: 0;
    padding: 5px 0 0 0;
    margin-right: 0px;
  }
  #groupSorTable{{:#index+1}} li{
  	margin: 0 0px 0px 0px;
    padding: 0px;
    float: left;
    font-size: 1.2em
   
   }
</style>
 <div class="item">
	<div class="itemTitle" style="cursor: pointer;"><span class="bg_09" onmousedown="javascript:if(event.button == 2) fn_deskBinding('{{:desktop_id}}');" id="deskEvent{{:desktop_id}}"�� onclick="javascript:openMenuWin('{{:desktop_id}}')">{{:menu_name}}</span></div>
	<div class="itemContent">
		<ul id="groupSorTable{{:#index+1}}" desktop_id="{{:desktop_id}}" p_desktop_id="{{:p_desktop_id}}" class="itemList connectedSortable" >
			{{for MenuDesk}}
				<li><a href="javascript:window.parent.openContentMain('{{:menu_id}}','{{:menu_name}}','{{:linkurl}}','{{:newwin}}');">
					<img src="/images/yunan/1.png" alt="" onmousedown="javascript:if(event.button == 2) fn_binding('{{:menu_id}}','{{:desktop_id}}');"  title="" id="rightEvent{{:menu_id}}" desktop_id="{{:desktop_id}}"/></a>
					<p>{{:menu_name}}</p>
				</li>
			{{/for}}
		</ul>
	</div>
</div>
{{/for}}

<div style="margin-top:10px;margin-left:10px;height:200px;width:430px;">
		<div style="text-align: center;position:relative;top:50%;cursor: pointer;">
			<img onclick="javascript:fnDialogWin();"  src="/images/yunan/add_desk.png" alt=""   title="" />
		</div>
</div>
</script>


<script>
var param = {paramVal:""};
$(document).ready(function(){	
	loadData();
});
function fnDialogWin(){
	$("#dialog-form").dialog( "open" );
}
//���������˵��Ҽ�
function fn_deskBinding(val){
param.paramVal = val;
   eventText = "#deskEvent"+val;
	$(eventText).contextMenu('myMenu1', {
      	bindings: {
        'edit': function(t) {
          openDeskMenuWin(val);
      	},
      	'del': function(t) {
      	Ext.Msg.confirm('ϵͳ��ʾ','�Ƿ�ȷ��ɾ��ѡ�в˵������?',function(btn){
		if(btn == 'yes'){
	          var bc = new BcRequest('pcmc','desk','delDeskInfo');
				bc.setExtraPs({"desktop_id":val});
				bc.setSuccFn(function(data,status){
					toastr.success("ɾ������ɹ���");
					loadData();
			    });
			    bc.postData();
			  }
		})
      	}
      }
     });
}

//�󶨲˵��Ҽ�
function fn_binding(val,desk_id){
param.paramVal = desk_id;
   eventText = "#rightEvent"+val;
	$(eventText).contextMenu('myMenu1', {
      	bindings: {
        'edit': function(t) {
          openDeskMenuWin(desk_id)
      	},
      	'del': function(t) {
      	 	Ext.Msg.confirm('ϵͳ��ʾ','�Ƿ�ȷ��ɾ��ѡ�в˵������?',function(btn){
		    if(btn == 'yes'){
	          var bc = new BcRequest('pcmc','desk','delDeskInfo');
				bc.setExtraPs({"desktop_id":desk_id});
				bc.setSuccFn(function(data,status){
					toastr.success("ɾ������ɹ���");
					loadData();
			    });
			    bc.postData();
			 }
		  })
      	}
      }
    });
}
function loadData(){
	 $.post("/P.tojson?sysName=pcmc&oprID=desk&actions=getGroupDeskList",
	  function(data,status){
	  	 var titleTxt = "";
	      $.each(data.Data,function(i){
	      	titleTxt+="#groupSorTable"+(i+1)+",";
	      });
	      titleTxt = titleTxt.substring(0,titleTxt.length-1);
	      var _rowContent = $("#DeskGroup").render(data);
		  $("#main_desk").empty();
		  $("#main_desk").append(_rowContent);

		  var _width=$(document).width()-50;
		  $(".item").css("width",_width/2);	
		  
		  $(titleTxt).sortable({
		 	  grid: [57,57] ,
		      connectWith: ".connectedSortable",
		      revert: "invalid",cursor: "move",
		      receive: function( event, ui ) {
					var bc ;
					//Ŀ����
					var desktop_id = $(this).attr("desktop_id");
					//��ǰѡ��ͼ��
					var curr_deskIds = ui.item.find("img").attr("desktop_id");
					bc = new BcRequest('pcmc','desk','updateDesk');
					bc.setExtraPs({"desktop_id":curr_deskIds,"p_desktop_id":desktop_id});
					bc.setSuccFn(function(data,status){
						toastr.success("�������óɹ���");
				    });
				    bc.postData();
			
			 }
		    }).disableSelection();
	  });	
};

var menuRecord=Ext.data.Record.create([  {
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
var iconRecord = Ext.data.Record.create([{
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

   var mtree = new Ext.tree.TreePanel({
	id:'menu-tree',
	region:'menuTree',
	split:true,
	width: 200,
	minSize: 175,
	maxSize: 500,	        
	margins:'0 0 0 5',
	cmargins:'0 0 0 0',
	frame:true,
	lines:false,
	animate : true,
	autoScroll : true,
	containerScroll : true,
	titleCollapse : false,
    rootVisible:false,
    listeners:{
	    "checkchange": function(node, state) {
	     	if(!node.leaf){
	     		node.ui.checkbox.checked = false;
	     	}
	    }
	},
    root : new Ext.tree.AsyncTreeNode({id:'0',text:'ϵͳ�˵�',draggable : false}),
    loader: new JrafXmlTreeLoader({
         nparams: {nid:'menuid',pid:'pmenuid',ntext:'{menuname}',loadAll:true,expanded:true,chk:true,chkfld:'selected',chkval:'checked'},
        action: {sysName:'pcmc',oprID:'desk',actions:'getSubSysMenu'},
        baseParams: {subsysid:'-1'},recordType:menuRecord,idProperty:"menuid"
    }),
    tbar : [{xtype:'syscombo',itemId:'syscombo',width:160},
      	{tooltip:'ˢ��',
      	iconCls:'arrow-refresh',
      	handler:function(){
      	    var sv=this.ownerCt.getComponent('syscombo').getValue();
      	    if(''!=sv){
	       		var mt=this.ownerCt.ownerCt;
	       		mt.getLoader().baseParams['subsysid']=sv;
	       		mt.getLoader().load(mt.getRootNode());
       		}
      	}}],
    collapseFirst:false,
  });
  
	var menuForm = new Ext.form.FormPanel({
		id : "menuForm",
         height: 200,
         width: 300,
         labelWidth: 60,
         labelAlign: "right",
         frame: true,
         defaults:{
           xtype:"textfield",
           width:180
        },
        items: [
           {name: "menu_name",fieldLabel: "�˵�����"}
        ]
	});
  
 function openMenuWin(val){
    MenuWin=Ext.getCmp('MenuWin');
    if(!MenuWin){
		MenuWin=new Ext.Window({
			layout:'fit',
			title:'ϵͳ�˵�',
			id:'MenuWin',
			width:320,
			height:405,
			closeAction:'hide',
			plain: true,
			modal: true,
			items:mtree,
			buttons : [{
				text : '����',id:"menuSaveBtn",
				handler : function() {
				 	var rs=mtree.getChecked();
				 	var menuPs = {};
				 	var menuParmen = [];
				 	var param={p_desktop_id:val,userid:"<%=currUserid%>"};
				 	Ext.each(rs, function (node) {
				 		var rec = node.attributes.record;
				 		var currUserid = "<%=currUserid%>";
				 		var menuNames = rec.get("menuname");
				 		var menuIds = rec.get("menuid");
				 		param['menu_id']=!param['menu_id'] ? menuIds:[].concat(param['menu_id']).concat(menuIds);
				 		param['menu_name']=!param['menu_name'] ? menuNames:[].concat(param['menu_name']).concat(menuNames);
				 	});
				 	var jr=new JrafRequest('pcmc','desk','addDeskGroup');
			 		jr.setExtraPs(param);
					jr.setSuccFn(function(data,status){
						loadData();
						MenuWin.hide();
				    });
					jr.postData();
				}
			},{
				text : '�ر�',id:"menuCloseBtn",
				handler : function() {
				 	 MenuWin.hide();
				}
			}],
	    	buttonAlign : "center"
	    });
	}
	MenuWin.show();
 }
 
 function openDeskMenuWin(val){
  var deskMenuWin=Ext.getCmp('deskMenuWin');
    if(!deskMenuWin){
		deskMenuWin=new Ext.Window({
			layout:'fit',
			title:"��������",
			id:'deskMenuWin',
			width:350,
			height:180,
			closeAction:'hide',
			plain: true,
			modal: true,
			items:menuForm,
			buttons : [{
				text : '����',id:"saveBtn",
				handler : function() {
			 		var jr=new JrafRequest('pcmc','desk','updateDesk');
			 		jr.setExtraPs({"desktop_id":param.paramVal});
			 		jr.setForm("menuForm");
					jr.setSuccFn(function(data,status){
						toastr.success("�������óɹ���");
						deskMenuWin.hide();
						loadData();
				    });
					jr.postData();
				}
			},{
				text : '�ر�',id:"closeBtn",
				handler : function() {
					deskMenuWin.hide();
				}
			}],
	    	buttonAlign : "center"
	    });
	} 
	deskMenuWin.show();
	menuForm.form.reset();
 }
</script>

</head>
 <body>
  <!--�����û�����Begin-->
<div id="dialog-form" title="��������" style="display: none;">
  <form id="groupForm" name="groupForm">
	  <table border="0" cellpadding="15" cellspacing="10">
		 <tr>
		  	<td align="right"><span class="STYLE13">�������ƣ�</span></td>
		  	<td align="left">
		  		<input name="userid" type="hidden" value="<%=session.getAttribute("userid")%>"/>
		  		<input id="menu_name" name="menu_name" type="text" style="width:200px;" class="form-field STYLE14" border="0" />
		  	</td>
		  </tr>
		  <tr>
		  	<td align="right" colspan="2">&nbsp;&nbsp;</td>
		  </tr>
		 <tr>
		  	<td align="right"><span class="STYLE13">��������</span></td>
		  	<td align="left">
		  		<input id="show_idx" name="show_idx" type="text"�� style="width:200px;" class="form-field STYLE14" border="0" />
		  	</td>
		  </tr>
	  </table>
  </form>
</div>
<!--End-->
  <div class="contextMenu" id="myMenu1">
      <ul>
        <li id="edit"><img src="/platform/icons/fam/group_edit.png" /> �༭<br /></li>
        <li id="del"><img src="/platform/icons/fam/group_delete.png" /> ɾ��<br /></li>
      </ul>
    </div>
    
  <p/>
  <div id="main_desk">
	</div>
  </body>
  <script>
  $(function() {
    $( "#dialog-form" ).dialog({
      autoOpen: false,
      height: 200,
      width: 380,
      modal: true,
      buttons: {
        "ȷ��": function() {
	        if ($("#menu_name").val()==""){
			    toastr.error("�������������","������Ϣ");
				return;
			}
			var bc = new BcRequest('pcmc','desk','addDeskGroup');
			bc.setForm("groupForm");
			bc.setFailFn(function(data,status){
				toastr.error(data.error_description);
				return;
				})
			bc.setSuccFn(function(data,status){
				toastr.success("����ɹ���");
	 			groupForm.reset();
	 			$("#dialog-form").dialog("close");
	 			loadData();
			});
			bc.postData();
        },
        "ȡ��": function() {
          $( this ).dialog( "close" );
          groupForm.reset();
        }
      }
    });
  });
  </script> 
</html>
