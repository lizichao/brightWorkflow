<%@ page contentType="text/html; charset=GBK" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/mooc/public/header.jsp"%>
<title>ѡ���û�</title>

<script id="userListRec" type="text/x-jsrender">	
<table class="table table-bordered table-striped table-hover">
<thead> 
<tr>
<th>����</th>
<th>�û��˺�</th>
<th>�û�����</th>
<th>��������</th>
<th>�û�����</th>
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
		   $("#userList").append("<span style='width:100%;color:red;'>��������</span>");
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
              <!-- �༶ -->   
         </div> 
         <div style="width:90%;margin-left:20px;margin-top:10px;text-align:center" id="userList">
              <!-- ѧ����Ϣ -->
         </div>
          <!-- ��ҳbegin -->
           <%@ include file="/mooc/public/pageinfo.jsp"%>
          <!-- ��ҳend -->
       </div>      
      </div> 
   </div> 
   <div id="buttonDiv" style="height:30px;text-align:left;margin-top:5px">
     <input type='button' id="" value="�ύ" onclick="confirmSelectUser()" />
   </div>
</body>
</html>