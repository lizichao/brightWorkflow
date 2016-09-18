<%@ page contentType="text/html; charset=GBK" %><%request.setCharacterEncoding("GBK");response.setHeader("Cache-Control", "no-cache");response.setHeader("Pragma", "no-cache");response.setDateHeader("Expires", 0);
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
	String currentVer = "2.0.5";//增加dialog
%>
<link href="<%=basePath%>platform/css/common.css?v=<%=currentVer%>" rel="stylesheet" type="text/css" />
<link href="<%=basePath%>platform/css/layer.css?v=<%=currentVer%>" rel="stylesheet" type="text/css" />
<link href="<%=basePath%>js/jquery/plugin/zebra_dialog/zebra_dialog.css?v=<%=currentVer%>" rel="stylesheet" type="text/css" />
<link href="<%=basePath%>js/jquery/plugin/circliful/css/jquery.circliful.css?v=<%=currentVer%>" rel="stylesheet" type="text/css" />
<link href="<%=basePath%>js/jquery/plugin/DropKick/dropkick.css?v=<%=currentVer%>" rel="stylesheet" type="text/css"/>
<link href="<%=basePath%>js/jquery/plugin/DropKick/example.css?v=<%=currentVer%>" rel="stylesheet" type="text/css"/>
<link href="<%=basePath%>js/jquery/plugin/toastr/toastr.min.css?v=<%=currentVer%>" rel="stylesheet" type="text/css"/>
<link href="<%=basePath%>js/jquery/plugin/datetimepicker/jquery.datetimepicker.css?v=<%=currentVer%>" rel="stylesheet" type="text/css"/>
<link href="<%=basePath%>ueditor/JME/mathquill/mathquill.css?v=<%=currentVer%>" rel="stylesheet" type="text/css"/>
<link href="<%=basePath%>js/jquery/plugin/Leedialog/dialog.css?v=<%=currentVer%>" rel="stylesheet" type="text/css"/>
<link href="<%=basePath%>js/jquery/plugin/fancybox/jquery.fancybox.css?v=<%=currentVer%>" rel="stylesheet" type="text/css" media="screen"/>

<link href="<%=basePath%>images/mooc/core.css?v=<%=currentVer%>" type="text/css" rel="stylesheet" />
<link href="<%=basePath%>images/mooc/question/base.css?v=<%=currentVer%>" rel="stylesheet" type="text/css" /> 
<link href="<%=basePath%>images/mooc/question/common.css?v=<%=currentVer%>" rel="stylesheet" type="text/css" /> 
<link href="<%=basePath%>images/mooc/question/teacher.css?v=<%=currentVer%>" rel="stylesheet" type="text/css" /> 
<link href="<%=basePath%>images/mooc/question/jquery-ui-1.css?v=<%=currentVer%>" rel="stylesheet" type="text/css" />

<script src="<%=basePath%>js/jquery/jquery-1.7.1.min.js?v=<%=currentVer%>" type="text/javascript"></script>
<script src="<%=basePath%>js/jquery/jsrender.js?v=<%=currentVer%>" type="text/javascript"></script>
<script src="<%=basePath%>js/jquery/plugin/circliful/js/jquery.circliful.min.js?v=<%=currentVer%>" type="text/javascript"></script>
<script src="<%=basePath%>js/jquery/pym.js?v=<%=currentVer%>" type="text/javascript"></script>
<script src="<%=basePath%>js/jquery/plugin/treeTable/jquery.treetable.js?v=<%=currentVer%>" type="text/javascript"></script>
<script src="<%=basePath%>platform/public/sysparam.js?v=<%=currentVer%>" type="text/javascript"></script>
<script src="<%=basePath%>js/jquery/common.js?v=<%=currentVer%>" type="text/javascript"></script>
<script src="<%=basePath%>js/jquery/layer.js?v=<%=currentVer%>" type="text/javascript"></script>
<script src="<%=basePath%>js/jquery/plugin/zebra_dialog/zebra_dialog.js?v=<%=currentVer%>" type="text/javascript"></script>
<script src="<%=basePath%>js/jquery/plugin/DropKick/jquery.dropkick-min.js?v=<%=currentVer%>" type="text/javascript"></script>
<script src="<%=basePath%>js/jquery/plugin/toastr/toastr.min.js?v=<%=currentVer%>" type="text/javascript"></script>
<script src="<%=basePath%>js/jquery/plugin/datetimepicker/jquery.datetimepicker.js?v=<%=currentVer%>" type="text/javascript"></script>
<script src="<%=basePath%>/ueditor/JME/mathquill/mathquill.min.js?v=<%=currentVer%>" type="text/javascript"></script>
<script src="<%=basePath%>js/jquery/plugin/Leedialog/dialog.js?v=<%=currentVer%>" type="text/javascript"></script>
<script src="<%=basePath%>platform/public/session.jsp?r=<%=Math.random()%>" type="text/javascript"></script>

<script src="<%=basePath%>js/jquery/plugin/fancybox/jquery.mousewheel.pack.js?v=<%=currentVer%>" type="text/javascript"></script>
<script src="<%=basePath%>js/jquery/plugin/fancybox/jquery.fancybox.js?v=<%=currentVer%>" type="text/javascript" ></script>

<script type="text/javascript">
var bc_cfg = {
  siteurl: "<%=basePath%>",
  li_loading: '<li class="loading_spinner"></li>',
  div_loading: '<div class="loading_spinner" style="height:30px;"></div>',
  loading: '<img src="/images/mooc/loading_circle.gif" alt="加载中，请稍后！" title="加载中，请稍后！">',
  loading_error: '<span style="org">数据加载失败！</span>',
  is_ie: ($.browser.msie && !$.support.style) ? true: false,
  is_ie6: ($.browser.msie && ($.browser.version == "6.0") && !$.support.style) ? true: false,
  today: '<%=cn.brightcom.jraf.util.DatetimeUtil.getNow("yyyy-MM-dd")%>'
};
</script>