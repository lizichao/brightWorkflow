	var layer_config = {};
	function parentHideMiddlePanel(callback){
		$(".middlePanel").animate({width:0},"slow",function(){
			$(this).hide();$(".middlePanel-rightPanel-boundaryLine").hide();
			$(".rightPanel").width($(".mainPanel").width()-$(".menuPanel").width()-1);
			if(callback) callback.call();
		});
	}
	function hideMiddlePanel(){
		$(window.top.parentHideMiddlePanel(function(){window.top.parentCorner('show');}));
	}
	
	function parentShowMiddlePanel(){
		$(".middlePanel").show();
		$(".rightPanel").width(640);
		$(".middlePanel").animate({width:320},"slow",function(){
			$(".rightPanel").width($(".mainPanel").width() - $(".menuPanel").width() - $(".middlePanel").width() - 2);
		});
		$(".middlePanel-rightPanel-boundaryLine").show();
	}
	function showMiddlePanel(){
		var el = $(".subRightPanel").hide();
		resetHeight(0,true);
		$(window.top.parentShowMiddlePanel());
	}
	
	function parentCorner(state){
		$('#resultList_page')[0].contentWindow.childCorner(state);
	}
	function childCorner(state){
		var el = $(".subRightPanel");
		if(state == 'fadeIn'){
			el.find(".loader").width(el.width()).height(el.height());
		  el.find("div=[class^='cornerL']").height(el.height());
		  el.find("div=[class^='cornerL']").stop().animate({width:el.width()},'slow',function(){
		    el.find(".subRightPanelBody").fadeIn('fast');
		    el.find(".loader").fadeIn('fast');
		    if($.isFunction(layer_config.callback) && !layer_config.iscall){layer_config.iscall=true;layer_config.callback.call();}
		  });
		} else if(state == 'fadeOut'){
			el.find(".loader").fadeOut('fast');
			el.find("div=[class^='cornerR']").height(el.height()).width(el.width()+1);
			el.find("div=[class^='cornerL']").width(1);
			el.find("div=[class^='cornerR']").stop().animate({width:1},'slow',function(){
				el.find(".subRightPanelBody").fadeIn('fast');
				var _height = el.find(".subRightPanelBody .listBody").height();
				_height = resetHeight(_height,true);
				$(this).height(_height);
				el.find(".subRightPanelBody .listBody").height(_height);
				if($.isFunction(layer_config.callback) && !layer_config.iscall){layer_config.iscall=true;layer_config.callback.call();}
		  });
		} else if(state == 'show'){
		  el.find(".loader").hide();
			el.show();
		  childCorner('fadeIn');
		}
	}
	
	function resetHeight(_height,isSscrollTop){
		var __height = $(".subMainPanel").height();
		if(_height < __height) _height = __height;
		$(window.top.parentResetHeight(_height,isSscrollTop));
		return _height;
	}
	function parentResetHeight(_height,isSscrollTop){
		if(!_height) _height = 0;
		var __height = $(window).height();
		var ___height = $('.middlePanel').find("#content_list").height() + 125;
		__height = __height < ___height ? ___height : __height;
		_height = _height < __height ? __height : _height;
		$('.middlePanel,.boundaryLine,.rightPanel,#resultList_page').height(_height);
		if(isSscrollTop) BcUtil.scroll_to_top();
	}
	
	function loadDetailPanel(_type,_paper_id,_send_id){
		layer_config.callback = function(){loadSinglePaperResult(_send_id)};
		layer_config.iscall = false;
		if(_type=='correct'){
			if(!layer_config.correctPageTpl){
				layer_config.correctPageTpl = $(".correctPageTpl").contents().find('.contentBody').html();
			}
			$('.subRightPanelBody').empty().append(layer_config.correctPageTpl);
		} else {
			if(!layer_config.detailPageTpl){
				layer_config.detailPageTpl = $(".detailPageTpl").contents().find('.contentBody').html();
			}
			$('.subRightPanelBody').empty().append(layer_config.detailPageTpl);
		}
		
		var el = $(".subRightPanel");
		if(el.is(":hidden")){
			hideMiddlePanel();
		} else {
			childCorner('fadeIn');
		}
	}
