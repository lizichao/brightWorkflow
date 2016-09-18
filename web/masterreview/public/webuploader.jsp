<%@ page language="java" import="java.util.*" pageEncoding="GBK"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String webpath = request.getScheme()+"://"+request.getServerName();

String rownum =(String)request.getParameter("rownum");
String file_upload_type =(String)request.getParameter("file_upload_type");
String hiddenAttachId =(String)request.getParameter("hiddenAttachId");
String hiddenDisplayId =(String)request.getParameter("hiddenDisplayId");
String uploadType =(String)request.getParameter("uploadType");
String buttonName =(String)request.getParameter("buttonName");
String uploadSingleId =(String)request.getParameter("uploadSingleId");

buttonName = new String(buttonName.getBytes("iso-8859-1"), "utf-8") ;
//buttonName = URLDecoder.decode(buttonName,"UTF-8");
System.out.println("buttonName========"+buttonName);
%>
<!DOCTYPE html>
<html lang="zh-cn">
	<head>
	    <base href="<%=basePath%>">
	    <meta charset="GBK">
	    <title>WebUploader�ļ��ϴ�</title>
  <meta http-equiv="pragma" content="no-cache" /> 
  <meta http-equiv="cache-control" content="no-cache" /> 
  <meta http-equiv="expires" content="0" /> 
  <meta http-equiv="keywords" content="keyword1,keyword2,keyword3" /> 
  <meta http-equiv="description" content="This is my page" /> 
  <meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport" /> 
  <meta content="yes" name="apple-mobile-web-app-capable" /> 
  <meta content="black" name="apple-mobile-web-app-status-bar-style" /> 
  <meta content="telephone=no" name="format-detection" /> 
  <!-- ������Դ --> 
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
		
		/*webUpload.css���Ƴ���ʽ*/
		.webuploader-container {
			position: relative;
			font-size: 12px;
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
			background: #7cbae5;
			background-image: -webkit-linear-gradient(top,#7cbae5,#57B1EF);
			background-image: -moz-linear-gradient(top,#7cbae5,#57B1EF);
			background-image: -ms-linear-gradient(top,#7cbae5,#57B1EF);
			background-image: -o-linear-gradient(top,#7cbae5,#57B1EF);
			background-image: linear-gradient(to bottom,#7cbae5,#57B1EF);
			/*padding-top: 5px;*/
			color: #fff;
			text-align: center;
			border-radius: 3px;
			overflow: hidden;
			border:0px solid #1F6DA3;
			width:100px;
			height: 30px;
			line-height: 30px;
		}
		.webuploader-pick-hover {	
			background:#1F7FC5;
			background-image:-webkit-linear-gradient(top,#1F7FC5,#055A96);
			background-image:-moz-linear-gradient(top,#1F7FC5,#055A96);
			background-image:-ms-linear-gradient(top,#1F7FC5,#055A96);
			background-image:-o-linear-gradient(top,#1F7FC5,#055A96);
			background-image:linear-gradient(to bottom,#1F7FC5,#055A96);
			text-decoration:none;
		}
		
		.webuploader-pick-disable {
			opacity: 0.6;
			pointer-events:none;
		}
		/*webUpload.css���Ƴ���ʽend */
		</style> 
  <!--����JS--> 
  <link href="/workflow/js/picture-preview/css/picture_preview.css" rel="stylesheet" />
   <script type="text/javascript" src="/js/jquery/jquery-1.7.1.min.js"></script>
  <script type="text/javascript" src="/js/webuploader-0.1.5/webuploader.js"></script> 
     <script src="/js/jquery/plugin/fancybox/jquery.mousewheel.pack.js" type="text/javascript"></script>
  <script src="/js/jquery/plugin/fancybox/jquery.fancybox.js" type="text/javascript" ></script>
  
  <script type="text/javascript" src="/workflow/js/dialog/js/dialog1.0.js"></script>
<script type="text/javascript" src="/workflow/js/picture-preview/js/picture_preview.js"></script>
  <script type="text/javascript">
        var rownum = '<%=rownum%>'
        var hiddenAttachId = '<%=hiddenAttachId%>'
        var file_upload_type = '<%=file_upload_type%>'
        var hiddenDisplayId = '<%=hiddenDisplayId%>'
        var uploadType = '<%=uploadType%>'
        var buttonName = '<%=buttonName%>'
        var uploadSingleId = '<%=uploadSingleId%>'
        var basePath = '<%=basePath%>'
		var file_num =0;
        var isFlag = true;
		var config = {
			pick: "#picker",
			pickTitle: "ѡ�񸽼�",
			queuedTitle: "�ȴ��ύ",
			accept: {
      	      title: '',
      	    //  extensions: 'gif',
		    //  mimeTypes: 'image/*' //video/*;application/msword
            },
          fileNumLimit:5,
          fileSingleSizeLimit: 50 * 1024 * 1024
		};
		// �ļ��ϴ�
		function init(a){
		//debugger
			$.extend(config,a);
			$("#picker").html(buttonName || config.pickTitle);
			createUploader();
		}
		
		
		function createUploader(){
			var BASE_URL = "<%=basePath%>";
	        var $ = jQuery,
	        $list = $('#thelist'),
	        $btn = $('#ctlBtn'),
	        state = 'pending',
	        formSubmitParam = {
					"fileName":"fileName",
		             "rownum":"<%=rownum%>",
		             "file_upload_type":"<%=file_upload_type%>"
			};
	         $.extend(formSubmitParam,config.param);
	        
	        uploader;
			    uploader = WebUploader.create({
			        // ��ѹ��image
			        resize: false,
			        // swf�ļ�·��
			        swf: BASE_URL+'js/webuploader-0.1.5/Uploader.swf',
			        // �ļ����շ����
			        server: BASE_URL + "/servlet/UploadServlet",
			        //auto: true,// [��ѡ] [Ĭ��ֵ��false] ����Ϊ true �󣬲���Ҫ�ֶ������ϴ������ļ�ѡ�񼴿�ʼ�ϴ�
			          quality: 90,
			          formData:formSubmitParam,
							
			        // ѡ���ļ��İ�ť����ѡ��
			        // �ڲ����ݵ�ǰ�����Ǵ�����������inputԪ�أ�Ҳ������flash.
			        //pick: '#picker',
			        pick: config.pick,
			        accept: config.accept,
			        // 50 M ���Ƶ����ļ��Ĵ�СΪ������50M
		        	fileSingleSizeLimit: config.fileSingleSizeLimit,    //��֤�����ļ���С�Ƿ񳬳�����, ���������������У���λΪByte���ֽڣ���
		        	fileNumLimit : config.fileNumLimit,
			        //��������ã����̨�ļ�����������룬�ļ�����Ҳ������
			        sendAsBinary:true, // [Ĭ��ֵ��false] �Ƿ��Ѷ����Ƶ����ķ�ʽ�����ļ������������ϴ�����php://input��Ϊ�ļ����ݣ� ����������$_GET�����С�
			    	  //����Ϊtrue�Ϳ����ظ�ѡ��ͬһ���ļ�
			    	duplicate: false //{Boolean} [��ѡ] [Ĭ��ֵ��undefined] ȥ�أ� �����ļ����֡��ļ���С������޸�ʱ��������hash Key.
			    });
			    
			    uploader.on( 'beforeFileQueued', function( file ) {
			    
			    });
			    
		
		    // �����ļ���ӽ�����ʱ��
		    /*���ļ�����������Ժ󴥷���
		     *@parameter file {File}File����
		     *
		     */
		    uploader.on( 'fileQueued', function( file ) {
		    	//debugger
		    	 file_num ++;
		    	//decodeURIComponent(file.name) ������ļ���
			      if(file_num==5){
			        //$("#picker").remove();
			      }
		    	
			
			    
		    	  //�����彻��
		    	 // parent.file_num = file_num;
		        //  var val = parent.document.getElementById("ifm"+rownum);
		        //  val.height = (parseInt(val.height) + 120);
		         /// if(file_num >= config.fileNumLimit){
		        	//  return false;
		         // }
		          //uploader.upload();
		        //parent.resizeDiv(120);
		    });
		     
		    uploader.on('filesQueued', function(files) {
		    	uploader.getFiles()
		    	if(isFlag){
		    		  uploader.upload();
		    	}else{
		    		isFlag = true;
		    		if(parent.initWebUploader){
			    		parent.initWebUploader(uploadSingleId,rownum,file_upload_type,buttonName,hiddenAttachId,hiddenDisplayId,uploadType,config);
			    	}else{
			    		parent.Headmaster.initWebUploader(uploadSingleId,rownum,file_upload_type,buttonName,hiddenAttachId,hiddenDisplayId,uploadType,config);
			    	}
		    		/*
		    	     var array = uploader.getFiles();//��ȡ�����е��ļ�
			            if (array!="") {
			            	for(var index in array) {
			            		var file = array[index];
			            		  uploader.removeFile(file,true);    
			            	}
			            }*/
		    	}
		         
		        //parent.resizeDiv(120);
		    });
		     
		    
		    /*uploadStart�¼�  ĳ���ļ���ʼ�ϴ�ǰ������һ���ļ�ֻ�ᴥ��һ�Ρ�
		     *@parameter  file {File}File����
		     *���ã��������������ò���formData�Ĳ���
		     */
		    uploader.on('uploadStart', function(file) {
		    	//debugger
		    	uploader.options.formData["fileName"]=file.name;//���ں�̨������ͨ��fileName��������ȡ��Ӧ���ļ������������������������Ӧ��ֵ
		    });
		    
		
		    // �ļ��ϴ������д���������ʵʱ��ʾ��
		    /*uploadProgress�¼� �ϴ������д�����Я���ϴ����ȡ�
		     *@parameter file {File}File����
		     *@parameter percentage {Number}�ϴ�����
		     */
		    uploader.on('uploadProgress', function(file, percentage) {
		        var $li = $( '#'+file.id ),
		            //$percent = $li.find('.progress .progress-bar');
		            $percent = $li.find('.progress .progress-bar');
		
		        // �����ظ�����
		        if (!$percent.length ) { //��������Զ������������ʽ��˼·һ��div���������һ��div����һ��divΪ�ڶ���div���Ķ���
		            $percent = $('<div class="progress">' +
		              			'<div class="progress-bar"></div>' +
		            			'</div>').appendTo( $li ).find('.progress-bar');//
		        }
		
		        $li.find('p.state').text('�ϴ���');
		
		        $percent.css( 'width', percentage * 100 + '%' );//��̬���õڶ���div�ĳ��ȡ�
		        //��̬���õڶ���div�ĳ��ȡ�
		        $percent.html(parseInt(percentage * 100) + '%');
		    });
		    
			 var successFileid="";
			/*uploadSuccess���������ļ��ϴ��ɹ�ʱ������
			 *parameter  file {File}File����
			 *parameter  response {Object}����˷��ص�����
			 */
		    uploader.on('uploadSuccess', function(file,response) {
		    	debugger
	    	   var fileName = file.name;
	    	   var fileSize = file.fileSize;
	    	   //var filePath = file.filePath;
	    	   var attachmentId = response.attachmentId;
	    	   var filePath = response.filePath;
	    	   /*
	    	   if(parent.uploadSuccessCallBack){
	    		   parent.uploadSuccessCallBack(file,response);
	    		   return;
	    	   }*/
	    	   parent.document.getElementById(hiddenAttachId+rownum).value=attachmentId;
	    	 
	    		var attachmentArray =[];
	    		if(uploadType && uploadType!='null' && uploadType!='undefined'){
	    			attachmentArray.push("<img id='imgUpload' style='max-height: 100px;max-width:100px;' onclick='parent.showOriginalImg(this)'   border='0' src=\"<%=basePath%>"+filePath+"\">");
	    		    // ��������ͼ
				       // ���Ϊ��ͼƬ�ļ������Բ��õ��ô˷�����
				       // thumbnailWidth x thumbnailHeight Ϊ 100 x 100
				   
	    		}else{
		    		//attachmentArray.push("<div>");
		    		attachmentArray.push("<a class='chachu'  href=\"<%=basePath%>WorkflowAttachMentDownload?attachmentId="+attachmentId+"\">"+fileName+"</a>");
		    		attachmentArray.push(fileSize);
		    		attachmentArray.push("&nbsp;&nbsp;&nbsp;&nbsp;");
		    		attachmentArray.push("<a class='chachu'  href=\"javascript:void(0);\" onclick=\"parent.Headmaster.deleteReceiveFileAttachment(\'"+attachmentId+"\',this,"+hiddenAttachId+rownum+");\" >ɾ��</a>");
		    		//attachmentArray.push("</div>");
	    		}
	    		//$list.empty();
	    	//	$list.append(attachmentArray.join(""));
	    	    $(parent.document.getElementById(hiddenDisplayId+rownum)).empty();
	    		$(parent.document.getElementById(hiddenDisplayId+rownum)).append(attachmentArray.join(""));
	    		
	    		//$("#imgUpload").fancyZoom({scaleImg: true, closeOnClick: true});
	    		
	    		 /*
	    	      var s = '';
			      s = '<div id="' + file.id + '" class="item" >' +
			          '<h4 class="info" style="display:none;">' + file.name + '</h4>' +
			          '<h3 class="info" >' + decodeURIComponent(file.name) + '</h3>' +
			          '<p class="state">'+ config.queuedTitle + '...  ';
			      if(parent.initWebUploader){s += '<button class="btn btn-default cancel" >ȡ��</button>';}    
			      s += '</p></div>';
			      $list.append(s);*/
				      
		    	 /*
		        $( '#'+file.id ).find('p.state').text('���ϴ�');
		       if(successFileid==""){
		         successFileid = response.id;
		       }else{
		         successFileid+=","+response.id;
		       }  */
		       
		    });
			 uploader.on('uploadFinished', function() {
			      //���ø�����ؼ�
		        // var val = parent.document.getElementById("attachment_id");
		       //  val.value=successFileid;
		         //���ø����巽��
		       //  parent.uploadSuc();
		    });
			/*uploadError���������ļ��ϴ�����ʱ������
			 *@parameter  file {File}File����
			 *@parameter  reason {String}�����code
			 */
		    uploader.on( 'uploadError', function(file,reason) {
		        $( '#'+file.id ).find('p.state').text('�ϴ�����');
		    });
		
			//���ܳɹ�����ʧ�ܣ��ļ��ϴ����ʱ������
			/* uploadComplete�¼�  ���ܳɹ�����ʧ�ܣ��ļ��ϴ����ʱ������
			 * file {File} [��ѡ]File����
			 */
		    uploader.on('uploadComplete', function(file) {
		    	if(parent.initWebUploader){
		    		parent.initWebUploader(uploadSingleId,rownum,file_upload_type,buttonName,hiddenAttachId,hiddenDisplayId,uploadType,config);
		    	}else{
		    		parent.Headmaster.initWebUploader(uploadSingleId,rownum,file_upload_type,buttonName,hiddenAttachId,hiddenDisplayId,uploadType,config);
		    	}
		    	
		    	
		    //	 uploader.removeFile( file );
		    /*
		        var array = uploader.getFiles();//��ȡ�����е��ļ�
	            if (array!="") {
	            	for(var index in array) {
	            		var file = array[index];
	            		  uploader.removeFile(file,true);    
	            	}
	            }*/
		    	isFlag = true;
		    //	createUploader();
		    	//
		        //$( '#'+file.id ).find('.progress').fadeOut();//fadeOut()�����ϴ��Ľ�����
		    });
		    
		    //
		    /*��validate��ͨ��ʱ���������ʹ����¼�����ʽ֪ͨ�����ߡ�ͨ��upload.on('error', handler)���Բ��񵽴������
		     *Ŀǰ�����´�������ض�����������ʹ�����
		     *Q_EXCEED_NUM_LIMIT ��������fileNumLimit�ҳ��Ը�uploader��ӵ��ļ������������ֵʱ���͡�
			 *Q_EXCEED_SIZE_LIMIT ��������Q_EXCEED_SIZE_LIMIT�ҳ��Ը�uploader��ӵ��ļ��ܴ�С�������ֵʱ���͡�
			 *Q_TYPE_DENIED ���ļ����Ͳ�����ʱ��������
			 *F_DUPLICATE ����������duplicate��Ϊtrueʱ���ļ��ظ�
		     *
		     */
		    uploader.on('error', function(type){
		    	//debugger
		     	if ("F_DUPLICATE" == type) {//�ļ��ظ�������������duplicate��Ϊtrueʱ
		     		parent.toastr.error("���ļ���ѡ��");
		     	} else if ("Q_TYPE_DENIED" == type) {//���ļ����Ͳ����㣬��������Ӧ��accept����ʱ
		     		parent.toastr.error("��֧�ָ������ļ�");
		     	} else if ("Q_EXCEED_NUM_LIMIT" == type) {
		     		parent.toastr.error("�����ļ��ϴ�����");
		     	} else if ("Q_EXCEED_SIZE_LIMIT" == type ) {
		     		parent.toastr.error("�ļ�����������ѡ��");
		     	}
		    	isFlag = false;
		    	/*
		        var array = uploader.getFiles();//��ȡ�����е��ļ�
	            if (array!="") {
	            	for(var index in array) {
	            		var file = array[index];
	            		  uploader.removeFile(file,true);    
	            	}
	            }*/
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
		            $btn.text('��ͣ�ϴ�');
		        } else {
		            $btn.text('��ʼ�ϴ�');
		        }
		    });
		
		    
		    $btn.on( 'click', function() {
		        if ( state == 'uploading' ) {
		            uploader.stop();
		        } else {
		            var array = uploader.getFiles();//��ȡ�����е��ļ�
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
		        return false;  //�����Ƕ����form���У�Ϊ�˷�ֹ��ǰ��click�¼��������form���¼����������践��false
		    });
		    
		    $list.on("click" ,".cancel",function(){
		        var $ele = $(this);            
		        var id = $ele.parent().parent().attr("id"); 
		        uploader.removeFile(id,true);           
		        $ele.parent().parent().remove();
		    });
		}
		
		
		</script>   
  <div id="uploader" class="wu-example"> <!--  style="margin-left:-8px;" -->
   <!--��������ļ���Ϣ--> 
   
    <div class="btns" > 
	    <div id="picker">ѡ�񸽼�</div>
	    <!-- style="display:none" --> 
	     <button id="ctlBtn" class="btn btn-default" style="display:none;">��ʼ�ϴ�</button> 
	    <!-- style="display: none;" --> 
    </div>
     <div id="thelist" class="uploader-list" style="width:100%" ></div>  
  </div>   
 </body>
</html>