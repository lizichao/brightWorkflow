!function($,win,undef){function setCenter(obj,time){var left=($(window).width()-obj.outerWidth())/2,top=($(window).height()-obj.outerHeight())/2,top=(document.documentElement.scrollTop?document.documentElement.scrollTop:document.body.scrollTop)+(top>0?top:0);obj.css({left:left}).animate({top:top},{duration:time,queue:!1})}function creatMsgbox(){return 0!==$("#Validform_msg").length?!1:(msgobj=$('<div id="Validform_msg"><div class="Validform_title">'+tipmsg.tit+'<a class="Validform_close" href="javascript:void(0);">&chi;</a></div><div class="Validform_info"></div><div class="iframe"><iframe frameborder="0" scrolling="no" height="100%" width="100%"></iframe></div></div>').appendTo("body"),msgobj.find("a.Validform_close").click(function(){return msgobj.hide(),msghidden=!0,errorobj&&errorobj.focus().addClass("Validform_error"),!1}).focus(function(){this.blur()}),$(window).bind("scroll resize",function(){!msghidden&&setCenter(msgobj,400)}),void 0)}var errorobj=null,msgobj=null,msghidden=!0,tipmsg={tit:"提示信息",w:{"*":"不能为空！","*6-16":"请填写6到16位任意字符！",n:"请填写数字！","n6-16":"请填写6到16位数字！",s:"不能输入特殊字符！","s6-18":"请填写6到18位字符！",p:"请填写邮政编码！",m:"请填写手机号码！",e:"邮箱地址格式不对！",url:"请填写网址！"},def:"请填写正确信息！",undef:"datatype未定义！",reck:"两次输入的内容不一致！",r:"&nbsp",c:"正在检测信息…",s:"请{填写|选择}{0|信息}！",v:"所填信息没有经过验证，请稍后…",p:"正在提交数据…"};$.Tipmsg=tipmsg;var Validform=function(forms,settings,inited){var settings=$.extend({},Validform.defaults,settings);settings.datatype&&$.extend(Validform.util.dataType,settings.datatype);var brothers=this;return brothers.tipmsg={w:{}},brothers.forms=forms,brothers.objects=[],inited===!0?!1:(forms.each(function(){if("inited"==this.validform_inited)return!0;this.validform_inited="inited";var curform=this;curform.settings=$.extend({},settings);var $this=$(curform);curform.validform_status="normal",$this.data("tipmsg",brothers.tipmsg),$this.delegate("[datatype]","blur",function(){var subpost=arguments[1];Validform.util.check.call(this,$this,subpost)}),$this.delegate(":text","keypress",function(event){13==event.keyCode&&0==$this.find(":submit").length&&$this.submit()}),Validform.util.enhance.call($this,curform.settings.tiptype,curform.settings.usePlugin,curform.settings.tipSweep),curform.settings.btnSubmit&&$this.find(curform.settings.btnSubmit).bind("click",function(){return $this.trigger("submit"),!1}),$this.submit(function(){var subflag=Validform.util.submitForm.call($this,curform.settings);return subflag===undef&&(subflag=!0),subflag}),$this.find("[type='reset']").add($this.find(curform.settings.btnReset)).bind("click",function(){Validform.util.resetForm.call($this)})}),(1==settings.tiptype||(2==settings.tiptype||3==settings.tiptype)&&settings.ajaxPost)&&creatMsgbox(),void 0)};Validform.defaults={tiptype:1,tipSweep:!1,showAllError:!1,postonce:!1,ajaxPost:!1},Validform.util={dataType:{"*":/[\w\W]+/,"*6-16":/^[\w\W]{6,16}$/,n:/^\d+$/,"n6-16":/^\d{6,16}$/,s:/^[\u4E00-\u9FA5\uf900-\ufa2d\w\.\s]+$/,"s6-18":/^[\u4E00-\u9FA5\uf900-\ufa2d\w\.\s]{6,18}$/,p:/^[0-9]{6}$/,m:/^13[0-9]{9}$|14[0-9]{9}|15[0-9]{9}$|17[0-9]{9}$|18[0-9]{9}$/,e:/^\w+([-+.']\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/,url:/^(\w+:\/\/)?\w+(\.\w+)+.*$/},toString:Object.prototype.toString,isEmpty:function(val){return""===val||val===$.trim(this.attr("tip"))},getValue:function(obj){var inputval,curform=this;return obj.is(":radio")?(inputval=curform.find(":radio[name='"+obj.attr("name")+"']:checked").val(),inputval=inputval===undef?"":inputval):obj.is(":checkbox")?(inputval="",curform.find(":checkbox[name='"+obj.attr("name")+"']:checked").each(function(){inputval+=$(this).val()+","}),inputval=inputval===undef?"":inputval):inputval=obj.val(),inputval=$.trim(inputval),Validform.util.isEmpty.call(obj,inputval)?"":inputval},enhance:function(tiptype,usePlugin,tipSweep,addRule){var curform=this;curform.find("[datatype]").each(function(){2==tiptype?0==$(this).parent().next().find(".Validform_checktip").length&&($(this).parent().next().append("<span class='Validform_checktip' />"),$(this).siblings(".Validform_checktip").remove()):(3==tiptype||4==tiptype)&&0==$(this).siblings(".Validform_checktip").length&&($(this).parent().append("<span class='Validform_checktip' />"),$(this).parent().next().find(".Validform_checktip").remove())}),curform.find("input[recheck]").each(function(){if("inited"==this.validform_inited)return!0;this.validform_inited="inited";var _this=$(this),recheckinput=curform.find("input[name='"+$(this).attr("recheck")+"']");recheckinput.bind("keyup",function(){if(recheckinput.val()==_this.val()&&""!=recheckinput.val()){if(recheckinput.attr("tip")&&recheckinput.attr("tip")==recheckinput.val())return!1;_this.trigger("blur")}}).bind("blur",function(){if(recheckinput.val()!=_this.val()&&""!=_this.val()){if(_this.attr("tip")&&_this.attr("tip")==_this.val())return!1;_this.trigger("blur")}})}),curform.find("[tip]").each(function(){if("inited"==this.validform_inited)return!0;this.validform_inited="inited";var defaultvalue=$(this).attr("tip"),altercss=$(this).attr("altercss");$(this).focus(function(){$(this).val()==defaultvalue&&($(this).val(""),altercss&&$(this).removeClass(altercss))}).blur(function(){""===$.trim($(this).val())&&($(this).val(defaultvalue),altercss&&$(this).addClass(altercss))})}),curform.find(":checkbox[datatype],:radio[datatype]").each(function(){if("inited"==this.validform_inited)return!0;this.validform_inited="inited";var _this=$(this),name=_this.attr("name");curform.find("[name='"+name+"']").filter(":checkbox,:radio").bind("click",function(){setTimeout(function(){_this.trigger("blur")},0)})}),curform.find("select[datatype][multiple]").bind("click",function(){var _this=$(this);setTimeout(function(){_this.trigger("blur")},0)}),Validform.util.usePlugin.call(curform,usePlugin,tiptype,tipSweep,addRule)},usePlugin:function(plugin,tiptype,tipSweep,addRule){var curform=this,plugin=plugin||{};if(curform.find("input[plugin='swfupload']").length&&"undefined"!=typeof swfuploadhandler){var custom={custom_settings:{form:curform,showmsg:function(msg,type){Validform.util.showmsg.call(curform,msg,tiptype,{obj:curform.find("input[plugin='swfupload']"),type:type,sweep:tipSweep})}}};custom=$.extend(!0,{},plugin.swfupload,custom),curform.find("input[plugin='swfupload']").each(function(n){return"inited"==this.validform_inited?!0:(this.validform_inited="inited",$(this).val(""),swfuploadhandler.init(custom,n),void 0)})}if(curform.find("input[plugin='datepicker']").length&&$.fn.datePicker&&(plugin.datepicker=plugin.datepicker||{},plugin.datepicker.format&&(Date.format=plugin.datepicker.format,delete plugin.datepicker.format),plugin.datepicker.firstDayOfWeek&&(Date.firstDayOfWeek=plugin.datepicker.firstDayOfWeek,delete plugin.datepicker.firstDayOfWeek),curform.find("input[plugin='datepicker']").each(function(){return"inited"==this.validform_inited?!0:(this.validform_inited="inited",plugin.datepicker.callback&&$(this).bind("dateSelected",function(){var d=new Date($.event._dpCache[this._dpId].getSelected()[0]).asString(Date.format);plugin.datepicker.callback(d,this)}),$(this).datePicker(plugin.datepicker),void 0)})),curform.find("input[plugin*='passwordStrength']").length&&$.fn.passwordStrength&&(plugin.passwordstrength=plugin.passwordstrength||{},plugin.passwordstrength.showmsg=function(obj,msg,type){Validform.util.showmsg.call(curform,msg,tiptype,{obj:obj,type:type,sweep:tipSweep})},curform.find("input[plugin='passwordStrength']").each(function(){return"inited"==this.validform_inited?!0:(this.validform_inited="inited",$(this).passwordStrength(plugin.passwordstrength),void 0)})),"addRule"!=addRule&&plugin.jqtransform&&$.fn.jqTransSelect){if("true"==curform[0].jqTransSelected)return;curform[0].jqTransSelected="true";var jqTransformHideSelect=function(oTarget){var ulVisible=$(".jqTransformSelectWrapper ul:visible");ulVisible.each(function(){var oSelect=$(this).parents(".jqTransformSelectWrapper:first").find("select").get(0);oTarget&&oSelect.oLabel&&oSelect.oLabel.get(0)==oTarget.get(0)||$(this).hide()})},jqTransformCheckExternalClick=function(event){0===$(event.target).parents(".jqTransformSelectWrapper").length&&jqTransformHideSelect($(event.target))},jqTransformAddDocumentListener=function(){$(document).mousedown(jqTransformCheckExternalClick)};plugin.jqtransform.selector?(curform.find(plugin.jqtransform.selector).filter('input:submit, input:reset, input[type="button"]').jqTransInputButton(),curform.find(plugin.jqtransform.selector).filter("input:text, input:password").jqTransInputText(),curform.find(plugin.jqtransform.selector).filter("input:checkbox").jqTransCheckBox(),curform.find(plugin.jqtransform.selector).filter("input:radio").jqTransRadio(),curform.find(plugin.jqtransform.selector).filter("textarea").jqTransTextarea(),curform.find(plugin.jqtransform.selector).filter("select").length>0&&(curform.find(plugin.jqtransform.selector).filter("select").jqTransSelect(),jqTransformAddDocumentListener())):curform.jqTransform(),curform.find(".jqTransformSelectWrapper").find("li a").click(function(){$(this).parents(".jqTransformSelectWrapper").find("select").trigger("blur")})}},getNullmsg:function(curform){var nullmsg,obj=this,reg=/[\u4E00-\u9FA5\uf900-\ufa2da-zA-Z\s]+/g,label=curform[0].settings.label||".Validform_label";if(label=obj.siblings(label).eq(0).text()||obj.siblings().find(label).eq(0).text()||obj.parent().siblings(label).eq(0).text()||obj.parent().siblings().find(label).eq(0).text(),label=label.replace(/\s(?![a-zA-Z])/g,"").match(reg),label=label?label.join(""):[""],reg=/\{(.+)\|(.+)\}/,nullmsg=curform.data("tipmsg").s||tipmsg.s,""!=label){if(nullmsg=nullmsg.replace(/\{0\|(.+)\}/,label),obj.attr("recheck"))return nullmsg=nullmsg.replace(/\{(.+)\}/,""),obj.attr("nullmsg",nullmsg),nullmsg}else nullmsg=obj.is(":checkbox,:radio,select")?nullmsg.replace(/\{0\|(.+)\}/,""):nullmsg.replace(/\{0\|(.+)\}/,"$1");return nullmsg=obj.is(":checkbox,:radio,select")?nullmsg.replace(reg,"$2"):nullmsg.replace(reg,"$1"),obj.attr("nullmsg",nullmsg),nullmsg},getErrormsg:function(curform,datatype,recheck){var str,regxp=/^(.+?)((\d+)-(\d+))?$/,regxp2=/^(.+?)(\d+)-(\d+)$/,regxp3=/(.*?)\d+(.+?)\d+(.*)/,mac=datatype.match(regxp);if("recheck"==recheck)return str=curform.data("tipmsg").reck||tipmsg.reck;var tipmsg_w_ex=$.extend({},tipmsg.w,curform.data("tipmsg").w);if(mac[0]in tipmsg_w_ex)return curform.data("tipmsg").w[mac[0]]||tipmsg.w[mac[0]];for(var name in tipmsg_w_ex)if(-1!=name.indexOf(mac[1])&&regxp2.test(name))return str=(curform.data("tipmsg").w[name]||tipmsg.w[name]).replace(regxp3,"$1"+mac[3]+"$2"+mac[4]+"$3"),curform.data("tipmsg").w[mac[0]]=str,str;return curform.data("tipmsg").def||tipmsg.def},_regcheck:function(datatype,gets,obj,curform){var curform=curform,info=null,passed=!1,reg=/\/.+\//g,regex=/^(.+?)(\d+)-(\d+)$/,type=3;if(reg.test(datatype)){var regstr=datatype.match(reg)[0].slice(1,-1),param=datatype.replace(reg,""),rexp=RegExp(regstr,param);passed=rexp.test(gets)}else if("[object Function]"==Validform.util.toString.call(Validform.util.dataType[datatype]))passed=Validform.util.dataType[datatype](gets,obj,curform,Validform.util.dataType),passed===!0||passed===undef?passed=!0:(info=passed,passed=!1);else{if(!(datatype in Validform.util.dataType)){var temp,mac=datatype.match(regex);if(mac){for(var name in Validform.util.dataType)if(temp=name.match(regex),temp&&mac[1]===temp[1]){var str=Validform.util.dataType[name].toString(),param=str.match(/\/[mgi]*/g)[1].replace("/",""),regxp=new RegExp("\\{"+temp[2]+","+temp[3]+"\\}","g");str=str.replace(/\/[mgi]*/g,"/").replace(regxp,"{"+mac[2]+","+mac[3]+"}").replace(/^\//,"").replace(/\/$/,""),Validform.util.dataType[datatype]=new RegExp(str,param);break}}else passed=!1,info=curform.data("tipmsg").undef||tipmsg.undef}"[object RegExp]"==Validform.util.toString.call(Validform.util.dataType[datatype])&&(passed=Validform.util.dataType[datatype].test(gets))}if(passed){if(type=2,info=obj.attr("sucmsg")||curform.data("tipmsg").r||tipmsg.r,obj.attr("recheck")){var theother=curform.find("input[name='"+obj.attr("recheck")+"']:first");gets!=theother.val()&&(passed=!1,type=3,info=obj.attr("errormsg")||Validform.util.getErrormsg.call(obj,curform,datatype,"recheck"))}}else info=info||obj.attr("errormsg")||Validform.util.getErrormsg.call(obj,curform,datatype),Validform.util.isEmpty.call(obj,gets)&&(info=obj.attr("nullmsg")||Validform.util.getNullmsg.call(obj,curform));return{passed:passed,type:type,info:info}},regcheck:function(datatype,gets,obj){var curform=this,info=null;if("ignore"===obj.attr("ignore")&&Validform.util.isEmpty.call(obj,gets))return obj.data("cked")&&(info=""),{passed:!0,type:4,info:info};obj.data("cked","cked");for(var res,dtype=Validform.util.parseDatatype(datatype),eithor=0;eithor<dtype.length;eithor++){for(var dtp=0;dtp<dtype[eithor].length&&(res=Validform.util._regcheck(dtype[eithor][dtp],gets,obj,curform),res.passed);dtp++);if(res.passed)break}return res},parseDatatype:function(datatype){var reg=/\/.+?\/[mgi]*(?=(,|$|\||\s))|[\w\*-]+/g,dtype=datatype.match(reg),sepor=datatype.replace(reg,"").replace(/\s*/g,"").split(""),arr=[],m=0;arr[0]=[],arr[0].push(dtype[0]);for(var n=0;n<sepor.length;n++)"|"==sepor[n]&&(m++,arr[m]=[]),arr[m].push(dtype[n+1]);return arr},showmsg:function(msg,type,o,triggered){if("byajax"!=triggered&&msg!=undef&&("bycheck"!=triggered||!o.sweep||(!o.obj||o.obj.is(".Validform_error"))&&"function"!=typeof type)){if($.extend(o,{curform:this}),"function"==typeof type)return type(msg,o,Validform.util.cssctl),void 0;(1==type||"byajax"==triggered&&4!=type)&&msgobj.find(".Validform_info").html(msg),(1==type&&"bycheck"!=triggered&&2!=o.type||"byajax"==triggered&&4!=type)&&(msghidden=!1,msgobj.find(".iframe").css("height",msgobj.outerHeight()),msgobj.show(),setCenter(msgobj,100)),2==type&&o.obj&&(o.obj.parent().next().find(".Validform_checktip").html(msg),Validform.util.cssctl(o.obj.parent().next().find(".Validform_checktip"),o.type)),3!=type&&4!=type||!o.obj||(o.obj.siblings(".Validform_checktip").html(msg),Validform.util.cssctl(o.obj.siblings(".Validform_checktip"),o.type))}},cssctl:function(obj,status){switch(status){case 1:obj.removeClass("Validform_right Validform_wrong").addClass("Validform_checktip Validform_loading");break;case 2:obj.removeClass("Validform_wrong Validform_loading").addClass("Validform_checktip Validform_right");break;case 4:obj.removeClass("Validform_right Validform_wrong Validform_loading").addClass("Validform_checktip");break;default:obj.removeClass("Validform_right Validform_loading").addClass("Validform_checktip Validform_wrong")}},check:function(curform,subpost,bool){var settings=curform[0].settings,subpost=subpost||"",inputval=Validform.util.getValue.call(curform,$(this));if(settings.ignoreHidden&&$(this).is(":hidden")||"dataIgnore"===$(this).data("dataIgnore"))return!0;if(settings.dragonfly&&!$(this).data("cked")&&Validform.util.isEmpty.call($(this),inputval)&&"ignore"!=$(this).attr("ignore"))return!1;var flag=Validform.util.regcheck.call(curform,$(this).attr("datatype"),inputval,$(this));if(inputval==this.validform_lastval&&!$(this).attr("recheck")&&""==subpost)return flag.passed?!0:!1;this.validform_lastval=inputval;var _this;if(errorobj=_this=$(this),!flag.passed)return Validform.util.abort.call(_this[0]),bool||(Validform.util.showmsg.call(curform,flag.info,settings.tiptype,{obj:$(this),type:flag.type,sweep:settings.tipSweep},"bycheck"),!settings.tipSweep&&_this.addClass("Validform_error")),!1;var ajaxurl=$(this).attr("ajaxurl");if(ajaxurl&&!Validform.util.isEmpty.call($(this),inputval)&&!bool){var inputobj=$(this);if(inputobj[0].validform_subpost="postform"==subpost?"postform":"","posting"===inputobj[0].validform_valid&&inputval==inputobj[0].validform_ckvalue)return"ajax";inputobj[0].validform_valid="posting",inputobj[0].validform_ckvalue=inputval,Validform.util.showmsg.call(curform,curform.data("tipmsg").c||tipmsg.c,settings.tiptype,{obj:inputobj,type:1,sweep:settings.tipSweep},"bycheck"),Validform.util.abort.call(_this[0]);var ajaxsetup=$.extend(!0,{},settings.ajaxurl||{}),localconfig={type:"POST",cache:!1,url:ajaxurl,data:"param="+encodeURIComponent(inputval)+"&name="+encodeURIComponent($(this).attr("name")),success:function(data){"y"===$.trim(data.status)?(inputobj[0].validform_valid="true",data.info&&inputobj.attr("sucmsg",data.info),Validform.util.showmsg.call(curform,inputobj.attr("sucmsg")||curform.data("tipmsg").r||tipmsg.r,settings.tiptype,{obj:inputobj,type:2,sweep:settings.tipSweep},"bycheck"),_this.removeClass("Validform_error"),errorobj=null,"postform"==inputobj[0].validform_subpost&&curform.trigger("submit")):(inputobj[0].validform_valid=data.info,Validform.util.showmsg.call(curform,data.info,settings.tiptype,{obj:inputobj,type:3,sweep:settings.tipSweep}),_this.addClass("Validform_error")),_this[0].validform_ajax=null},error:function(data){if("200"==data.status)return"y"==data.responseText?ajaxsetup.success({status:"y"}):ajaxsetup.success({status:"n",info:data.responseText}),!1;if("abort"!==data.statusText){var msg="status: "+data.status+"; statusText: "+data.statusText;Validform.util.showmsg.call(curform,msg,settings.tiptype,{obj:inputobj,type:3,sweep:settings.tipSweep}),_this.addClass("Validform_error")}return inputobj[0].validform_valid=data.statusText,_this[0].validform_ajax=null,!0}};if(ajaxsetup.success){var temp_suc=ajaxsetup.success;ajaxsetup.success=function(data){localconfig.success(data),temp_suc(data,inputobj)}}if(ajaxsetup.error){var temp_err=ajaxsetup.error;ajaxsetup.error=function(data){localconfig.error(data)&&temp_err(data,inputobj)}}return ajaxsetup=$.extend({},localconfig,ajaxsetup,{dataType:"json"}),_this[0].validform_ajax=$.ajax(ajaxsetup),"ajax"}return ajaxurl&&Validform.util.isEmpty.call($(this),inputval)&&(Validform.util.abort.call(_this[0]),_this[0].validform_valid="true"),bool||(Validform.util.showmsg.call(curform,flag.info,settings.tiptype,{obj:$(this),type:flag.type,sweep:settings.tipSweep},"bycheck"),_this.removeClass("Validform_error")),errorobj=null,!0},submitForm:function(settings,flg,url,ajaxPost,sync){var curform=this;if("posting"===curform[0].validform_status)return!1;if(settings.postonce&&"posted"===curform[0].validform_status)return!1;var beforeCheck=settings.beforeCheck&&settings.beforeCheck(curform);if(beforeCheck===!1)return!1;var inflag,flag=!0;if(curform.find("[datatype]").each(function(){if(flg)return!1;if(settings.ignoreHidden&&$(this).is(":hidden")||"dataIgnore"===$(this).data("dataIgnore"))return!0;var _this,inputval=Validform.util.getValue.call(curform,$(this));if(errorobj=_this=$(this),inflag=Validform.util.regcheck.call(curform,$(this).attr("datatype"),inputval,$(this)),!inflag.passed)return Validform.util.showmsg.call(curform,inflag.info,settings.tiptype,{obj:$(this),type:inflag.type,sweep:settings.tipSweep}),_this.addClass("Validform_error"),settings.showAllError?(flag&&(flag=!1),!0):(_this.focus(),flag=!1,!1);if($(this).attr("ajaxurl")&&!Validform.util.isEmpty.call($(this),inputval)){if("true"!==this.validform_valid){var thisobj=$(this);return Validform.util.showmsg.call(curform,curform.data("tipmsg").v||tipmsg.v,settings.tiptype,{obj:thisobj,type:3,sweep:settings.tipSweep}),_this.addClass("Validform_error"),thisobj.trigger("blur",["postform"]),settings.showAllError?(flag&&(flag=!1),!0):(flag=!1,!1)}}else $(this).attr("ajaxurl")&&Validform.util.isEmpty.call($(this),inputval)&&(Validform.util.abort.call(this),this.validform_valid="true");Validform.util.showmsg.call(curform,inflag.info,settings.tiptype,{obj:$(this),type:inflag.type,sweep:settings.tipSweep}),_this.removeClass("Validform_error"),errorobj=null}),settings.showAllError&&curform.find(".Validform_error:first").focus(),flag){var beforeSubmit=settings.beforeSubmit&&settings.beforeSubmit(curform);if(beforeSubmit===!1)return!1;if(curform[0].validform_status="posting",!settings.ajaxPost&&"ajaxPost"!==ajaxPost){settings.postonce||(curform[0].validform_status="normal");var url=url||settings.url;return url&&curform.attr("action",url),settings.callback&&settings.callback(curform)}var ajaxsetup=$.extend(!0,{},settings.ajaxpost||{});if(ajaxsetup.url=url||ajaxsetup.url||settings.url||curform.attr("action"),Validform.util.showmsg.call(curform,curform.data("tipmsg").p||tipmsg.p,settings.tiptype,{obj:curform,type:1,sweep:settings.tipSweep},"byajax"),sync?ajaxsetup.async=!1:sync===!1&&(ajaxsetup.async=!0),ajaxsetup.success){var temp_suc=ajaxsetup.success;ajaxsetup.success=function(data){settings.callback&&settings.callback(data),curform[0].validform_ajax=null,curform[0].validform_status="y"===$.trim(data.status)?"posted":"normal",temp_suc(data,curform)}}if(ajaxsetup.error){var temp_err=ajaxsetup.error;ajaxsetup.error=function(data){settings.callback&&settings.callback(data),curform[0].validform_status="normal",curform[0].validform_ajax=null,temp_err(data,curform)}}var localconfig={type:"POST",async:!0,data:curform.serializeArray(),success:function(data){"y"===$.trim(data.status)?(curform[0].validform_status="posted",Validform.util.showmsg.call(curform,data.info,settings.tiptype,{obj:curform,type:2,sweep:settings.tipSweep},"byajax")):(curform[0].validform_status="normal",Validform.util.showmsg.call(curform,data.info,settings.tiptype,{obj:curform,type:3,sweep:settings.tipSweep},"byajax")),settings.callback&&settings.callback(data),curform[0].validform_ajax=null},error:function(data){var msg="status: "+data.status+"; statusText: "+data.statusText;Validform.util.showmsg.call(curform,msg,settings.tiptype,{obj:curform,type:3,sweep:settings.tipSweep},"byajax"),settings.callback&&settings.callback(data),curform[0].validform_status="normal",curform[0].validform_ajax=null}};ajaxsetup=$.extend({},localconfig,ajaxsetup,{dataType:curform.attr("dataType"),type:curform.attr("method")}),curform[0].validform_ajax=$.tizi_ajax(ajaxsetup,!0)}return!1},resetForm:function(){var brothers=this;brothers.each(function(){this.reset&&this.reset(),this.validform_status="normal"}),brothers.find(".Validform_right").text(""),brothers.find(".passwordStrength").children().removeClass("bgStrength"),brothers.find(".Validform_checktip").removeClass("Validform_wrong Validform_right Validform_loading"),brothers.find(".Validform_error").removeClass("Validform_error"),brothers.find("[datatype]").removeData("cked").removeData("dataIgnore").each(function(){this.validform_lastval=null})},abort:function(){this.validform_ajax&&this.validform_ajax.abort()}},$.Datatype=Validform.util.dataType,Validform.prototype={dataType:Validform.util.dataType,eq:function(n){var obj=this;return n>=obj.forms.length?null:(n in obj.objects||(obj.objects[n]=new Validform($(obj.forms[n]).get(),{},!0)),obj.objects[n])},resetStatus:function(){var obj=this;return $(obj.forms).each(function(){this.validform_status="normal"}),this},setStatus:function(status){var obj=this;return $(obj.forms).each(function(){this.validform_status=status||"posting"}),this},getStatus:function(){var obj=this,status=$(obj.forms)[0].validform_status;return status},ignore:function(selector){var obj=this,selector=selector||"[datatype]";return $(obj.forms).find(selector).each(function(){$(this).data("dataIgnore","dataIgnore").removeClass("Validform_error")}),this},unignore:function(selector){var obj=this,selector=selector||"[datatype]";return $(obj.forms).find(selector).each(function(){$(this).removeData("dataIgnore")}),this},addRule:function(rule){for(var obj=this,rule=rule||[],index=0;index<rule.length;index++){var o=$(obj.forms).find(rule[index].ele);for(var attr in rule[index])"ele"!==attr&&o.attr(attr,rule[index][attr])}return $(obj.forms).each(function(){var $this=$(this);Validform.util.enhance.call($this,this.settings.tiptype,this.settings.usePlugin,this.settings.tipSweep,"addRule")}),this},ajaxPost:function(flag,sync,url){var obj=this;return $(obj.forms).each(function(){(1==this.settings.tiptype||2==this.settings.tiptype||3==this.settings.tiptype)&&creatMsgbox(),Validform.util.submitForm.call($(obj.forms[0]),this.settings,flag,url,"ajaxPost",sync)}),this},submitForm:function(flag,url){var obj=this;return $(obj.forms).each(function(){var subflag=Validform.util.submitForm.call($(this),this.settings,flag,url);subflag===undef&&(subflag=!0),subflag===!0&&this.submit()}),this},resetForm:function(){var obj=this;return Validform.util.resetForm.call($(obj.forms)),this},abort:function(){var obj=this;return $(obj.forms).each(function(){Validform.util.abort.call(this)}),this},check:function(bool,selector){var selector=selector||"[datatype]",obj=this,curform=$(obj.forms),flag=!0;return curform.find(selector).each(function(){Validform.util.check.call(this,curform,"",bool)||(flag=!1)}),flag},config:function(setup){var obj=this;return setup=setup||{},$(obj.forms).each(function(){var $this=$(this);this.settings=$.extend(!0,this.settings,setup),Validform.util.enhance.call($this,this.settings.tiptype,this.settings.usePlugin,this.settings.tipSweep)}),this}},$.fn.Validform=function(settings){return new Validform(this,settings)},$.Showmsg=function(msg){creatMsgbox(),Validform.util.showmsg.call(win,msg,1,{})},$.Hidemsg=function(){msgobj.hide(),msghidden=!0},$.tiziHidemsg=function(){msgobj&&(msgobj.hide(),msghidden=!0)}}(jQuery,window);