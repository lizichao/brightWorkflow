<%@ page language="java" import="java.util.*" pageEncoding="GBK"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String webpath = request.getScheme()+"://"+request.getServerName();
%>
<!DOCTYPE html>
<html lang="zh-cn">
	<head>
	    <base href="<%=basePath%>">
	    <meta charset="GBK">
	    <title>WebUploader文件上传</title>
  <meta http-equiv="pragma" content="no-cache" /> 
  <meta http-equiv="cache-control" content="no-cache" /> 
  <meta http-equiv="expires" content="0" /> 
  <meta http-equiv="keywords" content="keyword1,keyword2,keyword3" /> 
  <meta http-equiv="description" content="This is my page" /> 
  <meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport" /> 
  <meta content="yes" name="apple-mobile-web-app-capable" /> 
  <meta content="black" name="apple-mobile-web-app-status-bar-style" /> 
  <meta content="telephone=no" name="format-detection" /> 
  <!-- 引入资源 --> 
  <style type="text/css">
		.progress{
			height: 20px;
			background-color: #ccc;
		}
		
		.progress .progress-bar{
			width: 0;
			height:100%;
			background-color: skyblue;
		}
		
		/*webUpload.css中移出样式*/
		.webuploader-container {
			position: relative;
		}
		.webuploader-element-invisible {
			position: absolute !important;
			clip: rect(1px 1px 1px 1px); /* IE6, IE7 */
		    clip: rect(1px,1px,1px,1px);
		}
		.webuploader-pick {
			position: relative;
			display: inline-block;
			cursor: pointer;
			background: #32b59d;
			background-image: -webkit-linear-gradient(top,#32b59d,#189d7e);
			background-image: -moz-linear-gradient(top,#32b59d,#189d7e);
			background-image: -ms-linear-gradient(top,#32b59d,#189d7e);
			background-image: -o-linear-gradient(top,#32b59d,#189d7e);
			background-image: linear-gradient(to bottom,#32b59d,#189d7e);
			padding: 10px 15px;
			color: #fff;
			text-align: center;
			border-radius: 3px;
			overflow: hidden;
			border:1px solid #00836f;
		}
		.webuploader-pick-hover {	
			background:#0aa88e;
			background-image:-webkit-linear-gradient(top,#0aa88e,#208072);
			background-image:-moz-linear-gradient(top,#0aa88e,#208072);
			background-image:-ms-linear-gradient(top,#0aa88e,#208072);
			background-image:-o-linear-gradient(top,#0aa88e,#208072);
			background-image:linear-gradient(to bottom,#0aa88e,#208072);
			text-decoration:none;
		}
		
		.webuploader-pick-disable {
			opacity: 0.6;
			pointer-events:none;
		}
		/*webUpload.css中移出样式end */
		</style> 
  <!--引入JS--> 
  <script src="/js/jquery/jquery-1.7.1.min.js" type="text/javascript"></script> 
  <script type="text/javascript" src="/js/webuploader-0.1.5/webuploader.js"></script> 
  <script type="text/javascript">
		var file_num =0;
		var config = {
			pick: "#picker",
			pickTitle: "选择附件",
			queuedTitle: "等待提交",
			accept: {
      	title: 'Images',
      	extensions: 'gif,jpg,jpeg,bmp,png',
		    mimeTypes: 'image/*' //video/*;application/msword
      },
      fileNumLimit:5,
      fileSingleSizeLimit: 50 * 1024 * 1024
		};
		// 文件上传
		function init(a){
			$.extend(config,a);
			$("#picker").html(config.pickTitle);
			createUploader();
		}
		function createUploader(){
			var BASE_URL = "<%=basePath%>";
	    var $ = jQuery,
	        $list = $('#thelist'),
	        $btn = $('#ctlBtn'),
	        state = 'pending',
	        uploader;
			    uploader = WebUploader.create({
			        // 不压缩image
			        resize: false,
			        // swf文件路径
			        swf: BASE_URL+'js/webuploader-0.1.5/Uploader.swf',
			        // 文件接收服务端
			        server: BASE_URL + "/servlet/UploadServlet",
			        //auto: true,// [可选] [默认值：false] 设置为 true 后，不需要手动调用上传，有文件选择即开始上传
			        
							formData:{
								"fileName":"fileName",
					             "uploadsource":"exam"
							},
							
			        // 选择文件的按钮。可选。
			        // 内部根据当前运行是创建，可能是input元素，也可能是flash.
			        //pick: '#picker',
			        pick: config.pick,
			        accept: config.accept,
			        // 50 M 限制单个文件的大小为不超过50M
		        	fileSingleSizeLimit: config.fileSingleSizeLimit,    //验证单个文件大小是否超出限制, 超出则不允许加入队列，单位为Byte（字节）。
		        	fileNumLimit : config.fileNumLimit,
			        //如果不设置，则后台文件输出出现乱码，文件内容也是乱码
			        sendAsBinary:true, // [默认值：false] 是否已二进制的流的方式发送文件，这样整个上传内容php://input都为文件内容， 其他参数在$_GET数组中。
			    	  //设置为true就可以重复选择同一个文件
			    	  duplicate: false //{Boolean} [可选] [默认值：undefined] 去重， 根据文件名字、文件大小和最后修改时间来生成hash Key.
			    });
		
		    // 当有文件添加进来的时候
		    /*当文件被加入队列以后触发。
		     *@parameter file {File}File对象
		     *
		     */
		    uploader.on( 'fileQueued', function( file ) {
		    	file_num ++;
		    	//decodeURIComponent(file.name) 解码后文件名
		      var s = '';
		      s = '<div id="' + file.id + '" class="item" >' +
		          '<h4 class="info" style="display:none;">' + file.name + '</h4>' +
		          '<h3 class="info" >' + decodeURIComponent(file.name) + '</h3>' +
		          '<p class="state">'+ config.queuedTitle + '...  ';
		      if(parent.initWebUploader){s += '<button class="btn btn-default cancel" >取消</button>';}    
		      s += '</p></div>';
		      $list.append(s);
		      
		      if(file_num==5){
		        $("#picker").remove();
		      }
		    
	    	  //父窗体交互
	    	  parent.file_num = file_num;
	          var val = parent.document.getElementById("ifm");
	          val.height = (parseInt(val.height) + 120);
	        //parent.resizeDiv(120);
		    });
		    
		    /*uploadStart事件  某个文件开始上传前触发，一个文件只会触发一次。
		     *@parameter  file {File}File对象
		     *作用：可以在这里设置参数formData的参数
		     */
		    uploader.on( 'uploadStart', function(file) {
		    	uploader.options.formData["fileName"]=file.name;//由于后台数据是通过fileName参数来获取相应的文件名，所以在这里可以设置相应的值
		    });
		    
		
		    // 文件上传过程中创建进度条实时显示。
		    /*uploadProgress事件 上传过程中触发，携带上传进度。
		     *@parameter file {File}File对象
		     *@parameter percentage {Number}上传进度
		     */
		    uploader.on('uploadProgress', function(file, percentage) {
		        var $li = $( '#'+file.id ),
		            //$percent = $li.find('.progress .progress-bar');
		            $percent = $li.find('.progress .progress-bar');
		
		        // 避免重复创建
		        if (!$percent.length ) { //这里可以自定义进度条的样式，思路一个div里面包含另一个div，第一个div为第二个div填充的对象
		            $percent = $('<div class="progress">' +
		              			'<div class="progress-bar"></div>' +
		            			'</div>').appendTo( $li ).find('.progress-bar');//
		        }
		
		        $li.find('p.state').text('上传中');
		
		        $percent.css( 'width', percentage * 100 + '%' );//动态设置第二个div的长度。
		        //动态设置第二个div的长度。
		        $percent.html(parseInt(percentage * 100) + '%');
		        
		    });
		    
			 var successFileid="";
			/*uploadSuccess函数，当文件上传成功时触发。
			 *parameter  file {File}File对象
			 *parameter  response {Object}服务端返回的数据
			 */
		    uploader.on('uploadSuccess', function(file,response) {
		         $( '#'+file.id ).find('p.state').text('已上传');
		       if(successFileid==""){
		         successFileid = response.id;
		       }else{
		         successFileid+=","+response.id;
		       }  
		       
		    });
			 uploader.on('uploadFinished', function() {
			      //调用父窗体控件
		        var val = parent.document.getElementById("attachment_id");
		        val.value=successFileid;
		         //调用父窗体方法
		         parent.uploadSuc();
		       
		    });
			/*uploadError函数，当文件上传出错时触发。
			 *@parameter  file {File}File对象
			 *@parameter  reason {String}出错的code
			 */
		    uploader.on( 'uploadError', function(file,reason) {
		        $( '#'+file.id ).find('p.state').text('上传出错');
		    });
		
			//不管成功或者失败，文件上传完成时触发。
			/* uploadComplete事件  不管成功或者失败，文件上传完成时触发。
			 * file {File} [可选]File对象
			 */
		    uploader.on('uploadComplete', function(file) {
		        //$( '#'+file.id ).find('.progress').fadeOut();//fadeOut()隐藏上传的进度条
		    });
		    
		    //
		    /*当validate不通过时，会以派送错误事件的形式通知调用者。通过upload.on('error', handler)可以捕获到此类错误，
		     *目前有以下错误会在特定的情况下派送错来。
		     *Q_EXCEED_NUM_LIMIT 在设置了fileNumLimit且尝试给uploader添加的文件数量超出这个值时派送。
			 *Q_EXCEED_SIZE_LIMIT 在设置了Q_EXCEED_SIZE_LIMIT且尝试给uploader添加的文件总大小超出这个值时派送。
			 *Q_TYPE_DENIED 当文件类型不满足时触发。。
			 *F_DUPLICATE 当设置属性duplicate不为true时，文件重复
		     *
		     */
		    uploader.on('error', function(type){
		     	if ("F_DUPLICATE" == type) {//文件重复，当设置属性duplicate不为true时
		     		parent.toastr.error("该文件已选中");
		     	} else if ("Q_TYPE_DENIED" == type) {//当文件类型不满足，当设置相应的accept属性时
		     		parent.toastr.error("不支持该类型文件");
		     	} else if ("Q_EXCEED_NUM_LIMIT" == type) {
		     		parent.toastr.error("超出文件上传数量");
		     	} else if ("Q_EXCEED_SIZE_LIMIT" == type ) {
		     		parent.toastr.error("文件过大，请重新选择");
		     	}
		    });
		
		    uploader.on( 'all', function(type) {
		        if ( type === 'startUpload' ) {
		            state = 'uploading';
		        } else if ( type === 'stopUpload' ) {
		            state = 'paused';
		        } else if ( type === 'uploadFinished' ) {
		            state = 'done';
		        }
		
		        if ( state === 'uploading' ) {
		            $btn.text('暂停上传');
		        } else {
		            $btn.text('开始上传');
		        }
		    });
		
		    $btn.on( 'click', function() {
		        if ( state == 'uploading' ) {
		            uploader.stop();
		        } else {
		            var array = uploader.getFiles();//获取队列中的文件
		            if (array!="") {
		            	var fileInfo = $("#fileInfo");
		            	var text = "";
		            	for(var index in array) {
		            		var file = array[index];
		            		text += file.name+","+file.size+","+file.getStatus()+","+file.getSource()+"<br>"
		            	}
		            	$("#fileInfo").html(text);
		            }
		            uploader.upload();
		        }
		        return false;  //如果是嵌套在form表单中，为了防止当前的click事件触发外层form表单事件，则这里需返回false
		    });
		    $list.on("click" ,".cancel",function(){
		        var $ele = $(this);            
		        var id = $ele.parent().parent().attr("id"); 
		        uploader.removeFile(id,true);           
		        $ele.parent().parent().remove();
		    });
		}
		
		</script>   
  <div id="uploader" class="wu-example"> 
   <!--用来存放文件信息--> 
   <div id="thelist" class="uploader-list" style="text-align:center;"></div> 
   <div class="btns" style="text-align:center;"> 
    <div id="picker">选择附件</div>
    <!-- style="display:none" --> 
    <button id="ctlBtn" class="btn btn-default" style="display:none;">开始上传</button> 
    <!-- style="display: none;" --> 
   </div> 
  </div>   
 </body>
</html>