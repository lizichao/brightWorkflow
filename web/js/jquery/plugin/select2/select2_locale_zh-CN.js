/**
 * Select2 Chinese translation
 */
(function ($) {
    "use strict";
    $.fn.select2.locales['zh-CN'] = {
        formatNoMatches: function () { return "û���ҵ�ƥ����"; },
        formatInputTooShort: function (input, min) { var n = min - input.length; return "��������" + n + "���ַ�";},
        formatInputTooLong: function (input, max) { var n = input.length - max; return "��ɾ��" + n + "���ַ�";},
        formatSelectionTooBig: function (limit) { return "��ֻ��ѡ�����" + limit + "��"; },
        formatLoadMore: function (pageNumber) { return "���ؽ���С�"; },
        formatSearching: function () { return "�����С�"; }
    };

    $.extend($.fn.select2.defaults, $.fn.select2.locales['zh-CN']);
})(jQuery);