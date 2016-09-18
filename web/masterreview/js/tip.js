(function($){
 $.fn.customtip = function(opt){
	  var options = $.extend({}, $.fn.customtip.defaults, opt||{});
	  init(options);
	 $(this).mouseover(function(e){
	    this.selftip=this.title || options.title;
	    //this.title="";
	    if(this.selftip!="")
	    showTip(e,this.selftip,options.opacity);
	  }).mouseout(function(){
	  //  this.title = this.selftip;
	    tipdiv.hide().children(":first").html("");
	  }).mousemove(function(e){
	    showTip(e);
	  });
  };
  
$.fn.customtip.defaults={
  maxwidth: 300,                              //�����
  opacity: 0.8,                                  //͸����
  background: "#006CDB",                 //������ʽ
  border: "#FEFFD4 solid 1px",           //�߿���ʽ
  contentcolor: "#FFFFFE",                 //����ɫ
  font: "12px verdana,arial,sans-serif" //������ʽ
 };

var tipdiv=null;
 
 function showTip(evt,selftip,opacity){
  if(selftip){//������over����
   tipdiv.children(":first").html(selftip);
   tipdiv.css({
    "opacity":opacity,
    "top":(evt.pageY+20)+"px",
    "left":(evt.pageX+10)+"px"
   }).show();
  }else{
   tipdiv.css({
    "top":(evt.pageY+20)+"px",
    "left":(evt.pageX+10)+"px"
   });
  }
 }

 function init(options){
  var tipobj=$("#jq_bbf_tipdiv");
  if(tipobj.length==0){
   tipobj=[];
   tipobj.push("<div id=\"jq_bbf_tipdiv\" style=\"display:none;position:absolute;z-index:1000;max-width:");
   tipobj.push(options.maxwidth);
   tipobj.push("px;background:");
   tipobj.push(options.background);
   tipobj.push(";width:auto!important;width:auto;border:");
   tipobj.push(options.border);
   tipobj.push(";text-align:left;padding:3px;\"><p style=\"margin:0;padding:0;color:");
   tipobj.push(options.contentcolor);
   tipobj.push(";font:");
   tipobj.push(options.font);
   tipobj.push(";\"></p></div>");
   tipobj=$(tipobj.join("")).appendTo("body");
  }
  tipdiv=tipobj;
  tipobj=null;
 };
})(jQuery);