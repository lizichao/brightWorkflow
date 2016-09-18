<%@ page contentType="text/html; charset=GBK" %>
<html>
<head>
<link rel="shortcut icon" href="icons/favicon.ico" >
<title>JRAF Gui Designer</title>
    <link rel="stylesheet" type="text/css" href="css/Loading.css" />
<%@ include file="/platform/public/includejs.jsp"%>
    <link rel="stylesheet" type="text/css" href="css/Ext.ux.guid.plugin.Designer.css" />
<%@ include file="/designer/includejs.jsp"%>
</head>
<body style="overflow:hidden;" onunload="myunload();">
<div id="loading-mask"></div>
<div id="loading">
  <div class="loading-indicator"><img src="icons/loading-balls.gif" style="margin-right:8px;" align="absmiddle"/>Loading GuiDesigner....</div>
</div>

<script>
window.postfix=".jsp";
 var options = 
	  {dock : 1,
	   compressed : 0,
	   codepress : 1,
	   autoresize : 1,
	   floatheight : 480,
	   floatwidth : 580,
	   cmpfiles : "{0}ThirdParty.Components.json"};

 //Url based actions
 var windowMode = Ext.ux.UTIL.getUrlAction('window',options.float)==1; //Change this flag to true if you want designer to be a window
 var docked = Ext.ux.UTIL.getUrlAction('docked',options.dock)==1;
 var nocache = Ext.ux.UTIL.getUrlAction('nocache',options.nocache)==1;
 var autoResize = Ext.ux.UTIL.getUrlAction('autoresize',options.autoresize)==1;
 var cmpfiles = (options.cmpfiles || "").replace('\r').split("\n");
 var designer = new Ext.ux.guid.plugin.Designer({
  codePress     : !Ext.isSafari && options.codepress==1,
  componentFiles : cmpfiles,
  autoResize    : autoResize,
  dockedMode    : docked,
  toolboxTarget : 'toolbox',
  nocache : nocache,
  customProperties : true
 , repository      : new Ext.ux.guid.data.JrafFileRepository()
})

var prop = new Ext.ux.guid.plugin.Designer.RightPanel({
		title: ' Ù–‘',
        region: 'east',
        disabled: true,
	    collapsible : true,
        border  : false,
        minHeight : 150,
        split:true,
        width:237,
        designer: designer
   });
var leftbar = new Ext.ux.guid.plugin.Designer.LeftPanel({
		designer: designer
	});
         
 //Items is the array with the designer area
 var items = docked
      ? [{ region : 'west',
	       layout : 'fit',
	       collapsible : true,
		   split : true,
	       border : false,
	       id     : 'toolbox',
	       xtype  : 'panel',
	       title  : 'JRAFGuiDesigner',
	       width  : 237,
	       items : leftbar
	     }]
	  : [];

  items.push({
	  region:'center',
	  layout:'fit',
	  border:false,
	  bodyBorder:false,
	  title:'BodyPanel',
	  style:'padding:3px 3px;background:black',
	  items:{
		border:false,
		bodyBorder:false,
		bodyStyle:'background:black;border:dashed green 1px;',
		xtype :'jsonpanel',
		 plugins : [
			designer
		  ]
	 }
  });
  items.push(prop);

Ext.onReady(function (){
    setTimeout(function(){
		Ext.get('loading').remove();
		Ext.get('loading-mask').fadeOut({
			remove:true
		});
	}, 350);
  	var vport=new Ext.Viewport({
		layout : 'border',
		items: items
 	});
});

function myunload(){
	Ext.destroy(vport);
	designer=null;
	items=null;
	prop=null;
	leftbar=null;
	vport=null;
}
</script>
</body>
</html>