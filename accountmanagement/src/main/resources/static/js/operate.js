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
    openPage('../html/applicationcode/applicationcodeAdd.html', '新增')
}

function applicationcodeUpdateClicked(rowData) {
    if (selectOneRowDataVerify(rowData.data)) {
        openPage('../html/applicationcode/applicationcodeUpdate.html', '修改')
    }
}

function applicationcodeDeleteClicked(rowData) {
    if (selectOneRowDataVerify(rowData.data)) {
        var data = rowData.data[0]
        var url = '../api/applicationCode/delete/' + data.applicationName
        //删除应用编码后对应用编码下拉框进行刷新
        deleteOp(url, 'id_applicationCodeTable', function () {
            removeOptionExceptFirst('applicationCodeQueryForm_appName')
            handleAppNameSelect('applicationCodeQueryBlock', 'applicationCodeQueryForm_appName')
        })
    }
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
        var data = rowData.data[0]
        var url = '../api/applicationSequence/delete/' + data.applicationName + '/' + data.sequenceType
        deleteOp(url, 'id_applicationSequenceTable')
    }
}

/////////////////////////// permission.html /////////////////////////////////

function permissionAddClicked() {
    openPage('../html/permission/permissionAdd.html', '新增')
}
function permissionUpdateClicked(rowData) {
    if (selectOneRowDataVerify(rowData.data)) {
        openPage('../html/permission/permissionUpdate.html', '修改')
    }
}
function permissionDeleteClicked(rowData) {
    if (selectOneRowDataVerify(rowData.data)) {
        var data = rowData.data[0]
        var url = '../api/permission/delete/' + data.id
        deleteOp(url, 'id_permissionTable')
    }
}

///////////////////////// role.html //////////////////////////////////////
function roleAddClicked() {
    openPage('../html/role/roleAdd.html', '新增')
}

function roleUpdateClicked(rowData) {
    if (selectOneRowDataVerify(rowData.data)) {
        openPage('../html/role/roleUpdate.html', '修改')
    }
}

function roleDeleteClicked(rowData) {
    if (selectOneRowDataVerify(rowData.data)) {
        var data = rowData.data[0]
        var url = '../api/role/delete/' + data.id
        deleteOp(url, 'id_roleTable')
    }
}

