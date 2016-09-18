<%@ page contentType="text/html; charset=GBK" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>选择审批人</title>
<link href="/workflow/css/style.css" rel="stylesheet">
<link href="/workflow/js/checkbox/red.css" rel="stylesheet">

<script type="text/javascript" src="/js/jquery/jquery-1.10.2.min.js"></script>
<script src="/workflow/js/checkbox/icheck.min.js"></script>
<script type="text/javascript" src="/js/jquery/common.js"></script>
<script src="/js/jquery/jsrender.js" type="text/javascript"></script>
<script type="text/javascript" src="/workflow/common/jquery-ui-1.9.2.min.js"></script>


<script type="text/javascript">
function submitUser(){
	var nexthandlers = [];
	var nextPrincipalHandlers = [];
	$("#to option").each(function(i) {
		 if(isPrincipal && i==0){
			 nextPrincipalHandlers.push($(this).attr("value"));
		 }
		nexthandlers.push($(this).attr("value"));
	});
	var flowId = paramObject.flowId;
	var currentTaskKey = paramObject.currentTaskKey;
	//var operateType = paramObject.operateType;
	nexthandlers = nexthandlers.length>0 ? nexthandlers.join(",") : "";
	if(nexthandlers == ""){
		alert("请选择人员!");
		return false;
	}
	
	var paramVariable= {
			"isCondition":flowId,
			"nextHandlers":nexthandlers
	};
	window.parent.Brightcom.workflow.closeCounterSignOperate(currentTaskKey,flowId,paramVariable);
	//window.parent.Brightcom.workflow.submitWorkflowRequest(operateType,flowId,nexthandlers,"",nextPrincipalHandlers);
}

var iframeObj = window.parent.frames[0];
var paramObject = window.dialogVal() || iframeObj.dialogVal();
var isPrincipal = paramObject.isPrincipal;
var flowId = paramObject.flowId;
var taskId = paramObject.taskId;
$(document).ready(function(){
	 var configHandlers = paramObject.configHandlers;
	// var selectedUsers = paramObject.selectedUsers;
	 for(var i=0; i <configHandlers.length; i++){
		 var configHandlerEach = configHandlers[i];
		// if(!selectedUsers.contains()){
			 $("#from").append( "<option value=\'"+configHandlerEach.userId+"\'>"+configHandlerEach.userName+"</option>" );
		// }
	 }
	 if(isPrincipal){
		 $("#dealWay").show();
	 }
	 /*
	 for(var j =0;j<selectedUsers.length;j++){
		 var selectedUserEach = selectedUsers[i];
		 $("#to").append( "<option value=\'"+selectedUserEach.userId+"\'>"+selectedUserEach.userName+"</option>" );
	 }*/
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

<div class="hi-box hi-xzclr">   
<div class="hi-main">

<div class="hi-hd">
<h3><i class="ico-xzclr"></i>选择审批人</h3> 
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
<div id="dealWay" class="hi-btn" style="display:none">
    <span>办理方式</span>
    <input type="radio"  id='radio' name='principalRadio' value='' onclick=""/>主办&nbsp;&nbsp;
    <input type="radio"  id='radio' name='minorRadio' value='' onclick=""/>分办&nbsp;&nbsp;
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