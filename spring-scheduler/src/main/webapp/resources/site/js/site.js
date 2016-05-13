/**
 * Created by root on 2016/2/29.
 */

$(function () {

    // initialize tab panel
    $('ul[class="nav nav-tabs"]').find('li').each(function () {
        var target = $('#' + $(this).attr('data-target'));
        if ($(this).hasClass('active')) {
            target.show();
        } else {
            target.hide();
        }
        $(this).click(function () {
            $(this).siblings().each(function () {
                $(this).removeClass('active');
                $('#' + $(this).attr('data-target')).hide();
            });
            $(this).addClass('active');
            $('#' + $(this).attr('data-target')).show();
        });
    });

    // initial collapse panel
    $('a[data-toggle="collapse"]').each(function () {
        if (Boolean($(this).attr('data-spread'))) {
            $($(this).attr('href')).collapse('show');
        }
    })
});


var HtmlUtil = {
    htmlEncodeByRegExp: function (str) {
        var s = "";
        if (str.length == 0) return "";
        s = str.replace(/&/g, "&amp;");
        s = s.replace(/</g, "&lt;");
        s = s.replace(/>/g, "&gt;");
        s = s.replace(/ /g, "&nbsp;");
        s = s.replace(/\'/g, "&#39;");
        s = s.replace(/\"/g, "&quot;");
        return s;
    },
    htmlDecodeByRegExp: function (str) {
        var s = "";
        if (str.length == 0) return "";
        s = str.replace(/&amp;/g, "&");
        s = s.replace(/&lt;/g, "<");
        s = s.replace(/&gt;/g, ">");
        s = s.replace(/&nbsp;/g, " ");
        s = s.replace(/&#39;/g, "\'");
        s = s.replace(/&quot;/g, "\"");
        return s;
    }
};

function truncate(str, length) {
    return str.substring(0, length) + '...';
}