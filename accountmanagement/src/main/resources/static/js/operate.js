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
    openPage('../html/applicationsequence/applicationsequenceUpdate.html', '修改')
}

function applicationsequenceDeleteClicked(rowData) {

}

