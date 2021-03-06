
$.ajaxSettings.contentType="application/x-www-form-urlencoded; charset=utf-8";
//===============================ctrl+enter 提交
function isKeyTrigger(e,keyCode){
    var argv = isKeyTrigger.arguments;
    var argc = isKeyTrigger.arguments.length;
    var bCtrl = false;
    if(argc > 2){
        bCtrl = argv[2];
    }
    var bAlt = false;
    if(argc > 3){
        bAlt = argv[3];
    }

    var nav4 = window.Event ? true : false;

    if(typeof e == 'undefined') {
        e = event;
    }

    if( bCtrl && 
        !((typeof e.ctrlKey != 'undefined') ? 
            e.ctrlKey : e.modifiers & Event.CONTROL_MASK > 0)){
        return false;
    }
    if( bAlt && 
        !((typeof e.altKey != 'undefined') ? 
            e.altKey : e.modifiers & Event.ALT_MASK > 0)){
        return false;
    }
    var whichCode = 0;
    if (nav4) whichCode = e.which;
    else if (e.type == "keypress" || e.type == "keydown")
        whichCode = e.keyCode;
    else whichCode = e.button;

    return (whichCode == keyCode);
}

//输入小数(包括整数)，保留1位小数
$.fn.onefloatinput = function() {
    $(this).css("ime-mode", "disabled");
    this.bind("keypress", function(e) {
        if (e.charCode === 0) return true;  //非字符键 for firefox
        var code = (e.keyCode ? e.keyCode : e.which);  //兼容火狐 IE
        if (code >= 48 && code <= 57) {
            var pos = getCurPosition(this);
            var selText = getSelectedText(this);
            var dotPos = this.value.indexOf(".");
            //整数部分2位
            if (this.value.substr(0,dotPos).length >=2) return false;
            if (dotPos > 0 && pos > dotPos) {
            	//小数部分1位
                if (pos > dotPos + 1) return false;
                if (selText.length > 0 || this.value.substr(dotPos + 1).length < 1)
                    return true;
                else
                    return false;
            }
            return true;
        }
        //输入"."
        if (code == 46) {
            var selText = getSelectedText(this);
            if (selText.indexOf(".") > 0) return true; //选中文本包含"."
            else if (/^[0-9]+\.$/.test(this.value + String.fromCharCode(code)))
                return true;
        }
        return false;
    });
    this.bind("blur", function() {
        if (this.value.lastIndexOf(".") >=2 && this.value > 10) {
            this.value = 10;
        }else if (this.value.lastIndexOf(".") <=0 && this.value > 10){
        	this.value = 10;
        }else if (this.value.lastIndexOf(".") >=2 && this.value <= 10){
        	this.value = this.value.substr(0, 2);
        } else if (isNaN(this.value)) {
            this.value = "";
        }
        if (this.value)
            this.value = parseFloat(this.value).toFixed(1);
        $(this).trigger("input");
    });
    this.bind("paste", function() {
        if (window.clipboardData) {
            var s = clipboardData.getData('text');
            if (!isNaN(s)) {
                value = parseFloat(s);
                return true;
            }
        }
        return false;
    });
    this.bind("dragenter", function() {
        return false;
    });
    this.bind("keyup", function() {
    });
    this.bind("propertychange", function(e) {
        if (isNaN(this.value))
            this.value = this.value.replace(/[^0-9\.]/g, "");
    });
    this.bind("input", function(e) {
//        if (isNaN(this.value))
            this.value = this.value.replace(/[^0-9\.]/g, "");
    });
};

//输入小数(包括整数)，保留两位小数
$.fn.decimalinput = function() {
    $(this).css("ime-mode", "disabled");
    this.bind("keypress", function(e) {
        if (e.charCode === 0) return true;  //非字符键 for firefox
        var code = (e.keyCode ? e.keyCode : e.which);  //兼容火狐 IE
        if (code >= 48 && code <= 57) {
            var pos = getCurPosition(this),
            	selText = getSelectedText(this),
            	dotPos = this.value.indexOf("."),
            	intLen = 11;

            //全部最多14位，包含小数点后的数字
            if(this.value.length >= 14){
            	return false;
            }else if(dotPos == -1 && this.value.length >= intLen){//整数部分11位
            	return false;
            }else if(pos == intLen && selText.indexOf('.') == 0){
            	return false;
            }else if(dotPos == intLen && pos < intLen){
            	return false;
            }
            
            if (dotPos > 0 && pos > dotPos) {
            	//小数部分2位
                if (pos > dotPos + 2) return false;
                if (selText.length > 0 || this.value.substr(dotPos + 1).length < 2)
                    return true;
                else
                    return false;
            }
            return true;
        }
        
        //输入"."
        if (code == 46) {
            var selText = getSelectedText(this);
            if (selText.indexOf(".") > -1) return true; //选中文本包含"."
            else if (/^[0-9]+\.$/.test(this.value + String.fromCharCode(code)))
                return true;
        }
        return false;
    });
    this.bind("blur", function() {
        if (this.value.lastIndexOf(".") == (this.value.length - 1)) {
            this.value = this.value.substr(0, this.value.length - 1);
        } else if (isNaN(this.value)) {
            this.value = "";
        }
        if (this.value)
            this.value = parseFloat(this.value).toFixed(2);
        $(this).trigger("input");
    });
    this.bind("paste", function() {
        if (window.clipboardData) {
            var s = clipboardData.getData('text');
            if (!isNaN(s)) {
                value = parseFloat(s);
                return true;
            }
        }
        return false;
    });
    this.bind("dragenter", function() {
        return false;
    });
    this.bind("keyup", function() {
    });
    this.bind("propertychange", function(e) {
        if (isNaN(this.value))
            this.value = this.value.replace(/[^0-9\.]/g, "");
    });
    this.bind("input", function(e) {
        if (isNaN(this.value))
            this.value = this.value.replace(/[^0-9\.]/g, "");
    });
}; 

//输入小数(包括整数)，保留1位小数
$.fn.floatinput = function() {
    $(this).css("ime-mode", "disabled");
    this.bind("keypress", function(e) {
        if (e.charCode === 0) return true;  //非字符键 for firefox
        var code = (e.keyCode ? e.keyCode : e.which);  //兼容火狐 IE
        if (code >= 48 && code <= 57) {
            var pos = getCurPosition(this);
            var selText = getSelectedText(this);
            var dotPos = this.value.indexOf(".");
            //整数部分4位
            if (this.value.substr(0,dotPos).length >=4) return false;
            if (dotPos > 0 && pos > dotPos) {
            	//小数部分1位
                if (pos > dotPos + 1) return false;
                if (selText.length > 0 || this.value.substr(dotPos + 1).length < 1)
                    return true;
                else
                    return false;
            }
            return true;
        }
        //输入"."
        if (code == 46) {
            var selText = getSelectedText(this);
            if (selText.indexOf(".") > 0) return true; //选中文本包含"."
            else if (/^[0-9]+\.$/.test(this.value + String.fromCharCode(code)))
                return true;
        }
        return false;
    });
    this.bind("blur", function() {
        if (this.value.lastIndexOf(".") == (this.value.length - 1)) {
            this.value = this.value.substr(0, this.value.length - 1);
        } else if (isNaN(this.value)) {
            this.value = "";
        }
        if (this.value)
            this.value = parseFloat(this.value).toFixed(1);
        $(this).trigger("input");
    });
    this.bind("paste", function() {
        if (window.clipboardData) {
            var s = clipboardData.getData('text');
            if (!isNaN(s)) {
                value = parseFloat(s);
                return true;
            }
        }
        return false;
    });
    this.bind("dragenter", function() {
        return false;
    });
    this.bind("keyup", function() {
    });
    this.bind("propertychange", function(e) {
        if (isNaN(this.value))
            this.value = this.value.replace(/[^0-9\.]/g, "");
    });
    this.bind("input", function(e) {
        if (isNaN(this.value))
            this.value = this.value.replace(/[^0-9\.]/g, "");
    });
};

//获取当前光标在文本框的位置
function getCurPosition(domObj) {
  var position = 0;
  if (domObj.selectionStart || domObj.selectionStart == '0') {
      position = domObj.selectionStart;
  }
  else if (document.selection) { //for IE
      domObj.focus();
      var currentRange = document.selection.createRange();
      var workRange = currentRange.duplicate();
      domObj.select();
      var allRange = document.selection.createRange();
      while (workRange.compareEndPoints("StartToStart", allRange) > 0) {
          workRange.moveStart("character", -1);
          position++;
      }
      currentRange.select();
  }
  return position;
}
//获取当前文本框选中的文本
function getSelectedText(domObj) {
  if (domObj.selectionStart || domObj.selectionStart == '0') {
      return domObj.value.substring(domObj.selectionStart, domObj.selectionEnd);
  }
  else if (document.selection) { //for IE
      domObj.focus();
      var sel = document.selection.createRange();
      return sel.text;
  }
  else return '';
}

Date.prototype.format = function(format){	//eg: format = "yyyy-MM-dd hh:mm:ss"; 
    var o = {
    	"M+" : this.getMonth() + 1, // month
    	"d+" : this.getDate(), // day
    	"h+" : this.getHours(), // hour
    	"m+" : this.getMinutes(), // minute
    	"s+" : this.getSeconds(), // second
    	"q+" : Math.floor((this.getMonth() + 3) / 3), // quarter
    	"S" : this.getMilliseconds()  
    };
  
    if (/(y+)/.test(format)) {
    	format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));  
    }
    
    for (var k in o) {
        if (new RegExp("(" + k + ")").test(format)) {
            format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
        }
    }
	return format;
};

var BcUtil = {
	config : {},
	optionIndex : ['A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'],
	//停止事件传播
	stop_propagation: function(e){
		if(!e){return;}
		if(e.stopPropagation) {
			e.stopPropagation();
	    }else {
	    	e.cancelBubble = true;
	    }
	},
	//文本计数
	count_char: function(_textarea, _span, _cut, limit){
		var _txta = document.getElementById(_textarea);
		var _left = limit - _txta.value.length;
		if( _cut && _left < 0){
			document.getElementById(_span).innerHTML = 0;
			_txta.value = _txta.value.substring(0, limit);
			return false;
		}
		document.getElementById(_span).innerHTML = _left;
	},
	//top图标到顶端
	scroll_to_top: function(callback){
		if($(document).scrollTop() > 0){
			if(callback){
				$('body,html').animate({	//保证滑动到顶部之后，再触发callback
					scrollTop: 0
				}, 800, callback);
			}else{
				$('body,html').animate({
					scrollTop: 0
				}, 800);
			}	
		}else{							//不再触发scroll事件
			if(callback){
				callback();
			}
		}
		return false;
	},
	//单选按钮单击事件
	radio_click: function(obj){
		var _nm = $(obj).attr("name");
		$(".radio [name="+_nm+"]").attr("checked", false).parent("span").removeClass("checked");
		$(obj).attr("checked", true).parent("span").addClass("checked");
		return _nm;
	},
	//复选框点击事件
	checkbox_onclick: function(obj){
		var _span = $(obj).find("span");
		if(!_span.hasClass("disabled")){
			_span.toggleClass("checked");
			if(_span.hasClass("checked")){
				$(obj).find("input").attr("checked", true).val(1);
			}else{
				$(obj).find("input").attr("checked", false).val(0);
			}
		}
	},
	//下拉选择框事件
	selector_click : function(obj){
		var _o = $(obj).find(".selector_cnt").toggle();
		$(".selector_cnt").not(_o[0]).hide();
	},
	//file input 上传控件
	uploader_change : function(_this, sel){
		var _v = $(_this).val();
		$(sel).text(_v);
	},
	//获取url参数
	get_query_str: function(str){
	    var rs = new RegExp("(^|)"+str+"=([^\:]*)(\:|$)","gi").exec(window.location.href.toString()), tmp;

	    if(tmp=rs){
	        return tmp[2];
	    }
	    return "";
	},
	//生成GUID
	get_new_guid: function(){
		var guid = "";
	    for (var i = 1; i <= 32; i++){
	    	var n = Math.floor(Math.random()*16.0).toString(16);
	    	guid +=   n;
	    	if((i==8)||(i==12)||(i==16)||(i==20))
	    		guid += "-";
    	}
	    return guid;   
	},
	
	//生成随机ID
	generate_tmpid: function(len, let){
		if( !len ) { len = 10; }
		if( !let ) { let = "abcdefghijklmnopqrstuvwxyz0123456789"; }
		var i, word = "";
		for(i=0; i<len; i++) {
			word	+= let.charAt(Math.round(Math.random()*(let.length-1)));
		}
		return word;
	},
	//时间戳转为日期格式(并格式化为yyyy-mm-dd)
	get_date: function(tm){
		return new Date(parseInt(tm) * 1000).format("yyyy-MM-dd");
	},
	//将满足日期格式的字符串转为时间
	get_time: function(dt){
		dt = dt.replace(/-/g, "/");
		return new Date(dt);
	},
	//日期格式转为时间戳
	get_timestamp: function(dt){
		//如将2012-06-21格式的字符串转化为日期格式，请先用“/”替换掉“-”，才能正确转换，否则浏览器不兼容
		dt = dt.replace(/-/g, "/");
		var a = new Date(dt);
		return (a.getTime() / 1000);
	},
	//对时间差进行格式化
	get_dateDiffFormat: function(dt,dt2){
    var minute = 60;
    var hour = minute * 60;
    var day = hour * 24;
    var halfamonth = day * 15;
    var month = day * 30;
    
	var dateTimeStamp = this.get_timestamp(dt);
	//var now = new Date().getTime() / 1000;
	var now = dt2 ? this.get_timestamp(dt2) : new Date().getTime() / 1000;
    var diffValue = now - dateTimeStamp;
		
	var monthC = diffValue / month;
    var weekC = diffValue / (7 * day);
    var dayC = diffValue / day;
    var hourC = diffValue / hour;
    var minC = diffValue / minute;
	if (monthC >= 12) {
       result = parseInt(dt.substr(0,4))+'年'+parseInt(dt.substr(5,2))+'月'+parseInt(dt.substr(8,2))+'日';
	}
    else if (monthC >= 1) {
        result = parseInt(dt.substr(5,2))+'月'+parseInt(dt.substr(8,2))+'日';
    } else if (weekC >= 1) {
        result = parseInt(weekC) + "个星期前";
    } else if (dayC >= 1) {
        result = parseInt(dayC) + "天前";
    } else if (hourC >= 1) {
        result = parseInt(hourC) + "个小时前";
    } else if (minC >= 1) {
        result = parseInt(minC) + "分钟前";
    } else result = "刚刚";

    return result;
	},
	//对年月时间格式时间比较时间差
	get_yearMonthDiffFormat: function(dt,dt2){
	    var minute = 60;
	    var hour = minute * 60;
	    var day = hour * 24;
	    var halfamonth = day * 15;
	    var month = day * 30;
	    var year = month * 12;
	    
		var dateTimeStamp = this.get_timestamp(dt);
		//var now = new Date().getTime() / 1000;
		var now = this.get_timestamp(dt2) ;
	    var diffValue = now - dateTimeStamp;
			
		var yearC = diffValue / year;
		var monthC = diffValue / month;
	    var weekC = diffValue / (7 * day);
	    var dayC = diffValue / day;
	    var hourC = diffValue / hour;
	    var minC = diffValue / minute;
	    return {"year":parseInt(yearC),"month":parseInt(monthC)};
	},
	toggle : function(id){
		$("#"+id).toggle();
	},
	//texarea 自动高度
	textarea_autoheight: function(textarea)
	{
		var mn	= 40;
		var mx	= 390;
		if( !textarea || !textarea.nodeName || textarea.nodeName!="TEXTAREA" ) {
			return;
		}
		if( !textarea.id ) {
			textarea.id	= "tmptxtarea_"+Math.round(Math.random()*10000);
		}
		dv	= d.getElementById("tmptxtdv_"+textarea.id);
		if( !dv ) {
			var dv	= d.createElement("DIV");
			dv.id		= "tmptxtdv_"+textarea.id;
			dv.className	= textarea.className;
			dv.style.width		= textarea.clientWidth + "px";
			dv.style.overflow		= "auto";
			//dv.style.whiteSpace	= "pre-wrap";
			dv.style.visibility	= "hidden";
			dv.style.display		= "block";
			dv.style.position		= "absolute";
			textarea.parentNode.appendChild(dv);
		}
		dv.innerHTML	= "";
		dv.appendChild(d.createTextNode(textarea.value+"\n"));
		var	h = parseInt(dv.clientHeight, 10);
		if( isNaN(h) ) {
			return;
		}
		if( h <= mn ) {
			h	= mn;
			textarea.style.overflow	= "hidden";
		}
		else if( h >= mx ) {
			h	= mx;
			textarea.style.overflow	= "auto";
		}
		else {
			textarea.style.overflow	= "hidden";
		}
		textarea.style.height	= h + "px";
	},
	
	//截取字符串，1个汉字/大写字母==2个小写英文
	cutstr : function(str, len, dot)
	{
		if(!str || !len) { return ''; }
		var a = 0;
		var i = 0;
		var temp = '';
		for(i=0; i<str.length; i++){
			if(str.charCodeAt(i)>255){
				a+=2;
			}else{
				a++;
			}
			if(a > len){
				if(typeof(dot) != "undefined" && dot==true){
					return temp + "...";
				}else{
					return temp;
				}
			}
			temp += str.charAt(i);
		}
		return str;
	},
	
	//清除html标签
	htmlTotext : function(str) {
		return $('<p>'+str+'</p>').text();
	},
	
	request : function(relurl, siteurl)
	{
		var url = '';
		if(!siteurl){
			url = bc_cfg.siteurl+relurl;
		}else{
			url = siteurl+relurl;
		}
		window.location.href = url;
	},
	
	/** 
	*textarea文本框输入字数检测 
	*textareaId：textarea的dom标识 
	*maxLen：要求的最大字节长度 
	*/  
	chkTextareaLen : function (textareaId,counterId,maxLen) {  
	    try{  
	        var textareaValue = document.getElementById(textareaId).value;  
	        var curLen = 0,substrLen = 0;  
	          
	        for (var i=0; i<textareaValue.length; i++) {    
	            if (textareaValue.charCodeAt(i)>127 || textareaValue.charCodeAt(i)==94) {    
	                curLen += 2;    
	            } else {  
	                curLen ++;    
	            }   
	      
	            if(curLen > maxLen){  
	                substrLen = i;  
	                break;  
	            }  
	        }  
	          
	        if(curLen > maxLen) {  
	            if(substrLen == 0) substrLen = maxLen;  
	            document.getElementById(textareaId).value = textareaValue.substring(0,substrLen);  
	            alert("文本长度不能大于"+maxLen+"个字节(中文占2个字节)");   
	        }else{  
	            document.getElementById(counterId).innerHTML = "还可以录入"+(maxLen - curLen)+"个字符";  
	        }  
	    }catch(e){}  
	},
	
	clear_if_zero : function(_this){
		var _v = $(_this).val();
		if(!parseInt(_v)){
			$(_this).val('');
		}
	},
	
	retrieve_if_zero : function(_this, _val){
		var _v = $(_this).val();
		if(!parseInt(_v)){
			$(_this).val(_val);
		}
	},
	
	hasPlugin: function (name){
	    name = name.toLowerCase();
	    for (var i=0; i < navigator.plugins.length; i++){
	        if (navigator.plugins[i].description.toLowerCase().indexOf(name) > -1){
	            return true;
	        }
	    }
	    return false;
	},        

	//plugin detection for IE
	hasIEPlugin: function (name){
	    try {
	        new ActiveXObject(name);
	        return true;
	    } catch (ex){
	        return false;
	    }
	},

	//detect flash for all browsers
	hasFlash: function (){
	    var result = BcUtil.hasPlugin("Flash");
	    if (!result){
	        result = BcUtil.hasIEPlugin("ShockwaveFlash.ShockwaveFlash");
	    }
	    return result;
	},
	
	/**
	 * 创建平台ajax请求链接
	 * @example
	 * data= {
	 *	user: {
	 *		first_name: "Thomas",
	 *		last_name: "Mazur",
	 *		account: {
	 *			status: "active"
	 *		}
	 *	}
	 * }
	 * ("<p>Hello {user.first_name} {user.last_name}! Your account is <strong>{user.account.status}</strong></p>", data)
	 * @name formatTplByJSON
	 * @memberOf BcUtil
	 * @function
	 * @param {String} s template
	 * @param {Object} data data
	 */
	formatTplByJSON: function (template, data){
	  return template.replace(/\{([\w\.]*)\}/g, function(str, key) {
	    var keys = key.split("."), v = data[keys.shift()];
	    for (var i = 0, l = keys.length; i < l; i++) v = v[keys[i]];
	    return (typeof v !== "undefined" && v !== null) ? v : "";
	  });
	},
	//控制台输出jsons对象
	printJSON: function (json){
	  for(i in json) console.log(i+':'+json[i]);
	},
	
	//将指定form表单中的input标签中的值拼装成JSON对象
	crForm : function(form) {
      var fElements = form.elements || document.forms[form].elements,
          hasSubmit = false,
          encoder = encodeURIComponent,
          name,
          data = {},
          buf = {},
          type,
          hasValue;
			
      $.each(fElements, function(i){
      	  element = fElements[i];
          name = element.name;
          type = element.type;

          if (!element.disabled && name) {
              if (/select-(one|multiple)/i.test(type)) {
                  $.each(element.options, function(j){
                  	  opt = element.options[j];
                      if (opt.selected) {
                      	hasValue = opt.hasAttribute ? opt.hasAttribute('value') : opt.getAttributeNode('value').specified;
                      	var val=hasValue ? opt.value : opt.text;
                      	data[name]=name!=buf[name] ? val:[].concat(data[name]).concat(val||'');
                      	buf[name]=name;
                      }
                  });
              } else if (!(/file|undefined|reset|button/i.test(type))) {
                  if (!(/radio|checkbox/i.test(type) && !element.checked) && !(type == 'submit' && hasSubmit)) {
                      data[name]=name!=buf[name] ? element.value:[].concat(data[name]).concat(element.value||'');
                      buf[name]=name;
                      hasSubmit = /submit/i.test(type);
                  }
              }
          }
      });
      return data;
  },	
	
	/**
	 * 创建平台ajax请求链接
	 * @example
	 * 'p.tojson?sysName='+s+'&oprID='+o+'&actions='+a
	 * @name buildJrafUrl
	 * @memberOf JrafUtil
	 * @function
	 * @param {String} s sysName
	 * @param {String} o oprID
	 * @param {String} a actions
	 */
	buildJrafUrl:function(s,o,a,f)
	{
		if(f) return 'P.tojson?';
		return 'P.tojson?sysName='+s+'&oprID='+o+'&actions='+a;
	},
	
	//paramData('c_identitytype','Y','Identity_Type',[['','全部']],[['style','font-size:11px;width:85px'],['onChange','changeVal()']])
	paramData : function (s, v, e, b, c){
	  var a;
	  if (_sYs_paRm_liSt) {
	      var d = _sYs_paRm_liSt[e];
	      if (d) {
	          a = d.items ? d.items: []
	      }
	  }
	  if(b) a = b.concat(a);
	  var s = this.genSelect(s,a,c,v)
	  return s
	},
	genSelect : function (selectName, opts, params, val){
	    var s = "<select id='" + selectName + "' name='" + selectName + "'";
	    if(params){
	    	for (var i = 0; i < params.length; i++) {
	        var opt = params[i];
	        s += " " + opt[0] + "='" + opt[1]+"'";
	    	}
	    }
	    s += " >";
	    for (var i = 0; i < opts.length; i++) {
	        var opt = opts[i];
	        s += "<option value='" + opt[0] + "'";
	        if(opt[0] == val) s += " selected";
	        s += ">" + opt[1] + "</option>";
	    }
	    s += "</select>";
	    return s;
	},
	//paramVal('Identity_Type','00','无')
	paramVal : function (e, v, b){
	  var a;
	  if (_sYs_paRm_liSt) {
	      var d = _sYs_paRm_liSt[e];
	      if (d) {
	          a = d.items ? d.items: []
	      }
	  }  
	  var s="";
	  if(a){
		  for (var i = 0; i < a.length; i++) {
		      var opt = a[i];
		      if(opt[0] == v) s = opt[1];
		  }
		}
	  if(b) s = (s=="")?b:s; 
	  return s;
	}	
	
};

/**
 * 平台Ajax交易请求
 * @class BcRequest
 * @param {String} s
 * <li><b>s</b> :String sysName</li>
 * @param {String} o
 * <li><b>o</b> :String oprID</li>
 * @param {String} a
 * <li><b>a</b> :String action</li>
 * @param {String} c 可选
 */
function BcRequest(s,o,a,c)
{
	var j_r=this;
	this.sysName=s,this.oprID=o,this.actions=a,this.config=c||{valid:false};
	this.j_url= BcUtil.buildJrafUrl(s,o,a,true);
	this.j_form,this.j_succ_fn,this.j_fail_fn,this.j_valid_fn,this.j_start_fn;
	this.j_method = 'POST';
	this.j_extraParams={sysName:s,oprID:o,actions:a};

	/**
	 * 设置Form表单,用于提交表单数据
	 * @function
	 * @param {Mixed} form
	 */
	this.setForm=function(form,valid){
		j_r.j_form = form;
		if(typeof(valid)!='undefined' && valid==false) j_r.config.valid = true;
	};
	/**
	 * 设置Ajax提交方式
	 * @function
	 * @param {String} m
 	 * <li><b>m</b> :String POST/GET</li>
	 */
	this.setMethod=function(m){
		j_r.j_method=m;
	};
	/**
	 * 设置Ajax请求成功(非交易成功)需执行的方法
	 * @function
	 * @param {Function} sf
	 */
	this.setSuccFn=function(sf){
		if(sf && sf!='function(){}')
			j_r.j_succ_fn=sf;
	};
	/**
	 * 设置Ajax请求失败(非交易失败)需执行的方法
	 * @function
	 * @param {Function} ff
	 */
	this.setFailFn=function(ff){
		j_r.j_fail_fn=ff;
	};
	/**
	 * 设置合法性校验方法
	 * @function
	 * @param {Function} vf
 	 * <li><b>vf</b> :Function function(){}</li>
	 */
	this.setValidFn=function(vf){
		j_r.j_valid_fn=vf;
	};
	/**
	 * 设置提交前方法(比如显示正在加载图片层)
	 * @function
	 * @param {Function} vf
 	 * <li><b>vf</b> :Function function(){}</li>
	 */
	this.setStartFn=function(vf){
		j_r.j_start_fn=vf;
	};
	/**
	 * 设置请求额外参数
	 * @function
	 * @param {Object} ep
 	 * <li><b>ep</b> :Object {name1:v1,name2:v2,...}</li>
	 */
	this.setExtraPs=function(ep){
		$.extend(j_r.j_extraParams,ep||{});
	};
	/**
	 * 默认校验方法
	 * @function
	 * @private
	 */
	this.isValid=function(){
		BcUtil.config.bcRequest = j_r;
		if(!j_r.j_form || j_r.config.valid) return true;
		$.validator.setDefaults({
			submitHandler: function() {
				BcUtil.config.bcRequest.config.valid = true;
				BcUtil.config.bcRequest.postData();
			}
		});
		if(typeof j_r.j_form == 'string') {
		  $("#"+j_r.j_form).validate();
		  $("#"+j_r.j_form).submit();
		} else {
		 	$(j_r.j_form).validate();
		 	$(j_r.j_form).submit();
		}
		return false;
	};
	/**
	 * 交易提交前处理
	 * @function
	 * @private
	 */
	this.doStart=function(){
    if(j_r.j_start_fn)
		  j_r.j_start_fn.call();
	};
	/**
	 * 交易成功处理
	 * @function
	 * @private
	 */
	this.doSuccess=function(a,b){//BcUtil.printJSON(a);
    if(a) {
    	if(j_r.j_succ_fn && a.result == '0'){
    		j_r.j_succ_fn(a,b);
    		return;
    	}
    }
    j_r.doFailure(a,b);
	};
	/**
	 * 交易失败处理
	 * @function
	 * @private
	 */
	this.doFailure=function(a, b){
		if(a){
			if(a.result == '-3'){
				zebra_alert(a.hint_description);
				return;
			}
			if(j_r.j_fail_fn){
	  		j_r.j_fail_fn(a,b);
	  		return;
	    }
	    if(!a.error_description || a.error_description=='') zebra_alert("交易处理失败!");
			else alert(a.error_description);
		} else {
			zebra_alert("交易处理失败!");
		}
	};
	/**
	 * 执行交易请求
	 * @function
	 */
	this.postData=function(){
		var validFn=j_r.j_valid_fn||j_r.isValid;
		if(!validFn.call())
			return;
    var formParams = j_r.j_form ? BcUtil.crForm(j_r.j_form) : {};
    var param = $.extend({},j_r.j_extraParams,formParams,{t:Math.round(Math.random()*1000)});
    j_r.doStart();
		$.post(j_r.j_url,
			$.param(param||{},true),
			j_r.doSuccess);
	};
};
/**
 * 动态设置session
 * @class BcMOOCSetSession
 * @param {String} s
 * <li><b>s</b> :String subjectid</li>
 * @param {String} g
 * <li><b>g</b> :String gradecode</li>
 * @param {String} f
 * <li><b>f</b> :String foldercode</li>
 */
function BcMOOCSetSession(s,g,f)
{
	$.get("/mooc/public/session.jsp",{set_session:true,subjectid:s,gradecode:g,foldercode:f});
}

//保留二位小数，不足二位补零
Number.prototype.toFixed = function(len) {
    var tempNum = 0;
    var s, temp;
    var s1 = this + "";
    var start = s1.indexOf(".");
    if (s1.substr(start + len + 1, 1) >= 5) {
        tempNum = 1;
    }
    var temp = Math.pow(10, len);
    s = Math.floor(this * temp) + tempNum;
    return s / temp;
}