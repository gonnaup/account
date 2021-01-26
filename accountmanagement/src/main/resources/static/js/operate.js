/**
 * 增删改查操作js
 */
///////////////////// account.html /////////////////////////
//账号添加按钮点击
function accountAddClicked() {
    console.log("a")
}

//账号更新按钮点击
function accountUpdateClicked(rowData) {
    console.log("u")
}

//账号删除按钮点击
function accountDeleteClicked(rowData) {
    console.log("d")
}


///////////////////////////// applicationcode.html /////////////////////////////
function applicationcodeAddClicked() {
    console.log("a")
}

function applicationcodeUpdateClicked(rowData) {
    console.log("u")
}

function applicationcodeDeleteClicked(rowData) {
    console.log("d")
}


//////////////////////////// applicationsequence.html //////////////////
function applicationsequenceAddClicked() {
    openPage('../html/applicationsequence/applicationsequenceAdd.html', '新增')
}

function applicationsequenceUpdateClicked(rowData) {
    if (selectOneRowDataVerify(rowData.data)) {
        openPage('../html/applicationsequence/applicationsequenceUpdate.html', '修改')
    }
}

function applicationsequenceDeleteClicked(rowData) {
    if (selectOneRowDataVerify(rowData.data)) {
        var layer = layui.layer
        layer.confirm('是否删除数据？', {icon: 3, btn: ['确认', '取消']},
            function (index) {
                layer.close(index)//关闭layer
                var $ = layui.jquery
                var data = rowData.data[0]
                $.ajax({
                    url: '../api/applicationSequence/delete/' + data.applicationName + '/' + data.sequenceType,
                    type: 'delete',
                    headers: {token_jwt: obtainJwt()||''},
                    success: function (data) {
                        operateSuccessMsg()
                        layui.table.reload('id_applicationSequenceTable')
                    }
                })
            })
    }
}

