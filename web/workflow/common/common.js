   /**			jQuery UI--��ʼ	  			  **/
    /*******************************************/
    var _plugin_jqui = {

        /**
         * ��ť���
         */
        button: {
            onOff: function(options) {
                var defaults = {
                    btnText: false // text
                };
                options = $.extend({}, defaults, options);
                var dlgButton = $('.ui-dialog-buttonpane button');
                if (options.btnText) {
                    // TODO ��ѯ�Ż�����������ͬ���ֵ����
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
         * �Ի������
         */
        dialog: {
        	event: {
				close: function() {
					$(this).dialog('close');
				}
			},
            /**
             * ��ť��ط���
             */
            button: {
                /**
                 * Ϊdialog�е�button����icon�������ṹ
                 * [{
                 *     text: '�ݴ�',
                 *     title: '��ʱ�洢',
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

                        // �ָ���
                        try {
                            if (v.division) {
                                var position = v.division || 'after';
                                if (position == 'before') {
                                    $btn.before("<span class='ui-state-error ui-dialog-button-devision'></span>");
                                } else if (position == 'after') {
                                    $btn.after("<span class='ui-state-error ui-dialog-button-devision'></span>");
                                } else {
                                    alert('δ֪λ�ã�' + position);
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
                                alert('������position|by dialog.button.group');
                            }
                        } catch (e) {
                        }
                    });
                }
            },
            /**
             * ��������������ȡwindow�ĸ߶�
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
             * ��������������ȡ���öԻ���ĸ߶�
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
         * ѡ����
         */
        tab: {
            /**
             * �Զ�����ѡ��ĸ߶�
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
                 * ���Ĵ�����
                 */
                function innerDeal() {
                    // ��IEĬ��ֵ
                    var gap = 80;
                    // ���⴦��IE
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

                // ���ڴ�С�ı��ʱ��
                window.onresize = innerDeal;

            }
        },

		/**
		 * һЩ��Ч
		 */
		effect: {

			/*
			 * ����Ч��
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
		 * �Զ����
		 */
		autocomplete: {

			/*
			 * �����Զ����
			 */
			triggerSearch: function(e) {
				if ($(this).val().length > 0) {
            		$(this).autocomplete('search', $(this).val());
            	}
			},

			/*
			 * ȡ���Զ���ɵĽ�������triggerSearchʹ�ã�Ŀ��������ı����ȡ����֮����Զ�����������������������ѭ������
			 */
			cancelResult: function(ele) {
				setTimeout(function() {
					$('.ui-autocomplete').hide();
				}, 50);
			}
		}
    };
    /*******************************************/
    /**			jQuery UI--����	  			  **/
    /*******************************************/
