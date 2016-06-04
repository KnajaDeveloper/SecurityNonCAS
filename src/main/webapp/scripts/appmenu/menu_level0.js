var pagginationAppMenuLv0 = $.extend({}, UtilPaggination);

pagginationAppMenuLv0.setEventPaggingBtn("paggingAppMenuLv0", pagginationAppMenuLv0);
pagginationAppMenuLv0.loadTable = function loadTable(jsonData) {
    $('#tbAppMenuLv0').empty();
    $('#chkCheckAllLv0').prop('checked', false);

    if (jsonData.length <= 0) {
        $('#tbAppMenuLv0').append('<tr><td class="text-center" colspan="8">' + LABEL.NO_RESULT + '</td></tr>');
    } else {
        jsonData.forEach(function (v) {
            $('#tbAppMenuLv0').append('<tr>' +
                '<td class="text-center">' +
                '<input type="checkbox" id="chkMenuLv0_' + v.id + '"/>' +
                '</td>' +
                '<td class="text-center">' +
                '<button id="btnEditMenu_' + v.id + '" type="button" class="btn btn-xs btn-info"><span class="glyphicon glyphicon-pencil"><span/></button>' +
                '</td>' +
                '<td class="text-center">' + v.segment + '</td>' +
                '<td>' + v.link + '</td>' +
                '<td class="text-center"><span class="fa ' + v.menuIcon + '"></span></td>' +
                '<td>' + v.menu_t_name + '</td>' +
                '<td>' + v.menu_e_name + '</td>' +
                '<td>' + v.controller + '</td>' +
                '</tr>'
            );
        });
    }
};

function loadAllMenuLevel_0() {
    var pageNo = $('#paggingAppMenuLv0CurrentPage').val();
    if(pageNo == 0)
        pageNo = 1;

    pagginationAppMenuLv0.setOptionJsonData({
        url: contextPath + "/appmenus/findPaggingDataAppMenu",
        data: {level: 0}
    });
    pagginationAppMenuLv0.setOptionJsonSize({
        url: contextPath + "/appmenus/findPaggingSizeAppMenu",
        data: {level: 0}
    });
    pagginationAppMenuLv0.search(pagginationAppMenuLv0);

    pagginationAppMenuLv0.loadPage(pageNo, pagginationAppMenuLv0);
}



