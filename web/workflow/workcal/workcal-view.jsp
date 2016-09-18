<%@page contentType="text/html;charset=UTF-8"%>
<%response.setHeader("Cache-Control", "no-cache");response.setHeader("Pragma", "no-cache");response.setDateHeader("Expires", 0);
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
	String currentVer = "2.0.5";
%>
<%pageContext.setAttribute("currentHeader", "scope");%>
<%pageContext.setAttribute("currentMenu", "workcal");%>
<!doctype html>
<html lang="en">

<head>
    <title>工作日历</title>
    <link href="<%=basePath%>css/yuexue/common.css?v=<%=currentVer%>" rel="stylesheet" type="text/css" />
    
    <link type="text/css" rel="stylesheet" href="/workflow/css/bootstrap-datepicker/datepicker.css">
     <script type="text/javascript" src="/js/jquery/jquery-1.7.1.min.js"></script>
     <script type="text/javascript" src="/workflow/common/WorkCalendar.js"></script>
     <script src="<%=basePath%>js/jquery/common.js?v=<%=currentVer%>" type="text/javascript"></script>
<script type="text/javascript">
$(function() {
	var bcReq = new BcRequest('workflow','workcalController','getWorkcalCalendar');
	bcReq.setExtraPs({"PageSize":"10","PageNo":1});
	bcReq.setSuccFn(function(data,status){
		var workCalendar = new WorkCalendar();
		var workCalendarObject = data.Data[0];
		workCalendar.render('#m-main');
		//JSON.stringify(JSON.parse(workCalendarObject.weeks))
		//workCalendarObject.weeks.substring(1,workCalendarObject.weeks.length)
		workCalendar.activeByWeek(JSON.parse(workCalendarObject.weeks));
		workCalendar.markHolidays(JSON.parse(workCalendarObject.holidays));
		workCalendar.markWorkdays(JSON.parse(workCalendarObject.workdays));
		workCalendar.markExtrdays(JSON.parse(workCalendarObject.extrdays));
	});
	bcReq.postData();
});
</script>
</head>

  <body>

    <div class="row-fluid">

	  <!-- start of main -->
      <section id="m-main" class="span10">

      </section>
	  <!-- end of main -->
	</div>

  </body>

</html>
