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
function renderSelect(id, url, type, callback) {
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
                node.append("<option value='" + obj.value + "'>" + obj.name + "</option>")
            })
            layui.form.render("select")//重新渲染select
            if (callback) {
                callback()
            }
        }
    })
}

/**
 * 处理应用名选择框
 * 如果是ADMIN用户则显示并渲染应用名下拉框，否则隐藏下拉框
 * @param blockId 下拉框所属<div/>ID
 * @param selectId 下拉框ID
 * @param adminCallback 角色为admin时，渲染完成后的回调
 * @param commonCallback 普通用户的回调
 */
function handleAppNameSelect(blockId, selectId, adminCallback, commonCallback) {
    var api = '../api/applicationCode/listAll'//数据api
    if (isAdmin()) {
        renderSelect(selectId, api, 'get', adminCallback)
    } else {
        var $ = layui.jquery
        $('#' + blockId).css('display', 'none')
        if (commonCallback) {
            commonCallback()
        }
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

/**
 * 数据新增方法
 * @param url 请求api
 * @param data 数据，表格原始数据
 * @param tableId 需要刷新的表格ID
 */
function addOp(url, data, tableId) {
    saveOp(url, data, tableId, 'post')
}

/**
 * 数据修改方法
 * @param url 请求api
 * @param data 数据，表格原始数据
 * @param tableId 需要刷新的表格ID
 */
function updateOp(url, data, tableId) {
    saveOp(url, data, tableId, 'put')
}

function saveOp(url, data, tableId, type) {
    var $ = layui.jquery
    $.ajax({
        url: url,
        type: type,
        data: JSON.stringify(data),
        headers: {token_jwt: obtainJwt() || ''},
        contentType: 'application/json',
        success: function (data) {
            closeAllLayerPage()//关闭layer
            operateSuccessMsg()
            layui.table.reload(tableId)
        }
    })
}

/**
 * 删除数据方法
 * @param url 请求api
 * @param tableId 需要刷新的表格ID
 * @param callback 成功后的回调函数
 */
function deleteOp(url, tableId, callback) {
    var layer = layui.layer
    layer.confirm('是否删除数据？', {icon: 3, btn: ['确认', '取消']},
        function (index) {
            layer.close(index)//关闭layer
            var $ = layui.jquery
            $.ajax({
                url: url,
                type: 'delete',
                headers: {token_jwt: obtainJwt() || ''},
                success: function (data) {
                    operateSuccessMsg()
                    layui.table.reload(tableId)
                    if (callback != undefined) {
                        callback()
                    }
                }
            })
        })
}

/**
 * 移除下拉框除第一个选项的所有选项，用于下拉框刷新
 * @param selectId
 */
function removeOptionExceptFirst(selectId) {
    removeOptionExceptFirstN(selectId, 1)
}

/**
 * 移除除前n个以外的选项
 * @param selectId 选择器ID
 * @param exceptIndex 前面不移除的选项个数
 */
function removeOptionExceptFirstN(selectId, exceptNunber) {
    var selectNode = document.getElementById(selectId)
    while (selectNode.childElementCount > (exceptNunber || 0)) {
        selectNode.removeChild(selectNode.lastChild)
    }
}

/**
 *
 * @param id 需要渲染的id
 * @param url 数据请求url
 * @param type 请求类型
 * @param listener 下拉框监听器函数
 * @param callback 渲染成功后的回调函数
 */
function renderMultSelect(id, url, name, type, listener, callback) {
    var $ = layui.jquery
    $.ajax({
        url: url,
        type: type || 'get',
        headers: {token_jwt: obtainJwt() || ''},
        success: function (data) {
            var pList = xmSelect.render({
                el: '#' + id,
                name: name,
                on: function (data) {
                    if (listener) {
                        listener(data)
                    }
                },
                toolbar: {
                    show: true,
                },
                paging: true,
                pageSize: 5,
                direction: 'down',
                data: data.data
            })
            if (callback) {
                callback(pList)
            }
        }
    })
}
