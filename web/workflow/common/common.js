   /**			jQuery UI--开始	  			  **/
    /*******************************************/
    var _plugin_jqui = {

        /**
         * 按钮相关
         */
        button: {
            onOff: function(options) {
                var defaults = {
                    btnText: false // text
                };
                options = $.extend({}, defaults, options);
                var dlgButton = $('.ui-dialog-buttonpane button');
                if (options.btnText) {
                    // TODO 查询优化，兼容有相同文字的情况
                    dlgButton = $('.ui-button-text:contains(' + options.btnText + ')').parent();
                }
                if (options.enable) {
                    dlgButton.attr('disabled', '');
                    dlgButton.removeClass('ui-state-disabled');
                } else {
                    dlgButton.attr('disabled', 'disabled');
                    dlgButton.addClass('ui-state-disabled');
                }
            }
        },

        /**
         * 对话框相关
         */
        dialog: {
        	event: {
				close: function() {
					$(this).dialog('close');
				}
			},
            /**
             * 按钮相关方法
             */
            button: {
                /**
                 * 为dialog中的button设置icon，参数结构
                 * [{
                 *     text: '暂存',
                 *     title: '暂时存储',
                 *     icons: {
                 *         primary: 'ui-icon-disk'
                 *     }
                 * }]
                 * @param {Object} options
                 */
                setAttrs: function(options) {
                    var _set_btns = [];
                    $.each(options, function(i, v) {
                        _set_btns[_set_btns.length] = v;
                    });
                    $.each(_set_btns, function(i, v) {
                        var $btn = $('.ui-button-text').filter(function() {
                            return $(this).text() == v.text;
                        }).parent();
                        var _icons = {};
                        if (v.icons) {
                            var arrayIcons = v.icons.split(' ');
                            if (arrayIcons.length == 1) {
                                _icons.primary = arrayIcons[0];
                            } else if (arrayIcons.length == 2) {
                                _icons.primary = arrayIcons[0];
                                _icons.secondary = arrayIcons[1];
                            }
                            $btn.button({
                                icons: _icons
                            });
                        }
                        $btn.attr('title', v.title);

                        // 分隔符
                        try {
                            if (v.division) {
                                var position = v.division || 'after';
                                if (position == 'before') {
                                    $btn.before("<span class='ui-state-error ui-dialog-button-devision'></span>");
                                } else if (position == 'after') {
                                    $btn.after("<span class='ui-state-error ui-dialog-button-devision'></span>");
                                } else {
                                    alert('未知位置：' + position);
                                }
                            }
                        } catch (e) {
                        }
                    });
                },

                /*[{
                 text: 'aaa',
                 position: ['left'|'right']
                 }]*/
                group: function(opts) {
                    $.each(opts, function(i, v) {
                        var $btn = $('.ui-button-text').filter(function() {
                            return $(this).text() == v.text;
                        }).parent();
                        try {
                            if (!v.position || v.position == 'after') {
                                $btn.after("<span class='ui-state-error ui-dialog-button-devision'></span>");
                            } else if (v.position == 'before') {
                                $btn.before("<span class='ui-state-error ui-dialog-button-devision'></span>");
                            } else {
                                alert('请设置position|by dialog.button.group');
                            }
                        } catch (e) {
                        }
                    });
                }
            },
            /**
             * 根据浏览器差异获取window的高度
             */
            getBodyHeight: function() {
                var tempBodyHeight = document.documentElement.clientHeight;
                if ($.common.browser.isIE()) {
                    //tempBodyHeight += 150;
                } else {
                    tempBodyHeight -= 10;
                }
                return tempBodyHeight;
            },

            /**
             * 根据浏览器差异获取设置对话框的高度
             */
            getHeight: function(_height) {
                var tempBodyHeight = _height;
                if ($.common.browser.isIE()) {
                    tempBodyHeight += 100;
                } else {
                    tempBodyHeight -= 10;
                }
                return tempBodyHeight;
            }
        },

        /**
         * 选项卡相关
         */
        tab: {
            /**
             * 自动设置选项卡的高度
             * @param {Object} options
             */
            autoHeight: function(options) {
                var defaults = {
                    increment: {
                        ie: 0,
                        firefox: 0,
                        chrome: 0
                    }
                };

                options = $.extend({}, defaults, options);

                /**
                 * 核心处理函数
                 */
                function innerDeal() {
                    // 非IE默认值
                    var gap = 80;
                    // 特殊处理IE
                    if ($.common.browser.isIE()) {
                        gap = 60;
                        gap += options.increment.ie;
                    } else if ($.common.browser.isMozila()) {
                        gap += options.increment.firefox;
                    } else if ($.common.browser.isChrome()) {
                        gap += options.increment.chrome;
                    }

                    var height = document.body.clientHeight - gap;
                    $('.ui-tabs-panel').height(height);
                    if ($.isFunction(options.callback)) {
                        options.callback();
                    }
                }

                innerDeal();

                // 窗口大小改变的时候
                window.onresize = innerDeal;

            }
        },

		/**
		 * 一些特效
		 */
		effect: {

			/*
			 * 斑马效果
			 */
			zebra: function(selector) {
				$(selector).hover(function() {
					$(this).addClass('ui-state-hover');
				}, function() {
					$(this).removeClass('ui-state-hover');
				});
			}
		},

		/**
		 * 自动完成
		 */
		autocomplete: {

			/*
			 * 触发自动完成
			 */
			triggerSearch: function(e) {
				if ($(this).val().length > 0) {
            		$(this).autocomplete('search', $(this).val());
            	}
			},

			/*
			 * 取消自动完成的结果，配合triggerSearch使用，目的是针对文本框获取焦点之后就自动根据现有内容搜索后点击死循环问题
			 */
			cancelResult: function(ele) {
				setTimeout(function() {
					$('.ui-autocomplete').hide();
				}, 50);
			}
		}
    };
    /*******************************************/
    /**			jQuery UI--结束	  			  **/
    /*******************************************/
