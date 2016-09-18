<%@ page contentType="text/html; charset=GBK" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>ѡ��������</title>
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
		alert("��ѡ����Ա!");
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
        //ѡ��һ��  
        $("#addOne").click(function(){  
            $("#from option:selected").clone().appendTo("#to");  
            $("#from option:selected").remove();  
        });  
  
        //ѡ��ȫ��  
        $("#addAll").click(function(){  
            $("#from option").clone().appendTo("#to");  
            $("#from option").remove();  
        });  
          
        //�Ƴ�һ��  
        $("#removeOne").click(function(){  
            $("#to option:selected").clone().appendTo("#from");  
            $("#to option:selected").remove();  
        });  
  
        //�Ƴ�ȫ��  
        $("#removeAll").click(function(){  
            $("#to option").clone().appendTo("#from");  
            $("#to option").remove();  
        });  
  
        //��������  
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
  
        //����һ��  
        $("#Up").click(function(){  
            var selected = $("#to option:selected");  
            if(selected.get(0).index!=0){  
                selected.each(function(){  
                    $(this).prev().before($(this));  
                });  
            }  
        });  
  
        //����һ��  
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
  
        //�����ײ�  
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
<h3><i class="ico-xzclr"></i>ѡ��������</h3> 
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
  <li id="Top">�ö�</li>
  <li id="Up">����</li>
  <li id="Down">����</li>
  <li id="Buttom">�õ�</li>
</ul></div>

</div>

<div class="hi-page">
<div id="dealWay" class="hi-btn" style="display:none">
    <span>����ʽ</span>
    <input type="radio"  id='radio' name='principalRadio' value='' onclick=""/>����&nbsp;&nbsp;
    <input type="radio"  id='radio' name='minorRadio' value='' onclick=""/>�ְ�&nbsp;&nbsp;
  <!--  <input type="radio" style="height:10px;margin:0" id='wayRadio' name='wayRadio' value='2' onclick=""/> ��֪&nbsp;&nbsp; --> 
</div>  
<div class="hi-btn"><input name="" type="button" value="�� ��" class=" hi-radius3"  onclick="submitUser()" /></div>  
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
            <input type="button" id="Top" value="�ö�" style="width:50px;" /><br />  
            <input type="button" id="Up" value="����" style="width:50px;" /><br />  
            <input type="button" id="Down" value="����" style="width:50px;" /><br />  
            <input type="button" id="Buttom" value="�õ�" style="width:50px;" /><br />  
        </td>  
    </tr>  
</table>  
<input type='button' id="" value="�ύ" onclick="submitUser()" /> -->
</body>
</html>