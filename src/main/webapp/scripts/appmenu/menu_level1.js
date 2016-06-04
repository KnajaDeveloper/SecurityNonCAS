var pagginationAppMenuLv1 = $.extend({}, UtilPaggination);

pagginationAppMenuLv1.setEventPaggingBtn("paggingAppMenuLv1", pagginationAppMenuLv1);
pagginationAppMenuLv1.loadTable = function loadTable(jsonData) {
    $('#tbAppMenuLv1').empty();
    $('#chkCheckAllLv1').prop('checked', false);
    
    if (jsonData.length <= 0) {
        $('#tbAppMenuLv1').append('<tr><td class="text-center" colspan="8">' + LABEL.NO_RESULT + '</td></tr>');
    } else {
        jsonData.forEach(function (v) {
            var parentMenuName = 'ไม่มี';
            if (v.parent != 0) {
                if (_language == 'TH') {
                    parentMenuName = v.parent_t_name;
                } else {
                    parentMenuName = v.parent_e_name;
                }
            }

            $('#tbAppMenuLv1').append('<tr>' +
                '<td class="text-center">' +
                '<input type="checkbox" id="chkMenuLv1_' + v.id + '"/>' +
                '</td>' +
                '<td class="text-center">' +
                '<button id="btnEditMenu_' + v.id + '" type="button" class="btn btn-xs btn-info"><span class="glyphicon glyphicon-pencil"><span/></button>' +
                '</td>' +
                '<td class="text-center">' + v.segment + '</td>' +
                '<td>' + v.link + '</td>' +
                '<td>' + v.menu_t_name + '</td>' +
                '<td>' + v.menu_e_name + '</td>' +
                '<td>' + v.controller + '</td>' +
                '<td>' + parentMenuName + '</td>' +
                '</tr>'
            );
        });
    }
};

function loadAllMenuLevel_1() {
    var pageNo = $('#paggingAppMenuLv1CurrentPage').val();
    if(pageNo == 0)
        pageNo = 1;

    pagginationAppMenuLv1.setOptionJsonData({
        url: contextPath + "/appmenus/findPaggingDataAppMenu",
        data: {level: 1}
    });
    pagginationAppMenuLv1.setOptionJsonSize({
        url: contextPath + "/appmenus/findPaggingSizeAppMenu",
        data: {level: 1}
    });
    pagginationAppMenuLv1.search(pagginationAppMenuLv1);

    pagginationAppMenuLv1.loadPage(pageNo, pagginationAppMenuLv1);
}



