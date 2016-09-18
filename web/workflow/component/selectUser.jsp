<%@ page contentType="text/html; charset=GBK" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/mooc/public/header.jsp"%>
<title>选择用户</title>

<script id="userListRec" type="text/x-jsrender">	
<table class="table table-bordered table-striped table-hover">
<thead> 
<tr>
<th>操作</th>
<th>用户账号</th>
<th>用户名称</th>
<th>所属机构</th>
<th>用户类型</th>
</tr>
</thead>
{{for Data}}
    <tr>
       <input type="hidden" id="userid{{:#index+1}}"  name="userid{{:#index+1}}" value="{{:userid}}">
       <input type="hidden" id="usercode{{:#index+1}}"  name="usercode{{:#index+1}}" value="{{:usercode}}">
       <input type="hidden" id="username{{:#index+1}}"  name="username{{:#index+1}}" value="{{:username}}">
      <td><input type="radio"  id='selectUser_{{:#index+1}}radio' name='selectUser_{{:#index+1}}' value='{{:#index+1}}' onclick=""/></td>
      <td>{{:usercode}}</td>
      <td>{{:username}}</td>
      <td>{{:deptname}}</td>
      <td>{{:usertype}}</td>
    </tr>
{{/for}}
</table>
</script>

<script type="text/javascript">
$(document).ready(function(){
	var bcReq = new BcRequest('pcmc','userrole','getUserList');
	//bcReq.setExtraPs({"PageSize":"10","PageNo":1,"query_processInstanceId":processInstanceId,'query_processDefId':processDefId});
	bcReq.setSuccFn(function(data,status){
		 $("#userList").empty();
		 if(data.Data.length>0){
			 var userContent=$("#userListRec").render(data);
			 $("#userList").append(userContent);
			 $("#ext-comp-1012").show();
             setPageInfo(data);
		 }else{
		   $("#userList").append("<span style='width:100%;color:red;'>暂无数据</span>");
			 $("#ext-comp-1012").hide();
		 }
	});
	bcReq.postData();
});

function confirmSelectUser(){
	var selectIndex =  $('input:radio:checked').val();
	var userid =  $('#userid'+selectIndex).val();
	var usercode =  $('#usercode'+selectIndex).val();
	var username =  $('#username'+selectIndex).val();
	
	window.opener.$('#subTaskUserId').val(userid);
	window.opener.$('#subTaskUserName').val(username);
	window.close()
}

</script>
</head>
<body>
  <div style="height: 500px;" class="mainContainer" pagename="paper_question" status="paper_question"> 
      <div class="child-content fn-clear"> 
       <div style="height: 500px; " class="content"> 
         <div class="filter-box"> 
              <!-- 班级 -->   
         </div> 
         <div style="width:90%;margin-left:20px;margin-top:10px;text-align:center" id="userList">
              <!-- 学生信息 -->
         </div>
          <!-- 分页begin -->
           <%@ include file="/mooc/public/pageinfo.jsp"%>
          <!-- 分页end -->
       </div>      
      </div> 
   </div> 
   <div id="buttonDiv" style="height:30px;text-align:left;margin-top:5px">
     <input type='button' id="" value="提交" onclick="confirmSelectUser()" />
   </div>
</body>
</html>