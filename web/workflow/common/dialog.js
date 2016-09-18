//模态窗口插件
(function($) {
    $.Dialog = {
        option: {
            title: "dialog",
            height: "300",
            width: "500",
            modal: true,
            resizable: false,
            external: true,
            draggable: true
        },

        open: function(url, dlgid, options) {
            var op = $.extend({}, $.Dialog.option, options);
            dlgid += new Date().getTime(); // 加入当前时间戳，建立动态ID避免ID冲突
            var tempDialog = document.getElementById(dlgid);
            if (tempDialog != undefined && tempDialog != null) {
                $dialog = $(tempDialog);
            } else {
                $body = $("body");
                $div = $("<div style='padding:0; overflow:hidden;'></div>");
                $div.attr("id", dlgid);
                $body.append($div);
                $dialog = $("#" + dlgid);
            }

            if (op.external) {
                var iframeWidth = parseInt(op.width) - 10;
                var iframeHeight = parseInt(op.height);
                iframe = $("<iframe>").attr({ "src": url, "frameborder": "0", "marginwidth": "0", "marginheight": "0" })
                .attr("iframeId","iframe"+dlgid)
                .css({ "width": iframeWidth + "px", "height": iframeHeight + "px"});
                $dialog.html(iframe);
            } else {
                //嵌入html
                alert("不支持external: false的模式");
            }
            $dialog.dialog(op);

            $dialog.height(parseInt(op.height) - $($dialog).siblings(".ui-dialog-titlebar").outerHeight());
            iframe.height(parseInt(op.height) - $($dialog).siblings(".ui-dialog-titlebar").outerHeight());

            if (op.param) $dialog.data("param", op.param);
            $dialog.data(dlgid + "callback", options.callback);
            $("body").data(dlgid, $dialog);

            var iframeObj = $("#" + dlgid + " iframe").get(0).contentWindow;

            //在iframe的页面中创建window.dialogVal方法，在iframe中使用window.dialogVal()取得传递的参数
            iframeObj.dialogVal = function() {
                if ($dialog.data("param") != undefined && $dialog.data("param") != null) {
                    return $dialog.data("param");
                } else {
                    return "";
                }
            }
            //在iframe的页面中创建window.closeDialog方法，在iframe中使用window.closeDialog()关闭窗口
            iframeObj.closeDialog = function() {
                $.Dialog.close(dlgid);
            }

            //在iframe的页面中创建window.closeDialogAndReturn方法，
            //在iframe中使用window.closeDialogAndReturn(returnValue)关闭窗口并返回参数,returnValue为返回的参数
            iframeObj.closeDialogAndReturn = function(returnValue) {
                $.Dialog.closeAndReturnVal(dlgid, returnValue);
            }

            //除去原有的关闭事件并绑定自定义关闭动作
            $("#" + dlgid).siblings(".ui-dialog-titlebar").find(".ui-dialog-titlebar-close").unbind().bind("click", function() {
                $.Dialog.close(dlgid);
            });
        },
        openImg: function(url, options) {
            var html =
            '<div class="dialog" id="dialog-window" title="图片">' +
 		      '<img src="' + url + '" alt="" />'+
            // ' <iframe src="' + url + '" frameBorder="0" style="border: 0; " scrolling="auto" width="100%" height="100%"></iframe>' +
            '</div>';
            return $(html).dialog($.extend({
              modal: true,
              closeOnEscape: false,
              draggable: true,
              resizable: false,
              close: function(event, ui) {
                $(this).dialog("destroy"); // 关闭时销毁
              }
            }, options));
          },
        //关闭dialog
        close: function(dialog) {
            if (typeof dialog == 'string') {
                dialog = $("body").data(dialog);
            }
            if (dialog == undefined || dialog == null) {
                dialog = $("#" + dialog);
            }
            $("iframe", dialog).remove();
            dialog.dialog("close");
            dialog.parent().remove();  //移除新建的窗口
            dialog.remove();
        },
        //关闭dialog并执行返回函数
        closeAndReturnVal: function(dlgid, returnValue) {
            var dialog = dlgid;
            if (typeof dialog == 'string') {
                dialog = $("body").data(dialog);
            }
            if (dialog == undefined || dialog == null) {
                dialog = $("#" + dialog);
            }
            if ($(dialog).data(dlgid + "callback") != null) {
                $(dialog).data(dlgid + "callback").apply(this, [returnValue]);  //执行回调函数并返回value,value可自行定义，在回调函数中自行操作
            }
            $.Dialog.close(dialog);
        }
    };

})(jQuery); 