var _language = commonData.language;
var _oldMenu;

$(document).ready(function () {

    loadAllMenuLevel_0();
    loadAllMenuLevel_1();
    loadAllMenuLevel_2();

    $('.icp-dd').each(function () {
        $(this).iconpicker({
            container: $(' ~ .dropdown-menu:first', $(this))
        });
    });
    $('.iconpicker-item').on('click', function () {
        var icon = $(this).attr('title').substr(1);
        $('#ddlAddMenuIcon').attr('class', 'fa ' + icon);
        $('#ddlEditMenuIcon').attr('class', 'fa ' + icon);
    });
    $('.popover-title').hide();

});

// Add menu ------------------------------------------------------------------------------------------------------------
$('#btnAddMenuLv0').click(function () {
    $('#mdAddMenu').attr('level', 0);
    openModalAddMenu(0);
});
$('#btnAddMenuLv1').click(function () {
    $('#mdAddMenu').attr('level', 1);
    openModalAddMenu(1);
});
$('#btnAddMenuLv2').click(function () {
    $('#mdAddMenu').attr('level', 2);
    openModalAddMenu(2);
});

$('#btnCancelAdd').click(function () {
    $('#mdAddMenu').modal('hide');
});
$('#btnSaveAddMenu').click(function () {
    var link = $('#txtAddLink').val();
    var menuTh = $('#txtAddMenuTh').val();
    var menuEn = $('#txtAddMenuEn').val();
    var controller = $('#txtAddController').val();
    var menuLv = $('#mdAddMenu').attr('level');
    var sequent = $('#txtAddSequent').val();
    var parent = $('#ddlAddParent').val();
    var menuIcon = $('#ddlAddMenuIcon').attr('class').split(' ')[1];
	if(menuIcon == null || menuIcon == undefined) {
		menuIcon = '';
	}
    var arrRoleId = [];
    $('[id^=chkAddRole_]:checked').each(function () {
        var id = this.id.split('_')[1];
        arrRoleId.push(id);
    });

    if (link == '') {
        $('#txtAddLink').attr('data-content', MESSAGE.COMPLETE_FIELD).popover('show');
    } else if (menuTh == '') {
        $('#txtAddMenuTh').attr('data-content', MESSAGE.COMPLETE_FIELD).popover('show');
    } else if (menuEn == '') {
        $('#txtAddMenuEn').attr('data-content', MESSAGE.COMPLETE_FIELD).popover('show');
    } else if (controller == '') {
        $('#txtAddController').attr('data-content', MESSAGE.COMPLETE_FIELD).popover('show');
    } else if (sequent == '') {
        $('#txtAddSequent').attr('data-content', MESSAGE.COMPLETE_FIELD).popover('show');
    } else if (!$.isNumeric(sequent) || sequent.indexOf('.') >= 0 || sequent < 1) {
        $('#txtAddSequent').attr('data-content', MESSAGE.COMPLETE_NUMBER_GT_0).popover('show');
    } else if (menuLv > 0 && parent == 0) {
        $('#ddlAddParent').attr('data-content', MESSAGE.COMPLETE_FIELD).popover('show');
    } else if (menuLv == 0 && (menuIcon == undefined || menuIcon == null || menuIcon == '')) {
        bootbox.alert(MESSAGE.ALERT_MENU_ICON);
    } else if (arrRoleId.length == 0) {
        bootbox.alert(MESSAGE.ALERT_MENU_ROLE);
    } else {
        $.ajax({
            type: "POST",
            contentType: "application/json; charset=UTF-8",
            dataType: "json",
            headers: {
                Accept: "application/json"
            },
            url: contextPath + '/appmenus/insertAppMenu',
            data: JSON.stringify({
                link: link,
                menuTh: menuTh,
                menuEn: menuEn,
                controller: controller,
                level: menuLv,
                sequent: sequent,
                parent: parent,
                menuIcon: menuIcon,
                arrRoleId: arrRoleId
            }),
            success: function (data, status, xhr) {
                if (xhr.status === 201) {
                    if (data.rowCountSameLink > 0) {
                        bootbox.alert(MESSAGE.DUPLICATE_LINK);
                    } else if (data.rowCountSameController > 0) {
                        bootbox.alert(MESSAGE.DUPLICATE_CONTROLLER);
                    } else if (data.rowCountSameSequent > 0) {
                        bootbox.alert(MESSAGE.DUPLICATE_SEQUENT);
                    }

                    if (data.rowCountSameLink == 0 && data.rowCountSameController == 0 && data.rowCountSameSequent == 0) {
                        bootbox.alert(MESSAGE.ALERT_SAVE_COMPLETED);
                        $('#mdAddMenu').modal('hide');
                        if (menuLv == 0) {
                            loadAllMenuLevel_0();
                        } else if (menuLv == 1) {
                            loadAllMenuLevel_1();
                        } else if (menuLv == 2) {
                            loadAllMenuLevel_2();
                        }
                    }
                } else {
                    bootbox.alert(MESSAGE.ALERT_SAVE_FAILED);
                }
            },
            error: functon(){
                bootbox.alert(MESSAGE.ALERT_SAVE_FAILED);
            },
            async: false
        });
    }
});

// Edit menu -----------------------------------------------------------------------------------------------------------
$('#tbAppMenuLv0').on('click', '[id^=btnEditMenu_]', function () {
    var id = this.id.split('_')[1];
    $('#mdEditMenu').attr('level', 0);
    openModalEditMenu(id, 0);
});
$('#tbAppMenuLv1').on('click', '[id^=btnEditMenu_]', function () {
    var id = this.id.split('_')[1];
    $('#mdEditMenu').attr('level', 1);
    openModalEditMenu(id, 1);
});
$('#tbAppMenuLv2').on('click', '[id^=btnEditMenu_]', function () {
    var id = this.id.split('_')[1];
    $('#mdEditMenu').attr('level', 2);
    openModalEditMenu(id, 2);
});

$('#btnCancelEdit').click(function () {
    if(checkChangeData()) {
        bootbox.confirm(MESSAGE.ALERT_WAS_CHANGED, function (result) {
            if (result) {
                $('#mdEditMenu').modal('hide');
            }
        });
    } else {
        $('#mdEditMenu').modal('hide');
    }
});
$('#btnSaveEditMenu').click(function () {
    var id = $('#hiddenEditMenuId').val();
    var link = $('#txtEditLink').val();
    var menuTh = $('#txtEditMenuTh').val();
    var menuEn = $('#txtEditMenuEn').val();
    var controller = $('#txtEditController').val();
    var menuLv = $('#mdEditMenu').attr('level');
    var sequent = $('#txtEditSequent').val();
    var parent = $('#ddlEditParent').val();
    var menuIcon = $('#ddlEditMenuIcon').attr('class').split(' ')[1];

    var roles = [];
    $('[id^=chkEditRole_]').each(function () {
        var id = this.id.split('_')[1];
        roles.push({
            roleId: id,
            used: $(this).prop('checked')
        });
    });
    if (link == '') {
        $('#txtEditLink').attr('data-content', MESSAGE.COMPLETE_FIELD).popover('show');
    } else if (menuTh == '') {
        $('#txtEditMenuTh').attr('data-content', MESSAGE.COMPLETE_FIELD).popover('show');
    } else if (menuEn == '') {
        $('#txtEditMenuEn').attr('data-content', MESSAGE.COMPLETE_FIELD).popover('show');
    } else if (controller == '') {
        $('#txtEditController').attr('data-content', MESSAGE.COMPLETE_FIELD).popover('show');
    } else if (sequent == '') {
        $('#txtEditSequent').attr('data-content', MESSAGE.COMPLETE_FIELD).popover('show');
    } else if (!$.isNumeric(sequent) || sequent.indexOf('.') >= 0 || sequent < 1) {
        $('#txtEditSequent').attr('data-content', MESSAGE.COMPLETE_NUMBER_GT_0).popover('show');
    } else if (menuLv > 0 && parent == 0) {
        $('#ddlEditParent').attr('data-content', MESSAGE.COMPLETE_FIELD).popover('show');
    } else if (menuLv == 0 && (menuIcon == undefined || menuIcon == null || menuIcon == '')) {
        bootbox.alert(MESSAGE.ALERT_MENU_ICON);
    } else if ($('[id^=chkEditRole_]:checked').length == 0) {
        bootbox.alert(MESSAGE.ALERT_MENU_ROLE);
    } else {
        if(checkChangeData()) {
            $.ajax({
                type: "POST",
                contentType: "application/json; charset=UTF-8",
                dataType: "json",
                headers: {
                    Accept: "application/json"
                },
                url: contextPath + '/appmenus/updateAppMenu',
                data: JSON.stringify({
                        menuId: id,
                        link: link,
                        menuTh: menuTh,
                        menuEn: menuEn,
                        controller: controller,
                        level: menuLv,
                        sequent: sequent,
                        parent: parent,
                        menuIcon: menuIcon,
                        roles: roles
                    }
                ),
                success: function (data, status, xhr) {
                    if (xhr.status === 200) {
                        if (data.rowCountSameLink > 0) {
                            bootbox.alert(MESSAGE.DUPLICATE_LINK);
                        } else if (data.rowCountSameController > 0) {
                            bootbox.alert(MESSAGE.DUPLICATE_CONTROLLER);
                        } else if (data.rowCountSameSequent > 0) {
                            bootbox.alert(MESSAGE.DUPLICATE_SEQUENT);
                        }

                        if (data.rowCountSameLink == 0 && data.rowCountSameController == 0 && data.rowCountSameSequent == 0) {
                            bootbox.alert(MESSAGE.ALERT_SAVE_COMPLETED);
                            $('#mdEditMenu').modal('hide');

                            if (menuLv == 0) {
                                loadAllMenuLevel_0();
                            } else if (menuLv == 1) {
                                loadAllMenuLevel_1();
                            } else if (menuLv == 2) {
                                loadAllMenuLevel_2();
                            }
                        }
                    } else {
                        bootbox.alert(MESSAGE.ALERT_SAVE_FAILED);
                    }
                },
                error: function(){
                    bootbox.alert(MESSAGE.ALERT_SAVE_FAILED);
                },
                async: false
            });
        }else{
            bootbox.alert(MESSAGE.ALERT_NO_CHANGE);
        }

    }
});

// Delete menu ---------------------------------------------------------------------------------------------------------
$('#btnDeleteMenuLv0').click(function () {
    var arrMenuId = [];
    $('[id^=chkMenuLv0_]:checked').each(function () {
        arrMenuId.push(this.id.split('_')[1]);
    });

    if (arrMenuId.length > 0) {
        bootbox.confirm(MESSAGE.ALERT_CONFIRM_DELETE, function (result) {
            if (result) {
                deleteMenu(arrMenuId, 0);
            }
        });
    } else {
        bootbox.alert(MESSAGE.ALERT_SELECT_ROW);
    }
});
$('#btnDeleteMenuLv1').click(function () {
    var arrMenuId = [];
    $('[id^=chkMenuLv1_]:checked').each(function () {
        arrMenuId.push(this.id.split('_')[1]);
    });

    if (arrMenuId.length > 0) {
        bootbox.confirm(MESSAGE.ALERT_CONFIRM_DELETE, function (result) {
            if (result) {
                deleteMenu(arrMenuId, 1);
            }
        });
    } else {
        bootbox.alert(MESSAGE.ALERT_SELECT_ROW);
    }
});
$('#btnDeleteMenuLv2').click(function () {
    var arrMenuId = [];
    $('[id^=chkMenuLv2_]:checked').each(function () {
        arrMenuId.push(this.id.split('_')[1]);
    });

    if (arrMenuId.length > 0) {
        bootbox.confirm(MESSAGE.ALERT_CONFIRM_DELETE, function (result) {
            if (result) {
                deleteMenu(arrMenuId, 2);
            }
        });
    } else {
        bootbox.alert(MESSAGE.ALERT_SELECT_ROW);
    }
});

// Checkbox management -------------------------------------------------------------------------------------------------
$('#chkCheckAllLv0').change(function () {
    var isCheckAll = $('#chkCheckAllLv0').prop('checked');
    $('[id^=chkMenuLv0_]').prop('checked', isCheckAll);
});
$('#chkCheckAllLv1').change(function () {
    var isCheckAll = $('#chkCheckAllLv1').prop('checked');
    $('[id^=chkMenuLv1_]').prop('checked', isCheckAll);
});
$('#chkCheckAllLv2').change(function () {
    var isCheckAll = $('#chkCheckAllLv2').prop('checked');
    $('[id^=chkMenuLv2_]').prop('checked', isCheckAll);
});

$('#tbAppMenuLv0').on('change', '[id^=chkMenuLv0_]', function () {
    var chkChecked = $('[id^=chkMenuLv0_]:checked');
    var chkAll = $('[id^=chkMenuLv0_]');

    if (chkChecked.length == chkAll.length) {
        $('#chkCheckAllLv0').prop('checked', true);
    } else {
        $('#chkCheckAllLv0').prop('checked', false);
    }
});
$('#tbAppMenuLv1').on('change', '[id^=chkMenuLv1_]', function () {
    var chkChecked = $('[id^=chkMenuLv1_]:checked');
    var chkAll = $('[id^=chkMenuLv1_]');

    if (chkChecked.length == chkAll.length) {
        $('#chkCheckAllLv1').prop('checked', true);
    } else {
        $('#chkCheckAllLv1').prop('checked', false);
    }
});
$('#tbAppMenuLv2').on('change', '[id^=chkMenuLv2_]', function () {
    var chkChecked = $('[id^=chkMenuLv2_]:checked');
    var chkAll = $('[id^=chkMenuLv2_]');

    if (chkChecked.length == chkAll.length) {
        $('#chkCheckAllLv2').prop('checked', true);
    } else {
        $('#chkCheckAllLv2').prop('checked', false);
    }
});

// ---------------------------------------------------------------------------------------------------------------------
function loadMenuParent(idDDLParent, lvlMenuParent) {
    if (lvlMenuParent >= 0) {
        $.ajax({
            type: "GET",
            contentType: "application/json; charset=UTF-8",
            dataType: "json",
            headers: {
                Accept: "application/json"
            },
            url: contextPath + '/appmenus/findMenuParent?level=' + lvlMenuParent,
            success: function (data, status, xhr) {
                if (xhr.status === 200) {
                    $('#' + idDDLParent).children('[value!=0]').remove();
                    $.each(data, function (k, v) {
                        var parentName = '';
                        if (_language == 'TH') {
                            parentName = v.menu_th;
                        } else {
                            parentName = v.menu_en;
                        }
                        $('#' + idDDLParent).append('<option value="' + v.id + '">' + parentName + '</option>');
                    });
                }
            },
            async: false
        });
    } else {
        $('#' + idDDLParent).children('[value!=0]').remove();
    }

}
function loadAllRole(idGroupRule, idCheckboxBase) {
    $.ajax({
        type: "GET",
        contentType: "application/json; charset=UTF-8",
        dataType: "json",
        headers: {
            Accept: "application/json"
        },
        url: contextPath + '/appmenus/findAllRole',
        success: function (data, status, xhr) {
            if (xhr.status === 200) {
                $('#' + idGroupRule).empty();
                $.each(data, function (k, v) {
                    $('#' + idGroupRule).append('<div class="checkbox">' +
                        '<label class="col-sm-12">' +
                        '<input id="' + idCheckboxBase + v.id + '" type="checkbox" />' + v.roleName +
                        '</label>' +
                        '</div>');
                });
            }
        },
        async: false
    });
}

function openModalAddMenu(menuLevel) {
    $('#txtAddLink').val('').popover('hide');
    $('#txtAddMenuTh').val('').popover('hide');
    $('#txtAddMenuEn').val('').popover('hide');
    $('#txtAddController').val('').popover('hide');
    $('#txtAddSequent').val('').popover('hide');
    $('#ddlAddParent').popover('hide');

    if (menuLevel == 0) {
        $('#ddlAddMenuIcon').attr('class', 'fa fa-adjust');
        $('#ddlAddMenuIcon').parent().parent().parent().parent().show();
        $('#ddlAddParent').parent().parent().hide();
    } else {
        $('#ddlAddMenuIcon').parent().parent().parent().parent().hide();
        $('#ddlAddParent').parent().parent().show();
        $('#ddlAddMenuIcon').attr('class', '');
    }
    loadMenuParent('ddlAddParent', menuLevel - 1);

    loadAllRole('grpAddRule', 'chkAddRole_');
    $('#mdAddMenu').modal({backdrop: 'static'});

    // focus 1st element
    setTimeout(function () {
        $('#txtAddLink').focus();
    }, 500);
}
function openModalEditMenu(menuId, menuLevel) {

    loadMenuParent('ddlEditParent', menuLevel - 1);
    loadAllRole('grpEditRule', 'chkEditRole_');

    if (menuLevel == 0) {
        $('#ddlEditMenuIcon').attr('class', '');
        $('#ddlEditMenuIcon').parent().parent().parent().parent().show();
        $('#ddlEditParent').parent().parent().hide();
    } else {
        $('#ddlEditMenuIcon').parent().parent().parent().parent().hide();
        $('#ddlEditParent').parent().parent().show();
        $('#ddlEditMenuIcon').attr('class', '');
    }

    var menu = null;
    $.ajax({
        type: "GET",
        contentType: "application/json; charset=UTF-8",
        dataType: "json",
        headers: {
            Accept: "application/json"
        },
        url: contextPath + '/appmenus/findMenu?id=' + menuId,
        success: function (data, status, xhr) {
            menu = data;
            _oldMenu = data;
        },
        async: false
    });

    $('#hiddenEditMenuId').val(menu.id);
    $('#txtEditLink').val(menu.link).popover('hide');
    $('#txtEditMenuTh').val(menu.menu_t_name).popover('hide');
    $('#txtEditMenuEn').val(menu.menu_e_name).popover('hide');
    $('#txtEditController').val(menu.controller).popover('hide');
    $('#txtEditSequent').val(menu.sequent).popover('hide');
    $('#ddlEditParent').val(menu.parent).popover('hide');
    $('#ddlEditMenuIcon').attr('class', 'fa ' + menu.menuIcon);
    $('[id^=chkEditRole_]').prop('checked', false);                     // clear check
    menu.role.forEach(function (roleId) {                               // loop add check
        $('[id^=chkEditRole_' + roleId + ']').prop('checked', true);
    });

    $('#mdEditMenu').modal({backdrop: 'static'});

    // focus 1st element
    setTimeout(function () {
        $('#txtEditLink').focus();
    }, 200);
}

function deleteMenu(arrMenuId, menuLv) {
    $.ajax({
            type: "POST",
            contentType: "application/json; charset=UTF-8",
            dataType: "json",
            headers: {
                Accept: "application/json"
            },
            url: contextPath + '/appmenus/deleteAppMenu',
            data: JSON.stringify(arrMenuId),
            success: function (data, status, xhr) {
                bootbox.alert(MESSAGE.ALERT_DELETE_COMPLETED);
                loadAllMenuLevel_0();
                loadAllMenuLevel_1();
                loadAllMenuLevel_2();
            },
            async: false
        }
    );
}

function checkChangeData(){
    var link = $('#txtEditLink').val();
    var menuTh = $('#txtEditMenuTh').val();
    var menuEn = $('#txtEditMenuEn').val();
    var controller = $('#txtEditController').val();
    var sequent = $('#txtEditSequent').val();
    var parent = $('#ddlEditParent').val();
    var icon = $('#ddlEditMenuIcon').attr('class').split(' ')[1];
    var roles = [];
    $('[id^=chkEditRole_]:checked').each(function(){
        roles.push(this.id.split('_')[1]);
    });

    var changeRole = false;
    if(roles.length != _oldMenu.role.length) {
        changeRole = true;
    } else {
        $.each(roles, function(k, v) {
            if(!$.inArray(v, _oldMenu.role))
                changeRole = true;
        });
    }
    if(link != _oldMenu.link || menuTh != _oldMenu.menu_t_name || menuEn != _oldMenu.menu_e_name ||
        controller != _oldMenu.controller || sequent != _oldMenu.sequent || parent != _oldMenu.parent ||
        icon != _oldMenu.menuIcon || changeRole) {
        return true;
    }
    return false;
}
