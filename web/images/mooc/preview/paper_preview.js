Teacher.paper.paper_preview={mainMenu:$(".child-menu"),mainContent:$(".child-content .content-wrap"),menuContainer:$(".paper-list-container"),mainContainer:$(".preview-content"),typeList:$(".type-list"),typeCur:$(".current-type"),typeOption:$(".type-list ul"),typeItem:$(".type-list select"),typeInputItem:$(".type-list p.types"),typeHandle:".type-handle-box",questionHandle:".question-handle-box",questionItemBox:".question-item-box",unitBox:".unit-box",savePaper:$("#save-btn"),saveasPaper:$("#saveas-btn"),resetPaper:$("#reset-btn"),userUnit:$(".user-unit"),requestBtn:$("a.request-btn"),init:function(){Teacher.paper.paper_preview.boxInit(),Teacher.paper.paper_preview.orderSort(),Teacher.paper.paper_preview.initStyle(),Teacher.paper.paper_preview.number_change(),Teacher.paper.paper_preview.sortable_zujuan(),$(window).resize(function(){Teacher.paper.paper_preview.boxInit()}),this.mainContainer.on({mouseenter:function(){$(this);$(this).parent(".paper-type-box").addClass("paper-type-hover")},mouseleave:function(){$(this).parent(".paper-type-box").removeClass("paper-type-hover")}},Teacher.paper.paper_preview.typeHandle),this.mainContainer.on({mouseenter:function(){$(this);$(this).parent(".question-type-box").addClass("question-type-hover");var len=$(this).parent(".question-type-box").find(".question-item-box").length;0==len&&$(this).parent(".question-type-box").find(".control-box").find(".text-icon").css("color","grey")},mouseleave:function(){$(this).parent(".question-type-box").removeClass("question-type-hover")}},Teacher.paper.paper_preview.questionHandle),this.mainContainer.on({mouseenter:function(){$(this).addClass("question-item-hover")},mouseleave:function(){$(this).removeClass("question-item-hover")}},Teacher.paper.paper_preview.questionItemBox),this.mainContainer.on({mouseenter:function(){$(this).addClass("hover")},mouseleave:function(){$(this).removeClass("hover")}},Teacher.paper.paper_preview.unitBox),this.typeList.hover(function(){var this_a=Teacher.paper.paper_preview;this_a.typeOption.show()},function(){var this_a=Teacher.paper.paper_preview;this_a.typeOption.hide()}),this.typeInputItem.find('input[type="radio"]').click(function(){var this_a=Teacher.paper.paper_preview,thisType=$(this).attr("id");switch($(".current-type").attr("sty",$(this).attr("sty")),thisType){case"default":$(".icon-view").removeClass("hide"),$(".preview-content .fn-hide").removeClass("fn-hide"),$(".current-type").attr("sty","1");break;case"static":$(".icon-view").removeClass("hide"),$(".preview-content .fn-hide").removeClass("fn-hide"),$("#menu-secret-mark .icon-view,#menu-student-info .icon-view,#menu-cent-box .icon-view").addClass("hide"),$("#secret-mark,#student-info,#cent-box").addClass("fn-hide"),$(".current-type").attr("sty","2");break;case"test":$(".icon-view").removeClass("hide"),$(".preview-content .fn-hide").removeClass("fn-hide"),$("#menu-paper-prititle .icon-view,#menu-separate-line .icon-view,#menu-secret-mark .icon-view,#menu-paper-info .icon-view,#menu-cent-box .icon-view,#menu-alert-info .icon-view,.list-type-title>.icon-view").addClass("hide"),$("#paper-prititle,#separate-line,#secret-mark,#paper-info,#cent-box,#alert-info,.paper-type-box>.handle-box,.deco-box").addClass("fn-hide"),$(".current-type").attr("sty","3");break;case"homework":$("#menu-paper-prititle .icon-view,#menu-separate-line .icon-view,#menu-secret-mark .icon-view,#menu-paper-info .icon-view,#menu-cent-box .icon-view,#menu-alert-info .icon-view,#menu-student-info .icon-view,.list-title>.icon-view").addClass("hide"),$("#paper-prititle,#student-info,#separate-line,#secret-mark,#paper-info,#cent-box,#alert-info,.handle-box").addClass("fn-hide"),$(".current-type").attr("sty","4")}this_a.typeOption.hide(),this_a.ajax_struction()}),this.typeItem.change(function(){var this_a=Teacher.paper.paper_preview,thisType=$(this).val();switch($(".current-type").attr("sty",$(this).attr("sty")),thisType){case"default":$(".icon-view").removeClass("hide"),$(".preview-content .fn-hide").removeClass("fn-hide"),$(".current-type").attr("sty","1");break;case"static":$(".icon-view").removeClass("hide"),$(".preview-content .fn-hide").removeClass("fn-hide"),$("#menu-secret-mark .icon-view,#menu-student-info .icon-view,#menu-cent-box .icon-view").addClass("hide"),$("#secret-mark,#student-info,#cent-box").addClass("fn-hide"),$(".current-type").attr("sty","2");break;case"test":$(".icon-view").removeClass("hide"),$(".preview-content .fn-hide").removeClass("fn-hide"),$("#menu-paper-prititle .icon-view,#menu-separate-line .icon-view,#menu-secret-mark .icon-view,#menu-paper-info .icon-view,#menu-cent-box .icon-view,#menu-alert-info .icon-view,.list-type-title>.icon-view").addClass("hide"),$("#paper-prititle,#separate-line,#secret-mark,#paper-info,#cent-box,#alert-info,.paper-type-box>.handle-box,.deco-box").addClass("fn-hide"),$(".current-type").attr("sty","3");break;case"homework":$("#menu-paper-prititle .icon-view,#menu-separate-line .icon-view,#menu-secret-mark .icon-view,#menu-paper-info .icon-view,#menu-cent-box .icon-view,#menu-alert-info .icon-view,#menu-student-info .icon-view,.list-title>.icon-view").addClass("hide"),$("#paper-prititle,#student-info,#separate-line,#secret-mark,#paper-info,#cent-box,#alert-info,.handle-box").addClass("fn-hide"),$(".current-type").attr("sty","4")}this_a.typeOption.hide(),this_a.ajax_struction()}),this.mainMenu.on("click",".icon-view",function(){if($(".icon-view").attr("class").indexOf("disabled")>0)return!1;$(".icon-view").addClass("disabled");var hideEle,this_a=Teacher.paper.paper_preview,$this=$(this),thisId="#"+$this.parent().attr("data-id");$this.toggleClass("hide"),hideEle=$(thisId).children(".handle-box").length>0?$(thisId).children(".handle-box"):$(thisId),hideEle.toggleClass("fn-hide"),hideEle.is(":visible")&&(this_a.flashMask(hideEle),this_a.goPosition(hideEle)),this_a.ajax_left_select()}),this.mainMenu.on("click",".menu-item-title",function(){var this_a=Teacher.paper.paper_preview,$this=$(this),thisId="#"+$this.parent().attr("data-id");$(thisId).children(".handle-box").length>0?this_a.flashMask($(thisId).children(".handle-box")):this_a.flashMask($(thisId)),this_a.goPosition($(thisId))}),this.mainMenu.on("click",".question-item",function(){var this_a=Teacher.paper.paper_preview,$this=$(this),thisId="#"+$this.attr("data-id");this_a.flashMask($(thisId)),this_a.goPosition($(thisId))}),this.mainContent.on("click",".question-item-box",function(){var this_a=Teacher.paper.paper_preview,$this=$(this),thisId="#menu-"+$this.attr("id");this_a.flashMask($(thisId),171,16),$this.find(".answer").hasClass("undis")?$this.find(".answer").removeClass("undis"):$this.find(".answer").addClass("undis")}),this.mainContainer.on("click","a",function(e){var this_a1=this;Teacher.paper.paper_preview.nBtnClick(e,this_a1)}),this.mainMenu.on("click",".set-btn",function(){var this_a=this;Teacher.paper.paper_preview.setBtnClick(this_a)}),this.mainMenu.on("click",".icon-set",function(){var this_a=this;Teacher.paper.paper_preview.setIcoBtnClick(this_a)}),this.mainContainer.on("click",".unit-box",function(){var this_a=this;Teacher.paper.paper_preview.setUnitBtnClick(this_a)}),this.mainContainer.on("click",".handle-box",function(){var this_a=this;Teacher.paper.paper_preview.setHandleBtnClick(this_a)}),this.savePaper.click(function(){Teacher.paper.paper_preview.saveBtnClick(0)}),this.saveasPaper.click(function(){Teacher.paper.paper_preview.saveBtnClick(1)}),this.resetPaper.click(function(){$.tiziDialog({content:"是否新建一份空白试卷，并放弃所有未保存的修改？",cancel:!0,ok:function(){Teacher.paper.paper_preview.reset_paper()}})})},saveBtnClick:function(save_as){return $(".question-item-box").length<=0?($.tiziDialog({content:Teacher.paper.common.ErrorNoQuestion}),!1):(callbackfn=function(){var preTitle=$(".preview-title h3").html();$.tiziDialog({id:"commonSaveTitle",title:"保存试卷",content:$(".savePaperContent").html().replace("commonSaveTitleForm_beta","commonSaveTitleForm"),icon:null,width:480,okVal:"保存试卷",ok:function(){return $(".commonSaveTitleForm").submit(),!1},cancel:!0,cancelVal:"继续编辑"}),$(".paper_title").val(preTitle),Common.valid.paperSaveTitle(save_as),$("#tiziLogin").hide(),$("#tiziRegister").hide()},seajs.use("tizi_login_form",function(ex){ex.loginCheck("function",callbackfn)}),void 0)},setHandleBtnClick:function(this_a){var thisId="#win-"+$(this_a).parent().attr("id");this.randerConfigBox(this_a),this.renderAlertBox(),this.goPosWin(thisId),this.radio_render(".openwin-box")},setUnitBtnClick:function(this_a){var thisId="#win-"+$(this_a).attr("id");this.randerConfigBox(this_a),this.renderAlertBox(),this.goPosWin(thisId),this.radio_render(".openwin-box")},setIcoBtnClick:function(this_a){var thisId="#win-"+$(this_a).parent().data("id");this.randerConfigBox(this_a),this.renderAlertBox(),this.goPosWin(thisId),this.radio_render(".openwin-box")},setBtnClick:function(this_a){this.randerConfigBox(this_a),this.renderAlertBox(),this.radio_render(".openwin-box")},randerConfigBox:function(){$.tiziDialog({icon:null,width:700,title:"试卷设置",cancel:!0,content:'<div class="openwin-box">'+$("#setting-content").html()+"</div>",ok:function(){var this_a=Teacher.paper.paper_preview,jsonstr=this_a.getOption($(".openwin-box")),sid=$(".subject-chose").data("subject"),url=baseUrlName+"paper/paper_preview/save_paper_config";$.tizi_ajax({url:url,type:"POST",dataType:"json",data:{config:jsonstr,sid:sid},success:function(data){0==data.errorcode&&$.tiziDialog({content:data.error})}});var optionJson=new Function("return "+this_a.getOption($(".openwin-box"),"id"))();this_a.reRenderPage(optionJson)}})},nBtnClick:function(e,this_a1){if(this_a=$(this_a1).find("span"),"del-icon"==this_a.attr("class"))if("inner"==this_a.attr("opt")){var qid="#q"+this_a.data("qid"),menuId="#menu-q"+this_a.data("qid");$.tiziDialog({content:"确定删除当前题目？",cancel:!0,ok:function(){var qorigin=this_a.data("qorigin");Teacher.paper.paper_preview.del_inner_question(this_a.data("qid"),qorigin,function(){$(qid).remove(),$(menuId).remove(),Teacher.paper.paper_preview.orderSort(),Teacher.paper.paper_preview.orderType()},function(){})}})}else{var refer=this_a.attr("refer");$.tiziDialog({content:"确定清空并删除当前题型？",cancel:!0,ok:function(){Teacher.paper.paper_preview.del_question_type(this_a.attr("refer"),function(){$("#question-type"+refer).remove(),$("#menu-type-li-questiontype"+refer).remove(),Teacher.paper.paper_preview.orderType(),Teacher.paper.paper_preview.orderSort()},function(){})}})}if("text-icon"==this_a.attr("class")&&"清空"==this_a.html()){var refer=this_a.attr("refer");this_a.parents(".question-type-box").find(".question-item-box").length>0&&$.tiziDialog({content:"确定清空当前题型？",cancel:!0,ok:function(){Teacher.paper.paper_preview.clean_question_type(this_a.attr("refer"),function(){$("#question-type"+refer).find(".question-item-box").remove(),$("#menu-outer-ul-question-type"+refer).find(".question-item").remove(),Teacher.paper.paper_preview.orderType(),Teacher.paper.paper_preview.orderSort()},function(){})}})}if("up-icon"==this_a.attr("class"))if("inner"==this_a.attr("opt")){var outerId=this_a.parent().parent().parent().parent().attr("id"),inde=this.isFirstEle("#q"+this_a.data("qid"),"#"+outerId,".question-item-box");if(1==inde||0==inde)return e.stopPropagation(),void 0;this.moveQuestion("#"+outerId,"#"+this_a.parent().parent().parent().attr("id"),"up",".question-item-box"),this.moveQuestion("#menu-outer-ul-"+outerId,"#menu-q"+this_a.data("qid"),"up",".question-item"),this.orderSort(),$(".up-icon").removeClass("up-icon").addClass("up-icon-2"),this.up_inner_question(outerId,function(){$(".up-icon-2").addClass("up-icon").removeClass("up-icon-2")})}else{var outerId="#question-type"+this_a.attr("refer");if("1"==this_a.attr("papertype")){var inde=this.isFirstEle(outerId,"#paper-type1",".question-type-box");if(1==inde||0==inde)return e.stopPropagation(),void 0;this.moveQuestion("#paper-type1",outerId,"up",".question-type-box"),this.moveQuestion("#menu-paper1-con","#menu-type-li-questiontype"+this_a.attr("refer"),"up",".menu-type-li"),$(".up-icon").removeClass("up-icon").addClass("up-icon-2"),this.up_question_type("paper-type1",function(){$(".up-icon-2").addClass("up-icon").removeClass("up-icon-2")})}else{var inde=this.isFirstEle(outerId,"#paper-type2",".question-type-box");if(1==inde||0==inde)return e.stopPropagation(),void 0;this.moveQuestion("#paper-type2",outerId,"up",".question-type-box"),this.moveQuestion("#menu-paper2-con","#menu-type-li-questiontype"+this_a.attr("refer"),"up",".menu-type-li"),this.sortable_zujuan(),$(".up-icon").removeClass("up-icon").addClass("up-icon-2"),this.up_question_type("paper-type2",function(){$(".up-icon-2").addClass("up-icon").removeClass("up-icon-2")})}this.orderType(),this.orderSort()}if("down-icon"==this_a.attr("class"))if("inner"==this_a.attr("opt")){var outerId=this_a.parent().parent().parent().parent().attr("id"),inde=this.isFirstEle("#q"+this_a.data("qid"),"#"+outerId,".question-item-box");if(1==inde||2==inde)return e.stopPropagation(),void 0;this.moveQuestion("#"+outerId,"#"+this_a.parent().parent().parent().attr("id"),"down",".question-item-box"),this.moveQuestion("#menu-outer-ul-"+outerId,"#menu-q"+this_a.data("qid"),"down",".question-item"),this.orderSort(),$(".down-icon").removeClass("down-icon").addClass("down-icon-2"),this.up_inner_question(outerId,function(){$(".down-icon-2").addClass("down-icon").removeClass("down-icon-2")})}else{var outerId="#question-type"+this_a.attr("refer");if("1"==this_a.attr("papertype")){var inde=this.isFirstEle(outerId,"#paper-type1",".question-type-box");if(1==inde||2==inde)return e.stopPropagation(),void 0;this.moveQuestion("#paper-type1",outerId,"down",".question-type-box"),this.moveQuestion("#menu-paper1-con","#menu-type-li-questiontype"+this_a.attr("refer"),"down",".menu-type-li"),$(".down-icon").removeClass("down-icon").addClass("down-icon-2"),this.up_question_type("paper-type1",function(){$(".down-icon-2").addClass("down-icon").removeClass("down-icon-2")})}else{var inde=this.isFirstEle(outerId,"#paper-type2",".question-type-box");if(1==inde||2==inde)return e.stopPropagation(),void 0;this.moveQuestion("#paper-type2",outerId,"down",".question-type-box"),this.moveQuestion("#menu-paper2-con","#menu-type-li-questiontype"+this_a.attr("refer"),"down",".menu-type-li"),this.sortable_zujuan(),$(".down-icon").removeClass("down-icon").addClass("down-icon-2"),this.up_question_type("paper-type2",function(){$(".down-icon-2").addClass("down-icon").removeClass("down-icon-2")})}this.orderType(),this.orderSort()}if("set-icon"==this_a.attr("class")){var thisId="#win-"+$(this_a1).parent().parent().parent().attr("id");$.tiziDialog({icon:null,width:700,title:"试卷设置",cancel:!0,content:'<div class="openwin-box">'+$("#setting-content").html()+"</div>",ok:function(){var this_a=Teacher.paper.paper_preview,jsonstr=this_a.getOption($(".openwin-box")),sid=$(".subject-chose").data("subject"),url=baseUrlName+"paper/paper_preview/save_paper_config";$.tizi_ajax({url:url,type:"POST",dataType:"json",data:{config:jsonstr,sid:sid},success:function(data){0==data.errorcode&&$.tiziDialog({content:data.error})}});var optionJson=new Function("return "+this_a.getOption($(".openwin-box"),"id"))();this_a.reRenderPage(optionJson)}}),this.renderAlertBox(),this.goPosWin(thisId),this.radio_render(".openwin-box")}if("change-icon"==this_a.attr("class")){if($(this_a).hasClass("disabled"))return;$(this_a).addClass("disabled");var sid=$(".subject-chose").data("subject"),cid=$(this_a).attr("data-cid"),qtype=$(this_a).attr("data-qtype_id"),pqtype=$(this_a).parents(".question-type-box").attr("id").substr(13),qid=$(this_a).attr("data-qid"),qlevel=$(this_a).attr("data-level"),qindex=$(this_a).parents(".question-item-box").find(".question-index").text(),qids=new Array;$(this_a).parents(".question-type-box").find(".change-icon").each(function(i,e){"0"===$(e).attr("data-qorigin")&&qids.push($(e).attr("data-qid"))});var param={sid:sid,cid:cid,qids:qids,qindex:qindex,qid:qid,qtype:qtype,pqtype:pqtype,qlevel:qlevel};this.change_inner_question(param,qid,function(){$("#q"+qid).find(".change-icon").removeClass("disabled")},function(){$("#q"+qid).find(".change-icon").removeClass("disabled")})}e.stopPropagation()},change_inner_question:function(param,qid,success,err){$.tizi_ajax({type:"POST",dataType:"json",url:baseUrlName+"paper/paper_question/change_question",data:param,success:function(data){1==data.errorcode?($("#q"+qid).attr("id","q"+data.qid),$("#q"+data.qid).attr("data-pqid",data.pqid),$("#q"+data.qid).html(data.html),$("#menu-q"+qid).attr("id","menu-q"+data.qid),$("#menu-q"+data.qid).attr("data-pqid",data.pqid),$("#menu-q"+data.qid).attr("data-id","q"+data.qid),$("#menu-q"+data.qid).attr("title",data.qtitle),$("#menu-q"+data.qid).find(".title-content").html(data.qtitle),Teacher.paper.paper_preview.up_inner_question($("#q"+data.qid).parents(".question-type-box").attr("id"),function(){}),success()):($.tiziDialog({content:data.error}),err())}})},del_inner_question:function(qid,qorigin,success,err){var url=baseUrlName+"paper/paper_question/remove_question_from_paper",sid=$(".subject-chose").data("subject"),para={qid:qid,sid:sid,qorigin:qorigin};$.tizi_ajax({url:url,type:"POST",dataType:"json",data:para,success:function(data){1==data.errorcode?(success(),Teacher.paper.paper_common.randerCart(data.question_cart)):(err(),$.tiziDialog({content:data.error}))}})},del_question_type:function(type,success,err){var url=baseUrlName+"paper/paper_preview/delete_question_type",sid=$(".subject-chose").data("subject"),para={qtype:type,sid:sid};$.tizi_ajax({url:url,type:"POST",dataType:"json",data:para,success:function(data){1==data.errorcode?(success(),Teacher.paper.paper_common.randerCart(data.question_cart)):(err(),$.tiziDialog({content:data.error}))}})},up_inner_question:function(type,unlock){var outer=$("#"+type),qorder=new Array;type=type.substr(13),outer.find(".question-item-box").each(function(i,e){qorder.push($(e).attr("data-pqid"))});var url=baseUrlName+"paper/paper_preview/save_question_order",sid=$(".subject-chose").data("subject"),para={qtype:type,sid:sid,qorder:qorder.toString()};$.tizi_ajax({url:url,type:"POST",dataType:"json",data:para,success:function(data){0==data.errorcode&&$.tiziDialog({content:data.error}),unlock()}})},up_question_type:function(type,unlock){var outer=$("#"+type),qorder=new Array;outer.find(".question-type-box").each(function(i,e){qorder.push($(e).attr("id").substr(13))});var url=baseUrlName+"paper/paper_preview/save_question_type_order",sid=$(".subject-chose").data("subject"),para={sectiontype:type.substr(10),sid:sid,qtorder:qorder.toString()};$.tizi_ajax({url:url,type:"POST",dataType:"json",data:para,success:function(data){0==data.errorcode&&$.tiziDialog({content:data.error}),unlock()}})},ajax_move_question:function(qid,typeid){var q_item=$("#"+typeid).find(".question-item"),qorder=new Array;q_item.each(function(i,e){var rid=$(e).attr("data-pqid");qorder.push(rid)});var sid=$(".subject-chose").data("subject"),url=baseUrlName+"paper/paper_preview/move_question",para={qorder:qorder.toString(),pqid:qid,qtype:typeid.substr(27),sid:sid};$.tizi_ajax({url:url,type:"POST",dataType:"json",data:para,success:function(data){Teacher.paper.paper_common.randerCart(data.question_cart),0==data.errorcode&&$.tiziDialog({content:data.error})}})},clean_question_type:function(type,success,err){var url=baseUrlName+"paper/paper_question/remove_question_from_cart",sid=$(".subject-chose").data("subject"),para={qtype:type,sid:sid};$.tizi_ajax({url:url,type:"POST",dataType:"json",data:para,success:function(data){1==data.errorcode?(success(),Teacher.paper.paper_common.randerCart(data.question_cart)):(err(),$.tiziDialog({content:data.error}))}})},ajax_struction:function(){$("body").find("#temp-con-div").remove(),$("body").append("<div id='temp-con-div' class='fn-hide'></div>");var contentHtml=$("#setting-content").html();$("#temp-con-div").html(contentHtml),this.renderAlertBox("#temp-con-div"),$("#temp-con-div").find(".radio-render-cent-verify").html("1"),this.radio_render("#temp-con-div");var jsonstr=this.getOption($("#temp-con-div"));switch($("body").find("#temp-con-div").remove(),this.getStruc()){case"3":jsonstr=this.changeIsCheckScore(jsonstr);break;case"4":jsonstr=this.changeIsCheckScore(jsonstr)}var url=baseUrlName+"paper/paper_preview/save_paper_config",sid=$(".subject-chose").data("subject"),para={config:jsonstr,sid:sid,type:this.getStruc()};$.tizi_ajax({url:url,type:"POST",dataType:"json",data:para,success:function(data){0==data.errorcode&&$.tiziDialog({content:data.error})}})},changeIsCheckScore:function(str){var json=new Function("return "+str)();for(i in json)json[i].ischeckedscore&&(json[i].ischeckedscore=0);return JSON.stringify(json)},ajax_left_select:function(){$("body").find("#temp-con-div").remove(),$("body").append("<div id='temp-con-div' class='fn-hide'></div>");var contentHtml=$("#setting-content").html();$("#temp-con-div").html(contentHtml),this.renderAlertBox("#temp-con-div"),this.radio_render("#temp-con-div");var jsonstr=this.getOption($("#temp-con-div"));$("body").find("#temp-con-div").remove();var url=baseUrlName+"paper/paper_preview/save_paper_config",sid=$(".subject-chose").data("subject"),para={config:jsonstr,sid:sid};$.tizi_ajax({url:url,type:"POST",dataType:"json",data:para,success:function(data){$(".icon-view").removeClass("disabled"),0==data.errorcode&&$.tiziDialog({content:data.error})}})},getStruc:function(){var strName=$(".current-type").attr("sty");return strName},reset_paper:function(){var url=baseUrlName+"paper/paper_question/reset_paper",sid=$(".subject-chose").data("subject"),para={sid:sid};$.tizi_ajax({url:url,type:"POST",dataType:"json",data:para,success:function(data){1==data.errorcode?($.tiziDialog({ok:!1,content:data.error}),location.href=baseUrlName+"teacher/paper/preview/"+sid):$.tiziDialog({content:data.error})}})},reRenderPage:function(json){for(i in json){var refer=i.substr(4),ischeck=json[i].ischecked,ischeckscore=json[i].ischeckedscore,headerEle=["separate-line","secret-mark","paper-title","paper-prititle","paper-info","student-info","cent-box","alert-info"];if(headerEle=headerEle.toString(),ischeck?($("#menu-"+refer).find(".icon-view").removeClass("hide"),-1!=headerEle.indexOf(refer)?$("#"+refer).removeClass("fn-hide"):$("#"+refer).find(".question-handle-box").removeClass("fn-hide")):($("#menu-"+refer).find(".icon-view").addClass("hide"),-1!=headerEle.indexOf(refer)?$("#"+refer).addClass("fn-hide"):$("#"+refer).find(".question-handle-box").addClass("fn-hide")),void 0!=ischeckscore&&(0==ischeckscore?$("#"+refer).find(".deco-box").addClass("fn-hide"):$("#"+refer).find(".deco-box").removeClass("fn-hide")),"win-question-type"==i.substr(0,17)){var ch_title=json[i].title,ch_des=json[i].content;$("#"+i.substr(4)).find(".change-inner-title").html(ch_title),$("#"+i.substr(4)).find(".change-inner-des").html(ch_des),$("#left-question-cart-name"+i.substr(17)).html(json[i].title),$(".menu-render-type-name"+i.substr(17)).html(json[i].title)}if(-1!="secret-mark,paper-title,paper-prititle,paper-info,student-info".indexOf(i.substr(4))&&$("#"+i.substr(4)).html(json[i].title),"win-alert-info"==i&&$("#alert-info").find("dd").html(json[i].content),"win-paper-type1"==i||"win-paper-type2"==i){ischeck?$("#"+i.substr(4)).find(".type-handle-box").removeClass("fn-hide"):$("#"+i.substr(4)).find(".type-handle-box").addClass("fn-hide");var ch_title=json[i].title,ch_des=json[i].content;$("#"+i.substr(4)).find(".type-handle-box").find("dt").html(ch_title),$("#"+i.substr(4)).find(".type-handle-box").find("dd").html(ch_des)}}},radio_render:function(openbox){for(var tables=$(openbox).find("table"),i=0;i<tables.length;i++){var radiorender=(tables.eq(i),tables.find(".radio-render"));if(0!=radiorender.length)for(var j=0;j<radiorender.length;j++){var rname=radiorender.eq(j).attr("name"),val=parseInt(radiorender.eq(j).html());val=0==val?1:0,$('input[name="'+rname+'"]').eq(val).attr("checked",!0)}}},moveQuestion:function(container,moveEle,toward,sameEle){var now_no=$(container).find(sameEle).index($(moveEle)),nu="up"==toward?now_no-1:now_no;if(!("up"==toward&&0==now_no||"down"==toward&&now_no==$(container).find(sameEle).length-1)){var clo=$(moveEle).clone();$(moveEle).remove(),"up"==toward&&$(container).find(sameEle).eq(nu).before(clo),"down"==toward&&$(container).find(sameEle).eq(nu).after(clo)}},isFirstEle:function(ele,con,same){var isfir=$(con).find(same).index($(ele)),len=$(con).find(same).length;return 1==len?1:0==isfir?0:isfir==$(con).find(same).length-1?2:void 0},getOption:function(openbox,get_show){var getshow="pos";null!=get_show&&(getshow=get_show);for(var tables=openbox.find("table"),sendStr="{",i=0;i<tables.length;i++){sendStr+="win-paper-type"==tables.eq(i).attr(getshow).substr(0,14)?'"'+tables.eq(i).attr(getshow).substr(0,17)+'":':'"'+tables.eq(i).attr(getshow)+'":',sendStr+="{",""!=tables.eq(i).attr(getshow).replace(/[^0-9]/gi,"")&&(sendStr+='"id":'+tables.eq(i).attr(getshow).replace(/[^0-9]/gi,"")+","),"win-paper-typeone"==tables.eq(i).attr(getshow).substr(0,17)&&(sendStr+='"type":1,'),"win-paper-typetwo"==tables.eq(i).attr(getshow).substr(0,17)&&(sendStr+='"type":2,');for(var inputs=tables.eq(i).find("input,textarea"),j=0;j<inputs.length;j++)if("radio"==inputs.eq(j).attr("type")){var checkval=$('input[name="'+inputs.eq(j).attr("name")+'"]:checked').val();0==j&&(sendStr+=j==inputs.length-2?'"ischecked":'+checkval:'"ischecked":'+checkval+","),2==j&&(sendStr+=j==inputs.length-2?'"ischecked":'+checkval:'"ischeckedscore":'+checkval+",")}else sendStr+="text-input"==inputs.eq(j).attr("class")?j==inputs.length-1?'"title":"'+inputs.eq(j).val().replace(/"/g,"'")+'"':'"title":"'+inputs.eq(j).val()+'",':j==inputs.length-1?'"content":"'+inputs.eq(j).val().replace(/"/g,"'").replace(/\n/g,"<br />")+'"':'"content":"'+inputs.eq(j).val()+'",';sendStr+=i==tables.length-1?"}":"},"}return sendStr+="}"},renderAlertBox:function(con){var conBox=".openwin-box";void 0!=con&&(conBox=con);for(var left_header=$(".paper-header").find("li"),i=0;i<left_header.length;i++){var icon_radio=left_header.eq(i).find(".icon-view");if(0!=icon_radio.length)if(-1==icon_radio.eq(0).attr("class").indexOf("hide")){var outer=left_header.eq(i).data("id");$(conBox).find("#win-"+outer).find('span[name="'+outer+'"]').html(1)}else{var outer=left_header.eq(i).data("id");$(conBox).find("#win-"+outer).find('span[name="'+outer+'"]').html(0)}}var child_list=$(".paper-list-container").find(".child-list");$(".paper-set-win").find('table[id^="win-question-type"]').hide();for(var win_length=$(".paper-set-win").find('table[id^="win-question-type"]').length,n=0;n<child_list.length;n++)for(var type_li=child_list.eq(n).find(".menu-type-li"),i=0;i<type_li.length;i++){var icon_radio=type_li.eq(i).find(".icon-view");if(0!=icon_radio.length){if(-1==icon_radio.eq(0).attr("class").indexOf("hide")){var outer="question-type"+type_li.eq(i).attr("id").replace(/[^0-9]/gi,"");$(conBox).find("#win-"+outer).find('span[name="'+outer+'"]').html(1)}else{var outer="question-type"+type_li.eq(i).attr("id").replace(/[^0-9]/gi,"");$(conBox).find("#win-"+outer).find('span[name="'+outer+'"]').html(0)}$('.paper-set-win table[id="win-'+outer+'"]').show(),$(".paper-set-win").find('table[id^="win-question-type"]').eq(win_length-1).show();var righttype=$(".paper-set-win").find('table[id^="win-question-type"]:visible').find(".title-td");this.renderNum(righttype)}}for(var paperheader=$(".paper-list-container").find(".list-type-title"),i=0;i<paperheader.length;i++){var icon_v=paperheader.eq(i).find(".icon-view");if(0!=icon_v.length)if(-1==icon_v.eq(0).attr("class").indexOf("hide")){var outer=paperheader.eq(i).data("id");$(conBox).find("#win-"+outer).find('span[name="'+outer+'"]').html(1)}else{var outer=paperheader.eq(i).data("id");$(conBox).find("#win-"+outer).find('span[name="'+outer+'"]').html(0)}}$(".preview-content").find(".unit-box").each(function(i,e){var this_id=$(e).attr("id");-1!="secret-mark,paper-title,paper-prititle,paper-info,student-info".indexOf(this_id)&&$("#win-"+this_id).find('input[type="text"]').val($(e).text()),"alert-info"==this_id&&$("#win-alert-info").find("textarea").val($(e).find("dd").html().replace(/<br>/g,"\n"))}),$(".preview-content").find(".paper-type-box").find(".type-handle-box").each(function(i,e){var ch_title=$(e).find("dt").text(),ch_des=$(e).find("dd").html(),this_id=$(e).parent().attr("id");$("#win-"+this_id).find('input[type="text"]').val(ch_title),$("#win-"+this_id).find("textarea").val(ch_des.replace(/<br>/g,"\n"))}),$(".preview-content").find(".question-type-box").find(".question-handle-box").each(function(i,e){var ch_title=$(e).find(".change-inner-title").text(),ch_des=$(e).find(".change-inner-des").html(),this_id=$(e).parent().attr("id");$("#win-"+this_id).find('input[type="text"]').val(ch_title),$("#win-"+this_id).find("textarea").val(ch_des.replace(/<br>/g,"\n"));var ch_deco=$(e).find(".deco-box");if(-1==ch_deco.attr("class").indexOf("hide")){var outer=$("#win-"+this_id).find('input[type="radio"]').eq(3).attr("name");$(conBox).find("#win-"+this_id).find('span[name="'+outer+'"]').html(1)}else{var outer=$("#win-"+this_id).find('input[type="radio"]').eq(3).attr("name");$(conBox).find("#win-"+this_id).find('span[name="'+outer+'"]').html(0)}})},flashMask:function(effect,width,height){var w=width||effect.width(),h=height||effect.height();effect.prepend('<div class="mask"></div>'),$(".mask").css({width:w,height:h}).fadeIn("fast",function(){$(".mask").fadeOut("fast",function(){$(".mask").remove()})})},renderNum:function(righttype){righttype.each(function(index,element){switch($(element).text("题型"+(index+1)+"头部"),$(element).text().substr(2,1)){case"1":$(element).text("题型一头部");break;case"2":$(element).text("题型二头部");break;case"3":$(element).text("题型三头部");break;case"4":$(element).text("题型四头部");break;case"5":$(element).text("题型五头部");break;case"6":$(element).text("题型六头部");break;case"7":$(element).text("题型七头部");break;case"8":$(element).text("题型八头部");break;case"9":$(element).text("题型九头部");break;case"10":$(element).text("题型十头部");break;case"11":$(element).text("题型十一头部");break;case"12":$(element).text("题型十二头部");break;case"13":$(element).text("题型十三头部");break;case"14":$(element).text("题型十四头部");break;case"15":$(element).text("题型十五头部");break;case"16":$(element).text("题型十六头部");break;case"17":$(element).text("题型十七头部");break;case"18":$(element).text("题型十八头部");break;case"19":$(element).text("题型十九头部");break;case"20":$(element).text("题型二十头部");break;default:return}})},goPosition:function(effect){return this.mainContainer.scrollTop(0),this.mainContainer.scrollTop(effect.offset().top-90),effect},goPosWin:function(thisId){$(thisId).addClass("active"),$(".paper-set-win").scrollTop(0),$(".paper-set-win").scrollTop($(thisId).position().top-60)},initStyle:function(){var sty=$(".current-type").attr("sty");switch(sty){case"1":$(".current-type").find("span").html("默认结构");break;case"2":$(".current-type").find("span").html("标准结构");break;case"3":$(".current-type").find("span").html("测验结构");break;case"4":$(".current-type").find("span").html("作业结构")}},orderSort:function(){Teacher.paper.paper_common.orderSort()},orderType:function(){Teacher.paper.paper_common.orderType()},number_change:function(){Teacher.paper.common.number_change(".type-title-nu"),Teacher.paper.common.number_change(".menu-questiontype-nu")},contentSort:function(itemContainer,itemContent,itemOrder){var itemvar=$(itemContent).clone();$(itemContent).remove(),"append"==itemOrder?$(itemContainer).append(itemvar):$(itemContainer).children().eq(itemOrder).after(itemvar)},boxInit:function(){this.menuContainer.height(this.mainMenu.height()-30),this.mainContainer.height(this.mainContent.height()-60)},sortable_zujuan:function(){this.mainMenu.find(".question-box").sortable({axis:"y",connectWith:".question-box",stop:function(event,ui){var itemOrder,this_a=Teacher.paper.paper_preview,itemContent="#"+ui.item.attr("data-id"),itemLen=ui.item.siblings().length,itemParent="#"+ui.item.parent().siblings(".list-title").attr("data-id"),itemIndex=ui.item.index();
itemOrder=0==itemLen?"append":itemIndex,this_a.contentSort(itemParent,itemContent,itemOrder),this_a.orderSort(),this_a.flashMask($(itemContent)),this_a.goPosition($(itemContent)),this_a.ajax_move_question(ui.item.attr("data-pqid"),ui.item.parent().attr("id"))}})}};