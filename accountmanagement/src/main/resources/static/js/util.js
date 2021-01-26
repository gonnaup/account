/**
 * 工具js
 */

/**
 * 获取Jwt
 * @returns {string}
 */
function obtainJwt() {
    return localStorage.getItem(JWT_LOCALSTORAGENAME)
}

/**
 * 填充账号信息
 * @param account
 */
function fillAccountInformation(account) {
    var $ = layui.jquery;
    $("#login_entrance").css('display', 'none')
    $("#account_information").css('display', 'inline-block')
    $("#account_avatar").attr('src', account.accountAvatar || '../default_avatar.png')
    $("#account_nickname").html(account.accountNickname)
    $("#login_entrance_logo").html("请登录")
}

/**
 * 清除账号信息
 */
function cleanAccountInformation() {
    var $ = layui.jquery
    $("#login_entrance").css('display', 'inline-block')
    $("#account_information").css('display', 'none')
    $("#account_avatar").attr('src', '')
    $("#account_nickname").html('')
}

/**
 * 禁用button
 * @param id
 */
function disabeButton(id) {
    var $ = layui.jquery
    let button = $(id);
    if (button) {
        button.addClass('layui-btn-disabled')
        button.attr('disabled', true)
    }
}

/**
 * 关闭所有页面层
 */
function closeAllLayerPage() {
    var layer = layui.layer
    layer.closeAll('page')
}
/////////////////////// layer //////////////////////////
/**
 * 操作成功提示
 */
function operateSuccessMsg() {
    var layer = layui.layer
    layer.msg('操作成功', {icon: 1, time: 3000})
}

function alertMsg(msg) {
    var layer = layui.layer
    layer.alert(msg, {icon: 7})
}

/**
 * 动态加载下拉框
 * @param id select元素id
 * @param url 数据url
 * @param type 请求类型，默认'get'
 * 数据返回类型[{id: xxx, name: xxx}]
 */
function renderSelect(id, url, type) {
    var type = type || 'get'
    var $ = layui.jquery
    var jwt = obtainJwt() || ''
    $.ajax({
        url: url,
        type: type,
        headers: {token_jwt: jwt},
        success: function (data) {
            var data = data.data
            var node = $('#' + id)//select节点
            data.forEach(function (obj) {
                node.append("<option value='" + obj.id + "'>" + obj.name + "</option>")
            })
            layui.form.render("select")//重新渲染select
        }
    })
}

/**
 * 处理应用名选择框
 * 如果是ADMIN用户则显示并渲染应用名下拉框，否则隐藏下拉框
 */
function handleAppNameSelect(blockId, selectId) {
    var api = '../api/applicationCode/listAll'//数据api
    if(isAdmin()) {
        renderSelect(selectId, api)
    } else {
        var $ = layui.jquery
        $('#' + blockId).css('display', 'none')
    }
}

/**
 * 验证表格中选中的行数
 * @param rowData
 */
function selectOneRowDataVerify(rowData) {
    if (rowData.length < 1) {
        alertMsg('请选中一行数据')
        return false
    }
    if (rowData.length > 1) {
        alertMsg('只能选中一行数据')
        return false
    }
    return true
}

/**
 * 获取表格选中行的数据
 * @param tableId
 */
function obtainTableSelectedRowData(tableId) {
    var table = layui.table
    return table.checkStatus(tableId).data
}
