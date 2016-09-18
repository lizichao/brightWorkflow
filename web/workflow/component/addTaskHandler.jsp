<%@ page contentType="text/html; charset=GBK" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>新增处理人</title>
<link href="/workflow/css/style.css" rel="stylesheet">
<link href="/workflow/js/checkbox/red.css" rel="stylesheet">

<script type="text/javascript" src="/js/jquery/jquery-1.10.2.min.js"></script>
<script src="/workflow/js/checkbox/icheck.min.js"></script>
<script type="text/javascript" src="/js/jquery/common.js"></script>
<script src="/js/jquery/jsrender.js" type="text/javascript"></script>
<script type="text/javascript" src="/workflow/common/jquery-ui-1.9.2.min.js"></script>


<script type="text/javascript">

function submitAddTaskHandler(){
//	var addTaskHandlerVal = "402881824c27d80b014c35122ce300a8,402881824d516090014d55533c0f004e";
	var addhandlers = [];
	$("#to option").each(function() {
		var toValue = $(this).attr("value");
		if(!existUser(taskHandlers,toValue)){
			addhandlers.push(toValue);
		}
	});
	if(addhandlers.length == 0){
		alert("没有可以新增的处理人！");
		return false;
	}
	
	var bcReq = new BcRequest('workflow','componentAction','addTaskHandler');
	
	var taskId = paramObject.taskId;
	var processInstanceId = paramObject.processInstanceId;
	 var processDefKey = paramObject.processDefKey;
	 var processDefName = paramObject.processDefName;
	 var processDefId = paramObject.processDefId;
	 
	var componentParam = {'taskId':taskId,
			'processInstanceId':processInstanceId,
			'processDefKey':processDefKey,
			'processDefName':processDefName,
			'processDefId':processDefId,
			'addTaskHandler':addhandlers.join(",")
	};
	bcReq.setExtraPs({"componentName":"addTaskHandler","componentParam":JSON.stringify(componentParam)});
	bcReq.setSuccFn(function(data,status){
		  window.parent.location.href = "/workflow/template/completeTaskForm.jsp?taskId="+taskId;
		 iframeObj.closeDialog();
	});
	bcReq.postData();
}

var iframeObj = window.parent.frames[0];
var paramObject = window.dialogVal() || iframeObj.dialogVal();
var taskHandlers = paramObject.taskHandlers;
$(document).ready(function(){
	$(".hi-table tr:odd").addClass("odd-row");
	$(".hi-tims-1 li:odd").addClass("odd-row");
	$(".hi-tims-2 li:odd").addClass("odd-row");
	
	 var taskId = paramObject.taskId;
	 var bcReq = new BcRequest('workflow','componentAction','queryTransferUsers');
	
	 var taskId = paramObject.taskId;
	 bcReq.setExtraPs({'taskId':taskId});
	 bcReq.setSuccFn(function(data,status){
	//	 var workCalendarObject = data.Data[0];
		 for(var i=0; i <data.Data.length; i++){
			 var taskhandlerObject = data.Data[i];
			 if(!existUser(taskHandlers,taskhandlerObject.userid)){
				 $("#from").append( "<option value=\'"+taskhandlerObject.userid+"\'>"+taskhandlerObject.username+"</option>" );
			 }
		 }
		 
		 var taskHandlerIds = [];
		 for(var j=0; j <taskHandlers.length; j++){
			 var taskhandlerSelectObject = taskHandlers[j];
			 taskHandlerIds.push(taskhandlerSelectObject.userId);
			// $("#to").append( "<option value=\'"+taskhandlerSelectObject.userId+"\'>"+taskhandlerSelectObject.userName+"</option>" );
		 }
		 renderToUsers(taskHandlerIds);
	 });
	 bcReq.postData();
	 /*
	 var taskId = paramObject.taskId;
	 var bcReq = new BcRequest('workflow','formServiceOperate','getTaskConfigHandlers');
	 var taskId = paramObject.taskId;
	 bcReq.setExtraPs({'taskId':taskId});
	 bcReq.setSuccFn(function(data,status){
	//	 var workCalendarObject = data.Data[0];
		 for(var i=0; i <data.Data.length; i++){
			 var taskhandlerObject = data.Data[i];
			// $("#from").append( "<option value=\'"+taskhandlerObject.userid+"\'>"+taskhandlerObject.username+"</option>" );
			 $("#from").append( "<li value=\'"+taskhandlerObject.userid+"\'>"+taskhandlerObject.username+"</li>" );
		 }
		 $("#from li").click(
			function () { 
				$(this).toggleClass('on'); 
			}
		 )
		  $("#to li").click(
			function () { 
				$(this).toggleClass('on'); 
			}
		 )
	 });
	 bcReq.postData();*/
})

function renderToUsers(taskHandlerIds){
	 var bcReq = new BcRequest('workflow','componentAction','getLastDelegateUsers');
		
	 var taskId = paramObject.taskId;
	 var processInstanceId = paramObject.processInstanceId;
	 bcReq.setExtraPs({
		 'processInstanceId':processInstanceId,
		 'taskId':taskId,
		 'delegateUser':taskHandlerIds.join(",")
	 });
	 bcReq.setSuccFn(function(data,status){
		 for(var i=0; i <data.Data.length; i++){
			 var taskdelegateObject = data.Data[i];
			 var originalUserArray = taskdelegateObject.originalUsers;
			 
			 var originalUserNames = [];
			 if(originalUserArray && originalUserArray.length>0){
				 for(var k=0; k <originalUserArray.length; k++){
					 var originalUserObject = originalUserArray[k];
					 originalUserNames.push(originalUserObject.userName);
				 }
				 $("#to").append( "<option value=\'"+taskdelegateObject.delegateUserId+"\'>"+taskdelegateObject.delegateUserName+"(代"+originalUserNames.join(",")+")</option>" );
			 }else{
				 $("#to").append( "<option value=\'"+taskdelegateObject.delegateUserId+"\'>"+taskdelegateObject.delegateUserName+"</option>" );
			 }
		 }
	 });
	 bcReq.postData();
}

//taskHandlers:任务的已经存在的处理人，taskhandlerObject可以配置的处理人
function existUser(taskHandlers,taskhandlerUserId){
	for(var j =0;j<taskHandlers.length;j++){
		var eachSelectedHandlerId = taskHandlers[j].userId;
		if(eachSelectedHandlerId == taskhandlerUserId){
			return true;
		}
	}
	return false;
}

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
<h3><i class="ico-xzclr"></i>新增处理人</h3> 
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
<div class="hi-btn"><input name="" type="button" value="提 交" class=" hi-radius3"  onclick="submitAddTaskHandler()" /></div>  
</div>


</div>
</div>

<!--弹出-->

<script type="text/javascript">
$(".hi-tims-1 option:odd").addClass("odd-row");
$(".hi-tims-2 option:odd").addClass("odd-row");
</script>

<!-- 
<div class="hi-box hi-xzclr">   
<div class="hi-main">

<div class="hi-hd">
<h3><i class="ico-xzclr"></i>新增处理人</h3> 
</div>

<div class="hi-xzcl">
<div class="hi-tims hi-tims-1">
   <div class="formControls hi-select" style="width:150px;height:330px;" >
        <select name="from" id="from" multiple="multiple" size="10" style="width:150px;height:330px;">  
        </select>  
	</div>  
</div>

<div class="hi-yd">
  <ul>
	  <li id="addAll">>></li>
	  <li id="addOne">></li>
	  <li id="removeOne"><</li>
	  <li id="removeAll"><<</li>
  </ul>
</div>

<div class="hi-tims hi-tims-2">
    <div class="formControls hi-select" style="width:150px;height:330px;" >
       <select name="to" id="to" multiple="multiple" size="10" style="width:150px;height:330px;">  
            </select>  
     </div>  
</div>

<div class="hi-db">
<ul>
  <li id="Top">置顶</li>
  <li id="Up">上移</li>
  <li id="Down">下移</li>
  <li id="Buttom">置底</li>
</ul>
</div>

</div>

<div class="hi-page">
<div class="hi-btn"><input name="" type="submit" value="提 交" class="hi-radius3" onclick="submitUser()" /></div>  
</div>


</div>
</div>
 -->
</body>
</html>