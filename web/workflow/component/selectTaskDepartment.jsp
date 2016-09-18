<%@ page contentType="text/html; charset=GBK" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>选择会签部门</title>
<link href="/workflow/css/style.css" rel="stylesheet">
<link href="/workflow/js/checkbox/red.css" rel="stylesheet">

<script type="text/javascript" src="/js/jquery/jquery-1.10.2.min.js"></script>
<script src="/workflow/js/checkbox/icheck.min.js"></script>
<script type="text/javascript" src="/js/jquery/common.js"></script>
<script src="/js/jquery/jsrender.js" type="text/javascript"></script>
<script type="text/javascript" src="/workflow/common/jquery-ui-1.9.2.min.js"></script>


<script type="text/javascript">
function submitUser(){
	var nextPrincipalDepartments = [];
	var nextDepartments = [];
	var wayRadioValue = $('input[name="wayRadio"]:checked').val();
	$("#to option").each(function(i) {
		 if(isPrincipal && i==0 && wayRadioValue =='0'){
			 nextPrincipalDepartments.push($(this).attr("value"));
		 }
		nextDepartments.push($(this).attr("value"));
	});
	var flowId = paramObject.flowId;
	var operateType = paramObject.operateType;
	nextDepartments = nextDepartments.length>0 ? nextDepartments.join(",") : "";
	if(nextDepartments == ""){
		alert("请选择部门!");
		return false;
	}
	window.parent.Brightcom.workflow.submitWorkflowRequest(operateType,flowId,"",nextDepartments,"",nextPrincipalDepartments);
}

var iframeObj = window.parent.frames[0];
var paramObject = window.dialogVal() || iframeObj.dialogVal();
var isPrincipal = paramObject.isPrincipal;
$(document).ready(function(){
	 var multiDepartments = paramObject.multiDepartments;
	 var selectedUsers = paramObject.selectedUsers;
	 for(var i=0; i <multiDepartments.length; i++){
		 var multiDepartmentEach = multiDepartments[i];
	//	 if(!selectedUsers.contains()){
			 $("#from").append( "<option value=\'"+multiDepartmentEach.deptId+"\'>"+multiDepartmentEach.deptName+"</option>" );
		// }
	 }
	 
	 if(isPrincipal){
		 $("#dealWay").show();
		 $("#departmentDiv").css("height","452px")
	 }
	 
	// for(var j =0;j<selectedUsers.length;j++){
		// var selectedUserEach = selectedUsers[i];
		// $("#to").append( "<option value=\'"+selectedUserEach.userId+"\'>"+selectedUserEach.userName+"</option>" );
	// }
})


 $(function(){  
		$("#from").dblclick(function(){
			var $option = $("#from option:selected");
			$option.appendTo("#to");
		});
		
		$("#to").dblclick(function(){
			var $option = $("#from option:selected");
			$option.appendTo("#from");
		});
        //选择一项  
        $("#addOne").click(function(){  
            $("#from option:selected").clone().appendTo("#to");  
            $("#from option:selected").remove();  
        });  
  
        //选择全部  
        $("#addAll").click(function(){  
            $("#from option").clone().appendTo("#to");  
            $("#from option").remove();  
        });  
          
        //移除一项  
        $("#removeOne").click(function(){  
            $("#to option:selected").clone().appendTo("#from");  
            $("#to option:selected").remove();  
        });  
  
        //移除全部  
        $("#removeAll").click(function(){  
            $("#to option").clone().appendTo("#from");  
            $("#to option").remove();  
        });  
  
        //移至顶部  
        $("#Top").click(function(){  
            var allOpts = $("#to option");  
            var selected = $("#to option:selected");  
            if(selected.get(0).index!=0){  
                for(i=0;i<selected.length;i++){  
                   var item = $(selected.get(i));  
                   var top = $(allOpts.get(0));  
                   item.insertBefore(top);  
                }  
            }  
        });  
  
        //上移一行  
        $("#Up").click(function(){  
            var selected = $("#to option:selected");  
            if(selected.get(0).index!=0){  
                selected.each(function(){  
                    $(this).prev().before($(this));  
                });  
            }  
        });  
  
        //下移一行  
        $("#Down").click(function(){  
            var allOpts = $("#to option");  
            var selected = $("#to option:selected");  
            if(selected.get(selected.length-1).index!=allOpts.length-1){  
                for(i=selected.length-1;i>=0;i--){  
                   var item = $(selected.get(i));  
                   item.insertAfter(item.next());  
                }  
            }  
        });  
  
        //移至底部  
        $("#Buttom").click(function(){  
            var allOpts = $("#to option");  
            var selected = $("#to option:selected");  
            if(selected.get(selected.length-1).index!=allOpts.length-1){  
                for(i=selected.length-1;i>=0;i--){  
                   var item = $(selected.get(i));  
                   var buttom = $(allOpts.get(length-1));  
                   item.insertAfter(buttom);  
                }  
            }  
        });  
    });  
</script>
</head>
<body>

<div id="departmentDiv" class="hi-box hi-xzclr">   
<div class="hi-main" >

<div class="hi-hd">
<h3><i class="ico-xzclr"></i>选择下节点审批部门</h3> 
</div>

<div class="hi-xzcl">
<div class="hi-tims hi-tims-1">
  <select  name="from" id="from" size="11" multiple="multiple">
  </select> 
</div>

<div class="hi-yd"><ul>
	  <li id="addAll">>></li>
	  <li id="addOne">></li>
	  <li id="removeOne"><</li>
	  <li id="removeAll"><<</li>
  </ul>
</div>

<div class="hi-tims hi-tims-2">
<select name="to" id="to" size="11" multiple="multiple">
</select> 
</div>

<div class="hi-db">
<ul>
  <li id="Top">置顶</li>
  <li id="Up">上移</li>
  <li id="Down">下移</li>
  <li id="Buttom">置底</li>
</ul></div>

</div>

<div class="hi-page">
<div id="dealWay" class="hi-btn" style="display:none;text-align:left">
    <span>办理方式</span>&nbsp;
    <input type="radio" style="height:10px;margin:0" id='wayRadio' name='wayRadio' value='0' checked="checked"/>主办&nbsp;
    <input type="radio" style="height:10px;margin:0" id='wayRadio' name='wayRadio' value='1' onclick=""/>分办&nbsp;
     <!--  <input type="radio" style="height:10px;margin:0" id='wayRadio' name='wayRadio' value='2' onclick=""/> 阅知&nbsp;&nbsp; --> 
</div>  
 <div class="hi-btn"><input name="" type="button" value="提 交" class=" hi-radius3"  onclick="submitUser()" /></div>  
</div>


</div>
</div>


<script type="text/javascript">
$(".hi-tims-1 option:odd").addClass("odd-row");
$(".hi-tims-2 option:odd").addClass("odd-row");
</script>
<!-- 
<table align="center" width="288" border="0" cellpadding="0" cellspacing="0">  
    <tr>  
        <td colspan="4">  
            <select name="from" id="from" multiple="multiple" size="10" style="width:100%">  
            </select>  
        </td>  
        <td align="center">  
            <input type="button" id="addAll" value=" >> " style="width:50px;" /><br />  
            <input type="button" id="addOne" value=" > " style="width:50px;" /><br />  
            <input type="button" id="removeOne" value="&lt;" style="width:50px;" /><br />  
            <input type="button" id="removeAll" value="&lt;&lt;" style="width:50px;" /><br />  
        </td>  
        <td colspan="4">  
            <select name="to" id="to" multiple="multiple" size="10" style="width:100%">  
            </select>  
        </td>  
        <td align="center">  
            <input type="button" id="Top" value="置顶" style="width:50px;" /><br />  
            <input type="button" id="Up" value="上移" style="width:50px;" /><br />  
            <input type="button" id="Down" value="下移" style="width:50px;" /><br />  
            <input type="button" id="Buttom" value="置底" style="width:50px;" /><br />  
        </td>  
    </tr>  
</table>  
<input type='button' id="" value="提交" onclick="submitUser()" /> -->
</body>
</html>